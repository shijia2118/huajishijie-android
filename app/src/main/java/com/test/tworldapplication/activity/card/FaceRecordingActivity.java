 package com.test.tworldapplication.activity.card;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.WriteInActivity;
import com.test.tworldapplication.activity.admin.WriteInNewActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.activity.other.DialogActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.AgentsLiangNumber;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.Photo;
import com.test.tworldapplication.entity.PostPictureUpload;
import com.test.tworldapplication.entity.PostReSubmit;
import com.test.tworldapplication.entity.PostSetOpen;
import com.test.tworldapplication.entity.PostTransfer;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.entity.RequestSetOpen;
import com.test.tworldapplication.entity.RequestTransfer;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.http.RequestLiangPay;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.Constants;
import com.test.tworldapplication.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

//import com.test.tworldapplication.utils.MySurfaceView;

public class FaceRecordingActivity extends BaseActivity implements View.OnClickListener {

  private static final String TAG = "FaceRecordingActivity";

  //    private Executor executor = Executors.newFixedThreadPool(1);

  private ExecutorService executorService = Executors.newSingleThreadExecutor();

  //UI
  private ImageView mRecordControl, mRecordControl0, mRecordControl1, mRecordControl2,
      cameraSwitchIv;
  //    private ImageView mPauseRecord;
  private SurfaceView surfaceView;
  private SurfaceHolder mSurfaceHolder;
  private Chronometer mRecordTime;
  private boolean startRecord;

  private ImageView mResult;

  //DATA

  //录像机状态标识
  private int mRecorderState;

  public static final int STATE_INIT = 0;
  public static final int STATE_RECORDING = 1;
  public static final int STATE_PAUSE = 2;

  public boolean isCameraBack = true;

  //    private boolean isRecording;// 标记，判断当前是否正在录制
  //    private boolean isPause; //暂停标识
  private long mPauseTime = 0;           //录制暂停时间间隔

  // 存储文件
  private File mVecordFile;
  private Camera mCamera;
  private MediaRecorder mediaRecorder;
  private String currentVideoFilePath;
  private String saveVideoPath = "";
  private Handler handle;
  private Boolean isGetBuffer = false;

  String from = "";
  String type = "";
  ArrayList<String> urlList = new ArrayList<>();
  Package mPackage = null;
  Promotion mPromotion = null;
  RequestCheck requestCheck = null;
  RequestImsi requestImsi = null;
  PostReSubmit postReSubmit = null;
  String phone = "";
  String numOne = ""; //预留电话1
  String numTwo = ""; //预留电话2
  String numThree = ""; //预留电话3

  String iccid = "";
  String strName = "";
  String strId = "";
  String strAddress = "";
  String strRemark = "";

  String strBuyName = "";
  String strBuyId = "";
  String strBuyAddress = "";
  String getSimId = "";
  String getIccid = "";
  String getImsi = "";
  int getSelectPackageId;
  int getSelectPromotionId;
  String getPrestore = "";
  String strBuyRemark = "";
  String strGetNumber = "";
  String strOrderNo = "";

  int miss = 0;

  Bitmap photoOne = null;
  Bitmap photoTwo = null;
  Bitmap videoPicOne = null;
  Bitmap videoPicTwo = null;

  private String handleUri = "";

  Bitmap video_one, video_two, bitmap_zero, bitmap_two, bitmap_one;

  private Integer index = 0;
  private Integer index0 = 0;
  private Integer picIndex = 0;
  private Integer preindex = 0;
  private boolean allowAnalysis = false;

  private CountDownTimer mTimer;
  private long savetime = 10000L;
  private TextView timer, view0, view1, tip, timer0;
  private ImageView mShow;

  private static final int SUBMIT_AGAIN = 900;
  final int BUYRESULT = 50;
  AgentsLiangNumber agentsLiangNumber;
  RequestLiangPay requestLiangPay;
  String preStore, detail, number;
  private int cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;

  private MediaRecorder.OnErrorListener OnErrorListener = new MediaRecorder.OnErrorListener() {
    @Override
    public void onError(MediaRecorder mediaRecorder, int what, int extra) {
      try {
        if (mediaRecorder != null) {
          mediaRecorder.reset();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_face);

    surfaceView = (SurfaceView) findViewById(R.id.record_surfaceView);
    mRecordControl = (ImageView) findViewById(R.id.record_control);
    mRecordTime = (Chronometer) findViewById(R.id.record_time);
    view1 = (TextView) findViewById(R.id.view1);
    timer = (TextView) findViewById(R.id.timer);
    view0 = (TextView) findViewById(R.id.view0);
    tip = (TextView) findViewById(R.id.tip);
    timer0 = (TextView) findViewById(R.id.timer0);

    mShow = (ImageView) findViewById(R.id.showimg);

    mRecordControl0 = (ImageView) findViewById(R.id.record_control0);
    mRecordControl1 = (ImageView) findViewById(R.id.record_control1);
    mRecordControl2 = (ImageView) findViewById(R.id.record_control2);
    cameraSwitchIv = findViewById(R.id.cameraSwitchIv);
    cameraSwitchIv.setOnClickListener(this);
    mRecordControl.setOnClickListener(this);
    mRecordControl0.setOnClickListener(this);
    mRecordControl1.setOnClickListener(this);
    mRecordControl2.setOnClickListener(this);

    setBackGroundTitle("人脸识别", true);

    from = getIntent().getStringExtra("from");
    urlList = getIntent().getStringArrayListExtra("urlList");

    switch (from) {

      case "0":
        /*成卡*/
        mRecordControl.setVisibility(View.VISIBLE);
        mRecordControl0.setVisibility(View.GONE);
        mRecordControl1.setVisibility(View.GONE);
        mRecordControl2.setVisibility(View.GONE);

        strName = getIntent().getStringExtra("name");
        strId = getIntent().getStringExtra("cardId");
        strAddress = getIntent().getStringExtra("address");
        strRemark = getIntent().getStringExtra("remark");

        mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
        mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
        requestCheck = (RequestCheck) getIntent().getExtras().getSerializable("requestCheck");
        phone = getIntent().getStringExtra("phone");
        iccid = getIntent().getStringExtra("iccid");
        type = getIntent().getStringExtra("type");
        break;
      case "1":

        mRecordControl.setVisibility(View.VISIBLE);
        mRecordControl0.setVisibility(View.GONE);
        mRecordControl1.setVisibility(View.GONE);
        mRecordControl2.setVisibility(View.GONE);

        strName = getIntent().getStringExtra("name");
        strId = getIntent().getStringExtra("cardId");
        strAddress = getIntent().getStringExtra("address");
        strRemark = getIntent().getStringExtra("remark");

        mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
        mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
        requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
        Log.d("vvv", requestImsi.toString());
        break;

      case "2":
        mRecordControl.setVisibility(View.GONE);
        mRecordControl0.setVisibility(View.VISIBLE);
        mRecordControl1.setVisibility(View.GONE);
        mRecordControl2.setVisibility(View.GONE);

        strBuyName = getIntent().getStringExtra("strBuyName");
        strBuyId = getIntent().getStringExtra("strBuyId");
        strBuyAddress = getIntent().getStringExtra("strBuyAddress");
        strBuyRemark = getIntent().getStringExtra("strBuyRemark");

        getSimId = getIntent().getStringExtra("getSimId");
        getIccid = getIntent().getStringExtra("getIccid");
        getImsi = getIntent().getStringExtra("getImsi");
        getSelectPackageId = getIntent().getIntExtra("getSelectPackageId", -1);
        getSelectPromotionId = getIntent().getIntExtra("getSelectPromotionId", -1);
        getPrestore = getIntent().getStringExtra("getPrestore");
        strGetNumber = getIntent().getStringExtra("getNumber");
        strOrderNo = getIntent().getStringExtra("orderNo");

        break;

      case "3":
        mRecordControl.setVisibility(View.GONE);
        mRecordControl0.setVisibility(View.VISIBLE);
        mRecordControl1.setVisibility(View.GONE);
        mRecordControl2.setVisibility(View.GONE);

        strBuyName = getIntent().getStringExtra("strBuyName");
        strBuyId = getIntent().getStringExtra("strBuyId");
        strBuyAddress = getIntent().getStringExtra("strBuyAddress");
        strBuyRemark = getIntent().getStringExtra("strBuyRemark");

        getSimId = getIntent().getStringExtra("getSimId");
        getIccid = getIntent().getStringExtra("getIccid");
        getImsi = getIntent().getStringExtra("getImsi");
        getSelectPackageId = getIntent().getIntExtra("getSelectPackageId", -1);
        getSelectPromotionId = getIntent().getIntExtra("getSelectPromotionId", -1);
        getPrestore = getIntent().getStringExtra("getPrestore");
        strGetNumber = getIntent().getStringExtra("getNumber");
        strOrderNo = getIntent().getStringExtra("orderNo");

        break;
      case "5":
        mRecordControl.setVisibility(View.GONE);
        mRecordControl0.setVisibility(View.GONE);
        mRecordControl1.setVisibility(View.VISIBLE);
        mRecordControl2.setVisibility(View.GONE);

        strName = getIntent().getStringExtra("name");
        strId = getIntent().getStringExtra("cardId");
        strAddress = getIntent().getStringExtra("address");
        strRemark = getIntent().getStringExtra("remark");

        number = getIntent().getStringExtra("number");
        preStore = getIntent().getStringExtra("preStore");
        detail = getIntent().getStringExtra("detail");

        type = getIntent().getStringExtra("type");
        Log.d("ccc", "FaceGet:" + type);
        break;
      case "6":
        mRecordControl.setVisibility(View.GONE);
        mRecordControl0.setVisibility(View.GONE);
        mRecordControl1.setVisibility(View.GONE);
        mRecordControl2.setVisibility(View.VISIBLE);

        postReSubmit = (PostReSubmit) getIntent().getExtras().getSerializable("postReSubmit");
        break;
      case "7":
        /*过户*/
        mRecordControl.setVisibility(View.VISIBLE);
        mRecordControl0.setVisibility(View.GONE);
        mRecordControl1.setVisibility(View.GONE);
        mRecordControl2.setVisibility(View.GONE);

        strName = getIntent().getStringExtra("name"); //姓名
        strId = getIntent().getStringExtra("cardId"); //证件号码
        strAddress = getIntent().getStringExtra("address"); //证件地址
        number = getIntent().getStringExtra("number"); //过户号码
        phone = getIntent().getStringExtra("phone");//联系人电话
        numOne=getIntent().getStringExtra("numOne");//预留电话1
        numTwo=getIntent().getStringExtra("numTwo");//预留电话2
        numThree=getIntent().getStringExtra("numThree");//预留电话3


//        mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
//        mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
//        requestCheck = (RequestCheck) getIntent().getExtras().getSerializable("requestCheck");
//        iccid = getIntent().getStringExtra("iccid");
//        type = getIntent().getStringExtra("type");
        break;

    }

    initView();
  }

  private void initView() {

    SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
    int switchFlag = sharedPreferences.getInt(Constants.SHOOTSWITCH, 2);
    cameraSwitchIv.setVisibility(switchFlag == 2 ? View.GONE : View.VISIBLE);
    int shootMode = sharedPreferences.getInt(Constants.SHOOTMODES, 1);
    cameraFacing = shootMode == 1 ? Camera.CameraInfo.CAMERA_FACING_FRONT
        : Camera.CameraInfo.CAMERA_FACING_BACK;

    mSurfaceCallBack = new SurfaceHolder.Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initCamera(cameraFacing);
      }

      @Override
      public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        //            if (mSurfaceHolder.getSurface() == null) {
        //                return;
        //            }

        if (mCamera == null) {
          return;
        }
        Camera.Parameters parameters = mCamera.getParameters();

        // 设置拍摄照片格式
        parameters.setPictureFormat(PixelFormat.JPEG);
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);

        try {
          //通过startPreview方法告知可以在surface上进行绘制操作
          mCamera.startPreview();
        } catch (Exception e) {
          Log.e(TAG, "Start preview failed", e);
          mCamera.release();
          mCamera = null;
        }
      }

      @Override
      public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
          releaseCamera();
          mCamera = null;
        }
      }
    };

    //配置SurfaceHolder
    mSurfaceHolder = surfaceView.getHolder();
    // 设置Surface不需要维护自己的缓冲区
    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    // 设置分辨率
    mSurfaceHolder.setFixedSize(320, 280);
    // 设置该组件不会让屏幕自动关闭
    mSurfaceHolder.setKeepScreenOn(true);
    //回调接口
    mSurfaceHolder.addCallback(mSurfaceCallBack);
  }

  private SurfaceHolder.Callback mSurfaceCallBack;

  public Camera.Size getOptimalPreviewSize(int w, int h) {

    Camera.Parameters parameters = mCamera.getParameters();
    List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
    final double ASPECT_TOLERANCE = 0.1;
    double targetRatio = (double) w / h;
    if (sizes == null) return null;

    Camera.Size optimalSize = null;
    double minDiff = Double.MAX_VALUE;

    int targetHeight = h;

    // Try to find an size match aspect ratio and size
    for (Camera.Size size : sizes) {
      Log.i("Main", "width: " + size.width + "  height：" + size.height);
      double ratio = (double) size.width / size.height;
      if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
      if (Math.abs(size.height - targetHeight) < minDiff) {
        optimalSize = size;
        minDiff = Math.abs(size.height - targetHeight);
      }
    }

    // Cannot find the one match the aspect ratio, ignore the requirement
    if (optimalSize == null) {
      minDiff = Double.MAX_VALUE;
      for (Camera.Size size : sizes) {
        if (Math.abs(size.height - targetHeight) < minDiff) {
          optimalSize = size;
          minDiff = Math.abs(size.height - targetHeight);
        }
      }
    }
    return optimalSize;
  }

  /**
   * 初始化摄像头
   *
   * @throws IOException
   * @author liuzhongjun
   */
  private void initCamera(int cameraFacing) {

    if (mCamera != null) {
      releaseCamera();
    }

    //        mCamera = Camera.open();

    mCamera = Camera.open(cameraFacing);

    if (mCamera == null) {
      Toast.makeText(this, "未能获取到相机！", Toast.LENGTH_SHORT).show();
      return;
    }
    try {
      //将相机与SurfaceHolder绑定
      mCamera.setPreviewDisplay(mSurfaceHolder);
      //配置CameraParams
      configCameraParams();
      //启动相机预览
      mCamera.startPreview();
    } catch (IOException e) {
      //有的手机会因为兼容问题报错，这就需要开发者针对特定机型去做适配了
      Log.d(TAG, "Error starting camera preview: " + e.getMessage());
    }
    isGetBuffer = true;
  }

  /**
   * 设置摄像头为竖屏
   *
   * @author lip
   * @date 2015-3-16
   */
  private void configCameraParams() {
    Camera.Parameters params = mCamera.getParameters();
    //设置相机的横竖屏(竖屏需要旋转90°)
    if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
      params.set("orientation", "portrait");
      mCamera.setDisplayOrientation(90);
    } else {
      params.set("orientation", "landscape");
      mCamera.setDisplayOrientation(0);
    }
    //设置聚焦模式
    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
    //缩短Recording启动时间
    params.setRecordingHint(true);
    //影像稳定能力
    if (params.isVideoStabilizationSupported()) params.setVideoStabilization(true);
    //        mCamera.setParameters(params);
    Camera.Parameters parameters = mCamera.getParameters();
    mCamera.setDisplayOrientation(90);
    mCamera.setParameters(parameters);
  }

  /**
   * 释放摄像头资源
   *
   * @author liuzhongjun
   * @date 2016-2-5
   */
  private void releaseCamera() {
    if (mCamera != null) {
      mCamera.setPreviewCallback(null);
      mCamera.stopPreview();
      mCamera.release();
      mCamera = null;
    }
  }

  /**
   * 开始录制视频
   */
  public boolean startRecord() {

    initCamera(cameraFacing);
    //录制视频前必须先解锁Camera
    mCamera.unlock();
    configMediaRecorder();
    try {
      //开始录制
      mediaRecorder.prepare();
      mediaRecorder.start();
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  /**
   * 停止录制视频
   */
  public void stopRecord() {
    // 设置后不会崩
    mediaRecorder.setOnErrorListener(null);
    mediaRecorder.setPreviewDisplay(null);
    //停止录制
    mediaRecorder.stop();
    mediaRecorder.reset();
    //释放资源
    mediaRecorder.release();
    mediaRecorder = null;
  }

  //    Handler handler = new Handler() {
  //        @Override
  //        public void handleMessage(Message msg) {
  //            super.handleMessage(msg);
  //            switch (msg.what) {
  //                case SUBMIT_AGAIN:
  //
  //                    break;
  //            }
  //        }
  //    };

  /**
   * 合并录像视频方法
   */

  /**
   * 点击中间按钮，执行的UI更新操作
   */

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.cameraSwitchIv:
        if (startRecord) return;
        cameraFacing = cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT
            ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        initCamera(cameraFacing);
        break;
      case R.id.record_control:
        getPreViewImage();

        mRecordTime.setBase(SystemClock.elapsedRealtime());
        mRecordTime.start();
        startRecord = true;
        view0.setVisibility(View.GONE);
        timer.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        mRecordTime.setVisibility(View.GONE);
        mRecordControl.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        timer0.setVisibility(View.VISIBLE);

        //                Toast.makeText( FaceRecordingActivity.this,"请注视屏幕", Toast.LENGTH_SHORT).show();
        Util.createToast(FaceRecordingActivity.this, "请注视屏幕");

        mTimer = new CountDownTimer(savetime, 1000L) {
          @Override
          public void onTick(long millisUntilFinished) {
            Log.i("tiker",":"+millisUntilFinished);

            long seconds = millisUntilFinished % 60000;
            timer.setText("00:00:0" + Math.round((float) seconds / 1000));
            Log.i("timer",":"+timer.getText());

            if (seconds / 1000 == 4) {
              Log.i("seconds",":"+seconds);
              Util.createToast(FaceRecordingActivity.this, "请微微张口");
              //                            Toast.makeText( FaceRecordingActivity.this,"请微微张口", Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onFinish() {

            mRecordTime.stop();
            startRecord = false;

            /**
             *  过户
             */
            String from = getIntent().getStringExtra ("from");
           if(from.equals("7")){
             dialog.getTvTitle().setText("正在上传图片");
             dialog.show();
             HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
             PostPictureUpload postPictureUpload = new PostPictureUpload();
             postPictureUpload.setSession_token(Util.getLocalAdmin(FaceRecordingActivity.this)[0]);
             postPictureUpload.setType("2");
             Photo photo = new Photo();
             photo.setPhoto1(BitmapUtil.bitmapToBase64(video_one));
             photo.setPhoto2(BitmapUtil.bitmapToBase64(video_two));
             photo.setPhoto3(BitmapUtil.compressitmapToBase64(BaseCom.photoOne));
             photo.setPhoto4(BitmapUtil.compressitmapToBase64(BaseCom.photoTwo));
             photo.setPhoto5(BitmapUtil.compressitmapToBase64(BaseCom.photoThree));
             photo.setPhoto6(BitmapUtil.compressitmapToBase64(BaseCom.photoFour));
             httpPost.setPhoto(photo);
             httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
             httpPost.setParameter(postPictureUpload);
             httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
             new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(FaceRecordingActivity.this, dialog, new SuccessValue<RequestPictureUpload>() {
                    @Override
                    public void OnSuccess(RequestPictureUpload value) {
                      dialog.getTvTitle().setText("正在提交");
                        dialog.show();

                        HttpPost<PostTransfer> httpPost = new HttpPost<>();
                        PostTransfer postTransfer = new PostTransfer();
                        postTransfer.setSession_token(Util.getLocalAdmin(FaceRecordingActivity.this)[0]);
                        postTransfer.setNumber(number);//过户号码
                        postTransfer.setName(strName); //姓名
                        postTransfer.setCardId(strId); //证件号码
                        postTransfer.setPhotoOne(value.getPhoto3());//新用户照片名称
                        postTransfer.setPhotoTwo(value.getPhoto4());//老用户照片名称
                        postTransfer.setPhotoThree(value.getPhoto5());//手持用户照片名称
                        postTransfer.setPhotoFour(value.getPhoto6());//手持老用户照片名称
                        postTransfer.setMemo2(value.getPhoto1()); //视频截图1
                        postTransfer.setMemo3(value.getPhoto2()); //视频截图2
                        postTransfer.setTel(phone); //联系人电话
                        postTransfer.setAddress(strAddress);
                        postTransfer.setNumOne(numOne); //预留号码1
                        postTransfer.setNumTwo(numTwo);//预留号码2
                        postTransfer.setNumThree(numThree);//预留号码3
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postTransfer);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postTransfer) + BaseCom.APP_PWD));
                        new CardHttp().transfer(CardRequest.transfer(new SuccessValue<HttpRequest<RequestTransfer>>() {
                            @Override
                            public void OnSuccess(HttpRequest<RequestTransfer> value) {
                                Log.d("aaa", value.getMes());
                                Util.createToast(FaceRecordingActivity.this, value.getMes());
                                if (value.getCode() == BaseCom.NORMAL) {
                                  Intent intent = new Intent(FaceRecordingActivity.this, MainNewActivity.class);
                                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                  startActivity(intent);
                                } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT)
                                    Util.gotoActy(FaceRecordingActivity.this, LoginActivity.class);
                            }
                        }, dialog), httpPost);
                    }
                }), httpPost);
           }else{
             Log.i("xxxxxx","=>>>>>>>1");

             final HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
             PostPictureUpload postPictureUpload = new PostPictureUpload();
             postPictureUpload.setSession_token(Util.getLocalAdmin(FaceRecordingActivity.this)[0]);
             postPictureUpload.setType("1");
             Photo photo = new Photo();
             photo.setPhoto1(BitmapUtil.bitmapToBase64(video_one));
             photo.setPhoto2(BitmapUtil.bitmapToBase64(video_two));
             httpPost.setPhoto(photo);
             Intent intent = new Intent(FaceRecordingActivity.this, AccountClosingActivity.class);
            Bundle bundle = new Bundle();
            intent.putStringArrayListExtra("urlList", urlList);
            bundle.putSerializable("mPackage", mPackage);
            bundle.putSerializable("mPromotion", mPromotion);

            if (from.equals("0")) {
              bundle.putSerializable("requestCheck", requestCheck);
              intent.putExtras(bundle);
              intent.putExtra("from", "0");
              intent.putExtra("type", type);
            } else {
              intent.putExtra("phone", phone);
              intent.putExtra("iccid", iccid);
              intent.putExtra("from", "1");
              bundle.putSerializable("requestImsi", requestImsi);
              intent.putExtras(bundle);
            }
            intent.putExtra("name", strName);
            intent.putExtra("cardId", strId);
            intent.putExtra("address", strAddress);
            intent.putExtra("remark", strRemark);
            intent.putExtra("face", "1");
            BaseCom.videoOne = video_one;
            BaseCom.videoTwo = video_two;
            Log.d("aaa", "goact");
            startActivity(intent);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
           }
          }
        }.start();

        break;
      case R.id.record_control0:
        getPreViewImage();
Log.i("xxxxxx","=>>>>>>>2");
        mRecordTime.setBase(SystemClock.elapsedRealtime());
        mRecordTime.start();

        view1.setVisibility(View.GONE);
        view0.setVisibility(View.GONE);
        mRecordTime.setVisibility(View.GONE);
        timer.setVisibility(View.GONE);
        mRecordControl0.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        timer0.setVisibility(View.VISIBLE);

        Toast.makeText(FaceRecordingActivity.this, "请注视屏幕", Toast.LENGTH_SHORT).show();

        mTimer = new CountDownTimer(savetime, 1000L) {
          @Override
          public void onTick(long millisUntilFinished) {
            long seconds = millisUntilFinished % 60000;
            timer.setText("00:00:0" + Math.round((float) seconds / 1000));

            if (seconds / 1000 == 4) {
              Util.createToast(FaceRecordingActivity.this, "请微微张口");
              //                            Toast.makeText( FaceRecordingActivity.this,"请微微张口", Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onFinish() {

            mRecordTime.stop();
            submit();
          }
        }.start();

        break;
      case R.id.record_control1:
        getPreViewImage();

        mRecordTime.setBase(SystemClock.elapsedRealtime());
        mRecordTime.start();

        view0.setVisibility(View.GONE);
        timer.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        mRecordTime.setVisibility(View.GONE);
        mRecordControl1.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        timer0.setVisibility(View.VISIBLE);

        //                Toast.makeText( FaceRecordingActivity.this,"请注视屏幕", Toast.LENGTH_SHORT).show();
        Util.createToast(FaceRecordingActivity.this, "请注视屏幕");
        mTimer = new CountDownTimer(savetime, 1000L) {
          @Override
          public void onTick(long millisUntilFinished) {
            long seconds = millisUntilFinished % 60000;
            timer.setText("00:00:0" + Math.round((float) seconds / 1000));

            if (seconds / 1000 == 4) {
              Util.createToast(FaceRecordingActivity.this, "请微微张口");
              //                            Toast.makeText( FaceRecordingActivity.this,"请微微张口", Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onFinish() {

            mRecordTime.stop();

            final HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
            PostPictureUpload postPictureUpload = new PostPictureUpload();
            postPictureUpload.setSession_token(Util.getLocalAdmin(FaceRecordingActivity.this)[0]);
            postPictureUpload.setType("1");
            Photo photo = new Photo();
            photo.setPhoto1(BitmapUtil.bitmapToBase64(video_one));
            photo.setPhoto2(BitmapUtil.bitmapToBase64(video_two));
            httpPost.setPhoto(photo);

            Intent intent = new Intent(FaceRecordingActivity.this, AccountClosingActivity.class);
            Bundle bundle = new Bundle();
            intent.putStringArrayListExtra("urlList", urlList);
            bundle.putSerializable("mPackage", mPackage);
            bundle.putSerializable("mPromotion", mPromotion);
            if (from.equals("5")) {
              intent.putExtra("from", "5");
              intent.putExtra("number", number);
              intent.putExtra("preStore", preStore);
              intent.putExtra("detail", detail);
            }
            intent.putExtra("name", strName);
            intent.putExtra("cardId", strId);
            intent.putExtra("address", strAddress);
            intent.putExtra("remark", strRemark);
            intent.putExtra("face", "1");
            Log.d("ccc", "facePost:" + type);
            intent.putExtra("type", type);
            BaseCom.videoOne = video_one;
            BaseCom.videoTwo = video_two;
            Log.d("aaa", "goact");
            startActivity(intent);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
          }
        }.start();

        break;
      case R.id.record_control2:
        getPreViewImage();

        mRecordTime.setBase(SystemClock.elapsedRealtime());
        mRecordTime.start();

        view0.setVisibility(View.GONE);
        timer.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        mRecordTime.setVisibility(View.GONE);
        mRecordControl2.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        timer0.setVisibility(View.VISIBLE);

        //                Toast.makeText( FaceRecordingActivity.this,"请注视屏幕", Toast.LENGTH_SHORT).show();
        Util.createToast(FaceRecordingActivity.this, "请注视屏幕");
        mTimer = new CountDownTimer(savetime, 1000L) {
          @Override
          public void onTick(long millisUntilFinished) {
            long seconds = millisUntilFinished % 60000;
            timer.setText("00:00:0" + Math.round((float) seconds / 1000));

            if (seconds / 1000 == 4) {
              Util.createToast(FaceRecordingActivity.this, "请微微张口");
              //                            Toast.makeText( FaceRecordingActivity.this,"请微微张口", Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onFinish() {

            mRecordTime.stop();
            //                        Util.gotoActy(FaceRecordingActivity.this, LoginActivity.class);
            submit0();
          }
        }.start();

        break;
    }
  }

  public void deletePic(Context context, String path) {
    if (!TextUtils.isEmpty(path)) {
      File file = new File(path);
      if (file.exists() && file.isFile()) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();//cutPic.this是一个上下文
        String url = MediaStore.Images.Media.DATA + "='" + path + "'";
        //删除图片
        contentResolver.delete(uri, url, null);
      }
    }
  }

  /**
   * 配置MediaRecorder()
   */

  private void configMediaRecorder() {
    mediaRecorder = new MediaRecorder();
    mediaRecorder.reset();
    mediaRecorder.setCamera(mCamera);
    mediaRecorder.setOnErrorListener(OnErrorListener);

    //使用SurfaceView预览
    mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

    //1.设置采集声音
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    //设置采集图像
    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
    //2.设置视频，音频的输出格式 mp4
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
    //3.设置音频的编码格式
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    //设置图像的编码格式
    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
    //设置立体声
    //        mediaRecorder.setAudioChannels(2);
    //设置最大录像时间 单位：毫秒
    //        mediaRecorder.setMaxDuration(60 * 1000);
    //设置最大录制的大小 单位，字节
    //        mediaRecorder.setMaxFileSize(1024 * 1024);
    //音频一秒钟包含多少数据位
    CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
    mediaRecorder.setAudioEncodingBitRate(44100);
    if (mProfile.videoBitRate > 2 * 1024 * 1024) {
      mediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
    } else {
      mediaRecorder.setVideoEncodingBitRate(1024 * 1024);
    }
    mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);

    //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
    mediaRecorder.setOrientationHint(270);
    //设置录像的分辨率
    mediaRecorder.setVideoSize(352, 288);

    //设置录像视频输出地址
    mediaRecorder.setOutputFile(currentVideoFilePath);
  }

  /**
   * 创建视频文件保存路径
   */
  public static String getSDPath(Context context) {
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      Toast.makeText(context, "请查看您的SD卡是否存在！", Toast.LENGTH_SHORT).show();
      return null;
    }

    File sdDir = Environment.getExternalStorageDirectory();
    File eis = new File(sdDir.toString() + "/RecordVideo/");
    if (!eis.exists()) {
      eis.mkdir();
    }
    return sdDir.toString() + "/RecordVideo/";
  }

  private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
    Camera.Size bestSize = sizes.get(0);
    int largestArea = bestSize.width * bestSize.height;
    for (Camera.Size s : sizes) {
      int area = s.width * s.height;
      if (area > largestArea) {
        bestSize = s;
        largestArea = area;
      }
    }
    return bestSize;
  }

  // 查找相机横纵比适配结果
  private Camera.Size getPreviewSize(List<Camera.Size> sizes, int width, int height) {
    Log.d("hhhh", width + "");
    Log.d("hhhh", height + "");
    final double TOLERANCE = 0.1;
    double targetRatio = (double) width / height;
    Camera.Size res = null;
    double minDiff = Double.MAX_VALUE;

    for (Camera.Size size : sizes) {
      double ratio = (double) size.width / size.height;
      if (Math.abs(ratio - targetRatio) > TOLERANCE) continue;
      if (Math.abs(ratio - targetRatio) < minDiff) {
        minDiff = Math.abs(ratio - targetRatio);
        res = size;
      }
    }

    if (res == null) {
      for (Camera.Size size : sizes) {
        if (Math.abs(size.height - height) < minDiff) {
          res = size;
          minDiff = Math.abs(size.height - height);
        }
      }
    }
    return res;
  }

  private void getPreViewImage() {

    mCamera.setPreviewCallback(new Camera.PreviewCallback() {
      @Override
      public void onPreviewFrame(byte[] data, Camera camera) {
        if (allowAnalysis) {
          allowAnalysis = false;

          //处理data
          Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
          YuvImage yuvimage =
              new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100,
              baos);// 80--JPG图片的质量[0-100],100最高
          byte[] rawImage = baos.toByteArray();
          //将rawImage转换成bitmap
          BitmapFactory.Options options = new BitmapFactory.Options();
          options.inPreferredConfig = Bitmap.Config.RGB_565;
          Bitmap bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
          //                    idx++;
          //                    text.setText(idx+"");
          //                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
          //                mResult=(ImageView)findViewById( R.id.result_iv );
          //                mResult.setImageBitmap(bitmap);
          rotateMyBitmap(bitmap);
        }
        executorService.execute(mAnalysisTask);
      }
    });
  }

  public void rotateMyBitmap(Bitmap bmp) {
    //*****旋转一下
    Matrix matrix = new Matrix();
    matrix.postRotate(270);

    Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);

    Bitmap nbmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

    mResult = (ImageView) findViewById(R.id.result_iv);
    //*******显示一下
    mResult.setImageBitmap(nbmp2);

    long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
    String strTime = String.valueOf(time);
    saveImg(nbmp2, index0 + ".jpg", this);
    index0++;
  }

  public boolean saveImg(Bitmap bitmap, String name, Context context) {
    try {
      //            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() ;
      String sdcardPath = System.getenv("EXTERNAL_STORAGE");      //获得sd卡路径
      String dir = Environment.getExternalStorageDirectory().toString()
          + "/toworldpic/";                    //图片保存的文件夹名
      File file = new File(dir);                                 //已File来构建
      if (!file.exists()) {                                     //如果不存在  就mkdirs()创建此文件夹
        file.mkdirs();
      }
      Log.i("SaveImg", "file uri==>" + dir);
      File mFile = new File(dir + name);                        //将要保存的图片文件
      if (mFile.exists()) {
        mFile.delete();
        //                Toast.makeText(context, "该图片已存在!", Toast.LENGTH_SHORT).show();
        //                return false;
      }

      FileOutputStream outputStream = new FileOutputStream(mFile);     //构建输出流
      bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);  //compress到输出outputStream
      Uri uri = Uri.fromFile(mFile);                               //获得图片的uri
      context.sendBroadcast(
          new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));//发送广播通知更新图库，这样系统图库可以找到这张图片

      SharedPreferences sharedPreferences0 = context.getSharedPreferences("mySP", MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPreferences0.edit(); //编辑文件
      editor.putString("pic" + picIndex, dir + name);
      editor.putString("toworldpic", sdcardPath + "/toworldpic");
      editor.commit();
      bitmap = add(bitmap);
      File file0 = new File(dir + name);
      try {
        FileOutputStream fos = new FileOutputStream(file0);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        if (picIndex == 0) {
          video_one = bitmap;
        } else if (picIndex == 1) video_two = bitmap;
      } catch (Exception e) {
        e.printStackTrace();
      }
      picIndex++;

      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return false;
  }

  //水印
  private Bitmap add(Bitmap bm) {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    //canvas.drawColor(Color.WHITE);
    int width = bm.getWidth() / 2 - bm.getWidth() / 5;
    int length = bm.getHeight() * 2 / 3 + bm.getHeight() / 5;
    Log.d("fff", width + "");
    Log.d("fff", length + "");
    Paint paint = new Paint();
    int color = getResources().getColor(R.color.grey0);
    paint.setColor(color);
    paint.setAlpha(39);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(3);
    paint.setTextAlign(Paint.Align.LEFT);
    paint.setTextSize(35);
    paint.setShadowLayer(2, 2, 2, Color.BLACK);
    Path path = new Path();
    //        path.moveTo(60, 410);
    //        path.lineTo(320, 410);
    path.moveTo(width - 5, length - 100);
    path.lineTo(width + 350, length - 100);
    //        path.moveTo(150, 305);
    //        path.lineTo(900, 305);
    canvas.drawTextOnPath("    话机实名认证专用", path, 0, 40, paint);
    Path path1 = new Path();
    path1.moveTo(width, length - 50);
    path1.lineTo(width + 350, length - 50);
    //        path1.moveTo(60, 440);
    //        path1.lineTo(320, 440);
    ////        path1.moveTo(168, 335);
    ////        path1.lineTo(900, 335);
    canvas.drawTextOnPath("" + format.format(Calendar.getInstance().getTime()), path1, 0, 40,
        paint);
    Path path2 = new Path();

    path2.moveTo(width - 20, length);
    path2.lineTo(width + 350, length);
    //        path2.moveTo(60, 470);
    //        path2.lineTo(320, 470);
    ////        path2.moveTo(250, 365);
    ////        path2.lineTo(900, 365);
    SharedPreferences sharedPreferences = getSharedPreferences(BaseCom.ADMIN, MODE_PRIVATE);
    String user = sharedPreferences.getString("user", "");
    canvas.drawTextOnPath("                " + user, path2, 0, 40, paint);

    int min = bm.getWidth() < bm.getHeight() ? bm.getWidth() : bm.getHeight();
    float scale = (float) min / 400;
    Matrix matrix = new Matrix();
    //        matrix.postScale(scale, scale);
    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    return BitmapUtil.createWaterMaskLeftTop(this, bm, bitmap, 0, 0);
  }

  private Runnable mAnalysisTask = new Runnable() {
    @Override
    public void run() {
      try {
        Thread.sleep(4000);
        Log.d("aaaa", "aaaaa");
        index++;
        if (index < 3) {
          allowAnalysis = true;
        }
        //                if(index==0){
        //                    Util.createToast(FaceRecordingActivity.this, "请选择读取信息的方式!");
        //                }
        //                if(index==2){
        //                    Util.createToast(FaceRecordingActivity.this, "请微微张口!");
        //                }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mTimer != null) {
      mTimer.cancel();
    }
    try {
      // 向学生传达“问题解答完毕后请举手示意！”
      executorService.shutdown();

      // 向学生传达“XX分之内解答不完的问题全部带回去作为课后作业！”后老师等待学生答题
      // (所有的任务都结束的时候，返回TRUE)
      if (!executorService.awaitTermination(1, TimeUnit.MILLISECONDS)) {
        // 超时的时候向线程池中所有的线程发出中断(interrupted)。
        executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
      System.out.println("awaitTermination interrupted: " + e);
      executorService.shutdownNow();
    }
    //        executorService.shutdown();

  }

  private void submit() {
    dialog.getTvTitle().setText("正在上传图片");
    dialog.show();
    final HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
    PostPictureUpload postPictureUpload = new PostPictureUpload();
    postPictureUpload.setSession_token(Util.getLocalAdmin(FaceRecordingActivity.this)[0]);
    postPictureUpload.setType("5");
    Photo photo = new Photo();

    //        BaseCom.photoOne = bitmap_zero;
    //        BaseCom.photoThree = bitmap_two;
    //        BaseCom.photoTwo = bitmap_one;

    photo.setPhoto1(BitmapUtil.bitmapToBase64(BaseCom.photoOne));/*正面*/
    photo.setPhoto2(BitmapUtil.bitmapToBase64(BaseCom.photoTwo));/*背面*/
    photo.setPhoto3(BitmapUtil.bitmapToBase64(BaseCom.photoThree));/*扫描面*/
    photo.setPhoto5(BitmapUtil.bitmapToBase64(video_one));
    photo.setPhoto6(BitmapUtil.bitmapToBase64X(video_two));

    httpPost.setPhoto(photo);
    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
    httpPost.setParameter(postPictureUpload);
    httpPost.setApp_sign(
        Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));

    new OtherHttp().pictureUpload(
        new OtherRequest().pictureUpload(FaceRecordingActivity.this, dialog,
            new SuccessValue<RequestPictureUpload>() {
              @Override
              public void OnSuccess(RequestPictureUpload value) {
                if (urlList != null && urlList.size() > 0) {
                  for (int i = 0; i < urlList.size(); i++) {
                    deletePic(FaceRecordingActivity.this, urlList.get(i));
                  }
                  SharedPreferences sharedPreferences = getSharedPreferences("mySP", MODE_PRIVATE);
                  String uri0 = sharedPreferences.getString("pic0", "");
                  String uri1 = sharedPreferences.getString("pic1", "");
                  String photo1 = sharedPreferences.getString("photo1", "");
                  String photo2 = sharedPreferences.getString("photo2", "");
                  String photo3 = sharedPreferences.getString("photo3", "");
                  String witonePic = sharedPreferences.getString("witonePic", "");
                  String witonePicTmp = sharedPreferences.getString("witonePicTmp", "");

                  deletePic(FaceRecordingActivity.this, photo1);
                  deletePic(FaceRecordingActivity.this, photo2);
                  deletePic(FaceRecordingActivity.this, photo3);
                  deletePic(FaceRecordingActivity.this, uri0);
                  deletePic(FaceRecordingActivity.this, uri1);
                  deletePic(FaceRecordingActivity.this, witonePic);
                  deletePic(FaceRecordingActivity.this, witonePicTmp);
                }
                Log.d("ccc", value.getPhoto1());
                Log.d("ccc", value.getPhoto2());
                Log.d("ccc", value.getPhoto3());
                dialog.getTvTitle().setText("正在提交");
                dialog.show();
                PostSetOpen postSetOpen = new PostSetOpen();
                postSetOpen.setSession_token(Util.getLocalAdmin(FaceRecordingActivity.this)[0]);
                postSetOpen.setNumber(strGetNumber);
                postSetOpen.setOrderNo(strOrderNo);
                postSetOpen.setAuthenticationType("App人脸识别");
                postSetOpen.setCustomerName(strBuyName);
                postSetOpen.setCertificatesType("Idcode");
                postSetOpen.setCertificatesNo(strBuyId);
                postSetOpen.setAddress(strBuyAddress);
                postSetOpen.setDescription(strBuyRemark);
                postSetOpen.setCardType("ESIM");
                postSetOpen.setSimId(getSimId);
                postSetOpen.setIccid(getIccid);
                postSetOpen.setImsi(getImsi);
                postSetOpen.setPackageId(getSelectPackageId);
                postSetOpen.setPromotionsId(getSelectPromotionId);
                postSetOpen.setOrderAmount(getPrestore);
                postSetOpen.setPayAmount(getPrestore);

                postSetOpen.setPhotoFront(value.getPhoto2());
                postSetOpen.setPhotoBack(value.getPhoto1());
                postSetOpen.setMemo4(value.getPhoto3());
                postSetOpen.setVideoPhotos1(value.getPhoto5());
                postSetOpen.setVideoPhotos2(value.getPhoto6());

                postSetOpen.setPayMethod(BaseCom.payMethod);
                HttpPost<PostSetOpen> httpPost1 = new HttpPost<PostSetOpen>();
                httpPost1.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost1.setParameter(postSetOpen);
                httpPost1.setApp_sign(
                    Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postSetOpen) + BaseCom.APP_PWD));
                new CardHttp().liangSetOpen(httpPost1,
                    new Subscriber<HttpRequest<RequestSetOpen>>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {
                        dialog.dismiss();
                      }

                      @Override
                      public void onNext(
                          final HttpRequest<RequestSetOpen> requestSetOpenHttpRequest) {
                        dialog.dismiss();
                        Util.createToast(FaceRecordingActivity.this,
                            requestSetOpenHttpRequest.getMes());
                        if (requestSetOpenHttpRequest.getCode() == BaseCom.NORMAL) {
                          // request = requestSetOpenHttpRequest.getData().getRequest();
                          Intent intent =
                              new Intent(FaceRecordingActivity.this, DialogActivity.class);
                          intent.putExtra("from", from);
                          startActivityForResult(intent, BUYRESULT);
                        } else if (requestSetOpenHttpRequest.getCode() == BaseCom.LOSELOG
                            || requestSetOpenHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                          Util.gotoActy(FaceRecordingActivity.this, LoginActivity.class);
                        } else {
                          /*是否重新提交*/

                          AlertDialog.Builder builder =
                              new AlertDialog.Builder(FaceRecordingActivity.this);

                          builder.setCancelable(false);
                          builder.setMessage(
                              requestSetOpenHttpRequest.getMes() + "\n" + "是否重新提交该订单?");
                          builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();

                              new Thread(new Runnable() {
                                @Override
                                public void run() {
                                  try {
                                    Thread.sleep(1500);
                                    android.os.Message message = new android.os.Message();
                                    message.what = SUBMIT_AGAIN;
                                    //                                                handler.sendMessage(message); //告诉主线程执行任务

                                  } catch (InterruptedException e) {
                                    e.printStackTrace();
                                  }
                                }
                              }).start();
                            }
                          });
                          builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              AppManager.getAppManager().finishActivity();
                              AppManager.getAppManager().finishActivity(HuajiSelectActivity.class);
                              AppManager.getAppManager()
                                  .finishActivity(HuajiCuteDetailActivity.class);
                              AppManager.getAppManager()
                                  .finishActivity(MessageCollectionNewActivity.class);
                            }
                          });
                          builder.create().show();
                        }
                      }
                    });
              }
            }), httpPost);
  }

  private int uploadSuccessCount;
  private Map<String, String> imgMap = new HashMap<>();
  private int totalPic;

  private void uploadImage(String bitmap, String key) {
    if (bitmap == null) return;
    HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
    PostPictureUpload postPictureUpload = new PostPictureUpload();
    postPictureUpload.setSession_token(Util.getLocalAdmin(FaceRecordingActivity.this)[0]);
    postPictureUpload.setType("6");
    Photo photo = new Photo();
    photo.setPhoto1(bitmap);
    httpPost.setPhoto(photo);
    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
    httpPost.setParameter(postPictureUpload);
    httpPost.setApp_sign(
        Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
    new OtherHttp().pictureUpload(
        new OtherRequest().pictureUpload(FaceRecordingActivity.this, dialog, value -> {
          uploadSuccessCount++;
          imgMap.put(key, value.getPhoto1());
          if (uploadSuccessCount == totalPic) {
            writeIn();
          }
        }), httpPost);
  }

  private void writeIn() {
    HttpPost<PostReSubmit> httpPost = new HttpPost<>();
    postReSubmit.setPhoto1(imgMap.get(Constants.PHOTOONE));
    postReSubmit.setPhoto2(imgMap.get(Constants.PHOTOTWO));
    postReSubmit.setPhoto3(imgMap.get(Constants.PHOTOTHREE));
    postReSubmit.setLicensePic(imgMap.get(Constants.LICENCE));
    postReSubmit.setBranchPic(imgMap.get(Constants.GRID));
    postReSubmit.setPhoto4(imgMap.get(Constants.VIDEOPICONE));
    postReSubmit.setPhoto5(imgMap.get(Constants.VIDEOPICTWO));
    postReSubmit.setSession_token(Util.getLocalAdmin(FaceRecordingActivity.this)[0]);
    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
    httpPost.setParameter(postReSubmit);
    httpPost.setApp_sign(
        Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postReSubmit) + BaseCom.APP_PWD));
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
        dialog.dismiss();
        String toast = "";
        if (stringHttpRequest.getMes() != null && !stringHttpRequest.getMes().equals("")) {
          toast = stringHttpRequest.getMes();
        } else if (stringHttpRequest.getMsg() != null && !stringHttpRequest.getMsg().equals("")) {
          toast = stringHttpRequest.getMsg();
        }
        Util.createToast(FaceRecordingActivity.this, toast);

        if (stringHttpRequest.getCode() == BaseCom.NORMAL) {
          AppManager.getAppManager().finishActivity();
          AppManager.getAppManager().finishActivity(WriteInNewActivity.class);
        } else if (stringHttpRequest.getCode() == BaseCom.LOSELOG) {

        } else {
          AppManager.getAppManager().finishActivity();
        }
      }
    }, httpPost);
  }

  private void submit0() {
    dialog.getTvTitle().setText("正在上传图片");
    dialog.show();
    totalPic = 5;
    if (!TextUtils.isEmpty(postReSubmit.getLicensePic())) {
      totalPic++;
    }
    if (!TextUtils.isEmpty(postReSubmit.getBranchPic())) {
      totalPic++;
    }

    //        BaseCom.photoOne = bitmap_zero;
    //        BaseCom.photoThree = bitmap_two;
    //        BaseCom.photoTwo = bitmap_one;

    uploadImage(BitmapUtil.urlToBase64(this, postReSubmit.getPhoto1()), Constants.PHOTOONE);
    uploadImage(BitmapUtil.urlToBase64(this, postReSubmit.getPhoto2()), Constants.PHOTOTWO);
    uploadImage(BitmapUtil.urlToBase64(this, postReSubmit.getPhoto3()), Constants.PHOTOTHREE);
    uploadImage(BitmapUtil.bitmapToBase64(video_one), Constants.VIDEOPICONE);
    uploadImage(BitmapUtil.bitmapToBase64(video_two), Constants.VIDEOPICTWO);
    if (!TextUtils.isEmpty(postReSubmit.getLicensePic())) {
      uploadImage(BitmapUtil.urlToBase64(this, postReSubmit.getLicensePic()), Constants.LICENCE);
    }
    if (!TextUtils.isEmpty(postReSubmit.getBranchPic())) {
      uploadImage(BitmapUtil.urlToBase64(this, postReSubmit.getBranchPic()), Constants.GRID);
    }

  }
}

