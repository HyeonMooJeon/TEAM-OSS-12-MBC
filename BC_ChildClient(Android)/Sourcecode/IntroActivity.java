package com.example.person.bc_child;
//앱 시작시 보여주는 창.
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //앱이 시작된 후 intent를 통해 현재 액티비티 -> Login액티비티로 이동.
                Intent intent = new Intent(IntroActivity.this,  LoginActivity.class);
                startActivity(intent);
                //뒤로 버튼 눌르면 안나오고 종료가 되도록 finish로 액티비티 완전 종료
                finish();
            }
            //2초 지속
        }, 2000);
    }
}