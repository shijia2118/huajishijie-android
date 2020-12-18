package com.test.tworldapplication.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WheelView extends ScrollView {
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;
    private static final int OFF_SET_DEFAULT = 1;
    private int offset = OFF_SET_DEFAULT;

    private Context context;
    private LinearLayout views;
    private List<String> items;

    private OnWheelViewListener listener;
    private Runnable scrollerTask;
    private Paint paint;
    private Typeface fontFace;

    private int displayItemCount; // 每页显示的数量
    private int selectedIndex = 1;
    private int initialY;
    private int itemHeight;
    private int newCheck = 50;
    private int[] selectedAreaBorder;
    private int scrollDirection = -1;
    private int viewWidth;
    private int textSize = 16;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int textFocusColor = Color.parseColor("#0288ce");
    private int textOutsideColor = Color.parseColor("#bbbbbb");
    private int divideLineColor = Color.parseColor("#83cde6");
    private double lineStartPercent = 0;
    private double lineEndPercent = 1;

    public WheelView(Context context) {
        super(context);
        initView(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;
        for (String item : items) {
            views.addView(createView(item));
        }
        refreshItemView(0);
    }

    private void initView(Context context) {
        this.context = context;
        this.setVerticalScrollBarEnabled(false);
        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);
        scrollerTask = new Runnable() {
            public void run() {
                int newY = getScrollY();
                if (initialY - newY == 0) {
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    if (remainder == 0) {
                        selectedIndex = divided + offset;
                        if (listener != null) {
                            listener.onSelected(selectedIndex, items.get(selectedIndex));
                        }
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    if (listener != null) {
                                        listener.onSelected(selectedIndex, items.get(selectedIndex));
                                    }
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    if (listener != null) {
                                        listener.onSelected(selectedIndex, items.get(selectedIndex));
                                    }
                                }
                            });
                        }
                    }
                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    private TextView createView(String item) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        if (fontFace != null) {
            tv.setTypeface(fontFace);
        }
        if (itemHeight == 0) {
            itemHeight = getViewMeasuredHeight(tv);
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }
        return tv;
    }

    private int getViewMeasuredHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

    public void startScrollerTask() {
        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }
        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                itemView.setTextColor(textFocusColor);
            } else {
                itemView.setTextColor(textOutsideColor);
            }
        }
    }

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        }
        if (null == paint) {
            paint = new Paint();
            paint.setColor(divideLineColor);
            paint.setStrokeWidth(dp2px(1f));
        }
        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine((float) (viewWidth * lineStartPercent), obtainSelectedAreaBorder()[0], (float) (viewWidth * lineEndPercent), obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine((float) (viewWidth * lineStartPercent), obtainSelectedAreaBorder()[1], (float) (viewWidth * lineEndPercent), obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };

        super.setBackgroundDrawable(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        setBackgroundDrawable(null);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        refreshItemView(t);
        if (t > oldt) {
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
            scrollDirection = SCROLL_DIRECTION_UP;
        }
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    public interface OnWheelViewListener {
        void onSelected(int selectedIndex, String item);
    }

    public void setOnWheelViewListener(OnWheelViewListener listener) {
        this.listener = listener;
    }

    public void setSeletion(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, p * itemHeight);
            }
        });
    }

    private List<String> getItems() {
        return items;
    }

    public void setItems(List<String> list) {
        if (null == items) {
            items = new ArrayList<String>();
        }
        items.clear();
        items.addAll(list);
        for (int i = 0; i < offset; i++) {
            items.add(0, "");
            items.add("");
        }
        initData();
    }

    public void refresh(List<String> list) {
        views.removeAllViews();
        setItems(list);
    }

    public void refresh() {
        views.removeAllViews();
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getSeletedItem() {
        return items.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setFontFace(Typeface fontFace) {
        this.fontFace = fontFace;
    }

    public void setTextPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = left;
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
    }

    public void setTextFocusColor(int textFocusColor) {
        this.textFocusColor = textFocusColor;
    }

    public void setTextOutsideColor(int textOutsideColor) {
        this.textOutsideColor = textOutsideColor;
    }

    public void setDivideLineColor(int divideLineColor) {
        this.divideLineColor = divideLineColor;
    }

    public void setLineEndPercent(double lineEndPercent) {
        this.lineEndPercent = lineEndPercent;
    }

    public void setLineStartPercent(double lineStartPercent) {
        this.lineStartPercent = lineStartPercent;
    }

    private int dp2px(float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}