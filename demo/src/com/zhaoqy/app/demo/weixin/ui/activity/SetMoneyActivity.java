package com.zhaoqy.app.demo.weixin.ui.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.register.ToastUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SetMoneyActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private TextView  mTitle;
	private TextView  mChatID;
	private EditText  mMoney;
	private ImageView mBack;
	private Button    mPay;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_setmoney);
		mContext = this;
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mChatID = (TextView) findViewById(R.id.id_setmoney_id);
		mMoney = (EditText) findViewById(R.id.id_setmoney_money);
		mPay = (Button) findViewById(R.id.id_setmoney_pay);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mPay.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.pay_title);
		String strname = "您正在向 " + "zhaoqy" + "<br/>微信号：" + "13421812821" + " 付钱";
		mChatID.setText(Html.fromHtml(strname));
		String strmoney = "￥<font color='#ff11aca6'>" + "188.00" + "<small>元</small></font> ";
		mMoney.setText(Html.fromHtml(strmoney));
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
		case R.id.id_setmoney_pay:
		{
			ToastUtil.showLongToast(mContext, "付款成功！");
			finish();
			break;
		}
		default:
			break;
		}
	}
}
