package com.zhaoqy.app.demo.page.vmall.task;

import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.activity.VmallSearchActivity.ViewSearch;
import com.zhaoqy.app.demo.page.vmall.adapter.SearchAdapter;
import com.zhaoqy.app.demo.page.vmall.item.SGoods;
import com.zhaoqy.app.demo.page.vmall.util.Constant;
import com.zhaoqy.app.demo.page.vmall.util.DBManager;
import com.zhaoqy.app.demo.page.vmall.util.HttpUtil;
import com.zhaoqy.app.demo.page.vmall.util.PaserUtil;
import com.zhaoqy.app.demo.page.vmall.util.VmallStatic;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SearchAsync extends AsyncTask<String, Void, List<SGoods>>{

	private Context context;
	private ViewSearch vs;
	private SearchAdapter sa,sagv;
	private List<SGoods> salist;
	private ProgressDialog pd;
	
	private boolean isshow;
	public SearchAsync(Context context, ViewSearch vs,SearchAdapter sa,SearchAdapter sagv,List<SGoods> salist,boolean isshow) {
		this.context = context;
		this.vs=vs;
		this.sa=sa;
		this.sagv=sagv;
		this.salist=salist;
		this.isshow=isshow;
	}

	protected void onPreExecute() {
		show();
		pd=new ProgressDialog(context);
		pd.setTitle("Loading...");
		pd.show();
	}

	protected List<SGoods> doInBackground(String... params) {
		List<SGoods> list=null;
		Cursor cursor = DBManager.rawQuery(VmallStatic.db, "select * from "+Constant.TABLE_NAME+" where "+Constant._ID+"='"+params[0]+"'",null);
		if (cursor.getCount() == 1) {
			byte[] buff = DBManager.CursorToByte(cursor);
			list=PaserUtil.parserSearchListJson(buff);
		} else {
			if(HttpUtil.isHaveInternet(context)){
				byte[] buff = HttpUtil.getDataFromHttp(params[0]);
				String sql = "insert into " + Constant.TABLE_NAME + " values('" + params[0] + "','" + new String(buff, 0, buff.length)+ "')";
				DBManager.execSQL(VmallStatic.db, sql);
				list=PaserUtil.parserSearchListJson(buff);
			}
		}
		return list;
	}

	protected void onPostExecute(final List<SGoods> result) {
		if(result!=null&&result.size()!=0){
			salist.addAll(result);
			
			sa.notifyDataSetChanged();
			sagv.notifyDataSetChanged();
		}else{
			Toast.makeText(context, "没有更多数据！", Toast.LENGTH_SHORT).show();
		}
		vs.search_change.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				change();
			}
		});
		pd.dismiss();
	}
	
	private void show() {
		vs.search_change.setVisibility(View.VISIBLE);
		vs.search_ll_tv1.setText("相关度");
		vs.search_ll_tv1.setTextColor(Color.parseColor("#aa0000"));
		vs.search_ll_tv2.setVisibility(View.VISIBLE);
		vs.search_ll_tv3.setVisibility(View.VISIBLE);
		vs.search_ll_tv4.setVisibility(View.VISIBLE);
		vs.search_ll_tv1.setClickable(true);
		vs.search_gvword.setVisibility(View.GONE);
		if(isshow){
			showlv();
		}else{
			showgv();
		}
	}
	
	private void change() {
		if(vs.search_gv.getVisibility()==View.GONE){
			showgv();
		}else{
			showlv();
		}
	}
	private void showgv() {
		vs.search_change.setImageResource(R.drawable.vmall_search_icon_list);
		vs.search_gv.setVisibility(View.VISIBLE);
		vs.search_lv.setVisibility(View.GONE);
	}
	private void showlv() {
		vs.search_change.setImageResource(R.drawable.vmall_search_icon_grid);
		vs.search_lv.setVisibility(View.VISIBLE);
		vs.search_gv.setVisibility(View.GONE);
	}
}
