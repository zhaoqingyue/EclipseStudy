package com.zhaoqy.app.demo.index.indexable.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.index.indexable.util.StringMatcher;
import com.zhaoqy.app.demo.index.indexable.view.IndexableListView;

public class IndexableActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ArrayList<String> mItems;
	private IndexableListView mListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_indexable);
        mContext = this;

        initView();
        setListener();
        initData();
    }
    
    private void initView()
    {
    	mBack = (ImageView) findViewById(R.id.id_title_left_img);
 		mTitle = (TextView) findViewById(R.id.id_title_text);
 		mListView = (IndexableListView) findViewById(R.id.listview);
    }
    
    private void setListener()
    {
    	mBack.setOnClickListener(this);
    }
    
    private void initData()
    {
    	mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.index_item1);
		
		mItems = new ArrayList<String>();
        mItems.add("Diary of a Wimpy Kid 6: Cabin Fever");
        mItems.add("Steve Jobs");
        mItems.add("Inheritance (The Inheritance Cycle)");
        mItems.add("11/22/63: A Novel");
        mItems.add("The Hunger Games");
        mItems.add("The LEGO Ideas Book");
        mItems.add("Explosive Eighteen: A Stephanie Plum Novel");
        mItems.add("Catching Fire (The Second Book of the Hunger Games)");
        mItems.add("Elder Scrolls V: Skyrim: Prima Official Game Guide");
        mItems.add("Death Comes to Pemberley");
        mItems.add("Diary of a Wimpy Kid 6: Cabin Fever");
        mItems.add("Steve Jobs");
        mItems.add("Inheritance (The Inheritance Cycle)");
        mItems.add("11/22/63: A Novel");
        mItems.add("The Hunger Games");
        mItems.add("The LEGO Ideas Book");
        mItems.add("Explosive Eighteen: A Stephanie Plum Novel");
        mItems.add("Catching Fire (The Second Book of the Hunger Games)");
        mItems.add("Elder Scrolls V: Skyrim: Prima Official Game Guide");
        mItems.add("Death Comes to Pemberley");
        Collections.sort(mItems);

        ContentAdapter adapter = new ContentAdapter(mContext, android.R.layout.simple_list_item_1, mItems);
        mListView.setAdapter(adapter);
        mListView.setFastScrollEnabled(true);
    }
    
    private class ContentAdapter extends ArrayAdapter<String> implements SectionIndexer 
    {
    	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	
		public ContentAdapter(Context context, int textViewResourceId, List<String> objects) 
		{
			super(context, textViewResourceId, objects);
		}

		@Override
		public int getPositionForSection(int section) 
		{
			for (int i = section; i >= 0; i--) 
			{
				for (int j = 0; j < getCount(); j++) 
				{
					if (i == 0) 
					{
						for (int k = 0; k <= 9; k++) 
						{
							if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k)))
							{
								return j;
							}
						}
					} 
					else 
					{
						if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(mSections.charAt(i))))
						{
							return j;
						}
					}
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) 
		{
			return 0;
		}

		@Override
		public Object[] getSections() 
		{
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
			{
				sections[i] = String.valueOf(mSections.charAt(i));
			}
			return sections;
		}
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
