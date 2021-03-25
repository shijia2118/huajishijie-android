package com.test.tworldapplication.entity;

import java.util.List;

public class RequestGetSonOrderList {

    private List<order> order;
    private String count;

    public List<RequestGetSonOrderList.order> getOrder() {
        return order;
    }

    public void setOrder(List<RequestGetSonOrderList.order> order) {
        this.order = order;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public class order{
        private String orderNo;
        private String number;
        private String acceptUser;
        private String statusName;
        private String startTime;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getAcceptUser() {
            return acceptUser;
        }

        public void setAcceptUser(String acceptUser) {
            this.acceptUser = acceptUser;
        }


        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }


        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
    }
}
