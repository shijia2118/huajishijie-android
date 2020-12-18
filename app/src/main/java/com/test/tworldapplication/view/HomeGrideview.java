package com.test.tworldapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.HomeGrideAdapter;
import com.test.tworldapplication.entity.Home;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/17.
 */
public class HomeGrideview extends LinearLayout{
    LinearLayout mLinearLayout;
    @BindView(R.id.home_grideview)
    GridView homeGrideview;
    Context context;

    public GridView getHomeGrideview() {
        return homeGrideview;
    }

    public HomeGrideview(Context context) {
        super(context);
    }

    public HomeGrideview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        mLinearLayout = (LinearLayout) mInflater.inflate(R.layout.view_home_grideview, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(mLinearLayout, params);
        ButterKnife.bind(this);
    }

    public void initGrideView(List<Home> list) {
        HomeGrideAdapter adapter = new HomeGrideAdapter(context, list);
        homeGrideview.setAdapter(adapter);

    }

}
