package com.zhaoqy.app.demo.notebook.windnotes.activity;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.windnotes.util.CommonUtil;

public class WindNoteActivity extends Activity 
{
	private Context mContext;
	private LinearLayout note;		
	private TextView titleTxt;		
	private EditText noteTxt;		
	
	private ImageButton backBtn;	
	private ImageButton lockBtn;	
	private ImageButton deleteBtn;	
	private ImageButton confirmBtn;	
	
	private Dialog delDialog;		
	private Integer s_id;			
	private String title;			
	private String content;			
	private Boolean lock;			
	private int color;				
	private SharedPreferences sp;
	private SQLiteDatabase wn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windnote_note); 
        mContext = this;
		
        wn=CommonUtil.getDatabase(mContext, R.raw.windnote);
        sp = getSharedPreferences("setting", 0);
        color=sp.getInt("color", getResources().getColor(R.color.blue));
        
		note=(LinearLayout)findViewById(R.id.note);
		note.setBackgroundColor(color);
		
        titleTxt=(TextView)findViewById(R.id.title_note);
        noteTxt=(EditText)findViewById(R.id.note_txt); 
        backBtn=(ImageButton)findViewById(R.id.back_btn);
        lockBtn=(ImageButton)findViewById(R.id.lock_btn);
        deleteBtn=(ImageButton)findViewById(R.id.delete_btn);
        confirmBtn=(ImageButton)findViewById(R.id.confirm_btn);
        
        Intent intent=getIntent();		
        @SuppressWarnings("unchecked")
		HashMap<String, Object> map=(HashMap<String, Object>) intent.getSerializableExtra("data");
        title=(String) map.get("title");
        content=(String) map.get("content");
        lock=(Boolean)map.get("lock");
        s_id=(Integer)map.get("id");
        titleTxt.setText(title);
        noteTxt.setText(content);

        setLock(lock);
        ImageButton[] btns={backBtn,lockBtn,deleteBtn,confirmBtn};
		for(ImageButton btn:btns)
			btn.setOnClickListener(click);
	}
	
	public void setLock(Boolean b){		
		focus(noteTxt,!b);
        lockBtn.setImageResource(b==true?R.drawable.windnote_add_unlock:R.drawable.windnote_add_lock);
        noteTxt.setTextColor(b==true?getResources().getColor(R.color.windnote_darkgray):color);
        noteTxt.setBackgroundResource(b==true?R.color.gray:R.color.white);
	}
	public void focus(EditText view,Boolean b){
		view.setCursorVisible(b);
		view.setFocusable(b);
	    view.setFocusableInTouchMode(b);
	    if(b==true)
	    	view.requestFocus();
	    else
	    	view.clearFocus();
		Spannable text = (Spannable)view.getText();
		Selection.setSelection(text, b?text.length():0);
	}
	private OnClickListener click=new OnClickListener(){
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.back_btn:
				finish();
				break;
			case R.id.lock_btn:
				lock=!lock;
				setLock(lock);
				break;
			case R.id.delete_btn:
				delete();
				break;
			case R.id.confirm_btn:
				save();
				break;
			}
		}
		
	};
	
	private void delete(){		
		View deleteView = View.inflate(this, R.layout.view_windnote_delete, null);
		delDialog=new Dialog(this,R.style.dialog);
		delDialog.setContentView(deleteView);
		Button yesBtn=(Button)deleteView.findViewById(R.id.delete_yes);
		Button noBtn=(Button)deleteView.findViewById(R.id.delete_no);
		TextView goneTimeTxt=(TextView)deleteView.findViewById(R.id.gone_time);
		TextView goneCountTxt=(TextView)deleteView.findViewById(R.id.gone_count);
		Cursor cursor=wn.rawQuery("select n_time,n_count from notes where id="+s_id,null);
		while(cursor.moveToNext()){
			int time=cursor.getInt(cursor.getColumnIndex("n_time"));
			int count=cursor.getInt(cursor.getColumnIndex("n_count"));
			String time_txt=time>0?String.valueOf(time):"n";
			String count_txt=count>0?String.valueOf(count):"n";
			goneTimeTxt.setText(R.string.windnote_note_left);
			goneCountTxt.setText(time_txt+getResources().getString(R.string.windnote_note_time)+count_txt+getResources().getString(R.string.windnote_note_count));
		}
		yesBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				wn.execSQL("delete from notes where id="+s_id);
				Toast.makeText(mContext, R.string.windnote_note_deleted, Toast.LENGTH_SHORT).show();
				delDialog.dismiss();
				Intent intent=new Intent(mContext, WindMainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		noBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				delDialog.dismiss();
			}
		});
		delDialog.show();
	}
	private void save()			
	{
		String n_content=noteTxt.getText().toString().trim();
		Boolean n_lock=lock;
		if(n_content.trim().length()>0)
		{
			wn.execSQL("update notes set n_content=?,n_lock=? where id=?",new Object[]{n_content,n_lock,s_id});
			if(!n_content.equals(content))
			{
				Toast.makeText(mContext, R.string.windnote_note_saved, Toast.LENGTH_SHORT).show();
			}
			Intent intent=new Intent(mContext, WindMainActivity.class);
			startActivity(intent);
			finish();
		}
		else
			Toast.makeText(mContext, R.string.windnote_note_null, Toast.LENGTH_SHORT).show();
	}
}
