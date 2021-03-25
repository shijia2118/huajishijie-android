package wintone.passportreader.sdk;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import kernal.idcard.android.RecogParameterMessage;
import kernal.idcard.android.RecogService;
import kernal.idcard.android.ResultMessage;
import wintone.passport.sdk.utils.AppManager;
import wintone.passport.sdk.utils.CameraParametersUtils;
import wintone.passport.sdk.utils.Devcode;
import wintone.passport.sdk.utils.FrameCapture;
import wintone.passport.sdk.view.ViewfinderView;

/**
 * 项目名称：PassportReader_Sample_Sdk 类名称：CameraActivity
 * 类描述：手动拍照护照MRZ码识别的类。创建人：huangzhen 创建时间：2014年7月10日 下午3:22:10 修改人：huanzhen
 * 修改时间：2014年11月11日 下午3:22:10 修改备注：在原来的基础上更改拍照界面使程序变得更加美观
 */
@SuppressLint("NewApi")
public class CameraActivity extends Activity implements SurfaceHolder.Callback,
        Camera.PreviewCallback, OnClickListener {
    public String PATH = Environment.getExternalStorageDirectory().toString()
            + "/wtimage/";
    private int width, height, WIDTH, HEIGHT;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private RelativeLayout rightlyaout, bg_camera_doctype;
    private ToneGenerator tone;
    public RecogService.recogBinder recogBinder;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private boolean istakePic = false;// 判断是否已经拍照，如果已经拍照则提示正在识别中请稍等
    private float scale = 1;
    private long time1, recogTime;
    private boolean isCompress = false;// 是否将分辨率大的图片压缩成小的图片
    private ViewfinderView viewfinder_view;
    private int uiRot = 0, tempUiRot = 0, rotation = 0;
    private Bitmap rotate_bitmap;
    private int rotationWidth, rotationHeight;
    private Camera.Parameters parameters;
    private boolean isOpenFlash = false;
    private ImageButton imbtn_flash, imbtn_camera_back, imbtn_takepic,
            imbtn_eject;
    // 设置全局变量，进行自动检测参数的传递 start
    private byte[] data1;
    private int regWidth, regHeight, left, right, top, bottom, nRotateType;
    private TextView tv_camera_doctype;
    private int lastPosition = 0;
    // end
    private int quality = 85;
    private final String IDCardPath = Environment.getExternalStorageDirectory()
            .toString() + "/AndroidWT/IdCapture/";
    private String picPathString = PATH + "WintoneIDCard.jpg";
    private String HeadJpgPath = PATH + "head.jpg";
    private String recogResultPath = PATH + "idcapture.txt",
            recogResultString = "";
    private double screenInches;
    private int[] nflag = new int[4];
    private boolean isTakePic = false;
    private String devcode = "";
    public static int nMainIDX;
    private Vibrator mVibrator;
    private int Format = ImageFormat.NV21;// .YUY2
    private String name = "";
    private boolean isFocusSuccess = false;
    public boolean isTouched = false;
    private boolean isFirstGetSize = true;
    private Size size;
    private TimerTask timer;
    private Message msg;
    private Runnable touchTimeOut = new Runnable() {
        @Override
        public void run() {

            isTouched = false;
        }
    };
    public static Handler resetIsTouchedhandler = new Handler();
    private int DelayedFrames = -1;
    // private boolean isConfirmSideLine = true;
    private int ConfirmSideSuccess = -1;
    private int LoadBufferImage = -1;
    private CameraParametersUtils cameraParametersUtils;
    Timer time = new Timer();
    private List<Size> list;// 存放预览分辨率集合
    private boolean isSetCameraParamter = false;
    public static boolean isOpendetectLightspot = false;
    private ImageView imbtn_spot_dection;
    private Intent recogIntent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            cameraParametersUtils.setScreenSize(CameraActivity.this);
            width = cameraParametersUtils.srcWidth;
            height = cameraParametersUtils.srcHeight;
            rotation = CameraParametersUtils.setRotation(width, height, uiRot,
                    rotation);
            if (msg.what == 100) {
                if (rotation == 0 || rotation == 180) {
                    findLandscapeView();
                } else if (rotation == 90 || rotation == 270) {
                    findPortraitView();
                }
            } else {

                if (rotation == 0 || rotation == 180) {
                    changeCameraParammter(uiRot);
                    findLandscapeView();
                } else if (rotation == 90 || rotation == 270) {
                    changeCameraParammter(uiRot);
                    findPortraitView();
                }
            }

            isTouched = false;
            isSetCameraParamter = false;
        }
    };
    private Toast mtoast;
    private Handler detectLightspotHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (mtoast != null) {
                    mtoast.setText(R.string.detectLightspot);
                } else {
                    /*
					 * 第一个参数：当前的上下文环境，用this或getApplicationContext()表示。
					 * 第二个参数：显示的字符串，用R.string表示。
					 * 第三个参数：显示的时间长短。用LENGTH_LONG(长)或LENGTH_SHORT(短)表示，也可以用毫秒。
					 */
                    mtoast = Toast.makeText(getApplicationContext(),
                            getString(R.string.detectLightspot),
                            Toast.LENGTH_SHORT);
                }

            } else if (msg.what == 2) {
                // imbtn_spot_dection
                // .setBackgroundResource(R.drawable.spot_dection_on);

                if (mtoast != null) {
                    mtoast.setText(R.string.opendetectLightspot);
                } else {
					/*
					 * 第一个参数：当前的上下文环境，用this或getApplicationContext()表示。
					 * 第二个参数：显示的字符串，用R.string表示。
					 * 第三个参数：显示的时间长短。用LENGTH_LONG(长)或LENGTH_SHORT(短)表示，也可以用毫秒。
					 */
                    mtoast = Toast.makeText(getApplicationContext(),
                            getString(R.string.opendetectLightspot),
                            Toast.LENGTH_SHORT);
                }
            } else if (msg.what == 3) {
                // imbtn_spot_dection
                // .setBackgroundResource(R.drawable.spot_dection_off);

                if (mtoast != null) {
                    mtoast.setText(R.string.closeddetectLightspot);
                } else {
					/*
					 * 第一个参数：当前的上下文环境，用this或getApplicationContext()表示。
					 * 第二个参数：显示的字符串，用R.string表示。
					 * 第三个参数：显示的时间长短。用LENGTH_LONG(长)或LENGTH_SHORT(短)表示，也可以用毫秒。
					 */
                    mtoast = Toast.makeText(getApplicationContext(),
                            getString(R.string.closeddetectLightspot),
                            Toast.LENGTH_SHORT);
                }
            }
            // 规定显示500毫秒后，toast消失
            showMyToast(mtoast, 300);
            // mtoast.show(); //显示toast信息
        }
    };
    private Runnable updateUI = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            cameraParametersUtils.setScreenSize(CameraActivity.this);
            width = cameraParametersUtils.srcWidth;
            height = cameraParametersUtils.srcHeight;
            rotation = CameraParametersUtils.setRotation(width, height, uiRot,
                    rotation);
            if (rotation == 0 || rotation == 180) {
                cameraParametersUtils.getCameraPreParameters(camera, rotation,
                        list);
                setCameraParamters();
                findLandscapeView();

            } else if (rotation == 90 || rotation == 270) {
                cameraParametersUtils.getCameraPreParameters(camera, rotation,
                        list);
                setCameraParamters();
                findPortraitView();

            }
            isTouched = false;
            isSetCameraParamter = false;
        }

    };
    // 识别验证
    public ServiceConnection recogConn = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            recogBinder = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {

            recogBinder = (RecogService.recogBinder) service;

        }

        ;

    };
    private int flag = 0;// 只识别正反面的标志 0-正反面 1-正面 2-背面
    private boolean isTakePicRecog = false;// 是否进行强制拍照识别
    public static boolean isTakePicRecogFrame = false;
    private String picPathString1 = "";
    private int detectLightspot = 0;
    private int VehicleLicenseflag = 0;// 从哪个界面跳转到相机界面的，0为从MainActivity界面跳转的，1为从VehicleLicenseMainActivity界面跳转的

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (resetIsTouchedhandler == null) {
            resetIsTouchedhandler = new Handler();
        }
        sum = 0;
        if (getResources().getConfiguration().locale.getLanguage().equals("zh")
                && getResources().getConfiguration().locale.getCountry()
                .equals("CN")) {
            RecogService.nTypeInitIDCard = 0;
        } else if (getResources().getConfiguration().locale.getLanguage()
                .equals("zh")
                && getResources().getConfiguration().locale.getCountry()
                .equals("TW")) {
            RecogService.nTypeInitIDCard = 0;
        } else {
            RecogService.nTypeInitIDCard = 3;
        }
        RecogService.isRecogByPath = false;
        RecogService.isOnlyReadSDAuthmodeLSC = false;// 如果为真，程序不再将assets中的文件复制到sd卡中，包括授权文件，此参数设置必须在调用识别之前
        bg_camera_doctype = (RelativeLayout) findViewById(R.id.bg_camera_doctype);
        viewfinder_view = (ViewfinderView) this
                .findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceViwe);
        imbtn_flash = (ImageButton) this.findViewById(R.id.imbtn_flash);
        imbtn_camera_back = (ImageButton) this
                .findViewById(R.id.imbtn_camera_back);
        imbtn_takepic = (ImageButton) this.findViewById(R.id.imbtn_takepic);
        imbtn_takepic.setOnClickListener(this);
        imbtn_eject = (ImageButton) this.findViewById(R.id.imbtn_eject);
        imbtn_spot_dection = (ImageView) this
                .findViewById(R.id.imbtn_spot_dection);
        imbtn_eject.setOnClickListener(this);
        imbtn_eject.setVisibility(View.VISIBLE);
        tv_camera_doctype = (TextView) this
                .findViewById(R.id.tv_camera_doctype);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(CameraActivity.this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Intent intent = getIntent();
        nMainIDX = intent.getIntExtra("nMainId", 2);
        devcode = intent.getStringExtra("devcode");
        flag = intent.getIntExtra("flag", 0);
        VehicleLicenseflag = intent.getIntExtra("VehicleLicenseflag", 0);
        viewfinder_view.setIdcardType(nMainIDX);
        tv_camera_doctype.setTextColor(Color.rgb(243, 153, 18));
        switch (nMainIDX) {
            case 3000:
                tv_camera_doctype.setText(getString(R.string.mrz));
                break;
            case 13:
                tv_camera_doctype.setText(getString(R.string.passport));
                break;
            case 2:
                tv_camera_doctype.setText(getString(R.string.ID_card));
                break;
            case 9:
                tv_camera_doctype.setText(getString(R.string.EPT_HK_Macau));
                break;
            case 11:
                tv_camera_doctype.setText(getString(R.string.MRTTTP));
                break;
            case 12:
                tv_camera_doctype.setText(getString(R.string.visa));
                break;
            case 22:
                tv_camera_doctype.setText(getString(R.string.NEEPT_HK_Macau));
                break;
            case 5:
                tv_camera_doctype.setText(getString(R.string.china_driver));
                break;
            case 6:
                tv_camera_doctype
                        .setText(getString(R.string.china_driving_license));
                break;
            case 1001:
                tv_camera_doctype.setText(getString(R.string.HK_IDcard));
                break;
            case 14:
                tv_camera_doctype.setText(getString(R.string.HRPO));
                break;
            case 15:
                tv_camera_doctype.setText(getString(R.string.HRPR));
                break;
            case 1005:
                tv_camera_doctype.setText(getString(R.string.IDCard_Macau));
                break;
            case 1012:
                tv_camera_doctype.setText(getString(R.string.New_IDCard_Macau));
                break;

            case 10:
                tv_camera_doctype.setText(getString(R.string.TRTTTMTP));
                break;
            case 25:
                tv_camera_doctype.setText(getString(R.string.NTRTTTMTP));
                break;
            case 26:
                tv_camera_doctype.setText(getString(R.string.NTRTTTMTP_01));
                break;
            case 1031:
                tv_camera_doctype.setText(getString(R.string.Taiwan_IDcard_front));
                break;
            case 1032:
                tv_camera_doctype
                        .setText(getString(R.string.Taiwan_IDcard_reverse));
                break;
            case 1030:
                tv_camera_doctype
                        .setText(getString(R.string.National_health_insurance_card));
                break;
            case 2001:
                tv_camera_doctype.setText(getString(R.string.MyKad));
                break;
            case 2004:
                tv_camera_doctype.setText(getString(R.string.Singapore_IDcard));
                break;
            case 2003:
                tv_camera_doctype.setText(getString(R.string.Driver_license));
                break;
            case 2002:
                tv_camera_doctype
                        .setText(getString(R.string.California_driver_license));
                break;
            default:
                break;
        }

    }

    // private long natiantime=0;
    @Override
    @SuppressLint("NewApi")
    @TargetApi(19)
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.wintone_demo_carmera);
        cameraParametersUtils = new CameraParametersUtils(CameraActivity.this);
        width = cameraParametersUtils.srcWidth;
        height = cameraParametersUtils.srcHeight;
        // android设备的物理尺寸
        double x = Math
                .pow(displayMetrics.widthPixels / displayMetrics.xdpi, 2);
        double y = Math.pow(displayMetrics.heightPixels / displayMetrics.ydpi,
                2);
        screenInches = Math.sqrt(x + y);
        // android设备的物理尺寸 end
        cameraParametersUtils.hiddenVirtualButtons(getWindow().getDecorView());
        rotationWidth = displayMetrics.widthPixels;
        rotationHeight = displayMetrics.heightPixels;
        AppManager.getAppManager().addActivity(CameraActivity.this);
    }

    /**
     * @Title: findPortraitView @Description: 界面竖屏布局 @param 设定文件 @return void
     * 返回类型 @throws
     */
    public void findPortraitView() {
        isTakePicRecog = false;
        isTakePicRecogFrame = false;
        imbtn_takepic.setVisibility(View.GONE);
        imbtn_camera_back.setOnClickListener(this);
        imbtn_flash.setOnClickListener(this);
        // TODO Auto-generated method stub
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        uiRot = getWindowManager().getDefaultDisplay().getRotation();
        viewfinder_view.setDirecttion(uiRot);
        // 闪光灯的UI布局
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (int) (height * 0.05), (int) (height * 0.05));
        layoutParams.leftMargin = (int) (width * 0.89);
        layoutParams.topMargin = (int) (height * 0.08);
        imbtn_flash.setLayoutParams(layoutParams);
        // 返回按钮的UI布局
        layoutParams = new RelativeLayout.LayoutParams((int) (height * 0.05),
                (int) (height * 0.05));
        layoutParams.leftMargin = (int) (width * 0.02);
        layoutParams.topMargin = (int) (height * 0.08);
        imbtn_camera_back.setLayoutParams(layoutParams);
        // 检测光斑UI布局
        layoutParams = new RelativeLayout.LayoutParams((int) (width * 0.15),
                (int) (width * 0.12));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.topMargin = (int) (height * 0.07);
        imbtn_spot_dection.setLayoutParams(layoutParams);
        imbtn_spot_dection.setOnClickListener(this);
        if (isOpendetectLightspot) {
            imbtn_spot_dection
                    .setBackgroundResource(R.drawable.spot_dection_on);

        } else {
            imbtn_spot_dection
                    .setBackgroundResource(R.drawable.spot_dection_off);

        }
        int surfaceWidth = cameraParametersUtils.surfaceWidth;
        int surfaceHeight = cameraParametersUtils.surfaceHeight;
        if (height == surfaceView.getHeight() || surfaceView.getHeight() == 0) {
            layoutParams = new RelativeLayout.LayoutParams(width, height);
            surfaceView.setLayoutParams(layoutParams);
            // 证件类型背景UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (height * 0.65), (int) (height * 0.05));
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.topMargin = (int) (height * 0.46);
            bg_camera_doctype.setLayoutParams(layoutParams);

            // 拍照按钮布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (height * 0.1), (int) (height * 0.1));
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.topMargin = (int) (height * 0.75);
            imbtn_takepic.setLayoutParams(layoutParams);
        } else if (height > surfaceView.getHeight()
                && surfaceView.getHeight() != 0) {
            // 如果将虚拟硬件弹出则执行如下布局代码，相机预览分辨率不变压缩屏幕的高度
            int surfaceViewWidth = (surfaceView.getHeight() * width) / height;
            layoutParams = new RelativeLayout.LayoutParams(surfaceViewWidth,
                    height);
            layoutParams.leftMargin = (width - surfaceViewWidth) / 2;
            surfaceView.setLayoutParams(layoutParams);
            // 证件类型背景UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (height * 0.65), (int) (height * 0.05));
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.topMargin = (int) (height * 0.46)
                    - (height - surfaceView.getHeight()) / 2;
            bg_camera_doctype.setLayoutParams(layoutParams);

            // 拍照按钮布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (height * 0.1), (int) (height * 0.1));
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.topMargin = (int) (height * 0.75)
                    - (height - surfaceView.getHeight()) / 2;
            imbtn_takepic.setLayoutParams(layoutParams);

        }

        // 显示拍照按钮布局
        layoutParams = new RelativeLayout.LayoutParams((int) (width * 0.6),
                (int) (height * 0.03));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imbtn_eject.setLayoutParams(layoutParams);
        imbtn_eject.setBackgroundResource(R.drawable.locker_btn_def01);
        if (surfaceWidth < width || surfaceHeight < height) {

            layoutParams = new RelativeLayout.LayoutParams(surfaceWidth,
                    surfaceHeight);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            // 拍照按钮布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (height * 0.1), (int) (height * 0.1));
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.leftMargin = (int) (width * 0.83);
            imbtn_takepic.setLayoutParams(layoutParams);
        }

        if (screenInches >= 8) {
            tv_camera_doctype.setTextSize(25);
        } else {
            tv_camera_doctype.setTextSize(20);
        }
        if (nMainIDX == 3000) {
            // 由于自动判断机读码的种类并未弄清，暂时先将机读码的强制拍照功能隐藏
            imbtn_eject.setVisibility(View.GONE);
        } else {
            imbtn_eject.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @Title: findLandscapeView @Description: 界面横屏布局 @param 设定文件 @return void
     * 返回类型 @throws
     */
    public void findLandscapeView() {
        isTakePicRecog = false;
        isTakePicRecogFrame = false;
        imbtn_takepic.setVisibility(View.GONE);
        imbtn_camera_back.setOnClickListener(this);
        imbtn_flash.setOnClickListener(this);
        // TODO Auto-generated method stub
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        uiRot = getWindowManager().getDefaultDisplay().getRotation();
        viewfinder_view.setDirecttion(uiRot);
        // 闪光灯的UI布局
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (int) (width * 0.05), (int) (width * 0.05));
        layoutParams.leftMargin = (int) (width * 0.06);
        layoutParams.topMargin = (int) (height * 0.08);
        imbtn_flash.setLayoutParams(layoutParams);
        // 返回按钮的UI布局
        layoutParams = new RelativeLayout.LayoutParams((int) (width * 0.05),
                (int) (width * 0.05));
        layoutParams.leftMargin = (int) (width * 0.06);
        layoutParams.topMargin = (int) (height * 0.97) - (int) (width * 0.08);
        imbtn_camera_back.setLayoutParams(layoutParams);
        // 检测光斑UI布局
        layoutParams = new RelativeLayout.LayoutParams((int) (width * 0.08),
                (int) (width * 0.07));
        layoutParams.leftMargin = (int) (width * 0.05);
        layoutParams.topMargin = (int) (height * 0.57) - (int) (width * 0.08);
        imbtn_spot_dection.setLayoutParams(layoutParams);
        imbtn_spot_dection.setOnClickListener(this);
        if (isOpendetectLightspot) {
            imbtn_spot_dection
                    .setBackgroundResource(R.drawable.spot_dection_on);
        } else {
            imbtn_spot_dection
                    .setBackgroundResource(R.drawable.spot_dection_off);
        }

        int surfaceWidth = cameraParametersUtils.surfaceWidth;
        int surfaceHeight = cameraParametersUtils.surfaceHeight;
        if (width == surfaceView.getWidth() || surfaceView.getWidth() == 0) {

            layoutParams = new RelativeLayout.LayoutParams(width, height);
            surfaceView.setLayoutParams(layoutParams);

            // 证件类型背景UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (width * 0.65), (int) (width * 0.05));
            layoutParams.leftMargin = (int) (width * 0.2);
            layoutParams.topMargin = (int) (height * 0.46);
            bg_camera_doctype.setLayoutParams(layoutParams);

            // 拍照按钮布局
            layoutParams = new RelativeLayout.LayoutParams((int) (width * 0.1),
                    (int) (width * 0.1));
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.leftMargin = (int) (width * 0.885);
            imbtn_takepic.setLayoutParams(layoutParams);
        } else if (width > surfaceView.getWidth()) {
            // 如果将虚拟硬件弹出则执行如下布局代码，相机预览分辨率不变压缩屏幕的高度
            int surfaceViewHeight = (surfaceView.getWidth() * height) / width;
            layoutParams = new RelativeLayout.LayoutParams(width,
                    surfaceViewHeight);
            layoutParams.topMargin = (height - surfaceViewHeight) / 2;
            surfaceView.setLayoutParams(layoutParams);

            // 证件类型背景UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (width * 0.65), (int) (width * 0.05));
            layoutParams.leftMargin = (int) (width * 0.1805);
            layoutParams.topMargin = (int) (height * 0.46);
            bg_camera_doctype.setLayoutParams(layoutParams);

            // 拍照按钮布局
            layoutParams = new RelativeLayout.LayoutParams((int) (width * 0.1),
                    (int) (width * 0.1));
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.leftMargin = (int) (width * 0.83);
            imbtn_takepic.setLayoutParams(layoutParams);

        }
        // 显示拍照按钮布局
        layoutParams = new RelativeLayout.LayoutParams((int) (width * 0.03),
                (int) (height * 0.4));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imbtn_eject.setLayoutParams(layoutParams);
        imbtn_eject.setBackgroundResource(R.drawable.locker_btn);
        if (surfaceWidth < width || surfaceHeight < height) {

            layoutParams = new RelativeLayout.LayoutParams(surfaceWidth,
                    surfaceHeight);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            surfaceView.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (width * 0.05), (int) (width * 0.05));
            layoutParams.leftMargin = (int) (width * 0.1);
            layoutParams.topMargin = (int) (height * 0.08);
            imbtn_flash.setLayoutParams(layoutParams);
            // 返回按钮的UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (width * 0.05), (int) (width * 0.05));
            layoutParams.leftMargin = (int) (width * 0.1);
            layoutParams.topMargin = (int) (height * 0.92)
                    - (int) (width * 0.04);
            imbtn_camera_back.setLayoutParams(layoutParams);
            // 拍照按钮布局
            layoutParams = new RelativeLayout.LayoutParams((int) (width * 0.1),
                    (int) (width * 0.1));
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.leftMargin = (int) (width * 0.885);
            imbtn_takepic.setLayoutParams(layoutParams);
        }

        if (screenInches >= 8) {
            tv_camera_doctype.setTextSize(25);
        } else {
            tv_camera_doctype.setTextSize(20);
        }
        if (nMainIDX == 3000) {
            // 由于自动判断机读码的种类并未弄清，暂时先将机读码的强制拍照功能隐藏
            imbtn_eject.setVisibility(View.GONE);
        } else {
            imbtn_eject.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NewApi")
    @TargetApi(14)
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        if (camera != null) {
            // setCameraParamters();
            camera.setDisplayOrientation(rotation);
            if (isFirstGetSize) {
                // 可以避免有时进入相机界面布局混乱的问题
                CameraActivity.this.runOnUiThread(updateUI);
            } else {
                msg = new Message();
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 设置相机参数
     */
    public void setCameraParamters() {
        try {
            if (null == camera) {
                camera = Camera.open();
            }

            WIDTH = cameraParametersUtils.preWidth;
            HEIGHT = cameraParametersUtils.preHeight;
            parameters = camera.getParameters();
            // if (parameters.getSupportedFocusModes().contains(
            // parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            // if (time != null) {
            // time.cancel();
            // time = null;
            // }
            // isFocusSuccess = true;
            // parameters
            // .setFocusMode(parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            // } else
            if (parameters.getSupportedFocusModes().contains(
                    parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(parameters.FOCUS_MODE_AUTO);
            }

            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setExposureCompensation(0);
            parameters.setPreviewSize(WIDTH/* 1920 */, HEIGHT/* 1080 */);
            System.out.println("WIDTH:" + WIDTH + "---HEIGHT：" + HEIGHT);
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            camera.setPreviewCallback(CameraActivity.this);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(rotation);
            camera.startPreview();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        synchronized (this) {
            try {
                if (camera != null) {
                    camera.setPreviewCallback(null);
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }
            } catch (Exception e) {
                Log.i("TAG", e.getMessage());
            }
        }
    }

    public void closeCamera() {
        synchronized (this) {
            try {
                if (camera != null) {
                    camera.setPreviewCallback(null);
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }
            } catch (Exception e) {
                Log.i("TAG", e.getMessage());
            }
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // System.out.println("onResume");
        rotation = CameraParametersUtils.setRotation(width, height, uiRot,
                rotation);
        startCamera();
        setCameraParamters();
        isSetCameraParamter = true;
    }

    /**
     * 改变相机参数
     */
    public void changeCameraParammter(int uiRot) {
        int tempWidth = 0;
        cameraParametersUtils.getCameraPreParameters(camera, rotation, list);
        // 如果不加以下代码，则程序home键退出，再进入后程序会黑屏，如果不加变量的话，
        // 程序横竖屏切换每次都设置体验会很不好
        if (isSetCameraParamter) {
            setCameraParamters();
        }
        // end
    }

    /**
     * 能够解决小米pad电源键熄屏15秒后，重新打开卡死的现象
     */
    private void startCamera() {
        // TODO Auto-generated method stub
        // 获得Camera对象
        try {
            if (null == camera) {
                camera = Camera.open();
            }

            if (timer == null) {
                timer = new TimerTask() {
                    public void run() {
                        if (camera != null) {
                            try {
                                isFocusSuccess = false;
                                autoFocus();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    ;
                };
            }

            time.schedule(timer, 200, 2500);
            parameters = camera.getParameters();
            list = parameters.getSupportedPreviewSizes();
            cameraParametersUtils
                    .getCameraPreParameters(camera, rotation, list);
        } catch (Exception e) {
            // 禁止使用相机权限后防止布局混乱
            Toast.makeText(getApplicationContext(),
                    getString(R.string.openCameraPermission),
                    Toast.LENGTH_SHORT).show();
            msg = new Message();
            msg.what = 100;
            handler.sendMessage(msg);
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (time != null) {
            time.cancel();
            time = null;
        }
        if (resetIsTouchedhandler != null) {
            resetIsTouchedhandler.removeCallbacks(touchTimeOut);
            resetIsTouchedhandler = null;
        }
        if (recogBinder != null) {
            unbindService(recogConn);
            recogBinder = null;
        }
        closeCamera();
        super.onDestroy();
    }

    public void autoFocus() {

        if (camera != null) {
            synchronized (camera) {
                try {
                    if (camera.getParameters().getSupportedFocusModes() != null
                            && camera
                            .getParameters()
                            .getSupportedFocusModes()
                            .contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                        camera.autoFocus(new AutoFocusCallback() {
                            public void onAutoFocus(boolean success,
                                                    Camera camera) {
                                if (success) {
                                    isFocusSuccess = true;
                                }

                            }
                        });
                    } else {

                        Toast.makeText(getBaseContext(),
                                getString(R.string.unsupport_auto_focus),
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    camera.stopPreview();
                    camera.startPreview();
                    Toast.makeText(this, R.string.toast_autofocus_failure,
                            Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    // 快门按下的时候onShutter()被回调拍照声音
    private ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            if (tone == null)
                // 发出提示用户的声音
                tone = new ToneGenerator(1,// AudioManager.AUDIOFOCUS_REQUEST_GRANTED
                        ToneGenerator.MIN_VOLUME);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
        }
    };

    /* 拍照后回显 */
    private Camera.PictureCallback PictureCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

//            createPreviewPicture(data1, "WintoneIDCard_" + name + "_full.jpg",
//                    PATH, size.width, size.height, 0, 0, size.width,
//                    size.height);
                        createPreviewPicture(data1, "WintoneIDCard_" + name + "Cut.jpg",
                    PATH, size.width, size.height, 0, 0, size.width,
                    size.height);
            getRecogResult();
        }
    };

    // 监听返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            isTouched = true;
            sum = -1;
            if (time != null) {
                time.cancel();
                time = null;
            }
            if (resetIsTouchedhandler != null) {
                resetIsTouchedhandler.removeCallbacks(touchTimeOut);
                resetIsTouchedhandler = null;
            }
            Intent intent = new Intent();
            intent.setClass(CameraActivity.this, MainActivity.class);
            // 设置切换动画，从右边进入，左边退出
            // CameraActivity.this.finish();
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int sum = 0;// 为了保证线程同步

    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {
        uiRot = getWindowManager().getDefaultDisplay().getRotation();
        if (isTouched) {
            return;
        } else {

            if (sum == 0) {
                data1 = data;
                MyThread thread = new MyThread();
                thread.start();
            }
        }

    }

    /**
     * @author huangzhen 识别核心线程
     */
    class MyThread extends Thread {
        public MyThread() {
            // TODO Auto-generated constructor stub
        }

        @Override
        public void run() {
            RecogOpera();
        }
    }

    public synchronized void RecogOpera() {
        sum = sum + 1;
        if (isTouched) {
            return;
        }
        if (uiRot != tempUiRot) {
            isTouched = true;
            Message mesg = new Message();
            handler.sendMessage(mesg);
            tempUiRot = uiRot;
        }
        if (isFirstGetSize) {
            isFirstGetSize = false;
            size = camera.getParameters().getPreviewSize();
            if (nMainIDX == 3000) {
                RecogService.nMainID = 1034;
            } else {
                RecogService.nMainID = nMainIDX;
            }

            RecogService.isRecogByPath = false;
            recogIntent = new Intent(CameraActivity.this, RecogService.class);
            bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
            // natiantime=System.currentTimeMillis();
        }

        if (isTakePicRecog && recogBinder != null) {
            RecogService.isRecogByPath = true;
            name = pictureName();
            picPathString = PATH + "WintoneIDCard_" + name + "_full.jpg";
            picPathString1 = PATH + "WintoneIDCard_" + name + "Cut.jpg";
            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString( "witonePic", picPathString1);
            editor.putString( "witonePicTmp",PATH + "WintoneIDCard_" + name + "Cut_tmp.jpg" );
            editor.commit();
            isTouched = true;
            if (timer != null)
                timer.cancel();
            camera.takePicture(shutterCallback, null, PictureCallback);
            return;
        } else if (recogBinder != null && !isTakePicRecog) {
            if (nMainIDX != 3000) {
                // 非机读码识别
                if (!isTakePic) {
                    if (isFocusSuccess) {
                        int CheckPicIsClear = -1;
                        if (nMainIDX == 2 || nMainIDX == 22 || nMainIDX == 1030
                                || nMainIDX == 1031 || nMainIDX == 1032
                                || nMainIDX == 1005 || nMainIDX == 1001
                                || nMainIDX == 2001 || nMainIDX == 2004
                                || nMainIDX == 2002 || nMainIDX == 2003
                                || nMainIDX == 14 || nMainIDX == 15
                                || nMainIDX == 25 || nMainIDX == 26) {

                            if (rotation == 90 || rotation == 270) {
                                // System.out.println("size.width:"+size.width);
                                // 竖屏
                                recogBinder
                                        .SetROI((int) (size.height * 0.025),
                                                (int) (size.width - 0.59375 * size.height) / 2,
                                                (int) (size.height * 0.975),
                                                (int) (size.width + 0.59375 * size.height) / 2);// 预留参数
                                left = (int) (size.height * 0.025);
                                top = (int) (size.width - 0.59375 * size.height) / 2;
                                right = (int) (size.height * 0.975);
                                bottom = (int) (size.width + 0.59375 * size.height) / 2;
                            } else if (rotation == 0 || rotation == 180) {
                                // 横屏
                                recogBinder
                                        .SetROI((int) (size.width * 0.15),
                                                (int) (size.height - 0.45 * size.width) / 2,
                                                (int) (size.width * 0.9),
                                                (int) (size.height + 0.45 * size.width) / 2);// 预留参数
                                left = (int) (size.width * 0.15);
                                top = (int) (size.height - 0.45 * size.width) / 2;
                                right = (int) (size.width * 0.9);
                                bottom = (int) (size.height + 0.45 * size.width) / 2;
                            }

                        } else if (nMainIDX == 5 || nMainIDX == 6) {
                            if (rotation == 90 || rotation == 270) {

                                // 竖屏
                                recogBinder
                                        .SetROI((int) (size.height * 0.025),
                                                (int) (size.width - 0.64 * size.height) / 2,
                                                (int) (size.height * 0.975),
                                                (int) (size.width + 0.64 * size.height) / 2);// 预留参数
                                left = (int) (size.height * 0.025);
                                top = (int) (size.width - 0.64 * size.height) / 2;
                                right = (int) (size.height * 0.975);
                                bottom = (int) (size.width + 0.64 * size.height) / 2;
                            } else if (rotation == 0 || rotation == 180) {
                                // 横屏
                                recogBinder
                                        .SetROI((int) (size.width * 0.2),
                                                (int) (size.height - 0.45 * size.width) / 2,
                                                (int) (size.width * 0.86),
                                                (int) (size.height + 0.45 * size.width) / 2);// 预留参数
                                left = (int) (size.width * 0.2);
                                top = (int) (size.height - 0.45 * size.width) / 2;
                                right = (int) (size.width * 0.86);
                                bottom = (int) (size.height + 0.45 * size.width) / 2;
                            }
                        } else {
                            if (rotation == 90 || rotation == 270) {
                                // 竖屏
                                recogBinder
                                        .SetROI((int) (size.height * 0.025),
                                                (int) (size.width - 0.659 * size.height) / 2,
                                                (int) (size.height * 0.975),
                                                (int) (size.width + 0.659 * size.height) / 2);// 预留参数
                                left = (int) (size.height * 0.025);
                                top = (int) (size.width - 0.659 * size.height) / 2;
                                right = (int) (size.height * 0.975);
                                bottom = (int) (size.width + 0.659 * size.height) / 2;

                            } else if (rotation == 0 || rotation == 180) {
                                // 横屏
                                recogBinder
                                        .SetROI((int) (size.width * 0.2),
                                                (int) (size.height - 0.45 * size.width) / 2,
                                                (int) (size.width * 0.85),
                                                (int) (size.height + 0.45 * size.width) / 2);// 预留参数
                                left = (int) (size.width * 0.2);
                                top = (int) (size.height - 0.45 * size.width) / 2;
                                right = (int) (size.width * 0.85);
                                bottom = (int) (size.height + 0.45 * size.width) / 2;
                            }
                        }
                        // 预留start
                        if (rotation == 0) {
                            recogBinder.SetRotateType(0);
                        } else if (rotation == 180) {
                            recogBinder.SetRotateType(2);
                        } else if (rotation == 90) {
                            recogBinder.SetRotateType(1);
                        } else if (rotation == 270) {
                            recogBinder.SetRotateType(3);
                        }
                        // 预留end
                        LoadBufferImage = recogBinder.LoadBufferImageEx(data1,
                                size.width, size.height, 24, 0);
                        if (isOpendetectLightspot) {
                            detectLightspot = recogBinder.DetectLightspot();
                            if (detectLightspot == 0) {
                                Message mesg = new Message();
                                mesg.what = 1;
                                detectLightspotHandler.sendMessage(mesg);
                            }
                        } else {
                            detectLightspot = -2;
                        }
                        // 0是二代证、健保卡等，1适用于驾照行驶证,2适用于护照类的
                        // if (nMainIDX == 5 || nMainIDX == 6) {
                        // recogBinder.SetConfirmSideMethod(1);
                        // } else if (nMainIDX == 13 || nMainIDX ==
                        // 9
                        // || nMainIDX == 10 || nMainIDX == 11
                        // || nMainIDX == 12) {
                        // recogBinder.SetConfirmSideMethod(2);
                        // recogBinder.IsDetectRegionValid(1);
                        // } else {
                        // if (nMainIDX == 3 || nMainIDX == 2) {
                        // recogBinder.SetConfirmSideMethod(0);
                        // recogBinder.IsDetectRegionValid(1);
                        // recogBinder.IsDetect180Rotate(1);
                        // recogBinder.SetDetectIDCardType(flag);
                        // } else {
                        // recogBinder.SetConfirmSideMethod(4);
                        // }
                        // }
                        if (LoadBufferImage == 0) {
                            if (detectLightspot != 0) {
                                ConfirmSideSuccess = recogBinder
                                        .ConfirmSideLineEx(0);
                                if (ConfirmSideSuccess >= 0) {
                                    CheckPicIsClear = recogBinder
                                            .CheckPicIsClearEx();
                                }
                            }
                        }
                        // System.out.println("ConfirmSideSuccess:"
                        // + ConfirmSideSuccess + "--"
                        // + "CheckPicIsClear:" + CheckPicIsClear);
                        // createPreviewPicture(data,
                        // "test_ConfirmSideSuccess:"+ConfirmSideSuccess+"_CheckPicIsClear:"+CheckPicIsClear+"_"+pictureName()+".jpg",
                        // PATH, size.width, size.height, 0, 0,
                        // size.width,
                        // size.height);
                        if (LoadBufferImage == 0 && ConfirmSideSuccess >= 0
                                && CheckPicIsClear == 0) {
                            // Message msg=new Message();
                            // testHandler.sendMessage(msg);
                            name = pictureName();
                            picPathString = PATH + "WintoneIDCard_" + name
                                    + ".jpg";
                            recogResultPath = PATH + "idcapture_" + name
                                    + ".txt";
                            HeadJpgPath = PATH + "head_" + name + ".jpg";
                            // 存储全图 start
//                            picPathString1 = PATH + "WintoneIDCard_" + name
//                                    + "_full.jpg";
                            picPathString1 = PATH + "WintoneIDCard_" + name
                                    + "Cut.jpg";
                            saveFullPic(picPathString1);
                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString( "witonePic", picPathString1);
                            editor.putString( "witonePicTmp",PATH + "WintoneIDCard_" + name + "Cut_tmp.jpg" );
                            editor.putString( "witonpath",Environment.getExternalStorageDirectory().toString()
                                            + "/wtimage" );
                            editor.commit();
                            // 存储全图 end
                            isTakePic = true;
                            new FrameCapture(data1, WIDTH, HEIGHT, left, top,
                                    right, bottom, "11");
                            if (timer != null)
                                timer.cancel();
                            time1 = System.currentTimeMillis();
                            RecogService.isRecogByPath = false;
                            getRecogResult();
                        }
                    }
                }

            } else {
                // 机读码识别
                int returnType = 0;
                // data1 = data;
                regWidth = size.width;
                regHeight = size.height;
                left = (int) (0.15 * size.width);
                right = (int) (size.width * 0.85);
                top = size.height / 3;
                bottom = 2 * size.height / 3;
                // 预留参数
                recogBinder.SetROI(left, top, right, bottom);// 预留参数
                LoadBufferImage = recogBinder.LoadBufferImageEx(data1,
                        size.width, size.height, 24, 0);
                int CheckPicIsClear = -1;
                if (LoadBufferImage == 0) {
                    detectLightspot = -2;
                    if (detectLightspot != 0) {
                        ConfirmSideSuccess = recogBinder.ConfirmSideLineEx(0);
                        System.out.println("ConfirmSideSuccess："
                                + ConfirmSideSuccess);
                        if (ConfirmSideSuccess == 1034
                                || ConfirmSideSuccess == 1033
                                || ConfirmSideSuccess == 1036) {
                            CheckPicIsClear = recogBinder.CheckPicIsClearEx();
                            if (CheckPicIsClear == 0) {
                                viewfinder_view.setCheckLeftFrame(1);
                                viewfinder_view.setCheckTopFrame(1);
                                viewfinder_view.setCheckRightFrame(1);
                                viewfinder_view.setCheckBottomFrame(1);
                            }
                        }
                    }
                }
                if ((ConfirmSideSuccess == 1034 || ConfirmSideSuccess == 1033 || ConfirmSideSuccess == 1036)
                        && CheckPicIsClear == 0) {
                    // new FrameCapture(data1,WIDTH,HEIGHT,"11");
                    // Message msg=new Message();
                    // testHandler.sendMessage(msg);
                    nMainIDX = ConfirmSideSuccess;
                    name = pictureName();
                    picPathString1 = PATH + "WintoneIDCard_" + name
                            + "_full.jpg";
                    saveFullPic(picPathString1);
                    switch (ConfirmSideSuccess) {
                        case 1034:
                            if (!istakePic) {

                                istakePic = true;
                                time1 = System.currentTimeMillis();

                                name = pictureName();
                                picPathString = PATH + "WintoneIDCard_" + name
                                        + ".jpg";
                                recogResultPath = PATH + "idcapture_" + name
                                        + ".txt";
                                HeadJpgPath = PATH + "head_" + name + ".jpg";
                                // createPreviewPicture(data1,
                                // "WintoneIDCard_" +
                                // name
                                // + ".jpg", PATH, regWidth, regHeight,
                                // left,
                                // top, right, bottom);

                                getRecogResult();
                                new FrameCapture(data1, regWidth, regHeight, left,
                                        top, right, bottom, "11");
                            }
                            break;
                        case 1036:
                            if (!istakePic) {

                                istakePic = true;
                                time1 = System.currentTimeMillis();

                                name = pictureName();
                                picPathString = PATH + "WintoneIDCard_" + name
                                        + ".jpg";
                                recogResultPath = PATH + "idcapture_" + name
                                        + ".txt";
                                HeadJpgPath = PATH + "head_" + name + ".jpg";
                                // createPreviewPicture(data1,
                                // "WintoneIDCard_" +
                                // name
                                // + ".jpg", PATH, regWidth, regHeight,
                                // left,
                                // top, right, bottom);

                                getRecogResult();
                                new FrameCapture(data1, regWidth, regHeight, left,
                                        top, right, bottom, "11");
                            }
                            break;
                        case 1033:
                            if (!istakePic) {

                                istakePic = true;
                                time1 = System.currentTimeMillis();
                                name = pictureName();
                                picPathString = PATH + "WintoneIDCard_" + name
                                        + ".jpg";
                                recogResultPath = PATH + "idcapture_" + name
                                        + ".txt";
                                HeadJpgPath = PATH + "head_" + name + ".jpg";
                                // createPreviewPicture(data1,
                                // "WintoneIDCard_" +
                                // name
                                // + ".jpg", PATH, regWidth, regHeight,
                                // left,
                                // top, right, bottom);

                                getRecogResult();

                                new FrameCapture(data1, regWidth, regHeight, left,
                                        top, right, bottom, "11");
                            }
                            break;
                        default:
                            break;
                    }
                }

            }
        }
        sum = sum - 1;
    }

    private Handler testHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getApplicationContext(),
                    "证件类型:" + ConfirmSideSuccess, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 保存全图
     *
     * @param picPathString1
     */
    private void saveFullPic(String picPathString1) {
        // TODO Auto-generated method stub
        // 存储全图 start
        File file = new File(PATH);
        if (!file.exists())
            file.mkdirs();
        YuvImage yuvimage = new YuvImage(data1, Format, size.width,
                size.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        yuvimage.compressToJpeg(new Rect(0, 0, size.width, size.height),
                quality, baos);

        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(picPathString1);
            outStream.write(baos.toByteArray());
            outStream.close();
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch
            // block
            e.printStackTrace();
        }
        // 存储全图 end
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            // 返回按鈕点击事件
            case R.id.imbtn_camera_back:
                isTouched = true;
                sum = -1;
                if (time != null) {
                    time.cancel();
                    time = null;
                }
                if (resetIsTouchedhandler != null) {
                    resetIsTouchedhandler.removeCallbacks(touchTimeOut);
                    resetIsTouchedhandler = null;
                }

//                Intent intent = new Intent();
//                intent.setClass(CameraActivity.this, MainActivity.class);
                // 设置切换动画，从右边进入，左边退出
                 CameraActivity.this.finish();
//                startActivity(intent);
                break;
            // 闪光灯点击事件
            case R.id.imbtn_flash:
                // TODO Auto-generated method stub
                try {
                    if (camera == null)
                        camera = Camera.open();
                    parameters = camera.getParameters();
                    // 魅族4设备
                    if ("MX4".equals(Build.MODEL)) {
                        camera.stopPreview();
                    }
                    if (parameters.getSupportedFlashModes() != null
                            && parameters.getSupportedFlashModes().contains(
                            Camera.Parameters.FLASH_MODE_TORCH)
                            && parameters.getSupportedFlashModes().contains(
                            Camera.Parameters.FLASH_MODE_OFF)) {
                        if (!isOpenFlash) {
                            isOpenFlash = true;


                            parameters
                                    .setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            if (parameters.getSupportedFocusModes().contains(
                                    parameters.FOCUS_MODE_AUTO)) {
                                parameters.setFocusMode(parameters.FOCUS_MODE_AUTO);
                            }
                            parameters.setPictureFormat(PixelFormat.JPEG);
                            parameters.setExposureCompensation(0);
                            parameters.setPreviewSize(WIDTH, HEIGHT);
                            try {
                                camera.setPreviewDisplay(surfaceHolder);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            camera.setPreviewCallback(CameraActivity.this);
                            camera.setParameters(parameters);
                            imbtn_flash.setBackgroundResource(R.drawable.flash_off);
                        } else {
                            isOpenFlash = false;
                            parameters
                                    .setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            if (parameters.getSupportedFocusModes().contains(
                                    parameters.FOCUS_MODE_AUTO)) {
                                parameters.setFocusMode(parameters.FOCUS_MODE_AUTO);
                            }
                            parameters.setPictureFormat(PixelFormat.JPEG);
                            parameters.setExposureCompensation(0);
                            parameters.setPreviewSize(WIDTH, HEIGHT);
                            try {
                                camera.setPreviewDisplay(surfaceHolder);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            camera.setPreviewCallback(CameraActivity.this);
                            camera.setParameters(parameters);
                            imbtn_flash.setBackgroundResource(R.drawable.flash_on);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.unsupportflash),
                                Toast.LENGTH_SHORT).show();
                    }
                    // 魅族4设备
                    if ("MX4".equals(Build.MODEL)) {
                        camera.startPreview();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                break;
            // 拍照按钮触发事件
            case R.id.imbtn_takepic:
                isTakePicRecog = true;

                break;
            // 显示拍照按钮触发事件
            case R.id.imbtn_eject:
                // RelativeLayout.LayoutParams layoutParams;

                // 改变拍照按钮布局

                // if (width == surfaceView.getWidth()
                // || surfaceView.getWidth() == 0) {
                // // 证件类型背景UI布局 进行手动拍照
                // layoutParams = new RelativeLayout.LayoutParams(
                // (int) (width * 0.65), (int) (width * 0.05));
                // layoutParams.leftMargin = (int) (width * 0.16);
                // layoutParams.topMargin = (int) (height * 0.46);
                // bg_camera_doctype.setLayoutParams(layoutParams);
                // } else if (width > surfaceView.getWidth()) {
                // // 证件类型背景UI布局 进行手动拍照
                // layoutParams = new RelativeLayout.LayoutParams(
                // (int) (width * 0.65), (int) (width * 0.05));
                // layoutParams.leftMargin = (int) (width * 0.14);
                // layoutParams.topMargin = (int) (height * 0.46);
                // bg_camera_doctype.setLayoutParams(layoutParams);
                // }

                isTakePicRecogFrame = true;
                imbtn_takepic.setVisibility(View.VISIBLE);
                imbtn_eject.setVisibility(View.GONE);
                break;
            // 检测光斑按钮
            case R.id.imbtn_spot_dection:
                if (isOpendetectLightspot) {
                    isOpendetectLightspot = false;
                    imbtn_spot_dection
                            .setBackgroundResource(R.drawable.spot_dection_off);
                    Message mesg = new Message();
                    mesg.what = 3;
                    detectLightspotHandler.sendMessage(mesg);
                } else {
                    isOpendetectLightspot = true;
                    imbtn_spot_dection
                            .setBackgroundResource(R.drawable.spot_dection_on);
                    Message mesg = new Message();
                    mesg.what = 2;
                    detectLightspotHandler.sendMessage(mesg);
                }
                break;
            default:
                break;
        }
    }

    // 创建文件
    public void createFile(String path, String content, boolean iscreate) {
        if (iscreate) {
            System.out.println("path:" + path);
            File file = new File(path.substring(0, path.lastIndexOf("/")));
            if (!file.exists()) {
                file.mkdirs();
            }
            File newfile = new File(path);
            if (!newfile.exists()) {

                try {
                    newfile.createNewFile();
                    OutputStream out = new FileOutputStream(path);
                    byte[] buffer = content.toString().getBytes();
                    out.write(buffer, 0, buffer.length);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                newfile.delete();
                try {
                    newfile.createNewFile();
                    OutputStream out = new FileOutputStream(path);
                    byte[] buffer = content.toString().getBytes();
                    out.write(buffer, 0, buffer.length);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        ;
    }

    private ArrayList<Size> splitSize(String str, Camera camera) {
        if (str == null)
            return null;
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        ArrayList<Size> sizeList = new ArrayList<Size>();
        while (tokenizer.hasMoreElements()) {
            Size size = strToSize(tokenizer.nextToken(), camera);
            if (size != null)
                sizeList.add(size);
        }
        if (sizeList.size() == 0)
            return null;
        return sizeList;
    }

    private Size strToSize(String str, Camera camera) {
        if (str == null)
            return null;
        int pos = str.indexOf('x');
        if (pos != -1) {
            String width = str.substring(0, pos);
            String height = str.substring(pos + 1);
            return camera.new Size(Integer.parseInt(width),
                    Integer.parseInt(height));
        }
        return null;
    }

    public void createPreviewPicture(byte[] reconData, String pictureName,
                                     String path, int preWidth, int preHeight, int left, int top,
                                     int right, int bottom) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        // 如果有证件就将nv21数组保存成jpg图片 huangzhen
        YuvImage yuvimage = new YuvImage(reconData, Format, preWidth,
                preHeight, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(left, top, right, bottom), quality,
                baos);

        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(path + pictureName);
            outStream.write(baos.toByteArray());
            outStream.close();
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 如果有证件就将nv21数组保存成jpg图片 huangzhen
    }

    /**
     * @Title: pictureName @Description: 将文件命名 @param @return 设定文件 @return
     * String 文件以时间命的名字 @throws
     */
    public String pictureName() {
        String str = "";
        Time t = new Time();
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        if (month < 10)
            str = String.valueOf(year) + "0" + String.valueOf(month);
        else {
            str = String.valueOf(year) + String.valueOf(month);
        }
        if (date < 10)
            str = str + "0" + String.valueOf(date);
        else {
            str = str + String.valueOf(date);
        }
        if (hour < 10)
            str = str + "0" + String.valueOf(hour);
        else {
            str = str + String.valueOf(hour);
        }
        if (minute < 10)
            str = str + "0" + String.valueOf(minute);
        else {
            str = str + String.valueOf(minute);
        }
        if (second < 10)
            str = str + "0" + String.valueOf(second);
        else {
            str = str + String.valueOf(second);
        }
        return str;
    }

    public void getRecogResult() {
        RecogParameterMessage rpm = new RecogParameterMessage();
        rpm.nTypeLoadImageToMemory = 0;
        rpm.nMainID = nMainIDX;
        rpm.nSubID = null;
        rpm.GetSubID = true;
        rpm.GetVersionInfo = true;
        rpm.logo = "";
        rpm.userdata = "";
        rpm.sn = "";
        rpm.authfile = "";
        rpm.isSaveCut = true;
        if (isTakePicRecog) {
            rpm.isCut = true;
            rpm.nProcessType = 7;
            rpm.nSetType = 1;
            rpm.cutSavePath = picPathString1;
        } else {
            rpm.isCut = false;
        }
        rpm.triggertype = 0;
        rpm.devcode = Devcode.devcode;
        rpm.isOnlyClassIDCard = true;
        // rpm.idcardRotateDegree=3;
        if (nMainIDX == 2) {
            rpm.isAutoClassify = true;
            // System.out.println("数据:"+data1.length+"宽度:"+WIDTH+"高度:"+HEIGHT);
            rpm.nv21bytes = data1;
            rpm.nv21_width = WIDTH;
            rpm.nv21_height = HEIGHT;
            rpm.lpHeadFileName = "";// 保存证件头像
            rpm.lpFileName = picPathString; // rpm.lpFileName当为空时，会执行自动识别函数
        } else {
            rpm.nv21bytes = data1;
            rpm.nv21_width = WIDTH;
            rpm.nv21_height = HEIGHT;
            rpm.lpHeadFileName = HeadJpgPath;
            rpm.lpFileName = picPathString; // rpm.lpFileName当为空时，会执行自动识别函数
        }
        // end
        try {
            // camera.stopPreview();
            ResultMessage resultMessage;
            resultMessage = recogBinder.getRecogResult(rpm);
            viewfinder_view.setCheckLeftFrame(1);
            viewfinder_view.setCheckTopFrame(1);
            viewfinder_view.setCheckRightFrame(1);
            viewfinder_view.setCheckBottomFrame(1);
            if (resultMessage.ReturnAuthority == 0
                    && resultMessage.ReturnInitIDCard == 0
                    && resultMessage.ReturnLoadImageToMemory == 0
                    && resultMessage.ReturnRecogIDCard > 0) {
                String iDResultString = "";
                String[] GetFieldName = resultMessage.GetFieldName;
                String[] GetRecogResult = resultMessage.GetRecogResult;
                // 获得字段位置坐标的函数
                // List<int[]>listdata=
                // resultMessage.textNamePosition;
                istakePic = false;
                for (int i = 1; i < GetFieldName.length; i++) {
                    if (GetRecogResult[i] != null) {
                        if (!recogResultString.equals(""))
                            recogResultString = recogResultString
                                    + GetFieldName[i] + ":" + GetRecogResult[i]
                                    + ",";
                        else {
                            recogResultString = GetFieldName[i] + ":"
                                    + GetRecogResult[i] + ",";
                        }
                    }
                }
                // camera.setPreviewCallback(null);
                mVibrator = (Vibrator) getApplication().getSystemService(
                        Service.VIBRATOR_SERVICE);
                mVibrator.vibrate(200);
                // natiantime=System.currentTimeMillis()-natiantime;
                // Toast.makeText(getApplicationContext(), "总时间:"+ natiantime+"
                // ms", Toast.LENGTH_SHORT).show();
                // closeCamera();
                Intent intent = new Intent();
                intent.putExtra("recogResult", recogResultString);
                intent.putExtra("picPathString1", picPathString1);
                this.setResult(1, intent);
                CameraActivity.this.finish();
//				if (devcode != null)
//					intent.putExtra("devcode", devcode);
//				intent.putExtra("fullPagePath", picPathString1);
//				intent.putExtra("VehicleLicenseflag", VehicleLicenseflag);
//				// CameraActivity.this.finish();
//				startActivity(intent);
            } else {
                String string = "";
                if (resultMessage.ReturnAuthority == -100000) {
                    string = getString(R.string.exception)
                            + resultMessage.ReturnAuthority;
                } else if (resultMessage.ReturnAuthority != 0) {
                    string = getString(R.string.exception1)
                            + resultMessage.ReturnAuthority;
                } else if (resultMessage.ReturnInitIDCard != 0) {
                    string = getString(R.string.exception2)
                            + resultMessage.ReturnInitIDCard;
                } else if (resultMessage.ReturnLoadImageToMemory != 0) {
                    if (resultMessage.ReturnLoadImageToMemory == 3) {
                        string = getString(R.string.exception3)
                                + resultMessage.ReturnLoadImageToMemory;
                    } else if (resultMessage.ReturnLoadImageToMemory == 1) {
                        string = getString(R.string.exception4)
                                + resultMessage.ReturnLoadImageToMemory;
                    } else {
                        string = getString(R.string.exception5)
                                + resultMessage.ReturnLoadImageToMemory;
                    }
                } else if (resultMessage.ReturnRecogIDCard <= 0) {
                    if (resultMessage.ReturnRecogIDCard == -6) {
                        string = getString(R.string.exception9);
                    } else {
                        string = getString(R.string.exception6)
                                + resultMessage.ReturnRecogIDCard;
                    }
                }
                // closeCamera();
                Intent intent = new Intent(CameraActivity.this,
                        ShowResultActivity.class);
                intent.putExtra("exception", string);
                intent.putExtra("VehicleLicenseflag", VehicleLicenseflag);
                // CameraActivity.this.finish();
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    getString(R.string.recognized_failed), Toast.LENGTH_SHORT)
                    .show();

        } finally {
            if (recogBinder != null) {
                unbindService(recogConn);
                recogBinder = null;
            }
        }

    }

    /**
     * 自定义Toast的消失时间
     *
     * @param toast
     * @param cnt
     */
    public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }
}
