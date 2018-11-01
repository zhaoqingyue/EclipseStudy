package com.zhaoqy.app.demo.wifi.wifihot.view;

import java.util.Date;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class WifiListView extends ListView implements OnScrollListener 
{
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int RATIO = 3;
	private OnRefreshListener mRefreshListener;
	private RotateAnimation   mReverseAnimation;
	private RotateAnimation   mAnimation;
	private LinearLayout mHeadView;
	private TextView     mTips;
	private TextView     mLastUpdated;
	private ImageView    mArrow;
	private ProgressBar  mProgressBar;
	private boolean      mIsRecored;
	private boolean      mIsBack;
	private boolean      mIsRefreshable;
	private int          mHeadContentHeight;
	private int          mStartY;
	private int          mFirstItemIndex;
	private int          mState;

	public WifiListView(Context context) 
	{
		super(context);
		init(context);
	}

	public WifiListView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}

	private void init(Context context) 
	{
		setCacheColorHint(context.getResources().getColor(android.R.color.transparent));
		mHeadView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_pull_refresh, null);
		mArrow = (ImageView) mHeadView.findViewById(R.id.id_pull_refresh_head_arrow);
		mArrow.setMinimumWidth(70);
		mArrow.setMinimumHeight(50);
		mProgressBar = (ProgressBar) mHeadView.findViewById(R.id.id_pull_refresh_head_progressBar);
		mTips = (TextView) mHeadView.findViewById(R.id.id_pull_refresh_head_tips);
		mLastUpdated = (TextView) mHeadView.findViewById(R.id.id_pull_refresh_head_lastUpdated);

		measureView(mHeadView);
		mHeadContentHeight = mHeadView.getMeasuredHeight(); 
		mHeadView.getMeasuredWidth();
		mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
		mHeadView.invalidate();
		addHeaderView(mHeadView, null, false);
		setOnScrollListener(this);

		mAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mAnimation.setInterpolator(new LinearInterpolator());
		mAnimation.setDuration(250);
		mAnimation.setFillAfter(true);

		mReverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseAnimation.setInterpolator(new LinearInterpolator());
		mReverseAnimation.setDuration(200);
		mReverseAnimation.setFillAfter(true);

		mState = DONE;
		mIsRefreshable = false;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	{
		mFirstItemIndex = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
	}

	public boolean onTouchEvent(MotionEvent event) 
	{
		if (mIsRefreshable) 
		{
			switch (event.getAction()) 
			{
			case MotionEvent.ACTION_DOWN:
			{
				if (mFirstItemIndex == 0 && !mIsRecored) 
				{
					mIsRecored = true;
					mStartY = (int) event.getY();
				}
				break;
			}
			case MotionEvent.ACTION_UP:
			{
				if (mState != REFRESHING) 
				{
					if (mState == PULL_To_REFRESH) 
					{
						mState = DONE;
						changeHeaderViewByState();
					}
					
					if (mState == RELEASE_To_REFRESH) 
					{
						mState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				mIsRecored = false;
				mIsBack = false;
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				int tempY = (int) event.getY();

				if (!mIsRecored && mFirstItemIndex == 0) 
				{
					mIsRecored = true;
					mStartY = tempY;
				}

				if (mState != REFRESHING && mIsRecored) 
				{
					if (mState == RELEASE_To_REFRESH) 
					{
						setSelection(0);
						if (((tempY - mStartY) / RATIO < mHeadContentHeight) && (tempY - mStartY) > 0) 
						{
							mState = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
						else if (tempY - mStartY <= 0) 
						{
							mState = DONE;
							changeHeaderViewByState();
						}
					}
					if (mState == PULL_To_REFRESH) 
					{
						setSelection(0);
						if ((tempY - mStartY) / RATIO >= mHeadContentHeight) 
						{
							mState = RELEASE_To_REFRESH;
							mIsBack = true;
							changeHeaderViewByState();
						}
						else if (tempY - mStartY <= 0) 
						{
							mState = DONE;
							changeHeaderViewByState();
						}
					}

					if (mState == DONE) 
					{
						if (tempY - mStartY > 0) 
						{
							mState = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (mState == PULL_To_REFRESH) 
					{
						mHeadView.setPadding(0, -1 * mHeadContentHeight + (tempY - mStartY) / RATIO, 0, 0);
					}

					if (mState == RELEASE_To_REFRESH) 
					{
						mHeadView.setPadding(0, (tempY - mStartY) / RATIO - mHeadContentHeight, 0, 0);
					}
				}
				break;
			}
			default:
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void changeHeaderViewByState() 
	{
		switch (mState) 
		{
		case RELEASE_To_REFRESH:
		{
			mArrow.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mTips.setVisibility(View.VISIBLE);
			mLastUpdated.setVisibility(View.VISIBLE);
			mArrow.clearAnimation();
			mArrow.startAnimation(mAnimation);
			mTips.setText(R.string.wifi_release_refresh);
			break;
		}
		case PULL_To_REFRESH:
		{
			mProgressBar.setVisibility(View.GONE);
			mTips.setVisibility(View.VISIBLE);
			mLastUpdated.setVisibility(View.VISIBLE);
			mArrow.clearAnimation();
			mArrow.setVisibility(View.VISIBLE);
			if (mIsBack) 
			{
				mIsBack = false;
				mArrow.clearAnimation();
				mArrow.startAnimation(mReverseAnimation);
				mTips.setText(R.string.wifi_pull_refresh);
			}
			else 
			{
				mTips.setText(R.string.wifi_pull_refresh);
			}
			break;
		}
		case REFRESHING:
		{
			mHeadView.setPadding(0, 0, 0, 0);
			mProgressBar.setVisibility(View.VISIBLE);
			mArrow.clearAnimation();
			mArrow.setVisibility(View.GONE);
			mTips.setText("正在刷新...");
			mLastUpdated.setVisibility(View.VISIBLE);
			mLastUpdated.setVisibility(View.GONE);
			break;
		}
		case DONE:
		{
			mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
			mProgressBar.setVisibility(View.GONE);
			mArrow.clearAnimation();
			mArrow.setImageResource(R.drawable.wifi_pull_to_refresh_arrow);
			mTips.setText(R.string.wifi_pull_refresh);
			mLastUpdated.setVisibility(View.VISIBLE);
			break;
		}
		default:
			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) 
	{
		mRefreshListener = refreshListener;
		mIsRefreshable = true;
	}

	public interface OnRefreshListener 
	{
		public void onRefresh();
	}

	@SuppressWarnings("deprecation")
	public void onRefreshComplete() 
	{
		mState = DONE;
		mLastUpdated.setText(R.string.wifi_recently_update + new Date().toLocaleString());
		changeHeaderViewByState();
	}

	private void onRefresh() 
	{
		if (mRefreshListener != null) 
		{
			mRefreshListener.onRefresh();
		}
	}

	@SuppressWarnings("deprecation")
	private void measureView(View child) 
	{
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) 
		{
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) 
		{
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		}
		else 
		{
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@SuppressWarnings("deprecation")
	public void setAdapter(BaseAdapter adapter) 
	{
		mLastUpdated.setText(R.string.wifi_recently_update + new Date().toLocaleString());
		super.setAdapter(adapter);
	}
}
