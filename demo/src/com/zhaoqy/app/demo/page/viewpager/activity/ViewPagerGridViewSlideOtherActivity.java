package com.zhaoqy.app.demo.page.viewpager.activity;

import java.util.ArrayList;
import java.util.HashMap;
import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class ViewPagerGridViewSlideOtherActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_viewpager_gridview_other);
		
		GridView gridview=(GridView)findViewById(R.id.gridview);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();   
		for(int i=1;i<5;i++)   
		{   
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			if(i==1)
			{
				map.put("ItemImage", R.drawable.page_slide_viewpager_gridview_item1);
				map.put("ItemText", getResources().getString(R.string.page_slide_viewpager_gridview1));
			}
			if(i==2)
			{  
				map.put("ItemImage", R.drawable.page_slide_viewpager_gridview_item2);
				map.put("ItemText", getResources().getString(R.string.page_slide_viewpager_gridview2));
			}
			if(i==3)
			{  
				map.put("ItemImage", R.drawable.page_slide_viewpager_gridview_item3);
				map.put("ItemText", getResources().getString(R.string.page_slide_viewpager_gridview3));
			}
			if(i==4)
			{  
				map.put("ItemImage", R.drawable.page_slide_viewpager_gridview_item4);
				map.put("ItemText", getResources().getString(R.string.page_slide_viewpager_gridview4));   
			}
			lstImageItem.add(map); 

		}   

		SimpleAdapter saImageItems = new SimpleAdapter(this, 
				lstImageItem, 
				R.layout.item_page_slide_viewpager_gridview,      
				new String[] {"ItemImage","ItemText"},    
				new int[] {R.id.ItemImage,R.id.ItemText});   

		gridview.setAdapter(saImageItems);    
	}   
}
