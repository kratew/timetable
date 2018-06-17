package com.example.kimilm.timetable;

import java.io.Serializable;
import java.util.ArrayList;

//계정 정보 클래스
public class Friend implements Serializable
{
    String Id;  //유저아이디
    String Pw;  //유저비밀번호
    String Name; //유저이름
    ArrayList<String> frList;  //친구 리스트

    public Friend(String id, String pw, String name, ArrayList<String> frList)
    {
        Id = id;
        Pw = pw;
        Name = name;
        this.frList = new ArrayList<String>(frList);
    }

    public Friend() { frList = new ArrayList<String>(); }

    public Friend(String id) { Id = id; }

    public String getId() { return Id; }

    public String getPw() { return Pw; }

    public String getName() { return Name; }

    public ArrayList<String> getFrList() { return frList; }

    public void setId(String id) { Id = id; }

    public void setPw(String pw) { Pw = pw; }

    public void setName(String name) { Name = name; }

    public void setFrList(ArrayList<String> frList) { this.frList.addAll(frList); }
}
