package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;

import com.rqmod.provider.ImageManager;
import com.rqmod.util.Constant;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class MyOderListActivity extends Activity {
	private Button oneMonthOrders;
    private Button preMonthOrders;
    private ListView mOneMonthList;
    private ListView mPreMonthList;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		try {
			
			setContentView(R.layout.my_order_list_activity);
			
			oneMonthOrders = (Button)findViewById(R.id.my_order_one_month_orders);
			preMonthOrders = (Button)findViewById(R.id.my_order_pre_month_orders);
			mOneMonthList = (ListView)findViewById(R.id.my_order_list_one_month);
			mPreMonthList = (ListView)findViewById(R.id.my_order_list_pre_month);
			oneMonthOrders.setSelected(true);
			preMonthOrders.setSelected(false);		
						
//			ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
//			Cursor c = db.rawQuery("SELECT * FROM tbl_product", null);// WHERE age >= ?", new String[]{"33"});  
//			while (c.moveToNext()) {  
//			    int id = c.getInt(c.getColumnIndex("id"));  
//			    String pname = c.getString(c.getColumnIndex("productname"));  
//			    int type = c.getInt(c.getColumnIndex("type"));
//			    String desc = c.getString(c.getColumnIndex("description"));
//			    String picptah = c.getString(c.getColumnIndex("picturepath"));
//			    int isinsale = c.getInt(c.getColumnIndex("isinsale"));
//			    float price = c.getFloat(c.getColumnIndex("unitprice"));
//			    HashMap<String, Object> map = new HashMap<String, Object>();  
//			    Bitmap bmp = ImageManager.getInstance(this).getBitmap(picptah);
//				map.put("product_item_image", bmp);
//			    map.put("product_item_name", pname);
//			    map.put("product_item_id", "±àºÅ:"+String.valueOf(id));
//			    map.put("product_item_adword", desc);
//			    map.put("product_item_martPrice", "RMB:"+ Float.toString(price));  
//			    listItem.add(map);
//			}  
//			c.close();
//
//			SimpleAdapter mSimpleAdapter = new SimpleAdapter(
//					MyOderListActivity.this, 
//					listItem, 
//					R.layout.product_list_item, 
//					new String[] {"product_item_image", "product_item_name","product_item_id","product_item_adword", "product_item_martPrice"}, 
//					new int [] {R.id.product_item_image,R.id.product_item_name,R.id.product_item_id, R.id.product_item_adword,R.id.product_item_martPrice});
//			
//			mSimpleAdapter.setViewBinder(new ViewBinder(){
//
//				@Override
//				public boolean setViewValue(View arg0, Object arg1, String arg2) {
//					// TODO Auto-generated method stub
//					if( (arg0 instanceof ImageView) & (arg1 instanceof Bitmap) ) {  
//			            ImageView iv = (ImageView) arg0;  
//			            Bitmap bm = (Bitmap) arg1;  
//			            iv.setImageBitmap(bm);  
//			            return true;  
//			            }  
//			        return false;
//				}
//				
//			});
//			lvMenu.setAdapter(mSimpleAdapter);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
	
	private void getOrders()
	{
		if(Constant.FLAG_POST_IN_JSON)
		{
			
		}
		else
		{
			
		}
	}
}
