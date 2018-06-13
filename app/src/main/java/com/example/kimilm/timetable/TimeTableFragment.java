package com.example.kimilm.timetable;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dekoservidoni.omfm.OneMoreFabMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TimeTableFragment extends Fragment implements View.OnClickListener{

    ArrayList<TimeTable> timeTables;    //굳이 어레이리스트를 써야할까?
    ScrollView scrollView;
    FrameLayout frameLayout;
    GridLayout gridLayout;
    OneMoreFabMenu fab;
    Animation visib;
    Animation invisib;
    boolean checker; // fab.setVisibility(View.VISIBLE)가 최하단이 아닌 모든 스크롤 위치에서 작동하기 때문에 이를 막기 위한 변수.

    public TimeTableFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container == null){
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        frameLayout = (FrameLayout)view.findViewById(R.id.frame);
        gridLayout = (GridLayout)view.findViewById(R.id.gridLayout);
        scrollView = (ScrollView)view.findViewById(R.id.scrollView);
        fab = (OneMoreFabMenu)view.findViewById(R.id.faButton);
        fab.setOnClickListener(this);
        visib = AnimationUtils.loadAnimation(getActivity(), R.anim.visib);
        invisib = AnimationUtils.loadAnimation(getActivity(), R.anim.invisib);

        checker = false;

        //스크롤시 FloatingActionButton이 사라지는 코드
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {    // ScrollView에서 스크롤이 화면 최하단에 도달함을 감지하는 코드.
            @Override
            public void onScrollChanged() {
                if (scrollView != null) {
                    if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                        // scroll view가 최하단에 도달함.
                        //fab.setVisibility(View.INVISIBLE);
                        //fab.startAnimation(invisib);
                        checker = true;
                    } else {
                        // scroll view가 최하단이 아님.
                        if(checker == true) {
                            //fab.setVisibility(View.VISIBLE);
                            //fab.startAnimation(visib);
                            checker = false;
                        }
                    }
                }
            }
        });


        /*  ▼ NullPointerException 에러 발생!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        ActionBar actionBar = ((MainActivity)getActivity()).getActionBar(); // 액션바의 그림자 제거
        actionBar.setElevation(0);
        */

        setGridLayoutHeight();  //화면 사이즈에 맞게 변환하는 메소드.

        setHasOptionsMenu(true);
        return view;
    }

    // OneMoreFabMenu의 메뉴 아이템의 아이디를 가져오는 코드 ↓

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_time_table_fab_items, menu);

/*
        MenuItem item1 = menu.findItem(R.id.option1);
        MenuItem item2 = menu.findItem(R.id.option2);
        MenuItem item3 = menu.findItem(R.id.option3);

        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getActivity(), "13243546576454", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getActivity(), "13243546576454", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getActivity(), "SDYRTHEGRWSV", Toast.LENGTH_LONG).show();
                return true;
            }
        });
*/
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuI
                        Toast.makeText(getActivity(), "비교시간표 변경 선택됨!", Toast.LENGTH_LONG).show();
                        return false;tem item) {
                        return super.onOptionsItemSelected(item);
                    }
*/

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    int curId = item.getItemId();
    switch(curId){
        case R.id.option1:
            Toast.makeText(getActivity(), "강의추가 선택됨!", Toast.LENGTH_LONG).show();
            return false;
            case R.id.option2:
                Toast.makeText(getActivity(), "이미지로 저장 선택됨!", Toast.LENGTH_LONG).show();
                return false;
                case R.id.option3:
                    Toast.makeText(getActivity(), "이미지로 저장 선택됨!", Toast.LENGTH_LONG).show();
                    return false;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onOptionsMenuClosed(Menu menu) {
                super.onOptionsMenuClosed(menu);
            }

    @Override
    public void onClick(View v) {
        /* Snackbar : 간단한 문자열 메시지를 사용자에게 잠깐 보여줄 목적으로 사용
           - Toast 메시지와 비슷하지만, 사용자의 이벤트 처리가 가능하기 때문에 많이 사용
           - Snackbar.make(스넥바가 뜨게될 View, 사용자에게 보일 문자열 메시지, 스넥바가 화면에 뜨는 시간)
           - setAction() 메서드를 사용하면, Snackbar에서 사용자 이벤트를 처리할 수 있음
           - setAction(Action문자열, 이벤트 핸들러)
           - 사용자가 Action문자열을 클릭하면, 두 번째 매개변수인 OnClickListener()를 구현한 이벤트 핸들러가 실행
        */
        //=====================================================================
        Snackbar.make(v,"Snackbar with Action", Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW).setAction("현재 시간?", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US);
                String getTime = dateformat.format(date);
                Toast.makeText(getActivity(), getTime, Toast.LENGTH_LONG).show();
            }
        }).show();
        //=====================================================================
    }




    //화면 사이즈에 맞게 변환
    public void setGridLayoutHeight()
    {
        Display display = getActivity().getWindowManager().getDefaultDisplay(); // 프래그먼트 상에서 디스플레이 사이즈를 가져오기 위해 getActivity()를 추가함.
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = gridLayout.getLayoutParams();
        params.height = size.y;
    }

    public TimeTable getTable (View v)
    {
        TimeTable timeTable = new TimeTable();

        //로그인 혹은 로컬 디비에 저장되어있는 시간표를 불러온다.

        return timeTable;
    }

    public void deleteLesson (View v)
    {

    }

    public void saveTable (View v)
    {

    }

    public void insertLesson (View v)
    {

    }

    //WRITE_EXTERNAL_STORAGE PERMISSION 허용 팝업 기능 추가할 것
    //시간표를 이미지로 저장하는 코드 ↓
    public void toImage (View v)
    {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                getActivity().getPackageManager().PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                getActivity().getPackageManager().PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                getActivity().getPackageManager().PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                getActivity().getPackageManager().PERMISSION_DENIED))
        {
            Toast.makeText(getActivity(), "권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        /*화면 크기를 초과한 이미지는 BuildDrawingCache() 사용시 이미지를 읽어오지 못하는 문제가 있어서 Canvas 클래스 사용함*/
        //레이아웃 크기와 동일한 비트맵 생성

        Bitmap bitmap = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);

        //생성한 비트맵으로 캔버스를 만들고
        Canvas canvas = new Canvas(bitmap);

        //레이아웃을 캔버스에 그리기
        frameLayout.draw(canvas);

        //저장 경로 설정
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyFolder";

        //파일 클래스에 저장경로 전달
        File folder = new File(folderPath);

        //폴더가 존재하지 않으면
        if (!folder.exists())
        {
            //만들자
            folder.mkdirs();
        }

        //파일명을 설정해줘야 되는데 얘가 String이라서 재사용함
        folderPath = folderPath + File.separator + System.currentTimeMillis() + ".png";

        //기존에 만들었던 File 클래스 객체에 파일경로 설정
        folder = new File(folderPath);

        try
        {
            //파일 아웃풋 스트림
            FileOutputStream output = new FileOutputStream(folder);

            //그려둔 비트맵을 그림파일로 저장
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

            //아웃풋 스트림 정리
            output.close();

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(folder)));

            //저장 완료 메세지 띄우고
            Toast.makeText(getActivity(), "이미지 저장 완료", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            //만약 실패했다면 실패 메세지 띄우기
            e.printStackTrace();
            Toast.makeText(getActivity(), "이미지 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }   // 이미지를 저장하는 코드의 끝 ↑
}
