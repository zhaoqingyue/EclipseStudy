package com.zhaoqy.app.demo.notebook.windnotes.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.windnotes.util.CommonUtil;

public class WindWelcomeActivity extends Activity 
{
	private Context mContext;
	private LinearLayout welcome;		
	private TextView quoteTxt;			
	private int color;

	private SharedPreferences sp;
	private Dialog keyDialog;
	private EditText keyTxt;
	private Boolean needKey=true;		
	private SQLiteDatabase wn;
	private Handler welcomeHand;		
	private Runnable welcomeShow;
	
	private String quote;
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_windnote_welcome);
	    mContext = this;
	    
	    wn = CommonUtil.getDatabase(mContext, R.raw.windnote);
	    sp = getSharedPreferences("setting", 0);
	    String content = getResources().getString(R.string.windnote_welcome);		
	    String author = getResources().getString(R.string.windnote_name);			
	    String type = getResources().getString(R.string.windnote_name);			
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    if(!sp.getString("today","2012-12-21").equals(sdf.format(new Date())))		
	    {
		    Cursor cursor=wn.rawQuery("select * from quotes order by q_count,id limit 1", null);
		    if(cursor.moveToFirst())
		    {
		    	content=cursor.getString(cursor.getColumnIndex("q_content"));
		    	author=cursor.getString(cursor.getColumnIndex("q_author"));
		    	type=cursor.getString(cursor.getColumnIndex("q_type"));
				sp.edit().putString("q_content",content).commit();
				sp.edit().putString("q_author", author).commit();
				sp.edit().putString("q_type", type).commit();
		    	quote=content;
		    	int id=cursor.getInt(cursor.getColumnIndex("id"));
		    	wn.execSQL("update quotes set q_count=q_count+1 where id="+id);
		    	sp.edit().putString("today", sdf.format(new Date())).commit();
		    }
		    cursor.close();
	    }
	    else
	    {
	    	content=sp.getString("q_content", "");
	    	author=sp.getString("q_author", "");
	    	type=sp.getString("q_type", "");
	    	quote=content;
	    }
	    
	    color=sp.getInt("color", getResources().getColor(R.color.windnote_blue));
		welcome=(LinearLayout)findViewById(R.id.welcome);
		welcome.setBackgroundColor(color);
		welcome.setOnClickListener(new OnClickListener(){		
			@Override
			public void onClick(View arg0) {
				welcome();
	        	welcomeHand.removeCallbacks(welcomeShow);
			}
		});
		quoteTxt=(TextView)findViewById(R.id.quote_txt);
		quoteTxt.setTextColor(color);
		quoteTxt.setText(content+"\r\n\r\nby "+author);
        
		welcomeHand = new Handler();
		welcomeShow=new Runnable()
	    {
	        @Override
	        public void run()
	        {  
	        	welcome();
	        }
	    };
		welcomeHand.postDelayed(welcomeShow, (quote.length()+7)*100); 
	}
	private void welcome(){		
		Intent data=getIntent();
    	needKey=data.getBooleanExtra("needKey", true);
    	if(needKey&&sp.contains("key"))
    		enterKey();
       	else
    	{
            Intent intent = new Intent(mContext, WindMainActivity.class);
            startActivity(intent);
            finish();
    	}
	}

	private void enterKey()			
	{
		View keyView = View.inflate(mContext, R.layout.view_windnote_pwd, null);
		keyDialog = new Dialog(mContext, R.style.wind_dialog);
		keyDialog.setContentView(keyView);
		keyTxt = (EditText)keyView.findViewById(R.id.key_old);
		keyTxt.addTextChangedListener(change);
		keyDialog.show();
	}
	
	public TextWatcher change = new TextWatcher() 
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) 
		{
			String okey = keyTxt.getText().toString();
			String rkey = sp.getString("key", "");
			if(okey.equals(rkey))
			{
				needKey = false;
				keyDialog.dismiss();
	            Intent intent = new Intent(mContext, WindMainActivity.class);
	            startActivity(intent);
	            finish();
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) 
		{
		}
		
		@Override
		public void afterTextChanged(Editable s) 
		{
		}
	};
}
