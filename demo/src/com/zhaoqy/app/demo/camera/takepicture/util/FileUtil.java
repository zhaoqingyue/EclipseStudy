package com.zhaoqy.app.demo.camera.takepicture.util;

import java.io.File;
import java.io.FileOutputStream;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;

public class FileUtil 
{
	private static final int ww=480;
	private static final int hh=800;
	
	@SuppressLint("SimpleDateFormat")
	public static String getFilePath()
	{
		String filePath = "";
		String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + 
				File.separator + "Camera"+ File.separator;
		long time = System.currentTimeMillis();
		String date = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date(time));
		filePath = folderPath + "IMG_" + date + ".jpg";
		Log.e("zhaoqy", " filePath: " + filePath);  //filePath: /storage/emulated/0/DCIM/Camera/IMG_20150918_150509.jpg
		return filePath;
	}
	
	public static File getFile()
	{
		File file = new File(getFilePath());
		if(!file.exists())
		{
			File dirPath = file.getParentFile(); 
			dirPath.mkdirs();
		}
		return file;
	}
	
	public static int saveBitmap(String path, byte[] buffer)
	{
		int result=-1;
		try 
		{
			FileOutputStream out=new FileOutputStream(new File(path));
			out.write(buffer);
			out.flush();
			out.close();
			result=1;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public static int saveBitmap(String path, Bitmap bitmap)
	{
		int result=-1;
		try 
		{
			FileOutputStream fos=new FileOutputStream(new File(path));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			result=1;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public static Bitmap getdecodeBitmap(String filePath)
	{
		if(filePath == null)
		{
			return null;
		}
		BitmapFactory.Options options=new Options();
		options.inJustDecodeBounds=true;
		Bitmap bitmap=BitmapFactory.decodeFile(filePath, options);
		
		int width=options.outWidth;
		int height=options.outHeight;
		float scale=1f;
		
		if(width > ww && width > height)
		{
			scale = width / ww;
		}
		else if(height > hh && height > width)
		{
			scale = height/hh;
		}
		else
		{
			scale = 1;
		}
		
		options.inSampleSize = (int) scale;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	}
}
