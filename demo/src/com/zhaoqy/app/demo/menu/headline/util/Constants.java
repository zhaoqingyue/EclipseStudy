package com.zhaoqy.app.demo.menu.headline.util;

import java.util.ArrayList;
import java.util.List;

import com.zhaoqy.app.demo.menu.headline.item.NewsClassify;
import com.zhaoqy.app.demo.menu.headline.item.NewsEntity;

public class Constants 
{
	public final static int mark_recom = 0;
	public final static int mark_hot = 1;
	public final static int mark_frist = 2;
	public final static int mark_exclusive = 3;
	public final static int mark_favor = 4;
	
	public static ArrayList<NewsClassify> getData() 
	{
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(0);
		classify.setTitle("推荐");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1);
		classify.setTitle("热点");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(2);
		classify.setTitle("视频");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(3);
		classify.setTitle("本地");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(4);
		classify.setTitle("社会");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(5);
		classify.setTitle("订阅");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(6);
		classify.setTitle("娱乐");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(7);
		classify.setTitle("科技");
		newsClassify.add(classify);
		return newsClassify;
	}
	
	public static ArrayList<NewsEntity> getNewsList() 
	{
		ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
		for(int i=0 ; i<10 ; i++)
		{
			NewsEntity news = new NewsEntity();
			news.setId(i);
			news.setNewsId(i);
			news.setCollectStatus(false);
			news.setCommentNum(i + 10);
			news.setInterestedStatus(true);
			news.setLikeStatus(true);
			news.setReadStatus(false);
			news.setNewsCategory("000000000");
			news.setNewsCategoryId(1);
			news.setTitle("关于交往: 男人的自我修养");
			List<String> url_list = new ArrayList<String>();
			if(i%2 == 1)
			{
				String url1 = "http://infopic.gtimg.com/qq_news/digi/pics/102/102066/102066094_400_640.jpg";
				String url2 = "http://infopic.gtimg.com/qq_news/digi/pics/102/102066/102066096_400_640.jpg";
				String url3 = "http://infopic.gtimg.com/qq_news/digi/pics/102/102066/102066099_400_640.jpg";
				news.setPicOne(url1);
				news.setPicTwo(url2);
				news.setPicThr(url3);
				url_list.add(url1);
				url_list.add(url2);
				url_list.add(url3);
			}
			else
			{
				news.setTitle("AA用车: 智能租车平台");
				String url = "http://r3.sinaimg.cn/2/2014/0417/a7/6/92478595/580x1000x75x0.jpg";
				news.setPicOne(url);
				url_list.add(url);
			}
			news.setPicList(url_list);
			news.setPublishTime(Long.valueOf(i));
			news.setReadStatus(false);
			news.setSource("手机腾讯网");
			news.setSummary("44444");
			news.setMark(i);
			if(i == 4)
			{
				news.setTitle("狼牙榜");
				news.setLocal("推广");
				news.setIsLarge(true);
				String url = "http://imgt2.bdstatic.com/it/u=3269155243,2604389213&fm=21&gp=0.jpg";
				news.setPicOne(url);
				url_list.clear();
				url_list.add(url);
			}
			else
			{
				news.setIsLarge(false);
			}
			if(i == 2)
			{
				news.setComment("演技不错");
			}
			newsList.add(news);
		}
		return newsList;
	}
}
