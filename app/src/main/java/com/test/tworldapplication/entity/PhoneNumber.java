package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 16/10/20.
 */

public class PhoneNumber {
    private String regular;
    private String number;
    private String prestore;
    private String remarks;
    public PhoneNumber(String regular,String number,String prestore,String remarks){
        this.regular = regular;
        this.number = number;
        this.prestore = prestore;
        this.remarks = remarks;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrestore() {
        return prestore;
    }

    public void setPrestore(String prestore) {
        this.prestore = prestore;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
