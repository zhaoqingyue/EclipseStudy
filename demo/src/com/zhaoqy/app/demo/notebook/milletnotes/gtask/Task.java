package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.database.Cursor;
import android.text.TextUtils;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataConstants;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.util.GTaskUtil;

public class Task extends Node 
{
	private boolean    mCompleted;
    private String     mNotes;
    private JSONObject mMetaInfo;
    private Task       mPriorSibling;
    private TaskList   mParent;

    public Task() 
    {
        super();
        mCompleted = false;
        mNotes = null;
        mPriorSibling = null;
        mParent = null;
        mMetaInfo = null;
    }

    public JSONObject getCreateAction(int actionId) 
    {
        JSONObject js = new JSONObject();
        try 
        {
            js.put(GTaskUtil.GTASK_JSON_ACTION_TYPE, GTaskUtil.GTASK_JSON_ACTION_TYPE_CREATE);
            js.put(GTaskUtil.GTASK_JSON_ACTION_ID, actionId);
            js.put(GTaskUtil.GTASK_JSON_INDEX, mParent.getChildTaskIndex(this));
            JSONObject entity = new JSONObject();
            entity.put(GTaskUtil.GTASK_JSON_NAME, getName());
            entity.put(GTaskUtil.GTASK_JSON_CREATOR_ID, "null");
            entity.put(GTaskUtil.GTASK_JSON_ENTITY_TYPE, GTaskUtil.GTASK_JSON_TYPE_TASK);
            if (getNotes() != null) 
            {
                entity.put(GTaskUtil.GTASK_JSON_NOTES, getNotes());
            }
            js.put(GTaskUtil.GTASK_JSON_ENTITY_DELTA, entity);
            js.put(GTaskUtil.GTASK_JSON_PARENT_ID, mParent.getGid());
            js.put(GTaskUtil.GTASK_JSON_DEST_PARENT_TYPE, GTaskUtil.GTASK_JSON_TYPE_GROUP);
            js.put(GTaskUtil.GTASK_JSON_LIST_ID, mParent.getGid());
            if (mPriorSibling != null) 
            {
                js.put(GTaskUtil.GTASK_JSON_PRIOR_SIBLING_ID, mPriorSibling.getGid());
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return js;
    }

    public JSONObject getUpdateAction(int actionId) 
    {
        JSONObject js = new JSONObject();
        try 
        {
            js.put(GTaskUtil.GTASK_JSON_ACTION_TYPE, GTaskUtil.GTASK_JSON_ACTION_TYPE_UPDATE);
            js.put(GTaskUtil.GTASK_JSON_ACTION_ID, actionId);
            js.put(GTaskUtil.GTASK_JSON_ID, getGid());
            JSONObject entity = new JSONObject();
            entity.put(GTaskUtil.GTASK_JSON_NAME, getName());
            if (getNotes() != null) 
            {
                entity.put(GTaskUtil.GTASK_JSON_NOTES, getNotes());
            }
            entity.put(GTaskUtil.GTASK_JSON_DELETED, getDeleted());
            js.put(GTaskUtil.GTASK_JSON_ENTITY_DELTA, entity);
        } 
        catch (JSONException e) 
        {
            e.printStackTrace();
        }
        return js;
    }

    public void setContentByRemoteJSON(JSONObject js) 
    {
        if (js != null) 
        {
            try 
            {
                if (js.has(GTaskUtil.GTASK_JSON_ID)) 
                {
                    setGid(js.getString(GTaskUtil.GTASK_JSON_ID));
                }

                if (js.has(GTaskUtil.GTASK_JSON_LAST_MODIFIED)) 
                {
                    setLastModified(js.getLong(GTaskUtil.GTASK_JSON_LAST_MODIFIED));
                }

                if (js.has(GTaskUtil.GTASK_JSON_NAME)) 
                {
                    setName(js.getString(GTaskUtil.GTASK_JSON_NAME));
                }

                if (js.has(GTaskUtil.GTASK_JSON_NOTES)) 
                {
                    setNotes(js.getString(GTaskUtil.GTASK_JSON_NOTES));
                }

                if (js.has(GTaskUtil.GTASK_JSON_DELETED)) 
                {
                    setDeleted(js.getBoolean(GTaskUtil.GTASK_JSON_DELETED));
                }

                if (js.has(GTaskUtil.GTASK_JSON_COMPLETED)) 
                {
                    setCompleted(js.getBoolean(GTaskUtil.GTASK_JSON_COMPLETED));
                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }

    public void setContentByLocalJSON(JSONObject js) 
    {
        try 
        {
            JSONObject note = js.getJSONObject(GTaskUtil.META_HEAD_NOTE);
            JSONArray dataArray = js.getJSONArray(GTaskUtil.META_HEAD_DATA);
            if (note.getInt(NoteColumns.TYPE) != Notes.TYPE_NOTE) 
            {
                return;
            }

            for (int i = 0; i < dataArray.length(); i++) 
            {
                JSONObject data = dataArray.getJSONObject(i);
                if (TextUtils.equals(data.getString(DataColumns.MIME_TYPE), DataConstants.NOTE)) 
                {
                    setName(data.getString(DataColumns.CONTENT));
                    break;
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public JSONObject getLocalJSONFromContent() 
    {
        String name = getName();
        try 
        {
            if (mMetaInfo == null) 
            {
                if (name == null) 
                {
                    return null;
                }
                JSONObject js = new JSONObject();
                JSONObject note = new JSONObject();
                JSONArray dataArray = new JSONArray();
                JSONObject data = new JSONObject();
                data.put(DataColumns.CONTENT, name);
                dataArray.put(data);
                js.put(GTaskUtil.META_HEAD_DATA, dataArray);
                note.put(NoteColumns.TYPE, Notes.TYPE_NOTE);
                js.put(GTaskUtil.META_HEAD_NOTE, note);
                return js;
            } 
            else 
            {
                JSONObject note = mMetaInfo.getJSONObject(GTaskUtil.META_HEAD_NOTE);
                JSONArray dataArray = mMetaInfo.getJSONArray(GTaskUtil.META_HEAD_DATA);
                for (int i = 0; i < dataArray.length(); i++) 
                {
                    JSONObject data = dataArray.getJSONObject(i);
                    if (TextUtils.equals(data.getString(DataColumns.MIME_TYPE), DataConstants.NOTE)) 
                    {
                        data.put(DataColumns.CONTENT, getName());
                        break;
                    }
                }
                note.put(NoteColumns.TYPE, Notes.TYPE_NOTE);
                return mMetaInfo;
            }
        } 
        catch (JSONException e) 
        {
            e.printStackTrace();
            return null;
        }
    }

    public void setMetaInfo(MetaData metaData) 
    {
        if (metaData != null && metaData.getNotes() != null) 
        {
            try 
            {
                mMetaInfo = new JSONObject(metaData.getNotes());
            } 
            catch (Exception e) 
            {
                mMetaInfo = null;
            }
        }
    }

    public int getSyncAction(Cursor c) 
    {
        try 
        {
            JSONObject noteInfo = null;
            if (mMetaInfo != null && mMetaInfo.has(GTaskUtil.META_HEAD_NOTE)) 
            {
                noteInfo = mMetaInfo.getJSONObject(GTaskUtil.META_HEAD_NOTE);
            }

            if (noteInfo == null) 
            {
                return SYNC_ACTION_UPDATE_REMOTE;
            }

            if (!noteInfo.has(NoteColumns.ID)) 
            {
                return SYNC_ACTION_UPDATE_LOCAL;
            }

            if (c.getLong(SqlNote.ID_COLUMN) != noteInfo.getLong(NoteColumns.ID)) 
            {
                return SYNC_ACTION_UPDATE_LOCAL;
            }

            if (c.getInt(SqlNote.LOCAL_MODIFIED_COLUMN) == 0) 
            {
                if (c.getLong(SqlNote.SYNC_ID_COLUMN) == getLastModified()) 
                {
                    return SYNC_ACTION_NONE;
                } 
                else 
                {
                    return SYNC_ACTION_UPDATE_LOCAL;
                }
            } 
            else 
            {
                if (!c.getString(SqlNote.GTASK_ID_COLUMN).equals(getGid())) 
                {
                    return SYNC_ACTION_ERROR;
                }
                if (c.getLong(SqlNote.SYNC_ID_COLUMN) == getLastModified()) 
                {
                    return SYNC_ACTION_UPDATE_REMOTE;
                } 
                else 
                {
                    return SYNC_ACTION_UPDATE_CONFLICT;
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return SYNC_ACTION_ERROR;
    }

    public boolean isWorthSaving() 
    {
        return mMetaInfo != null || (getName() != null && getName().trim().length() > 0)
                || (getNotes() != null && getNotes().trim().length() > 0);
    }

    public void setCompleted(boolean completed) 
    {
        mCompleted = completed;
    }

    public void setNotes(String notes) 
    {
        mNotes = notes;
    }

    public void setPriorSibling(Task priorSibling) 
    {
        mPriorSibling = priorSibling;
    }

    public void setParent(TaskList parent) 
    {
        mParent = parent;
    }

    public boolean getCompleted() 
    {
        return mCompleted;
    }

    public String getNotes() 
    {
        return mNotes;
    }

    public Task getPriorSibling() 
    {
        return mPriorSibling;
    }

    public TaskList getParent() 
    {
        return mParent;
    }

}
