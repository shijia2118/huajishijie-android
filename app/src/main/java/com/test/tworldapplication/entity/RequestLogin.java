package com.test.tworldapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dasiy on 16/10/31.
 */

public class RequestLogin implements Parcelable {
    private String session_token;
    private String grade;
    private String isLogin;
    private String isCompare;
    private String cardId;
    private String address;

    protected RequestLogin(Parcel in) {
        session_token = in.readString();
        grade = in.readString();
        isLogin = in.readString();
        isCompare = in.readString();
        cardId = in.readString();
        address = in.readString();
        name = in.readString();
        tel = in.readString();
        workAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(session_token);
        dest.writeString(grade);
        dest.writeString(isLogin);
        dest.writeString(isCompare);
        dest.writeString(cardId);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(tel);
        dest.writeString(workAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RequestLogin> CREATOR = new Creator<RequestLogin>() {
        @Override
        public RequestLogin createFromParcel(Parcel in) {
            return new RequestLogin(in);
        }

        @Override
        public RequestLogin[] newArray(int size) {
            return new RequestLogin[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    private String tel;
    private String workAddress;

    public String getIsCompare() {
        return isCompare;
    }

    public void setIsCompare(String isCompare) {
        this.isCompare = isCompare;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
