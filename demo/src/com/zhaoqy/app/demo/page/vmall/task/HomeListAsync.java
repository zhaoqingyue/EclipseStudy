package com.zhaoqy.app.demo.page.vmall.task;

import java.util.List;
import java.util.Map;

import com.zhaoqy.app.demo.page.vmall.adapter.HomeListAdapter;
import com.zhaoqy.app.demo.page.vmall.adapter.HomeListRecoAdapter;
import com.zhaoqy.app.demo.page.vmall.fragment.HomeFragment.HomeViewList;
import com.zhaoqy.app.demo.page.vmall.item.Goods;
import com.zhaoqy.app.demo.page.vmall.util.Constant;
import com.zhaoqy.app.demo.page.vmall.util.DBManager;
import com.zhaoqy.app.demo.page.vmall.util.HttpUtil;
import com.zhaoqy.app.demo.page.vmall.util.PaserUtil;
import com.zhaoqy.app.demo.page.vmall.util.VmallStatic;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.ViewGroup;

public class HomeListAsync extends AsyncTask<String, Void, Map<String,Object>> 
{
	private Context context;
	private HomeViewList hvl;
	private ProgressDialog pd;
	
	public HomeListAsync(Context context,HomeViewList hvl)
	{
		this.context = context;
		this.hvl = hvl;
	}
	
	protected void onPreExecute() 
	{
		pd = new ProgressDialog(context);
		pd.setTitle("Loading...");
		pd.show();
	}

	protected Map<String,Object> doInBackground(String... params) 
	{
		Map<String,Object> map = null;
		Cursor cursor = DBManager.rawQuery(VmallStatic.db, "select * from "+Constant.TABLE_NAME+" where "+Constant._ID+"='"+params[0]+"'",null);
		if (cursor.getCount() == 1) 
		{
			byte[] buff = DBManager.CursorToByte(cursor);
			map = PaserUtil.parserHomeListJson(buff);
		} 
		else 
		{
			if(HttpUtil.isHaveInternet(context))
			{
				byte[] buff = HttpUtil.getDataFromHttp(params[0]);
				String sql = "insert into " + Constant.TABLE_NAME + " values('" + params[0] + "','" + new String(buff, 0, buff.length)+ "')";
				DBManager.execSQL(VmallStatic.db, sql);
				map = PaserUtil.parserHomeListJson(buff);
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	protected void onPostExecute(Map<String,Object> result) 
	{
		if(result != null && result.size() != 0)
		{
			List<Goods> list0 = (List<Goods>) result.get("明星商品栏目");
			HomeListAdapter hla = new HomeListAdapter(context, list0,hvl.home_gvstar);
			hvl.home_gvstar.setAdapter(hla);
			ViewGroup.LayoutParams linearParams = hvl.home_gvstar.getLayoutParams();
			linearParams.height = (int) (102*context.getResources().getDisplayMetrics().density*(hvl.home_gvstar.getCount()/2));
			hvl.home_gvstar.setLayoutParams(linearParams);
			
			List<Goods> list1=(List<Goods>) result.get("精品推荐");
			HomeListRecoAdapter hlra=new HomeListRecoAdapter(context, list1,hvl.home_gvreco);
			hvl.home_gvreco.setAdapter(hlra);
			ViewGroup.LayoutParams linearParams1 = hvl.home_gvreco.getLayoutParams();
			linearParams1.height = (int) (200*context.getResources().getDisplayMetrics().density*(hvl.home_gvreco.getCount()/2));
			hvl.home_gvreco.setLayoutParams(linearParams1);
		}
		pd.dismiss();
	}
}
