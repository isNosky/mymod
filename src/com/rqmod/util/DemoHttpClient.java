package com.rqmod.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.net.ParseException;
import android.os.Environment;

public class DemoHttpClient implements HttpClient {
	
	private static final String CACHDIR = "mymodcache/";	
		
	private String BuildResonse(HttpUriRequest request)
	{
		String strResponse;
		URI u = request.getURI();
		String strPath = u.getPath();
		int iPos = strPath.lastIndexOf("/");
		String servlet = strPath.substring(iPos);
		
		return getResponse(servlet);
	}
	
	private String getResponse(String servlet)
	{
		String filename = getDirectory() + servlet + ".json";
		File file=new File(filename);
		if(!file.exists()||file.isDirectory())
			return "";
		 
		byte[] buf = new byte[1024];
		StringBuffer sb=new StringBuffer();
		try {
			FileInputStream fis=new FileInputStream(file);
			while((fis.read(buf))!=-1) {
				sb.append(new String(buf));    
				buf=new byte[1024];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();		

	}
	
	 /** 获得缓存目录 **/
    private String getDirectory() {
        String dir = getSDPath() + "/" + CACHDIR;
        return dir;
    }
                                                                
    /** 取SD卡路径 **/
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }

	@Override
	public HttpResponse execute(HttpUriRequest request) throws IOException,
			ClientProtocolException {
		HttpResponse response = new BasicHttpResponse(null);
		response.setStatusCode(200);
		try {  
			String strRsp = BuildResonse(request);
            HttpEntity entity = new StringEntity(strRsp, "UTF-8");  
            response.setEntity(entity);
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();
        } catch (ParseException e) {  
        	e.printStackTrace();
        } catch (IOException e) {  
        	e.printStackTrace();
        }  
		
		return response;	
	}

	@Override
	public HttpResponse execute(HttpUriRequest request, HttpContext context)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request,
			HttpContext context) throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpUriRequest arg0,
			ResponseHandler<? extends T> arg1, HttpContext arg2)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2) throws IOException,
			ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2, HttpContext arg3)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientConnectionManager getConnectionManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpParams getParams() {
		// TODO Auto-generated method stub
		return null;
	} 
}
