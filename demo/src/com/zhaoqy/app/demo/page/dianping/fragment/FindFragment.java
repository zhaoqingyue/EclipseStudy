/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: FindFragment.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.fragment
 * @Description: 大众点评--发现
 * @author: zhaoqy
 * @date: 2015-12-16 下午7:23:47
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhaoqy.app.demo.R;

public class FindFragment extends Fragment 
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.fragment_dianping_find, null);
		return view;
	}
}
