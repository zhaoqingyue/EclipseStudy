package com.zhaoqy.app.demo.notebook.pwdnotes.view;

public class Point 
{
	public static int STATE_NORMAL      = 0; 
	public static int STATE_CHECK       = 1;
	public static int STATE_CHECK_ERROR = 2; 

	public float x;
	public float y;
	public int   state = 0;
	public int   index = 0;

	public Point() 
	{
	}

	public Point(float x, float y) 
	{
		this.x = x;
		this.y = y;
	}
}
