package com.test.tworldapplication.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.liaoinstan.springview.utils.DensityUtil;
import com.test.tworldapplication.activity.card.PackageSelectDetailActivity;

/**
 * Automatically calculates the ideal for each row
 * @author JJ
 *
 */
public class AutoGridView extends GridView {

    private static final String TAG = "AutoGridView";
    private int numColumnsID;
    private int previousFirstVisible;
    private int numColumns = 1;

    public AutoGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public AutoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AutoGridView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;
        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }


    /**
     * Sets the numColumns based on the attributeset
     */
    private void init(AttributeSet attrs) {
        // Read numColumns out of the AttributeSet
        int count = attrs.getAttributeCount();
        if(count > 0) {
            for(int i = 0; i < count; i++) {
                String name = attrs.getAttributeName(i);
                if(name != null && name.equals("numColumns")) {
                    // Update columns
                    this.numColumnsID = attrs.getAttributeResourceValue(i, 1);
                    updateColumns();
                    break;
                }
            }
        }
        Log.d(TAG, "numColumns set to: " + numColumns);
    }


    /**
     * Reads the amount of columns from the resource file and
     * updates the "numColumns" variable
     */
    private void updateColumns() {
        this.numColumns = getContext().getResources().getInteger(numColumnsID);
    }

    @Override
    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        super.setNumColumns(numColumns);

        Log.d(TAG, "setSelection --> " + previousFirstVisible);
        setSelection(previousFirstVisible);
    }

    @Override
    protected void onLayout(boolean changed, int leftPos, int topPos, int rightPos, int bottomPos) {
        super.onLayout(changed, leftPos, topPos, rightPos, bottomPos);
        setHeights(this);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        updateColumns();
        setNumColumns(this.numColumns);
    }

    @Override
    protected void onScrollChanged(int newHorizontal, int newVertical, int oldHorizontal, int oldVertical) {
        // Check if the first visible position has changed due to this scroll
        int firstVisible = getFirstVisiblePosition();
        if(previousFirstVisible != firstVisible) {
            // Update position, and update heights
            previousFirstVisible = firstVisible;
            setHeights(this);
        }

        super.onScrollChanged(newHorizontal, newVertical, oldHorizontal, oldVertical);
    }

    /**
     * Sets the height of each view in a row equal to the height of the tallest view in this row.
     */
    private void setHeights(AutoGridView listView) {
        int totalHeight=0;
        if(listView==null) return;

        ListAdapter listAdapter = getAdapter();
        if(listAdapter != null) {
            for(int i = 0; i < getChildCount(); i+=numColumns) {
                // Determine the maximum height for this row
                int maxHeight = 0;
                for(int j = i; j < i+numColumns; j++) {
                    View view = getChildAt(j);
                    if(view != null && view.getHeight() > maxHeight) {
                        maxHeight = view.getHeight();
                    }
                }
                //Log.d(TAG, "Max height for row #" + i/numColumns + ": " + maxHeight);

                // Set max height for each element in this row
                if(maxHeight > 0) {
                    for(int j = i; j < i+numColumns; j++) {
                        View view = getChildAt(j);
                        if(view != null && view.getHeight() != maxHeight) {
                            view.setMinimumHeight(maxHeight);
                        }
                    }
                }

                totalHeight=totalHeight+maxHeight;
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalHeight+100; //不清楚为什么相差100,反正加上去后,距离刚刚好
                listView.setLayoutParams(params);
            }
        }
    }


}
