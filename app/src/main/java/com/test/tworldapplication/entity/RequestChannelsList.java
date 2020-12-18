package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/21.
 */

public class RequestChannelsList {
    private Integer count;
    private Organization[] channels;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Organization[] getChannels() {
        return channels;
    }

    public void setChannels(Organization[] channels) {
        this.channels = channels;
    }
}
