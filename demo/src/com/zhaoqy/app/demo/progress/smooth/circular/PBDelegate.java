package com.zhaoqy.app.demo.progress.smooth.circular;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface PBDelegate 
{
	void draw(Canvas canvas, Paint paint);

	void start();

	void stop();

	void progressiveStop(CircularProgressDrawable.OnEndListener listener);
}
