package com.zhaoqy.app.demo.page.mushroom;

import com.zhaoqy.app.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeTabFragment extends BaseFragment 
{
	private TextView mMessage;
	private String   mMsgName;
	
	public void setMsgName(String msgName) 
	{
		mMsgName = msgName;
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
		View view = inflater.inflate(R.layout.fragment_home_tab, container, false);
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
		initDisplay();
	}
	
	private void initViews(View view) 
	{
		mMessage = (TextView) view.findViewById(R.id.id_home_tab_title);
	}
	
	private void initDisplay() 
	{
		mMessage.setText(mMsgName + "");
	}
}
