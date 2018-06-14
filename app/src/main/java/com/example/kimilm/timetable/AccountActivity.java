package com.example.kimilm.timetable;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mongodb.util.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity implements AccountCreateFragment.OnCreateAccountSetListener, AccountLoginFragment.OnLoginAccSetListener, AccountLogoutDeleteFragment.OnCurAccCheckSetListener{

    AccountLoginFragment frlogin;
    AccountCreateFragment frcreate;
    AccountLogoutDeleteFragment frlogoutdelacc;
    boolean isCurAcc;
    String curAccId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        frlogin = new AccountLoginFragment();
        frcreate = new AccountCreateFragment();
        frlogoutdelacc = new AccountLogoutDeleteFragment();

        // 기존 디바이스 계정 정보가 있으면 AccountLogoutDeleteFragment를, 없으면 AccountLoginFragment를 가져오는 코드 ↓
        Intent isCurAccIntent = getIntent();    // MainActivity에서 isCurAcc과 curAccId를 받음.
        isCurAcc = isCurAccIntent.getBooleanExtra("isCurAcc", false);
        if(isCurAcc == true){
            curAccId = isCurAccIntent.getStringExtra("curAccId");
            Toast.makeText(this, isCurAcc + curAccId, Toast.LENGTH_LONG);
        }else{Toast.makeText(this, isCurAcc+"", Toast.LENGTH_LONG);}

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isCurAcc == true) {
            fragmentTransaction.replace(R.id.fragment_lay, new AccountLogoutDeleteFragment());
            fragmentTransaction.commit();
            //Toast.makeText(this, "현재 디바이스에 계정 정보가 있음.", Toast.LENGTH_LONG).show();
        }else{
            fragmentTransaction.replace(R.id.fragment_lay, new AccountLoginFragment());
            fragmentTransaction.commit();
            //Toast.makeText(this, "현재 디바이스에 계정 정보가 없음.", Toast.LENGTH_LONG).show();
        }
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

        //──────────────────────────────────────────────────────────────────────────────────────────────────
        /* 완전한 Friend 인스턴스를 만들어보기 위한 코드
        TimeTable tt = new TimeTable();
        int i;
        boolean jbchk[] = new boolean[5 * 14 * 12];
        ArrayList<Lesson> ttl = new ArrayList<>();

        for(i = 0; i < 5*14*12; i++) { jbchk[i] = false; }
        ArrayList<String> ttt = new ArrayList<>();
        ttt.add("10");
        ttt.add("20");
        ttt.add("30");
        ArrayList<String> ttcr = new ArrayList<>();
        ttcr.add("aa");
        ttcr.add("bb");
        Lesson l1 = new Lesson("111","AA","x","3",ttt,"Q",ttcr, 121212);
        Lesson l2 = new Lesson("222","BB","y","2",ttt,"W",ttcr, 232323);
        Lesson l3 = new Lesson("333","CC","z","1",ttt,"E",ttcr, 343434);
        ttl.add(l1);
        ttl.add(l2);
        ttl.add(l3);

        tt.setJungBok(jbchk);
        tt.setLessons(ttl);

        JSONObject tmp = new JSONObject();
        JSONArray lstimes = new JSONArray();
        lstimes.put(jbchk[0]);


        ArrayList<String> f_list = new ArrayList<>();
        f_list.add("hhh");
        f_list.add("iii");
        f_list.add("jjj");

        fr_new.setTable(tt);
        fr_new.setFrList(f_list);

        try {
            int j;
            for(j = 0; j < 5 * 14 * 12; j++) {
                JSONArray ttjb = new JSONArray();
                ttjb.put(j, jbchk[j]);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        JSONObject ttObj = new JSONObject();
        */
        //──────────────────────────────────────────────────────────────────────────────────────────────────────
        try {
            obj.put("_id", fr_new.getId());
            obj.put("pwd", fr_new.getPw());
            obj.put("name", fr_new.getName());
            obj.put("timetable", fr_new.getTable());
            obj.put("f_id", fr_new.getFrList());
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        File file = new File(getFilesDir(), "AccInDevice.json");
        FileWriter fw = null;
        BufferedWriter bufwr = null;
        try {
            fw = new FileWriter(file);
            bufwr = new BufferedWriter(fw);
            bufwr.write(obj.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            if(bufwr != null){
                bufwr.close();
            }
            if(fw != null){
                fw.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        /*
        ────────────────────────────────────────────────────────────────
        이 메소드에서 받은 정보들로 서버에 계정정보를 저장하는 코드 추가 요망!!!
        ────────────────────────────────────────────────────────────────
         */

        isCurAcc = true;
        Intent retIntent = new Intent(this, MainActivity.class);
        retIntent.putExtra("btnType", btnType);
        retIntent.putExtra("newId", inputId);
        retIntent.putExtra("isCurAcc", isCurAcc);
        setResult(RESULT_OK, retIntent);
        finish();
    }


    // AccountLoginFragment에서 정보를 가져와서 로그인을 하는 코드 ↓
    @Override
    public void onLoginAccSet(int btnType, String loginId, String loginPw) {


        /*
        ────────────────────────────────────────────────────────────────────────
        이 메소드에서 받은 정보들로 서버에서 정보를 찾아서 로그인 하는 코드 추가 요망!!!
        ────────────────────────────────────────────────────────────────────────
         */

        isCurAcc = true;// 서버에 아이디가 존재하면 true, 없으면 false. 아이디가 있을때, 없을때 코드 나누기.
        Intent retIntent = new Intent(this, MainActivity.class);
        retIntent.putExtra("isCurAcc", isCurAcc);
        setResult(RESULT_OK, retIntent);
        finish();
    }

    // AccountLogoutDeleteFragment에 curAccId를 전달하는 메소드.
    public String getData(){
        return curAccId;
    }

    // AccountLogoutDeleteFragment에서 로그아웃/계정삭제 버튼이 눌렸을때 바뀐 isCurAcc를 받아오는 코드. ↓
    @Override
    public void OnCurAccCheckSet(boolean isCurAcc, int btnType) {
        Intent retIntent = new Intent(this, MainActivity.class);
        retIntent.putExtra("btnType", btnType);
        retIntent.putExtra("isCurAcc", isCurAcc);
        setResult(RESULT_OK, retIntent);
    }
}
