package com.weijia.mymod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity {
	int [] Ids = {R.id.quanbu,R.id.zhushi,R.id.yinliao,R.id.zhou};
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

		ImageView ivSet = (ImageView)findViewById(R.id.set);
		ivSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
				startActivity(intent);				
			}
		});
	}

}

