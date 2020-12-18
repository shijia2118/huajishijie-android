package com.test.tworldapplication.utils.bluetooth;

/**
 * Created by dasiy on 16/11/14.
 */

public interface PlkBleConnectCallback {
    void onConnectSuccess();

    void onConnectFailed(String var1);

    void onConnectLost();
}
