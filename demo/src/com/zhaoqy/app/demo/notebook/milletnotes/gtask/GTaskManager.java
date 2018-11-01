package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.DataColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.util.DataUtil;
import com.zhaoqy.app.demo.notebook.milletnotes.util.GTaskUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

@SuppressLint("UseValueOf")
public class GTaskManager 
{
    public static final int STATE_SUCCESS          = 0;
    public static final int STATE_NETWORK_ERROR    = 1;
    public static final int STATE_INTERNAL_ERROR   = 2;
    public static final int STATE_SYNC_IN_PROGRESS = 3;
    public static final int STATE_SYNC_CANCELLED   = 4;

    private static GTaskManager       mInstance = null;
    private Activity                  mActivity;
    private Context                   mContext;
    private ContentResolver           mContentResolver;
    private HashMap<String, TaskList> mGTaskListHashMap;
    private HashMap<String, Node>     mGTaskHashMap;
    private HashMap<String, MetaData> mMetaHashMap;
    private TaskList                  mMetaList;
    private HashSet<Long>             mLocalDeleteIdMap;
    private HashMap<String, Long>     mGidToNid;
    private HashMap<Long, String>     mNidToGid;
    private boolean                   mSyncing;
    private boolean                   mCancelled;

    private GTaskManager() 
    {
        mSyncing = false;
        mCancelled = false;
        mGTaskListHashMap = new HashMap<String, TaskList>();
        mGTaskHashMap = new HashMap<String, Node>();
        mMetaHashMap = new HashMap<String, MetaData>();
        mMetaList = null;
        mLocalDeleteIdMap = new HashSet<Long>();
        mGidToNid = new HashMap<String, Long>();
        mNidToGid = new HashMap<Long, String>();
    }

    public static synchronized GTaskManager getInstance() 
    {
        if (mInstance == null) 
        {
            mInstance = new GTaskManager();
        }
        return mInstance;
    }

    public synchronized void setActivityContext(Activity activity) 
    {
        mActivity = activity;
    }

    public int sync(Context context, GTaskASyncTask asyncTask) 
    {
        if (mSyncing) 
        {
            return STATE_SYNC_IN_PROGRESS;
        }
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        mSyncing = true;
        mCancelled = false;
        mGTaskListHashMap.clear();
        mGTaskHashMap.clear();
        mMetaHashMap.clear();
        mLocalDeleteIdMap.clear();
        mGidToNid.clear();
        mNidToGid.clear();

        try 
        {
            GTaskClient client = GTaskClient.getInstance();
            client.resetUpdateArray();
            if (!mCancelled) 
            {
                if (!client.login(mActivity)) 
                {
                }
            }
            asyncTask.publishProgess(mContext.getString(R.string.milletnote_sync_init_list));
            initGTaskList();
            asyncTask.publishProgess(mContext.getString(R.string.milletnote_sync_syncing));
            syncContent();
        } 
        catch (Exception e) 
        {
            return STATE_NETWORK_ERROR;
        } 
        finally 
        {
            mGTaskListHashMap.clear();
            mGTaskHashMap.clear();
            mMetaHashMap.clear();
            mLocalDeleteIdMap.clear();
            mGidToNid.clear();
            mNidToGid.clear();
            mSyncing = false;
        }
        return mCancelled ? STATE_SYNC_CANCELLED : STATE_SUCCESS;
    }

    private void initGTaskList() throws Exception 
    {
        if (mCancelled)
            return;
        GTaskClient client = GTaskClient.getInstance();
        try 
        {
            JSONArray jsTaskLists = client.getTaskLists();
            mMetaList = null;
            for (int i = 0; i < jsTaskLists.length(); i++) 
            {
                JSONObject object = jsTaskLists.getJSONObject(i);
                String gid = object.getString(GTaskUtil.GTASK_JSON_ID);
                String name = object.getString(GTaskUtil.GTASK_JSON_NAME);

                if (name.equals(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_META)) 
                {
                    mMetaList = new TaskList();
                    mMetaList.setContentByRemoteJSON(object);

                    JSONArray jsMetas = client.getTaskList(gid);
                    for (int j = 0; j < jsMetas.length(); j++) 
                    {
                        object = (JSONObject) jsMetas.getJSONObject(j);
                        MetaData metaData = new MetaData();
                        metaData.setContentByRemoteJSON(object);
                        if (metaData.isWorthSaving()) 
                        {
                            mMetaList.addChildTask(metaData);
                            if (metaData.getGid() != null) 
                            {
                                mMetaHashMap.put(metaData.getRelatedGid(), metaData);
                            }
                        }
                    }
                }
            }

            if (mMetaList == null) 
            {
                mMetaList = new TaskList();
                mMetaList.setName(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_META);
                GTaskClient.getInstance().createTaskList(mMetaList);
            }

            for (int i = 0; i < jsTaskLists.length(); i++) 
            {
                JSONObject object = jsTaskLists.getJSONObject(i);
                String gid = object.getString(GTaskUtil.GTASK_JSON_ID);
                String name = object.getString(GTaskUtil.GTASK_JSON_NAME);

                if (name.startsWith(GTaskUtil.MIUI_FOLDER_PREFFIX) && !name.equals(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_META))
                {
                    TaskList tasklist = new TaskList();
                    tasklist.setContentByRemoteJSON(object);
                    mGTaskListHashMap.put(gid, tasklist);
                    mGTaskHashMap.put(gid, tasklist);

                    JSONArray jsTasks = client.getTaskList(gid);
                    for (int j = 0; j < jsTasks.length(); j++) 
                    {
                        object = (JSONObject) jsTasks.getJSONObject(j);
                        gid = object.getString(GTaskUtil.GTASK_JSON_ID);
                        Task task = new Task();
                        task.setContentByRemoteJSON(object);
                        if (task.isWorthSaving()) 
                        {
                            task.setMetaInfo(mMetaHashMap.get(gid));
                            tasklist.addChildTask(task);
                            mGTaskHashMap.put(gid, task);
                        }
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    private void syncContent() throws Exception 
    {
        int syncType;
        Cursor c = null;
        String gid;
        Node node;
        mLocalDeleteIdMap.clear();

        if (mCancelled) 
        {
            return;
        }

        try 
        {
            c = mContentResolver.query(Notes.CONTENT_NOTE_URI, SqlNote.PROJECTION_NOTE, "(type<>? AND parent_id=?)", 
            	new String[] { String.valueOf(Notes.TYPE_SYSTEM), String.valueOf(Notes.ID_TRASH_FOLER) }, null);
            if (c != null) 
            {
                while (c.moveToNext()) 
                {
                    gid = c.getString(SqlNote.GTASK_ID_COLUMN);
                    node = mGTaskHashMap.get(gid);
                    if (node != null) 
                    {
                        mGTaskHashMap.remove(gid);
                        doContentSync(Node.SYNC_ACTION_DEL_REMOTE, node, c);
                    }
                    mLocalDeleteIdMap.add(c.getLong(SqlNote.ID_COLUMN));
                }
            } 
        } 
        finally 
        {
            if (c != null) 
            {
                c.close();
                c = null;
            }
        }
        syncFolder();
        try 
        {
            c = mContentResolver.query(Notes.CONTENT_NOTE_URI, SqlNote.PROJECTION_NOTE, "(type=? AND parent_id<>?)", 
            	new String[] { String.valueOf(Notes.TYPE_NOTE), String.valueOf(Notes.ID_TRASH_FOLER) }, NoteColumns.TYPE + " DESC");
            if (c != null) 
            {
                while (c.moveToNext()) 
                {
                    gid = c.getString(SqlNote.GTASK_ID_COLUMN);
                    node = mGTaskHashMap.get(gid);
                    if (node != null) 
                    {
                        mGTaskHashMap.remove(gid);
                        mGidToNid.put(gid, c.getLong(SqlNote.ID_COLUMN));
                        mNidToGid.put(c.getLong(SqlNote.ID_COLUMN), gid);
                        syncType = node.getSyncAction(c);
                    } 
                    else 
                    {
                        if (c.getString(SqlNote.GTASK_ID_COLUMN).trim().length() == 0) 
                        {
                            syncType = Node.SYNC_ACTION_ADD_REMOTE;
                        } 
                        else 
                        {
                            syncType = Node.SYNC_ACTION_DEL_LOCAL;
                        }
                    }
                    doContentSync(syncType, node, c);
                }
            } 
        } finally 
        {
            if (c != null) 
            {
                c.close();
                c = null;
            }
        }

        Iterator<Map.Entry<String, Node>> iter = mGTaskHashMap.entrySet().iterator();
        while (iter.hasNext()) 
        {
            Map.Entry<String, Node> entry = iter.next();
            node = entry.getValue();
            doContentSync(Node.SYNC_ACTION_ADD_LOCAL, node, null);
        }

        if (!mCancelled) 
        {
            if (!DataUtil.batchDeleteNotes(mContentResolver, mLocalDeleteIdMap)) 
            {
            }
        }

        if (!mCancelled) 
        {
            GTaskClient.getInstance().commitUpdate();
            refreshLocalSyncId();
        }
    }

    private void syncFolder() throws Exception 
    {
        Cursor c = null;
        String gid;
        Node node;
        int syncType;

        if (mCancelled) 
        {
            return;
        }

        try 
        {
            c = mContentResolver.query(ContentUris.withAppendedId(Notes.CONTENT_NOTE_URI, Notes.ID_ROOT_FOLDER), 
            		SqlNote.PROJECTION_NOTE, null, null, null);
            if (c != null) 
            {
                c.moveToNext();
                gid = c.getString(SqlNote.GTASK_ID_COLUMN);
                node = mGTaskHashMap.get(gid);
                if (node != null) 
                {
                    mGTaskHashMap.remove(gid);
                    mGidToNid.put(gid, (long) Notes.ID_ROOT_FOLDER);
                    mNidToGid.put((long) Notes.ID_ROOT_FOLDER, gid);
                    if (!node.getName().equals(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_DEFAULT))
                        doContentSync(Node.SYNC_ACTION_UPDATE_REMOTE, node, c);
                } 
                else 
                {
                    doContentSync(Node.SYNC_ACTION_ADD_REMOTE, node, c);
                }
            } 
        }
        finally 
        {
            if (c != null) 
            {
                c.close();
                c = null;
            }
        }

        try 
        {
            c = mContentResolver.query(Notes.CONTENT_NOTE_URI, SqlNote.PROJECTION_NOTE, "(_id=?)",
                    new String[] { String.valueOf(Notes.ID_CALL_RECORD_FOLDER) }, null);
            if (c != null) 
            {
                if (c.moveToNext()) 
                {
                    gid = c.getString(SqlNote.GTASK_ID_COLUMN);
                    node = mGTaskHashMap.get(gid);
                    if (node != null) 
                    {
                        mGTaskHashMap.remove(gid);
                        mGidToNid.put(gid, (long) Notes.ID_CALL_RECORD_FOLDER);
                        mNidToGid.put((long) Notes.ID_CALL_RECORD_FOLDER, gid);
                        if (!node.getName().equals(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_CALL_NOTE))
                            doContentSync(Node.SYNC_ACTION_UPDATE_REMOTE, node, c);
                    } 
                    else 
                    {
                        doContentSync(Node.SYNC_ACTION_ADD_REMOTE, node, c);
                    }
                }
            }
        } 
        finally 
        {
            if (c != null) 
            {
                c.close();
                c = null;
            }
        }

        try {
            c = mContentResolver.query(Notes.CONTENT_NOTE_URI, SqlNote.PROJECTION_NOTE, "(type=? AND parent_id<>?)", 
            	new String[] {String.valueOf(Notes.TYPE_FOLDER), String.valueOf(Notes.ID_TRASH_FOLER) }, NoteColumns.TYPE + " DESC");
            if (c != null) 
            {
                while (c.moveToNext()) 
                {
                    gid = c.getString(SqlNote.GTASK_ID_COLUMN);
                    node = mGTaskHashMap.get(gid);
                    if (node != null) 
                    {
                        mGTaskHashMap.remove(gid);
                        mGidToNid.put(gid, c.getLong(SqlNote.ID_COLUMN));
                        mNidToGid.put(c.getLong(SqlNote.ID_COLUMN), gid);
                        syncType = node.getSyncAction(c);
                    } 
                    else 
                    {
                        if (c.getString(SqlNote.GTASK_ID_COLUMN).trim().length() == 0) 
                        {
                            syncType = Node.SYNC_ACTION_ADD_REMOTE;
                        } 
                        else 
                        {
                            syncType = Node.SYNC_ACTION_DEL_LOCAL;
                        }
                    }
                    doContentSync(syncType, node, c);
                }
            } 
        } 
        finally 
        {
            if (c != null) 
            {
                c.close();
                c = null;
            }
        }

        Iterator<Map.Entry<String, TaskList>> iter = mGTaskListHashMap.entrySet().iterator();
        while (iter.hasNext()) 
        {
            Map.Entry<String, TaskList> entry = iter.next();
            gid = entry.getKey();
            node = entry.getValue();
            if (mGTaskHashMap.containsKey(gid)) 
            {
                mGTaskHashMap.remove(gid);
                doContentSync(Node.SYNC_ACTION_ADD_LOCAL, node, null);
            }
        }

        if (!mCancelled)
            GTaskClient.getInstance().commitUpdate();
    }

    private void doContentSync(int syncType, Node node, Cursor c) throws Exception 
    {
        if (mCancelled) 
        {
            return;
        }

        MetaData meta;
		switch (syncType) 
		{
		case Node.SYNC_ACTION_ADD_LOCAL:
			addLocalNode(node);
			break;
		case Node.SYNC_ACTION_ADD_REMOTE:
			addRemoteNode(node, c);
			break;
		case Node.SYNC_ACTION_DEL_LOCAL:
			meta = mMetaHashMap.get(c.getString(SqlNote.GTASK_ID_COLUMN));
			if (meta != null) 
			{
				GTaskClient.getInstance().deleteNode(meta);
			}
			mLocalDeleteIdMap.add(c.getLong(SqlNote.ID_COLUMN));
			break;
		case Node.SYNC_ACTION_DEL_REMOTE:
			meta = mMetaHashMap.get(node.getGid());
			if (meta != null) 
			{
				GTaskClient.getInstance().deleteNode(meta);
			}
			GTaskClient.getInstance().deleteNode(node);
			break;
		case Node.SYNC_ACTION_UPDATE_LOCAL:
			updateLocalNode(node, c);
			break;
		case Node.SYNC_ACTION_UPDATE_REMOTE:
			updateRemoteNode(node, c);
			break;
		case Node.SYNC_ACTION_UPDATE_CONFLICT:
			updateRemoteNode(node, c);
			break;
		case Node.SYNC_ACTION_NONE:
			break;
		case Node.SYNC_ACTION_ERROR:
		default:
			break;
		}
    }

    private void addLocalNode(Node node) throws Exception 
    {
        if (mCancelled) 
        {
            return;
        }

        SqlNote sqlNote;
        if (node instanceof TaskList) 
        {
            if (node.getName().equals(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_DEFAULT)) 
            {
                sqlNote = new SqlNote(mContext, Notes.ID_ROOT_FOLDER);
            }
            else if (node.getName().equals(GTaskUtil.MIUI_FOLDER_PREFFIX + GTaskUtil.FOLDER_CALL_NOTE)) 
            {
                sqlNote = new SqlNote(mContext, Notes.ID_CALL_RECORD_FOLDER);
            } 
            else 
            {
                sqlNote = new SqlNote(mContext);
                sqlNote.setContent(node.getLocalJSONFromContent());
                sqlNote.setParentId(Notes.ID_ROOT_FOLDER);
            }
        } 
        else 
        {
            sqlNote = new SqlNote(mContext);
            JSONObject js = node.getLocalJSONFromContent();
            try 
            {
                if (js.has(GTaskUtil.META_HEAD_NOTE)) 
                {
                    JSONObject note = js.getJSONObject(GTaskUtil.META_HEAD_NOTE);
                    if (note.has(NoteColumns.ID)) 
                    {
                        long id = note.getLong(NoteColumns.ID);
                        if (DataUtil.existInNoteDatabase(mContentResolver, id)) 
                        {
                            note.remove(NoteColumns.ID);
                        }
                    }
                }

                if (js.has(GTaskUtil.META_HEAD_DATA)) 
                {
                    JSONArray dataArray = js.getJSONArray(GTaskUtil.META_HEAD_DATA);
                    for (int i = 0; i < dataArray.length(); i++) {
                    	
                        JSONObject data = dataArray.getJSONObject(i);
                        if (data.has(DataColumns.ID)) 
                        {
                            long dataId = data.getLong(DataColumns.ID);
                            if (DataUtil.existInDataDatabase(mContentResolver, dataId)) 
                            {
                                data.remove(DataColumns.ID);
                            }
                        }
                    }

                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            sqlNote.setContent(js);

            Long parentId = mGidToNid.get(((Task) node).getParent().getGid());
            sqlNote.setParentId(parentId.longValue());
        }

        sqlNote.setGtaskId(node.getGid());
        sqlNote.commit(false);

        mGidToNid.put(node.getGid(), sqlNote.getId());
        mNidToGid.put(sqlNote.getId(), node.getGid());
        updateRemoteMeta(node.getGid(), sqlNote);
    }

    private void updateLocalNode(Node node, Cursor c) throws Exception 
    {
        if (mCancelled) 
        {
            return;
        }

        SqlNote sqlNote;
        sqlNote = new SqlNote(mContext, c);
        sqlNote.setContent(node.getLocalJSONFromContent());
        Long parentId = (node instanceof Task) ? mGidToNid.get(((Task) node).getParent().getGid()) : new Long(Notes.ID_ROOT_FOLDER);
        sqlNote.setParentId(parentId.longValue());
        sqlNote.commit(true);
        updateRemoteMeta(node.getGid(), sqlNote);
    }

    private void addRemoteNode(Node node, Cursor c) throws Exception 
    {
        if (mCancelled) 
        {
            return;
        }

        SqlNote sqlNote = new SqlNote(mContext, c);
        Node n;

        if (sqlNote.isNoteType()) 
        {
            Task task = new Task();
            task.setContentByLocalJSON(sqlNote.getContent());
            String parentGid = mNidToGid.get(sqlNote.getParentId());
            mGTaskListHashMap.get(parentGid).addChildTask(task);
            GTaskClient.getInstance().createTask(task);
            n = (Node) task;
            updateRemoteMeta(task.getGid(), sqlNote);
        } 
        else 
        {
            TaskList tasklist = null;
            String folderName = GTaskUtil.MIUI_FOLDER_PREFFIX;
            if (sqlNote.getId() == Notes.ID_ROOT_FOLDER)
            {
            	folderName += GTaskUtil.FOLDER_DEFAULT;
            }
            else if (sqlNote.getId() == Notes.ID_CALL_RECORD_FOLDER)
            {
            	folderName += GTaskUtil.FOLDER_CALL_NOTE;
            }
            else
            {
            	folderName += sqlNote.getSnippet();
            }

            Iterator<Map.Entry<String, TaskList>> iter = mGTaskListHashMap.entrySet().iterator();
            while (iter.hasNext()) 
            {
                Map.Entry<String, TaskList> entry = iter.next();
                String gid = entry.getKey();
                TaskList list = entry.getValue();
                if (list.getName().equals(folderName)) 
                {
                    tasklist = list;
                    if (mGTaskHashMap.containsKey(gid)) 
                    {
                        mGTaskHashMap.remove(gid);
                    }
                    break;
                }
            }

            if (tasklist == null) 
            {
                tasklist = new TaskList();
                tasklist.setContentByLocalJSON(sqlNote.getContent());
                GTaskClient.getInstance().createTaskList(tasklist);
                mGTaskListHashMap.put(tasklist.getGid(), tasklist);
            }
            n = (Node) tasklist;
        }

        sqlNote.setGtaskId(n.getGid());
        sqlNote.commit(false);
        sqlNote.resetLocalModified();
        sqlNote.commit(true);

        mGidToNid.put(n.getGid(), sqlNote.getId());
        mNidToGid.put(sqlNote.getId(), n.getGid());
    }

    private void updateRemoteNode(Node node, Cursor c) throws Exception 
    {
        if (mCancelled) 
        {
            return;
        }

        SqlNote sqlNote = new SqlNote(mContext, c);
        node.setContentByLocalJSON(sqlNote.getContent());
        GTaskClient.getInstance().addUpdateNode(node);
        updateRemoteMeta(node.getGid(), sqlNote);

        if (sqlNote.isNoteType()) 
        {
            Task task = (Task) node;
            TaskList preParentList = task.getParent();
            String curParentGid = mNidToGid.get(sqlNote.getParentId());
            TaskList curParentList = mGTaskListHashMap.get(curParentGid);
            if (preParentList != curParentList) 
            {
                preParentList.removeChildTask(task);
                curParentList.addChildTask(task);
                GTaskClient.getInstance().moveTask(task, preParentList, curParentList);
            }
        }
        sqlNote.resetLocalModified();
        sqlNote.commit(true);
    }

    private void updateRemoteMeta(String gid, SqlNote sqlNote) throws Exception 
    {
        if (sqlNote != null && sqlNote.isNoteType()) 
        {
            MetaData metaData = mMetaHashMap.get(gid);
            if (metaData != null) 
            {
                metaData.setMeta(gid, sqlNote.getContent());
                GTaskClient.getInstance().addUpdateNode(metaData);
            } 
            else 
            {
                metaData = new MetaData();
                metaData.setMeta(gid, sqlNote.getContent());
                mMetaList.addChildTask(metaData);
                mMetaHashMap.put(gid, metaData);
                GTaskClient.getInstance().createTask(metaData);
            }
        }
    }

    private void refreshLocalSyncId() throws Exception 
    {
        if (mCancelled) 
        {
            return;
        }

        mGTaskHashMap.clear();
        mGTaskListHashMap.clear();
        mMetaHashMap.clear();
        initGTaskList();

        Cursor c = null;
        try 
        {
            c = mContentResolver.query(Notes.CONTENT_NOTE_URI, SqlNote.PROJECTION_NOTE, "(type<>? AND parent_id<>?)", 
            	new String[] { String.valueOf(Notes.TYPE_SYSTEM), String.valueOf(Notes.ID_TRASH_FOLER) }, NoteColumns.TYPE + " DESC");
            if (c != null) 
            {
                while (c.moveToNext()) 
                {
                    String gid = c.getString(SqlNote.GTASK_ID_COLUMN);
                    Node node = mGTaskHashMap.get(gid);
                    if (node != null) 
                    {
                        mGTaskHashMap.remove(gid);
                        ContentValues values = new ContentValues();
                        values.put(NoteColumns.SYNC_ID, node.getLastModified());
                        mContentResolver.update(ContentUris.withAppendedId(Notes.CONTENT_NOTE_URI, 
                        		c.getLong(SqlNote.ID_COLUMN)), values, null, null);
                    } 
                }
            }
        } 
        finally 
        {
            if (c != null) 
            {
                c.close();
                c = null;
            }
        }
    }

    public String getSyncAccount() 
    {
        return GTaskClient.getInstance().getSyncAccount().name;
    }

    public void cancelSync() 
    {
        mCancelled = true;
    }
}
