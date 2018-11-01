package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataConstants;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.db.NotesHelper.TABLE;

public class SqlData 
{
	private static final int INVALID_ID                 = -99999;
	public  static final int DATA_ID_COLUMN             = 0;
    public  static final int DATA_MIME_TYPE_COLUMN      = 1;
    public  static final int DATA_CONTENT_COLUMN        = 2;
    public  static final int DATA_CONTENT_DATA_1_COLUMN = 3;
    public  static final int DATA_CONTENT_DATA_3_COLUMN = 4;
	
    public static final String[] PROJECTION_DATA = new String[] 
    { 
    	DataColumns.ID, 
    	DataColumns.MIME_TYPE, 
    	DataColumns.CONTENT, 
    	DataColumns.DATA1,
        DataColumns.DATA3
    };

    private ContentResolver mContentResolver;
    private ContentValues   mDiffDataValues;
    private String          mDataMimeType;
    private String          mDataContent;
    private String          mDataContentData3;
    private boolean         mIsCreate;
    private long            mDataId;
    private long            mDataContentData1;

    public SqlData(Context context) 
    {
        mContentResolver = context.getContentResolver();
        mIsCreate = true;
        mDataId = INVALID_ID;
        mDataMimeType = DataConstants.NOTE;
        mDataContent = "";
        mDataContentData1 = 0;
        mDataContentData3 = "";
        mDiffDataValues = new ContentValues();
    }

    public SqlData(Context context, Cursor c) 
    {
        mContentResolver = context.getContentResolver();
        mIsCreate = false;
        loadFromCursor(c);
        mDiffDataValues = new ContentValues();
    }

    private void loadFromCursor(Cursor c) 
    {
        mDataId = c.getLong(DATA_ID_COLUMN);
        mDataMimeType = c.getString(DATA_MIME_TYPE_COLUMN);
        mDataContent = c.getString(DATA_CONTENT_COLUMN);
        mDataContentData1 = c.getLong(DATA_CONTENT_DATA_1_COLUMN);
        mDataContentData3 = c.getString(DATA_CONTENT_DATA_3_COLUMN);
    }

    public void setContent(JSONObject js) throws JSONException 
    {
        long dataId = js.has(DataColumns.ID) ? js.getLong(DataColumns.ID) : INVALID_ID;
        if (mIsCreate || mDataId != dataId) 
        {
            mDiffDataValues.put(DataColumns.ID, dataId);
        }
        mDataId = dataId;

        String dataMimeType = js.has(DataColumns.MIME_TYPE) ? js.getString(DataColumns.MIME_TYPE) : DataConstants.NOTE;
        if (mIsCreate || !mDataMimeType.equals(dataMimeType)) 
        {
            mDiffDataValues.put(DataColumns.MIME_TYPE, dataMimeType);
        }
        mDataMimeType = dataMimeType;

        String dataContent = js.has(DataColumns.CONTENT) ? js.getString(DataColumns.CONTENT) : "";
        if (mIsCreate || !mDataContent.equals(dataContent)) 
        {
            mDiffDataValues.put(DataColumns.CONTENT, dataContent);
        }
        mDataContent = dataContent;

        long dataContentData1 = js.has(DataColumns.DATA1) ? js.getLong(DataColumns.DATA1) : 0;
        if (mIsCreate || mDataContentData1 != dataContentData1) 
        {
            mDiffDataValues.put(DataColumns.DATA1, dataContentData1);
        }
        mDataContentData1 = dataContentData1;

        String dataContentData3 = js.has(DataColumns.DATA3) ? js.getString(DataColumns.DATA3) : "";
        if (mIsCreate || !mDataContentData3.equals(dataContentData3)) 
        {
            mDiffDataValues.put(DataColumns.DATA3, dataContentData3);
        }
        mDataContentData3 = dataContentData3;
    }

    public JSONObject getContent() throws JSONException 
    {
        if (mIsCreate) 
        {
            return null;
        }
        JSONObject js = new JSONObject();
        js.put(DataColumns.ID, mDataId);
        js.put(DataColumns.MIME_TYPE, mDataMimeType);
        js.put(DataColumns.CONTENT, mDataContent);
        js.put(DataColumns.DATA1, mDataContentData1);
        js.put(DataColumns.DATA3, mDataContentData3);
        return js;
    }

    public void commit(long noteId, boolean validateVersion, long version) 
    {
        if (mIsCreate) 
        {
            if (mDataId == INVALID_ID && mDiffDataValues.containsKey(DataColumns.ID)) 
            {
                mDiffDataValues.remove(DataColumns.ID);
            }

            mDiffDataValues.put(DataColumns.NOTE_ID, noteId);
            Uri uri = mContentResolver.insert(Notes.CONTENT_DATA_URI, mDiffDataValues);
            try 
            {
                mDataId = Long.valueOf(uri.getPathSegments().get(1));
            } 
            catch (Exception e) 
            {
            	e.printStackTrace();
            }
        } 
        else 
        {
            if (mDiffDataValues.size() > 0) 
            {
                if (!validateVersion) 
                {
                    mContentResolver.update(ContentUris.withAppendedId(Notes.CONTENT_DATA_URI, mDataId), mDiffDataValues, null, null);
                } 
                else 
                {
                    mContentResolver.update(ContentUris.withAppendedId(Notes.CONTENT_DATA_URI, mDataId), mDiffDataValues, 
                    		" ? in (SELECT " + NoteColumns.ID + " FROM " + TABLE.NOTE + " WHERE " + NoteColumns.VERSION + "=?)", 
                    		new String[] { String.valueOf(noteId), String.valueOf(version) });
                }
            }
        }

        mDiffDataValues.clear();
        mIsCreate = false;
    }

    public long getId() 
    {
        return mDataId;
    }
}
