package com.example.kimilm.timetable;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import org.bson.Document;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

//계정 관리 액티비티
public class AccountActivity extends AppCompatActivity implements AccountCreateFragment.OnCreateAccountSetListener, AccountLoginFragment.OnLoginAccSetListener, AccountLogoutDeleteFragment.OnCurAccCheckSetListener
{
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
        // MainActivity에서 isCurAcc과 curAccId를 받음.
        Intent isCurAccIntent = getIntent();
        isCurAcc = isCurAccIntent.getBooleanExtra("isCurAcc", false);

        //계정 정보가 있다면
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

        //계정정보 유무에 따라 다른 프래그먼트 실행
        if(isCurAcc == true)
        {
            fragmentTransaction.replace(R.id.fragment_lay, new AccountLogoutDeleteFragment());
            fragmentTransaction.commit();
        }
        else
        {
            fragmentTransaction.replace(R.id.fragment_lay, new AccountLoginFragment());
            fragmentTransaction.commit();
        }
    }

    public void onFragmentChanged(int index)
    {
        // 프래그먼트 간 변환 인덱스.
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

    //유저 정보를 로컬에 저장하는 코드
    public static void saveAccount(String folderDir, Friend friend)
    {
        File file = new File(folderDir + "AccInDevice.json");

        Document userObj = new Document("_id", friend.getId()).append("pwd", friend.getPw())
                .append("name", friend.getName()).append("f_id", friend.getFrList());

        try
        {
            FileWriter output = new FileWriter(file);

            output.write(userObj.toJson());

            output.close();
        }
        catch (Exception e)
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

        //데이터 베이스에 유저 정보 저장
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

        // 디바이스에도 저장, 이 때 서버나 로컬에 저장할 시간표는 아직 없음
        Friend fr_new = new Friend(inputId, inputPw, inputName, new ArrayList<String>());

        saveAccount(TimeTable.folderPath, fr_new);

        isCurAcc = true;
        Intent retIntent = new Intent(this, MainActivity.class);

        //새로운 정보를 저장하여 메인으로 송신
        Friend dbUserData = new Friend();

        dbUserData.setId(inputId);
        dbUserData.setPw(inputPw);
        dbUserData.setName(inputName);

        retIntent.putExtra("friendInfo", dbUserData);

        retIntent.putExtra("isCurAcc", isCurAcc);
        setResult(RESULT_OK, retIntent);

        //시간표 다시 그리기
        MainActivity.toPrintTable = true;

        finish();
    }


    // AccountLoginFragment에서 정보를 가져와서 로그인을 하는 코드 ↓
    @Override
    public void onLoginAccSet(int btnType, String loginId, String loginPw)
    {
        final String id = loginId;
        final String pwd = loginPw;

        //유저 검색
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

        // 서버에 아이디가 존재하면
        if(loginDoc.get(0) != null)
        {
            isCurAcc = true;

            Friend dbUserData = new Friend();

            //저장할 데이터 세팅
            dbUserData.setId(loginDoc.get(0).getString("_id"));
            dbUserData.setPw(loginDoc.get(0).getString("pwd"));
            dbUserData.setName(loginDoc.get(0).getString("name"));

            //TimeTable을 어플리케이션으로 띄워둠, 전역변수처럼 사용하려고
            TimeTable.setTimeTable((Document)(loginDoc.get(0).get("timetable")));

            dbUserData.setFrList((ArrayList<String>)(loginDoc.get(0).get("f_id", ArrayList.class)));

            retIntent.putExtra("friendInfo", dbUserData);

            //디바이스 저장
            TimeTable.saveTable();
            saveAccount(TimeTable.folderPath, dbUserData);

            //시간표 그리기
            MainActivity.toPrintTable = true;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "해당 계정이 없습니다.", Toast.LENGTH_SHORT).show();
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
        //로그아웃, 계정 삭제시 데이터 리셋
        TimeTable.resetData();
        TimeTable.saveTable();

        //계정 파일도 삭제
        new File(TimeTable.folderPath + "AccInDevice.json").delete();

        //시간표 초기화
        MainActivity.fragmentAdapter.notifyDataSetChanged();

        //메인으로 데이터 송신
        Intent retIntent = new Intent(this, MainActivity.class);
        retIntent.putExtra("btnType", btnType);
        retIntent.putExtra("isCurAcc", isCurAcc);
        setResult(RESULT_OK, retIntent);

        finish();
    }
}
