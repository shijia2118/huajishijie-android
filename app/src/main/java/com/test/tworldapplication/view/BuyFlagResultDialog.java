package com.test.tworldapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dasiy on 16/10/18.
 */

public class BuyFlagResultDialog extends Dialog {
    View conentView;
    Context context;
//    @Bind(R.id.btCancel)
//    TextView btCancel;
//    @Bind(R.id.btSure)
//    TextView btSure;

    public BuyFlagResultDialog(Context context) {
        super(context);
        this.context = context;
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.view_product_result;
        conentView = inflater.inflate(layout, null);
        this.setContentView(conentView);


    }

    public BuyFlagResultDialog(final Context context, View.OnClickListener sureClickListener, View.OnClickListener cancelClickLstener, int themeResId) {
        super(context, themeResId);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        int layout = R.layout.view_buy_result;
        conentView = inflater.inflate(R.layout.view_product_result, null);
        TextView tvCancel = (TextView) conentView.findViewById(R.id.btCancel);
        TextView tvSure = (TextView) conentView.findViewById(R.id.btSure);
        tvCancel.setOnClickListener(cancelClickLstener);
        tvSure.setOnClickListener(sureClickListener);

//        tvCancel.setOnClickListener(cancelClickLstener);
//        tvSure.setOnClickListener(sureClickListener);
//        ButterKnife.bind(context, conentView);
        this.setContentView(conentView);

    }


//    @OnClick({R.id.btCancel, R.id.btSure})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btCancel:
//                Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btSure:
//                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
}
