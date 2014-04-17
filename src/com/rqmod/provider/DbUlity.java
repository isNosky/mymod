package com.rqmod.provider;


import com.rqmod.provider.TableUser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbUlity extends SQLiteOpenHelper {
    // 数据库名称常量
    private static final String DATABASE_NAME = "rqmod.db";
    // 数据库版本常量
    private static final int DATABASE_VERSION = 2;
    // 表名称常量
    public static final String TABLES_USER = "tbl_user";
    
    public static SQLiteDatabase db = null;

	public DbUlity(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
        db.execSQL("CREATE TABLE " + TABLES_USER + " ("
                + TableUser.PHONENUM + " TEXT PRIMARY KEY,"
                + TableUser.NICKNAME + " TEXT,"
                + TableUser.PASSWORD + " TEXT"
                + ");");
	}

	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS tbl_user");
	}

}
