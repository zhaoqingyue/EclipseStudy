package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.util.GTaskUtil;
import com.zhaoqy.app.demo.notebook.milletnotes.util.ResourceParser;

public class SqlNote 
{
    public static final String[] PROJECTION_NOTE = new String[] 
    {
        NoteColumns.ID, 
        NoteColumns.ALERTED_DATE, 
        NoteColumns.BG_COLOR_ID,
        NoteColumns.CREATED_DATE, 
        NoteColumns.HAS_ATTACHMENT, 
        NoteColumns.MODIFIED_DATE,
        NoteColumns.NOTES_COUNT, 
        NoteColumns.PARENT_ID, 
        NoteColumns.SNIPPET, 
        NoteColumns.TYPE,
        NoteColumns.WIDGET_ID, 
        NoteColumns.WIDGET_TYPE, 
        NoteColumns.SYNC_ID,
        NoteColumns.LOCAL_MODIFIED, 
        NoteColumns.ORIGIN_PARENT_ID, 
        NoteColumns.GTASK_ID,
        NoteColumns.VERSION
    };
    
    private static final int INVALID_ID              = -99999;
    public  static final int ID_COLUMN               = 0;
    public  static final int ALERTED_DATE_COLUMN     = 1;
    public  static final int BG_COLOR_ID_COLUMN      = 2;
    public  static final int CREATED_DATE_COLUMN     = 3;
    public  static final int HAS_ATTACHMENT_COLUMN   = 4;
    public  static final int MODIFIED_DATE_COLUMN    = 5;
    public  static final int NOTES_COUNT_COLUMN      = 6;
    public  static final int PARENT_ID_COLUMN        = 7;
    public  static final int SNIPPET_COLUMN          = 8;
    public  static final int TYPE_COLUMN             = 9;
    public  static final int WIDGET_ID_COLUMN        = 10;
    public  static final int WIDGET_TYPE_COLUMN      = 11;
    public  static final int SYNC_ID_COLUMN          = 12;
    public  static final int LOCAL_MODIFIED_COLUMN   = 13;
    public  static final int ORIGIN_PARENT_ID_COLUMN = 14;
    public  static final int GTASK_ID_COLUMN         = 15;
    public  static final int VERSION_COLUMN          = 16;

    private Context            mContext;
    private ContentResolver    mContentResolver;
    private ContentValues      mDiffNoteValues;
    private ArrayList<SqlData> mDataList;
    private String             mSnippet;
    private boolean            mIsCreate;
    private long               mId;
    private long               mAlertDate;
    private long               mCreatedDate;
    private long               mModifiedDate;
    private long               mParentId;
    private long               mOriginParent;
    private long               mVersion;
    private int                mHasAttachment;
    private int                mType;
    private int                mWidgetId;
    private int                mWidgetType;
    private int                mBgColorId;

    public SqlNote(Context context) 
    {
        mContext = context;
        mContentResolver = context.getContentResolver();
        mIsCreate = true;
        mId = INVALID_ID;
        mAlertDate = 0;
        mBgColorId = ResourceParser.getDefaultBgId(context);
        mCreatedDate = System.currentTimeMillis();
        mHasAttachment = 0;
        mModifiedDate = System.currentTimeMillis();
        mParentId = 0;
        mSnippet = "";
        mType = Notes.TYPE_NOTE;
        mWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        mWidgetType = Notes.TYPE_WIDGET_INVALIDE;
        mOriginParent = 0;
        mVersion = 0;
        mDiffNoteValues = new ContentValues();
        mDataList = new ArrayList<SqlData>();
    }

    public SqlNote(Context context, Cursor c) 
    {
        mContext = context;
        mContentResolver = context.getContentResolver();
        mIsCreate = false;
        loadFromCursor(c);
        mDataList = new ArrayList<SqlData>();
        if (mType == Notes.TYPE_NOTE)
            loadDataContent();
        mDiffNoteValues = new ContentValues();
    }

    public SqlNote(Context context, long id) 
    {
        mContext = context;
        mContentResolver = context.getContentResolver();
        mIsCreate = false;
        loadFromCursor(id);
        mDataList = new ArrayList<SqlData>();
        if (mType == Notes.TYPE_NOTE)
            loadDataContent();
        mDiffNoteValues = new ContentValues();
    }

    private void loadFromCursor(long id) 
    {
        Cursor c = null;
        try 
        {
            c = mContentResolver.query(Notes.CONTENT_NOTE_URI, PROJECTION_NOTE, "(_id=?)", new String[] { String.valueOf(id) }, null);
            if (c != null) 
            {
                c.moveToNext();
                loadFromCursor(c);
            } 
        } 
        finally 
        {
            if (c != null)
                c.close();
        }
    }

    private void loadFromCursor(Cursor c) 
    {
        mId = c.getLong(ID_COLUMN);
        mAlertDate = c.getLong(ALERTED_DATE_COLUMN);
        mBgColorId = c.getInt(BG_COLOR_ID_COLUMN);
        mCreatedDate = c.getLong(CREATED_DATE_COLUMN);
        mHasAttachment = c.getInt(HAS_ATTACHMENT_COLUMN);
        mModifiedDate = c.getLong(MODIFIED_DATE_COLUMN);
        mParentId = c.getLong(PARENT_ID_COLUMN);
        mSnippet = c.getString(SNIPPET_COLUMN);
        mType = c.getInt(TYPE_COLUMN);
        mWidgetId = c.getInt(WIDGET_ID_COLUMN);
        mWidgetType = c.getInt(WIDGET_TYPE_COLUMN);
        mVersion = c.getLong(VERSION_COLUMN);
    }

    private void loadDataContent() 
    {
        Cursor c = null;
        mDataList.clear();
        try 
        {
            c = mContentResolver.query(Notes.CONTENT_DATA_URI, SqlData.PROJECTION_DATA, "(note_id=?)", 
            		new String[] { String.valueOf(mId) }, null);
            if (c != null) 
            {
                if (c.getCount() == 0) 
                {
                    return;
                }
                while (c.moveToNext()) 
                {
                    SqlData data = new SqlData(mContext, c);
                    mDataList.add(data);
                }
            }
        } 
        finally 
        {
            if (c != null)
                c.close();
        }
    }

    public boolean setContent(JSONObject js) 
    {
        try 
        {
            JSONObject note = js.getJSONObject(GTaskUtil.META_HEAD_NOTE);
            if (note.getInt(NoteColumns.TYPE) == Notes.TYPE_SYSTEM) 
            {
            } 
            else if (note.getInt(NoteColumns.TYPE) == Notes.TYPE_FOLDER) 
            {
                String snippet = note.has(NoteColumns.SNIPPET) ? note.getString(NoteColumns.SNIPPET) : "";
                if (mIsCreate || !mSnippet.equals(snippet)) 
                {
                    mDiffNoteValues.put(NoteColumns.SNIPPET, snippet);
                }
                mSnippet = snippet;

                int type = note.has(NoteColumns.TYPE) ? note.getInt(NoteColumns.TYPE) : Notes.TYPE_NOTE;
                if (mIsCreate || mType != type) 
                {
                    mDiffNoteValues.put(NoteColumns.TYPE, type);
                }
                mType = type;
            } 
            else if (note.getInt(NoteColumns.TYPE) == Notes.TYPE_NOTE) 
            {
                JSONArray dataArray = js.getJSONArray(GTaskUtil.META_HEAD_DATA);
                long id = note.has(NoteColumns.ID) ? note.getLong(NoteColumns.ID) : INVALID_ID;
                if (mIsCreate || mId != id) 
                {
                    mDiffNoteValues.put(NoteColumns.ID, id);
                }
                mId = id;

                long alertDate = note.has(NoteColumns.ALERTED_DATE) ? note.getLong(NoteColumns.ALERTED_DATE) : 0;
                if (mIsCreate || mAlertDate != alertDate) 
                {
                    mDiffNoteValues.put(NoteColumns.ALERTED_DATE, alertDate);
                }
                mAlertDate = alertDate;

                int bgColorId = note.has(NoteColumns.BG_COLOR_ID) ? note.getInt(NoteColumns.BG_COLOR_ID) : 
                	ResourceParser.getDefaultBgId(mContext);
                if (mIsCreate || mBgColorId != bgColorId) 
                {
                    mDiffNoteValues.put(NoteColumns.BG_COLOR_ID, bgColorId);
                }
                mBgColorId = bgColorId;

                long createDate = note.has(NoteColumns.CREATED_DATE) ? note.getLong(NoteColumns.CREATED_DATE) : 
                	System.currentTimeMillis();
                if (mIsCreate || mCreatedDate != createDate) 
                {
                    mDiffNoteValues.put(NoteColumns.CREATED_DATE, createDate);
                }
                mCreatedDate = createDate;

                int hasAttachment = note.has(NoteColumns.HAS_ATTACHMENT) ? note.getInt(NoteColumns.HAS_ATTACHMENT) : 0;
                if (mIsCreate || mHasAttachment != hasAttachment) 
                {
                    mDiffNoteValues.put(NoteColumns.HAS_ATTACHMENT, hasAttachment);
                }
                mHasAttachment = hasAttachment;

                long modifiedDate = note.has(NoteColumns.MODIFIED_DATE) ? note.getLong(NoteColumns.MODIFIED_DATE) : 
                	System.currentTimeMillis();
                if (mIsCreate || mModifiedDate != modifiedDate) 
                {
                    mDiffNoteValues.put(NoteColumns.MODIFIED_DATE, modifiedDate);
                }
                mModifiedDate = modifiedDate;

                long parentId = note.has(NoteColumns.PARENT_ID) ? note.getLong(NoteColumns.PARENT_ID) : 0;
                if (mIsCreate || mParentId != parentId) 
                {
                    mDiffNoteValues.put(NoteColumns.PARENT_ID, parentId);
                }
                mParentId = parentId;

                String snippet = note.has(NoteColumns.SNIPPET) ? note.getString(NoteColumns.SNIPPET) : "";
                if (mIsCreate || !mSnippet.equals(snippet)) 
                {
                    mDiffNoteValues.put(NoteColumns.SNIPPET, snippet);
                }
                mSnippet = snippet;

                int type = note.has(NoteColumns.TYPE) ? note.getInt(NoteColumns.TYPE) : Notes.TYPE_NOTE;
                if (mIsCreate || mType != type) 
                {
                    mDiffNoteValues.put(NoteColumns.TYPE, type);
                }
                mType = type;

                int widgetId = note.has(NoteColumns.WIDGET_ID) ? note.getInt(NoteColumns.WIDGET_ID) : AppWidgetManager.INVALID_APPWIDGET_ID;
                if (mIsCreate || mWidgetId != widgetId) 
                {
                    mDiffNoteValues.put(NoteColumns.WIDGET_ID, widgetId);
                }
                mWidgetId = widgetId;

                int widgetType = note.has(NoteColumns.WIDGET_TYPE) ? note.getInt(NoteColumns.WIDGET_TYPE) : Notes.TYPE_WIDGET_INVALIDE;
                if (mIsCreate || mWidgetType != widgetType) 
                {
                    mDiffNoteValues.put(NoteColumns.WIDGET_TYPE, widgetType);
                }
                mWidgetType = widgetType;

                long originParent = note.has(NoteColumns.ORIGIN_PARENT_ID) ? note.getLong(NoteColumns.ORIGIN_PARENT_ID) : 0;
                if (mIsCreate || mOriginParent != originParent) 
                {
                    mDiffNoteValues.put(NoteColumns.ORIGIN_PARENT_ID, originParent);
                }
                mOriginParent = originParent;

                for (int i = 0; i < dataArray.length(); i++) 
                {
                    JSONObject data = dataArray.getJSONObject(i);
                    SqlData sqlData = null;
                    if (data.has(DataColumns.ID)) 
                    {
                        long dataId = data.getLong(DataColumns.ID);
                        for (SqlData temp : mDataList) 
                        {
                            if (dataId == temp.getId()) 
                            {
                                sqlData = temp;
                            }
                        }
                    }

                    if (sqlData == null) 
                    {
                        sqlData = new SqlData(mContext);
                        mDataList.add(sqlData);
                    }

                    sqlData.setContent(data);
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public JSONObject getContent() 
    {
        try 
        {
            JSONObject js = new JSONObject();
            if (mIsCreate) 
            {
                return null;
            }

            JSONObject note = new JSONObject();
            if (mType == Notes.TYPE_NOTE) 
            {
                note.put(NoteColumns.ID, mId);
                note.put(NoteColumns.ALERTED_DATE, mAlertDate);
                note.put(NoteColumns.BG_COLOR_ID, mBgColorId);
                note.put(NoteColumns.CREATED_DATE, mCreatedDate);
                note.put(NoteColumns.HAS_ATTACHMENT, mHasAttachment);
                note.put(NoteColumns.MODIFIED_DATE, mModifiedDate);
                note.put(NoteColumns.PARENT_ID, mParentId);
                note.put(NoteColumns.SNIPPET, mSnippet);
                note.put(NoteColumns.TYPE, mType);
                note.put(NoteColumns.WIDGET_ID, mWidgetId);
                note.put(NoteColumns.WIDGET_TYPE, mWidgetType);
                note.put(NoteColumns.ORIGIN_PARENT_ID, mOriginParent);
                js.put(GTaskUtil.META_HEAD_NOTE, note);

                JSONArray dataArray = new JSONArray();
                for (SqlData sqlData : mDataList) 
                {
                    JSONObject data = sqlData.getContent();
                    if (data != null) 
                    {
                        dataArray.put(data);
                    }
                }
                js.put(GTaskUtil.META_HEAD_DATA, dataArray);
            } 
            else if (mType == Notes.TYPE_FOLDER || mType == Notes.TYPE_SYSTEM) 
            {
                note.put(NoteColumns.ID, mId);
                note.put(NoteColumns.TYPE, mType);
                note.put(NoteColumns.SNIPPET, mSnippet);
                js.put(GTaskUtil.META_HEAD_NOTE, note);
            }

            return js;
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setParentId(long id) 
    {
        mParentId = id;
        mDiffNoteValues.put(NoteColumns.PARENT_ID, id);
    }

    public void setGtaskId(String gid) 
    {
        mDiffNoteValues.put(NoteColumns.GTASK_ID, gid);
    }

    public void setSyncId(long syncId) 
    {
        mDiffNoteValues.put(NoteColumns.SYNC_ID, syncId);
    }

    public void resetLocalModified() 
    {
        mDiffNoteValues.put(NoteColumns.LOCAL_MODIFIED, 0);
    }

    public long getId() 
    {
        return mId;
    }

    public long getParentId() 
    {
        return mParentId;
    }

    public String getSnippet() 
    {
        return mSnippet;
    }

    public boolean isNoteType() 
    {
        return mType == Notes.TYPE_NOTE;
    }

    public void commit(boolean validateVersion) 
    {
        if (mIsCreate) 
        {
            if (mId == INVALID_ID && mDiffNoteValues.containsKey(NoteColumns.ID)) 
            {
                mDiffNoteValues.remove(NoteColumns.ID);
            }

            Uri uri = mContentResolver.insert(Notes.CONTENT_NOTE_URI, mDiffNoteValues);
            try 
            {
                mId = Long.valueOf(uri.getPathSegments().get(1));
            } 
            catch (Exception e) 
            {
            	e.printStackTrace();
            }

            if (mType == Notes.TYPE_NOTE) 
            {
                for (SqlData sqlData : mDataList) 
                {
                    sqlData.commit(mId, false, -1);
                }
            }
        } 
        else 
        {
            if (mDiffNoteValues.size() > 0) 
            {
                mVersion ++;
                if (!validateVersion) 
                {
                    mContentResolver.update(Notes.CONTENT_NOTE_URI, mDiffNoteValues, "(" + NoteColumns.ID + "=?)", 
                    		new String[] { String.valueOf(mId) });
                } 
                else 
                {
                    mContentResolver.update(Notes.CONTENT_NOTE_URI, mDiffNoteValues, "(" + NoteColumns.ID + "=?) AND (" + 
                    		NoteColumns.VERSION + "<=?)", new String[] { String.valueOf(mId), String.valueOf(mVersion) });
                }
            }

            if (mType == Notes.TYPE_NOTE) 
            {
                for (SqlData sqlData : mDataList) 
                {
                    sqlData.commit(mId, validateVersion, mVersion);
                }
            }
        }

        loadFromCursor(mId);
        if (mType == Notes.TYPE_NOTE)
            loadDataContent();

        mDiffNoteValues.clear();
        mIsCreate = false;
    }
}
