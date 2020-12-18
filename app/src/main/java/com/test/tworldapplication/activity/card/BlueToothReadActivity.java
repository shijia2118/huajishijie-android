package com.test.tworldapplication.activity.card;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.plk.bluetoothlesdk.PlkBleDeviceInfo;
import com.plk.bluetoothlesdk.PlkBleScanCallback;
import com.plk.bluetoothlesdk.PlkBleService;
import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.DeviceAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.view.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class BlueToothReadActivity extends BaseActivity implements PlkBleScanCallback {
    ListView pairedListView;
    DeviceAdapter deviceAdapter;
    PlkBleService bleService;
    MyProgressDialog progressBar;
    private List<PlkBleDeviceInfo> deviceInfoList;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        ButterKnife.bind(this);
        pairedListView = (ListView) findViewById(R.id.paired_devices);
        deviceAdapter = new DeviceAdapter(this);
        pairedListView.setAdapter(deviceAdapter);
        bleService = new PlkBleService(BlueToothReadActivity.this);

        deviceAdapter.setList(null);
        deviceAdapter.notifyDataSetChanged();

        progressBar = new MyProgressDialog(BlueToothReadActivity.this, "请稍候",
                "请在搜索附近的蓝牙4.0设备...", ProgressDialog.STYLE_SPINNER, mHandler);
        progressBar.show();

        bleService.scanBleDevice(this);
        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("aaa", deviceInfoList.get(position).getDevice().getAddress());
                Log.d("aaa", deviceInfoList.get(position).getDevice().getName());
                Intent intent = new Intent();
                intent.putExtra("device_name", deviceInfoList.get(position).getDevice().getName());
                intent.putExtra(EXTRA_DEVICE_ADDRESS, deviceInfoList.get(position).getDevice().getAddress());

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                AppManager.getAppManager().finishActivity();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }

    @Override
    protected void onDestroy() {
        bleService.destroy();
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what != MyProgressDialog.HANDLER_MSG_TYPE_ON_PROCESSING) {
                if (progressBar != null) {
                    progressBar.dismss();
                }
            }

            switch (msg.what) {
                case MyProgressDialog.HANDLER_MSG_TYPE_SUCCESS:
                    deviceInfoList = (ArrayList<PlkBleDeviceInfo>) (msg.obj);
                    deviceAdapter.setList(deviceInfoList);
                    deviceAdapter.notifyDataSetChanged();
                    break;

                case MyProgressDialog.HANDLER_MSG_TYPE_ON_PROCESSING:
                    break;

                case MyProgressDialog.HANDLER_MSG_TYPE_CANCELD:
                case MyProgressDialog.HANDLER_MSG_TYPE_EXCEPTION:
                case MyProgressDialog.HANDLER_MSG_TYPE_FAILED:
                case MyProgressDialog.HANDLER_MSG_TYPE_TIMEOUT:
                default:
                    Toast.makeText(BlueToothReadActivity.this, (String) msg.obj, Toast.LENGTH_LONG)
                            .show();
                    break;
            }

            if (msg.what != MyProgressDialog.HANDLER_MSG_TYPE_ON_PROCESSING)
                progressBar.setFinishAndNoNeedReport();
        }
    };


    @Override
    public void onScannedWithDevice(PlkBleDeviceInfo plkBleDeviceInfo) {

    }

    @Override
    public void onScannedFinish(ArrayList<PlkBleDeviceInfo> arrayList) {
        postMessage(mHandler, MyProgressDialog.HANDLER_MSG_TYPE_SUCCESS,
                arrayList);
    }

    @Override
    public void onScannedFailed(String s) {
        postMessage(mHandler, MyProgressDialog.HANDLER_MSG_TYPE_EXCEPTION, s);
    }

    public static void postMessage(Handler handler, int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    @OnClick(R.id.button_scan)
    public void onClick() {
        deviceAdapter.setList(null);
        deviceAdapter.notifyDataSetChanged();

        progressBar = new MyProgressDialog(BlueToothReadActivity.this, "请稍候",
                "请在搜索附近的蓝牙4.0设备...", ProgressDialog.STYLE_SPINNER, mHandler);
        progressBar.show();

        bleService.scanBleDevice(this);
    }
}
