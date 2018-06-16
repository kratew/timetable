package com.example.kimilm.timetable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
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
import java.util.List;
import java.util.Locale;

public class FriendsFragment extends Fragment {

    ArrayList<FriendsItem> friends;
    ListView listView;
    ListViewAdapter adapter;
    ArrayList<String> fr_id;
    ArrayList<String> fr_name;


    //search Friend
    ArrayList<Document> searchDoc = new ArrayList<>();
    ArrayList<String> myFriends = new ArrayList<>();

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

        EditText searchName = (EditText) view.findViewById(R.id.searchName);
        listView = (ListView) view.findViewById(R.id.listView);

        adapter = new ListViewAdapter();

        myFriends.addAll(MainActivity.thisFr.frList);

        addUser(myFriends);

        listView.setAdapter(adapter);

        return view;
    } // end of onCreateView()

    public boolean addUser (final ArrayList<String> addFriends)
    {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    UseDB.searchFriendTable(searchDoc, addFriends);
                }
            }
        };

        thread.start();

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

        adapter.notifyDataSetChanged();

        return true;
    }

    public void resetAdapter()
    {
        adapter = new ListViewAdapter();

        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

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

        try { thread.join(); } catch (Exception e) {}

        resetAdapter();

        addUser(MainActivity.thisFr.frList);

        AccountActivity.saveAccount(TimeTable.folderPath, MainActivity.thisFr);

        Toast.makeText(getActivity(), "친구 삭제", Toast.LENGTH_SHORT).show();
    }

    public void compareTable ()
    {
//        adapter.notifyDataSetChanged();

        int count = adapter.getCount();

        Intent intent = new Intent(getActivity(), CompareTable.class);

        int key = 0;

        for (int i = 0; i < count; ++i)
        {
            if (((FriendsItem)adapter.getItem(i)).isChk())
            {
                CarryBundle.bundle.putSerializable(String.valueOf(key++), ((FriendsItem)(adapter.getItem(i))).getLessons());
            }
        }

        MainActivity.pagechk = true;

        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("FriendFragment", "============\tFragmentOnStart\t============");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("FriendFragment", "============\tFragmentOnResume\t============");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("FriendFragment", "============\tFragmentOnAttach\t============");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FriendFragment", "============\tFragmentOnPause\t============");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("FriendFragment", "============\tFragmentOnStop\t============");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("FriendFragment", "============\tFragmentOnDestroyView\t============");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FriendFragment", "============\tFragmentOnDestroy\t============");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("FriendFragment", "============\tFragmentOnDetach\t============");
    }
}