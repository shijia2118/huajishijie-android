package com.test.tworldapplication.entity;

import java.io.Serializable;

/**
 * Created by dasiy on 17/5/20.
 */

public class PreRandomNumber implements Serializable{
    private String num;
    private String poolid;
    private String ltype;
    private String infos;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPoolid() {
        return poolid;
    }

    public void setPoolid(String poolid) {
        this.poolid = poolid;
    }

    public String getLtype() {
        return ltype;
    }

    public void setLtype(String ltype) {
        this.ltype = ltype;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }
}
