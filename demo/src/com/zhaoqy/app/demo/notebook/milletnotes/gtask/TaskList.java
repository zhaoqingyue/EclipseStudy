package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import java.util.ArrayList;
import org.json.JSONObject;
import android.database.Cursor;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.util.GTaskUtil;

public class TaskList extends Node 
{
	private int             mIndex;
    private ArrayList<Task> mChildren;

    public TaskList() 
    {
        super();
        mChildren = new ArrayList<Task>();
        mIndex = 1;
    }

    public JSONObject getCreateAction(int actionId) 
    {
        JSONObject js = new JSONObject();
        try 
        {
            js.put(GTaskUtil.GTASK_JSON_ACTION_TYPE, GTaskUtil.GTASK_JSON_ACTION_TYPE_CREATE);
            js.put(GTaskUtil.GTASK_JSON_ACTION_ID, actionId);
            js.put(GTaskUtil.GTASK_JSON_INDEX, mIndex);
            JSONObject entity = new JSONObject();
            entity.put(GTaskUtil.GTASK_JSON_NAME, getName());
            entity.put(GTaskUtil.GTASK_JSON_CREATOR_ID, "null");
            entity.put(GTaskUtil.GTASK_JSON_ENTITY_TYPE, GTaskUtil.GTASK_JSON_TYPE_GROUP);
            js.put(GTaskUtil.GTASK_JSON_ENTITY_DELTA, entity);

        } 
        catch (Exception e) 
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
            entity.put(GTaskUtil.GTASK_JSON_DELETED, getDeleted());
            js.put(GTaskUtil.GTASK_JSON_ENTITY_DELTA, entity);
        } 
        catch (Exception e) 
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
            JSONObject folder = js.getJSONObject(GTaskUtil.META_HEAD_NOTE);
            if (folder.getInt(NoteColumns.TYPE) == Notes.TYPE_FOLDER) 
            {
                String name = folder.getString(NoteColumns.SNIPPET);
                setName(GTaskUtil.MIUI_FOLDER_PREFFIX + name);
            } 
            else if (folder.getInt(NoteColumns.TYPE) == Notes.TYPE_SYSTEM) 
            {
                if (folder.getLong(NoteColumns.ID) == Notes.ID_ROOT_FOLDER)
                {
                	setName(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_DEFAULT);
                } 
                else if (folder.getLong(NoteColumns.ID) == Notes.ID_CALL_RECORD_FOLDER)
                {
                	setName(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_CALL_NOTE);
                }
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public JSONObject getLocalJSONFromContent() 
    {
        try 
        {
            JSONObject js = new JSONObject();
            JSONObject folder = new JSONObject();
            String folderName = getName();
            if (getName().startsWith(GTaskUtil.MIUI_FOLDER_PREFFIX))
            {
            	folderName = folderName.substring(GTaskUtil.MIUI_FOLDER_PREFFIX.length(), folderName.length());
            }
            folder.put(NoteColumns.SNIPPET, folderName);
            if (folderName.equals(GTaskUtil.FOLDER_DEFAULT) || folderName.equals(GTaskUtil.FOLDER_CALL_NOTE))
            {
            	folder.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM);
            }
            else
            {
            	folder.put(NoteColumns.TYPE, Notes.TYPE_FOLDER);
            }
            js.put(GTaskUtil.META_HEAD_NOTE, folder);
            return js;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getSyncAction(Cursor c) 
    {
        try 
        {
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
                    return SYNC_ACTION_UPDATE_REMOTE;
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return SYNC_ACTION_ERROR;
    }

    public int getChildTaskCount() 
    {
        return mChildren.size();
    }

    public boolean addChildTask(Task task) 
    {
        boolean ret = false;
        if (task != null && !mChildren.contains(task)) 
        {
            ret = mChildren.add(task);
            if (ret) 
            {
                task.setPriorSibling(mChildren.isEmpty() ? null : mChildren.get(mChildren.size() - 1));
                task.setParent(this);
            }
        }
        return ret;
    }

    public boolean addChildTask(Task task, int index) 
    {
        if (index < 0 || index > mChildren.size()) 
        {
            return false;
        }

        int pos = mChildren.indexOf(task);
        if (task != null && pos == -1) 
        {
            mChildren.add(index, task);
            Task preTask = null;
            Task afterTask = null;
            if (index != 0)
            {
            	preTask = mChildren.get(index - 1);
            }
            if (index != mChildren.size() - 1)
            {
            	afterTask = mChildren.get(index + 1);
            }
            task.setPriorSibling(preTask);
            if (afterTask != null)
            {
            	afterTask.setPriorSibling(task);
            }
        }
        return true;
    }

    public boolean removeChildTask(Task task) 
    {
        boolean ret = false;
        int index = mChildren.indexOf(task);
        if (index != -1) 
        {
            ret = mChildren.remove(task);
            if (ret) 
            {
                task.setPriorSibling(null);
                task.setParent(null);

                if (index != mChildren.size()) 
                {
                    mChildren.get(index).setPriorSibling(index == 0 ? null : mChildren.get(index - 1));
                }
            }
        }
        return ret;
    }

    public boolean moveChildTask(Task task, int index) 
    {
        if (index < 0 || index >= mChildren.size()) 
        {
            return false;
        }

        int pos = mChildren.indexOf(task);
        if (pos == -1) 
        {
            return false;
        }

        if (pos == index)
            return true;
        return (removeChildTask(task) && addChildTask(task, index));
    }

    public Task findChildTaskByGid(String gid) 
    {
        for (int i = 0; i < mChildren.size(); i++) 
        {
            Task t = mChildren.get(i);
            if (t.getGid().equals(gid)) 
            {
                return t;
            }
        }
        return null;
    }

    public int getChildTaskIndex(Task task) 
    {
        return mChildren.indexOf(task);
    }

    public Task getChildTaskByIndex(int index) 
    {
        if (index < 0 || index >= mChildren.size()) 
        {
            return null;
        }
        return mChildren.get(index);
    }

    public Task getChilTaskByGid(String gid) 
    {
        for (Task task : mChildren) 
        {
            if (task.getGid().equals(gid))
                return task;
        }
        return null;
    }

    public ArrayList<Task> getChildTaskList() 
    {
        return this.mChildren;
    }

    public void setIndex(int index) 
    {
        this.mIndex = index;
    }

    public int getIndex() 
    {
        return this.mIndex;
    }
}
