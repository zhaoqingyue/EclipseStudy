package com.zhaoqy.app.demo.progress.square;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.progress.square.dialog.ColourDialog;
import com.zhaoqy.app.demo.progress.square.dialog.PercentDialog;
import com.zhaoqy.app.demo.progress.square.fragment.SquareFragment;
import com.zhaoqy.app.demo.progress.square.util.ColorUtil;

public class SquareProgressActivity extends Activity 
{
	private Context mContext;
	
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence drawerTitle;
    private CharSequence title;
    public static String[] partTitle, descriptions;
    private SquareFragment squareFragment;

    private int lastPosition;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        FragmentManager fragmentManager = getFragmentManager();
        squareFragment = new SquareFragment();
        fragmentManager.beginTransaction().replace(R.id.content_frame, squareFragment).commit();
        
        setContentView(R.layout.activity_progress_square);
        mContext = this;
        
        title = drawerTitle = getTitle();
        partTitle = getResources().getStringArray(R.array.drawer_titel);
        descriptions = getResources().getStringArray(R.array.drawer_descriptions);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerLayout.setDrawerShadow(R.drawable.progress_square_drawer_shadow, GravityCompat.START);
        drawerListView.setAdapter(adapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.progress_square_drawer, R.string.drawer_open, R.string.drawer_close) 
        {
            @Override
            public void onDrawerClosed(View view) 
            {
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) 
            {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
	}
	
	BaseAdapter adapter = new BaseAdapter() 
	{
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) 
        {
            View item = convertView;

            //Header Item
            View headerItem = LayoutInflater.from(mContext).inflate(R.layout.view_progress_square_list_header, parent, false);
            TextView title = (TextView) headerItem.findViewById(R.id.id_square_header);

            //Custom Style Item
            View styleItem = LayoutInflater.from(mContext).inflate(R.layout.view_progress_square_list_style, parent, false);
            CheckBox box = (CheckBox) styleItem.findViewById(R.id.id_square_checkbox);

            //Custom Style Item
            View styleBoxItem = LayoutInflater.from(mContext).inflate(R.layout.view_progress_square_list_style_box, parent, false);
            final CheckBox styleBox = (CheckBox) styleBoxItem.findViewById(R.id.id_square_checkbox);
            ImageView styleImage = (ImageView) styleBoxItem.findViewById(R.id.id_square_image);

            //Link to Github Item
            View githubItem = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_progress_square_list_github, parent, false);
            TextView githublink = (TextView) githubItem.findViewById(R.id.id_square_github);

            //Link to homepage Item
            View signerItem = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_progress_square_list_signer, parent, false);

            //Link to Image Item
            View imageItem = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_progress_square_list_image, parent, false);
            ImageView imagePreview = (ImageView) imageItem.findViewById(R.id.id_square_image);
            TextView imageDesc = (TextView) imageItem.findViewById(R.id.id_square_image_tag);

            switch (position) 
            {
			case 0:
			{
				title.setText("Colour");
				return headerItem;
			}
			case 11:
			{
				Context context = getApplicationContext();
				item = LayoutInflater.from(context).inflate(R.layout.view_progress_square_list_color, parent, false);
				item.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						final ColourDialog customColourDialog = new ColourDialog(mContext);
						customColourDialog.show();
						customColourDialog.getSaveButton().setOnClickListener(new OnClickListener() 
						{
							@Override
							public void onClick(View v) 
							{
								squareFragment.squareProgressBar.setColorRGB(customColourDialog.getChoosenRGB());
								customColourDialog.dismiss();
							}
						});
						selectItem(position);
						lastPosition = position;
					}
				});
				TextView textView = (TextView) item.findViewById(R.id.colour_name_center);
				textView.setText("choose RGB colour");
				return item;
			}
			case 12:
			{
				title.setText("Style");
				return headerItem;
			}
			case 13:
			{
				box.setText("opacity");
				box.setChecked(squareFragment.squareProgressBar.isOpacity());
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						squareFragment.squareProgressBar.setOpacity(isChecked);
					}
				});
				return styleItem;
			}
			case 14:
			{
				box.setText("Outline");
				box.setChecked(squareFragment.squareProgressBar.isOutline());
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						squareFragment.squareProgressBar.drawOutline(isChecked);
					}
				});
				return styleItem;
			}
			case 15:
			{
				box.setText("Startline");
				box.setChecked(squareFragment.squareProgressBar.isStartline());
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						squareFragment.squareProgressBar.drawStartline(isChecked);
					}
				});
				return styleItem;
			}
			case 16:
			{
				box.setText("Centerline");
				box.setChecked(squareFragment.squareProgressBar.isCenterline());
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						squareFragment.squareProgressBar.drawCenterline(isChecked);
					}
				});
				return styleItem;
			}
			case 17:
			{
				styleBox.setText("Show percent");
				styleBox.setChecked(squareFragment.squareProgressBar.isShowProgress());
				styleBox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						squareFragment.squareProgressBar.showProgress(isChecked);
					}
				});
				styleImage.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						final PercentDialog percentDialog = new PercentDialog(mContext);
						percentDialog.show();
						percentDialog.setPercentStyle(squareFragment.squareProgressBar.getPercentStyle());
						percentDialog.getSaveButton().setOnClickListener(new OnClickListener() 
						{
							@Override
							public void onClick(View v) 
							{
								squareFragment.squareProgressBar.setPercentStyle(percentDialog.getSettings());
								percentDialog.dismiss();
								styleBox.setChecked(true);
								drawerLayout.closeDrawers();
							}
						});
					}
				});
				return styleBoxItem;
			}
			case 18:
			{
				box.setText("Grayscale");
				box.setChecked(squareFragment.squareProgressBar.isGreyscale());
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						squareFragment.squareProgressBar.setImageGrayscale(isChecked);
					}
				});
				return styleItem;
			}
			case 19:
			{
				box.setText("Clear at 100%");
				box.setChecked(squareFragment.squareProgressBar.isClearOnHundred());
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						squareFragment.squareProgressBar.setClearOnHundred(isChecked);
					}
				});
				return styleItem;
			}
			case 20:
			{
				box.setText("Indeterminate");
				box.setChecked(squareFragment.squareProgressBar.isIndeterminate());
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						squareFragment.squareProgressBar.setIndeterminate(isChecked);
					}
				});
				return styleItem;
			}
			case 21:
			{
				title.setText("Image");
				return headerItem;
			}
			case 22:
			{
				imagePreview.setImageResource(R.drawable.progress_square_palace);
				imageDesc.setText("blenheim palace");
				imageItem.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						squareFragment.squareProgressBar.setImage(R.drawable.progress_square_palace);
					}
				});
				return imageItem;
			}
			case 23:
			{
				imagePreview.setImageResource(R.drawable.progress_square_stadium);
				imageDesc.setText("the millennium stadium");
				imageItem.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						squareFragment.squareProgressBar.setImage(R.drawable.progress_square_stadium);
					}
				});
				return imageItem;
			}
			case 24:
			{
				imagePreview.setImageResource(R.drawable.progress_square_edinburgh);
				imageDesc.setText("carlton hill");
				imageItem.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						squareFragment.squareProgressBar.setImage(R.drawable.progress_square_edinburgh);
					}
				});
				return imageItem;
			}
			case 25:
			{
				imagePreview.setImageResource(R.drawable.progress_square_holyroodpark);
				imageDesc.setText("holyrood park");
				imageItem.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						squareFragment.squareProgressBar.setImage(R.drawable.progress_square_holyroodpark);
					}
				});
				return imageItem;
			}
			case 26:
			{
				imagePreview.setImageResource(R.drawable.progress_square_operahuset);
				imageDesc.setText("operahuset oslo");
				imageItem.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						squareFragment.squareProgressBar.setImage(R.drawable.progress_square_operahuset);
					}
				});
				return imageItem;
			}
			case 27:
			{
				title.setText("Source");
				return headerItem;
			}
			case 28:
			{
				String text = "<font color=#4183C4>mrwonderman</font>/<b><font color=#4183C4>android-square-progressbar</font></b> (v. 1.6.0)";
				githublink.setText(Html.fromHtml(text));
				githubItem.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mrwonderman/android-square-progressbar"));
						startActivity(browserIntent);
					}
				});
				return githubItem;
			}
			case 29:
			{
				signerItem.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.halcyon.ch/"));
						startActivity(browserIntent);
					}
				});
				return signerItem;
			}
			default:
				break;
			}

			ArrayList<Integer> colourArray = ColorUtil.getColourArray();
			if (position <= 10) 
			{
				Context context = getApplicationContext();
				item = LayoutInflater.from(context).inflate(R.layout.view_progress_square_list_colour, parent, false);
				View colourView = item.findViewById(R.id.colour_preview);
				final Integer integer = colourArray.get(position - 1);
				colourView.setBackgroundColor(context.getResources().getColor(integer));
				item.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						squareFragment.squareProgressBar.setHoloColor(integer);
						selectItem(position);
						lastPosition = position;
					}
				});
				TextView textView = (TextView) item.findViewById(R.id.colour_name);
				textView.setText(getName(position - 1));
			}
			return item;
        }

        @Override
        public long getItemId(int position) 
        {
            return position;
        }

        @Override
        public Object getItem(int position) 
        {
            return position;
        }

        @Override
        public int getCount() 
        {
            return 30;
        }
    };
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        if (drawerToggle.onOptionsItemSelected(item)) 
        {
            return true;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) 
    {
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) 
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    
    public void setTitle(CharSequence title) 
    {
        this.title = title;
        getActionBar().setTitle(title);
    }
    
    private void selectItem(int position) 
    {
        drawerListView.setItemChecked(position, true);
    }
    
    private CharSequence getName(int position) 
    {
        switch (position) 
        {
            case 0:
                return "holo_blue_bright";
            case 1:
                return "holo_blue_dark";
            case 2:
                return "holo_blue_light";
            case 3:
                return "holo_green_dark";
            case 4:
                return "holo_green_light";
            case 5:
                return "holo_orange_dark";
            case 6:
                return "holo_orange_light";
            case 7:
                return "holo_purple";
            case 8:
                return "holo_red_dark";
            case 9:
                return "holo_red_light";

            default:
                break;
        }
        return "";
    }

	private class DrawerItemClickListener implements ListView.OnItemClickListener 
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			if ((position != 0) && (position != 12) && (position != 21) && (position != 27)) 
			{
				selectItem(position);
			} 
			else 
			{
				selectItem(lastPosition);
			}
		}
	}
}
