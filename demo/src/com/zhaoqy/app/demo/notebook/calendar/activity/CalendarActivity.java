package com.zhaoqy.app.demo.notebook.calendar.activity;

import android.app.Activity;
import android.os.Bundle;
import com.zhaoqy.app.demo.notebook.calendar.view.CalendarView;

public class CalendarActivity extends Activity 
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		CalendarView calView = new CalendarView(this);
		setContentView(calView);
		calView.requestFocus();
	}

	public void showDiaryList(int year, int month, int day) 
	{
		/*Intent k = new Intent(this, DiaryList.class);
		k.putExtra("cal", new GregorianCalendar(year,month,day));
		startActivity(k);*/
	}
}
