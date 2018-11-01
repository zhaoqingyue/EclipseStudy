package com.zhaoqy.app.demo.menu.headline.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class ColumnHScrollView extends HorizontalScrollView 
{
	private View      mContent;
	private View      mMore;
	private View      mColumn;
	private ImageView mLeftImage;
	private ImageView mRightImage;
	private Activity  mActivity;
	private int       mScreenWitdh = 0;
	
	public ColumnHScrollView(Context context) 
	{
		super(context);
	}

	public ColumnHScrollView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	public ColumnHScrollView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) 
	{
		super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
		shade_ShowOrHide();
		if(!mActivity.isFinishing() && mContent != null && mLeftImage !=null && mRightImage != null && mMore != null && mColumn !=null)
		{
			if(mContent.getWidth() <= mScreenWitdh)
			{
				mLeftImage.setVisibility(View.GONE);
				mRightImage.setVisibility(View.GONE);
			}
		}
		else
		{
			return;
		}
		if(paramInt1 ==0)
		{
			mLeftImage.setVisibility(View.GONE);
			mRightImage.setVisibility(View.VISIBLE);
			return;
		}
		if(mContent.getWidth() - paramInt1 + mMore.getWidth() + mColumn.getLeft() == mScreenWitdh)
		{
			mLeftImage.setVisibility(View.VISIBLE);
			mRightImage.setVisibility(View.GONE);
			return;
		}
		mLeftImage.setVisibility(View.VISIBLE);
		mRightImage.setVisibility(View.VISIBLE);
	}
	
	public void setParam(Activity activity, int screenWitdh, View paramView1, ImageView paramView2, ImageView paramView3, View paramView4, View paramView5)
	{
		mActivity = activity;
		mScreenWitdh = screenWitdh;
		mContent = paramView1;
		mLeftImage = paramView2;
		mRightImage = paramView3;
		mMore = paramView4;
		mColumn = paramView5;
	}
	
	public void shade_ShowOrHide() 
	{
		if (!mActivity.isFinishing() && mContent != null) 
		{
			measure(0, 0);
			if (mScreenWitdh >= getMeasuredWidth()) 
			{
				mLeftImage.setVisibility(View.GONE);
				mRightImage.setVisibility(View.GONE);
			}
		}
		else 
		{
			return;
		}
		if (getLeft() == 0) 
		{
			mLeftImage.setVisibility(View.GONE);
			mRightImage.setVisibility(View.VISIBLE);
			return;
		}
		if (getRight() == getMeasuredWidth() - mScreenWitdh) 
		{
			mLeftImage.setVisibility(View.VISIBLE);
			mRightImage.setVisibility(View.GONE);
			return;
		}
		mLeftImage.setVisibility(View.VISIBLE);
		mRightImage.setVisibility(View.VISIBLE);
	}
}
