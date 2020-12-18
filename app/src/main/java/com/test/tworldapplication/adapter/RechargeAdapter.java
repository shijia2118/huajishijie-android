package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.OrderCz;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/19.
 */

public class RechargeAdapter extends BaseAdapter {
    Context context;
    OrderCz[] orderCzs;

    String flag = "";
    int flag_old = 0;

    public RechargeAdapter(Context context) {
        this.context = context;
    }

    public void setOrderCzs(OrderCz[] orderCzs) {
        this.orderCzs = orderCzs;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return orderCzs.length;
    }

    @Override
    public Object getItem(int position) {
        return orderCzs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_recharge_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvDate.setText("日期:" + orderCzs[position].getRechargeDate().substring(0,10));
        holder.tvId.setText("类型:" + flag);
        holder.tvMoney.setText("金额:" + orderCzs[position].getPayAmount());
        holder.tvPhone.setText("号码:" + orderCzs[position].getNumber());
        holder.tvState.setText("状态:" + orderCzs[position].getStartName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;
        @BindView(R.id.tvPhone)
        TextView tvPhone;
        @BindView(R.id.tvMoney)
        TextView tvMoney;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvState)
        TextView tvState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
