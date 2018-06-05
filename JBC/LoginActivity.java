package com.example.admin.safasdf;
//로그인을 하기위한 창.
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends Activity {
    EditText et_id;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        et_id = (EditText)findViewById(R.id.textID);
    }
    //longin액티비티 안의 버튼중에 onClick아이디를 login으로 준게 있음.
    //그거를 누를경우 아래 시작. 현재 액티비티에서
    public void login(View view) {
        id = et_id.getText().toString();
        Intent forMain = new Intent(LoginActivity.this , MainActivity.class);
        forMain.putExtra("passID", id);
        startActivity(forMain);
    }
}

/*
activity_login의 ID들
textId = ID입력창
textPw = PW입력창
joinbtn = 회원가입 버튼
           - onClick 아이디 = mem_btn
loginbtn = 로그인 버튼
           - onClick 아이디 = log_btn

main에서 받을때
Intent IDPW = getIntent()
String PW = IDPW.getStringExtra("passID") 또는 passPW 입력하면 값을 받음.
 */