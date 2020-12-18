package com.test.tworldapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tworldapplication.R;

/**
 * Created by dasiy on 16/10/18.
 */

public class BuyResultDialog extends Dialog {
    View conentView;
    Context context;

    public BuyResultDialog(Context context) {
        super(context);
        this.context = context;
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.view_buy_result;
        conentView = inflater.inflate(layout, null);
        this.setContentView(conentView);


    }

    public BuyResultDialog(Context context,int layoutId, int themeResId) {
        super(context, themeResId);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        int layout = R.layout.view_buy_result;
        conentView = inflater.inflate(layoutId, null);
        this.setContentView(conentView);

    }


}
