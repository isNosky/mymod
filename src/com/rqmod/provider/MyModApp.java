package com.rqmod.provider;

import android.app.Application;

public class MyModApp  extends Application {  
	 
    private int userid; 
    private String CellphoneNumber;
    private MyModApp app;
      
    public String getCellphoneNumber() {
		return CellphoneNumber;
	}

	public void setCellphoneNumber(String cellphoneNumber) {
		CellphoneNumber = cellphoneNumber;
	}

	@Override 
    public void onCreate() {  
            super.onCreate();  
            setUserId(BLANK); //初始化全局变量  
            setCellphoneNumber(CELLPHONENUM);
    }  

    public int getUserId() {  
            return userid;  
    }  

    public void setUserId(int userid) {  
            this.userid = userid;  
    }  
      
    private static final int BLANK = -1;  
    private static final String CELLPHONENUM = "";
    
    public MyModApp getInstance()
    {
    	if(null == app)
    	{
    		app = new MyModApp();
    	}
    	return app;
    }
} 
