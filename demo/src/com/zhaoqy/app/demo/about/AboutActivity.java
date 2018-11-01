package com.zhaoqy.app.demo.about;

import java.io.InputStream;
import org.apache.http.util.EncodingUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class AboutActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mAbout; //关于信息
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mAbout = (TextView) findViewById(R.id.id_about_text);
		
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.main_item16);
		mAbout.setText(getFromAssets("about.txt"));
		mBack.setOnClickListener(this);
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

	/**
	 * 
	 * @Title: getFromAssets
	 * @Description: 读取本地文件信息(该方法可以换段落)
	 * @param fileName: 本地文件名
	 * @return
	 * @return: String
	 */
	public String getFromAssets(String fileName)
	{
		String about = "";
		try 
		{
			InputStream in = getResources().getAssets().open(fileName);
			int lenght = in.available();       //获取文件的字节数
			byte[]  buffer = new byte[lenght]; //创建byte数组
		
			in.read(buffer);  //将文件中的数据读到byte数组中
			about = EncodingUtils.getString(buffer, "UTF-8");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return about;
	}
}
