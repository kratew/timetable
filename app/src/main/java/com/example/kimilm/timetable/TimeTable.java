package com.example.kimilm.timetable;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kimilm on 2018. 5. 1..
 */

public class TimeTable extends Application
{
    public static TimeTableFragment fragment;
    public static FragmentStatePagerAdapter fragmentStatePagerAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //월-금, 9시-22시, 5분 단위로 중복 검사
    private static boolean jungBok [] = new boolean[5 * 14 * 12];

    //각 시간표별 강의 입력
    static ArrayList<Lesson> lessons = new ArrayList<>();

    public TimeTable ()
    {
        Arrays.fill(jungBok, false);
    }

    public TimeTable (TimeTable timeTable)
    {
        //며용
    }

    //false -> 강의 추가 실패, true -> 강의 추가 성공
    public static boolean addLesson (Lesson lesson)
    {
        for (int i = 0; i < lessons.size(); ++i)
        {
            if(lessons.get(i).title.contains(lesson.title))
            {
                return false;
            }
        }

        for (int i = 0; i < lesson.times.size(); ++i)
        {
            if (isJungBok(lesson.times.get(i)))
            {
                return false;
            }
        }

        for (int i = 0; i < lesson.times.size(); ++i)
        {
            setJungBok(lesson.times.get(i), true);
        }

        lessons.add(lesson);

        return true;
    }

    //학수번호로 lesson 삭제
    public static void delLesson (String code)
    {
        Lesson lesson = null;

        for(int i = 0; i < lessons.size(); ++i)
        {
            if(code.equals(lessons.get(i).code))
            {
                lesson = lessons.get(i);
            }
        }

        for (int i = 0; i < lesson.times.size(); ++i)
        {
            setJungBok(lesson.times.get(i), false);
        }

        lessons.remove(lesson);
    }

    //리턴이 true면 중복임
    private static boolean isJungBok (String times)
    {
        int day;
        int [] setTime;

        day = dayToInt(times.substring(0, 1));
        setTime = timeToInt(times.substring(1));

        for (int i = day + setTime[0]; i <= day + setTime[1]; ++i)
        {
            if(jungBok[i])
            {
                return true;
            }
        }

        return false;
    }

    private static void setJungBok (String times, boolean set)
    {
        int day;
        int [] setTime;

        day = dayToInt(times.substring(0, 1));
        setTime = timeToInt(times.substring(1));

        for (int i = day + setTime[0]; i <= day + setTime[1]; ++i)
        {
            jungBok[i] = set;
        }
    }

    //jungBok 배열의 날짜 시작 위치 리턴 ex) 월 -> 0 ~ 167, 화 -> 168 ~ 335
    private static int dayToInt (String day)
    {
        switch (day)
        {
            case "월":
                return 0;
            case "화":
                return 168;
            case "수":
                return 336;
            case "목":
                return 504;
            case "금":
                return 672;
        }
        return 0;
    }

    private static int [] timeToInt (String time)
    {
        int temp;

        //A, B, C, D, E 교시
        if (time.charAt(0) >= 65)
        {
            temp = 6 * (1 + 3 * (time.charAt(0) - 65));

            return new int [] { temp, temp + 17 };
        }
        //1 ~ 14 교시
        else
        {
            temp = (Integer.parseInt(time) - 1) * 12;

            return new int [] { temp, temp + 11 };
        }
    }

    public static void showLesson (Lesson lesson)
    {

    }
}
