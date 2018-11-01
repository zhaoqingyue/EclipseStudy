package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import org.json.JSONObject;

import android.database.Cursor;

public abstract class Node 
{
	public static final int SYNC_ACTION_NONE            = 0;
    public static final int SYNC_ACTION_ADD_REMOTE      = 1;
    public static final int SYNC_ACTION_ADD_LOCAL       = 2;
    public static final int SYNC_ACTION_DEL_REMOTE      = 3;
    public static final int SYNC_ACTION_DEL_LOCAL       = 4;
    public static final int SYNC_ACTION_UPDATE_REMOTE   = 5;
    public static final int SYNC_ACTION_UPDATE_LOCAL    = 6;
    public static final int SYNC_ACTION_UPDATE_CONFLICT = 7;
    public static final int SYNC_ACTION_ERROR           = 8;

    private String  mGid;
    private String  mName;
    private boolean mDeleted;
    private long    mLastModified;

    public Node() 
    {
        mGid = null;
        mName = "";
        mLastModified = 0;
        mDeleted = false;
    }

    public abstract JSONObject getCreateAction(int actionId);
    public abstract JSONObject getUpdateAction(int actionId);
    public abstract JSONObject getLocalJSONFromContent();
    public abstract void setContentByRemoteJSON(JSONObject js);
    public abstract void setContentByLocalJSON(JSONObject js);
    public abstract int getSyncAction(Cursor c);

    public void setGid(String gid) 
    {
        this.mGid = gid;
    }

    public void setName(String name) 
    {
        this.mName = name;
    }

    public void setLastModified(long lastModified) 
    {
        this.mLastModified = lastModified;
    }

    public void setDeleted(boolean deleted) 
    {
        this.mDeleted = deleted;
    }

    public String getGid() 
    {
        return this.mGid;
    }

    public String getName() 
    {
        return this.mName;
    }

    public long getLastModified() 
    {
        return this.mLastModified;
    }

    public boolean getDeleted() 
    {
        return this.mDeleted;
    }
}
