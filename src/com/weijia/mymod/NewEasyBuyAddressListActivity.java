package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.GlobalVar;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NewEasyBuyAddressListActivity extends Activity {

	LinearLayout llAddAddr = null;
	ListView lvAddr = null;
	LinearLayout llNoData = null;
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();	
	SimpleAdapter mAdaptor = null;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode)
		{
			case 0:
				switch (resultCode) {
				case RESULT_OK:
					Bundle b=data.getExtras();  //data为B中回传的Intent
					
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("textview_new_easy_buy_address_list_item_name", b.get("textview_new_easy_buy_address_list_item_name"));
					map.put("textview_new_easy_buy_address_list_item_phone", b.get("textview_new_easy_buy_address_list_item_phone"));
					map.put("textview_new_easy_buy_address_list_item_address", b.get("textview_new_easy_buy_address_list_item_address"));
					listItem.add(map);
					
					mAdaptor.notifyDataSetChanged();  
					lvAddr.invalidate();
					
					break;
				default:
			          break;
			}
			break;
			
			case 1:
				switch (resultCode) {
				case RESULT_OK:
					Bundle b=data.getExtras();  //data为B中回传的Intent
					String strUser = b.getString("UserName");
					String strPass = b.getString("Password");
					break;
				default:
			          break;
			}
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.new_easy_buy_address_list);
			lvAddr = (ListView)findViewById(R.id.listview_new_easy_buy_address_list);
			llNoData = (LinearLayout)findViewById(R.id.layout_new_easy_buy_address_no_data);			
			
			getAddrs();
			
			if(listItem.size() > 0)
			{
				lvAddr.setVisibility(ListView.VISIBLE);
			}
			else
			{
				
				llNoData.setVisibility(LinearLayout.VISIBLE);
				lvAddr.setVisibility(ListView.INVISIBLE);
			}
			
			mAdaptor = new SimpleAdapter(
					NewEasyBuyAddressListActivity.this, 
					listItem, 
					R.layout.new_easy_buy_address_list_item, 
					new String[] {"textview_new_easy_buy_address_list_item_name", "textview_new_easy_buy_address_list_item_phone","textview_new_easy_buy_address_list_item_address"}, 
					new int [] {R.id.textview_new_easy_buy_address_list_item_name,R.id.textview_new_easy_buy_address_list_item_phone,R.id.textview_new_easy_buy_address_list_item_address})
			{
                //在这个重写的函数里设置 每个 item 中按钮的响应事件
				View view = null;
                @Override
                public View getView(int position, View convertView,ViewGroup parent) {
					try {
						
						view = super.getView(position, convertView, parent);
						
						ImageButton btnMod=(ImageButton)view.findViewById(R.id.imageview_new_easy_buy_address_list_item_modify);
						btnMod.setOnClickListener(new OnClickListener() {
                            
                            @Override
                            public void onClick(View v) {
                            	
                            }
                        });
						
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						String str = e.getMessage();
						e.printStackTrace();
					}
                    return view;
                }
			};
			lvAddr.setAdapter(mAdaptor);
			
			llAddAddr = (LinearLayout) findViewById(R.id.receive_addr_add_layout);
			llAddAddr.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(NewEasyBuyAddressListActivity.this,NewAddrActivity.class);
					intent.addFlags(0);
					startActivityForResult(intent, 0);
				}});
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
	
	
	private void getAddrs()
    {
    	if(Constant.FLAG_POST_IN_JSON)
		{	
			
		}
		else
		{
			Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		
		    		JSONObject jsoin = new JSONObject();
		    		JSONObject jsonout = null;
		    		
					try {
						HttpPost request = new HttpPost();
						List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
						GlobalVar app = GlobalVar.getInstance();
			            postParameters.add(new BasicNameValuePair("UserID", String.valueOf(app.getUserId())));
			            //postParameters.add(new BasicNameValuePair("ShopID", "6"));
			            postParameters.add(new BasicNameValuePair("Token", GlobalVar.getInstance().getToken()));
			            postParameters.add(new BasicNameValuePair("StartNum", "0"));
			            postParameters.add(new BasicNameValuePair("Count", "100"));
			            
			            jsonout = HttpUtil.queryStringForPost(Constant.GETADDRSERVLET, postParameters);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = 2;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;
			
			    
		}	
    }
	
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case 1:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
					
					if(Constant.ERR_CODE_SUCCESS == iErrorCode)
					{
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("textview_new_easy_buy_address_list_item_name", 1);
						map.put("textview_new_easy_buy_address_list_item_phone", 1);
						map.put("textview_new_easy_buy_address_list_item_address", 1);
						listItem.add(map);
					}
					else
					{
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					String str = e.getMessage();
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            	
                break;            
            }
        }
    };
}
