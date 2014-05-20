package com.weijia.mymod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.register_activity);
			
			etUserName = (EditText)findViewById(R.id.register_input_name);
			etPassword = (EditText)findViewById(R.id.register_page_input_password);
			etConfirmPassword = (EditText)findViewById(R.id.register_input_confirm_password);
			
			btnRegister = (Button)findViewById(R.id.register_top);
			
			btnRegister.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String strPass = etPassword.getEditableText().toString();
					String strConfirmPass = etConfirmPassword.getEditableText().toString();
					if(!strPass.equals(strConfirmPass))
					{
						showDialog(getResources().getString(R.string.must_password_equal));
					}
					JSONObject jsoin = null;					
					JSONObject jsonout = HttpUtil.queryStringForPost(Constant.REGISTERSERVLET, jsoin);
				}});
			
			if(!nameCheck())
			{
				showDialog(getResources().getString(R.string.must_login_use_phone_num));
			}
			
			
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
		       .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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

      /* �ɽ��ܵĵ绰��ʽ��:

       * ^//(? : ����ʹ�� "(" ��Ϊ��ͷ

       * (//d{3}): ��������������

       * //)? : ����ʹ��")"����

       * [- ]? : ��������ʽ�����ʹ�þ�ѡ���Ե� "-".

       * (//d{4}) : �ٽ�������������

       * [- ]? : ����ʹ�þ�ѡ���Ե� "-" ����.

       * (//d{4})$: ���ĸ����ֽ���.

       * ���ԱȽ��������ָ�ʽ:

       * (123)456-78900, 123-4560-7890, 12345678900, (123)-4560-7890  

      */

      String expression = "^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";

      String expression2 ="^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";

      CharSequence inputStr = phoneNumber;

      /*����Pattern*/

      Pattern pattern = Pattern.compile(expression);

      /*��Pattern �Բ�������Matcher��Regular expression*/

      Matcher matcher = pattern.matcher(inputStr);

      /*����Pattern2*/

      Pattern pattern2 =Pattern.compile(expression2);

      /*��Pattern2 �Բ�������Matcher2��Regular expression*/

      Matcher matcher2= pattern2.matcher(inputStr);

      if(matcher.matches()||matcher2.matches())

      {

        isValid = true;

      }

      return isValid; 

    }
}
