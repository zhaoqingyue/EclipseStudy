/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: TextUtil.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.util
 * @Description: 文字工具类
 * @author: zhaoqy
 * @date: 2015-11-6 上午11:45:28
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import com.zhaoqy.app.demo.DemoApplication;

public class TextUtil 
{
	private DemoApplication mApplication;

	public TextUtil(DemoApplication application) 
	{
		mApplication = application;
	}

	/**
	 * 根据输入流转换成字符串
	 * @param inputStream 文字输入流
	 * @return 文字字符串(String 类型)
	 */
	public String readTextFile(InputStream inputStream) 
	{
		String readedStr = "";
		BufferedReader br;
		try 
		{
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String tmp;
			while ((tmp = br.readLine()) != null) 
			{
				readedStr += tmp;
			}
			br.close();
			inputStream.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return readedStr;
	}

	/**
	 * @Title: buildPattern
	 * @Description: 表情的正则表达式
	 * @return: Pattern
	 */
	private Pattern buildPattern() 
	{
		StringBuilder patternString = new StringBuilder(mApplication.mFacesText.size() * 3);
		patternString.append('(');
		for (int i = 0; i < mApplication.mFacesText.size(); i++) 
		{
			String s = mApplication.mFacesText.get(i);
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		patternString.replace(patternString.length() - 1, patternString.length(), ")");
		return Pattern.compile(patternString.toString());
	}

	/**
	 * 将文本中含有表情字符的内容换成带有表情图片的文本
	 * @param text 带有表情字符的文本
	 * @return 带有表情图片的文本
	 */
	public CharSequence replace(CharSequence text) 
	{
		try 
		{
			SpannableStringBuilder builder = new SpannableStringBuilder(text);
			Pattern pattern = buildPattern();
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) 
			{
				Bitmap bitmap = mApplication.getFaceBitmap(mApplication.mFacesText.indexOf(matcher.group()));
				if (bitmap != null) 
				{
					@SuppressWarnings("deprecation")
					ImageSpan span = new ImageSpan(bitmap);
					builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			return builder;
		}
		catch (Exception e) 
		{
			return text;
		}
	}

	public String getCharacterPinYin(char c) 
	{
		HanyuPinyinOutputFormat format = null;
		format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		String[] pinyin = null;
		try 
		{
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		} 
		catch (BadHanyuPinyinOutputFormatCombination e) 
		{
			e.printStackTrace();
		}
		//如果c不是汉字，toHanyuPinyinStringArray会返回null
		if (pinyin == null)
			return null;
		//只取一个发音，如果是多音字，仅取第一个发音
		return pinyin[0];
	}

	/**
	 * @Title: getStringPinYin
	 * @Description: 获取字符的拼音
	 * @param str
	 * @return: String
	 */
	public String getStringPinYin(String str) 
	{
		StringBuilder sb = new StringBuilder();
		String tempPinyin = null;
		for (int i = 0; i < str.length(); ++i) 
		{
			tempPinyin = getCharacterPinYin(str.charAt(i));
			if (tempPinyin == null) 
			{
				//如果str.charAt(i)非汉字，则保持原样
				sb.append(str.charAt(i));
			} 
			else 
			{
				sb.append(tempPinyin);
			}
		}
		return sb.toString();
	}
}
