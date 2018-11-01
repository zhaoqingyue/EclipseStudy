package com.zhaoqy.app.demo.progress.square.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.progress.square.util.CalculationUtil;
import com.zhaoqy.app.demo.progress.square.util.PercentStyle;

public class SquareProgressBar extends RelativeLayout
{
	private SquareProgressView bar;
	private ImageView imageView;
	private boolean opacity = false;
	private boolean greyscale;
	private boolean isFadingOnProgress = false;
	
	public SquareProgressBar(Context context) 
	{
		super(context);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.view_progress_squareprogressview, this, true);
		bar = (SquareProgressView) findViewById(R.id.squareProgressBar1);
        imageView = (ImageView) findViewById(R.id.imageView1);
		bar.bringToFront();
	}
	
	public SquareProgressBar(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.view_progress_squareprogressview, this, true);
		bar = (SquareProgressView) findViewById(R.id.squareProgressBar1);
        imageView = (ImageView) findViewById(R.id.imageView1);
		bar.bringToFront();
	}

	public SquareProgressBar(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.view_progress_squareprogressview, this, true);
		bar = (SquareProgressView) findViewById(R.id.squareProgressBar1);
        imageView = (ImageView) findViewById(R.id.imageView1);
		bar.bringToFront();
	}

	/**
	 * @param image the image as a ressourceId
	 */
	public void setImage(int image) 
	{
		imageView.setImageResource(image);

	}

	/**
	 * @param scale the image ScaleType
	 */
	public void setImageScaleType(ScaleType scale) 
	{
		imageView.setScaleType(scale);
	}

	/**
	 * @param progress the progress
	 */
	public void setProgress(double progress) 
	{
		bar.setProgress(progress);
		if (opacity) 
		{
			if (isFadingOnProgress) 
			{
				setOpacity(100 - (int) progress);
			} 
			else 
			{
				setOpacity((int) progress);
			}
		} 
		else 
		{
			setOpacity(100);
		}
	}

	/**
	 * Sets the colour of the {@link SquareProgressBar} to a predefined android holo color. <br/>
	 */
	public void setHoloColor(int androidHoloColor) 
	{
		bar.setColor(getContext().getResources().getColor(androidHoloColor));
	}

	/**
	 * Sets the colour of the {@link SquareProgressBar}. YOu can give it a hex-color string like <i>#C9C9C9</i>.
	 * @param colorString the colour of the {SquareProgressBar}
	 */
	public void setColor(String colorString) 
	{
		bar.setColor(Color.parseColor(colorString));
	}

	/**
	 * This sets the colour of the {@link SquareProgressBar} with a RGB colour.
	 */
	public void setColorRGB(int r, int g, int b) 
	{
		bar.setColor(Color.rgb(r, g, b));
	}

	/**
	 * This sets the colour of the {SquareProgressBar} with a RGB colour. Works when used with <code>android.graphics.Color.rgb(int, int, int)</code>
	 */
	public void setColorRGB(int rgb) 
	{
		bar.setColor(rgb);
	}

	/**
	 * This sets the width of the {@link SquareProgressBar}.
	 * @param width in Dp
	 */
	public void setWidth(int width) 
	{
		int padding = CalculationUtil.convertDpToPx(width, getContext());
		imageView.setPadding(padding, padding, padding, padding);
		bar.setWidthInDp(width);
	}

	/**
	 * @param progress the progress
	 */
	@SuppressWarnings("deprecation")
	private void setOpacity(int progress) 
	{
		imageView.setAlpha((int) (2.55 * progress));
	}

	/**
	 * @param opacity true if opacity should be enabled.
	 */
	public void setOpacity(boolean opacity) 
	{
		this.opacity = opacity;
		setProgress(bar.getProgress());
	}

	/**
	 * @param opacity true if opacity should be enabled.
	 * @param isFadingOnProgress default false. This changes the behavior the opacity works. If the progress increases then the images fades. When the
	 * progress reaches 100, then the image disappears.
	 */
	public void setOpacity(boolean opacity, boolean isFadingOnProgress) 
	{
		this.opacity = opacity;
		this.isFadingOnProgress = isFadingOnProgress;
		setProgress(bar.getProgress());
	}

	/**
	 * You can set the image to b/w with this method. Works fine with the opacity.
	 * @param greyscale true if the grayscale should be activated.
	 */
	public void setImageGrayscale(boolean greyscale) 
	{
		this.greyscale = greyscale;
		if (greyscale) 
		{
			ColorMatrix matrix = new ColorMatrix();
			matrix.setSaturation(0);
			imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
		} 
		else 
		{
			imageView.setColorFilter(null);
		}
	}

	/**
	 * If opacity is enabled.
	 * @return true if opacity is enabled.
	 */
	public boolean isOpacity() 
	{
		return opacity;
	}

	/**
	 * If greyscale is enabled.
	 * @return true if greyscale is enabled.
	 */
	public boolean isGreyscale() 
	{
		return greyscale;
	}

	/**
	 * Draws an outline of the progressbar. Looks quite cool in some situations.
	 * @param drawOutline true if it should or not.
	 * @since 1.3.0
	 */
	public void drawOutline(boolean drawOutline) 
	{
		bar.setOutline(drawOutline);
	}

	/**
	 * If outline is enabled or not.
	 * @return true if outline is enabled.
	 */
	public boolean isOutline() 
	{
		return bar.isOutline();
	}

	/**
	 * Draws the startline. this is the line where the progressbar starts the drawing around the image.
	 * @param drawStartline true if it should or not.
	 * @since 1.3.0
	 */
	public void drawStartline(boolean drawStartline) 
	{
		bar.setStartline(drawStartline);
	}

	/**
	 * If the startline is enabled.
	 * @return true if startline is enabled or not.
	 */
	public boolean isStartline() 
	{
		return bar.isStartline();
	}

	/**
	 * Defines if the percent text should be shown or not. To modify the text
	 * checkout {@link #setPercentStyle(ch.halcyon.squareprogressbar.utils.PercentStyle)}.
	 * @param showProgress true if it should or not.
	 */
	public void showProgress(boolean showProgress) 
	{
		bar.setShowProgress(showProgress);
	}

	/**
	 * If the progress text inside of the image is enabled.
	 * @return true if it is or not.
	 */
	public boolean isShowProgress() 
	{
		return bar.isShowProgress();
	}

	/**
	 * Sets a custom percent style to the text inside the image. Make sure you set {@link #showProgress(boolean)} to true. Otherwise it doesn't shows.
	 * The default settings are:</br>
	 * @param percentStyle
	 */
	public void setPercentStyle(PercentStyle percentStyle) 
	{
		bar.setPercentStyle(percentStyle);
	}

	/**
	 * Returns the {@link PercentStyle} of the percent text. Maybe returns the default value, check {@link #setPercentStyle(PercentStyle)} fo that.
	 * @return the percent style of the moment.
	 */
	public PercentStyle getPercentStyle() 
	{
		return bar.getPercentStyle();
	}

	/**
	 * If the progress hits 100% then the progressbar disappears if this flag is set to <code>true</code>. The default is set to false.
	 * @param removeOnFinished if it should disappear or not.
	 */
	public void setClearOnHundred(boolean clearOnHundred) 
	{
		bar.setClearOnHundred(clearOnHundred);
	}
	
	/**
	 * If the progressbar disappears when the progress reaches 100%.
	 */
	public boolean isClearOnHundred() 
	{
		return bar.isClearOnHundred();
	}

    /**
     * Set an image resource directly to the ImageView.
     * @param bitmap the {android.graphics.Bitmap} to set.
     */
    public void setImageBitmap(Bitmap bitmap)
    {
        imageView.setImageBitmap(bitmap);
    }

    /**
     * @param indeterminate true to enable the indeterminate mode (default true)
     */
    public void setIndeterminate(boolean indeterminate) 
    {
        bar.setIndeterminate(indeterminate);
    }

    /**
     * Returns the status of the indeterminate mode. The default status is false.
     */
    public boolean isIndeterminate() 
    {
        return bar.isIndeterminate();
    }

    /**
     * Draws a line in the center of the way the progressbar has to go.
     * @param drawCenterline true if it should or not.
     */
    public void drawCenterline(boolean drawCenterline) 
    {
        bar.setCenterline(drawCenterline);
    }

    /**
     * If the centerline is enabled or not.
     * @return true if centerline is enabled.
     */
    public boolean isCenterline() 
    {
        return bar.isCenterline();
    }

	/**
	 * Returns the {ImageView} that the progress gets drawn around.
     * @return the main ImageView
	 */
	public ImageView getImageView()
	{
		return imageView;
	}
}
