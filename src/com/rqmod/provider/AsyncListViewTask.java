package com.rqmod.provider;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.os.AsyncTask;


public class AsyncListViewTask extends AsyncTask<ListViewParams, Void, ArrayList<HashMap<String, Object>>>{
	ListViewParams m_lvp = null;
	@Override
	protected ArrayList<HashMap<String, Object>> doInBackground(
			ListViewParams... arg0) {
		m_lvp = arg0[0];		
		JSONObject jsonout = HttpUtil.queryStringForPost(m_lvp.getServlet(), m_lvp.getPostParameters());
		return m_lvp.cb.func(jsonout);		 
	}

	@Override
	protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
		
		m_lvp.list.clear();
		m_lvp.list.addAll(result);
		m_lvp.adapter.notifyDataSetChanged();
		m_lvp.lv.invalidate();
		super.onPostExecute(result);
	}

}
