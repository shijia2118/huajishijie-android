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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/18.
 */

public class AccountAdapter extends BaseAdapter {
    Context context;
    List<Account> list;

    public AccountAdapter(Context context, List<Account> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_account_main, null);
            holder = new ViewHolder(convertView, position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgLogo.setBackgroundResource(list.get(position).getImgLogo());
        holder.tvLogo.setText(list.get(position).getTvLogo());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.imgLogo)
        ImageView imgLogo;
        @BindView(R.id.tvLogo)
        TextView tvLogo;

        ViewHolder(View view, int position) {

            ButterKnife.bind(this, view);

        }
    }

}
