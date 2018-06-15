package com.example.admin.asdf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Setting_Area_Activity extends Activity{
    String setArea;
    String name;
    EditText ed_name;
    EditText ed_meter;
    int chose_marker=0;
    ImageView cafe, school, pcroom, friend, cancel;
    final int Result_OK = 1001;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        //상단 타이틀바 제거.
        super.onCreate(saveInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.set_tag_meter);
        System.out.println("Setting_Area_Activity 시작 \n");
        //ed_name에는 name, ed_meter에는 거리
        ed_name = (EditText)findViewById(R.id.setname);
        ed_meter = (EditText)findViewById(R.id.setmeter);
        cafe = (ImageView)findViewById(R.id.cafe);
        school = (ImageView)findViewById(R.id.school);
        pcroom = (ImageView)findViewById(R.id.pcroom);
        friend = (ImageView)findViewById(R.id.friend);
        cancel = (ImageView)findViewById(R.id.cancel);
        setting();

        cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting();
                chose_marker = 1;
                cafe.setBackgroundResource(R.drawable.check);
            }
        });
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting();
                chose_marker = 2;
                school.setBackgroundResource(R.drawable.check);
            }
        });
        pcroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting();
                chose_marker = 3;
                pcroom.setBackgroundResource(R.drawable.check);
            }
        });

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting();
                chose_marker = 4;
                friend.setBackgroundResource(R.drawable.check);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //ok버튼 누를시
    public void OK(View view) {
        name = ed_name.getText().toString();
        setArea = ed_meter.getText().toString();
        Intent result = new Intent(Setting_Area_Activity.this, MainActivity.class);
        //입력값 없을시 값 안넘어가짐.
        if(name.getBytes().length <= 0) {
            Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_LONG).show();
        }
        else if(setArea.getBytes().length <= 0) {
            Toast.makeText(this, "범위를 정해주세요.", Toast.LENGTH_LONG).show();
        }
        else if(chose_marker == 0) {
            Toast.makeText(this, "마커를 선택해주세요.", Toast.LENGTH_LONG).show();
        }
        else {
            result.putExtra("name", name);
            result.putExtra("area", setArea);
            result.putExtra("marker", chose_marker);
            setResult(Result_OK, result);
            System.out.println("Main액티비티로 돌려주는값 : " + name + " / " + setArea);
            System.out.println("Setting_Area_Acitivity 종료 \n");
            finish();
        }
    }

    //체크 해제
    void setting() {
        cafe.setBackgroundResource(R.drawable.coffee);
        school.setBackgroundResource(R.drawable.school);
        pcroom.setBackgroundResource(R.drawable.pcroom);
        friend.setBackgroundResource(R.drawable.friend);
        cafe.setVisibility(View.VISIBLE);
        school.setVisibility(View.VISIBLE);
        pcroom.setVisibility(View.VISIBLE);
        friend.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
    }
}
