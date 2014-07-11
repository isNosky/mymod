package com.weijia.mymod;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.GlobalVar;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;
import com.rqmod.util.StringEncrypt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ModPassActivity extends Activity {

	EditText etUserName = null;
	EditText etPassword = null;
	EditText etConfirmPassword = null;
	Button btnModPass = null;
	SQLiteDatabase db = null;
	DatabaseManager dbm = null;
	String strPass = "";
	
	CheckBox cbShowPass = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_pass);
		
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		
		etUserName = (EditText)findViewById(R.id.register_input_name);
		etPassword = (EditText)findViewById(R.id.register_input_password);
		etConfirmPassword = (EditText)findViewById(R.id.register_input_confirm_password);
		btnModPass = (Button)findViewById(R.id.register_top); 
		cbShowPass = (CheckBox)findViewById(R.id.show_password);
		GlobalVar app = GlobalVar.getInstance();
		etUserName.setText(app.getCellphoneNumber());
		etUserName.setEnabled(false);
		btnModPass.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				strPass = etPassword.getEditableText().toString();
				String strConfirmPass = etConfirmPassword.getEditableText().toString();
				if(!strPass.equals(strConfirmPass))
				{
					showDialog(getResources().getString(R.string.must_password_equal));
					return;
				}	
				
				strPass = StringEncrypt.Encrypt(strPass, "SHA-256");
				if(Constant.FLAG_POST_IN_JSON)
				{
					Thread thread = new Thread(){ 
				    	@Override 
					    public void run() { 	
				    		
			    			JSONObject jsoin = new JSONObject();	
			    			JSONObject jsonout = null;
							try {
								
								jsoin.put("CellphoneOSType", "Android");
								jsoin.put("CellphoneBrand", android.os.Build.BRAND + " " + android.os.Build.MODEL);
								jsoin.put("Age", "25");
								jsoin.put("Sex", 0);
								jsonout = HttpUtil.queryStringForPost(Constant.MODIFYPASSWORDSERVLET, jsoin);		
					    	} catch (Exception e) { 
					    		String str = e.getMessage();
					    	} 
					    	  
					    	Message message= handler.obtainMessage() ; 
					    	message.obj = jsonout; 
					    	message.what = Constant.MODIFYPASSWORD_MSG;
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
								GlobalVar app = GlobalVar.getInstance();
								List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
					            postParameters.add(new BasicNameValuePair("UserID",String.valueOf(app.getUserId())));
					            postParameters.add(new BasicNameValuePair("OldPassword", app.getPassword()));
					            postParameters.add(new BasicNameValuePair("NewPassword", strPass));
					            
					            jsonout = HttpUtil.queryStringForPost(Constant.MODIFYPASSWORDSERVLET, postParameters);
					    	} catch (Exception e) { 
					    		String str = e.getMessage();
					    	} 
					    	  
					    	Message message= handler.obtainMessage() ; 
					    	message.obj = jsonout; 
					    	message.what = Constant.MODIFYPASSWORD_MSG;
					    	handler.sendMessage(message); 
					    	} 
				    	}; 
				    	thread.start(); 
				    	thread = null;
		            
				}
			}});
		
		GlobalVar.getInstance().saveActivity(this);
		
		cbShowPass.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				String pwTextString = "";
				if(etPassword == null) {
				    pwTextString = "";
				} else {
				    pwTextString = etPassword.getText().toString();
				}
				if(arg1) {
					etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				if(!TextUtils.isEmpty(pwTextString)) {
					etPassword.setSelection(pwTextString.length());
				}
				
				pwTextString = "";
				if(etConfirmPassword == null) {
				    pwTextString = "";
				} else {
				    pwTextString = etConfirmPassword.getText().toString();
				}
				if(arg1) {
					etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				if(!TextUtils.isEmpty(pwTextString)) {
					etConfirmPassword.setSelection(pwTextString.length());
				}
				
			}});
	}

	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("È·¶¨", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void updateDB(String user,String password)
	{
		try {
			//db.update(TBL_SHOPCAR, values1, whereClause, whereArgs);
			GlobalVar app = GlobalVar.getInstance();
			  String strSql = "update tbl_user set password = " + password + " where id = " + app.getUserId();
			  db.execSQL(strSql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
	public void RedirectLogin()
    {
    	AlertDialog.Builder dialog=new AlertDialog.Builder(ModPassActivity.this);
		dialog.setTitle(getResources().getString(R.string.token_invalid_login_tip))
			.setIcon(android.R.drawable.ic_dialog_info)
			.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(ModPassActivity.this,LoginActivity.class);
					intent.setFlags(Constant.LOGIN_MSG);
					startActivityForResult(intent,Constant.LOGIN_MSG);
				}
		}).create().show();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mod_pass, menu);
		return true;
	}
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case Constant.MODIFYPASSWORD_MSG:
                //¹Ø±Õ
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
					
					switch(iErrorCode)
					{
					case Constant.ERR_CODE_SUCCESS:
						GlobalVar app = GlobalVar.getInstance();
						updateDB(app.getCellphoneNumber(),strPass);
						ModPassActivity.this.finish();
						break;
					case Constant.ERR_CODE_TOKEN_INVALID:
						RedirectLogin();
						break;
					default:
						showDialog(getResources().getString(R.string.upomp_bypay_changepswfail));
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
	
}
