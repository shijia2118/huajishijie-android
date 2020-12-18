package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.ChannelsOpen;
import com.test.tworldapplication.entity.QdsOrder;
import com.test.tworldapplication.entity.RequestChannelOpenCount;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/19.
 */

public class QdsOrderAdapter extends BaseAdapter {
    Context context;
    ChannelsOpen[] channelsOpens;
    int flag = 0;

    public QdsOrderAdapter(Context context) {
        this.context = context;
    }

    public void setList(ChannelsOpen[] channelsOpens) {
        this.channelsOpens = channelsOpens;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return channelsOpens.length;
    }

    @Override
    public Object getItem(int position) {
        return channelsOpens[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_qdsorder_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNumber.setText("开户量:" + String.valueOf(channelsOpens[position].getOpenCount()));
        holder.tvQdsName.setText("渠道商名称:" + channelsOpens[position].getName());
        holder.tvName.setText("姓名:" + channelsOpens[position].getContact());
        holder.tvPhone.setText("号码:" + channelsOpens[position].getOrgCode());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tvPhone)
        TextView tvPhone;
        @BindView(R.id.tvQdsName)
        TextView tvQdsName;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvNumber)
        TextView tvNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);


        }
    }


}
