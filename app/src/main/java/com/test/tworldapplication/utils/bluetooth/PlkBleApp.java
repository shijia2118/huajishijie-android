package com.test.tworldapplication.utils.bluetooth;


import java.util.ArrayList;

/**
 * Created by dasiy on 16/11/14.
 */

public class PlkBleApp {
    private static final byte MAX_BYTES_PER_BLOCK = 20;
    private static final byte MAX_DATA_BYTES_PER_BLCK = 19;

    public PlkBleApp() {
    }

    protected static byte getBlocksByApduCmdLen(short len) {
        short length = (short)(len + 1);
        byte res = length % 19 == 0?(byte)(length / 19):(byte)(length / 19 + 1);
        return res;
    }

    protected static byte[] getNthBleCmd(byte[] apdu, short off, short len, byte nth) {
        Object res = null;
        byte blocks = getBlocksByApduCmdLen(len);
        byte[] res1;
        if(nth < blocks - 1) {
            res1 = new byte[20];
        } else {
            if(nth != blocks - 1) {
                return null;
            }

            int b_len = len + 1 - nth * 19 + 1;
            res1 = new byte[b_len];
        }

        if(nth == 0) {
            res1[0] = (byte)(blocks - 1);
            res1[1] = 111;
            PlkUtilTool.arrayCopy(apdu, off, res1, 2, res1.length - 2);
        } else {
            res1[0] = (byte)(-64 | nth);
            PlkUtilTool.arrayCopy(apdu, off + nth * 19 - 1, res1, 1, res1.length - 1);
        }

        return res1;
    }

    private static short getBlockNum(ArrayList<byte[]> receviedResp) {
        for(int i = 0; i < receviedResp.size(); ++i) {
            byte byte0 = ((byte[])receviedResp.get(i))[0];
            if((byte0 & -64) == 0) {
                return (short)(PlkUtilTool.byte2Short((byte)(byte0 & 63)) + 1);
            }
        }

        return (short)0;
    }

    private static short getNthBlockByFirstByte(byte byte0) {
        return (byte)(byte0 & -64) == 0?0:PlkUtilTool.byte2Short((byte)(byte0 & 63));
    }

    protected static boolean isReceivedAllResp(ArrayList<byte[]> receviedResp) {
        short blockNums = getBlockNum(receviedResp);
        return blockNums != 0 && PlkUtilTool.isBitAllSetWithValue(PlkBleDataContainer.getInstance().resp_received_flag, (short)0, blockNums, (byte)1);
    }

    protected static boolean addReceivedBlock(ArrayList<byte[]> receviedResp, byte[] resp) {
        int i = 0;
        boolean t_nth = false;

        short var5;
        for(var5 = getNthBlockByFirstByte(resp[0]); i < receviedResp.size(); ++i) {
            short r_nth = getNthBlockByFirstByte(((byte[])receviedResp.get(i))[0]);
            if(r_nth == var5) {
                return false;
            }
        }

        if(i == receviedResp.size()) {
            receviedResp.add(resp);
            PlkUtilTool.setBitValueByIndex(PlkBleDataContainer.getInstance().resp_received_flag, (short)0, var5, (byte)1);
        }

        return true;
    }

    protected static String getReceivedData(ArrayList<byte[]> receviedResp) {
        byte[] resp_buff = PlkBleDataContainer.getInstance().apdu_buff;
        short dataLen = 0;

        for(short i = 0; i < receviedResp.size(); ++i) {
            byte[] arr = (byte[])receviedResp.get(i);
            byte byte0 = arr[0];
            short nth = getNthBlockByFirstByte(byte0);
            if(nth == 0) {
                PlkUtilTool.arrayCopy(arr, 4, resp_buff, 0, arr.length - 4);
                dataLen = (short)(dataLen + (arr.length - 4));
            } else {
                PlkUtilTool.arrayCopy(arr, 1, resp_buff, (nth - 1) * 19 + 16, arr.length - 1);
                dataLen = (short)(dataLen + (arr.length - 1));
            }
        }

        return PlkUtilTool.byteArrayToHexString(resp_buff, 0, dataLen);
    }
}
