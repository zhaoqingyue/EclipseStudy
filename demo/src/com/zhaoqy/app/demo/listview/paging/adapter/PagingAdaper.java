package com.zhaoqy.app.demo.listview.paging.adapter;

import com.zhaoqy.app.demo.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PagingAdaper extends PagingBaseAdapter<String> 
{
    @Override
    public int getCount() 
    {
        return items.size();
    }

    @Override
    public String getItem(int position) 
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) 
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        TextView textView;
        String text = getItem(position);

        if (convertView != null) 
        {
            textView = (TextView) convertView;
        } 
        else 
        {
        	textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_paging, null);
        }
        textView.setText(text);
        return textView;
    }
}
