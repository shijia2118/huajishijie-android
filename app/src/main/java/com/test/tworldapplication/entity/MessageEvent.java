package com.test.tworldapplication.entity;

public class MessageEvent {
    public static final int LOCATION_MESSAGE = 10001;
    public static final int CHECK_PERMISSION = 10002;
    public static final int START_LOCATE = 10003;
    public static final int STOP_COUNT = 10004;
    public static final int START_COUNT = 10005;
    public static final int BACK_TO_HOME = 10006;

    private int flag;
    private String message;

    public MessageEvent(int flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
