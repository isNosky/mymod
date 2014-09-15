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
    public static final String DB_NAME = "rqdata.db"; //��������ݿ��ļ���  
    public static final String DB_SUBFDL = "db"; //��������ݿ��ļ���  
    public static final String PACKAGE_NAME = "mymod";//����  
//    public static final String DB_PATH = Environment.getExternalStorageDirectory().getPath()  
//                    + Environment.getDataDirectory().getAbsolutePath() + "/"  
//                    + PACKAGE_NAME + "/"  
//                    + DB_SUBFDL;  //���ֻ��������ݿ��λ��  
    public static final String DB_PATH = Environment.getDataDirectory().getAbsolutePath();
    private Context context;  
    private static final String CACHDIR = "mymodcache/";	
    
    DatabaseManager(Context context) {  
            this.context = context;  
            
    }  

    public SQLiteDatabase openDatabase() {  
    	
    	String strFilePath = this.context.getCacheDir().getAbsolutePath();
		File destDir = new File(strFilePath+"/db");
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		
		//��ʱ
		File destDir2 = new File(strFilePath+"/pic");
		if (!destDir2.exists()) {
			destDir2.mkdirs();
		}
				
		return this.openDatabase(strFilePath + "/db" + "/" + DB_NAME);  
    }  

    private void CopyAssetsFile()
    {
    	String savePath = getDirectory();
    	File dir = new File(savePath);
    	if (!dir.exists())
    		dir.mkdir();
  	  
    	String[] strFiles;
		try {
			strFiles = context.getResources().getAssets().list("json");
		
	    	for(int i = 0 ; i < strFiles.length ; i++)
	    	{
	    		String filename = savePath + "/" + strFiles[i];
	    		try {
	    				if (!(new File(filename)).exists()) {
							InputStream is = context.getResources().getAssets().open(strFiles[i]);
							FileOutputStream fos = new FileOutputStream(filename);
							byte[] buffer = new byte[7168];
							int count = 0;
							while ((count = is.read(buffer)) > 0) {
								fos.write(buffer, 0, count);
							}
	    		  	    fos.close();
	    		  	    is.close();
	    		  	   }
	    		  	  } catch (Exception e) {
	    		  	   e.printStackTrace();
	    		  	  }
	    	}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	  
    }
    
    /** ��û���Ŀ¼ **/
    private String getDirectory() {
        String dir = getSDPath() + "/" + CACHDIR;
        return dir;
    }
                                                                
    /** ȡSD��·�� **/
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);  //�ж�sd���Ƿ����
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //��ȡ��Ŀ¼
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    } 
    private SQLiteDatabase openDatabase(String dbfile)  {  
            try {
            		
            	//deleteDbFile(dbfile);
                    if (!(new File(dbfile).exists())) {
                            InputStream is = this.context.getResources().openRawResource(R.raw.rqdata); //����������ݿ�  
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
    
    public void deleteDbFile(String dbfile)
    {
    	File destDir2 = new File(dbfile);
		if (destDir2.exists()) {
			destDir2.delete();
		}
    }
}

