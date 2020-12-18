package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.OrderGH;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/21.
 */

public class ProgressAdapter extends BaseAdapter {
    Context context;
    List<OrderGH> orderGHList;
    String type = "";

    public void setType(String type) {
        this.type = type;
    }

    public void setOrderGHList(List<OrderGH> orderGHList) {
        this.orderGHList = orderGHList;
    }

    public ProgressAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return orderGHList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderGHList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_progress_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText("姓名:" + orderGHList.get(position).getName());
        holder.tvType.setText("类型:" + type);
        holder.tvPhone.setText("号码:" + orderGHList.get(position).getNumber());
        holder.tvState.setText("状态:" + orderGHList.get(position).getStartName());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvPhone)
        TextView tvPhone;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.imgRight)
        ImageView imgRight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }
}
