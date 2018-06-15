package com.example.kimilm.timetable;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mongodb.util.JSON;

import org.bson.Document;
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

    ArrayList<Document> loginDoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        frlogin = new AccountLoginFragment();
        frcreate = new AccountCreateFragment();
        frlogoutdelacc = new AccountLogoutDeleteFragment();

        // 기존 디바이스 계정 정보가 있으면 AccountLogoutDeleteFragment를, 없으면 AccountLoginFragment를 가져오는 코드 ↓
        Intent isCurAccIntent = getIntent();    // MainActivity에서 isCurAcc과 curAccId를 받음.
        isCurAcc = isCurAccIntent.getBooleanExtra("isCurAcc", false);

        if(isCurAcc == true)
        {
            curAccId = isCurAccIntent.getStringExtra("curAccId");
            Toast.makeText(this, isCurAcc + curAccId, Toast.LENGTH_LONG);
        }
        else
        {
            Toast.makeText(this, isCurAcc+"", Toast.LENGTH_LONG);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isCurAcc == true)
        {
            fragmentTransaction.replace(R.id.fragment_lay, new AccountLogoutDeleteFragment());
            fragmentTransaction.commit();
            //Toast.makeText(this, "현재 디바이스에 계정 정보가 있음.", Toast.LENGTH_LONG).show();
        }
        else
        {
            fragmentTransaction.replace(R.id.fragment_lay, new AccountLoginFragment());
            fragmentTransaction.commit();
            //Toast.makeText(this, "현재 디바이스에 계정 정보가 없음.", Toast.LENGTH_LONG).show();
        }
    }

    public void onFragmentChanged(int index)
    {  // 프래그먼트 간 변환 인덱스.
        if (index == 0)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frlogin).commit();
        }
        else if (index == 1)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frcreate).commit();
        }
        else if (index == 2)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_lay, frlogoutdelacc).commit();
        }
    }

    // AccountCreateFragment에서 정보를 가져와서 새로운 계정을 만들고 서버에 저장하는 코드 ↓
    @Override
    public void onCreateAccountSet(int btnType, String inputId, String inputPw, String inputName) {

    public void saveAccount(Friend friend)
    {
        JSONObject obj = new JSONObject();

        try
        {
            obj.put("_id", friend.getId());
            obj.put("pwd", friend.getPw());
            obj.put("name", friend.getName());
            obj.put("f_id", friend.getFrList());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        File file = new File(getFilesDir(), "AccInDevice.json");
        FileWriter fw = null;
        BufferedWriter bufwr = null;

        try
        {
            fw = new FileWriter(file);
            bufwr = new BufferedWriter(fw);
            bufwr.write(obj.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            if(bufwr != null)
            {
                bufwr.close();
            }
            if(fw != null)
            {
                fw.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // 유저 정보 등록에 성공했는가
    boolean insertSuccess = false;

    // AccountCreateFragment에서 정보를 가져와서 새로운 계정을 만들고 서버에 저장하는 코드 ↓
    @Override
    public void onCreateAccountSet(int btnType, String inputId, String inputPw, String inputName)
    {
        final String id = inputId;
        final String pwd = inputPw;
        final String name = inputName;

        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    insertSuccess = UseDB.insertUser(id, pwd, name);
                }
            }
        };

        thread.start();

        try { thread.join(); } catch (Exception e) {}

        //실패
        if(!insertSuccess)
        {
            Toast.makeText(TimeTable.fragment.getActivity(), "사용자가 이미 존재합니다", Toast.LENGTH_SHORT).show();

            return;
        }

        // 디바이스에도 저장
        Friend fr_new = new Friend(inputId, inputPw, inputName, new ArrayList<String>());

        saveAccount(fr_new);

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
    public void onLoginAccSet(int btnType, String loginId, String loginPw)
    {
        final String id = loginId;
        final String pwd = loginPw;

        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    UseDB.searchUser(loginDoc, id, pwd);
                }
            }
        };

        thread.start();

        try { thread.join(); } catch (Exception e) {}

        //메인으로 보낼 데이터
        Intent retIntent = new Intent(this, MainActivity.class);

        // 서버에 아이디가 존재하면 true, 없으면 false. 아이디가 있을때, 없을때 코드 나누기.
        if(loginDoc.get(0) != null)
        {
            isCurAcc = true;

            Friend dbUserData = new Friend();

            dbUserData.setId(loginDoc.get(0).getString("_id"));
            dbUserData.setPw(loginDoc.get(0).getString("pwd"));
            dbUserData.setName(loginDoc.get(0).getString("name"));

            //TimeTable을 어플리케이션으로 띄워둠, 전역변수처럼 사용하려고
            TimeTable.setTimeTable((Document)loginDoc.get(0).get("table"));

            dbUserData.setFrList((ArrayList<String>)(loginDoc.get(0).get("f_id", ArrayList.class)));

            retIntent.putExtra("friendInfo", dbUserData);
        }
        else
        {
            isCurAcc = false;
        }

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
    public void OnCurAccCheckSet(boolean isCurAcc, int btnType)
    {
        Intent retIntent = new Intent(this, MainActivity.class);
        retIntent.putExtra("btnType", btnType);
        retIntent.putExtra("isCurAcc", isCurAcc);
        setResult(RESULT_OK, retIntent);
    }
}
