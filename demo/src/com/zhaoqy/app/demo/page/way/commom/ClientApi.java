package com.zhaoqy.app.demo.page.way.commom;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhaoqy.app.demo.page.way.item.Activity;
import com.zhaoqy.app.demo.page.way.item.Choice;
import com.zhaoqy.app.demo.page.way.item.ChoiceDetail;
import com.zhaoqy.app.demo.page.way.item.Special;
import com.zhaoqy.app.demo.page.way.item.UserInfo;

public class ClientApi 
{
	private static String startId;

	public ClientApi() 
	{
	}

	public static JSONObject ParseJson(final String path, final String encode) 
	{
		HttpClient httpClient = new DefaultHttpClient();
		//HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		//HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpPost httpPost = new HttpPost(path);
		try 
		{
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) 
			{
				String result = EntityUtils.toString(httpResponse.getEntity(), encode);
				JSONObject jsonObject = new JSONObject(result);
				return jsonObject;
			}
		} 
		catch (Exception e) 
		{
			return null;
		} 
		finally 
		{
			if (httpClient != null)
			{
				httpClient.getConnectionManager().shutdown();
			}	
		}
		return null;
	}

	public static ArrayList<Choice> getChoiceList(String Url) 
	{
		ArrayList<Choice> list = new ArrayList<Choice>();
		JSONObject json = ParseJson(Url, "utf-8");
		if (json == null) 
		{
			return null;
		} 
		else 
		{
			try 
			{
				JSONArray Data = json.getJSONObject("obj").getJSONArray("list");
				for (int i=0; i<Data.length(); i++) 
				{
					JSONObject data = Data.getJSONObject(i);
					Choice choice = new Choice();
					choice.setId(data.optString("id"));
					JSONObject element = data.getJSONObject("tour");
					choice.setTitle(element.optString("title"));
					choice.setPubdate(element.optString("startdate"));
					choice.setPictureCount(element.optString("cntP"));
					choice.setImage(element.optString("coverpic"));
					choice.setViewCount(element.optString("pcolor"));
					choice.setFavoriteCount(element.getString("likeCnt"));
					choice.setViewCount(element.optString("viewCnt"));
					choice.setForeword(element.optString("foreword"));
					UserInfo userInfo = new UserInfo();
					JSONObject owner = element.optJSONObject("owner");
					userInfo.setUsername(owner.optString("username"));
					userInfo.setNickname(owner.optString("nickname"));
					userInfo.setUserId(owner.optString("userid"));
					userInfo.setAvatar(owner.getString("avatar"));
					choice.setUserInfo(userInfo);
					JSONArray dispcitys = element.getJSONArray("dispCities");
					String[] citys = new String[dispcitys.length()];
					for (int j=0; j<dispcitys.length(); j++) 
					{
						citys[j] = dispcitys.optString(j);
					}
					choice.setDispCities(citys);
					choice.setCmtCount(element.getString("cntcmt"));
					choice.setTourId(element.optString("id"));
					list.add(choice);
					if (i == Data.length() - 1) 
					{
						startId = choice.getId();
					}
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			return list;
		}
	}

	public static String getStartId() 
	{
		return startId;
	}

	public static ArrayList<ChoiceDetail> getChoiceDetailList(String tourId) 
	{
		ArrayList<ChoiceDetail> list = new ArrayList<ChoiceDetail>();
		URL.setId(tourId);
		JSONObject json = ParseJson(URL.CHOICE_DETAIL_URL, "utf-8");
		if (json == null) 
		{
			return null;
		} 
		else 
		{
			try 
			{
				JSONArray Data = json.getJSONObject("obj").getJSONArray("records");
				for (int i=0; i<Data.length(); i++) 
				{
					JSONObject data = Data.getJSONObject(i);
					ChoiceDetail choiceDetail = new ChoiceDetail();
					choiceDetail.setText(data.getString("words"));
					choiceDetail.setImage("http://img.117go.com/timg/p308/" + data.getString("picfile"));
					list.add(choiceDetail);
				}
			}
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			return list;
		}
	}
	
	public static ArrayList<ArrayList<Special>> getSpecialList() 
	{
		ArrayList<ArrayList<Special>> special_list = new ArrayList<ArrayList<Special>>();
		JSONObject json = ParseJson(URL.SPECIAL_URL, "utf-8");
		if (json == null) 
		{
			return null;
		} 
		else 
		{
			try 
			{
				JSONArray data1 = json.getJSONObject("obj").getJSONArray("trip_list");
				ArrayList<Special> special1 = new ArrayList<Special>();
				for (int i=0; i<data1.length(); i++) 
				{
					Special special = new Special();
					JSONObject data = data1.getJSONObject(i);
					special.setId(data.getString("id"));
					special.setName(data.getString("name"));
					special.setIamge("http://img.117go.com/timg/p616/"+data.getString("coverpic"));
					special1.add(special);
				}
				
				JSONArray data2 = json.getJSONObject("obj").getJSONArray("pic_list");
				ArrayList<Special> special2 = new ArrayList<Special>();
				for (int i=0; i<data2.length(); i++) 
				{
					Special special = new Special();
					JSONObject data = data2.getJSONObject(i);
					special.setId(data.getString("id"));
					special.setName(data.getString("name"));
					special.setIamge("http://img.117go.com/timg/p616/"+data.getString("coverpic"));
					special2.add(special);
				}
				
				special_list.add(special1);
				special_list.add(special2);
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			return special_list;
		}
	}
	
	public static ArrayList<Choice> getSpecialDetailTour(String searchId)
	{
		ArrayList<Choice> list = new ArrayList<Choice>();
		URL.setId(searchId);
		JSONObject json = ParseJson(URL.SPECIAL_DETAIL_URL, "utf-8");
		if (json == null) 
		{
			return null;
		} 
		else 
		{
			try 
			{
				JSONArray Data = json.getJSONObject("obj").getJSONArray("items");
				for (int i=0; i<Data.length(); i++) 
				{
					JSONObject data = Data.getJSONObject(i);
					Choice choice = new Choice();
					choice.setId(data.optString("id"));
					JSONObject element = data.getJSONObject("tour");
					choice.setTitle(element.optString("title"));
					choice.setPubdate(element.optString("startdate"));
					choice.setPictureCount(element.optString("cntP"));
					choice.setImage(element.optString("coverpic"));
					choice.setViewCount(element.optString("pcolor"));
					choice.setFavoriteCount(element.getString("likeCnt"));
					choice.setViewCount(element.optString("viewCnt"));
					choice.setForeword(element.optString("foreword"));
					UserInfo userInfo = new UserInfo();
					JSONObject owner = element.optJSONObject("owner");
					userInfo.setUsername(owner.optString("username"));
					userInfo.setNickname(owner.optString("nickname"));
					userInfo.setUserId(owner.optString("userid"));
					userInfo.setAvatar(owner.getString("avatar"));
					choice.setUserInfo(userInfo);
					JSONArray dispcitys = element.getJSONArray("dispCities");
					String[] citys = new String[dispcitys.length()];
					for (int j=0; j<dispcitys.length(); j++) 
					{
						citys[j] = dispcitys.optString(j);
					}
					choice.setDispCities(citys);
					choice.setCmtCount(element.getString("cntcmt"));
					choice.setTourId(element.optString("id"));
					list.add(choice);
					if (i == Data.length() - 1) 
					{
						startId = choice.getId();
					}
				}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			return list;
		}
	}
	
	public static ArrayList<Activity> getActivityList()
	{
		ArrayList<Activity> list = new ArrayList<Activity>();
		JSONObject json = ParseJson(URL.ACTIVITY_URL, "utf-8");
		if (json == null) 
		{
			return null;
		} 
		else 
		{
			try 
			{
				JSONArray Data = json.getJSONArray("obj");
				for (int i=0; i<Data.length(); i++) 
				{
					Activity activity = new Activity();
					JSONObject data = Data.getJSONObject(i);
					activity.setId(data.getString("id"));
					activity.setName(data.getString("name"));
					activity.setIamge("http://img.117go.com/timg/p616/" + data.getString("coverpic"));
					activity.setUrlS(data.getString("url"));
					list.add(activity);
				}
			}
			catch (Exception e) 
			{
			}
		}
		return list;
	}
}
