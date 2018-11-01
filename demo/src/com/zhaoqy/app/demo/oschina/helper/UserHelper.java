/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: UserUtils.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.util
 * @Description: 登录信息
 * @author: zhaoqy
 * @date: 2015-11-18 上午10:44:52
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.helper;

import java.io.File;
import java.net.URLEncoder;
import java.util.Properties;
import android.content.Context;
import android.media.AudioManager;
import com.zhaoqy.app.demo.oschina.common.ApiClient;
import com.zhaoqy.app.demo.oschina.common.AppConfig;
import com.zhaoqy.app.demo.oschina.common.AppProperty;
import com.zhaoqy.app.demo.oschina.item.ActiveList;
import com.zhaoqy.app.demo.oschina.item.Blog;
import com.zhaoqy.app.demo.oschina.item.BlogCommentList;
import com.zhaoqy.app.demo.oschina.item.BlogList;
import com.zhaoqy.app.demo.oschina.item.CommentList;
import com.zhaoqy.app.demo.oschina.item.FavoriteList;
import com.zhaoqy.app.demo.oschina.item.FriendList;
import com.zhaoqy.app.demo.oschina.item.MessageList;
import com.zhaoqy.app.demo.oschina.item.MyInformation;
import com.zhaoqy.app.demo.oschina.item.News;
import com.zhaoqy.app.demo.oschina.item.NewsList;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Post;
import com.zhaoqy.app.demo.oschina.item.PostList;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.item.SearchList;
import com.zhaoqy.app.demo.oschina.item.Software;
import com.zhaoqy.app.demo.oschina.item.SoftwareCatalogList;
import com.zhaoqy.app.demo.oschina.item.SoftwareList;
import com.zhaoqy.app.demo.oschina.item.Tweet;
import com.zhaoqy.app.demo.oschina.item.TweetList;
import com.zhaoqy.app.demo.oschina.item.User;
import com.zhaoqy.app.demo.oschina.item.UserInformation;
import com.zhaoqy.app.demo.oschina.util.CacheUtils;
import com.zhaoqy.app.demo.oschina.util.CyptoUtils;
import com.zhaoqy.app.demo.oschina.util.FileUtils;
import com.zhaoqy.app.demo.oschina.util.NetUtils;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class UserHelper 
{
	public static final int PAGE_SIZE = 20; //默认分页大小
	private static boolean login = false;    //登录状态
	private static int loginUid = 0;	     //登录用户的id
	
	/**
	 * 初始化用户登录信息
	 */
	public static void initLoginInfo(Context context) 
	{
		User loginUser = getLoginInfo(context);
		if(loginUser != null && loginUser.getUid()>0 && loginUser.isRememberMe())
		{
			loginUid = loginUser.getUid();
			login = true;
		}
		else
		{
			Logout(context);
		}
	}
	
	/**
	 * 用户登录验证
	 * @param account
	 * @param pwd
	 * @return
	 * @throws AppException
	 */
	public static User loginVerify(Context context, String account, String pwd) 
	{
		return ApiClient.login(context, account, pwd);
	}
	
	/**
	 * 用户是否登录
	 * @return
	 */
	public static boolean isLogin() 
	{
		return login;
	}
	
	/**
	 * 判断缓存数据是否可读
	 * @param cachefile
	 * @return
	 */
	private static boolean isReadDataCache(Context context, String cachefile)
	{
		return CacheUtils.readObject(context, cachefile) != null;
	}
	
	/**
	 * 获取登录用户id
	 * @return
	 */
	public static int getLoginUid() 
	{
		return loginUid;
	}
	
	/**
	 * 用户注销
	 */
	public static void Logout(Context context) 
	{
		ApiClient.cleanCookie();
		cleanCookie(context);
		login = false;
		loginUid = 0;
	}
	
	/**
	 * 应用程序是否发出提示音
	 * @return
	 */
	public static boolean isAppSound(Context context) 
	{
		return isAudioNormal(context) && isVoice(context);
	}
	
	/**
	 * 检测当前系统声音是否为正常模式
	 * @return
	 */
	public static boolean isAudioNormal(Context context) 
	{
		@SuppressWarnings("static-access")
		AudioManager mAudioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE); 
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
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
	 * 清除保存的缓存
	 */
	public static void cleanCookie(Context context)
	{
		AppProperty.removeProperty(context, AppConfig.CONF_COOKIE);
	}
	
	/**
	 * 是否Https登录
	 * @return
	 */
	public static boolean isHttpsLogin(Context context)
	{
		String perf_httpslogin = AppProperty.getProperty(context, AppConfig.CONF_HTTPS_LOGIN);
		//默认是http
		if(StringUtils.isEmpty(perf_httpslogin))
		{
			return false;
		}
		else
		{
			return StringUtils.toBool(perf_httpslogin);
		}
	}
	
	/**
	 * 设置是是否Https登录
	 * @param b
	 */
	public static void setConfigHttpsLogin(Context context, boolean b)
	{
		AppProperty.setProperty(context, AppConfig.CONF_HTTPS_LOGIN, String.valueOf(b));
	}
	
	/**
	 * 保存登录信息
	 * @param username
	 * @param pwd
	 */
	@SuppressWarnings("serial")
	public static void saveLoginInfo(Context context, final User user) 
	{
		loginUid = user.getUid();
		login = true;
		AppProperty.setProperties(context,new Properties()
		{{
			setProperty("user.uid", String.valueOf(user.getUid()));
			setProperty("user.name", user.getName());
			setProperty("user.face", FileUtils.getFileName(user.getFace()));//用户头像-文件名
			setProperty("user.account", user.getAccount());
			setProperty("user.pwd", CyptoUtils.encode("oschinaApp",user.getPwd()));
			setProperty("user.location", user.getLocation());
			setProperty("user.followers", String.valueOf(user.getFollowers()));
			setProperty("user.fans", String.valueOf(user.getFans()));
			setProperty("user.score", String.valueOf(user.getScore()));
			setProperty("user.isRememberMe", String.valueOf(user.isRememberMe()));//是否记住我的信息
		}});		
	}
	
	/**
	 * 清除登录信息
	 */
	public static void cleanLoginInfo(Context context) 
	{
		loginUid = 0;
		login = false;
		AppProperty.removeProperty(context, "user.uid", "user.name", "user.face", "user.account", "user.pwd", 
				"user.location", "user.followers", "user.fans", "user.score", "user.isRememberMe");
	}
	
	/**
	 * 获取登录信息
	 * @return
	 */
	public static User getLoginInfo(Context context) 
	{		
		User lu = new User();		
		lu.setUid(StringUtils.toInt(AppProperty.getProperty(context, "user.uid"), 0));
		lu.setName(AppProperty.getProperty(context, "user.name"));
		lu.setFace(AppProperty.getProperty(context, "user.face"));
		lu.setAccount(AppProperty.getProperty(context, "user.account"));
		lu.setPwd(CyptoUtils.decode("oschinaApp",AppProperty.getProperty(context, "user.pwd")));
		lu.setLocation(AppProperty.getProperty(context, "user.location"));
		lu.setFollowers(StringUtils.toInt(AppProperty.getProperty(context, "user.followers"), 0));
		lu.setFans(StringUtils.toInt(AppProperty.getProperty(context, "user.fans"), 0));
		lu.setScore(StringUtils.toInt(AppProperty.getProperty(context, "user.score"), 0));
		lu.setRememberMe(StringUtils.toBool(AppProperty.getProperty(context, "user.isRememberMe")));
		return lu;
	}
	
	/**
	 * 我的个人资料
	 * @param isRefresh 是否主动刷新
	 * @return
	 * @throws AppException
	 */
	public static MyInformation getMyInformation(Context context, boolean isRefresh) 
	{
		MyInformation myinfo = null;
		String key = "myinfo_"+loginUid;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				myinfo = ApiClient.myInformation(context, loginUid);
				if(myinfo != null && myinfo.getName().length() > 0)
				{
					Notice notice = myinfo.getNotice();
					myinfo.setNotice(null);
					myinfo.setCacheKey(key);
					CacheUtils.saveObject(context, myinfo, key);
					myinfo.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				myinfo = (MyInformation)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			myinfo = (MyInformation)CacheUtils.readObject(context, key);
			if(myinfo == null)
			{
				myinfo = new MyInformation();
			}
		}
		return myinfo;
	}	
	
	/**
	 * 获取用户信息个人专页（包含该用户的动态信息以及个人信息）
	 * @param uid 自己的uid
	 * @param hisuid 被查看用户的uid
	 * @param hisname 被查看用户的用户名
	 * @param pageIndex 页面索引
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("deprecation")
	public static UserInformation getInformation(Context context, int uid, int hisuid, String hisname, int pageIndex, boolean isRefresh) 
	{
		String _hisname = ""; 
		if(!StringUtils.isEmpty(hisname))
		{
			_hisname = hisname;
		}
		UserInformation userinfo = null;
		String key = "userinfo_" + uid + "_" + hisuid + "_" + (URLEncoder.encode(hisname)) + "_" + pageIndex + "_" + PAGE_SIZE; 
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{			
			try
			{
				userinfo = ApiClient.information(context, uid, hisuid, _hisname, pageIndex, PAGE_SIZE);
				if(userinfo != null && pageIndex == 0)
				{
					Notice notice = userinfo.getNotice();
					userinfo.setNotice(null);
					userinfo.setCacheKey(key);
					CacheUtils.saveObject(context, userinfo, key);
					userinfo.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				userinfo = (UserInformation)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			userinfo = (UserInformation)CacheUtils.readObject(context, key);
			if(userinfo == null)
			{
				userinfo = new UserInformation();
			}
		}
		return userinfo;
	}
	
	/**
	 * 更新用户之间关系（加关注、取消关注）
	 * @param uid 自己的uid
	 * @param hisuid 对方用户的uid
	 * @param newrelation 0:取消对他的关注 1:关注他
	 * @return
	 * @throws AppException
	 */
	public static Result updateRelation(Context context, int uid, int hisuid, int newrelation) 
	{
		return ApiClient.updateRelation(context, uid, hisuid, newrelation);
	}
	
	/**
	 * 更新用户头像
	 * @param portrait 新上传的头像
	 * @return
	 * @throws AppException
	 */
	public static Result updatePortrait(Context context, File portrait) 
	{
		return ApiClient.updatePortrait(context, loginUid, portrait);
	}
	
	/**
	 * 清空通知消息
	 * @param uid
	 * @param type 1:@我的信息 2:未读消息 3:评论个数 4:新粉丝个数
	 * @return
	 * @throws AppException
	 */
	public static Result noticeClear(Context context, int uid, int type) 
	{
		return ApiClient.noticeClear(context, uid, type);
	}
	
	/**
	 * 获取用户通知信息
	 * @param uid
	 * @return
	 * @throws AppException
	 */
	public static Notice getUserNotice(Context context, int uid) 
	{
		return ApiClient.getUserNotice(context, uid);
	}
	
	/**
	 * 用户收藏列表
	 * @param type 0:全部收藏 1:软件 2:话题 3:博客 4:新闻 5:代码
	 * @param pageIndex 页面索引 0表示第一页
	 * @return
	 * @throws AppException
	 */
	public static FavoriteList getFavoriteList(Context context, int type, int pageIndex, boolean isRefresh)
	{
		FavoriteList list = null;
		String key = "favoritelist_"+loginUid+"_"+type+"_"+pageIndex+"_"+PAGE_SIZE; 
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getFavoriteList(context, loginUid, type, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (FavoriteList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (FavoriteList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new FavoriteList();
			}
		}
		return list;
	}
	
	/**
	 * 用户粉丝、关注人列表
	 * @param relation 0:显示自己的粉丝 1:显示自己的关注者
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static FriendList getFriendList(Context context, int relation, int pageIndex, boolean isRefresh) 
	{
		FriendList list = null;
		String key = "friendlist_" + loginUid + "_" + relation + "_" + pageIndex + "_" + PAGE_SIZE; 
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getFriendList(context, loginUid, relation, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (FriendList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (FriendList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new FriendList();
			}
		}
		return list;
	}
	
	/**
	 * 新闻列表
	 * @param catalog
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ApiException
	 */
	public static NewsList getNewsList(Context context, int catalog, int pageIndex, boolean isRefresh) 
	{
		NewsList list = null;
		String key = "newslist_" + catalog + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getNewsList(context, catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (NewsList)CacheUtils.readObject(context, key);
			}		
		} 
		else 
		{
			list = (NewsList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new NewsList();
			}
		}
		return list;
	}
	
	/**
	 * 新闻详情
	 * @param news_id
	 * @return
	 * @throws ApiException
	 */
	public static News getNews(Context context, int news_id, boolean isRefresh) 
	{		
		News news = null;
		String key = "news_" + news_id;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				news = ApiClient.getNewsDetail(context, news_id);
				if(news != null)
				{
					Notice notice = news.getNotice();
					news.setNotice(null);
					news.setCacheKey(key);
					CacheUtils.saveObject(context, news, key);
					news.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				news = (News)CacheUtils.readObject(context, key);

			}
		} 
		else 
		{
			news = (News)CacheUtils.readObject(context, key);
			if(news == null)
			{
				news = new News();
			}
		}
		return news;		
	}
	
	/**
	 * 用户博客列表
	 * @param authoruid
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("deprecation")
	public static BlogList getUserBlogList(Context context, int authoruid, String authorname, int pageIndex, boolean isRefresh) 
	{
		BlogList list = null;
		String key = "userbloglist_" + authoruid + "_" + (URLEncoder.encode(authorname)) + "_" + loginUid + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getUserBlogList(context, authoruid, authorname, loginUid, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (BlogList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (BlogList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new BlogList();
			}
		}
		return list;
	}
	
	/**
	 * 博客列表
	 * @param type 推荐：recommend 最新：latest
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static BlogList getBlogList(Context context, String type, int pageIndex, boolean isRefresh) 
	{
		BlogList list = null;
		String key = "bloglist_" + type + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getBlogList(context, type, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (BlogList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (BlogList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new BlogList();
			}
		}
		return list;
	}
	
	/**
	 * 博客详情
	 * @param blog_id
	 * @return
	 * @throws AppException
	 */
	public static Blog getBlog(Context context, int blog_id, boolean isRefresh) 
	{
		Blog blog = null;
		String key = "blog_" + blog_id;
		if(NetUtils.isNetworkConnected(context) && (!CacheUtils.isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				blog = ApiClient.getBlogDetail(context, blog_id);
				if(blog != null)
				{
					Notice notice = blog.getNotice();
					blog.setNotice(null);
					blog.setCacheKey(key);
					CacheUtils.saveObject(context, blog, key);
					blog.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				blog = (Blog)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			blog = (Blog)CacheUtils.readObject(context, key);
			if(blog == null)
			{
				blog = new Blog();
			}
		}
		return blog;
	}
	
	/**
	 * 软件列表
	 * @param searchTag 软件分类  推荐:recommend 最新:time 热门:view 国产:list_cn
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static SoftwareList getSoftwareList(Context context, String searchTag, int pageIndex, boolean isRefresh) 
	{
		SoftwareList list = null;
		String key = "softwarelist_" + searchTag + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getSoftwareList(context, searchTag, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (SoftwareList)CacheUtils.readObject(context, key);
			}
		}
		else 
		{
			list = (SoftwareList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new SoftwareList();
			}
		}
		return list;
	}
	
	/**
	 * 软件分类的软件列表
	 * @param searchTag 从softwarecatalog_list获取的tag
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static SoftwareList getSoftwareTagList(Context context, int searchTag, int pageIndex, boolean isRefresh) 
	{
		SoftwareList list = null;
		String key = "softwaretaglist_" + searchTag + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (CacheUtils.isCacheDataFailure(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getSoftwareTagList(context, searchTag, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (SoftwareList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (SoftwareList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new SoftwareList();
			}
		}
		return list;
	}
	
	/**
	 * 软件分类列表
	 * @param tag 第一级:0  第二级:tag
	 * @return
	 * @throws AppException
	 */
	public static SoftwareCatalogList getSoftwareCatalogList(Context context, int tag) 
	{
		SoftwareCatalogList list = null;
		String key = "softwarecataloglist_" + tag;
		if(NetUtils.isNetworkConnected(context) && CacheUtils.isCacheDataFailure(context, key)) 
		{
			try
			{
				list = ApiClient.getSoftwareCatalogList(context, tag);
				if(list != null)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (SoftwareCatalogList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (SoftwareCatalogList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new SoftwareCatalogList();
			}
		}
		return list;
	}
	
	/**
	 * 软件详情
	 * @param soft_id
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("deprecation")
	public static Software getSoftware(Context context, String ident, boolean isRefresh) 
	{
		Software soft = null;
		String key = "software_" + (URLEncoder.encode(ident));
		if(NetUtils.isNetworkConnected(context) && (CacheUtils.isCacheDataFailure(context, key) || isRefresh)) 
		{
			try
			{
				soft = ApiClient.getSoftwareDetail(context, ident);
				if(soft != null)
				{
					Notice notice = soft.getNotice();
					soft.setNotice(null);
					soft.setCacheKey(key);
					CacheUtils.saveObject(context, soft, key);
					soft.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				soft = (Software)CacheUtils.readObject(context, key);
			}
		}
		else 
		{
			soft = (Software)CacheUtils.readObject(context, key);
			if(soft == null)
			{
				soft = new Software();
			}
		}
		return soft;
	}
	
	/**
	 * 帖子列表
	 * @param catalog
	 * @param pageIndex
	 * @return
	 * @throws ApiException
	 */
	public static PostList getPostList(Context context, int catalog, int pageIndex, boolean isRefresh)
	{
		PostList list = null;
		String key = "postlist_" + catalog + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{		
			try
			{
				list = ApiClient.getPostList(context, catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (PostList)CacheUtils.readObject(context, key);
			}
		}
		else 
		{
			list = (PostList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new PostList();
			}
		}
		return list;
	}
	
	/**
	 * Tag相关帖子列表
	 * @param tag
	 * @param pageIndex
	 * @return
	 * @throws ApiException
	 */
	@SuppressWarnings("deprecation")
	public static PostList getPostListByTag(Context context, String tag, int pageIndex, boolean isRefresh) 
	{
		PostList list = null;
		String key = "postlist_" + (URLEncoder.encode(tag)) + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{		
			try
			{
				list = ApiClient.getPostListByTag(context, tag, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (PostList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (PostList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new PostList();
			}
		}
		return list;
	}
	
	/**
	 * 读取帖子详情
	 * @param post_id
	 * @return
	 * @throws ApiException
	 */
	public static Post getPost(Context context, int post_id, boolean isRefresh) 
	{		
		Post post = null;
		String key = "post_" + post_id;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{	
			try
			{
				post = ApiClient.getPostDetail(context, post_id);
				if(post != null)
				{
					Notice notice = post.getNotice();
					post.setNotice(null);
					post.setCacheKey(key);
					CacheUtils.saveObject(context, post, key);
					post.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				post = (Post)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			post = (Post)CacheUtils.readObject(context, key);
			if(post == null)
			{
				post = new Post();
			}
		}
		return post;		
	}
	
	/**
	 * 动弹列表
	 * @param catalog -1 热门，0 最新，大于0 某用户的动弹(uid)
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static TweetList getTweetList(Context context, int catalog, int pageIndex, boolean isRefresh) 
	{
		TweetList list = null;
		String key = "tweetlist_" + catalog + "_" + pageIndex + "_" + PAGE_SIZE;		
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getTweetList(context, catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (TweetList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (TweetList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new TweetList();
			}
		}
		return list;
	}
	
	/**
	 * 获取动弹详情
	 * @param tweet_id
	 * @return
	 * @throws AppException
	 */
	public static Tweet getTweet(Context context, int tweet_id, boolean isRefresh) 
	{
		Tweet tweet = null;
		String key = "tweet_" + tweet_id;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				tweet = ApiClient.getTweetDetail(context, tweet_id);
				if(tweet != null)
				{
					Notice notice = tweet.getNotice();
					tweet.setNotice(null);
					tweet.setCacheKey(key);
					CacheUtils.saveObject(context, tweet, key);
					tweet.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				tweet = (Tweet)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			tweet = (Tweet)CacheUtils.readObject(context, key);
			if(tweet == null)
			{
				tweet = new Tweet();
			}
		}
		return tweet;
	}
	
	/**
	 * 动态列表
	 * @param catalog 1最新动态 2@我 3评论 4我自己
	 * @param id
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static ActiveList getActiveList(Context context, int catalog, int pageIndex, boolean isRefresh) 
	{
		ActiveList list = null;
		String key = "activelist_" + loginUid + "_" + catalog + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getActiveList(context, loginUid, catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (ActiveList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (ActiveList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new ActiveList();
			}
		}
		return list;
	}
	
	/**
	 * 留言列表
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static MessageList getMessageList(Context context, int pageIndex, boolean isRefresh) 
	{
		MessageList list = null;
		String key = "messagelist_" + loginUid + "_" + pageIndex + "_" + PAGE_SIZE;
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getMessageList(context, loginUid, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (MessageList)CacheUtils.readObject(context, key);
			}
		}
		else 
		{
			list = (MessageList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new MessageList();
			}
		}
		return list;
	}
	
	/**
	 * 博客评论列表
	 * @param id 博客Id
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static BlogCommentList getBlogCommentList(Context context, int id, int pageIndex, boolean isRefresh) 
	{
		BlogCommentList list = null;
		String key = "blogcommentlist_" + id + "_" + pageIndex + "_" + PAGE_SIZE;		
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getBlogCommentList(context, id, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (BlogCommentList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (BlogCommentList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new BlogCommentList();
			}
		}
		return list;
	}
	
	/**
	 * 评论列表
	 * @param catalog 1新闻 2帖子 3动弹 4动态
	 * @param id 某条新闻，帖子，动弹的id 或者某条留言的friendid
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public static CommentList getCommentList(Context context, int catalog, int id, int pageIndex, boolean isRefresh) 
	{
		CommentList list = null;
		String key = "commentlist_" + catalog + "_" + id + "_" + pageIndex + "_" + PAGE_SIZE;		
		if(NetUtils.isNetworkConnected(context) && (!isReadDataCache(context, key) || isRefresh)) 
		{
			try
			{
				list = ApiClient.getCommentList(context, catalog, id, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0)
				{
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					CacheUtils.saveObject(context, list, key);
					list.setNotice(notice);
				}
			}
			catch(Exception e)
			{
				list = (CommentList)CacheUtils.readObject(context, key);
			}
		} 
		else 
		{
			list = (CommentList)CacheUtils.readObject(context, key);
			if(list == null)
			{
				list = new CommentList();
			}
		}
		return list;
	}
	
	/**
	 * 获取搜索列表
	 * @param catalog 全部:all 新闻:news  问答:post 软件:software 博客:blog 代码:code
	 * @param content 搜索的内容
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws AppException
	 */
	public static SearchList getSearchList(Context context, String catalog, String content, int pageIndex, int pageSize) 
	{
		return ApiClient.getSearchList(context, catalog, content, pageIndex, pageSize);
	}
	
	/**
	 * 发帖子
	 * @param post （uid、title、catalog、content、isNoticeMe）
	 * @return
	 * @throws AppException
	 */
	public static Result pubPost(Context context, Post post)
	{
		return ApiClient.pubPost(context, post);
	}
	
	/**
	 * 发动弹
	 * @param Tweet-uid & msg & image
	 * @return
	 * @throws AppException
	 */
	public static Result pubTweet(Context context, Tweet tweet) 
	{
		return ApiClient.pubTweet(context, tweet);
	}
	
	/**
	 * 删除动弹
	 * @param uid
	 * @param tweetid
	 * @return
	 * @throws AppException
	 */
	public static Result delTweet(Context context, int uid, int tweetid) 
	{
		return ApiClient.delTweet(context, uid, tweetid);
	}
	
	/**
	 * 发送留言
	 * @param uid 登录用户uid
	 * @param receiver 接受者的用户id
	 * @param content 消息内容，注意不能超过250个字符
	 * @return
	 * @throws AppException
	 */
	public static Result pubMessage(Context context, int uid, int receiver, String content)
	{
		return ApiClient.pubMessage(context, uid, receiver, content);
	}
	
	/**
	 * 转发留言
	 * @param uid 登录用户uid
	 * @param receiver 接受者的用户名
	 * @param content 消息内容，注意不能超过250个字符
	 * @return
	 * @throws AppException
	 */
	public static Result forwardMessage(Context context, int uid, String receiver, String content) 
	{
		return ApiClient.forwardMessage(context, uid, receiver, content);
	}
	
	/**
	 * 删除留言
	 * @param uid 登录用户uid
	 * @param friendid 留言者id
	 * @return
	 * @throws AppException
	 */
	public static Result delMessage(Context context, int uid, int friendid) 
	{
		return ApiClient.delMessage(context, uid, friendid);
	}
	
	/**
	 * 发表评论
	 * @param catalog 1新闻  2帖子  3动弹  4动态
	 * @param id 某条新闻，帖子，动弹的id
	 * @param uid 用户uid
	 * @param content 发表评论的内容
	 * @param isPostToMyZone 是否转发到我的空间  0不转发  1转发
	 * @return
	 * @throws AppException
	 */
	public static Result pubComment(Context context, int catalog, int id, int uid, String content, int isPostToMyZone) 
	{
		return ApiClient.pubComment(context, catalog, id, uid, content, isPostToMyZone);
	}
	
	/**
	 * 
	 * @param id 表示被评论的某条新闻，帖子，动弹的id 或者某条消息的 friendid 
	 * @param catalog 表示该评论所属什么类型：1新闻  2帖子  3动弹  4动态
	 * @param replyid 表示被回复的单个评论id
	 * @param authorid 表示该评论的原始作者id
	 * @param uid 用户uid 一般都是当前登录用户uid
	 * @param content 发表评论的内容
	 * @return
	 * @throws AppException
	 */
	public static Result replyComment(Context context, int id, int catalog, int replyid, int authorid, int uid, String content) 
	{
		return ApiClient.replyComment(context, id, catalog, replyid, authorid, uid, content);
	}
	
	/**
	 * 删除评论
	 * @param id 表示被评论对应的某条新闻,帖子,动弹的id 或者某条消息的 friendid
	 * @param catalog 表示该评论所属什么类型：1新闻  2帖子  3动弹  4动态&留言
	 * @param replyid 表示被回复的单个评论id
	 * @param authorid 表示该评论的原始作者id
	 * @return
	 * @throws AppException
	 */
	public static Result delComment(Context context, int id, int catalog, int replyid, int authorid) 
	{
		return ApiClient.delComment(context, id, catalog, replyid, authorid);
	}
	
	/**
	 * 发表博客评论
	 * @param blog 博客id
	 * @param uid 登陆用户的uid
	 * @param content 评论内容
	 * @return
	 * @throws AppException
	 */
	public static Result pubBlogComment(Context context, int blog, int uid, String content) 
	{
		return ApiClient.pubBlogComment(context, blog, uid, content);
	}
	
	/**
	 * 发表博客评论
	 * @param blog 博客id
	 * @param uid 登陆用户的uid
	 * @param content 评论内容
	 * @param reply_id 评论id
	 * @param objuid 被评论的评论发表者的uid
	 * @return
	 * @throws AppException
	 */
	public static Result replyBlogComment(Context context, int blog, int uid, String content, int reply_id, int objuid) 
	{
		return ApiClient.replyBlogComment(context, blog, uid, content, reply_id, objuid);
	}
	
	/**
	 * 删除博客评论
	 * @param uid 登录用户的uid
	 * @param blogid 博客id
	 * @param replyid 评论id
	 * @param authorid 评论发表者的uid
	 * @param owneruid 博客作者uid
	 * @return
	 * @throws AppException
	 */
	public static Result delBlogComment(Context context, int uid, int blogid, int replyid, int authorid, int owneruid) 
	{
		return ApiClient.delBlogComment(context, uid, blogid, replyid, authorid, owneruid);
	}
	
	/**
	 * 删除博客
	 * @param uid 登录用户的uid
	 * @param authoruid 博客作者uid
	 * @param id 博客id
	 * @return
	 * @throws AppException
	 */
	public static Result delBlog(Context context, int uid, int authoruid, int id) 
	{ 	
		return ApiClient.delBlog(context, uid, authoruid, id);
	}
	
	/**
	 * 用户添加收藏
	 * @param uid 用户UID
	 * @param objid 比如是新闻ID 或者问答ID 或者动弹ID
	 * @param type 1:软件 2:话题 3:博客 4:新闻 5:代码
	 * @return
	 * @throws AppException
	 */
	public static Result addFavorite(Context context, int uid, int objid, int type) 
	{
		return ApiClient.addFavorite(context, uid, objid, type);
	}
	
	/**
	 * 用户删除收藏
	 * @param uid 用户UID
	 * @param objid 比如是新闻ID 或者问答ID 或者动弹ID
	 * @param type 1:软件 2:话题 3:博客 4:新闻 5:代码
	 * @return
	 * @throws AppException
	 */
	public static Result delFavorite(Context context, int uid, int objid, int type) 
	{ 	
		return ApiClient.delFavorite(context, uid, objid, type);
	}
}
