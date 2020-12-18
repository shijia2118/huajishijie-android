package com.test.tworldapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.card.WhitePreOpenActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.entity.WhitePreOpen;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 17/5/8.
 */

public class NumberAdapter extends BaseAdapter {
    Context context;
    List<WhitePreOpen> list;

    public NumberAdapter(Context context, List<WhitePreOpen> list) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_whitepreopen_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("aaa", list.get(position).getPhone());
        Log.d("aaa", list.get(position).getStatus());
        viewHolder.tvPhone.setText("手机号:  " + list.get(position).getPhone() + "  状态:  ");
        viewHolder.tvStatus.setText(list.get(position).getStatus());
        return convertView;
    }

}

class ViewHolder {
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    public ViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
