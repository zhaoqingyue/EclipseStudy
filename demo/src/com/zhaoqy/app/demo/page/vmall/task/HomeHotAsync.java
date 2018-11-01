package com.zhaoqy.app.demo.page.vmall.task;

import java.util.List;

import com.zhaoqy.app.demo.page.vmall.adapter.HomeHotAdapter;
import com.zhaoqy.app.demo.page.vmall.fragment.HomeFragment.HomeViewHot;
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

public class HomeHotAsync extends AsyncTask<String, Void, List<String>> {

	private Context context;
	private HomeViewHot hvh;
	private ProgressDialog pd;
	
	public HomeHotAsync(Context context,HomeViewHot hvh){
		this.context=context;
		this.hvh=hvh;
	}
	
	protected void onPreExecute() {
		pd=new ProgressDialog(context);
		pd.setTitle("Loading...");
		pd.show();
	}

	protected List<String> doInBackground(String... params) {
		List<String> list=null;
		Cursor cursor = DBManager.rawQuery(VmallStatic.db, "select * from "+Constant.TABLE_NAME+" where "+Constant._ID+"='"+params[0]+"'",null);
		if (cursor.getCount() == 1) {
			byte[] buff = DBManager.CursorToByte(cursor);
			list=PaserUtil.parserHomeHotJson(buff);
		} else {
			if(HttpUtil.isHaveInternet(context)){
				byte[] buff = HttpUtil.getDataFromHttp(params[0]);
				String sql = "insert into " + Constant.TABLE_NAME + " values('" + params[0] + "','" + new String(buff, 0, buff.length)+ "')";
				DBManager.execSQL(VmallStatic.db, sql);
				list=PaserUtil.parserHomeHotJson(buff);
			}
		}
		return list;
	}

	protected void onPostExecute(List<String> result) {
		if(result!=null&&result.size()!=0){
			result.remove(0);
			HomeHotAdapter hha=new HomeHotAdapter(context, result);
			hvh.home_gvhot.setAdapter(hha);
			ViewGroup.LayoutParams linearParams = hvh.home_gvhot.getLayoutParams();
			linearParams.height = (int) (42*context.getResources().getDisplayMetrics().density*3);
			hvh.home_gvhot.setLayoutParams(linearParams);
		}
		pd.dismiss();
	}
}
