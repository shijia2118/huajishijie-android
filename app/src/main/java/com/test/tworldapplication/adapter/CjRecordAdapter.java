package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.Account;
import com.test.tworldapplication.entity.CjRecord;
import com.test.tworldapplication.entity.LotteryRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/18.
 */

public class CjRecordAdapter extends BaseAdapter {
    Context context;
    List<LotteryRecord> list;

    public CjRecordAdapter(Context context, List<LotteryRecord> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(list.get(position).getType() + ":" + list.get(position).getName());
        holder.tvTime.setText(list.get(position).getCreateDate());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvTime)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }

}
