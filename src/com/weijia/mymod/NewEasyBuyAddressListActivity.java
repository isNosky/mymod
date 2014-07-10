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

import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.GlobalVar;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
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
	
	private final static String TBL_ADDR = "tbl_addr";
	
	SQLiteDatabase db = null;
	DatabaseManager dbm = null;
	
	int m_iFlag = -1;
	
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
					map.put("textview_new_easy_buy_address_list_item_default", b.get("isdefault"));
					map.put("textview_new_easy_buy_address_list_item_alias", b.get("textview_new_easy_buy_address_list_item_alias"));
					map.put("textview_new_easy_buy_address_list_item_name", b.get("textview_new_easy_buy_address_list_item_name"));
					map.put("textview_new_easy_buy_address_list_item_phone", b.get("textview_new_easy_buy_address_list_item_phone"));
					map.put("textview_new_easy_buy_address_list_item_area", b.get("textview_new_easy_buy_address_list_item_area"));
					map.put("textview_new_easy_buy_address_list_item_street", b.get("textview_new_easy_buy_address_list_item_street"));
					map.put("textview_new_easy_buy_address_list_item_addr", b.get("textview_new_easy_buy_address_list_item_addr"));
					listItem.add(map);
					
					mAdaptor.notifyDataSetChanged();  
					lvAddr.invalidate();
					
					refreshView();
					break;
				default:
			          break;
			}
			break;
			
			case 1:
				switch (resultCode) {
				case RESULT_OK:
					Bundle b=data.getExtras();  //data为B中回传的Intent
					
					HashMap<String, Object> map = new HashMap<String, Object>();
					String alias = (String) b.get("textview_new_easy_buy_address_list_item_alias");
					String name = (String) b.get("textview_new_easy_buy_address_list_item_name");
					String phone = (String) b.get("textview_new_easy_buy_address_list_item_phone");
					String area = (String) b.get("textview_new_easy_buy_address_list_item_area");
					String street = (String) b.get("textview_new_easy_buy_address_list_item_street");
					String addr = (String) b.get("textview_new_easy_buy_address_list_item_addr");
					String type = (String) b.get("operate_type");
					int iPosition = b.getInt("position"); 
					
					listItem.remove(iPosition);
					
					if(type.equalsIgnoreCase("delete"))
					{
						
					}
					else
					{
						map.put("textview_new_easy_buy_address_list_item_alias", alias);
						map.put("textview_new_easy_buy_address_list_item_name",name);
						map.put("textview_new_easy_buy_address_list_item_phone", phone);
						map.put("textview_new_easy_buy_address_list_item_area", area);
						map.put("textview_new_easy_buy_address_list_item_street", street);
						map.put("textview_new_easy_buy_address_list_item_addr", addr);
						map.put("textview_new_easy_buy_address_list_item_default", b.get("isdefault"));
						listItem.add(map);
					}
					
					mAdaptor.notifyDataSetChanged();  
					lvAddr.invalidate();
					
					refreshView();
					break;
				default:
			          break;
			}
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private void refreshView()
	{
		if(listItem.size() > 0)
		{
			lvAddr.setVisibility(ListView.VISIBLE);
			llNoData.setVisibility(LinearLayout.INVISIBLE);
		}
		else
		{
			
			llNoData.setVisibility(LinearLayout.VISIBLE);
			lvAddr.setVisibility(ListView.INVISIBLE);
		}		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.new_easy_buy_address_list);
			
			dbm = DatabaseManager.getInstance(this);
			db = dbm.openDatabase();
			
			GlobalVar.getInstance().saveActivity(this);
			
			lvAddr = (ListView)findViewById(R.id.listview_new_easy_buy_address_list);
			llNoData = (LinearLayout)findViewById(R.id.layout_new_easy_buy_address_no_data);			
			
			Intent intent = getIntent();
			m_iFlag = intent.getFlags();
//			if(101 == m_iFlag)
//			{
//				setTheme(android.R.style.Theme_NoDisplay);
//			}
			getAddrs();
			
			refreshView();
			
			String [] from = new String[] {
					"textview_new_easy_buy_address_list_item_alias",
					"textview_new_easy_buy_address_list_item_name", 
					"textview_new_easy_buy_address_list_item_phone",
					"textview_new_easy_buy_address_list_item_area",
					"textview_new_easy_buy_address_list_item_street",
					"textview_new_easy_buy_address_list_item_addr",
					};
			int [] to = new int [] {
					R.id.textview_new_easy_buy_address_list_item_alias,
					R.id.textview_new_easy_buy_address_list_item_name,
					R.id.textview_new_easy_buy_address_list_item_phone,
					R.id.textview_new_easy_buy_address_list_item_area,
					R.id.textview_new_easy_buy_address_list_item_street,
					R.id.textview_new_easy_buy_address_list_item_addr};
			
			mAdaptor = new SimpleAdapter(
					NewEasyBuyAddressListActivity.this, 
					listItem, 
					R.layout.new_easy_buy_address_list_item, 
					from,
					to)
			{
                //在这个重写的函数里设置 每个 item 中按钮的响应事件
				View view = null;
				int iPos = -1;
                @Override
                public View getView(int position, View convertView,ViewGroup parent) {
					try {
						
						iPos = position;
						view = super.getView(position, convertView, parent);
						
						TextView tvDefault = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_default);
						HashMap<String, Object> map = listItem.get(position);
						if((Boolean) map.get("textview_new_easy_buy_address_list_item_default"))
						{
							tvDefault.setVisibility(TextView.VISIBLE);
						}
						else
						{
							tvDefault.setVisibility(TextView.INVISIBLE);
						}
						
//						ImageButton btnMod=(ImageButton)view.findViewById(R.id.imageview_new_easy_buy_address_list_item_modify);
//						btnMod.setOnClickListener(new OnClickListener() {
//                            
//                            @Override
//                            public void onClick(View v) {
//                            	Intent intent = new Intent(NewEasyBuyAddressListActivity.this,NewAddrActivity.class);
//                            	//intent.putExtra("textview_new_easy_buy_address_list_item_alias", );
//                            	intent.addFlags(1);
//                            	startActivityForResult(intent, 1);
//                            }
//                        });
						
						view.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								TextView tvAlias = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_alias);
								TextView tvName = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_name);
								TextView tvPhone = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_phone);
								TextView tvArea = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_area);
								TextView tvStreet = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_street);
								TextView tvAddr = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_addr);
                            
								if(101 == m_iFlag)
								{
									Intent intent = new Intent(NewEasyBuyAddressListActivity.this,FillOrderActivity.class);
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_alias", tvAlias.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_name", tvName.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_phone", tvPhone.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_area", tvArea.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_street", tvStreet.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_addr", tvAddr.getText());
	                            	intent.putExtra("position", iPos);
									setResult(RESULT_OK,intent); 									
									NewEasyBuyAddressListActivity.this.finish();
								}
								else
								{
									Intent intent = new Intent(NewEasyBuyAddressListActivity.this,NewAddrActivity.class);
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_alias", tvAlias.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_name", tvName.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_phone", tvPhone.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_area", tvArea.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_street", tvStreet.getText());
	                            	intent.putExtra("textview_new_easy_buy_address_list_item_addr", tvAddr.getText());
	                            	intent.putExtra("position", iPos);
	                            	intent.addFlags(1);
	                            	startActivityForResult(intent, 1);	
								}
							}});
						
						
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
//			lvAddr.setOnItemClickListener(new OnItemClickListener() {  
//				  
//	            @Override  
//	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
//	                    long arg3) {  
//	            	showDialog("点击第"+arg2+"个项目");  
//	            }  
//	        });
			
			lvAddr.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					View view = arg1;
					TextView tvAlias = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_alias);
					TextView tvName = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_name);
					TextView tvPhone = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_phone);
					TextView tvArea = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_area);
					TextView tvStreet = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_street);
					TextView tvAddr = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_addr);
                	
					Intent intent = new Intent(NewEasyBuyAddressListActivity.this,NewAddrActivity.class);
                	intent.putExtra("textview_new_easy_buy_address_list_item_alias", tvAlias.getText());
                	intent.putExtra("textview_new_easy_buy_address_list_item_name", tvName.getText());
                	intent.putExtra("textview_new_easy_buy_address_list_item_phone", tvPhone.getText());
                	intent.putExtra("textview_new_easy_buy_address_list_item_area", tvArea.getText());
                	intent.putExtra("textview_new_easy_buy_address_list_item_street", tvStreet.getText());
                	intent.putExtra("textview_new_easy_buy_address_list_item_addr", tvAddr.getText());
                	
                	intent.putExtra("position", arg2);
					if(101 == m_iFlag)
					{
						setResult(RESULT_OK,intent); 									
						NewEasyBuyAddressListActivity.this.finish();
					}
					else
					{									
                    	intent.addFlags(1);
                    	startActivityForResult(intent, 1);	
					}
					
				}});
			
//			lvAddr.setOnItemSelectedListener(new OnItemSelectedListener(){
//
//				@Override
//				public void onItemSelected(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					View view = arg1;
//					TextView tvAlias = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_alias);
//					TextView tvName = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_name);
//					TextView tvPhone = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_phone);
//					TextView tvArea = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_area);
//					TextView tvStreet = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_street);
//					TextView tvAddr = (TextView)view.findViewById(R.id.textview_new_easy_buy_address_list_item_addr);
//                	
//					Intent intent = new Intent(NewEasyBuyAddressListActivity.this,NewAddrActivity.class);
//                	intent.putExtra("textview_new_easy_buy_address_list_item_alias", tvAlias.getText());
//                	intent.putExtra("textview_new_easy_buy_address_list_item_name", tvName.getText());
//                	intent.putExtra("textview_new_easy_buy_address_list_item_phone", tvPhone.getText());
//                	intent.putExtra("textview_new_easy_buy_address_list_item_area", tvArea.getText());
//                	intent.putExtra("textview_new_easy_buy_address_list_item_street", tvStreet.getText());
//                	intent.putExtra("textview_new_easy_buy_address_list_item_addr", tvAddr.getText());
//                	
//                	intent.putExtra("position", arg2);
//					if(101 == m_iFlag)
//					{
//						setResult(RESULT_OK,intent); 									
//						NewEasyBuyAddressListActivity.this.finish();
//					}
//					else
//					{									
//                    	intent.addFlags(1);
//                    	startActivityForResult(intent, 1);	
//					}
//				}
//
//				@Override
//				public void onNothingSelected(AdapterView<?> arg0) {
//					// TODO Auto-generated method stub
//					
//				}});
			
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
			    	message.what = Constant.GETADDR_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null;
			
			    
		}	
    }
	
	private String getUserNameById(int id)
	{
		String name = "";
		Cursor c = db.rawQuery("select nickname from tbl_user where id = " + id, null);// WHERE age >= ?", new String[]{"33"}); 
		
		while (c.moveToNext()) {
			name = c.getString(c.getColumnIndex("nickname"));
		}  
		c.close();			
		
		return name;
	}
	
	private void insertToDB(int iAddrID,boolean isdefault,String district,String street,String building,String phonenumber1 )
	{
		ContentValues values = new ContentValues();
		values.put("id", iAddrID);
		GlobalVar app = GlobalVar.getInstance();
		values.put("subscriberid", app.getUserId());
		values.put("isdefault", isdefault?1:0);
		values.put("addr", district + street + building);
		values.put("name", getUserNameById(app.getUserId()));
		values.put("phonenumber1", phonenumber1);
		
		try {
			if(-1 == db.insertOrThrow(TBL_ADDR  ,null , values))
			{
				Log.e("insertToDB", "Error");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}
	
	public void RedirectLogin()
    {
    	AlertDialog.Builder dialog=new AlertDialog.Builder(NewEasyBuyAddressListActivity.this);
		dialog.setTitle(getResources().getString(R.string.token_invalid_login_tip))
			.setIcon(android.R.drawable.ic_dialog_info)
			.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(NewEasyBuyAddressListActivity.this,LoginActivity.class);
					intent.setFlags(Constant.LOGIN_MSG);
					startActivityForResult(intent,Constant.LOGIN_MSG);
				}
		}).create().show();
    }
	private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
            case Constant.GETADDR_MSG:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
										
					switch(iErrorCode)
					{
					case Constant.ERR_CODE_SUCCESS:
						JSONArray jsonAddrs = jsonout.getJSONArray("Addrs");
						for(int shopid = 0 ; shopid < jsonAddrs.length() ; shopid++)
						{
							JSONObject jsonAddr = (JSONObject) jsonAddrs.get(shopid);
							HashMap<String, Object> map = new HashMap<String, Object>();
							int aid = jsonAddr.getInt("id");
							map.put("textview_new_easy_buy_address_list_item_name", getUserNameById(jsonAddr.getInt("subscriberid")));
							String phonenumber1 = jsonAddr.getString("phonenumber1");
							map.put("textview_new_easy_buy_address_list_item_phone", phonenumber1);
							String district = jsonAddr.getString("district");
							map.put("textview_new_easy_buy_address_list_item_area", district);
							String street = jsonAddr.getString("street");
							map.put("textview_new_easy_buy_address_list_item_street", street);
							String building = jsonAddr.getString("building");
							map.put("textview_new_easy_buy_address_list_item_addr", building);
							boolean isdefault = jsonAddr.getInt("isdefault")==0?false:true;
							map.put("textview_new_easy_buy_address_list_item_default",isdefault );
														
							listItem.add(map);
							
							insertToDB(aid,isdefault,district,street,building,phonenumber1);
							
							mAdaptor.notifyDataSetChanged();  
							lvAddr.invalidate();
							refreshView();
						}
						break;
					case Constant.ERR_CODE_TOKEN_INVALID:
						RedirectLogin();
						break;
					default:
						
						break;
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
    
    private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
}
