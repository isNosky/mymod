package com.rqmod.provider;

import android.app.Application;

public class MyModApp  extends Application {  
	 
    private int userid;  
      
    @Override 
    public void onCreate() {  
            super.onCreate();  
            setUserId(BLANK); //初始化全局变量  
    }  

    public int getUserId() {  
            return userid;  
    }  

    public void setUserId(int userid) {  
            this.userid = userid;  
    }  
      
    private static final int BLANK = -1;  
} 
