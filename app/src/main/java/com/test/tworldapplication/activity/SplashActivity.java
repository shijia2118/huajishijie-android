package com.test.tworldapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;

import wintone.passport.sdk.utils.AppManager;

public class SplashActivity extends BaseActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SharedPreferences share = getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
            String session = share.getString("gride", "");
            if (!session.equals("")) {
                Intent intent = new Intent(SplashActivity.this, MainNewActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        handler.sendEmptyMessageDelayed(0, 1500);
    }

}
