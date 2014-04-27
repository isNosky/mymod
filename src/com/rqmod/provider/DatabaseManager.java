package com.rqmod.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.weijia.mymod.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DatabaseManager {
	private static DatabaseManager dbm = null;
	private final int BUFFER_SIZE = 400000;  
    public static final String DB_NAME = "rqdata.db"; //保存的数据库文件名  
    public static final String DB_SUBFDL = "db"; //保存的数据库文件名  
    public static final String PACKAGE_NAME = "rqmod";//包名  
//    public static final String DB_PATH = Environment.getExternalStorageDirectory().getPath()  
//                    + Environment.getDataDirectory().getAbsolutePath() + "/"  
//                    + PACKAGE_NAME + "/"  
//                    + DB_SUBFDL;  //在手机里存放数据库的位置  
    public static final String DB_PATH = "/data/rqmod/db";
    private Context context;  

    DatabaseManager(Context context) {  
            this.context = context;  
    }  

    public SQLiteDatabase openDatabase() {  
            return this.openDatabase(DB_PATH + "/" + DB_NAME);  
    }  

    private SQLiteDatabase openDatabase(String dbfile) {  
            try {  
                    if (!(new File(dbfile).exists())) {
                            InputStream is = this.context.getResources().openRawResource(R.raw.rqdata); //欲导入的数据库  
                            FileOutputStream fos = new FileOutputStream(dbfile);  
                            byte[] buffer = new byte[BUFFER_SIZE];  
                            int count = 0;  
                            while ((count = is.read(buffer)) > 0) {  
                                    fos.write(buffer, 0, count);  
                            }  
                            fos.close();  
                            is.close();  
                    }  
                    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,  
                                    null);  
                    return db;  
            } catch (FileNotFoundException e) {  
                    Log.e("Database", "File not found");  
                    e.printStackTrace();  
            } catch (IOException e) {  
                    Log.e("Database", "IO exception");  
                    e.printStackTrace();  
            }  
            return null;  
    }
    
    public static DatabaseManager getInstance(Context context)
    {
    	if(dbm == null)
    	{
    		dbm = new DatabaseManager(context);
    	}
		return dbm;
    	
    }
}

