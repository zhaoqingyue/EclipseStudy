package com.zhaoqy.app.demo.notebook.windnotes.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.windnotes.dialog.WindDialog;
import com.zhaoqy.app.demo.notebook.windnotes.dialog.WindDialog.OnColorChangedListener;
import com.zhaoqy.app.demo.notebook.windnotes.util.CommonUtil;
import com.zhaoqy.app.demo.notebook.windnotes.util.ConstantUtil;

public class WindMainActivity extends Activity 
{
	private Context mContext;
	
	private Dialog menuDialog;	
	private Dialog delDialog;		
	private GridView menuGrid;		
	private View menuView;			
	
	private ImageButton addBtn;		
	private ImageButton menuBtn;	
	private ImageButton searchBtn;	

	
	private ListView notesLis;		
	private GridView notesGrd;		
	private TextView titleTxt;		
	private LinearLayout main;		
	private EditText keyTxt;		
	private EditText againTxt;		
	private EditText newTxt;		
	private EditText searchTxt;		
	private TextView refreshTxt;	
	
	private Integer s_id;			
		
	private int color;				
	private String q_content;		
	private String q_author;		
	private String q_type;			
	private HashMap<Integer,Integer> idMap;		
	
	final int ACTION_SKIN=0;	
	final int ACTION_KEY=1;
	final int ACTION_SAY=2;
	final int ACTION_HELP=3;
	final int ACTION_ABOUT=4;
	final int ACTION_EXIT=5;
	private float mx;		
	private float my;
	
	private WindDialog cpDialog;		
	private SharedPreferences sp;			
	private Dialog keyDialog;				
	private SQLiteDatabase wn;				
	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_windnote_main);
		mContext = this;
		
		wn=CommonUtil.getDatabase(mContext, R.raw.windnote);
		sp = getSharedPreferences("setting", 0);
		idMap=new HashMap<Integer, Integer>();		
        color=sp.getInt("color", getResources().getColor(R.color.windnote_blue));
		main=(LinearLayout)findViewById(R.id.main);
		main.setBackgroundColor(color);
		
		titleTxt=(TextView)findViewById(R.id.title_main);
		addBtn=(ImageButton)findViewById(R.id.add_btn);
		menuBtn=(ImageButton)findViewById(R.id.menu_btn);
		searchBtn=(ImageButton)findViewById(R.id.search_btn);
	
		notesLis=(ListView)findViewById(R.id.notes_lis);
		notesLis.setVerticalScrollBarEnabled(true);
		notesGrd=(GridView)findViewById(R.id.notes_grd);
		notesGrd.setVerticalScrollBarEnabled(true);
		@SuppressWarnings("deprecation")
		int width=getWindowManager().getDefaultDisplay().getWidth();	
		notesGrd.setNumColumns(width/120);			
		
		q_content=sp.getString("q_content", "");
		q_author=sp.getString("q_author", "");
		q_type=sp.getString("q_type", "");
		
		ImageButton[] btns={addBtn,menuBtn,searchBtn};
		for(ImageButton btn:btns)
			btn.setOnClickListener(click);
		
	
		
		menuDialog = new Dialog(mContext, R.style.wind_dialog);		
		menuView = View.inflate(mContext, R.layout.view_wind_menu, null);
		menuDialog.setContentView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) 
			{
				if (keyCode == KeyEvent.KEYCODE_MENU)
				dialog.dismiss();
				return false;
			}
		});
		menuGrid=(GridView)menuView.findViewById(R.id.grid);
		menuGrid.setAdapter(getMenuAdapter());	
		menuGrid.setOnItemClickListener(itemClick);
		searchTxt=(EditText)findViewById(R.id.search_txt);
		searchTxt.setBackgroundColor(color);
		searchTxt.addTextChangedListener(search);
		searchTxt.setText(sp.getString("word", ""));
		titleTxt.setOnClickListener(click);
		refreshTxt=(TextView)findViewById(R.id.refresh_txt);
		
		Long lastdate=sp.getLong("lastdate", new Date().getTime());		
		long passday=(int)(new Date().getTime()-lastdate)/(3600000*24);
		wn.execSQL("update notes set n_time=n_time-"+passday+" where n_time>0");
		sp.edit().putLong("lastdate",new Date().getTime()).commit();
		showItem();
	}
	
	public OnTouchListener touch = new OnTouchListener()
	{		
		@Override
		public boolean onTouch(View view, MotionEvent e) 
		{
			float x = e.getX();
			float y = e.getY();
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				mx=x;
				my=y;
				break;
			case MotionEvent.ACTION_UP:
				float dx = x-mx;
				float dy = y-my;
				if(dy>30 && dx<30)
				{			
					refreshTxt.setVisibility(View.VISIBLE);
					showItem();
					Handler refreshHand = new Handler();
					Runnable refreshShow=new Runnable()		
				    {
				        @Override
				        public void run()
				        {  
				        	refreshTxt.setVisibility(View.GONE);
				        }
				    };
					refreshHand.postDelayed(refreshShow, 500);
				}
			}
			return false;
		}
	};
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{			
		float x = e.getX();
		float y = e.getY();
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			mx=x;
			my=y;
			break;
		case MotionEvent.ACTION_UP:
			float dx = x-mx;
			float dy = y-my;
			if(dy>30 && dx<30)
			{
				refreshTxt.setVisibility(View.VISIBLE);
				showItem();
				Handler refreshHand = new Handler();
				Runnable refreshShow=new Runnable()
			    {
			        @Override
			        public void run()
			        {  
			        	refreshTxt.setVisibility(View.GONE);
			        }
			    };
				refreshHand.postDelayed(refreshShow, 500);
			}
		}
		return true;
	}

	public TextWatcher change = new TextWatcher() 
	{		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) 
		{
			String key=keyTxt.getText().toString();
			String again=againTxt.getText().toString();
			if(key.length()>=6 && key.equals(again))
			{
				sp.edit().putString("key", key).commit();
				Toast.makeText(mContext, getResources().getString(R.string.windnote_key_success) + key, Toast.LENGTH_LONG).show();
				keyDialog.dismiss();
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
	
	public TextWatcher change2 = new TextWatcher() 
	{		
		@Override	
		public void onTextChanged(CharSequence s, int start, int before, int count) 
		{
			String old=keyTxt.getText().toString();
			String key=newTxt.getText().toString();
			String keyAgain=againTxt.getText().toString();
			String rkey=sp.getString("key", "");
			if(old.equals(rkey)&&key.length()>=6&&key.equals(keyAgain))
			{
				sp.edit().putString("key", key).commit();
				Toast.makeText(mContext, getResources().getString(R.string.windnote_key_success)+key,Toast.LENGTH_LONG).show();
				keyDialog.dismiss();
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
	
	public TextWatcher change3 = new TextWatcher() 
	{		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) 
		{
			String old=keyTxt.getText().toString();
			String rkey=sp.getString("key", "");
			if(old.equals(rkey))
			{
				sp.edit().remove("key").commit();
				Toast.makeText(mContext, R.string.windnote_key_canceled,Toast.LENGTH_SHORT).show();
				keyDialog.dismiss();
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
	
	private void setKey()
	{			
		keyDialog=new Dialog(mContext, R.style.wind_dialog);
		View keyView = View.inflate(mContext, R.layout.view_windnote_set_key, null);
		keyDialog.setContentView(keyView);
		keyTxt=(EditText)keyView.findViewById(R.id.key_txt);
		againTxt=(EditText)keyView.findViewById(R.id.again_txt);
		keyTxt.addTextChangedListener(change);
		againTxt.addTextChangedListener(change);
		keyDialog.show();
	}
	
	private void editKey(){		
		View keyView = View.inflate(mContext, R.layout.view_windnote_edit_key, null);
		final Dialog dialog=new Dialog(mContext,R.style.wind_dialog);
		dialog.setContentView(keyView);
		Button resetBtn=(Button)keyView.findViewById(R.id.reset_key);
		Button cancelBtn=(Button)keyView.findViewById(R.id.cancel_key);
		resetBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				resetKey();
				dialog.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				cancelKey();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private void resetKey()
	{	
		keyDialog=new Dialog(mContext,R.style.wind_dialog);
		View keyView = View.inflate(mContext, R.layout.view_windnote_reset_key, null);
		keyDialog.setContentView(keyView);
		keyTxt=(EditText)keyView.findViewById(R.id.key_old);
		newTxt=(EditText)keyView.findViewById(R.id.key_new);
		againTxt=(EditText)keyView.findViewById(R.id.key_new_again);
		keyTxt.addTextChangedListener(change2);
		keyTxt.setOnFocusChangeListener(focusChange);
		newTxt.addTextChangedListener(change2);		
		againTxt.addTextChangedListener(change2);
		keyDialog.show();
	}
	
	private void cancelKey()		
	{
		keyDialog=new Dialog(mContext, R.style.wind_dialog);
		View keyView = View.inflate(mContext, R.layout.view_windnote_cancel_key, null);
		keyDialog.setContentView(keyView);
		keyTxt=(EditText)keyView.findViewById(R.id.key_old);
		keyTxt.addTextChangedListener(change3);
		keyDialog.show();
	}	
	
	private void showItem()
	{	
		String word=searchTxt.getText().toString().trim();
		SimpleAdapter adapter = new SimpleAdapter(mContext, getData(word), R.layout.item_windnote_note,
				new String[]{"id","title","content","time","count","lock","postdate"},
				new int[]{R.id.id, R.id.title,R.id.content,R.id.time,R.id.count,R.id.lock,R.id.postdate});
	
		notesLis.setVisibility(View.VISIBLE);
		notesGrd.setVisibility(View.GONE);
		notesLis.setAdapter(adapter);		
		notesLis.setOnItemClickListener(new OnItemClickListener(){	
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ListView listView =(ListView)parent;		
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);  
				wn.execSQL("update notes set n_count=n_count-1 where n_count>0 and id="+idMap.get(position));	
				Intent intent=new Intent(mContext, WindNoteActivity.class);
				intent.putExtra("data", map);
				startActivity(intent);
				finish();
			}
		});
		notesLis.setOnTouchListener(touch);
		notesLis.setOnItemLongClickListener(longClick);			
		titleTxt.setText(getResources().getString(R.string.app_name)+"\t["+notesLis.getCount()+"]");
	}
	
	public OnFocusChangeListener focusChange=new OnFocusChangeListener()
	{		
		@Override
		public void onFocusChange(View v, boolean hasFocus) 
		{
			EditText txt=(EditText)v;
			String rkey=sp.getString("key", "");
			if(!v.hasFocus()&&!txt.getText().toString().equals(rkey)&&!txt.getText().toString().equals(""))
				Toast.makeText(mContext, R.string.windnote_key_wrong, Toast.LENGTH_SHORT).show();		
		}
	};
	
	private void chooseColor()
	{		
		Dialog dialog=new Dialog(mContext, R.style.wind_dialog);
		View colorView = View.inflate(mContext, R.layout.view_wind_menu, null);
		dialog.setContentView(colorView);
		GridView colorGrid=(GridView)colorView.findViewById(R.id.grid);
		colorGrid.setNumColumns(2);
		colorGrid.setAdapter(getColorAdapter());		
		colorGrid.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) 
			{
				if(getResources().getColor(ConstantUtil.colors[position]) != color)
				{
					if(position<ConstantUtil.colors.length-1)		
					{						
						sp.edit().putInt("color", getResources().getColor(ConstantUtil.colors[position])).commit();
						Intent intent=new Intent(mContext, WindWelcomeActivity.class);
						intent.putExtra("needKey", false);
						startActivity(intent);
						finish();
					}
					else if(position==ConstantUtil.colors.length-1)		
					{
						cpDialog = new WindDialog(mContext, color,   
		                        getResources().getString(R.string.windnote_word_confirm),   
		                        new OnColorChangedListener() { 
		                    @Override  
		                    public void colorChanged(int c) 
		                    {  
								sp.edit().putInt("color", c).commit();
								Intent intent=new Intent(mContext,WindWelcomeActivity.class);
								intent.putExtra("needKey", false);
								startActivity(intent);
								finish();
		                    }
		                });
						cpDialog.getWindow().setBackgroundDrawableResource(R.drawable.windnote_focused_shape);
		                cpDialog.show();  
					}
				}
				else
				{
					Toast.makeText(mContext, R.string.windnote_skin, Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.show();
	}
	
	private SimpleAdapter getColorAdapter()			
	{
		SimpleAdapter adapter = new SimpleAdapter(this,getColor(),R.layout.item_windnote_menu,
				new String[]{"txt"},
				new int[]{R.id.item_txt});
		return adapter;
	}
	private SimpleAdapter getMenuAdapter()			
	{
		SimpleAdapter adapter = new SimpleAdapter(this,getMenu(),R.layout.item_windnote_menu,
				new String[]{"img","txt"},
				new int[]{R.id.item_img,R.id.item_txt});
		return adapter;
	}
	private List<Map<String, Object>> getColor() {			
		String[] txts=ConstantUtil.cs;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<txts.length;i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("txt", txts[i]);
			list.add(map);
		}
		return list;
	}
	private List<Map<String, Object>> getMenu() {			
		int[] imgs={R.drawable.windnote_menu_skin,R.drawable.windnote_menu_key,R.drawable.windnote_menu_say,R.drawable.windnote_menu_help,R.drawable.windnote_menu_about,R.drawable.windnote_menu_exit};
		int[] txts={R.string.windnote_action_skin,R.string.windnote_action_key,R.string.windnote_action_say,R.string.windnote_action_help,R.string.windnote_action_about,R.string.windnote_action_exit};
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<imgs.length;i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", imgs[i]);
			map.put("txt", getResources().getString(txts[i]));
			list.add(map);
		}
		return list;
	}
	private List<Map<String, Object>> getData(String word) 
	{		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Cursor cursor=wn.rawQuery("select id,n_title,n_content,n_time,n_count,n_lock,julianday(date('now','localtime'))-julianday(date(n_postdate)) as n_postday from notes where n_time!=0 and n_count!=0 order by n_postdate "+"desc", null);
		if(word.length()>0)
			cursor=wn.rawQuery("select id,n_title,n_content,n_time,n_count,n_lock,julianday(date('now','localtime'))-julianday(date(n_postdate)) as n_postday from notes where n_time!=0 and n_count!=0 and (n_title||'`'||n_content||'`'||n_postdate||'`'||n_postday) like '%"+word+"%' order by n_postdate "+"desc", null);
		if(word.equals("#all"))
			cursor=wn.rawQuery("select id,n_title,n_content,n_time,n_count,n_lock,julianday(date('now','localtime'))-julianday(date(n_postdate)) as n_postday from notes order by n_postdate "+"desc", null);
		sp.edit().putString("word", word).commit();
		int pos=0;
		while(cursor.moveToNext())
		{
			int n_id=cursor.getInt(cursor.getColumnIndex("id"));
			idMap.put(pos, n_id);
			pos+=1;
			String n_title=cursor.getString(cursor.getColumnIndex("n_title"));
			String n_content=cursor.getString(cursor.getColumnIndex("n_content"));
			Integer n_time=cursor.getInt(cursor.getColumnIndex("n_time"));
			Integer n_count=cursor.getInt(cursor.getColumnIndex("n_count"));
			Boolean n_lock=cursor.getInt(cursor.getColumnIndex("n_lock"))>0;
			Integer n_postdate=cursor.getInt(cursor.getColumnIndex("n_postday"));
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", n_id);
			map.put("title", n_title);
			map.put("content", n_content);
			map.put("time", n_time);
			map.put("count", n_count);
			map.put("lock", n_lock);
			map.put("postdate", n_postdate==0?getResources().getString(R.string.windnote_word_today):n_postdate+getResources().getString(R.string.windnote_word_ago));
			list.add(map);
		}
		cursor.close();
		return list;
	}
	
	private OnItemClickListener itemClick=new OnItemClickListener(){			
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch(position){
			case ACTION_SKIN:
				chooseColor();
				break;
			case ACTION_KEY:
				if(!sp.contains("key"))
					setKey();
				else
					editKey();
				break;
			case ACTION_SAY:
				say();
				break;
			case ACTION_HELP:
				help();
				break;
			case ACTION_ABOUT:
				about();
				break;
			case ACTION_EXIT:
				finish();
				System.exit(0);
				break;
			}
		}
	};
	private void help()
	{		
		wn.execSQL("update notes set n_count=1,n_postdate=datetime('now','localtime') where id=1");
		showItem();	
		menuDialog.dismiss();
	}
	
	private void about()
	{		
		Dialog aboutDialog=new Dialog(mContext, R.style.wind_dialog);
		View aboutView = View.inflate(mContext, R.layout.view_windnote_about, null);
		aboutDialog.setContentView(aboutView);
		aboutDialog.show();
	}
	
	@SuppressLint("SimpleDateFormat")
	private void say(){			
		Intent intent= new Intent(mContext, WindAddActivity.class);
		Bundle data = new Bundle();
		data.putString("title",getResources().getString(R.string.windnote_word_my)+q_type+getResources().getString(R.string.windnote_action_say)+"\t\t"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		data.putString("content","        "+q_author+getResources().getString(R.string.windnote_word_said)+q_content+"\r\n");
		intent.putExtras(data);
		startActivity(intent);
		finish();
	}
	
	private void delete()
	{	
		View deleteView = View.inflate(mContext, R.layout.view_windnote_delete, null);
		delDialog=new Dialog(this,R.style.dialog);
		delDialog.setContentView(deleteView);
		Button yesBtn=(Button)deleteView.findViewById(R.id.delete_yes);
		Button noBtn=(Button)deleteView.findViewById(R.id.delete_no);
		TextView goneTimeTxt=(TextView)deleteView.findViewById(R.id.gone_time);
		TextView goneCountTxt=(TextView)deleteView.findViewById(R.id.gone_count);
		Cursor cursor=wn.rawQuery("select n_time,n_count from notes where id="+s_id,null);
		while(cursor.moveToNext())
		{
			int time=cursor.getInt(cursor.getColumnIndex("n_time"));
			int count=cursor.getInt(cursor.getColumnIndex("n_count"));
			String time_txt=time>0?String.valueOf(time):"n";
			String count_txt=count>0?String.valueOf(count):"n";
			goneTimeTxt.setText(R.string.windnote_lave);
			goneCountTxt.setText(time_txt+getResources().getString(R.string.windnote_word_time)+count_txt+getResources().getString(R.string.windnote_word_count));
		}
		yesBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				wn.execSQL("delete from notes where id="+s_id);
				delDialog.dismiss();
				showItem();
			}
		});
		noBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				delDialog.dismiss();
			}
		});
		delDialog.show();
	}
	
	private OnItemLongClickListener longClick= new OnItemLongClickListener()	
	{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
		{
			s_id=idMap.get(position);
			delete();
			return false;
		}
	};
	
	private OnClickListener click=new OnClickListener()
	{		
		@Override
		public void onClick(View v) 
		{
			switch(v.getId())
			{
			case R.id.add_btn:		
				Intent intent= new Intent(mContext, WindAddActivity.class);
				if(getIntent().hasExtra("title"))
					intent.putExtras(getIntent().getExtras());
				startActivity(intent);
				finish();
				break;
			case R.id.menu_btn:		
				if (menuDialog == null) 
				{
					menuDialog = new Dialog(mContext, R.style.wind_dialog);
					menuDialog.show();
				}
				else
				{
					menuDialog.show();
				}
				break;
			case R.id.search_btn:		
				showHide(searchTxt);
				WindAddActivity.focus(searchTxt,true);
				break;
			case R.id.title_main:		
				searchTxt.setText("");
				sp.edit().remove("word").commit();
				showItem();
			}
		}
	};
	
	private TextWatcher search=new TextWatcher()
	{		
		@Override
		public void afterTextChanged(Editable arg0) 
		{
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
		{
		}
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
		{
			showItem();
		}
	};
	
	private void showHide(View view)
	{	
		if(view.getVisibility()==View.VISIBLE)
		{
			view.setVisibility(View.INVISIBLE);
		}
		else
		{
			view.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) 
	{		
		menu.removeItem(0);
		if (menuDialog == null) 
		{
			menuDialog = new Dialog(mContext, R.style.wind_dialog);
			menuDialog.show();
		}
		else 
		{
			menuDialog.show();
		}
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem mi)
	{
		return super.onOptionsItemSelected(mi);
	}
}
