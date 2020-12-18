package com.test.tworldapplication.activity.other;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.test.tworldapplication.inter.DataCallBack;

public class MessageService extends Service {
    private MessageBinder mBinder = new MessageBinder();

    // 获取消息线程
    private MessageThread messageThread = null;
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class MessageBinder extends Binder {
        //        Activity activity;

        public void start(DataCallBack callBack) {
            messageThread = new MessageThread();
            messageThread.isRunning = true;
            messageThread.setCallBack(callBack);
            messageThread.start();
//            handler.postDelayed(runnable, 2000);
        }

    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Log.d("tag", msg.obj.toString());
//        }
//    };
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
////            Log.d("tag", ".........");
//            String serverMessage = getServerMessage();
//            if (serverMessage != null && !"".equals(serverMessage)) {
//                Message message = new Message();
//                message.obj = serverMessage;
//                handler.sendMessage(message);
//            }
//
//
//            handler.postDelayed(this, 5000);
//        }
//    };

    /**
     * 从服务器端获取消息
     */
    class MessageThread extends Thread {
        private DataCallBack callBack;

        public DataCallBack getCallBack() {
            return callBack;
        }

        public void setCallBack(DataCallBack callBack) {
            this.callBack = callBack;
        }

        // 设置是否循环推送
        public boolean isRunning = true;

        public void run() {
//             while (isRunning) {
            try {
                // 间隔时间
//                Log.d("tag", "..............");
                boolean isChange = true;

                callBack.dataChange(isChange);
                Thread.sleep(20000);

            } catch (Exception e) {
                e.printStackTrace();
            }
//             }
        }
    }

    @Override
    public void onDestroy() {
        // System.exit(0);
        messageThread.isRunning = false;
        super.onDestroy();
    }

    /**
     * 模拟发送消息
     *
     * @return 返回服务器要推送的消息，否则如果为空的话，不推送
     */
    public String getServerMessage() {
        return "NEWS!";
    }
}