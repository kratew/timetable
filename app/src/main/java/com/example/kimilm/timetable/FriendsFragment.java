package com.example.kimilm.timetable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.bson.Document;

import java.util.ArrayList;

//친구 정보 프래그먼트, 메인의 뷰페이저에 들어감
public class FriendsFragment extends Fragment
{
    ListView listView;
    ListViewAdapter adapter;

    //search Friend
    ArrayList<Document> searchDoc = new ArrayList<>();
    ArrayList<String> myFriends = new ArrayList<>();

    public FriendsFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (container == null)
        {
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        EditText searchName = (EditText) view.findViewById(R.id.searchName);
        listView = (ListView) view.findViewById(R.id.listView);

        adapter = new ListViewAdapter();

        myFriends.addAll(MainActivity.thisFr.frList);

        addUser(myFriends);

        listView.setAdapter(adapter);

        return view;
    }

    //친구 추가, 성공은 true, 실패는 false
    public boolean addUser (final ArrayList<String> addFriends)
    {
        //결과를 받아와야 하는 쓰레드 작업
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    UseDB.searchFriendTable(searchDoc, addFriends);
                }
            }
        };

        thread.start();

        //기다림
        try { thread.join(); } catch (Exception e) {}

        // 불러온 친구를 리스트뷰에 넣음.
        if (searchDoc.get(0) != null)
        {
            for (Document friends : searchDoc) {
                adapter.addItem(false, friends.getString("_id"), friends.getString("name"),
                        friends.get("timetable", Document.class).get("lessons", ArrayList.class));
            }
        }
        else
        {
            return false;
        }

        //친구 목록 다시 그림
        adapter.notifyDataSetChanged();

        return true;
    }

    //로그아웃, 계정삭제 등의 작업시 어뎁터를 초기화
    public void resetAdapter()
    {
        adapter = new ListViewAdapter();

        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    //친구를 삭제하면 로컬 / 디비 모드 지우는 작업
    public void removeFriend()
    {
        int count = adapter.getCount();

        final ArrayList<String> delUsers = new ArrayList<>();

        for (int i = 0; i < count; ++i)
        {
            if (((FriendsItem)adapter.getItem(i)).isChk())
            {
                delUsers.add(((FriendsItem)adapter.getItem(i)).getId());

                MainActivity.thisFr.frList.remove(i);
            }
        }

        //결과를 받아와야 하는 작업
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                for (int i = 0; i < delUsers.size(); ++i)
                {
                    UseDB.deleteFriend(MainActivity.thisFr.getId(), delUsers.get(i));
                }
            }
        };

        thread.start();

        //기다림
        try { thread.join(); } catch (Exception e) {}

        //친구들을 새로 그리고
        resetAdapter();

        addUser(MainActivity.thisFr.frList);

        //로컬 파일도 갱신
        AccountActivity.saveAccount(TimeTable.folderPath, MainActivity.thisFr);

        Toast.makeText(getActivity(), "친구 삭제", Toast.LENGTH_SHORT).show();
    }

    //시간표를 비교하여 액티비티를 띄움
    public void compareTable ()
    {
        int count = adapter.getCount();

        Intent intent = new Intent(getActivity(), CompareTable.class);

        int key = 0;

        //리스트뷰에 체크된 친구들을 대상으로 시간표 비교
        for (int i = 0; i < count; ++i)
        {
            if (((FriendsItem)adapter.getItem(i)).isChk())
            {
                CarryBundle.bundle.putSerializable(String.valueOf(key++), ((FriendsItem)(adapter.getItem(i))).getLessons());
            }
        }

        //메뉴 아이콘 세팅값
        MainActivity.pagechk = true;

        startActivity(intent);
    }
}