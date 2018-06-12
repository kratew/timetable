package com.example.kimilm.timetable;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AccountActivity extends AppCompatActivity {

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_lay, new AccountLoginFragment());
        fragmentTransaction.commit();
    }
/*
    public void switchFragment() {
        Fragment fr;

        if (isFragmentB) {
            fr = new FragmentB() ;
        } else {
            fr = new FragmentC() ;
        }

        isFragmentB = (isFragmentB) ? false : true ;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentBorC, fr);
        fragmentTransaction.commit();
    }
    */

    public void onFragmentChanged(int index) {  // 프래그먼트 간 변환 메소드.
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frlogin).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frcreate).commit();
        } else if (index == 2){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frlogoutdelacc).commit();
        }
    }
}
