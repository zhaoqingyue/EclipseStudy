package com.zhaoqy.app.demo.notebook.billnotes.fragment;

import java.util.ArrayList;
import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.activity.BillTransferActivity;
import com.zhaoqy.app.demo.notebook.billnotes.db.UserManager;
import com.zhaoqy.app.demo.notebook.billnotes.item.BillItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AccountFragment extends Fragment implements OnClickListener
{
	private View           mView;
	private TextView       mTransfer;
	private TextView       mAssets;
	private TextView       mCash;
	private TextView       mDeposit;
	private TextView       mCredit;
	private TextView       mAirpay;
	private UserManager    mManager;
	private List<BillItem> mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		mView = inflater.inflate(R.layout.fragment_billnote_account, container, false);
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
		mTransfer = (TextView)mView.findViewById(R.id.billnote_account_transfer);
		mAssets = (TextView) mView.findViewById(R.id.billnote_account_assets_value);
		mCash = (TextView) mView.findViewById(R.id.billnote_account_cash_value);
		mDeposit = (TextView) mView.findViewById(R.id.billnote_account_deposit_value);
		mCredit = (TextView) mView.findViewById(R.id.billnote_account_credit_value);
		mAirpay = (TextView) mView.findViewById(R.id.billnote_account_airpay_value);
	}
	
	private void setListener() 
	{
		mTransfer.setOnClickListener(this);
	}
	
	private void initData() 
	{
		getAccount();
		Double xian = Double.valueOf(mCash.getText().toString().trim());
		Double chu = Double.valueOf(mDeposit.getText().toString().trim());
		Double xin = Double.valueOf(mCredit.getText().toString().trim());
		Double web = Double.valueOf(mAirpay.getText().toString().trim());
		String zong = Double.toString(xian + chu + xin + web);
		mAssets.setText(zong);
	}
	
	private void getAccount()
	{
		mManager = new UserManager(getActivity());
		List<TextView> textes = new ArrayList<TextView>();
		textes.add(mCash);
		textes.add(mDeposit);
		textes.add(mCredit);
		textes.add(mAirpay);
		mList = mManager.getAccount();
		if (mList.size() > 0)
		{
			for(int i=0; i<textes.size(); i++)
			{
				textes.get(i).setText(mList.get(i).getBill().toString().trim());
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.billnote_account_transfer:
		{
			Intent intent = new Intent(getActivity(), BillTransferActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
