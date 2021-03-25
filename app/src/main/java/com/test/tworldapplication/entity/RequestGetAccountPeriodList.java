package com.test.tworldapplication.entity;

import java.util.List;

public class RequestGetAccountPeriodList {


    /**
     * amountSUM : 246.2
     * accountPeriod : [{"amount":123,"id":2,"tel":"123","openTime":"123","accountPeriod":"1"},{"amount":123.2,"id":4,"tel":"12","openTime":"123131","accountPeriod":"1"}]
     */

    private String amountSUM;
    private List<AccountPeriodBean> accountPeriod;

    public String getAmountSUM() {
        return amountSUM;
    }

    public void setAmountSUM(String amountSUM) {
        this.amountSUM = amountSUM;
    }

    public List<AccountPeriodBean> getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(List<AccountPeriodBean> accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public static class AccountPeriodBean {
        /**
         * amount : 123.0
         * id : 2
         * tel : 123
         * openTime : 123
         * accountPeriod : 1
         */

        private String amount;
        private String id;
        private String tel;
        private String openTime;
        private String accountPeriod;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getAccountPeriod() {
            return accountPeriod;
        }

        public void setAccountPeriod(String accountPeriod) {
            this.accountPeriod = accountPeriod;
        }
    }
}
