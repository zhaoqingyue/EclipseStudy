package com.zhaoqy.app.demo.notebook.milletnotes.adapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.item.NoteItem;
import com.zhaoqy.app.demo.notebook.milletnotes.view.NoteView;

public class NotesAdapter extends CursorAdapter 
{
    private HashMap<Integer, Boolean> mSelectedIndex;
    private Context mContext;
    private boolean mChoiceMode;
    private int     mNotesCount;
    
    public static class AppWidgetAttribute 
    {
        public int widgetId;
        public int widgetType;
    };

    @SuppressLint("UseSparseArrays")
	@SuppressWarnings("deprecation")
	public NotesAdapter(Context context) 
    {
        super(context, null);
        mSelectedIndex = new HashMap<Integer, Boolean>();
        mContext = context;
        mNotesCount = 0;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) 
    {
        return new NoteView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) 
    {
        if (view instanceof NoteView) 
        {
        	NoteItem itemData = new NoteItem(context, cursor);
            ((NoteView) view).bind(context, itemData, mChoiceMode, isSelectedItem(cursor.getPosition()));
        }
    }

    public void setCheckedItem(final int position, final boolean checked) 
    {
        mSelectedIndex.put(position, checked);
        notifyDataSetChanged();
    }

    public boolean isInChoiceMode() 
    {
        return mChoiceMode;
    }

    public void setChoiceMode(boolean mode) 
    {
        mSelectedIndex.clear();
        mChoiceMode = mode;
    }

    public void selectAll(boolean checked) 
    {
        Cursor cursor = getCursor();
        for (int i = 0; i < getCount(); i++) 
        {
            if (cursor.moveToPosition(i)) 
            {
                if (NoteItem.getNoteType(cursor) == Notes.TYPE_NOTE) 
                {
                    setCheckedItem(i, checked);
                }
            }
        }
    }

    public HashSet<Long> getSelectedItemIds() 
    {
        HashSet<Long> itemSet = new HashSet<Long>();
        for (Integer position : mSelectedIndex.keySet()) 
        {
            if (mSelectedIndex.get(position) == true) 
            {
                Long id = getItemId(position);
                if (id == Notes.ID_ROOT_FOLDER) 
                {
                } 
                else 
                {
                    itemSet.add(id);
                }
            }
        }
        return itemSet;
    }

    public HashSet<AppWidgetAttribute> getSelectedWidget() 
    {
        HashSet<AppWidgetAttribute> itemSet = new HashSet<AppWidgetAttribute>();
        for (Integer position : mSelectedIndex.keySet()) 
        {
            if (mSelectedIndex.get(position) == true) 
            {
                Cursor c = (Cursor) getItem(position);
                if (c != null) 
                {
                    AppWidgetAttribute widget = new AppWidgetAttribute();
                    NoteItem item = new NoteItem(mContext, c);
                    widget.widgetId = item.getWidgetId();
                    widget.widgetType = item.getWidgetType();
                    itemSet.add(widget);
                } 
                else 
                {
                    return null;
                }
            }
        }
        return itemSet;
    }

    public int getSelectedCount() 
    {
        Collection<Boolean> values = mSelectedIndex.values();
        if (null == values) 
        {
            return 0;
        }
        Iterator<Boolean> iter = values.iterator();
        int count = 0;
        while (iter.hasNext()) 
        {
            if (true == iter.next()) 
            {
                count++;
            }
        }
        return count;
    }

    public boolean isAllSelected() 
    {
        int checkedCount = getSelectedCount();
        return (checkedCount != 0 && checkedCount == mNotesCount);
    }

    public boolean isSelectedItem(final int position) 
    {
        if (null == mSelectedIndex.get(position)) 
        {
            return false;
        }
        return mSelectedIndex.get(position);
    }

    @Override
    protected void onContentChanged() 
    {
        super.onContentChanged();
        calcNotesCount();
    }

    @Override
    public void changeCursor(Cursor cursor) 
    {
        super.changeCursor(cursor);
        calcNotesCount();
    }

    private void calcNotesCount() 
    {
        mNotesCount = 0;
        for (int i = 0; i < getCount(); i++) 
        {
            Cursor c = (Cursor) getItem(i);
            if (c != null) 
            {
                if (NoteItem.getNoteType(c) == Notes.TYPE_NOTE) 
                {
                    mNotesCount++;
                }
            } 
            else 
            {
                return;
            }
        }
    }
}
