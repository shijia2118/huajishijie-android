package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class OrderNewAdapter extends BaseAdapter {
    Context context;
    //    List<OrderGH> list;
    int flag = 0;
//    private OrderKH[] arrKh;
//    private OrderGH[] arrGh;

    private List<OrderKH> orderKHList;
    private List<OrderGH> orderGHList;
    private List<OrderCz> orderCzList;

    public void setOrderCzList(List<OrderCz> orderCzList) {
        this.orderCzList = orderCzList;
    }

    public void setOrderKHList(List<OrderKH> orderKHList) {
        this.orderKHList = orderKHList;
    }

    public void setOrderGHList(List<OrderGH> orderGHList) {
        this.orderGHList = orderGHList;
    }
    //
//    public void setArrGh(OrderGH[] arrGh) {
//        this.arrGh = arrGh;
//    }
//
//    public void setArrKh(OrderKH[] arrKh) {
//        this.arrKh = arrKh;
//    }

    public OrderNewAdapter(Context context) {
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
                n = orderKHList.size();
                break;
            case 2:
            case 3:
                n = orderGHList.size();
                break;
            case 4:
            case 5:
                n = orderCzList.size();
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
                object = orderKHList.size();
                break;
            case 2:
            case 3:
                object = orderGHList.size();
                break;
            case 4:
            case 5:
                object = orderCzList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_order_new_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (flag) {
            case 0:
            case 1:
                holder.llCzGone.setVisibility(View.VISIBLE);
                holder.llCz.setVisibility(View.GONE);
                holder.imgRight.setVisibility(View.VISIBLE);
                holder.tvId.setText("姓名:" + orderKHList.get(position).getCustomerName());
                holder.tvDate.setText("日期:" + orderKHList.get(position).getStartTime().substring(0, 10));
                holder.tvName.setText("号码:" + orderKHList.get(position).getNumber());
                holder.tvNumber.setText("状态:" + orderKHList.get(position).getOrderStatusName());
                break;
            case 2:
            case 3:
                holder.llCzGone.setVisibility(View.VISIBLE);
                holder.llCz.setVisibility(View.GONE);
                holder.imgRight.setVisibility(View.VISIBLE);
                holder.tvId.setText("姓名:" + orderGHList.get(position).getName());
                holder.tvDate.setText("日期:" + orderGHList.get(position).getStartTime().substring(0, 10));
                holder.tvName.setText("号码:" + orderGHList.get(position).getNumber());
                holder.tvNumber.setText("状态:" + orderGHList.get(position).getStartName());
                break;
            case 4:
                holder.llCzGone.setVisibility(View.GONE);
                holder.llCz.setVisibility(View.VISIBLE);
                holder.tvCzDate.setText("日期:" + orderCzList.get(position).getRechargeDate().substring(0, 10));
                holder.tvReId.setText("类型:话费充值");
                holder.tvCzMoney.setText("金额:" + orderCzList.get(position).getPayAmount());
                holder.tvCzPhone.setText("号码:" + orderCzList.get(position).getNumber());
                holder.tvCzState.setText("状态:" + orderCzList.get(position).getStartName());
                break;
            case 5:
                holder.llCzGone.setVisibility(View.GONE);
                holder.llCz.setVisibility(View.VISIBLE);
                holder.tvCzDate.setText("日期:" + orderCzList.get(position).getRechargeDate().substring(0, 10));
                holder.tvReId.setText("类型:账户充值");
                holder.tvCzMoney.setText("金额:" + orderCzList.get(position).getPayAmount());
                holder.tvCzPhone.setText("号码:" + orderCzList.get(position).getNumber());
                holder.tvCzState.setText("状态:" + orderCzList.get(position).getStartName());
                break;

        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvNumber)
        TextView tvNumber;
        @BindView(R.id.ll_czGone)
        RelativeLayout llCzGone;
        @BindView(R.id.imgRight)
        ImageView imgRight;
        @BindView(R.id.tvReId)
        TextView tvReId;
        @BindView(R.id.tvCzPhone)
        TextView tvCzPhone;
        @BindView(R.id.tvCzMoney)
        TextView tvCzMoney;
        @BindView(R.id.ll_cz)
        LinearLayout llCz;
        @BindView(R.id.tvCzDate)
        TextView tvCzDate;
        @BindView(R.id.tvCzState)
        TextView tvCzState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
