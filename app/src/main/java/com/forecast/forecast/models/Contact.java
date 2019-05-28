package com.forecast.forecast.models;

import cn.bmob.v3.BmobObject;

public class Contact extends BmobObject {

    private String UserId;
    private String Name;
    private String Phone;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
