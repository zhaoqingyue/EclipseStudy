/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: NewDataToast.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.view
 * @Description: 新数据Toast提示控件(带音乐播放)
 * @author: zhaoqy
 * @date: 2015-11-26 上午10:45:57
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;

public class NewDataToast extends Toast 
{
	private MediaPlayer mPlayer;
	private boolean mIsSound;
	
	public NewDataToast(Context context) 
	{
		this(context, false);
	}
	
	public NewDataToast(Context context, boolean isSound) 
	{
		super(context);
		
		mIsSound = isSound;
        mPlayer = MediaPlayer.create(context, R.raw.newdatatoast);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				mp.release();
			}        	
        });
    }

	@Override
	public void show() 
	{
		super.show();
		
		if(mIsSound)
		{
			mPlayer.start();
		}
	}
	
	/**
	 * 设置是否播放声音
	 */
	public void setIsSound(boolean isSound) 
	{
		mIsSound = isSound;
	}
	
	/**
	 * 获取控件实例
	 * @param context
	 * @param text 提示消息
	 * @param isSound 是否播放声音
	 * @return
	 */
	public static NewDataToast makeText(Context context, CharSequence text, boolean isSound) 
	{
		NewDataToast result = new NewDataToast(context, isSound);
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        View v = inflate.inflate(R.layout.view_oschina_newdata_toast, null);
        //设置控件最小宽度为手机屏幕宽度
        v.setMinimumWidth(dm.widthPixels);
        
        TextView tv = (TextView)v.findViewById(R.id.new_data_toast_message);
        tv.setText(text);
        
        result.setView(v);
        result.setDuration(600);
        result.setGravity(Gravity.TOP, 0, (int)(dm.density*75));
        return result;
    }
}
