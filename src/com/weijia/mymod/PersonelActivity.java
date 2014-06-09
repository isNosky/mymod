package com.weijia.mymod;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.MyModApp;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersonelActivity extends Activity {
	TextView tvUserName = null;
	TextView tvUserLevel = null;
	TextView tvUserScore = null;
	RelativeLayout rlLoginInfo = null;
	RelativeLayout rlNotLoginInfo = null;
	DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			PersonelActivity.ClickListener myClickListener = new PersonelActivity.ClickListener();
			
			setContentView(R.layout.personel_activity);
			
			//initTopUI();
			
			Button btnLogin = (Button)findViewById(R.id.personal_click_for_login);
			btnLogin.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(arg0.getId() == R.id.personal_click_for_login)
					{
						Intent intent = new Intent(PersonelActivity.this,LoginActivity.class);
						startActivity(intent);
					}
				}
			});
			
			Button btnExitLogin = (Button)findViewById(R.id.personel_logout_but);
			btnExitLogin.setVisibility(4);
			
			RelativeLayout myAccount = (RelativeLayout)findViewById(R.id.my_account);
			RelativeLayout myMaterialFlow = (RelativeLayout)findViewById(R.id.my_material_flow);
			myAccount.setOnClickListener(myClickListener);
			myMaterialFlow.setOnClickListener(myClickListener);
			btnExitLogin.setOnClickListener(myClickListener);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
		
		
		
	}
	
	private void initTopUI()
	{
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		
		rlLoginInfo = (RelativeLayout)findViewById(R.id.personal_for_login_info);
		rlNotLoginInfo = (RelativeLayout)findViewById(R.id.personal_for_not_login);

		tvUserName = (TextView)findViewById(R.id.who_and_say_hello);
		tvUserLevel = (TextView)findViewById(R.id.user_level);
		tvUserScore = (TextView)findViewById(R.id.user_score);
		
		Cursor c = db.rawQuery("select count(*) logincount from tbl_user where islogin=1", null);// WHERE age >= ?", new String[]{"33"}); 
		
    	int logincount = 0;
		while (c.moveToNext()) {
		    logincount = c.getInt(c.getColumnIndex("logincount"));
		}  
		c.close();
		
		if(0 == logincount)
		{
			
		}
		else
		{
			rlNotLoginInfo.setVisibility(View.GONE);
			Cursor c1 = db.rawQuery("select * logincount from tbl_user where islogin=1", null);// WHERE age >= ?", new String[]{"33"}); 
			
			String strPhoneNum = "";
			while (c1.moveToNext()) {
				strPhoneNum = c1.getString(c1.getColumnIndex("phonenum"));
			}  
			c.close();
			
			tvUserName.setText(strPhoneNum);
			tvUserLevel.setText("铜牌用户");
			tvUserScore.setText("0");
		}
	}
	class ClickListener implements View.OnClickListener {
        
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()) {
               
                case R.id.my_account:
                {
                	Intent account = new Intent(PersonelActivity.this, AccountMgrActivity.class);
                	startActivity(account);
                    return;
                }

                case R.id.my_material_flow:
                {

                	Intent intent1 = new Intent(PersonelActivity.this, MyOderListActivity.class);
                	startActivity(intent1);
                    return;
                }  
                
                case R.id.personel_logout_but:
                {
                	AlertDialog.Builder dialog=new AlertDialog.Builder(PersonelActivity.this);
    				dialog.setTitle(getResources().getString(R.string.pg_my_jd_logout_confrim_string))
    					.setIcon(android.R.drawable.ic_dialog_info)
    					.setPositiveButton(getResources().getString(R.string.upomp_bypay_affirm), new DialogInterface.OnClickListener() {
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    															
    							logout();
    						}
    				}).setNegativeButton(getResources().getString(R.string.upomp_bypay_return), new DialogInterface.OnClickListener() {
    	             

    	            public void onClick(DialogInterface dialog, int which) {
    	                // TODO Auto-generated method stub
    	                dialog.cancel();//取消弹出框
    	            }
    	        }).create().show();
                	
                	
                }
            }
        }
    }
	
	private void logout()
	{
		
		if(Constant.FLAG_POST_IN_JSON)
		{	
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		
	    			JSONObject jsoin = new JSONObject();
	    			JSONObject jsonout = null;
					try {	
						MyModApp app = (MyModApp)PersonelActivity.this.getApplication();
						jsoin.put("UserID", app.getUserId());	
						jsonout = HttpUtil.queryStringForPost(Constant.LOGOUTSERVLET, jsoin);	
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
		    		
		    		JSONObject jsonout = null;
		    		
					try {
						MyModApp app = (MyModApp) PersonelActivity.this.getApplication();
						HttpPost request = new HttpPost();
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			            postParameters.add(new BasicNameValuePair("UserID", String.valueOf(app.getUserId())));			           
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.LOGOUTSERVLET, postParameters);
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
						//注销成功
						showDialog(getResources().getString(R.string.jshop_logout_success));
					}
					else
					{
						showDialog(strErrDesc);
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
