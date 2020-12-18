package com.test.tworldapplication.entity;

/**
 * Created by dasiy on 17/5/20.
 */

public class PostPreRandomNumber {
    private String session_token;
    private Integer numberpool;
    private String ruleNameId;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public Integer getNumberpool() {
        return numberpool;
    }

    public void setNumberpool(Integer numberpool) {
        this.numberpool = numberpool;
    }

    public String getRuleNameId() {
        return ruleNameId;
    }

    public void setRuleNameId(String ruleNameId) {
        this.ruleNameId = ruleNameId;
    }
}
