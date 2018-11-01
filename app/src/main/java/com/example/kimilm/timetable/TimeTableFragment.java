package com.example.kimilm.timetable;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TimeTableFragment extends Fragment
{
    FrameLayout frameLayout;
    GridLayout gridLayout;
    ScrollView scrollView;

    //디비에서 강의 정보를 받아오기 위한 변수
    ArrayList<Document> documents;

    BottomSheetDialog modalBottomSheet;

    public TimeTableFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(container == null)
        {
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        frameLayout = (FrameLayout) view.findViewById(R.id.frame);

        gridLayout = (GridLayout)view.findViewById(R.id.gridLayout);

        scrollView = (ScrollView)view.findViewById(R.id.scrollView);

        setGridLayoutHeight();  //화면 사이즈에 맞게 변환하는 메소드.

        setHasOptionsMenu(true);

        documents = new ArrayList<>();

        //디비에서 강의정보를 읽어온 후 documents 변수에 저장
        new Thread() {
            @Override
            public void run() {
                mongo(documents, null, null);
            }
        }.start();

        return view;
    }

    //화면 사이즈에 맞게 변환
    public void setGridLayoutHeight()
    {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = gridLayout.getLayoutParams();
        params.height = size.y;
        gridLayout.setLayoutParams(params);
    }

    //강의 띄우기
    public void showTable (Lesson lesson, byte cFlag, boolean noCompare)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        View [] view = new View[lesson.times.size()];

        int r = (int)(Math.random() * 255);
        int g = (int)(Math.random() * 255);
        int b = (int)(Math.random() * 255);

        for(int i = 0; i < lesson.times.size(); ++i)
        {
            //강의에 따라 출력될 위치 설정
            LinearLayout wrapLayout = new LinearLayout(getContext());

            wrapLayout.setLayoutParams(layoutParams);

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view[i] = inflater.inflate(R.layout.lesson_view, wrapLayout,true);

            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(setBtnWidth(lesson.times.get(i).substring(0, 1)),
                    setBtnHeight(lesson.times.get(i).substring(1)));

            viewParams.setMargins(setBtnLeftMargin(lesson.times.get(i).substring(0, 1)),
                    setBtnTopMargin(lesson.times.get(i).substring(1)), 0, 0);

            view[i].setLayoutParams(viewParams);

            //강의 비교가 아니라면 정보창을 띄우는 온클릭 리스너 생성
            if (noCompare)
            {
                view[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        BottomSheet(R.layout.modal_bottom_sheet_info_lesson, v);
                    }
                });
            }

            // 컬러 플래그
            if (cFlag == 0)
            {
                //0 -> 저장된 색으로 세팅
                view[i].setBackgroundColor(lesson.getColor());
            }
            else if (cFlag == 1)
            {
                //1 ->랜덤 색상을 입력
                view[i].setBackgroundColor(Color.rgb(r, g, b));
                lesson.setColor(Color.rgb(r, g, b));
            }
            else
            {
                //2 -> 시간표 비교용 알파값
                view[i].setBackgroundColor(Color.argb(170, 250, 200, 200));
            }

            //보여지는 부분
            ((TextView)(view[i].findViewById(R.id.floatTitle))).setText(lesson.title);
            ((TextView)(view[i].findViewById(R.id.floatClassRoom))).setText(lesson.classroom.toString().replace("[", "").replace("]", ""));
            ((TextView)(view[i].findViewById(R.id.floatProf))).setText(lesson.prof);

            if (noCompare)
            {
                //감추고 정보를 가지고 있음
                ((TextView) (view[i].findViewById(R.id.floatClassify))).setText(lesson.classify);
                ((TextView) (view[i].findViewById(R.id.floatCredit))).setText(lesson.credit);
                ((TextView) (view[i].findViewById(R.id.floatCode))).setText(lesson.code);
                ((TextView) (view[i].findViewById(R.id.floatTimes))).setText(lesson.times.toString().replace("[", "").replace("]", ""));
                ((TextView) (view[i].findViewById(R.id.floatCount))).setText(String.valueOf(lesson.times.size()));

                //동적 뷰 제거 위해 아이디 세팅, 코드 + 뷰 카운트로 유니크하게 구분
                wrapLayout.setId(Integer.parseInt(lesson.code + i));
            }

            //강의 비교가 아니라면 TimeTableFragment에 출력
            if(noCompare)
            {
                frameLayout.addView(wrapLayout);
            }
            //비교라면 CompareActivity에 출력
            else
            {
                CompareTable.compareFrame.addView(wrapLayout);
            }
        }

        ((LinearLayout)view[0].findViewById(R.id.floatLessonTexts)).setVisibility(View.VISIBLE);
    }

    // 좌우 위치
    public int setBtnLeftMargin(String time)
    {
        switch(time)
        {
            case "월":
                return getActivity().findViewById(R.id.mon).getLeft();
            case "화":
                return getActivity().findViewById(R.id.tue).getLeft();
            case "수":
                return getActivity().findViewById(R.id.wed).getLeft();
            case "목":
                return getActivity().findViewById(R.id.thr).getLeft();
            case "금":
                return getActivity().findViewById(R.id.fri).getLeft();
        }
        return 0;
    }

    // 너비
    public int setBtnWidth(String time)
    {
        switch(time)
        {
            case "월":
                return getActivity().findViewById(R.id.mon).getWidth();
            case "화":
                return getActivity().findViewById(R.id.tue).getWidth();
            case "수":
                return getActivity().findViewById(R.id.wed).getWidth();
            case "목":
                return getActivity().findViewById(R.id.thr).getWidth();
            case "금":
                return getActivity().findViewById(R.id.fri).getWidth();
        }
        return 0;
    }

    // 상하 위치
    public int setBtnTopMargin(String time)
    {
        //A, B, C, D, E 교시
        if (time.charAt(0) >= 65)
        {
            switch(time)
            {
                case "A":
                    return getActivity().findViewById(R.id.time1).getTop()
                            + Math.round(getActivity().findViewById(R.id.time1).getHeight() * 0.5f);
                case "B":
                    return getActivity().findViewById(R.id.time3).getTop();

                case "C":
                    return getActivity().findViewById(R.id.time4).getTop()
                            + Math.round(getActivity().findViewById(R.id.time4).getHeight() * 0.5f);
                case "D":
                    return getActivity().findViewById(R.id.time6).getTop();

                case "E":
                    return getActivity().findViewById(R.id.time7).getTop()
                            + Math.round(getActivity().findViewById(R.id.time7).getHeight() * 0.5f);
            }
        }
        //1 ~ 14 교시
        else
        {
            if(Integer.parseInt(time) < 9)
            {
                int resId = getResources().getIdentifier("time" + Integer.parseInt(time), "id", getActivity().getPackageName());
                return getActivity().findViewById(resId).getTop();
            }
            else
            {
                int resId = getResources().getIdentifier("time" + Integer.parseInt(time), "id", getActivity().getPackageName());
                return getActivity().findViewById(resId).getTop()
                        + (int)Math.floor(getActivity().findViewById(resId).getHeight() / 60f * (30 - ((Integer.parseInt(time) - 9) * 5)));
            }
        }
        return 0;
    }

    // 높이
    public int setBtnHeight(String time)
    {
        //A, B, C, D, E 교시
        if (time.charAt(0) >= 65)
        {
            switch(time)
            {
                case "A":
                    return Math.round(getActivity().findViewById(R.id.time1).getHeight() * 1.5f);

                case "B":
                    return Math.round(getActivity().findViewById(R.id.time3).getHeight() * 1.5f);

                case "C":
                    return Math.round(getActivity().findViewById(R.id.time4).getHeight() * 1.5f);

                case "D":
                    return Math.round(getActivity().findViewById(R.id.time6).getHeight() * 1.5f);

                case "E":
                    return Math.round(getActivity().findViewById(R.id.time7).getHeight() * 1.5f);
            }
        }
        //1 ~ 14 교시
        else
        {
            if(Integer.parseInt(time) < 9)
            {
                int resId = getResources().getIdentifier("time" + Integer.parseInt(time), "id", getActivity().getPackageName());
                return getActivity().findViewById(resId).getHeight();
            }
            else
            {
                int resId = getResources().getIdentifier("time" + Integer.parseInt(time), "id", getActivity().getPackageName());
                return (int)Math.ceil(getActivity().findViewById(resId).getHeight() * 11 / 12.0);
            }
        }
        return 0;
    }

    // 강의 추가 화면
    public void popInsertLessonFragment ()
    {
        //프래그먼트 생성하고 강의 정보 넘김
        InsertLessonFragment insertLessonFragment = new InsertLessonFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Doc", documents);
        insertLessonFragment.setArguments(bundle);

        //화면에 띄움
        FragmentManager fManager = getFragmentManager();

        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.addToBackStack(null);
        fTransaction.replace(R.id.coordinator, insertLessonFragment).commit();
    }

    //강의 정보 화면
    public void BottomSheet(int layoutId, View parent)
    {
        final View view = getLayoutInflater().inflate(layoutId, null);

        // 바텀시트 제거
        ((ImageView)view.findViewById(R.id.lessonExit)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                modalBottomSheet.dismiss();
            }
        });

        // 강의 삭제 온클릭
        ((ImageView)view.findViewById(R.id.lessonDelete)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //어플에서 삭제하고
                TimeTable.delLesson(((TextView)view.findViewById(R.id.lessonCode)).getText().toString());

                int count = Integer.parseInt(((TextView)view.findViewById(R.id.lessonCount)).getText().toString());

                //뷰에서 삭제하고
                for(int i = 0; i < count; ++i)
                {
                    frameLayout.removeView(frameLayout.findViewById(Integer.parseInt(
                            ((TextView)view.findViewById(R.id.lessonCode)).getText().toString() + i)));
                }

                //저장
                TimeTable.saveTable();

                //디비 업데이트
                new Thread()
                {
                    @Override
                    public void run() {
                        UseDB.uploadTimeTable(MainActivity.isCurAcc, MainActivity.thisFr.getId());
                    }
                }.start();

                modalBottomSheet.dismiss();
            }
        });

        // 정보 세팅
        ((TextView)view.findViewById(R.id.lessonTitle)).setText(((TextView)parent.findViewById(R.id.floatTitle)).getText());
        ((TextView)view.findViewById(R.id.lessonProf)).setText(((TextView)parent.findViewById(R.id.floatProf)).getText());
        ((TextView)view.findViewById(R.id.lessonTime)).setText(((TextView)parent.findViewById(R.id.floatTimes)).getText());
        ((TextView)view.findViewById(R.id.lessonCredit)).setText(((TextView)parent.findViewById(R.id.floatCredit)).getText());
        ((TextView)view.findViewById(R.id.lessonClassify)).setText(((TextView)parent.findViewById(R.id.floatClassify)).getText());
        ((TextView)view.findViewById(R.id.lessonClassroom)).setText(((TextView)parent.findViewById(R.id.floatClassRoom)).getText());
        ((TextView)view.findViewById(R.id.lessonCode)).setText(((TextView)parent.findViewById(R.id.floatCode)).getText());
        ((TextView)view.findViewById(R.id.lessonCount)).setText(((TextView)parent.findViewById(R.id.floatCount)).getText());

        modalBottomSheet = new BottomSheetDialog(getActivity());

        modalBottomSheet.setContentView(view);

        modalBottomSheet.show();
    }

    //시간표를 이미지로 저장
    public void toImage ()
    {
        //권한 설정
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != getActivity().getPackageManager().PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != getActivity().getPackageManager().PERMISSION_GRANTED)
        {
            Toast.makeText(getActivity(), "저장소 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String [] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }

        //화면 크기를 초과한 이미지는 BuildDrawingCache() 사용시 이미지를 읽어오지 못하는 문제가 있어서 Canvas 클래스 사용함
        //레이아웃 크기와 동일한 비트맵 생성
        Bitmap bitmap = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        frameLayout.draw(canvas);

        //MyFolder가 없다면 생성
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyFolder";

        File folder = new File(folderPath);

        if (!folder.exists())
        {
            folder.mkdirs();
        }

        folderPath = folderPath + File.separator + System.currentTimeMillis() + ".png";

        folder = new File(folderPath);

        try
        {
            FileOutputStream output = new FileOutputStream(folder);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

            output.close();

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(folder)));

            Toast.makeText(getActivity(), "이미지 저장 완료", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "이미지 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    //몽고디비 연동 자바코드
    public static void mongo (ArrayList<Document> document, String key, String value)
    {
        document.clear();

        String IP = "45.119.146.33";
        int Port = 27017;
        String dbName = "TimeTable";

        //몽고디비 연결
        MongoClient mongoClient = new MongoClient(IP, Port);
        MongoDatabase db = mongoClient.getDatabase(dbName);

        //가천대 홈페이지상의 IT대학 코드
        MongoCollection<Document> collection = db.getCollection("CJ0200");

        Document query = new Document();

        //검색
        if(key != null && value != null)
        {
            query = new Document(key, new Document("$regex", value));
        }

        MongoCursor<Document> cursor = collection.find(query).iterator();

        while (cursor.hasNext())
        {
            document.add(cursor.next());
        }

        mongoClient.close();
    }

    // Document -> Lesson 변환
    public static Lesson parseLesson(Document document, boolean color)
    {
        @ColorInt int setcolor = 0;

        if(color)
        {
            setcolor = document.getInteger("color");
        }

        return new Lesson(document.getString("_id"), document.getString("title"),
                document.getString("classify"), document.get("credit").toString(),
                (ArrayList<String>)(document.get("times", ArrayList.class)), document.getString("prof"),
                (ArrayList<String>)(document.get("classroom", ArrayList.class)), setcolor);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().invalidateOptionsMenu();
    }
}
