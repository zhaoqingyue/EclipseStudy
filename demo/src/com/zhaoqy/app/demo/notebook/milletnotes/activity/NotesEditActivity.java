package com.zhaoqy.app.demo.notebook.milletnotes.activity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes.TextNote;
import com.zhaoqy.app.demo.notebook.milletnotes.dialog.DateTimePickerDialog;
import com.zhaoqy.app.demo.notebook.milletnotes.dialog.DateTimePickerDialog.OnDateTimeSetListener;
import com.zhaoqy.app.demo.notebook.milletnotes.model.WorkingNote;
import com.zhaoqy.app.demo.notebook.milletnotes.model.WorkingNote.NoteSettingChangedListener;
import com.zhaoqy.app.demo.notebook.milletnotes.util.DataUtil;
import com.zhaoqy.app.demo.notebook.milletnotes.util.ResourceParser;
import com.zhaoqy.app.demo.notebook.milletnotes.util.ResourceParser.TextAppearanceResources;
import com.zhaoqy.app.demo.notebook.milletnotes.view.NoteEditText;
import com.zhaoqy.app.demo.notebook.milletnotes.view.NoteEditText.OnTextViewChangeListener;
import com.zhaoqy.app.demo.notebook.milletnotes.view.NoteWidgetProvider_2x;
import com.zhaoqy.app.demo.notebook.milletnotes.view.NoteWidgetProvider_4x;

@SuppressLint("UseSparseArrays")
public class NotesEditActivity extends Activity implements OnClickListener, NoteSettingChangedListener, OnTextViewChangeListener
{
	private static final int    SHORTCUT_ICON_TITLE_MAX_LEN = 10;
	private static final String PREFERENCE_FONT_SIZE = "pref_font_size";
	public  static final String TAG_CHECKED = String.valueOf('\u221A');
	public  static final String TAG_UNCHECKED = String.valueOf('\u25A1');
	
	private class HeadViewHolder 
	{
        public TextView  tvModified;
        public ImageView ivAlertIcon;
        public TextView  tvAlertDate;
        public ImageView ibSetBgColor;
    }

    private static final Map<Integer, Integer> sBgSelectorBtnsMap = new HashMap<Integer, Integer>();
    static 
    {
        sBgSelectorBtnsMap.put(R.id.iv_bg_yellow, ResourceParser.YELLOW);
        sBgSelectorBtnsMap.put(R.id.iv_bg_red,    ResourceParser.RED);
        sBgSelectorBtnsMap.put(R.id.iv_bg_blue,   ResourceParser.BLUE);
        sBgSelectorBtnsMap.put(R.id.iv_bg_green,  ResourceParser.GREEN);
        sBgSelectorBtnsMap.put(R.id.iv_bg_white,  ResourceParser.WHITE);
    }

    private static final Map<Integer, Integer> sBgSelectorSelectionMap = new HashMap<Integer, Integer>();
    static 
    {
        sBgSelectorSelectionMap.put(ResourceParser.YELLOW, R.id.iv_bg_yellow_select);
        sBgSelectorSelectionMap.put(ResourceParser.RED,    R.id.iv_bg_red_select);
        sBgSelectorSelectionMap.put(ResourceParser.BLUE,   R.id.iv_bg_blue_select);
        sBgSelectorSelectionMap.put(ResourceParser.GREEN,  R.id.iv_bg_green_select);
        sBgSelectorSelectionMap.put(ResourceParser.WHITE,  R.id.iv_bg_white_select);
    }

    private static final Map<Integer, Integer> sFontSizeBtnsMap = new HashMap<Integer, Integer>();
    static 
    {
        sFontSizeBtnsMap.put(R.id.ll_font_large,  ResourceParser.TEXT_LARGE);
        sFontSizeBtnsMap.put(R.id.ll_font_small,  ResourceParser.TEXT_SMALL);
        sFontSizeBtnsMap.put(R.id.ll_font_normal, ResourceParser.TEXT_MEDIUM);
        sFontSizeBtnsMap.put(R.id.ll_font_super,  ResourceParser.TEXT_SUPER);
    }

    private static final Map<Integer, Integer> sFontSelectorSelectionMap = new HashMap<Integer, Integer>();
    static 
    {
        sFontSelectorSelectionMap.put(ResourceParser.TEXT_LARGE,  R.id.iv_large_select);
        sFontSelectorSelectionMap.put(ResourceParser.TEXT_SMALL,  R.id.iv_small_select);
        sFontSelectorSelectionMap.put(ResourceParser.TEXT_MEDIUM, R.id.iv_medium_select);
        sFontSelectorSelectionMap.put(ResourceParser.TEXT_SUPER,  R.id.iv_super_select);
    }

    private HeadViewHolder    mNoteHeaderHolder;
    private View              mHeadViewPanel;
    private View              mNoteBgColorSelector;
    private View              mFontSizeSelector;
    private EditText          mNoteEditor;
    private View              mNoteEditorPanel;
    private WorkingNote       mWorkingNote;
    private SharedPreferences mSharedPrefs;
    private LinearLayout      mEditTextList;
    private String            mUserQuery;
    private Pattern           mPattern;
    private Context           mContext;
    private int               mFontSizeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milletnote_edit);
        mContext = this;

        if (savedInstanceState == null && !initActivityState(getIntent())) 
        {
            finish();
            return;
        }
        initResources();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) 
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(Intent.EXTRA_UID)) 
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra(Intent.EXTRA_UID, savedInstanceState.getLong(Intent.EXTRA_UID));
            if (!initActivityState(intent)) 
            {
                finish();
                return;
            }
        }
    }

    private boolean initActivityState(Intent intent) 
    {
        mWorkingNote = null;
        if (TextUtils.equals(Intent.ACTION_VIEW, intent.getAction())) 
        {
            long noteId = intent.getLongExtra(Intent.EXTRA_UID, 0);
            mUserQuery = "";
            if (intent.hasExtra(SearchManager.EXTRA_DATA_KEY)) 
            {
                noteId = Long.parseLong(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
                mUserQuery = intent.getStringExtra(SearchManager.USER_QUERY);
            }

            if (!DataUtil.visibleInNoteDatabase(getContentResolver(), noteId, Notes.TYPE_NOTE)) 
            {
                Intent jump = new Intent(mContext, NotesMainActivity.class);
                startActivity(jump);
                showToast(R.string.milletnote_error_note_not_exist);
                finish();
                return false;
            }
            else 
            {
                mWorkingNote = WorkingNote.load(this, noteId);
                if (mWorkingNote == null) 
                {
                    finish();
                    return false;
                }
            }
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
            		| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        else if(TextUtils.equals(Intent.ACTION_INSERT_OR_EDIT, intent.getAction())) 
        {
            long folderId = intent.getLongExtra(Notes.INTENT_EXTRA_FOLDER_ID, 0);
            int widgetId = intent.getIntExtra(Notes.INTENT_EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int widgetType = intent.getIntExtra(Notes.INTENT_EXTRA_WIDGET_TYPE,  Notes.TYPE_WIDGET_INVALIDE);
            int bgResId = intent.getIntExtra(Notes.INTENT_EXTRA_BACKGROUND_ID, ResourceParser.getDefaultBgId(this));

            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            long callDate = intent.getLongExtra(Notes.INTENT_EXTRA_CALL_DATE, 0);
            if (callDate != 0 && phoneNumber != null) 
            {
                long noteId = 0;
                if ((noteId = DataUtil.getNoteIdByPhoneNumberAndCallDate(getContentResolver(),  phoneNumber, callDate)) > 0) 
                {
                    mWorkingNote = WorkingNote.load(mContext, noteId);
                    if (mWorkingNote == null) 
                    {
                        finish();
                        return false;
                    }
                } 
                else 
                {
                    mWorkingNote = WorkingNote.createEmptyNote(mContext, folderId, widgetId, widgetType, bgResId);
                    mWorkingNote.convertToCallNote(phoneNumber, callDate);
                }
            } 
            else 
            {
                mWorkingNote = WorkingNote.createEmptyNote(mContext, folderId, widgetId, widgetType, bgResId);
            }

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            		| WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } 
        else 
        {
            finish();
            return false;
        }
        mWorkingNote.setOnSettingStatusChangedListener(this);
        return true;
    }

    @Override
    protected void onResume() 
    {
        super.onResume();
        initNoteScreen();
    }

    private void initNoteScreen() 
    {
        mNoteEditor.setTextAppearance(mContext, TextAppearanceResources.getTexAppearanceResource(mFontSizeId));
        if (mWorkingNote.getCheckListMode() == TextNote.MODE_CHECK_LIST) 
        {
            switchToListMode(mWorkingNote.getContent());
        } 
        else 
        {
            mNoteEditor.setText(getHighlightQueryResult(mWorkingNote.getContent(), mUserQuery));
            mNoteEditor.setSelection(mNoteEditor.getText().length());
        }
        for (Integer id : sBgSelectorSelectionMap.keySet()) 
        {
            findViewById(sBgSelectorSelectionMap.get(id)).setVisibility(View.GONE);
        }
        mHeadViewPanel.setBackgroundResource(mWorkingNote.getTitleBgResId());
        mNoteEditorPanel.setBackgroundResource(mWorkingNote.getBgColorResId());
        mNoteHeaderHolder.tvModified.setText(DateUtils.formatDateTime(mContext, mWorkingNote.getModifiedDate(), 
        	DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR));

        showAlertHeader();
    }

    private void showAlertHeader() 
    {
        if (mWorkingNote.hasClockAlert()) 
        {
            long time = System.currentTimeMillis();
            if (time > mWorkingNote.getAlertDate()) 
            {
                mNoteHeaderHolder.tvAlertDate.setText(R.string.milletnote_note_alert_expired);
            } 
            else 
            {
                mNoteHeaderHolder.tvAlertDate.setText(DateUtils.getRelativeTimeSpanString(mWorkingNote.getAlertDate(), time, 
                		DateUtils.MINUTE_IN_MILLIS));
            }
            mNoteHeaderHolder.tvAlertDate.setVisibility(View.VISIBLE);
            mNoteHeaderHolder.ivAlertIcon.setVisibility(View.VISIBLE);
        } 
        else 
        {
            mNoteHeaderHolder.tvAlertDate.setVisibility(View.GONE);
            mNoteHeaderHolder.ivAlertIcon.setVisibility(View.GONE);
        };
    }

    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
        initActivityState(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        if (!mWorkingNote.existInDatabase()) 
        {
            saveNote();
        }
        outState.putLong(Intent.EXTRA_UID, mWorkingNote.getNoteId());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) 
    {
        if (mNoteBgColorSelector.getVisibility() == View.VISIBLE && !inRangeOfView(mNoteBgColorSelector, ev)) 
        {
            mNoteBgColorSelector.setVisibility(View.GONE);
            return true;
        }

        if (mFontSizeSelector.getVisibility() == View.VISIBLE && !inRangeOfView(mFontSizeSelector, ev)) 
        {
            mFontSizeSelector.setVisibility(View.GONE);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean inRangeOfView(View view, MotionEvent ev) 
    {
        int []location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) 
        {
            return false;
        }
        return true;
    }

    private void initResources() 
    {
        mHeadViewPanel = findViewById(R.id.note_title);
        mNoteHeaderHolder = new HeadViewHolder();
        mNoteHeaderHolder.tvModified = (TextView) findViewById(R.id.tv_modified_date);
        mNoteHeaderHolder.ivAlertIcon = (ImageView) findViewById(R.id.iv_alert_icon);
        mNoteHeaderHolder.tvAlertDate = (TextView) findViewById(R.id.tv_alert_date);
        mNoteHeaderHolder.ibSetBgColor = (ImageView) findViewById(R.id.btn_set_bg_color);
        mNoteHeaderHolder.ibSetBgColor.setOnClickListener(this);
        mNoteEditor = (EditText) findViewById(R.id.note_edit_view);
        mNoteEditorPanel = findViewById(R.id.sv_note_edit);
        mNoteBgColorSelector = findViewById(R.id.note_bg_color_selector);
        for (int id : sBgSelectorBtnsMap.keySet()) 
        {
            ImageView iv = (ImageView) findViewById(id);
            iv.setOnClickListener(this);
        }

        mFontSizeSelector = findViewById(R.id.font_size_selector);
        for (int id : sFontSizeBtnsMap.keySet()) 
        {
            View view = findViewById(id);
            view.setOnClickListener(this);
        };
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mFontSizeId = mSharedPrefs.getInt(PREFERENCE_FONT_SIZE, ResourceParser.BG_DEFAULT_FONT_SIZE);
        if(mFontSizeId >= TextAppearanceResources.getResourcesSize()) 
        {
            mFontSizeId = ResourceParser.BG_DEFAULT_FONT_SIZE;
        }
        mEditTextList = (LinearLayout) findViewById(R.id.note_edit_list);
    }

    @Override
    protected void onPause() 
    {
        super.onPause();
        clearSettingState();
    }

    private void updateWidget() 
    {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        if (mWorkingNote.getWidgetType() == Notes.TYPE_WIDGET_2X) 
        {
            intent.setClass(mContext, NoteWidgetProvider_2x.class);
        } 
        else if (mWorkingNote.getWidgetType() == Notes.TYPE_WIDGET_4X) 
        {
            intent.setClass(mContext, NoteWidgetProvider_4x.class);
        } 
        else 
        {
            return;
        }
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] 
        {
            mWorkingNote.getWidgetId()
        });
        sendBroadcast(intent);
        setResult(RESULT_OK, intent);
    }

    public void onClick(View v) 
    {
        int id = v.getId();
        if (id == R.id.btn_set_bg_color) 
        {
            mNoteBgColorSelector.setVisibility(View.VISIBLE);
            findViewById(sBgSelectorSelectionMap.get(mWorkingNote.getBgColorId())).setVisibility(View.VISIBLE);
        } 
        else if (sBgSelectorBtnsMap.containsKey(id)) 
        {
            findViewById(sBgSelectorSelectionMap.get(mWorkingNote.getBgColorId())).setVisibility(View.GONE);
            mWorkingNote.setBgColorId(sBgSelectorBtnsMap.get(id));
            mNoteBgColorSelector.setVisibility(View.GONE);
        } 
        else if (sFontSizeBtnsMap.containsKey(id)) 
        {
            findViewById(sFontSelectorSelectionMap.get(mFontSizeId)).setVisibility(View.GONE);
            mFontSizeId = sFontSizeBtnsMap.get(id);
            mSharedPrefs.edit().putInt(PREFERENCE_FONT_SIZE, mFontSizeId).commit();
            findViewById(sFontSelectorSelectionMap.get(mFontSizeId)).setVisibility(View.VISIBLE);
            if (mWorkingNote.getCheckListMode() == TextNote.MODE_CHECK_LIST) 
            {
                getWorkingText();
                switchToListMode(mWorkingNote.getContent());
            } 
            else 
            {
                mNoteEditor.setTextAppearance(mContext, TextAppearanceResources.getTexAppearanceResource(mFontSizeId));
            }
            mFontSizeSelector.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() 
    {
        if(clearSettingState()) 
        {
            return;
        }
        saveNote();
        super.onBackPressed();
    }

    private boolean clearSettingState() 
    {
        if (mNoteBgColorSelector.getVisibility() == View.VISIBLE) 
        {
            mNoteBgColorSelector.setVisibility(View.GONE);
            return true;
        } 
        else if (mFontSizeSelector.getVisibility() == View.VISIBLE) 
        {
            mFontSizeSelector.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    public void onBackgroundColorChanged() 
    {
        findViewById(sBgSelectorSelectionMap.get(mWorkingNote.getBgColorId())).setVisibility(View.VISIBLE);
        mNoteEditorPanel.setBackgroundResource(mWorkingNote.getBgColorResId());
        mHeadViewPanel.setBackgroundResource(mWorkingNote.getTitleBgResId());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) 
    {
        if (isFinishing()) 
        {
            return true;
        }
        clearSettingState();
        menu.clear();
        
        if (mWorkingNote.getFolderId() == Notes.ID_CALL_RECORD_FOLDER) 
        {
            getMenuInflater().inflate(R.menu.milletnotes_note_call_edit, menu);
        } 
        else 
        {
            getMenuInflater().inflate(R.menu.milletnotes_note_edit, menu);
        }
        
        if (mWorkingNote.getCheckListMode() == TextNote.MODE_CHECK_LIST) 
        {
            menu.findItem(R.id.menu_list_mode).setTitle(R.string.milletnote_edit_menu_normal_mode);
        } 
        else 
        {
            menu.findItem(R.id.menu_list_mode).setTitle(R.string.milletnote_edit_menu_list_mode);
        }
        
        if (mWorkingNote.hasClockAlert()) 
        {
            menu.findItem(R.id.menu_alert).setVisible(false);
        } 
        else 
        {
            menu.findItem(R.id.menu_delete_remind).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
		switch (item.getItemId()) 
		{
		case R.id.menu_new_note:
		{
			createNewNote();
			break;
		}
		case R.id.menu_delete:
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(getString(R.string.milletnote_alert_title_delete));
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setMessage(getString(R.string.milletnote_alert_message_delete_note));
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					deleteCurrentNote();
					finish();
				}
			});
			builder.setNegativeButton(android.R.string.cancel, null);
			builder.show();
			break;
		}
		case R.id.menu_font_size:
		{
			mFontSizeSelector.setVisibility(View.VISIBLE);
			findViewById(sFontSelectorSelectionMap.get(mFontSizeId)).setVisibility(View.VISIBLE);
			break;
		}
		case R.id.menu_list_mode:
		{
			mWorkingNote.setCheckListMode(mWorkingNote.getCheckListMode() == 0 ? TextNote.MODE_CHECK_LIST : 0);
			break;
		}
		case R.id.menu_share:
		{
			getWorkingText();
			sendTo(mContext, mWorkingNote.getContent());
			break;
		}
		case R.id.menu_send_to_desktop:
		{
			sendToDesktop();
			break;
		}
		case R.id.menu_alert:
		{
			setReminder();
			break;
		}
		case R.id.menu_delete_remind:
		{
			mWorkingNote.setAlertDate(0, false);
			break;
		}
		default:
			break;
		}
        return true;
    }

    private void setReminder() 
    {
        DateTimePickerDialog d = new DateTimePickerDialog(mContext, System.currentTimeMillis());
        d.setOnDateTimeSetListener(new OnDateTimeSetListener() 
        {
            public void OnDateTimeSet(AlertDialog dialog, long date) 
            {
                mWorkingNote.setAlertDate(date	, true);
            }
        });
        d.show();
    }

    private void sendTo(Context context, String info) 
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, info);
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    private void createNewNote() 
    {
        saveNote();
        finish();
        Intent intent = new Intent(mContext, NotesEditActivity.class);
        intent.setAction(Intent.ACTION_INSERT_OR_EDIT);
        intent.putExtra(Notes.INTENT_EXTRA_FOLDER_ID, mWorkingNote.getFolderId());
        startActivity(intent);
    }

    private void deleteCurrentNote() 
    {
        if (mWorkingNote.existInDatabase()) 
        {
            HashSet<Long> ids = new HashSet<Long>();
            long id = mWorkingNote.getNoteId();
            if (id != Notes.ID_ROOT_FOLDER) 
            {
                ids.add(id);
            } 
            
            if (!isSyncMode()) 
            {
                if (!DataUtil.batchDeleteNotes(getContentResolver(), ids)) 
                {
                }
            } 
            else 
            {
                if (!DataUtil.batchMoveToFolder(getContentResolver(), ids, Notes.ID_TRASH_FOLER)) 
                {
                }
            }
        }
        mWorkingNote.markDeleted(true);
    }

    private boolean isSyncMode() 
    {
        return NotesSettingsActivity.getSyncAccountName(mContext).trim().length() > 0;
    }

    public void onClockAlertChanged(long date, boolean set) 
    {
        if (!mWorkingNote.existInDatabase()) 
        {
            saveNote();
        }
        if (mWorkingNote.getNoteId() > 0) 
        {
            Intent intent = new Intent(mContext, AlarmReceiver.class);
            intent.setData(ContentUris.withAppendedId(Notes.CONTENT_NOTE_URI, mWorkingNote.getNoteId()));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
            AlarmManager alarmManager = ((AlarmManager) getSystemService(ALARM_SERVICE));
            showAlertHeader();
            if(!set) 
            {
                alarmManager.cancel(pendingIntent);
            } 
            else 
            {
                alarmManager.set(AlarmManager.RTC_WAKEUP, date, pendingIntent);
            }
        } 
        else 
        {
            showToast(R.string.milletnote_empty_for_clock);
        }
    }

    public void onWidgetChanged() 
    {
        updateWidget();
    }

    public void onEditTextDelete(int index, String text) 
    {
        int childCount = mEditTextList.getChildCount();
        if (childCount == 1) 
        {
            return;
        }

        for (int i = index + 1; i < childCount; i++) 
        {
            ((NoteEditText) mEditTextList.getChildAt(i).findViewById(R.id.et_edit_text)).setIndex(i - 1);
        }

        mEditTextList.removeViewAt(index);
        NoteEditText edit = null;
        if(index == 0) 
        {
            edit = (NoteEditText) mEditTextList.getChildAt(0).findViewById(R.id.et_edit_text);
        }
        else 
        {
            edit = (NoteEditText) mEditTextList.getChildAt(index - 1).findViewById(R.id.et_edit_text);
        }
        int length = edit.length();
        edit.append(text);
        edit.requestFocus();
        edit.setSelection(length);
    }

    public void onEditTextEnter(int index, String text) 
    {
        View view = getListItem(text, index);
        mEditTextList.addView(view, index);
        NoteEditText edit = (NoteEditText) view.findViewById(R.id.et_edit_text);
        edit.requestFocus();
        edit.setSelection(0);
        for (int i = index + 1; i < mEditTextList.getChildCount(); i++) 
        {
            ((NoteEditText) mEditTextList.getChildAt(i).findViewById(R.id.et_edit_text)).setIndex(i);
        }
    }

    private void switchToListMode(String text) 
    {
        mEditTextList.removeAllViews();
        String[] items = text.split("\n");
        int index = 0;
        for (String item : items) 
        {
            if(!TextUtils.isEmpty(item)) 
            {
                mEditTextList.addView(getListItem(item, index));
                index++;
            }
        }
        mEditTextList.addView(getListItem("", index));
        mEditTextList.getChildAt(index).findViewById(R.id.et_edit_text).requestFocus();
        mNoteEditor.setVisibility(View.GONE);
        mEditTextList.setVisibility(View.VISIBLE);
    }

    private Spannable getHighlightQueryResult(String fullText, String userQuery) 
    {
        SpannableString spannable = new SpannableString(fullText == null ? "" : fullText);
        if (!TextUtils.isEmpty(userQuery)) 
        {
            mPattern = Pattern.compile(userQuery);
            Matcher m = mPattern.matcher(fullText);
            int start = 0;
            while (m.find(start)) 
            {
                spannable.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.milletnotes_highlight)), 
                		m.start(), m.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                start = m.end();
            }
        }
        return spannable;
    }

    private View getListItem(String item, int index) 
    {
        View view = LayoutInflater.from(this).inflate(R.layout.item_milletnote_note_edit, null);
        final NoteEditText edit = (NoteEditText) view.findViewById(R.id.et_edit_text);
        edit.setTextAppearance(this, TextAppearanceResources.getTexAppearanceResource(mFontSizeId));
        CheckBox cb = ((CheckBox) view.findViewById(R.id.cb_edit_item));
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
            {
                if (isChecked) 
                {
                    edit.setPaintFlags(edit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else 
                {
                    edit.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
                }
            }
        });

        if (item.startsWith(TAG_CHECKED)) 
        {
            cb.setChecked(true);
            edit.setPaintFlags(edit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            item = item.substring(TAG_CHECKED.length(), item.length()).trim();
        }
        else if (item.startsWith(TAG_UNCHECKED)) 
        {
            cb.setChecked(false);
            edit.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
            item = item.substring(TAG_UNCHECKED.length(), item.length()).trim();
        }
        edit.setOnTextViewChangeListener(this);
        edit.setIndex(index);
        edit.setText(getHighlightQueryResult(item, mUserQuery));
        return view;
    }

    public void onTextChange(int index, boolean hasText) 
    {
        if (index >= mEditTextList.getChildCount()) 
        {
            return;
        }
        if(hasText) 
        {
            mEditTextList.getChildAt(index).findViewById(R.id.cb_edit_item).setVisibility(View.VISIBLE);
        } 
        else 
        {
            mEditTextList.getChildAt(index).findViewById(R.id.cb_edit_item).setVisibility(View.GONE);
        }
    }

    public void onCheckListModeChanged(int oldMode, int newMode) 
    {
        if (newMode == TextNote.MODE_CHECK_LIST) 
        {
            switchToListMode(mNoteEditor.getText().toString());
        } 
        else 
        {
            if (!getWorkingText()) 
            {
                mWorkingNote.setWorkingText(mWorkingNote.getContent().replace(TAG_UNCHECKED + " ", ""));
            }
            mNoteEditor.setText(getHighlightQueryResult(mWorkingNote.getContent(), mUserQuery));
            mEditTextList.setVisibility(View.GONE);
            mNoteEditor.setVisibility(View.VISIBLE);
        }
    }

    private boolean getWorkingText() 
    {
        boolean hasChecked = false;
        if (mWorkingNote.getCheckListMode() == TextNote.MODE_CHECK_LIST) 
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mEditTextList.getChildCount(); i++) 
            {
                View view = mEditTextList.getChildAt(i);
                NoteEditText edit = (NoteEditText) view.findViewById(R.id.et_edit_text);
                if (!TextUtils.isEmpty(edit.getText())) 
                {
                    if (((CheckBox) view.findViewById(R.id.cb_edit_item)).isChecked()) 
                    {
                        sb.append(TAG_CHECKED).append(" ").append(edit.getText()).append("\n");
                        hasChecked = true;
                    }
                    else 
                    {
                        sb.append(TAG_UNCHECKED).append(" ").append(edit.getText()).append("\n");
                    }
                }
            }
            mWorkingNote.setWorkingText(sb.toString());
        }
        else 
        {
            mWorkingNote.setWorkingText(mNoteEditor.getText().toString());
        }
        return hasChecked;
    }

    private boolean saveNote() 
    {
        getWorkingText();
        boolean saved = mWorkingNote.saveNote();
        if (saved) 
        {
            setResult(RESULT_OK);
        }
        return saved;
    }

    private void sendToDesktop() 
    {
        if (!mWorkingNote.existInDatabase()) 
        {
            saveNote();
        }

        if (mWorkingNote.getNoteId() > 0) 
        {
            Intent sender = new Intent();
            Intent shortcutIntent = new Intent(mContext, NotesEditActivity.class);
            shortcutIntent.setAction(Intent.ACTION_VIEW);
            shortcutIntent.putExtra(Intent.EXTRA_UID, mWorkingNote.getNoteId());
            sender.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            sender.putExtra(Intent.EXTRA_SHORTCUT_NAME, makeShortcutIconTitle(mWorkingNote.getContent()));
            sender.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, 
            	   Intent.ShortcutIconResource.fromContext(mContext, R.drawable.milletnotes_icon));
            sender.putExtra("duplicate", true);
            sender.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            showToast(R.string.milletnote_enter_desktop);
            sendBroadcast(sender);
        } 
        else 
        {
            showToast(R.string.milletnote_empty_to_desktop);
        }
    }

    private String makeShortcutIconTitle(String content) 
    {
        content = content.replace(TAG_CHECKED, "");
        content = content.replace(TAG_UNCHECKED, "");
        return content.length() > SHORTCUT_ICON_TITLE_MAX_LEN ? content.substring(0, SHORTCUT_ICON_TITLE_MAX_LEN) : content;
    }

    private void showToast(int resId) 
    {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    private void showToast(int resId, int duration) 
    {
        Toast.makeText(this, resId, duration).show();
    }
}
