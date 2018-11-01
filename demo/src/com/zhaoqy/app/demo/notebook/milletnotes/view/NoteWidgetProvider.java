package com.zhaoqy.app.demo.notebook.milletnotes.view;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.activity.NotesEditActivity;
import com.zhaoqy.app.demo.notebook.milletnotes.activity.NotesMainActivity;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.util.ResourceParser;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

public abstract class NoteWidgetProvider extends AppWidgetProvider 
{
	public static final String [] PROJECTION = new String [] 
	{
        NoteColumns.ID,
        NoteColumns.BG_COLOR_ID,
        NoteColumns.SNIPPET
    };

    public static final int COLUMN_ID           = 0;
    public static final int COLUMN_BG_COLOR_ID  = 1;
    public static final int COLUMN_SNIPPET      = 2;

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) 
    {
        ContentValues values = new ContentValues();
        values.put(NoteColumns.WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        for (int i = 0; i < appWidgetIds.length; i++) 
        {
            context.getContentResolver().update(Notes.CONTENT_NOTE_URI,
                    values,
                    NoteColumns.WIDGET_ID + "=?",
                    new String[] { String.valueOf(appWidgetIds[i])});
        }
    }

    private Cursor getNoteWidgetInfo(Context context, int widgetId) 
    {
        return context.getContentResolver().query(Notes.CONTENT_NOTE_URI,
                PROJECTION,
                NoteColumns.WIDGET_ID + "=? AND " + NoteColumns.PARENT_ID + "<>?",
                new String[] { String.valueOf(widgetId), String.valueOf(Notes.ID_TRASH_FOLER) },
                null);
    }

    protected void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
    {
        update(context, appWidgetManager, appWidgetIds, false);
    }

    private void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, boolean privacyMode) 
    {
        for (int i = 0; i < appWidgetIds.length; i++) 
        {
            if (appWidgetIds[i] != AppWidgetManager.INVALID_APPWIDGET_ID) 
            {
                int bgId = ResourceParser.getDefaultBgId(context);
                String snippet = "";
                Intent intent = new Intent(context, NotesEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(Notes.INTENT_EXTRA_WIDGET_ID, appWidgetIds[i]);
                intent.putExtra(Notes.INTENT_EXTRA_WIDGET_TYPE, getWidgetType());

                Cursor c = getNoteWidgetInfo(context, appWidgetIds[i]);
                if (c != null && c.moveToFirst()) 
                {
                    if (c.getCount() > 1) 
                    {
                        c.close();
                        return;
                    }
                    snippet = c.getString(COLUMN_SNIPPET);
                    bgId = c.getInt(COLUMN_BG_COLOR_ID);
                    intent.putExtra(Intent.EXTRA_UID, c.getLong(COLUMN_ID));
                    intent.setAction(Intent.ACTION_VIEW);
                } 
                else 
                {
                    snippet = context.getResources().getString(R.string.milletnote_widget_havenot_content);
                    intent.setAction(Intent.ACTION_INSERT_OR_EDIT);
                }
                if (c != null) 
                {
                    c.close();
                }

                RemoteViews rv = new RemoteViews(context.getPackageName(), getLayoutId());
                rv.setImageViewResource(R.id.widget_bg_image, getBgResourceId(bgId));
                intent.putExtra(Notes.INTENT_EXTRA_BACKGROUND_ID, bgId);
                PendingIntent pendingIntent = null;
                if (privacyMode) 
                {
                    rv.setTextViewText(R.id.widget_text,  context.getString(R.string.milletnote_widget_under_visit_mode));
                    pendingIntent = PendingIntent.getActivity(context, appWidgetIds[i], new Intent(
                    context, NotesMainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                } 
                else 
                {
                    rv.setTextViewText(R.id.widget_text, snippet);
                    pendingIntent = PendingIntent.getActivity(context, appWidgetIds[i], intent, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                rv.setOnClickPendingIntent(R.id.widget_text, pendingIntent);
                appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
            }
        }
    }

    protected abstract int getBgResourceId(int bgId);
    protected abstract int getLayoutId();
    protected abstract int getWidgetType();
}
