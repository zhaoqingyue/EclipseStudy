package com.zhaoqy.app.demo.picture.process;

public class Factory 
{
	private static IDataTrafficManage dataTrafficManage = new DataTrafficManage();

	public static IDataTrafficManage getDataTrafficManage() 
	{
		return dataTrafficManage;
	}
}
