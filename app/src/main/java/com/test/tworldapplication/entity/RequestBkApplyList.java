package com.test.tworldapplication.entity;

import java.util.List;

public class RequestBkApplyList {
    private Integer count;
    private List<LiangAgents> wcardApply;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<LiangAgents> getWcardApply() {
        return wcardApply;
    }

    public void setWcardApply(List<LiangAgents> wcardApply) {
        this.wcardApply = wcardApply;
    }
}
