package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 2018/4/25.
 */

public class RequestWinningOrder {
    private Integer count;
    private List<LotteryRecord> LotteryRecord;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<LotteryRecord> getLotteryRecord() {
        return LotteryRecord;
    }

    public void setLotteryRecord(List<LotteryRecord> lotteryRecord) {
        LotteryRecord = lotteryRecord;
    }
}
