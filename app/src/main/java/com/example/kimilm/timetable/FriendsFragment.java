package com.example.kimilm.timetable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FriendsFragment extends Fragment {

    ArrayList<FriendsItem> friends;
    ListView listView;
    ListViewAdapter adapter;
    ArrayList<String> fr_id;
    ArrayList<String> fr_name;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public ArrayList<FriendsItem> getFriends() {
        return friends;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        adapter = new ListViewAdapter();

        listView.setAdapter(adapter);

        EditText searchName = (EditText) view.findViewById(R.id.searchName);

        fr_id = new ArrayList<>();

        for(int i = 0; i < fr_id.size(); i++){  // 불러온 친구를 리스트뷰에 넣음.
            adapter.addItem(false, fr_name.get(i), fr_id.get(i));
        }

        searchName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String filterText = s.toString();
                ((ListViewAdapter) listView.getAdapter()).getFilter().filter(filterText);
            }
        });

        return view;
    } // end of onCreateView()
}