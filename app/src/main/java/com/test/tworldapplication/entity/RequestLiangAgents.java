package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 17/5/15.
 */

public class RequestLiangAgents {
    private Integer count;
    private List<LiangAgent> LiangAgents;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<LiangAgent> getLiangAgents() {
        return LiangAgents;
    }

    public void setLiangAgents(List<LiangAgent> liangAgents) {
        LiangAgents = liangAgents;
    }
}
