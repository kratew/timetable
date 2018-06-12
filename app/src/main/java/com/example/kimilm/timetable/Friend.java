package com.example.kimilm.timetable;

import java.util.ArrayList;

public class Friend {
    String Id; // 아이디
    String Name; // 이름
    String pNum; // 폰번호
    TimeTable table; // 친구 시간표
    ArrayList<String> frList;  // 새로 추가한 친구의 아이디를 저장하는 어레이리스트

    public Friend(String id, String pw, String name, String pNum, TimeTable table) {
        Id = id;
        Name = name;
        this.pNum = pNum;
        this.table = table;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getpNum() {
        return pNum;
    }

    public TimeTable getTable() {
        return table;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }

    public void setTable(TimeTable table) {
        this.table = table;
    }
}
