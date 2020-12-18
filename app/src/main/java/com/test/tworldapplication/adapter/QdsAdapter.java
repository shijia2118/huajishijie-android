package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.Organization;
import com.test.tworldapplication.entity.Qds;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/19.
 */

public class QdsAdapter extends BaseAdapter {
    Context context;
    Organization[] organizations;
    int flag = 0;

    public QdsAdapter(Context context) {
        this.context = context;
    }

    public void setList(Organization[] organizations) {
        this.organizations = organizations;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return organizations.length;
    }

    @Override
    public Object getItem(int position) {
        return organizations[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_qds_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvQdsName.setText("渠道名称:" + organizations[position].getName());
        holder.tvName.setText("姓名:" + organizations[position].getContact());
        holder.tvNumber.setText("号码:" + organizations[position].getOrgCode());
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvNumber)
        TextView tvNumber;
        @BindView(R.id.tvQdsName)
        TextView tvQdsName;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);


        }
    }


}
