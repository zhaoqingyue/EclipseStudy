package com.zhaoqy.app.demo.page.mushroom;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;

public class CategoryFragment extends BaseFragment 
{
	private TextView mTitle;

	public static CollectFragment newInstance() 
	{
		CollectFragment collectFragment = new CollectFragment();
		return collectFragment;
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_mushroom_category, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) 
	{
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
	}

	private void initViews(View view) 
	{
		mTitle = (TextView) view.findViewById(R.id.id_category_title);
		mTitle.setText(R.string.page_slide_item6_category);
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
	}
}
