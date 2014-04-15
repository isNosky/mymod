package com.weijia.mymod;

import android.app.Activity;
import android.os.Bundle;

public class NewEasyBuyAddressListActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.new_easy_buy_address_list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
}
