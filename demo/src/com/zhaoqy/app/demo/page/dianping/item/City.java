package com.zhaoqy.app.demo.page.dianping.item;

public class City 
{
	private int    id;
	private String name;
	private String sortKey;
	
	public City() 
	{
	}
	
	public City(int id, String name, String sortKey) 
	{
		this.id = id;
		this.name = name;
		this.sortKey = sortKey;
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getSortKey() 
	{
		return sortKey;
	}
	
	public void setSortKey(String sortKey) 
	{
		this.sortKey = sortKey;
	}
}
