/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaSoftwareDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 软件详情
 * @author: zhaoqy
 * @date: 2015-11-24 下午2:43:10
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.common.ApiClient;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.FavoriteList;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.item.Software;
import com.zhaoqy.app.demo.oschina.util.CacheUtils;
import com.zhaoqy.app.demo.oschina.util.NetUtils;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class OSChinaSoftwareDetailActivity extends OSChinaBaseActivity implements OnClickListener
{
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;
	
	private GestureDetector mGestureDetector;
	private Context      mContext;
	private FrameLayout  mHeader;
	private ImageView    mBack;
	private ImageView    mFavorite;
	private ImageView    mRefresh;
	private ProgressBar  mProgressbar;
	private ScrollView   mScrollView;
	private ImageView    mLogo;
	private TextView     mTitle;
	private TextView     mLicense;
	private TextView     mLanguage;
	private TextView     mOS;
	private TextView     mRecordtime;
	private LinearLayout mLanguageView;
	private LinearLayout mOsView;
	private ImageView    mLangImage;
	private ImageView    mOsImage;
	private Button       mHomepage;
	private Button       mDocment;
	private Button       mDownload;
	private WebView      mWebView;
    private Software     mSoftwareDetail;
    private Bitmap       mLogoBitmap;
    private String       mIdent;
	private boolean      mIsFullScreen;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_software_detail);
        mContext = this;
        
        initView();   
        setListener();
        initData();
        //注册双击全屏事件
    	regOnDoubleEvent();
    }

	private void initView()
    {
    	mHeader = (FrameLayout)findViewById(R.id.software_detail_header);
    	mBack = (ImageView)findViewById(R.id.software_detail_back);
    	mFavorite = (ImageView)findViewById(R.id.software_detail_favorite);
    	mRefresh = (ImageView)findViewById(R.id.software_detail_refresh);
    	mProgressbar = (ProgressBar)findViewById(R.id.software_detail_head_progress);
    	mScrollView = (ScrollView)findViewById(R.id.software_detail_scrollview);
    	
    	mLogo = (ImageView)findViewById(R.id.software_detail_logo);
    	mTitle = (TextView)findViewById(R.id.software_detail_title);
    	mLicense = (TextView)findViewById(R.id.software_detail_license);
    	mLanguage = (TextView)findViewById(R.id.software_detail_language);
    	mOS = (TextView)findViewById(R.id.software_detail_os);
    	mRecordtime = (TextView)findViewById(R.id.software_detail_recordtime);
    	
    	mHomepage = (Button)findViewById(R.id.software_detail_homepage);
    	mDocment = (Button)findViewById(R.id.software_detail_document);
    	mDownload = (Button)findViewById(R.id.software_detail_download);
    	
    	mLanguageView = (LinearLayout)findViewById(R.id.software_detail_language_ll);
    	mOsView = (LinearLayout)findViewById(R.id.software_detail_os_ll);
    	mLangImage = (ImageView)findViewById(R.id.software_detail_language_iv);
    	mOsImage = (ImageView)findViewById(R.id.software_detail_os_iv);
    	mWebView = (WebView)findViewById(R.id.software_detail_webview);
    }
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
    	mFavorite.setOnClickListener(this);
    	mRefresh.setOnClickListener(this);
	}
    
	private void initData()
	{
		mIdent = getIntent().getStringExtra("mIdent");
		mWebView.getSettings().setJavaScriptEnabled(false);
    	mWebView.getSettings().setSupportZoom(true);
    	mWebView.getSettings().setBuiltInZoomControls(true);
    	mWebView.getSettings().setDefaultFontSize(15);
		initData(mIdent, false);
	}
	
    private void initData(final String mIdent, final boolean isRefresh)
    {
    	headButtonSwitch(DATA_LOAD_ING);
		new Thread()
		{
			public void run() 
			{
                Message msg = new Message();
				try 
				{
					mSoftwareDetail = UserHelper.getSoftware(mContext, mIdent, isRefresh);
					if(mSoftwareDetail != null && !StringUtils.isEmpty(mSoftwareDetail.getLogo()))
					{
		    			mLogoBitmap = ApiClient.getNetBitmap(mSoftwareDetail.getLogo());
					}
	                msg.what = (mSoftwareDetail!=null && mSoftwareDetail.getId()>0) ? 1 : 0;
	                msg.obj = (mSoftwareDetail!=null) ? mSoftwareDetail.getNotice() : null;
	            } 
				catch (Exception e) 
				{
	                e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
                mHandler.sendMessage(msg);
			}
		}.start();
    }
    
    /**
     * 头部按钮展示
     * @param type
     */
    private void headButtonSwitch(int type) 
    {
    	switch (type) 
    	{
		case DATA_LOAD_ING:
			mScrollView.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.VISIBLE);
			mFavorite.setVisibility(View.GONE);
			mRefresh.setVisibility(View.GONE);
			break;
		case DATA_LOAD_COMPLETE:
			mScrollView.setVisibility(View.VISIBLE);
			mProgressbar.setVisibility(View.GONE);
			mFavorite.setVisibility(View.VISIBLE);
			mRefresh.setVisibility(View.GONE);
			break;
		case DATA_LOAD_FAIL:
			mScrollView.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.GONE);
			mFavorite.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
    }
	
	/**
	 * 注册双击全屏事件
	 */
	private void regOnDoubleEvent()
	{
		mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{
				mIsFullScreen = !mIsFullScreen;
				if (!mIsFullScreen) 
				{
                    WindowManager.LayoutParams params = getWindow().getAttributes();   
                    params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);   
                    getWindow().setAttributes(params);   
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  
                    mHeader.setVisibility(View.VISIBLE);
                }
				else 
				{    
                    WindowManager.LayoutParams params = getWindow().getAttributes();   
                    params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;   
                    getWindow().setAttributes(params);   
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);   
                    mHeader.setVisibility(View.GONE);
                }
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.software_detail_back:
		{
			finish();
			break;
		}
		case R.id.software_detail_favorite:
		{
			if(mIdent == "" || mSoftwareDetail == null) return;
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
			final int uid = UserHelper.getLoginUid();
			final Handler handler = new Handler()
			{
				public void handleMessage(Message msg) 
				{
					if(msg.what == 1)
					{
						Result res = (Result)msg.obj;
						if(res.OK())
						{
							if(mSoftwareDetail.getFavorite() == 1)
							{
								mSoftwareDetail.setFavorite(0);
								mFavorite.setImageResource(R.drawable.oschina_head_favorite_n);
							}
							else
							{
								mSoftwareDetail.setFavorite(1);
								mFavorite.setImageResource(R.drawable.oschina_head_favorite_y);
							}
							//重新保存缓存
							CacheUtils.saveObject(mContext, mSoftwareDetail, mSoftwareDetail.getCacheKey());
						}
						UIHelper.ToastMessage(mContext, res.getErrorMessage());
					}
				}        			
    		};
    		new Thread()
    		{
				public void run() 
				{
					Message msg = new Message();
					Result res = null;
					try 
					{
						if(mSoftwareDetail.getFavorite() == 1)
						{
							res = UserHelper.delFavorite(mContext, uid, mSoftwareDetail.getId(), FavoriteList.TYPE_SOFTWARE);
						}
						else
						{
							res = UserHelper.addFavorite(mContext, uid, mSoftwareDetail.getId(), FavoriteList.TYPE_SOFTWARE);
						}
						msg.what = 1;
						msg.obj = res;
		            } 
					catch (Exception e) 
					{
		            	e.printStackTrace();
		            	msg.what = -1;
		            	msg.obj = e;
		            }
	                handler.sendMessage(msg);
				}        			
    		}.start();	
			break;
		}
		case R.id.software_detail_refresh:
		{
			initData(mIdent, true);
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) 
	{
		mGestureDetector.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if(msg.what == 1)
			{	
				headButtonSwitch(DATA_LOAD_COMPLETE);
				
				//是否收藏
				if(mSoftwareDetail.getFavorite() == 1)
				{
					mFavorite.setImageResource(R.drawable.oschina_head_favorite_y);
				}
				else
				{
					mFavorite.setImageResource(R.drawable.oschina_head_favorite_n);
				}
					
				
				mLogo.setImageBitmap(mLogoBitmap);
				String title = mSoftwareDetail.getExtensionTitle()+" "+mSoftwareDetail.getTitle();
				mTitle.setText(title);
				String body = UIHelper.WEB_STYLE + mSoftwareDetail.getBody();
				//读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
				boolean isLoadImage;
				if(NetUtils.NETTYPE_WIFI == NetUtils.getNetworkType(mContext))
				{
					isLoadImage = true;
				}
				else
				{
					isLoadImage = UIHelper.isLoadImage(mContext);
				}
				
				if(isLoadImage)
				{
					body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
					body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
				}
				else
				{
					body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>","");
				}

				mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8",null);
				mWebView.setWebViewClient(UIHelper.getWebViewClient());
				mLicense.setText(mSoftwareDetail.getLicense());
				mRecordtime.setText(mSoftwareDetail.getRecordtime());
				String language = mSoftwareDetail.getLanguage();
				String os = mSoftwareDetail.getOs();
				if(StringUtils.isEmpty(language))
				{
					mLanguageView.setVisibility(View.GONE);
					mLangImage.setVisibility(View.GONE);
				}
				else
				{
					mLanguage.setText(language);
				}
				
				if(StringUtils.isEmpty(os))
				{
					mOsView.setVisibility(View.GONE);
					mOsImage.setVisibility(View.GONE);
				}
				else
				{
					mOS.setText(os);
				}

				if(StringUtils.isEmpty(mSoftwareDetail.getHomepage()))
				{
					mHomepage.setVisibility(View.GONE);
				}
				else
				{
					mHomepage.setOnClickListener(homepageClickListener);						
				}
				
				if(StringUtils.isEmpty(mSoftwareDetail.getDocument()))
				{
					mDocment.setVisibility(View.GONE);
				}
				else
				{
					mDocment.setOnClickListener(docmentClickListener);						
				}
				
				if(StringUtils.isEmpty(mSoftwareDetail.getDownload()))
				{
					mDownload.setVisibility(View.GONE);
				}
				else
				{
					mDownload.setOnClickListener(downloadClickListener);						
				}
				
				//发送通知广播
				if(msg.obj != null)
				{
					UIHelper.sendBroadCast(mContext, (Notice)msg.obj);
				}					
			}
			else if(msg.what == 0)
			{
				headButtonSwitch(DATA_LOAD_FAIL);
				
				UIHelper.ToastMessage(mContext, "读取失败，可能已被删除");
			}
			else if(msg.what == -1 && msg.obj != null)
			{
				headButtonSwitch(DATA_LOAD_FAIL);
			}
		}
	};
	
	private View.OnClickListener homepageClickListener = new View.OnClickListener() 
	{
		public void onClick(View v) 
		{	
			UIHelper.openBrowser(v.getContext(), mSoftwareDetail.getHomepage());
		}
	};
	
	private View.OnClickListener docmentClickListener = new View.OnClickListener() 
	{
		public void onClick(View v) 
		{	
			UIHelper.openBrowser(v.getContext(), mSoftwareDetail.getDocument());
		}
	};
	
	private View.OnClickListener downloadClickListener = new View.OnClickListener() 
	{
		public void onClick(View v) 
		{	
			UIHelper.openBrowser(v.getContext(), mSoftwareDetail.getDownload());
		}
	};
}
