package com.zhaoqy.app.demo.picture.process;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DataTrafficManage implements IDataTrafficManage 
{
	private final static String tag = "demo";
	private static CookieStore cookieStore;
	private static String URL = "http://10.1.12.229:8080/gwbzb";

	public static String post(String action, List<NameValuePair> params) 
	{
		String result = null;
		try 
		{
			String url = URL + action;
			Log.d(tag, "url is" + url);
			HttpPost httpPost = new HttpPost(url);

			if (params != null && params.size() > 0) 
			{
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				httpPost.setEntity(entity);
			}
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=\"UTF-8\"");
			httpPost.addHeader("user-agent", "Mozilla/5.0 (Linux; U; Android 4.0.1; zh-cn; sdk Build/ICS_MR0)"
							+ " AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			if (cookieStore != null) 
			{
				httpClient.setCookieStore(cookieStore);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) 
			{
				builder.append(s);
			}
			result = builder.toString();
			cookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
		} 
		catch (Exception e) 
		{
			Log.e(tag, e.toString(), e);
		}
		return result;
	}

	public String login(String username, String password) 
	{
		String result = null;
		try 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			NameValuePair nvUserName = new BasicNameValuePair("userName", username);
			NameValuePair nvuserPass = new BasicNameValuePair("userPass", password);
			params.add(nvUserName);
			params.add(nvuserPass);
			String action = "/login.do";
			cookieStore = null;
			result = post(action , params);
			if(result != null && result.startsWith("<html>"))
			{
				result = null;
			}
			Log.d(tag, "result is ( " + result + " )");
		} 
		catch (Exception e) 
		{
			Log.e(tag, e.toString(),e);
		}
		Log.d(tag, "over");
		return result;
	}
	
	public Bitmap  getImage(String URL) 
	{
		Bitmap result = null;
		try 
		{
			String url = URL;
			Log.d(tag, "url is:" + url);
			HttpGet get = new HttpGet(url);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); 
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000); 
			HttpResponse httpResponse = httpClient.execute(get);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			{
		        InputStream is = httpResponse.getEntity().getContent();
		        byte[] bytes = new byte[1024];
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        int count = 0;
		        while ((count = is.read(bytes)) != -1) 
		        {
		            bos.write(bytes, 0, count);
		        }
		        byte[] byteArray = bos.toByteArray();
		        result = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		    }
			else
			{
		    	 Log.d(tag, "httpResponse getStatusCode = " + httpResponse.getStatusLine().getStatusCode());
		    }
			
		} 
		catch (Exception e) 
		{
			Log.e(tag, e.toString(), e);
		}
		Log.d(tag, "over");
		return result;
	}
}
