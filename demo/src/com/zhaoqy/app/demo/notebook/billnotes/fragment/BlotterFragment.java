/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: BlotterFragment.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.notebook.billnotes.fragment
 * @Description: 记事薄
 * @author: zhaoqy
 * @date: 2015-12-30 上午11:37:45
 * @version: V1.0
 */

package com.zhaoqy.app.demo.notebook.billnotes.fragment;

import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.activity.BillAddNoteActivity;
import com.zhaoqy.app.demo.notebook.billnotes.activity.BillLookNoteActivity;
import com.zhaoqy.app.demo.notebook.billnotes.adapter.NoteAdapter;
import com.zhaoqy.app.demo.notebook.billnotes.db.UserManager;
import com.zhaoqy.app.demo.notebook.billnotes.item.NoteItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BlotterFragment extends Fragment implements OnClickListener, OnItemClickListener
{
	private View           mView;
	private ListView       mListView;
	private Button         mAdd;
	private List<NoteItem> mList;
	private NoteAdapter    mAdapter;
	private UserManager    mManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		mView = inflater.inflate(R.layout.fragment_billnote_blotter, container, false);
		return mView;
	}
	
	@Override
	public void onStart() 
	{
		super.onStart();
		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mListView = (ListView) mView.findViewById(R.id.billnote_blotter_listview);
		mAdd = (Button) mView.findViewById(R.id.billnote_blotter_add);
	}
	
	private void setListener() 
	{
		mAdd.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}
	
	private void initData() 
	{
		mList = getNotes();
		mAdapter = new NoteAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);
	}
	
	private List<NoteItem> getNotes()
	{
		mManager = new UserManager(getActivity());
		mList = mManager.getNotes();
		return mList;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.billnote_blotter_add:
		{
			Intent intent = new Intent(getActivity(), BillAddNoteActivity.class);
			getActivity().startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent(getActivity(),BillLookNoteActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", mList.get(position));
		intent.putExtras(bundle);
		getActivity().startActivity(intent);
	}
}
