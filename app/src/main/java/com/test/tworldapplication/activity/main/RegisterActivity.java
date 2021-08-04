package com.test.tworldapplication.activity.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Photo;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.PostPictureUpload;
import com.test.tworldapplication.entity.PostRegister;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.entity.RequestRegister;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.CountDownTimerUtils;
import com.test.tworldapplication.utils.DialogManager;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.SquareCenterImageView;
import com.test.tworldapplication.view.WheelView;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import wintone.passport.sdk.utils.AppManager;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.imgLicence)
    SquareCenterImageView imgLicence;
    @BindView(R.id.imgLicenceRemove)
    ImageView imgLicenceRemove;
    @BindView(R.id.imgFront)
    ImageView imgFront;
    @BindView(R.id.imgFrontRemove)
    ImageView imgFrontRemove;
    @BindView(R.id.imgIdBack)
    ImageView imgIdBack;
    @BindView(R.id.imgIdBackRemove)
    ImageView imgIdBackRemove;
    int flag = 0;
    int state_zere = 0, state_one = 0, state_two = 0;
    String file_zero, file_one, file_two;
    @BindView(R.id.ll_channelType)
    LinearLayout llChannelType;
    List<String> list = new ArrayList<>();
    @BindView(R.id.tvChannelType)
    TextView tvChannelType;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etNameTrue)
    EditText etNameTrue;
    @BindView(R.id.etId)
    EditText etId;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etMail)
    EditText etMail;
    @BindView(R.id.etChannelName)
    EditText etChannelName;
    @BindView(R.id.etSuperiorReferral)
    EditText etSuperiorReferral;
    @BindView(R.id.tvSure)
    TextView tvSure;
    Bitmap bitmap_zero = null, bitmap_one = null;
    String strEtPhone = "";
    String base64_zero = "";
    String base64_one = "";
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.etAddressDetail)
    EditText etAddressDetail;
    int provinceIndex = 0, cityIndex = 0;//省份编码与城市编码
    @BindView(R.id.activity_register)
    LinearLayout activityRegister;

    private AlertDialog alertDialogialog;
    private TextView asureTextView, cancelTextView;
    private TextView chooseAreaTextView;
    private WheelView provinceWheelView, cityWheelView, countyWheelView;
    private JSONObject jsonObject;
    private String[] allProvince;
    private Map<String, String[]> cityMap = new HashMap<>();
    private Map<String, String[]> countyMap = new HashMap<>();
    private String currentProvince = "";
    private String currentCity = "";
    private String currentCounty = "";
    private List<String> p_list = new ArrayList<>();
    private List<String> c_list = new ArrayList<>();
    InputMethodManager
            imm;
    Area area;

    PopupWindow mPopupWindow;
    ViewHolder viewHolder;
    final int CAMERA = 100;
    File tempFile;
    private final int REQUEST_CODE_CAMERA = 10086;

    /*先解析xml获取选择地址的数据,用于选择地址,点击注册按钮 判断如果两张图片均无  直接提交信息,若不是 有一张图片就上传一张*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setBackGroundTitle("注册", true);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        area = BaseUtils.getArea(RegisterActivity.this);
        list.clear();

        list.add("一级代理");
        list.add("二级代理");
        list.add("渠道商");
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_photo, null);
        viewHolder = new ViewHolder(music_popunwindwow);
        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                activityRegister.setAlpha((float) 1);

            }
        });

    }

    @OnClick(R.id.tvSure)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            Log.d("aaa", provinceIndex + "");
            Log.d("aaa", cityIndex + "");

//        final String strChannelType_true = "";
            final String strName = etName.getText().toString();
            final String strPassword = etPassword.getText().toString();
            final String strTrueName = etNameTrue.getText().toString();
            final String strId = etId.getText().toString();//无
            final String strPhone = etPhone.getText().toString();
            final String strCode = etCode.getText().toString();
            final String strMail = etMail.getText().toString();
            final String strAddress = etAddressDetail.getText().toString();
            final String strChannelType = tvChannelType.getText().toString();
            final String strChannelName = etChannelName.getText().toString();
            final String strSuperiorReferral = etSuperiorReferral.getText().toString();
            List<Integer> int_list = new ArrayList<>();
            for (int i = 0; i < strPassword.length(); i++) {
                if (!Character.isDigit(strPassword.charAt(i))) {
                    int_list.add(1);
                    Log.d("aaa", "1");
                } else {
                    int_list.add(2);
                    Log.d("aaa", "2");
                }
            }
            List<Integer> name_list = new ArrayList<>();
            for (int i = 0; i < strName.length(); i++) {
                if (Character.isDigit(strName.charAt(i))) {
                    name_list.add(1);
                    Log.d("aaa", "1");
                } else {
                    name_list.add(2);
                    Log.d("aaa", "2");
                }
            }

            if (strName.equals("") || strPassword.equals("") || strTrueName.equals("") || strId.equals("") || strPhone.equals("") || strAddress.equals("") || strChannelType.equals("请选择") || strChannelName.equals("") || strSuperiorReferral.equals("") || strCode.equals("")) {
                Util.createToast(RegisterActivity.this, "请将信息填写完整!");

            } else if (!int_list.contains(1) || !int_list.contains(2) || strPassword.length() < 6 || strPassword.length() > 12) {
                Util.createToast(RegisterActivity.this, "请输入6-12位数字和字母组合密码!");
            } else if (!name_list.contains(2)) {
                Util.createToast(RegisterActivity.this, "用户名不能只由数字组成!");
            } else if (strName.length() > 30) {
                Util.createToast(RegisterActivity.this, "用户名过长!");
            } else if (strPhone.length() != 11) {
                Util.createToast(RegisterActivity.this, "请输入正确的手机号码!");
            } else if (strId.length() != 15 && strId.length() != 18) {
                Log.d("aaa", strId.length() + "");
                Util.createToast(RegisterActivity.this, "请输入正确的身份证号!");
            } else {
                /*如果没有图片  直接提交信息*/
                if (bitmap_zero == null && bitmap_one == null) {
                    dialog.getTvTitle().setText("正在提交");
                    dialog.show();
                    String strChannelType_true = "";
                    switch (strChannelType) {
                        case "一级代理":
                        case "二级代理":
                            strChannelType_true = "0";
                            break;
                        case "渠道商":
                            strChannelType_true = "1";
                            break;
                    }

                    HttpPost<PostRegister> httpPost = new HttpPost<PostRegister>();
                    PostRegister postRegister = new PostRegister();
                    postRegister.setUserName(strName);
                    postRegister.setCodeId(strId);
                    postRegister.setContact(strTrueName);
                    postRegister.setEmail(strMail);
                    postRegister.setOrgName(strChannelName);
                    postRegister.setOrgType(strChannelType_true);
                    postRegister.setPassword(Util.GetMD5Code(strPassword));
                    postRegister.setRemdCode(strSuperiorReferral);
                    postRegister.setTel(strPhone);
                    postRegister.setCaptcha(strCode);
                    postRegister.setOrgAddress(strAddress);
                    postRegister.setProvinceCode(area.getList().get(provinceIndex).getP_id());
                    postRegister.setCityCode(area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id());
                    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                    httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postRegister) + BaseCom.APP_PWD));
                    httpPost.setParameter(postRegister);
                    new AdminHttp().register(AdminRequest.register(new SuccessValue<HttpRequest<RequestRegister>>() {
                        @Override
                        public void OnSuccess(HttpRequest<RequestRegister> value) {
                            Log.d("aaa", value.getMes());
                            Log.d("aaa", value.getCode() + "");
                            if (value.getCode() == BaseCom.NORMAL) {

                                Util.createToast(RegisterActivity.this, "注册成功,等待审核!");
                                AppManager.getAppManager().finishActivity();
                            } else
                                Toast.makeText(RegisterActivity.this, value.getMes(), Toast.LENGTH_SHORT).show();
                        }
                    }, dialog), httpPost);

                } else {
                    dialog.getTvTitle().setText("正在上传图片");
                    dialog.show();
                    HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
                    PostPictureUpload postPictureUpload = new PostPictureUpload();
                    postPictureUpload.setType("4");
                    Photo photo = new Photo();
                    if (bitmap_zero != null && bitmap_one != null) {
                        photo.setPhoto1(BitmapUtil.bitmapToBase64(BitmapUtil.compressBitmap(bitmap_zero)));
                        photo.setPhoto2(BitmapUtil.bitmapToBase64(BitmapUtil.compressBitmap(bitmap_one)));
                    } else {
                        if (bitmap_zero != null) {
                            photo.setPhoto1(BitmapUtil.bitmapToBase64(BitmapUtil.compressBitmap(bitmap_zero)));
                        }
                        if (bitmap_one != null) {
                            photo.setPhoto1(BitmapUtil.bitmapToBase64(BitmapUtil.compressBitmap(bitmap_one)));
                        }
                    }


                    httpPost.setPhoto(photo);
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setParameter(postPictureUpload);
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));

                    new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(RegisterActivity.this, dialog, new SuccessValue<RequestPictureUpload>() {
                        @Override
                        public void OnSuccess(RequestPictureUpload value) {
                            Log.d("aaa", value.getPhoto1());
                            dialog.getTvTitle().setText("正在提交");
                            dialog.show();
                            String strChannelType_true = "";
                            switch (strChannelType) {
                                case "一级代理":
                                case "二级代理":
                                    strChannelType_true = "0";
                                    break;
                                case "渠道商":
                                    strChannelType_true = "1";
                                    break;
                            }

                            HttpPost<PostRegister> httpPost = new HttpPost<PostRegister>();
                            PostRegister postRegister = new PostRegister();

                            postRegister.setProvinceCode(area.getList().get(provinceIndex).getP_id());
                            postRegister.setCityCode(area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id());
                            postRegister.setUserName(strName);
                            postRegister.setCodeId(strId);
                            postRegister.setOrgAddress(strAddress);
                            postRegister.setContact(strTrueName);
                            postRegister.setEmail(strMail);
                            postRegister.setOrgName(strChannelName);
                            postRegister.setOrgType(strChannelType_true);
                            postRegister.setPassword(Util.GetMD5Code(strPassword));
                            postRegister.setRemdCode(strSuperiorReferral);
                            postRegister.setTel(strPhone);
                            postRegister.setCaptcha(strCode);

                            if (bitmap_zero != null && bitmap_one != null) {
                                postRegister.setPhotoOne(value.getPhoto1());
                                postRegister.setPhotoTwo(value.getPhoto2());
                            } else {
                                if (bitmap_zero != null) {
                                    postRegister.setPhotoOne(value.getPhoto1());
                                }
                                if (bitmap_one != null) {
                                    postRegister.setPhotoTwo(value.getPhoto1());
                                }
                            }
                            httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                            httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postRegister) + BaseCom.APP_PWD));
                            httpPost.setParameter(postRegister);
                            new AdminHttp().register(AdminRequest.register(new SuccessValue<HttpRequest<RequestRegister>>() {
                                @Override
                                public void OnSuccess(HttpRequest<RequestRegister> value) {
                                    Log.d("aaa", value.getCode() + "");
                                    Log.d("aaa", value.getMes());
                                    if (value.getCode() == BaseCom.NORMAL) {
                                        Util.createToast(RegisterActivity.this, "注册成功,等待审核!");
                                        AppManager.getAppManager().finishActivity();
                                    } else
                                        Toast.makeText(RegisterActivity.this, value.getMes(), Toast.LENGTH_SHORT).show();
                                }
                            }, dialog), httpPost);
                        }
                    }), httpPost);
                }


            }
        }
    }


    @OnClick({R.id.ll_address, R.id.tvGetCode, R.id.ll_channelType, R.id.imgLicence, R.id.imgLicenceRemove, R.id.imgFront, R.id.imgFrontRemove, R.id.imgIdBack, R.id.imgIdBackRemove})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvGetCode:
                strEtPhone = etPhone.getText().toString();
                if (strEtPhone.length() != 11) {
                    Util.createToast(RegisterActivity.this, "请输入正确的手机号码");
                } else {
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetCode, 60000, 1000);

                    mCountDownTimerUtils.start();

                    HttpPost<PostCode> httpPost = new HttpPost<PostCode>();
                    PostCode postCode = new PostCode();
                    postCode.setTel(etPhone.getText().toString());
                    postCode.setCaptcha_type("1");
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
                    httpPost.setParameter(postCode);
                    Log.d("aaa", gson.toJson(httpPost));
                    new AdminHttp().getCode(AdminRequest.getCode(dialog), httpPost);
                }
                break;
            case R.id.ll_channelType:
                if (!Util.isFastDoubleClick())
                    Util.createDialog(RegisterActivity.this, list, tvChannelType, null);
                break;
            case R.id.imgLicence:
                flag = 0;
                Log.d("aaa", "click");

                switch (state_zere) {
                    case 0:
                        requestPermission();
//                        DialogManager.changeAvatar(RegisterActivity.this, mOnHanlderResultCallback);
//                        activityRegister.setAlpha((float) 0.1);
//                        mPopupWindow.showAtLocation(imgFront, Gravity.BOTTOM, 0, 0);
//                        BaseUtils.pickPhoto(RegisterActivity.this);
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgLicence.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(RegisterActivity.this, file_zero, DisplayUtil.dp2px(RegisterActivity.this, 60), DisplayUtil.dp2px(RegisterActivity.this, 60), location[0], location[1]);
                }


                break;
            case R.id.imgLicenceRemove:
                flag = 0;
                base64_zero = "";
                disPlayImage(null);
                break;
            case R.id.imgFront:
                Log.d("aaa", "click");
                flag = 1;
                switch (state_one) {
                    case 0:
                        requestPermission();
//                        DialogManager.changeAvatar(RegisterActivity.this, mOnHanlderResultCallback);
//                        activityRegister.setAlpha((float) 0.1);
//                        mPopupWindow.showAtLocation(imgFront, Gravity.BOTTOM, 0, 0);

//                        BaseUtils.pickPhoto(RegisterActivity.this);
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgFront.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(RegisterActivity.this, file_one, DisplayUtil.dp2px(RegisterActivity.this, 60), DisplayUtil.dp2px(RegisterActivity.this, 60), location[0], location[1]);
                        break;
                }

                break;
            case R.id.imgFrontRemove:
                flag = 1;
                base64_one = "";
                disPlayImage(null);
                break;
            case R.id.imgIdBack:
                Log.d("aaa", "click");
                flag = 2;

                switch (state_two) {
                    case 0:
                        BaseUtils.pickPhoto(RegisterActivity.this);
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgIdBack.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(RegisterActivity.this, file_two, DisplayUtil.dp2px(RegisterActivity.this, 60), DisplayUtil.dp2px(RegisterActivity.this, 60), location[0], location[1]);
                        break;
                }
                break;
            case R.id.imgIdBackRemove:
                flag = 2;
                disPlayImage(null);
                break;
            case R.id.ll_address:
                if (!Util.isFastDoubleClick())
                    showChooseAreaDialog_new();
                break;


        }
    }

    public static void showCamera(Activity activity, File file, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = activity.getPackageName() + ".fileprovider"; //【清单文件中provider的authorities属性的值】
            Log.d("xxx", authority);
            Log.d("xxx", file.getName());
            uri = FileProvider.getUriForFile(activity, authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        Log.i("bqt", "【uri】" + uri);//【content://{$authority}/files/bqt/temp】或【file:///storage/emulated/0/bqt/temp】
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
    }


    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            DialogManager.changeAvatar(RegisterActivity.this, new SuccessNull() {
                @Override
                public void onSuccess() {
                    File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                    fileDir = new File(fileDir, "bqt");
                    if (!fileDir.exists()) fileDir.mkdirs();
                    switch (flag) {
                        case 0:
                            tempFile = new File(fileDir, "temp_zc0.jpg");
                            break;
                        case 1:
                            tempFile = new File(fileDir, "temp_zc1.jpg");
                            break;
                        case 2:
                            tempFile = new File(fileDir, "temp_zc2.jpg");
                            break;


                    }

                    showCamera(RegisterActivity.this, tempFile, REQUEST_CODE_CAMERA);
                }
            }, mOnHanlderResultCallback);
        else
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED&& grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    DialogManager.changeAvatar(RegisterActivity.this, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                            fileDir = new File(fileDir, "bqt");
                            if (!fileDir.exists()) fileDir.mkdirs();
                            switch (flag) {
                                case 0:
                                    tempFile = new File(fileDir, "temp_zc0.jpg");
                                    break;
                                case 1:
                                    tempFile = new File(fileDir, "temp_zc1.jpg");
                                    break;
                                case 2:
                                    tempFile = new File(fileDir, "temp_zc2.jpg");
                                    break;


                            }

                            showCamera(RegisterActivity.this, tempFile, REQUEST_CODE_CAMERA);
                        }
                    }, mOnHanlderResultCallback);
                } else {
                    Util.createToast(RegisterActivity.this, "请赋予权限");
                }

                break;
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.i("test", "onHanlderSuccess: " + resultList.toString());
            if (resultList != null) {
                String path = resultList.get(0).getPhotoPath();
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                viewHolder.imgSelect.setImageBitmap(bitmap);
                Log.d("qqq", path);
                switch (flag) {
                    case 0:
                        file_zero = path;
                        break;
                    case 1:
                        file_one = path;
                        break;
                    case 2:
                        file_two = path;
                        break;
                }
                disPlayImage(path);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

    private void clearFocus(EditText editText) {
//        etName.setFocusable(false);
//        etPassword.setFocusable(false);
    }

    private void disPlayImage(String capturePath) {
        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(getResources(), capturePath, DisplayUtil.dp2px(RegisterActivity.this, 200), DisplayUtil.dp2px(RegisterActivity.this, 200));

        switch (flag) {
            case 0:

                imgLicence.setImageBitmap(bitmap);

                if (capturePath != null) {
                    state_zere = 1;
                    bitmap_zero = bitmap;
                    imgLicenceRemove.setVisibility(View.VISIBLE);
                } else {
                    state_zere = 0;
                    bitmap_zero = null;
                    imgLicenceRemove.setVisibility(View.INVISIBLE);
                }

                break;
            case 1:

                imgFront.setImageBitmap(bitmap);

                if (capturePath != null) {
                    state_one = 1;
                    bitmap_one = bitmap;
                    imgFrontRemove.setVisibility(View.VISIBLE);
                } else {
                    state_one = 0;
                    bitmap_one = null;
                    imgFrontRemove.setVisibility(View.INVISIBLE);
                }

                break;
            case 2:
                imgIdBack.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_two = 1;
                    imgIdBackRemove.setVisibility(View.VISIBLE);
                } else {
                    state_two = 0;
                    imgIdBackRemove.setVisibility(View.INVISIBLE);
                }


                break;


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String capturePath = cursor.getString(columnIndex);
                    switch (flag) {
                        case 0:
                            file_zero = capturePath;
                            break;
                        case 1:
                            file_one = capturePath;
                            break;
                        case 2:
                            file_two = capturePath;
                            break;
                    }
                    cursor.close();
                    disPlayImage(capturePath);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    switch (flag) {
                        case 0:
                            disPlayImage(file_zero);
                            break;
                        case 1:
                            disPlayImage(file_one);
                            break;

                    }

                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    String path = tempFile.getAbsolutePath();
                    switch (flag) {
                        case 0:
                            file_zero = path;
                            disPlayImage(file_zero);
                            break;
                        case 1:
                            file_one = path;
                            disPlayImage(file_one);
                            break;
                        case 2:
                            file_two = path;
                            disPlayImage(file_two);
                            break;

                    }

                }
                break;

        }
    }


    private void dismissChosseAreaDialog() {
        if (alertDialogialog != null && alertDialogialog.isShowing()) {
            alertDialogialog.dismiss();
        }
    }

    private void showChooseAreaDialog_new() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_choose_area, null);
        alertDialogialog = new AlertDialog.Builder(this, R.style.ActionSheetDialogStyle).create();
        alertDialogialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialogialog.show();
        asureTextView = (TextView) view.findViewById(R.id.asure_textview);
        cancelTextView = (TextView) view.findViewById(R.id.cancel_textview);
        chooseAreaTextView = (TextView) view.findViewById(R.id.choose_area_textview);

        provinceWheelView = (WheelView) view.findViewById(R.id.province_wheelview);
        cityWheelView = (WheelView) view.findViewById(R.id.city_wheelview);
        countyWheelView = (WheelView) view.findViewById(R.id.county_wheelview);
        provinceWheelView.setDivideLineColor(getResources().getColor(R.color.colorBlue));
        provinceWheelView.setTextFocusColor(getResources().getColor(R.color.colorGray));
        provinceWheelView.setTextOutsideColor(getResources().getColor(R.color.colorGray6));
        provinceWheelView.setTextSize(14);
//        provinceWheelView.setFontFace(DisplayUtil.getFontFace());
        provinceWheelView.setOffset(4);
        Log.d("bbb", "0");
        provinceWheelView.setTextPadding(0, DisplayUtil.dp2px(this, 5), 0, DisplayUtil.dp2px(this, 5));
        provinceWheelView.setItems(BaseUtils.provinceStrList);
        provinceWheelView.setSeletion(2);
        provinceIndex = 2;
        List<String> provinceStrList = BaseUtils.provinceStrList;
        currentProvince = provinceStrList.get(2);
        provinceWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                provinceIndex = selectedIndex - 4;
                currentProvince = item;
                List<String> c_list = new ArrayList<String>();
                for (int i = 0; i < area.getList().get(provinceIndex).getP_list().size(); i++) {
                    c_list.add(area.getList().get(provinceIndex).getP_list().get(i).getC_name());
                }
                cityWheelView.refresh(c_list);
                cityWheelView.setSeletion(0);
                cityIndex = 0;
                currentCity = c_list.get(0);
            }
        });
        cityWheelView.setDivideLineColor(getResources().getColor(R.color.colorBlue));
        cityWheelView.setTextFocusColor(getResources().getColor(R.color.colorGray));
        cityWheelView.setTextOutsideColor(getResources().getColor(R.color.colorGray6));
        cityWheelView.setTextSize(14);
//        cityWheelView.setFontFace(DisplayUtil.getFontFace());
        cityWheelView.setOffset(4);
        cityWheelView.setTextPadding(0, DisplayUtil.dp2px(this, 5), 0, DisplayUtil.dp2px(this, 5));
        cityWheelView.setItems(BaseUtils.cityStrList);
        cityIndex = 0;
        cityWheelView.setSeletion(0);
        currentCity = BaseUtils.cityStrList.get(0);
        cityWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                cityIndex = selectedIndex - 4;
                currentCity = item;
            }
        });

        asureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissChosseAreaDialog();
                tvAddress.setText(currentProvince + currentCity);
                Log.d("aaa", currentProvince);
                Log.d("aaa", provinceIndex + "");
                Log.d("aaa", currentCity);
                Log.d("aaa", cityIndex + "");

            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissChosseAreaDialog();

            }
        });

        alertDialogialog.setCancelable(false);
        alertDialogialog.setCanceledOnTouchOutside(false);
        alertDialogialog.getWindow().setLayout(DisplayUtil.getWindowWidth(this), WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialogialog.setContentView(view);
    }


    class ViewHolder {
        @BindView(R.id.tvTake)
        TextView tvTake;
        @BindView(R.id.tvBook)
        TextView tvBook;
        @BindView(R.id.tvCancel)
        TextView tvCancel;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.tvTake, R.id.tvBook, R.id.tvCancel})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvTake:
                    Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String strImgPath = Environment.getExternalStorageDirectory()
                            .toString() + "/dlion/";// 存放照片的文件夹
                    String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                            .format(new Date()) + ".jpg";// 照片命名
                    File out = new File(strImgPath);
                    if (!out.exists()) {
                        out.mkdirs();
                    }
                    out = new File(strImgPath, fileName);
                    strImgPath = strImgPath + fileName;// 该照片的绝对路径
                    switch (flag) {
                        case 0:
                            file_zero = strImgPath;
                            break;
                        case 1:
                            file_one = strImgPath;
                            break;
                    }

                    Uri uri = Uri.fromFile(out);
                    imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(imageCaptureIntent, 1);


                    mPopupWindow.dismiss();
                    break;
                case R.id.tvBook:
                    BaseUtils.pickPhoto(RegisterActivity.this);

                    mPopupWindow.dismiss();
                    break;
                case R.id.tvCancel:
                    mPopupWindow.dismiss();
                    break;
            }
        }
    }
}
