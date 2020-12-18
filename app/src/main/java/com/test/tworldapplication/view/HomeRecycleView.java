package com.test.tworldapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.HomeRecycleAdapter;
import com.test.tworldapplication.entity.Home;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 27733 on 2016/10/12.
 */
public class HomeRecycleView extends LinearLayout {
    Context context;
    @BindView(R.id.home_recycle)
    RecyclerView homeRecycle;
    HomeRecycleAdapter adapter;
    private LinearLayout mLinearLayout;

    public HomeRecycleAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(HomeRecycleAdapter adapter) {
        this.adapter = adapter;
    }

    public RecyclerView getHomeRecycle() {
        return homeRecycle;
    }

    public void setHomeRecycle(RecyclerView homeRecycle) {
        this.homeRecycle = homeRecycle;
    }


    public HomeRecycleView(Context context) {
        super(context);
    }

    public HomeRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        mLinearLayout = (LinearLayout) mInflater.inflate(R.layout.view_home_recycle, null);
        LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.addView(mLinearLayout, params);
        ButterKnife.bind(this);
    }
    public  void initRecycle(List<Home>list){
        homeRecycle.setLayoutManager(new GridLayoutManager(context,3));
        homeRecycle.setItemAnimator(new DefaultItemAnimator());
        homeRecycle.addItemDecoration(new DividerGridItemDecoration(context));
        adapter = new HomeRecycleAdapter(context,list);
        homeRecycle.setAdapter(adapter);
    }
}
