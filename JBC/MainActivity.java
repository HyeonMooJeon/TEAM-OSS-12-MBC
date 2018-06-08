package com.example.admin.safasdf;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

//병철 테스트 ip, port
//ip = 192.168.55.140, port = 5001
public class MainActivity extends AppCompatActivity {

    TextView tv,lati, longi, ID;
    ToggleButton tb;
    public String sendData;
    String getID;
    public Socket socket;
    //----------------------------------------------------포트와 IP꼭 바꿔줄것!!
    public static final int PORT = 7890;
    public static final String IP = "192.168.55.140";
    public BufferedWriter networkWriter;
    Timer timer = null;
    TimerTask t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        //Login액티비티에서 ID받아오기
        Intent intent = getIntent();
        getID = intent.getStringExtra("passID");

        final Thread thread = new Thread() {
            public void run() {
                try {
                    socket = new Socket(IP, PORT);
                    networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //서버가 없을 경우.
            }
        };
        thread.start();
        //퍼미션 체크
        checkPermissionF();
        tv = (TextView) findViewById(R.id.textView2);
        lati = (TextView) findViewById(R.id.sendlati);
        longi = (TextView) findViewById(R.id.sendlongi);
        ID = (TextView)findViewById(R.id.getid);
        ID.setText(getID);
        tv.setText("위치정보 미수신중");

        tb = (ToggleButton)findViewById(R.id.toggle1);
        // im LocationManager얻어옴.
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //버튼 클릭 확인.
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(tb.isChecked()){ //수신될 경우
                        tv.setText("수신중..");
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,  1000000, mLocationListener);
                                // 위치제공자 , 시간간격, 변경거리
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1000000, mLocationListener);

                        t = new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    String getinfo = SendData();
                                    PrintWriter out = new PrintWriter(networkWriter, true);
                                    out.println(getinfo);
                                    System.out.println("\n서버로 보내지는 데이터 = " + out +"\n보내야 하는 데이터 "+ getinfo);
                                }  catch (Exception e) { }
                            }
                        };
                        timer = new Timer();
                        timer.schedule(t,0,10000);

                    }else{              //수신이 안되는경우
                        tv.setText("위치정보 미수신중");
                        lm.removeUpdates(mLocationListener);  //  미수신중일떄 자원 해제해준다.

                        timer.cancel();
                        t.cancel();
                    }
                }catch(SecurityException ex){ }
            }
        });
    }

    public String SendData() {
        System.out.println("SendData를 호출했습니다.\n\n");
        return sendData;
    }

    //위치 정보 각각에 값 넣기.
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //위치값 변경시 이벤트 실행
            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도는 실행문서 제거함.
            float accuracy = location.getAccuracy();    //정확도도 제거
            String provider = location.getProvider();   //위치제공자
            tv.setText("위치정보 : " + provider);
            lati.setText(""+latitude);
            longi.setText(""+longitude);
            //해시값 사용하기위해 콤마(,)로 바꿈.
            sendData = getID + ","+Double.toString(latitude)+"/" + Double.toString(longitude);
            //Intent ID = getIntent()
            //String PW = IDPW.getStringExtra("passID") 또는 passPW 입력하면 값을 받음.
            //sendlongi = 경도   sendlati = 위도    getid = 아이디 얻어오기, 여기서는 lati, longi, ID
            // + "\n고도 : " + altitude + "\n정확도 : "  + accuracy
            //위에 주석 코드 앞으로 넣으면 고도, 정확도까지 제공.
            //-----------------데이터 보내기----------------
            //        아이디 +         위도              +         경도
            /*
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write("" + sendData);
                bufferedWriter.flush();
            } catch (Exception e){
                e.printStackTrace();
            }
            */
        }
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    //퍼미션 체크.
    private void checkPermissionF() {
        //안드로이드 버젼확인, 4.0이전, 후로 방식이 다름.
        if (android.os.Build.VERSION.SDK_INT >= 4.0) {
            int permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //요청 권한, 한번이라도 거부하면 true.
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("단말기의 GPS 권한이 필요합니다.\\n계속하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (Build.VERSION.SDK_INT >= 4.0) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"권한 요청 취소", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();
                    //앱 설치후 처음 시작.
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    //        getThumbInfo();
                }
            }else{       //권한이 있을 경우
            }
        } else {         //마시멜로 이하 버젼인경우.. 요새는 그닥없으니 별도 처리 X
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            // 권한을 허용시 인텐트를 띄워라
            for(int i = 0 ; i < permissions.length ; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult WRITE_EXTERNAL_STORAGE ( 권한 성공 ) ");
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult ACCESS_FINE_LOCATION ( 권한 성공 ) ");
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult ACCESS_COARSE_LOCATION ( 권한 성공 ) ");
                    }
          /*          if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult READ_PHONE_STATE ( 권한 성공 ) ");
                    }*/
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("onRequestPermissionsResult READ_EXTERNAL_STORAGE ( 권한 성공 ) ");
                    }
                }
            }
        } else {
            System.out.println("onRequestPermissionsResult ( 권한 거부) ");
            Toast.makeText(getApplicationContext(), "요청 권한 거부", Toast.LENGTH_SHORT).show();
        }

    }
    //앱 종료시 소켓도 닫아줌.
    @Override
    public void onStop() {
        super.onStop();
        try{
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
