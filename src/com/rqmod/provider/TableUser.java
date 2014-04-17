package com.rqmod.provider;

import android.net.Uri;

public class TableUser {
	// ��Ȩ����
    public static final String AUTHORITY = "com.rqmod.provider.TABLES";
    // ����Uri
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/table");
    // Ĭ��������
    public static final String DEFAULT_SORT_ORDER = "num DESC";
    // ���ֶγ���
    public static final String PHONENUM = "phonenum";
    public static final String NICKNAME= "nickname";
    public static final String PASSWORD= "password";
}
