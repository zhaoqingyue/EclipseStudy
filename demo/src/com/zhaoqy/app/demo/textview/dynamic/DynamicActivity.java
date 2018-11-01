package com.zhaoqy.app.demo.textview.dynamic;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class DynamicActivity extends Activity implements OnClickListener, OnGlobalLayoutListener
{
	private TextView  mTitle;
	private ImageView mBack;
	private View      mRoot;  //最外层布局
    private View      mLogo;  //Logo图标
    private View      mLabel; //Logo附近的文字
    private int       mRootBottom = Integer.MIN_VALUE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_textview_dynamic);
		
		initView(); 
		initData();
		setListener();
	}

	private void initView() 
	{
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		
		mRoot = findViewById(R.id.root);
		mLogo = findViewById(R.id.logo);
		mLabel = findViewById(R.id.label);
	}

	private void initData() 
	{
		mTitle.setText(R.string.textview_item3);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRoot.getViewTreeObserver().addOnGlobalLayoutListener(this);
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

	@Override
	public void onGlobalLayout() 
	{
		Rect r = new Rect();
		mRoot.getGlobalVisibleRect(r);
        //进入Activity时会布局，第一次调用onGlobalLayout，先记录开始软键盘没有弹出时底部的位置
        if (mRootBottom == Integer.MIN_VALUE) 
        {
        	mRootBottom = r.bottom;
            return;
        }
        //adjustResize，软键盘弹出后高度会变小
        if (r.bottom < mRootBottom) 
        {
            RelativeLayout.LayoutParams lp = (LayoutParams) mLogo.getLayoutParams();
            //如果Logo不是水平居中，说明是因为接下来的改变Logo大小位置导致的再次布局，忽略掉，否则无限循环
            if (lp.getRules()[RelativeLayout.CENTER_HORIZONTAL] != 0) 
            {
                //Logo显示到左上角
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0); //取消水平居中
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);    //左对齐

                //缩小Logo为1/2
                int height = mLogo.getHeight(); //getMeasuredHeight()
                int width = mLogo.getWidth();
                lp.width = width / 2;
                lp.height = height / 2;
                mLogo.setLayoutParams(lp);

                //Logo下的文字
                RelativeLayout.LayoutParams labelParams = (LayoutParams) mLabel.getLayoutParams();
                labelParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0); //取消水平居中
                labelParams.addRule(RelativeLayout.BELOW, 0);             //取消显示到logo的下方
                labelParams.addRule(RelativeLayout.RIGHT_OF, R.id.logo);  //显示到Logo的右方
                labelParams.addRule(RelativeLayout.CENTER_VERTICAL);      //垂直居中
                mLabel.setLayoutParams(labelParams);
            }
        } 
        else 
        { 
        	//软键盘收起或初始化时
            RelativeLayout.LayoutParams lp = (LayoutParams) mLogo.getLayoutParams();
            //如果没有水平居中，说明是软键盘收起，否则是开始时的初始化或者因为此处if条件里的语句修改控件导致的再次布局，忽略掉，否则无限循环
            if (lp.getRules()[RelativeLayout.CENTER_HORIZONTAL] == 0) 
            {
                //居中Logo
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);

                //还原Logo为原来大小
                int height = mLogo.getHeight();
                int width = mLogo.getWidth();
                lp.width = width * 2;
                lp.height = height * 2;
                mLogo.setLayoutParams(lp);

                //Logo下的文字
                RelativeLayout.LayoutParams labelParams = (LayoutParams) mLabel.getLayoutParams();
                labelParams.addRule(RelativeLayout.CENTER_HORIZONTAL);  //设置水平居中
                labelParams.addRule(RelativeLayout.BELOW, R.id.logo);   //设置显示到Logo下面
                labelParams.addRule(RelativeLayout.RIGHT_OF, 0);        //取消显示到Logo右面
                labelParams.addRule(RelativeLayout.CENTER_VERTICAL, 0); //取消垂直居中
                mLabel.setLayoutParams(labelParams);
            }
        }
	}
}
