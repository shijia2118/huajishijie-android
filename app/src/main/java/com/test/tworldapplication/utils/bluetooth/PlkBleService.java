//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.test.tworldapplication.utils.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class PlkBleService {
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning = false;
    private Handler mHandler;
    private boolean mTransmiting;
    private boolean recevedAllResp;
    private boolean timeOut;
    private boolean exception;
    private boolean noPlkBle;
    private String respStr;
    private boolean registedReceiver = false;
    private boolean boundServerce = false;
    private static PlkBluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;
    private String mDeviceName;
    BluetoothGattCharacteristic writeCharacteristic;
    BluetoothGattCharacteristic notifyCharacteristic;
    private static int bleStatus;
    private String TAG = "PlkBleService";
    private static final int BLE_STATUS_NOT_SUPPORT = -1;
    private static final int BLE_STATUS_INAVALID = 0;
    private static final int BLE_STATUS_AVALID = 1;
    private static final int BLE_STATUS_SCANNED = 2;
    private static final int BLE_STATUS_CONNNECTED = 3;
    private static final int BLE_STATUS_RESETING = 4;
    private static final int BLE_STATUS_RESETED = 5;
    private static final int BLE_STATUS_TRANSMITING = 6;
    private static final int BLE_STATUS_CLOSING = 7;
    private static final int EXCEPTION_TYPE_NO_EXCEPTION = 0;
    private static final int EXCEPTION_TYPE_NO_CARD = 1;
    private static final int EXCEPTION_TYPE_TIME_OUT = 2;
    private static final int EXCEPTION_TYPE_INVALID_DATA = 3;
    private static final int EXCEPTION_TYPE_EXCEPTION = 4;
    private static final int EXCEPTION_TYPE_EXCEPTION_NO_PLK_BLE = 5;
    protected PlkBleScanCallback scanCallback;
    protected PlkBleConnectCallback connCallback;
    private static final long SCAN_PERIOD = 10000L;
    private static final int REQUEST_ENABLE_BT = 170;
    private PlkBleDataContainer dc;
    private LeScanCallback mLeScanCallback = new LeScanCallback() {
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            System.err.println("scanned: " + device.getName());

            int i;
            for (i = 0; i < PlkBleService.this.dc.mLeDevices.size() && !((PlkBleDeviceInfo) PlkBleService.this.dc.mLeDevices.get(i)).getDevice().getAddress().equals(device.getAddress()); ++i) {
                ;
            }

            if (i == PlkBleService.this.dc.mLeDevices.size()) {
                PlkBleDeviceInfo pbdi = new PlkBleDeviceInfo();
                pbdi.setDevice(device);
                pbdi.setRssi(rssi);
                PlkBleService.this.dc.mLeDevices.add(pbdi);
                PlkBleService.this.scanCallback.onScannedWithDevice(pbdi);
            }

        }
    };
    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(PlkBleService.this.TAG, "BroadcastReceiver onReceive:" + action);
            if ("com.example.plkbluetoothlesdk.ACTION_GATT_CONNECTED".equals(action)) {
                PlkBleService.this.mBluetoothLeService.discoverService();
            } else if ("com.example.plkbluetoothlesdk.ACTION_GATT_DISCONNECTED".equals(action)) {
                PlkBleService.bleStatus = 2;
                PlkBleService.this.connCallback.onConnectLost();
            } else if ("com.example.plkbluetoothlesdk.le.ACTION_GATT_SERVICES_DISCOVERED".equals(action)) {
                if (PlkBleService.this.isPlkBleDevice(PlkBleService.this.mBluetoothLeService.getSupportedGattServices())) {
                    PlkBleService.bleStatus = 3;
                    PlkBleService.this.connCallback.onConnectSuccess();
                } else {
                    PlkBleService.this.mBluetoothLeService.disconnect();
                    PlkBleService.this.noPlkBle = true;
                    PlkBleService.this.connCallback.onConnectFailed("连接的设备不是(PLK-LE)读卡器,请选择连接其他设备！");
                }
            } else if ("com.example.plkbluetoothlesdk.ACTION_DATA_AVAILABLE".equals(action)) {
                byte[] received_bytes = PlkUtilTool.hexStringToByteArray(intent.getStringExtra("com.example.plkbluetoothlesdk.EXTRA_DATA"));
                PlkBleService.this.received_data(received_bytes);
            }

        }
    };
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            PlkBleService.this.mBluetoothLeService = ((PlkBluetoothLeService.LocalBinder) service).getService();
            if (!PlkBleService.this.mBluetoothLeService.initialize()) {
                PlkBleService.this.exception = true;
            } else {
                PlkBleService.this.mBluetoothLeService.connect(PlkBleService.this.mDeviceAddress);
            }

        }

        public void onServiceDisconnected(ComponentName componentName) {
            PlkBleService.this.mBluetoothLeService = null;
            if (PlkBleService.bleStatus == 6) {
                PlkBleService.bleStatus = 2;
            }

        }
    };

    public PlkBleService(Context context) {
        bleStatus = -1;
        this.mContext = context;
        this.dc = PlkBleDataContainer.getInstance();
        this.mHandler = new Handler();
        this.initial_bluetooth_ble();
    }

    private boolean initial_bluetooth_ble() {
        bleStatus = -1;
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return false;
        } else {
            BluetoothManager bluetoothManager = (BluetoothManager) this.mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            this.mBluetoothAdapter = bluetoothManager.getAdapter();
            if (this.mBluetoothAdapter == null) {
                return false;
            } else {
                bleStatus = 0;
                if (!this.mBluetoothAdapter.isEnabled()) {
                    return false;
                } else {
                    bleStatus = 1;
                    return true;
                }
            }
        }
    }

    private void stopLeScan() {
        this.mScanning = false;
        this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
        if (this.dc.mLeDevices.size() == 0) {
            this.scanCallback.onScannedFailed("很报歉，未收到附近的蓝牙BLE设备，请稍候重试！");
        } else {
            Log.e("stopLeScan", "stopLeScan");
            if (bleStatus == 1) {
                bleStatus = 2;
                Collections.sort(this.dc.mLeDevices);
                this.scanCallback.onScannedFinish(this.dc.mLeDevices);
            }
        }

    }

    private void scanLeDevice(boolean enable) {
        if (enable) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    PlkBleService.this.stopLeScan();
                }
            }, 10000L);
            this.mScanning = true;
            bleStatus = 1;
            this.dc.mLeDevices.clear();
            this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
        } else {
            this.stopLeScan();
        }

    }

    public void scanBleDevice(PlkBleScanCallback callback) {
        this.scanCallback = callback;
        if (bleStatus < 2) {
            this.initial_bluetooth_ble();
        }

        if (bleStatus == -1) {
            this.scanCallback.onScannedFailed("该手机不支持蓝牙BLE！");
        } else if (bleStatus == 0) {
            this.scanCallback.onScannedFailed("请先打开蓝牙！");
        } else {
            this.scanLeDevice(true);
        }

    }

    private void connectDevice(PlkBleConnectCallback conn) throws PlkException {
        if (this.mScanning) {
            this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
            this.mScanning = false;
        }

        Intent gattServiceIntent = new Intent(this.mContext, PlkBluetoothLeService.class);
        this.mContext.bindService(gattServiceIntent, this.mServiceConnection, Context.BIND_AUTO_CREATE);
        this.boundServerce = true;
        this.mContext.registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
        this.registedReceiver = true;
        this.timeOut = false;
        this.exception = false;
        this.noPlkBle = false;
        this.connCallback = conn;
        if (this.mBluetoothLeService != null) {
            boolean result = this.mBluetoothLeService.connect(this.mDeviceAddress);
            if (!result) {
                throw new PlkException("设备连接失败，请稍候重试！");
            }
        }

    }

    public void connectDevice(PlkBleDeviceInfo deviceInfo, PlkBleConnectCallback conn) throws PlkException {
        this.mDeviceAddress = deviceInfo.getDevice().getAddress();
        this.mDeviceName = deviceInfo.getDevice().getName();
        this.connectDevice(conn);
    }

    public void connectDevice(int id, PlkBleConnectCallback conn) throws PlkException {
        this.mDeviceAddress = ((PlkBleDeviceInfo) this.dc.mLeDevices.get(id)).getDevice().getAddress();
        this.mDeviceName = ((PlkBleDeviceInfo) this.dc.mLeDevices.get(id)).getDevice().getName();
        this.connectDevice(conn);
    }

    private boolean connSyncTask() throws PlkException {
        ExecutorService executor = Executors.newCachedThreadPool();
        FutureTask futureTask = new FutureTask(new Callable() {
            public Integer call() throws Exception {
                int i = 0;

                while (true) {
                    TimeUnit.MILLISECONDS.sleep(10L);
                    ++i;
                    Log.e(PlkBleService.this.TAG, "connecting:" + i);
                    if (PlkBleService.bleStatus == 3 || PlkBleService.this.exception || PlkBleService.this.timeOut || PlkBleService.this.noPlkBle) {
                        break;
                    }

                    if (i >= 1000) {
                        PlkBleService.this.timeOut = true;
                        break;
                    }
                }

                return PlkBleService.this.exception ? Integer.valueOf(4) : (PlkBleService.this.timeOut ? Integer.valueOf(2) : (PlkBleService.bleStatus == 3 ? Integer.valueOf(0) : (PlkBleService.this.noPlkBle ? Integer.valueOf(5) : Integer.valueOf(4))));
            }
        });
        executor.submit(futureTask);
        executor.shutdown();

        try {
            int e = ((Integer) futureTask.get()).intValue();
            if (e == 0) {
                return true;
            }

            if (e == 2) {
                throw new PlkException("连接超时，请稍候重试！");
            }

            if (e == 5) {
                throw new PlkException("连接的设备不是(PLK-LE)读卡器,请选择连接其他设备！");
            }

            throw new PlkException("发生未知异常！");
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        } catch (ExecutionException var5) {
            var5.printStackTrace();
        }

        return false;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.plkbluetoothlesdk.ACTION_GATT_CONNECTED");
        intentFilter.addAction("com.example.plkbluetoothlesdk.ACTION_GATT_DISCONNECTED");
        intentFilter.addAction("com.example.plkbluetoothlesdk.le.ACTION_GATT_SERVICES_DISCOVERED");
        intentFilter.addAction("com.example.plkbluetoothlesdk.ACTION_DATA_AVAILABLE");
        return intentFilter;
    }

    private void clearBeforeSend() {
        this.mTransmiting = true;
        this.recevedAllResp = false;
        this.timeOut = false;
        this.exception = false;
        this.respStr = null;
        this.dc.queue.clear();
        this.dc.receivedData.clear();
        PlkUtilTool.arrayFillWithByte(this.dc.resp_received_flag, 0, this.dc.resp_received_flag.length, (byte) 0);
    }

    private void transmit(byte cmdType, byte[] data, short off, short len) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                PlkBleService.this.stopTransmit();
            }
        }, 10000L);
        this.clearBeforeSend();
        if (cmdType != 98 && cmdType != 110 && cmdType != 82) {
            byte blocks = PlkBleApp.getBlocksByApduCmdLen(len);
            byte i = 0;

            do {
                byte[] apdu = PlkBleApp.getNthBleCmd(data, off, len, i);
                Log.e(this.TAG, "Send apdu:" + PlkUtilTool.byteArrayToHexString(apdu));
                this.writeCharacteristic.setValue(apdu);
                this.mBluetoothLeService.writeCharacteristic(this.writeCharacteristic);
                ++i;
            } while (i < blocks);
        } else {
            Log.e(this.TAG, "Send apdu:" + PlkUtilTool.byteArrayToHexString(data));
            this.writeCharacteristic.setValue(data);
            this.mBluetoothLeService.writeCharacteristic(this.writeCharacteristic);
        }

        this.dc.cmdByte = cmdType;
    }

    private void setNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        int charaProp = characteristic.getProperties();
        if ((charaProp | 16) > 0) {
            Log.e(this.TAG, "setNotification:" + enable);
            this.mBluetoothLeService.setCharacteristicNotification(characteristic, enable);
        }

    }

    public String transmitDataSync(String hexString) throws PlkException {
//        if (bleStatus < 5) {
//            throw new PlkException("连接的设备未上电,请先上电！");
//        } else {
        String apduStr = hexString.replaceAll(" ", "");
        if (apduStr.length() >= 10 && apduStr.length() % 2 == 0) {
            bleStatus = 6;
            Object apdu = null;

            byte[] apdu1;
            try {
                apdu1 = PlkUtilTool.hexStringToByteArray(apduStr);
            } catch (Exception var5) {
                throw new PlkException("发送的命令数据有误！");
            }

            this.transmit((byte) 111, apdu1, (short) 0, (short) apdu1.length);
            String res = this.transmitSyncTask();
            return res;
        } else {
            throw new PlkException("发送的命令数据有误！");
        }
    }
//    }

    private void stopTransmit() {
        if (this.mTransmiting) {
            if (!this.recevedAllResp) {
                Log.e(this.TAG, "disconnect by code on transmit data!!!");
                this.mBluetoothLeService.disconnect();
            }

            this.mTransmiting = false;
        }

        this.timeOut = true;
    }

    protected void received_data(byte[] received_bytes) {
        String received_data = PlkUtilTool.byteArrayToHexString(received_bytes);
        if (received_data.equals("00636E81")) {
            this.recevedAllResp = true;
            this.mTransmiting = false;
            this.respStr = received_data;
        } else {
            if ((received_bytes[0] & -64) == 0 && (received_bytes[1] != this.dc.cmdByte + 1 || received_bytes[2] != -112 || received_bytes[3] != 0)) {
                this.mTransmiting = false;
                Log.e(this.TAG, "接收数据格式有误，连接将关闭");
                this.mBluetoothLeService.disconnect();
                this.exception = true;
                return;
            }

            boolean addSuccess = PlkBleApp.addReceivedBlock(this.dc.receivedData, received_bytes);
            if (addSuccess) {
                this.recevedAllResp = PlkBleApp.isReceivedAllResp(this.dc.receivedData);
                Log.e(this.TAG, "recevedAllResp:" + this.recevedAllResp);
                if (this.recevedAllResp) {
                    this.mTransmiting = false;
                    this.respStr = PlkBleApp.getReceivedData(this.dc.receivedData);
                    Log.e(this.TAG, "received resp:" + this.respStr);
                }
            }
        }

    }

    public boolean isPlkBleDevice(List<BluetoothGattService> gattServices) {
        Iterator var3 = gattServices.iterator();

        while (var3.hasNext()) {
            BluetoothGattService gattService = (BluetoothGattService) var3.next();
            String uuid = gattService.getUuid().toString();
            System.err.println("gattService.getUuid: " + uuid);
            if (uuid.equals("0000fff0-0000-1000-8000-00805f9b34fb")) {
                List gattCharacteristics = gattService.getCharacteristics();
                byte chaFlag = 0;
                Iterator var8 = gattCharacteristics.iterator();

                do {
                    if (!var8.hasNext()) {
                        return false;
                    }

                    BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) var8.next();
                    uuid = characteristic.getUuid().toString();
                    System.err.println("characteristic.getUuid: " + uuid);
                    if (uuid.equals("0000fff1-0000-1000-8000-00805f9b34fb")) {
                        this.writeCharacteristic = characteristic;
                        chaFlag = (byte) (chaFlag | 1);
                    } else if (uuid.equals("0000fff2-0000-1000-8000-00805f9b34fb")) {
                        this.notifyCharacteristic = characteristic;
                        this.setNotification(this.notifyCharacteristic, true);
                        chaFlag = (byte) (chaFlag | 2);
                    }
                } while (chaFlag != 3);

                return true;
            }
        }

        return false;
    }

    private String transmitSyncTask() throws PlkException {
        ExecutorService executor = Executors.newCachedThreadPool();
        FutureTask futureTask = new FutureTask(new Callable() {
            public Integer call() throws Exception {
                int i = 0;

                do {
                    byte[] received_bytes = (byte[]) PlkBleService.this.dc.queue.take();
                    if (received_bytes != null) {
                        PlkBleService.this.received_data(received_bytes);
                    }

                    ++i;
                    if (i >= 2000) {
                        PlkBleService.this.timeOut = true;
                    }
                }
                while (!PlkBleService.this.recevedAllResp && !PlkBleService.this.exception && !PlkBleService.this.timeOut);

                return PlkBleService.this.exception ? Integer.valueOf(3) : (PlkBleService.this.timeOut ? Integer.valueOf(2) : (PlkBleService.this.recevedAllResp ? (PlkBleService.this.respStr.equals("00636E81") ? Integer.valueOf(1) : Integer.valueOf(0)) : Integer.valueOf(4)));
            }
        });
        executor.submit(futureTask);
        executor.shutdown();

        try {
            int e = ((Integer) futureTask.get()).intValue();
            if (e == 1) {
                throw new PlkException("请放上卡片！");
            }

            if (e == 2) {
                throw new PlkException("响应超时，连接将关闭！");
            }

            if (e == 3) {
                throw new PlkException("接收数据格式有误，连接将关闭！");
            }

            if (e == 0) {
                if (this.respStr == null) {
                    throw new PlkException("接收数据格式有误，连接将关闭！");
                }

                return this.respStr;
            }

            throw new PlkException("发生未知异常！");
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        } catch (ExecutionException var5) {
            var5.printStackTrace();
        }

        return null;
    }

    public String resetDevice() throws PlkException {
        Log.e("resetDevice", String.valueOf(bleStatus));
        bleStatus = 4;
        this.transmit((byte) 98, PlkBleConstant.PLK_BLE_PROTOCOL_RESET, (short) 0, (short) 2);
        String res = this.transmitSyncTask();
        if (res != null) {
            bleStatus = 5;
        }

        return res;
    }

    public boolean closeSession() throws PlkException {
        if (bleStatus < 5) {
            throw new PlkException("设备未开启！");
        } else {
            bleStatus = 7;
            this.transmit((byte) 110, PlkBleConstant.PLK_BLE_PROTOCOL_CLOSE, (short) 0, (short) 2);
            String res = this.transmitSyncTask();
            if (res != null && res.equals("006F9000")) {
                bleStatus = 5;
                return true;
            } else {
                return false;
            }
        }
    }

    public void disConnect() throws PlkException {
        if (bleStatus < 3) {
            throw new PlkException("还未连接设备！");
        } else {
            this.mBluetoothLeService.disconnect();
        }
    }

    public void destroy() {
        if (this.registedReceiver) {
            this.mContext.unregisterReceiver(this.mGattUpdateReceiver);
        }

        if (this.boundServerce) {
            this.mContext.unbindService(this.mServiceConnection);
        }

        this.mBluetoothLeService = null;
    }
}
