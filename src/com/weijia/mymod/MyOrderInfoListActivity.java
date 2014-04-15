package com.weijia.mymod;

import android.app.Activity;
import android.os.Bundle;

public class MyOrderInfoListActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.my_order_list_activity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
}
