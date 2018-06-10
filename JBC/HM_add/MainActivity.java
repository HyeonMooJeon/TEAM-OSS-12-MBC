package com.example.admin.asdf;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


// 현재시점부터 출력할 것임.
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    //2018-05-30 add
    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    String data;
    double latitude ;
    double longitude ;
    TextView tv;
    //병철 사용 변수
    final int Request_OK = 1000;
    final int Result_OK = 1001;
    int i=0;                    //버튼 클릭. %2 나머지 1과 0으로 눌렸나 안눌렸나 확인.
    double MKlong, MKlati;      //범위 정하는 버튼시 그때의 위도.경도 저장 변수
    String name;                //Setting_Area_Activity에서 받는 이름
    int Area;                   // 같이 받은 범위 길이.
    ToggleButton Areaset;       //버튼 on,off 가능 토글 버튼으로 만듬
    MarkerOptions mymarker;     //마커추가
    CircleOptions setArea;      //범위 원형으로 추가
    //----------
    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;                    //구글맵 메인
    private Marker currentMarker = null;

    //2018-06-04 add
    // private LatLng startLatLng = new LatLng(latitude, longitude);        //polyline 시작점
    //private LatLng endLatLng = new LatLng(latitude, longitude);        //polyline 끝점
    //private boolean walkState = false;                    //걸음 상태
    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocatiion;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    Intent popup;
    LatLng currentPosition;
    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    @Override
    protected void onCreate(Bundle savedInstanceState) { //oncreate 시작

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        // StrictMode는 개발자가 실수하는 것을 감지하고 해결할 수 있도록 돕는 일종의 개발 툴
        // - 메인 스레드에서 디스크 접근, 네트워크 접근 등 비효율적 작업을 하려는 것을 감지하여
        //   프로그램이 부드럽게 작동하도록 돕고 빠른 응답을 갖도록 함, 즉  Android Not Responding 방지에 도움
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        tv =(TextView) findViewById(R.id.tv);

        //병철 getdistance()사이 거리알기, SettingArea() 범위 정하기
        Button trackingbutton = (Button) findViewById(R.id.TrackingButton);
        Areaset = (ToggleButton) findViewById(R.id.SetArea);
        Areaset.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             i++;
             if (i % 2 == 1) {
                 System.out.println("액티비티 전환\n");
                 popup = new Intent(MainActivity.this, Setting_Area_Activity.class);
                 startActivityForResult(popup,Request_OK);
             } else {
                 System.out.println("표시 삭제\n");
                 DelArea();
             }
         }
    });
        //---------------------------------------------

        trackingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread worker = new Thread() {    //worker 를 Thread 로 생성
                    public void run() { //스레드 실행구문
                        try {
                            //소켓을 생성하고 입출력 스트립을 소켓에 연결한다.
                            socket = new Socket("192.168.55.140", 5000); //소켓생성
                            out = new PrintWriter(socket.getOutputStream(), true); //데이터를 전송시 stream 형태로 변환하여// 전송한다.
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //데이터 수신시 stream을 받아들인다.

                        } catch (IOException e) { e.printStackTrace(); }

                        try {
                            while (true) { //스레드 루프

                                out.println(tv.getText()); //텍스트에 입력한 문자열을 서버에 송신한다.

                                data = in.readLine(); // in으로 받은 데이타를 String 형태로 읽어 data 에 저장

                                String str = data;
                                String[] array = str.split(","); //받은 data를 잘라서 넣는다. 서버에서 처리해줘야함 ex) baby_1

                                latitude = Double.parseDouble(array[0]);
                                longitude = Double.parseDouble(array[1]);
                                //병철---범위 벗어났을 경우 Toast로 보여주며 진동 추가.
                                if(Areaset.isChecked()) {
                                    if(getdistance(latitude, MKlati, longitude, MKlong)>1000) {
                                        showToast();
                                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                        vibe.vibrate(500);
                                    }
                                }
                                //------------------------------------

                            }
                        } catch (Exception e) {
                        }
                    } //
                };  //worker 스레드 종료
                /*
                Thread AutopoliLine = new Thread(){ // 경로표시 스레드
                    public void run(){
                        while(true) {
                            polylineOptions = new PolylineOptions();
                            polylineOptions.color(Color.GREEN); //초록색설정
                            polylineOptions.width(5);           //폭 5
                            LatLng latLng = new LatLng(latitude, longitude); //현재위치 넣어줌.
                            arrayPoints.add(latLng);    //LatLng는  위,경도
                            polylineOptions.addAll(arrayPoints);
                            mGoogleMap.addPolyline(polylineOptions);
                            try {
                                Thread.sleep(600000); //60만= 600초 10분마다한번씩 위치 찍어준다.
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                };      //경로표시 스레드 종료


                AutopoliLine.start();
                */
                worker.start();
            }

        });

        Log.d(TAG, "onCreate");
        mActivity = this;


        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    } //oncreate 끝


    /*
    private void drawPath(){        //polyline을 그려주는 메소드
        PolylineOptions options = new PolylineOptions().add(startLatLng).add(endLatLng).width(15).color(Color.BLACK).geodesic(true);
        polylines.add(mMap.addPolyline(options));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 18));
    }
    */


    @Override
    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }
        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }

    private void startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }
            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void stopLocationUpdates() {
        Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");
        mGoogleMap = googleMap;
        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();
        //mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){

            @Override
            public boolean onMyLocationButtonClick() {
                Log.d( TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.d( TAG, "onMapClick :");
            }
        });
        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {
                if (mMoveMapByUser == true && mRequestingLocationUpdates){
                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }
                mMoveMapByUser = true;
            }
        });
        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
            }
        });
    }

    @Override //위도 경도 받아오기
    public void onLocationChanged(Location location) {
        currentPosition = new LatLng(latitude,longitude);
        // = new LatLng( location.getLatitude(), location.getLongitude());  //이부분에 데이터 위 경도값 줘야함.
        //latitude longitude
        Log.d(TAG, "onLocationChanged : ");
        String markerTitle = getCurrentAddress(currentPosition);
        String markerSnippet = "위도:" + String.valueOf(latitude)
                + " 경도:" + String.valueOf(longitude);
        //수정 위,경도
        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet);
        mCurrentLocatiion = location;
    }

    @Override
    protected void onStart() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){
            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }
        super.onStart();
        //worker.start();
    }

    @Override
    protected void onStop() {
        if (mRequestingLocationUpdates) {
            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }
        if ( mGoogleApiClient.isConnected()) {
            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        if ( mRequestingLocationUpdates == false ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                } else {
                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }else{
                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }

    public String getCurrentAddress(LatLng latlng) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견"+latitude+"-"+longitude+"data"+data, Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        mMoveMapByUser = false;
        if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(latitude,longitude);

        MarkerOptions markerOptions = new MarkerOptions(); //마커 객체생성
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        //구글맵의 디폴트 현재 위치는 파란색 동그라미로 표시
        //마커를 원하는 이미지로 변경하여 현재 위치 표시하도록 수정 fix - 2017. 11.27
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baby));

        currentMarker = mGoogleMap.addMarker(markerOptions);

        if ( mMoveMapByAPI ) {

            Log.d( TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude() ) ;
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }

    public void setDefaultLocation() {
        mMoveMapByUser = false;

        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mGoogleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if ( mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {
            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permissionAccepted) {
                if ( mGoogleApiClient.isConnected() == false) {
                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }
            } else {
                checkPermissions();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                askPermissionOnceAgain = true;
                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS); //위치서비스 활성화
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    //병철 switch 전까지.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    //--------------------------

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");
                        if ( mGoogleApiClient.isConnected() == false ) {
                            Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }
                break;
        }
        if(resultCode == Result_OK || requestCode == Request_OK)
        {
            try{
                String getarea= data.getExtras().getString("area");
                Area = Integer.parseInt(getarea);
                System.out.println("area을 가져옵니다 : " + Area+"\n");
                String getname = data.getExtras().getString("name");
                name = getname;
                System.out.println("name을 가져옵니다." + name+"\n");
            } catch(NullPointerException e) {};

            System.out.println("데이터를 가져옵니다.\n");
            System.out.println("넓이는 "+Area + "이름은 " + name +"\n");
            SettingArea();
        }
    }

    //병철 아래 싹 전부
    public void DelArea() {
        System.out.println("DelArea 호출 \n");
        this.mGoogleMap.clear();
    }

    public void SettingArea(){
        System.out.println("SettingArea 호출 \n");
        MKlati = latitude;
        MKlong = longitude;
        LatLng position = new LatLng(latitude , longitude);


        //나의위치 마커
        mymarker = new MarkerOptions().position(position);   //마커위치
        System.out.println("마커 title에 들어간는 값은 " + name + " 입니다 거리는 " + Area + " 입니다 \n");
        mymarker.title(name);
        mymarker.snippet("범위 : " + Area+"m");

        // 반경 1KM원
        setArea = new CircleOptions().center(position) //원점
                .radius(Area)      //반지름 단위 : 1km = 1000m
                .strokeWidth(1f)  //선 두께 1f
                .fillColor(Color.parseColor("#880000ff")); //배경색

        //마커추가
        this.mGoogleMap.addMarker(mymarker);

        //원추가
        this.mGoogleMap.addCircle(setArea);

    }

    public void showToast() {
        Toast.makeText(this, "범위 벗어남", Toast.LENGTH_LONG).show();
    }

    public double getdistance(double lati1, double lati2, double long1, double long2){

        double theta, dist;
        theta = long1 - long2;
        dist = Math.sin(deg2rad(lati1)) * Math.sin(deg2rad(lati2)) + Math.cos(deg2rad(lati1)) * Math.cos(deg2rad(lati2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }
    /*
    각의 단위 그리는 방법.
    디그리(degree) = 원 한바퀴를 360도로 표현하는 방법(일상에서 쓰는 법)
    라디안(radioan) = 반지름의 길이와 두개의 반지름이 이루는 호의 길이가 같은 경우가 1 라디안 이다.
    Math.PI = 파이(3.141592...)
    */
    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad){
        return (double)(rad * (double)180 / Math.PI);
    }
}
