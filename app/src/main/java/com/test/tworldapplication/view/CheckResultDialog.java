package com.test.tworldapplication.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.tworldapplication.R;

import org.w3c.dom.Text;

/**
 * Created by dasiy on 16/10/18.
 */

public class CheckResultDialog extends Dialog {
    View conentView;
    Context context;
    ImageView imageView;
    TextView tvTitle;
    TextView tvToast;

    public CheckResultDialog(Context context) {
        super(context);


    }

    public TextView getTvToast() {
        return tvToast;
    }

    public void setTvToast(TextView tvToast) {
        this.tvToast = tvToast;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public CheckResultDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = R.layout.view_submit_result;
        conentView = inflater.inflate(layout, null);
        imageView = (ImageView) conentView.findViewById(R.id.imgLogo);
        tvTitle = (TextView) conentView.findViewById(R.id.tvTitle);
        tvToast = (TextView) conentView.findViewById(R.id.tvTitle);
        this.setContentView(conentView);

    }
//
//    public void setFlag(int flag) {
////        LayoutInflater inflater = (LayoutInflater) context
////                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        int layout = R.layout.view_check_result;
////        int layout = R.layout.view_submit_result;
//        switch (flag) {
//            case 0:
////                layout = R.layout.view_check_result;
//                imageView.setBackgroundResource(R.drawable.success);
//                tvTitle.setText("正在登录");
//                tvToast.setText("请稍后……");
//                break;
//            //正在提交
//            case 1:
//                imageView.setBackgroundResource(R.drawable.success);
//                tvTitle.setText("正在登录");
//                tvToast.setText("请稍后……");
//                break;
//
//            case 2:
//                imageView.setBackgroundResource(R.drawable.success);
//                tvTitle.setText("正在登录");
//                tvToast.setText("请稍后……");
//                break;
//            //正在获取
//            case 3:
//                imageView.setBackgroundResource(R.drawable.success);
//                tvTitle.setText("正在登录");
//                tvToast.setText("请稍后……");
//                break;
//            case 4:
//                imageView.setBackgroundResource(R.drawable.success);
//                tvTitle.setText("正在登录");
//                tvToast.setText("请稍后……");
//                break;
//            case 5:
//                imageView.setBackgroundResource(R.drawable.success);
//                tvTitle.setText("正在登录");
//                tvToast.setText("请稍后……");
//                break;
//            case 6:
//                imageView.setBackgroundResource(R.drawable.success);
//                tvTitle.setText("正在登录");
//                tvToast.setText("请稍后……");
//                break;
//        }
//        conentView = inflater.inflate(layout, null);
//        // 设置SelectPicPopupWindow的View
//        this.setContentView(conentView);

//    }


}
