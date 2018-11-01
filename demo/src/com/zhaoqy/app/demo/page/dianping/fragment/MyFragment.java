/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MyFragment.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.fragment
 * @Description: 大众点评--我的
 * @author: zhaoqy
 * @date: 2015-12-16 下午5:22:18
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.fragment;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.dianping.activity.DianpingLoginActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MyFragment extends Fragment implements OnClickListener
{
	private View mPerson;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_dianping_my, null);
		mPerson = view.findViewById(R.id.index_my_person);
		mPerson.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.index_my_person:
		{
			Intent intent = new Intent(getActivity(), DianpingLoginActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
