package com.test.tworldapplication.base;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.AdminActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.other.MessageMainActivity;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;

import wintone.passport.sdk.utils.AppManager;

/**
 * Created by 27733 on 2016/10/11.
 */
public  class BaseActivity extends AppCompatActivity implements DialogInterface.OnKeyListener {
    public Animation mAnimation;
    private Application myApplication;
    public ImageView imgNew;
    public CheckResultDialog dialog;
    public Gson gson = new Gson();
    public LinearLayout llBack;
    View decorView;

//    public String[] adminStr;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int option = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT > 19)
            getWindow().setStatusBarColor(Color.WHITE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialog = new CheckResultDialog(this, R.style.CustomDialog);
//        dialog.setFlag(1);
        dialog.setOnKeyListener(this);
        myApplication = new MyApplication();
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.click_info);
        AppManager.getAppManager().addActivity(this);


//        adminStr = Util.getLocalAdmin(this);

//        private OnBooleanListener onPermissionListener;    /**     * 权限请求     * @param permission Manifest.permission.CAMERA     * @param onBooleanListener 权限请求结果回调，true-通过  false-拒绝     */    public void permissionRequests(Activity activity,String permission, OnBooleanListener onBooleanListener){        onPermissionListener=onBooleanListener;        if (ContextCompat.checkSelfPermission(activity,                permission)                != PackageManager.PERMISSION_GRANTED) {            // Should we show an explanation?            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,                    Manifest.permission.READ_CONTACTS)) {                //权限通过                onPermissionListener.onClick(true);            } else {                //没有权限，申请一下                ActivityCompat.requestPermissions(activity,                        new String[]{permission},                        1);            }        }else {            //权限已有            if (onPermissionListener != null) {                onPermissionListener.onClick(true);            }        }    }    @Override    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {        if (requestCode == 1) {            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {                //权限通过                if(onPermissionListener!=null){                    onPermissionListener.onClick(true);                }            } else {                //权限拒绝                if(onPermissionListener!=null){                    onPermissionListener.onClick(false);                }            }            return;        }        super.onRequestPermissionsResult(requestCode, permissions, grantResults);    }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void gotoActy(Class clazz) {
        Intent intent = new Intent(AppManager.getAppManager().currentActivity(), clazz);
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

    }

    public void gotoActy(Class clazz, Integer flag) {
        Intent intent = new Intent(AppManager.getAppManager().currentActivity(), clazz);
        intent.putExtra("flag", flag);
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //写下你希望按下返回键达到的效果代码，不写则不会有反应
//            finish();
            AppManager.getAppManager().finishActivity(this);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void setBackGroundTitle(String title, boolean isBack, boolean isMessage, boolean isAdmin) {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
//        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        LinearLayout llAdmin = (LinearLayout) findViewById(R.id.ll_admin);
        LinearLayout llMessage = (LinearLayout) findViewById(R.id.ll_message);
        ImageView imgMessage = (ImageView) findViewById(R.id.imgMessage);
        ImageView imgAdmin = (ImageView) findViewById(R.id.imgAdmin);
        imgNew = (ImageView) findViewById(R.id.imgNew);
        tvTitle.setText(title);
        imgNew.setVisibility(View.INVISIBLE);
        if (isBack) {
            llBack.setVisibility(View.VISIBLE);
        } else {
            llBack.setVisibility(View.GONE);
        }
        if (isMessage) {
            imgMessage.setVisibility(View.VISIBLE);
        } else {
            imgMessage.setVisibility(View.GONE);
        }
        if (isAdmin) {
            imgAdmin.setVisibility(View.VISIBLE);
        } else {
            imgAdmin.setVisibility(View.GONE);
        }

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                AppManager.getAppManager().finishActivity(BaseActivity.this);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActy(MessageMainActivity.class);
            }
        });

        llAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ccc", "1");
                gotoActy(AdminActivity.class);
            }
        });
    }

    public void setBackGroundTitle(String title, boolean isBack) {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        if (isBack) {
            llBack.setVisibility(View.VISIBLE);
        } else {
            llBack.setVisibility(View.GONE);
        }
        tvTitle.setText(title);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().finishActivity(BaseActivity.this);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

            }
        });
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (Util.isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }

        return onTouchEvent(ev);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
