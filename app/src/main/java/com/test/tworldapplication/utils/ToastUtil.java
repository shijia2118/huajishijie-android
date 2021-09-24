package com.test.tworldapplication.utils;


import android.content.Context;
import android.widget.Toast;

import wintone.passport.sdk.utils.AppManager;

final public class ToastUtil {
    private static Toast toast;//单例的toast

    public static void showToast(Context context, final String text) {
        if (toast == null) {
            //AppManager.getAppManager().currentActivity()是使用工具类获取当前Activity对象的方法
            AppManager.getAppManager().currentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } else {
            //如果show()是在子线程触发的,则在主线程来显示
            if ("main".equals(Thread.currentThread().getName())) {
                toast.setText(text);//将文本设置给toast
                toast.show();
            } else {
                AppManager.getAppManager().currentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast.setText(text);//将文本设置给toast
                        toast.show();
                    }
                });
            }
        }
    }
}
