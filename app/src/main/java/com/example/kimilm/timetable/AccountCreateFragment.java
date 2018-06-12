package com.example.kimilm.timetable;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AccountCreateFragment extends Fragment {

    EditText inputId;
    EditText inputPw;
    EditText inputName;
    Button create;
    int btnType;
    String createId;
    String createPw;
    String createName;

    public AccountCreateFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_create, container, false);

        inputId = (EditText)view.findViewById(R.id.inputId);
        inputPw = (EditText)view.findViewById(R.id.inputPw);
        inputName = (EditText)view.findViewById(R.id.inputName);
        create = (Button)view.findViewById(R.id.createAccBtn);

        // 계정생성 및 로그인 버튼을 누르면 입력 양식을 AccountActivity로 쏴주는 코드 ↓
        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btnType = 1;
                createId = inputId.getText().toString();
                createPw = inputPw.getText().toString();
                createName = inputName.getText().toString();

                onCreateAccountSetListener.onCreateAccountSet(btnType, createId, createPw, createName);
            }
        });

        return view;
    }

    // AccountActivity로 데이터를 보내기 위한 인터페이스 생성 ↓
    public interface OnCreateAccountSetListener{
        void onCreateAccountSet(int btnType, String inputId, String inputPw, String inputName);
    }
    private OnCreateAccountSetListener onCreateAccountSetListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnCreateAccountSetListener){
            onCreateAccountSetListener = (OnCreateAccountSetListener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateAccountSetListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        onCreateAccountSetListener = null;
    }






}
