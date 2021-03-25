package com.test.tworldapplication.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.card.MessageCollectionNewActivity2;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.utils.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import kernal.idcard.android.RecogParameterMessage;
import kernal.idcard.android.RecogService;
import kernal.idcard.android.ResultMessage;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import wintone.passport.sdk.utils.Devcode;
import wintone.passport.sdk.utils.SharedPreferencesHelper;

public class TestOrcActivity extends BaseActivity {

    TextView tv1;
    TextView tv2;
    TextView tv3;
    String filePath;
    private String result;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_orc);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        findViewById(R.id.bt).setOnClickListener(view -> {
            dialog.getTvTitle().setText("正在扫描,请稍后");
            dialog.show();
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Exception {
                    AssetManager am = getAssets();
                    bitmap = BitmapFactory.decodeStream(am.open("test.jpg"));
                    String[] path = null;
                    try {
                        path = am.list("");  // ""获取所有,填入目录获取该目录下所有资源
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    List<String>  t = getAssetPicPath(TestOrcActivity.this);
                    filePath = t.get(0);
                    emitter.onNext("");
                }
            }).subscribeOn(Schedulers.io())
                    .subscribe(o -> {
                        tv1.postDelayed(() -> {
                           test();
                        }, 200);
                    });
        });

    }

    public static List<String> getAssetPicPath(Context context){
        AssetManager am = context.getAssets();

        String[] path = null;
        try {
            path = am.list("");  // ""获取所有,填入目录获取该目录下所有资源
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> pciPaths = new ArrayList<>();
        for(int i = 0; i < path.length; i++){
            if (path[i].endsWith(".jpg")){  // 根据图片特征找出图片
                pciPaths.add(path[i]);
            }
        }
        return pciPaths;
    }
    public RecogService.recogBinder recogBindViewer;

    private void test() {
        RecogService.nMainID = SharedPreferencesHelper.getInt(
                getApplicationContext(), "nMainId", 2);
        RecogService.isRecogByPath = true;
        Intent recogIntent = new Intent(this,
                RecogService.class);

        bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
    }

    /**
     * 将asset文件写入缓存
     */
    private boolean copyAssetAndWrite(String fileName) {
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
            }
            InputStream is = getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            filePath = outFile.getAbsolutePath();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String recogResultString = "";
    private String[] splite_Result;

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
            rpm.devcode = Devcode.devcode;
            // nProcessType：0-取消所有操作，1－裁切，
            // 2－旋转，3－裁切+旋转，4－倾斜校正，5－裁切+倾斜校正，6－旋转+倾斜校正，7－裁切+旋转+倾斜校正
            rpm.nProcessType = 7;
            rpm.nSetType = 1;// nSetType: 0－取消操作，1－设置操作
            rpm.bitmap = bitmap;
//            rpm.lpFileName = filePath; // rpm.lpFileName当为空时，会执行自动识别函数
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
//                    spit(recogResultString);
////                    disPlayImage(selectPath);
//                    tv2.setText(splite_Result[0].substring(3));
//                    tv3.setText(splite_Result[4].substring(3));
//                    tv1.setText(splite_Result[5].substring(7));
//                    recogResultString = "";
//                    dialog.dismiss();
                    tv1.setText(recogResultString);
                } else {
                    tv2.setText("");
                    tv1.setText("");
                    tv3.setText("");

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
                    Util.createToast(TestOrcActivity.this, string);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        getString(R.string.recognized_failed),
                        Toast.LENGTH_SHORT).show();

            } finally {
                dialog.dismiss();
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

}
