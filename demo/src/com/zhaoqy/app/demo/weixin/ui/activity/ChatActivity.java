/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: ChatActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.weixin.ui.activity
 * @Description: 微信聊天页面
 * @author: zhaoqy
 * @date: 2015-12-1 下午3:14:45
 * @version: V1.0
 */

package com.zhaoqy.app.demo.weixin.ui.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.weixin.adapter.ExpressionAdapter;
import com.zhaoqy.app.demo.weixin.adapter.ExpressionPageAdapter;
import com.zhaoqy.app.demo.weixin.ui.view.ExpandGridView;
import com.zhaoqy.app.demo.weixin.ui.view.PasteEditText;

public class ChatActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener
{
	public static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	public static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	
	public static final String COPY_IMAGE = "EASEMOBIMG";
	
	private ImageView micImage;
	private ListView listView;
	private PasteEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private View buttonSend;
	private View buttonPressToSpeak;
	private RelativeLayout emojiIconContainer;
	private LinearLayout btnContainer;
	private View more;
	
	private ViewPager expressionViewpager;
	private LinearLayout    mPoints;
	private int previousSelectPosition = 0;
	
	private InputMethodManager manager;
	private List<String> reslist;
	public static ChatActivity activityInstance = null;
	private String toChatUsername;
	static int resendPos;
	private ImageView iv_emoticons_normal, img_right;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private Button btnMore;
	public String playMsgId;
	private AnimationDrawable animationDrawable;
	
	
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setContentView(R.layout.activity_weixin_chat);
		super.onCreate(savedInstanceState);
		mContext = this;
		
		initView();
		setUpView();
		setListener();
	}

	protected void initView() 
	{
		img_right = (ImageView) findViewById(R.id.id_title_right_img);
		micImage = (ImageView) findViewById(R.id.mic_image);
		animationDrawable = (AnimationDrawable) micImage.getBackground();
		animationDrawable.setOneShot(false);
		listView = (ListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		buttonSend = findViewById(R.id.btn_send);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		mPoints = (LinearLayout) findViewById(R.id.id_chat_viewpager_points);
		
		emojiIconContainer = (RelativeLayout) findViewById(R.id.ll_face_container);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		btnMore = (Button) findViewById(R.id.btn_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = findViewById(R.id.more);
		edittext_layout.setBackgroundResource(R.drawable.weixin_input_bar_normal);

		
		prepareData();
		//表情list
		reslist = getExpressionRes(62);
		//初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		View gv3 = getGridChildView(3);
		views.add(gv1);
		views.add(gv2);
		views.add(gv3);
		expressionViewpager.setAdapter(new ExpressionPageAdapter(views));
		expressionViewpager.setOnPageChangeListener(this);
		expressionViewpager.setCurrentItem(previousSelectPosition);
		mPoints.getChildAt(previousSelectPosition).setEnabled(true);
		
		
		
		edittext_layout.requestFocus();
		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				if (hasFocus) 
				{
					edittext_layout.setBackgroundResource(R.drawable.weixin_input_bar_active);
				} 
				else 
				{
					edittext_layout.setBackgroundResource(R.drawable.weixin_input_bar_normal);
				}
			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				edittext_layout.setBackgroundResource(R.drawable.weixin_input_bar_active);
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		
		//监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				if (!TextUtils.isEmpty(s)) 
				{
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} 
				else 
				{
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	private void prepareData() 
	{
    	View view;
    	for (int i = 0; i < 3; i++) 
    	{
			//添加点view对象
			view = new View(this);
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_viewpager_point_bg));
			LayoutParams lp = new LayoutParams(10, 10);
			lp.leftMargin = 10;
			view.setLayoutParams(lp);
			view.setEnabled(false);
			view.setVisibility(View.VISIBLE);
			mPoints.addView(view);
		}
    }

	private void setUpView() 
	{
		activityInstance = this;
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		int count = listView.getCount();
		if (count > 0) 
		{
			listView.setSelection(count);
		}

		listView.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				hideKeyboard();
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
				return false;
			}
		});
	}

	protected void setListener() 
	{
		findViewById(R.id.id_title_left_img).setVisibility(View.VISIBLE);
		findViewById(R.id.id_title_left_img).setOnClickListener(this);
		findViewById(R.id.view_camera).setOnClickListener(this);
		findViewById(R.id.view_file).setOnClickListener(this);
		findViewById(R.id.view_video).setOnClickListener(this);
		findViewById(R.id.view_photo).setOnClickListener(this);
		findViewById(R.id.view_location).setOnClickListener(this);
		findViewById(R.id.view_audio).setOnClickListener(this);
		img_right.setOnClickListener(this);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 消息图标点击事件
	 * 
	 * @param view
	 */
	@SuppressLint("ShowToast")
	@Override
	public void onClick(View view) 
	{
		hideKeyboard();
		switch (view.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		case R.id.id_title_right_img:
		{
			break;
		}
		case R.id.view_camera:
		{
			//点击照相图标
			selectPicFromCamera();
			break;
		}
		case R.id.view_file:
		{
			//发送文件
			selectFileFromLocal();
			break;		
		}
		case R.id.view_video:
		{
			//视频通话
			Intent intent = new Intent(mContext, VideoCallActivity.class);
			intent.putExtra("username", toChatUsername);
			intent.putExtra("isComingCall", false);
			startActivity(intent);
			break;
		}
		case R.id.view_photo:
		{
			//点击图片图标
			selectPicFromLocal(); 
			break;
		}
		case R.id.view_location:
		{
			//位置
			startActivityForResult(new Intent(mContext, BaiduMapActivity.class), REQUEST_CODE_MAP);
			break;
		}
		case R.id.view_audio:
		{
			//语音通话
			Intent intent = new Intent(mContext, VoiceCallActivity.class);
			intent.putExtra("username", toChatUsername);
			intent.putExtra("isComingCall", false);
			startActivity(intent);
			break;
		}
		case R.id.iv_emoticons_normal:
		{
			//点击显示表情框
			more.setVisibility(View.VISIBLE);
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			emojiIconContainer.setVisibility(View.VISIBLE);
			hideKeyboard();
			break;
		}
		case R.id.iv_emoticons_checked:
		{
			//点击隐藏表情框
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			break;
		}
		case R.id.btn_send:
		{
			//点击发送按钮(发文字和表情)
			String s = mEditTextContent.getText().toString();
			sendText(s);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 照相获取图片
	 */
	@SuppressLint("ShowToast")
	public void selectPicFromCamera() 
	{
	}

	/**
	 * 选择文件
	 */
	private void selectFileFromLocal() 
	{
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) 
		{
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);
		} else 
		{
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() 
	{
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) 
		{
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} 
		else 
		{
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 发送文本消息
	 */
	private void sendText(String content) 
	{
	}

	/**
	 * 显示语音图标按钮
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) 
	{
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		emojiIconContainer.setVisibility(View.GONE);
	}

	/**
	 * 显示键盘图标
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) 
	{
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) 
		{
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} 
		else 
		{
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 点击清空聊天记录
	 * 
	 * @param view
	 */
	public void emptyHistory(View view) 
	{
		String st5 = "是否清空所有聊天记录?";
		Intent intent = new Intent(mContext, AlertDialog.class);
		intent.putExtra("titleIsCancel", true);
		intent.putExtra("msg", st5);
		intent.putExtra("cancel", true);
		startActivityForResult(intent, REQUEST_CODE_EMPTY_HISTORY);
	}

	/**
	 * 显示或隐藏图标按钮页
	 * 
	 * @param view
	 */
	public void more(View view) 
	{
		if (more.getVisibility() == View.GONE) 
		{
			System.out.println("more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
		} 
		else 
		{
			if (emojiIconContainer.getVisibility() == View.VISIBLE) 
			{
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} 
			else 
			{
				more.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) 
	{
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) 
		{
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取表情的gridview的子view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) 
	{
		View view = View.inflate(this, R.layout.view_weixin_expression, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) 
		{
			List<String> list1 = reslist.subList(0, 21);
			list.addAll(list1);
		}
		else if (i == 2) 
		{
			list.addAll(reslist.subList(21, 42));
		} 
		else if (i == 3) 
		{
			list.addAll(reslist.subList(42, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) 
	{
		List<String> reslist = new ArrayList<String>();
		for (int x = 0; x <= getSum; x++) 
		{
			String filename = "f_static_0" + x;
			reslist.add(filename);
		}
		return reslist;
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() 
	{
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) 
		{
			if (getCurrentFocus() != null)
			{
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	public void back(View view) 
	{
		finish();
	}

	/**
	 * 覆盖手机返回键
	 */
	@Override
	public void onBackPressed() 
	{
		if (more.getVisibility() == View.VISIBLE) 
		{
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		} 
		else 
		{
			super.onBackPressed();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) 
	{
		//点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
		{
			super.onNewIntent(intent);
		}
		else 
		{
			finish();
			startActivity(intent);
		}
	}

	/**
	 * 转发消息
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) 
	{
	}

	public String getToChatUsername() 
	{
		return toChatUsername;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) 
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) 
	{
	}

	@Override
	public void onPageSelected(int position) 
	{
		mPoints.getChildAt(previousSelectPosition).setEnabled(false);	   
		mPoints.getChildAt(position % 3).setEnabled(true); 
		expressionViewpager.setCurrentItem(position % 3);
		previousSelectPosition = position  % 3;
	}
}
