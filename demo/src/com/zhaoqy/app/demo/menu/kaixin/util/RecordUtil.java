/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: RecordUtil.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.util
 * @Description: 录音工具类
 * @author: zhaoqy
 * @date: 2015-11-6 下午1:58:05
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.util;

import java.io.File;
import java.io.IOException;
import android.media.MediaRecorder;

public class RecordUtil 
{
	private static final int SAMPLE_RATE_IN_HZ = 8000;
	private MediaRecorder mRecorder = new MediaRecorder();
	private String        mPath; //录音的路径

	public RecordUtil(String path) 
	{
		mPath = path;
	}

	/**
	 * @Title: start
	 * @Description: 开始录音
	 * @throws IOException
	 * @return: void
	 */
	@SuppressWarnings("deprecation")
	public void start() throws IOException 
	{
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) 
		{
			throw new IOException("SD Card is not mounted,It is  " + state + ".");
		}
		File directory = new File(mPath).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) 
		{
			throw new IOException("Path to file could not be created");
		}
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		mRecorder.setOutputFile(mPath);
		mRecorder.prepare();
		mRecorder.start();
	}

	/**
	 * @Title: stop
	 * @Description: 结束录音
	 * @throws IOException
	 * @return: void
	 */
	public void stop() throws IOException 
	{
		mRecorder.stop();
		mRecorder.release();
	}

	/**
	 * @Title: getAmplitude
	 * @Description: 获取录音时间
	 * @return: double
	 */
	public double getAmplitude() 
	{
		if (mRecorder != null) 
		{
			return (mRecorder.getMaxAmplitude());
		}
		return 0;
	}
}
