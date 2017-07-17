package com.example.parktaejun.sixsense.DataClass;

/**
 * Created by parktaejun on 2017. 7. 17..
 */

public class SMSContentData {
    private String phoneNum, displayName, body;

    public SMSContentData() {

    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }
}
