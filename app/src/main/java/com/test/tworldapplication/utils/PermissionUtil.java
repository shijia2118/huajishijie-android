package com.test.tworldapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

/**
 * Created by dasiy on 17/2/11.
 */

public class PermissionUtil {
    private static void log(String debug) {
        Log.d("PermissionUtil", debug);
    }

    /**
     * 主动请求权限,按官方推荐逻辑,此方法应该在每次需要权限时都要调用
     * 检查权限->询问请求历史 ? 解释权限 (点击确认再次请求) : 直接请求权限
     *
     * @param thisActivity
     * @param permission
     * @param requestCode
     * @param dialogMessage
     */
    public static void requestPermissionWithCheck(Activity thisActivity, String permission, int requestCode, String dialogMessage) {

        if (Build.VERSION.SDK_INT < 23) {
            log("your sdk < 23,the method 'requestPermissionWithCheck(..' do nothing");
            return;
        }
        try {
            //检查权限是否已经开启
            if (ContextCompat.checkSelfPermission(thisActivity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                log("the permission has not,continue...");
                //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                //此时应该向用户解释为什么需要这个权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                        permission)) {
                    //弹出解释弹窗
                    showExplanation(thisActivity, requestCode, dialogMessage, permission);
                } else {
                    //发起权限请求
                    requestPermissions(thisActivity, new String[]{permission}, requestCode);
                }
            } else {
                log("the permission has ready");
            }
        } catch (Exception e) {
            log("the method 'requestPermissionWithCheck(..' apper error,so jump setting");
            //请求权限的过程中出现问题直接跳到设置界面,让用户手动设置权限
            openPermissionSetting(thisActivity);
        }

    }

    /**
     * 直接请求权限
     */
    public static void requestPermissions(Activity thisActivity, String[] permissions, int requestCode) {
        log("request Permission " + Arrays.asList(permissions));
        ActivityCompat.requestPermissions(thisActivity,
                permissions,
                requestCode);
    }

    /**
     * 开启本应用权限设置界面
     *
     * @param context
     */
    public static void openPermissionSetting(Context context) {
        log("start jump Setting...");
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
        log("jump Setting end");
    }

    /**
     * 弹出解释权限的弹框,点击确定请求权限
     *
     * @param thisActivity
     * @param requestCode
     * @param dialogMessage
     * @param requestPermission
     */
    public static void showExplanation(final Activity thisActivity, final int requestCode,
                                       String dialogMessage, final String requestPermission) {
        log("here show showExplanation");
        String defaultMessage = "Need " + requestPermission + " authority, otherwise next function can not be used";
        showMessageOKCancel(thisActivity,
                dialogMessage == null ? defaultMessage : defaultMessage
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(thisActivity, new String[]{requestPermission}, requestCode);
                    }
                });
    }

    /**
     * 在onRequestPermissionsResult中判断某个requestCode陪同的权限请求结果是否成功
     *
     * @param grantResults
     * @return
     */
    public static boolean checkRequestCode(@NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //权限请求成功
            Log.d("ddd", "0");
            return true;
        } else {
            Log.d("ddd", "01");
            //权限请求被拒绝
            return false;
        }
    }

        /*此方法是在onRequestPermissionsResult中回调的例子
    public final static void requestPermissionsResult(final Activity activity,
                                                      final int requestCode,
                                                      @NonNull String[] permissions,
                                                      @NonNull int[] grantResults) {
        if (activity == null) {
            return;
        }
        switch (requestCode) {
            case -1:
                if (PermissionUtil.checkRequestCode(grantResults)) {
                    //权限请求成功
                    Log.d("ddd", "0");

                } else {
                    //权限请求被拒绝
                    Log.d("ddd", "0");
                }
                break;
            default:
        }
    }
//    */

    /**
     * 一个普通弹窗
     *
     * @param context
     * @param message
     * @param okListener
     */
    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

}
