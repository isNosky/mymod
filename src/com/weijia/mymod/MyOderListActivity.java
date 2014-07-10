package com.weijia.mymod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.AsyncViewTask;
import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.GlobalVar;
import com.rqmod.provider.ImageManager;
import com.rqmod.provider.MyModApp;
import com.rqmod.provider.OrderDetail;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.SimpleAdapter.ViewBinder;

@SuppressLint("NewApi")
public class MyOderListActivity extends Activity {
	private Button oneMonthOrders;
    private Button preMonthOrders;
    private ListView mOneMonthList;
    private ListView mPreMonthList;
    private ListView m_CurrList;
    
    DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
	
	int Ids[] = {R.id.my_order_one_month_orders,R.id.my_order_pre_month_orders};
	
	private final static String TBL_ORDER = "tbl_order";
	private final static String TBL_ORDER_DETAIL = "tbl_order_detail";
	private final static String TBL_ORDER_HISTORY = "tbl_order_history";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order_list_activity);
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();	
					
		oneMonthOrders = (Button)findViewById(R.id.my_order_one_month_orders);
		preMonthOrders = (Button)findViewById(R.id.my_order_pre_month_orders);
		mOneMonthList = (ListView)findViewById(R.id.my_order_list_one_month);
		mPreMonthList = (ListView)findViewById(R.id.my_order_list_pre_month);
		
		OnClickListener lsnr = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Button btn = (Button)findViewById(arg0.getId());
				btn.setTextColor(getResources().getColor(R.color.green));
				for(int i = 0 ; i < Ids.length ; i++ )
				{
					if(arg0.getId() != Ids[i] )
					{
						btn.setTextColor(getResources().getColor(R.color.black));
					}
				}
				
				if(R.id.my_order_one_month_orders == arg0.getId())
				{
					m_CurrList = mOneMonthList;
					getOrders(R.id.my_order_one_month_orders);					
					oneMonthOrders.setSelected(true);
					preMonthOrders.setSelected(false);	
				}
				if(R.id.my_order_pre_month_orders == arg0.getId())
				{
					m_CurrList = mPreMonthList;
					getOrders(R.id.my_order_pre_month_orders);					
					oneMonthOrders.setSelected(false);
					preMonthOrders.setSelected(true);	
				}
			}
		};
		
		oneMonthOrders.setOnClickListener(lsnr);
		preMonthOrders.setOnClickListener(lsnr);
		
		
		GlobalVar.getInstance().saveActivity(this);
		oneMonthOrders.performClick();
	}
	
	private void getOrders(final int iType)
	{		
		String strSQL = "delete from tbl_order";
		db.execSQL(strSQL);
		
		strSQL = "delete from tbl_order_detail";
		db.execSQL(strSQL);
		
		if(Constant.FLAG_POST_IN_JSON)
		{
			
		}
		else
		{            
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		JSONObject jsonout = null;
			    	try {
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			            //postParameters.add(new BasicNameValuePair("CellphoneNumber", app.getCellphoneNumber()));   
						postParameters.add(new BasicNameValuePair("SubscriberID", String.valueOf(GlobalVar.getInstance().getUserId())));
						postParameters.add(new BasicNameValuePair("Token", GlobalVar.getInstance().getToken()));
						//postParameters.add(new BasicNameValuePair("OrderID", "6"));
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");     
						Date curDate = new Date(System.currentTimeMillis());//获取当前时间  
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(curDate);
						
						
			            if(R.id.my_order_one_month_orders == iType)
			            {
			            	String str = formatter.format(curDate);  
			            	calendar.add(Calendar.MONTH, -1);
			            	Date monthago = calendar.getTime();
			            	String str2 = formatter.format(monthago);  
			            	postParameters.add(new BasicNameValuePair("start_date", str));
			            	postParameters.add(new BasicNameValuePair("end_date", str2));
			            }
			            else if(R.id.my_order_pre_month_orders == iType)
			            {
			            	String str = formatter.format(curDate);  
			            	calendar.add(Calendar.MONTH, -1);
			            	Date monthago = calendar.getTime();
			            	String str2 = formatter.format(monthago);  
			            	
			            	calendar.add(Calendar.MONTH, -4);
			            	Date monthsago = calendar.getTime();
			            	String str3 = formatter.format(monthsago);  
			            	
			            	postParameters.add(new BasicNameValuePair("start_date", str2));
			            	postParameters.add(new BasicNameValuePair("end_date", str3));			            	
			            }
			            jsonout = HttpUtil.queryStringForPost(Constant.GETORDERSSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    		e.printStackTrace();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.GETORDERS_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;  
          
		}
	}
	
	private void getOrdersHistory(final int orderId)
	{		
		if(Constant.FLAG_POST_IN_JSON)
		{
			
		}
		else
		{            
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		JSONObject jsonout = null;
			    	try { 					
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			            //postParameters.add(new BasicNameValuePair("CellphoneNumber", app.getCellphoneNumber()));  
						postParameters.add(new BasicNameValuePair("UserId", String.valueOf(GlobalVar.getInstance().getUserId())));
						postParameters.add(new BasicNameValuePair("OrderId", String.valueOf(orderId)));
						postParameters.add(new BasicNameValuePair("Token", GlobalVar.getInstance().getToken()));
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.ORDERHISTORYSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    		e.printStackTrace();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.ORDERHISTORY_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;  
          
		}
	}
	
	private String getOrderStatus(String orderstatus)
	{
		try {
			String [] orderstatusarray = getResources().getStringArray(R.array.order_status);
			return orderstatusarray[Integer.parseInt(orderstatus)];
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderstatus;
	}
	
	private void setOrderList(ListView lv)
	{
		
		try {
			listItem.clear();
			Cursor c = db.rawQuery("SELECT * FROM tbl_order", null);// WHERE age >= ?", new String[]{"33"});  
			while (c.moveToNext()) {  
			    int id = c.getInt(c.getColumnIndex("orderid"));  
			    float totalprice = c.getFloat(c.getColumnIndex("totalprice")) ;
			    String createtime = c.getString(c.getColumnIndex("createtime"));
			    String orderstatus = c.getString(c.getColumnIndex("orderstatus"));
			    
			    HashMap<String, Object> map = new HashMap<String, Object>();  
				map.put("order_item_Text", id);
			    map.put("order_item_totalPrice", totalprice);
			    map.put("order_item_subtime", createtime);
			    map.put("order_item_status", getOrderStatus(orderstatus));			    
			    
			    ArrayList<HashMap<String, Object>> listItem2 = new ArrayList<HashMap<String,Object>>();
			    Cursor c1 = db.rawQuery("SELECT * FROM tbl_order_detail where orderid = " + id, null);// WHERE age >= ?", new String[]{"33"});  
			    while (c1.moveToNext()) {  
			    	
				    int foodid = c1.getInt(c1.getColumnIndex("foodid")) ;
				    int quantity = c1.getInt(c1.getColumnIndex("quantity"));
				    //float amount = c1.getFloat(c1.getColumnIndex("amount")); 
				    
				    Cursor c2 = db.rawQuery("SELECT * FROM tbl_product where id = " + foodid, null);
				    if(c2.moveToNext())
				    {
				    	String picptah = c2.getString(c2.getColumnIndex("picturepath"));
				    
					    //Bitmap bmp = ImageManager.getInstance(this).getBitmap(picptah);				    
					    String productname = c2.getString(c2.getColumnIndex("productname"));				    
					    
					    HashMap<String, Object> map2 = new HashMap<String, Object>();  
					    map2.put("product_list_item_image", picptah);
					    map2.put("order_product_item_name", productname);
					    
					    listItem2.add(map2);
				    }
				    c2.close();
			    }	
			    map.put("product_list_item", listItem2);		
				c1.close();
				
				listItem.add(map);
			}  
			c.close();

			OrderAdpatorView mSimpleAdapter = new OrderAdpatorView(
					MyOderListActivity.this, 
					listItem, 
					R.layout.order_list_item, 
					new String[] {"order_item_Text", "order_item_totalPrice","order_item_subtime","order_item_status"}, 
					new int [] {R.id.order_item_Text,R.id.order_item_totalPrice,R.id.order_item_subtime, R.id.order_item_status});
			
			lv.setAdapter(mSimpleAdapter);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
	
	private void handleGetOrderMsg(Message msg)
	{
		String strSQL = "delete from tbl_order";
		db.execSQL(strSQL);
		strSQL = "delete from tbl_order_detail";
		db.execSQL(strSQL);
		
		try {
    		JSONObject jsonout = (JSONObject) msg.obj;
			int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
			String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
			
			JSONArray jsonOrders = jsonout.getJSONArray("Orders");			
			boolean bHasOrder = false;
			for(int jsorder = 0 ; jsorder < jsonOrders.length() ; jsorder++)
			{
				JSONObject jsonOrder = (JSONObject) jsonOrders.get(jsorder);
				if(Constant.ERR_CODE_SUCCESS == iErrorCode)
				{
					//showDialog(getResources().getString(R.string.upomp_bypay_registeractivity_succeed));
					//RegisterActivity.this.finalize();
					
					try {
		            	ContentValues values = new ContentValues();
						values.put("orderid", jsonOrder.getInt("orderID"));
						boolean b = jsonOrder.isNull("sumOfMoney") || !jsonOrder.has("sumOfMoney") || jsonOrder.get("sumOfMoney").toString().isEmpty();
						values.put("totalprice", b?"0.0":String.valueOf(jsonOrder.getDouble("sumOfMoney")));
						values.put("createtime", jsonOrder.getString("createTime"));
						values.put("addrid", jsonOrder.getInt("orderFormAddressID"));
						values.put("deliveriername", jsonOrder.getString("orderDeliveryName"));
						values.put("deliveriertel", jsonOrder.getString("orderDeliveryTel"));
						String order_item_status = jsonOrder.getString("orderCurrentStatus");
						values.put("orderstatus", order_item_status);
						values.put("orderfromshopid", jsonOrder.getInt("orderFromShop"));
						if((!order_item_status.equalsIgnoreCase("完成"))
								&& (!order_item_status.equalsIgnoreCase("客户取消"))
								&& (!order_item_status.equalsIgnoreCase("店长拒绝")))
						{
							bHasOrder = true;
						}
						
						if(-1 == db.insert(TBL_ORDER, null, values))
						{
							//
						}
						
						ContentValues valuesdetail = new ContentValues();
						JSONArray jsa = jsonOrder.getJSONArray("orderDetails");
						for(int i = 0 ; i < jsa.length() ; i++)
						{
							JSONObject od = (JSONObject) jsa.get(i);
							valuesdetail.put("orderid", od.getString("orderID"));
							valuesdetail.put("foodid", od.getInt("productID"));
							valuesdetail.put("quantity", od.getInt("quantity"));
							valuesdetail.put("amount", od.getInt("amount"));
							
							if(-1 == db.insertOrThrow(TBL_ORDER_DETAIL, null, valuesdetail))
							{
								//
							}
						}			
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						String str = e.getMessage();
						e.printStackTrace();
					}
				
				}
				else
				{
					showDialog(strErrDesc);
				}
			}
			
			if(bHasOrder)
			{
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
				   @Override
				   public void run() {
					   Message message= handler.obtainMessage() ; 
					   message.what = Constant.TIMER_MSG;
					   handler.sendMessage(message); 
				   }
				  };
				timer.schedule(task, 1000 * 10); //10秒后
			}
		} catch (JSONException e) {
			String str = e.getMessage();
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		setOrderList(m_CurrList);
	}
	
	private void handleGetOrderHistoryMsg(Message msg)
	{
		
		ArrayList<ContentValues> listItem = new ArrayList<ContentValues>();
		try {
    		JSONObject jsonout = (JSONObject) msg.obj;
			int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
			String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
			
			JSONArray jsonOrders = jsonout.getJSONArray("Orders");
			
			
			for(int jsorder = 0 ; jsorder < jsonOrders.length() ; jsorder++)
			{
				JSONObject jsonOrder = (JSONObject) jsonOrders.get(jsorder);
				if(Constant.ERR_CODE_SUCCESS == iErrorCode)
				{
					//showDialog(getResources().getString(R.string.upomp_bypay_registeractivity_succeed));
					//RegisterActivity.this.finalize();
					
					try {
						
		            	ContentValues values = new ContentValues();
		            	int orderid = jsonOrder.getInt("orderID");
		            	values.put("orderid", orderid);
		            	String orderstatus = String.valueOf(jsonOrder.getInt("orderStatus"));
						values.put("orderstatus", orderstatus);
						String comment = jsonOrder.getString("comment");
						values.put("comment", comment);
						String optime = jsonOrder.getString("opTime");

						values.put("optime", optime);

						listItem.add(values);
						
						if(-1 == db.insert(TBL_ORDER_HISTORY, null, values))
						{
							//
						}
//						
//						ContentValues valuesdetail = new ContentValues();
//						JSONArray jsa = jsonOrder.getJSONArray("orderDetails");
//						for(int i = 0 ; i < jsa.length() ; i++)
//						{
//							JSONObject od = (JSONObject) jsa.get(i);
//							valuesdetail.put("orderid", od.getString("orderID"));
//							valuesdetail.put("foodid", od.getInt("productID"));
//							valuesdetail.put("quantity", od.getInt("quantity"));
//							valuesdetail.put("amount", od.getInt("amount"));
//							
//							if(-1 == db.insert(TBL_ORDER_DETAIL, null, values))
//							{
//								//
//							}
//						}			
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						String str = e.getMessage();
						e.printStackTrace();
					}
				
				}
				else
				{
					showDialog(strErrDesc);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("OrderHistoryList", (ArrayList<? extends Parcelable>) listItem);
		Intent intent = new Intent(MyOderListActivity.this,OrderHistoryActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	private void handleCancelOrderMsg(Message msg)
	{
		
		ArrayList<ContentValues> listItem = new ArrayList<ContentValues>();
		try {
    		JSONObject jsonout = (JSONObject) msg.obj;
			int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
			String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
			
			if(Constant.ERR_CODE_SUCCESS == iErrorCode)
			{
				showDialog(getResources().getString(R.string.cancel_order_success_info));
				MyOderListActivity.this.finish();
			}
			else
			{
				showDialog(strErrDesc);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getOrders(R.id.my_order_one_month_orders);
		setOrderList(m_CurrList);
	}
	
	public void RedirectLogin()
    {
    	AlertDialog.Builder dialog=new AlertDialog.Builder(MyOderListActivity.this);
		dialog.setTitle(getResources().getString(R.string.token_invalid_login_tip))
			.setIcon(android.R.drawable.ic_dialog_info)
			.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(MyOderListActivity.this,LoginActivity.class);
					intent.setFlags(Constant.LOGIN_MSG);
					startActivityForResult(intent,Constant.LOGIN_MSG);
				}
		}).create().show();
    }
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
        	
        	JSONObject jsonout = (JSONObject) msg.obj;
			try {
				int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
				
				switch(iErrorCode)
				{
				case Constant.ERR_CODE_SUCCESS:
					break;
				case Constant.ERR_CODE_TOKEN_INVALID:
					RedirectLogin();
					break;
				default:
					break;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            switch(msg.what){
            case Constant.GETORDERS_MSG:
            	handleGetOrderMsg(msg);
            	break;
            case Constant.ORDERHISTORY_MSG:
            	handleGetOrderHistoryMsg(msg);
                break;                
            case Constant.CANCELORDERS_MSG:
            	handleCancelOrderMsg(msg);
                break;
            case Constant.TIMER_MSG:
            	oneMonthOrders.performClick();
            	break;
            }
        }
    };
    
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
    
    class OrderAdpatorView extends SimpleAdapter
    {
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		private LayoutInflater mInflater;
    	List<? extends Map<String, ?>> mData;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) { 
				convertView = mInflater.inflate(R.layout.order_list_item, null); 					
			}
			
			TextView tvText = (TextView)convertView.findViewById(R.id.order_item_Text); 
			TextView tvTotalPrice = (TextView)convertView.findViewById(R.id.order_item_totalPrice); 
			TextView tvCreateTime = (TextView)convertView.findViewById(R.id.order_item_subtime); 
			TextView tvStatus = (TextView)convertView.findViewById(R.id.order_item_status); 
			Button btnTrace = (Button)convertView.findViewById(R.id.order_item_track);
			Button btnCancel = (Button)convertView.findViewById(R.id.cancel_order_button);
			
			RelativeLayout llayout = (RelativeLayout)convertView.findViewById(R.id.product_gallery_order_layout);
			ImageView iv = (ImageView)llayout.findViewById(R.id.product_list_item_image);
			TextView tv = (TextView)llayout.findViewById(R.id.order_product_item_name);
			
			HashMap<String, Object> map = (HashMap<String, Object>) mData.get(position);  
			int order_item_Text = (Integer) map.get("order_item_Text");
			tvText.setText(String.valueOf(order_item_Text));
			float order_item_totalPrice = (Float) map.get("order_item_totalPrice");
			tvTotalPrice.setText(String.valueOf(order_item_totalPrice));
			String order_item_subtime = (String) map.get("order_item_subtime");
			tvCreateTime.setText(order_item_subtime);
			String order_item_status = (String) map.get("order_item_status");
			tvStatus.setText(order_item_status);
			
			ArrayList<HashMap<String, Object>> listItem2 = (ArrayList<HashMap<String, Object>>) map.get("product_list_item");
			
			if(0 == listItem2.size())
			{
				
			}
			else if(1 == listItem2.size())
			{
				HashMap<String, Object> map2 = listItem2.get(0); 
				//Bitmap bmp = (Bitmap) map2.get("product_list_item_image");
				String pic = (String) map2.get("product_list_item_image");
				iv.setTag(pic);
				new AsyncViewTask().execute(iv);
				String name = (String) map2.get("order_product_item_name");
				//iv.setImageBitmap(bmp);
				tv.setText(name);
			}
			else
			{
				for(int i = 0 ; i < listItem2.size() ; i++)
				{
					HashMap<String, Object> map2 = listItem2.get(i); 
					//Bitmap bmp = (Bitmap) map2.get("product_list_item_image");
					String pic = (String) map2.get("product_list_item_image");
					ImageView iivv = null;
					try {
						iivv = new ImageView(MyOderListActivity.this);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					iivv.setTag(pic);
					new AsyncViewTask().execute(iivv);
					String name = (String) map2.get("order_product_item_name");
					//iv.setImageBitmap(bmp);
					iivv.setId(i+4096);
					iivv.setPadding(2, 1, 2, 1);
					try {
						if(0 == i)
						{
							RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(								
						            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);							
							llayout.addView(iivv,lp);
						}
						else
						{
							RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(								
						            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
								lp.addRule(RelativeLayout.RIGHT_OF,i+4095);
								//lp.addRule(RelativeLayout.ALIGN_RIGHT
							llayout.addView(iivv,lp);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			lvButtonListener lbl = new lvButtonListener(position);
			
			btnTrace.setOnClickListener(lbl);
			
			if(!order_item_status.equalsIgnoreCase("提交"))
			{
				btnCancel.setVisibility(Button.INVISIBLE);
			}
			
			btnCancel.setOnClickListener(lbl);
			
			llayout.setOnClickListener(lbl);
			
			return convertView;
		}

		public OrderAdpatorView(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
			this.mInflater = LayoutInflater.from(context); 
			mData = data;
		}
    	
    }
    
    class lvButtonListener implements OnClickListener { 
        private int position ; 

        lvButtonListener( int pos) { 
            position = pos; 
        } 
        
        @Override 
        public void onClick( View v) { 
            int vid= v.getId(); 
            HashMap<String, Object> map = listItem.get(position);
            int orderId = (Integer) map.get("order_item_Text");
            
            switch(vid)
            {
            case R.id.order_item_track:
            	getOrdersHistory(orderId);
            	break;
            case R.id.cancel_order_button:
            	cancelOrder(orderId);
            	break;
            case R.id.product_gallery_order_layout:
            	Intent intent = new Intent(MyOderListActivity.this,GoodsInfoActivity.class);
            	intent.putExtra("goodstype", "order");
				intent.putExtra("tbl_order_detail", orderId);
				startActivity(intent);
				break;
            }
        } 
        
        
    } 
    
    private void cancelOrder(final int orderId)
    {
    	if(Constant.FLAG_POST_IN_JSON)
		{
			
		}
		else
		{            
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		JSONObject jsonout = null;
			    	try { 							    		
			    		//MyModApp app = (MyModApp)FillOrderActivity.this.getApplication();
						HttpPost request = new HttpPost();
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			            //postParameters.add(new BasicNameValuePair("CellphoneNumber", app.getCellphoneNumber()));  
						postParameters.add(new BasicNameValuePair("UserId", String.valueOf(GlobalVar.getInstance().getUserId())));
						postParameters.add(new BasicNameValuePair("OrderId", String.valueOf(orderId)));
						postParameters.add(new BasicNameValuePair("Token", GlobalVar.getInstance().getToken()));
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.CANCELORDERSSERVLET , postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    		e.printStackTrace();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.CANCELORDERS_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;  
          
		}
    }
}
