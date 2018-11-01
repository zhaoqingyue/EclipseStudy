/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CategoryFragment.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.fragment
 * @Description: 华为商城--分类
 * @author: zhaoqy
 * @date: 2015-12-18 下午3:08:31
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.fragment;


import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.activity.VmallDetailActivity;
import com.zhaoqy.app.demo.page.vmall.task.CategoryListAsync;
import com.zhaoqy.app.demo.page.vmall.util.UrlUtil;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class CategoryFragment extends Fragment 
{
	private CategoryView cv;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_vmall_category, null);
		cv=new CategoryView();
		
		cv.category_lvleft=(ListView) view.findViewById(R.id.category_lvleft);
		cv.category_lvright=(ListView) view.findViewById(R.id.category_lvright);
		new CategoryListAsync(getActivity(), cv).execute(UrlUtil.category);
		cv.category_lvright.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView iv=(ImageView) view.findViewById(R.id.list_category_ivpicUrl);
				Intent intent=new Intent(getActivity(), VmallDetailActivity.class);
				intent.putExtra("url", iv.getContentDescription().toString());
				startActivity(intent);
			}
		});
		return view;
	}
	
	public static class CategoryView{
		public ListView category_lvleft,category_lvright;
	}
}
