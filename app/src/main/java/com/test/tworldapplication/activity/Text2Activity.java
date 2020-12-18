package com.test.tworldapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alipay.sdk.app.PayTask;
import com.test.tworldapplication.R;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Text2Activity extends AppCompatActivity {
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("sss", "sss");
//            Result result = new Result((String) msg.obj);
//            Toast.makeText(DemoActivity.this, result.getResult(),
//                    Toast.LENGTH_LONG).show();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text2);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btClick)
    public void onClick() {
        final String ur = "https://openapi.alipay.com/gateway.do?app_id=2018040202488101&biz_content=%7B%22out_trade_no%22%3A%221524824243.42%22%2C%22product_code%22%3A%22FAST_INSTANT_TRADE_PAY%22%2C%22total_amount%22%3A1.0%2C%22subject%22%3A%22%5Cu6d4b%5Cu8bd5%5Cu8ba2%5Cu5355%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fprojectsedus.com%2F&return_url=http%3A%2F%2Fwestlake.xiyoukeji.com%2F&sign_type=RSA2&timestamp=2018-04-27+18%3A17%3A23&version=1.0&sign=ISWoSm3H0MVXU14A%2FgRwsIE1PPhgXXpl6chgWfMZT9gGYDR%2Be%2F7oJV5a29dr3xpvZLywld8gwyRjrFYCDLZZDQYUnA%2BP%2FTO2aJtvM3nu0xhMG%2Bn%2B%2FPJqTs6onqXD5Reg3TV%2F5nxpTbBKTvV%2FOg17SQl3TD1JhiPk0l1tbSQ1mRSXGiz60gGoTsXEvINI2CUQzYRVmDAhrxtSe0Fw5Am11ahV2EGPBWTMg82G5jBCFaDxhUba%2B9Lecv3ZVXW8fbMXF7snBu8VQoj5TlZz9XgWWoDu%2FYDv9sYp9hkIzluQJY%2FN1T3g23Gjnf7HtJA5j5nTd044VTuH94seYSHNIm1HgA%3D%3D";
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(Text2Activity.this);
                Map result = alipay.payV2(ur, true);
                Log.d("sss", "sss");
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
//        Intent intent = new Intent(Text2Activity.this, DialogActivity.class);
//        intent.putExtra("from", "");
//        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                Toast.makeText(Text2Activity.this, "sdfg", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
