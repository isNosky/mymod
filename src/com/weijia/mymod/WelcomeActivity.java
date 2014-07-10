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
import com.rqmod.util.StringEncrypt;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomeActivity extends Activity {
	
	DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	boolean bLoginFailed = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		setContentView(R.layout.activity_welcome);

		GlobalVar.getInstance().saveActivity(this);
		
		DoPreAction();
		
		if(!isConn(this))
		{
			setNetworkMethod(this);
			
		}
		else
		{
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
			   @Override
			   public void run() {
				   try {
					int iVerCode = getPackageManager().getPackageInfo("com.weijia.mymod", 0).versionCode;
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   Intent it = null;
				   if(bLoginFailed)
				   {
					   it = new Intent(WelcomeActivity.this, LoginActivity.class); 
				   }
				   else
				   {
					   it = new Intent(WelcomeActivity.this, MainActivity.class); 
				   }
				   startActivity(it); //执行
				   WelcomeActivity.this.finish();
			   }
			  };
			timer.schedule(task, 1000 * 3); //10秒后
		}
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
			GlobalVar.getInstance().setPassword(strPassword);
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
	
	public boolean isConn(Context context){
        boolean bisConnFlag=false;
        ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if(network!=null){
            bisConnFlag=conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }
	
	public void setNetworkMethod(final Context context){
        //提示对话框
        AlertDialog.Builder builder=new Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {                
            	
                Intent intent=null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本 
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                finish();
            }
        }).show();
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
						bLoginFailed = true;
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
