package com.weijia.mymod;

import com.rqmod.provider.GlobalVar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class AccountMgrActivity extends Activity implements View.OnClickListener {
	
	private RelativeLayout easyBuyLayout;
	private RelativeLayout my_account_safe;
		@Override
		public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		try {
			
			setContentView(R.layout.my_account_center);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
		easyBuyLayout = (RelativeLayout)findViewById(R.id.my_easy_buy);
		easyBuyLayout.setOnClickListener((OnClickListener) this);
		
		my_account_safe = (RelativeLayout)findViewById(R.id.my_account_safe);
		my_account_safe.setOnClickListener((OnClickListener) this);
		
		GlobalVar.getInstance().saveActivity(this);
	}
		
	public void onClick(View v) {
        switch(v.getId()) 
        {
            case R.id.my_easy_buy:
            {
            	Intent intent = new Intent(AccountMgrActivity.this, NewEasyBuyAddressListActivity.class);
                //intent.putExtra("title", getString(0x7f0b0367));
                startActivity(intent);
                break;
            }
            case R.id.my_account_safe:
            {
            	Intent intent = new Intent(AccountMgrActivity.this, ModPassActivity.class);                
                startActivity(intent);
                break;
            }            
        }
	}
}
