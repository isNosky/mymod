package com.weijia.mymod;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShopListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_list_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shop_list, menu);
		return true;
	}

}
