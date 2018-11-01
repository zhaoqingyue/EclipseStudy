package com.zhaoqy.app.demo.page.way.util;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class Utils 
{
	public static final int IO_BUFFER_SIZE = 8 * 1024;

    private Utils() 
    {
    };

    public static void disableConnectionReuseIfNecessary() 
    {
        if (hasHttpConnectionBug()) 
        {
            System.setProperty("http.keepAlive", "false");
        }
    }

    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) 
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) 
        {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    @SuppressLint("NewApi")
    public static boolean isExternalStorageRemovable() 
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) 
        {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @SuppressLint("NewApi")
    public static File getExternalCacheDir(Context context) 
    {
        if (hasExternalCacheDir()) 
        {
            return context.getExternalCacheDir();
        }

        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    @SuppressLint("NewApi")
    public static long getUsableSpace(File path) 
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) 
        {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    
    public static int getMemoryClass(Context context) 
    {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    public static boolean hasHttpConnectionBug() 
    {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
    }

    public static boolean hasExternalCacheDir() 
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasActionBar() 
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
}
