/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: LinkView.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.view
 * @Description: 超链接文本控件
 * @author: zhaoqy
 * @date: 2015-11-20 下午2:11:09
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.view;

import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.item.URLs;
import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class LinkView extends TextView 
{
	public LinkView(Context context) 
	{
		super(context);
	}
	
	public LinkView(Context context, AttributeSet attrs) 
	{  
        super(context, attrs);
    }  
	
	public LinkView(Context context, AttributeSet attrs, int defStyle) 
	{  
        super(context, attrs, defStyle);
    }  
	
	public void setLinkText(String linktxt)
	{
		Spanned span = Html.fromHtml(linktxt);
		setText(span);
		setMovementMethod(LinkMovementMethod.getInstance());
		parseLinkText(span);
	}
	
	public void parseLinkText(Spanned spanhtml)
	{
		CharSequence text = getText();
		if(text instanceof Spannable)
		{
			int end = text.length();
			Spannable sp = (Spannable)getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);	
			URLSpan[] htmlurls = spanhtml != null ? spanhtml.getSpans(0, end, URLSpan.class) : new URLSpan[]{};
			if(urls.length == 0 && htmlurls.length == 0) return;
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			//这里会清除之前所有的样式
			//style.clearSpans();
			for(URLSpan url : urls)
			{
				//只需要移除之前的URL样式，再重新设置
				style.removeSpan(url);
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan,sp.getSpanStart(url),sp.getSpanEnd(url),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			for(URLSpan url : htmlurls)
			{
				//只需要移除之前的URL样式，再重新设置
				style.removeSpan(url);
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan,spanhtml.getSpanStart(url),spanhtml.getSpanEnd(url),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			setText(style);
		}
	}
	
	public void parseLinkText()
	{
		parseLinkText(null);
	}
	
	private static class MyURLSpan extends ClickableSpan
	{     
		private String mUrl; 
		
		MyURLSpan(String url) 
		{     
			mUrl =url;     
		}  
		
		@Override  
		public void onClick(View widget) 
		{  
			URLs urls = URLs.parseURL(mUrl);
			if(urls != null)
			{
				UIHelper.showLinkRedirect(widget.getContext(), urls.getObjType(), urls.getObjId(), urls.getObjKey());
			}
			else
			{
				UIHelper.openBrowser(widget.getContext(), mUrl);
			}
		}     
	} 
}
