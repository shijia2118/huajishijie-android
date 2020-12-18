package com.test.tworldapplication.entity;

import java.io.Serializable;
import java.util.List;

public class RequestAgencyGetBill implements Serializable{

    /**
     * accountName : 顾**
     * others : [{"name":"呼转国内通话费","value":"5.25"}]
     * basic : [{"name":"基础资费包新","value":"4.90"},{"name":"迷你模组5元来显","value":"5.00"}]
     * sum : 15.15
     * costItem : 基础资费包新
     * billingCycle : 201805
     * accountTel : 17091500119
     * meal : 9.9
     */

    private String accountName;
    private String sum;
    private String costItem;
    private String billingCycle;
    private String accountTel;
    private String meal;
    private List<OthersBean> others;
    private List<BasicBean> basic;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getCostItem() {
        return costItem;
    }

    public void setCostItem(String costItem) {
        this.costItem = costItem;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getAccountTel() {
        return accountTel;
    }

    public void setAccountTel(String accountTel) {
        this.accountTel = accountTel;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public List<OthersBean> getOthers() {
        return others;
    }

    public void setOthers(List<OthersBean> others) {
        this.others = others;
    }

    public List<BasicBean> getBasic() {
        return basic;
    }

    public void setBasic(List<BasicBean> basic) {
        this.basic = basic;
    }

    public static class OthersBean implements Serializable{
        /**
         * name : 呼转国内通话费
         * value : 5.25
         */

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class BasicBean implements Serializable{
        /**
         * name : 基础资费包新
         * value : 4.90
         */

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
