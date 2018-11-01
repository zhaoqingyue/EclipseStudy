package com.zhaoqy.app.demo.picture.qrcode.activity;

import java.util.Hashtable;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zhaoqy.app.demo.R;

public class QrCodeImageActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private ImageView mImage0;
	private ImageView mImage1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_qrcode);
		initView();
		setListener();
		initData();
		initImage0();
		initImage1();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mImage0 = (ImageView) findViewById(R.id.id_qrcode_image0);
		mImage1 = (ImageView) findViewById(R.id.id_qrcode_image1);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_item6);
	}
	
	private void initImage0() 
	{
		Bitmap qrcode = generateQRCode("zhaoqy@User:" + "zhaoqy");
		mImage0.setImageBitmap(qrcode);
	}
	
	private void initImage1() 
	{
		Resources res = getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.page_slide_way_icon_login_weixin);
		Bitmap qrcode = createImage("zhaoqy", 500, 500, bmp);
		mImage1.setImageBitmap(qrcode);
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
	
	 /**
     * 生成图片,并添加logo
     * @param text
     * @param w
     * @param h
     * @param logo
     * @return
     */
    public static  Bitmap createImage(String text,int w,int h,Bitmap logo) 
    {
        try 
        {
            Bitmap scaleLogo = getScaleLogo(logo, w, h);
            int offsetX = 0;
            int offsetY = 0;
            if(scaleLogo != null)
            {
                offsetX = (w - scaleLogo.getWidth())/2;
                offsetY = (h - scaleLogo.getHeight())/2;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            
            for (int y = 0; y < h; y++) 
            {
                for (int x = 0; x < w; x++) 
                {
                    //判断是否在logo图片中
                    if(offsetX != 0 && offsetY != 0 && x >= offsetX && x < offsetX+scaleLogo.getWidth() && y>= offsetY && y < offsetY+scaleLogo.getHeight())
                    {
                        int pixel = scaleLogo.getPixel(x-offsetX,y-offsetY);
                        //如果logo像素是透明则写入二维码信息
                        if(pixel == 0)
                        {
                            if(bitMatrix.get(x, y))
                            {
                                pixel = 0xff000000;
                            }
                            else
                            {
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * w + x] = pixel;
                    }
                    else
                    {
                        if (bitMatrix.get(x, y)) 
                        {
                            pixels[y * w + x] = 0xff000000;
                        } 
                        else 
                        {
                            pixels[y * w + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;

        } 
        catch (WriterException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
     * 缩放logo到二维码的1/5
     * @param logo
     * @param w
     * @param h
     * @return
     */
    private static Bitmap getScaleLogo(Bitmap logo,int w,int h)
    {
        if(logo == null)return null;
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 / logo.getHeight());
        matrix.postScale(scaleFactor,scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
        return result;
    }
}
