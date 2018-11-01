package com.zhaoqy.app.demo.camera.modify.util;

import java.io.File;
import java.io.FileOutputStream;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileUtil 
{
	/**
	 * 
	 * @Title: saveBitmap
	 * @Description: 保存文件
	 * @param path
	 * @param bitmap
	 * @return: void
	 */
	@SuppressLint("SimpleDateFormat")
	public static void saveBitmap(Context context, Bitmap bitmap)
	{
		try 
		{
			String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + 
					"demo"+ File.separator;
			File dirFile = new File(path);
	        if(!dirFile.exists())
	        {
	            dirFile.mkdirs();
	        }
			
	        String filePath = "";
	        long time = System.currentTimeMillis();
			String date = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date(time));
			filePath = path + "IMG_" + date + ".jpg";
			PreferencesUtil.putString(context, "demoicon", filePath);
			
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	 /**
     * 从本地获取图片
     * @param pathString 文件路径
     * @return 图片
     */
    public static Bitmap getBitmap(Context context)
    {
        Bitmap bitmap = null;
        String filePath = PreferencesUtil.getString(context, "demoicon", "");
        if (!filePath.isEmpty())
        {
        	 try
             {
                 File file = new File(filePath);
                 if(file.exists())
                 {
                     bitmap = BitmapFactory.decodeFile(filePath);
                 }
             } 
             catch (Exception e)
             {
             }
        }
        return bitmap;
    }
}
