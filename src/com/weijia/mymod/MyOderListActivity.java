package com.weijia.mymod;

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
import com.rqmod.provider.ImageManager;
import com.rqmod.provider.MyModApp;
import com.rqmod.provider.OrderDetail;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
		oneMonthOrders.setSelected(true);
		preMonthOrders.setSelected(false);		
		
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
					if(R.id.my_order_one_month_orders == arg0.getId())
					{
						setOrderList(mOneMonthList);						
					}
					if(R.id.my_order_pre_month_orders == arg0.getId())
					{
						setOrderList(mPreMonthList);						
					}
				}
			}
		};
	}
	
	private void getOrders()
	{
		JSONObject jsonout = null;
		if(Constant.FLAG_POST_IN_JSON)
		{
			
		}
		else
		{
			MyModApp app = (MyModApp)getApplication();
			HttpPost request = new HttpPost();
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("CellphoneNumber", app.getCellphoneNumber()));           
            
            jsonout = HttpUtil.queryStringForPost(Constant.DOORDERSERVLET, postParameters);
            
            
            try {
            	ContentValues values = new ContentValues();
				values.put("orderid", jsonout.getString("OrderID"));
				values.put("totalprice", jsonout.getDouble("SumOfMoney"));
				values.put("createtime", jsonout.getString("CreateTime"));
				values.put("addrid", jsonout.getInt("OrderFormAddressID"));
				values.put("deliveriername", jsonout.getString("OrderDeliveryName"));
				values.put("deliveriertel", jsonout.getString("OrderDeliveryTel"));
				values.put("orderstatus", jsonout.getString("OrderCurrentStatus"));
				values.put("orderfromshopid", jsonout.getInt("OrderFromShop"));
				if(-1 == db.insert(TBL_ORDER, null, values))
				{
					//
				}
				
				ContentValues valuesdetail = new ContentValues();
				JSONArray jsa = jsonout.getJSONArray("OrderDetailList");
				for(int i = 0 ; i < jsa.length() ; i++)
				{
					OrderDetail od = (OrderDetail) jsa.get(i);
					valuesdetail.put("orderid", jsonout.getString("OrderID"));
					valuesdetail.put("foodid", jsonout.getDouble("FoodID"));
					valuesdetail.put("quantity", jsonout.getString("Quantity"));
					valuesdetail.put("amount", jsonout.getInt("Amount"));
					
					if(-1 == db.insert(TBL_ORDER_DETAIL, null, values))
					{
						//
					}
				}			
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
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
			    listItem.add(map);
			}  
			c.close();

			SimpleAdapter mSimpleAdapter = new SimpleAdapter(
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
}
