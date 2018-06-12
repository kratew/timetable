package com.example.kimilm.timetable;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AccountLoginFragment extends Fragment {

    Button createBtn;

    public AccountLoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account_login, container, false);

        // 계정 생성 버튼 누르면 -> AccountCreateFragment로 변환. ↓
        createBtn = (Button)view.findViewById(R.id.cteateAccBtn);
        createBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AccountActivity accountActivity = (AccountActivity) getActivity();
                accountActivity.onFragmentChanged(1);
            }
        });

        return view;
    }
}
