/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: UIHelper.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.helper
 * @Description: 应用程序UI工具包：封装UI相关的一些操作
 * @author: zhaoqy
 * @date: 2015-11-19 上午9:20:24
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.helper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.activity.OSChinaAboutActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaBlogDetailActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaCommentPubActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaImageActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaImageZoomActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaLoginActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaMainActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaMessageDetailActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaMessageForwardActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaMessagePubActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaNewsDetailActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaQuestionDetailActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaQuestionPubActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaQuestionTagActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaSearchActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaSettingActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaSoftwareDetailActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaSoftwareLibActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaTweetDetailActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaTweetPubActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaUserCenterActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaUserInfoActivity;
import com.zhaoqy.app.demo.oschina.adapter.FaceAdapter;
import com.zhaoqy.app.demo.oschina.common.ApiClient;
import com.zhaoqy.app.demo.oschina.common.AppConfig;
import com.zhaoqy.app.demo.oschina.common.AppProperty;
import com.zhaoqy.app.demo.oschina.item.Active;
import com.zhaoqy.app.demo.oschina.item.Comment;
import com.zhaoqy.app.demo.oschina.item.CommentList;
import com.zhaoqy.app.demo.oschina.item.Messages;
import com.zhaoqy.app.demo.oschina.item.News;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.item.Tweet;
import com.zhaoqy.app.demo.oschina.item.URLs;
import com.zhaoqy.app.demo.oschina.util.CacheUtils;
import com.zhaoqy.app.demo.oschina.util.FileUtils;
import com.zhaoqy.app.demo.oschina.util.ImageUtils;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.widget.MyQuickAction;
import com.zhaoqy.app.demo.oschina.widget.QuickAction;

public class UIHelper 
{
	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;
	
	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;
	
	/** 表情图片匹配 */
	private static Pattern facePattern = Pattern.compile("\\[{1}([0-9]\\d*)\\]{1}");
	
	/** 全局web样式 */
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} " +
			"img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} " +
			"pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} " +
			"a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
	
	/**
	 * 显示关于我们
	 * @param context
	 */
	public static void showAbout(Context context)
	{
		Intent intent = new Intent(context, OSChinaAboutActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示首页
	 * @param activity
	 */
	public static void showHome(Context context)
	{
		Intent intent = new Intent(context, OSChinaMainActivity.class);
		context.startActivity(intent);
		((Activity) context).finish();
	}
	
	/**
	 * 发送通知广播
	 * @param context
	 * @param notice
	 */
	public static void sendBroadCast(Context context, Notice notice)
	{
		if(!UserHelper.isLogin() || notice == null) return;
		Intent intent = new Intent("net.oschina.app.action.APPWIDGET_UPDATE"); 
		intent.putExtra("atmeCount", notice.getAtmeCount());
		intent.putExtra("msgCount", notice.getMsgCount());
		intent.putExtra("reviewCount", notice.getReviewCount());
		intent.putExtra("newFansCount", notice.getNewFansCount());
		context.sendBroadcast(intent);
	}
	
	/**
	 * 发送广播-发布动弹
	 * @param context
	 * @param notice
	 */
	public static void sendBroadCastTweet(Context context, int what, Result res, Tweet tweet)
	{
		if(res==null && tweet==null) return;
		Intent intent = new Intent("net.oschina.app.action.APP_TWEETPUB"); 
		intent.putExtra("MSG_WHAT", what);
		if(what == 1)
		{
			intent.putExtra("RESULT", res);
		}
		else
		{
			intent.putExtra("TWEET", tweet);
		}
		context.sendBroadcast(intent);
	}
	
	/**
	 * 显示登录页面
	 * @param activity
	 */
	public static void showLoginDialog(Context context)
	{
		Intent intent = new Intent(context,OSChinaLoginActivity.class);
		if(context instanceof OSChinaMainActivity)
		{
			intent.putExtra("LOGINTYPE", OSChinaLoginActivity.LOGIN_MAIN);
		}
		else if(context instanceof OSChinaSettingActivity)
		{
			intent.putExtra("LOGINTYPE", OSChinaLoginActivity.LOGIN_SETTING);
		}
		else
		{
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}
	
	/**
	 * 是否加载显示文章图片
	 * @return
	 */
	public static boolean isLoadImage(Context context)
	{
		String perf_loadimage = AppProperty.getProperty(context, AppConfig.CONF_LOAD_IMAGE);
		//默认是加载的
		if(StringUtils.isEmpty(perf_loadimage))
		{
			return true;
		}
		else
		{
			return StringUtils.toBool(perf_loadimage);
		}
	}
	
	/**
	 * 菜单显示登录或登出
	 * @param activity
	 * @param menu
	 */
	public static void showMenuLoginOrLogout(Menu menu)
	{
		if(UserHelper.isLogin())
		{
			menu.findItem(R.id.oschina_menu_user).setTitle(R.string.main_menu_logout);
			menu.findItem(R.id.oschina_menu_user).setIcon(R.drawable.oschina_menu_logout);
		}
		else
		{
			menu.findItem(R.id.oschina_menu_user).setTitle(R.string.main_menu_login);
			menu.findItem(R.id.oschina_menu_user).setIcon(R.drawable.oschina_menu_login);
		}
	}
	
	/**
	 * 快捷栏显示登录与登出
	 * @param activity
	 * @param qa
	 */
	public static void showSettingLoginOrLogout(Context context, QuickAction qa)
	{
		if(UserHelper.isLogin())
		{
			qa.setIcon(MyQuickAction.buildDrawable(context, R.drawable.oschina_menu_logout));
			qa.setTitle(context.getString(R.string.main_menu_logout));
		}
		else
		{
			qa.setIcon(MyQuickAction.buildDrawable(context, R.drawable.oschina_menu_login));
			qa.setTitle(context.getString(R.string.main_menu_login));
		}
	}
	
	/**
	 * 快捷栏是否显示文章图片
	 * @param activity
	 * @param qa
	 */
	public static void showSettingIsLoadImage(Context context, QuickAction qa)
	{
		if(isLoadImage(context))
		{
			qa.setIcon(MyQuickAction.buildDrawable(context, R.drawable.oschina_menu_picnoshow));
			qa.setTitle(context.getString(R.string.main_menu_picnoshow));
		}
		else
		{
			qa.setIcon(MyQuickAction.buildDrawable(context, R.drawable.oschina_menu_picshow));
			qa.setTitle(context.getString(R.string.main_menu_picshow));
		}
	}
	
	/**
	 * 文章是否加载图片显示
	 * @param activity
	 */
	public static void changeSettingIsLoadImage(Context context)
	{
		if(isLoadImage(context))
		{
			setConfigLoadimage(context, false);
			ToastMessage(context, "已设置文章不加载图片");
		}
		else
		{
			setConfigLoadimage(context, true);
			ToastMessage(context, "已设置文章加载图片");
		}
	}
	
	public static void changeSettingIsLoadImage(Context context, boolean b)
	{
		setConfigLoadimage(context, b);
	}
	
	/**
	 * 设置是否加载文章图片
	 * @param b
	 */
	public static void setConfigLoadimage(Context context, boolean b)
	{
		AppProperty.setProperty(context, AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
	}
	
	/**
	 * 是否左右滑动
	 * @return
	 */
	public static boolean isScroll(Context context)
	{
		String perf_scroll = AppProperty.getProperty(context, AppConfig.CONF_SCROLL);
		//默认是关闭左右滑动
		if(StringUtils.isEmpty(perf_scroll))
		{
			return false;
		}
		else
		{
			return StringUtils.toBool(perf_scroll);
		}
	}
	
	/**
	 * 设置是否左右滑动
	 * @param b
	 */
	public static void setConfigScroll(Context context, boolean b)
	{
		AppProperty.setProperty(context, AppConfig.CONF_SCROLL, String.valueOf(b));
	}
	
	/**
	 * 是否发出提示音
	 * @return
	 */
	public static boolean isVoice(Context context)
	{
		String perf_voice = AppProperty.getProperty(context, AppConfig.CONF_VOICE);
		//默认是开启提示声音
		if(StringUtils.isEmpty(perf_voice))
		{
			return true;
		}
		else
		{
			return StringUtils.toBool(perf_voice);
		}
	}
	
	/**
	 * 设置是否发出提示音
	 * @param b
	 */
	public static void setConfigVoice(Context context, boolean b)
	{
		AppProperty.setProperty(context, AppConfig.CONF_VOICE, String.valueOf(b));
	}
	
	/**
	 * 清除app缓存
	 * @param activity
	 */
	public static void clearAppCache(final Context context)
	{
		final Handler handler = new Handler()
		{
			public void handleMessage(Message msg) 
			{
				if(msg.what==1)
				{
					ToastMessage(context, "缓存清除成功");
				}
				else
				{
					ToastMessage(context, "缓存清除失败");
				}
			}
		};
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				try 
				{				
					CacheUtils.clearAppCache(context);
					msg.what = 1;
				}
				catch (Exception e) 
				{
					e.printStackTrace();
	            	msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * 加载显示用户头像
	 * @param imgFace
	 * @param faceURL
	 */
	public static void showUserFace(final ImageView imgFace, final String faceURL)
	{
		showLoadImage(imgFace, faceURL, "用户头像加载失败");
	}
	
	/**
	 * 加载显示图片
	 * @param imgFace
	 * @param faceURL
	 * @param errMsg
	 */
	public static void showLoadImage(final ImageView imgView, final String imgURL, final String errMsg)
	{
		//读取本地图片
		if(StringUtils.isEmpty(imgURL) || imgURL.endsWith("portrait.gif"))
		{
			Bitmap bmp = BitmapFactory.decodeResource(imgView.getResources(), R.drawable.oschina_dface);
			imgView.setImageBitmap(bmp);
			return;
		}
		
		//是否有缓存图片
    	final String filename = FileUtils.getFileName(imgURL);
    	//Environment.getExternalStorageDirectory(); //返回/sdcard
    	String filepath = imgView.getContext().getFilesDir() + File.separator + filename;
		File file = new File(filepath);
		if(file.exists())
		{
			Bitmap bmp = ImageUtils.getBitmap(imgView.getContext(), filename);
			imgView.setImageBitmap(bmp);
			return;
    	}
		
		//从网络获取&写入图片缓存
		String _errMsg = "图片加载失败";
		if(!StringUtils.isEmpty(errMsg))
			_errMsg = errMsg;
		final String ErrMsg = _errMsg;
		final Handler handler = new Handler()
		{
			public void handleMessage(Message msg) 
			{
				if(msg.what==1 && msg.obj != null)
				{
					imgView.setImageBitmap((Bitmap)msg.obj);
					try 
					{
                    	//写图片缓存
						ImageUtils.saveImage(imgView.getContext(), filename, (Bitmap)msg.obj);
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				else
				{
					Toast.makeText(imgView.getContext(), ErrMsg, Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				try 
				{
					Bitmap bmp = ApiClient.getNetBitmap(imgURL);
					msg.what = 1;
					msg.obj = bmp;
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * url跳转
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url)
	{
		URLs urls = URLs.parseURL(url);
		if(urls != null)
		{
			showLinkRedirect(context, urls.getObjType(), urls.getObjId(), urls.getObjKey());
		}
		else
		{
			openBrowser(context, url);
		}
	}
	
	public static void showLinkRedirect(Context context, int objType, int objId, String objKey)
	{
		switch (objType) 
		{
			case URLs.URL_OBJ_TYPE_NEWS:
			{
				showNewsDetail(context, objId);
				break;
			}
			case URLs.URL_OBJ_TYPE_QUESTION:
			{
				showQuestionDetail(context, objId);
				break;
			}
			case URLs.URL_OBJ_TYPE_QUESTION_TAG:
			{
				showQuestionListByTag(context, objKey); 
				break;
			}
			case URLs.URL_OBJ_TYPE_SOFTWARE:
			{
				showSoftwareDetail(context, objKey);
				break;
			}
			case URLs.URL_OBJ_TYPE_ZONE:
			{
				showUserCenter(context, objId, objKey);
				break;
			}
			case URLs.URL_OBJ_TYPE_TWEET:
			{
				showTweetDetail(context, objId);
				break;
			}
			case URLs.URL_OBJ_TYPE_BLOG:
			{
				showBlogDetail(context, objId);
				break;
			}
			case URLs.URL_OBJ_TYPE_OTHER:
			{
				openBrowser(context, objKey);
				break;
			}
			default:
				break;
		}
	}
	
	/**
	 * 打开浏览器
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url)
	{
		try 
		{
			Uri uri = Uri.parse(url);  
			Intent it = new Intent(Intent.ACTION_VIEW, uri);  
			context.startActivity(it);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			ToastMessage(context, "无法浏览此网页", 500);
		} 
	}
	
	/**
	 * 获取webviewClient对象
	 * @return
	 */
	public static WebViewClient getWebViewClient()
	{
		return new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,String url) 
			{
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}
	
	/**
	 * 获取TextWatcher对象
	 * @param context
	 * @param tmlKey
	 * @return
	 */
	public static TextWatcher getTextWatcher(final Context context, final String temlKey) 
	{
		return new TextWatcher() 
		{		
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				//保存当前EditText正在编辑的内容
				AppProperty.setProperty(context, temlKey, s.toString());
			}		
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}		
			public void afterTextChanged(Editable s) {}
		};
	}
	
	/**
	 * 编辑器显示保存的草稿
	 * @param context
	 * @param editer
	 * @param temlKey
	 */
	public static void showTempEditContent(Context context, EditText editer, String temlKey) 
	{
		String tempContent = AppProperty.getProperty(context, temlKey);
		if(!StringUtils.isEmpty(tempContent)) 
		{
			SpannableStringBuilder builder = parseFaceByText(context, tempContent);
			editer.setText(builder);
			editer.setSelection(tempContent.length());//设置光标位置
		}
	}
	
	/**
	 * 将[12]之类的字符串替换为表情
	 * @param context
	 * @param content
	 */
	public static SpannableStringBuilder parseFaceByText(Context context, String content) 
	{
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Matcher matcher = facePattern.matcher(content);
		while (matcher.find()) 
		{
			//使用正则表达式找出其中的数字
			int position = StringUtils.toInt(matcher.group(1));
			int resId = 0;
			try 
			{
				if(position > 65 && position < 102)
				{
					position = position-1;
				}
				else if(position > 102)
				{
					position = position-2;
				}
				resId = FaceAdapter.getImageIds()[position];
				Drawable d = context.getResources().getDrawable(resId);
				//设置表情图片的显示大小
				d.setBounds(0, 0, 35, 35);
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} 
			catch (Exception e) 
			{
			}
		}
		return builder;
	}
	
	/**
	 * 清除文字
	 * @param cont
	 * @param editer
	 */
	public static void showClearWordsDialog(final Context cont, final EditText editer, final TextView numwords)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setTitle("清除文字吗？");
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				//清除文字
				editer.setText("");
				numwords.setText("160");
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	/**
	 * 显示新闻详情
	 * @param context
	 * @param newsId
	 */
	public static void showNewsDetail(Context context, int newsId)
	{
		Intent intent = new Intent(context, OSChinaNewsDetailActivity.class);
		intent.putExtra("news_id", newsId);
		context.startActivity(intent);
	}
	
	/**
	 * 显示帖子详情
	 * @param context
	 * @param postId
	 */
	public static void showQuestionDetail(Context context, int postId)
	{
		Intent intent = new Intent(context, OSChinaQuestionDetailActivity.class);
		intent.putExtra("post_id", postId);
		context.startActivity(intent);
	}
	
	/**
	 * 显示相关Tag帖子列表
	 * @param context
	 * @param tag
	 */
	public static void showQuestionListByTag(Context context, String tag)
	{
		Intent intent = new Intent(context, OSChinaQuestionTagActivity.class);
		intent.putExtra("post_tag", tag);
		context.startActivity(intent);
	}
	
	/**
	 * 显示我要提问页面
	 * @param context
	 */
	public static void showQuestionPub(Context context)
	{
		Intent intent = new Intent(context, OSChinaQuestionPubActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示动弹详情及评论
	 * @param context
	 * @param tweetId
	 */
	public static void showTweetDetail(Context context, int tweetId)
	{
		Intent intent = new Intent(context, OSChinaTweetDetailActivity.class);
		intent.putExtra("tweet_id", tweetId);
		context.startActivity(intent);
	}
	
	/**
	 * 显示动弹一下页面
	 * @param context
	 */
	public static void showTweetPub(Activity context)
	{
		Intent intent = new Intent(context, OSChinaTweetPubActivity.class);
		context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	
	public static void showTweetPub(Activity context,String atme,int atuid)
	{
		Intent intent = new Intent(context, OSChinaTweetPubActivity.class);
		intent.putExtra("at_me", atme);
		intent.putExtra("at_uid", atuid);
		context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	
	/**
	 * 显示博客详情
	 * @param context
	 * @param blogId
	 */
	public static void showBlogDetail(Context context, int blogId)
	{
		Intent intent = new Intent(context, OSChinaBlogDetailActivity.class);
		intent.putExtra("blog_id", blogId);
		context.startActivity(intent);
	}
	
	/**
	 * 显示软件详情
	 * @param context
	 * @param ident
	 */
	public static void showSoftwareDetail(Context context, String ident)
	{
		Intent intent = new Intent(context, OSChinaSoftwareDetailActivity.class);
		intent.putExtra("ident", ident);
		context.startActivity(intent);
	}
	
	/**
	 * 显示用户动态
	 * @param context
	 * @param uid
	 * @param hisuid
	 * @param hisname
	 */
	public static void showUserCenter(Context context, int hisuid, String hisname)
	{
    	Intent intent = new Intent(context, OSChinaUserCenterActivity.class);
		intent.putExtra("his_id", hisuid);
		intent.putExtra("his_name", hisname);
		context.startActivity(intent);
	}
	
	/**
	 * 新闻超链接点击跳转
	 * @param context
	 * @param newsId
	 * @param newsType
	 * @param objId
	 */
	public static void showNewsRedirect(Context context, News news)
	{
		String url = news.getUrl();
		if(StringUtils.isEmpty(url)) 
		{
			int newsId = news.getId();
			int newsType = news.getNewType().type;
			String objId = news.getNewType().attachment;
			switch (newsType) 
			{
				case News.NEWSTYPE_NEWS:
				{
					showNewsDetail(context, newsId);
					break;
				}
				case News.NEWSTYPE_SOFTWARE:
				{
					showSoftwareDetail(context, objId);
					break;
				}
				case News.NEWSTYPE_POST:
				{
					showQuestionDetail(context, StringUtils.toInt(objId));
					break;
				}
				case News.NEWSTYPE_BLOG:
				{
					showBlogDetail(context, StringUtils.toInt(objId));
					break;
				}
			}
		}
		else 
		{
			showUrlRedirect(context, url);
		}
	}
	
	/**
	 * 动态点击跳转到相关新闻、帖子等
	 * @param context
	 * @param id
	 * @param catalog 0其他  1新闻  2帖子  3动弹  4博客  
	 */
	public static void showActiveRedirect(Context context, Active active)
	{
		String url = active.getUrl();
		if(StringUtils.isEmpty(url)) 
		{
			int id = active.getObjectId();
			int catalog = active.getActiveType();
			switch (catalog) 
			{
				case Active.CATALOG_OTHER:
					break;
				case Active.CATALOG_NEWS:
				{
					showNewsDetail(context, id);
					break;
				}
				case Active.CATALOG_POST:
				{
					showQuestionDetail(context, id);
					break;
				}
				case Active.CATALOG_TWEET:
				{
					showTweetDetail(context, id);
					break;
				}
				case Active.CATALOG_BLOG:
				{
					showBlogDetail(context, id);
					break;
				}
			}
		}
		else 
		{
			showUrlRedirect(context, url);
		}
	}

	/**
	 * 显示评论发表页面
	 * @param context
	 * @param id 新闻|帖子|动弹的id
	 * @param catalog 1新闻 2帖子 3动弹 4动态
	 */
	public static void showCommentPub(Activity context, int id, int catalog)
	{
		Intent intent = new Intent(context, OSChinaCommentPubActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("catalog", catalog);
		context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	
	/**
	 * 显示评论回复页面
	 * @param context
	 * @param id
	 * @param catalog
	 * @param replyid
	 * @param authorid
	 */
	public static void showCommentReply(Context context, int id, int catalog, int replyid, int authorid, String author, String content)
	{
		Intent intent = new Intent(context, OSChinaCommentPubActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("catalog", catalog);
		intent.putExtra("reply_id", replyid);
		intent.putExtra("author_id", authorid);
		intent.putExtra("author", author);
		intent.putExtra("content", content);
		if(catalog == CommentList.CATALOG_POST)
		{
			((Activity) context).startActivityForResult(intent, REQUEST_CODE_FOR_REPLY);
		}	
		else
		{
			((Activity) context).startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
		}
	}
	
	/**
	 * 显示留言对话页面
	 * @param context
	 * @param catalog
	 * @param friendid
	 */
	public static void showMessageDetail(Context context, int friendid, String friendname)
	{
		Intent intent = new Intent(context, OSChinaMessageDetailActivity.class);
		intent.putExtra("friend_name", friendname);
		intent.putExtra("friend_id", friendid);
		context.startActivity(intent);
	}
	
	/**
	 * 显示留言回复界面
	 * @param context
	 * @param friendId 对方id
	 * @param friendName 对方名称
	 */
	public static void showMessagePub(Activity context, int friendId, String friendName)
	{
    	Intent intent = new Intent();
		intent.putExtra("user_id", UserHelper.getLoginUid());
		intent.putExtra("friend_id", friendId);
		intent.putExtra("friend_name", friendName);
		intent.setClass(context, OSChinaMessagePubActivity.class);
		context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	
	/**
	 * 显示转发留言界面
	 * @param context
	 * @param friendName 对方名称
	 * @param messageContent 留言内容
	 */
	public static void showMessageForward(Activity context, String friendName, String messageContent)
	{
    	Intent intent = new Intent();
		intent.putExtra("user_id", UserHelper.getLoginUid());
		intent.putExtra("friend_name", friendName);
		intent.putExtra("message_content", messageContent);
		intent.setClass(context, OSChinaMessageForwardActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 调用系统安装了的应用分享
	 * @param context
	 * @param title
	 * @param url
	 */
	public static void showShareMore(Activity context,final String title,final String url)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
		intent.putExtra(Intent.EXTRA_TEXT, title + " " +url);
		context.startActivity(Intent.createChooser(intent, "选择分享"));
	}
	
	/**
	 * 分享到'新浪微博'或'腾讯微博'的对话框
	 * @param context 当前Activity
	 * @param title	分享的标题
	 * @param url 分享的链接
	 */
	public static void showShareDialog(final Activity context,final String title,final String url)
	{
		/*AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.btn_star);
		builder.setTitle(context.getString(R.string.share));
		builder.setItems(R.array.app_share_items,new DialogInterface.OnClickListener(){
			AppConfig cfgHelper = AppConfig.getAppConfig(context);
			AccessInfo access = cfgHelper.getAccessInfo();
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
					case 0://新浪微博
						//分享的内容
						final String shareMessage = title + " " +url;
						//初始化微博
						if(SinaWeiboHelper.isWeiboNull())
			    		{
			    			SinaWeiboHelper.initWeibo();
			    		}
						//判断之前是否登陆过
				        if(access != null)
				        {   
				        	SinaWeiboHelper.progressDialog = new ProgressDialog(context); 
				        	SinaWeiboHelper.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
				        	SinaWeiboHelper.progressDialog.setMessage(context.getString(R.string.sharing));
				        	SinaWeiboHelper.progressDialog.setCancelable(true);
				        	SinaWeiboHelper.progressDialog.show();
				        	new Thread()
				        	{
								public void run() 
								{						        	
									SinaWeiboHelper.setAccessToken(access.getAccessToken(), access.getAccessSecret(), access.getExpiresIn());
									SinaWeiboHelper.shareMessage(context, shareMessage);
				        		}
				        	}.start();
				        }
				        else
				        {
				        	SinaWeiboHelper.authorize(context, shareMessage);
				        }
						break;
					case 1://腾讯微博
						QQWeiboHelper.shareToQQ(context, title, url);
						break;
					case 2://更多
						showShareMore(context, title, url);
						break;
				}				
			}
		});
		builder.create().show();*/
	}
	
	/**
	 * 收藏操作选择框
	 * @param context
	 * @param thread
	 */
	public static void showFavoriteOptionDialog(Context context, final Thread thread)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.oschina_dialog_menu);
		builder.setTitle(context.getString(R.string.select));
		builder.setItems(R.array.favorite_options,new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1) 
			{
				switch (arg1) 
				{
					case 0:
					{
						//删除
						thread.start();
						break;
					}	
				}				
			}
		});
		builder.create().show();
	}
	
	/**
	 * 消息列表操作选择框
	 * @param context
	 * @param msg
	 * @param thread
	 */
	public static void showMessageListOptionDialog(final Activity context,final Messages msg,final Thread thread)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.oschina_dialog_menu);
		builder.setTitle(context.getString(R.string.select));
		builder.setItems(R.array.message_list_options,new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1) 
			{
				switch (arg1) 
				{
					case 0:
					{
						//回复
						showMessagePub(context,msg.getFriendId(),msg.getFriendName());
						break;
					}
					case 1:
					{
						//转发
						showMessageForward(context,msg.getFriendName(),msg.getContent());
						break;
					}
					case 2:
					{
						//删除
						thread.start();
						break;
					}
				}				
			}
		});
		builder.create().show();
	}
	
	/**
	 * 消息详情操作选择框
	 * @param context
	 * @param msg
	 * @param thread
	 */
	public static void showMessageDetailOptionDialog(final Activity context,final Comment msg,final Thread thread)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.oschina_dialog_menu);
		builder.setTitle(context.getString(R.string.select));
		builder.setItems(R.array.message_detail_options,new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1) 
			{
				switch (arg1) 
				{
					case 0:
					{
						//转发
						showMessageForward(context,msg.getAuthor(),msg.getContent());
						break;
					}
					case 1:
					{
						//删除
						thread.start();
						break;
					}
				}				
			}
		});
		builder.create().show();
	}
	
	/**
	 * 评论操作选择框
	 * @param context
	 * @param id 某条新闻，帖子，动弹的id 或者某条消息的 friendid
	 * @param catalog 该评论所属类型：1新闻  2帖子  3动弹  4动态
	 * @param comment 本条评论对象，用于获取评论id&评论者authorid
	 * @param thread 处理删除评论的线程，若无删除操作传null
	 */
	public static void showCommentOptionDialog(final Context context, final int id, final int catalog, final Comment comment, final Thread thread)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.oschina_dialog_menu);
		builder.setTitle(context.getString(R.string.select));
		if(thread != null)
		{
			builder.setItems(R.array.comment_options_2,new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface arg0, int arg1) 
				{
					switch (arg1) 
					{
						case 0:
						{
							//回复
							showCommentReply(context,id,catalog,comment.getId(),comment.getAuthorId(),comment.getAuthor(),comment.getContent());
							break;
						}
						case 1:
						{
							//删除
							thread.start();
							break;
						}
					}				
				}
			});
		}
		else
		{
			builder.setItems(R.array.comment_options_1,new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface arg0, int arg1) 
				{
					switch (arg1) 
					{
						case 0:
						{
							//回复
							showCommentReply(context,id,catalog,comment.getId(),comment.getAuthorId(),comment.getAuthor(),comment.getContent());
							break;
						}
					}				
				}
			});
		}
		builder.create().show();
	}
	
	/**
	 * 博客列表操作
	 * @param context
	 * @param thread
	 */
	public static void showBlogOptionDialog(final Context context,final Thread thread)
	{
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(context.getString(R.string.delete_blog))
		.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				if(thread != null)
				{
					thread.start();
				}
				else
				{
					ToastMessage(context, R.string.msg_noaccess_delete);
				}
				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		})
		.create().show();
	}
	
	/**
	 * 动弹操作选择框
	 * @param context
	 * @param thread
	 */
	public static void showTweetOptionDialog(final Context context, final Thread thread)
	{
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(context.getString(R.string.delete_tweet))
		.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				if(thread != null)
				{
					thread.start();
				}
				else
				{
					ToastMessage(context, R.string.msg_noaccess_delete);
				}
				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		})
		.create().show();
	}
	
	/**
	 * 是否重新发布动弹操对话框
	 * @param context
	 * @param thread
	 */
	public static void showResendTweetDialog(final Context context, final Thread thread)
	{
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(context.getString(R.string.republish_tweet))
		.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				if(OSChinaTweetPubActivity.mMessage != null)
				{
					OSChinaTweetPubActivity.mMessage.setVisibility(View.VISIBLE);
				}
				thread.start();
			}
		})
		.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		})
		.create().show();
	}
	
	/**
	 * 显示图片对话框
	 * @param context
	 * @param imgUrl
	 */
	public static void showImageDialog(Context context, String imgUrl)
	{
		Intent intent = new Intent(context, OSChinaImageActivity.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}
	
	public static void showImageZoomDialog(Context context, String imgUrl)
	{
		Intent intent = new Intent(context, OSChinaImageZoomActivity.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}
	
	/**
	 * 组合动态的动作文本
	 * @param objecttype
	 * @param objectcatalog
	 * @param objecttitle
	 * @return
	 */
	public static SpannableString parseActiveAction(String author,int objecttype,int objectcatalog,String objecttitle)
	{
		String title = "";
		int start = 0;
		int end = 0;
		if(objecttype == 32 && objectcatalog == 0)
		{
			title = "加入了开源中国";
		}
		else if(objecttype == 1 && objectcatalog == 0)
		{
			title = "添加了开源项目 " + objecttitle;
		}
		else if(objecttype == 2 && objectcatalog == 1)
		{
			title = "在讨论区提问：" + objecttitle;
		}
		else if(objecttype == 2 && objectcatalog == 2)
		{
			title = "发表了新话题：" + objecttitle;
		}
		else if(objecttype == 3 && objectcatalog == 0)
		{
			title = "发表了博客 " + objecttitle;
		}
		else if(objecttype == 4 && objectcatalog == 0)
		{
			title = "发表一篇新闻 " + objecttitle;
		}
		else if(objecttype == 5 && objectcatalog == 0)
		{
			title = "分享了一段代码 " + objecttitle;
		}
		else if(objecttype == 6 && objectcatalog == 0)
		{
			title = "发布了一个职位：" + objecttitle;
		}
		else if(objecttype==16 && objectcatalog==0)
		{
			title = "在新闻 "+objecttitle+" 发表评论";
		}
		else if(objecttype == 17 && objectcatalog == 1)
		{
			title = "回答了问题：" + objecttitle;
		}
		else if(objecttype == 17 && objectcatalog == 2)
		{
			title = "回复了话题：" + objecttitle;
		}
		else if(objecttype == 17 && objectcatalog == 3)
		{
			title = "在 " + objecttitle + " 对回帖发表评论";
		}
		else if(objecttype == 18 && objectcatalog == 0)
		{
			title = "在博客 " + objecttitle + " 发表评论";
		}
		else if(objecttype == 19 && objectcatalog == 0)
		{
			title = "在代码 " + objecttitle + " 发表评论";
		}
		else if(objecttype == 20 && objectcatalog == 0)
		{
			title = "在职位 " + objecttitle + " 发表评论";
		}
		else if(objecttype == 101 && objectcatalog == 0)
		{
			title = "回复了动态：" + objecttitle;
		}
		else if(objecttype == 100)
		{
			title = "更新了动态";
		}
		title = author + " " + title;
		SpannableString sp = new SpannableString(title);
		//设置用户名字体大小、加粗、高亮 
		sp.setSpan(new AbsoluteSizeSpan(14,true), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置标题字体大小、高亮 
        if(!StringUtils.isEmpty(objecttitle))
        {
        	start = title.indexOf(objecttitle);
			if(objecttitle.length()>0 && start>0)
			{
				end = start + objecttitle.length();  
				sp.setSpan(new AbsoluteSizeSpan(14,true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
        }
        return sp;
	}
	
	/**
	 * 组合动态的回复文本
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseActiveReply(String name,String body)
	{
		SpannableString sp = new SpannableString(name + "：" + body);
		//设置用户名字体加粗、高亮 
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/**
	 * 组合消息文本
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseMessageSpan(String name,String body,String action)
	{
		SpannableString sp = null;
		int start = 0;
		int end = 0;
		if(StringUtils.isEmpty(action))
		{
			sp = new SpannableString(name + "：" + body);
			end = name.length();
		}
		else
		{
			sp = new SpannableString(action + name + "：" + body);
			start = action.length();
			end = start + name.length();
		}
		//设置用户名字体加粗、高亮 
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/**
	 * 组合回复引用文本
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseQuoteSpan(String name,String body)
	{
		SpannableString sp = new SpannableString("回复：" + name + "\n" + body);
		//设置用户名字体加粗、高亮 
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 3, 3+name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 3, 3+name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/**
	 * 用户登录或注销
	 * @param activity
	 */
	public static void loginOrLogout(Context context)
	{
		if(UserHelper.isLogin())
		{
			UserHelper.Logout(context);
			UIHelper.ToastMessage(context, "已退出登录");
		}
		else
		{
			UIHelper.showLoginDialog(context);
		}
	}
	
	/**
	 * 显示我的资料
	 * @param context
	 */
	public static void showUserInfo(Context context)
	{
		if(!UserHelper.isLogin())
		{
			UIHelper.showLoginDialog(context);
		}
		else
		{
			Intent intent = new Intent(context, OSChinaUserInfoActivity.class);
			context.startActivity(intent);
		}
	}
	
	/**
	 * 显示软件界面
	 * @param context
	 */
	public static void showSoftware(Context context)
	{
		Intent intent = new Intent(context, OSChinaSoftwareLibActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示搜索界面
	 * @param context
	 */
	public static void showSearch(Context context)
	{
		Intent intent = new Intent(context,  OSChinaSearchActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示系统设置界面
	 * @param context
	 */
	public static void showSetting(Context context)
	{
		Intent intent = new Intent(context,  OSChinaSettingActivity.class);
		context.startActivity(intent);
	}	
	
	/**
	 * 退出程序
	 * @param cont
	 */
	public static void Exit(final Context cont)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				//退出
				OSChinaAppManager.getAppManager().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	/**
	 * 弹出Toast消息
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void ToastMessage(Context cont, int msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void ToastMessage(Context cont, String msg,int time)
	{
		Toast.makeText(cont, msg, time).show();
	}
}
