package com.zhaoqy.app.demo.progress.square.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class ColourDialog extends Dialog 
{
	private Button  saveButton;
	private SeekBar rSeekBar;
	private SeekBar gSeekBar;
	private SeekBar bSeekBar;
	private int     choosenRGB;

	public ColourDialog(final Context context) 
	{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_progress_square_color);
		this.setCancelable(false);

		saveButton = (Button) findViewById(R.id.shareColourDialog);
		Button closeButton = (Button) findViewById(R.id.returnColourDialog);
		closeButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dismiss();
			}
		});
		
		rSeekBar = (SeekBar) findViewById(R.id.rSeekBar);
		rSeekBar.setMax(255);
		rSeekBar.setProgress(111);
		rSeekBar.setOnSeekBarChangeListener(rgbOnSeekBarListener());

		gSeekBar = (SeekBar) findViewById(R.id.gSeekBar);
		gSeekBar.setMax(255);
		gSeekBar.setProgress(111);
		gSeekBar.setOnSeekBarChangeListener(rgbOnSeekBarListener());

		bSeekBar = (SeekBar) findViewById(R.id.bSeekBar);
		bSeekBar.setMax(255);
		bSeekBar.setProgress(111);
		bSeekBar.setOnSeekBarChangeListener(rgbOnSeekBarListener());

		calculateRGB();
	}

	private OnSeekBarChangeListener rgbOnSeekBarListener() 
	{
		return new OnSeekBarChangeListener() 
		{
			@Override
			public void onStopTrackingTouch(SeekBar arg0) 
			{
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) 
			{
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) 
			{
				calculateRGB();
			}
		};
	}

	/**
	 * Returns the save button of the dialog.
	 */
	public Button getSaveButton() 
	{
		return saveButton;
	}

	/**
	 * Calculates the current set RGB value according to the three
	 */
	private void calculateRGB() 
	{
		int r = rSeekBar.getProgress();
		int g = gSeekBar.getProgress();
		int b = bSeekBar.getProgress();
		((TextView) findViewById(R.id.rgbText)).setText("(" + r + "," + g + "," + b + ")");
		choosenRGB = Color.rgb(r, g, b);
		getWindow().setBackgroundDrawable(new ColorDrawable(choosenRGB));
	}

	/**
	 * Returns the Color which was chosen in the Dialog.
	 */
	public int getChoosenRGB() 
	{
		return choosenRGB;
	}
}
