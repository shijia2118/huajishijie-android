package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.Home;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/17.
 */
public class HomeGrideAdapter extends BaseAdapter {
    Context context;
    static List<Home> list;
//    private ImageView imgItem;
//    private TextView tvItem;

    public HomeGrideAdapter(Context context, List<Home> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_home_gride, null);
//            imgItem = (ImageView) convertView.findViewById(R.id.imgItem);
//            tvItem = (TextView) convertView.findViewById(R.id.tvItem);
            holder = new ViewHolder(convertView, position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvItem.setText(list.get(position).getItemName());
        holder.imgItem.setBackgroundResource(list.get(position).getImageId());
//        imgItem.setBackgroundResource(list.get(position).getImageId());
//        tvItem.setText(list.get(position).getItemName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.imgItem)
        ImageView imgItem;
        @BindView(R.id.tvItem)
        TextView tvItem;
        @BindView(R.id.llhomeAdapter)
        LinearLayout llhomeAdapter;

        ViewHolder(View view, int position) {
            ButterKnife.bind(this, view);

        }
    }
}
