package com.zhaoqy.app.demo.page.mushroom;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.zhaoqy.app.demo.R;

public class TopIndicator extends LinearLayout 
{
	private int[] mDrawableIds = new int[] { 
			R.drawable.page_slide_mushroom_home,
			R.drawable.page_slide_mushroom_category, 
			R.drawable.page_slide_mushroom_collect,
			R.drawable.page_slide_mushroom_setting };
	
	private List<CheckedTextView> mCheckedList = new ArrayList<CheckedTextView>();
	private List<View> mViewList = new ArrayList<View>();
	private CharSequence[] mLabels = new CharSequence[] { "首页", "分类", "收藏", "设置" };
	private int mScreenWidth;
	private int mUnderLineWidth;
	private View mUnderLine;
	private int mUnderLineFromX = 0;

	@SuppressLint("NewApi")
	public TopIndicator(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public TopIndicator(Context context, AttributeSet attrs) 
	{
		this(context, attrs, 0);
	}

	public TopIndicator(Context context) 
	{
		super(context);
		init(context);
	}

	private void init(final Context context) 
	{
		setOrientation(LinearLayout.VERTICAL);
		setBackgroundColor(Color.rgb(250, 250, 250));

		mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
		mUnderLineWidth = mScreenWidth / mLabels.length;
		mUnderLine = new View(context);
		mUnderLine.setBackgroundColor(Color.rgb(247, 88, 123));
		LinearLayout.LayoutParams underLineParams = new LinearLayout.LayoutParams(mUnderLineWidth, 4);
		LinearLayout topLayout = new LinearLayout(context);
		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		topLayout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;

		int size = mLabels.length;
		for (int i = 0; i < size; i++) 
		{
			final int index = i;
			final View view = inflater.inflate(R.layout.view_page_slide_mushroom_top_indicator, null);
			final CheckedTextView itemName = (CheckedTextView) view.findViewById(R.id.item_name);
			itemName.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(mDrawableIds[i]), null, null, null);
			itemName.setCompoundDrawablePadding(10);
			itemName.setText(mLabels[i]);
			topLayout.addView(view, params);
			itemName.setTag(index);
			mCheckedList.add(itemName);
			mViewList.add(view);

			view.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if (null != mTabListener) 
					{
						mTabListener.onIndicatorSelected(index);
					}
				}
			});

			if (i == 0) 
			{
				itemName.setChecked(true);
				itemName.setTextColor(Color.rgb(247, 88, 123));
			} 
			else 
			{
				itemName.setChecked(false);
				itemName.setTextColor(Color.rgb(19, 12, 14));
			}
		}
		this.addView(topLayout, topLayoutParams);
		this.addView(mUnderLine, underLineParams);
	}

	public void setTabsDisplay(Context context, int index) 
	{
		int size = mCheckedList.size();
		for (int i = 0; i < size; i++) 
		{
			CheckedTextView checkedTextView = mCheckedList.get(i);
			if ((Integer) (checkedTextView.getTag()) == index) 
			{
				checkedTextView.setChecked(true);
				checkedTextView.setTextColor(Color.rgb(247, 88, 123));
			} 
			else 
			{
				checkedTextView.setChecked(false);
				checkedTextView.setTextColor(Color.rgb(19, 12, 14));
			}
		}
		doUnderLineAnimation(index);
	}

	private void doUnderLineAnimation(int index) 
	{
		TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX, index * mUnderLineWidth, 0, 0);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(150);
		mUnderLine.startAnimation(animation);
		mUnderLineFromX = index * mUnderLineWidth;
	}

	private OnTopIndicatorListener mTabListener;

	public interface OnTopIndicatorListener 
	{
		void onIndicatorSelected(int index);
	}

	public void setOnTopIndicatorListener(OnTopIndicatorListener listener) 
	{
		mTabListener = listener;
	}
}
