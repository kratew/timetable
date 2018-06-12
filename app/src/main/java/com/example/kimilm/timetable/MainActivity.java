package com.example.kimilm.timetable;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    RelativeLayout container;
    ViewPager viewPager;
    TabLayout tabLayout;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (RelativeLayout) findViewById(R.id.container);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));  // viewPager에 Adapter 설정

        drawer = (DrawerLayout)findViewById(R.id.drawer);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);    // tabLayout을 ViewPager와 연동.
        tabLayout.addOnTabSelectedListener(this);   // tabLayout의 이벤트 핸들러 등록.

        //얘를 기본값으로 못 바꾸나?
        //getSupportActionBar().setElevation(0);  // 액션바 그림자 제거.

        // MainActivity에 NavigationDrawer 설정하는 코드 ↓
        //getSupportActionBar().setDisplayShowTitleEnabled(false);    // ActionBar()에 기본 타이틀 표시하지 않도록 false 설정.
        toggle=new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close); // Toggle 생성.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // ActionBar에서 기본 홈 버튼을 사용 가능.
        toggle.syncState(); // ActionBarDrawerToggle의 상태를 sync
        // NavigationView에 이벤트 설정.
        NavigationView navigationView=(NavigationView)findViewById(R.id.main_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.nav_account){
                    Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_LONG).show();
                }else if(id==R.id.nav_contact_mail){
                    Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_LONG).show();
                }else if(id==R.id.nav_contact_phone){
                    Toast.makeText(getApplicationContext(), "C", Toast.LENGTH_LONG).show();
                }else if(id==R.id.nav_setting){
                    Toast.makeText(getApplicationContext(), "D", Toast.LENGTH_LONG).show();
                }else if(id==R.id.nav_search){
                    Toast.makeText(getApplicationContext(), "E", Toast.LENGTH_LONG).show();
                }else if(id==R.id.nav_zoom_in){
                    Toast.makeText(getApplicationContext(), "F", Toast.LENGTH_LONG).show();
                }else if(id==R.id.nav_zoom_out){
                    Toast.makeText(getApplicationContext(), "G", Toast.LENGTH_LONG).show();
                }else if(id==R.id.nav_help){
                    Toast.makeText(getApplicationContext(), "H", Toast.LENGTH_LONG).show();
                }else if(id==R.id.nav_home){
                    Toast.makeText(getApplicationContext(), "I", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // onOptionsItemSelected() Required. 이벤트가 toggle에서 발생한거라면 메뉴 이벤트 로직에서 벗어나게 처리.
        if(toggle.onOptionsItemSelected(item)){
            return false;
        }
        return super.onOptionsItemSelected(item);
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
    class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments=new ArrayList<>();//fragments ArrayList

        //탭 버튼 문자열 배열
        String title[]=new String[]{"", ""};

        //Adapter 생성자
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
            //프래그먼트를 생성하여 ArrayList에 add
            fragments.add(new TimeTableFragment());
            fragments.add(new FriendsFragment());
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


