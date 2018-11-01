package com.zhaoqy.app.demo.notebook.milletnotes.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.NoteColumns;
import com.zhaoqy.app.demo.notebook.milletnotes.gtask.GTaskSyncService;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotesSettingsActivity extends PreferenceActivity 
{
	public  static final String PREFERENCE_NAME = "notes_preferences";
    public  static final String PREFERENCE_SYNC_ACCOUNT_NAME = "pref_key_account_name";
    public  static final String PREFERENCE_LAST_SYNC_TIME = "pref_last_sync_time";
    public  static final String PREFERENCE_SET_BG_COLOR_KEY = "pref_key_bg_random_appear";
    private static final String PREFERENCE_SYNC_ACCOUNT_KEY = "pref_sync_account_key";
    private static final String AUTHORITIES_FILTER_KEY = "authorities";
    private Context            mContext;
    private PreferenceCategory mAccountCategory;
    private GTaskReceiver      mReceiver;
    private Account[]          mOriAccounts;
    private boolean            mHasAddedAccount;

    @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.milletnotes_settings);
        mContext = this;
        
        mAccountCategory = (PreferenceCategory) findPreference(PREFERENCE_SYNC_ACCOUNT_KEY);
        mReceiver = new GTaskReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GTaskSyncService.GTASK_SERVICE_BROADCAST_NAME);
        registerReceiver(mReceiver, filter);
        mOriAccounts = null;
        View header = LayoutInflater.from(this).inflate(R.layout.view_milletnotes_settings_header, null);
        getListView().addHeaderView(header, null, true);
    }

    @Override
    protected void onResume() 
    {
        super.onResume();
        if (mHasAddedAccount) 
        {
            Account[] accounts = getGoogleAccounts();
            if (mOriAccounts != null && accounts.length > mOriAccounts.length) 
            {
                for (Account accountNew : accounts) 
                {
                    boolean found = false;
                    for (Account accountOld : mOriAccounts) 
                    {
                        if (TextUtils.equals(accountOld.name, accountNew.name)) 
                        {
                            found = true;
                            break;
                        }
                    }
                    if (!found) 
                    {
                        setSyncAccount(accountNew.name);
                        break;
                    }
                }
            }
        }
        refreshUI();
    }

    @Override
    protected void onDestroy() 
    {
        if (mReceiver != null) 
        {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    private void loadAccountPreference() 
    {
        mAccountCategory.removeAll();
        Preference accountPref = new Preference(this);
        final String defaultAccount = getSyncAccountName(this);
        accountPref.setTitle(getString(R.string.milletnote_settings_account_title));
        accountPref.setSummary(getString(R.string.milletnote_settings_account_summary));
        accountPref.setOnPreferenceClickListener(new OnPreferenceClickListener() 
        {
            public boolean onPreferenceClick(Preference preference) 
            {
                if (!GTaskSyncService.isSyncing()) 
                {
                    if (TextUtils.isEmpty(defaultAccount)) 
                    {
                        showSelectAccountAlertDialog();
                    } 
                    else 
                    {
                        showChangeAccountConfirmAlertDialog();
                    }
                } 
                else 
                {
                    Toast.makeText(mContext, R.string.milletnote_settings_cannot_change_account, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        mAccountCategory.addPreference(accountPref);
    }

    private void loadSyncButton() 
    {
        Button syncButton = (Button) findViewById(R.id.preference_sync_button);
        TextView lastSyncTimeView = (TextView) findViewById(R.id.prefenerece_sync_status_textview);

        if (GTaskSyncService.isSyncing()) 
        {
            syncButton.setText(getString(R.string.milletnote_settings_sync_cancel));
            syncButton.setOnClickListener(new View.OnClickListener() 
            {
                public void onClick(View v) 
                {
                    GTaskSyncService.cancelSync(mContext);
                }
            });
        } 
        else 
        {
            syncButton.setText(getString(R.string.milletnote_settings_sync_immediately));
            syncButton.setOnClickListener(new View.OnClickListener() 
            {
                public void onClick(View v) 
                {
                    GTaskSyncService.startSync(NotesSettingsActivity.this);
                }
            });
        }
        syncButton.setEnabled(!TextUtils.isEmpty(getSyncAccountName(this)));

        if (GTaskSyncService.isSyncing()) 
        {
            lastSyncTimeView.setText(GTaskSyncService.getProgressString());
            lastSyncTimeView.setVisibility(View.VISIBLE);
        } 
        else 
        {
            long lastSyncTime = getLastSyncTime(this);
            if (lastSyncTime != 0) 
            {
                lastSyncTimeView.setText(getString(R.string.milletnote_settings_last_sync_time,
                DateFormat.format(getString(R.string.milletnote_settings_last_sync_time_format), lastSyncTime)));
                lastSyncTimeView.setVisibility(View.VISIBLE);
            } 
            else 
            {
                lastSyncTimeView.setVisibility(View.GONE);
            }
        }
    }

    private void refreshUI() 
    {
        loadAccountPreference();
        loadSyncButton();
    }

    private void showSelectAccountAlertDialog() 
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        View titleView = LayoutInflater.from(mContext).inflate(R.layout.dialog_milletnotes_accout_title, null);
        TextView titleTextView = (TextView) titleView.findViewById(R.id.account_dialog_title);
        titleTextView.setText(getString(R.string.milletnote_settings_dialog_select_account_title));
        TextView subtitleTextView = (TextView) titleView.findViewById(R.id.account_dialog_subtitle);
        subtitleTextView.setText(getString(R.string.milletnote_settings_dialog_select_account_tips));
        dialogBuilder.setCustomTitle(titleView);
        dialogBuilder.setPositiveButton(null, null);
        Account[] accounts = getGoogleAccounts();
        String defAccount = getSyncAccountName(this);

        mOriAccounts = accounts;
        mHasAddedAccount = false;

        if (accounts.length > 0) 
        {
            CharSequence[] items = new CharSequence[accounts.length];
            final CharSequence[] itemMapping = items;
            int checkedItem = -1;
            int index = 0;
            for (Account account : accounts) 
            {
                if (TextUtils.equals(account.name, defAccount)) 
                {
                    checkedItem = index;
                }
                items[index++] = account.name;
            }
            dialogBuilder.setSingleChoiceItems(items, checkedItem,
            new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    setSyncAccount(itemMapping[which].toString());
                    dialog.dismiss();
                    refreshUI();
                }
            });
        }

        View addAccountView = LayoutInflater.from(mContext).inflate(R.layout.view_milletnotes_add_account_title, null);
        dialogBuilder.setView(addAccountView);

        final AlertDialog dialog = dialogBuilder.show();
        addAccountView.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
                mHasAddedAccount = true;
                Intent intent = new Intent("android.settings.ADD_ACCOUNT_SETTINGS");
                intent.putExtra(AUTHORITIES_FILTER_KEY, new String[] { "gmail-ls" });
                startActivityForResult(intent, -1);
                dialog.dismiss();
            }
        });
    }

    private void showChangeAccountConfirmAlertDialog() 
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        View titleView = LayoutInflater.from(this).inflate(R.layout.dialog_milletnotes_accout_title, null);
        TextView titleTextView = (TextView) titleView.findViewById(R.id.account_dialog_title);
        titleTextView.setText(getString(R.string.milletnote_settings_dialog_change_account_title, getSyncAccountName(mContext)));
        TextView subtitleTextView = (TextView) titleView.findViewById(R.id.account_dialog_subtitle);
        subtitleTextView.setText(getString(R.string.milletnote_settings_dialog_change_account_warn_msg));
        dialogBuilder.setCustomTitle(titleView);
        CharSequence[] menuItemArray = new CharSequence[] 
        {
            getString(R.string.milletnote_settings_menu_change_account),
            getString(R.string.milletnote_settings_menu_remove_account),
            getString(R.string.milletnote_settings_menu_cancel)
        };
        dialogBuilder.setItems(menuItemArray, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {
                if (which == 0) 
                {
                    showSelectAccountAlertDialog();
                } 
                else if (which == 1) 
                {
                    removeSyncAccount();
                    refreshUI();
                }
            }
        });
        dialogBuilder.show();
    }

    private Account[] getGoogleAccounts() 
    {
        AccountManager accountManager = AccountManager.get(this);
        return accountManager.getAccountsByType("com.google");
    }

    private void setSyncAccount(String account) 
    {
        if (!getSyncAccountName(mContext).equals(account)) 
        {
            SharedPreferences settings = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            if (account != null) 
            {
                editor.putString(PREFERENCE_SYNC_ACCOUNT_NAME, account);
            } 
            else 
            {
                editor.putString(PREFERENCE_SYNC_ACCOUNT_NAME, "");
            }
            editor.commit();
            setLastSyncTime(this, 0);
            new Thread(new Runnable() 
            {
                public void run() 
                {
                    ContentValues values = new ContentValues();
                    values.put(NoteColumns.GTASK_ID, "");
                    values.put(NoteColumns.SYNC_ID, 0);
                    getContentResolver().update(Notes.CONTENT_NOTE_URI, values, null, null);
                }
            }).start();
            Toast.makeText(mContext, getString(R.string.milletnote_settings_success_set_accout, account), Toast.LENGTH_SHORT).show();
        }
    }

    private void removeSyncAccount() 
    {
        SharedPreferences settings = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        if (settings.contains(PREFERENCE_SYNC_ACCOUNT_NAME)) 
        {
            editor.remove(PREFERENCE_SYNC_ACCOUNT_NAME);
        }
        if (settings.contains(PREFERENCE_LAST_SYNC_TIME)) 
        {
            editor.remove(PREFERENCE_LAST_SYNC_TIME);
        }
        editor.commit();
        new Thread(new Runnable() 
        {
            public void run() 
            {
                ContentValues values = new ContentValues();
                values.put(NoteColumns.GTASK_ID, "");
                values.put(NoteColumns.SYNC_ID, 0);
                getContentResolver().update(Notes.CONTENT_NOTE_URI, values, null, null);
            }
        }).start();
    }

    public static String getSyncAccountName(Context context) 
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(PREFERENCE_SYNC_ACCOUNT_NAME, "");
    }

    public static void setLastSyncTime(Context context, long time) 
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(PREFERENCE_LAST_SYNC_TIME, time);
        editor.commit();
    }

    public static long getLastSyncTime(Context context) 
    {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(PREFERENCE_LAST_SYNC_TIME, 0);
    }

    private class GTaskReceiver extends BroadcastReceiver 
    {
        @Override
        public void onReceive(Context context, Intent intent) 
        {
            refreshUI();
            if (intent.getBooleanExtra(GTaskSyncService.GTASK_SERVICE_BROADCAST_IS_SYNCING, false)) 
            {
                TextView syncStatus = (TextView) findViewById(R.id.prefenerece_sync_status_textview);
                syncStatus.setText(intent.getStringExtra(GTaskSyncService.GTASK_SERVICE_BROADCAST_PROGRESS_MSG));
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
		case android.R.id.home: 
		{
			Intent intent = new Intent(mContext, NotesMainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		default:
			return false;
        }
    }
}
