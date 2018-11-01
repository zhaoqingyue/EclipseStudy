package com.zhaoqy.app.demo.index.search.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.index.search.util.CharacterParser;
import com.zhaoqy.app.demo.index.search.util.PinyinComparator;
import com.zhaoqy.app.demo.index.search.util.SortModel;
import com.zhaoqy.app.demo.index.search.view.ClearEditText;
import com.zhaoqy.app.demo.index.search.view.SideBar;
import com.zhaoqy.app.demo.index.search.view.SideBar.OnTouchingLetterChangedListener;
import com.zhaoqy.app.demo.index.search.view.SortAdapter;

public class IndexSearchActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private PinyinComparator pinyinComparator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_search);
		initViews();
		setListener();
		initData();
	}

	private void initViews() 
	{
		
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() 
		{
			@Override
			public void onTouchingLetterChanged(String s) 
			{
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1)
				{
					sortListView.setSelection(position);
				}
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			}
		});
		
		SourceDateList = filledData(getResources().getStringArray(R.array.date));
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		mClearEditText.addTextChangedListener(new TextWatcher() 
		{
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				filterData(s.toString());
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}
			
			public void afterTextChanged(Editable s) 
			{
			}
		});
		
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.index_search_filter);
	}
	
	@SuppressLint("DefaultLocale")
	private List<SortModel> filledData(String [] date)
	{
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	private void filterData(String filterStr)
	{
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr))
		{
			filterDateList = SourceDateList;
		}
		else
		{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList)
			{
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString()))
				{
					filterDateList.add(sortModel);
				}
			}
		}
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
}
