package com.rqmod.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.weijia.mymod.R;
import com.weijia.mymod.R.raw;

public class ImageManager {
	
	private static ImageManager instance = null;
	private Context context;  
	private final int BUFFER_SIZE = 400000;
	
	ImageManager(Context context) {  
        this.context = context;  
	}
	
	public Bitmap getBitmap(String strFilePath)
	{
		File tempFile =new File( strFilePath.trim());	
		if(tempFile.exists())
		{
			return BitmapFactory.decodeFile(strFilePath);
		}
		
        String fileName = tempFile.getName(); 
        String strFileNameNoExt = fileName.substring(0,fileName.lastIndexOf("."));        
        
		Class<?> resource = R.class;

		try {
		
		    Class<?>[] classes = resource.getClasses();
		
		    for (Class<?> c : classes) {
		
		        int i = c.getModifiers();
		
		        String className = c.getName();
		
		        String s = Modifier.toString(i);
		
		        if (className.contains("R$raw")) {
		        	
		        	Field [] fds = c.getDeclaredFields();
		        	for(int i1 = 0 ; i1 < fds.length ; i1++)
		        	{
		        		String cn = fds[i1].getName();
		        		if(cn.equalsIgnoreCase(strFileNameNoExt))
		        		{
		        			int iValue = (Integer) fds[i1].get(null);
		        			savePic(strFilePath,iValue);
		        			return BitmapFactory.decodeFile(strFilePath);
		        		}
		        	}
		            
		
		        } else {
		
		            continue;
		
		        }
		
		    }
		
		} catch (Exception e) {
		
		    e.printStackTrace();
		
		}

		
		return null;
		
	}
	
	void savePic(String strFileName,int id)
    {
    	InputStream is = this.context.getResources().openRawResource(id); //欲导入的数据库  
        FileOutputStream fos;
        
		try {
			fos = new FileOutputStream(strFileName);			 
	        byte[] buffer = new byte[BUFFER_SIZE];  
	        int count = 0;  
        
			while ((count = is.read(buffer)) > 0) {  
			        fos.write(buffer, 0, count);  
			}
			fos.close();  
	        is.close(); 
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    }
	
	public static ImageManager getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new ImageManager(context);
		}
		return instance;
	}
}
