package com.test.tworldapplication.activity.admin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.kernal.passportreader.sdk.CardsCameraActivity;
import com.kernal.passportreader.sdk.utils.DefaultPicSavePath;
import com.kernal.passportreader.sdk.utils.ManageIDCardRecogResult;
import com.luck.picture.lib.tools.ToastUtils;
import com.squareup.picasso.Picasso;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.SelectAddressActivity;
import com.test.tworldapplication.activity.card.AccountClosingActivity;
import com.test.tworldapplication.activity.card.FaceRecordingActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Photo;
import com.test.tworldapplication.entity.PostCaptcha;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.PostPictureUpload;
import com.test.tworldapplication.entity.PostReSubmit;
import com.test.tworldapplication.entity.RequestLogin;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.Constants;
import com.test.tworldapplication.utils.CountDownTimerUtils;
import com.test.tworldapplication.utils.DialogManager;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.GetJsonDataUtil;
import com.test.tworldapplication.utils.JsonBean;
import com.test.tworldapplication.utils.LogUtils;
import com.test.tworldapplication.utils.SPUtil;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.WheelView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kernal.idcard.android.RecogParameterMessage;
import kernal.idcard.android.RecogService;
import kernal.idcard.android.ResultMessage;
import kernal.idcard.camera.CardOcrRecogConfigure;
import org.json.JSONArray;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wintone.passport.sdk.utils.AppManager;
import wintone.passport.sdk.utils.Devcode;
import wintone.passport.sdk.utils.SharedPreferencesHelper;

public class WriteInNewActivity extends BaseActivity {

  @BindView(R.id.user_name) EditText etName;
  @BindView(R.id.user_numb) EditText etNumb;
  @BindView(R.id.user_id_card) EditText etId;
  @BindView(R.id.user_id_address) EditText etIdAddress;
  @BindView(R.id.user_e_mail) EditText etEmail;
  @BindView(R.id.user_provience) TextView tvProvince;
  @BindView(R.id.user_address) EditText etAddress;
  @BindView(R.id.imgBack) ImageView imgBack;
  @BindView(R.id.imgFront) ImageView imgFront;
  @BindView(R.id.imgBackend) ImageView imgBackend;
  @BindView(R.id.imgHandle) ImageView imgHandle;
  @BindView(R.id.imgFrontRemove) ImageView imgFrontRemove;
  @BindView(R.id.imgBackendRemove) ImageView imgBackendRemove;
  @BindView(R.id.imgHandleRemove) ImageView imgHandleRemove;
  @BindView(R.id.tvGetCode) TextView tvGetCode;
  @BindView(R.id.submit) TextView tvSubmit;
  @BindView(R.id.phone_code) EditText etCode;
  @BindView(R.id.gridNameEt) EditText gridNameEt;
  @BindView(R.id.imgLicence) ImageView imgLicence;
  @BindView(R.id.imgLicenceRemove) ImageView imgLicenceRemove;
  @BindView(R.id.imgGrid) ImageView imgGrid;
  @BindView(R.id.imgGridRemove) ImageView imgGridRemove;
  @BindView(R.id.address_layout) LinearLayout address_layout;
  View clickView;
  String path_front = "", path_backend = "", path_handle = "";
  String path_licence = "", path_grid = "";
  File tempFile;
  private final int REQUEST_CODE_CAMERA = 10086;
  private WheelView provinceWheelView, cityWheelView, countyWheelView;
  private String currentProvince = "";
  private String currentCity = "";
  private AlertDialog alertDialogialog;
  private TextView asureTextView, cancelTextView;
  Area area;
  int provinceIndex = 0, cityIndex = 0;//省份编码与城市编码
  private TextView chooseAreaTextView;
  private String isCompare;
  int indextime = 0;

  private ArrayList<JsonBean> options1Items = new ArrayList<>();
  private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
  private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

  private Thread thread;
  private static final int MSG_LOAD_DATA = 0x0001;
  private static final int MSG_LOAD_SUCCESS = 0x0002;
  private static final int MSG_LOAD_FAILED = 0x0003;
  final int STARTSCAN = 60;
  private boolean isLoaded = false;
  private final int CAMERA = 10009;
  private String selectPath;
  private GeoCoder geoCoder;
  private PoiInfo poiInfo;
  private int uploadSuccessCount;
  private Map<String, String> imgMap = new HashMap<>();
  private int totalPic;
  RequestLogin requestLogin;
  Map<String, String> map = new HashMap<>();

  @Override
  protected void onDestroy() {
    if (mLocationClient != null) mLocationClient.stop();
    if (geoCoder != null) geoCoder.destroy();
    super.onDestroy();
    LogUtils.setAppendFile("WriteInActivity:onDestroy");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_info_write_in_new);
    ButterKnife.bind(this);
    initWarnDialog();
    initLocation();

    imgBack.setVisibility(View.VISIBLE);
    requestLogin = getIntent().getParcelableExtra("data");
    if (requestLogin != null && !TextUtils.isEmpty(requestLogin.getCardId())) {
      etId.setText(requestLogin.getCardId());
      etIdAddress.setText(requestLogin.getAddress());
      etName.setText(requestLogin.getName());
      etNumb.setText(requestLogin.getTel());
      etNumb.setEnabled(false);
    }
    area = BaseUtils.getArea(WriteInNewActivity.this);
    mHandler.sendEmptyMessage(MSG_LOAD_DATA);

    String from = getIntent().getStringExtra("from");
    switch (from) {
      case "0":
        setBackGroundTitle("工号实名制", true);
        isCompare = getIntent().getStringExtra("isCompare");
        switch (isCompare) {
          case "N":
            tvSubmit.setText("提交");
            break;
          case "Y":
            tvSubmit.setText("下一步");
            break;
        }
        break;
      case "1":
        tvSubmit.setText("提交");
        setBackGroundTitle("补登记资料", true);
        break;
    }

    //        SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
    //        SharedPreferences.Editor editor = sharedPreferences.edit();
    //        editor.putString("YorN", "1");
    //        editor.commit();
    //        imgFront.setBackgroundResource(R.drawable.front);
    //        imgBackend.setBackgroundResource(R.drawable.backend);
    //        imgHandle.setBackgroundResource(R.drawable.handle);
    imgFrontRemove.setVisibility(View.GONE);
    imgBackendRemove.setVisibility(View.GONE);
    imgHandleRemove.setVisibility(View.GONE);
    Picasso.with(this).load(R.drawable.front).into(imgFront);
    Picasso.with(this).load(R.drawable.backend).into(imgBackend);
    Picasso.with(this).load(R.drawable.handle).into(imgHandle);
  }

  LocationClient mLocationClient;
  private void initLocation() {
    //定位初始化
    geoCoder = GeoCoder.newInstance();
    geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
      @Override
      public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

      }

      @Override
      public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
          ToastUtils.s(WriteInNewActivity.this, "获取位置信息失败");
          return;
        } else {
          if (reverseGeoCodeResult.getPoiList() != null) {
            poiInfo = reverseGeoCodeResult.getPoiList().get(0);
            ReverseGeoCodeResult.AddressComponent addressCompents = reverseGeoCodeResult.getAddressDetail();
            if (addressCompents != null) {
              if (TextUtils.isEmpty(poiInfo.area)) {
                poiInfo.area = addressCompents.district;
              }
              if (TextUtils.isEmpty(poiInfo.province)) {
                poiInfo.province = addressCompents.province;
              }
              formatPoiInfo();
              String province = (TextUtils.isEmpty(addressCompents.province) ? "" : addressCompents.province);
              String city = (TextUtils.isEmpty(addressCompents.city) ? "" : addressCompents.city);
              String pro_city = "";
              if (province.equals(city)) {
                pro_city = province;
              } else {
                pro_city = province + city;
              }
              tvProvince.setText(pro_city + (TextUtils.isEmpty(addressCompents.district) ? "" : addressCompents.district));
            }
            etAddress.setText(poiInfo.getAddress());
          }
          //行政区号
        }
      }
    });
    mLocationClient = new LocationClient(this);
    //通过LocationClientOption设置LocationClient相关参数
    LocationClientOption option = new LocationClientOption();
    option.setOpenGps(true); // 打开gps
    option.setCoorType("bd09ll"); // 设置坐标类型
    option.setScanSpan(0);


    //设置locationClientOption
    mLocationClient.setLocOption(option);

    //注册LocationListener监听器
    mLocationClient.registerLocationListener(bdLocation -> {
      if (poiInfo != null) return;
      if (bdLocation != null) {
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
            .location(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()))
            .newVersion(1)
            .radius(100)
        );

      }
    });
    //开启地图定位图层
    mLocationClient.start();
  }

  private void formatPoiInfo() {
    if (poiInfo == null) return;
    if (!TextUtils.isEmpty(poiInfo.province) && poiInfo.province.endsWith("省")) {
      poiInfo.province = poiInfo.province.substring(0, poiInfo.province.length() - 1);
    }
    if (!TextUtils.isEmpty(poiInfo.province) && poiInfo.province.endsWith("市")) {
      poiInfo.province = poiInfo.province.substring(0, poiInfo.province.length() - 1);
    }
    if (!TextUtils.isEmpty(poiInfo.city) && poiInfo.city.endsWith("市")) {
      poiInfo.city = poiInfo.city.substring(0, poiInfo.city.length() - 1);
    }
  }

  @OnClick({
      R.id.imgBack, R.id.submit, R.id.tvGetCode, R.id.province_layout, R.id.user_provience,
      R.id.imgFront, R.id.imgBackend, R.id.imgHandle, R.id.imgFrontRemove, R.id.imgBackendRemove,
      R.id.imgHandleRemove, R.id.address_layout, R.id.imgGridRemove, R.id.imgGrid, R.id.imgLicenceRemove, R.id.imgLicence
  })
  public void onClick(View view) {
    switch (view.getId()) {
      //case R.id.address_layout:
      //  Intent intent = new Intent(this, SelectAddressActivity.class);
      //  startActivityForResult(intent, 99);
      //  break;
      case R.id.imgBack:
        AppManager.getAppManager().finishActivity();
        break;
      case R.id.submit:
        if (!Util.isFastDoubleClick()) {
          String mName = etName.getText().toString();
          String mNumb = etNumb.getText().toString();
          String mId = etId.getText().toString();
          String mEmail = etEmail.getText().toString();
          String mProvince = tvProvince.getText().toString();
          String mAddress = etAddress.getText().toString();

          if (etName.getText().toString().equals("")) {
            Util.createToast(WriteInNewActivity.this, "请输入姓名");
          } else if (!checkPhone(etNumb.getText().toString())) {
            Util.createToast(WriteInNewActivity.this, "请输入正确的手机号");
          } else if (!isIdcard(etId.getText().toString())) {
            Util.createToast(WriteInNewActivity.this, "请输入正确的身份证号码");
          } else if (etIdAddress.getText().toString().equals("")) {
            Util.createToast(WriteInNewActivity.this, "请输入正确的证件地址");
          } else if (tvProvince.getText().toString().equals("请选择")) {
            Util.createToast(WriteInNewActivity.this, "请选择渠道地址");
          } else if (etAddress.getText().toString().equals("")) {
            Util.createToast(WriteInNewActivity.this, "请输入详细地址");
          } else if (gridNameEt.getText().toString().equals("")) {
            Util.createToast(WriteInNewActivity.this, "请输入网点名称");
          } else if (etCode.getText().toString().equals("")) {
            Util.createToast(WriteInNewActivity.this, "请输入验证码");
          } else if (path_front.equals("") || path_backend.equals("") || path_handle.equals("")) {
            Util.createToast(WriteInNewActivity.this, "请将图片信息填写完整");
          } else if (TextUtils.isEmpty(path_licence) && TextUtils.isEmpty(path_grid)) {
            Util.createToast(WriteInNewActivity.this, "营业执照或网点照片至少上传一张");
          } else {
            dialog.getTvTitle().setText("资料提交中");
            dialog.show();
            HttpPost<PostCaptcha> httpPost = new HttpPost<>();
            PostCaptcha postCaptcha = new PostCaptcha();
            postCaptcha.setTel(mNumb);
            postCaptcha.setCaptcha(etCode.getText().toString());
            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost.setParameter(postCaptcha);
            httpPost.setApp_sign(
                Util.encode(BaseCom.APP_PWD + gson.toJson(postCaptcha) + BaseCom.APP_PWD));
            Log.d("aaa", gson.toJson(httpPost));
            new AdminHttp().verifyCaptcha(
                AdminRequest.verifyCaptcha(WriteInNewActivity.this, dialog, new SuccessNull() {
                  @Override
                  public void onSuccess() {
                    if (tvSubmit.getText().equals("提交")) {
                      dialog.getTvTitle().setText("正在上传图片");
                      dialog.show();
                      uploadSuccessCount = 0;
                      //HttpPost<PostPictureUpload> httpPost0 = new HttpPost<>();
                      //PostPictureUpload postPictureUpload = new PostPictureUpload();
                      //postPictureUpload.setSession_token(
                      //    Util.getLocalAdmin(WriteInNewActivity.this)[0]);
                      //postPictureUpload.setType("6");
                      //final Photo photo = new Photo();
                      //photo.setPhoto1(BitmapUtil.urlToBase64(WriteInNewActivity.this, path_front));
                      //photo.setPhoto2(
                      //    BitmapUtil.urlToBase64(WriteInNewActivity.this, path_backend));
                      //photo.setPhoto3(BitmapUtil.urlToBase64(WriteInNewActivity.this, path_handle));
                      //
                      //httpPost0.setPhoto(photo);
                      //httpPost0.setApp_key(Util.encode(BaseCom.APP_KEY));
                      //httpPost0.setParameter(postPictureUpload);
                      //httpPost0.setApp_sign(Util.encode(
                      //    BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
                      uploadImage(BitmapUtil.urlToBase64(WriteInNewActivity.this, path_front), Constants.PHOTOONE);
                      uploadImage(BitmapUtil.urlToBase64(WriteInNewActivity.this, path_backend), Constants.PHOTOTWO);
                      uploadImage(BitmapUtil.urlToBase64(WriteInNewActivity.this, path_handle), Constants.PHOTOTHREE);
                      totalPic = 3;
                      if (!TextUtils.isEmpty(path_licence)) {
                        totalPic++;
                        uploadImage(BitmapUtil.urlToBase64(WriteInNewActivity.this, path_licence), Constants.LICENCE);
                      }
                      if (!TextUtils.isEmpty(path_grid)) {
                        totalPic++;
                        uploadImage(BitmapUtil.urlToBase64(WriteInNewActivity.this, path_grid), Constants.GRID);
                      }

                    } else if (tvSubmit.getText().equals("下一步")) {
                      PostReSubmit postReSubmit = new PostReSubmit();
                      postReSubmit.setContact(mName);
                      postReSubmit.setTel(mNumb);
                      postReSubmit.setCardId(mId);
                      postReSubmit.setAddress(etIdAddress.getText().toString());
                      postReSubmit.setWorkAddress(
                          tvProvince.getText().toString() + etAddress.getText().toString());

                      if (poiInfo != null) {
                        postReSubmit.setProvinceCode(poiInfo.getProvince());
                        postReSubmit.setCityCode(poiInfo.getCity());
                        postReSubmit.setDistrictCode(poiInfo.getArea());
                      }
                      postReSubmit.setPhoto1(path_front);
                      postReSubmit.setPhoto2(path_backend);
                      postReSubmit.setPhoto3(path_handle);
                      postReSubmit.setLicensePic(path_licence);
                      postReSubmit.setBranchPic(path_grid);
                      postReSubmit.setBranchName(gridNameEt.getText().toString());
                      postReSubmit.setEmail(mEmail);

                      Intent intent =
                          new Intent(WriteInNewActivity.this, FaceRecordingActivity.class);
                      Bundle bundle = new Bundle();
                      bundle.putSerializable("postReSubmit", postReSubmit);
                      intent.putExtras(bundle);
                      intent.putExtra("from", "6");
                      startActivity(intent);
                    }
                  }
                }), httpPost);
          }
        }

        break;
      case R.id.province_layout:
      case R.id.user_provience:
        showChooseAreaDialog_new();
        break;
      case R.id.tvGetCode:
        String strEtPhone = etNumb.getText().toString();
        if (strEtPhone.length() != 11) {
          Util.createToast(WriteInNewActivity.this, "请输入正确的手机号码!");
        } else {
          dialog.getTvTitle().setText("正在获取验证码");
          dialog.show();
          CountDownTimerUtils mCountDownTimerUtils =
              new CountDownTimerUtils(tvGetCode, 60000, 1000);
          mCountDownTimerUtils.start();
          HttpPost<PostCode> httpPost = new HttpPost<PostCode>();
          PostCode postCode = new PostCode();
          postCode.setSession_token(Util.getLocalAdmin(WriteInNewActivity.this)[0]);
          postCode.setTel(strEtPhone);
          postCode.setCaptcha_type("5");
          httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
          httpPost.setApp_sign(
              Util.encode(BaseCom.APP_PWD + gson.toJson(postCode) + BaseCom.APP_PWD));
          httpPost.setParameter(postCode);
          Log.d("aaa", gson.toJson(httpPost));
          new AdminHttp().getCode(AdminRequest.getCode(WriteInNewActivity.this, dialog), httpPost);
        }
        break;
      case R.id.imgFront:
        clickView = imgFront;
        if (path_front.equals("")) {
          requestPermission();
        } else {
          int[] location = new int[2];
          imgFront.getLocationOnScreen(location);
          Util.turnToPhotoDetailActivity(WriteInNewActivity.this, path_front,
              DisplayUtil.dp2px(WriteInNewActivity.this, 60),
              DisplayUtil.dp2px(WriteInNewActivity.this, 60), location[0], location[1]);
        }
        break;
      case R.id.imgBackend:
        clickView = imgBackend;
        if (path_backend.equals("")) {
          requestPermission();
        } else {
          int[] location = new int[2];
          imgBackend.getLocationOnScreen(location);
          Util.turnToPhotoDetailActivity(WriteInNewActivity.this, path_backend,
              DisplayUtil.dp2px(WriteInNewActivity.this, 60),
              DisplayUtil.dp2px(WriteInNewActivity.this, 60), location[0], location[1]);
        }
        break;
      case R.id.imgHandle:
        clickView = imgHandle;
        if (path_handle.equals("")) {
          requestPermission();
        } else {
          int[] location = new int[2];
          imgHandle.getLocationOnScreen(location);
          Util.turnToPhotoDetailActivity(WriteInNewActivity.this, path_handle,
              DisplayUtil.dp2px(WriteInNewActivity.this, 60),
              DisplayUtil.dp2px(WriteInNewActivity.this, 60), location[0], location[1]);
        }
        break;
      case R.id.imgFrontRemove:
        clickView = imgFrontRemove;
        showPic("");
        break;
      case R.id.imgBackendRemove:
        clickView = imgBackendRemove;
        showPic("");
        break;
      case R.id.imgHandleRemove:
        clickView = imgHandleRemove;
        showPic("");
        break;
      case R.id.imgLicence:
        clickView = imgLicence;
        if (path_licence.equals("")) {
          requestPermission();
        } else {
          int[] location = new int[2];
          imgLicence.getLocationOnScreen(location);
          Util.turnToPhotoDetailActivity(WriteInNewActivity.this, path_licence,
              DisplayUtil.dp2px(WriteInNewActivity.this, 60),
              DisplayUtil.dp2px(WriteInNewActivity.this, 60), location[0], location[1]);
        }
        break;
      case R.id.imgLicenceRemove:
        clickView = imgLicenceRemove;
        showPic("");
        break;
      case R.id.imgGrid:
        clickView = imgGrid;
        if (path_grid.equals("")) {
          requestPermission();
        } else {
          int[] location = new int[2];
          imgLicence.getLocationOnScreen(location);
          Util.turnToPhotoDetailActivity(WriteInNewActivity.this, path_grid,
              DisplayUtil.dp2px(WriteInNewActivity.this, 60),
              DisplayUtil.dp2px(WriteInNewActivity.this, 60), location[0], location[1]);
        }
        break;
      case R.id.imgGridRemove:
        clickView = imgGridRemove;
        showPic("");
        break;
    }
  }

  Dialog warnDialog;

  private void initWarnDialog() {
    if (warnDialog == null) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      View view = LayoutInflater.from(this).inflate(R.layout.dialog_realname_tip, null, false);
      TextView readTv = view.findViewById(R.id.readTv);
      Observable.timer(5, TimeUnit.SECONDS)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(o -> {
            readTv.setEnabled(true);
            readTv.setTextColor(Color.parseColor("#EC6C00"));
          });
      readTv.setOnClickListener(v ->  warnDialog.dismiss());
      warnDialog = builder.setView(view)
          .setCancelable(false)
          .create();
    }
    warnDialog.show();
  }



  private void uploadImage(String bitmap, String key) {
    if (bitmap == null) return;
    HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
    PostPictureUpload postPictureUpload = new PostPictureUpload();
    postPictureUpload.setSession_token(Util.getLocalAdmin(WriteInNewActivity.this)[0]);
    postPictureUpload.setType("0");
    Photo photo = new Photo();
    photo.setPhoto1(bitmap);
    httpPost.setPhoto(photo);
    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
    httpPost.setParameter(postPictureUpload);
    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
    new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(WriteInNewActivity.this, dialog, value -> {
      uploadSuccessCount++;
      imgMap.put(key, value.getPhoto1());
      if (uploadSuccessCount == totalPic) {
        writeIn();
      }
    }), httpPost);
  }


  private void writeIn() {
    String mName = etName.getText().toString();
    String mNumb = etNumb.getText().toString();
    String mId = etId.getText().toString();
    String mEmail = etEmail.getText().toString();
    String mProvince = tvProvince.getText().toString();
    String mAddress = etAddress.getText().toString();
    HttpPost<PostReSubmit> httpPost = new HttpPost<>();
    PostReSubmit postReSubmit = new PostReSubmit();
    postReSubmit.setContact(mName);
    postReSubmit.setTel(mNumb);
    postReSubmit.setCardId(mId);
    postReSubmit.setAddress(etIdAddress.getText().toString());
    postReSubmit.setWorkAddress(
        tvProvince.getText().toString() + etAddress.getText()
            .toString());
    if (poiInfo != null) {
      postReSubmit.setProvinceCode(poiInfo.getProvince());
      postReSubmit.setCityCode(poiInfo.getCity());
      postReSubmit.setDistrictCode(poiInfo.area);
    }
    postReSubmit.setPhoto1(imgMap.get(Constants.PHOTOONE));
    postReSubmit.setPhoto2(imgMap.get(Constants.PHOTOTWO));
    postReSubmit.setPhoto3(imgMap.get(Constants.PHOTOTHREE));
    postReSubmit.setLicensePic(imgMap.get(Constants.LICENCE));
    postReSubmit.setBranchPic(imgMap.get(Constants.GRID));
    postReSubmit.setBranchName(gridNameEt.getText().toString());
    postReSubmit.setEmail(mEmail);
    //postReSubmit.setProvinceCode(area.getList().get(provinceIndex).getP_id());
    //postReSubmit.setCityCode(area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id());
    postReSubmit.setSession_token(
        Util.getLocalAdmin(WriteInNewActivity.this)[0]);
    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
    httpPost.setParameter(postReSubmit);
    httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD
        + gson.toJson(postReSubmit)
        + BaseCom.APP_PWD));
    Log.d("aaa", gson.toJson(httpPost));
    new CardHttp().writeIn(new Subscriber<HttpRequest<String>>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onNext(HttpRequest<String> stringHttpRequest) {
        //                                                    Util.gotoActy(WriteInActivity.this, LoginActivity.class);
        String toast = "";
        if (stringHttpRequest.getMes() != null
            && !stringHttpRequest.getMes().equals("")) {
          toast = stringHttpRequest.getMes();
        } else if (stringHttpRequest.getMsg() != null
            && !stringHttpRequest.getMsg().equals("")) {
          toast = stringHttpRequest.getMsg();
        }
        Util.createToast(WriteInNewActivity.this, toast);

        if (stringHttpRequest.getCode() == BaseCom.NORMAL) {
          AppManager.getAppManager().finishActivity();
        } else if (stringHttpRequest.getCode() == BaseCom.LOSELOG
            || stringHttpRequest.getCode()
            == BaseCom.VERSIONINCORRENT) {
          Util.createToast(WriteInNewActivity.this,
              stringHttpRequest.getMsg());
        }
        ////                                                        Util.gotoActy(WriteInActivity.this, LoginActivity.class);
      }
    }, httpPost);
  }


  //    Uri imageUri;

  public void showCamera(int requestCode) {
    CardOcrRecogConfigure.getInstance()
        .initLanguage(getApplicationContext())
        .setSaveCut(true)
        .setOpenIDCopyFuction(true)
        .setnMainId(
            kernal.idcard.camera.SharedPreferencesHelper.getInt(getApplicationContext(), "nMainId",
                2))
        .setnSubID(
            kernal.idcard.camera.SharedPreferencesHelper.getInt(getApplicationContext(), "nSubID",
                0))
        .setFlag(0)
        .setnCropType(0)
        .setSavePath(new DefaultPicSavePath(this, true));
    Intent intent = new Intent(this, CardsCameraActivity.class);
    startActivityForResult(intent, requestCode);
  }

  public void showCamera(Activity activity, File file, int requestCode) {
    BaseCom.camera = 1;

    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Uri uri;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      String authority =
          "com.test.tworldapplication.fileprovider"; //【清单文件中provider的authorities属性的值】
      uri = FileProvider.getUriForFile(activity, authority, file);
    } else {
      uri = Uri.fromFile(file);
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    activity.startActivityForResult(intent, requestCode);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == CAMERA) {
      BaseCom.camera = 0;
      if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED
          && grantResults[2] == PackageManager.PERMISSION_GRANTED
          && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
        choosePic();
      } else {
        Toast.makeText(WriteInNewActivity.this, "请赋予权限", Toast.LENGTH_SHORT).show();
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
      //
      //            try {
      //                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
      //                ((ImageView) clickView).setImageBitmap(bitmap);
      //                //将图片解析成Bitmap对象，并把它显现出来
      //            } catch (FileNotFoundException e) {
      //                e.printStackTrace();
      //            }
      if (tempFile == null) return;
      String path = tempFile.getAbsolutePath();
      showPic(path);
    } else if (requestCode == 111) {
      if (resultCode == Activity.RESULT_OK) {
        Bundle bundle = data.getBundleExtra("resultbundle");
        String path = "";
        if (bundle != null) {
          ResultMessage resultMessage = (ResultMessage) bundle.getSerializable("resultMessage");

          String[] picPath = bundle.getStringArray("picpath");
          //数据的封装
          String result = ManageIDCardRecogResult.managerSucessRecogResult(resultMessage,
              getApplicationContext());

          spit(result);
          if (splite_Result == null || splite_Result.length < 7) {
            ToastUtils.s(this, "证件识别错误");
            return;
          }
          path_front = picPath[0];
          selectPath = picPath[0];
          if (!(requestLogin != null && !TextUtils.isEmpty(requestLogin.getCardId()))) {
            etName.setText(splite_Result[1].substring(3));
            etIdAddress.setText(splite_Result[5].substring(3));
            etId.setText(splite_Result[6].substring(7));
          }

          imgFrontRemove.setVisibility(View.VISIBLE);
          selectPath = path_front;
          Picasso.with(this).invalidate(new File(selectPath));
          Picasso.with(this).load(new File(path_front)).into(imgFront);
        } else {
          String error = data.getStringExtra("error");
          ToastUtils.s(this, error);
        }
      }
    } else if (requestCode == 99) {
      if (resultCode == Activity.RESULT_OK) {
        poiInfo = data.getParcelableExtra("data");
        formatPoiInfo();
        if (poiInfo != null) {
          String province = TextUtils.isEmpty(poiInfo.getProvince()) ? "" : poiInfo.getProvince();
          String city = TextUtils.isEmpty(poiInfo.getCity()) ? "" : poiInfo.getCity();
          String pro_city = "";
          if (province.equals(city)) {
            pro_city = province;
          } else {
            pro_city = province + city;
          }
          tvProvince.setText(pro_city + (TextUtils.isEmpty(poiInfo.area) ? "" : poiInfo.area));
          etAddress.setText(poiInfo.getAddress() + poiInfo.getName() );
        }

      }
    }
  }

  private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback =
      new GalleryFinal.OnHanlderResultCallback() {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
          Log.i("test", "onHanlderSuccess: " + resultList.toString());
          if (resultList != null && resultList.size() > 0) {
            String path = resultList.get(0).getPhotoPath();
            showPic(path);
          }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
      };

  private void showPic(String capturePath) {
    if (!capturePath.equals("")) {
      //            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(getResources(), capturePath, DisplayUtil.dp2px(WriteInActivity.this, 200), DisplayUtil.dp2px(WriteInActivity.this, 200));
      //            File file = new File(capturePath);
      //            try {
      //                FileOutputStream fos = new FileOutputStream(file);
      //                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
      //                fos.flush();
      //                fos.close();
      //            } catch (Exception e) {
      //                e.printStackTrace();
      //            }

      Picasso.with(this).invalidate(new File(capturePath));
      Picasso.with(this).load(new File(capturePath)).into((ImageView) clickView);
      switch (clickView.getId()) {
        case R.id.imgFront:
          path_front = capturePath;
          imgFrontRemove.setVisibility(View.VISIBLE);
          Message message = new Message();
          selectPath = path_front;
          message.what = STARTSCAN;
          mHandler.sendMessageDelayed(message, 500);
          break;
        case R.id.imgBackend:
          path_backend = capturePath;
          imgBackendRemove.setVisibility(View.VISIBLE);
          break;
        case R.id.imgHandle:
          path_handle = capturePath;
          imgHandleRemove.setVisibility(View.VISIBLE);
          break;
        case R.id.imgLicence:
          path_licence = capturePath;
          imgLicenceRemove.setVisibility(View.VISIBLE);
          break;
        case R.id.imgGrid:
          path_grid = capturePath;
          imgGridRemove.setVisibility(View.VISIBLE);
          break;

      }
    } else {
      switch (clickView.getId()) {
        case R.id.imgFrontRemove:
          path_front = "";
          Picasso.with(this).load(R.drawable.front).into(imgFront);
          imgFrontRemove.setVisibility(View.GONE);
          if (!(requestLogin != null && !TextUtils.isEmpty(requestLogin.getCardId()))) {
            etName.setText("");
            etId.setText("");
            etIdAddress.setText("");
          }
          break;
        case R.id.imgBackendRemove:
          path_backend = "";
          Picasso.with(this).load(R.drawable.backend).into(imgBackend);
          imgBackendRemove.setVisibility(View.GONE);
          break;
        case R.id.imgHandleRemove:
          path_handle = "";
          Picasso.with(this).load(R.drawable.handle).into(imgHandle);
          imgHandleRemove.setVisibility(View.GONE);
          break;
        case R.id.imgLicenceRemove:
          path_licence = "";
          Picasso.with(this).load(R.mipmap.license).into(imgLicence);
          imgLicenceRemove.setVisibility(View.GONE);
          break;
        case R.id.imgGridRemove:
          path_grid = "";
          Picasso.with(this).load(R.mipmap.icon_grid).into(imgGrid);
          imgGridRemove.setVisibility(View.GONE);
          break;
      }
    }
  }

  private void choosePic() {
    switch (clickView.getId()) {
      case R.id.imgFront:
        DialogManager.changeAvatar(WriteInNewActivity.this, new SuccessNull() {
          @Override
          public void onSuccess() {
            //                        File fileDir = Environment.getExternalStorageDirectory();
            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir()
                : Environment.getExternalStorageDirectory();
            fileDir = new File(fileDir, "bqt");
            if (!fileDir.exists()) fileDir.mkdirs();
            tempFile = new File(fileDir, "temp_front.jpg");
            showCamera(111);
          }
        }, mOnHanlderResultCallback);
        break;
      case R.id.imgBackend:
        DialogManager.changeAvatar(WriteInNewActivity.this, new SuccessNull() {
          @Override
          public void onSuccess() {
            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir()
                : Environment.getExternalStorageDirectory();
            fileDir = new File(fileDir, "bqt");
            if (!fileDir.exists()) fileDir.mkdirs();
            tempFile = new File(fileDir, "temp_back.jpg");
            showCamera(WriteInNewActivity.this, tempFile, REQUEST_CODE_CAMERA);
          }
        }, mOnHanlderResultCallback);
        break;
      case R.id.imgHandle:
        if (!Util.isFastDoubleClick()) {

          DialogManager.changeAvatar(WriteInNewActivity.this, new SuccessNull() {
            @Override
            public void onSuccess() {
              File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir()
                  : Environment.getExternalStorageDirectory();
              fileDir = new File(fileDir, "bqt");
              if (!fileDir.exists()) fileDir.mkdirs();
              tempFile = new File(fileDir, "temp_last.jpg");
              showCamera(WriteInNewActivity.this, tempFile, REQUEST_CODE_CAMERA);
            }
          }, mOnHanlderResultCallback);
        }

        break;
      case R.id.imgLicence:
        DialogManager.changeAvatar(WriteInNewActivity.this, new SuccessNull() {
          @Override
          public void onSuccess() {
            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir()
                : Environment.getExternalStorageDirectory();
            fileDir = new File(fileDir, "bqt");
            if (!fileDir.exists()) fileDir.mkdirs();
            tempFile = new File(fileDir, "temp_licence.jpg");
            showCamera(WriteInNewActivity.this, tempFile, REQUEST_CODE_CAMERA);
          }
        }, mOnHanlderResultCallback);
        break;
      case R.id.imgGrid:
        DialogManager.changeAvatar(WriteInNewActivity.this, new SuccessNull() {
          @Override
          public void onSuccess() {
            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir()
                : Environment.getExternalStorageDirectory();
            fileDir = new File(fileDir, "bqt");
            if (!fileDir.exists()) fileDir.mkdirs();
            tempFile = new File(fileDir, "temp_grid.jpg");
            showCamera(WriteInNewActivity.this, tempFile, REQUEST_CODE_CAMERA);
          }
        }, mOnHanlderResultCallback);
        break;
    }
  }

  private void requestPermission() {

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED) {
      /*所有权限都被允许   初始化界面*/
      choosePic();
    } else {
      /*申请权限*/
      ActivityCompat.requestPermissions(this, new String[] {
          Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
      }, CAMERA);
    }
  }

  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_LOAD_DATA:
          if (thread == null) {//如果已创建就不再重新创建子线程了

            //            Toast.makeText(JsonDataActivity.this,"Begin Parse Data",Toast.LENGTH_SHORT).show();
            thread = new Thread(new Runnable() {
              @Override
              public void run() {
                // 写子线程中的操作,解析省市区数据
                initJsonData();
              }
            });
            thread.start();
          }
          break;

        case MSG_LOAD_SUCCESS:
          //          Toast.makeText(JsonDataActivity.this,"Parse Succeed",Toast.LENGTH_SHORT).show();
          isLoaded = true;
          break;

        case MSG_LOAD_FAILED:
          //          Toast.makeText(JsonDataActivity.this,"Parse Failed",Toast.LENGTH_SHORT).show();
          break;
        case STARTSCAN:
            dialog.getTvTitle().setText("正在扫描,请稍后");
            dialog.show();
            RecogService.nMainID =
                SharedPreferencesHelper.getInt(getApplicationContext(), "nMainId", 2);
            RecogService.isRecogByPath = true;
            Intent recogIntent = new Intent(WriteInNewActivity.this, RecogService.class);
            bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
      }
    }
  };

  public RecogService.recogBinder recogBindViewer;
  private String recogResultString = "";
  private String[] splite_Result;
  private String result = "";

  public ServiceConnection recogConn = new ServiceConnection() {

    public void onServiceDisconnected(ComponentName name) {
      recogBindViewer = null;
    }

    public void onServiceConnected(ComponentName name, IBinder service) {

      recogBindViewer = (RecogService.recogBinder) service;

      RecogParameterMessage rpm = new RecogParameterMessage();
      rpm.nTypeLoadImageToMemory = 0;
      rpm.nMainID = SharedPreferencesHelper.getInt(getApplicationContext(), "nMainId", 2);
      rpm.nSubID = null;
      rpm.GetSubID = true;
      rpm.GetVersionInfo = true;
      rpm.logo = "";
      rpm.userdata = "";
      rpm.sn = "";
      rpm.authfile = "";
      rpm.isCut = true;
      rpm.triggertype = 0;
      rpm.devcode = Devcode.devcode;
      // nProcessType：0-取消所有操作，1－裁切，
      // 2－旋转，3－裁切+旋转，4－倾斜校正，5－裁切+倾斜校正，6－旋转+倾斜校正，7－裁切+旋转+倾斜校正
      rpm.nProcessType = 7;
      rpm.nSetType = 1;// nSetType: 0－取消操作，1－设置操作
      rpm.lpFileName = selectPath; // rpm.lpFileName当为空时，会执行自动识别函数
      // rpm.lpHeadFileName = selectPath;//保存证件头像
      rpm.isSaveCut = false;// 保存裁切图片 false=不保存 true=保存
      if (SharedPreferencesHelper.getInt(getApplicationContext(), "nMainId", 2) == 2) {
        rpm.isAutoClassify = true;
        rpm.isOnlyClassIDCard = true;
      } else if (SharedPreferencesHelper.getInt(getApplicationContext(), "nMainId", 2) == 3000) {
        rpm.nMainID = 1034;
      }
      // end
      try {
        ResultMessage resultMessage;
        resultMessage = recogBindViewer.getRecogResult(rpm);
        if (resultMessage.ReturnAuthority == 0
            && resultMessage.ReturnInitIDCard == 0
            && resultMessage.ReturnLoadImageToMemory == 0
            && resultMessage.ReturnRecogIDCard > 0) {
          String iDResultString = "";
          String[] GetFieldName = resultMessage.GetFieldName;
          String[] GetRecogResult = resultMessage.GetRecogResult;
          map.clear();
          for (int i = 1; i < GetFieldName.length; i++) {
            if (GetRecogResult[i] != null) {
              map.put(GetFieldName[i], GetRecogResult[i]);
            }
          }
          if (!(requestLogin != null && !TextUtils.isEmpty(requestLogin.getCardId()))) {
            etName.setText(map.get("姓名"));
            etIdAddress.setText(map.get("住址"));
            etId.setText(map.get("公民身份号码"));
          }
          recogResultString = "";
          dialog.dismiss();
        } else {
          dialog.dismiss();
          String string = "";
          if (resultMessage.ReturnAuthority == -100000) {
            string = getString(R.string.exception) + resultMessage.ReturnAuthority;
          } else if (resultMessage.ReturnAuthority != 0) {
            string = getString(R.string.exception1) + resultMessage.ReturnAuthority;
          } else if (resultMessage.ReturnInitIDCard != 0) {
            string = getString(R.string.exception2) + resultMessage.ReturnInitIDCard;
          } else if (resultMessage.ReturnLoadImageToMemory != 0) {
            if (resultMessage.ReturnLoadImageToMemory == 3) {
              string = getString(R.string.exception3) + resultMessage.ReturnLoadImageToMemory;
            } else if (resultMessage.ReturnLoadImageToMemory == 1) {
              string = getString(R.string.exception4) + resultMessage.ReturnLoadImageToMemory;
            } else {
              string = getString(R.string.exception5) + resultMessage.ReturnLoadImageToMemory;
            }
          } else if (resultMessage.ReturnRecogIDCard <= 0) {
            if (resultMessage.ReturnRecogIDCard == -6) {
              string = getString(R.string.exception9);
            } else {
              string = getString(R.string.exception6) + resultMessage.ReturnRecogIDCard;
            }
          }
          Util.createToast(WriteInNewActivity.this, string);
        }
      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(getApplicationContext(), getString(R.string.recognized_failed),
            Toast.LENGTH_SHORT).show();
      } finally {
        if (recogBindViewer != null) {
          unbindService(recogConn);
        }
      }
    }
  };

  public void spit(String str) {
    Log.i("result", str);
    if (str.contains(",")) {
      splite_Result = str.split(",");
    } else {
      splite_Result = str.split("#");
    }
    for (int i = 0; i < splite_Result.length; i++) {
      if (result.equals("")) {
        result = splite_Result[i] + "\n";
      } else {
        result = result + splite_Result[i] + "\n";
      }
      Log.d("ccc", splite_Result[i]);
    }
  }

  private void showChooseAreaDialog_new() {
    Intent intent = new Intent(this, SelectAddressActivity.class);
    startActivityForResult(intent, 99);
    //View view = LayoutInflater.from(this).inflate(R.layout.dialog_choose_area, null);
    //alertDialogialog = new AlertDialog.Builder(this, R.style.ActionSheetDialogStyle).create();
    //alertDialogialog.getWindow().setGravity(Gravity.BOTTOM);
    //alertDialogialog.show();
    //asureTextView = (TextView) view.findViewById(R.id.asure_textview);
    //cancelTextView = (TextView) view.findViewById(R.id.cancel_textview);
    //chooseAreaTextView = (TextView) view.findViewById(R.id.choose_area_textview);
    //
    //provinceWheelView = (WheelView) view.findViewById(R.id.province_wheelview);
    //cityWheelView = (WheelView) view.findViewById(R.id.city_wheelview);
    //countyWheelView = (WheelView) view.findViewById(R.id.county_wheelview);
    //provinceWheelView.setDivideLineColor(getResources().getColor(R.color.colorBlue));
    //provinceWheelView.setTextFocusColor(getResources().getColor(R.color.colorGray));
    //provinceWheelView.setTextOutsideColor(getResources().getColor(R.color.colorGray6));
    //provinceWheelView.setTextSize(14);
    ////        provinceWheelView.setFontFace(DisplayUtil.getFontFace());
    //provinceWheelView.setOffset(4);
    //Log.d("bbb", "0");
    //provinceWheelView.setTextPadding(0, DisplayUtil.dp2px(this, 5), 0, DisplayUtil.dp2px(this, 5));
    //provinceWheelView.setItems(BaseUtils.provinceStrList);
    //provinceWheelView.setSeletion(2);
    //provinceIndex = 2;
    //List<String> provinceStrList = BaseUtils.provinceStrList;
    //currentProvince = provinceStrList.get(2);
    //provinceWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
    //  @Override
    //  public void onSelected(int selectedIndex, String item) {
    //    provinceIndex = selectedIndex - 4;
    //    currentProvince = item;
    //    List<String> c_list = new ArrayList<String>();
    //    for (int i = 0; i < area.getList().get(provinceIndex).getP_list().size(); i++) {
    //      c_list.add(area.getList().get(provinceIndex).getP_list().get(i).getC_name());
    //    }
    //    cityWheelView.refresh(c_list);
    //    cityWheelView.setSeletion(0);
    //    cityIndex = 0;
    //    currentCity = c_list.get(0);
    //  }
    //});
    //cityWheelView.setDivideLineColor(getResources().getColor(R.color.colorBlue));
    //cityWheelView.setTextFocusColor(getResources().getColor(R.color.colorGray));
    //cityWheelView.setTextOutsideColor(getResources().getColor(R.color.colorGray6));
    //cityWheelView.setTextSize(14);
    ////        cityWheelView.setFontFace(DisplayUtil.getFontFace());
    //cityWheelView.setOffset(4);
    //cityWheelView.setTextPadding(0, DisplayUtil.dp2px(this, 5), 0, DisplayUtil.dp2px(this, 5));
    //cityWheelView.setItems(BaseUtils.cityStrList);
    //cityIndex = 0;
    //cityWheelView.setSeletion(0);
    //currentCity = BaseUtils.cityStrList.get(0);
    //cityWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
    //  @Override
    //  public void onSelected(int selectedIndex, String item) {
    //    cityIndex = selectedIndex - 4;
    //    currentCity = item;
    //  }
    //});
    //
    //asureTextView.setOnClickListener(new View.OnClickListener() {
    //  @Override
    //  public void onClick(View v) {
    //    dismissChosseAreaDialog();
    //    String address = "";
    //    if (currentProvince.equals("北京") || currentProvince.equals("天津") || currentProvince.equals(
    //        "澳门") || currentProvince.equals("上海") || currentProvince.equals("重庆")) {
    //      address = currentCity;
    //    } else {
    //      address = currentProvince + currentCity;
    //    }
    //    tvProvince.setText(address);
    //    Log.d("aaa", currentProvince);
    //    Log.d("aaa", provinceIndex + "");
    //    Log.d("aaa", currentCity);
    //    Log.d("aaa", cityIndex + "");
    //    Log.d("aaa", area.getList().get(provinceIndex).getP_id() + "");
    //    Log.d("aaa", area.getList().get(provinceIndex).getP_list().get(cityIndex).getC_id() + "");
    //  }
    //});
    //cancelTextView.setOnClickListener(new View.OnClickListener() {
    //  @Override
    //  public void onClick(View v) {
    //    dismissChosseAreaDialog();
    //  }
    //});
    //
    //alertDialogialog.setCancelable(false);
    //alertDialogialog.setCanceledOnTouchOutside(false);
    //alertDialogialog.getWindow()
    //    .setLayout(DisplayUtil.getWindowWidth(this), WindowManager.LayoutParams.WRAP_CONTENT);
    //alertDialogialog.setContentView(view);
  }

  private void dismissChosseAreaDialog() {
    if (alertDialogialog != null && alertDialogialog.isShowing()) {
      alertDialogialog.dismiss();
    }
  }

  private void ShowPickerView() {// 弹出选择器

    OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
      @Override
      public void onOptionsSelect(int options1, int options2, int options3, View v) {
        //返回的分别是三个级别的选中位置
        String tx = options1Items.get(options1).getPickerViewText() + options2Items.get(options1)
            .get(options2);

        //        Toast.makeText(getActivity(), tx, Toast.LENGTH_SHORT).show();
        tvProvince.setText(tx);
        //        presenter.chage(RegisterPresenter.TYPE_AREA, tx);

      }
    }).setTitleText("城市选择").setDividerColor(Color.BLACK).setTextColorCenter(Color.BLACK) //设置选中项文字颜色
        .setContentTextSize(20).build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
    //        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
    pvOptions.setPicker(options1Items, options2Items);//二级选择器
    pvOptions.show();
  }

  private void initJsonData() {//解析数据

    /**
     * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
     * 关键逻辑在于循环体
     *
     * */
    String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

    ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

    /**
     * 添加省份数据
     *
     * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
     * PickerView会通过getPickerViewText方法获取字符串显示出来。
     */
    options1Items = jsonBean;

    for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
      ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
      ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

      for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
        String CityName = jsonBean.get(i).getCityList().get(c).getName();
        CityList.add(CityName);//添加城市

        ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
        if (jsonBean.get(i).getCityList().get(c).getArea() == null
            || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
          City_AreaList.add("");
        } else {

          for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size();
              d++) {//该城市对应地区所有数据
            String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

            City_AreaList.add(AreaName);//添加该城市所有地区数据
          }
        }
        Province_AreaList.add(City_AreaList);//添加该省所有地区数据
      }

      /**
       * 添加城市数据
       */
      options2Items.add(CityList);

      /**
       * 添加地区数据
       */
      options3Items.add(Province_AreaList);
    }

    mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
  }

  public ArrayList<JsonBean> parseData(String result) {//Gson 解析
    ArrayList<JsonBean> detail = new ArrayList<>();
    try {
      JSONArray data = new JSONArray(result);
      Gson gson = new Gson();
      for (int i = 0; i < data.length(); i++) {
        JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
        detail.add(entity);
      }
    } catch (Exception e) {
      e.printStackTrace();
      mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
    }
    return detail;
  }

  protected boolean checkPhone(String str) {
    String pattern = "0?[0-9]{11}";
    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(str);
    return m.matches();
  }

  public static boolean isEmail(String email) {
    String pattern = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(email);
    return m.matches();
  }

  public static boolean isIdcard(String idcard) {
    String pattern = "^\\d{15}$|^\\d{17}[0-9Xx]$";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(idcard);
    return m.matches();
  }
}
