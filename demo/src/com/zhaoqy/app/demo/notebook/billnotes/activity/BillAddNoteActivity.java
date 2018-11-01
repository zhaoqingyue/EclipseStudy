package com.zhaoqy.app.demo.notebook.billnotes.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.db.UserManager;
import com.zhaoqy.app.demo.notebook.billnotes.item.NoteItem;

public class BillAddNoteActivity extends Activity implements OnClickListener
{
	private Context     mContext;
	private Button      mSave;
	private Button      mUndo;
	private Button      mTime;
	private EditText    mTitle;
	private EditText    mContent;
	private UserManager mManager;
	private String      mDate;
	private int         mType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billnotes_add_note);
		mContext = this;
		
		initView();
		setListener();
	}

	private void initView() 
	{
		mTitle = (EditText) findViewById(R.id.edit_title);
		mTitle.requestFocus();
		mContent = (EditText) findViewById(R.id.edit_context);
		mSave = (Button) findViewById(R.id.save_btn);
		mUndo = (Button) findViewById(R.id.undo_btn);
		mTime = (Button) findViewById(R.id.addtime_btn);
	}
	
	private void setListener() 
	{
		mSave.setOnClickListener(this);
		mUndo.setOnClickListener(this);
		mTime.setOnClickListener(this);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.save_btn:
		{
			if (mTitle.getText().toString().trim().isEmpty()) 
			{
				Toast.makeText(mContext, "请添加标题", Toast.LENGTH_SHORT).show();
			}
			else if (mContent.getText().toString().trim().isEmpty()) 
			{
				Toast.makeText(mContext, "请添加内容", Toast.LENGTH_SHORT).show();
			}
			else
			{
				mManager = new UserManager(mContext);
				insertNote(mManager);
				finish();
			}
			break;
		}
		case R.id.undo_btn:
		{
			if (!mTitle.getText().toString().trim().isEmpty()) 
			{
				mType = 1;
			}
			if (!mContent.getText().toString().trim().isEmpty()) 
			{
				mType = 2;
			}
			switch (mType) 
			{
			case 1:
			{
				String sub1 = cancalInto(mTitle.getText().toString().trim());
				mTitle.setText(sub1);
				mTitle.setSelection(mTitle.getText().length());
				break;
			}
			case 2:
			{
				String sub2=cancalInto(mContent.getText().toString().trim());
				mContent.setText(sub2);
				mContent.setSelection(mContent.getText().length());
				break;
			}
			default:
				break;
			}
			break;
		}
		case R.id.addtime_btn:
		{
			mDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String etext = mContent.getText().toString().trim();
			mContent.setText(etext + mDate);
			break;
		}
		default:
			break;
		}
	}

	public  void insertNote(UserManager service) 
	{
		NoteItem ete = new NoteItem(mTitle.getText().toString(), mContent.getText().toString(), mDate);
		service.insertNote(ete);
	}

	private String cancalInto(String intext) 
	{
		String subedtext = intext.substring(0, intext.length() - 1);
		return subedtext;
	}
}
