package com.zhaoqy.app.demo.menu.cloud.fragment;

import java.util.ArrayList;
import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.cloud.adapter.FileItemAdapter;
import com.zhaoqy.app.demo.menu.cloud.item.FileItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

public class FilesFragment extends Fragment implements OnClickListener
{
	private ListView lv_files;
	private PopupWindow menu;
	private boolean show = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_360cloud_file, container, false);
		findView(view);
		init();
		return view;
	}

	private void findView(View v) {
		lv_files = (ListView) v.findViewById(R.id.lv_files);
	}

	private void init() 
	{
		List<FileItem> files = new ArrayList<FileItem>();
		files.add(new FileItem(R.drawable.cloud_type_folder, "音乐", ""));
		files.add(new FileItem(R.drawable.cloud_type_apk, "Baidu.apk", "18.3K 修改时间:2012-12-08"));
		files.add(new FileItem(R.drawable.cloud_type_avi, "哈雷波特.avi", "28.3M 修改时间:2013-09-18"));
		files.add(new FileItem(R.drawable.cloud_type_epub, "不能说的秘密.epub", "18.3B 修改时间:2012-12-08"));
		files.add(new FileItem(R.drawable.cloud_type_mkv, "钢铁侠.mkv", "18.3K 修改时间:2012-12-08"));
		files.add(new FileItem(R.drawable.cloud_type_pdf, "欢迎.pdf", "183.3K 修改时间:2012-12-08"));
		files.add(new FileItem(R.drawable.cloud_type_zip, "N281.zip", "13.3B 修改时间:2012-04-08"));
		files.add(new FileItem(R.drawable.cloud_type_rar, "N282.rar", "8.3G 修改时间:2012-12-08"));
		files.add(new FileItem(R.drawable.cloud_type_txt, "欢迎使用360云盘.txt", "18.3K 修改时间:2012-12-08"));

		View menuView = LayoutInflater.from(getActivity()).inflate(R.layout.view_360cloud_popwindow, null);
		menu = new PopupWindow(menuView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		menu.setFocusable(false);
		menu.setOutsideTouchable(false);

		FileItemAdapter adapter = new FileItemAdapter(getActivity(), files, this);
		lv_files.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) 
	{
		if (!show)
		{
			show = true;
			showPopupWindows(v);
		}
		else
		{
			show = false;
			menu.dismiss();
		}
	}

	private void showPopupWindows(View v) 
	{
		menu.showAtLocation(v, Gravity.CENTER, 0, 0);
	}
}
