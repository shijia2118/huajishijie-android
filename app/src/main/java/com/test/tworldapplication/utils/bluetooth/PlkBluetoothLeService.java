package com.test.tworldapplication.utils.bluetooth;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import java.util.List;
import java.util.UUID;

/**
 * Created by dasiy on 16/11/14.
 */
@SuppressLint({"NewApi"})
public class PlkBluetoothLeService extends Service {
    private static final String TAG = PlkBluetoothLeService.class.getSimpleName();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = 0;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    public static final String ACTION_GATT_CONNECTED = "com.example.plkbluetoothlesdk.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.plkbluetoothlesdk.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.plkbluetoothlesdk.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_DATA_AVAILABLE = "com.example.plkbluetoothlesdk.ACTION_DATA_AVAILABLE";
    public static final String EXTRA_DATA = "com.example.plkbluetoothlesdk.EXTRA_DATA";
    public static final UUID UUID_PLK_BLE_RESP = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == 2) {
                intentAction = "com.example.plkbluetoothlesdk.ACTION_GATT_CONNECTED";
                PlkBluetoothLeService.this.mConnectionState = 2;
                PlkBluetoothLeService.this.broadcastUpdate(intentAction);
                Log.i(PlkBluetoothLeService.TAG, "Connected to GATT server.");
            } else if (newState == 0) {
                intentAction = "com.example.plkbluetoothlesdk.ACTION_GATT_DISCONNECTED";
                PlkBluetoothLeService.this.mConnectionState = 0;
                Log.i(PlkBluetoothLeService.TAG, "Disconnected from GATT server.");
                PlkBluetoothLeService.this.broadcastUpdate(intentAction);
            }

        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
                Log.e(PlkBluetoothLeService.TAG, "onServicesDiscovered received: GATT_SUCCESS");
                PlkBluetoothLeService.this.broadcastUpdate("com.example.plkbluetoothlesdk.le.ACTION_GATT_SERVICES_DISCOVERED");
            } else {
                Log.w(PlkBluetoothLeService.TAG, "onServicesDiscovered received: " + status);
            }

        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == 0) {
                PlkBluetoothLeService.this.broadcastUpdate("com.example.plkbluetoothlesdk.ACTION_DATA_AVAILABLE", characteristic);
            }

        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == 0) {
                Log.e(PlkBluetoothLeService.TAG, "onCharacteristicWrite success!!!!!");
            }

        }

        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == 0) {
                Log.e(PlkBluetoothLeService.TAG, "onDescriptorRead success!!!!!");
            }

        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == 0) {
                Log.e(PlkBluetoothLeService.TAG, "onDescriptorWrite success!!!!!");
            }

        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == 0) {
                Log.e(PlkBluetoothLeService.TAG, "onReadRemoteRssi success!!!!!");
            }

        }

        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            if (status == 0) {
                Log.e(PlkBluetoothLeService.TAG, "onReliableWriteCompleted success!!!!!");
            }

        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.e(PlkBluetoothLeService.TAG, "onCharacteristicChanged.....");
            PlkBluetoothLeService.this.broadcastUpdate("com.example.plkbluetoothlesdk.ACTION_DATA_AVAILABLE", characteristic);
        }
    };
    private final IBinder mBinder = new PlkBluetoothLeService.LocalBinder();

    public PlkBluetoothLeService() {
    }

    public void onCreate() {
        Log.e(TAG, "SERVICE onCreate");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void broadcastUpdate(String action) {
        Intent intent = new Intent(action);
        this.sendBroadcast(intent);
    }

    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            String characteristicChanged = PlkUtilTool.byteArrayToHexString(data, 0, data.length);
            Log.e(TAG, "characteristicChanged: " + characteristicChanged);
            PlkBleDataContainer.getInstance().queue.add(data);
            Intent intent = new Intent(action);
            intent.putExtra("com.example.plkbluetoothlesdk.EXTRA_DATA", characteristicChanged);
        }

    }

    public IBinder onBind(Intent intent) {
        Log.e(TAG, "on service PlkBluetoothLeService bind");
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        this.close();
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
            if (this.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        } else {
            return true;
        }
    }

    public boolean connect(String address) {
        if (this.mBluetoothAdapter != null && address != null) {
            if (this.mBluetoothDeviceAddress != null && address.equals(this.mBluetoothDeviceAddress) && this.mBluetoothGatt != null) {
                Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
                if (this.mBluetoothGatt.connect()) {
                    this.mConnectionState = 1;
                    return true;
                } else {
                    return false;
                }
            } else {
                BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(address);
                if (device == null) {
                    Log.w(TAG, "Device not found.  Unable to connect.");
                    return false;
                } else {
                    this.mBluetoothGatt = device.connectGatt(this, false, this.mGattCallback);
                    Log.d(TAG, "Trying to create a new connection.");
                    this.mBluetoothDeviceAddress = address;
                    this.mConnectionState = 1;
                    return true;
                }
            }
        } else {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
    }

    protected void discoverService() {
        Log.i(TAG, "Attempting to start service discovery:" + this.mBluetoothGatt.discoverServices());
    }

    public void disconnect() {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            this.mBluetoothGatt.disconnect();
        } else {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
    }

    public void close() {
        if (this.mBluetoothGatt != null) {
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            this.mBluetoothGatt.readCharacteristic(characteristic);
        } else {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            this.mBluetoothGatt.writeCharacteristic(characteristic);
        } else {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
    }

    public void beginReliableWrite() {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            this.mBluetoothGatt.beginReliableWrite();
        } else {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
    }

    public void executeReliableWrite() {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            this.mBluetoothGatt.executeReliableWrite();
        } else {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
    }

    public void abortReliableWrite() {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            this.mBluetoothGatt.abortReliableWrite();
        } else {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            this.mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
            if (enabled) {
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                this.mBluetoothGatt.writeDescriptor(descriptor);
                Log.e(TAG, "write notification in descriptor");
            }

        } else {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        return this.mBluetoothGatt == null ? null : this.mBluetoothGatt.getServices();
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        PlkBluetoothLeService getService() {
            return PlkBluetoothLeService.this;
        }
    }
}
