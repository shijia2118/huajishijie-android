package com.test.tworldapplication.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.LinearLayout;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.Phone;
import com.test.tworldapplication.inter.RecyclerItemClickListener;

import java.util.List;

/**
 * Created by dasiy on 16/10/18.
 */

public class PhoneAdapter extends QuickAdapter<Phone> {
    Context context;
    List<Phone> list;
    RecyclerItemClickListener listener;

    public void setListener(RecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public PhoneAdapter(List<Phone> datas,Context context) {
        super(datas);
        list = datas;
        this.context=context;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.adapter_phone;
    }

    @Override
    public void convert(VH holder, Phone data, final int position) {
        holder.setText(R.id.tvPhone, list.get(position).getNumber());
        LinearLayout llContent = holder.getView(R.id.llContent);
        if (list.get(position).getNumber().equals(""))
            llContent.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        else
            llContent.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_recharge_solid_pink));
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(view, position);
            }
        });
    }


}
