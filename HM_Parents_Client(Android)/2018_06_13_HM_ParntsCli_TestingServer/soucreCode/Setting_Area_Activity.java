package com.example.person.googlemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class Setting_Area_Activity extends Activity{
    String setArea;
    String name;
    EditText ed_name;
    EditText ed_meter;
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
    }

    public void OK(View view) {
        name = ed_name.getText().toString();
        setArea = ed_meter.getText().toString();
        Intent result = new Intent(Setting_Area_Activity.this, MainActivity.class);
        result.putExtra("name", name);
        result.putExtra("area", setArea);
        setResult(Result_OK, result);
        System.out.println("Main액티비티로 돌려주는값 : " + name + " / " +  setArea);
        System.out.println("Setting_Area_Acitivity 종료 \n");
        finish();
    }
}
