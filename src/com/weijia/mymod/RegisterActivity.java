package com.weijia.mymod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class RegisterActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("κ����Ƥ-�û�ע��");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.register);
		
		Button btnRegister;
		btnRegister = (Button)findViewById(R.id.regButton);
	
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText etNickName = (EditText)findViewById(R.id.nicknameEditText);
				EditText etTelNo = (EditText)findViewById(R.id.telEditText);
				EditText etPasswd = (EditText)findViewById(R.id.pwdEditText);
				EditText etAge = (EditText)findViewById(R.id.ageEditText);
				RadioGroup rgGender = (RadioGroup)findViewById(R.id.radioGroup);
				String strNickName = etNickName.getText().toString();
				String strTelNo = etTelNo.getText().toString();
				String strPasswd = etPasswd.getText().toString();
				String strAge = etAge.getText().toString();
				int idGender = rgGender.getCheckedRadioButtonId();
				
				Build bd = new Build();
				String strBrand = bd.BRAND;
				String strModel = bd.MODEL;
				String strOSType = "Android";
				
				if(strTelNo.equals("") && etNickName.equals(""))
				{
					showDialog("�ǳƻ��ֻ�����ȫΪ��");
					return;
				}
				
				if(strPasswd.equals(""))
				{
					showDialog("���벻��Ϊ��");
					return;
				}
				
				showDialog("�����ֻ���:Android/" + strBrand +"/" + strModel);
			}
		});
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
}
