package com.zhaoqy.app.demo.index.contacts.activity;

import java.io.InputStream;
import java.util.ArrayList;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.index.contacts.util.PingYinUtil;
import com.zhaoqy.app.demo.index.contacts.view.Index;
import com.zhaoqy.app.demo.index.contacts.view.OnIndex;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsActivity extends Activity implements OnClickListener, OnItemClickListener, OnIndex
{
	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	private static final int PHONES_NUMBER_INDEX = 1;
	private static final int PHONES_PHOTO_ID_INDEX = 2;
	private static final int PHONES_CONTACT_ID_INDEX = 3;
	private ArrayList<String> mContactsName = new ArrayList<String>();
	private ArrayList<String> mContactsNumber = new ArrayList<String>();
	private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	private ImageView mBack;
	private TextView  mTop;
	private ListView  mListview;
	private TextView  mLetter;
	private Index     mIndex;
	private MyAdapter mAdapter;
	private Bitmap    mBitmap;
	private String    mTitle[];
	private int       mName;
	private int       mPhone;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_contacts);
		
		getPhoneContacts();
		initTitle();
		initView();
		setListener();
		initData();
		getbundleValue();
	}

	private void getbundleValue() 
	{
		Intent intent = getIntent();
		mName = intent.getIntExtra("add_index_name", 0);
		mPhone = intent.getIntExtra("add_index_phone", 0);
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTop = (TextView) findViewById(R.id.id_title_text);
		mListview = (ListView) findViewById(R.id.contacts_listview);
		mLetter = (TextView) findViewById(R.id.contacts_letter);
		mIndex = (Index) findViewById(R.id.contacts_index);
	}
	
	private void setListener()
	{
		mBack.setOnClickListener(this);
		mListview.setOnItemClickListener(this);
		mIndex.setOnIndex(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTop.setText(R.string.index_item0);
		
		mAdapter = new MyAdapter();
		mListview.setAdapter(mAdapter);
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_default_icon);
	}
	
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		if (mName == 1) 
		{
			Intent intent = getIntent();
			intent.putExtra("add_name", mContactsName.get(position));
			setResult(AddContactsActivity.SEND_SEARCH_NAME, intent);
			finish();
		}

		if (mPhone == 2) 
		{
			Intent intent = getIntent();
			intent.putExtra("add_phone", mContactsNumber.get(position));
			setResult(AddContactsActivity.SEND_SEARCH_PHONE, intent);
			finish();
		}
	}

	public void OnIndexSelect(String str) 
	{
		mLetter.setVisibility(View.VISIBLE);
		mLetter.setText(str);
		for (int i=0; i<mTitle.length; i++) 
		{
			if (mTitle[i].equals(str)) 
			{
				mListview.setSelection(i);
				break;
			}
		}
	}

	public void OnIndexUp() 
	{
		mLetter.setVisibility(View.GONE);
	}
	
	private void getPhoneContacts() 
	{
		ContentResolver resolver = getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) 
		{
			while (phoneCursor.moveToNext()) 
			{
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				if (TextUtils.isEmpty(phoneNumber))
				{
					continue;
				}
	
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				Bitmap contactPhoto = null;

				if (photoid > 0) 
				{
					Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
					contactPhoto = BitmapFactory.decodeStream(input);
				}
				else 
				{
					contactPhoto = mBitmap;
				}
				mContactsName.add(contactName);
				mContactsNumber.add(phoneNumber);
				mContactsPhonto.add(contactPhoto);
			}
			phoneCursor.close();
		}
		mTitle = new String[mContactsName.size()];
	}
	
	private void initTitle() 
	{
		for (int i=0; i<mContactsName.size(); i++) 
		{
			String str = PingYinUtil.converterToFirstSpell(mContactsName.get(i).substring(0, 1));
			mTitle[i] = str;
		}

		for (int i=0; i<mTitle.length; i++) 
		{
			for (int j=i+1; j<mTitle.length; j++) 
			{
				if (mTitle[i].charAt(0) > mTitle[j].charAt(0)) 
				{
					String str = mTitle[i];
					mTitle[i] = mTitle[j];
					mTitle[j] = str;
					String str2 = mContactsName.get(i);
					mContactsName.set(i, mContactsName.get(j));
					mContactsName.set(j, str2);
					String str3 = mContactsNumber.get(i);
					mContactsNumber.set(i, mContactsNumber.get(j));
					mContactsNumber.set(j, str3);
					Bitmap temp = mContactsPhonto.get(i);
					mContactsPhonto.set(i, mContactsPhonto.get(j));
					mContactsPhonto.set(j, temp);
				}
			}
		}
	}

	public class MyAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mContactsName.size();
		}

		public Object getItem(int position) 
		{
			return mContactsName.get(position);
		}

		public long getItemId(int position) 
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder viewHolder;
			if (convertView == null) 
			{
				convertView = getLayoutInflater().inflate(R.layout.item_contacts, null);
				viewHolder = new ViewHolder();
				viewHolder.mTitle = (TextView) convertView.findViewById(R.id.item_contacts_title);
				viewHolder.mName = (TextView) convertView.findViewById(R.id.item_contacts_name);
				viewHolder.mNum = (TextView) convertView.findViewById(R.id.item_contacts_number);
				viewHolder.mPhoto = (ImageView) convertView.findViewById(R.id.item_contacts_photo);
				convertView.setTag(viewHolder);
			}
			else 
			{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.mName.setText(mContactsName.get(position));
			viewHolder.mNum.setText(mContactsNumber.get(position));
			viewHolder.mPhoto.setImageBitmap(mContactsPhonto.get(position));
			String str2 = mTitle[position];
			if (position > 0) 
			{
				String str1 = mTitle[position-1];
				if (str2.equals(str1)) 
				{
					viewHolder.mTitle.setVisibility(View.GONE);
				} 
				else 
				{
					viewHolder.mTitle.setVisibility(View.VISIBLE);
					viewHolder.mTitle.setText(str2);
				}
			} 
			else 
			{
				viewHolder.mTitle.setVisibility(View.VISIBLE);
				viewHolder.mTitle.setText(str2);
			}
			return convertView;
		}

		public class ViewHolder 
		{
			TextView  mTitle;
			TextView  mName;
			TextView  mNum;
			ImageView mPhoto;
		}
	}
}
