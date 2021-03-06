package com.zhaoqy.app.demo.camera.qrcode.view;

import java.util.Collection;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.qrcode.camera.CameraManager;

public class QRCodeScanView extends View 
{
	private static final long ANIMATION_DELAY = 100L;
	private static final int OPAQUE = 0xFF;
	private static final int SPEEN_DISTANCE = 5;

	private final Paint paint;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;
	private Bitmap resultBitmap;
	private Bitmap qrLineBitmap;// 扫描的分割线
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int resultPointColor;
	private boolean isFirst;
	private int slideTop;
	private int qrWidth;// 扫描线的长
	private int qrHeight;// 扫描线的高
	private Rect qrSrc;
	private Rect qrDst;
	
	public QRCodeScanView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		paint = new Paint();
		Resources resources = getResources();
		qrLineBitmap = BitmapFactory.decodeResource(resources, R.drawable.weixin_scan_line);
		qrWidth = qrLineBitmap.getWidth();
		qrHeight = qrLineBitmap.getHeight();
		qrSrc = new Rect(0, 0, qrWidth, qrHeight);

		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) 
	{
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) 
		{
			return;
		}
		// 初始化中间线滑动的最上边和最下边
		if (!isFirst) 
		{
			isFirst = true;
			slideTop = frame.top;
		}

		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (resultBitmap != null) 
		{
			//Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		}
		else 
		{
			//绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if (slideTop >= frame.bottom) 
			{
				slideTop = frame.top;
			}

			qrDst = new Rect(frame.left, slideTop, frame.right, slideTop + qrHeight);
			canvas.drawBitmap(qrLineBitmap, qrSrc, qrDst, null);

			int linewidth = 10;
			paint.setColor(frameColor);

			canvas.drawRect(15 + frame.left, 15 + frame.top, 15 + (linewidth + frame.left), 15 + (50 + frame.top), paint);
			canvas.drawRect(15 + frame.left, 15 + frame.top, 15 + (50 + frame.left), 15 + (linewidth + frame.top), paint);
			canvas.drawRect(-15 + ((0 - linewidth) + frame.right), 15 + frame.top, -15 + (1 + frame.right), 15 + (50 + frame.top), paint);
			canvas.drawRect(-15 + (-50 + frame.right), 15 + frame.top, -15 + frame.right, 15 + (linewidth + frame.top), paint);
			canvas.drawRect(15 + frame.left, -15 + (-49 + frame.bottom), 15 + (linewidth + frame.left), -15 + (1 + frame.bottom), paint);
			canvas.drawRect(15 + frame.left, -15 + ((0 - linewidth) + frame.bottom), 15 + (50 + frame.left), -15 + (1 + frame.bottom), paint);
			canvas.drawRect(-15 + ((0 - linewidth) + frame.right), -15 + (-49 + frame.bottom), -15 + (1 + frame.right), -15 + (1 + frame.bottom), paint);
			canvas.drawRect(-15 + (-50 + frame.right), -15 + ((0 - linewidth) + frame.bottom), -15 + frame.right, -15 + (linewidth - (linewidth - 1) + frame.bottom), paint);

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) 
			{
				lastPossibleResultPoints = null;
			} 
			else 
			{
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) 
				{
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) 
			{
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) 
				{
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
				}
			}
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() 
	{
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live scanning display.
	 * 
	 * @param barcode  An image of the decoded barcode.
	 *           
	 */
	public void drawResultBitmap(Bitmap barcode) 
	{
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) 
	{
		possibleResultPoints.add(point);
	}
}
