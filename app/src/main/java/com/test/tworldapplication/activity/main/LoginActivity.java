package com.test.tworldapplication.activity.main;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.rxbus2.RxUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.test.tworldapplication.BuildConfig;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.SplashActivity;
import com.test.tworldapplication.activity.TestOrcActivity;
import com.test.tworldapplication.activity.admin.WriteInActivity;
import com.test.tworldapplication.activity.admin.WriteInNewActivity;
import com.test.tworldapplication.activity.card.MessageCollectionNewActivity;
import com.test.tworldapplication.activity.order.OrderBkDetailActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.base.MyApplication;
import com.test.tworldapplication.base.OSSConfig;
import com.test.tworldapplication.entity.Admin;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.City;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.JudgeLocationResponse;
import com.test.tworldapplication.entity.MessageEvent;
import com.test.tworldapplication.entity.PostLocationEntity;
import com.test.tworldapplication.entity.PostLogin;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.PostRemarkOrderInfo;
import com.test.tworldapplication.entity.Province;
import com.test.tworldapplication.entity.RequestLogin;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.FileLogUtils;
import com.test.tworldapplication.utils.LocationHelper;
import com.test.tworldapplication.utils.LogUtils;
import com.test.tworldapplication.utils.SPUtil;
import com.test.tworldapplication.utils.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;
import wintone.passport.sdk.utils.CheckPermission;
import wintone.passport.sdk.utils.PermissionActivity;
import wintone.passport.sdk.utils.SharedPreferencesHelper;

@RuntimePermissions
public class LoginActivity extends BaseActivity implements SuccessNull {


    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvForget)
    TextView tvForget;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    String re = "";
    @BindView(R.id.etUser)
    EditText etUser;
    @BindView(R.id.etPassword)
    EditText etPassword;
    PostLogin postLogin;
    String from = "";
    SharedPreferences sharedPreferences;

    PostOpenPower postOpenPower;
    AlertDialog.Builder _dialog;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getFlag()) {
            case MessageEvent.LOCATION_MESSAGE:
                String content = messageEvent.getMessage();
                LogUtils.setAppendFile("locationMessage:" + content);
                if (!content.startsWith("null")) {
                    String[] arr = content.split(",");
                    String province = arr[0];
                    String city = "";
                    if (arr.length == 2)
                        city = arr[1];
                    else
                        city = arr[0];
                    dialog.getTvTitle().setText("正在进行位置校验");
                    dialog.show();
                    HttpPost<PostLocationEntity> httpPost = new HttpPost<>();
                    PostLocationEntity postLocationEntity = new PostLocationEntity();
                    postLocationEntity.setSession_token(Util.getLocalAdmin(LoginActivity.this)[0]);
                    postLocationEntity.setProvinceCode(province);
                    postLocationEntity.setCityCode(city);
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setParameter(postLocationEntity);
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postLocationEntity) + BaseCom.APP_PWD));

                    new OrderHttp().judgeLocation(new Subscriber<HttpRequest<JudgeLocationResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            LogUtils.setAppendFile("judgeLocation:" + e.toString());
                        }

                        @Override
                        public void onNext(HttpRequest<JudgeLocationResponse> booleanHttpRequest) {
                            dialog.dismiss();
                            dialog.getTvTitle().setText("正在登录");
                            LogUtils.setAppendFile("judgeLocation:" + new Gson().toJson(booleanHttpRequest));

                            if (booleanHttpRequest.getData().getJudgeLocation()) {

                                BaseCom.login = _requestLoginHttpRequest.getData();
                                SharedPreferences share = LoginActivity.this.getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
                                SharedPreferences.Editor edit = share.edit(); //编辑文件
                                edit.clear();
                                edit.putString("session_token", _requestLoginHttpRequest.getData().getSession_token());
                                edit.putString("gride", _requestLoginHttpRequest.getData().getGrade());
                                edit.commit();  //保存数据信息

                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(BaseCom.ADMIN, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit(); //编辑文件
                                editor.putString("user", userName);
                                editor.putString("password", passWord);

                                editor.commit();  //保存数据信息
                                LogUtils.setAppendFile("startActivity0:" + from);
                                if (!Util.isFastDoubleShow()) {
                                    LogUtils.setAppendFile("startHandler0");
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessageDelayed(message, 100);

                                }

                            } else {

                                SharedPreferences share = LoginActivity.this.getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
                                SharedPreferences.Editor edit = share.edit(); //编辑文件
                                edit.clear();
                                edit.putString("session_token", "");
                                edit.putString("gride", "");
                                edit.commit();  //保存数据信息
                                if (hasShow == 0) {
                                    hasShow = 1;
                                    _dialog.show();
                                }
                            }
                        }
                    }, httpPost);


                }
//
//                if (!content.equals("null,null")) {
//                    String provinceCode = "";
//                    String cityCode = "";
//                    String[] arr = content.split(",");
//                    String province = arr[0];
//                    String city = "";
//                    if (arr.length == 2)
//                        city = arr[1];
//                    Area area = BaseUtils.getArea(LoginActivity.this);
//                    List<Province> provinces = area.getList();
//                    Province province1 = new Province(province);
//                    if (provinces.contains(province1)) {
//                        int index = provinces.indexOf(province1);
//                        provinceCode = provinces.get(index).getP_id();
//                        if (!city.equals("")) {
//                            List<City> cities = provinces.get(index).getP_list();
//                            City city1 = new City(city);
//                            if (cities.contains(city1)) {
//                                int index0 = cities.indexOf(city1);
//                                cityCode = cities.get(index0).getC_id();
//                            }
//                        }
//
//                    }
//                    LogUtils.setAppendFile("judgeLocationRequest:" + provinceCode + "," + cityCode);
//                    if (!provinceCode.equals("")) {
//
////                        provinceCode = "88";
////
//                    }
//
//                }
                else {
                    BaseCom.login = _requestLoginHttpRequest.getData();
                    SharedPreferences share = LoginActivity.this.getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
                    SharedPreferences.Editor edit = share.edit(); //编辑文件
                    edit.clear();
                    edit.putString("session_token", _requestLoginHttpRequest.getData().getSession_token());
                    edit.putString("gride", _requestLoginHttpRequest.getData().getGrade());
                    edit.commit();  //保存数据信息

                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(BaseCom.ADMIN, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit(); //编辑文件
                    editor.putString("user", userName);
                    editor.putString("password", passWord);

                    editor.commit();  //保存数据信息
                    LogUtils.setAppendFile("startActivity0:" + from);
                    if (!Util.isFastDoubleShow()) {
                        LogUtils.setAppendFile("startHandler1");
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessageDelayed(message, 100);


//                        if (from == null || from.equals("1")) {
//                            Log.d("ddd", "3");
//                            LogUtils.setAppendFile("startActivity1:" + from);
//                            gotoActy(MainNewActivity.class);
//                            AppManager.getAppManager().finishActivity();
//
//                        } else {
//                            Log.d("ddd", "4");
//                            LogUtils.setAppendFile("startActivity2:" + from);
//                            AppManager.getAppManager().finishActivity();
//                            EventBus.getDefault().post(new MessageEvent(MessageEvent.START_COUNT, ""));
//                        }
                    }


                }

                break;
        }


    }

    int hasShow = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        if (BuildConfig.DEBUG) {
//
//            TextView testORg = findViewById(R.id.testOrg);
//            testORg.setVisibility(View.VISIBLE);
//            testORg.setOnClickListener(view -> {
//                Intent intent = new Intent(LoginActivity.this, TestOrcActivity.class);
//                startActivity(intent);
//            });
//        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        sharedPreferences = getSharedPreferences(BaseCom.ADMIN, MODE_PRIVATE);
        Log.d("yyy", Util.getVersion());
        _dialog = Util.createAlertDialog(this,
                "用户未在常用地使用", null);
        _dialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                hasShow = 0;
            }
        });


        from = getIntent().getStringExtra("from");
        if (from == null) {
            SharedPreferences share = getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
            String session = share.getString("gride", "");
            if (!session.equals("")) {
                Intent intent = new Intent(LoginActivity.this, MainNewActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity();
            }
        } else if (from != null && from.equals("1"))
            AppManager.getAppManager().finishAllActivityBesidesCurrent();
        String user = sharedPreferences.getString("user", "");
        String password = sharedPreferences.getString("password", "");
        etUser.setText(user);
        etPassword.setText(password);
        etUser.setSelection(etUser.getText().toString().length());
        dialog.getTvTitle().setText("正在登录");
//        uploadLog();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent();
                    intent.putExtra("from", "0");
                    intent.putExtra("isCompare", msg.obj.toString());
                    if (_requestLoginHttpRequest != null && _requestLoginHttpRequest.getData() != null) {
                        intent.putExtra("data", _requestLoginHttpRequest.getData());
                    }
                    intent.setClass(LoginActivity.this, WriteInNewActivity.class);
                    LogUtils.setAppendFile("startActivity:WriteInActivity");
                    startActivity(intent);
                    break;
                case 1:
                    if (from == null || from.equals("1")) {
                        Log.d("ddd", "3");
                        LogUtils.setAppendFile("startActivity1:" + from);
                        //如果不需要工号实名认证的话,先进行定位验证,通过后,再进行短信验证.
                        Intent intent2 = new Intent(LoginActivity.this,NumberVerificationActivity.class);
                        intent2.putExtra("phone", "13116770003");
                        startActivity(intent2);
//                        gotoActy(MainNewActivity.class);
//                        AppManager.getAppManager().finishActivity();

                    } else {
                        Log.d("ddd", "4");
                        LogUtils.setAppendFile("startActivity2:" + from);
                        AppManager.getAppManager().finishActivity();
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.START_COUNT, ""));
                    }
                    break;
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.tvForget, R.id.tvSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvForget:
                if (!Util.isFastDoubleClick()) {
                    gotoActy(PasswordForgetActivity.class);
                }
                break;
            case R.id.tvSubmit:
                /* 判断用户名和密码是否输入完整,登录成功后跳转页面 当前页面关闭,将返回回来的login保存至BaseCom,调用个人信息接口,储存至basecom*/
                if (!Util.isFastDoubleClick()) {
//                    uploadLog();
                    LoginActivityPermissionsDispatcher.callPhoneWithCheck(this);

                }
                break;
        }
    }

    //
    @OnClick(R.id.tvRegister)
    public void onClick() {
//        LocationHelper.getInstance().startLocation();


        if (!Util.isFastDoubleClick()) {
            gotoActy(RegisterActivity.class);
        }
    }


    @Override
    public void onSuccess() {
        if (from == null || from.equals("1")) {
            Log.d("ddd", "3");
//            gotoActy(MessageCollectionActivity.class);
//            gotoActy(PackageSelectActivity.class);
            gotoActy(MainNewActivity.class);
            AppManager.getAppManager().finishActivity();

//            finish();
        } else {
            Log.d("ddd", "4");
//            finish();
            AppManager.getAppManager().finishActivity();
        }


    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            if (from == null) {
                Log.d("aaa", "0");
                return super.onKeyDown(keyCode, event);
            } else {
                Log.d("aaa", "1");
                return false;
            }
        } else {
            Log.d("aaa", "2");
            return super.onKeyDown(keyCode, event);
        }

    }

    HttpRequest<RequestLogin> _requestLoginHttpRequest;
    String userName, passWord;

    private void uploadLog() {
        String path = LogUtils.getPath();
//        String path = SPUtil.get(MyApplication.context, "log_path", "").toString();
        if (!path.equals("") && FileLogUtils.checkFileExit(path)) {
//            Toast.makeText(this, "正在上传", Toast.LENGTH_SHORT).show();
            Log.d("xxx", "upload");
            File file = new File(path);
            if (!file.exists()) {
                Log.w("AsyncPutImage", "FileNotExist");
                Log.w("LocalFile", path);
                return;
            }
            Log.d("zzzzzz", file.getName());

            // 构造上传请求
            PutObjectRequest put = new PutObjectRequest(OSSConfig.BUCKET, "newLog/00" + "/" + userName + "/" + file.getName(), path);
            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    Log.d("vvvvv", currentSize + "");
                }
            });


            OSSAsyncTask task = MyApplication.oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    Log.d("vvvvv", "success");
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    Log.d("vvvvv", "failed");
                }
            });

        }
    }


    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE})
    void callPhone() {
        //检查gps是否开启
        if (Util.isOPen(LoginActivity.this)) {
            userName = etUser.getText().toString();
            passWord = etPassword.getText().toString();

            if (userName.equals("") || passWord.equals("")) {
                Util.createToast(this, "用户名和密码输入完整!");
            } else {
                dialog.show();
                HttpPost<PostLogin> httpPost = new HttpPost<PostLogin>();
                postLogin = new PostLogin();
                postLogin.setUserName(userName);
                postLogin.setPassword(Util.GetMD5Code(BaseCom.PASSWORD0 + passWord + BaseCom.PASSWORD1));
                httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postLogin) + BaseCom.APP_PWD));
                httpPost.setParameter(postLogin);
                Log.d("aaa", gson.toJson(httpPost));
                LogUtils.setAppendFile(userName + "," + passWord);
                new AdminHttp().login(new Subscriber<HttpRequest<RequestLogin>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        LogUtils.setAppendFile("loginError:" + e.toString());

                    }

                    @Override
                    public void onNext(HttpRequest<RequestLogin> requestLoginHttpRequest) {
                        dialog.dismiss();
                        LogUtils.setAppendFile(new Gson().toJson(requestLoginHttpRequest));
                        if (requestLoginHttpRequest.getCode() == BaseCom.NORMAL) {
                            _requestLoginHttpRequest = requestLoginHttpRequest;
                            SharedPreferences share = LoginActivity.this.getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
                            SharedPreferences.Editor edit = share.edit(); //编辑文件
                            edit.putString("session_token", requestLoginHttpRequest.getData().getSession_token());
                            edit.putString("user", etUser.getText().toString());
                            edit.putString("password", etPassword.getText().toString());
                            edit.commit();  //保存数据信息

                            if (requestLoginHttpRequest.getData().getIsLogin().equals("Y")) {
                                edit.putString("gride", "");
                                edit.commit();
                                Message message = new Message();
                                message.what = 0;
                                message.obj = requestLoginHttpRequest.getData().getIsCompare();
                                LogUtils.setAppendFile("startActivity:message");
                                handler.sendMessageDelayed(message, 100);

                            } else if (requestLoginHttpRequest.getData().getIsLogin().equals("N")) {

//                                edit.putString("gride", _requestLoginHttpRequest.getData().getGrade());
//                                edit.commit();  //保存数据信息
////
//                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(BaseCom.ADMIN, MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit(); //编辑文件
//                                editor.putString("user", userName);
//                                editor.putString("password", passWord);
//
//                                editor.commit();  //保存数据信息
//                                if (from == null) {
//
//                                    gotoActy(MainNewActivity.class);
//                                    finish();
//
//                                } else {
//                                    finish();
//                                }

                                LogUtils.setAppendFile("startLocation");
                                LocationHelper.getInstance().startLocation();
                            }
                        } else
                            Util.createToast(LoginActivity.this, requestLoginHttpRequest.getMes());

                    }
                }, httpPost);
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
        }


    }

    /**
     * ,4，当用户拒绝获取权限的提示
     */
    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE})
    void showDenied() {
        Toast.makeText(LoginActivity.this, "获得权限才可以使用", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE})
    void neverAskAgain() {
        Toast.makeText(LoginActivity.this, "请手动打开权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(LoginActivity.this, requestCode, grantResults);
    }
//
//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//
//            if (from == null) {
//                Log.d("aaa", "0");
//                return false;
//            } else {
//                Log.d("aaa", "1");
//                return true;
//            }
//
//        } else {
//            Log.d("aaa", "2");
//            return false;
//        }
//    }
}

