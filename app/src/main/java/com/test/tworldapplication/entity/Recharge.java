package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/10/18.
 */
public class Recharge {
    String prime;
    String current;

    public Recharge(String prime,String current) {
        this.prime = prime;
        this.current = current;
    }

    public String getPrime() {
        return prime;
    }

    public void setPrime(String prime) {
        this.prime = prime;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}
