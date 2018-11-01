package com.zhaoqy.app.demo.notebook.milletnotes.adapter;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FoldersAdapter extends CursorAdapter
{
	public static final String [] PROJECTION = 
	{
        NoteColumns.ID,
        NoteColumns.SNIPPET
    };

    public static final int ID_COLUMN   = 0;
    public static final int NAME_COLUMN = 1;

    @SuppressWarnings("deprecation")
	public FoldersAdapter(Context context, Cursor c) 
    {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) 
    {
        return new FolderView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) 
    {
        if (view instanceof FolderView) 
        {
            String folderName = (cursor.getLong(ID_COLUMN) == Notes.ID_ROOT_FOLDER) ? 
            		context.getString(R.string.milletnote_menu_move_parent_folder) : cursor.getString(NAME_COLUMN);
            ((FolderView) view).bind(folderName);
        }
    }

    public String getFolderName(Context context, int position) 
    {
        Cursor cursor = (Cursor) getItem(position);
        return (cursor.getLong(ID_COLUMN) == Notes.ID_ROOT_FOLDER) ? 
        		context.getString(R.string.milletnote_menu_move_parent_folder) : cursor.getString(NAME_COLUMN);
    }

    private class FolderView extends LinearLayout 
    {
        private TextView mName;

        public FolderView(Context context) 
        {
            super(context);
            inflate(context, R.layout.item_milletnotes_folder, this);
            mName = (TextView) findViewById(R.id.tv_folder_name);
        }

        public void bind(String name) 
        {
            mName.setText(name);
        }
    }
}
