package com.test.tworldapplication.entity;

import java.util.List;

/**
 * Created by dasiy on 17/5/20.
 */

public class RequestPreRandomNumber {
    private Integer count;
    private List<PreRandomNumber> numbers;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<PreRandomNumber> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<PreRandomNumber> numbers) {
        this.numbers = numbers;
    }
}
