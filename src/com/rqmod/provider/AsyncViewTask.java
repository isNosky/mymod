package com.rqmod.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.rqmod.util.Constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;

public class AsyncViewTask extends AsyncTask<View, Void, Bitmap> {
    private ImageView mView;
    private ImageMemoryCache memoryCache;
    private ImageFileCache fileCache;
    private HashMap<String, SoftReference<Drawable>> imageCache;
 
    public AsyncViewTask(Context context) {
        imageCache = new HashMap<String, SoftReference<Drawable>>();
        memoryCache = new ImageMemoryCache(context);
        fileCache = new ImageFileCache();
    }
 
    protected Bitmap doInBackground(View... views) {
   	
        View view = views[0];
        String strURL = Constant.getURL() + view.getTag().toString();	
    	Bitmap bm = getBitmap(strURL);        
        this.mView = (ImageView) view;
        return bm;
    }
 
    protected void onPostExecute(Bitmap bm) {
        if (bm != null) {
            this.mView.setImageBitmap(bm);
            this.mView = null;
        }
    }
    
    public Bitmap getBitmap(String url) {
        // ���ڴ滺���л�ȡͼƬ
        Bitmap result = memoryCache.getBitmapFromCache(url);
        if (result == null) {
            // �ļ������л�ȡ
            result = fileCache.getImage(url);
            if (result == null) {
                // �������ȡ
                result = ImageGetFromHttp.downloadBitmap(url);
                if (result != null) {
                    fileCache.saveBitmap(result, url);
                    memoryCache.addBitmapToCache(url, result);
                }
            } else {
                // ��ӵ��ڴ滺��
                memoryCache.addBitmapToCache(url, result);
            }
        }
        return result;
    }
    
    public static class ImageLoader {  
    	  
        public static Bitmap loadImage(String url) {  
            Bitmap bitmap = null;  
            HttpClient client = new DefaultHttpClient();  
            HttpResponse response = null;  
            InputStream inputStream = null;  
            try {  
                response = client.execute(new HttpGet(url));  
                HttpEntity entity = response.getEntity();  
                inputStream = entity.getContent();  
                bitmap = BitmapFactory.decodeStream(inputStream);  
            } catch (ClientProtocolException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            return bitmap;  
        }  
    }  
 
}
