/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: NearbyPhotoLayout.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.view
 * @Description: 附近照片
 * @author: zhaoqy
 * @date: 2015-11-10 下午4:42:55
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.zhaoqy.app.demo.DemoApplication;
import com.zhaoqy.app.demo.R;

public class NearbyPhotoLayout 
{
	private DemoApplication mApplication;
	private View      mLayout;
	private ImageView mPhoto;

	public NearbyPhotoLayout(Context context, DemoApplication application) 
	{
		mApplication = application;
		mLayout = LayoutInflater.from(context).inflate(R.layout.item_menu_kaixin_desktop_lbs_nearby_photo_display, null);
		mPhoto = (ImageView) mLayout.findViewById(R.id.lbs_nearby_photo_display_item_photo);
	}

	public View getLayout() 
	{
		return mLayout;
	}

	public void setPhoto(String image) 
	{
		mPhoto.setImageBitmap(mApplication.getNearbyPhoto(image));
	}
}
