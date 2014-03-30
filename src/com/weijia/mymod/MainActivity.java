package com.weijia.mymod;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	int [] Ids = {R.id.quanbu,R.id.zhushi,R.id.yinliao,R.id.zhou};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//设置分类的监听器
		OnClickListener lsnr = new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView tv = (TextView)findViewById(v.getId());
				tv.setTextColor(getResources().getColor(R.color.green));
				for(int i = 0 ; i < Ids.length ; i++ )
				{
					if(v.getId() != Ids[i])
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
		
		//设置按钮的监听器
		ImageView ivSet = (ImageView)findViewById(R.id.set);
		ivSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intent);				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
