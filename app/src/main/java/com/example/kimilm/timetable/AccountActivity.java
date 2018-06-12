package com.example.kimilm.timetable;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity implements AccountCreateFragment.OnCreateAccountSetListener, AccountLoginFragment.OnLoginAccSetListener{

    AccountLoginFragment frlogin;
    AccountCreateFragment frcreate;
    AccountLogoutDeleteFragment frlogoutdelacc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        frlogin = new AccountLoginFragment();
        frcreate = new AccountCreateFragment();
        frlogoutdelacc = new AccountLogoutDeleteFragment();

        // AccountActivity가 떴을 때 AccountLoginFragment를 가져오는 코드 ↓
        /*
        ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────
        기존 디바이스 계정 정보가 있으면 AccountLogoutDeleteFragment를, 없으면 AccountLoginFragment를 가져오는 코드 작성 요망!!!
        ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_lay, new AccountLoginFragment());
        fragmentTransaction.commit();

    }

    public void onFragmentChanged(int index) {  // 프래그먼트 간 변환 인덱스.
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frlogin).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frcreate).commit();
        } else if (index == 2){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frlogoutdelacc).commit();
        }
    }

    // AccountCreateFragment에서 정보를 가져와서 새로운 계정을 만들고 서버에 저장하는 코드 ↓
    @Override
    public void onCreateAccountSet(int btnType, String inputId, String inputPw, String inputName) {

        // 가져온 정보를 디바이스에 저장하는 코드 ↓

        JSONObject obj = new JSONObject();
        Friend fr_new = new Friend(inputId, inputPw, inputName, new TimeTable(), new ArrayList<String>());
        try {
            obj.put("_id", fr_new.getId());
            obj.put("pwd", fr_new.getPw());
            obj.put("name", fr_new.getName());
            obj.put("timetable", null);
            obj.put("f_id", null);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try{
            fos = openFileOutput("AccInDevice.txt", Context.MODE_PRIVATE);
            fos.write(obj.toString().getBytes());
            fos.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }


        /*
        ───────────────────────────────────────────────────────────────────────────────────
        이 메소드에서 받은 정보들로 새로운 계정을 만들고 서버에 계정정보를 저장하는 코드 추가 요망!!!
        ───────────────────────────────────────────────────────────────────────────────────
         */
    }

    // AccountLoginFragment에서 정보를 가져와서 로그인을 하는 코드 ↓
    @Override
    public void onLoginAccSet(int btnType, String loginId, String loginPw) {


        /*
        ───────────────────────────────────────────────────
        이 메소드에서 받은 정보들로 로그인 하는 코드 추가 요망!!!
        ───────────────────────────────────────────────────
         */
    }


}
