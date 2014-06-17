package com.weijia.mymod;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.GlobalVar;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewAddrActivity extends Activity {

	Button btnDelete = null;
	Button btnOK = null;
	
	EditText etAlias = null;
	EditText etName = null;
	EditText etMobile = null;
	EditText etAddr = null;
	
	RelativeLayout rlArea = null;
	RelativeLayout rlStreet = null;
	TextView tvArea = null;
	TextView tvStreet = null;
	private final static int AREA_DIALOG = 1;
	private final static int STREET_DIALOG = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_addr);
		
		btnDelete = (Button)findViewById(R.id.button_new_easy_buy_address_delete);
		btnOK = (Button)findViewById(R.id.button_new_easy_buy_address_ok);
		etAlias = (EditText)findViewById(R.id.edittext_new_easy_buy_address_alias);
		etName = (EditText)findViewById(R.id.edittext_new_easy_buy_address_name);
		etMobile = (EditText)findViewById(R.id.edittext_new_easy_buy_address_mobile);
		etAddr = (EditText)findViewById(R.id.edittext_new_easy_buy_address_detail);
		
		rlArea = (RelativeLayout)findViewById(R.id.layout_new_easy_buy_address_areas);
		rlStreet = (RelativeLayout)findViewById(R.id.layout_new_easy_buy_address_street);
		
		tvArea = (TextView)findViewById(R.id.textview_new_easy_buy_address_areas_content);
		tvStreet = (TextView)findViewById(R.id.textview_new_easy_buy_address_street_content);
		
		Intent intent = getIntent();
		int flag = intent.getFlags();
		if(0 == flag)	//新增地址
		{
			btnDelete.setVisibility(Button.GONE);
		}
		else
		{
			
		}
		
		rlArea.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(AREA_DIALOG);
			}});
		
		rlStreet.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(STREET_DIALOG);
			}});
		
		btnOK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setAddr();
			}});
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog=null;
        switch (id) {
        case AREA_DIALOG:
            try {
				Builder builder=new android.app.AlertDialog.Builder(this);
				//设置对话框的图标
				//builder.setIcon(R.drawable.header);
				//设置对话框的标题
				builder.setTitle("选择县区");
				
				//添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
				final String [] items = getResources().getStringArray(R.array.district);
				builder.setItems(items, new android.content.DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which) {
				        String hoddy=(String) items[which];
				        tvArea.setText(tvArea.getText().toString() + ":" + hoddy);
				    }
				});
				//创建一个列表对话框
				dialog=builder.create();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String str = e.getMessage();
				e.printStackTrace();
			}
            break;
            
        case STREET_DIALOG:
            try {
				Builder builder=new android.app.AlertDialog.Builder(this);
				//设置对话框的图标
				//builder.setIcon(R.drawable.header);
				//设置对话框的标题
				builder.setTitle("选择街道");
				
				//添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
				final String [] itemstreet = getResources().getStringArray(R.array.yanta_district);
				builder.setItems(itemstreet, new android.content.DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface dialog, int which) {
				        String hoddy=(String) itemstreet[which];
				        tvStreet.setText(tvStreet.getText().toString() + ":" + hoddy);
				    }
				});
				//创建一个列表对话框
				dialog=builder.create();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				String str = e.getMessage();
				e.printStackTrace();
			}
            break;
        }
        return dialog;
    }
	
	private void insertToDB(int iAddrID)
	{
		
	}
	
	private void setAddr()
	{
		if(Constant.FLAG_POST_IN_JSON)
		{
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		
	    			JSONObject jsoin = new JSONObject();	
	    			JSONObject jsonout = null;
					try {
						
						jsonout = HttpUtil.queryStringForPost(Constant.REGISTERSERVLET, jsoin);		
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = 1;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;
			
		}
		else
		{
            Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		
	    			JSONObject jsoin = new JSONObject();	
	    			JSONObject jsonout = null;
					try {
						HttpPost request = new HttpPost();
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
						GlobalVar app = GlobalVar.getInstance();
			            postParameters.add(new BasicNameValuePair("UserID", String.valueOf(app.getUserId())));
			            postParameters.add(new BasicNameValuePair("Addr", ""));
			            postParameters.add(new BasicNameValuePair("Token", GlobalVar.getInstance().getToken()));
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.SETADDRSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = 1;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;
            
		}
	}
	
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case 1:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
					
					if(Constant.ERR_CODE_SUCCESS == iErrorCode)
					{
						int AddrID = jsonout.getInt("AddrID");
						insertToDB(AddrID);
						
						Intent intent = new Intent(NewAddrActivity.this, NewEasyBuyAddressListActivity.class);						
						intent.putExtra("textview_new_easy_buy_address_list_item_name", etName.getEditableText().toString());
						intent.putExtra("textview_new_easy_buy_address_list_item_phone", etMobile.getEditableText().toString());
						intent.putExtra("textview_new_easy_buy_address_list_item_address", tvArea.getText().toString() + tvStreet.getText().toString() + etAddr.getEditableText().toString());
						
						setResult(RESULT_OK,intent); 
						finish();
					}
					else
					{
						showDialog(getResources().getString(R.string.register_fail));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					String str = e.getMessage();
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                break;
            }
        }
    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_addr, menu);
		return true;
	}
	
	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
}
