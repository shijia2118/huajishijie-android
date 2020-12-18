package com.test.tworldapplication.utils.bluetooth;

/**
 * Created by dasiy on 16/11/14.
 */

public class PlkBleConstant {
    protected static final byte CMD_CODE_RESET = 98;
    protected static final byte CMD_CODE_SEND = 111;
    protected static final byte CMD_CODE_CLOSE = 110;
    protected static final byte CMD_CODE_DEBUG = 82;
    protected static final int SIGNAL_INTENSITY_PER_METER = 78;
    protected static final float ENVIRONMENTAL_ATTENUATION_FACTOR = 2.0F;
    protected static final byte[] PLK_BLE_PROTOCOL_RESET = new byte[]{(byte)0, (byte)98};
    protected static final byte[] PLK_BLE_PROTOCOL_CLOSE = new byte[]{(byte)0, (byte)110};
    protected static final byte[] PLK_BLE_PROTOCOL_DEBUG = new byte[]{(byte)0, (byte)82};
    protected static final String MSG_STRING_BLE_NOT_SUPPORT = "该手机不支持蓝牙BLE！";
    protected static final String MSG_STRING_BLE_NOT_AVAILABLE = "请先打开蓝牙！";
    protected static final String MSG_STRING_BLE_SCANNED_WITH_NO_DEVICE = "很报歉，未收到附近的蓝牙BLE设备，请稍候重试！";
    protected static final String MSG_STRING_BLE_CONNECT_FAILED = "设备连接失败，请稍候重试！";
    protected static final String MSG_STRING_BLE_ON_CONNECTING = "正在连接设备，请稍候！";
    protected static final String MSG_STRING_BLE_CONNECTED_NOT_PLK_LE = "连接的设备不是(PLK-LE)读卡器,请选择连接其他设备！";
    protected static final String MSG_STRING_BLE_UN_RESETTED = "连接的设备未上电,请先上电！";
    protected static final String MSG_STRING_BLE_APDU_ERROR = "发送的命令数据有误！";
    protected static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    protected static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    protected static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    protected static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    protected static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    protected static final String UUID_PLK_BLE_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
    protected static final String UUID_PLK_BLE_SELF_NOTIFY_VALUE = "0000fff2-0000-1000-8000-00805f9b34fb";
    protected static final String UUID_PLK_BLE_WRITE_WITHOUT_RESPONSE = "0000fff1-0000-1000-8000-00805f9b34fb";
    protected static final String UUID_PLK_BLE_DESCRIPTOR_IN_NOTIFY_CHARACTERISTIC = "00002902-0000-1000-8000-00805f9b34fb";

    public PlkBleConstant() {
    }
}
