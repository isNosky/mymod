package com.weijia.mymod;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends TabActivity {
	private TabHost tabHost;
	
	private static final String HOME = "主页";    
	private static final String BOX = "购物车";    
	private static final String MY = "我的"; 
	
	private Intent homeIntent;
	private Intent boxIntent;
	private Intent myIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tabHost=this.getTabHost();  
		tabHost.setBackgroundColor(Color.argb(150, 20, 80, 150));

        tabHost.setFocusable(true);  
		prepareIntent();  
        setupIntent(); 
        
        //ListenShopCar();
	}
	 private void setupIntent(){  
	        tabHost.addTab(buildTabSpec(HOME,R.drawable.home_w, homeIntent));  
	        tabHost.addTab(buildTabSpec(BOX,R.drawable.box, boxIntent));  
	        tabHost.addTab(buildTabSpec(MY,R.drawable.my, myIntent));  

	    }  
	  
	    private TabSpec buildTabSpec(String tag, int icon, Intent intent) {  
	        View view = View.inflate(MainActivity.this, R.layout.tabs_bg, null);  
	        ((ImageView)view.findViewById(R.id.icon)).setImageResource(icon);  
	        ((TextView)view.findViewById(R.id.tabsText)).setText(tag);  
	        
	        TabHost.TabSpec spec = tabHost.newTabSpec(tag);
	        
	        spec.setContent(intent);  
	        spec.setIndicator(view);
            return spec;
	    }  
	  
	    private void prepareIntent() {  
	        homeIntent=new Intent(this, MenuActivity.class);  
	        boxIntent=new Intent(this, ShoppingCarActivity.class);  
	        myIntent=new Intent(this, PersonelActivity.class);  

	    }
//	    public TabHost getTabHost()
//	    {
//	    	return tabHost;
//	    }
	    
	    private void ListenShopCar()
	    {
	    	getTabWidget().getChildAt(1).setOnClickListener(new OnClickListener() { 
	            @Override 
	            public void onClick(View v) { 

	            	tabHost.setCurrentTab(1);
	            	//ShoppingCarActivity.this.finish();
	            	
	            } 
	        }); 
	    }
}
