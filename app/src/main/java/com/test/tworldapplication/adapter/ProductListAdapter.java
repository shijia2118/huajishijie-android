package com.test.tworldapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.card.ProductInputActivity;
import com.test.tworldapplication.entity.Account;
import com.test.tworldapplication.entity.Product;
import com.test.tworldapplication.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dasiy on 2018/1/4.
 */

public class ProductListAdapter extends BaseAdapter {
    Context context;
    List<Product> list;
    ProductInputActivity.OnPositionClick onPositionClick;

    public ProductListAdapter(Context context, List<Product> list, ProductInputActivity.OnPositionClick onPositionClick) {
        this.context = context;
        this.list = list;
        this.onPositionClick = onPositionClick;
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
            view = View.inflate(context, R.layout.inflate_product_list, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        if (list.get(i).getProdOfferName() != null)
            viewHolder.tvTitle.setText(list.get(i).getProdOfferName());
        if (list.get(i).getProdOfferDesc() != null)
            viewHolder.tvDiscription.setText(list.get(i).getProdOfferDesc());

        if (list.get(i).getProdOfferId() != null)
        viewHolder.tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Util.isFastDoubleClick())
                    onPositionClick.clickCallback(i);
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDiscription)
        TextView tvDiscription;
        @BindView(R.id.tvBuy)
        TextView tvBuy;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
