package com.zhaoqy.app.demo.listview.custom.activity;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListView2Activity extends Activity implements OnClickListener
{
	static final int ANIMATION_DURATION = 200;
	private MyAnimListAdapter mMyAnimListAdapter;
	private List<MyCell> mAnimList = new ArrayList<MyCell>();
	private ImageView    mBack;
	private TextView     mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview2);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		for (int i=0;i<50;i++) {
			MyCell cell = new MyCell();
			cell.name = "Cell No."+Integer.toString(i);
			mAnimList.add(cell);
		}

		mMyAnimListAdapter = new MyAnimListAdapter(this, R.layout.view_listview_delete_left, mAnimList);
		ListView myListView = (ListView) findViewById(R.id.chainListView);
		myListView.setAdapter(mMyAnimListAdapter);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.listview_item2));
	}
	
	private void deleteCell(final View v, final int index) 
	{
		AnimationListener al = new AnimationListener() 
		{
			@Override
			public void onAnimationEnd(Animation arg0) 
			{
				mAnimList.remove(index);
				ViewHolder vh = (ViewHolder)v.getTag();
				vh.needInflate = true;
				mMyAnimListAdapter.notifyDataSetChanged();
			}
			@Override public void onAnimationRepeat(Animation animation) {}
			@Override public void onAnimationStart(Animation animation) {}
		};

		collapse(v, al);
	}

	private void collapse(final View v, AnimationListener al) 
	{
		final int initialHeight = v.getMeasuredHeight();
		Animation anim = new Animation() 
		{
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) 
			{
				if (interpolatedTime == 1) 
				{
					v.setVisibility(View.GONE);
				}
				else 
				{
					v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() 
			{
				return true;
			}
		};

		if (al!=null) 
		{
			anim.setAnimationListener(al);
		}
		anim.setDuration(ANIMATION_DURATION);
		v.startAnimation(anim);
	}


	private class MyCell 
	{
		public String name;
	}

	private class ViewHolder 
	{
		public boolean needInflate;
		public TextView text;
		ImageButton imageButton;
	}

	public class MyAnimListAdapter extends ArrayAdapter<MyCell> 
	{
		private LayoutInflater mInflater;

		public MyAnimListAdapter(Context context, int textViewResourceId, List<MyCell> objects) 
		{
			super(context, textViewResourceId, objects);
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			final View view;
			ViewHolder vh;
			MyCell cell = (MyCell)getItem(position);

			if (convertView==null) 
			{
				view = mInflater.inflate(R.layout.view_listview_delete_left, parent, false);
				setViewHolder(view);
			}
			else if (((ViewHolder)convertView.getTag()).needInflate) 
			{
				view = mInflater.inflate(R.layout.view_listview_delete_left, parent, false);
				setViewHolder(view);
			}
			else 
			{
				view = convertView;
			}

			vh = (ViewHolder)view.getTag();
			vh.text.setText(cell.name);
			vh.imageButton.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					deleteCell(view, position);
				}
			});
			return view;
		}

		private void setViewHolder(View view) 
		{
			ViewHolder vh = new ViewHolder();
			vh.text = (TextView)view.findViewById(R.id.cell_name_textview);
			vh.imageButton = (ImageButton) view.findViewById(R.id.cell_trash_button);
			vh.needInflate = false;
			view.setTag(vh);
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
		default:
			break;
		}
	}
}
