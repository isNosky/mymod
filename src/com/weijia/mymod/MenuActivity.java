package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.ImageManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost;
import android.widget.TextView;

public class MenuActivity extends Activity {
	
	private static final String TBL_SHOPCAR = "tbl_shopcar";
	int [] Ids = {R.id.quanbu,R.id.zhushi,R.id.yinliao,R.id.zhou};
	ListView lvMenu = null;
	ImageView vShopCar = null;
	SQLiteDatabase db = null;
	DatabaseManager dbm = null;

	TabHost tabHost = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("魏家凉皮-菜品");
		// 设置当前Activity界面布局
		setContentView(R.layout.activity_main);
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		
		tabHost = ((TabActivity)getParent()).getTabHost();
		
		OnClickListener lsnr = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)findViewById(arg0.getId());
				tv.setTextColor(getResources().getColor(R.color.green));
				for(int i = 0 ; i < Ids.length ; i++ )
				{
					if(arg0.getId() != Ids[i])
					{
						TextView tv1 = (TextView)findViewById(Ids[i]);
						tv1.setTextColor(getResources().getColor(R.color.black));
					}
				}
			}
		};
		
		for(int i = 0 ; i < Ids.length ; i++ )
		{
			TextView tv = (TextView)findViewById(Ids[i]);
			tv.setOnClickListener(lsnr);
		}

//		ImageView ivSet = (ImageView)findViewById(R.id.set);
//		ivSet.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
//				startActivity(intent);				
//			}
//		});
		lvMenu = (ListView)findViewById(R.id.product_list);	

		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		
		try {
			Cursor c = db.rawQuery("SELECT * FROM tbl_product", null);// WHERE age >= ?", new String[]{"33"});  
			while (c.moveToNext()) {  
			    int id = c.getInt(c.getColumnIndex("id"));  
			    String pname = c.getString(c.getColumnIndex("productname"));  
			    int type = c.getInt(c.getColumnIndex("type"));
			    String desc = c.getString(c.getColumnIndex("description"));
			    String picptah = c.getString(c.getColumnIndex("picturepath"));
			    int isinsale = c.getInt(c.getColumnIndex("isinsale"));
			    float price = c.getFloat(c.getColumnIndex("unitprice"));
			    HashMap<String, Object> map = new HashMap<String, Object>();  
			    Bitmap bmp = ImageManager.getInstance(this).getBitmap(picptah);
				map.put("product_item_image", bmp);
			    map.put("product_item_name", pname);
			    map.put("product_item_id", "编号:"+String.valueOf(id));
			    map.put("product_item_adword", desc);
			    map.put("product_item_martPrice", "RMB:"+ Float.toString(price));  
			    listItem.add(map);
			}  
			c.close();

			SimpleAdapter mSimpleAdapter = new SimpleAdapter(
					MenuActivity.this, 
					listItem, 
					R.layout.product_list_item, 
					new String[] {"product_item_image", "product_item_name","product_item_id","product_item_adword", "product_item_martPrice"}, 
					new int [] {R.id.product_item_image,R.id.product_item_name,R.id.product_item_id, R.id.product_item_adword,R.id.product_item_martPrice});
			
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
			lvMenu.setAdapter(mSimpleAdapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
		
		vShopCar = (ImageView)findViewById(R.id.shop_car_add);
		vShopCar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for(int i = 0; i < lvMenu.getChildCount(); i++){  
				      View view = lvMenu.getChildAt(i);  
				      CheckBox cb = (CheckBox)view.findViewById(R.id.product_checkbox);  
				      if(cb.isChecked())
				      {
				    	  //加入后台数据库
				    	  TextView tvName = (TextView)view.findViewById(R.id.product_item_name);
				    	  TextView tvPrice = (TextView)view.findViewById(R.id.product_item_martPrice);
				    	  TextView tvPrdtId = (TextView)view.findViewById(R.id.product_item_id);

				    	  String strName = tvName.getText().toString();
				    	  String strPrice = tvPrice.getText().toString();
				    	  String strPrdtId = tvPrdtId.getText().toString();
				    	  int productid = Integer.parseInt(strPrdtId.substring(3));
				    	  ContentValues values = new ContentValues();
				    	  values.put("productname", strName);
				    	  values.put("productid", productid);
				    	  values.put("unitprice", Float.parseFloat(strPrice.substring(4)));
				    	  values.put("buycount", 1);
				    	  
						  Cursor count = db.rawQuery("select count(*) goodscount from tbl_shopcar where productid = " + productid, null);
						  count.moveToFirst();
						  int iColIdx = count.getColumnIndex("goodscount");
						  int goodscount = count.getInt(iColIdx);
						  count.close();
						  
						  if(goodscount > 1)
						  {
							  
						  }
						  else if(goodscount == 1)
						  {
							  Cursor buycount = db.rawQuery("select buycount from tbl_shopcar where productid = " + productid, null);
							  buycount.moveToFirst();
							  int iColIdx1 = buycount.getColumnIndex("buycount");
							  int iBuycount = buycount.getInt(iColIdx1);
							  buycount.close();
							  
//							  ContentValues values1 = new ContentValues();
//							  values1.put("buycount", iBuycount +1);
//							  
//							  String whereClause = "productid=?";
//							  String [] whereArgs = {String.valueOf(productid)};
							  try {
								//db.update(TBL_SHOPCAR, values1, whereClause, whereArgs);
								  String strSql = "update tbl_shopcar set buycount = " + String.valueOf(iBuycount +1) + " where productid = " + String.valueOf(productid);
								  db.execSQL(strSql);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								String str = e.getMessage();
								e.printStackTrace();
							}
						  }
						  else
						  {
					    	  if(-1 == db.insert(TBL_SHOPCAR, null, values))
					    	  {
					    		  break;
					    	  }
						  }
				      }
				}
				AlertDialog.Builder dialog=new AlertDialog.Builder(MenuActivity.this);
				dialog.setTitle("提示")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("去购物车", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
															
							tabHost.setCurrentTab(1);
						}
				}).setNegativeButton("再逛逛", new DialogInterface.OnClickListener() {
	             

	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub
	                dialog.cancel();//取消弹出框
	            }
	        }).create().show();
			}
			
		});
	}
	
	

}

