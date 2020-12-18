package com.test.tworldapplication.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.entity.Numbers;
import com.test.tworldapplication.entity.PhoneNumber;

import butterknife.ButterKnife;

/**
 * Created by dasiy on 16/10/18.
 */

public class NumberDetailDialog extends Dialog {
    View conentView;
    Context context;
    TextView tvRegular;
    TextView tvMoney;
    TextView tvRemarks;

    public NumberDetailDialog(Context context) {
        super(context);
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        conentView = inflater.inflate(R.layout.view_number_detail, null);
        this.setContentView(conentView);
        tvRegular = (TextView)conentView.findViewById(R.id.tvRegular);
        tvMoney = (TextView)conentView.findViewById(R.id.tvMoney);
        tvRemarks = (TextView)conentView.findViewById(R.id.tvRemarks);



    }

    public NumberDetailDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.view_number_detail, null);
        this.setContentView(conentView);
        tvRegular = (TextView)conentView.findViewById(R.id.tvRegular);
        tvMoney = (TextView)conentView.findViewById(R.id.tvMoney);
        tvRemarks = (TextView)conentView.findViewById(R.id.tvRemarks);
    }

    public void setNumber(Numbers number) {
        String[] sourceStrArray = number.getInfos().split(",");


        tvMoney.setText(sourceStrArray[0]);
        tvRegular.setText(number.getLtype());
        tvRemarks.setText(sourceStrArray[1]);
    }

    ;



}
