package com.test.tworldapplication.entity;

import android.content.Intent;

import java.util.List;

/**
 * Created by dasiy on 2019/8/15.
 */

public class RequestPreNumberNew {
    private Integer count;
    private List<Number> numbers;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Number> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Number> numbers) {
        this.numbers = numbers;
    }
}
