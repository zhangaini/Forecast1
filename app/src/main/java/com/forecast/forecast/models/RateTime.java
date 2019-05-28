package com.forecast.forecast.models;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class RateTime extends BmobObject implements Serializable {

    private String UserId;
    private String Number;
    private BmobDate Time;
    private String Score;

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public BmobDate getTime() {
        return Time;
    }

    public void setTime(BmobDate time) {
        Time = time;
    }
}
