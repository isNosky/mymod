package com.weijia.mymod;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class RegisterActivity extends Activity {
	
	EditText etUserName = null;
	EditText etPassword = null;
	EditText etConfirmPassword = null;
	
	Button btnRegister = null;
	
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() 
	        .detectDiskReads() 
	        .detectDiskWrites() 
	        .detectNetwork()   // or .detectAll() for all detectable problems 
	        .penaltyLog() 
	        .build()); 
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder() 
			.detectLeakedSqlLiteObjects() 
			.detectLeakedClosableObjects() 
			.penaltyLog()
			.penaltyDeath() 
			.build()); 
		
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.register_activity);
			
			etUserName = (EditText)findViewById(R.id.register_input_name);
			etPassword = (EditText)findViewById(R.id.register_input_password);
			etConfirmPassword = (EditText)findViewById(R.id.register_input_confirm_password);
			
			btnRegister = (Button)findViewById(R.id.register_top);
			
			btnRegister.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					final String strUser = etUserName.getEditableText().toString();
					final String strPass = etPassword.getEditableText().toString();
					String strConfirmPass = etConfirmPassword.getEditableText().toString();
					if(!strPass.equals(strConfirmPass))
					{
						showDialog(getResources().getString(R.string.must_password_equal));
					}					
					
					if(Constant.FLAG_POST_IN_JSON)
					{
						Thread thread = new Thread(){ 
					    	@Override 
						    public void run() { 	
					    		
				    			JSONObject jsoin = new JSONObject();	
				    			JSONObject jsonout = null;
								try {
									jsoin.put("CellphoneNumber", strUser);
									jsoin.put("NickName", strUser);
									jsoin.put("Password", strPass);
									jsoin.put("CellphoneOSType", "Android");
									jsoin.put("CellphoneBrand", android.os.Build.BRAND + " " + android.os.Build.MODEL);
									jsoin.put("Age", "25");
									jsoin.put("Sex", 0);
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
						            postParameters.add(new BasicNameValuePair("CellphoneNumber", strUser));
						            postParameters.add(new BasicNameValuePair("NickName", strUser));
						            postParameters.add(new BasicNameValuePair("Password", strPass));
						            postParameters.add(new BasicNameValuePair("CellphoneOSType", "Android"));
						            postParameters.add(new BasicNameValuePair("CellphoneBrand", android.os.Build.BRAND + " " + android.os.Build.MODEL));
						            postParameters.add(new BasicNameValuePair("Age", "25"));
						            postParameters.add(new BasicNameValuePair("Sex", "0"));
						            
						            jsonout = HttpUtil.queryStringForPost(Constant.REGISTERSERVLET, postParameters);
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
					
					
					
				}});
			
//			if(!nameCheck())
//			{
//				showDialog(getResources().getString(R.string.must_login_use_phone_num));
//			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
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
	
	private boolean nameCheck() {
        boolean checkFlag = false;
        String checkStr = etUserName.getText().toString();
        if(TextUtils.isEmpty(checkStr.trim())
        		&& !isPhoneNumberValid(checkStr.trim())) {
            checkFlag = true;
            //mUserNameTxt.setError(JDStringUtils.getErrorSpanned(getApplicationContext(), 0x7f0b031c));
        }
        return checkFlag;
    }
	
	public static boolean isPhoneNumberValid(String phoneNumber)

    {

      boolean isValid = false;

      /* 可接受的电话格式有:

       * ^//(? : 可以使用 "(" 作为开头

       * (//d{3}): 紧接着三个数字

       * //)? : 可以使用")"接续

       * [- ]? : 在上述格式后可以使用具选择性的 "-".

       * (//d{4}) : 再紧接着三个数字

       * [- ]? : 可以使用具选择性的 "-" 接续.

       * (//d{4})$: 以四个数字结束.

       * 可以比较下列数字格式:

       * (123)456-78900, 123-4560-7890, 12345678900, (123)-4560-7890  

      */

      String expression = "^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";

      String expression2 ="^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";

      CharSequence inputStr = phoneNumber;

      /*创建Pattern*/

      Pattern pattern = Pattern.compile(expression);

      /*将Pattern 以参数传入Matcher作Regular expression*/

      Matcher matcher = pattern.matcher(inputStr);

      /*创建Pattern2*/

      Pattern pattern2 =Pattern.compile(expression2);

      /*将Pattern2 以参数传入Matcher2作Regular expression*/

      Matcher matcher2= pattern2.matcher(inputStr);

      if(matcher.matches()||matcher2.matches())

      {

        isValid = true;

      }

      return isValid; 

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
						showDialog(getResources().getString(R.string.upomp_bypay_registeractivity_succeed));
						RegisterActivity.this.finalize();
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
}
