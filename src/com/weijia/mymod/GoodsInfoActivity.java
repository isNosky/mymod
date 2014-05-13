package com.weijia.mymod;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GoodsInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fill_order_commodity);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.goods_info, menu);
		return true;
	}

}
