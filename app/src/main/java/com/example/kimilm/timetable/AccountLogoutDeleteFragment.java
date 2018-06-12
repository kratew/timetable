package com.example.kimilm.timetable;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AccountLogoutDeleteFragment extends Fragment {

    Button logoutBtn;

    public AccountLogoutDeleteFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_logout_delete, container, false);

        // 로그아웃 버튼 누르면 -> 로그인 AccountLoginFragment로 변환. ↓
        logoutBtn = (Button)view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AccountActivity accountActivity = (AccountActivity) getActivity();
                accountActivity.onFragmentChanged(0);
            }
        });

        return view;
    }
}
