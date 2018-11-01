package com.zhaoqy.app.demo.animation;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class AnimationAddShopActivity extends Activity implements OnClickListener
{
	private ImageView      mBack;
	private TextView       mTitle;
	private ListView       mListView;//商品listView
	private Context        mContext;
	private ProductAdapter mProductAdapter;
	private ImageView      mShopCart;//购物车
	private ViewGroup      mAnimLayout;//动画层
	private ImageView      mBuyImg;// 这是在界面上跑的小图片
	private BadgeView      mBuyNumView;//显示购买数量的控件
	private int            mBuyNum = 0;//购买数量
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_add_shop);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mShopCart = (ImageView) findViewById(R.id.id_animation_add_shop_icon);
		mListView = (ListView) findViewById(R.id.id_animation_add_shop_product_list);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_add_shop));
		
		mBuyNumView = new BadgeView(mContext, mShopCart);
		mBuyNumView.setTextColor(Color.WHITE);
		mBuyNumView.setBackgroundColor(Color.RED);
		mBuyNumView.setTextSize(12);
		
		mProductAdapter = new ProductAdapter(getData());
		mListView.setAdapter(mProductAdapter);
	}
	
	private List<String> getData() 
	{
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) 
		{
			list.add("这是一个商品" + i);
		}
		return list;
	}
	
	private class ProductAdapter extends BaseAdapter 
	{
		private LayoutInflater inflater;
		private List<String> dataLsit;

		public ProductAdapter(List<String> data) 
		{
			inflater = LayoutInflater.from(mContext);
			dataLsit = data;
		}

		@Override
		public int getCount() 
		{

			return dataLsit.size();
		}

		@Override
		public Object getItem(int position) 
		{
			return dataLsit.get(position);
		}

		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			String name = dataLsit.get(position);
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = inflater.inflate(R.layout.item_animation_add_shop_product, null);
				holder = new ViewHolder();
				holder.nameTxt = (TextView) convertView.findViewById(R.id.name);
				holder.buyBtn = (Button) convertView.findViewById(R.id.product_buy_btn);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.nameTxt.setText(name);
			//购买按钮的点击事件监听
			holder.buyBtn.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
					v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
					mBuyImg = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
					mBuyImg.setImageResource(R.drawable.animation_add_shop_sign);// 设置buyImg的图片
					setAnimation(mBuyImg, start_location);// 开始执行动画
				}
			});
			return convertView;
		}

		class ViewHolder 
		{
			TextView nameTxt;
			Button buyBtn;
		}
	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() 
	{
		ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final ViewGroup vg, final View view, int[] location) 
	{
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	private void setAnimation(final View v, int[] start_location) 
	{
		mAnimLayout = null;
		mAnimLayout = createAnimLayout();
		mAnimLayout.addView(v); //把动画小球添加到动画层
		final View view = addViewToAnimLayout(mAnimLayout, v, start_location);
		int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
		mShopCart.getLocationInWindow(end_location);// shopCart是那个购物车

		//计算位移
		int endX = 0 - start_location[0] + 40;          //动画位移的X坐标
		int endY = end_location[1] - start_location[1]; //动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);  //动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0); //动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(800); //动画的执行时间
		view.startAnimation(set);
		//动画监听事件
		set.setAnimationListener(new AnimationListener() 
		{
			//动画的开始
			@Override
			public void onAnimationStart(Animation animation) 
			{
				v.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) 
			{
			}

			//动画的结束
			@Override
			public void onAnimationEnd(Animation animation) 
			{
				v.setVisibility(View.GONE);
				mBuyNum++;
				mBuyNumView.setText(mBuyNum + "");//
				mBuyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				mBuyNumView.show();
			}
		});
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
