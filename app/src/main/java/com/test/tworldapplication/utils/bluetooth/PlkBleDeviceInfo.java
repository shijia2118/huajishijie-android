package com.test.tworldapplication.utils.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.plk.bluetoothlesdk.PlkUtilTool;

/**
 * Created by dasiy on 16/11/14.
 */

public class PlkBleDeviceInfo implements Comparable {
    private BluetoothDevice device;
    private int rssi;
    private float distance;

    public PlkBleDeviceInfo() {
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
        this.setDistance(rssi);
    }

    private void setDistance(int rssi) {
        int iRssi = Math.abs(rssi);
        float power = (float) (iRssi - 78) / 20.0F;
        this.distance = (float) PlkUtilTool.doubleRound(Math.pow(10.0D, (double) power), 2, 5);
    }

    public float getDistance() {
        return this.distance;
    }

    public int compareTo(Object o) {
        byte result = 0;
        if (o instanceof com.plk.bluetoothlesdk.PlkBleDeviceInfo) {
            com.plk.bluetoothlesdk.PlkBleDeviceInfo pbdi = (com.plk.bluetoothlesdk.PlkBleDeviceInfo) o;
            if (pbdi.getDistance() > this.getDistance()) {
                return -1;
            }

            if (pbdi.getDistance() < this.getDistance()) {
                return 1;
            }
        }

        return result;
    }
}
