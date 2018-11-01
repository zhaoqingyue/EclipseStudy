package com.zhaoqy.app.demo.picture.custom;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.custom.view.CustomShapeImageView;
import com.zhaoqy.app.demo.picture.custom.view.CustomShapeSquareImageView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomImageActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private GridView  mGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_custom);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView()
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mGridView = (GridView) findViewById(R.id.gridview);
	}
	
	private void setListener()
	{
		mBack.setOnClickListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_item8);
		mGridView.setAdapter(new SvgImagesAdapter(this));
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
	
	private class SvgImagesAdapter extends BaseAdapter 
	{
        private List<Integer> mSvgRawResourceIds = new ArrayList<Integer>();
        private Context mContext;

        public SvgImagesAdapter(Context context) 
        {
            mContext = context;
            mSvgRawResourceIds.add(R.raw.shape_star);
            mSvgRawResourceIds.add(R.raw.shape_heart);
            mSvgRawResourceIds.add(R.raw.shape_flower);
            mSvgRawResourceIds.add(R.raw.shape_star_2);
            mSvgRawResourceIds.add(R.raw.shape_star_3);
            mSvgRawResourceIds.add(R.raw.shape_circle_2);
            mSvgRawResourceIds.add(R.raw.shape_5);
        }

        @Override
        public int getCount() 
        {
            return mSvgRawResourceIds.size();
        }

        @Override
        public Integer getItem(int i) 
        {
            return mSvgRawResourceIds.get(i);
        }

        @Override
        public long getItemId(int i) 
        {
            return mSvgRawResourceIds.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) 
        {
            return new CustomShapeSquareImageView(mContext, R.drawable.image_sample_1, 
            		CustomShapeImageView.Shape.SVG, getItem(i));
        }
    }
}
