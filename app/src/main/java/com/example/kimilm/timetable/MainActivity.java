package com.example.kimilm.timetable;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.bson.Document;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

//메인
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener
{
    RelativeLayout container;
    ViewPager viewPager;
    TabLayout tabLayout;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    int pageState;

    public static Friend thisFr;    //유저의 계정 정보
    public static boolean isCurAcc; //디바이스에 저장된 계정이 있는가
    public static String curAccId;  //유저의 아이디
    public static MyPagerAdapter fragmentAdapter;   //뷰페이저 어댑터

    static boolean pagechk;     //뷰페이저마다 다른 메뉴를 띄우기 위해
    static boolean toPrintTable = true; //시간표 출력 제어용

    ArrayList<String> fr_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setStatusBarColor();

        setContentView(R.layout.activity_main);

        //새로운 계정으로 초기화
        thisFr = new Friend();

        container = (RelativeLayout) findViewById(R.id.container);
        fragmentAdapter = new MyPagerAdapter(getSupportFragmentManager());

        container = (RelativeLayout)findViewById(R.id.container);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentAdapter);  // viewPager에 Adapter 설정

        drawer = (DrawerLayout)findViewById(R.id.drawer);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);    // tabLayout을 ViewPager와 연동.
        tabLayout.addOnTabSelectedListener(this);   // tabLayout의 이벤트 핸들러 등록.

        // viewpager의 페이지가 넘어갈 때 마다 이를 감지하는 코드 ↓
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            //스크롤되면 메뉴 아이콘 바꾸기 위해
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                pagechk = true;
            }

            //현재 페이지 정보 저장
            @Override
            public void onPageSelected(int position)
            {
                pageState = position;
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        // 어플 실행 시 onPrepareOptionsMenu가 두 번 실행되지 않게 하기 위한 변수.
        pagechk = false;

        // MainActivity에 NavigationDrawer 설정
        // Toggle 생성
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle.syncState();

        // NavigationView에 이벤트 설정.
        NavigationView navigationView=(NavigationView)findViewById(R.id.main_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                //아이디에 따라서 다르게 동작함
                int id=item.getItemId();

                pagechk = true;

                if(id == R.id.nav_account)
                {
                    // 계정설정 클릭시 AccountActivity로 넘어감
                    Intent myIntent = new Intent(getApplicationContext(), AccountActivity.class);

                    // 디바이스에 계정 정보가 있으면 true, 없으면 false를 전달
                    myIntent.putExtra("isCurAcc", isCurAcc);

                    // 디바이스 계정정보가 있으면 그 id를 전달.
                    if(isCurAcc == true)
                    {
                        myIntent.putExtra("curAccId", curAccId);
                    }

                    startActivityForResult(myIntent, 1000);
                    drawer.closeDrawer(Gravity.LEFT);
                }
                else if(id == R.id.nav_contact_mail)
                {
                    //미구현된 기능
                    Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_LONG).show();
                    drawer.closeDrawer(Gravity.LEFT);
                }
                else if(id == R.id.nav_contact_phone)
                {
                    //미구현된 기능
                    Toast.makeText(getApplicationContext(), "C", Toast.LENGTH_LONG).show();
                    drawer.closeDrawer(Gravity.LEFT);
                }
                return false;
            }
        });

        // 디바이스 내에 계정 정보가 있으면 불러오는 작업
        File files = new File(TimeTable.folderPath + "AccInDevice.json");

        // 존재하는 파일이 있으면 파일을 불러와서 계정정보 세팅
        if(files.exists())
        {
            FileReader fr = null;
            BufferedReader bufrd = null;
            char ch;

            try
            {
                String jsonStr = new String();

                fr = new FileReader(files);
                bufrd = new BufferedReader(fr);

                while((ch = (char)bufrd.read()) != -1)
                {
                    jsonStr += String.valueOf(ch);

                    if(ch == '}')
                    {
                        break;
                    }
                }

                Document jsonObj = Document.parse(jsonStr);

                isCurAcc = true;

                curAccId = jsonObj.getString("_id");

                thisFr.setId(jsonObj.get("_id").toString());
                thisFr.setPw(jsonObj.get("pwd").toString());
                thisFr.setName(jsonObj.get("name").toString());

                thisFr.setFrList((ArrayList<String>)(jsonObj.get("f_id", ArrayList.class)));

                bufrd.close();
                fr.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else    // 파일이 없다.
        {
            isCurAcc = false;
            Toast.makeText(this, "계정 파일이 없음", Toast.LENGTH_LONG).show();
        }
    }

    public void setStatusBarColor()
    {
        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(getResources().getColor(R.color.color8));
    }

    //옵션메뉴 띄우기
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        //FrameLayout에 addView 하는 형태로 강의정보를 추가함
        //뷰페이저를 초기화하면 시간표가 전부 날라가서 모든 레이아웃이 그려진 이후에 호출되는 이 친구를 사용해 시간표를 그림
        if(toPrintTable)
        {
            for(Lesson lesson : TimeTable.lessons)
            {
                TimeTable.fragment.showTable(lesson, (byte)0, true);
            }
            toPrintTable = false;
        }

        if(pageState == 0 && pagechk == true)   //시간표 페이지에 출력될 메뉴
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.fragment_time_table_fab_items, menu);

            pagechk = false;
        }
        else if(pageState != 0 && pagechk == true)     //친구 페이지에 출력될 메뉴
        {
            MenuInflater inflater = getMenuInflater();//MenuInflater 반환
            inflater.inflate(R.menu.fragment_friends_items, menu);

            pagechk = false;

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //메뉴 아이콘 터치에 따른 동작 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (toggle.onOptionsItemSelected(item)) { return true; }

        if(pageState == 0)  //시간표 페이지
        {
            switch (item.getItemId())
            {
                //강의 추가 프래그먼트 띄우기
                case R.id.option1:
                    ((TimeTableFragment)(fragmentAdapter.fragments.get(0))).popInsertLessonFragment();
                    break;

                //현재 시간표를 이미지로 저장하기
                case R.id.option2:
                    ((TimeTableFragment)(fragmentAdapter.fragments.get(0))).toImage();
                    Toast.makeText(this, "이미지로 저장됨", Toast.LENGTH_LONG).show();
                    break;

                //미구현된 기능
                case R.id.option3:
                    Toast.makeText(this, "비교시간표 변경", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
        else    //친구 페이지
        {
            switch (item.getItemId())
            {
                //친구 추가
                case R.id.option1:
                    Intent frIntent = new Intent(this, FriendAddActivity.class);
                    startActivityForResult(frIntent, 1001);
                    break;

                //친구 삭제
                case R.id.option2:
                    Toast.makeText(this, "친구 삭제", Toast.LENGTH_LONG).show();
                    ((FriendsFragment)(fragmentAdapter.fragments.get(1))).removeFriend();
                    break;

                //시간표 비교
                case R.id.option3:
                    ((FriendsFragment)(fragmentAdapter.fragments.get(1))).compareTable();
                    break;

                //미구현된 기능
                case R.id.option4:
                    Toast.makeText(this, "메세지 보내기", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }
    @Override
    public void onTabReselected(TabLayout.Tab tab) { }

    public void dataChanged()
    {
        viewPager.getAdapter().notifyDataSetChanged();
    }

    //TabLayout과 연동하기 위한 ViewPager의 Adapter
    class MyPagerAdapter extends FragmentStatePagerAdapter
    {
        //fragments ArrayList
        List<Fragment> fragments=new ArrayList<>();

        //탭 버튼 문자열 배열 -> 사용 안
        String title[]=new String[]{"", ""};

        //Adapter 생성자
        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
            //프래그먼트를 생성하여 ArrayList에 add
            fragments.add(new TimeTableFragment());
            fragments.add(new FriendsFragment());

            //프래그먼트에 쉽게 접근하기 위해 참조값을 받아옴
            TimeTable.fragment = (TimeTableFragment) fragments.get(0);
        }

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

        //시간표 초기화를 위해 뷰페이저를 갱신하도록 함
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    //Activity들에 대한 정보가 넘어오면
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        pagechk = true;

        //비슷한 동작을 하는 액티비티끼리 코드를 묶음
        if (requestCode == 1000 && resultCode == RESULT_OK)
        {
            //변경 이후 세팅
            isCurAcc = data.getBooleanExtra("isCurAcc", true);

            //1 : createBtn  2 : loginBtn
            if (data.getIntExtra("btnType", 0) < 3)
            {
                //계정 정보를 받아오지 못했다면
                if (!isCurAcc)
                {
                    Toast.makeText(this, "계정이 없습니다.", Toast.LENGTH_SHORT);
                    return;
                }

                //받아온 정보로 계정 세팅
                Friend getFriend = (Friend) data.getSerializableExtra("friendInfo");

                thisFr.setId(getFriend.getId());
                thisFr.setPw(getFriend.getPw());
                thisFr.setName(getFriend.getName());
                thisFr.setFrList(getFriend.getFrList());
                curAccId = getFriend.getName();

                ((FriendsFragment)(fragmentAdapter.fragments.get(1))).addUser(thisFr.frList);

                Toast.makeText(this, thisFr.getId() + " 로그인", Toast.LENGTH_LONG).show();
            }
            //3 : logoutBtn  4 : deleteBtn
            else if (data.getIntExtra("btnType", 0) > 2) {
                thisFr = new Friend();

                ((FriendsFragment)(fragmentAdapter.fragments.get(1))).resetAdapter();
                Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show();
            }
        }
        // 가져온 아이디로 검색하여 FriendFragment를 갱신함
        else if (requestCode == 1001 && resultCode == RESULT_OK)
        {
            fr_id.clear();
            fr_id.add(data.getStringExtra("frId").toString());

            if (thisFr.getId().equals(fr_id.get(0)))
            {
                Toast.makeText(this, "본인 아이디는 추가할 수 없습니다.", Toast.LENGTH_LONG).show();

                return;
            }

            for (int i = 0; i < thisFr.frList.size(); ++i)
            {
                if (thisFr.frList.get(i).equals(fr_id.get(0)))
                {
                    Toast.makeText(this, "이미 존재하는 친구입니다.", Toast.LENGTH_LONG).show();

                    return;
                }
            }

            if (!((FriendsFragment) fragmentAdapter.fragments.get(1)).addUser(fr_id))
            {
                Toast.makeText(this, "찾는 친구가 없습니다.", Toast.LENGTH_LONG).show();

                return;
            }

            //데이터 갱신
            thisFr.frList.add(fr_id.get(0));

            //친구 추가 디비 갱신
            new Thread()
            {
                @Override
                public void run()
                {
                    UseDB.insertFriend(thisFr.getId(), fr_id.get(0));
                }
            }.start();

            //파일도 갱신
            AccountActivity.saveAccount(TimeTable.folderPath, thisFr);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        pagechk = true;
    }
}