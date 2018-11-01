/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinPhotoPictureDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 普通照片显示
 * @author: zhaoqy
 * @date: 2015-11-10 上午11:22:13
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.PhotoResult;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoAnimations;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;
import com.zhaoqy.app.demo.menu.kaixin.view.PhotoGallery;
import com.zhaoqy.app.demo.menu.kaixin.view.PhotoImageView;

@SuppressWarnings("deprecation")
public class MenuKaixinPhotoPictureDetailActivity extends MenuKaixinBaseActivity implements OnClickListener, OnItemClickListener, OnItemSelectedListener
{
	private Context        mContext;
	private LinearLayout   mTop;
	private TextView       mBack;
	private TextView       mTitle;
	private TextView       mToPeople;
	private TextView       mDescription;
	private RelativeLayout mBottom;
	private ImageButton    mSaveas;
	private Button         mComment;
	private ImageButton    mLike;
	private PhotoGallery   mGallery;
	private String         mUid;             //照片所属用户的ID
	private String         mName;            //照片所属用户的姓名
	private PhotoResult    mPhotoResult;     //照片数据
	private int            mAvatar;          //照片所属用户的头像
	private int            mTotalCount;      //照片总数量
	private int            mCurrentPosition; //照片当前的编号
	private boolean        mIsLike;          //是否赞过
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_photo_picture_detail);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mTop = (LinearLayout) findViewById(R.id.photopicturedetail_top);
		mBack = (TextView) findViewById(R.id.photopicturedetail_back);
		mTitle = (TextView) findViewById(R.id.photopicturedetail_title);
		mToPeople = (TextView) findViewById(R.id.photopicturedetail_topeople);
		mDescription = (TextView) findViewById(R.id.photopicturedetail_description);
		mBottom = (RelativeLayout) findViewById(R.id.photopicturedetail_bottom);
		mSaveas = (ImageButton) findViewById(R.id.photopicturedetail_saveas);
		mComment = (Button) findViewById(R.id.photopicturedetail_comment);
		mLike = (ImageButton) findViewById(R.id.photopicturedetail_like);
		mGallery = (PhotoGallery) findViewById(R.id.photopicturedetail_gallery);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mToPeople.setOnClickListener(this);
		mSaveas.setOnClickListener(this);
		mComment.setOnClickListener(this);
		mLike.setOnClickListener(this);
		mGallery.setOnItemClickListener(this);
		mGallery.setOnItemSelectedListener(this);
	}

	private void initData() 
	{
		//获取照片所属用户的ID、姓名、头像、照片数据、当前照片的编号
		mUid = getIntent().getStringExtra("uid");
		mName = getIntent().getStringExtra("name");
		mAvatar = getIntent().getIntExtra("avatar", -1);
		mPhotoResult = getIntent().getParcelableExtra("result");
		mCurrentPosition = getIntent().getIntExtra("position", 0);
		//获得照片总数量
		mTotalCount = mPhotoResult.getImages().size();
		//修改显示的内容
		changeContent();
		//添加适配器
		mGallery.setAdapter(new PhotoGalleryAdapter());
		mGallery.setSelection(mCurrentPosition);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.photopicturedetail_back:
		{
			finish();
			break;
		}
		case R.id.photopicturedetail_topeople:
		{
			//跳转到好友资料界面,并传递数据
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinFriendInfoActivity.class);
			intent.putExtra("uid", mUid);
			intent.putExtra("name", mName);
			intent.putExtra("avatar", mAvatar);
			startActivity(intent);
			break;
		}
		case R.id.photopicturedetail_saveas:
		{
			//获取当前照片的编号
			int image = mPhotoResult.getImages().get(mCurrentPosition).getImage();
			//获取当前照片
			Bitmap bitmap = mApplication.getPhoto(image);
			//保存当前照片
			boolean result = PhotoUtil.saveToSDCard(bitmap);
			if (result) 
			{
				Toast.makeText(mContext, "已保存到/sdcard/demo/download/下", Toast.LENGTH_SHORT).show();
			} 
			else 
			{
				Toast.makeText(mContext, "保存失败,请检查SD卡是否存在", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case R.id.photopicturedetail_comment:
		{
			//跳转到评论内容显示界面,并传递照片数据
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinPhotoCommentDetailActivity.class);
			intent.putExtra("result", mPhotoResult.getImages().get(mCurrentPosition));
			startActivity(intent);
			break;
		}
		case R.id.photopicturedetail_like:
		{
			//判断是否赞过,如果赞过提示信息,否则修改界面显示内容
			if (!mIsLike) 
			{
				mIsLike = true;
				mLike.setImageResource(R.drawable.menu_kaixin_photo_like_disabled);
				Toast.makeText(mContext, "我和" + mPhotoResult.getImages().get(mCurrentPosition).getLike_count() + "个人觉得挺赞的", Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessageDelayed(0, 500);
			} 
			else 
			{
				Toast.makeText(mContext, "您已经赞过了", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//显示或隐藏标题栏和底部栏
		if (mTop.isShown() && mBottom.isShown()) 
		{
			PhotoAnimations.startGoneAnimation(mTop, mBottom);
		}
		else 
		{
			PhotoAnimations.startVisibleAnimation(mTop, mBottom);
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
		//滑动时修改显示内容
		mCurrentPosition = position;
		changeContent();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) 
	{
	}

	/**
	 * 修改显示内容
	 */
	private void changeContent() 
	{
		mTitle.setText(mCurrentPosition + 1 + "/" + mTotalCount);
		mDescription.setText(mPhotoResult.getImages().get(mCurrentPosition).getDescription());
		mComment.setText(mPhotoResult.getImages().get(mCurrentPosition).getComment_count() + "");
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			mIsLike = false;
			mLike.setImageResource(R.drawable.menu_kaixin_photo_like_selector);
			mGallery.setSelection(mCurrentPosition + 1);
		}
	};

	private class PhotoGalleryAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mPhotoResult.getImages().size();
		}

		public Object getItem(int position) 
		{
			return mPhotoResult.getImages().get(position);
		}

		public long getItemId(int position) 
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			PhotoImageView view = null;
			if (convertView == null) 
			{
				view = new PhotoImageView(mContext);
				view.setLayoutParams(new Gallery.LayoutParams(mScreenWidth,	mScreenHeight));
			} 
			else 
			{
				view = (PhotoImageView) convertView;
			}
			//获取图片的编号
			int photo = mPhotoResult.getImages().get(position).getImage();
			Bitmap bitmap = null;
			//如果等于-1则获取默认的照片否则获取相对应的照片
			if (photo == -1) 
			{
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_kaixin_head_default);
			} 
			else 
			{
				bitmap = mApplication.getPhoto(photo);
			}
			//获取缩放比例
			float scale = getScale(bitmap);
			//获取缩放之后的图片宽度和高度
			int bitmapWidth = (int) (bitmap.getWidth() * scale);
			int bitmapHeight = (int) (bitmap.getHeight() * scale);
			//获取缩放之后的图片
			Bitmap zoomBitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, true);
			//显示图片
			view.setImageWidth(bitmapWidth);
			view.setImageHeight(bitmapHeight);
			view.setImageBitmap(zoomBitmap);
			return view;
		}
	}

	/**
	 * 获取缩放比例
	 * @param bitmap 需要缩放的图片
	 */
	private float getScale(Bitmap bitmap) 
	{
		float scaleWidth = mScreenWidth / (float) bitmap.getWidth();
		float scaleHeight = mScreenHeight / (float) bitmap.getHeight();
		return Math.min(scaleWidth, scaleHeight);
	}

	public void onBackPressed() 
	{
		//关闭当前界面
		finish();
	}
}
