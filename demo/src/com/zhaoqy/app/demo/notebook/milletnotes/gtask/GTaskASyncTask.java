package com.zhaoqy.app.demo.notebook.milletnotes.gtask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.activity.NotesMainActivity;
import com.zhaoqy.app.demo.notebook.milletnotes.activity.NotesSettingsActivity;

public class GTaskASyncTask extends AsyncTask<Void, String, Integer>
{
	private static int GTASK_SYNC_NOTIFICATION_ID = 5234235;

    public interface OnCompleteListener 
    {
        void onComplete();
    }

    private Context mContext;
    private NotificationManager mNotifiManager;
    private GTaskManager mTaskManager;
    private OnCompleteListener mOnCompleteListener;

    public GTaskASyncTask(Context context, OnCompleteListener listener) 
    {
        mContext = context;
        mOnCompleteListener = listener;
        mNotifiManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mTaskManager = GTaskManager.getInstance();
    }

    public void cancelSync() 
    {
        mTaskManager.cancelSync();
    }

    public void publishProgess(String message) 
    {
        publishProgress(new String[] 
        {
            message
        });
    }

    @SuppressWarnings("deprecation")
	private void showNotification(int tickerId, String content) 
    {
		Notification notification = new Notification(R.drawable.milletnotes_notification, 
        		mContext.getString(tickerId), System.currentTimeMillis());
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        PendingIntent pendingIntent;
        if (tickerId != R.string.milletnote_ticker_success) 
        {
            pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, NotesSettingsActivity.class), 0);
        } 
        else 
        {
            pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, NotesMainActivity.class), 0);
        }
        notification.setLatestEventInfo(mContext, mContext.getString(R.string.milletnote_name), content, pendingIntent);
        mNotifiManager.notify(GTASK_SYNC_NOTIFICATION_ID, notification);
    }

    @Override
    protected Integer doInBackground(Void... unused) 
    {
        publishProgess(mContext.getString(R.string.milletnote_sync_progress_login, NotesSettingsActivity.getSyncAccountName(mContext)));
        return mTaskManager.sync(mContext, this);
    }

    @Override
    protected void onProgressUpdate(String... progress) 
    {
        showNotification(R.string.milletnote_ticker_syncing, progress[0]);
        if (mContext instanceof GTaskSyncService) 
        {
            ((GTaskSyncService) mContext).sendBroadcast(progress[0]);
        }
    }

    @Override
    protected void onPostExecute(Integer result) 
    {
        if (result == GTaskManager.STATE_SUCCESS) 
        {
            showNotification(R.string.milletnote_ticker_success, 
            		mContext.getString(R.string.milletnote_sync_account_success, mTaskManager.getSyncAccount()));
            NotesSettingsActivity.setLastSyncTime(mContext, System.currentTimeMillis());
        } 
        else if (result == GTaskManager.STATE_NETWORK_ERROR) 
        {
            showNotification(R.string.milletnote_ticker_fail, mContext.getString(R.string.milletnote_sync_error_network));
        } 
        else if (result == GTaskManager.STATE_INTERNAL_ERROR) 
        {
            showNotification(R.string.milletnote_ticker_fail, mContext.getString(R.string.milletnote_sync_error_internal));
        } 
        else if (result == GTaskManager.STATE_SYNC_CANCELLED) 
        {
            showNotification(R.string.milletnote_ticker_cancel, mContext.getString(R.string.milletnote_sync_error_cancelled));
        }
        if (mOnCompleteListener != null) 
        {
            new Thread(new Runnable() 
            {
                public void run() 
                {
                    mOnCompleteListener.onComplete();
                }
            }).start();
        }
    }
}
