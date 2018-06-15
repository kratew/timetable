package com.example.kimilm.timetable;

import android.graphics.drawable.Drawable;

public class FriendsItem {
    boolean chk;
    private String Name;
    private String id;
    private TimeTable table;

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

    public TimeTable getTable() {
        return table;
    }

    public void setTable(TimeTable table) {
        this.table = table;
    }
}
