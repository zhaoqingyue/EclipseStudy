package com.zhaoqy.app.demo.notebook.billnotes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.item.NoteItem;

public class BillLookNoteActivity extends Activity implements OnClickListener
{
	private Button   mBack;
	private TextView mTitle;
	private TextView mContent;
	private NoteItem mItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billnotes_look_note);
		
		initView();
		initData();
		setListener();
	}

	private void initView()
	{
		mBack = (Button) findViewById(R.id.id_billnotes_look_note_back);
		mTitle = (TextView) findViewById(R.id.id_billnotes_look_note_title);
		mContent = (TextView) findViewById(R.id.id_billnotes_look_note_context);
	}
	
	private void initData() 
	{
		Intent intent = getIntent();
		mItem = (NoteItem) intent.getSerializableExtra("data");
		mContent.setText(mItem.getContent());
		mTitle.setText(mItem.getTitle());
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_billnotes_look_note_back:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
}
