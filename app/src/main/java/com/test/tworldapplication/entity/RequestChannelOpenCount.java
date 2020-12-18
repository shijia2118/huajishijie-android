package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/11/21.
 */

public class RequestChannelOpenCount {
    private Integer count;
    private ChannelsOpen[] channelsOpenCount;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ChannelsOpen[] getChannelsOpenCount() {
        return channelsOpenCount;
    }

    public void setChannelsOpenCount(ChannelsOpen[] channelsOpenCount) {
        this.channelsOpenCount = channelsOpenCount;
    }
}
