package com.test.tworldapplication.activity.card;

import android.os.Bundle;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;

import wintone.passport.sdk.utils.AppManager;

public class OrderSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    AppManager.getAppManager().finishActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
