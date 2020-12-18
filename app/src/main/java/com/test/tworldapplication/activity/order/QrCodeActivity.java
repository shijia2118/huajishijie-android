package com.test.tworldapplication.activity.order;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.AboutUsActivity;
import com.test.tworldapplication.activity.card.AccountClosingActivity;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostIntroduction;
import com.test.tworldapplication.entity.PostQrCode;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.EventBusCarrier;
import com.test.tworldapplication.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wintone.passport.sdk.utils.AppManager;

public class QrCodeActivity extends BaseActivity {
    @BindView(R.id.imgQrCode)
    ImageView imgQrCode;
    @BindView( R.id.tvFinish )
    TextView tvFinish;

    private String number="";
    private String promotionId="";
    private String packagesId="";
    final OkHttpClient okHttpClient=new OkHttpClient();
    private String getUrl="",url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.bind(this);
        setBackGroundTitle("二维码", true);
        EventBus.getDefault().register(this);


        number=getIntent().getStringExtra( "tel" );
        packagesId=getIntent().getStringExtra( "packagesId" );
        promotionId=getIntent().getStringExtra( "promotionId" );
//        int screenWidth = DisplayUtil.getWindowWidth(this); // 获取屏幕宽度
//        ViewGroup.LayoutParams lp = imgQrCode.getLayoutParams();
//        lp.width = screenWidth;
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        imgQrCode.setLayoutParams(lp);
//
//        imgQrCode.setMaxWidth(screenWidth);
//        imgQrCode.setMaxHeight(screenWidth * 8);
        getData();

        tvFinish.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().finishActivityBesides(MainNewActivity.class);
//                Util.gotoActy(QrCodeActivity.this, MainNewActivity.class);
            }
        } );
    }

    public void getData(){
        HttpPost<PostQrCode> httpPost = new HttpPost<>();
        PostQrCode postQrCode = new PostQrCode();
        postQrCode.setSession_token( Util.getLocalAdmin(QrCodeActivity.this)[0]);
        postQrCode.setNumber(number  );
        postQrCode.setPackagesId( packagesId );
        postQrCode.setPromotionId( promotionId );
        httpPost.setApp_key(Util.encode( BaseCom.APP_KEY));
        httpPost.setParameter(postQrCode);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postQrCode) + BaseCom.APP_PWD));
        new CardHttp().getQr( CardRequest.getQr( QrCodeActivity.this, new SuccessValue<RequestQrCode>() {
            @Override
            public void OnSuccess(RequestQrCode value) {
                getUrl=value.getUrl();
                stringToBitmap(value.getUrlBase64());
//                getCode();
//                BitmapUtil._LoadImageByUrl(ImageView.ScaleType.CENTER_CROP, imgQrCode, value.getUrl(), DisplayUtil.getWindowWidth(QrCodeActivity.this), DisplayUtil.getWindowHeight(QrCodeActivity.this));
            }
        } ),httpPost );

    }

    public  Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
//            BitmapUtil.LoadImageByUrl( imgQrCode, url);
            imgQrCode.setImageBitmap( bitmap );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    private void getCode(){
        url= "https://www.kuaizhan.com/common/encode-png?large=true&data="+getUrl;
        final Request request=new Request.Builder().url(url).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()){
                        Log.d( "kkk","success" );
                        EventBusCarrier eventBusCarrier = new EventBusCarrier();
                        eventBusCarrier.setEventType( "1" );
                        eventBusCarrier.setObject( "receive" );
                        EventBus.getDefault().post( eventBusCarrier );
                    }else {
                        Log.e("lll", "run: "+response.code()+response.message());
                    }
//                    Log.i("lll", "run: ");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

//        new Handler().postDelayed( new Runnable() {
//            @Override
//            public void run() {
//                BitmapUtil._LoadImageByUrl(ImageView.ScaleType.CENTER_CROP, imgQrCode, url, DisplayUtil.getWindowWidth(QrCodeActivity.this), DisplayUtil.getWindowHeight(QrCodeActivity.this));
//            }
//        }, 2000);
//        BitmapUtil._LoadImageByUrl(ImageView.ScaleType.CENTER_CROP, imgQrCode, "https://www.kuaizhan.com/common/encode-png?large=true&data=http://121.46.26.224:8088/xxx/xxx.html?packagesId=383&promotionId=270&number=17043130002&sign=7C364573C74811D55CDDD59B2ED82303&dataTime=2019-05-22%2017:51:23&userName=app2", DisplayUtil.getWindowWidth(QrCodeActivity.this), DisplayUtil.getWindowHeight(QrCodeActivity.this));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventBusCarrier carrier) {
        String content = (String) carrier.getObject();
        String msg = content;
        switch (msg) {
            case "receive":
//                Bitmap bitmap = getHttpBitmap(url);
//                imgQrCode .setImageBitmap(bitmap); //设置Bitmap
                BitmapUtil.LoadImageByUrl( imgQrCode, url);

                break;
        }
    }

    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
//            Log.d(TAG, url);
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); //解除注册
    }
}
