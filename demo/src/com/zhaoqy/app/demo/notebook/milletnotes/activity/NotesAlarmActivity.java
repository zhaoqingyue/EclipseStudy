package com.zhaoqy.app.demo.notebook.milletnotes.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.util.DataUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

public class NotesAlarmActivity extends Activity implements OnClickListener, OnDismissListener
{
	private static final int SNIPPET_PREW_MAX_LEN = 60;
    private MediaPlayer mPlayer;
    private String      mSnippet;
    private long        mNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        if (!isScreenOn()) 
        {
            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        }

        Intent intent = getIntent();
        try 
        {
            mNoteId = Long.valueOf(intent.getData().getPathSegments().get(1));
            mSnippet = DataUtil.getSnippetById(getContentResolver(), mNoteId);
            mSnippet = mSnippet.length() > SNIPPET_PREW_MAX_LEN ? mSnippet.substring(0, SNIPPET_PREW_MAX_LEN) + "..." : mSnippet;
        }
        catch (IllegalArgumentException e) 
        {
            e.printStackTrace();
            return;
        }

        mPlayer = new MediaPlayer();
        if (DataUtil.visibleInNoteDatabase(getContentResolver(), mNoteId, Notes.TYPE_NOTE)) 
        {
            showActionDialog();
            playAlarmSound();
        } 
        else 
        {
            finish();
        }
    }

    private boolean isScreenOn() 
    {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    private void playAlarmSound() 
    {
        Uri url = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        int silentModeStreams = Settings.System.getInt(getContentResolver(), Settings.System.MODE_RINGER_STREAMS_AFFECTED, 0);

        if ((silentModeStreams & (1 << AudioManager.STREAM_ALARM)) != 0) 
        {
            mPlayer.setAudioStreamType(silentModeStreams);
        } 
        else 
        {
            mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        }
        try 
        {
            mPlayer.setDataSource(this, url);
            mPlayer.prepare();
            mPlayer.setLooping(true);
            mPlayer.start();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
    }

    private void showActionDialog() 
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.milletnote_name);
        dialog.setMessage(mSnippet);
        dialog.setPositiveButton(R.string.milletnote_alert_ok, this);
        if (isScreenOn()) 
        {
            dialog.setNegativeButton(R.string.milletnote_alert_enter, this);
        }
        dialog.show().setOnDismissListener(this);
    }

    public void onClick(DialogInterface dialog, int which) 
    {
        switch (which) 
        {
		case DialogInterface.BUTTON_NEGATIVE:
		{
			Intent intent = new Intent(this, NotesEditActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			intent.putExtra(Intent.EXTRA_UID, mNoteId);
			startActivity(intent);
			break;
		}
		default:
			break;
        }
    }

    public void onDismiss(DialogInterface dialog) 
    {
        stopAlarmSound();
        finish();
    }

    private void stopAlarmSound() 
    {
        if (mPlayer != null) 
        {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
