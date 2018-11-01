package com.zhaoqy.app.demo.dialog;

import java.util.ArrayList;
import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogActivity extends Activity implements OnClickListener, Runnable
{
	final String[] mItems = { "item0", "item1", "itme2", "item3", "itme4", "item5", "item6" };
	private static final int DIALOG_0 = 0;
	private static final int DIALOG_1 = 1;
	private static final int DIALOG_2 = 2;
	private static final int DIALOG_3 = 3;
	private static final int DIALOG_4 = 4;
	private static final int DIALOG_5 = 5;
	private static final int DIALOG_6 = 6;
	private static final int DIALOG_7 = 7;
	private static final int MAX_PROGRESS = 100;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;
	private TextView  mItem3;
	private TextView  mItem4;
	private TextView  mItem5;
	private TextView  mItem6;
	private TextView  mItem7;
	private TextView  mItem8;
	private ProgressDialog mProgressDialog;
	private ArrayList<Integer> MultiChoiceID = new ArrayList<Integer>();
	private int mSingleChoiceID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_dialog_item0);
		mItem1 = (TextView) findViewById(R.id.id_dialog_item1);
		mItem2 = (TextView) findViewById(R.id.id_dialog_item2);
		mItem3 = (TextView) findViewById(R.id.id_dialog_item3);
		mItem4 = (TextView) findViewById(R.id.id_dialog_item4);
		mItem5 = (TextView) findViewById(R.id.id_dialog_item5);
		mItem6 = (TextView) findViewById(R.id.id_dialog_item6);
		mItem7 = (TextView) findViewById(R.id.id_dialog_item7);
		mItem8 = (TextView) findViewById(R.id.id_dialog_item8);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
		mItem4.setOnClickListener(this);
		mItem5.setOnClickListener(this);
		mItem6.setOnClickListener(this);
		mItem7.setOnClickListener(this);
		mItem8.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.main_item8));
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		case R.id.id_dialog_item0:
		{
			CreatDialog(DIALOG_0);
			break;
		}
		case R.id.id_dialog_item1:
		{
			CreatDialog(DIALOG_1);
			break;
		}
		case R.id.id_dialog_item2:
		{
			CreatDialog(DIALOG_2);
			break;
		}
		case R.id.id_dialog_item3:
		{
			CreatDialog(DIALOG_3);
			break;
		}
		case R.id.id_dialog_item4:
		{
			CreatDialog(DIALOG_4);
			break;
		}
		case R.id.id_dialog_item5:
		{
			CreatDialog(DIALOG_5);
			break;
		}
		case R.id.id_dialog_item6:
		{
			CreatDialog(DIALOG_6);
			break;
		}
		case R.id.id_dialog_item7:
		{
			CreatDialog(DIALOG_7);
			break;
		}
		case R.id.id_dialog_item8:
		{
			LoadingDialog dialog = new LoadingDialog(mContext);
			dialog.show();
			break;
		}
		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void CreatDialog(int id) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		switch (id) 
		{
		case DIALOG_0:
		{
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("你确定要离开吗？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					showDialog("你选择了确定");
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					showDialog("你选择了取消");
				}
			});
			break;
		}
		case DIALOG_1:
		{
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("投票");
			builder.setMessage("您认为什么样的内容能吸引您？");
			builder.setPositiveButton("有趣味的", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					showDialog("你选择了有趣味的");
				}
			});
			builder.setNeutralButton("有思想的", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					showDialog("你选择了有思想的");
				}
			});
			builder.setNegativeButton("主题强的", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					showDialog("你选择了主题强的");
				}
			});
			break;
		}
		case DIALOG_2:
		{
			builder.setTitle("列表选择框");
			builder.setItems(mItems, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					showDialog("你选择的id为" + which + " , " + mItems[which]);
				}
			});
			break;
		}
		case DIALOG_3:
		{
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setIcon(R.drawable.ic_launcher);
			mProgressDialog.setTitle("进度条窗口");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setMax(MAX_PROGRESS);
			mProgressDialog.setButton("确定", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					showDialog("你选择了确定");
				}
			});
			mProgressDialog.setButton2("取消", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					showDialog("你选择了取消");
				}
			});
			mProgressDialog.show();
			new Thread(this).start();
			return;
		}
		case DIALOG_4:
		{
			mSingleChoiceID = -1;
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("单项选择");
			builder.setSingleChoiceItems(mItems, 0, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					mSingleChoiceID = whichButton;
					showDialog("你选择的id为" + whichButton + " , " + mItems[whichButton]);
				}
			});
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					if (mSingleChoiceID > 0) 
					{
						showDialog("你选择的是" + mSingleChoiceID);
					}
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					showDialog("你选择了取消");
				}
			});
			break;
		}
		case DIALOG_5:
		{
			MultiChoiceID.clear();
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("多项选择");
			builder.setMultiChoiceItems(mItems, new boolean[] { false, false, false, false, false, false, false }, new DialogInterface.OnMultiChoiceClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) 
				{
					if (isChecked) 
					{
						MultiChoiceID.add(whichButton);
						showDialog("你选择的id为" + whichButton + " , " + mItems[whichButton]);
					} 
					else 
					{
						MultiChoiceID.remove(whichButton);
					}
				}
			});
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					String str = "";
					int size = MultiChoiceID.size();
					for (int i = 0; i < size; i++) 
					{
						str += mItems[MultiChoiceID.get(i)] + ", ";
					}
					showDialog("你选择的是" + str);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
				}
			});
			break;
		}
		case DIALOG_6:
		{
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(R.layout.dialogs_custom, null);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("自定义输入框");
			builder.setView(textEntryView);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					EditText userName = (EditText) textEntryView.findViewById(R.id.etUserName);
					EditText password = (EditText) textEntryView.findViewById(R.id.etPassWord);
					showDialog("姓名 ：" + userName.getText().toString() + "密码：" + password.getText().toString());
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
				}
			});
			break;
		}
		case DIALOG_7:
		{
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setTitle("读取ing");
			mProgressDialog.setMessage("正在读取中请稍候");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return;
		}
		default:
			break;
		}
		builder.create().show();
	}
	
	private void showDialog(String str) 
	{
		new AlertDialog.Builder(mContext).setMessage(str).show();
	}

	public void run() 
	{
		int Progress = 0;
		while (Progress < MAX_PROGRESS) 
		{
			try 
			{
				Thread.sleep(100);
				Progress++;
				mProgressDialog.incrementProgressBy(1);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
