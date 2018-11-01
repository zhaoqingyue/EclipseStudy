/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: PullToRefreshListView.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.view
 * @Description: 下拉刷新控件
 * @author: zhaoqy
 * @date: 2015-11-19 下午2:34:44
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.view;

import com.zhaoqy.app.demo.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class PullToRefreshListView extends ListView implements OnScrollListener 
{
    private final static int PULL_To_REFRESH = 0;    //下拉刷新标志   
    private final static int RELEASE_To_REFRESH = 1; //松开刷新标志   
    private final static int REFRESHING = 2;         //正在刷新标志   
    private final static int DONE = 3;               //刷新完成标志   
  
    private LayoutInflater inflater;  
    private RelativeLayout   headView;  
    private TextView       tipsTextview;  
    private TextView       lastUpdatedTextView;  
    private ImageView      arrowImageView;  
    private ProgressBar    progressBar;  
    //用来设置箭头图标动画效果   
    private RotateAnimation animation;  
    private RotateAnimation reverseAnimation;  
    //用于保证startY的值在一个完整的touch事件中只被记录一次   
    private boolean isRecored;  
    private int headContentHeight;  
    private int headContentOriginalTopPadding;
    private int startY;  
    private int firstItemIndex;  
    private int currentScrollState;
    private int state;  
    private boolean isBack;  
    public OnRefreshListener refreshListener;  
    
    public PullToRefreshListView(Context context, AttributeSet attrs) 
    {  
        super(context, attrs);  
        init(context);  
    }  
    
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) 
    {  
        super(context, attrs, defStyle);  
        init(context);  
    }  
  
    private void init(Context context) 
    {   
    	//设置滑动效果
        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        animation.setInterpolator(new LinearInterpolator());  
        animation.setDuration(100);  
        animation.setFillAfter(true);  
        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        reverseAnimation.setInterpolator(new LinearInterpolator());  
        reverseAnimation.setDuration(100);  
        reverseAnimation.setFillAfter(true);  
        
        inflater = LayoutInflater.from(context);  
        headView = (RelativeLayout) inflater.inflate(R.layout.view_oschina_pull_to_refresh_head, null);  
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);  
        arrowImageView.setMinimumWidth(50);  
        arrowImageView.setMinimumHeight(50);  
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);  
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);  
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);  
        
        headContentOriginalTopPadding = headView.getPaddingTop();  
        measureView(headView);  
        headContentHeight = headView.getMeasuredHeight();  
        headView.getMeasuredWidth(); 
        headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());  
        headView.invalidate();  
        addHeaderView(headView);        
        setOnScrollListener(this); 
    }  
  
    public void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount,  int totalItemCount) 
    {  
        firstItemIndex = firstVisiableItem;  
    }  
  
    public void onScrollStateChanged(AbsListView view, int scrollState) 
    {  
    	currentScrollState = scrollState;
    }  
  
    public boolean onTouchEvent(MotionEvent event) 
    {  
        switch (event.getAction()) 
        {  
        case MotionEvent.ACTION_DOWN:  
        {
        	 if (firstItemIndex == 0 && !isRecored) 
             {  
                 startY = (int) event.getY();  
                 isRecored = true;  
             }  
             break;  
        }
        case MotionEvent.ACTION_CANCEL: //失去焦点&取消动作
        case MotionEvent.ACTION_UP:  
        {
        	if (state != REFRESHING) 
            {  
               if (state == PULL_To_REFRESH) 
               {  
            	   //当前-抬起-ACTION_UP：PULL_To_REFRESH-->DONE-由下拉刷新状态到刷新完成状态
                   state = DONE;  
                   changeHeaderViewByState();    
                }  
                else if (state == RELEASE_To_REFRESH) 
                {  
                	//当前-抬起-ACTION_UP：RELEASE_To_REFRESH-->REFRESHING-由松开刷新状态，到刷新完成状态
                    state = REFRESHING;  
                    changeHeaderViewByState();  
                    onRefresh();              
                }  
            }  
            isRecored = false;  
            isBack = false;  
            break;  
        }
        case MotionEvent.ACTION_MOVE:  
        {
        	int tempY = (int) event.getY(); 
            if (!isRecored && firstItemIndex == 0) 
            {  
                //当前-滑动-记录拖拽时的位置 Y："+tempY
                isRecored = true;  
                startY = tempY;  
            }  
            if (state != REFRESHING && isRecored) 
            {  
                //可以松开刷新了   
                if (state == RELEASE_To_REFRESH) 
                {  
                    //往上推，推到屏幕足够掩盖head的程度，但还没有全部掩盖   
                    if ((tempY - startY < headContentHeight+20) && (tempY - startY) > 0) 
                    {  
                    	//当前-滑动-ACTION_MOVE：RELEASE_To_REFRESH--》PULL_To_REFRESH-由松开刷新状态转变到下拉刷新状态
                        state = PULL_To_REFRESH;  
                        changeHeaderViewByState();   
                    }  
                    //一下子推到顶   
                    else if (tempY - startY <= 0) 
                    {  
                    	//当前-滑动-ACTION_MOVE：RELEASE_To_REFRESH--》DONE-由松开刷新状态转变到done状态
                        state = DONE;  
                        changeHeaderViewByState();     
                    }  
                    //往下拉，或者还没有上推到屏幕顶部掩盖head   
                    else 
                    {  
                        //不用进行特别的操作，只用更新paddingTop的值就行了   
                    }  
                }  
                //还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态   
                else if (state == PULL_To_REFRESH) 
                {  
                    //下拉到可以进入RELEASE_TO_REFRESH的状态   
                    if (tempY - startY >= headContentHeight+20 && currentScrollState == SCROLL_STATE_TOUCH_SCROLL) 
                    {  
                    	//当前-滑动-PULL_To_REFRESH--》RELEASE_To_REFRESH-由done或者下拉刷新状态转变到松开刷新
                        state = RELEASE_To_REFRESH;  
                        isBack = true;  
                        changeHeaderViewByState(); 
                    }  
                    //上推到顶了   
                    else if (tempY - startY <= 0) 
                    {  
                    	//当前-滑动-PULL_To_REFRESH--》DONE-由Done或者下拉刷新状态转变到done状态
                        state = DONE;  
                        changeHeaderViewByState();   
                    }  
                }  
                //done状态下   
                else if (state == DONE) 
                {  
                    if (tempY - startY > 0) 
                    {  
                    	//当前-滑动-DONE--》PULL_To_REFRESH-由done状态转变到下拉刷新状态
                        state = PULL_To_REFRESH;  
                        changeHeaderViewByState(); 
                    }  
                }  
                
                //更新headView的size   
                if (state == PULL_To_REFRESH) 
                { 
                	//当前-下拉刷新PULL_To_REFRESH-TopPad："+topPadding
                	int topPadding = (int)((-1 * headContentHeight + (tempY - startY)));
                	headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(), headView.getPaddingBottom());   
                    headView.invalidate();  
                }  
  
                //更新headView的paddingTop   
                if (state == RELEASE_To_REFRESH) 
                {  
                	//当前-释放刷新RELEASE_To_REFRESH-TopPad："+topPadding
                	int topPadding = (int)((tempY - startY - headContentHeight));
                	headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(), headView.getPaddingBottom());    
                    headView.invalidate();  
                }  
            }  
            break;  
        }
        default:
        	break;
        }  
        return super.onTouchEvent(event);  
    }  
  
    // 当状态改变时候，调用该方法，以更新界面   
    private void changeHeaderViewByState() 
    {  
        switch (state) 
        {  
        case RELEASE_To_REFRESH:  
        {
        	//当前状态，松开刷新
            arrowImageView.setVisibility(View.VISIBLE);  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            arrowImageView.clearAnimation();  
            arrowImageView.startAnimation(animation);  
            tipsTextview.setText("松开可以刷新");  
            break;  
        }
        case PULL_To_REFRESH:
        {
        	//当前状态，下拉刷新
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            arrowImageView.clearAnimation();  
            arrowImageView.setVisibility(View.VISIBLE);  
            if (isBack) 
            {  
                isBack = false;  
                arrowImageView.clearAnimation();  
                arrowImageView.startAnimation(reverseAnimation);  
            } 
            tipsTextview.setText("下拉可以刷新");  
            break;  
        }
        case REFRESHING:
        {
        	//当前状态,正在刷新...
        	headView.setPadding(headView.getPaddingLeft(), headContentOriginalTopPadding, headView.getPaddingRight(), headView.getPaddingBottom());   
            headView.invalidate();  
            progressBar.setVisibility(View.VISIBLE);  
            arrowImageView.clearAnimation();  
            arrowImageView.setVisibility(View.GONE);  
            tipsTextview.setText("加载中...");  
            lastUpdatedTextView.setVisibility(View.GONE); 
            break;  
        }
        case DONE:  
        {
        	//当前状态，done
        	headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());  
            headView.invalidate();  
            progressBar.setVisibility(View.GONE);  
            arrowImageView.clearAnimation();  
            //此处更换图标   
            arrowImageView.setImageResource(R.drawable.oschina_pulltorefresh_arrow);  
            tipsTextview.setText("下拉可以刷新");  
            lastUpdatedTextView.setVisibility(View.VISIBLE); 
            break;  
        }
        default:
        	break;
        }  
    }  
  
    //点击刷新
    public void clickRefresh() 
    {
    	setSelection(0);
    	state = REFRESHING;  
        changeHeaderViewByState();  
        onRefresh(); 
    }
    
    public void setOnRefreshListener(OnRefreshListener refreshListener) 
    {  
        this.refreshListener = refreshListener;  
    }  
  
    public interface OnRefreshListener 
    {  
        public void onRefresh();  
    }  
  
    public void onRefreshComplete(String update) 
    {  
        lastUpdatedTextView.setText(update);  
        onRefreshComplete();
    } 
    
    public void onRefreshComplete() 
    {  
        state = DONE;  
        changeHeaderViewByState();  
    }  
  
    private void onRefresh() 
    {  
        if (refreshListener != null) 
        {  
            refreshListener.onRefresh();  
        }  
    }  
  
    //计算headView的width及height值  
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
}
