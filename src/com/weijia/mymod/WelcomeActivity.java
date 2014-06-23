package com.weijia.mymod;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.GlobalVar;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomeActivity extends Activity {
	
	DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		setContentView(R.layout.activity_welcome);

		DoPreAction();
		
		final Intent it = new Intent(WelcomeActivity.this, MainActivity.class); //你要转向的Activity  
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
		   @Override
		   public void run() {
		    startActivity(it); //执行
		    WelcomeActivity.this.finish();
		   }
		  };
		timer.schedule(task, 1000 * 3); //10秒后
		
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	private void DoPreAction()
	{
		String strPhoneNum = "";
		String strPassword = "";
		Cursor c1 = null;
		try {
			c1 = db.rawQuery("select * from tbl_user where islogin=1", null);
			
			while (c1.moveToNext()) {
				strPhoneNum = c1.getString(c1.getColumnIndex("phonenum"));
				strPassword = c1.getString(c1.getColumnIndex("password"));
			}  
			c1.close();
			GlobalVar.getInstance().setCellphoneNumber(strPhoneNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}	
		
		if(strPhoneNum.isEmpty() || strPassword.isEmpty())
		{
			
		}
		else
		{
			Login(strPhoneNum,strPassword);
		}
	}
	
	private void Login(final String username,final String password)
	{
		try {
			
		 	if(Constant.FLAG_POST_IN_JSON)
			{								
		 		
			}
			else
			{						    
			    
			    Thread thread = new Thread(){ 
			    	@Override 
				    public void run() { 	
			    		JSONObject jsonout = null;
				    	try { 							    		
				    		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				            postParameters.add(new BasicNameValuePair("LoginName", username));
				            postParameters.add(new BasicNameValuePair("Password", password));				            
						    jsonout = HttpUtil.queryStringForPost(Constant.LOGINSERVLET, postParameters);
				    	} catch (Exception e) { 
				    		String str = e.getMessage();
				    	} 
				    	  
				    	Message message= handler.obtainMessage() ; 
				    	message.obj = jsonout; 
				    	message.what = Constant.LOGIN_MSG;
				    	handler.sendMessage(message); 
				    	} 
			    	}; 
			    	thread.start(); 
			    	thread = null; 
			}	
			return;
        } 
	 catch(Exception e) {
            e.printStackTrace();
     }
	}
	
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case Constant.LOGIN_MSG:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
					
					if(Constant.ERR_CODE_SUCCESS == iErrorCode)
					{						
						int iUserId = jsonout.getInt("UserID");
						String strToken = jsonout.getString("token");
						GlobalVar.getInstance().setToken(strToken);
						GlobalVar.getInstance().setUserId(iUserId);						
					}
					else
					{
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					String str = e.getMessage();
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					String str = e.getMessage();
					e.printStackTrace();
				}
                break;
            }
        }
    };
}
