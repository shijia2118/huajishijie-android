package com.test.tworldapplication.activity.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.card.MessageCollectionActivity;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import wintone.passport.sdk.utils.AppManager;

public class DialogActivity extends Activity {
    String from = "";
    @BindView(R.id.tvToast)
    TextView tvToast;
    @BindView(R.id.imgToast)
    ImageView imgToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra("from");
        switch (from) {
            case "2":
                /**/
                tvToast.setText("购买成功!");
                imgToast.setBackgroundResource(R.drawable.shape_buy_background);
                break;
            case "3":
                /*开户*/
                tvToast.setText("开户成功!");
                imgToast.setBackgroundResource(R.drawable.shape_buy_background);
                break;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppManager.getAppManager().finishActivityBesides(MainNewActivity.class);
//                Util.gotoActy(DialogActivity.this, MainNewActivity.class);
            }
        },1500);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                Message message = new Message();
////                message.what = 1;
//                //handler.sendMessageDelayed(message, 1000);
//            }
//        }).start();

    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            setResult(RESULT_OK, new Intent());
//            finish();
//        }
//    };
}
