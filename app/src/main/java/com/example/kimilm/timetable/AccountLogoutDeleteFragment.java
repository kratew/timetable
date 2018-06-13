package com.example.kimilm.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class AccountLogoutDeleteFragment extends Fragment {

    String curAccIdData;
    TextView curIdText;
    Button logoutBtn;
    Button deleteAccBtn;
    boolean isCurAcc;
    int btnType;

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

        curIdText = (TextView)view.findViewById(R.id.curId);
        logoutBtn = (Button)view.findViewById(R.id.logoutBtn);
        deleteAccBtn = (Button)view.findViewById(R.id.deleteAccBtn);

        curIdText.setText(curAccIdData); //현재 디바이스의 계정 정보를 가져와서 아이디를 curId에 받은 후 curIdText에 뿌리는 코드

        // 로그아웃 버튼 누르면 -> 현재 디바이스의 계정 정보 삭제 && 로그인 AccountLoginFragment로 변환. ↓
        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                File files = new File(getActivity().getFilesDir(), "AccInDevice.json");
                files.delete(); // 파일을 디바이스에서 삭제.
                AccountActivity accountActivity = (AccountActivity) getActivity();
                accountActivity.onFragmentChanged(0);
                isCurAcc = false;
                btnType = 3;
                onCurAccCheckSetListener.OnCurAccCheckSet(isCurAcc, btnType);
                getActivity().finish();
            }
        });

        // 계정 삭제 버튼 누르면 -> 서버에서 계정 삭제하고 로그아웃! ↓
        deleteAccBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                File files = new File(getActivity().getFilesDir(), "AccInDevice.json");
                files.delete(); // 파일을 디바이스에서 삭제.
                /*
                ────────────────────────────────
                현재 계정을 서버에서 삭제하는 코드!!
                ────────────────────────────────
                */
                isCurAcc = false;
                btnType = 4;
                onCurAccCheckSetListener.OnCurAccCheckSet(isCurAcc, btnType);
                getActivity().finish();
            }
        });
        return view;
    }

    // 로그아웃, 계정삭제 버튼이 눌리면 AccountActivity에 isCurAcc = false를 전달하는 코드. ↓
    public interface OnCurAccCheckSetListener{
        void OnCurAccCheckSet(boolean isCurAcc, int btnType);
    }
    private OnCurAccCheckSetListener onCurAccCheckSetListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // MainActivity -> AccountActivity -> 이 프래그먼트 현재 디바이스에 있는 아이디 데이터 전달받은거 ↓
        if(getActivity() != null && getActivity() instanceof AccountActivity){
            curAccIdData = ((AccountActivity)getActivity()).getData();
        }
        if(context instanceof OnCurAccCheckSetListener){
            onCurAccCheckSetListener = (OnCurAccCheckSetListener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateAccountSetListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCurAccCheckSetListener = null;
    }

}
