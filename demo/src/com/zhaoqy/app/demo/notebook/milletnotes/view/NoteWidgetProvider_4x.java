package com.zhaoqy.app.demo.notebook.milletnotes.view;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.util.ResourceParser;

import android.appwidget.AppWidgetManager;
import android.content.Context;

public class NoteWidgetProvider_4x extends NoteWidgetProvider
{
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{
        super.update(context, appWidgetManager, appWidgetIds);
    }

    protected int getLayoutId() 
    {
        return R.layout.view_milletnotes_widget_4x;
    }

    @Override
    protected int getBgResourceId(int bgId) 
    {
        return ResourceParser.WidgetBgResources.getWidget4xBgResource(bgId);
    }

    @Override
    protected int getWidgetType() 
    {
        return Notes.TYPE_WIDGET_4X;
    }
}
