package com.example.kimilm.timetable;


import java.io.Serializable;
import java.util.ArrayList;

public class Friend implements Serializable
{
    String Id; // 아이디
    String Pw;
    String Name; // 이름
    //String pNum; // 폰번호 -> 추후에 추가할지 말지 고려하자.
//    TimeTable table; // 갖고있는 시간표  //어플리캐이션으로 띄워둠ㅠㅠㅠ 망한코드
    ArrayList<String> frList;  // 새로 추가한 친구의 아이디를 저장하는 어레이리스트

//    public Friend(String id, String pw, String name, TimeTable table, ArrayList<String> frList) {
//        Id = id;
//        Pw = pw;
//        Name = name;
////        this.table = ;
//        this.frList = new ArrayList<String>(frList);
//    }
    public Friend(String id, String pw, String name, ArrayList<String> frList) {
        Id = id;
        Pw = pw;
        Name = name;
//        this.table = ;
        this.frList = new ArrayList<String>(frList);
    }

    public Friend() {
        frList = new ArrayList<String>();
    }

    public Friend(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public String getPw() {
        return Pw;
    }

    public String getName() {
        return Name;
    }

    /*public String getpNum() {
        return pNum;
    }*/

//    public TimeTable getTable() {
//        return table;
//    }

    public ArrayList<String> getFrList() {
        return frList;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setPw(String pw) {
        Pw = pw;
    }

    public void setName(String name) {
        Name = name;
    }

    /*public void setpNum(String pNum) {
        this.pNum = pNum;
    }*/

//    public void setTable(TimeTable table) {
//        this.table = table;
//    }

    public void setFrList(ArrayList<String> frList) {
        this.frList = new ArrayList<String>(frList);
    }
}
