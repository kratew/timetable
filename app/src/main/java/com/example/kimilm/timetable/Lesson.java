package com.example.kimilm.timetable;

import android.support.annotation.ColorInt;

import java.util.ArrayList;

//시간표 정보를 담을 클래스
public class Lesson
{
    String code;      //학수번호
    String title;     //강의명
    String classify;  //전선, 전필, 교양
    String credit;          //학점
    ArrayList<String> times;    //강의시간
    String prof;      //교수
    ArrayList<String> classroom;       //강의실

    @ColorInt
    int color;     //시간표에 표시할 색상

    public Lesson ()
    {
        times = new ArrayList<>();
        classroom = new ArrayList<>();
    }

    public Lesson (String title, ArrayList<String> times, String prof)
    {
        this();

        this.title = title;
        this.times.addAll(times);
        this.prof = prof;
    }


    public Lesson (String code, String title, String classify, String credit, ArrayList<String> times, String prof, ArrayList<String> classroom, @ColorInt int color)
    {
        this();

        this.code = code;
        this.title = title;
        this.classify = classify;
        this.credit = credit;
        this.times.addAll(times);
        this.prof = prof;
        this.classroom.addAll(classroom);
        this.color = color;
    }

    public Lesson (Lesson lesson)
    {
        this();

        this.code = lesson.getCode();
        this.title = lesson.getTitle();
        this.classify = lesson.getClassify();
        this.credit = lesson.getCredit();
        this.times.addAll(getTimes());
        this.prof = lesson.getProf();
        this.classroom.addAll(getClassroom());
        this.color = lesson.getColor();
    }

    public String getCode() { return code; }

    public String getTitle() { return title; }

    public String getClassify() { return classify; }

    public String getCredit() { return credit; }

    public ArrayList<String> getTimes() { return times; }

    public String getProf() { return prof; }

    public ArrayList<String> getClassroom() { return classroom; }

    public int getColor() { return color; }

    public void setColor(int color) { this.color = color; }
}
