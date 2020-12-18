package com.test.tworldapplication.entity;

import java.util.ArrayList;
import java.util.List;

public class ProductBean {
    private List<ProdsEntity> voiceProds = new ArrayList<>();
    private List<ProdBean> dataProds = new ArrayList<>();
    private List<ProdsEntity> privilegeProds = new ArrayList<>();

    public List<ProdsEntity> getVoiceProds() {
        return voiceProds;
    }

    public void setVoiceProds(List<ProdsEntity> voiceProds) {
        this.voiceProds = voiceProds;
    }

    public List<ProdBean> getDataProds() {
        return dataProds;
    }

    public void setDataProds(List<ProdBean> dataProds) {
        this.dataProds = dataProds;
    }

    public List<ProdsEntity> getPrivilegeProds() {
        return privilegeProds;
    }

    public void setPrivilegeProds(List<ProdsEntity> privilegeProds) {
        this.privilegeProds = privilegeProds;
    }
}
