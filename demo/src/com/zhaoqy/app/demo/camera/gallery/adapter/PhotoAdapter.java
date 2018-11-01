package com.zhaoqy.app.demo.camera.gallery.adapter;

import java.util.List;

import com.zhaoqy.app.demo.camera.gallery.fragment.PhotoFragment;
import com.zhaoqy.app.demo.camera.gallery.item.PhotoItem;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class PhotoAdapter extends FragmentPagerAdapter 
{
	private List<PhotoItem> mList;

	public PhotoAdapter(FragmentManager fm) 
	{
		super(fm);
	}
	
	public PhotoAdapter(FragmentManager fm, List<PhotoItem> list)
	{
		super(fm); 
		mList = list;
	}

	@Override
	public Fragment getItem(int arg0) 
	{
		return PhotoFragment.newInstance(mList.get(arg0));
	}

	@Override
	public int getCount() 
	{
		return mList.size();
	}
	
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
    	super.destroyItem(container, position, object);
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) 
    {
    	return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) 
    {
    	return super.isViewFromObject(view, object);
    }
    
    @Override
    public Parcelable saveState() 
    {
    	return super.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) 
    {
    	super.restoreState(state, loader);
    }
}
