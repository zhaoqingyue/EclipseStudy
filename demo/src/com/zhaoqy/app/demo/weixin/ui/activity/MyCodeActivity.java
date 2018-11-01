package com.zhaoqy.app.demo.weixin.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zhaoqy.app.demo.R;

public class MyCodeActivity extends Activity implements OnClickListener
{
	private TextView  mTitle;
	private TextView  mName;
	private TextView  mAccout;
	private ImageView mBack;
	private ImageView mCode;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_mycode);
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mName = (TextView) findViewById(R.id.id_mycode_name);
		mAccout = (TextView) findViewById(R.id.id_mycode_id);
		mCode = (ImageView) findViewById(R.id.id_mycode_code);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText(R.string.mycode_title);
		mBack.setVisibility(View.VISIBLE);
		mName.setText("zhaoqy");
		mAccout.setText(R.string.weixin_me_chat_id);
		Bitmap qrcode = generateQRCode("zhaoqy@User:" + "zhaoqy");
		mCode.setImageBitmap(qrcode);
	}
	
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
		default:
			break;
		}
	}

	private Bitmap bitMatrix2Bitmap(BitMatrix matrix) 
	{
		int w = matrix.getWidth();
		int h = matrix.getHeight();
		int[] rawData = new int[w * h];
		for (int i=0; i<w; i++) 
		{
			for (int j=0; j<h; j++) 
			{
				int color = Color.WHITE;
				if (matrix.get(i, j)) 
				{
					color = Color.BLACK;
				}
				rawData[i + (j * w)] = color;
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.RGB_565);
		bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
		return bitmap;
	}

	private Bitmap generateQRCode(String content) 
	{
		try 
		{
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 500, 500);
			return bitMatrix2Bitmap(matrix);
		} 
		catch (WriterException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
