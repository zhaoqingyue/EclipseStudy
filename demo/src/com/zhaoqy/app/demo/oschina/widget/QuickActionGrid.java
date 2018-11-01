package com.zhaoqy.app.demo.oschina.widget;

import java.util.List;

import com.zhaoqy.app.demo.R;

import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class QuickActionGrid extends QuickActionWidget {

	private GridView mGridView;

    public QuickActionGrid(Context context) {
        super(context);

        setContentView(R.layout.view_oschina_quick_grid);
        
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);

        final View v = getContentView();
        mGridView = (GridView) v.findViewById(R.id.gdi_grid);
    }

    @Override
    protected void populateQuickActions(final List<QuickAction> quickActions) {

        mGridView.setAdapter(new BaseAdapter() {

			public View getView(int position, View view, ViewGroup parent) {

                TextView textView = (TextView) view;

                if (view == null) {
                    final LayoutInflater inflater = LayoutInflater.from(getContext());
                    textView = (TextView) inflater.inflate(R.layout.item_oschina_quick_grid, mGridView, false);
                }

                QuickAction quickAction = quickActions.get(position);
                textView.setText(quickAction.mTitle);
                textView.setCompoundDrawablesWithIntrinsicBounds(null, quickAction.mDrawable, null, null);

                return textView;

            }

			public long getItemId(int position) {
                return position;
            }

			public Object getItem(int position) {
                return null;
            }

			public int getCount() {
                return quickActions.size();
            }
        });

        mGridView.setOnItemClickListener(mInternalItemClickListener);
        mGridView.setOnKeyListener(mKeyListener);
    }

    @Override
    protected void onMeasureAndLayout(Rect anchorRect, View contentView) {

        contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        contentView.measure(MeasureSpec.makeMeasureSpec(getScreenWidth(), MeasureSpec.EXACTLY),
                LayoutParams.WRAP_CONTENT);

        int rootHeight = contentView.getMeasuredHeight();

        int offsetY = getArrowOffsetY();
        int dyTop = anchorRect.top;
        int dyBottom = getScreenHeight() - anchorRect.bottom;

        boolean onTop = (dyTop > dyBottom);
        int popupY = (onTop) ? anchorRect.top - rootHeight + offsetY : anchorRect.bottom - offsetY;

        setWidgetSpecs(popupY, onTop);
    }

    private OnItemClickListener mInternalItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            getOnQuickActionClickListener().onQuickActionClicked(QuickActionGrid.this, position);
            if (getDismissOnClick()) {
                dismiss();
            }
        }
    };
    
    private LinearLayout.OnKeyListener mKeyListener = new LinearLayout.OnKeyListener(){
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) && event.getRepeatCount() == 0 && isShowing()) {
				if(isMenuClick()) {
					setMenuClick(false);
				}else{
					dismiss();
				}
			}else if(keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0 && !isShowing()) {
				show();
			}
			return true;
		}    	
    };

}
