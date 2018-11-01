package com.zhaoqy.app.demo.notebook.milletnotes.dialog;

import java.util.Calendar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.milletnotes.dialog.DateTimePicker.OnDateTimeChangedListener;

public class DateTimePickerDialog extends AlertDialog implements OnClickListener
{
    private OnDateTimeSetListener mOnDateTimeSetListener;
    private DateTimePicker        mDateTimePicker;
    private Calendar              mDate = Calendar.getInstance();
    private boolean               mIs24HourView;

    public interface OnDateTimeSetListener 
    {
        void OnDateTimeSet(AlertDialog dialog, long date);
    }

    @SuppressWarnings("deprecation")
	public DateTimePickerDialog(Context context, long date) 
    {
        super(context);
        mDateTimePicker = new DateTimePicker(context);
        setView(mDateTimePicker);
        mDateTimePicker.setOnDateTimeChangedListener(new OnDateTimeChangedListener() 
        {
            public void onDateTimeChanged(DateTimePicker view, int year, int month, int dayOfMonth, int hourOfDay, int minute) 
            {
                mDate.set(Calendar.YEAR, year);
                mDate.set(Calendar.MONTH, month);
                mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDate.set(Calendar.MINUTE, minute);
                updateTitle(mDate.getTimeInMillis());
            }
        });
        mDate.setTimeInMillis(date);
        mDate.set(Calendar.SECOND, 0);
        mDateTimePicker.setCurrentDate(mDate.getTimeInMillis());
        setButton(context.getString(R.string.milletnote_datetime_ok), this);
        setButton2(context.getString(R.string.milletnote_datetime_cancel), (OnClickListener)null);
        set24HourView(DateFormat.is24HourFormat(this.getContext()));
        updateTitle(mDate.getTimeInMillis());
    }

    public void set24HourView(boolean is24HourView) 
    {
        mIs24HourView = is24HourView;
    }

    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) 
    {
        mOnDateTimeSetListener = onDateTimeSetListener;
    }

    @SuppressWarnings("deprecation")
	private void updateTitle(long date) 
    {
        int flag = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME;
        flag |= mIs24HourView ? DateUtils.FORMAT_24HOUR : DateUtils.FORMAT_24HOUR;
        setTitle(DateUtils.formatDateTime(this.getContext(), date, flag));
    }

    public void onClick(DialogInterface arg0, int arg1) 
    {
        if (mOnDateTimeSetListener != null) 
        {
            mOnDateTimeSetListener.OnDateTimeSet(this, mDate.getTimeInMillis());
        }
    }
}
