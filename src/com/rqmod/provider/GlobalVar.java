package com.rqmod.provider;

import java.util.ArrayList;

import com.rqmod.util.Constant;
import com.weijia.mymod.LoginActivity;
import com.weijia.mymod.MenuActivity;
import com.weijia.mymod.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class GlobalVar {
	 
    private GlobalVar() {
		super();
		this.userid = BLANK;
		CellphoneNumber = CELLPHONENUM;
	}

	private int userid; 
    private String CellphoneNumber;
    private String token;
    private String password;
    private ArrayList<Activity> activities = new ArrayList<Activity>();
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private static GlobalVar app = null;
      
    public String getCellphoneNumber() {
		return CellphoneNumber;
	}

	public void setCellphoneNumber(String cellphoneNumber) {
		CellphoneNumber = cellphoneNumber;
	}

    public int getUserId() {  
            return userid;  
    }  

    public void setUserId(int userid) {  
            this.userid = userid;  
    }  
      
    private static final int BLANK = -1;  
    private static final String CELLPHONENUM = "";
    
    public static GlobalVar getInstance()
    {
    	if(null == app)
    	{
    		app = new GlobalVar();
    	}
    	return app;
    }
    
    public void saveActivity(Activity a)
    {
    	activities.add(a);
    }
    public void clearDB(Context context)
    {
    	SQLiteDatabase db = null;
		db = DatabaseManager.getInstance(context).openDatabase();
		
    	String strSql = "delete from tbl_order";
	    db.execSQL(strSql);
	    
	    strSql = "delete from tbl_order_history";
	    db.execSQL(strSql);
	    
	    strSql = "delete from tbl_addr";
	    db.execSQL(strSql);
	    
	    strSql = "delete from tbl_shopcar";
	    db.execSQL(strSql);
    }
    
    public void exitSystem()
    {
    	for(int i = 0 ; i < activities.size() ; i++)
    	{
    		Activity a = activities.get(i);
    		if(a != null)
    		{
    			a.finish();
    		}
    	}
    }
}
