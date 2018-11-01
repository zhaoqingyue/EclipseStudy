package com.zhaoqy.app.demo.page.vmall.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HomePagerAdapter extends PagerAdapter 
{
	private List<ImageView> ivlist;

	public HomePagerAdapter(List<ImageView> viewlist) 
	{
		this.ivlist = viewlist;
	}
	
	public int getCount() 
	{
		return ivlist.size()*100;
	}

	public boolean isViewFromObject(View arg0, Object arg1) 
	{
		return arg0 == arg1;
	}
	
	public Object instantiateItem(ViewGroup container, int position) 
	{
		position %= ivlist.size();
		container.addView(ivlist.get(position), 0);
		if (position<0)
		{  
            position = ivlist.size()+position;  
        }
		return ivlist.get(position);
	}
	
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
		position%=ivlist.size();
		if (position<0)
		{  
            position = ivlist.size()+position;  
        }
		ImageView view = ivlist.get(position);
        container.removeView(view);
	}
}
