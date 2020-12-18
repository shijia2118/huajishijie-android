package com.test.tworldapplication.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.test.tworldapplication.R;

/**
 * Created by 27733 on 2016/10/13.
 */
public class PasswordPop extends PopupWindow {
    Context context;
    View conentView;
    EditText edit0;
    EditText edit1;
    PasswordView pwdView;

    public PasswordView getPwdView() {
        return pwdView;
    }
    //

    public PasswordPop(final Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.view_pop_password, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        pwdView = (PasswordView) conentView.findViewById(R.id.pwd_view);
//        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
//            @Override
//            public void inputFinish() {
//                //输入完成后我们简单显示一下输入的密码
//                //也就是说——>实现你的交易逻辑什么的在这里写
//                dismiss();
////                Toast.makeText(context, pwdView.getStrPassword(), Toast.LENGTH_SHORT).show();
//            }
//        });

        /**
         *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
         *  如果写了，会覆盖我们在自定义控件中提供的响应
         *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
        pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.update();

    }


    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);


        } else {
            this.dismiss();
        }
    }

}
