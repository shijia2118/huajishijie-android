package com.test.tworldapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.test.tworldapplication.R;
import com.test.tworldapplication.base.MyApplication;
import com.test.tworldapplication.inter.IdTypeSelect;
import com.test.tworldapplication.inter.SuccessNull;

import cn.finalteam.galleryfinal.GalleryFinal;

/**
 * Created by xiongchang on 17/2/17.
 */

public class DialogManager {
    public static void changeAvatar(Context context, final GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.inflate_dialog_click_avatar, null);
        //获得dialog的window窗口

//        SharedPreferences sharedPreferences0 = context.getSharedPreferences( "mySP", Context.MODE_PRIVATE );
//        int readModes = sharedPreferences0.getInt( "readModes", 1 );
//        if(readModes==1){
//
//        }else if(readModes==2){
//
//        }else if(readModes==3){
//
//        }


        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        //window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);

        dialog.show();

        Button btnCamera = (Button) dialogView.findViewById(R.id.btn_camera_avatar_dialog);
        Button btnAlbum = (Button) dialogView.findViewById(R.id.btn_album_avatar_dialog);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel_avatar_dialog);

        SharedPreferences sharedPreferences0 = context.getSharedPreferences( "mySP", Context.MODE_PRIVATE );
        int readModes = sharedPreferences0.getInt( "readModes", -1 );
        if(readModes==1){
            btnCamera.setVisibility( View.VISIBLE );
            btnAlbum.setVisibility( View.GONE );
        }else if(readModes==2){
            btnCamera.setVisibility( View.GONE );
            btnAlbum.setVisibility( View.VISIBLE );
        }else if(readModes==3){
            btnCamera.setVisibility( View.VISIBLE );
            btnAlbum.setVisibility( View.VISIBLE );
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFinal.openCamera(110, MyApplication.functionConfig, mOnHanlderResultCallback);
                dialog.dismiss();
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFinal.openGallerySingle(111, MyApplication.functionConfig, mOnHanlderResultCallback);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public static void changeAvatar(Context context, final SuccessNull successNull, final GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback) {
       changeAvatar(context, successNull, mOnHanlderResultCallback, -1);
    }

    public static void changeAvatar(Context context, final SuccessNull successNull, final GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback, int mode) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.inflate_dialog_click_avatar, null);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        //window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);

        dialog.show();

        Button btnCamera = (Button) dialogView.findViewById(R.id.btn_camera_avatar_dialog);
        Button btnAlbum = (Button) dialogView.findViewById(R.id.btn_album_avatar_dialog);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel_avatar_dialog);


        SharedPreferences sharedPreferences0 = context.getSharedPreferences( "mySP", Context.MODE_PRIVATE );
        int readModes = sharedPreferences0.getInt( "readModes", -1 );
        if (mode != -1) {
            readModes = mode;
        }
        if(readModes==1){
            btnCamera.setVisibility( View.VISIBLE );
            btnAlbum.setVisibility( View.GONE );
        }else if(readModes==2){
            btnCamera.setVisibility( View.GONE );
            btnAlbum.setVisibility( View.VISIBLE );
        }else if(readModes==3){
            btnCamera.setVisibility( View.VISIBLE );
            btnAlbum.setVisibility( View.VISIBLE );
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successNull.onSuccess();
//                GalleryFinal.openCamera(110, MyApplication.functionConfig, mOnHanlderResultCallback);
                dialog.dismiss();
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFinal.openGallerySingle(111, MyApplication.functionConfig, mOnHanlderResultCallback);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void idTypeSelect(Context context, final IdTypeSelect idTypeSelect) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.id_type_select_dialog, null);
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        assert window != null;
        window.setGravity(Gravity.CENTER);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();

        //设置窗口宽度为充满全屏减去边距
        int marginHorizontal = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
        lp.width = context.getResources().getDisplayMetrics().widthPixels - 2 * marginHorizontal;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //将设置好的属性set回去
        window.setAttributes(lp);

        // 设置当用户点击对话框周边时，不允许关闭对话框
        dialog.setCanceledOnTouchOutside(false);

        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);

        dialog.show();

        TextView tvConfirm = (TextView) dialogView.findViewById(R.id.tv_confirm);

        final int[] checkIndex = {0};

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                idTypeSelect.result(checkIndex[0]);
            }
        });

        RadioGroup rgTypeList = (RadioGroup) dialogView.findViewById(R.id.rg_type_list);
        RadioButton rbIdCard = (RadioButton) dialogView.findViewById(R.id.rb_id_card);
        RadioButton rbResidencePermit = (RadioButton) dialogView.findViewById(R.id.rb_residence_permit);

        // 设置默认选中的单选按钮
        rgTypeList.check(rbIdCard.getId());

        rgTypeList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 在这里处理单选按钮的选中事件
                if(checkedId == rbIdCard.getId()){
                    checkIndex[0] = 0;
                } else {
                    checkIndex[0] = 1;
                }
            }
        });

    }
}

