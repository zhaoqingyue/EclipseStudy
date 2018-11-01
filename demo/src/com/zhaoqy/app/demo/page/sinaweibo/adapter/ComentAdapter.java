package com.zhaoqy.app.demo.page.sinaweibo.adapter;

import java.util.ArrayList;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.sinaweibo.item.Comments;
import com.zhaoqy.app.demo.page.sinaweibo.util.WeiboAutolinkUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ComentAdapter extends BaseAdapter 
{
	private Context             mContext;
	private LayoutInflater      mInflater;
	private ArrayList<Comments> mComments;
    
    public ComentAdapter(ArrayList<Comments> coms, Context context)
    {
    	mContext = context;
    	mInflater = LayoutInflater.from(mContext);
        mComments = coms;
    }
    
    @Override
    public int getCount() 
    {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) 
    {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) 
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        ViewhoderComm holder=null;
        if(convertView==null)
        {
            holder=new ViewhoderComm();
            convertView=mInflater.inflate(R.layout.item_page_slide_sinaweibo_comment, null);
            holder.ivicon=(ImageView) convertView.findViewById(R.id.id_sinaweibo_comment_usericon);
            holder.tvname=(TextView) convertView.findViewById(R.id.id_sinaweibo_comment_info);
            holder.tvcomments=(TextView) convertView.findViewById(R.id.id_sinaweibo_comment_name);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewhoderComm) convertView.getTag();
        }
        Comments comments=(Comments) getItem(position);
        try 
        {
            holder.tvname.setText(comments.getUser().getScreen_name());
            holder.tvcomments.setText(WeiboAutolinkUtil.Autolink(comments.getText(), mContext));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewhoderComm
    {
        ImageView ivicon;
        TextView  tvname;
        TextView  tvcomments;
    }
}
