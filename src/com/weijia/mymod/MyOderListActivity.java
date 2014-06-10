package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.ImageManager;
import com.rqmod.provider.MyModApp;
import com.rqmod.provider.OrderDetail;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class MyOderListActivity extends Activity {
	private Button oneMonthOrders;
    private Button preMonthOrders;
    private ListView mOneMonthList;
    private ListView mPreMonthList;
    
    DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	
	int Ids[] = {R.id.my_order_one_month_orders,R.id.my_order_pre_month_orders};
	
	private final static String TBL_ORDER = "tbl_order";
	private final static String TBL_ORDER_DETAIL = "tbl_order_detail";
	
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
//		oneMonthOrders.setSelected(true);
//		preMonthOrders.setSelected(false);		
		
		OnClickListener lsnr = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
					setOrderList(mOneMonthList);
					oneMonthOrders.setSelected(true);
					preMonthOrders.setSelected(false);	
				}
				if(R.id.my_order_pre_month_orders == arg0.getId())
				{
					setOrderList(mPreMonthList);	
					oneMonthOrders.setSelected(false);
					preMonthOrders.setSelected(true);	
				}
			}
		};
		
		oneMonthOrders.setOnClickListener(lsnr);
		preMonthOrders.setOnClickListener(lsnr);
		
		getOrders();
		
		oneMonthOrders.performClick();
	}
	
	private void getOrders()
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
						postParameters.add(new BasicNameValuePair("SubscriberID", "6"));
						//postParameters.add(new BasicNameValuePair("OrderID", "6"));
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.GETORDERSSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    		e.printStackTrace();
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
			    		//MyModApp app = (MyModApp)FillOrderActivity.this.getApplication();
						HttpPost request = new HttpPost();
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			            //postParameters.add(new BasicNameValuePair("CellphoneNumber", app.getCellphoneNumber()));  
						postParameters.add(new BasicNameValuePair("UserId", String.valueOf(orderId)));
						postParameters.add(new BasicNameValuePair("OrderId", String.valueOf(orderId)));
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.ORDERHISTORYSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    		e.printStackTrace();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = 2;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;  
          
		}
	}
	private void setOrderList(ListView lv)
	{
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		try {	
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
			    map.put("order_item_status", orderstatus);			    
			    
			    ArrayList<HashMap<String, Object>> listItem2 = new ArrayList<HashMap<String,Object>>();
			    Cursor c1 = db.rawQuery("SELECT foodid,quantity,amount FROM tbl_order_detail where orderid = " + id, null);// WHERE age >= ?", new String[]{"33"});  
			    while (c1.moveToNext()) {  
			    	
				    int foodid = c1.getInt(c1.getColumnIndex("foodid")) ;
				    int quantity = c1.getInt(c1.getColumnIndex("quantity"));
				    //float amount = c1.getFloat(c1.getColumnIndex("amount")); 
				    
				    Cursor c2 = db.rawQuery("SELECT * FROM tbl_product where id = " + foodid, null);
				    c2.moveToFirst();
				    String picptah = c2.getString(c2.getColumnIndex("picturepath"));
				    Bitmap bmp = ImageManager.getInstance(this).getBitmap(picptah);				    
				    String productname = c2.getString(c2.getColumnIndex("productname"));				    
				    
				    HashMap<String, Object> map2 = new HashMap<String, Object>();  
				    map2.put("product_list_item_image", bmp);
				    map2.put("order_product_item_name", productname);
				    
				    listItem2.add(map2);
				    
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
						values.put("orderid", jsonOrder.getInt("orderID"));
						values.put("totalprice", jsonOrder.getDouble("sumOfMoney"));
						values.put("createtime", jsonOrder.getString("createTime"));
						values.put("addrid", jsonOrder.getInt("orderFormAddressID"));
						values.put("deliveriername", jsonOrder.getString("orderDeliveryName"));
						values.put("deliveriertel", jsonOrder.getString("orderDeliveryTel"));
						values.put("orderstatus", jsonOrder.getString("orderCurrentStatus"));
						values.put("orderfromshopid", jsonOrder.getInt("orderFromShop"));
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
							
							if(-1 == db.insert(TBL_ORDER_DETAIL, null, values))
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleGetOrderHistoryMsg(Message msg)
	{
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
						values.put("orderid", jsonOrder.getInt("orderID"));
						values.put("totalprice", jsonOrder.getDouble("sumOfMoney"));
						values.put("createtime", jsonOrder.getString("createTime"));
						values.put("addrid", jsonOrder.getInt("orderFormAddressID"));
						values.put("deliveriername", jsonOrder.getString("orderDeliveryName"));
						values.put("deliveriertel", jsonOrder.getString("orderDeliveryTel"));
						values.put("orderstatus", jsonOrder.getString("orderCurrentStatus"));
						values.put("orderfromshopid", jsonOrder.getInt("orderFromShop"));
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
							
							if(-1 == db.insert(TBL_ORDER_DETAIL, null, values))
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case 1:
                //¹Ø±Õ
            	handleGetOrderMsg(msg);
            	break;
            case 2:
            	handleGetOrderHistoryMsg(msg);
                break;
            }
        }
    };
    
    private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("È·¶¨", new DialogInterface.OnClickListener() {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) { 
				convertView = mInflater.inflate(R.layout.order_list_item, null); 					
			}
			
			TextView tvText = (TextView)convertView.findViewById(R.id.order_item_Text); 
			TextView tvTotalPrice = (TextView)convertView.findViewById(R.id.order_item_totalPrice); 
			TextView tvCreateTime = (TextView)convertView.findViewById(R.id.order_item_subtime); 
			TextView tvStatus = (TextView)convertView.findViewById(R.id.order_item_status); 
			Button btnTrace = (Button)convertView.findViewById(R.id.order_item_track);
			
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
				Bitmap bmp = (Bitmap) map2.get("product_list_item_image");
				String name = (String) map2.get("order_product_item_name");
				iv.setImageBitmap(bmp);
				tv.setText(name);
			}
			else
			{
				for(int i = 0 ; i < listItem2.size() ; i++)
				{
					HashMap<String, Object> map2 = listItem2.get(i); 
					Bitmap bmp = (Bitmap) map2.get("product_list_item_image");
					String name = (String) map2.get("order_product_item_name");
					iv.setImageBitmap(bmp);
					iv.setId(i);
					if(0 == i)
					{
						RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(								
			                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);							
						llayout.addView(iv);
					}
					else
					{
						RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(								
			                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
							lp.addRule(RelativeLayout.RIGHT_OF,i-1);
						llayout.addView(iv);
					}
				}
			}
			
			btnTrace.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					int orderId = 0;
					getOrdersHistory(orderId);
				}});
			
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
}
