package com.weijia.mymod;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.MyModApp;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
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
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class FillOrderActivity extends Activity {

	private MyModApp app;
	
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
	
	int id = 0;
	String nickname; 
    String addr;
    String phonenumber1; 
    String phonenumber2; 
    float totalprice = 0;
        
    JSONArray jaFood = new JSONArray();
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (MyModApp) getApplication();
		
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
		
		tvReceiverInfo.setVisibility(View.VISIBLE);
		rlReceiver.setVisibility(View.VISIBLE);
		
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		
		Cursor c = db.rawQuery("select a.id,b.nickname,a.phonenumber1,a.phonenumber2,a.addr from tbl_addr a,tbl_user b where a.isdefault = 1 and a.subscriberid = b.id", null);
		
		while (c.moveToNext()) {			
		    id = c.getInt(c.getColumnIndex("id"));  
		    nickname = c.getString(c.getColumnIndex("nickname")); 
		    addr = c.getString(c.getColumnIndex("addr"));
		    phonenumber1 = c.getString(c.getColumnIndex("phonenumber1")); 
		    phonenumber2 = c.getString(c.getColumnIndex("phonenumber2")); 
		}  
		c.close();
		
		tvReceiverName.setText(nickname);
		tvMobileContent.setText((!phonenumber1.isEmpty())?phonenumber1:phonenumber2);
		tvAddr.setText(addr);
		
		Cursor c1 = db.rawQuery("select sum(buycount*unitprice) totalprice from tbl_shopcar", null);
		
		while (c1.moveToNext()) {			
			totalprice = c1.getFloat(c1.getColumnIndex("totalprice"));
		}  
		c1.close();
		
		String strOrg = tvFillOrderMoney.getText().toString();
		tvFillOrderMoney.setText(strOrg +":RMB"+totalprice);	
		
		btnSubmitOrder.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub			
				
				Cursor c2 = db.rawQuery("select productid,buycount from tbl_shopcar", null);
				
				while (c2.moveToNext()) {		
					 int iFoodId = c2.getInt(c2.getColumnIndex("productid"));
					 int iBuyCount = c2.getInt(c2.getColumnIndex("buycount"));
					 JSONObject jsonObject = new JSONObject();
					 try {
						jsonObject.put("FoodId", iFoodId);
						jsonObject.put("Quantity", iBuyCount);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
					 jaFood.put(jsonObject);
				}  
				c2.close();
				if(Constant.FLAG_POST_IN_JSON)
				{	
					Thread thread = new Thread(){ 
				    	@Override 
					    public void run() { 	
				    		
			    			JSONObject jsoin = new JSONObject();
			    			JSONObject jsonout = null;
							try {
								jsoin.put("UserID", app.getUserId());						
								jsoin.put("ShopID", 1);	//当前写死，东仪路店
								jsoin.put("deliveryAddressID", id);
								jsoin.put("Amount", totalprice);						
								jsoin.put("FoodList", jaFood);
								jsonout = HttpUtil.queryStringForPost(Constant.DOORDERSERVLET, jsoin);	
					    	} catch (Exception e) { 
					    		String str = e.getMessage();
					    	} 
					    	  
					    	Message message= handler.obtainMessage() ; 
					    	message.obj = jsonout; 
					    	message.what = 1;
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
					            postParameters.add(new BasicNameValuePair("UserID", String.valueOf(app.getUserId())));
					            postParameters.add(new BasicNameValuePair("ShopID", String.valueOf(1)));
					            postParameters.add(new BasicNameValuePair("deliveryAddressID", String.valueOf(id)));
					            postParameters.add(new BasicNameValuePair("Amount", String.valueOf(totalprice)));
					            postParameters.add(new BasicNameValuePair("FoodList", jaFood.toString()));
					            
					            jsonout = HttpUtil.queryStringForPost(Constant.DOORDERSERVLET, postParameters);
					    	} catch (Exception e) { 
					    		String str = e.getMessage();
					    	} 
					    	  
					    	Message message= handler.obtainMessage() ; 
					    	message.obj = jsonout; 
					    	message.what = 1;
					    	handler.sendMessage(message); 
					    	} 
				    	}; 
				    	thread.start(); 
				    	thread = null;
					
				}				
				
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
				startActivity(intent);
			}});
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
	
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case 1:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
					
					if(Constant.ERR_CODE_SUCCESS == iErrorCode)
					{
						//下订单成功
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
                break;
            }
        }
    };

}
