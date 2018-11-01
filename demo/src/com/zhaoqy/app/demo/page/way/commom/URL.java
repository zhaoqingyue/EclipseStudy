package com.zhaoqy.app.demo.page.way.commom;

public class URL 
{
	private static String mId;
	
	public static void setId(String id)
	{
		mId = id;
	}
	
	public final static String REFRESH_URL = "http://app.117go.com/demo27/php/plaza.php?submit=getPlaza4&startId=0&fetchNewer=1&length=20&type=0&isWaterfall=0&token=&v=a5.0.4&vc=anzhi&vd=f2e4ee47505f6fba";
	public final static String LOAD_URL = "http://app.117go.com/demo27/php/plaza.php?submit=getPlaza4&startId=" + ClientApi.getStartId() + "&fetchNewer=0&length=20&type=0&isWaterfall=0&token=&v=a5.0.4&vc=anzhi&vd=f2e4ee47505f6fba";
	public final static String SPECIAL_URL = "http://app.117go.com/demo27/php/gloryAction.php?submit=getGlory&vc=wandoujia&vd=80f117eb4244b778&v=a5.0.6";
	public final static String ACTIVITY_URL = "http://app.117go.com/demo27/php/eventAction.php?submit=getEventList&startId=0&length=20&vc=wandoujia&vd=80f117eb4244b778&v=a5.0.6";
	public final static String CHOICE_DETAIL_URL = "http://app.117go.com/demo27/php/formAction.php?submit=getATour2&tourid=" + mId + "&recType=1&refer=PlazaHome&ID2=1&token=3a79c4024f682aee74723a419f6605f9&v=a5.0.4&vc=anzhi&vd=f2e4ee47505f6fba";
	public final static String SPECIAL_DETAIL_URL = "http://app.117go.com/demo27/php/searchAction.php?submit=getSearchTours&searchid=" + mId + "&startId=0&length=20&fetchNewer=1&vc=wandoujia&vd=80f117eb4244b778&v=a5.0.6";
}
