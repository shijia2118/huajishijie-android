package com.test.tworldapplication.activity.card;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.WriteInNewActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.activity.other.WebViewActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Package;
import com.test.tworldapplication.entity.Photo;
import com.test.tworldapplication.entity.PostAddress;
import com.test.tworldapplication.entity.PostMoney;
import com.test.tworldapplication.entity.PostOpen;
import com.test.tworldapplication.entity.PostPictureUpload;
import com.test.tworldapplication.entity.PostSetOpenNew;
import com.test.tworldapplication.entity.PostWhiteOpen;
import com.test.tworldapplication.entity.Promotion;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestMoney;
import com.test.tworldapplication.entity.RequestOpen;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.entity.RequestSetOpenNew;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.Constants;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.utils.signature.SignatureView;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

import static com.test.tworldapplication.base.BaseCom.INNORMAL;

public class AccountClosingActivity extends BaseActivity {

    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvPackageMoney)
    TextView tvPackageMoney;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.tvMoneyDiscounted)
    TextView tvMoneyDiscounted;
    @BindView(R.id.tvFinalMoney)
    TextView tvFinalMoney;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.llAgreement)
    LinearLayout llAgreement;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.tvAgreement)
    TextView tvAgreement;
    @BindView(R.id.xy)
    ImageView xy;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.sign)
    SignatureView sign;
    Package mPackage = null;
    Promotion mPromotion = null;
    RequestCheck requestCheck = null;
    String sum = "";
    String name = "";
    String cardId = "";
    String address = "";
    String remark = "";
    Bitmap photoOne = null;
    Bitmap photoTwo = null;
    RequestImsi requestImsi;
    Bitmap bitmapTerminate;
    String from = "";
    String type = "";
    String certificatesType = "Idcode";
    String cardType = "ESIM";
    String phone = "", iccid = "";
    ArrayList<String> urlList;
    String face = "";
    String number;

    Bitmap videoPicOne = null;
    Bitmap videoPicTwo = null;
    int picShow = 0;
    int apiShow = 0;
    //    public Timer timer;
    int showLocal = 0;
    int showApi = 0;
    String detail, preStore;
    int totalPic;//需要上传的图片总数
    AtomicInteger uploadSuccessCount = new AtomicInteger(0);

//    String certificatesNo, iccid, certificatesType = "Idcode", certificatesAddress, cardType = "ESIM", number, customerName, orderAmount, payAmount;

    /*0成卡,计算结算金额sum,接受参数RequestCheck,Package,Promotion,两张bitmap和一些身份证信息,点击提交,先上传图片,再提交数据 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_closing);
        ButterKnife.bind(this);
        setBackGroundTitle("开户协议", true);
        from = getIntent().getStringExtra("from");
        face = getIntent().getStringExtra("face");
        urlList = getIntent().getStringArrayListExtra("urlList");
        tvCancel.setVisibility(View.VISIBLE);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().finishActivityBesides(MainNewActivity.class);
//                Util.gotoActy(AccountClosingActivity.this, MainNewActivity.class);
            }
        });


        switch (from) {
            case "0":
                /*成卡*/
//                 llAgreement.setVisibility(View.VISIBLE);
                //tvSubmit.setClickable(false);
                requestCheck = (RequestCheck) getIntent().getSerializableExtra("requestCheck");
                type = getIntent().getStringExtra("type");
                Log.d("type",type);

                break;
            case "1":
                //llAgreement.setVisibility(View.GONE);
                requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
                phone = getIntent().getStringExtra("phone");
                iccid = getIntent().getStringExtra("iccid");

                break;
            case "2":
                number = getIntent().getStringExtra("number");
                Log.d("nnn", "getNumber:" + number);
                type = getIntent().getStringExtra("type");
                apiShow = 1;
                break;
            case "5":
                type = getIntent().getStringExtra("type");
                number = getIntent().getStringExtra("number");
                preStore = getIntent().getStringExtra("preStore");
                detail = getIntent().getStringExtra("detail");
                phone = getIntent().getStringExtra("number");
                apiShow = 1;
                break;
        }
        Log.d("typezzz",type);
        Log.d("fromzzz",from);

        remark = getIntent().getStringExtra("remark");
        cardId = getIntent().getStringExtra("cardId");
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");

        photoOne = BaseCom.photoOne;
        photoTwo = BaseCom.photoTwo;

        videoPicOne = BaseCom.videoOne;
        videoPicTwo = BaseCom.videoTwo;

        mPackage = (Package) getIntent().getSerializableExtra("mPackage");
        mPromotion = (Promotion) getIntent().getSerializableExtra("mPromotion");
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    tvSubmit.setClickable(true);
                else
                    tvSubmit.setClickable(false);
            }
        });
        init();

        loadXy();


    }

    private void loadXy() {
        xy.setAdjustViewBounds(true);
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_CENTER)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setLoadingDrawableId(R.mipmap.agreement12)
                .setFailureDrawableId(R.mipmap.agreement12)
                .setUseMemCache(true)
                .setIgnoreGif(false)
                .build();

        x.image().bind(xy, "http://dfd", imageOptions);
        tvSubmit.setEnabled(true);
        tvSubmit.setVisibility(View.VISIBLE);


//        handler.sendEmptyMessageDelayed(2, 4000);
//        if (timer != null)
//            timer.cancel();
//        timer = new Timer();
//        Log.d("ddd", "dddddddd");
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                timer.cancel();
//                Log.d("ddd", "0000000000");
//                //.setLoadingDrawableId(R.mipmap.agreement4)
//
//
////                EventBus.getDefault().post(new MessageEvent("999"));
//                // TODO Auto-generated method stub
//
//            }
//        }, 4000);

//        Picasso.with(AccountClosingActivity.this).load(R.mipmap.agreement)
//                .fit()
//                .into(xy);


//        PostAddress postAddress = new PostAddress();
//        postAddress.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
//        HttpPost<PostAddress> httpPost = new HttpPost<>();
//        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
//        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postAddress) + BaseCom.APP_PWD));
//        httpPost.setParameter(postAddress);
//        new CardHttp().address(httpPost, new Subscriber<HttpRequest<String>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(HttpRequest<String> requestAddressHttpRequest) {
////               // xy.setImageURL(requestAddressHttpRequest.getData().getAddress());
//                // BitmapUtil.LoadImageByUrl(ImageView.ScaleType.CENTER,xy,requestAddressHttpRequest.getData(),0,0);
//                if (requestAddressHttpRequest.getCode() == BaseCom.NORMAL) {
//                    if (showLocal == 0) {
//                        showApi = 1;
//                        xy.setAdjustViewBounds(true);
//                        BitmapUtil.LoadImageByUrl(xy, requestAddressHttpRequest.getData());
//                        tvSubmit.setEnabled(true);
//                        picShow = 1;
//                        if (apiShow == 1)
//                            tvSubmit.setVisibility(View.VISIBLE);
//                    }
//                }
//
//
////                ImageLoader loader = new ImageLoader(NimApplication.getHttpRequestQueue(),new BitmapCache());
////                NetworkImageView imageView=new NetworkImageView(this);
////                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
////                imageView.setErrorImageResId(R.drawable.g_ic_failed_small);//单个网络视图未正确加载时显示的图片
////                imageView.setDefaultImageResId(R.drawable.loading);
////                imageView.setImageUrl(describe_pic.get(i),loader);//设置网络图片视图的的图片地址和加载器
////                imageView.setAdjustViewBounds(true);
////                goods_ll.addView(imageView);
//            }
//        });


    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    reNewCard();
                    break;
                case 1:
                    preOpen();
                    break;
                case 2:
                    if (showApi == 0) {
                        showLocal = 1;
                        xy.setAdjustViewBounds(true);
                        ImageOptions imageOptions = new ImageOptions.Builder()
                                .setPlaceholderScaleType(ImageView.ScaleType.FIT_CENTER)
                                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                                .setLoadingDrawableId(R.mipmap.agreement11)
                                .setFailureDrawableId(R.mipmap.agreement11)
                                .setUseMemCache(true)
                                .setIgnoreGif(false)
                                .build();
                        x.image().bind(xy, "http://dfd", imageOptions);
                        tvSubmit.setEnabled(true);
                        picShow = 1;
                        if (apiShow == 1)
                            tvSubmit.setVisibility(View.VISIBLE);
                    }


                    break;
                case 3:
                    AppManager.getAppManager().finishActivityBesides(MainNewActivity.class);

                    break;
            }

        }
    };

    public void init() {
        switch (from) {
            case "0":
                getMoney();
                tvNumber.setText("开户号码: " + requestCheck.getNumber());
                tvMoney.setText("预存金额: " + requestCheck.getPrestore());
                tvPackageMoney.setText("套餐金额: 0");
                tvActivity.setText("活动包: " + mPackage.getName());
                tvMoneyDiscounted.setText("优惠金额: 0");

                break;
            case "1":
                tvNumber.setText("开户号码: " + phone);
                tvMoney.setText("预存金额: " + requestImsi.getPrestore());
                tvPackageMoney.setText("套餐金额: 0");
                tvActivity.setText("活动包: " + mPackage.getName());
                tvMoneyDiscounted.setText("优惠金额: 0");
//                tvSubmit.setVisibility(View.VISIBLE);
                break;
//            case "5":
//                tvNumber.setText("激活号码: " + phone);
//                tvMoney.setText("预存金额: " + preStore);
//                tvPackageMoney.setText("套餐金额: 0");
//                tvActivity.setText("活动包: " + detail);
//                tvMoneyDiscounted.setText("优惠金额: 0");
//                break;
        }


    }

    /*sum = value.getSum();
                    tvFinalMoney.setText("总金额: " + sum);*/
    private void getMoney() {
//        if (Util.isLog(AccountClosingActivity.this)) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(AccountClosingActivity.this);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface alertDialog, int which) {
                AppManager.getAppManager().finishActivityBesides(MainNewActivity.class);

                if (from.equals("0")) {
                    /*成卡*/
                    AppManager.getAppManager().finishActivity();
                    AppManager.getAppManager().finishActivity(MessageCollectionNewActivity.class);
                    AppManager.getAppManager().finishActivity(SelectActivity.class);
                    AppManager.getAppManager().finishActivity(PackageSelectActivity.class);
                    AppManager.getAppManager().finishActivity(RenewCardActivity.class);
                    if (face.equals("1")) {
                        AppManager.getAppManager().finishActivity(FaceRecordingActivity.class);
                    }
                } else {
                    AppManager.getAppManager().finishActivity();
                    AppManager.getAppManager().finishActivity(CardReadingActivity.class);
                    AppManager.getAppManager().finishActivity(MessageCollectionNewActivity.class);
                    AppManager.getAppManager().finishActivity(NewCardActivity.class);
                    if (face.equals("1")) {
                        AppManager.getAppManager().finishActivity(FaceRecordingActivity.class);
                    }

                }
            }
        });


        HttpPost<PostMoney> httpPost = new HttpPost<>();
        PostMoney postMoney = new PostMoney();
        postMoney.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
        postMoney.setPromotionId(String.valueOf(mPromotion.getId()));
        postMoney.setPrestore(requestCheck.getPrestore());
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postMoney);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postMoney) + BaseCom.APP_PWD));
        new CardHttp().money(new Subscriber<HttpRequest<RequestMoney>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                builder.setMessage(e.getMessage());
                builder.create().show();
            }

            @Override
            public void onNext(HttpRequest<RequestMoney> requestMoneyHttpRequest) {
                if (requestMoneyHttpRequest.getCode() == BaseCom.NORMAL) {
                    sum = requestMoneyHttpRequest.getData().getSum();
                    tvFinalMoney.setText("总金额: " + sum);
                    apiShow = 1;
                    if (picShow == 1)
                        tvSubmit.setVisibility(View.VISIBLE);
                } else if (requestMoneyHttpRequest.getCode() == BaseCom.LOSELOG || requestMoneyHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(AccountClosingActivity.this, LoginActivity.class);
                else {
                    builder.setMessage(requestMoneyHttpRequest.getMes());
                    builder.create().show();
                }

            }
        }, httpPost);
    }
//    }


    @OnClick({R.id.tvSubmit, R.id.tvAgreement, R.id.close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:

                switch (from) {
                    case "0":
                        //成卡
                        if (!Util.isFastDoubleClick()) {

                            if (sign.isEmpty()) {
                                Toast.makeText(this, "请签名后再提交", Toast.LENGTH_SHORT).show();

                            } else {
                                compound();
//                                if (face.equals( "1" )) {
//                                    picFirst();
//                                }
                                reNewCard();
                            }

//                            if (urlList != null && urlList.size() > 0) {
//                                for (int i = 0; i < urlList.size(); i++)
//                                    deletePic(AccountClosingActivity.this, urlList.get(i));
//                            }

                        }
                        break;
                    case "1":
                        //白卡
                        if (!Util.isFastDoubleClick()) {
                            newCard();
                        }
                        break;
                    case "2":
                    case "5":
                        /*激活*/
                        if (!Util.isFastDoubleClick()) {

                            if (sign.isEmpty()) {
                                Toast.makeText(this, "请签名后再提交", Toast.LENGTH_SHORT).show();

                            } else {
                                compound();
                                preOpen();
                            }

                        }
                        break;
                }
                break;
            case R.id.tvAgreement:
                Intent intent = new Intent(AccountClosingActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://121.46.26.224:8088/notices/android_xy.jpg");
                startActivity(intent);
                break;
            case R.id.close:
                sign.clear();
                break;
        }


    }


    private void compound() {
        //bitmapTerminate = BitmapUtil.createWatermark(((BitmapDrawable)xy.getDrawable()).getBitmap(),sign.getTransparentSignatureBitmap());
        Bitmap bitmapSign = sign.getTransparentSignatureBitmap();
        Matrix matrix = new Matrix();
        matrix.postScale(0.1f, 0.1f); //长和宽放大缩小的比例
// ((BitmapDrawable) xy.getDrawable()).getBitmap()
        Bitmap resizeBmp = Bitmap.createBitmap(bitmapSign, 0, 0, bitmapSign.getWidth(), bitmapSign.getHeight(), matrix, true);
        Bitmap resourceBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.agreement12);
        bitmapTerminate = BitmapUtil.createWaterMaskRightBottom(this, resourceBmp
                , resizeBmp, 45, 42);
        SimpleDateFormat format = new SimpleDateFormat("yyyy  MM  dd", Locale.CHINESE);
        Date date = Calendar.getInstance().getTime();
        bitmapTerminate = BitmapUtil.drawTextToLeftBottom(this, bitmapTerminate, format.format(date),
                4, Color.BLACK, 20, 13);

        //     File file = new File(Environment.getExternalStorageDirectory(), "bm2.png");

//        try {
////            FileOutputStream fos = new FileOutputStream(file);
////            bitmapTerminate.compress(Bitmap.CompressFormat.JPEG, 100, fos);
////            fos.flush();
////            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{filePath$Name});//删除系统缩略图
                file.delete();//删除SD中图片
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // 删除所有文件

            } else if (file.isDirectory()) {
                deleteDirWihtFile(file);
            }// 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }


    public void reNewCard() {
        dialog.getTvTitle().setText("正在上传图片");
        dialog.show();

        HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
        PostPictureUpload postPictureUpload = new PostPictureUpload();
        postPictureUpload.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
        postPictureUpload.setType(type);
        final Photo photo = new Photo();
        if(photoOne!=null) photo.setPhoto1(BitmapUtil.bitmapToBase64(photoOne));
        if(photoTwo!=null) photo.setPhoto2(BitmapUtil.bitmapToBase64X(photoTwo));
        if(BaseCom.photoThree!=null)  photo.setPhoto3(BitmapUtil.bitmapToBase64X(BaseCom.photoThree));
        if(BaseCom.photoFour!=null)  photo.setPhoto4(BitmapUtil.bitmapToBase64X(BaseCom.photoFour));
        if(bitmapTerminate!=null)  photo.setPhoto5(BitmapUtil.bitmapToBase64(bitmapTerminate));

        httpPost.setPhoto(photo);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPictureUpload);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
        new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(AccountClosingActivity.this, null, value -> {
            for(int i=1;i<=5;i++){
                switch (i){
                    case 1:
                        if(value.getPhoto1()!=null)   imgMap.put(Constants.PHOTOONE,value.getPhoto1());
                        break;
                    case 2:
                        if(value.getPhoto2()!=null) imgMap.put(Constants.PHOTOTWO,value.getPhoto2());
                        break;
                    case 3:
                        if(value.getPhoto3()!=null) imgMap.put(Constants.PHOTOTHREE,value.getPhoto3());
                        break;
                    case 4:
                        if(value.getPhoto4()!=null) imgMap.put(Constants.PHOTOFOUR,value.getPhoto4());
                    case 5:
                        if(value.getPhoto5()!=null) imgMap.put(Constants.BITMAPTERMINATE,value.getPhoto5());
                        break;
                }
            }

            HttpPost<PostPictureUpload> httpPost2 = new HttpPost<>();
            PostPictureUpload postPictureUpload2 = new PostPictureUpload();
            postPictureUpload2.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
            postPictureUpload2.setType(type);
            final Photo photo2 = new Photo();

            if (face.equals("1")) {
                photo2.setPhoto1(BitmapUtil.bitmapToBase64(videoPicOne));
                photo2.setPhoto2(BitmapUtil.bitmapToBase64X(videoPicTwo));
            }
            httpPost2.setPhoto(photo2);
            httpPost2.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost2.setParameter(postPictureUpload2);
            httpPost2.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload2) + BaseCom.APP_PWD));

            new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(AccountClosingActivity.this, null, value2 -> {
                for(int i=1;i<=2;i++){
                    switch (i){
                        case 1:
                            if(value2.getPhoto1()!=null)   imgMap.put(Constants.VIDEOPICONE,value2.getPhoto1());
                            break;
                        case 2:
                            if(value2.getPhoto2()!=null) imgMap.put(Constants.VIDEOPICTWO,value2.getPhoto2());
                            break;
                    }
                }
                dialog.dismiss();
            finalUpload();
            }), httpPost);
        }), httpPost);

//        totalPic = 0;
//        uploadSuccessCount.set(0);
//        if (photoOne != null) {
//            totalPic++;
//        }
//        if (photoTwo != null) {
//            totalPic++;
//        }
//        if (BaseCom.photoThree != null) {
//            totalPic++;
//        }
//        if (BaseCom.photoFour != null) {
//            totalPic++;
//        }
//        if (bitmapTerminate != null) {
//            totalPic++;
//        }
//        if (face.equals("1")) {
//            totalPic++;
//            totalPic++;
//            //photo.setPhoto5(BitmapUtil.bitmapToBase64(videoPicOne));
//            //photo.setPhoto6(BitmapUtil.bitmapToBase64X(videoPicTwo));
//        }
//        if (photoOne != null) {
//            uploadImage(photoOne, Constants.PHOTOONE);
//        }
//        if (BaseCom.photoFour != null) {
//            uploadImage(BaseCom.photoFour, Constants.PHOTOFOUR);
//        }
//        if (photoTwo != null) {
//            uploadImage(photoTwo, Constants.PHOTOTWO);
//        }
//        if (BaseCom.photoThree != null) {
//            uploadImage(BaseCom.photoThree, Constants.PHOTOTHREE);
//        }
//
//        if (bitmapTerminate != null) {
//            uploadImage(bitmapTerminate, Constants.BITMAPTERMINATE);
//        }
//        if (face.equals("1")) {
//            uploadImage(videoPicOne, Constants.VIDEOPICONE);
//            uploadImage(videoPicTwo, Constants.VIDEOPICTWO);
//        }
    }

    Map<String, String> imgMap = new HashMap<>();

    private void uploadImage(Bitmap bitmap, String key) {
        if (bitmap == null) return;
        HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
        PostPictureUpload postPictureUpload = new PostPictureUpload();
        postPictureUpload.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
        postPictureUpload.setType("0");
        Photo photo = new Photo();
        photo.setPhoto1(BitmapUtil.bitmapToBase64(bitmap));
        httpPost.setPhoto(photo);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPictureUpload);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
        new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(AccountClosingActivity.this, null, value -> {
            uploadSuccessCount.incrementAndGet();
            imgMap.put(key, value.getPhoto1());
            if (uploadSuccessCount.get() == totalPic) {
                dialog.dismiss();
                finalUpload();
            }
        }), httpPost);
    }
    private void uploadImage2(Bitmap bitmap, String key) {
        if (bitmap == null) return;
        HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
        PostPictureUpload postPictureUpload = new PostPictureUpload();
        postPictureUpload.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
        postPictureUpload.setType("0");
        Photo photo = new Photo();
        photo.setPhoto1(BitmapUtil.bitmapToBase64(bitmap));
        httpPost.setPhoto(photo);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPictureUpload);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
        new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(AccountClosingActivity.this, null, value -> {
            uploadSuccessCount.incrementAndGet();
            imgMap.put(key, value.getPhoto1());
            if (uploadSuccessCount.get() == totalPic) {
                finalUpload2();
                dialog.dismiss();
            }
        }), httpPost);
    }
    private boolean uploading;

    private void finalUpload() {
        if (uploading) return;
        uploading = true;
        if (urlList != null && urlList.size() > 0) {
            for (int i = 0; i < urlList.size(); i++)
                deletePic(AccountClosingActivity.this, urlList.get(i));
        }

        dialog.getTvTitle().setText("正在提交");
        dialog.show();
        HttpPost<PostOpen> httpPost = new HttpPost<>();
        PostOpen postOpen = new PostOpen();
        postOpen.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
        postOpen.setNumber(requestCheck.getNumber());
        switch (type) {
            case "0":
                if (face.equals("1")) {
                    postOpen.setAuthenticationType("App人脸识别");
                } else {
                    postOpen.setAuthenticationType("App识别仪");
                }
                /*lanya*/
                break;
            case "1":
                if (face.equals("1")) {
                    postOpen.setAuthenticationType("App人脸识别");
                } else {
                    postOpen.setAuthenticationType("App扫描");
                }
                /*saomiao*/
                break;
        }

        postOpen.setCustomerName(name);
        postOpen.setCertificatesType("Idcode");
        postOpen.setCertificatesNo(cardId);
        postOpen.setAddress(address);
        postOpen.setDescription(remark);
        postOpen.setCardType("SIM");
        postOpen.setSimId(String.valueOf(requestCheck.getSimId()));
        postOpen.setSimICCID(String.valueOf(requestCheck.getSimICCID()));
        postOpen.setPackageId(mPackage.getId());
        postOpen.setPromotionsId(mPromotion.getId());
        postOpen.setOrderAmount(sum);
        postOpen.setAgreementFront(imgMap.get(Constants.BITMAPTERMINATE));
        postOpen.setPhotoFront(imgMap.get(Constants.PHOTOONE));
        postOpen.setPhotoBack(imgMap.get(Constants.PHOTOTWO));
        postOpen.setMemo4(imgMap.get(Constants.PHOTOTHREE));
        postOpen.setMemo11(imgMap.get(Constants.PHOTOFOUR));
        for (Map.Entry<String, String> entry: imgMap.entrySet()) {
            Log.i("imgUrl", entry.getKey() + ":" + entry.getValue());
        }

        if (face.equals("1")) {
            postOpen.setVideoPhotos1(imgMap.get(Constants.VIDEOPICONE));
            postOpen.setVideoPhotos2(imgMap.get(Constants.VIDEOPICTWO));
        }


        postOpen.setOrg_number_poolsId(requestCheck.getOrg_number_poolsId());
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postOpen);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpen) + BaseCom.APP_PWD));
        new CardHttp().open(CardRequest.open(AccountClosingActivity.this, dialog, new SuccessValue<HttpRequest<RequestOpen>>() {
            @Override
            public void OnSuccess(final HttpRequest<RequestOpen> value) {
                uploading = false;
                Util.createToast(AccountClosingActivity.this, value.getMes());
                Log.d("aaa", value.getMes());
                Log.d("aaa", value.getCode() + "");
                if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(AccountClosingActivity.this, LoginActivity.class);
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AccountClosingActivity.this);
                    builder.setCancelable(false);
                    switch (value.getCode()) {
                        case BaseCom.NORMAL:
                            builder.setMessage("订单提交成功");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
                                    //                                            Util.gotoActy(AccountClosingActivity.this, MainNewActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity();
                                    //                                            AppManager.getAppManager().finishActivity(MessageCollectionActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity(SelectActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity(PackageSelectActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity(RenewCardActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity(FaceRecordingActivity.class);


                                    SharedPreferences sharedPreferences = getSharedPreferences("mySP", MODE_PRIVATE);
                                    String uri0 = sharedPreferences.getString("pic0", "");
                                    String uri1 = sharedPreferences.getString("pic1", "");
                                    String photo1 = sharedPreferences.getString("photo1", "");
                                    String photo2 = sharedPreferences.getString("photo2", "");
                                    String photo3 = sharedPreferences.getString("photo3", "");
                                    String witonePic = sharedPreferences.getString("witonePic", "");
                                    String witonePicTmp = sharedPreferences.getString("witonePicTmp", "");

                                    String path = sharedPreferences.getString("witonpath", "");//camera
                                    String dir = sharedPreferences.getString("toworldpic", "");//face


                                    //                                            if(face.equals( "1" )) {
                                    //                                                deleteSingleFile( uri0 );
                                    //                                                deleteSingleFile( uri1 );
                                    deletePic(AccountClosingActivity.this, photo1);
                                    deletePic(AccountClosingActivity.this, photo2);
                                    deletePic(AccountClosingActivity.this, photo3);
                                    deletePic(AccountClosingActivity.this, uri0);
                                    deletePic(AccountClosingActivity.this, uri1);
                                    deletePic(AccountClosingActivity.this, witonePic);
                                    deletePic(AccountClosingActivity.this, witonePicTmp);

                                    deleteDir(Environment.getExternalStorageDirectory().toString() + "/toworldpic");
                                    //                                                deleteDir( Environment.getExternalStorageDirectory().toString()+"/DCIM/GalleryFinal" );
                                    //                                            }
                                    //                                            deleteSingleFile( photo1 );
                                    //                                            deleteSingleFile( photo2 );
                                    //                                            deleteSingleFile( photo3 );

                                    deleteDir(Environment.getExternalStorageDirectory().toString() + "/DCIM/GalleryFinal");

                                    //                                            if (type.equals( "1" )){
                                    //                                                deleteSingleFile( witonePic );
                                    //                                                deleteSingleFile( witonePicTmp );
                                    deleteDir(Environment.getExternalStorageDirectory().toString() + "/wtimage");
                                    //                                            }
                                    handler.sendEmptyMessageDelayed(3, 500);

                                }
                            });
                            break;
                        default:
                            builder.setMessage(value.getMes() + "\n" + "是否重新提交该订单?");
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1500);
                                                android.os.Message message = new android.os.Message();
                                                message.what = 0;
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
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
                                    handler.sendEmptyMessageDelayed(3, 500);

                                    //                                            AppManager.getAppManager().finishActivityBesides(MainNewActivity.class);
                                    //                                            Util.gotoActy(AccountClosingActivity.this, MainNewActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity();
                                    //                                            AppManager.getAppManager().finishActivity(MessageCollectionActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity(SelectActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity(PackageSelectActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity(RenewCardActivity.class);
                                    //                                            AppManager.getAppManager().finishActivity(FaceRecordingActivity.class);

                                }
                            });
                            break;
                        case INNORMAL:
                            Toast.makeText(AccountClosingActivity.this, "图片解析错误", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    builder.create().show();


                }
            }
        }), httpPost);
    }

    private void finalUpload2() {
        if (uploading) return;
        uploading = true;
        if (urlList != null && urlList.size() > 0) {
            for (int i = 0; i < urlList.size(); i++)
                deletePic(AccountClosingActivity.this, urlList.get(i));
        }

        dialog.getTvTitle().setText("正在提交");
        dialog.show();
        HttpPost<PostSetOpenNew> httpPost = new HttpPost<>();
        PostSetOpenNew postOpen = new PostSetOpenNew();
        postOpen.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
        postOpen.setNumber(number);
        switch (type) {
            case "0":
                if (face.equals("1")) {
                    postOpen.setAuthenticationType("App人脸识别");
                } else {
                    postOpen.setAuthenticationType("App识别仪");
                }
                /*lanya*/
                break;
            case "1":
                if (face.equals("1")) {
                    postOpen.setAuthenticationType("App人脸识别");
                } else {
                    postOpen.setAuthenticationType("App扫描");
                }
                /*saomiao*/
                break;
        }

        postOpen.setCustomerName(name);
        postOpen.setCertificatesType("Idcode");
        postOpen.setCertificatesNo(cardId);
        postOpen.setAddress(address);
        postOpen.setAgreementFront(imgMap.get(Constants.BITMAPTERMINATE));
        postOpen.setPhotoFront(imgMap.get(Constants.PHOTOTWO));
        postOpen.setPhotoBack(imgMap.get(Constants.PHOTOONE));

        postOpen.setMemo11(imgMap.get(Constants.PHOTOFOUR));
        postOpen.setMemo4(imgMap.get(Constants.PHOTOTHREE));

        if (face.equals("1")) {
            postOpen.setVideoPhotos1(imgMap.get(Constants.VIDEOPICONE));
            postOpen.setVideoPhotos2(imgMap.get(Constants.VIDEOPICTWO));
        }


        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postOpen);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postOpen) + BaseCom.APP_PWD));
        new CardHttp().setOpen(new Subscriber<HttpRequest<RequestSetOpenNew>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestSetOpenNew> stringHttpRequest) {
                uploading = false;
                Util.createToast(AccountClosingActivity.this, stringHttpRequest.getMes());
                if (stringHttpRequest.getCode() == BaseCom.LOSELOG)
                    Util.gotoActy(AccountClosingActivity.this, LoginActivity.class);
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AccountClosingActivity.this);
                    builder.setCancelable(false);
                    switch (stringHttpRequest.getCode()) {
                        case BaseCom.NORMAL:
                            builder.setMessage("订单提交成功");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();

                                    SharedPreferences sharedPreferences = getSharedPreferences("mySP", MODE_PRIVATE);
                                    String uri0 = sharedPreferences.getString("pic0", "");
                                    String uri1 = sharedPreferences.getString("pic1", "");
                                    String photo1 = sharedPreferences.getString("photo1", "");
                                    String photo2 = sharedPreferences.getString("photo2", "");
                                    String photo3 = sharedPreferences.getString("photo3", "");
                                    String witonePic = sharedPreferences.getString("witonePic", "");
                                    String witonePicTmp = sharedPreferences.getString("witonePicTmp", "");

                                    String path = sharedPreferences.getString("witonpath", "");//camera
                                    String dir = sharedPreferences.getString("toworldpic", "");//face

                                    deletePic(AccountClosingActivity.this, photo1);
                                    deletePic(AccountClosingActivity.this, photo2);
                                    deletePic(AccountClosingActivity.this, photo3);
                                    deletePic(AccountClosingActivity.this, uri0);
                                    deletePic(AccountClosingActivity.this, uri1);
                                    deletePic(AccountClosingActivity.this, witonePic);
                                    deletePic(AccountClosingActivity.this, witonePicTmp);

                                    deleteDir(Environment.getExternalStorageDirectory().toString() + "/toworldpic");
                                    //
                                    deleteDir(Environment.getExternalStorageDirectory().toString() + "/DCIM/GalleryFinal");

                                    deleteDir(Environment.getExternalStorageDirectory().toString() + "/wtimage");
                                    handler.sendEmptyMessageDelayed(3, 500);

                                }
                            });
                            break;
                        case INNORMAL:
                            Toast.makeText(AccountClosingActivity.this, "图片解析错误", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            builder.setMessage(stringHttpRequest.getMes() + "\n" + "是否重新提交该订单?");
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1500);
                                                android.os.Message message = new android.os.Message();
                                                message.what = 1;
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
                                public void onClick(DialogInterface alertDialog, int which) {
                                    alertDialog.dismiss();
                                    handler.sendEmptyMessageDelayed(3, 500);

                                    //                                            Util.gotoActy(AccountClosingActivity.this, MainNewActivity.class);

                                }
                            });
                            break;
                    }
                    builder.create().show();
                }
            }
        }, httpPost);
    }

    public void preOpen() {
        uploadSuccessCount.set(0);
        dialog.getTvTitle().setText("正在上传图片");
        dialog.show();
        totalPic = 0;
        if (photoOne != null) {
            totalPic++;
        }
        if (photoTwo != null) {
            totalPic++;
        }
        if (BaseCom.photoThree != null) {
            totalPic++;
        }
        if (BaseCom.photoFour != null) {
            totalPic++;
        }
        if (bitmapTerminate != null) {
            totalPic++;
        }
        if (face.equals("1")) {
            totalPic++;
            totalPic++;
            //photo.setPhoto5(BitmapUtil.bitmapToBase64(videoPicOne));
            //photo.setPhoto6(BitmapUtil.bitmapToBase64X(videoPicTwo));
        }
        if (photoOne != null) {
            uploadImage2(photoOne, Constants.PHOTOONE);
        }
        if (BaseCom.photoFour != null) {
            uploadImage2(BaseCom.photoFour, Constants.PHOTOFOUR);
        }
        if (photoTwo != null) {
            uploadImage2(photoTwo, Constants.PHOTOTWO);
        }
        if (BaseCom.photoThree != null) {
            uploadImage2(BaseCom.photoThree, Constants.PHOTOTHREE);
        }

        if (bitmapTerminate != null) {
            uploadImage2(bitmapTerminate, Constants.BITMAPTERMINATE);
        }
        if (face.equals("1")) {
            uploadImage2(videoPicOne, Constants.VIDEOPICONE);
            uploadImage2(videoPicTwo, Constants.VIDEOPICTWO);
        }


    }

    public void deletePic(Context context, String path) {
        if (!TextUtils.isEmpty(path)) {
            Log.d("图片*", path);
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = context.getContentResolver();//cutPic.this是一个上下文
                String url = MediaStore.Images.Media.DATA + "='" + path + "'";
                //删除图片
                contentResolver.delete(uri, url, null);
            }

        }
//        //上面没删除成功就执行下面这个
//        File file = new File(path);
//        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
//        if (file.exists() && file.isFile()) {
//            if(file.delete()){
//                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
//                Log.d("图片*", path);
//            }
//        }
    }

    public void newCard() {
        dialog.getTvTitle().setText("正在上传图片");
        dialog.show();

        HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
        PostPictureUpload postPictureUpload = new PostPictureUpload();
        postPictureUpload.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
        postPictureUpload.setType("0");
        Photo photo = new Photo();
        photo.setPhoto1(BitmapUtil.bitmapToBase64(photoOne));
        photo.setPhoto2(BitmapUtil.bitmapToBase64(photoTwo));
        photo.setPhoto3(BitmapUtil.bitmapToBase64(BaseCom.photoThree));

        httpPost.setPhoto(photo);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPictureUpload);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
        new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(AccountClosingActivity.this, dialog, new SuccessValue<RequestPictureUpload>() {
            @Override
            public void OnSuccess(RequestPictureUpload value) {
                if (urlList != null && urlList.size() > 0) {
                    for (int i = 0; i < urlList.size(); i++)
                        deletePic(AccountClosingActivity.this, urlList.get(i));
                }

                dialog.getTvTitle().setText("正在提交");
                dialog.show();
                HttpPost<PostWhiteOpen> httpPost = new HttpPost<>();
                PostWhiteOpen postWhiteOpen = new PostWhiteOpen();
                postWhiteOpen.setSession_token(Util.getLocalAdmin(AccountClosingActivity.this)[0]);
                postWhiteOpen.setAddress(address);
                postWhiteOpen.setSimId(requestImsi.getSimId());
                postWhiteOpen.setCardType(cardType);
                postWhiteOpen.setCertificatesNo(cardId);
                postWhiteOpen.setCertificatesType(certificatesType);
                postWhiteOpen.setCustomerName(name);
                postWhiteOpen.setIccid(iccid);
                postWhiteOpen.setImsi(requestImsi.getImsi());
                postWhiteOpen.setNumber(phone);
                postWhiteOpen.setPhotoFront(value.getPhoto1());
                postWhiteOpen.setPhotoBack(value.getPhoto2());
                postWhiteOpen.setPhotoScan(value.getPhoto3());


                postWhiteOpen.setOrderAmount(String.valueOf(requestImsi.getPrestore()));//金额
                postWhiteOpen.setPackageId(String.valueOf(mPackage.getId()));
                postWhiteOpen.setPromotionsId(String.valueOf(mPromotion.getId()));
                postWhiteOpen.setPayAmount(String.valueOf(requestImsi.getPrestore()));//金额
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postWhiteOpen);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postWhiteOpen) + BaseCom.APP_PWD));
                Log.d("aaa", gson.toJson(httpPost));
                new CardHttp().whiteSetOpen(CardRequest.whiteSetOpen(AccountClosingActivity.this, dialog, new SuccessNull() {
                    @Override
                    public void onSuccess() {
                        AppManager.getAppManager().finishActivity();
                        AppManager.getAppManager().finishActivity(CardReadingActivity.class);
                        AppManager.getAppManager().finishActivity(MessageCollectionNewActivity.class);
                        AppManager.getAppManager().finishActivity(NewCardActivity.class);
                    }
                }), httpPost);

            }
        }), httpPost);


    }
}