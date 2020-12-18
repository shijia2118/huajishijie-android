package com.test.tworldapplication.activity.other;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.plk.bluetoothlesdk.PlkBleDeviceInfo;
import com.plk.bluetoothlesdk.PlkBleScanCallback;
import com.plk.bluetoothlesdk.PlkBleService;
import com.test.tworldapplication.R;
import com.test.tworldapplication.adapter.DeviceAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.view.MyProgressDialog;


public class ScanDeviceActivity extends BaseActivity implements OnClickListener, PlkBleScanCallback {
    private ListView lv_devices;
    private Button bt_scan;

    private static Context mContext;

    private DeviceAdapter deviceAdapter;
    private List<PlkBleDeviceInfo> deviceInfoList;

    private static MyProgressDialog progressBar;

    private String TAG = "plkbletest";

    private PlkBleService bleService;

    protected static final String EXTRAS_DEVICE_SELECTED_INDEX = "select_device_index";
    protected static final String EXTRAS_DEVICE_NAME = "select_device_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device);

        mContext = this;

        initial_ui();

        bleService = new PlkBleService(mContext);
    }

    private void initial_ui() {
        bt_scan = (Button) this.findViewById(R.id.button_scan);
        bt_scan.setOnClickListener(this);

        lv_devices = (ListView) this.findViewById(R.id.listview_devices);
        deviceAdapter = new DeviceAdapter(this);
        lv_devices.setAdapter(deviceAdapter);

        this.lv_devices.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final Intent intent = new Intent(mContext,
                        SendApduActivity.class);

                intent.putExtra(EXTRAS_DEVICE_SELECTED_INDEX, position);
                intent.putExtra(EXTRAS_DEVICE_NAME, deviceInfoList
                        .get(position).getDevice().getName());
                startActivity(intent);
            }
        });
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
                    Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_LONG)
                            .show();
                    break;
            }

            if (msg.what != MyProgressDialog.HANDLER_MSG_TYPE_ON_PROCESSING)
                progressBar.setFinishAndNoNeedReport();
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.button_scan) {

            deviceAdapter.setList(null);
            deviceAdapter.notifyDataSetChanged();

            progressBar = new MyProgressDialog(mContext, "请稍候",
                    "请在搜索附近的蓝牙4.0设备...", ProgressDialog.STYLE_SPINNER, mHandler);
            progressBar.show();

            bleService.scanBleDevice(this);
        }
    }

    public static void postMessage(Handler handler, int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }


    @Override
    protected void onDestroy() {
        System.err.println("ScanDeviceActivity onDestroy........");

        bleService.destroy();
        super.onDestroy();
    }

    @Override
    public void onScannedWithDevice(PlkBleDeviceInfo plkBleDeviceInfo) {
        Log.d("aaa", "1");
    }

    @Override
    public void onScannedFinish(ArrayList<PlkBleDeviceInfo> arrayList) {
        Log.d("aaa", "2");
        postMessage(mHandler, MyProgressDialog.HANDLER_MSG_TYPE_SUCCESS,
                arrayList);
    }

    @Override
    public void onScannedFailed(String msg) {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        Log.e(TAG, "onScannedFailed" + msg);

        postMessage(mHandler, MyProgressDialog.HANDLER_MSG_TYPE_EXCEPTION, msg);
    }
}
