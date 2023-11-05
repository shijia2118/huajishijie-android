package com.test.tworldapplication.activity.card;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.senter.helper.ConsantHelper;
import cn.com.senter.model.IdentityCardZ;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.kernal.passportreader.sdk.CardsCameraActivity;
import com.kernal.passportreader.sdk.ThreadManager;
import com.kernal.passportreader.sdk.utils.DefaultPicSavePath;
import com.kernal.passportreader.sdk.utils.ImportRecog;
import com.kernal.passportreader.sdk.utils.ManageIDCardRecogResult;
import com.luck.picture.lib.tools.ToastUtils;
import com.otg.idcard.OTGReadCardAPI;
import com.sunrise.reader.ReaderManagerService;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.activity.other.DialogActivity;
import com.test.tworldapplication.adapter.PostRegistration;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.AgentsLiangNumber;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.Photo;
import com.test.tworldapplication.entity.PostOpenInformation;
import com.test.tworldapplication.entity.PostPictureUpload;
import com.test.tworldapplication.entity.PostSetOpen;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.entity.RequestPreNumber;
import com.test.tworldapplication.entity.RequestSetOpen;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.http.RequestLiangPay;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.BlueReaderHelper;
import com.test.tworldapplication.utils.DialogManager;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;
import com.wildma.idcardcamera.camera.IDCardCamera;

import io.reactivex.Flowable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import kernal.idcard.android.RecogParameterMessage;
import kernal.idcard.android.RecogService;
import kernal.idcard.android.ResultMessage;
import kernal.idcard.camera.CardOcrRecogConfigure;
import kernal.idcard.camera.IBaseReturnMessage;
import org.codehaus.jackson.map.ObjectMapper;
import rx.Subscriber;
import sunrise.bluetooth.SRBluetoothCardReader;
import wintone.passport.sdk.utils.AppManager;
import wintone.passport.sdk.utils.Devcode;
import wintone.passport.sdk.utils.SharedPreferencesHelper;
import wintone.passportreader.sdk.CameraActivity;

import static com.sunrise.icardreader.helper.ConsantHelper.READ_CARD_FAILED;
import static com.sunrise.icardreader.helper.ConsantHelper.READ_CARD_SUCCESS;

public class MessageCollectionNewActivity2 extends BaseActivity implements IBaseReturnMessage {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.imgIdLast)
    ImageView imgIdLast;
    @BindView(R.id.imgIdBackLast)
    ImageView imgIdBackLast;
    @BindView(R.id.imgIdLasRemove)
    ImageView imgIdLasRemove;
    @BindView(R.id.imgIdBackLasRemove)
    ImageView imgIdBackLasRemove;
    @BindView(R.id.ll_cuteshow)
    LinearLayout llCuteshow;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvYue)
    TextView tvYue;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvOpen)
    TextView tvOpen;
    private ArrayList<String> IPArray = null;
    private Integer index = -1;

    int flag = 0;
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.tvCollection0)
    TextView tvCollection0;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etId)
    EditText etId;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.imgIdFront)
    ImageView imgFront;
    @BindView(R.id.imgIdFrontRemove)
    ImageView imgFrontRemove;
    @BindView(R.id.imgIdBack)
    ImageView imgIdBack;
    @BindView(R.id.imgIdBackRemove)
    ImageView imgBackRemove;
    @BindView(R.id.activity_message_collection)
    LinearLayout activityMessageCollection;
    @BindView(R.id.tvNext)
    TextView tvNext;

    @BindView(R.id.toBlueTooth)
    TextView toBlueTooth;
    @BindView(R.id.toScan)
    TextView toScan;

    @BindView(R.id.tvFace)
    TextView tvFace;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.layout3)
    LinearLayout layout3;
    @BindView(R.id.layout4)
    LinearLayout layout4;
    @BindView(R.id.layout5)
    LinearLayout layout5;
    @BindView(R.id.layout6)
    LinearLayout layout6;

    private final int REQUEST_CODE_CAMERA = 10086;
    private File tempFile;
    private File tempFile2;


    private SRBluetoothCardReader mSRBlueReaderHelper;


    int state_zere = 0, state_one = 0, state_two = 0, state_three = 0;
    String file_zero, file_one, file_two, file_three;
    Package mPackage = null;
    Promotion mPromotion = null;
    RequestCheck requestCheck = null;
    String phone = "";
    private BluetoothAdapter btAdapt;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int SUBMIT_AGAIN = 900;
    BlueReaderHelper mBlueReaderHelper;
    OTGReadCardAPI ReadCardAPI;
    private static final int SETTING_BT = 22;
    public static String loginTicket = "987654321123456789";
    public static final int MESSAGE_VALID_BTBUTTON = 17;
    String from = "";
    private String selectPath = "";
    Bitmap bm_card, bitmap_zero, bitmap_one, bitmap_two, bitmap_three;
    String[] mac;
    String type = "1";
    private String devcode = Devcode.devcode;// 项目授权开发码
    PopupWindow mPopupWindow;
    public RecogService.recogBinder recogBindViewer;
    private String recogResultString = "";
    private String[] splite_Result;
    private String result = "";
    private final int CAMERA = 10009;
    RequestImsi requestImsi = null;
    String iccid = "";
    AgentsLiangNumber agentsLiangNumber;
    RequestLiangPay requestLiangPay;
    RequestPreNumber requestPreNumber;
    String orderNO;
    ArrayList<String> urlList = new ArrayList<>();

    //    String request = "";
    final int BUYRESULT = 50;
    final int OPENRESULT = 70;
    final int STARTSCAN = 60;
    private View clickView = null;

    private int modes;

    String face = "";
    /*0成卡,获取传递过来的三个参数 点击获取信息按钮,先获取本地设备信息,判断蓝牙是否开启,未开启去开启蓝牙,若无蓝牙信息跳到选择蓝牙设备界面,选择之后回调,将蓝牙地址存本地。若已开启,若无蓝牙设备信息,选择设备存本地。蓝牙开启也有设备信息,提示设备信息,根据信息判断读取哪个设备,开始读取数据*/
    /*读取数据成功之后,将身份证图片存为bm_card,选择的图片存为bitmap_zero,与Package,Promotion,RequestCheck,以及身份证信息传递到提交页面*/
    /*1白卡,获取被锁定的手机号,两张照片,读取身份证信息,一起传到选择写卡页面*/
    private int bulmorcamera = 0;
    private String handleUri = "";

    int indextime = 0;
    String number, preStore, detail;
    private int readModesTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_collection2);

        SharedPreferences sharedPreferences0 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
        modes = sharedPreferences0.getInt("modes", -1);
        readModesTwo = sharedPreferences0.getInt( "readModesTwo", -1 );
        if (modes == 1) {

        } else if (modes == 2) {

        } else if (modes == 3) {

        }



        ButterKnife.bind(this);
        setBackGroundTitle("信息采集", true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        etName.clearFocus();
        etId.clearFocus();
        etId.setKeyListener(null);
        etAddress.clearFocus();
        etRemark.clearFocus();

//modes
        type = getIntent().getStringExtra("type");
        face = getIntent().getStringExtra("face");
        from = getIntent().getStringExtra("from");


        if (type.equals("0")) {
            /*蓝牙*/
            tvCollection.setVisibility(View.VISIBLE);
            tvCollection0.setVisibility(View.GONE);

            if (modes == 3) {
                toBlueTooth.setVisibility(View.GONE);
                toScan.setVisibility(View.VISIBLE);
            } else {
                toBlueTooth.setVisibility(View.GONE);
                toScan.setVisibility(View.GONE);
            }
        } else {
            /*识别*/
            tvCollection.setVisibility(View.GONE);
            tvCollection0.setVisibility(View.VISIBLE);

            if (modes == 3) {
                toBlueTooth.setVisibility(View.VISIBLE);
                toScan.setVisibility(View.GONE);
            } else {
                toBlueTooth.setVisibility(View.GONE);
                toScan.setVisibility(View.GONE);
            }
        }
        tvNext.setVisibility(View.GONE);
        tvOpen.setVisibility(View.GONE);
        if (from.equals("0")) {
            /*成卡开户*/
            if (face.equals("0")) {
                tvNext.setVisibility(View.VISIBLE);

                tvFace.setVisibility(View.GONE);

                imgIdBack.setImageResource(R.mipmap.second_1);

                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);

                mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
                mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
                requestCheck = (RequestCheck) getIntent().getExtras().getSerializable("requestCheck");
            } else if (face.equals("1")) {
                tvNext.setVisibility(View.GONE);

                tvFace.setVisibility(View.VISIBLE);

                imgIdBack.setImageResource(R.mipmap.second_1);


                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.VISIBLE);

                mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
                mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
                requestCheck = (RequestCheck) getIntent().getExtras().getSerializable("requestCheck");
            }
        } else if (from.equals("1")) {
            /*白卡开户*/
            if (face.equals("0")) {

                tvNext.setVisibility(View.VISIBLE);

                tvFace.setVisibility(View.GONE);

                imgIdBack.setImageResource(R.mipmap.second_1);


                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);

                mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
                mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
                requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
                iccid = getIntent().getStringExtra("iccid");
                phone = getIntent().getStringExtra("phone");
            } else if (face.equals("1")) {
                tvNext.setVisibility(View.GONE);

                tvFace.setVisibility(View.VISIBLE);

                imgIdBack.setImageResource(R.mipmap.second_1);

                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.VISIBLE);

                mPackage = (Package) getIntent().getExtras().getSerializable("mPackage");
                mPromotion = (Promotion) getIntent().getExtras().getSerializable("mPromotion");
                requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
                iccid = getIntent().getStringExtra("iccid");
                phone = getIntent().getStringExtra("phone");
            }
        } else if (from.equals("2")) {
            if (face.equals("0")) {
                tvBuy.setVisibility(View.VISIBLE);
                tvFace.setVisibility(View.GONE);

                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);

                requestLiangPay = (RequestLiangPay) getIntent().getSerializableExtra("requestLiangPay");
                agentsLiangNumber = (AgentsLiangNumber) getIntent().getSerializableExtra("agentsLiangNumber");
                requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
            } else if (face.equals("1")) {
                tvBuy.setVisibility(View.GONE);
                tvFace.setVisibility(View.VISIBLE);

                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);

                requestLiangPay = (RequestLiangPay) getIntent().getSerializableExtra("requestLiangPay");
                agentsLiangNumber = (AgentsLiangNumber) getIntent().getSerializableExtra("agentsLiangNumber");
                requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
            }

        } else if (from.equals("3")) {
            /*作废？*/
            if (face.equals("0")) {
                tvOpen.setVisibility(View.VISIBLE);
                tvFace.setVisibility(View.GONE);

                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);

                requestPreNumber = (RequestPreNumber) getIntent().getSerializableExtra("data");
            } else if (face.equals("1")) {
                tvOpen.setVisibility(View.GONE);
                tvFace.setVisibility(View.VISIBLE);

                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);
                requestPreNumber = (RequestPreNumber) getIntent().getSerializableExtra("data");
            }

        } else if (from.equals("4")) {

//            if (face.equals( "0" )) {
            face = "0";
            tvNext.setVisibility(View.VISIBLE);
            tvFace.setVisibility(View.GONE);

            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.GONE);
            layout5.setVisibility(View.GONE);
            layout6.setVisibility(View.GONE);

            orderNO = getIntent().getStringExtra("orderNO");
        } else if (from.equals("5")) {
            /*写卡激活*/
            number = getIntent().getStringExtra("number");
            preStore = getIntent().getStringExtra("preStore");
            detail = getIntent().getStringExtra("detail");

            Log.d("nnn", "getNumber:" + number);
            if (face.equals("0")) {
                tvNext.setVisibility(View.VISIBLE);

                tvFace.setVisibility(View.GONE);

                imgIdBack.setImageResource(R.mipmap.second_1);

                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.GONE);


            } else if (face.equals("1")) {
                tvNext.setVisibility(View.GONE);

                tvFace.setVisibility(View.VISIBLE);

                imgIdBack.setImageResource(R.mipmap.second_1);


                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.GONE);
                layout6.setVisibility(View.VISIBLE);


            }
        }


        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能


    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.i("test", "onHanlderSuccess: " + resultList.toString());
            if (resultList != null && resultList.size() > 0) {
                String path = resultList.get(0).getPhotoPath();
                //110拍照111相册
                Log.d("test", path);
                switch (flag) {//0front 1back 2last
                    case 0:
                        if (reqeustCode == 110) {
                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("photo1", path);
                            editor.commit();
                            file_zero = path;

//                            renameFile(path,);

                            disPlayImage0(path);
                        } else {
                            file_zero = path;
                            disPlayImage0(path);
                        }
                        break;
                    case 1:
                        if (reqeustCode == 110) {
                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("photo2", path);
                            editor.commit();
                            file_one = path;
                            disPlayImage(path);
                        } else {
                            file_one = path;
                            disPlayImage(path);
                        }
                        break;
                    case 21:
                        if (reqeustCode == 111) {
                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("photo3", path);
                            editor.commit();
                            file_three = path;
                            disPlayImage(path);
                        } else {
                            file_three = path;
                            disPlayImage(path);
                        }
                        break;
                    case 2:
                        switch (type) {
                            case "0"://蓝牙
                                if (reqeustCode == 110) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("photo3", path);
                                    editor.commit();
                                    file_two = path;
                                    disPlayImage(path);
                                } else {
                                    file_two = path;
                                    disPlayImage(path);
                                }
                                break;
                            case "1"://扫描
                                if (reqeustCode == 110) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("photo3", path);
                                    editor.commit();
                                    file_two = path;
                                    selectPath = path;
                                    disPlayImage(path);
                                } else {
//                                    file_two = path;
//                                    selectPath = path;
//                                    disPlayImage( path );

                                    file_two = path;
                                    selectPath = path;
                                    disPlayImage(path);
                                    SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
//                                    String reco = sharedPreferences.getString("reco", "");
//                                    selectPath = reco;
//                                    }
                                }


                                break;
                        }

                        break;

                }

                //if (reqeustCode == 110)
                // urlList.add(resultList.get(0).getPhotoPath());

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };


    public  void showCamera(Activity activity, File file, int requestCode) {
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

    public  void showCamera(int requestCode) {
        tempFile = null;
        CardOcrRecogConfigure.getInstance()
            .initLanguage(getApplicationContext())
            .setSaveCut(true)
            .setOpenIDCopyFuction(true)
            .setnMainId(kernal.idcard.camera.SharedPreferencesHelper.getInt(
                getApplicationContext(), "nMainId", 2))
            .setnSubID( kernal.idcard.camera.SharedPreferencesHelper.getInt(
                getApplicationContext(), "nSubID", 0))
            .setFlag(0)
            .setnCropType(0)
            .setSavePath(new DefaultPicSavePath(this,true));
        Intent intent=new Intent(this,CardsCameraActivity.class);
        startActivityForResult(intent, requestCode);
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Uri uri;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //    String authority = activity.getPackageName() + ".fileprovider"; //【清单文件中provider的authorities属性的值】
        //    Log.d("xxx", authority);
        //    Log.d("xxx", file.getName());
        //    uri = FileProvider.getUriForFile(activity, authority, file);
        //} else {
        //    uri = Uri.fromFile(file);
        //}
        //Log.i("bqt", "【uri】" + uri);//【content://{$authority}/files/bqt/temp】或【file:///storage/emulated/0/bqt/temp】
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //activity.startActivityForResult(intent, requestCode);
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            /*所有权限都被允许   初始化界面*/
            switch (clickView.getId()) {
                case R.id.imgIdFront:
                    DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                            fileDir = new File(fileDir, "bqt");
                            if (!fileDir.exists()) fileDir.mkdirs();
                            tempFile = new File(fileDir, "temp_front.jpg");
                            showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);

                        }
                    }, mOnHanlderResultCallback, readModesTwo);
                    break;
                case R.id.imgIdBack:
                    DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                            fileDir = new File(fileDir, "bqt");
                            if (!fileDir.exists()) fileDir.mkdirs();
                            tempFile = new File(fileDir, "temp_back.jpg");
                            showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);

                        }
                    }, mOnHanlderResultCallback);
                    break;
                case R.id.imgIdLast:
                    if (!Util.isFastDoubleClick()) {
                        switch (type) { //成卡开户
                            case "0":
//                                DialogManager.changeAvatar(MessageCollectionNewActivity.this, mOnHanlderResultCallback);
                                DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                                    @Override
                                    public void onSuccess() {
                                        File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                                        fileDir = new File(fileDir, "bqt");
                                        if (!fileDir.exists()) fileDir.mkdirs();
                                        tempFile = new File(fileDir, "temp_last.jpg");

                                        if (modes == 1 || toBlueTooth.getVisibility() == View.VISIBLE) {
                                            showCamera(REQUEST_CODE_CAMERA);
                                        } else {
//                                            showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);
                                            IDCardCamera.create(MessageCollectionNewActivity2.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                                        }
                                    }
                                }, mOnHanlderResultCallback);
                                break;
                            case "1":
                                bulmorcamera = 0;
                                DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                                    @Override
                                    public void onSuccess() {
                                        bulmorcamera = 1;


//                                        Intent intent = new Intent(MessageCollectionNewActivity2.this, CameraActivity.class);
////
//                                        intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
//                                                getApplicationContext(), "nMainId", 2));
//                                        intent.putExtra("devcode", devcode);
//                                        intent.putExtra("flag", 0);
//                                        startActivityForResult(intent, 10);
//                                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                                        showCamera(REQUEST_CODE_CAMERA);
                                        File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                                        fileDir = new File(fileDir, "bqt");
                                        if (!fileDir.exists()) fileDir.mkdirs();
                                        tempFile = new File(fileDir, "temp_last.jpg");

                                        if (modes == 1 || toBlueTooth.getVisibility() == View.VISIBLE) {
                                            showCamera(REQUEST_CODE_CAMERA);
                                        } else {
                                            showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);
                                        }

                                    }
                                }, mOnHanlderResultCallback);
                                break;
                        }
                    }

                    break;
                case R.id.imgIdBackLast:
                    DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                            fileDir = new File(fileDir, "bqt");
                            if (!fileDir.exists()) fileDir.mkdirs();
                            tempFile = new File(fileDir, "temp_idback.jpg");
//                            showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);
                            IDCardCamera.create(MessageCollectionNewActivity2.this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);

                        }
                    }, mOnHanlderResultCallback);
                    break;
            }


        } else {
            /*申请权限*/
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAMERA
            );
        }
    }

    @OnClick({R.id.imgIdFront, R.id.imgIdFrontRemove, R.id.imgIdBack,
        R.id.imgIdBackRemove, R.id.tvNext, R.id.imgIdLast,
        R.id.imgIdLasRemove, R.id.ll_zhifubao, R.id.ll_zhanghu,
        R.id.imgIdBackLast, R.id.imgIdBackLasRemove,
        R.id.tvBuy, R.id.tvOpen, R.id.toScan, R.id.toBlueTooth, R.id.tvFace})
    public void onClick(View view) {
        this.clickView = view;

        switch (view.getId()) {
            case R.id.imgIdFront:
                flag = 0;

                switch (state_zere) {
                    case 0:
                        requestPermission();
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgFront.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(MessageCollectionNewActivity2.this, file_zero, DisplayUtil.dp2px(
                            MessageCollectionNewActivity2.this, 60), DisplayUtil.dp2px(
                            MessageCollectionNewActivity2.this, 60), location[0], location[1]);

                        break;
                }
                break;
            case R.id.imgIdFrontRemove:
                flag = 0;
                showPic(null);
                imgFront.setImageResource(R.mipmap.third_one);
                break;
            case R.id.imgIdBack:
                flag = 1;
                switch (state_one) {
                    case 0:
                        requestPermission();
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgIdBack.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(MessageCollectionNewActivity2.this, file_one, DisplayUtil.dp2px(
                            MessageCollectionNewActivity2.this, 60), DisplayUtil.dp2px(
                            MessageCollectionNewActivity2.this, 60), location[0], location[1]);

                        break;
                }
                break;
            case R.id.imgIdBackRemove:
                flag = 1;
                showPic(null);
                imgIdBack.setImageResource(R.mipmap.second_1);

//                if (face.equals("0")) {
//                } else if (face.equals("1")) {
//                    imgIdBack.setImageResource(R.mipmap.second_2);
//                }

                break;
            case R.id.imgIdLast:
                flag = 2;
                switch (state_two) {
                    case 0:
                        requestPermission();
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgIdLast.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(MessageCollectionNewActivity2.this, file_two, DisplayUtil.dp2px(
                            MessageCollectionNewActivity2.this, 60), DisplayUtil.dp2px(
                            MessageCollectionNewActivity2.this, 60), location[0], location[1]);

                        break;
                }
                break;

            case R.id.imgIdLasRemove:
                flag = 2;
                etName.setText("");
                etId.setText("");
                etAddress.setText("");
                etRemark.setText("");
                showPic0(null);
                imgIdLast.setImageResource(R.mipmap.firstid);

                indextime = 0;

                break;
            case R.id.imgIdBackLast:
                flag = 21;
                switch (state_three) {
                    case 0:
                        requestPermission();
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgIdBackLast.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(MessageCollectionNewActivity2.this, file_three, DisplayUtil.dp2px(
                            MessageCollectionNewActivity2.this, 60), DisplayUtil.dp2px(
                            MessageCollectionNewActivity2.this, 60), location[0], location[1]);

                        break;
                }
                break;
            case R.id.imgIdBackLasRemove:
                flag = 21;
                showPic(null);
                imgIdBackLast.setImageResource(R.mipmap.idcardback);
                break;
            case R.id.tvNext:
                if (!Util.isFastDoubleClick()) {
                    final String strName = etName.getText().toString();
//                    final String strId="330326111222331454";
                    final String strId = etId.getText().toString();
                    final String strAddress = etAddress.getText().toString();
                    final String strRemark = etRemark.getText().toString();

                    if (bitmap_zero == null || bitmap_one == null || bitmap_two == null || strName.equals("") || strId.equals("") || strAddress.equals("")) {
                        Util.createToast(MessageCollectionNewActivity2.this, "请将信息填写完整");
                    } else if (strName.length() > 30) {
                        Util.createToast(MessageCollectionNewActivity2.this, "用户名过长!");
                    } else if (strId.length() != 15 && strId.length() != 18) {
                        Util.createToast(MessageCollectionNewActivity2.this, "请输入正确的身份证号!");
                    } else {

                        if (from.equals("4")) {
                            dialog.getTvTitle().setText("正在上传图片");
                            dialog.show();
                            HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
                            final PostPictureUpload postPictureUpload = new PostPictureUpload();
                            postPictureUpload.setSession_token(Util.getLocalAdmin(
                                MessageCollectionNewActivity2.this)[0]);
                            postPictureUpload.setType("0");
                            Photo photo = new Photo();
                            photo.setPhoto1(BitmapUtil.bitmapToBase64(bitmap_one));
                            photo.setPhoto2(BitmapUtil.bitmapToBase64(bitmap_zero));
                            photo.setPhoto3(BitmapUtil.bitmapToBase64(bitmap_two));
                            httpPost.setPhoto(photo);
                            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                            httpPost.setParameter(postPictureUpload);
                            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));


                            new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(
                                MessageCollectionNewActivity2.this, dialog, new SuccessValue<RequestPictureUpload>() {
                                @Override
                                public void OnSuccess(RequestPictureUpload value) {
                                    if (urlList != null && urlList.size() > 0) {
                                        for (int i = 0; i < urlList.size(); i++)
                                            deletePic(MessageCollectionNewActivity2.this, urlList.get(i));
                                    }
                                    HttpPost<PostRegistration> httpPost = new HttpPost<>();
                                    PostRegistration postRegistration = new PostRegistration();
                                    postRegistration.setAddress(strAddress);
                                    postRegistration.setCertificatesNo(strId);
                                    postRegistration.setCustomerName(strName);
                                    postRegistration.setMemo4(value.getPhoto3());
                                    postRegistration.setPhotoFront(value.getPhoto1());
                                    postRegistration.setPhotoBack(value.getPhoto2());
                                    postRegistration.setOrderNo(orderNO);
                                    postRegistration.setSession_token(Util.getLocalAdmin(
                                        MessageCollectionNewActivity2.this)[0]);
                                    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                                    httpPost.setParameter(postRegistration);
                                    httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postRegistration) + BaseCom.APP_PWD));
                                    new CardHttp().liangRegistration(httpPost, new Subscriber<HttpRequest>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(HttpRequest httpRequest) {
                                            Util.createToast(MessageCollectionNewActivity2.this, httpRequest.getMes());
                                            if (httpRequest.getCode() == BaseCom.NORMAL) {
                                                AppManager.getAppManager().finishActivity();
                                            }
                                        }
                                    });

                                }
                            }), httpPost);


                        } else {

                            Intent intent = new Intent(this, AccountClosingActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putStringArrayListExtra("urlList", urlList);
                            bundle.putSerializable("mPackage", mPackage);
                            bundle.putSerializable("mPromotion", mPromotion);

                            if (from.equals("0")) {

                                bundle.putSerializable("requestCheck", requestCheck);
                                intent.putExtras(bundle);
                                intent.putExtra("from", "0");
                                intent.putExtra("type", type);
                            } else if (from.equals("1")) {
                                intent.putExtra("phone", phone);
                                intent.putExtra("iccid", iccid);
                                intent.putExtra("from", "1");
                                bundle.putSerializable("requestImsi", requestImsi);
                                intent.putExtras(bundle);


                            } else if (from.equals("5")) {
                                intent.putExtra("from", "2");
                                Log.d("nnn", "postNumber:" + number);
                                intent.putExtra("number", number);
                                intent.putExtra("type", type);
                            }
                            intent.putExtra("face", "0");
                            intent.putExtra("name", strName);
                            intent.putExtra("cardId", strId);
                            intent.putExtra("address", strAddress);
                            intent.putExtra("remark", strRemark);
                            BaseCom.photoOne = bitmap_zero;
                            BaseCom.photoTwo = bitmap_one;
                            BaseCom.photoThree = bitmap_two;
                            BaseCom.photoFour = bitmap_three;
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        }


                    }
                }
                break;
            case R.id.tvBuy:
                if (!Util.isFastDoubleClick()) {
                    submitBuy();
                }
                break;
            case R.id.tvOpen:
                if (!Util.isFastDoubleClick()) {
                    final String strOpenName = etName.getText().toString();
                    final String strOpenId = etId.getText().toString();
                    final String strOpenAddress = etAddress.getText().toString();
                    final String strOpenRemark = etRemark.getText().toString();
                    if (bitmap_zero == null || bitmap_one == null || bitmap_two == null || strOpenName.equals("") || strOpenId.equals("") || strOpenAddress.equals("")) {
                        Util.createToast(MessageCollectionNewActivity2.this, "请将信息填写完整");
                    } else if (strOpenName.length() > 30) {
                        Util.createToast(MessageCollectionNewActivity2.this, "用户名过长!");
                    } else if (strOpenId.length() != 15 && strOpenId.length() != 18) {
                        Util.createToast(MessageCollectionNewActivity2.this, "请输入正确的身份证号!");
                    } else {

                        dialog.getTvTitle().setText("正在上传图片");
                        dialog.show();
                        final HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
                        PostPictureUpload postPictureUpload = new PostPictureUpload();
                        postPictureUpload.setSession_token(Util.getLocalAdmin(
                            MessageCollectionNewActivity2.this)[0]);
                        postPictureUpload.setType("1");
                        Photo photo = new Photo();
                        photo.setPhoto1(BitmapUtil.bitmapToBase64(bitmap_zero));/*正面*/
                        photo.setPhoto2(BitmapUtil.bitmapToBase64(bitmap_one));/*背面*/
                        photo.setPhoto3(BitmapUtil.bitmapToBase64(bitmap_two));/*扫描面*/
                        httpPost.setPhoto(photo);
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postPictureUpload);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));

                        new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(
                            MessageCollectionNewActivity2.this, dialog, new SuccessValue<RequestPictureUpload>() {
                            @Override
                            public void OnSuccess(RequestPictureUpload value) {
                                if (urlList != null && urlList.size() > 0) {
                                    for (int i = 0; i < urlList.size(); i++)
                                        deletePic(MessageCollectionNewActivity2.this, urlList.get(i));
                                }
                                Log.d("ccc", value.getPhoto1());
                                Log.d("ccc", value.getPhoto2());
                                Log.d("ccc", value.getPhoto3());
//                            dialog.getTvTitle().setText("正在提交");
//                            dialog.show();

                                PostOpenInformation postOpenInformation = new PostOpenInformation();
                                postOpenInformation.setSession_token(Util.getLocalAdmin(
                                    MessageCollectionNewActivity2.this)[0]);
                                postOpenInformation.setAddress(strOpenAddress);
                                postOpenInformation.setSimId(requestPreNumber.getSimId());
                                postOpenInformation.setCardType("ESIM");
                                postOpenInformation.setCertificatesNo(strOpenId);
                                postOpenInformation.setCertificatesType("Idcode");
                                postOpenInformation.setIccid(requestPreNumber.getIccid());
                                postOpenInformation.setImsi(requestPreNumber.getImsi());
                                postOpenInformation.setNumber(requestPreNumber.getNumber());
                                postOpenInformation.setCustomerName(strOpenName);
                                postOpenInformation.setOrderAmount(String.valueOf(requestPreNumber.getAmount()));
                                postOpenInformation.setPackageId(requestPreNumber.getPackageId());
                                postOpenInformation.setPromotionId(requestPreNumber.getPromotionId());
                                postOpenInformation.setPayAmount(String.valueOf(requestPreNumber.getAmount()));

                                postOpenInformation.setPhoto1(value.getPhoto1());
                                postOpenInformation.setPhoto2(value.getPhoto2());
                                postOpenInformation.setPhoto3(value.getPhoto3());

                                final HttpPost<PostOpenInformation> httpPost = new HttpPost<>();
                                httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                                httpPost.setParameter(postOpenInformation);
                                httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpenInformation) + BaseCom.APP_PWD));
                                new CardHttp().openInformation(httpPost, new Subscriber<HttpRequest>() {
                                    @Override
                                    public void onCompleted() {


                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(HttpRequest httpRequest) {
                                        Toast.makeText(MessageCollectionNewActivity2.this, httpRequest.getMes(), Toast.LENGTH_SHORT).show();
                                        if (httpRequest.getCode() == BaseCom.NORMAL) {
                                            Intent intent = new Intent(
                                                MessageCollectionNewActivity2.this, DialogActivity.class);
                                            intent.putExtra("from", from);
                                            startActivityForResult(intent, OPENRESULT);

                                        } else if (httpRequest.getCode() == BaseCom.LOSELOG || httpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                                            Util.gotoActy(MessageCollectionNewActivity2.this, LoginActivity.class);
                                        } else {
                                            /*是否重新提交*/

                                        }
                                    }
                                });


                            }
                        }), httpPost);


                    }
                }
                break;

            case R.id.toBlueTooth:
                type = "0";
                tvCollection.setVisibility(View.VISIBLE);
                tvCollection0.setVisibility(View.GONE);
                toBlueTooth.setVisibility(View.GONE);
                toScan.setVisibility(View.VISIBLE);
                etName.setText("");
                etId.setText("");
                etAddress.setText("");

                flag = 2;
                showPic0(null);

                imgIdLast.setImageResource(R.mipmap.firstid);
                imgIdLasRemove.setVisibility(View.GONE);
                indextime = 0;


                break;

            case R.id.toScan:
                type = "1";
                tvCollection.setVisibility(View.GONE);
                tvCollection0.setVisibility(View.VISIBLE);
                toBlueTooth.setVisibility(View.VISIBLE);
                toScan.setVisibility(View.GONE);
                etName.setText("");
                etId.setText("");
                etAddress.setText("");
                flag = 2;
                showPic0(null);

                imgIdLast.setImageResource(R.mipmap.firstid);
                imgIdLasRemove.setVisibility(View.GONE);

                indextime = 0;


                break;


            case R.id.tvFace:
                if (from.equals("0") || from.equals("1") || from.equals("5")) {
                    if (!Util.isFastDoubleClick()) {
                        final String strName = etName.getText().toString();
//                    final String strId="330326111222331454";
                        final String strId = etId.getText().toString();
                        final String strAddress = etAddress.getText().toString();
                        final String strRemark = etRemark.getText().toString();

                        if (bitmap_zero == null || bitmap_one == null || bitmap_two == null || bitmap_three == null || strName.equals("") || strId.equals("") || strAddress.equals("")) {
                            Util.createToast(MessageCollectionNewActivity2.this, "请将信息填写完整");
                        } else if (strName.length() > 30) {
                            Util.createToast(MessageCollectionNewActivity2.this, "用户名过长!");
                        } else if (strId.length() != 15 && strId.length() != 18) {
                            Util.createToast(MessageCollectionNewActivity2.this, "请输入正确的身份证号!");
                        } else {
                            Intent intent = new Intent(this, FaceRecordingActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putStringArrayListExtra("urlList", urlList);
                            bundle.putSerializable("mPackage", mPackage);
                            bundle.putSerializable("mPromotion", mPromotion);

                            if (from.equals("0")) {

                                bundle.putSerializable("requestCheck", requestCheck);
                                intent.putExtras(bundle);
                                intent.putExtra("from", "0");
                                intent.putExtra("type", type);
                            } else if (from.equals("5")) {
                                intent.putExtra("number", number);
                                intent.putExtra("preStore", preStore);
                                intent.putExtra("detail", detail);
                                intent.putExtra("from", "5");
                                Log.d("ccc", "messagePost:" + type);
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
                            BaseCom.photoOne = bitmap_zero;
                            BaseCom.photoTwo = bitmap_one;
                            BaseCom.photoThree = bitmap_two;
                            BaseCom.photoFour = bitmap_three;

                            startActivity(intent);
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                        }
                    }
                } else if (from.equals("2") || from.equals("3")) {
                    if (!Util.isFastDoubleClick()) {
                        submitBuy0();
                    }
                }
                break;

        }
    }

    private void submitBuy() {
        final String strBuyName = etName.getText().toString();
        final String strBuyId = etId.getText().toString();
        final String strBuyAddress = etAddress.getText().toString();
        final String strBuyRemark = etRemark.getText().toString();

        if (bitmap_zero == null || bitmap_one == null || bitmap_two == null || strBuyName.equals("") || strBuyId.equals("") || strBuyAddress.equals("")) {
            Util.createToast(MessageCollectionNewActivity2.this, "请将信息填写完整");
        } else if (strBuyName.length() > 30) {
            Util.createToast(MessageCollectionNewActivity2.this, "用户名过长!");
        } else if (strBuyId.length() != 15 && strBuyId.length() != 18) {
            Util.createToast(MessageCollectionNewActivity2.this, "请输入正确的身份证号!");
        } else {
            dialog.getTvTitle().setText("正在上传图片");
            dialog.show();
            final HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
            PostPictureUpload postPictureUpload = new PostPictureUpload();
            postPictureUpload.setSession_token(Util.getLocalAdmin(MessageCollectionNewActivity2.this)[0]);
            postPictureUpload.setType("5");
            Photo photo = new Photo();
            photo.setPhoto1(BitmapUtil.bitmapToBase64(bitmap_zero));/*正面*/
            photo.setPhoto2(BitmapUtil.bitmapToBase64(bitmap_one));/*背面*/
            photo.setPhoto3(BitmapUtil.bitmapToBase64(bitmap_two));/*扫描面*/
            httpPost.setPhoto(photo);
            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost.setParameter(postPictureUpload);
            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));

            new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(
                MessageCollectionNewActivity2.this, dialog, new SuccessValue<RequestPictureUpload>() {
                @Override
                public void OnSuccess(RequestPictureUpload value) {
                    Log.i("photo",value.getPhoto1());
                    if (urlList != null && urlList.size() > 0) {
                        for (int i = 0; i < urlList.size(); i++) {
                            deletePic(MessageCollectionNewActivity2.this, urlList.get(i));

                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", MODE_PRIVATE);
                            String uri0 = sharedPreferences.getString("pic0", "");
                            String uri1 = sharedPreferences.getString("pic1", "");
                            String photo1 = sharedPreferences.getString("photo1", "");
                            String photo2 = sharedPreferences.getString("photo2", "");
                            String photo3 = sharedPreferences.getString("photo3", "");
                            String witonePic = sharedPreferences.getString("witonePic", "");
                            String witonePicTmp = sharedPreferences.getString("witonePicTmp", "");

                            deletePic(MessageCollectionNewActivity2.this, photo1);
                            deletePic(MessageCollectionNewActivity2.this, photo2);
                            deletePic(MessageCollectionNewActivity2.this, photo3);
                            deletePic(MessageCollectionNewActivity2.this, uri0);
                            deletePic(MessageCollectionNewActivity2.this, uri1);
                            deletePic(MessageCollectionNewActivity2.this, witonePic);
                            deletePic(MessageCollectionNewActivity2.this, witonePicTmp);
                        }
                    }
                    Log.d("ccc", value.getPhoto1());
                    Log.d("ccc", value.getPhoto2());
                    Log.d("ccc", value.getPhoto3());
                    dialog.getTvTitle().setText("正在提交");
                    dialog.show();
                    PostSetOpen postSetOpen = new PostSetOpen();
                    postSetOpen.setSession_token(Util.getLocalAdmin(MessageCollectionNewActivity2.this)[0]);
                    postSetOpen.setNumber(agentsLiangNumber.getNumber());
                    postSetOpen.setOrderNo(requestLiangPay.getOrderNo());
                    switch (type) {
                        case "0":
//                            postSetOpen.setAuthenticationType("读取");
                            if (face.equals("1")) {
                                postSetOpen.setAuthenticationType("App人脸识别");
                            } else {
                                postSetOpen.setAuthenticationType("App识别仪");
                            }
                            break;
                        case "1":
                            if (face.equals("1")) {
                                postSetOpen.setAuthenticationType("App人脸识别");
                            } else {
                                postSetOpen.setAuthenticationType("App扫描");
                            }
//                            postSetOpen.setAuthenticationType("手工");
                            break;
                    }
                    postSetOpen.setCustomerName(strBuyName);
                    postSetOpen.setCertificatesType("Idcode");
                    postSetOpen.setCertificatesNo(strBuyId);
                    postSetOpen.setAddress(strBuyAddress);
                    postSetOpen.setDescription(strBuyRemark);
                    postSetOpen.setCardType("ESIM");
                    postSetOpen.setSimId(requestImsi.getSimId());
                    postSetOpen.setIccid(requestImsi.getIccid());
                    postSetOpen.setImsi(requestImsi.getImsi());
                    postSetOpen.setPackageId(agentsLiangNumber.getSelectPackage().getId());
                    postSetOpen.setPromotionsId(agentsLiangNumber.getSelectPromotion().getId());
                    postSetOpen.setOrderAmount(agentsLiangNumber.getPrestore());
                    postSetOpen.setPayAmount(agentsLiangNumber.getPrestore());
                    postSetOpen.setPhotoFront(value.getPhoto2());
                    postSetOpen.setPhotoBack(value.getPhoto1());
                    postSetOpen.setMemo4(value.getPhoto3());
                    postSetOpen.setPayMethod(BaseCom.payMethod);
                    HttpPost<PostSetOpen> httpPost1 = new HttpPost<PostSetOpen>();
                    httpPost1.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                    httpPost1.setParameter(postSetOpen);
                    httpPost1.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postSetOpen) + BaseCom.APP_PWD));
                    new CardHttp().liangSetOpen(httpPost1, new Subscriber<HttpRequest<RequestSetOpen>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onNext(final HttpRequest<RequestSetOpen> requestSetOpenHttpRequest) {
                            dialog.dismiss();
                            Util.createToast(MessageCollectionNewActivity2.this, requestSetOpenHttpRequest.getMes());
                            if (requestSetOpenHttpRequest.getCode() == BaseCom.NORMAL) {
                                // request = requestSetOpenHttpRequest.getData().getRequest();
                                Intent intent = new Intent(MessageCollectionNewActivity2.this, DialogActivity.class);
                                intent.putExtra("from", from);
                                startActivityForResult(intent, BUYRESULT);
                            } else if (requestSetOpenHttpRequest.getCode() == BaseCom.LOSELOG || requestSetOpenHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                                Util.gotoActy(MessageCollectionNewActivity2.this, LoginActivity.class);
                            } else {
                                /*是否重新提交*/

                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                    MessageCollectionNewActivity2.this);

                                builder.setCancelable(false);
                                builder.setMessage(requestSetOpenHttpRequest.getMes() + "\n" + "是否重新提交该订单?");
                                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();


                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(1500);
                                                    Message message = new Message();
                                                    message.what = SUBMIT_AGAIN;
                                                    handler.sendMessage(message); //告诉主线程执行任务

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
                                        AppManager.getAppManager().finishActivity(HuajiCuteDetailActivity.class);
                                    }
                                });
                                builder.create().show();


                            }
                        }
                    });

                }
            }), httpPost);


        }

    }


    private void submitBuy0() {
        final String strBuyName = etName.getText().toString();
        final String strBuyId = etId.getText().toString();
        final String strBuyAddress = etAddress.getText().toString();
        final String strBuyRemark = etRemark.getText().toString();

        if (bitmap_zero == null || bitmap_one == null || bitmap_two == null || strBuyName.equals("") || strBuyId.equals("") || strBuyAddress.equals("")) {
            Util.createToast(MessageCollectionNewActivity2.this, "请将信息填写完整");
        } else if (strBuyName.length() > 30) {
            Util.createToast(MessageCollectionNewActivity2.this, "用户名过长!");
        } else if (strBuyId.length() != 15 && strBuyId.length() != 18) {
            Util.createToast(MessageCollectionNewActivity2.this, "请输入正确的身份证号!");
        } else {
            dialog.getTvTitle().setText("正在上传图片");
            dialog.show();
            Photo photo = new Photo();
            photo.setPhoto1(BitmapUtil.bitmapToBase64(bitmap_zero));/*正面*/
            photo.setPhoto2(BitmapUtil.bitmapToBase64(bitmap_one));/*背面*/
            photo.setPhoto3(BitmapUtil.bitmapToBase64(bitmap_two));/*扫描面*/
            photo.setPhoto7(BitmapUtil.bitmapToBase64(bitmap_three));//身份证背面

            BaseCom.photoOne = bitmap_zero;
            BaseCom.photoThree = bitmap_two;
            BaseCom.photoTwo = bitmap_one;
            BaseCom.photoFour = bitmap_three;

            Intent intent = new Intent(MessageCollectionNewActivity2.this, FaceRecordingActivity.class);
            intent.putExtra("strBuyName", strBuyName);
            intent.putExtra("strBuyId", strBuyId);
            intent.putExtra("strBuyAddress", strBuyAddress);
            intent.putExtra("strBuyRemark", strBuyRemark);
            intent.putExtra("getSimId", requestImsi.getSimId());
            intent.putExtra("getIccid", requestImsi.getIccid());
            intent.putExtra("getImsi", requestImsi.getImsi());
            intent.putExtra("getSelectPackageId", agentsLiangNumber.getSelectPackage().getId());
            intent.putExtra("getSelectPromotionId", agentsLiangNumber.getSelectPromotion().getId());
            intent.putExtra("getPrestore", agentsLiangNumber.getPrestore());
            intent.putExtra("getNumber", agentsLiangNumber.getNumber());
            intent.putExtra("orderNo", requestLiangPay.getOrderNo());

            intent.putExtra("from", from);
            intent.putExtra("face", "1");
            dialog.dismiss();

            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {
            case CAMERA: {
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
//                    gotoPhoto();
                    switch (clickView.getId()) {
                        case R.id.imgIdFront:
                            DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                                @Override
                                public void onSuccess() {
                                    File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                                    fileDir = new File(fileDir, "bqt");
                                    if (!fileDir.exists()) fileDir.mkdirs();
                                    tempFile = new File(fileDir, "temp_front.jpg");
                                    showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);

                                }
                            }, mOnHanlderResultCallback);
                            break;
                        case R.id.imgIdBack:
                            DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                                @Override
                                public void onSuccess() {
                                    File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                                    fileDir = new File(fileDir, "bqt");
                                    if (!fileDir.exists()) fileDir.mkdirs();
                                    tempFile = new File(fileDir, "temp_back.jpg");
                                    showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);

                                }
                            }, mOnHanlderResultCallback);
                            break;
                        case R.id.imgIdLast:
                            switch (type) {
                                case "0":
                                    DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                                        @Override
                                        public void onSuccess() {
                                            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                                            fileDir = new File(fileDir, "bqt");
                                            if (!fileDir.exists()) fileDir.mkdirs();
                                            tempFile = new File(fileDir, "temp_last.jpg");
//                                            showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);
                                            IDCardCamera.create(MessageCollectionNewActivity2.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                                        }
                                    }, mOnHanlderResultCallback);
                                    break;
                                case "1":
                                    bulmorcamera = 0;
                                    DialogManager.changeAvatar(MessageCollectionNewActivity2.this, new SuccessNull() {
                                        @Override
                                        public void onSuccess() {
                                            bulmorcamera = 1;

//                                            Intent intent = new Intent(
//                                                MessageCollectionNewActivity2.this, CameraActivity.class);
////
//                                            intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
//                                                    getApplicationContext(), "nMainId", 2));
//                                            intent.putExtra("devcode", devcode);
//                                            intent.putExtra("flag", 0);
//                                            startActivityForResult(intent, 10);
                                            showCamera(MessageCollectionNewActivity2.this, tempFile, REQUEST_CODE_CAMERA);
                                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                        }
                                    }, mOnHanlderResultCallback);
                                    break;
                            }

                            break;
                    }


                } else {
                    //申请失败，可以继续向用户解释。
                    Toast.makeText(MessageCollectionNewActivity2.this, "请赋予权限", Toast.LENGTH_SHORT).show();
                }
                return;


            }

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

    //水印
    private Bitmap add(Bitmap bm) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        //String string = "仅用于话机世界开户使用\n" + format.format(Calendar.getInstance().getTime()) +
        //         "\n" + 123456789;
//        Bitmap bitmap = Bitmap.createBitmap(600, 300, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        //canvas.rotate(15);
//        Paint paint = new Paint();
//        paint.setTextSize(15);
//        paint.setTextAlign(Paint.Align.LEFT);
//        paint.setColor(Color.GRAY);
//        Paint.FontMetrics fm = paint.getFontMetrics();
//        canvas.drawText(string, 0, 145+ fm.top - fm.ascent, paint);
//        canvas.save();
//
//        return BitmapUtil.drawTextToLeftTop(this,bm,string,25,Color.GRAY,0,0);

//
//        List<String> list = new ArrayList<>();
//        list.add("仅用于话机世界开户使用");
//        list.add(format.format(Calendar.getInstance().getTime()));
//        list.add("123456789");
//        WaterMarkBg waterMarkBg = new WaterMarkBg(this,list,45,35);
        Bitmap bitmap = Bitmap.createBitmap(550, 550, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(39);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(40);
        paint.setShadowLayer(2, 2, 2, Color.BLACK);
        Path path = new Path();
        path.moveTo(0, 380);
        path.lineTo(260, 0);
        canvas.drawTextOnPath("   话机实名认证专用", path, 0, 30, paint);
        Path path1 = new Path();
        path1.moveTo(0, 460);
        path1.lineTo(260, 80);
        canvas.drawTextOnPath("        " + format.format(Calendar.getInstance().getTime()), path1, 0, 30, paint);
        Path path2 = new Path();
        path2.moveTo(0, 540);
        path2.lineTo(260, 160);
        SharedPreferences sharedPreferences = getSharedPreferences(BaseCom.ADMIN, MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "");
        canvas.drawTextOnPath("                  " + user, path2, 0, 30, paint);


        int min = bm.getWidth() < bm.getHeight() ? bm.getWidth() : bm.getHeight();
        float scale = (float) min / 700;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        return BitmapUtil.createWaterMaskLeftTop(this, bm, bitmap, 0, 0);
    }


    private void showPic(String capturePath) {

        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(getResources(), capturePath, DisplayUtil.dp2px(
            MessageCollectionNewActivity2.this, 200), DisplayUtil.dp2px(
            MessageCollectionNewActivity2.this, 200));

        if (capturePath != null) {
            urlList.add(capturePath);//
            if (capturePath.contains("_tmp.jpg") || capturePath.contains("_tmp.png")) {
                bitmap = add(bitmap);
            }
            File file = new File(capturePath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        switch (flag) {
            case 0:
                imgFront.setImageBitmap(bitmap);
                if (capturePath != null) {
                    bitmap_zero = bitmap;
                    state_zere = 1;
                    imgFrontRemove.setVisibility(View.VISIBLE);
                } else {
                    state_zere = 0;
                    bitmap_zero = null;
                    imgFrontRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                imgIdBack.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_one = 1;
                    bitmap_one = bitmap;
                    imgBackRemove.setVisibility(View.VISIBLE);
                } else {
                    state_one = 0;
                    bitmap_one = null;
                    imgBackRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                imgIdLast.setImageBitmap(bitmap);
                if (capturePath != null) {
                    // urlList.add(capturePath);
                    state_two = 1;
                    bitmap_two = bitmap;
                    imgIdLasRemove.setVisibility(View.VISIBLE);
                    if (type.equals("1") && bulmorcamera == 0) {
                        Message message = new Message();
                        message.what = STARTSCAN;
                        handler.sendMessageDelayed(message, 500);


                    }
                } else {
                    state_two = 0;
                    bitmap_two = null;
                    imgIdLasRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 21:
                imgIdBackLast.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_three = 1;
                    bitmap_three = bitmap;
                    imgIdBackLasRemove.setVisibility(View.VISIBLE);
                } else {
                    state_three = 0;
                    bitmap_three = null;
                    imgIdBackLasRemove.setVisibility(View.INVISIBLE);
                }
                break;

        }
    }

    private void disPlayImage(String capturePath) {
        if (capturePath.contains("_tmp.jpg") || capturePath.contains("_tmp.png")) {
            showPic(capturePath);
        } else {
            String mUri = capturePath;
//            String nUri=capturePath;
//            nUri=nUri.substring( nUri.indexOf( "." ));
//            mUri=mUri.substring( mUri.lastIndexOf( "." ));
//            String[] filename= capturePath.split( "." );
//            handleUri = filename [0]+"_tmp"+filename[1];
            if (!TextUtils.isEmpty(capturePath) && mUri.lastIndexOf(".") > 0) {
                handleUri = mUri.substring(0, mUri.lastIndexOf(".")) + "_tmp" + mUri.substring(mUri.lastIndexOf("."));
            } else {
                if(!mUri.isEmpty()) ToastUtils.s(this, "图片异常：" + mUri);
                handleUri = "";
            }

            if (handleUri.equals("")) {
            } else {
                copyFile(capturePath, handleUri, handler);
                switch (flag) {
                    case 0:
                        file_zero = handleUri;
                        break;
                    case 1:
                        file_one = handleUri;
                        break;
                    case 2:
                        file_two = handleUri;
                        SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("reco", handleUri);
                        editor.commit();
                        break;
                    case 21:
                        file_three = handleUri;
                        SharedPreferences sharedPreferences2 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putString("reco", handleUri);
                        editor2.commit();
                        break;
                }
                showPic(handleUri);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    if (mac[1].equals("")) {
                        /*开启蓝牙成功,若本地未存蓝牙地址,跳转设备选择界面*/
                        Intent serverIntent2 = new Intent(MessageCollectionNewActivity2.this, BlueToothActivity.class);
                        startActivityForResult(serverIntent2, SETTING_BT);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                } else if (resultCode == RESULT_CANCELED&&flag!=21) {
                    Util.createToast(MessageCollectionNewActivity2.this, "请开启蓝牙!");
                } else if(resultCode == IDCardCamera.RESULT_CODE){ //身份证正面照(集成身份证国徽框后)
                    file_three = IDCardCamera.getImagePath(data);
                    SharedPreferences sharedPreferences3 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                    editor3.putString("photo3", file_three);
                    editor3.commit();
                    disPlayImage(file_three);
                }
                break;
            case SETTING_BT:
                if (data != null) {
                    String address = data.getStringExtra("device_address");
                    String name = data.getStringExtra("device_name");
                    saveAddressmac(name, address);
                }
                break;
            case 10:
                if (data != null) {
                    Log.d("aaa", "3");
                    spit(data.getStringExtra("recogResult"));

                    etName.setText(splite_Result[0].substring(3));
                    etAddress.setText(splite_Result[4].substring(3));
                    etId.setText(splite_Result[5].substring(7));
                    flag = 2;
                    String fullPagePath = data.getStringExtra("picPathString1");
                    file_two = fullPagePath;
                    disPlayImage(fullPagePath);
                }
                break;
            case 11:
                if (data != null) {
                    Log.d("aaa", "3");
                    spit(data.getStringExtra("recogResult"));

                    //etName.setText(splite_Result[0].substring(3));
                    //etAddress.setText(splite_Result[4].substring(3));
                    //etId.setText(splite_Result[5].substring(7));
                    flag = 3;
                    String fullPagePath = data.getStringExtra("picPathString1");
                    file_three = fullPagePath;
                    disPlayImage(fullPagePath);
                }
                break;
            case 200:
                break;
            case BUYRESULT:
                AppManager.getAppManager().finishActivityBesides(MainNewActivity.class);
//                Util.gotoActy(MessageCollectionNewActivity.this, MainNewActivity.class);
//                AppManager.getAppManager().finishActivity();
//                AppManager.getAppManager().finishActivity(HuajiSelectActivity.class);
//                AppManager.getAppManager().finishActivity(HuajiCuteDetailActivity.class);
                break;
            case OPENRESULT:
                AppManager.getAppManager().finishActivityBesides(MainNewActivity.class);
//                Util.gotoActy(MessageCollectionNewActivity.this, MainNewActivity.class);
//                AppManager.getAppManager().finishActivity();
//                AppManager.getAppManager().finishActivity(HuajiSelectActivity.class);
//                AppManager.getAppManager().finishActivity(QudaoWhitePhoneDetailActivity.class);
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = null;
                    if (data != null) {
                        bundle=data.getBundleExtra("resultbundle");
                    }

                    String path = "";
                    if (tempFile != null) {
                        path = tempFile.getAbsolutePath();
                        Log.d("bqt", tempFile.getAbsolutePath());//【data/user/0/包名/files/bqt/temp】或【/storage/emulated/0/bqt/temp】
                    }
                    switch (flag) {//0front 1back 2last
                        case 0: //免冠照片
                            SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("photo1", path);
                            editor.commit();
                            file_zero = path;
                            disPlayImage(path);

//
//                            if (reqeustCode == 110) {
//
//
//                                disPlayImage0(path);
//                            } else {
//                                file_zero = path;
//                                disPlayImage0(path);
//                            }
                            break;
                        case 21: //身份证背面
                            SharedPreferences sharedPreferences3 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                            editor3.putString("photo3", path);
                            editor3.commit();
                            file_three = path;
                            disPlayImage(path);
                            break;
                        case 1://身份证正面照+卡板号码照片
                            SharedPreferences sharedPreferences1 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.putString("photo2", path);
                            editor1.commit();
                            file_one = path;
                            disPlayImage(path);
//                            if (reqeustCode == 110) {
//
//                            } else {
//                                file_one = path;
//                                disPlayImage(path);
//                            }
                            break;
                        case 2://身份证正面照(集成身份证人像框前)
                            switch (type) {
                                case "0"://蓝牙
                                    SharedPreferences sharedPreferences2 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                    editor2.putString("photo3", path);
                                    editor2.commit();
                                    file_two = path;
                                    disPlayImage(path);
//                                    if (reqeustCode == 110) {
//
//                                    } else {
//                                        file_two = path;
//                                        disPlayImage(path);
//                                    }
                                    break;
                                case "1"://扫描
                                    if (requestCode == 110) {

                                    }
                                    if (bundle != null) {
                                        ResultMessage resultMessage=(ResultMessage) bundle.getSerializable("resultMessage");

                                        String[] picPath=bundle.getStringArray("picpath");
                                        //数据的封装
                                        String result=ManageIDCardRecogResult.managerSucessRecogResult(resultMessage,getApplicationContext());
                                        spit(result);
                                        if (splite_Result == null || splite_Result.length < 7) {
                                            ToastUtils.s(this, "证件识别错误");
                                            return;
                                        }
                                        file_two = picPath[0];
                                        selectPath = picPath[0];
                                        etName.setText(splite_Result[1].substring(3));
                                        etAddress.setText(splite_Result[5].substring(3));
                                        etId.setText(splite_Result[6].substring(7));
                                        disPlayImage(picPath[1]);
                                    } else {
                                        String error=data.getStringExtra("error");
                                        ToastUtils.s(this, error);

                                    }

//                                    if (reqeustCode == 110) {
//
//                                    } else {
//                                        file_two = path;
//                                        selectPath = path;
//                                        disPlayImage(path);
//                                        SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
//                                        String reco = sharedPreferences.getString("reco", "");
//                                        selectPath = reco;
////                                    }
//                                    }


                                    break;
                            }

                            break;

                    }

                }
                break;
            case IDCardCamera.TYPE_IDCARD_FRONT: //身份证正面照(集成身份证人像框后)
                String path = IDCardCamera.getImagePath(data);
                SharedPreferences sharedPreferences2 = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putString("photo3", path);
                editor2.commit();
                file_two = path;
                disPlayImage(path);
                break;

        }


    }

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

    // 识别验证

    public ServiceConnection recogConn = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            recogBindViewer = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {

            recogBindViewer = (RecogService.recogBinder) service;

            RecogParameterMessage rpm = new RecogParameterMessage();
            rpm.nTypeLoadImageToMemory = 0;
            rpm.nMainID = SharedPreferencesHelper.getInt(
                    getApplicationContext(), "nMainId", 2);
            rpm.nSubID = null;
            rpm.GetSubID = true;
            rpm.GetVersionInfo = true;
            rpm.logo = "";
            rpm.userdata = "";
            rpm.sn = "";
            rpm.authfile = "";
            rpm.isCut = true;
            rpm.triggertype = 0;
            rpm.devcode = devcode;
            // nProcessType：0-取消所有操作，1－裁切，
            // 2－旋转，3－裁切+旋转，4－倾斜校正，5－裁切+倾斜校正，6－旋转+倾斜校正，7－裁切+旋转+倾斜校正
            rpm.nProcessType = 7;
            rpm.nSetType = 1;// nSetType: 0－取消操作，1－设置操作
            rpm.lpFileName = selectPath; // rpm.lpFileName当为空时，会执行自动识别函数
            // rpm.lpHeadFileName = selectPath;//保存证件头像
            rpm.isSaveCut = false;// 保存裁切图片 false=不保存 true=保存
            if (SharedPreferencesHelper.getInt(getApplicationContext(),
                    "nMainId", 2) == 2) {
                rpm.isAutoClassify = true;
                rpm.isOnlyClassIDCard = true;
            } else if (SharedPreferencesHelper.getInt(getApplicationContext(),
                    "nMainId", 2) == 3000) {
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

                    for (int i = 1; i < GetFieldName.length; i++) {
                        if (GetRecogResult[i] != null) {
                            if (!recogResultString.equals("")) {
                                recogResultString = recogResultString
                                        + GetFieldName[i] + ":"
                                        + GetRecogResult[i] + ",";
                            } else {
                                recogResultString = GetFieldName[i] + ":"
                                        + GetRecogResult[i] + ",";
                            }
                        }
                    }
                    spit(recogResultString);
                    flag = 2;
//                    disPlayImage(selectPath);
                    etName.setText(splite_Result[0].substring(3));
                    etAddress.setText(splite_Result[4].substring(3));
                    etId.setText(splite_Result[5].substring(7));
                    recogResultString = "";
                    dialog.dismiss();

                } else {
                    flag = 2;
                    etName.setText("");
                    etId.setText("");
                    etAddress.setText("");
                    etRemark.setText("");
                    showPic0(null);
                    imgIdLast.setImageResource(R.mipmap.firstid);

                    indextime = 0;

                    dialog.dismiss();
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
                    Util.createToast(MessageCollectionNewActivity2.this, string);

                }
            } catch (Exception e) {
                flag = 2;
                etName.setText("");
                etId.setText("");
                etAddress.setText("");
                etRemark.setText("");
                showPic0(null);
                imgIdLast.setImageResource(R.mipmap.firstid);

                indextime = 0;

                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        getString(R.string.recognized_failed),
                        Toast.LENGTH_SHORT).show();

            } finally {
                if (recogBindViewer != null) {
                    unbindService(recogConn);
                }
            }

        }
    };

//    private void test() {
//        com.sunrise.icardreader.model.IdentityCardZ identityCard = new com.sunrise.icardreader.model.IdentityCardZ();
////        etName.setText(identityCard.name.trim());
////        etId.setText(identityCard.cardNo.trim());
////        etAddress.setText(identityCard.address.trim());
////        etName.setSelection(etName.getText().toString().length());
//        identityCard.name = "test";
//        identityCard.cardNo = "123456";
//        identityCard.address = "测试地址";
//        readCardSuccess0(identityCard);
//    }


    private final String SPNAME = "Addressmac";

    private void saveAddressmac(String name, String addressmac) {
        SharedPreferences.Editor editor = this.getSharedPreferences(SPNAME, MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("addressmac", addressmac);
        editor.commit();
    }

    private String[] getAddressmac() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SPNAME, MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String addressmac = sharedPreferences.getString("addressmac", "");
        String[] strings = new String[2];
        strings[0] = name;
        strings[1] = addressmac;
        return strings;
    }

    @Override
    protected void onStop() {
        //关闭管控线程，防止报错
        if (mSRBlueReaderHelper != null)
            ReaderManagerService.stopServer();
        super.onStop();
    }

    public void readCard() {
        handler.sendEmptyMessageDelayed(MESSAGE_VALID_BTBUTTON, 0);
    }

    @OnClick(R.id.tvCollection)
    public void onClick() {
        mac = getAddressmac();
        Log.d("ccc", mac[0]);
        Log.d("ccc", mac[1]);
        if (btAdapt.isEnabled()) {
            if (mac[1].equals("")) {
                /*蓝牙地址未存本地,跳转选择蓝牙设备界面*/
                Intent serverIntent2 = new Intent(MessageCollectionNewActivity2.this, BlueToothActivity.class);
                startActivityForResult(serverIntent2, SETTING_BT);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MessageCollectionNewActivity2.this);
                builder.setMessage("是否读取设备" + mac[0]);
                builder.setTitle("提示");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface alertDialog, int which) {
                        alertDialog.dismiss();
                        dialog.getTvTitle().setText("正在读取");
//                        dialog.show();
                        /*读卡设备2*/
                        if (mac[0].startsWith("ST")) {
                            dialog.show();
                            mBlueReaderHelper = new BlueReaderHelper(MessageCollectionNewActivity2.this, handler);
                            mBlueReaderHelper.setServerAddress("senter-online.cn");
                            mBlueReaderHelper.setServerPort(10002);
                            if (mBlueReaderHelper.registerBlueCard(mac[1]) == true) {
                                new BlueReadTask()
                                        .executeOnExecutor(Executors.newCachedThreadPool());
                            } else {
                                dialog.dismiss();
                                Util.createToast(MessageCollectionNewActivity2.this, "请确认蓝牙设备已连接");
                            }
                        } else if (mac[0].startsWith("BT")) {
                            IPArray = new ArrayList<String>();
                            IPArray.add("103.21.119.78");
                            IPArray.add("103.21.119.78");
                            IPArray.add("103.21.119.78");
                            try {
                                ReadCardAPI = new OTGReadCardAPI(getApplicationContext(), IPArray);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            handler.sendEmptyMessageDelayed(MESSAGE_VALID_BTBUTTON, 1000);
                        } else if (mac[0].startsWith("SR")) {
                            dialog.show();
                            mSRBlueReaderHelper = new SRBluetoothCardReader(new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    Log.d("vvv", msg.what + "");
                                    switch (msg.what) {
                                        case READ_CARD_SUCCESS:
                                            mSRBlueReaderHelper.unRegisterBlueCard();
                                            com.sunrise.icardreader.model.IdentityCardZ a = (com.sunrise.icardreader.model.IdentityCardZ) msg.obj;
//                                            Log.d("vvv", a.name);
                                            dialog.dismiss();
                                            readCardSuccess0(a);
                                            break;
                                        case READ_CARD_FAILED:
                                            dialog.dismiss();
                                            Toast.makeText(MessageCollectionNewActivity2.this, "读取失败", Toast.LENGTH_SHORT).show();
                                            break;

                                    }
                                }
                            }, MessageCollectionNewActivity2.this, "FE870B0163113409C134283661490AEF");
                            if (mSRBlueReaderHelper.registerBlueCard(mac[1]))
                                Log.d("bbb", "1111111");
                            new Thread() {
                                public void run() {
                                    //蓝牙读身份证
                                    mSRBlueReaderHelper.readCard(30);
                                }
                            }.start();
                        }
                    }
                });
                builder.setNegativeButton("其他设备", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface alertDialog, int which) {
                        alertDialog.dismiss();
                        Intent serverIntent2 = new Intent(MessageCollectionNewActivity2.this, BlueToothActivity.class);
                        startActivityForResult(serverIntent2, SETTING_BT);
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                });
                builder.create().show();

            }
        } else {
            /*开启蓝牙*/
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    private void readCardSuccess0(com.sunrise.icardreader.model.IdentityCardZ identityCard) {
        if (identityCard != null) {
            etName.setText(identityCard.name.trim());
            etId.setText(identityCard.cardNo.trim());
            etAddress.setText(identityCard.address.trim());
            etName.setSelection(etName.getText().toString().length());
            modes = 2;
        }
    }

    @Override
    public void scanOCRIdCardSuccess(ResultMessage resultMessage, String[] strings) {
        String result=
            ManageIDCardRecogResult.managerSucessRecogResult(resultMessage,getApplicationContext());
        Log.i("scanOCRIdCardSuccess", result);
    }

    @Override
    public void scanOCRIdCardError(String s, String[] strings) {
        ToastUtils.s(this, s);
    }

    @Override
    public void authOCRIdCardSuccess(String s) {
        ToastUtils.s(this, s);
    }

    @Override
    public void authOCRIdCardError(String s) {
        ToastUtils.s(this, s);
    }

    private class BlueReadTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String strCardInfo) {
            tvCollection.setEnabled(true);

            /*读取到的信息是空,提示读取失败*/
            if (TextUtils.isEmpty(strCardInfo)) {
                handler.sendEmptyMessage(ConsantHelper.READ_CARD_FAILED);
                return;
            }
            /*读取到的信息是空,提示读取失败*/
            if (strCardInfo.length() <= 2) {
                readCardFailed(strCardInfo);
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            IdentityCardZ mIdentityCardZ = new IdentityCardZ();

            try {
                mIdentityCardZ = (IdentityCardZ) objectMapper.readValue(
                        strCardInfo, IdentityCardZ.class);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(ConsantHelper.STAGE_LOG, "mIdentityCardZ failed");
//                nfcTask = null;

                return;
            }
            dialog.dismiss();
            /*读卡成功*/
            readCardSuccess(mIdentityCardZ);


            super.onPostExecute(strCardInfo);

        }

        @Override
        protected String doInBackground(Void... params) {
            String strCardInfo=mBlueReaderHelper.read();
            return strCardInfo;
        }
    }

    private void readCardFailed(String strcardinfo) {
        int bret = Integer.parseInt(strcardinfo);
        switch (bret) {
            case -1:
                dialog.dismiss();
                Util.createToast(MessageCollectionNewActivity2.this, "服务器连接失败");
                break;
            case 1:
            case 2:
            case 4:
            case -2:
                dialog.dismiss();
                Util.createToast(MessageCollectionNewActivity2.this, "读卡失败");
                break;
            case 3:
                dialog.dismiss();
                Util.createToast(MessageCollectionNewActivity2.this, "网络超时");
                break;
            case 5:
                dialog.dismiss();
                Util.createToast(MessageCollectionNewActivity2.this, "读取成功");
                break;
        }
    }

    private void readCardSuccess(IdentityCardZ identityCard) {

        if (identityCard != null) {
            etName.setText(identityCard.name.trim());
            etId.setText(identityCard.cardNo.trim());
            etAddress.setText(identityCard.address.trim());
            etName.setSelection(etName.getText().toString().length());
            modes = 2;
            try {

//                bitmap_two = BitmapFactory.decodeByteArray(identityCard.avatar,
//                        0, identityCard.avatar.length);
//                imgIdLast.setImageBitmap(bitmap_two);
//                flag = 2;
//                state_two = 3;
//                imgIdLasRemove.setVisibility(View.VISIBLE);

                Log.e(ConsantHelper.STAGE_LOG, "图片成功");
            } catch (Exception e) {
                Log.e(ConsantHelper.STAGE_LOG, "图片失败" + e.getMessage());
            }

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 110:
                    showPic(handleUri);
//                    Log.i("我收到消息",""+msg.arg1) ;
//                    //拷贝过程中更新进度条
//                    if(msg.arg1==100) {
//                        //拷贝完了就隐藏进度条
//                    }
//                    Toast.makeText( MessageCollectionNewActivity.this,"...",Toast.LENGTH_SHORT ).show();
                    break;
                case 111:
                    showPic0(handleUri);
//                    Log.i("我收到消息",""+msg.arg1) ;
//                    //拷贝过程中更新进度条
//                    if(msg.arg1==100) {
//                        //拷贝完了就隐藏进度条
//                    }
//                    Toast.makeText( MessageCollectionNewActivity.this,"...",Toast.LENGTH_SHORT ).show();
                    break;
                case ConsantHelper.READ_CARD_SUCCESS:
                    dialog.dismiss();
                    tvCollection.setEnabled(true);
                    break;

                case ConsantHelper.SERVER_CANNOT_CONNECT:
                    dialog.dismiss();
                    Util.createToast(MessageCollectionNewActivity2.this, "服务器连接失败,请检查网络!");
                    tvCollection.setEnabled(true);
                    break;

                case ConsantHelper.READ_CARD_FAILED:
                    dialog.dismiss();
                    Util.createToast(MessageCollectionNewActivity2.this, "无法读取信息请重试!");
                    tvCollection.setEnabled(true);
                    break;

                case ConsantHelper.READ_CARD_WARNING:
                    String str = (String) msg.obj;
                    dialog.dismiss();
                    if (str.indexOf("card") > -1) {
                        Util.createToast(MessageCollectionNewActivity2.this, "读卡失败: 卡片丢失,或读取错误!");
                    } else {
                        String[] datas = str.split(":");
                        Util.createToast(MessageCollectionNewActivity2.this, "网络超时!");
//                        tvInfo.setText("网络超时 错误码: "+ Integer.toHexString(new Integer(datas[1])));
                    }
                    tvCollection.setEnabled(true);
                    break;
                case MESSAGE_VALID_BTBUTTON:
                    ReadCardAPI.setmac(mac[1]);
                    int tt = ReadCardAPI.BtReadCard(btAdapt, loginTicket);
                    Log.e("For Test", " ReadCard TT=" + tt);

                    if (tt == 2) {
//                        dialog.dismiss();
                        Util.createToast(MessageCollectionNewActivity2.this, "接收数据超时!");
                    }
                    if (tt == 41) {
//                        dialog.dismiss();
                        Util.createToast(MessageCollectionNewActivity2.this, "读卡失败!");
                    }
                    if (tt == 42) {
//                        dialog.dismiss();
                        Util.createToast(MessageCollectionNewActivity2.this, "没有找到服务器!");
                    }
                    if (tt == 43) {
//                        dialog.dismiss();
                        Util.createToast(MessageCollectionNewActivity2.this, "服务器繁忙!");
                    }
                    if (tt == 90) {
//                        dialog.dismiss();
                        Util.createToast(MessageCollectionNewActivity2.this, "读卡成功!");

                        etName.setText(ReadCardAPI.Name().trim());
                        etId.setText(ReadCardAPI.CardNo().trim());
                        etAddress.setText(ReadCardAPI.Address().trim());
                        modes = 2;
                        try {
                            bitmap_two = BitmapFactory.decodeByteArray(ReadCardAPI.GetImage(), 0, ReadCardAPI.GetImage().length);
                            imgIdLast.setImageBitmap(bitmap_two);
                            flag = 2;
                            state_two = 3;
                            imgIdLasRemove.setVisibility(View.VISIBLE);
                            Log.e(ConsantHelper.STAGE_LOG, "图片成功");
                        } catch (Exception e) {
                            Log.e(ConsantHelper.STAGE_LOG, "图片失败" + e.getMessage());
                        }
                        ReadCardAPI.release();
                    }


                    break;
                case STARTSCAN:
                    if (indextime < 1) {
                        dialog.getTvTitle().setText("正在扫描,请稍后");
                        dialog.show();
                        RecogService.nMainID = SharedPreferencesHelper.getInt(
                                getApplicationContext(), "nMainId", 2);
                        RecogService.isRecogByPath = true;
                        Intent recogIntent = new Intent(MessageCollectionNewActivity2.this,
                                RecogService.class);

                        bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);

                        Log.d("mmm", indextime + "");
                        indextime++;
                    }
                    break;
                case SUBMIT_AGAIN:
                    submitBuy();
                    break;
            }
        }
    };


    /**
     * 复制单个文件
     *
     * @param oldPath 原文件路径 如：c:/fqf.txt String
     * @param newPath 复制后路径 如：f:/fqf.txt
     */
    public static void copyFile(String oldPath, String newPath, Handler handler) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {//文件存在时
                InputStream inStream = new FileInputStream(oldPath);//读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                int value = 0;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;//字节数 文件大小
                    value++;  //计数
//                    System.out.println("完成"+bytesum+"  总大小"+fileTotalLength);
                    fs.write(buffer, 0, byteread);
//                    Thread.sleep(10);//每隔10ms发送一消息，也就是说每隔10ms value就自增一次，将这个value发送给主线程处理
                }

                Message msg = new Message(); //创建一个msg对象
                msg.what = 110;
                msg.arg1 = value; //当前的value
                handler.sendMessage(msg);

                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }


    private void showPic0(String capturePath) {

        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(getResources(), capturePath, DisplayUtil.dp2px(
            MessageCollectionNewActivity2.this, 200), DisplayUtil.dp2px(
            MessageCollectionNewActivity2.this, 200));

        if (capturePath != null) {
            urlList.add(capturePath);//
            if (capturePath.contains("_tmp.jpg") || capturePath.contains("_tmp.png")) {
                bitmap = add0(bitmap);
            }
            File file = new File(capturePath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        switch (flag) {
            case 0:
                imgFront.setImageBitmap(bitmap);
                if (capturePath != null) {
                    bitmap_zero = bitmap;
                    state_zere = 1;
                    imgFrontRemove.setVisibility(View.VISIBLE);
                } else {
                    state_zere = 0;
                    bitmap_zero = null;
                    imgFrontRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                imgIdBack.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_one = 1;
                    bitmap_one = bitmap;
                    imgBackRemove.setVisibility(View.VISIBLE);
                } else {
                    state_one = 0;
                    bitmap_one = null;
                    imgBackRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 21:
                imgIdBackLast.setImageBitmap(bitmap);
                if (capturePath != null) {
                    bitmap_three = bitmap;
                    state_three = 1;
                    imgIdBackLasRemove.setVisibility(View.VISIBLE);
                } else {
                    state_three = 0;
                    bitmap_three = null;
                    imgIdBackLasRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                imgIdLast.setImageBitmap(bitmap);
                if (capturePath != null) {
                    // urlList.add(capturePath);
                    state_two = 1;
                    bitmap_two = bitmap;
                    imgIdLasRemove.setVisibility(View.VISIBLE);
                    if (type.equals("1") && bulmorcamera == 0) {
                        Message message = new Message();
                        message.what = STARTSCAN;
                        handler.sendMessageDelayed(message, 500);


                    }
                } else {
                    state_two = 0;
                    bitmap_two = null;
                    imgIdLasRemove.setVisibility(View.INVISIBLE);
                }
                break;

        }
    }

    //水印
    private Bitmap add0(Bitmap bm) {

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
        canvas.drawTextOnPath("" + format.format(Calendar.getInstance().getTime()), path1, 0, 40, paint);
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

    private void disPlayImage0(String capturePath) {
        if (capturePath.contains("_tmp.jpg") || capturePath.contains("_tmp.png")) {
            showPic0(capturePath);
        } else {
            String mUri = capturePath;
//            String nUri=capturePath;
//            nUri=nUri.substring( nUri.indexOf( "." ));
//            mUri=mUri.substring( mUri.lastIndexOf( "." ));
//            String[] filename= capturePath.split( "." );
//            handleUri = filename [0]+"_tmp"+filename[1];
            handleUri = mUri.substring(0, mUri.lastIndexOf(".")) + "_tmp" + mUri.substring(mUri.lastIndexOf("."));
            if (handleUri.equals("")) {
            } else {
                copyFile0(capturePath, handleUri, handler);
                switch (flag) {
                    case 0:
                        file_zero = handleUri;
                        break;
                    case 1:
                        file_one = handleUri;
                        break;
                    case 2:
                        file_two = handleUri;
                        SharedPreferences sharedPreferences = getSharedPreferences("mySP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("reco", handleUri);
                        editor.commit();
                        break;
                    case 21:
                        file_three = handleUri;
                        break;
                }
                showPic0(handleUri);
            }
        }
    }

    public static void copyFile0(String oldPath, String newPath, Handler handler) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {//文件存在时
                InputStream inStream = new FileInputStream(oldPath);//读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                int value = 0;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;//字节数 文件大小
                    value++;  //计数
//                    System.out.println("完成"+bytesum+"  总大小"+fileTotalLength);
                    fs.write(buffer, 0, byteread);
//                    Thread.sleep(10);//每隔10ms发送一消息，也就是说每隔10ms value就自增一次，将这个value发送给主线程处理
                }

                Message msg = new Message(); //创建一个msg对象
                msg.what = 111;
                msg.arg1 = value; //当前的value
                handler.sendMessage(msg);

                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }


    private void renameFile(String oldPath, String newPath) {
        if (TextUtils.isEmpty(oldPath)) {
            return;
        }

        if (TextUtils.isEmpty(newPath)) {
            return;
        }

        File file = new File(oldPath);

        if (file.exists()) {
            file.delete();
        }
        file.renameTo(new File(newPath));
    }
}
