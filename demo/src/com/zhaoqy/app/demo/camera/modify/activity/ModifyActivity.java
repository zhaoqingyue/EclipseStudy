package com.zhaoqy.app.demo.camera.modify.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.modify.util.FileUtil;
import com.zhaoqy.app.demo.camera.modify.view.SelectPopup;
import com.zhaoqy.app.demo.camera.modify.view.SelectPopup.OnPopupListener;

public class ModifyActivity extends Activity implements OnClickListener, OnPopupListener
{
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	public static final int CUT_PHOTO = 3;
	private Context      mContext;
	private ImageView    mBack;
	private TextView     mTitle;
	private LinearLayout mRoot;
	private ImageView    mHead;
	private SelectPopup  mPopup;
	private Uri          mPhotoUri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mRoot = (LinearLayout) findViewById(R.id.id_modify_root);
		mHead = (ImageView) findViewById(R.id.id_modify_user_header);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mHead.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.camera_modify);
		mPopup = new SelectPopup(mContext, this);
		Bitmap head = FileUtil.getBitmap(mContext);
		if (head != null)
		{
			mHead.setImageBitmap(head);
		}
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
		case R.id.id_modify_user_header:
		{
			mPopup.showAtLocation(mRoot, Gravity.BOTTOM, 0, 0);
			break;
		}
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (resultCode == RESULT_OK) 
		{
			switch (requestCode) 
			{
			case SELECT_PIC_BY_TACK_PHOTO:
			{
				//选择自拍结果
				beginCrop(mPhotoUri);
				break;
			}
			case SELECT_PIC_BY_PICK_PHOTO:
			{
				//选择图库图片结果
				beginCrop(intent.getData());
				break;
			}
			case CUT_PHOTO:
			{
				handleCrop(intent);
				break;
			}
			default:
				break;
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void beginCrop(Uri uri) 
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		//aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		//outputX outputY 是裁剪图片宽高，注意如果return-data=true情况下,其实得到的是缩略图，并不是真实拍摄的图片大小，
		//而原因是拍照的图片太大，所以这个宽高当你设置很大的时候发现并不起作用，就是因为返回的原图是缩略图，但是作为头像还是够清晰了
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		//返回图片数据
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CUT_PHOTO);
	}

	/**
	 * 保存裁剪之后的图片数据
	 */
	private void handleCrop(Intent result) 
	{
		Bundle extras = result.getExtras();
		if (extras != null) 
		{
			Bitmap photo = extras.getParcelable("data");
			mHead.setImageBitmap(photo);
			FileUtil.saveBitmap(mContext, photo);
		}
	}

	@Override
	public void onChoosePhoto() 
	{
		//从相册中取图片
		Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(choosePictureIntent, SELECT_PIC_BY_PICK_PHOTO);
	}

	@Override
	public void onTakePhoto() 
	{
		takePhoto();
	}

	@Override
	public void onCancel() 
	{
	}
	
	/**
	 * 
	 * 系统相机拍照
	 */
	private void takePhoto() 
	{
		//执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (!SDState.equals(Environment.MEDIA_MOUNTED)) 
		{
			return;
		}
		try 
		{
			mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
			if (mPhotoUri != null) 
			{
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mPhotoUri);
				startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
			} 
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
