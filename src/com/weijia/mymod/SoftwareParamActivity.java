package com.weijia.mymod;

import com.rqmod.util.Constant;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SoftwareParamActivity extends Activity {

	EditText et = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_software_param);
		
		et = (EditText) findViewById(R.id.register_input_name);
		Button btn = (Button) findViewById(R.id.debug_para_button);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Constant.setWebApp(et.getEditableText().toString());
				AlertDialog.Builder builder=new Builder(SoftwareParamActivity.this);
		        builder.setTitle("设置成功").setMessage("当前URL:"+Constant.getURL()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
		            
		            @Override
		            public void onClick(DialogInterface dialog, int which) {                
		            	
		            	SoftwareParamActivity.this.finish();
		            }
		        }).show();
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.software_param, menu);
		return true;
	}

}
