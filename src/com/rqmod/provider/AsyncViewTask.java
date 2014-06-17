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

public class AsyncViewTask extends AsyncTask<View, Void, Drawable> {
    private ImageView mView;
    private HashMap<String, SoftReference<Drawable>> imageCache;
 
    public AsyncViewTask() {
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }
 
    protected Drawable doInBackground(View... views) {
        Drawable drawable = null;
        View view = views[0];
        if (view.getTag() != null) {
            if (imageCache.containsKey(view.getTag())) {
                SoftReference<Drawable> cache = imageCache.get(view.getTag().toString());
                drawable = cache.get();
                if (drawable != null) {
                    return drawable;
                }
            }
            try {
            	String strURL = Constant.URL + view.getTag().toString();	
                if (URLUtil.isHttpUrl(strURL)) {// 如果为网络地址。则连接url下载图片
                    URL url = new URL(strURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    drawable = Drawable.createFromStream(stream, "src");
                    stream.close();
                } else {// 如果为本地数据，直接解析
                    drawable = Drawable.createFromPath(view.getTag().toString());
                }
            } catch (Exception e) {
                Log.v("img", e.getMessage());
                return null;
            }
        }
        this.mView = (ImageView) view;
        return drawable;
    }
 
    protected void onPostExecute(Drawable drawable) {
        if (drawable != null) {
            this.mView.setImageDrawable(drawable);
            this.mView = null;
        }
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
