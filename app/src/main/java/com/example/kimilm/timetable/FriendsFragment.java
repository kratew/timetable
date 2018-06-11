package com.example.kimilm.timetable;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FriendsFragment extends AAH_FabulousFragment implements View.OnClickListener{

    ArrayList<FriendsItem> friends;
    FloatingActionButton fab;

    public FriendsFragment(){
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        fab = (FloatingActionButton)view.findViewById(R.id.faButton);
        fab.setOnClickListener(this);

        return view;
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


    // Fabulous Filter 코드
    /*
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.filter_sample_view, null);
        RelativeLayout rl_content = (RelativeLayout) contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = (LinearLayout) contentView.findViewById(R.id.ll_buttons);
        contentView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter("closed");
            }
        });
    }
    */
}
