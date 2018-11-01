package com.zhaoqy.app.demo.notebook.billnotes.fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.activity.BillAddBillActivity;
import com.zhaoqy.app.demo.notebook.billnotes.adapter.MyBillAdapter;
import com.zhaoqy.app.demo.notebook.billnotes.db.UserManager;
import com.zhaoqy.app.demo.notebook.billnotes.item.BillItem;

public class MyBillFragment extends Fragment implements OnClickListener
{
	private Integer[] mImg1 = { 
			R.drawable.billnotes_mybill_icon1, 
			R.drawable.billnotes_mybill_icon2, 
			R.drawable.billnotes_mybill_icon3,
			R.drawable.billnotes_mybill_icon4, 
			R.drawable.billnotes_mybill_icon5, 
			R.drawable.billnotes_mybill_icon6, 
			R.drawable.billnotes_mybill_icon7,
			R.drawable.billnotes_mybill_icon8, 
			R.drawable.billnotes_mybill_icon9, 
			R.drawable.billnotes_mybill_icon10, 
			R.drawable.billnotes_mybill_icon11,
			R.drawable.billnotes_mybill_icon12  };
	private Integer[] mImg2 = { 
			R.drawable.billnotes_mybill_icon13, 
			R.drawable.billnotes_mybill_icon14, 
			R.drawable.billnotes_mybill_icon15,
			R.drawable.billnotes_mybill_icon16, 
			R.drawable.billnotes_mybill_icon17, 
			R.drawable.billnotes_mybill_icon18, 
			R.drawable.billnotes_mybill_icon19 };
	private View           mView;
	private Button         mAdd;
	private ListView       mListView;
	private MyBillAdapter  mAdapter;
	private List<BillItem> mList;
	private UserManager    mManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		mView = inflater.inflate(R.layout.fragment_billnote_mybill, container, false);
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
		mAdd = (Button) mView.findViewById(R.id.billnote_mybill_add);
		mListView = (ListView) mView.findViewById(R.id.billnote_mybill_listview);
	}

	private void initData() 
	{
		mManager = new UserManager(getActivity());
		mList = new ArrayList<BillItem>();
		mList = mManager.getBills();
		for (BillItem dbent : mList) 
		{
			int state = Integer.parseInt(dbent.getState().toString().trim());
			if(state == 0)
			{
				dbent.setImg(mImg1[dbent.getImg()]);
			}
			else if(state == 1)
			{
				dbent.setImg(mImg2[dbent.getImg()]);
			}
		}
		mAdapter = new MyBillAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);
	}  
	
	private void setListener() 
	{
		mAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.billnote_mybill_add:
		{
			Intent intent = new Intent(getActivity(), BillAddBillActivity.class);
			getActivity().startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
