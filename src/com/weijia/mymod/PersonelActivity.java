package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.GlobalVar;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonelActivity extends Activity {
	

	TextView tvUserName = null;
	TextView tvUserLevel = null;
	TextView tvUserScore = null;
	RelativeLayout rlLoginInfo = null;
	RelativeLayout rlNotLoginInfo = null;
	DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	Button btnExitLogin = null;
	private PopupWindow popup = null;
	private ListView menulistview = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		
		GlobalVar.getInstance().saveActivity(this);
		
		try {
			PersonelActivity.ClickListener myClickListener = new PersonelActivity.ClickListener();
			
			setContentView(R.layout.personel_activity);
	
			setDebugPara();
			
			Button btnLogin = (Button)findViewById(R.id.personal_click_for_login);
			btnLogin.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(arg0.getId() == R.id.personal_click_for_login)
					{
						Intent intent = new Intent(PersonelActivity.this,LoginActivity.class);
						startActivityForResult(intent,0);
					}
				}
			});
			
			btnExitLogin = (Button)findViewById(R.id.personel_logout_but);			
			
			RelativeLayout myAccount = (RelativeLayout)findViewById(R.id.my_account);
			RelativeLayout myMaterialFlow = (RelativeLayout)findViewById(R.id.my_material_flow);
			myAccount.setOnClickListener(myClickListener);
			myMaterialFlow.setOnClickListener(myClickListener);
			btnExitLogin.setOnClickListener(myClickListener);
			
			initTopUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
	
	private void setDebugPara()
	{
		LinearLayout ll = (LinearLayout) findViewById(R.id.logo_and_title);
		ll.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				Intent intent = new Intent(PersonelActivity.this,SoftwareParamActivity.class);
				startActivity(intent);
				return false;
			}});
	}
	
	private void initPopuWindows() {
		
		menulistview = new ListView(this);
		popup = new PopupWindow(menulistview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub		
		switch (resultCode) {
			case RESULT_OK:
				initTopUI();
				break;
			default:
		          break;
	}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	long mExitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
             if ((System.currentTimeMillis() - mExitTime) > 2000) {
                     Object mHelperUtils;
                     Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                     mExitTime = System.currentTimeMillis();

             } else {
            	 GlobalVar.getInstance().exitSystem();
            	 GlobalVar.getInstance().clearDB(this);
             }
             return true;
		 }

		 return super.onKeyDown(keyCode, event);
	}
	
	private void initTopUI()
	{		
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
			btnExitLogin.setVisibility(View.GONE);
			rlNotLoginInfo.setVisibility(View.VISIBLE);
		}
		else
		{
			rlNotLoginInfo.setVisibility(View.GONE);
			rlLoginInfo.setVisibility(View.VISIBLE);
			btnExitLogin.setVisibility(View.VISIBLE);
			
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			tvUserName.setText(strPhoneNum);
			tvUserLevel.setText("铜牌用户");
			tvUserScore.setText("0");
		}
	}	
	
	private boolean checkLogin()
	{
		GlobalVar app = GlobalVar.getInstance();
		int iUserID = app.getUserId();
		if(-1 == iUserID)
		{
			Intent intent = new Intent(PersonelActivity.this,LoginActivity.class);
			startActivityForResult(intent, 0);
			return true;
		}
		return false;
	}
	
	class ClickListener implements View.OnClickListener {
        
        public void onClick(View v) {
        	
        	if(checkLogin())
        	{
        		return;
        	}
        	
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
    							initTopUI();
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
		String strSql = "update tbl_user set islogin=0";
	    db.execSQL(strSql);
	    
	    strSql = "delete from tbl_user";
	    db.execSQL(strSql);
	    
	    strSql = "delete from tbl_order";
	    db.execSQL(strSql);
	    
	    strSql = "delete from tbl_order_history";
	    db.execSQL(strSql);
	    
	    strSql = "delete from tbl_addr";
	    db.execSQL(strSql);
	    
	    strSql = "delete from tbl_shopcar";
	    db.execSQL(strSql);
	    
	    GlobalVar app = GlobalVar.getInstance();
	    app.setUserId(-1);
	    app.setCellphoneNumber("");
	    app.setPassword("");
	    app.setToken("");
	    
		if(Constant.FLAG_POST_IN_JSON)
		{	
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		
	    			JSONObject jsoin = new JSONObject();
	    			JSONObject jsonout = null;
					try {	
						GlobalVar app = GlobalVar.getInstance();
						jsoin.put("UserID", app.getUserId());	
						jsonout = HttpUtil.queryStringForPost(Constant.LOGOUTSERVLET, jsoin);	
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.LOGOUT_MSG;
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
						GlobalVar app = GlobalVar.getInstance();
						HttpPost request = new HttpPost();
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			            postParameters.add(new BasicNameValuePair("UserID", String.valueOf(app.getUserId())));			           
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.LOGOUTSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.LOGOUT_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;
			
		}		
	}
	
	public void RedirectLogin()
    {
    	AlertDialog.Builder dialog=new AlertDialog.Builder(PersonelActivity.this);
		dialog.setTitle(getResources().getString(R.string.token_invalid_login_tip))
			.setIcon(android.R.drawable.ic_dialog_info)
			.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(PersonelActivity.this,LoginActivity.class);
					intent.setFlags(Constant.LOGIN_MSG);
					startActivityForResult(intent,Constant.LOGIN_MSG);
				}
		}).create().show();
    }
	
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case Constant.LOGOUT_MSG:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
										
					switch(iErrorCode)
					{
					case Constant.ERR_CODE_SUCCESS:
						try {
						    GlobalVar app = GlobalVar.getInstance();
						    String strSql = "update tbl_user set islogin = 0" + " where id = " + String.valueOf(app.getUserId());
						    db.execSQL(strSql);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							String str = e.getMessage();
							e.printStackTrace();
						}
						initTopUI();
						//注销成功
						showDialog(getResources().getString(R.string.jshop_logout_success));

						break;
					case Constant.ERR_CODE_TOKEN_INVALID:
						RedirectLogin();
						break;
					default:
						showDialog(strErrDesc);
						break;
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
