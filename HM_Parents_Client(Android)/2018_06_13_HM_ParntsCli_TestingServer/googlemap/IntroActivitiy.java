package com.example.person.googlemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivitiy extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                //앱이 시작된 후 intent를 통해 현재 액티비티 -> Main액티비티로 이동.
                Intent intent = new Intent(IntroActivitiy.this, MainActivity.class);

                startActivity(intent);
                //뒤로 버튼 눌르면 안나오고 종료가 되도록 finish로 액티비티 완전 종료
                finish();
            }
            //2초 지속
        }, 3000);
    }
}
