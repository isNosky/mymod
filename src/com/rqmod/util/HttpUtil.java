package com.rqmod.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.util.EntityUtils; 
import org.apache.http.message.BasicNameValuePair;   
import org.apache.http.protocol.HTTP;   
import org.apache.http.util.EntityUtils;

import com.rqmod.util.Constant;
public class HttpUtil {
	
	public static HttpClient httpClient=new DefaultHttpClient();
	static String DEFAULT_ENCODING = "UTF-8";
		// 获得Get请求对象request
	public static HttpGet getHttpGet(String servlet){
		String url = Constant.getURL() + servlet;
		HttpGet request = new HttpGet(url);
		
		return request;
	}
	// 获得Post请求对象request
	public static HttpPost getHttpPost(String servlet,JSONObject jso){
		
		String url = Constant.getURL() + servlet;
		HttpPost request = new HttpPost(url);
		ByteArrayEntity arrayEntity = null;
		try {
		String params = jso.toString();
		
		byte[] jsonByte = null;
		
			jsonByte = params.getBytes(DEFAULT_ENCODING);
			arrayEntity = new ByteArrayEntity(jsonByte); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(arrayEntity);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		return request;
	}
	
	public static HttpPost getHttpPost(String servlet,List<NameValuePair> postParameters){
		
		String url = Constant.getURL() + servlet;
		HttpPost request = new HttpPost(url);
		UrlEncodedFormEntity formEntity = null;
		try {
			formEntity = new UrlEncodedFormEntity(postParameters,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        request.setEntity(formEntity);
		request.setHeader("Accept", "application/json");
		//request.setHeader("Content-type", "text/html; charset=utf-8");
		
		return request;
	}
	// 根据请求获得响应对象response
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = httpClient.execute(request);
		return response;
	}
	// 根据请求获得响应对象response
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = httpClient.execute(request);
		return response;
	}
	
	// 发送Post请求，获得响应查询结果
	public static JSONObject queryStringForPost(String servlet,JSONObject jso){
		// 根据url获得HttpPost对象
		
		HttpPost request = HttpUtil.getHttpPost(servlet,jso);
		JSONObject jsoOut = null;
		
		try {
			// 获得响应对象
			HttpResponse response = HttpUtil.getHttpResponse(request);
			
			// 判断是否请求成功
			if(response.getStatusLine().getStatusCode()==200){
				// 获得响应
				byte[] responseByte;				
				String result = null;
				responseByte = EntityUtils.toByteArray(response.getEntity());
				result = new String(responseByte, 0, responseByte.length, DEFAULT_ENCODING);
				jsoOut = new JSONObject(result);
				return jsoOut;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return jsoOut;
		} catch (IOException e) {
			e.printStackTrace();
			return jsoOut;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
	
	// 发送Post请求，获得响应查询结果
		public static JSONObject queryStringForPost(String servlet,List<NameValuePair> postParameters){
			// 根据url获得HttpPost对象
			
			HttpPost request = HttpUtil.getHttpPost(servlet,postParameters);			
			JSONObject jsoOut = null;
			
			try {
				// 获得响应对象
				HttpResponse response = HttpUtil.getHttpResponse(request);
				
				// 判断是否请求成功
				if(response.getStatusLine().getStatusCode()==200){
					// 获得响应
					byte[] responseByte;				
					String result = null;
					responseByte = EntityUtils.toByteArray(response.getEntity());
					result = new String(responseByte, 0, responseByte.length, DEFAULT_ENCODING);
					jsoOut = new JSONObject(result);
					return jsoOut;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return jsoOut;
			} catch (IOException e) {
				e.printStackTrace();
				return jsoOut;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	    }
	
	// 获得响应查询结果
	public static String queryStringForPost(HttpPost request){
		String result = null;
		try {
			// 获得响应对象
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// 判断是否请求成功
			if(response.getStatusLine().getStatusCode()==200){
				// 获得响应
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "网络异常！";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "网络异常！";
			return result;
		}
        return null;
    }
	// 发送Get请求，获得响应查询结果
	public static  String queryStringForGet(String url){
		// 获得HttpGet对象
		HttpGet request = HttpUtil.getHttpGet(url);
		String result = null;
		try {
			// 获得响应对象
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// 判断是否请求成功
			if(response.getStatusLine().getStatusCode()==200){
				// 获得响应
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "网络异常！";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "网络异常！";
			return result;
		}
        return null;
    }
}
