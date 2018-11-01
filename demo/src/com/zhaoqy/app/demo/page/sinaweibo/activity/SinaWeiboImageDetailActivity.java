package com.zhaoqy.app.demo.page.sinaweibo.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.zhaoqy.app.demo.R;

public class SinaWeiboImageDetailActivity extends Activity 
{
	ImageView   mImageView;
	ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo_image_detail);
		
		Intent intent = getIntent();
		String ur = intent.getStringExtra("orurl");
		
		mImageView = (ImageView) findViewById(R.id.id_sinaweibo_image_detail_img);
		mProgressBar = (ProgressBar) findViewById(R.id.id_sinaweibo_image_detail_progressbar);
		new ImgAysnTask().execute(ur);
	}

	class ImgAysnTask extends AsyncTask<String, Integer, Bitmap> 
	{
		@Override
		protected void onCancelled() 
		{
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap result) 
		{
			mImageView.setImageBitmap(result);
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) 
		{
			mProgressBar.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected Bitmap doInBackground(String... params) 
		{
			try 
			{
				URL url = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				int lenth = conn.getContentLength();
				File file = new File("mnt/sdcard/yuantu.jpg");
				if (!file.exists()) 
				{
					file.createNewFile();
				}
				FileOutputStream os = new FileOutputStream(file);
				BufferedInputStream bs = new BufferedInputStream(is);
				byte[] bytes = new byte[1024];
				int down = 0;
				int dowpos = 0;
				while ((down = bs.read(bytes)) != -1) 
				{
					dowpos += down;
					publishProgress((dowpos * 100) / lenth);
					os.write(bytes, 0, down);
				}
				os.close();
				bs.close();
			} 
			catch (MalformedURLException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			return BitmapFactory.decodeFile("mnt/sdcard/yuantu.jpg");
		}
	}
}
