package com.forecast.forecast.models;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Step extends BmobObject implements Serializable {

    private String UserId;
    private String StepNumber;
    private BmobDate Time;
    private String Distance;
    private String Quantity;


    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getStepNumber() {
        return StepNumber;
    }

    public void setStepNumber(String stepNumber) {
        StepNumber = stepNumber;
    }

    public BmobDate getTime() {
        return Time;
    }

    public void setTime(BmobDate time) {
        Time = time;
    }
}
