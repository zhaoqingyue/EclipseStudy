package com.zhaoqy.app.demo.notebook.pwdnotes.util;

public class RoundUtil 
{
	public static boolean checkInRound(float sx, float sy, float r, float x, float y) 
	{
		return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
	}
}
