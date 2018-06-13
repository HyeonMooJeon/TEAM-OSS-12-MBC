package com.example.admin.asdf;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

// 2018-06-09 현무 추가 장소 출력위한 google web service 라이브러리
// json으로 받아와 string을 파싱되어져 있는 데이터를 내가 원하는 값만 출력
import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static java.lang.System.exit;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        PlacesListener {

    //2018-05-30 현무 추가
    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    String data;
    double latitude;
    double longitude;
    TextView tv;
    private static int port = 7890;
    private static String ip = "192.168.55.140";

    //병철 추가 사용 변수
    int i = 0;                    //버튼 클릭. %2 나머지 1과 0으로 눌렸나 안눌렸나 확인.
    double MKlong, MKlati;      //범위 정하는 버튼시 그때의 위도.경도 저장 변수
    String name;                //Setting_Area_Activity에서 받는 이름
    int Area;                   // 같이 받은 범위 길이.
    ToggleButton Areaset;       //버튼 on,off 가능 토글 버튼으로 만듬
    MarkerOptions mymarker;     //마커추가
    CircleOptions setArea;      //범위 원형으로 추가
    int chosemarker;
    Vibrator vibe;

    //----------
    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;                    //구글맵 메인
    private Marker currentMarker = null;

    //
    //2018-06-04 현무 추가
    private static PolylineOptions polylineOptions = null;
    private static ArrayList<LatLng> arrayPoints = new ArrayList<LatLng>();
    private static Polyline line = null;//폴리라인 2018-06-10 현무 추가
    LatLng setting_LatLng = null;

    //2018-06-09 현무 추가
    List<Marker> previous_marker = null;
    private static Thread worker;         // 스레드 작동
    private static Thread AutopoliLine;   //오버 레이로 이동경로표시
    private static Thread dataparser;    //데이터 파싱 사용 x 예상

    //현무추가 2018-06-10 ~ 2018-06-11
    public BufferedWriter networkWriter;
    public Timer timer = null;
    public T_Task t_task;
    public TimeControl TimeControl;
    public Timer timer_Control = null;
    List<LatLng> endLatLng = new ArrayList<LatLng>(); // List 인터페이스로 PolyLineOption에 정의된 List타입의 정보를 받을곳

    // 2018-06-12 현무 추가 마커에 쓸 현재시간
    private static Date today = new Date();
    private static ArrayList<String> timeMarker = new ArrayList<String>();

    //현무추가 2018-05-29 ~
    private static final String TAG = "googlemap";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 4000;  // 4초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 3000; // 3초

    //현무추가 2018-06-01~ 2018-06-04
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


        final Button trackingbutton = (Button) findViewById(R.id.TrackingButton);
        final Button line_button = (Button) findViewById(R.id.LineButton);
        Button buttonPolice = (Button) findViewById(R.id.buttonPolice); //폴리스버튼
        tv = (TextView) findViewById(R.id.tv);

        // StrictMode는 개발자가 실수하는 것을 감지하고 해결할 수 있도록 돕는 일종의 개발 툴
        // - 메인 스레드에서 디스크 접근, 네트워크 접근 등 비효율적 작업을 하려는 것을 감지하여
        //   프로그램이 부드럽게 작동하도록 돕고 빠른 응답을 갖도록 함, 즉  Android Not Responding 방지에 도움
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //ArrayList 초기화와 버튼 클릭시 showPlaceInformation() 메소드를 호출
        previous_marker = new ArrayList<Marker>();

        polylineOptions = new PolylineOptions(); //위치정보 저장된다.


        //인근 경찰서 출력 버튼 (3000미터 이내 경찰서만 맵상에 출력한다. )
        buttonPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaceInformation(currentPosition);
            }
        });

        //아이정보 받아오기 Timer와 TimeTask사용
        trackingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //토글버튼 사용하면 메인UI가 정지된다. 스레드,핸들링해도 마찬가지로 인해 따로 토글과 같이구현
                    if (trackingbutton.getText().equals("위치 찾기 시작")) {
                        trackingbutton.setText("위치 찾는중");
                        Log.d(TAG, "수신중");
                        worker = new ThreadSocket();            // 스레드 작동 메인 ui에서 따로돌린다.
                        worker.start();

                        try{
                            Thread.sleep(1000);
                            for(int j=0;j<10000;j++){ }
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        //위치정보 를 10초 간격으로 설정하여 메소드 실행
                        t_task = new T_Task();
                        timer = new Timer();
                        timer.schedule(t_task, 0, 5000);


                        //위치정보 저장을위해. 데이터는 저장되어 polyline에 사용한다. 11초 간격으로 실행
                        TimeControl = new TimeControl();
                        timer_Control = new Timer();
                        timer_Control.schedule(TimeControl, 11000, 11000);

                    }
                    //토글중이 아닐때 timer 캔슬시킨다.
                    else if (trackingbutton.getText().equals("위치 찾는중")) {
                        trackingbutton.setText("위치 찾기 시작");
                        Log.d(TAG, "미수신중");

                        timer.cancel();
                        t_task.cancel();

                        timer_Control.cancel();
                        TimeControl.cancel();

                        try{
                            socket.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                }
            }
        });
        //병철추가 범위설정.
        Areaset = (ToggleButton) findViewById(R.id.SetArea);
        Areaset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i % 2 == 1) {
                    System.out.println("액티비티 전환\n");
                    popup = new Intent(MainActivity.this, Setting_Area_Activity.class);
                    startActivityForResult(popup, 1234);
                } else {
                    System.out.println("표시 삭제\n");
                    DelArea();
                }
            }
        });

        //이동경로 그리기 리스너 +
        //이동경로별 날자와 시간을 입력해준다.
        line_button.setOnClickListener(new View.OnClickListener() {
            @Override //HM 2018_06_10 오버레이로 이동경로 그리기
            public void onClick(View v) {
                try {
                    if (line_button.getText().equals("이동경로 그리기")) {
                        line_button.setText("이동경로 그리는중");
                        Log.d(TAG, "이동경로 그리기중");

                        //list형 인터페이스 인 endLatLng는 ArrayList를 포함하여 받을수있음.
                        // 위의 타이머에서 수집한 정보 넣어준다.
                        endLatLng = polylineOptions.getPoints();

                        if (endLatLng.size() >= 2) {

                            for (int i = 0; i < endLatLng.size(); i++) {
                                //마커를 찍어주어서 선을 연결한다. 요청에따라 없앨수있음.
                                //같은 인덱스는 같은 시간에 받은것임 오차차이는 데이터를 전달하고 받는 방식에 따라 +-10초정도 차이가 있다.
                                //FootPrint 출력 아이가 언제 이동했는지 10분단위로 마커를 찍어 시간을 출력시켜준다.
                                //i가 저장되는 간격은 10초이므로 1 == 10초
                                try {
                                    if (!((timeMarker.get(i = i + 1)) == null)) {
                                        mGoogleMap.addMarker(new MarkerOptions().position(endLatLng.get(i))
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.footprint))
                                                .title("지나간 날자,시간")
                                                .alpha(0.5f) //투명도 설정
                                                .snippet(timeMarker.get(i)));

                                    } else {
                                        break;
                                    }
                                } catch(IndexOutOfBoundsException e){
                                    Log.d(TAG,"인덱스:" +i);

                                }
                            }
                        }

                        polylineOptions.color(Color.BLUE);                 //파란색 설정
                        polylineOptions.width(5);//넓이5
                        polylineOptions.geodesic(true);
                        line = mGoogleMap.addPolyline(polylineOptions); //폴리라인을 추가해서 구글맵에 그린다.
                        line.setVisible(true);                           //보이게 설정
                        Log.d(TAG, "ThreadPoliLine 실행중");
                    } else if (line_button.getText().equals("이동경로 그리는중")) {
                        //토글중이 아닐때
                        line.setVisible(false);                         //안보이게 설정
                        //line.remove();                                 //라인 제거 가능.(폴리라인)
                        //polylineOptions = new PolylineOptions();      //객체로 덮어 정보제거 가능
                        mGoogleMap.clear();
                        line_button.setText("이동경로 그리기");
                        Log.d(TAG, "이동경로 그리지 않음");


                    }
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Log.d(TAG, "onCreate");
        mActivity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        //맵을 frgment로 얻어온다.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    } //oncreate 끝


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
        } else {

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

        Log.d(TAG, "stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
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
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

            @Override
            public boolean onMyLocationButtonClick() {

                Log.d(TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d(TAG, "onMapClick :");
            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {

                if (mMoveMapByUser == true && mRequestingLocationUpdates) {

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
        location.setLatitude(latitude);
        location.setLatitude(longitude);
        currentPosition = new LatLng(latitude, longitude);
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

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() == false) {

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();
        //worker.start();
    }

    @Override
    protected void onStop() {

        try{
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        if (mRequestingLocationUpdates) {
            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {


        if (mRequestingLocationUpdates == false) {

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
            } else {
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

            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견 위도-경도:" + latitude + " - " + longitude+
                    "\n Tag란에 아이의 Tag를 입력하여 주시고 위치 찾기 버튼을 눌러주세요", Toast.LENGTH_LONG).show();
            Log.d(TAG, "현재위도:" + latitude + "현재위도:" + longitude + "data : " + data);
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

        LatLng currentLatLng = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions(); //마커 객체생성
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        //구글맵의 디폴트 현재 위치는 애기 사진으로 표시
        //마커를 원하는 이미지로 변경하여 현재 위치 표시하도록 수정
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baby));

        currentMarker = mGoogleMap.addMarker(markerOptions);

        if (mMoveMapByAPI) {

            Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera " + latitude + " " + longitude);
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

        if (currentMarker != null) {
            currentMarker.remove();
        }

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

            if (mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (permsRequestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {
            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {

                if (mGoogleApiClient.isConnected() == false) {
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
        if (requestCode == 1234 || resultCode == 1001 ) {
            SettingArea();
            System.out.println("데이터를 가져옵니다.\n");
            try {
                Area = Integer.parseInt( data.getExtras().getString("area") );
            } catch (NullPointerException e) {    }
            try {
                String getname = data.getExtras().getString("name");
                name = getname;
            } catch (NullPointerException e) { }
            try{
                chosemarker = data.getExtras().getInt("marker");
            }catch(NullPointerException e) { }
            System.out.println("넓이는 " + Area + "이름은 " + name);
            SettingArea();
        }

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");
                        if (mGoogleApiClient.isConnected() == false) {
                            Log.d(TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }
                break;
        }
    }

    //병철추가  789~853
    public void DelArea() {
        System.out.println("DelArea 호출 \n");
        this.mGoogleMap.clear();
        //mymarker.visible(false);
        //setArea.visible(false);
        vibe.cancel();

    }

    public void checkArea() {
        Log.d(TAG, "checkArea()시작");
        try {
            double chk = getdistance(MKlati, latitude, MKlong, longitude);
            if((int)chk > Area) {
                Toast.makeText(this, "범위를 벗어남", Toast.LENGTH_LONG).show();
                vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(500);
                Log.d(TAG, "checkArea()에서 구한 범위 : "+ chk + "<" + Area);
            }
        }  catch (Exception e) { }
    }

    public void SettingArea() {
        System.out.println("SettingArea 호출 \n");
        MKlati = latitude;
        MKlong = longitude;
        LatLng position = new LatLng(latitude, longitude);


        //나의위치 마커
        mymarker = new MarkerOptions().position(position);   //마커위치
        mymarker.title(name);
        mymarker.snippet(Area + "m");
        switch(chosemarker) {
            case 1:
                mymarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.coffee));
                break;
            case 2:
                mymarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.school));
                break;
            case 3:
                mymarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pcroom));
                break;
            case 4:
                mymarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.friend));
                break;
        }
        // 반경 1KM원
        setArea = new CircleOptions().center(position) //원점
                .radius(Area)      //반지름 단위 : 1km = 1000m
                .strokeWidth(1f)  //선 두께 1f
                .fillColor(Color.parseColor("#80000000")); //배경색

        //마커추가
        this.mGoogleMap.addMarker(mymarker);

        //원추가
        this.mGoogleMap.addCircle(setArea);
        checkArea();
    }

    /*
    각의 단위 그리는 방법.
    디그리(degree) = 원 한바퀴를 360도로 표현하는 방법(일상에서 쓰는 법)
    라디안(radioan) = 반지름의 길이와 두개의 반지름이 이루는 호의 길이가 같은 경우가 1 라디안 이다.
    Math.PI = 파이(3.141592...)
    */
    public double getdistance(double lati1, double lati2, double long1, double long2) {

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
    private double deg2rad(double deg) {
        return (double) (deg * Math.PI / (double) 180);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad) {
        return (double) (rad * (double) 180 / Math.PI);
    }

    //HM 2018_06_08 스레드 및 데이터
    public class ThreadSocket extends Thread {

        public ThreadSocket() {
        }

        //스레드 실행구문
        public void run() {
            try {
                //소켓을 생성하고 입출력 스트립을 소켓에 연결한다.
                socket = new Socket(ip, port); //소켓생성
                if(socket==null)
                {
                    Log.d(TAG, "socket 생성 실패\n");
                    exit( 1);
                }
                networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out = new PrintWriter(networkWriter, true); //데이터를 전송시 stream 형태로 변환하여// 전송한다.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //데이터 수신시 stream을 받아들인다.
            } catch (IOException e) {
                e.printStackTrace();
            }

        } //
    }

    //HM 2018_06_10 데이터 처리 사용
    public class T_Task extends TimerTask {
        public T_Task() {
        }

        @Override
        public void run() {
            try {
                out.println(tv.getText()); //텍스트에 입력한 문자열을 서버에 송신한다. 데이터 방법1 계속보낼떄마다 10초마다 문자열을 보내줌 방법2 첫태그만 보낸다.
                Log.d(TAG, "서버에 보낸 Text:" + tv.getText());

                data = in.readLine(); // in으로 받은 데이타를 String 형태로 읽어 data 에 저장
                Log.d(TAG, "서버에 읽은 Data:" + data);

                String str = data;
                String[] array = str.split(","); //받은 data를 잘라서 넣는다. 서버에서 처리해줘야함 ex) baby_1
                Log.d(TAG, "Data 처리 위도:" + array[0] + "경도:" + array[1]);

                latitude = Double.parseDouble(array[0]);
                longitude = Double.parseDouble(array[1]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 2018-06-09 현무 추가 장소 출력 implements해서 사용중
    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    // 2018-06-09 현무 추가 장소 출력 implements해서 사용중
    @Override
    public void onPlacesStart() {

    }

    // 2018-06-09 현무 추가 장소 출력 implements해서 사용중
    @Override
    public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //구글맵 API참고 장소에대한 정의되어져 있음 구글맵에서 제공
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());

                    String markerSnippet = getCurrentAddress(latLng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(markerSnippet);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.police)); //경찰서만 제공할거기떄문에 경찰서 아이콘으로 대체
                    Marker item = mGoogleMap.addMarker(markerOptions);
                    previous_marker.add(item);

                }

                //중복 마커 제거

                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);

                previous_marker.clear();
                previous_marker.addAll(hashSet);

            }
        });
    }

    // 2018-06-09 현무 추가 장소 출력 implements해서 사용중
    @Override
    public void onPlacesFinished() {

    }

    // 2018-06-09 현무 추가 장소 출력
    public void showPlaceInformation(LatLng location) {
        //지도 클리어
        //mGoogleMap.clear();

        //지역정보 마커 클리어
        if (previous_marker != null)
            previous_marker.clear();

        new NRPlaces.Builder()
                .listener(MainActivity.this)
                .key("AIzaSyCWKWesIJPDvl_Wron4b9oveRiHbRCXDfY") //개발자 api place키
                .latlng(latitude, longitude)    //현재 위치
                .radius(3000)                       //3000 미터 내에서 검색
                .type(PlaceType.POLICE)             //경찰서
                .build()
                .execute();
    }

    //HM 2018_06_10 데이터 처리 사용
    public class TimeControl extends TimerTask {
        public TimeControl() {
        }

        @Override
        public void run() {
            try {

                if( (latitude != 0.0) && (longitude != 0.0) ) {
                    polylineOptions.add(new LatLng(latitude, longitude));
                    Log.d(TAG, "현재 폴리라인이 저장한 값 :" + polylineOptions.getPoints());
                    SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss");
                    Log.d(TAG, date.format(today));
                    long mnow = System.currentTimeMillis();
                    today = new Date(mnow);
                    timeMarker.add(date.format(today));
                    //병철 꼽사리
                }else{
                    Log.d(TAG, "서버에서 위도 경도를 받아오지 못합니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}

