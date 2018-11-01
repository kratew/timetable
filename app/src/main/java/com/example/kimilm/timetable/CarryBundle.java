package com.example.kimilm.timetable;

import android.os.Bundle;

//프래그먼트 -> 액티비티로 Document ArrayList를 넘겨야 하는데
//Intent를 사용하면 해쉬맵 구조를 잘 변환하지 못하여 데이터 전달용 클래스를 하나 생성함
public class CarryBundle
{
    public static Bundle bundle = new Bundle();
}
