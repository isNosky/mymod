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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
	

	private static final String TBL_SHOPCAR = "tbl_shopcar";
	private static final int TEXTVIEW_ID_OFFSET = 2048;
	//int [] Ids = {R.id.quanbu,R.id.zhushi,R.id.yinliao,R.id.zhou};
	ListView lvMenu = null;
	ImageView vShopCar = null;
	SQLiteDatabase db = null;
	DatabaseManager dbm = null;

	TabHost tabHost = null;
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();		
	
	long mExitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
             if ((System.currentTimeMillis() - mExitTime) > 2000) {
                     Object mHelperUtils;
                     Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                     mExitTime = System.currentTimeMillis();

             } else {
                     finish();
             }
             return true;
		 }

		 return super.onKeyDown(keyCode, event);
	}
	
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
		
//		OnClickListener lsnr = new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				TextView tv = (TextView)findViewById(arg0.getId());
//				tv.setTextColor(getResources().getColor(R.color.green));
//				for(int i = 0 ; i < Ids.length ; i++ )
//				{
//					if(arg0.getId() != Ids[i])
//					{
//						TextView tv1 = (TextView)findViewById(Ids[i]);
//						tv1.setTextColor(getResources().getColor(R.color.black));
//					}
//				}
//			}
//		};
//		
//		for(int i = 0 ; i < Ids.length ; i++ )
//		{
//			TextView tv = (TextView)findViewById(Ids[i]);
//			tv.setOnClickListener(lsnr);
//		}

//		ImageView ivSet = (ImageView)findViewById(R.id.set);
//		ivSet.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
//				startActivity(intent);				
//			}
//		});
		lvMenu = (ListView)findViewById(R.id.product_list);	

		initView();
		
		
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
				dialog.setTitle(getResources().getString(R.string.shake_dialog_title))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(getResources().getString(R.string.menu_goto_shop_car), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
															
							tabHost.setCurrentTab(1);
						}
				}).setNegativeButton(getResources().getString(R.string.menu_remain), new DialogInterface.OnClickListener() {
	             

	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub
	                dialog.cancel();//取消弹出框
	            }
	        }).create().show();
			}
			
		});
	}
	
	private void initView() {
        // 获取xml的RelativeLayout
		LinearLayout layout = (LinearLayout) findViewById(R.id.lllayout);

		
		try {
			Cursor c = db.rawQuery("SELECT * FROM tbl_product_type", null);// WHERE age >= ?", new String[]{"33"});  
			while (c.moveToNext()) {  
			    int type = c.getInt(c.getColumnIndex("type"));  
			    String pname = c.getString(c.getColumnIndex("name"));  
			    
			    HashMap<String, Object> map = new HashMap<String, Object>();  
			    map.put("type", type);
			    map.put("name", pname);
			    listItem.add(map);
			}  
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}

        for (int i = 0; i < listItem.size(); i++) {
        	
        	HashMap<String, Object> map = listItem.get(i);
        	map.put("textview_id", i+TEXTVIEW_ID_OFFSET);
        	// 每行都有一个linearlayout
            LinearLayout lLayout = new LinearLayout(getBaseContext());
            lLayout.setId(i + 1024);
            lLayout.setOrientation(LinearLayout.VERTICAL);
            lLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_round));
            lLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
            LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            lLayout.setLayoutParams(lLayoutlayoutParams);
            
            TextView tv = new TextView(getBaseContext());
            tv.setId(i + TEXTVIEW_ID_OFFSET);
            tv.setText(map.get("name").toString());
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize((float) 15.0);
//            int lw = layout.getWidth();
//            lw = lw / listItem.size();
//            tv.setWidth(lw);

            map.put("textview_obj", tv);
            // 为TextView添加长高设置
            LinearLayout.LayoutParams layoutParams_txt = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(layoutParams_txt);

            // 添加到每行的linearlayout中
            lLayout.addView(tv);
            // 把每个linearlayout加到relativelayout中
            //layout.addView(lLayout, lLayoutlayoutParams);     
            layout.addView(lLayout);

           
        }
        
        
        
        OnClickListener lsnr = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)findViewById(arg0.getId());
				tv.setTextColor(getResources().getColor(R.color.green));
				int type= -1;
				for(int i = 0 ; i < listItem.size() ; i++ )
				{
					HashMap<String, Object> map = listItem.get(i);
					TextView tv1 = (TextView)(map.get("textview_obj"));
					if(arg0.getId() != tv1.getId() )
					{
						tv1.setTextColor(getResources().getColor(R.color.black));
					}	
					else
					{
						type = (Integer) map.get("type");
					}
				}
				getMenuByType(type);
			}
		};
		
		for(int ii = 0 ; ii < listItem.size() ; ii++ )
		{
			HashMap<String, Object> map1 = listItem.get(ii);
			TextView tv1 = (TextView)(map1.get("textview_obj"));
			tv1.setOnClickListener(lsnr);
		}

		//模拟第一个tab页被点击
		HashMap<String, Object> maptmp = listItem.get(0);
		TextView tvtmp = (TextView)(maptmp.get("textview_obj"));
		tvtmp.performClick();
    }
	
	private void getMenuByType(int product_type)
	{
		ArrayList<HashMap<String, Object>> listItem1 = new ArrayList<HashMap<String,Object>>();		
		
		try {
			String strSql = "SELECT * FROM tbl_product";
			if(-1 != product_type)
			{
				strSql = strSql + " where type = " + product_type;
			}
			Cursor c = db.rawQuery(strSql, null);// WHERE age >= ?", new String[]{"33"});  
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
			    listItem1.add(map);
			}  
			c.close();

			SimpleAdapter mSimpleAdapter = new SimpleAdapter(
					MenuActivity.this, 
					listItem1, 
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
	}

	
}

