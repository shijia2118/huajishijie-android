package com.test.tworldapplication.base;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.test.tworldapplication.entity.MessageEvent;
import com.test.tworldapplication.galleryfinal.UILImageLoader;
import com.test.tworldapplication.galleryfinal.UILPauseOnScrollListener;
import com.test.tworldapplication.utils.CrashHandler;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.util.Calendar;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by 27733 on 2016/10/11.
 */
public class MyApplication extends MultiDexApplication {
    public static MyApplication context;
    public static FunctionConfig functionConfig;
    private static int activityAount;
    public static OSS oss;
    boolean isForeground = true;
    public static int mTodayYear, mTodayMonth, mTodayDay;

    @Override
    public void onCreate() {
        super.onCreate();
        context = MyApplication.this;
//        Bugly.init(getApplicationContext(), "70b1f0463b", true);
        CrashReport.initCrashReport(getApplicationContext(), "46086ea2c5", true);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        initGalleryFinal();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        Calendar calendar = Calendar.getInstance();
        mTodayYear = calendar.get(Calendar.YEAR);
        mTodayMonth = calendar.get(Calendar.MONTH);
        mTodayDay = calendar.get(Calendar.DAY_OF_MONTH);

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(OSSConfig.ACCESSKEYID, OSSConfig.ACCESSKEYSECRET);

        //使用自己的获取STSToken的类
//        OSSCredentialProvider credentialProvider = new STSGetter(stsServer);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        oss = new OSSClient(getApplicationContext(), OSSConfig.ENDPOINT, credentialProvider, conf);
//        CrashHandler.getInstance().init(this);

    }

    public static Application getInstance() {
        return context;
    }

    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {

            if (activityAount == 0 && !isForeground) {
                //app回到前台
                isForeground = true;
                EventBus.getDefault().post(new MessageEvent(MessageEvent.START_LOCATE, ""));

            }
            activityAount++;


        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityAount--;

            if (activityAount == 0) {
                isForeground = false;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initGalleryFinal() {
        //galleryFinal
        ThemeConfig theme = new ThemeConfig.Builder()
                .build();
        //配置功能
        functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true).setEnableEdit(true).setEnableRotate(true).setEnablePreview(true).build();
//                .setEnableCrop(true).setEnableRotate(true).setEnablePreview(true).setForceCrop(true).setForceCropEdit(true).build();
        //配置imageloader
        ImageLoader imageLoader = new UILImageLoader();
        initImageLoader();
        PauseOnScrollListener pauseOnScrollListener = new UILPauseOnScrollListener(false, true);
        //设置核心配置信息
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.discCacheFileNameGenerator(new Md5FileNameGenerator());
        config.discCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

}
