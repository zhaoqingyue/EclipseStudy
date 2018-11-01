package com.zhaoqy.app.demo.picture.process;

import com.zhaoqy.app.demo.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageProcessActivity extends Activity implements OnClickListener
{
	private String tag = "demo";
	private ImageView mBack;
	private TextView  mTitle;
	private Bitmap bitmap;
	private Bitmap watermarkBitmap;
	private ImageView iv;
	private Button rotateButton;
	private Button scalebutton;
	private Button watermarkbutton;
	private Button resetkbutton;
	private Button alphaplusbutton;
	private Button alphaminusbutton;
	private Button skewbutton;
	private Button meshbutton;

	private int alpha = 255;
	private String URL = "http://e.hiphotos.baidu.com/image/pic/item/5243fbf2b2119313f4f4d5ba67380cd791238d00.jpg";
	private String watermarkURL = "http://www.javaapk.com/wp-content/themes/javaapktm/images/logo.png";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_process);
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		iv = (ImageView) findViewById(R.id.id_process_iamge);
		rotateButton = (Button) findViewById(R.id.id_process_rotate);
		scalebutton = (Button) findViewById(R.id.id_process_scale);
		watermarkbutton = (Button) findViewById(R.id.id_process_ｗatermark);
		resetkbutton = (Button) findViewById(R.id.id_process_reset);
		alphaplusbutton = (Button) findViewById(R.id.id_process_alpha_add);
		alphaminusbutton = (Button) findViewById(R.id.id_process_alpha_sub);
		skewbutton = (Button) findViewById(R.id.id_process_tilt);
		meshbutton = (Button) findViewById(R.id.id_process_distortion);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		rotateButton.setOnClickListener(this);
		scalebutton.setOnClickListener(this);
		watermarkbutton.setOnClickListener(this);
		resetkbutton.setOnClickListener(this);
		alphaplusbutton.setOnClickListener(this);
		alphaminusbutton.setOnClickListener(this);
		skewbutton.setOnClickListener(this);
		meshbutton.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_item5);
		
		GetImageTask git = new GetImageTask();
		git.execute(URL);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		case R.id.id_process_rotate:
		{
			try 
			{
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				Log.e(tag, "dstbmp width:" + dstbmp.getWidth() + ", height:" + dstbmp.getHeight());
				destoryBitmap(bitmap);
				setBitmap(dstbmp);
				BitmapDrawable bmpDraw = new BitmapDrawable(bitmap);
				iv.setImageDrawable(bmpDraw);
			} 
			catch (Exception e) 
			{
				Log.e(tag, e.toString(), e);
			}
			break;
		}
		case R.id.id_process_scale:
		{
			try 
			{
				Matrix matrix = new Matrix();
				matrix.postScale(1.8f, 1.8f);
				Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				destoryBitmap(bitmap);
				setBitmap(dstbmp);
				BitmapDrawable bmpDraw = new BitmapDrawable(bitmap);
				iv.setImageDrawable(bmpDraw);
			}
			catch (Exception e) 
			{
				Log.e(tag, e.toString(), e);
			}
			break;
		}
		case R.id.id_process_ｗatermark:
		{
			try 
			{
				GetWatermarkTask git = new GetWatermarkTask();
				git.execute(watermarkURL);
			} 
			catch (Exception e) 
			{
				Log.e(tag, e.toString(), e);
			}
			break;
		}
		case R.id.id_process_reset:
		{
			try 
			{
				GetImageTask git = new GetImageTask();
				git.execute(URL);
			} 
			catch (Exception e) 
			{
				Log.e(tag, e.toString(), e);
			}
			break;
		}
		case R.id.id_process_alpha_add:
		{
			try 
			{
				alpha += 10;
				if (alpha > 255) 
				{
					alpha = 255;
				}
				iv.setAlpha(alpha);
			} 
			catch (Exception e) 
			{
				Log.e(tag, e.toString(), e);
			}
			break;
		}
		case R.id.id_process_alpha_sub:
		{
			try 
			{
				alpha -= 10;
				if (alpha < 0) 
				{
					alpha = 0;
				}
				iv.setAlpha(alpha);
			} 
			catch (Exception e) 
			{
				Log.e(tag, e.toString(), e);
			}
			break;
		}
		case R.id.id_process_tilt:
		{
			skew(0.07f, 0.02f, 0.6f, 0.9f);
			break;
		}
		case R.id.id_process_distortion:
		{
			mesh();
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		destoryBitmap(bitmap);
		destoryBitmap(watermarkBitmap);
	}
	
	public void destoryBitmap(Bitmap bitmap) 
	{
		try 
		{
			if (bitmap != null && !bitmap.isRecycled()) 
			{
				bitmap.recycle();
				bitmap = null;
			}
			System.gc();
		} 
		catch (Exception e) 
		{
			Log.e(tag, e.toString(), e);
		}
	}
	
	class GetImageTask extends AsyncTask<String, Integer, Bitmap> 
	{
		private String tag = GetImageTask.class.toString();

		@Override
		protected Bitmap doInBackground(String... params) 
		{
			IDataTrafficManage dataTrafficManage = Factory.getDataTrafficManage();
			Bitmap imageBm = dataTrafficManage.getImage(params[0]);
			return imageBm;
		}

		protected void onPostExecute(Bitmap result) 
		{
			try 
			{
				if (result != null) 
				{
					destoryBitmap(bitmap);
					setBitmap(result);
					@SuppressWarnings("deprecation")
					BitmapDrawable bmpDraw = new BitmapDrawable(bitmap);
					iv.setImageDrawable(bmpDraw);
				}
				else 
				{
					Log.i(tag, "result is null");
				}
			} 
			catch (Exception e) 
			{
				Log.e(tag, e.toString(), e);
			}
		}
	}

	class GetWatermarkTask extends AsyncTask<String, Integer, Bitmap> 
	{
		private String tag = GetWatermarkTask.class.toString();

		@Override
		protected Bitmap doInBackground(String... params) 
		{
			IDataTrafficManage dataTrafficManage = Factory.getDataTrafficManage();
			Bitmap imageBm = dataTrafficManage.getImage(params[0]);
			return imageBm;
		}

		protected void onPostExecute(Bitmap result) 
		{
			try 
			{
				if (result != null && bitmap != null) 
				{
					watermarkBitmap = result;
					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					int widthWatermark = watermarkBitmap.getWidth();
					int heightWatermark = watermarkBitmap.getHeight();
					Bitmap newb = Bitmap.createBitmap(width, height,Config.ARGB_8888);
					Canvas cv = new Canvas(newb);
					cv.drawBitmap(bitmap, 0, 0, null);
					cv.drawBitmap(watermarkBitmap, width - widthWatermark + 5, height - heightWatermark + 5, null);
					cv.save(Canvas.ALL_SAVE_FLAG);
					destoryBitmap(bitmap);
					destoryBitmap(watermarkBitmap);
					setBitmap(newb);
					@SuppressWarnings("deprecation")
					BitmapDrawable bmpDraw = new BitmapDrawable(bitmap);
					iv.setImageDrawable(bmpDraw);
				} 
				else 
				{
					Log.i(tag, "result is null");
				}
			} 
			catch (Exception e) 
			{
				Log.e(tag, e.toString(), e);
			}
		}
	}

	public void setBitmap(Bitmap bitmap) 
	{
		this.bitmap = bitmap;
		this.alpha = 255;
	}

	public void skew(float x, float y) 
	{
		try 
		{
			Matrix matrix = new Matrix();
			// matrix.postScale(0.8f, 0.8f);
			matrix.setSkew(x, y);
			Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			destoryBitmap(bitmap);
			setBitmap(dstbmp);
			@SuppressWarnings("deprecation")
			BitmapDrawable bmpDraw = new BitmapDrawable(bitmap);
			iv.setImageDrawable(bmpDraw);
		} 
		catch (Exception e) 
		{
			Log.e(tag, e.toString(), e);
		}
	}

	public void skew(float x, float y, float px, float py) 
	{
		try 
		{
			Matrix matrix = new Matrix();
			// matrix.postScale(0.8f, 0.8f);
			matrix.setSkew(x, y, px, py);
			Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			destoryBitmap(bitmap);
			setBitmap(dstbmp);
			@SuppressWarnings("deprecation")
			BitmapDrawable bmpDraw = new BitmapDrawable(bitmap);
			iv.setImageDrawable(bmpDraw);
		} 
		catch (Exception e) 
		{
			Log.e(tag, e.toString(), e);
		}
	}

	public void mesh() 
	{
		try 
		{
			int width = 80;
			int height = 80;
			int COUNT = (width + 1) * (height + 1);
			float[] verts = new float[COUNT * 2];
			float[] orig = new float[COUNT * 2];
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();

			int index = 0;
			for (int y = 0; y <= height; y++) 
			{
				float fy = bitmapHeight * y / height;
				for (int x = 0; x <= width; x++) 
				{
					float fx = bitmapWidth * x / width;
					orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
					orig[index * 2 + 1] = verts[index * 2 + 1] = fy;
					index += 1;
				}
			}
			float cx =200;
			float cy =280;
			warp(cx, cy, COUNT, verts, orig);
			Bitmap newb = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Config.ARGB_8888);
			Canvas cv = new Canvas(newb);
			cv.drawBitmapMesh(bitmap, width, height, verts, 0, null, 0, null);
			cv.save(Canvas.ALL_SAVE_FLAG);
			destoryBitmap(bitmap);
			setBitmap(newb);
			@SuppressWarnings("deprecation")
			BitmapDrawable bmpDraw = new BitmapDrawable(bitmap);
			iv.setImageDrawable(bmpDraw);
		} 
		catch (Exception e) 
		{
			Log.e(tag, e.toString(), e);
		}
	}
	
	private void warp(float cx, float cy, int count, float[] verts, float[] orig) 
	{
		for (int i = 0; i < count * 2; i += 2) 
		{
			float dx = cx - orig[i + 0];
			float dy = cy - orig[i + 1];
			float dd = dx * dx + dy * dy;
			float d = (float) Math.sqrt(dd);
			float pull = 80000 / ((float) (dd * d));
			if (pull >= 1) 
			{
				verts[i + 0] = cx;
				verts[i + 1] = cy;
			} 
			else 
			{
				verts[i + 0] = orig[i + 0] + dx * pull;
				verts[i + 1] = orig[i + 1] + dy * pull;
			}
		}
	}
}
