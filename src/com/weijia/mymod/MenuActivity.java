package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MenuActivity extends Activity {
	
	int [] Ids = {R.id.quanbu,R.id.zhushi,R.id.yinliao,R.id.zhou};
	ListView lvMenu = null;
	ImageView vShopCar = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("魏家凉皮-菜品");
		// 设置当前Activity界面布局
		setContentView(R.layout.activity_main);
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
		for(int i = 0 ; i < 1 ;i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();  
			map.put("product_item_image", R.drawable.ganmianpi);
            map.put("product_item_name", "魏家凉皮");
            map.put("product_item_adword", "凉皮壤、调料好、分量多");
            map.put("product_item_martPrice", "RMB15.00");  
            listItem.add(map);
		}

		SimpleAdapter mSimpleAdapter = new SimpleAdapter(
				MenuActivity.this, 
				listItem, 
				R.layout.product_list_item, 
				new String[] {"product_item_image", "product_item_name","product_item_adword", "product_item_martPrice"}, 
				new int [] {R.id.product_item_image,R.id.product_item_name,R.id.product_item_adword,R.id.product_item_martPrice});

		lvMenu.setAdapter(mSimpleAdapter);
		
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
				    	  //SQLiteDatabase db = getWritableDatabase(); 
				      }
				}
			}
			
		});
	}
	
	

}

