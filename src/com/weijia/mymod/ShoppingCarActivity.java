package com.weijia.mymod;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.DbUlity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.SimpleAdapter.ViewBinder;
import android.text.Layout;
import android.util.Log;

public class ShoppingCarActivity extends Activity {
	
	DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	DbUlity dbu = null;
	Button btnOrder = null;
	TabHost tbhost = null;
	ListView lvGood = null;
	Button btnDec = null;
	Button btnInc = null;
	boolean bHasGood = false;
	float fTotalPrice = 0;
	RelativeLayout loPrice = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.shopping_cart_layout);
			
			dbm = DatabaseManager.getInstance(this);
			db = dbm.openDatabase();
			
			lvGood = (ListView)findViewById(R.id.shopping_cart_list);
			btnOrder = (Button)findViewById(R.id.cart_no_data_forward_cuxiao);
			View vNodata = (View)findViewById(R.id.shopping_cart_no_data);
			View vPrice = (View)findViewById(R.id.shopping_cart_count_price_layout);
			loPrice = (RelativeLayout)findViewById(R.id.shoping_cart_user_no_login_tips);
			btnDec = (Button)findViewById(R.id.cart_single_product_num_reduce);
			btnInc = (Button)findViewById(R.id.cart_single_product_num_add);
			tbhost = ((TabActivity)getParent()).getTabHost();
			
			ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
			try {
				Cursor count = db.rawQuery("select count(*) goodscount from tbl_shopcar", null);
				count.moveToFirst();
				int iColIdx = count.getColumnIndex("goodscount");
				int goodscount = count.getInt(iColIdx);
				count.close();
				if(0 == goodscount)
				{
					vPrice.setVisibility(8);
//					loPrice.setVisibility(8);
					lvGood.setVisibility(8);
					btnOrder.setOnClickListener(new View.OnClickListener(){

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							tbhost.setCurrentTab(0);
						}
						
					});
				}
				else
				{
					Cursor c = db.rawQuery("select a.picturepath,b.* from tbl_product a,tbl_shopcar b where a.id=b.productid", null);// WHERE age >= ?", new String[]{"33"}); 
					
					while (c.moveToNext()) {
						bHasGood = true;
					    int id = c.getInt(c.getColumnIndex("id"));  
					    String pname = c.getString(c.getColumnIndex("productname"));  
					    int type = c.getInt(c.getColumnIndex("productid"));
					    float price = c.getFloat(c.getColumnIndex("unitprice"));
					    int buycount = c.getInt(c.getColumnIndex("buycount"));
					    String strPicPath = c.getString(c.getColumnIndex("picturepath")); 
					    float unittotalprice = price*buycount;
					    
					    HashMap<String, Object> map = new HashMap<String, Object>();  
					    Bitmap bmp = getBitmapFromFile(strPicPath);
						map.put("cart_single_product_image", bmp);
					    map.put("cart_single_product_name", pname);
					    map.put("cart_single_product_id", "±àºÅ:"+String.valueOf(id));
					    map.put("cart_single_product_et_num", buycount);
					    map.put("cart_single_product_price", "RMB:"+ Float.toString(unittotalprice));  
					    listItem.add(map);
					    
					    fTotalPrice += unittotalprice;
					}  
					c.close();
					
					vNodata.setVisibility(8);
					
					SimpleAdapter mSimpleAdapter = new SimpleAdapter(
							ShoppingCarActivity.this, 
							listItem, 
							R.layout.shopping_cart_single_product_item, 
							new String[] {"cart_single_product_image", "cart_single_product_name","cart_single_product_id","cart_single_product_et_num", "cart_single_product_price"}, 
							new int [] {R.id.cart_single_product_image,R.id.cart_single_product_name,R.id.cart_single_product_id, R.id.cart_single_product_et_num,R.id.cart_single_product_price});
					
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
					lvGood.setAdapter(mSimpleAdapter);
					
					btnDec.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
						}});
					
					btnInc.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
						}});
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String str = e.getMessage();
				e.printStackTrace();
			}
	
		}
		catch(Exception e)
		{
			String str = e.getMessage();
			e.printStackTrace();
			System.out.println(str);
		}
		

	}
	
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
	
	private int getGoodsInCar()
	{
		SQLiteDatabase db = dbm.openDatabase();
		if(null != db)
		{
			//db.query();
		}
		return 0;
		
	}
	
	public  static Bitmap getBitmapFromUrl(String imgUrl) {  
	    URL url;  
	    Bitmap bitmap = null;  
	    try {  
	        url = new URL(imgUrl);  
	        InputStream is = url.openConnection().getInputStream();  
	        BufferedInputStream bis = new BufferedInputStream(is);  // bitmap = BitmapFactory.decodeStream(bis);     ×¢ÊÍ1                                          
	        byte[] b = getBytes(is);  
	        bitmap = BitmapFactory.decodeByteArray(b,0,b.length);bis.close();  
	    } catch (MalformedURLException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return bitmap;  
	}
	
	public  static Bitmap getBitmapFromFile(String imgFile) {  
	    URL url;  
	    Bitmap bitmap = null;  
	    try {  
	        File f = new File(imgFile);
	        InputStream is = new FileInputStream(f);  
	        BufferedInputStream bis = new BufferedInputStream(is);  // bitmap = BitmapFactory.decodeStream(bis);     ×¢ÊÍ1                                          
	        byte[] b = getBytes(is);  
	        bitmap = BitmapFactory.decodeByteArray(b,0,b.length);bis.close();  
	    } catch (MalformedURLException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return bitmap;  
	}
	public static byte[] getBytes(InputStream is) throws IOException{             
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();             
	    byte[] b = new byte[1024];             
	    int len = 0;             
	    while ((len = is.read(b, 0, 1024)) != -1) {              
	        baos.write(b, 0, len);             
	        baos.flush();             
	    }         
	    byte[] bytes = baos.toByteArray();             
	    return bytes;          
	} 
}
