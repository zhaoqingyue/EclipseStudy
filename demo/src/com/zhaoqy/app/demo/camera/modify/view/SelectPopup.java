package com.zhaoqy.app.demo.camera.modify.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import com.zhaoqy.app.demo.R;

public class SelectPopup extends PopupWindow implements OnClickListener
{
	private OnPopupListener mListener;
	private View            mPopView;
	
	public SelectPopup(Context context, OnPopupListener listener) 
	{  
        super(context);  
        mListener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopView = inflater.inflate(R.layout.dialog_setphotot, null);
        setContentView(mPopView);
        setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		
        Button btn_choose_photo = (Button)mPopView.findViewById(R.id.btn_choose_photo);
        Button btn_take_photo = (Button)mPopView.findViewById(R.id.btn_take_photo);
        Button btn_cancel = (Button)mPopView.findViewById(R.id.btn_cancel);
       
        btn_choose_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        
        //设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        //点击外面的控件也可以使得PopUpWindow dimiss
        setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(R.style.ModifyPopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);//半透明颜色
    } 
	
	@Override
	public void onClick(View v) 
	{
		switch(v.getId()) 
		{
		case R.id.btn_choose_photo:
		{
			mListener.onChoosePhoto();
			break;
		}	
		case R.id.btn_take_photo:
		{
			mListener.onTakePhoto();
			break;
		}
		case R.id.btn_cancel:
		{
			mListener.onCancel();
			break;
		}
		default:
			break;
		}
		dismiss();
	}
	
	public interface OnPopupListener 
	{
		public void onChoosePhoto(); //选择本地照片
		public void onTakePhoto();   //照相
		public void onCancel();      //取消
	}
}
