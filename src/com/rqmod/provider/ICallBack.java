package com.rqmod.provider;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

public interface ICallBack {
	public ArrayList<HashMap<String, Object>> func(JSONObject jsonout);  
}
