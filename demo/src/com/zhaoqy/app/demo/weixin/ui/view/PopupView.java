package com.zhaoqy.app.demo.weixin.ui.view;

import java.util.ArrayList;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.weixin.item.ActionItem;
import com.zhaoqy.app.demo.weixin.util.ViewHolder;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PopupView extends PopupWindow 
{
	protected final int LIST_PADDING = 10;              //列表弹窗的间隔
	private final int[] mLocation = new int[2];         //坐标的位置（x、y）
	private Context mContext;
	private Rect mRect = new Rect();                    //实例化一个矩形
	private int mScreenWidth;                           //屏幕的宽度和高度
	private boolean mIsDirty;                           //判断是否需要添加或更新列表子类项
	private int popupGravity = Gravity.NO_GRAVITY;      //位置不在中心
	private OnItemOnClickListener mItemOnClickListener; //弹窗子类项选中时的监听
	private ListView mListView;                         //定义列表对象
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>(); // 定义弹窗子类项列表

	public PopupView(Context context) 
	{
		//设置布局的参数
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@SuppressWarnings("deprecation")
	public PopupView(Context context, int width, int height) 
	{
		mContext = context;
		setFocusable(true);        //设置可以获得焦点
		setTouchable(true);        //设置弹窗内可点击
		setOutsideTouchable(true); //设置弹窗外可点击

		//获得屏幕的宽度和高度
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mScreenWidth = wm.getDefaultDisplay().getWidth();
		wm.getDefaultDisplay().getHeight();

		//设置弹窗的宽度和高度
		setWidth(width);
		setHeight(height);
		setBackgroundDrawable(new BitmapDrawable());
		//设置弹窗的布局界面
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.view_pop_menu, null));
		setAnimationStyle(R.style.pop_top_style);
		initUI();
	}

	private void initUI() 
	{
		mListView = (ListView) getContentView().findViewById(R.id.id_pop_menu_list);
		mListView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) 
			{
				//点击子类项后，弹窗消失
				dismiss();
				if (mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems.get(index), index);
			}
		});
	}

	public void show(View view) 
	{
		//获得点击屏幕的位置坐标
		view.getLocationOnScreen(mLocation);
		//设置矩形的大小
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());

		//判断是否需要添加或更新列表子类项
		if (mIsDirty) 
		{
			populateActions();
		}

		//显示弹窗的位置
		showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth() / 2), mRect.bottom);
	}

	private void populateActions() 
	{
		mIsDirty = false;
		//设置列表的适配器
		mListView.setAdapter(new BaseAdapter() 
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				if (convertView == null) 
				{
					convertView = LayoutInflater.from(mContext).inflate(R.layout.view_pop_top_item, parent, false);
				}
				TextView textView = ViewHolder.get(convertView, R.id.id_pop_item_name);
				textView.setTextColor(mContext.getResources().getColor(android.R.color.white));
				textView.setTextSize(16);
				//设置文本居中
				textView.setGravity(Gravity.CENTER_VERTICAL);
				//设置文本域的范围
				//textView.setPadding(0, 10, 0, 10);
				//设置文本在一行内显示（不换行）
				textView.setSingleLine(true);
				ActionItem item = mActionItems.get(position);

				//设置文本文字
				textView.setText(item.mTitle);
				if (item.mDrawable != null) 
				{
					//设置文字与图标的间隔
					textView.setCompoundDrawablePadding(10);
					//设置在文字的左边放一个图标
					textView.setCompoundDrawablesWithIntrinsicBounds(item.mDrawable, null, null, null);
				}
				return convertView;
			}

			@Override
			public long getItemId(int position) 
			{
				return position;
			}

			@Override
			public Object getItem(int position) 
			{
				return mActionItems.get(position);
			}

			@Override
			public int getCount() 
			{
				return mActionItems.size();
			}
		});
	}

	public void addAction(ActionItem action) 
	{
		if (action != null) 
		{
			mActionItems.add(action);
			mIsDirty = true;
		}
	}

	public void cleanAction() 
	{
		if (mActionItems.isEmpty()) 
		{
			mActionItems.clear();
			mIsDirty = true;
		}
	}

	public ActionItem getAction(int position) 
	{
		if (position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}

	public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener) 
	{
		mItemOnClickListener = onItemOnClickListener;
	}

	public static interface OnItemOnClickListener 
	{
		public void onItemClick(ActionItem item, int position);
	}
}
