package com.zhaoqy.app.demo.page.vmall.util;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class VmallStatic 
{
	public static LinkedHashMap<String, SoftReference<Bitmap>> hashMap = new LinkedHashMap<String, SoftReference<Bitmap>>();
	public static VmallLru lru = new VmallLru((int) (Runtime.getRuntime().maxMemory() / 8.0), hashMap);
	public static SQLiteDatabase db;
}
