package com.rqmod.provider;

import android.net.Uri;

public class TableUser {
	// 授权常量
    public static final String AUTHORITY = "com.rqmod.provider.TABLES";
    // 访问Uri
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/table");
    // 默认排序常量
    public static final String DEFAULT_SORT_ORDER = "num DESC";
    // 表字段常量
    public static final String PHONENUM = "phonenum";
    public static final String NICKNAME= "nickname";
    public static final String PASSWORD= "password";
}
