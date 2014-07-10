package com.weijia.mymod;

import com.rqmod.provider.AsyncViewTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		Intent intent = getIntent();
		String strTag = intent.getStringExtra("image_tag");
		ImageView iv = (ImageView)findViewById(R.id.menu_img_view);
		iv.setTag(strTag);
		new AsyncViewTask().execute(iv);
		
		iv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

}
