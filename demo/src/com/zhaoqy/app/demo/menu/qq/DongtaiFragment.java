package com.zhaoqy.app.demo.menu.qq;

import com.zhaoqy.app.demo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DongtaiFragment extends Fragment 
{
	private View parentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		parentView = inflater.inflate(R.layout.fragment_menu_qq_dongtai, container, false);
		return parentView;
	}
}
