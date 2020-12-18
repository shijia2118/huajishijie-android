package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/7.
 */

public class RequestNoticeList {
    private Notice[] notice;
    private Integer count;

    public Notice[] getNotice() {
        return notice;
    }

    public void setNotice(Notice[] notice) {
        this.notice = notice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
