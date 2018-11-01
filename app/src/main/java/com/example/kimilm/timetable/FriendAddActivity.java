package com.example.kimilm.timetable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//친구 추가를 위한 액티비티
public class FriendAddActivity extends AppCompatActivity
{
    EditText searchId;
    Button searchBtn;
    String frId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);

        searchId = (EditText)findViewById(R.id.searchId);
        searchBtn = (Button)findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                frId = searchId.getText().toString();
                Intent retIntent = new Intent(getApplicationContext(), MainActivity.class);
                retIntent.putExtra("frId", frId);
                setResult(RESULT_OK, retIntent);
                finish();
            }
        });
    }
}
