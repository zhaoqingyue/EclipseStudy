package com.zhaoqy.app.demo.weixin.ui.dialog;

import android.content.Context;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.weixin.ui.view.FlippingImageView;

public class FlippingDialog extends BaseDialog 
{
	private FlippingImageView mFivIcon;
	private TextView mHtvText;
	private String mText;

	public FlippingDialog(Context context, String text) 
	{
		super(context);
		mText = text;
		init();
	}

	private void init() 
	{
		setContentView(R.layout.dialog_flipping);
		mFivIcon = (FlippingImageView) findViewById(R.id.loadingdialog_fiv_icon);
		mHtvText = (TextView) findViewById(R.id.loadingdialog_htv_text);
		mFivIcon.startAnimation();
		mHtvText.setText(mText);
	}

	public void setText(String text) 
	{
		mText = text;
		mHtvText.setText(mText);
	}

	@Override
	public void dismiss() 
	{
		if (isShowing()) 
		{
			super.dismiss();
		}
	}
}
