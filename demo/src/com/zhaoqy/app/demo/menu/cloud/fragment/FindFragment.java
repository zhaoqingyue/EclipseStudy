package com.zhaoqy.app.demo.menu.cloud.fragment;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.cloud.adapter.MenuItemAdapter;
import com.zhaoqy.app.demo.menu.cloud.item.MenuItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class FindFragment extends Fragment
{
	private GridView gv_menu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_360cloud_find, container, false);
		findView(view);
		init();
		return view;
	}

	private void findView(View v) 
	{
		gv_menu = (GridView) v.findViewById(R.id.gv_menu);
	}

	private void init() 
	{
		List<MenuItem> menus = new ArrayList<MenuItem>();
		menus.add(new MenuItem(R.drawable.cloud_find_leidian, "海量手机资源", "电子书+音乐+壁纸+铃声"));
		menus.add(new MenuItem(R.drawable.cloud_find_downloaded, "已下载", "可离线查看"));
		menus.add(new MenuItem(R.drawable.cloud_find_photo, "图片", ""));
		menus.add(new MenuItem(R.drawable.cloud_find_video, "视频", ""));
		menus.add(new MenuItem(R.drawable.cloud_find_file, "文档", ""));
		menus.add(new MenuItem(R.drawable.cloud_find_music, "音乐", ""));
		
		int margin = (int) (getResources().getDisplayMetrics().density * 14 * 13 / 9);
		MenuItemAdapter adapter = new MenuItemAdapter(getActivity(), menus, margin);
		gv_menu.setAdapter(adapter);
	}
}
