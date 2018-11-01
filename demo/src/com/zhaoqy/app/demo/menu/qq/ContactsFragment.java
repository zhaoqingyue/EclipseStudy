package com.zhaoqy.app.demo.menu.qq;

import com.zhaoqy.app.demo.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactsFragment extends Fragment 
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_menu_qq_contact, container, false);
	}
}
