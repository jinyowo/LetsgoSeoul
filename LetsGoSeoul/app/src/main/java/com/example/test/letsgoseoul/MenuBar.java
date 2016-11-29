package com.example.test.letsgoseoul;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//메뉴바
public class MenuBar extends AppCompatActivity {

    private GPSListener gpsListener = new GPSListener();
    private LocationManager manager;

    //나의 위치정보를 저장
    private static double myLat;
    private static double myLng;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //gps 허가
        checkDangerousPermissions();
    }

    //액션바 띄워주기
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //액션바 메뉴 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        Intent intent;
        switch (curId) {
            case R.id.near:       //내 위치 중심으로 맛집 및 명소 검색
                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);       //gps 확인
                if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showSettingsAlert();    //설정창
                }
                startLocationService();  //gps
                break;

            case R.id.toHome:       //MAIN 화면으로 이동
                Context context = getApplicationContext();
                String temp = getClass().getName();         //현재 화면의 클래스 이름름

               //현재 위치가 HOME 화면이면 동작하지 않는다
                if(!temp.equals("com.example.test.letsgoseoul.MainActivity")) {
                    intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);       //안드로이드 스택 clear
                    startActivity(intent); // 다음 화면으로 넘어간다
                }
                break;

            case android.R.id.home:         //뒤로가기
                finish();                    //창 닫기
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public void setNear() {
         Intent intent = new Intent(getApplicationContext(), Selected_Place.class);
        intent.putExtra("lat", myLat);
        intent.putExtra("lng", myLng);
        intent.putExtra("name", "현재 위치");
        intent.putExtra("near", "yes" );
        //Toast.makeText(MainActivity.this,lat + " , " + lng,Toast.LENGTH_LONG).show();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);           //안드로이드 코드 clear
        startActivity(intent);

    }

    //gps 허가
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //    Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //gps 설정되어있지 않을 때 설정창 이동
    public void showSettingsAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("GPS 사용유무셋팅")
                .setMessage("GPS 셋팅이 되지 않았습니다. \n 설정창으로 가시겠습니까?")
                .setPositiveButton("Setting".toString(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }


    //gps 정보요청
    private void startLocationService() {

        long minTime = 1000;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);
            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

                //   Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

    }


    private class GPSListener implements LocationListener {

        //위치 정보가 업데이트 될 때
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude  = location.getLongitude();
            myLat =latitude;
            myLng = longitude;
             //String msg = "Latitude : "+ myLat+ "\nLongitude:"+ myLng;
            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            try {
                manager.removeUpdates(gpsListener);         //gps 리스너 업데이트 종료
            } catch(SecurityException ex) {
                ex.printStackTrace();
            }
            setNear();
        }
        public void onProviderDisabled(String provider) {
        }


        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }
}


