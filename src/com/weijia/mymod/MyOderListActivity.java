package com.weijia.mymod;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class MyOderListActivity extends Activity {
	private Button oneMonthOrders;
    private Button preMonthOrders;
    private ListView mOneMonthList;
    private ListView mPreMonthList;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		try {
			
			setContentView(R.layout.my_order_list_activity);
			
			oneMonthOrders = (Button)findViewById(R.id.my_order_one_month_orders);
			preMonthOrders = (Button)findViewById(R.id.my_order_pre_month_orders);
			mOneMonthList = (ListView)findViewById(R.id.my_order_list_one_month);
			mPreMonthList = (ListView)findViewById(R.id.my_order_list_pre_month);
			oneMonthOrders.setSelected(true);
			preMonthOrders.setSelected(false);		
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
}
