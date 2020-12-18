package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.Home;
import com.test.tworldapplication.inter.OnHomeListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 27733 on 2016/10/11.
 */
public class HomeRecycleAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Home> list;
    OnHomeListener homeListener;

    public OnHomeListener getHomeListener() {
        return homeListener;
    }

    public void setHomeListener(OnHomeListener homeListener) {
        this.homeListener = homeListener;
    }

    public HomeRecycleAdapter(Context context, List<Home> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.adapter_home_recycle, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvItem.setText(list.get(position).getItemName());
        holder.imgItem.setBackgroundResource(list.get(position).getImageId());
        holder.llhomeAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeListener.getPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

class MyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imgItem)
    ImageView imgItem;
    @BindView(R.id.tvItem)
    TextView tvItem;
    @BindView(R.id.llhomeAdapter)
    LinearLayout llhomeAdapter;

    public MyViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

    }
}
