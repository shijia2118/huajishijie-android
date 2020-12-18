package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.ProductRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 2018/1/4.
 */

public class ProductRecordListAdapter extends BaseAdapter {
    Context context;
    List<ProductRecord> list;

    public ProductRecordListAdapter(Context context, List<ProductRecord> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.inflate_product_records_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.tvStatus.setText(list.get(i).getOrderStatusName());
        viewHolder.tvNumber.setText("订购手机:" + list.get(i).getNumber());
        viewHolder.tvName.setText(list.get(i).getProdOfferName());
        viewHolder.tvData.setText(list.get(i).getCreateDate());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvNumber)
        TextView tvNumber;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvData)
        TextView tvData;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
