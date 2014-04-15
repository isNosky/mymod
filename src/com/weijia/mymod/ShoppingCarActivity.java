package com.weijia.mymod;

import android.app.Activity;
import android.os.Bundle;

public class ShoppingCarActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.shopping_cart_layout);
		}
		catch(Exception e)
		{
			String str = e.getMessage();
			e.printStackTrace();
			System.out.println(str);
		}
	}
}
