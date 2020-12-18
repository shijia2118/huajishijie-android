package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 16/11/9.
 */

public class RequestImsi implements Serializable{
    private String imsi;
    private String smscent;
    private String simId;
    private String prestore;
    private Package[] packages;
    private String iccid;

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getSmscent() {
        return smscent;
    }

    public void setSmscent(String smscent) {
        this.smscent = smscent;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getPrestore() {
        return prestore;
    }

    public void setPrestore(String prestore) {
        this.prestore = prestore;
    }

    public Package[] getPackages() {
        return packages;
    }

    public void setPackages(Package[] packages) {
        this.packages = packages;
    }
}
