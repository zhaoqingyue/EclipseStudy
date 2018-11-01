package com.zhaoqy.app.demo.camera.gallery.fragment;

import java.lang.reflect.Field;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.gallery.item.PhotoItem;
import com.zhaoqy.app.demo.camera.gallery.util.ImageLoaderHelper;

public class PhotoFragment extends Fragment 
{
	private ImageView mImageView;
	private PhotoItem mPhotoItem;
	
	public static PhotoFragment newInstance(PhotoItem item) 
	{
		PhotoFragment f = new PhotoFragment();
		Bundle args = new Bundle();
		args.putSerializable("PhotoFragment", item);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo, container, false);
		findViews(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		mPhotoItem = (PhotoItem) getArguments().getSerializable("PhotoFragment");
		refresh();
	}
	
	private void findViews(View view) 
	{
		mImageView = (ImageView) view.findViewById(R.id.id_fragment_photo);
	}
	
	private void refresh() 
	{
		ImageLoaderHelper.mImageLoader.displayImage("file://" + mPhotoItem.getImagePath(), mImageView, ImageLoaderHelper.mGalleryItemOption);
	}

	@Override
	public void onDetach() 
	{
		super.onDetach();
		
		try 
		{
			//用来初始化ChildFragmentManager,避免出现null应用异常
		    Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
		    childFragmentManager.setAccessible(true);
		    childFragmentManager.set(this, null);
		} 
		catch (NoSuchFieldException e) 
		{
		    throw new RuntimeException(e);
		} 
		catch (IllegalAccessException e) 
		{
		    throw new RuntimeException(e);
		}
	}
}
