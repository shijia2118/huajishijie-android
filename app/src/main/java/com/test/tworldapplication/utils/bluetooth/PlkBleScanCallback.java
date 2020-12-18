package com.test.tworldapplication.utils.bluetooth;


import java.util.ArrayList;

/**
 * Created by dasiy on 16/11/14.
 */

public interface PlkBleScanCallback {
    void onScannedWithDevice(PlkBleDeviceInfo var1);

    void onScannedFinish(ArrayList<PlkBleDeviceInfo> var1);

    void onScannedFailed(String var1);
}
