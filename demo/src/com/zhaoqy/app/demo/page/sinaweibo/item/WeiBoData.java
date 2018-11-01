package com.zhaoqy.app.demo.page.sinaweibo.item;

import java.util.ArrayList;

public class WeiBoData 
{
	public static ArrayList<Statuses> getStatusesList()
	{
		ArrayList<Statuses> statuses = new ArrayList<Statuses>();
		
		for (int i=0; i<10; i++) 
		{
			Statuses item = new Statuses();
			item.setId(i);
			item.setText("求关注");
			item.setSource("新浪微博");
			item.setCreated_at("Tue May 31 17:46:55 +0800 2015");
			item.setComments_count(i);
			item.setReposts_count(i + 2);
			item.setAttitudes_count(i + 5);
			item.setUser(getUser());
			statuses.add(item);
		}
		return statuses;
	}
	
	public static Statuses getStatuses()
	{
		Statuses item = new Statuses();
		item.setId(122222);
		item.setText("求关注");
		item.setSource("新浪微博");
		item.setCreated_at("Tue May 31 17:46:55 +0800 2015");
		item.setComments_count(10);
		item.setReposts_count(22);
		item.setAttitudes_count(35);
		item.setUser(getUser());
		return item;
	}
	
	public static Statuses getCurStatuses()
	{
		Statuses item = new Statuses();
		item.setId(1000000000);
		item.setText("求关注");
		item.setSource("新浪微博");
		item.setCreated_at("Tue May 31 17:46:55 +0800 2015");
		item.setComments_count(10);
		item.setReposts_count(12);
		item.setAttitudes_count(15);
		item.setUser(getUser());
		return item;
	}
	
	public static User getUser()
	{
		User user = new User();
		user.setId(1404376560);
		user.setScreen_name("zhaoqy");
		user.setLocation("北京 朝阳区");
		user.setDescription("人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。");
		user.setFavourites_count(100);
		user.setFollow_me(true);
		user.setFollowers_count(300);
		user.setFriends_count(1000);
		user.setGender("男");
		return user;
	}
	
	public static ArrayList<User> getUserList()
	{
		ArrayList<User> users = new ArrayList<User>();
		
		for (int i=0; i<10; i++) 
		{
			User item = new User();
			item.setId(1404376560);
			item.setScreen_name("zhaoqy");
			item.setLocation("北京 朝阳区");
			item.setDescription("人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。");
			item.setFavourites_count(100);
			item.setFollow_me(true);
			item.setFollowers_count(300);
			item.setFriends_count(1000);
			item.setGender("男");
			users.add(item);
		}
		return users;
	}
	
	public static ArrayList<Comments> getCommentList()
	{
		ArrayList<Comments> comments = new ArrayList<Comments>();
		
		for (int i=0; i<10; i++) 
		{
			Comments item = new Comments();
			item.setId(i);
			item.setText("评论的内容");
			item.setSource("新浪微博");
			item.setCreated_at("Tue May 31 17:46:55 +0800 2015");
			item.setStatus(getStatuses());
			item.setUser(getUser());
			comments.add(item);
		}
		
		return comments;
	}
}
