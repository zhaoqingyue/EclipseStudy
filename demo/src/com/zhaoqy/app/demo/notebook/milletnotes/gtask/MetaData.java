package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import org.json.JSONException;
import org.json.JSONObject;
import com.zhaoqy.app.demo.notebook.milletnotes.util.GTaskUtil;

public class MetaData extends Task 
{
	private String mRelatedGid = null;

    public void setMeta(String gid, JSONObject metaInfo) 
    {
        try 
        {
            metaInfo.put(GTaskUtil.META_HEAD_GTASK_ID, gid);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        setNotes(metaInfo.toString());
        setName(GTaskUtil.META_NOTE_NAME);
    }

    public String getRelatedGid() 
    {
        return mRelatedGid;
    }

    @Override
    public boolean isWorthSaving() 
    {
        return getNotes() != null;
    }

    @Override
    public void setContentByRemoteJSON(JSONObject js) 
    {
        super.setContentByRemoteJSON(js);
        if (getNotes() != null) 
        {
            try 
            {
                JSONObject metaInfo = new JSONObject(getNotes().trim());
                mRelatedGid = metaInfo.getString(GTaskUtil.META_HEAD_GTASK_ID);
            } 
            catch (JSONException e) 
            {
                mRelatedGid = null;
            }
        }
    }
}
