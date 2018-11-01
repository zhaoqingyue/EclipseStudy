package com.zhaoqy.app.demo.notebook.windnotes.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class CommonUtil 
{
	public static SQLiteDatabase getDatabase(Context context, int raw_id) 
	{
		try 
		{
			int BUFFER_SIZE = 100000;
			String DB_NAME = "windnote.db";
			String PACKAGE_NAME = "com.zhaoqy.app.demo";
			String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases/";
			File destDir = new File(DB_PATH);
			if (!destDir.exists()) 
			{
				destDir.mkdirs();
			}
			String file = DB_PATH + DB_NAME;
			if (!(new File(file).exists())) 
			{
				InputStream is = context.getResources().openRawResource(raw_id);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) 
				{
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
			return db;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
    }
	
	/**
	 * 
	 * @Title: getPackageName
	 * @Description: 获取应用包名
	 * @param context
	 * @return
	 * @return: String
	 */
	public static String getPackageName(Context context)
	{
		String packageNames = "";  //当前版本的包名
		 
		try
		{
		    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
		    packageNames = info.packageName;  
		} 
		catch (NameNotFoundException e)
		{
		    e.printStackTrace();
		}
		return packageNames;
	}
}
