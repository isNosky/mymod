package com.weijia.mymod;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.rqmod.util.HttpUtil;

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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login_activity);
	        initBtn();
	        initEditTxt();
	        handleClickEvent();
		}
		catch(Exception e)
		{
			String str = e.getMessage();
			e.printStackTrace();
			System.out.println(str);
		}
	}
	private void initBtn() {
        //nameTextView = (TextView)findViewById(R.id.name);
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
				// TODO Auto-generated method stub
				 try {
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
                //intent.putExtra("com.360buy:navigationDisplayFlag", getIntent().getIntExtra("com.360buy:navigationDisplayFlag", 0x0));
                //intent.putExtra("com.360buy:loginResendFlag", getIntent().getIntExtra("com.360buy:loginResendFlag", -0x1));
                startActivity(intent);
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
//        findPassword.setOnClickListener(new View.OnClickListener() {
//        	@Override
//            public void onClick(View v) {
//                URLParamMap map = new URLParamMap();
//                if(TextUtils.isEmpty(findPdUrl)) {
//                    return;
//                }
//                map.put("to", findPdUrl.toString().trim());
//                CommonUtil.queryBrowserUrl("to", map, new CommonBase.BrowserUrlListener(this) {
//                    
//                    1(LoginActivity.9 p1) {
//                    }
//                    
//                    public void onComplete(String url) {
//                        LoginActivity.9.this$0.post(new Runnable(this, url) {
//                            
//                            1(LoginActivity.9.1 p1, String p2) {
//                            }
//                            
//                            public void run() {
//                                Intent intent = new Intent(LoginActivity.9.this$0, WebActivity.class);
//                                intent.putExtra("url", url);
//                                intent.putExtra("com.360buy:navigationDisplayFlag", LoginActivity.9.this$0.getIntent().getIntExtra("com.360buy:navigationDisplayFlag", 0x0));
//                                LoginActivity.9.this$0.startActivityInFrame(intent);
//                            }
//                        });
//                    }
//                });
//            }
//        });
    }
	
	 private boolean nameCheck() {
	        boolean checkFlag = false;
	        String checkStr = mUserNameTxt.getText().toString();
	        if(TextUtils.isEmpty(checkStr.trim())) {
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
}