package com.weijia.mymod;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.GlobalVar;
import com.rqmod.provider.MyModApp;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;
import com.rqmod.util.StringEncrypt;

public class LoginActivity extends Activity {

	public static final String AUTO_SAVE_USER_NAME = "auto_save_user_name_for_select";
    public static final String FIND_PD_KEY = "findpwd";
    public static final int NAME_LOG_IN = 0x0;
    public static final int PHONE_LOG_IN = 0x1;
    public static final String PREF_LOGIN = "loginFlag";
    public static final String PREF_NAME = "userInfo";
    public static final String RESEND_FLAG = "com.360buy:loginResendFlag";
    private static final int RETRY_TIMES = 0x3;
    private static final String TAG = "LoginActivity";
    public static final int TO_FINISH = 0x0;
    public static final int TO_JD_ACCOUNT_SAFE = 0x7;
    public static final int TO_JD_COLLECT = 0x4;
    public static final int TO_JD_COMMENT_DISCUSS = 0x5;
    public static final int TO_JD_EASY_BUY = 0x6;
    public static final int TO_JD_GAME = 0x2;
    public static final int TO_JD_MESSAGE = 0x3;
    public static final int TO_JD_ORDER = 0x9;
    public static final int TO_JD_REPAIR_CHANGE = 0x8;
    public static final int TO_MY_JD = 0x1;
    public static final int TO_UNKNOW = -0x1;
    private AlertDialog alertDialog;
    private boolean bRememberMe;
    private TextView findPassword;
    private String findPdUrl;
    private LinearLayout historyUserNameLayout;
    private View loginDividerLine;
    View.OnClickListener loginNameSelectorClickListener;
    private ImageView login_name_selector;
    private RelativeLayout login_page_input_name_layout;
    private ImageView login_user_icon;
    private Context mContext;
    
    private TextView mInputmethodView;
    Button mLoginCancel;
    Button mLoginConfirm;
    //LoginUserBase mLoginUser;
    Button mRegLink;
    CheckBox mRememberMe;
    TextView mTitle;
    private EditText mUserNameTxt;
    private static EditText mUserPassword = null;
    TextView nameTextView;
    private ArrayList<String> names;
    public static final String passwordTag = "jdPasswordTag";
    TextView passwordTextView;
    String sUserName;
    String sUserPassword;
    EditText etName;
    EditText etPasswd;
    private static final String TBL_USER = "tbl_user";
    
    DatabaseManager dbm = null;
	SQLiteDatabase db = null;
	
	int iFlag = -1;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
			case RESULT_OK:
				Bundle b=data.getExtras();  //data为B中回传的Intent
				String strUser = b.getString("UserName");
				String strPass = b.getString("Password");
				
				mUserNameTxt.setText(strUser);
				mUserPassword.setText(strPass);
				
				mLoginConfirm.performClick();
				break;
			default:
		          break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login_activity);
			dbm = DatabaseManager.getInstance(this);
			db = dbm.openDatabase();
			
			Intent intent = getIntent();
	        iFlag = intent.getFlags();
			
	        initBtn();
	        initEditTxt();
	        handleClickEvent();
	        hideOrShowText();
		}
		catch(Exception e)
		{
			String str = e.getMessage();
			e.printStackTrace();
			System.out.println(str);
		}
		
		GlobalVar.getInstance().saveActivity(this);
	}
	private void initBtn() {
        //nameTextView = (TextView)findViewById(R.id.login_input_name);
        //passwordTextView = (TextView)findViewById(0x7f0a04a1);		
		
        mLoginConfirm = (Button)findViewById(R.id.login_comfirm_button);
        mRegLink = (Button)findViewById(R.id.register_link);
        mRememberMe = (CheckBox)findViewById(R.id.remember_user);
        login_user_icon = (ImageView)findViewById(R.id.login_user_icon);
        findPassword = (TextView)findViewById(R.id.login_page_find_password);
        login_name_selector = (ImageView)findViewById(R.id.login_name_selector);
        login_page_input_name_layout = (RelativeLayout)findViewById(R.id.login_page_input_name_layout);
        login_name_selector.setClickable(true);
        login_name_selector.setTag(Boolean.valueOf(false));
        login_name_selector.setOnClickListener(loginNameSelectorClickListener);
        //mTitle = (TextView)findViewById(0x7f0a00b1);
        //mTitle.setText(0x7f0b03d4);
        historyUserNameLayout = (LinearLayout)findViewById(R.id.history_user_name_layout);
        loginDividerLine = findViewById(R.id.login_divider_line);
        etName = (EditText)findViewById(R.id.login_input_name);          	
    }
	
	
	private void initEditTxt() {
        mUserNameTxt = (EditText)findViewById(R.id.login_input_name);
        mUserPassword = (EditText)findViewById(R.id.login_input_password);
        mUserPassword.setTag("jdPasswordTag");
//        mInputmethodView = (TextView)findViewById(0x7f0a04ab);
        
//        mUserNameTxt.addTextChangedListener(new TextWatcher(this) {
//            
//        	void TextWatcher(LoginActivity p1) {
//            }
//            
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(historyUserNameLayout.getVisibility() == 0) {
//                }
//            }
//            
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if((s != null) && (!TextUtils.isEmpty(s. .getText()))) {
//                    s.setText("");
//                }
//                if((mUserNameTxt == null) || (mUserNameTxt.getText() == null)) {
//                    return;
//                }
//                String userName = mUserNameTxt.getText().toString();
//                if(!TextUtils.isEmpty(userName)) {
//                    Bitmap bitmap = UserPhotoUpload.getUserBitmapPhotoFromCache(userName);
//                    if(bitmap != null) {
//                        int size = getResources().getDimensionPixelSize(0x7f090079);
//                        int padding = DPIUtil.dip2px(10.0f);
//                        login_user_icon.setPadding(padding, padding, padding, padding);
//                        login_user_icon.setImageBitmap(ImageUtil.toRoundBitmap(bitmap, size, size));
//                        return;
//                    }
//                    login_user_icon.setPadding(0x0, 0x0, 0x0, 0x0);
//                    login_user_icon.setImageResource(0x7f0202bc);
//                }
//            }
//         
//			
//        });
    }
	
	private void handleClickEvent() {
        ScrollView scrollview = (ScrollView)findViewById(R.id.login_scroller);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if((event.getAction() == 0x2) && (historyUserNameLayout.getVisibility() == 0)) {
                    return true;
                }
                return false;
			}
		});
        mLoginConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//1、首先检查各个参数的有效性
				if(nameCheck())
				{
					showDialog(getResources().getString(R.string.must_login_use_phone_num));
				}
				
				// TODO 向网络请求登录
				try {
					
					 	if(Constant.FLAG_POST_IN_JSON)
						{								
					 		Thread thread = new Thread(){ 
						    	@Override 
							    public void run() { 	
						    		JSONObject jsonout = null;
							    	try { 							    		
							    		JSONObject jsoin = getLoginJSO();
										jsonout = HttpUtil.queryStringForPost(Constant.LOGINSERVLET, jsoin);
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
						else
						{						    
						    
						    Thread thread = new Thread(){ 
						    	@Override 
							    public void run() { 	
						    		JSONObject jsonout = null;
							    	try { 							    		
							    		List<NameValuePair> postParameters = getLoginPara();				            
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
		});
        mRegLink.setOnClickListener(new View.OnClickListener() {        
        	@Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 0);
            }
        });
        mRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        	@Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch(buttonView.getId()) {
                    case R.id.remember_user:
                    {
                        if(isChecked) {
                            bRememberMe = true;
                            return;
                        }
                        bRememberMe = false;
                        break;
                    }
                }
            }
        });
        mRememberMe.setChecked(true);
        SharedPreferences preferences = getSharedPreferences(sUserName, 0);
        if(preferences != null) {
            findPdUrl = preferences.getString("findpwd", "");
            if(TextUtils.isEmpty(findPdUrl)) {
                findPassword.setVisibility(0x8);
            }
        } else {
            findPassword.setVisibility(0x8);
        }
    }
	
	private JSONObject getLoginJSO() throws JSONException
	{
		JSONObject jso = new JSONObject();
		jso.put("PhoneNum", mUserNameTxt.getEditableText().toString());
		jso.put("NickName", mUserNameTxt.getEditableText().toString());
		jso.put("Password", StringEncrypt.Encrypt(mUserPassword.getEditableText().toString(), "SHA-256"));
		return jso;
	}
	
	private List<NameValuePair> getLoginPara() 
	{
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("LoginName", mUserNameTxt.getEditableText().toString()));
        postParameters.add(new BasicNameValuePair("Password", StringEncrypt.Encrypt(mUserPassword.getEditableText().toString(), "SHA-256")));

        return postParameters;
	}
	
	private int HandleLoginResult(JSONObject jso)
	{
		if(jso != null)
		{
			try {
				int iErrCode = jso.getInt("ErrorCode");
				int iUserId = jso.getInt("UserID");
				String strToken = jso.getString("token");
				//TODO:存入数据库
				 ContentValues values = new ContentValues();
				 String strNickName = mUserNameTxt.getEditableText().toString();
				 values.put("nickname", strNickName);
				 values.put("id", iUserId);
				 String strPhoneNum = mUserNameTxt.getEditableText().toString();
				 values.put("phonenum", strPhoneNum);
				 String strPassword = StringEncrypt.Encrypt(mUserPassword.getEditableText().toString(), "SHA-256");
				 values.put("password", strPassword);
				 values.put("islogin", 1);
				 
				 Cursor count = db.rawQuery("select count(1) userscount from tbl_user where id = " + iUserId, null);
				 count.moveToFirst();
				 int iColIdx = count.getColumnIndex("userscount");
				 int userscount = count.getInt(iColIdx);
				 count.close();
				 
				 GlobalVar.getInstance().setToken(strToken);
				 GlobalVar.getInstance().setUserId(iUserId);
				 GlobalVar.getInstance().setCellphoneNumber(strPhoneNum);
				 GlobalVar.getInstance().setPassword(strPassword);
				 
				 if(0 == userscount)
				 {
					 if(-1 == db.insert(TBL_USER, null, values))				 
					 {
						 
					 }
					 else
					 {
						
					 }
				 }
				 else
				 {
					 try {
							
							  String strSql = "update tbl_user set islogin = 1" + " where id = " + String.valueOf(iUserId);
							  db.execSQL(strSql);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							String str = e.getMessage();
							e.printStackTrace();
						}
				 }
				 Intent intent = null;
				 if(iFlag == Constant.LOGIN_MSG)
				 {
					 intent = new Intent(LoginActivity.this, PersonelActivity.class);
				 }
				 else
				 {
					 intent = new Intent(LoginActivity.this, MenuActivity.class);
				 }
				 setResult(RESULT_OK,intent); 
				 LoginActivity.this.finish();
				return 1;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
		
	}
	 private boolean nameCheck() {
	        boolean checkFlag = false;
	        String checkStr = mUserNameTxt.getText().toString();
	        if(TextUtils.isEmpty(checkStr.trim())
	        		&& !isPhoneNumberValid(checkStr.trim())) {
	            checkFlag = true;
	            //mUserNameTxt.setError(JDStringUtils.getErrorSpanned(getApplicationContext(), 0x7f0b031c));
	        }
	        return checkFlag;
	    }
	    
	    private boolean passWordCheck() {
	        boolean checkFlag = false;
	        String checkStr = mUserPassword.getText().toString();
	        if(TextUtils.isEmpty(checkStr.trim())) {
	            checkFlag = true;
	            //mUserPassword.setError(JDStringUtils.getErrorSpanned(getApplicationContext(), 0x7f0b031d));
	        }
	        return checkFlag;
	    }
	    
	    private void getUserName() {
	        sUserName = mUserNameTxt.getText().toString();
	    }
	    
	    private void getUserPassword() {
	        sUserPassword = EncryptPassword2(mUserPassword.getText().toString());
	    }
	    
	    private void setUserName(String name) {
	        String oldName = mUserNameTxt.getText().toString();
	        if(TextUtils.equals(name, oldName)) {
	            return;
	        }
	        mUserPassword.setText("");
	        mUserNameTxt.setText(name);
	        if(!TextUtils.isEmpty(name)) {
	            mUserNameTxt.setSelection(name.length());
	        }
	    }
	    
	    private void setUserPassword(String password) {
	        if(password.length() > 0) {
	            mUserPassword.setText(password);
	        }
	    }
	    
	    private void getUserPasswordNoCode() {
	        sUserPassword = mUserPassword.getText().toString();
	    }
	    
	    public static String EncryptPassword2(String str) {
	        MessageDigest messageDigest = null;
	        try {
	            messageDigest = MessageDigest.getInstance("MD5");
	            messageDigest.reset();
	            //messageDigest.update(getBytes("UTF-8"));
	        } catch(NoSuchAlgorithmException localNoSuchAlgorithmException1) {
	        }
	        byte[] byteArray = messageDigest.digest();
	        StringBuffer md5StrBuff = new StringBuffer();
//	        if((int i = 0x0) >= byteArray.length) {
//	            i = i + 0x1;
//	            return byteArray.length;
//	        }
//	        if(Integer.toHexString((byteArray[i] & 0xff)).length() == 0x1) {
//	            md5StrBuff.append("0").append(Integer.toHexString((byteArray[i] & 0xff)));
//	        }
//	        md5StrBuff.append(Integer.toHexString((byteArray[i] & 0xff)));
	        return md5StrBuff.toString().toUpperCase();
	    }
	    
//	    public void onRemember() {
//	        if((nameCheck()) || (passWordCheck())) {
//	            return;
//	        }
//	        getUserName();
//	        String password = SafetyManager.getPassword();
//	        password == null ? password : (mUserPassword.getText().toString().equals(""))) {
//	            getUserPasswordNoCode();
//	        }
//	        getUserPassword();
//	        SafetyManager.saveSafety(sUserName, sUserPassword, true);
//	    }
//	    
//	    public static void clearRemember(boolean username, boolean password, boolean remember) {
//	        SafetyManager.clearSafety();
//	        if(username) {
//	            SafetyManager.removeUsername();
//	        }
//	        if(password) {
//	            SafetyManager.removePassword();
//	        }
//	        if(remember) {
//	            SafetyManager.removeRemember();
//	        }
//	    }
	    
	    private void getRememberedUser() {
	        String name;// = SafetyManager.getUserName();
	        String passowrd;// = SafetyManager.getPassword();
	        setUserName("");
	        setUserPassword("");
	    }
	    
	    public void onStart() {
	        super.onStart();
	    }
	    
	    protected void onPause() {
	        //closeJDInputMethod();
	        super.onPause();
	    }
	    
	    private void hideOrShowText() {
	    	
	        CheckBox slipButton = (CheckBox)findViewById(R.id.login_switchBtn);
	        slipButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	                EditText etPasswd = (EditText)findViewById(R.id.login_input_password);
	                String pwTextString;
	                
	                try {
						if(etPasswd == null) {
						    pwTextString = "";
						} else {
						    pwTextString = etPasswd.getText().toString();
						}
						if(isChecked) {
							etPasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
						} else {
							etPasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
						}
						if(!TextUtils.isEmpty(pwTextString)) {
							etPasswd.setSelection(pwTextString.length());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						String str = e.getMessage();
						e.printStackTrace();
					}
	            }
	        });
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
	    private void showDialog(String msg){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(msg)
			       .setCancelable(false)
			       .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
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
							//showDialog(getResources().getString(R.string.signin_login_success));
							HandleLoginResult(jsonout);
							
//							int iUserId = (Integer) jsonout.get(Constant.PARA_USER_ID);
//							GlobalVar app = GlobalVar.getInstance();
//							app.setUserId(iUserId);
							//登录成功,登录页面消失
							
							//刷新个人信息页面
							
						}
						else
						{
							showDialog(getResources().getString(R.string.signin_login_fail));
							//showDialog(strErrDesc);
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