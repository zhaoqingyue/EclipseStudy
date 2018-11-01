package com.zhaoqy.app.demo.login.email.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class LoginEmailHomeActivity extends Activity implements OnClickListener, OnGroupClickListener, OnChildClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ExpandableListView mExpendView;
	private ExpendAdapter mAdapter = new ExpendAdapter();
	private int []mGroupClick = new int[5];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_email_home);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack =  (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mExpendView = (ExpandableListView) findViewById(R.id.list);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mExpendView.setOnGroupClickListener(this);
		mExpendView.setOnChildClickListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText("QQ邮箱");
		
		//设置默认图标不显示
		mExpendView.setGroupIndicator(null);
		mExpendView.setAdapter(mAdapter);
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
	
	//一级点击事件
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) 
	{
		mGroupClick[groupPosition] += 1;
		mAdapter.notifyDataSetChanged();
		return false;
	}
	
	//二级点击事件
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) 
	{
		if (groupPosition == 0 && childPosition == 1) 
		{
			//联系人列表
		} 
		else if (groupPosition == 0 && childPosition == 0) 
		{
			//添加联系人
		} 
		else if (groupPosition == 1 && childPosition == 0) 
		{
			//新邮箱
		} 
		else if (groupPosition == 1 && childPosition == 1) 
		{
			//草稿箱
		} 
		else if (groupPosition == 2 && childPosition == 0) 
		{
			//全部邮件
		} 
		else if (groupPosition == 2 && childPosition == 1) 
		{
			//未读邮件
		} 
		else if (groupPosition == 2 && childPosition == 2) 
		{
			//已读邮件
		}
		return false;
	}
	
	private class ExpendAdapter extends BaseExpandableListAdapter
	{
		String []group_title=new String[]{"联系人","写邮件","收件箱"};
		
		String [][] child_text=new String [][]{{"联系人列表","添加联系人"}, {"新邮件","草稿箱"}, {"全部邮件","未读邮件","已读邮件"},};
		int [][] child_icons=new int[][]{{R.drawable.login_email_icon_listlianxiren, R.drawable.login_email_icon_tianjia},
										 {R.drawable.login_email_icon_xieyoujian, R.drawable.login_email_icon_caogaoxiang},
										 {R.drawable.login_email_icon_all, R.drawable.login_email_icon_notread, R.drawable.login_email_icon_read}};
        
		/**
		 * 获取一级标签中二级标签的内容
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) 
		{
			return child_text[groupPosition][childPosition];
		}
        
		/**
		 * 获取二级标签ID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) 
		{
			return childPosition;
		}
		/**
		 * 对一级标签下的二级标签进行设置
		 */
		@SuppressLint("SimpleDateFormat")
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
		{
			convertView = getLayoutInflater().inflate(R.layout.item_login_email_child, null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			tv.setText(child_text[groupPosition][childPosition]);
			ImageView iv = (ImageView) convertView.findViewById(R.id.child_icon);
			iv.setImageResource(child_icons[groupPosition][childPosition]);
			return convertView;
		}
        
		/**
		 * 一级标签下二级标签的数量
		 */
		@Override
		public int getChildrenCount(int groupPosition) 
		{
			return child_text[groupPosition].length;
		}
        
		/**
		 * 获取一级标签内容
		 */
		@Override
		public Object getGroup(int groupPosition) 
		{
			return group_title[groupPosition];
		}
        
		/**
		 * 一级标签总数
		 */
		@Override
		public int getGroupCount() 
		{
			return group_title.length;
		}
        
		/**
		 * 一级标签ID
		 */
		@Override
		public long getGroupId(int groupPosition) 
		{
			return groupPosition;
		}
		/**
		 * 对一级标签进行设置
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
		{
			convertView = getLayoutInflater().inflate(R.layout.item_login_email_group, null);
			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
			ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
			TextView tv = (TextView) convertView.findViewById(R.id.iv_title);
			iv.setImageResource(R.drawable.login_email_icon_group_right);
			tv.setText(group_title[groupPosition]);
			
			if(groupPosition == 0)
			{
				icon.setImageResource(R.drawable.login_email_icon_constants);
			}
			else if(groupPosition == 1)
			{
				icon.setImageResource(R.drawable.login_email_icon_mailto);
			}
			else if(groupPosition == 2)
			{
				icon.setImageResource(R.drawable.login_email_icon_mailbox);
			}
			
			if(mGroupClick[groupPosition]%2 == 0)
			{
				iv.setImageResource(R.drawable.login_email_icon_group_right);
			}
			else
			{
				iv.setImageResource(R.drawable.login_email_icon_group_down);
			}
			return convertView;
		}
		/**
		 * 指定位置相应的组视图
		 */
		@Override
		public boolean hasStableIds() 
		{
			return true;
		}
        
		/**
		 *  当选择子节点的时候，调用该方法
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) 
		{
			return true;
		}
	}
}
