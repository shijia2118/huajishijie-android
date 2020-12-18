package com.test.tworldapplication.utils.bluetooth;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by dasiy on 16/11/14.
 */

public class PlkBleDataContainer {
    private static PlkBleDataContainer pbdc;
    protected byte cmdByte;
    protected byte[] apdu_buff = new byte[261];
    protected byte[] resp_received_flag = new byte[20];
    protected ArrayList<PlkBleDeviceInfo> mLeDevices = new ArrayList();
    protected ArrayList<byte[]> receivedData = new ArrayList();
    protected byte[] received_bytes;
    BlockingQueue<byte[]> queue = new ArrayBlockingQueue(5);

    protected PlkBleDataContainer() {
    }

    protected static PlkBleDataContainer getInstance() {
        if(pbdc == null) {
            pbdc = new PlkBleDataContainer();
        }

        return pbdc;
    }

    protected void destroy() {
        pbdc = null;
    }
}
