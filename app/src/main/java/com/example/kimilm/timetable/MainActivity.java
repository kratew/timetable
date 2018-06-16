package com.example.kimilm.timetable;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.bson.Document;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    RelativeLayout container;
    ViewPager viewPager;
    TabLayout tabLayout;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    LinearLayout account_window;
    public static Friend thisFr;
    public static boolean isCurAcc;
    public static String curAccId;

    public static MyPagerAdapter fragmentAdapter;
    int pageState;
    static boolean pagechk;


    static boolean toPrintTable = true;

    //================from new Master
    ArrayList<String> fr_id = new ArrayList<>();
    //================from new Master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatusBarColor();

        Log.d("Main", "============\tMainOnCreate\t============");

        setContentView(R.layout.activity_main);

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {    // viewpager의 페이지가 넘어갈 때 마다 이를 감지하는 코드 ↓
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                pagechk = true;
            }

            @Override
            public void onPageSelected(int position)
            {
                pageState = position;
                Log.d("$$onPageSelected act", "position : " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });

        pagechk = false;    // 어플 실행 시 onPrepareOptionsMenu가 두 번 실행되지 않게 하기 위한 변수.
        Log.d("$$onCreate() #\t#\t#\t#\t#", "pagechk : "+ pagechk);

        // MainActivity에 NavigationDrawer 설정하는 코드 ↓
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close); // Toggle 생성.

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // ActionBar에서 기본 홈 버튼을 사용 가능.

        toggle.syncState(); // ActionBarDrawerToggle의 상태를 sync

        // NavigationView에 이벤트 설정.
        NavigationView navigationView=(NavigationView)findViewById(R.id.main_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id=item.getItemId();

                pagechk = true;

                if(id==R.id.nav_account)
                {   // 계정설정 클릭시 AccountActivity로 날아감.
                    Intent myIntent = new Intent(getApplicationContext(), AccountActivity.class);
                    myIntent.putExtra("isCurAcc", isCurAcc); // 디바이스에 계정 정보가 있으면 true, 없으면 false를 전달함.

                    if(isCurAcc == true)
                    {   // 디바이스 계정정보가 있으면 그 id를 전달.
                        myIntent.putExtra("curAccId", curAccId);
                    }
                    startActivityForResult(myIntent, 1000);
                    drawer.closeDrawer(Gravity.LEFT);
                }
                else if(id==R.id.nav_contact_mail)
                {
                    Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_LONG).show();
                    drawer.closeDrawer(Gravity.LEFT);
                }
                else if(id==R.id.nav_contact_phone)
                {
                    Toast.makeText(getApplicationContext(), "C", Toast.LENGTH_LONG).show();
                    drawer.closeDrawer(Gravity.LEFT);
                }
                return false;
            }
        });

        // 디바이스 내에 계정 정보가 있으면 불러오는 코드 ↓
        File files = new File(TimeTable.folderPath + "AccInDevice.json");

        if(files.exists())
        {   // 만약 이미 존재하는 파일이 있으면 파일 불러오기.
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
        else
        {  // 파일이 없다.
            isCurAcc = false;
            Toast.makeText(this, "계정 파일이 없음", Toast.LENGTH_LONG).show();
        }
    } // end of onCreate()

    public void setStatusBarColor()
    {
        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(getResources().getColor(R.color.color8));
    }

    //옵션메뉴 띄우기 ↓
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        Log.d("$onPrepareOptionsMenu", "pagechk : "+pagechk);

        //여기가 최선이었다.
        //시간표 그리기
        if(toPrintTable)
        {
            Log.d("Draw #\t#\t#\t#\t#", "T\tI\tM\tE\tT\tA\tB\tL\tE\t" + toPrintTable);
            for(Lesson lesson : TimeTable.lessons)
            {
                TimeTable.fragment.showTable(lesson, (byte)0, true);
            }
        }
        toPrintTable = false;

        Log.d("$$onCreate() #\t#\t#\t#\t#", "pagechk : "+pagechk);
        Log.d("$$onPrepareOptionsMenu","Activated!");

        if(pageState == 0 && pagechk == true)
        {
            MenuInflater inflater = getMenuInflater();//MenuInflater 반환
            inflater.inflate(R.menu.fragment_time_table_fab_items, menu);

            pagechk = false;

            Log.d("$$tablemenu","activated");
        }
        else if(pageState != 0 && pagechk == true)
        {
            MenuInflater inflater = getMenuInflater();//MenuInflater 반환
            inflater.inflate(R.menu.fragment_friends_items, menu);

            pagechk = false;

            Log.d("$$friendmenu","activated");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) { return true; }

        if(pageState == 0)
        {
            switch (item.getItemId())
            {
                case R.id.option1:
                    ((TimeTableFragment)(fragmentAdapter.fragments.get(0))).popInsertLessonFragment();
                    break;

                case R.id.option2:
                    ((TimeTableFragment)(fragmentAdapter.fragments.get(0))).toImage();
                    Toast.makeText(this, "이미지로 저장", Toast.LENGTH_LONG).show();
                    break;

                case R.id.option3:
                    Toast.makeText(this, "비교시간표 변경", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
        else
        {
            switch (item.getItemId())
            {
                case R.id.option1:
                    Intent frIntent = new Intent(this, FriendAddActivity.class);
                    startActivityForResult(frIntent, 1001);
                    break;

                case R.id.option2:
                    Toast.makeText(this, "친구 삭제", Toast.LENGTH_LONG).show();
                    ((FriendsFragment)(fragmentAdapter.fragments.get(1))).removeFriend();
                    break;

                case R.id.option3:
                    ((FriendsFragment)(fragmentAdapter.fragments.get(1))).compareTable();
                    break;

                case R.id.option4:
                    Toast.makeText(this, "메세지 보내기", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
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

    public void dataChanged()
    {
        viewPager.getAdapter().notifyDataSetChanged();
    }

    /* TabLayout과 연동하기 위한 ViewPager의 Adapter 클래스 선언 */
    class MyPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragments=new ArrayList<>();//fragments ArrayList

        //탭 버튼 문자열 배열 -> 사용 안
        String title[]=new String[]{"", ""};

        //Adapter 생성자
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
            //프래그먼트를 생성하여 ArrayList에 add
            fragments.add(new TimeTableFragment());
            fragments.add(new FriendsFragment());

            //

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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        pagechk = true;

        Log.d("\t\t\t\t\tOnActivityResult!", "Where\tam\tI\t" + toPrintTable);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            //변경 이후 세팅
            isCurAcc = data.getBooleanExtra("isCurAcc", true);

            //1 : createBtn  2 : loginBtn
            if (data.getIntExtra("btnType", 0) < 3) {
                if (!isCurAcc) {
                    Toast.makeText(this, "계정이 없습니다.", Toast.LENGTH_SHORT);
                    return;
                }

                Friend getFriend = (Friend) data.getSerializableExtra("friendInfo");
                thisFr.setId(getFriend.getId());
                thisFr.setPw(getFriend.getPw());
                thisFr.setName(getFriend.getName());
                thisFr.setFrList(getFriend.getFrList());
                curAccId = getFriend.getName();

                ((FriendsFragment)(fragmentAdapter.fragments.get(1))).addUser(thisFr.frList);
            }
            //3 : logoutBtn  4 : deleteBtn
            else if (data.getIntExtra("btnType", 0) > 2) {
                thisFr = new Friend();

                ((FriendsFragment)(fragmentAdapter.fragments.get(1))).resetAdapter();
            }

            Toast.makeText(this, "AccountActivity가 정상적으로 종료됨." + isCurAcc, Toast.LENGTH_LONG).show();
        }
        // 가져온 아이디로 FriendFragment에서 검색하는 코드. ↓
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
    protected void onStart() {
        super.onStart();
        Log.d("Main", "============\tMainOnStart\t============");
    }


    @Override
    protected void onResume() {
        super.onResume();

        pagechk = true;

        Log.d("Main", "============\tMainOnResume\t============");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Main", "============\tMainOnPause\t============");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Main", "============\tMainOnStop\t============");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Main", "============\tMainOnDestroy\t============");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.d("Main", "============\tMainOnPostResume\t============");
    }
}