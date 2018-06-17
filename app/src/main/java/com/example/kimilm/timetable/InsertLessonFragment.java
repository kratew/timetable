package com.example.kimilm.timetable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

//강의 추가를 위한 프래그먼트
public class InsertLessonFragment extends Fragment
{
    ArrayList<Document> documents;
    ImageView insertExit;
    ImageView insertSearch;
    TextView insertTitle;
    RecyclerView recyclerView;

    public InsertLessonFragment()
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
        View view = inflater.inflate(R.layout.fragment_insert_lesson, container, false);

        insertExit = view.findViewById(R.id.insertExit);
        insertSearch = view.findViewById(R.id.insertSearch);
        insertTitle = view.findViewById(R.id.insertTitle);
        recyclerView = view.findViewById(R.id.modal_recyclerView);

        //데이터베이스에서 받아온 강의 정보를 저장함
        documents = (ArrayList<Document>) getArguments().getSerializable("Doc");

        insertExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                insertExit(v);
            }
        });

        insertSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                setInsertSearch(v);
            }
        });

        List<Lesson> list = new ArrayList<>();

        //강의 정보 변환
        for(Document doc : documents)
        {
            list.add(TimeTableFragment.parseLesson(doc, false));
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;
    }

    //프래그먼트 나감, 자주 사용될 기능이라 종료하지 않고 히든으로 숨김
    public void insertExit (View v)
    {
        FragmentManager fManager = getFragmentManager();

        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.hide(this).commit();
    }

    //강의 검색 프래그먼트로 넘어감
    public void setInsertSearch (View v)
    {
        SearchLessonFragment searchLessonFragment = new SearchLessonFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Doc", documents);
        searchLessonFragment.setArguments(bundle);

        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.addToBackStack(null);
        fTransaction.replace(R.id.coordinator, searchLessonFragment).commit();
    }
}
