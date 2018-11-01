/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CacheUtils.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.util
 * @Description: 缓存Utils
 * @author: zhaoqy
 * @date: 2015-11-18 下午3:01:39
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Properties;
import com.zhaoqy.app.demo.oschina.common.AppProperty;
import android.content.Context;

public class CacheUtils 
{
	private static final int CACHE_TIME = 60*60000;//缓存失效时间
	private static Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
	
	/**
	 * 判断缓存数据是否可读
	 * @param cachefile
	 * @return
	 */
	public static boolean isReadDataCache(Context context, String cachefile)
	{
		return readObject(context, cachefile) != null;
	}
	
	/**
	 * 判断缓存是否存在
	 * @param cachefile
	 * @return
	 */
	private static boolean isExistDataCache(Context context, String cachefile)
	{
		boolean exist = false;
		File data = context.getFileStreamPath(cachefile);
		if(data.exists())
			exist = true;
		return exist;
	}
	
	/**
	 * 判断缓存是否失效
	 * @param cachefile
	 * @return
	 */
	public static boolean isCacheDataFailure(Context context, String cachefile)
	{
		boolean failure = false;
		File data = context.getFileStreamPath(cachefile);
		if(data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
			failure = true;
		else if(!data.exists())
			failure = true;
		return failure;
	}
	
	/**
	 * 清除app缓存
	 */
	public static void clearAppCache(Context context)
	{
		//清除webview缓存
		/*File file = CacheManager.getCacheFileBaseDir();  
		if (file != null && file.exists() && file.isDirectory()) 
		{ 
		    for (File item : file.listFiles()) 
		    {  
		    	item.delete();  
		    }  
		    file.delete();  
		}  	*/	  
		context.deleteDatabase("webview.db");  
		context.deleteDatabase("webview.db-shm");  
		context.deleteDatabase("webview.db-wal");  
		context.deleteDatabase("webviewCache.db");  
		context.deleteDatabase("webviewCache.db-shm");  
		context.deleteDatabase("webviewCache.db-wal");  
		//清除数据缓存
		clearCacheFolder(context.getFilesDir(),System.currentTimeMillis());
		clearCacheFolder(context.getCacheDir(),System.currentTimeMillis());
		//2.2版本才有将应用缓存转移到sd卡的功能
		if(MethodsCompat.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO))
		{
			clearCacheFolder(MethodsCompat.getExternalCacheDir(context), System.currentTimeMillis());
		}
		//清除编辑器保存的临时内容
		Properties props = AppProperty.getProperties(context);
		for(Object key : props.keySet()) 
		{
			String _key = key.toString();
			if(_key.startsWith("temp"))
			{
				AppProperty.removeProperty(context, _key);
			}
		}
	}	
	
	/**
	 * 清除缓存目录
	 * @param dir 目录
	 * @param numDays 当前系统时间
	 * @return
	 */
	public static int clearCacheFolder(File dir, long curTime) 
	{          
	    int deletedFiles = 0;         
	    if (dir!= null && dir.isDirectory()) 
	    {             
	        try 
	        {                
	            for (File child:dir.listFiles()) 
	            {    
	                if (child.isDirectory()) 
	                {              
	                    deletedFiles += clearCacheFolder(child, curTime);          
	                }  
	                if (child.lastModified() < curTime) 
	                {     
	                    if (child.delete()) 
	                    {                   
	                        deletedFiles++;           
	                    }    
	                }    
	            }             
	        } 
	        catch(Exception e) 
	        {       
	            e.printStackTrace();    
	        }     
	    }       
	    return deletedFiles;     
	}
	
	/**
	 * 将对象保存到内存缓存中
	 * @param key
	 * @param value
	 */
	public static void setMemCache(String key, Object value) 
	{
		memCacheRegion.put(key, value);
	}
	
	/**
	 * 从内存缓存中获取对象
	 * @param key
	 * @return
	 */
	public static Object getMemCache(String key)
	{
		return memCacheRegion.get(key);
	}
	
	/**
	 * 保存磁盘缓存
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public static void setDiskCache(Context context, String key, String value) throws IOException 
	{
		FileOutputStream fos = null;
		try
		{
			fos = context.openFileOutput("cache_"+key+".data", Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.flush();
		}
		finally
		{
			try 
			{
				fos.close();
			} 
			catch (Exception e) 
			{
			}
		}
	}
	
	/**
	 * 获取磁盘缓存数据
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static String getDiskCache(Context context, String key) throws IOException 
	{
		FileInputStream fis = null;
		try
		{
			fis = context.openFileInput("cache_"+key+".data");
			byte[] datas = new byte[fis.available()];
			fis.read(datas);
			return new String(datas);
		}
		finally
		{
			try 
			{
				fis.close();
			} 
			catch (Exception e) 
			{
			}
		}
	}
	
	/**
	 * 保存对象
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static boolean saveObject(Context context, Serializable ser, String file) 
	{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try
		{
			fos = context.openFileOutput(file, context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			try 
			{
				oos.close();
			} 
			catch (Exception e) 
			{
			}
			try 
			{
				fos.close();
			}
			catch (Exception e) 
			{
			}
		}
	}
	
	/**
	 * 读取对象
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Serializable readObject(Context context, String file)
	{
		if(!isExistDataCache(context, file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try
		{
			fis = context.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable)ois.readObject();
		}
		catch(FileNotFoundException e)
		{
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//反序列化失败 - 删除缓存文件
			if(e instanceof InvalidClassException)
			{
				File data = context.getFileStreamPath(file);
				data.delete();
			}
		}
		finally
		{
			try 
			{
				ois.close();
			} 
			catch (Exception e) 
			{
			}
			try 
			{
				fis.close();
			} 
			catch (Exception e) 
			{
			}
		}
		return null;
	}
}
