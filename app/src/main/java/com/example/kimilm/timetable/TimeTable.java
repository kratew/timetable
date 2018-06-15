package com.example.kimilm.timetable;

import android.Manifest;
import android.app.Application;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kimilm on 2018. 5. 1..
 */

public class TimeTable extends Application
{
    static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "MyFolder" + File.separator + "saveTable.json";

    public static TimeTableFragment fragment;

    //월-금, 9시-22시, 5분 단위로 중복 검사
    private static boolean jungBok [] = new boolean[5 * 14 * 12];

    //각 시간표별 강의 입력
    static ArrayList<Lesson> lessons = new ArrayList<>();

    private boolean sFlag = true;

    public TimeTable ()
    {
        Arrays.fill(jungBok, false);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        File file = new File(filePath);

        if(file.exists() && sFlag)
        {
            sFlag = false;

            try {
                String temp = new String();

                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new FileReader(file));

                while ((temp = reader.readLine()) != null){
                    buffer.append(temp);
                }

                BasicDBObject timetableObject = BasicDBObject.parse(buffer.toString());

                lessons.clear();

                int i = 0;
                for(Object obj : (BasicDBList)(timetableObject.get("jungbok")))
                {
                    jungBok[i++] = Boolean.getBoolean(obj.toString());
                }

                ArrayList<String> [] strArray = new ArrayList[2];

                for(BasicDBObject dbo : (List<BasicDBObject>)timetableObject.get("lessons"))
                {
                    strArray[0] = TimeTableFragment.toSubString(dbo.get("times").toString());
                    strArray[1] = TimeTableFragment.toSubString(dbo.get("classroom").toString());

                    lessons.add(new Lesson(dbo.get("_id").toString(), dbo.get("title").toString(),
                            dbo.get("classify").toString(), dbo.get("credit").toString(),
                            strArray[0], dbo.get("prof").toString(),
                            strArray[1], Color.BLUE));
                }

                reader.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //저장된 강의 정보 가져올 때 사용 (예정)
    public TimeTable (TimeTable timeTable)
    {

    }

    public boolean[] getJungBok() {
        return jungBok;
    }

    public void setJungBok(boolean[] jungBok) {
        this.jungBok = jungBok;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    //false -> 강의 추가 실패, true -> 강의 추가 성공
    public static boolean addLesson (Lesson lesson)
    {
        // 강의 이름이 같다면 추가 불가, (원어 강의) 같은 접미사 체크하기 위해 두 번 검사
        for (int i = 0; i < lessons.size(); ++i)
        {
            if(lessons.get(i).title.contains(lesson.title))
            {
                return false;
            }
            if(lesson.title.contains(lessons.get(i).title))
            {
                return false;
            }
        }

        // 동일한 시간대가 존재한다면 추가 불가
        for (int i = 0; i < lesson.times.size(); ++i)
        {
            if (isJungBok(lesson.times.get(i)))
            {
                return false;
            }
        }

        // 둘 다 아니라면 강의 추가 가능
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

        // 학수번호에 해당하는 강의 뽑기
        for(int i = 0; i < lessons.size(); ++i)
        {
            if(code.equals(lessons.get(i).code))
            {
                lesson = lessons.get(i);
            }
        }

        // 삭제 전 중복 배열 세팅
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

    // 추가된 강의 자리에 boolean = ture;
    //강의 추가 또는 삭제시 중복 배열 세팅
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

    //jungBok 배열의 시간 정보 리턴 ex) 1교시 -> 0 ~ 11, 2교시 12 ~ 23
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

    // 강의 추가 또는 삭제시 로컬 파일에 저장 -> DBO 파일 리턴으로 바꿀 것
    public static void saveTable ()
    {
        if(ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != fragment.getActivity().getPackageManager().PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != fragment.getActivity().getPackageManager().PERMISSION_GRANTED)
        {
            Toast.makeText(fragment.getActivity(), "저장소 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(fragment.getActivity(), new String [] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }

        File file = new File(filePath);

        ArrayList<BasicDBObject> lessonObjList = new ArrayList<>();

        for(int i = 0; i < lessons.size(); ++i)
        {
            BasicDBObjectBuilder objBuilder = BasicDBObjectBuilder.start("_id", lessons.get(i).code)
                    .add("title", lessons.get(i).title)
                    .add("classify", lessons.get(i).classify)
                    .add("credit", lessons.get(i).credit)
                    .add("times", lessons.get(i).times)
                    .add("prof", lessons.get(i).prof)
                    .add("classroom", lessons.get(i).classroom);

            lessonObjList.add((BasicDBObject)objBuilder.get());
        }

        BasicDBObjectBuilder tableBuilder = BasicDBObjectBuilder
                .start("jungbok", jungBok).add("lessons", lessonObjList);

//                BasicDBObjectBuilder tableBuilder = BasicDBObjectBuilder.start("lessons", lessonObjList);

        BasicDBObject timeTableObj = (BasicDBObject)tableBuilder.get();

        //        BasicDBObject timeTableObj = new BasicDBObject("timetable", tableBuilder.get());

        try
        {
            FileWriter output = new FileWriter(file);

            output.write(timeTableObj.toJson());

            output.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
