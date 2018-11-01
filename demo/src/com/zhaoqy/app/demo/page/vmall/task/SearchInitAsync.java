package com.zhaoqy.app.demo.page.vmall.task;

import java.util.List;

import com.zhaoqy.app.demo.page.vmall.activity.VmallSearchActivity.ViewSearch;
import com.zhaoqy.app.demo.page.vmall.adapter.SearchAdapter;
import com.zhaoqy.app.demo.page.vmall.adapter.SearchInitAdapter;
import com.zhaoqy.app.demo.page.vmall.item.SGoods;
import com.zhaoqy.app.demo.page.vmall.util.Constant;
import com.zhaoqy.app.demo.page.vmall.util.DBManager;
import com.zhaoqy.app.demo.page.vmall.util.HttpUtil;
import com.zhaoqy.app.demo.page.vmall.util.PaserUtil;
import com.zhaoqy.app.demo.page.vmall.util.UrlUtil;
import com.zhaoqy.app.demo.page.vmall.util.VmallStatic;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchInitAsync extends AsyncTask<String, Void, List<String>>{

	private Context context;
	private ViewSearch vs;
	private ProgressDialog pd;
	private SearchAdapter sa,sagv;
	private List<SGoods> salist;
	
	public SearchInitAsync(Context context, ViewSearch vs,SearchAdapter sa, SearchAdapter sagv,List<SGoods> salist) {
		this.context = context;
		this.vs=vs;
		this.sa=sa;
		this.sagv=sagv;
		this.salist=salist;
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

	protected void onPostExecute(final List<String> result) {
		if(result!=null&&result.size()!=0){
			vs.search_etword.setHint(result.remove(0));
			SearchInitAdapter sia=new SearchInitAdapter(context, result);
			vs.search_gvword.setAdapter(sia);
			vs.search_gvword.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					vs.search_etword.setText(result.get(position));
					UrlUtil.search_page=1;
					UrlUtil.search_keyword=vs.search_etword.getText().toString().replace(" ", "%20");
					String path=UrlUtil.search_0+UrlUtil.search_page+UrlUtil.search_1+
							UrlUtil.search_item+UrlUtil.search_2+UrlUtil.search_keyword;
					new SearchAsync(context, vs,sa,sagv,salist,true).execute(path);
				}
			});
		}
		
		pd.dismiss();
	}
}
