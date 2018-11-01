package com.zhaoqy.app.demo.notebook.milletnotes.view;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.db.Notes;
import com.zhaoqy.app.demo.notebook.milletnotes.item.NoteItem;
import com.zhaoqy.app.demo.notebook.milletnotes.util.DataUtil;
import com.zhaoqy.app.demo.notebook.milletnotes.util.ResourceParser.NoteItemBgResources;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteView extends LinearLayout 
{
	private ImageView mAlert;
    private TextView  mTitle;
    private TextView  mTime;
    private TextView  mCallName;
    private NoteItem  mItemData;
    private CheckBox  mCheckBox;

    public NoteView(Context context) 
    {
        super(context);
        inflate(context, R.layout.item_milletnote_note, this);
        mAlert = (ImageView) findViewById(R.id.iv_alert_icon);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mTime = (TextView) findViewById(R.id.tv_time);
        mCallName = (TextView) findViewById(R.id.tv_name);
        mCheckBox = (CheckBox) findViewById(android.R.id.checkbox);
    }

    public void bind(Context context, NoteItem data, boolean choiceMode, boolean checked) 
    {
        if (choiceMode && data.getType() == Notes.TYPE_NOTE) 
        {
            mCheckBox.setVisibility(View.VISIBLE);
            mCheckBox.setChecked(checked);
        } 
        else 
        {
            mCheckBox.setVisibility(View.GONE);
        }

        mItemData = data;
        if (data.getId() == Notes.ID_CALL_RECORD_FOLDER) 
        {
            mCallName.setVisibility(View.GONE);
            mAlert.setVisibility(View.VISIBLE);
            mTitle.setTextAppearance(context, R.style.TextAppearancePrimaryItem);
            mTitle.setText(context.getString(R.string.milletnote_call_record_folder_name)
                    + context.getString(R.string.milletnote_format_folder_files_count, data.getNotesCount()));
            mAlert.setImageResource(R.drawable.milletnotes_call_record);
        } 
        else if (data.getParentId() == Notes.ID_CALL_RECORD_FOLDER) 
        {
            mCallName.setVisibility(View.VISIBLE);
            mCallName.setText(data.getCallName());
            mTitle.setTextAppearance(context,R.style.TextAppearanceSecondaryItem);
            mTitle.setText(DataUtil.getFormattedSnippet(data.getSnippet()));
            if (data.hasAlert()) 
            {
                mAlert.setImageResource(R.drawable.milletnotes_clock);
                mAlert.setVisibility(View.VISIBLE);
            } 
            else 
            {
                mAlert.setVisibility(View.GONE);
            }
        } 
        else 
        {
            mCallName.setVisibility(View.GONE);
            mTitle.setTextAppearance(context, R.style.TextAppearancePrimaryItem);
            if (data.getType() == Notes.TYPE_FOLDER) 
            {
                mTitle.setText(data.getSnippet() + context.getString(R.string.milletnote_format_folder_files_count,
                                data.getNotesCount()));
                mAlert.setVisibility(View.GONE);
            } 
            else 
            {
                mTitle.setText(DataUtil.getFormattedSnippet(data.getSnippet()));
                if (data.hasAlert()) 
                {
                    mAlert.setImageResource(R.drawable.milletnotes_clock);
                    mAlert.setVisibility(View.VISIBLE);
                } 
                else 
                {
                    mAlert.setVisibility(View.GONE);
                }
            }
        }
        mTime.setText(DateUtils.getRelativeTimeSpanString(data.getModifiedDate()));
        setBackground(data);
    }

    private void setBackground(NoteItem data) 
    {
        int id = data.getBgColorId();
        if (data.getType() == Notes.TYPE_NOTE) 
        {
            if (data.isSingle() || data.isOneFollowingFolder()) 
            {
                setBackgroundResource(NoteItemBgResources.getNoteBgSingleRes(id));
            } 
            else if (data.isLast()) 
            {
                setBackgroundResource(NoteItemBgResources.getNoteBgLastRes(id));
            } 
            else if (data.isFirst() || data.isMultiFollowingFolder()) 
            {
                setBackgroundResource(NoteItemBgResources.getNoteBgFirstRes(id));
            } 
            else 
            {
                setBackgroundResource(NoteItemBgResources.getNoteBgNormalRes(id));
            }
        } 
        else 
        {
            setBackgroundResource(NoteItemBgResources.getFolderBgRes());
        }
    }

    public NoteItem getItemData() 
    {
        return mItemData;
    }
}
