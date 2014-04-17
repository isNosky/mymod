package com.weijia.mymod;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;

public class ShoppingCarActivity extends Activity {
	
	Button btnOrder = null;
	TabHost tbhost = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.shopping_cart_layout);
			
			tbhost = (TabHost)findViewById(android.R.id.tabhost);
			btnOrder = (Button)findViewById(R.id.cart_no_data_forward_cuxiao);
			
			btnOrder.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					tbhost.setCurrentTab(0);
				}
				
			});
		}
		catch(Exception e)
		{
			String str = e.getMessage();
			e.printStackTrace();
			System.out.println(str);
		}
	}
}
