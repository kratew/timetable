package com.example.kimilm.timetable;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kimilm on 2018. 6. 10..
 */

public class InsertLessonFragment extends Fragment
{
    ImageView insertExit;
    ImageView insertSearch;
    TextView insertTitle;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.insert_lesson_modal_bottom_sheet, container, false);

        insertExit = view.findViewById(R.id.insertExit);
        insertSearch = view.findViewById(R.id.insertSearch);
        insertTitle = view.findViewById(R.id.insertTitle);
        recyclerView = view.findViewById(R.id.modal_recyclerView);

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

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void insertExit (View v)
    {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction().remove(InsertLessonFragment.this).commit();
        fragmentManager.popBackStack();
    }

    public void setInsertSearch (View v)
    {
        //Search Fragment
    }
}
