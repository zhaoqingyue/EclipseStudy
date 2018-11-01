package com.zhaoqy.app.demo.notebook.milletnotes.model;

import java.util.ArrayList;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.CallNote;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.TextNote;

public class Note 
{
	private ContentValues mNoteDiffValues;
    private NoteData      mNoteData;
   
    public static synchronized long getNewNoteId(Context context, long folderId) 
    {
        ContentValues values = new ContentValues();
        long createdTime = System.currentTimeMillis();
        values.put(NoteColumns.CREATED_DATE, createdTime);
        values.put(NoteColumns.MODIFIED_DATE, createdTime);
        values.put(NoteColumns.TYPE, Notes.TYPE_NOTE);
        values.put(NoteColumns.LOCAL_MODIFIED, 1);
        values.put(NoteColumns.PARENT_ID, folderId);
        Uri uri = context.getContentResolver().insert(Notes.CONTENT_NOTE_URI, values);

        long noteId = 0;
        try 
        {
            noteId = Long.valueOf(uri.getPathSegments().get(1));
        } 
        catch (NumberFormatException e) 
        {
            noteId = 0;
        }
        if (noteId == -1) 
        {
            throw new IllegalStateException("Wrong note id:" + noteId);
        }
        return noteId;
    }

    public Note() 
    {
        mNoteDiffValues = new ContentValues();
        mNoteData = new NoteData();
    }

    public void setNoteValue(String key, String value) 
    {
        mNoteDiffValues.put(key, value);
        mNoteDiffValues.put(NoteColumns.LOCAL_MODIFIED, 1);
        mNoteDiffValues.put(NoteColumns.MODIFIED_DATE, System.currentTimeMillis());
    }

    public void setTextData(String key, String value) 
    {
        mNoteData.setTextData(key, value);
    }

    public void setTextDataId(long id) 
    {
        mNoteData.setTextDataId(id);
    }

    public long getTextDataId() 
    {
        return mNoteData.mTextDataId;
    }

    public void setCallDataId(long id) 
    {
        mNoteData.setCallDataId(id);
    }

    public void setCallData(String key, String value) 
    {
        mNoteData.setCallData(key, value);
    }

    public boolean isLocalModified() 
    {
        return mNoteDiffValues.size() > 0 || mNoteData.isLocalModified();
    }

    public boolean syncNote(Context context, long noteId) 
    {
        if (noteId <= 0) 
        {
            throw new IllegalArgumentException("Wrong note id:" + noteId);
        }

        if (!isLocalModified()) 
        {
            return true;
        }

        if (context.getContentResolver().update(ContentUris.withAppendedId(Notes.CONTENT_NOTE_URI, noteId), mNoteDiffValues, null, null) == 0) 
        {
        }
        mNoteDiffValues.clear();
        if (mNoteData.isLocalModified() && (mNoteData.pushIntoContentResolver(context, noteId) == null)) 
        {
            return false;
        }
        return true;
    }

    private class NoteData 
    {
        private ContentValues mTextDataValues;
        private ContentValues mCallDataValues;
        private long mTextDataId;
        private long mCallDataId;

        public NoteData() 
        {
            mTextDataValues = new ContentValues();
            mCallDataValues = new ContentValues();
            mTextDataId = 0;
            mCallDataId = 0;
        }

        boolean isLocalModified() 
        {
            return mTextDataValues.size() > 0 || mCallDataValues.size() > 0;
        }

        void setTextDataId(long id) 
        {
            if(id <= 0) 
            {
                throw new IllegalArgumentException("Text data id should larger than 0");
            }
            mTextDataId = id;
        }

        void setCallDataId(long id) 
        {
            if (id <= 0) 
            {
                throw new IllegalArgumentException("Call data id should larger than 0");
            }
            mCallDataId = id;
        }

        void setCallData(String key, String value) 
        {
            mCallDataValues.put(key, value);
            mNoteDiffValues.put(NoteColumns.LOCAL_MODIFIED, 1);
            mNoteDiffValues.put(NoteColumns.MODIFIED_DATE, System.currentTimeMillis());
        }

        void setTextData(String key, String value) 
        {
            mTextDataValues.put(key, value);
            mNoteDiffValues.put(NoteColumns.LOCAL_MODIFIED, 1);
            mNoteDiffValues.put(NoteColumns.MODIFIED_DATE, System.currentTimeMillis());
        }

        Uri pushIntoContentResolver(Context context, long noteId) 
        {
            if (noteId <= 0) 
            {
                throw new IllegalArgumentException("Wrong note id:" + noteId);
            }

            ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
            ContentProviderOperation.Builder builder = null;

            if(mTextDataValues.size() > 0) 
            {
                mTextDataValues.put(DataColumns.NOTE_ID, noteId);
                if (mTextDataId == 0) 
                {
                    mTextDataValues.put(DataColumns.MIME_TYPE, TextNote.CONTENT_ITEM_TYPE);
                    Uri uri = context.getContentResolver().insert(Notes.CONTENT_DATA_URI, mTextDataValues);
                    try 
                    {
                        setTextDataId(Long.valueOf(uri.getPathSegments().get(1)));
                    } 
                    catch (Exception e) 
                    {
                        mTextDataValues.clear();
                        return null;
                    }
                }
                else 
                {
                    builder = ContentProviderOperation.newUpdate(ContentUris.withAppendedId(Notes.CONTENT_DATA_URI, mTextDataId));
                    builder.withValues(mTextDataValues);
                    operationList.add(builder.build());
                }
                mTextDataValues.clear();
            }

            if(mCallDataValues.size() > 0) 
            {
                mCallDataValues.put(DataColumns.NOTE_ID, noteId);
                if (mCallDataId == 0) 
                {
                    mCallDataValues.put(DataColumns.MIME_TYPE, CallNote.CONTENT_ITEM_TYPE);
                    Uri uri = context.getContentResolver().insert(Notes.CONTENT_DATA_URI, mCallDataValues);
                    try 
                    {
                        setCallDataId(Long.valueOf(uri.getPathSegments().get(1)));
                    } 
                    catch (Exception e) 
                    {
                        mCallDataValues.clear();
                        return null;
                    }
                } 
                else 
                {
                    builder = ContentProviderOperation.newUpdate(ContentUris.withAppendedId(Notes.CONTENT_DATA_URI, mCallDataId));
                    builder.withValues(mCallDataValues);
                    operationList.add(builder.build());
                }
                mCallDataValues.clear();
            }

            if (operationList.size() > 0) 
            {
                try 
                {
                    ContentProviderResult[] results = context.getContentResolver().applyBatch(Notes.AUTHORITY, operationList);
                    return (results == null || results.length == 0 || results[0] == null) ? null : ContentUris.withAppendedId(Notes.CONTENT_NOTE_URI, noteId);
                } 
                catch (Exception e) 
                {
                    return null;
                } 
            }
            return null;
        }
    }
}
