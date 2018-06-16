package com.example.kimilm.timetable;

import android.graphics.drawable.Drawable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FriendsItem {
    boolean chk;
    private String Name;
    private String id;
    private ArrayList<Lesson> table;

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

    public ArrayList<Lesson> getTable() {
        return table;
    }

    public void setTable(ArrayList<Lesson> lessons) {
        this.table.addAll(lessons);
    }
}
