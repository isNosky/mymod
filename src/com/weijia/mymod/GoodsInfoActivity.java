package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;

import com.rqmod.provider.DatabaseManager;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class GoodsInfoActivity extends Activity {
	
	DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	
	ListView lvGoodsInfo = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fill_order_commodity);
		
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		
		lvGoodsInfo = (ListView)findViewById(R.id.fill_order_commodity_list);
		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		Cursor c = db.rawQuery("select a.picturepath,a.description,b.* from tbl_product a,tbl_shopcar b where a.id=b.productid", null);// WHERE age >= ?", new String[]{"33"}); 
		
		while (c.moveToNext()) {
		    int id = c.getInt(c.getColumnIndex("id"));  
		    String pname = c.getString(c.getColumnIndex("productname"));
		    String desc = c.getString(c.getColumnIndex("description"));
		    int type = c.getInt(c.getColumnIndex("productid"));
		    float price = c.getFloat(c.getColumnIndex("unitprice"));
		    int buycount = c.getInt(c.getColumnIndex("buycount"));
		    String strPicPath = c.getString(c.getColumnIndex("picturepath")); 
		    float unittotalprice = price*buycount;
		    
		    HashMap<String, Object> map = new HashMap<String, Object>();  
		    Bitmap bmp = null;
			try {
				bmp = BitmapFactory.decodeFile(strPicPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
			    //
				e.printStackTrace();
			}
			
			map.put("order_commodity_item_left_image", bmp);
		    map.put("order_commodity_item_name", pname);
		    map.put("order_commodity_item_desc", desc);
		    map.put("order_commodity_item_num", buycount);
		    map.put("order_commodity_item_jdprice", "RMB:"+ Float.toString(unittotalprice));  
		    listItem.add(map);
		}  
		c.close();
		
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(
				GoodsInfoActivity.this, 
				listItem, 
				R.layout.fill_order_commodity_item, 
				new String[] {"order_commodity_item_left_image", "order_commodity_item_name","order_commodity_item_desc","order_commodity_item_num", "order_commodity_item_jdprice"}, 
				new int [] {R.id.order_commodity_item_left_image,R.id.order_commodity_item_name,R.id.order_commodity_item_desc, R.id.order_commodity_item_num,R.id.order_commodity_item_jdprice});
		
		mSimpleAdapter.setViewBinder(new ViewBinder(){

			@Override
			public boolean setViewValue(View arg0, Object arg1, String arg2) {
				// TODO Auto-generated method stub
				if( (arg0 instanceof ImageView) & (arg1 instanceof Bitmap) ) {  
		            ImageView iv = (ImageView) arg0;  
		            Bitmap bm = (Bitmap) arg1;  
		            iv.setImageBitmap(bm);  
		            return true;  
		            }  
		        return false;
			}
		});
		
		lvGoodsInfo.setAdapter(mSimpleAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.goods_info, menu);
		return true;
	}

}
