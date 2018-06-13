package com.example.kimilm.timetable;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    RelativeLayout container;
    ViewPager viewPager;
    TabLayout tabLayout;
    MyPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentAdapter = new MyPagerAdapter(getSupportFragmentManager());

        container = (RelativeLayout)findViewById(R.id.container);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentAdapter);  // viewPager에 Adapter 설정

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);    // tabLayout을 ViewPager와 연동.
        tabLayout.addOnTabSelectedListener(this);   // tabLayout의 이벤트 핸들러 등록.

        //얘를 기본값으로 못 바꾸나?
        //getSupportActionBar().setElevation(0);  // 액션바 그림자 제거.
    }

    /* OnTabSelectedListener의 콜백 메소드.
         - TabLayout의 탭 버튼을 사용자가 터치했을 때 이벤트를 처리하기 위한 콜백 메소드로,
           탭 버튼과 ViewPager 화면 조정을 setCurrentItem() 메소드로 처리.*/
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /* TabLayout과 연동하기 위한 ViewPager의 Adapter 클래스 선언 */
    class MyPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragments=new ArrayList<>();//fragments ArrayList

        //탭 버튼 문자열 배열
        String title[]=new String[]{"", ""};

        //Adapter 생성자
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
            //프래그먼트를 생성하여 ArrayList에 add
            fragments.add(new TimeTableFragment());
            fragments.add(new FriendsFragment());

            TimeTable.fragment = (TimeTableFragment) fragments.get(0);
        }

        /* 실제 ViewPager에서 보여질 프래그먼트를 반환
           - 일반 Adapter(리스트뷰 등)의 getView()와 같은 역할
           - @param position : ViewPager에서 보여져야할 페이지 값(0부터)
           - @return : 보여질 fragment
        */
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        //ViewPager에 보여질 총 페이지 수
        @Override
        public int getCount() {
            return fragments.size();
        }

        //getPageTitle() 메서드에서 반환한 문자열이 TabLayout의 버튼 문자열로 사용됨
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}


