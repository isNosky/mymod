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
	
	static String DEFAULT_ENCODING = "UTF-8";
	// ����URL
	public static final String BASE_URL="http://10.0.2.2:8080/WirelessOrder_Server/";
	// ���Get�������request
	public static HttpGet getHttpGet(String servlet){
		String url = Constant.URL + servlet;
		HttpGet request = new HttpGet(url);
		
		return request;
	}
	// ���Post�������request
	public static HttpPost getHttpPost(String servlet,JSONObject jso){
		String url = Constant.URL + servlet;
		HttpPost request = new HttpPost(url);

		String params = jso.toString();
		ByteArrayEntity arrayEntity = null;
		byte[] jsonByte = null;
		try {
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
	// ������������Ӧ����response
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	// ������������Ӧ����response
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	// ����Post���󣬻����Ӧ��ѯ���
	public static JSONObject queryStringForPost(String servlet,JSONObject jso){
		// ����url���HttpPost����
		
		HttpPost request = HttpUtil.getHttpPost(servlet,jso);
		JSONObject jsoOut = null;
		
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// �ж��Ƿ�����ɹ�
			if(response.getStatusLine().getStatusCode()==200){
				// �����Ӧ
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
	// �����Ӧ��ѯ���
	public static String queryStringForPost(HttpPost request){
		String result = null;
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// �ж��Ƿ�����ɹ�
			if(response.getStatusLine().getStatusCode()==200){
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		}
        return null;
    }
	// ����Get���󣬻����Ӧ��ѯ���
	public static  String queryStringForGet(String url){
		// ���HttpGet����
		HttpGet request = HttpUtil.getHttpGet(url);
		String result = null;
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// �ж��Ƿ�����ɹ�
			if(response.getStatusLine().getStatusCode()==200){
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		}
        return null;
    }
}
