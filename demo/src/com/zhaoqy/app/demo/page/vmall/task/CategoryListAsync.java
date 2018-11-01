package com.zhaoqy.app.demo.page.vmall.task;

import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.adapter.CategoryLeftAdapter;
import com.zhaoqy.app.demo.page.vmall.adapter.CategoryRightAdapter;
import com.zhaoqy.app.demo.page.vmall.fragment.CategoryFragment.CategoryView;
import com.zhaoqy.app.demo.page.vmall.item.Category;
import com.zhaoqy.app.demo.page.vmall.item.Product;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryListAsync extends AsyncTask<String, Void, List<Category>>{

	private Context context;
	private CategoryView cv;
	
	private ProgressDialog pd;
	private List<Product> rightlist;
	private CategoryRightAdapter cra ;

	public CategoryListAsync(Context context, CategoryView cv) {
		this.context = context;
		this.cv = cv;
	}

	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setTitle("Loading...");
		pd.show();
	}

	protected List<Category> doInBackground(String... params) {
		List<Category> list = null;
		Cursor cursor = DBManager.rawQuery(VmallStatic.db, "select * from "+Constant.TABLE_NAME+" where "+Constant._ID+"='"+params[0]+"'",null);
		if (cursor.getCount() == 1) {
			byte[] buff = DBManager.CursorToByte(cursor);
			list = PaserUtil.parserCategoryJson(buff);
		} else {
			if(HttpUtil.isHaveInternet(context)){
				byte[] buff = HttpUtil.getDataFromHttp(params[0]);
				String sql = "insert into " + Constant.TABLE_NAME + " values('" + params[0] + "','" + new String(buff, 0, buff.length)+ "')";
				DBManager.execSQL(VmallStatic.db, sql);
				list = PaserUtil.parserCategoryJson(buff);
			}
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	protected void onPostExecute(final List<Category> result) {
		if (result != null && result.size() != 0) {
			CategoryLeftAdapter cla = new CategoryLeftAdapter(context, result);
			cv.category_lvleft.setAdapter(cla);
			ViewGroup.LayoutParams linearParams = cv.category_lvleft
					.getLayoutParams();
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			linearParams.height = (int) (wm.getDefaultDisplay().getHeight() * context
					.getResources().getDisplayMetrics().density);
			cv.category_lvleft.setLayoutParams(linearParams);
			
			rightlist=result.get(0).getList();
			cra = new CategoryRightAdapter(context, rightlist,cv.category_lvright);
			cv.category_lvright.setAdapter(cra);
			cv.category_lvleft.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							for (int i = 0; i < parent.getCount(); i++) {
								View v = parent.getChildAt(parent.getCount()
										- i - 1);
								if (position == parent.getCount() - i - 1) {
									v.setBackgroundResource(R.drawable.vmall_category_arrow_left);
									TextView tv = (TextView) v
											.findViewById(R.id.list_category_left_tvname);
									tv.setTextColor(Color.parseColor("#960007"));
								} else {
									v.setBackgroundResource(R.drawable.vmall_category_item_shape);
									TextView tv = (TextView) v
											.findViewById(R.id.list_category_left_tvname);
									tv.setTextColor(Color.parseColor("#000000"));
								}
							}
							cra.setList(result.get(position).getList());
							cra.notifyDataSetChanged();
						}
					});
		}
		pd.dismiss();
	}
}
