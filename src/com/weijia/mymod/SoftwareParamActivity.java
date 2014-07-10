package com.weijia.mymod;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SoftwareParamActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_software_param);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.software_param, menu);
		return true;
	}

}
