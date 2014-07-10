package com.rqmod.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import android.widget.BaseAdapter;
import android.widget.ListView;

public class ListViewParams {
	ListView lv;
	String servlet;
	List<NameValuePair> postParameters;
	ICallBack cb;
	BaseAdapter adapter;
	ArrayList<HashMap<String, Object>> list;
	
	public ArrayList<HashMap<String, Object>> getList() {
		return list;
	}
	public void setList(ArrayList<HashMap<String, Object>> list) {
		this.list = list;
	}
	public BaseAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}
	public ListView getLv() {
		return lv;
	}
	public void setLv(ListView lv) {
		this.lv = lv;
	}
	public String getServlet() {
		return servlet;
	}
	public void setServlet(String servlet) {
		this.servlet = servlet;
	}
	public List<NameValuePair> getPostParameters() {
		return postParameters;
	}
	public void setPostParameters(List<NameValuePair> postParameters) {
		this.postParameters = postParameters;
	}
	
	public ICallBack getCb() {
		return cb;
	}
	public void setCb(ICallBack cb) {
		this.cb = cb;
	}
}
