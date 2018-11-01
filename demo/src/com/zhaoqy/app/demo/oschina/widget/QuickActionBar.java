package com.zhaoqy.app.demo.oschina.widget;

import java.lang.ref.WeakReference;
import java.util.List;

import com.zhaoqy.app.demo.R;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class QuickActionBar extends QuickActionWidget {

	private HorizontalScrollView mScrollView;
    private Animation mRackAnimation;
    private ViewGroup mRack;
    private ViewGroup mQuickActionItems;

    private List<QuickAction> mQuickActions;

    public QuickActionBar(Context context) {
        super(context);

        mRackAnimation = AnimationUtils.loadAnimation(context, R.anim.oschina_rack);

        mRackAnimation.setInterpolator(new Interpolator() {

			public float getInterpolation(float t) {
                final float inner = (t * 1.55f) - 1.1f;
                return 1.2f - inner * inner;
            }
        });

        setContentView(R.layout.view_oschina_quick_bar);

        final View v = getContentView();
        mRack = (ViewGroup) v.findViewById(R.id.gdi_rack);
        mQuickActionItems = (ViewGroup) v.findViewById(R.id.gdi_quick_action_items);
        mScrollView = (HorizontalScrollView) v.findViewById(R.id.gdi_scroll);
    }

    @Override
    public void show(View anchor) {
        super.show(anchor);
        mScrollView.scrollTo(0, 0);
        mRack.startAnimation(mRackAnimation);
    }

    @Override
    protected void onMeasureAndLayout(Rect anchorRect, View contentView) {

        contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        contentView.measure(MeasureSpec.makeMeasureSpec(getScreenWidth(), MeasureSpec.EXACTLY), LayoutParams.WRAP_CONTENT);

        int rootHeight = contentView.getMeasuredHeight();

        int offsetY = getArrowOffsetY();
        int dyTop = anchorRect.top;
        int dyBottom = getScreenHeight() - anchorRect.bottom;

        boolean onTop = (dyTop > dyBottom);
        int popupY = (onTop) ? anchorRect.top - rootHeight + offsetY : anchorRect.bottom - offsetY;

        setWidgetSpecs(popupY, onTop);
    }

    @Override
    protected void populateQuickActions(List<QuickAction> quickActions) {

        mQuickActions = quickActions;

        final LayoutInflater inflater = LayoutInflater.from(getContext());

        for (QuickAction action : quickActions) {
            TextView view = (TextView) inflater.inflate(R.layout.item_oschina_quick_bar, mQuickActionItems, false);
            view.setText(action.mTitle);

            view.setCompoundDrawablesWithIntrinsicBounds(null, action.mDrawable, null, null);
            view.setOnClickListener(mClickHandlerInternal);
            mQuickActionItems.addView(view);
            action.mView = new WeakReference<View>(view);
        }
    }

    @Override
    protected void onClearQuickActions() {
        super.onClearQuickActions();
        mQuickActionItems.removeAllViews();
    }

    private OnClickListener mClickHandlerInternal = new OnClickListener() {

        
		public void onClick(View view) {

            final OnQuickActionClickListener listener = getOnQuickActionClickListener();

            if (listener != null) {
                final int itemCount = mQuickActions.size();
                for (int i = 0; i < itemCount; i++) {
                    if (view == mQuickActions.get(i).mView.get()) {
                        listener.onQuickActionClicked(QuickActionBar.this, i);
                        break;
                    }
                }
            }

            if (getDismissOnClick()) {
                dismiss();
            }
        }

    };

}
