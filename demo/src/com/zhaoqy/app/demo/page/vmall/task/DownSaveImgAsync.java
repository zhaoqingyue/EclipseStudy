package com.zhaoqy.app.demo.page.vmall.task;

import java.lang.ref.SoftReference;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.zhaoqy.app.demo.page.vmall.util.HttpUtil;
import com.zhaoqy.app.demo.page.vmall.util.ReadWriteUtil;
import com.zhaoqy.app.demo.page.vmall.util.VmallStatic;

public class DownSaveImgAsync extends AsyncTask<String, Integer, Bitmap> 
{
	private Map<String, Bitmap> map;
	private Context context;
	private CallBack cb;
	private String key;
	
	public DownSaveImgAsync(Context context, CallBack cb, Map<String,Bitmap> map) 
	{
		this.context = context;
		this.cb = cb;
		this.map = map;
	}

	protected Bitmap doInBackground(String... params) 
	{
		key = params[0];
		params[0] = params[0].replace(" ", "%20");
		Bitmap bm = VmallStatic.lru.get(params[0]);
		if(bm == null)
		{
			SoftReference<Bitmap> sr = VmallStatic.hashMap.get(params[0]);
			if (sr != null) 
			{
				bm = sr.get();
				VmallStatic.hashMap.remove(params[0]);
				VmallStatic.lru.put(params[0], bm);
			}
			else
			{
				String str = params[0].replace("/", "").replace(":", "").replace(".", "");
				if (ReadWriteUtil.hasFile(str)) 
				{
					byte[] buff = ReadWriteUtil.readImg(str);
					bm = BitmapFactory.decodeByteArray(buff, 0, buff.length);
					VmallStatic.lru.put(params[0], bm);
				} 
				else 
				{
					if (HttpUtil.isHaveInternet(context)) 
					{
						byte[] buff = HttpUtil.getDataFromHttp(params[0]);
						ReadWriteUtil.writeImg(buff, str);
						bm = BitmapFactory.decodeByteArray(buff, 0, buff.length);
						VmallStatic.lru.put(params[0], bm);
					}
				}
			}
		}
		return bm;
	}

	protected void onPostExecute(Bitmap result) 
	{
		if (result != null && result.getByteCount() != 0) 
		{
			map.put(key, result);			
			cb.sendImage(result,key);
		}
	}

	public interface CallBack 
	{
		public abstract void sendImage(Bitmap bm, String key);
	}
}
