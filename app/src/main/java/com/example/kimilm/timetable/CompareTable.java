package com.example.kimilm.timetable;

import android.graphics.Point;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import org.bson.Document;

import java.util.ArrayList;

//비교한 시간표를 보여주는 액티비티
public class CompareTable extends AppCompatActivity
{
    GridLayout compareGrid;
    public static FrameLayout compareFrame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_table);

        setStatusBarColor();

        compareGrid = findViewById(R.id.compareGrid);
        compareFrame = findViewById(R.id.compareframe);

        setGridLayoutHeight();

        //인텐트에 번들을 담아 넘기면 해쉬맵 구조가 깨지는 문제 발생
        //데이터 전달용 번들 클래스 생성
        for(int i = 0; i < CarryBundle.bundle.size(); ++i)
        {
            for(Document doc : (ArrayList<Document>)CarryBundle.bundle.getSerializable(String.valueOf(i)))
            {
                TimeTable.fragment.showTable(TimeTableFragment.parseLesson(doc, false), (byte) 2, false);
            }
        }

        //혹시 모르니 다음 사용을 위해 초기화
        CarryBundle.bundle.clear();

        for(Lesson lesson : TimeTable.lessons)
        {
            TimeTable.fragment.showTable(lesson, (byte) 2, false);
        }

    }

    //상태바 색상 변경
    public void setStatusBarColor()
    {
        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(getResources().getColor(R.color.color8));
    }

    //시간표의 크기는 화면의 크기와 동일하게
    public void setGridLayoutHeight()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = compareGrid.getLayoutParams();
        params.height = size.y;
        compareGrid.setLayoutParams(params);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        finish();
    }
}
