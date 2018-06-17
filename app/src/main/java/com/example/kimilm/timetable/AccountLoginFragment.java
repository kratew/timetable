package com.example.kimilm.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

//로그인 프래그먼트
public class AccountLoginFragment extends Fragment
{
    int btnType;
    String loginId;
    String loginPw;
    EditText inputLoginId;
    EditText inputLoginPw;
    Button createBtn;
    Button loginBtn;

    public AccountLoginFragment()
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
        View view = inflater.inflate(R.layout.fragment_account_login, container, false);

        inputLoginId = (EditText)view.findViewById(R.id.inputLoginId);
        inputLoginPw = (EditText)view.findViewById(R.id.inputLoginPw);
        loginBtn = (Button)view.findViewById(R.id.loginBtn);
        createBtn = (Button)view.findViewById(R.id.cteateAccBtn);

        // 계정 생성 버튼 누르면 -> AccountCreateFragment로 변환. ↓
        createBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AccountActivity accountActivity = (AccountActivity) getActivity();
                accountActivity.onFragmentChanged(1);
            }
        });

        // 로그인 버튼을 누르면 입력한 양식을 AccountActivity로 쏴주는 코드 ↓
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                btnType = 2;
                loginId = inputLoginId.getText().toString();
                loginPw = inputLoginPw.getText().toString();

                onLoginAccSetListener.onLoginAccSet(btnType, loginId, loginPw);
            }
        });

        return view;
    }

    // AccountActivity로 데이터를 보내기 위한 인터페이스 생성 ↓
    public interface OnLoginAccSetListener
    {
        void onLoginAccSet(int btnType, String loginId, String loginPw);
    }

    private OnLoginAccSetListener onLoginAccSetListener;
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof OnLoginAccSetListener)
        {
            onLoginAccSetListener = (OnLoginAccSetListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateAccountSetListener");
        }
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        onLoginAccSetListener = null;
    }
}
