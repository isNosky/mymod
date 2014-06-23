package com.weijia.mymod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rqmod.provider.AsyncViewTask;
import com.rqmod.provider.DatabaseManager;
import com.rqmod.provider.GlobalVar;
import com.rqmod.provider.ImageManager;
import com.rqmod.util.Constant;
import com.rqmod.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
	

	private static final String TBL_SHOPCAR = "tbl_shopcar";
	private static final int TEXTVIEW_ID_OFFSET = 2048;
	//int [] Ids = {R.id.quanbu,R.id.zhushi,R.id.yinliao,R.id.zhou};
	ListView lvMenu = null;
	ImageView vShopCar = null;
	LinearLayout llShopCar = null;
	SQLiteDatabase db = null;
	DatabaseManager dbm = null;

	TabHost tabHost = null;
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();		
	
	long mExitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
             if ((System.currentTimeMillis() - mExitTime) > 2000) {
                     Object mHelperUtils;
                     Toast.makeText(this, getResources().getString(R.string.again_exit_app), Toast.LENGTH_SHORT).show();
                     mExitTime = System.currentTimeMillis();

             } else {
                     finish();
             }
             return true;
		 }

		 return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("魏家凉皮-菜品");
		// 设置当前Activity界面布局
		setContentView(R.layout.activity_main);
		dbm = DatabaseManager.getInstance(this);
		db = dbm.openDatabase();
		
		tabHost = ((TabActivity)getParent()).getTabHost();
		
		lvMenu = (ListView)findViewById(R.id.product_list);	

		
		getMenu();
		initView();		
		
		vShopCar = (ImageView)findViewById(R.id.shop_car_add);
		llShopCar = (LinearLayout)findViewById(R.id.ll_add_to_car);
		llShopCar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for(int i = 0; i < lvMenu.getChildCount(); i++){  
				      View view = lvMenu.getChildAt(i);  
				      CheckBox cb = (CheckBox)view.findViewById(R.id.product_checkbox);  
				      if(cb.isChecked())
				      {
				    	  //加入后台数据库
				    	  TextView tvName = (TextView)view.findViewById(R.id.product_item_name);
				    	  TextView tvPrice = (TextView)view.findViewById(R.id.product_item_martPrice);
				    	  TextView tvPrdtId = (TextView)view.findViewById(R.id.product_item_id);

				    	  String strName = tvName.getText().toString();
				    	  String strPrice = tvPrice.getText().toString();
				    	  String strPrdtId = tvPrdtId.getText().toString();
				    	  int productid = Integer.parseInt(strPrdtId.substring(3));
				    	  ContentValues values = new ContentValues();
				    	  values.put("productname", strName);
				    	  values.put("productid", productid);
				    	  values.put("unitprice", Float.parseFloat(strPrice.substring(4)));
				    	  values.put("buycount", 1);
				    	  
						  Cursor count = db.rawQuery("select count(*) goodscount from tbl_shopcar where productid = " + productid, null);
						  count.moveToFirst();
						  int iColIdx = count.getColumnIndex("goodscount");
						  int goodscount = count.getInt(iColIdx);
						  count.close();
						  
						  if(goodscount > 1)
						  {
							  
						  }
						  else if(goodscount == 1)
						  {
							  Cursor buycount = db.rawQuery("select buycount from tbl_shopcar where productid = " + productid, null);
							  buycount.moveToFirst();
							  int iColIdx1 = buycount.getColumnIndex("buycount");
							  int iBuycount = buycount.getInt(iColIdx1);
							  buycount.close();
							  
//							  ContentValues values1 = new ContentValues();
//							  values1.put("buycount", iBuycount +1);
//							  
//							  String whereClause = "productid=?";
//							  String [] whereArgs = {String.valueOf(productid)};
							  try {
								//db.update(TBL_SHOPCAR, values1, whereClause, whereArgs);
								  String strSql = "update tbl_shopcar set buycount = " + String.valueOf(iBuycount +1) + " where productid = " + String.valueOf(productid);
								  db.execSQL(strSql);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								String str = e.getMessage();
								e.printStackTrace();
							}
						  }
						  else
						  {
					    	  if(-1 == db.insert(TBL_SHOPCAR, null, values))
					    	  {
					    		  break;
					    	  }
						  }
				      }
				}
				AlertDialog.Builder dialog=new AlertDialog.Builder(MenuActivity.this);
				dialog.setTitle(getResources().getString(R.string.shake_dialog_title))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(getResources().getString(R.string.menu_goto_shop_car), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
															
							tabHost.setCurrentTab(1);
						}
				}).setNegativeButton(getResources().getString(R.string.menu_remain), new DialogInterface.OnClickListener() {
	             

	            public void onClick(DialogInterface dialog, int which) {
	                // TODO Auto-generated method stub
	                dialog.cancel();//取消弹出框
	            }
	        }).create().show();
			}
			
		});
	}
	
	private void initView() {
        // 获取xml的RelativeLayout
		LinearLayout layout = (LinearLayout) findViewById(R.id.lllayout);

		
		try {
			Cursor c = db.rawQuery("SELECT * FROM tbl_product_type", null);// WHERE age >= ?", new String[]{"33"});  
			while (c.moveToNext()) {  
			    int type = c.getInt(c.getColumnIndex("type"));  
			    String pname = c.getString(c.getColumnIndex("name"));  
			    
			    HashMap<String, Object> map = new HashMap<String, Object>();  
			    map.put("type", type);
			    map.put("name", pname);
			    listItem.add(map);
			}  
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}

        for (int i = 0; i < listItem.size(); i++) {
        	
        	HashMap<String, Object> map = listItem.get(i);
        	map.put("textview_id", i+TEXTVIEW_ID_OFFSET);
        	// 每行都有一个linearlayout
            LinearLayout lLayout = new LinearLayout(getBaseContext());
            lLayout.setId(i + 1024);
            lLayout.setOrientation(LinearLayout.VERTICAL);
            lLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_round));
            lLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
            LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            lLayout.setLayoutParams(lLayoutlayoutParams);
            
            TextView tv = new TextView(getBaseContext());
            tv.setId(i + TEXTVIEW_ID_OFFSET);
            tv.setText(map.get("name").toString());
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
            tv.setPadding(10, 10, 10, 10);
            tv.setTextSize((float) 15.0);
//            int lw = layout.getWidth();
//            lw = lw / listItem.size();
//            tv.setWidth(lw);

            map.put("textview_obj", tv);
            // 为TextView添加长高设置
            LinearLayout.LayoutParams layoutParams_txt = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(layoutParams_txt);

            // 添加到每行的linearlayout中
            lLayout.addView(tv);
            // 把每个linearlayout加到relativelayout中
            //layout.addView(lLayout, lLayoutlayoutParams);     
            layout.addView(lLayout);

           
        }
        
        
        
        OnClickListener lsnr = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)findViewById(arg0.getId());
				tv.setTextColor(getResources().getColor(R.color.green));
				int type= -1;
				for(int i = 0 ; i < listItem.size() ; i++ )
				{
					HashMap<String, Object> map = listItem.get(i);
					TextView tv1 = (TextView)(map.get("textview_obj"));
					if(arg0.getId() != tv1.getId() )
					{
						tv1.setTextColor(getResources().getColor(R.color.black));
					}	
					else
					{
						type = (Integer) map.get("type");
					}
				}
				getMenuByType(type);
			}
		};
		
		for(int ii = 0 ; ii < listItem.size() ; ii++ )
		{
			HashMap<String, Object> map1 = listItem.get(ii);
			TextView tv1 = (TextView)(map1.get("textview_obj"));
			tv1.setOnClickListener(lsnr);
		}		
    }
	
	private void getMenuByType(int product_type)
	{
		ArrayList<HashMap<String, Object>> listItem1 = new ArrayList<HashMap<String,Object>>();		
		
		try {
			String strSql = "SELECT * FROM tbl_product";
			if(-1 != product_type)
			{
				strSql = strSql + " where type = " + product_type;
			}
			Cursor c = db.rawQuery(strSql, null);// WHERE age >= ?", new String[]{"33"});  
			while (c.moveToNext()) {  
			    int id = c.getInt(c.getColumnIndex("id"));  
			    String pname = c.getString(c.getColumnIndex("productname"));  
			    int type = c.getInt(c.getColumnIndex("type"));
			    String desc = c.getString(c.getColumnIndex("description"));
			    String picptah = c.getString(c.getColumnIndex("picturepath"));
			    int isinsale = c.getInt(c.getColumnIndex("isinsale"));
			    float price = c.getFloat(c.getColumnIndex("unitprice"));
			    HashMap<String, Object> map = new HashMap<String, Object>();  
			    //Bitmap bmp = ImageManager.getInstance(this).getBitmap(picptah);
				map.put("product_item_image", picptah);
			    map.put("product_item_name", pname);
			    map.put("product_item_id", "编号:"+String.valueOf(id));
			    map.put("product_item_adword", desc);
			    map.put("product_item_martPrice", "RMB:"+ Float.toString(price));  
			    listItem1.add(map);
			}  
			c.close();

			MenuAdaptor mSimpleAdapter = new MenuAdaptor(
					MenuActivity.this, 
					listItem1, 
					R.layout.product_list_item, 
					new String[] {"product_item_image", "product_item_name","product_item_id","product_item_adword", "product_item_martPrice"}, 
					new int [] {R.id.product_item_image,R.id.product_item_name,R.id.product_item_id, R.id.product_item_adword,R.id.product_item_martPrice});
			
			mSimpleAdapter.setViewBinder(new ViewBinder(){

				@Override
				public boolean setViewValue(View arg0, Object arg1, String arg2) {
					// TODO Auto-generated method stub
					if( (arg0 instanceof ImageView) & (arg1 instanceof Bitmap) ) {  
			            ImageView iv = (ImageView) arg0;  
			            Bitmap bm = (Bitmap) arg1;  
			            iv.setImageBitmap(bm);  
			            return true;  
			            }  
			        return false;
				}
				
			});
			lvMenu.setAdapter(mSimpleAdapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String str = e.getMessage();
			e.printStackTrace();
		}
	}

	private void getMenu()
	{
		if(Constant.FLAG_POST_IN_JSON)
		{								
	 		Thread thread = new Thread(){ 
		    	@Override 
			    public void run() { 	
		    		JSONObject jsonout = null;
		    		GlobalVar app = GlobalVar.getInstance();
			    	try { 							    		
			    		JSONObject jsoin = new JSONObject();
			    		jsoin.put("Token", app.getToken());
						jsonout = HttpUtil.queryStringForPost(Constant.GETMENUSERVLET, jsoin);
			    	} catch (Exception e) { 
			    		String str = e.getMessage();
			    	} 
			    	  
			    	Message message= handler.obtainMessage() ; 
			    	message.obj = jsonout; 
			    	message.what = Constant.GETMENU_MSG;
			    	handler.sendMessage(message); 
			    	} 
		    	}; 
		    	thread.start(); 
		    	thread = null; 
			
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
				            postParameters.add(new BasicNameValuePair("Token", app.getToken()));	            
						    jsonout = HttpUtil.queryStringForPost(Constant.GETMENUSERVLET, postParameters);
				    	} catch (Exception e) { 
				    		String str = e.getMessage();
				    	} 
				    	  
				    	Message message= handler.obtainMessage() ; 
				    	message.obj = jsonout; 
				    	message.what = Constant.GETMENU_MSG;
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
            case Constant.GETMENU_MSG:
                //关闭
            	try {
            		JSONObject jsonout = (JSONObject) msg.obj;
					int iErrorCode = (Integer) jsonout.get(Constant.ERRCODE);
					String strErrDesc = (String) jsonout.get(Constant.ERRDESC);
					
					if(Constant.ERR_CODE_SUCCESS == iErrorCode)
					{
						HandleLoginResult(jsonout);
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
					String str = e.getMessage();
					e.printStackTrace();
				}
                break;
            }
        }
    };
	
	private int HandleLoginResult(JSONObject jso)
	{
		if(jso != null)
		{
			try {
				int iErrCode = jso.getInt("ErrorCode");
				//int iUserId = jso.getInt("UserID");
				
				JSONArray jsonTypes = jso.getJSONArray("Types");
				
				String strSql = "delete from tbl_product_type";
			    db.execSQL(strSql);
			    
			    for(int i = 0 ; i < jsonTypes.length() ; i++)
			    {
			    	JSONObject jsType = (JSONObject) jsonTypes.get(i);
			    	ContentValues values = new ContentValues();
			    	values.put("type", jsType.getString("id"));
			    	values.put("name", jsType.getString("name"));
			    	
			    	if(-1 == db.insert("tbl_product_type", null, values))
					{
						//
					}
			    }
			    
			    strSql = "delete from tbl_product";
			    db.execSQL(strSql);
			    JSONArray jsonMenus = jso.getJSONArray("Products");
			    for(int j = 0 ; j < jsonMenus.length() ; j++)
			    {
			    	JSONObject jsProduct = (JSONObject) jsonMenus.get(j);
			    	ContentValues values = new ContentValues();
			    	values.put("picturepath", jsProduct.getString("picturepath"));
			    	values.put("productname", jsProduct.getString("name"));
			    	values.put("description", jsProduct.getString("description"));
			    	values.put("id", jsProduct.getInt("id"));
			    	values.put("type", jsProduct.getInt("type"));
			    	values.put("unitprice", jsProduct.getDouble("unitprice"));
			    	values.put("isinsale", jsProduct.getInt("isinsale"));
			    	values.put("commnet", jsProduct.getString("commnet"));
			    	values.put("pri", jsProduct.getInt("pri"));
			    	
			    	if(-1 == db.insertOrThrow("tbl_product", null, values))
					{
						Log.v("TEST", "error occurs");
					}
			    }
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				String str = e.getMessage();
				Log.v("TEST", str);
				e.printStackTrace();
			}
		}
		
		//模拟第一个tab页被点击
		HashMap<String, Object> maptmp = listItem.get(0);
		TextView tvtmp = (TextView)(maptmp.get("textview_obj"));
		tvtmp.performClick();
		return 0;
	}	
	
	class MenuAdaptor extends SimpleAdapter{
		
		private LayoutInflater mInflater;
		List<HashMap<String, Object>> mlistData;
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mlistData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlistData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = mInflater.inflate(R.layout.product_list_item, null);
	        ImageView image = (ImageView) convertView.findViewById(R.id.product_item_image);
	        TextView product_item_name = (TextView) convertView.findViewById(R.id.product_item_name);
	        TextView product_item_id = (TextView) convertView.findViewById(R.id.product_item_id);
	        TextView product_item_adword = (TextView) convertView.findViewById(R.id.product_item_adword);
	        TextView product_item_martPrice = (TextView) convertView.findViewById(R.id.product_item_martPrice);
	        
	        HashMap<String, Object> map = (HashMap<String, Object>) mlistData.get(position);
	        String strTag = (String) map.get("product_item_image");
	        image.setTag(strTag);
	        new AsyncViewTask().execute(image);
	        String name = (String)map.get("product_item_name");
	        product_item_name.setText(name);
	        String id = (String)map.get("product_item_id");
	        product_item_id.setText(id);
	        String adword = (String)map.get("product_item_adword");
	        product_item_adword.setText(adword);
	        String price = (String)map.get("product_item_martPrice");
	        product_item_martPrice.setText(price);
	        
	        return convertView; 
		}

		public MenuAdaptor(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
			this.mInflater = LayoutInflater.from(context); 
			mlistData = (List<HashMap<String, Object>>) data;
		}	
		
	}
}

