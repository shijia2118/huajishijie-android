package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.ChannelsOpen;
import com.test.tworldapplication.entity.Organization;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/19.
 */

public class QdsNewAdapter extends BaseAdapter {
    Context context;
    List<Organization> organizationList;//0
    List<ChannelsOpen> channelsOpenList;//1
    int flag = 0;

    public QdsNewAdapter(Context context) {
        this.context = context;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    public void setChannelsOpenList(List<ChannelsOpen> channelsOpenList) {
        this.channelsOpenList = channelsOpenList;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        int count = 0;
        switch (flag) {
            case 0:
                count = organizationList.size();
                break;
            case 1:
                count = channelsOpenList.size();
                break;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object object = null;
        switch (flag) {
            case 0:
                object = organizationList.get(position);
                break;
            case 1:
                object = channelsOpenList.get(position);
                break;
        }
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_qdsorder_new_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (flag) {
            case 0:
                holder.tvNumber.setVisibility(View.GONE);
                holder.tvQdsName.setText("渠道商名称:" + organizationList.get(position).getName());
                holder.tvName.setText("姓名:" + organizationList.get(position).getContact());
                holder.tvPhone.setText("工号:" + organizationList.get(position).getOrgCode());
                break;
            case 1:
                holder.tvNumber.setVisibility(View.VISIBLE);
                holder.tvNumber.setText("开户量:" + String.valueOf(channelsOpenList.get(position).getOpenCount()));
                holder.tvQdsName.setText("渠道商名称:" + channelsOpenList.get(position).getName());
                holder.tvName.setText("姓名:" + channelsOpenList.get(position).getContact());
                holder.tvPhone.setText("工号:" + channelsOpenList.get(position).getOrgCode());
                break;
        }

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
