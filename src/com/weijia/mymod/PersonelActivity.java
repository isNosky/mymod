package com.weijia.mymod;

import com.rqmod.provider.DatabaseManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
			tvUserLevel.setText("Õ≠≈∆”√ªß");
			tvUserScore.setText("0");
		}
	}
	class ClickListener implements View.OnClickListener {
        
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()) {
               
                case R.id.my_account:
                {
//                    PersonelActivity.ClickListener runnableAccountCenter = new Runnable() {                        
//                        public void run() {
//                            Intent account = new Intent(PersonelActivity.ClickListener.this, AccountMgrActivity.class);
//                            PersonelActivity.ClickListener.startActivity(account);
//                        }
//                    };
//                    LoginUser.getInstance().executeLoginRunnable(this, runnableAccountCenter);
                	Intent account = new Intent(PersonelActivity.this, AccountMgrActivity.class);
                	startActivity(account);
                    return;
                }
//                case 2131361800:
//                {
//                    MainFrameActivity.menuItemClick(v.getId(), this$0);
//                    return;
//                }
                case R.id.my_material_flow:
                {
//                    PersonelActivity.ClickListener.7 runnableAllOrder = new Runnable(this) {
//                        public void run() {
//                            Intent intent = new Intent(PersonelActivity.ClickListener.this$0, MyOrderInfoListActivity.class);
//                            intent.putExtra("functionId", "allOrdersFunctionList");
//                            intent.putExtra("title", PersonelActivity.ClickListener.this$0.getString(0x7f0b059a));
//                            intent.putExtra("com.360buy:navigationDisplayFlag", -0x1);
//                            PersonelActivity.ClickListener.this$0.startActivityInFrame(intent);
//                        }
//                    };
//                    LoginUser.getInstance().executeLoginRunnable(this$0, runnableAllOrder);
                	Intent intent1 = new Intent(PersonelActivity.this, MyOrderInfoListActivity.class);
                	startActivity(intent1);
                    return;
                }  
                
                case R.id.personel_logout_but:
                {
                	
                }
            }
        }
    }
}
