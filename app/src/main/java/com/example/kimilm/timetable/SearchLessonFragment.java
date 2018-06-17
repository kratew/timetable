package com.example.kimilm.timetable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

//강의 검색 프래그먼트
public class SearchLessonFragment extends Fragment
{
    RadioGroup prefix;
    ImageView searchBack;
    ImageView searchBtn;
    EditText toSearch;
    RecyclerView recyclerView;
    String key;
    ArrayList<Document> searchDocument;

    public SearchLessonFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search_lesson, container, false);

        prefix = view.findViewById(R.id.prefix);
        searchBack = view.findViewById(R.id.searchBack);
        toSearch = view.findViewById(R.id.toSearch);
        recyclerView = view.findViewById(R.id.search_modal_recyclerView);

        searchDocument = new ArrayList<>();

        //체크된 버튼에 따른 검색 수행
        prefix.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.searchTitle:
                        key = "title";
                        break;

                    case R.id.searchProf:
                        key = "prof";
                        break;

                    case R.id.searchCode:
                        key = "code";
                        break;
                }
            }
        });

        prefix.check(R.id.searchTitle);

        //뒤로가기
        searchBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                searchBack(v);
            }
        });

        //검색
        toSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                //키보드의 엔터키가 눌리면 검색
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    lessonSearch(v);

                    KeyboardUtils.hideKeyboard(getContext());
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    //뒤로가기
    public void searchBack (View v)
    {
        KeyboardUtils.hideKeyboard(getContext());

        InsertLessonFragment insertLessonFragment = new InsertLessonFragment();
        insertLessonFragment.setArguments(getArguments());

        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.coordinator, insertLessonFragment).commit();
    }

    //강의 검색 기능
    public void lessonSearch (View v)
    {
        if(toSearch.getText().toString().length() == 0)
        {
            Toast.makeText(getActivity(), "입력 값이 없습니다.", Toast.LENGTH_SHORT).show();

            return;
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    TimeTableFragment.mongo(searchDocument, key, toSearch.getText().toString());
                }
            }
        };

        thread.start();

        //쓰레드 끝나는 시간을 기다려야한다
        try { thread.join(); } catch (Exception e) {}

        if(searchDocument.size() == 0)
        {
            Toast.makeText(getActivity(), "찾는 강의가 없습니다.", Toast.LENGTH_SHORT).show();

            return;
        }

        //받아온 강의정보 파싱
        List<Lesson> list = new ArrayList<>();

        for(Document doc : searchDocument)
        {
            list.add(TimeTableFragment.parseLesson(doc, false));
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}

//검색하면 키보드 내려가게 설정
class KeyboardUtils
{
    public static void hideKeyboard(Context context)
    {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }
}