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
import com.test.tworldapplication.entity.OrderCz;
import com.test.tworldapplication.entity.OrderGH;
import com.test.tworldapplication.entity.OrderKH;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/19.
 */

public class OrderAdapter extends BaseAdapter {
    Context context;
    //    List<OrderGH> list;
    int flag = 0;
    private OrderKH[] arrKh;
    private OrderGH[] arrGh;

    public void setArrGh(OrderGH[] arrGh) {
        this.arrGh = arrGh;
    }

    public void setArrKh(OrderKH[] arrKh) {
        this.arrKh = arrKh;
    }

    public OrderAdapter(Context context) {
        this.context = context;
    }


    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        int n = 0;
        switch (flag) {
            case 0:
            case 1:
                n = arrKh.length;
                break;
            case 2:
            case 3:
                n = arrGh.length;
                break;
        }
        return n;
    }

    @Override
    public Object getItem(int position) {
        Object object = null;
        switch (flag) {
            case 0:
            case 1:
                object = arrKh[position];
                break;
            case 2:
            case 3:
                object = arrGh[position];
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_order_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (flag) {
            case 0:
            case 1:
                holder.imgRight.setVisibility(View.VISIBLE);
                holder.tvId.setText("状态:" + arrKh[position].getOrderStatusName());
                holder.tvDate.setText("日期:" + arrKh[position].getStartTime().substring(0, 10));
                holder.tvName.setText("姓名:" + arrKh[position].getCustomerName());
                holder.tvNumber.setText("号码:" + arrKh[position].getNumber());
                break;
            case 2:
            case 3:
                holder.imgRight.setVisibility(View.VISIBLE);
                holder.tvId.setText("状态:" + arrGh[position].getStartName());
                holder.tvDate.setText("日期:" + arrGh[position].getStartTime().substring(0, 10));
                holder.tvName.setText("姓名:" + arrGh[position].getName());
                holder.tvNumber.setText("号码:" + arrGh[position].getNumber());
                break;

        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvNumber)
        TextView tvNumber;
        @BindView(R.id.ll_czGone)
        LinearLayout llCzGone;
        @BindView(R.id.imgRight)
        ImageView imgRight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);


        }
    }


}
