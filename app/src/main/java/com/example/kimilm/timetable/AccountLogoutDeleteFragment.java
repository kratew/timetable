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
import android.widget.EditText;

public class AccountLogoutDeleteFragment extends Fragment {

    EditText curIdText;
    Button logoutBtn;
    Button deleteAccBtn;
    String curId;

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

        curIdText = (EditText)view.findViewById(R.id.curId);
        logoutBtn = (Button)view.findViewById(R.id.logoutBtn);
        deleteAccBtn = (Button)view.findViewById(R.id.deleteAccBtn);

        /*
        ──────────────────────────────────────────────────────────────────────────────────
        현재 디바이스의 계정 정보를 가져와서 아이디를 curId에 받은 후 curIdText에 뿌리는 코드!!!
        ──────────────────────────────────────────────────────────────────────────────────
         */

        // 로그아웃 버튼 누르면 -> 로그인 AccountLoginFragment로 변환. ↓
        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AccountActivity accountActivity = (AccountActivity) getActivity();
                accountActivity.onFragmentChanged(0);
            }
        });

        // 계정 삭제 버튼 누르면 -> 서버에서 계정 삭제하고 로그아웃! ↓
        deleteAccBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /*
                ────────────────────────────────────────────────────────
                현재 계정을 디바이스에서도 제거하고 서버에서도 삭제하는 코드!!
                ────────────────────────────────────────────────────────
                */
            }
        });
        return view;
    }
}
