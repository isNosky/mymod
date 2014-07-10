package com.weijia.mymod;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.GlobalVar;
import com.rqmod.provider.MyModApp;
import com.rqmod.provider.OrderDetail;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FillOrderActivity extends Activity {		

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		setReceiveInfo(data);
		//initReceiveInfo();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private GlobalVar app;
	private final static int DIALOG=1;
	
	DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	RelativeLayout rlReceiver = null;
	TextView tvReceiverName = null;
	TextView tvMobileContent = null;
	TextView tvAddr = null;
	Button btnSubmitOrder = null;
	TextView tvReceiverInfo = null;
	TextView tvReceiverTitle = null;
	ImageView ivReceiverIcon = null;
	ImageView ivIndexIcon = null;
	TabHost tbhost = null;
	TextView tvReturnCar = null;
	TextView tvFillOrderMoney = null;
	RelativeLayout rlInventory = null;
	RelativeLayout llSelShop = null;
	ArrayList<HashMap<String, Object>> lstShops = new ArrayList<HashMap<String, Object>>();
	
	int m_iStartA = -1;
	int m_iEndA = -1;
	int m_iStartB = -1;
	int m_iEndB = -1;
	
	TextView tvShopName = null;
	
	int id = -1;
	String nickname = ""; 
    String addr = "";
    String phonenumber1 = ""; 
    String phonenumber2 = ""; 
    
    int m_iShopId = -1;
    float totalprice = 0;
        
    JSONArray jaProduct = null;
    ArrayList<String> ll = new ArrayList<String>();
    
    private final static String TBL_SHOP = "tbl_shop";
    private final static String TBL_SHOP_DELIVERY_TIMES = "tbl_shop_delivery_times";
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = GlobalVar.getInstance();
		
		setContentView(R.layout.new_fill_order);
		//tbhost = ((TabActivity)getParent()).getTabHost();
		
		rlReceiver = (RelativeLayout)findViewById(R.id.layout_receiver_info);
		tvReceiverName = (TextView)findViewById(R.id.textview_receiver_name_content);
		tvMobileContent = (TextView)findViewById(R.id.textview_receiver_mobile_content);
		tvAddr = (TextView)findViewById(R.id.textview_receiver_address_content);
		tvReceiverInfo = (TextView)findViewById(R.id.textview_receiver_title);
		tvReturnCar = (TextView)findViewById(R.id.textview_link_return_cart);
		tvFillOrderMoney = (TextView)findViewById(R.id.fill_order_money);
		btnSubmitOrder = (Button)findViewById(R.id.submit_order);
		ivReceiverIcon = (ImageView)findViewById(R.id.imageview_receiver_icon);
		ivIndexIcon = (ImageView)findViewById(R.id.index_icon1);
		rlInventory = (RelativeLayout)findViewById(R.id.layout_product_inventory);
		
		llSelShop =  (RelativeLayout)findViewById(R.id.select_shop);
		
		tvReceiverInfo.setVisibility(View.VISIBLE);
		rlReceiver.setVisibility(View.VISIBLE);
		
		tvShopName = (TextView)findViewById(R.id.textview_select_shop_tips);
		
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		
		initReceiveInfo();
		setAddrLayout();
		setTotalPrice();
		
		
		btnSubmitOrder.setOnClickListener(new OnClickListener(){

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub			
				
				if(CheckLogined())
				{
					Intent intent = new Intent(FillOrderActivity.this,LoginActivity.class);
					startActivity(intent);
					return;
				}
				
//				if(CheckTime())
//				{
//					return;
//				}
				
				if(CheckReceiveInfo())
				{
					return;
				}
				
				if(CheckShop())
				{
					return;
				}
				
				getJSONProduct();				
				DoOrder();
				
			}});
		
		tvReturnCar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//tbhost.setCurrentTab(1);				
				FillOrderActivity.this.finish();
			}});
		
		rlInventory.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FillOrderActivity.this,GoodsInfoActivity.class);
				intent.putExtra("goodstype", "shopcar");
				startActivity(intent);
			}});
		
		llSelShop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DIALOG);
			}});
		
		getShops();
		
		GlobalVar.getInstance().saveActivity(this);
	}

	private int getCurTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
		String str = formatter.format(curDate);     
		
		return str2int(str);
	}
	
	private boolean CheckTime()
	{
		int iCurTime = getCurTime();
		if(( (iCurTime > m_iStartA) &&  (iCurTime < m_iEndA) )
				&& ((iCurTime > m_iStartB) &&  (iCurTime < m_iEndB)))
		{
			showDialog(getResources().getString(R.string.order_time_forbiden));
			return true;
		}
		return false;
	}
	
	@SuppressLint("NewApi")
	private boolean CheckReceiveInfo()
	{
		if(nickname.isEmpty() || addr.isEmpty() || phonenumber1.isEmpty())		
		{
			showDialog(getResources().getString(R.string.reveive_info_not_completed));
			return true;
		}
		return false;
	}
	
	private boolean CheckShop()
	{
		if(-1 == m_iShopId)
		{
			showDialog(getResources().getString(R.string.shop_not_selected));
			return true;
		}
		return false;
	}
	
	private boolean CheckLogined()
	{
		GlobalVar app = GlobalVar.getInstance();		
		if(-1 == app.getUserId())
		{
			showDialog(getResources().getString(R.string.login_first));
			
			return true;
		}
		return false;
	}
	
	private void DoOrder()
	{
		if(Constant.FLAG_POST_IN_JSON)
		{	
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		
	    			JSONObject jsoin = new JSONObject();
	    			JSONObject jsonout = null;
					try {
						jsoin.put("UserID", app.getUserId());						
						jsoin.put("ShopID", m_iShopId);	//当前写死，东仪路店
						jsoin.put("deliveryAddressID", id);
						jsoin.put("Amount", totalprice);						
						jsoin.put("ProductIDs", jaProduct);
						jsonout = HttpUtil.queryStringForPost(Constant.DOORDERSERVLET, jsoin);	
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.DOORDERSER_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;
			
		}
		else
		{
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		
		    		JSONObject jsoin = new JSONObject();
		    		JSONObject jsonout = null;
		    		
					try {
						HttpPost request = new HttpPost();
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			            postParameters.add(new BasicNameValuePair("UserID", String.valueOf(GlobalVar.getInstance().getUserId())));
			            //postParameters.add(new BasicNameValuePair("UserID", String.valueOf(-1)));
			            //postParameters.add(new BasicNameValuePair("Token", GlobalVar.getInstance().getToken()));
			            postParameters.add(new BasicNameValuePair("ShopID", String.valueOf(m_iShopId)));
			            postParameters.add(new BasicNameValuePair("deliveryAddressID", String.valueOf(id)));
			            postParameters.add(new BasicNameValuePair("Amount", String.valueOf(totalprice)));
			            postParameters.add(new BasicNameValuePair("ProductIDs", jaProduct.toString()));
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.DOORDERSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.DOORDERSER_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;
			
			    
		}				
	}
	
	private void getJSONProduct()
	{
		jaProduct = new JSONArray();
		
		Cursor c2 = db.rawQuery("select a.productid productid ,a.buycount buycount ,a.buycount*b.unitprice amount from tbl_shopcar a,tbl_product b where a.productid = b.id", null);
		
		while (c2.moveToNext()) {		
			 int iFoodId = c2.getInt(c2.getColumnIndex("productid"));
			 int iBuyCount = c2.getInt(c2.getColumnIndex("buycount"));
			 float fAmount = c2.getInt(c2.getColumnIndex("amount"));
			 JSONObject jsonObject = new JSONObject();
			 try {
				jsonObject.put("ProductId", iFoodId);
				jsonObject.put("Quantity", iBuyCount);
				jsonObject.put("Amount", fAmount);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 jaProduct.put(jsonObject);
		}  
		c2.close();
	}
	
	private void setTotalPrice()
	{
		Cursor c1 = db.rawQuery("select sum(buycount*unitprice) totalprice from tbl_shopcar", null);
		
		while (c1.moveToNext()) {			
			totalprice = c1.getFloat(c1.getColumnIndex("totalprice"));
		}  
		c1.close();
		
		String strOrg = tvFillOrderMoney.getText().toString();
		tvFillOrderMoney.setText(strOrg +":RMB"+totalprice);	
	}
	
	private void setAddrLayout()
	{
		rlReceiver.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FillOrderActivity.this,NewEasyBuyAddressListActivity.class);
				//Intent intent = new Intent(FillOrderActivity.this,NewAddrActivity.class);
				intent.setFlags(101);
				startActivityForResult(intent,2);
			}});
		
	}
	
	private void setReceiveInfo(Intent data)
	{
		if(null == data)
			return;
    	nickname = data.getStringExtra("textview_new_easy_buy_address_list_item_name");
    	phonenumber1 = data.getStringExtra("textview_new_easy_buy_address_list_item_phone");
    	addr = data.getStringExtra("textview_new_easy_buy_address_list_item_area")
    			+ data.getStringExtra("textview_new_easy_buy_address_list_item_street")
    			+data.getStringExtra("textview_new_easy_buy_address_list_item_addr");
    	tvReceiverName.setText(nickname);
		tvMobileContent.setText((!phonenumber1.isEmpty())?phonenumber1:phonenumber2);
		tvAddr.setText(addr);
	}
	
	@SuppressLint("NewApi")
	private void initReceiveInfo()
	{
		GlobalVar app = GlobalVar.getInstance(); 
		Cursor c = db.rawQuery("select id,name,phonenumber1,phonenumber2,addr from tbl_addr where isdefault = 1 and subscriberid = " + app.getUserId(), null);
		
		nickname = ""; 
		addr = "";
		phonenumber1 = "";
		phonenumber2 = "";
		while (c.moveToNext()) {			
		    id = c.getInt(c.getColumnIndex("id"));  
		    nickname = c.getString(c.getColumnIndex("name")); 
		    addr = c.getString(c.getColumnIndex("addr"));
		    phonenumber1 = c.getString(c.getColumnIndex("phonenumber1")); 
		    phonenumber2 = c.getString(c.getColumnIndex("phonenumber2")); 
		}  
		c.close();
		
		tvReceiverName.setText(nickname);
		tvMobileContent.setText((!phonenumber1.isEmpty())?phonenumber1:phonenumber2);
		tvAddr.setText(addr);
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog=null;
        switch (id) {
        case DIALOG:
            try {
				Builder builder=new android.app.AlertDialog.Builder(this);
				//设置对话框的图标
				//builder.setIcon(R.drawable.header);
				//设置对话框的标题
				builder.setTitle("选择配送门店");
				
				//添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
				//String[] items = (String[]) lstShops.toArray(new String[lstShops.size()]);
				
				int iCurTime = getCurTime();
				for(int i = 0 ; i < lstShops.size() ; i++)
				{
					HashMap<String, Object> map = lstShops.get(i);
					int iInterval = (Integer)map.get("servernowtime") - (Integer)map.get("preshopquerytime");
					
					if(( (iCurTime >= (Integer)map.get("nosalestarttime_a")) &&  (iCurTime <= (Integer)map.get("nosaleendtime_a")) )
							|| ((iCurTime >= (Integer)map.get("nosalestarttime_b")) &&  (iCurTime <= (Integer)map.get("nosaleendtime_b"))
							||(iInterval > 10)))
					{
						continue;
					}
					ll.add((String) map.get("shopname"));
				}
				String[] items = (String[]) ll.toArray(new String[ll.size()]);
				builder.setItems(items, new android.content.DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which) {
				    	m_iShopId = (Integer) lstShops.get(which).get("shopid");
				        String hoddy=(String) ll.get(which);
				        tvShopName.setText(tvShopName.getText().toString() + hoddy);
				    }
				});
				//创建一个列表对话框
				dialog=builder.create();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String str = e.getMessage();
				e.printStackTrace();
			}
            break;
        }
        return dialog;
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fill_order, menu);
		return true;
	}
	
	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void clearShopCar()
	{
		String strSQL = "delete from tbl_shopcar";
		db.execSQL(strSQL);
	}
	
	public void RedirectLogin()
    {
    	AlertDialog.Builder dialog=new AlertDialog.Builder(FillOrderActivity.this);
		dialog.setTitle(getResources().getString(R.string.token_invalid_login_tip))
			.setIcon(android.R.drawable.ic_dialog_info)
			.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(FillOrderActivity.this,LoginActivity.class);
					intent.setFlags(Constant.LOGIN_MSG);
					startActivityForResult(intent,Constant.LOGIN_MSG);
				}
		}).create().show();
    }
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case Constant.DOORDERSER_MSG:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (jsonout.has(Constant.ERRDESC)? jsonout.getString(Constant.ERRDESC):"");
					
					switch(iErrorCode)
					{
					case Constant.ERR_CODE_SUCCESS:
						//下订单成功
						showDialog(R.string.order_success_info);
						clearShopCar();
						FillOrderActivity.this.finish();
						Intent intent = new Intent(FillOrderActivity.this,MyOderListActivity.class);
						startActivity(intent);
						break;
					case Constant.ERR_CODE_TOKEN_INVALID:
						RedirectLogin();
						break;
					default:
						showDialog(strErrDesc);
						break;
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					String str = e.getMessage();
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            	
                break;

            case Constant.GETSHOP_MSG:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
										
					switch(iErrorCode)
					{
					case Constant.ERR_CODE_SUCCESS:
						JSONArray jsonShops = jsonout.getJSONArray("Shops");
						for(int shopid = 0 ; shopid < jsonShops.length() ; shopid++)
						{
							JSONObject jsonShop = (JSONObject) jsonShops.get(shopid);
							try {
								int iShopID = jsonShop.getInt("shopid");
				            	ContentValues values = new ContentValues();
								values.put("shopid", iShopID);
								String shopname = jsonShop.getString("shopname");
								values.put("shopname", shopname);
								JSONArray jaProducts = jsonShop.getJSONArray("productIDs");
								values.put("productids", jaProducts.toString());	
								if(-1 == db.insert(TBL_SHOP, null, values))
								{
									//
								}
								
//								ContentValues valuests = new ContentValues();
//								JSONArray jaDeliveryTimes = jsonShop.getJSONArray("deliveryTimes");
//								for(int jdtid = 0 ; jdtid < jaDeliveryTimes.length() ; jdtid++)
//								{
//									JSONObject ts = (JSONObject) jaDeliveryTimes.get(jdtid);
//									valuests.put("shopid", iShopID);
//									valuests.put("starttime", str2int(ts.getString("startTime")));
//									valuests.put("endtime", str2int(ts.getString("endTime")));
//									
//									if(-1 == db.insert(TBL_SHOP_DELIVERY_TIMES, null, values))
//									{
//										//
//									}
//								}	
								int iStartA = 0;
								int iEndA = 0;
								int iStartB = 0;
								int iEndB = 0;
								
								ContentValues valuests = new ContentValues();				
								if((jsonShop.has("nosalestarttime_a") && jsonShop.has("nosaleendtime_a"))
										||(jsonShop.isNull("nosalestarttime_a") || jsonShop.isNull("nosaleendtime_a")))
								{													
									valuests.put("shopid", iShopID);
									iStartA = str2int(jsonShop.getString("nosalestarttime_a"));
									iEndA = str2int(jsonShop.getString("nosaleendtime_a"));
									valuests.put("starttime", iStartA);
									valuests.put("endtime", iEndA);
									if(-1 == db.insert(TBL_SHOP_DELIVERY_TIMES, null, values))
									{
										//
									}
								}
								
								if((jsonShop.has("nosalestarttime_b") && jsonShop.has("nosaleendtime_b"))
									||(jsonShop.isNull("nosalestarttime_b") || jsonShop.isNull("nosaleendtime_b")))
								{
									valuests.clear();							
									valuests.put("shopid", iShopID);
									iStartB = str2int(jsonShop.getString("nosalestarttime_b"));
									iEndB = str2int(jsonShop.getString("nosaleendtime_b"));
									valuests.put("starttime", iStartB);
									valuests.put("endtime", iEndB);
									if(-1 == db.insert(TBL_SHOP_DELIVERY_TIMES, null, values))
									{
										//
									}
								}
								
								int preshopquerytime = 0;
								int servernowtime = 0;
								if((jsonShop.has("preshopquerytime") && jsonShop.has("servernowtime"))
										||(jsonShop.isNull("preshopquerytime") || jsonShop.isNull("servernowtime")))
									{
										preshopquerytime = str2int(jsonShop.getString("preshopquerytime"));
										servernowtime = str2int(jsonShop.getString("servernowtime"));
									}
								
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("shopid", iShopID);
								map.put("shopname", shopname);
								map.put("nosalestarttime_a", iStartA);
								map.put("nosaleendtime_a", iStartA);
								map.put("nosalestarttime_b", iStartB);
								map.put("nosaleendtime_b", iStartB);
								map.put("preshopquerytime", preshopquerytime);
								map.put("servernowtime", servernowtime);
								lstShops.add(map);
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								String str = e.getMessage();
								e.printStackTrace();
							}
						}
						break;
					case Constant.ERR_CODE_TOKEN_INVALID:
						RedirectLogin();
						break;
					default:
						showDialog(strErrDesc);
						break;
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					String str = e.getMessage();
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            	
                break;
            
            }
        }
    };
    
    private void getShops()
    {
    	if(Constant.FLAG_POST_IN_JSON)
		{	
			
		}
		else
		{
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		
		    		JSONObject jsoin = new JSONObject();
		    		JSONObject jsonout = null;
		    		
					try {
						HttpPost request = new HttpPost();
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			            //postParameters.add(new BasicNameValuePair("UserID", String.valueOf(app.getUserId())));
			            //postParameters.add(new BasicNameValuePair("ShopID", "6"));
			            postParameters.add(new BasicNameValuePair("Token", GlobalVar.getInstance().getToken()));
			            postParameters.add(new BasicNameValuePair("StartNum", "0"));
			            postParameters.add(new BasicNameValuePair("Count", "100"));
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.GETSHOPSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.GETSHOP_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;
			
			    
		}	
    }

    private int str2int(String strTime)
    {
    	String[] str = strTime.split(":");
    	int iTime = Integer.parseInt(str[0])*60 + Integer.parseInt(str[1]);
    	return iTime;
    }
}
