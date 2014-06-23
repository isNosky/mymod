package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;

import com.weijia.mymod.MyOderListActivity.OrderAdpatorView;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class OrderHistoryActivity extends Activity {

	ListView lvOrderHistory = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_history);		
		
		lvOrderHistory = (ListView)findViewById(R.id.order_history_list);
		
		Bundle bundle = getIntent().getExtras();
		ArrayList listItem = bundle.getParcelableArrayList("OrderHistoryList");
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		for(int i = 0 ; i < listItem.size() ; i++)
		{
			ContentValues values  = (ContentValues) listItem.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("orderid", values.get("orderid").toString());
			map.put("orderstatus", values.get("orderstatus").toString());
			map.put("comment", values.get("comment").toString());
			map.put("opTime", values.get("opTime").toString());
			list.add(map);
		}
		
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(
				OrderHistoryActivity.this, 
				list, 
				R.layout.order_history_list_item, 
				new String[] {"order_history_optime", "order_history_status","order_history_comment"}, 
				new int [] {R.id.order_history_optime,R.id.order_history_status,R.id.order_history_comment});
		
		lvOrderHistory.setAdapter(mSimpleAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_history, menu);
		return true;
	}

}
