package com.weijia.mymod;

import java.util.HashMap;

import com.rqmod.provider.DatabaseManager;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
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
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		
		Cursor c = db.rawQuery("select b.id,b.nickname,a.phonenumber1,a.phonenumber2,a.addr from tbl_addr a,tbl_user b where a.isdefault = 1 and a.subscriberid = b.id", null);
		
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
				
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fill_order, menu);
		return true;
	}

}
