package com.example.kimilm.timetable;

import org.bson.Document;

import java.util.ArrayList;

//리스트뷰
public class FriendsItem {
    boolean chk;
    private String Name;
    private String id;
    private ArrayList<Document> lessons;

    public FriendsItem()
    {
        lessons = new ArrayList<>();
    }

    public boolean isChk() {
        return chk;
    }

    public void setChk(boolean chk) {
        this.chk = chk;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Document> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Document> lessons) {
        this.lessons.addAll(lessons);
    }
}
