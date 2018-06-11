package com.example.kimilm.timetable;

import android.graphics.drawable.Drawable;

public class FriendsItem {
    private Drawable Photo;
    private String Name;
    private String PhoneNum;
    private String Major;
    private String Club;

    public void setPhoto(Drawable photo){
        this.Photo = Photo;
    }

    public void setName(String Name){
        this.Name = Name;
    }

    public void setPhoneNum(String PhoneNum){
        this.PhoneNum = PhoneNum;
    }

    public void setMajor(String Major){
        this.Major = Major;
    }

    public void setClub(String Club){
        this.Club = Club;
    }

    public Drawable getPhoto(){
        return this.Photo;
    }

    public String getName(String Name){
        return this.Name;
    }

    public String getPhoneNum(String PhoneNum){
        return this.PhoneNum;
    }

    public String getMajor(String Major) {
        return this.Major;
    }

    public String getClub(String Club){
        return this.Club;
    }
}
