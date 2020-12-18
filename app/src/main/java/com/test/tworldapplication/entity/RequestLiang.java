package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 17/5/15.
 */

public class RequestLiang {
    private Integer count;
    private List<LiangRule> LiangRule;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<com.test.tworldapplication.entity.LiangRule> getLiangRule() {
        return LiangRule;
    }

    public void setLiangRule(List<com.test.tworldapplication.entity.LiangRule> liangRule) {
        LiangRule = liangRule;
    }
}
