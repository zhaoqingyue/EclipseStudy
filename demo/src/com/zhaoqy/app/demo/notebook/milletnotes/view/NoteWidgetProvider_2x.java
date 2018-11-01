package com.zhaoqy.app.demo.notebook.milletnotes.view;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.util.ResourceParser;
import android.appwidget.AppWidgetManager;
import android.content.Context;

public class NoteWidgetProvider_2x extends NoteWidgetProvider
{
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{
        super.update(context, appWidgetManager, appWidgetIds);
    }

    @Override
    protected int getLayoutId() 
    {
        return R.layout.view_milletnotes_widget_2x;
    }

    @Override
    protected int getBgResourceId(int bgId) 
    {
        return ResourceParser.WidgetBgResources.getWidget2xBgResource(bgId);
    }

    @Override
    protected int getWidgetType() 
    {
        return Notes.TYPE_WIDGET_2X;
    }
}
