package com.test.tworldapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.test.tworldapplication.R;
import com.test.tworldapplication.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dasiy on 16/10/22.
 */

public class WheelDialog extends Dialog{
    List<String> list = new ArrayList<>();
    public WheelDialog(Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.view_wheel_dialog,null);
        WheelView view = (WheelView)layout.findViewById(R.id.wheelView);
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        view.setDivideLineColor(context.getResources().getColor(R.color.colorBlue1));
        view.setTextFocusColor(context.getResources().getColor(R.color.colorBlue1));
        view.setTextOutsideColor(context.getResources().getColor(R.color.colorBlue1));
        view.setTextSize(18);
//        view.setFontFace(DisplayUtil.getFontFace());
        view.setOffset(4);
        view.setTextPadding(0, DisplayUtil.dp2px(context, 5), 0, DisplayUtil.dp2px(context, 5));
//        view.setItems(Arrays.asList(allProvince));
        view.setItems(list);
        view.setSeletion(2);
        show();
        setContentView(view);

    }


}
