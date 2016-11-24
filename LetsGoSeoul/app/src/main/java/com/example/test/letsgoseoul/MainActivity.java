package com.example.test.letsgoseoul;

import android.*;
import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.provider.Settings.Secure.isLocationProviderEnabled;
import static com.example.test.letsgoseoul.R.id.listView;
public class MainActivity extends AppCompatActivity {

    private ListView  mListView;
    //GPS 정보를 받아올 Listener
    private GPSListener gpsListener = new GPSListener();
    private LocationManager manager;

    //화면에 띄워줄 리스트뷰
    private static ArrayList<String> hotPlace;

    //나의 위치정보를 저장
    private static String myLat;
    private static String myLng;

    //장소 Top 10 리스트 받아옴
    private String url = "http://nodetest.iptime.org:3000/facebook/listlocation";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Lets Go Seoul");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable());

        mListView = (ListView) findViewById(listView);
        hotPlace = new ArrayList<String>();
        //서울중심
        setSeoul();
        checkDangerousPermissions();
    }

    public void onSeoulButtonClicked(View v) {    //서울중심
        setSeoul();
    }

    //사용자위치 중심
    public void onNearButtonClicked(View v) {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        showSettingsAlert();     //설정창
        startLocationService();  //gps

    }

    //Seoul 기준
    public void setSeoul() {
        hotPlace.clear();           //이전 리스트 clear
        //통신
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();

                            //결과 값 출력
                            JSONArray jarr = new JSONArray(response);   // JSONArray 생성

                            //index 1~10 까지 저장
                            String temp[] = new String[11];

                            for(int i=0; i < jarr.length(); i++){
                                JSONObject jObject = jarr.getJSONObject(i);  // JSONObject 추출
                                String name = jObject.getString("name");
                                int id = jObject.getInt("id");

                                Log.v("Location", id + " , " +  name);
                                temp[id] = id + " : " + name;
                            }

                            for(int i=1; i<=10; i++) {
                                hotPlace.add(temp[i]);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        startSort(mListView,hotPlace);
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    //NEAR가 중심인 경우
    public void setNear() {

        hotPlace.clear();
        hotPlace.add("니어");
        hotPlace.add("명동 ");
        hotPlace.add("명동");
        hotPlace.add("명동");
        startSort(mListView,hotPlace);

    }

    private String seletedName;
    private double seletedLat;
    private double seletedLng;


    //리스트뷰 sorting
   public void startSort(ListView lv,ArrayList hotPlace) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_item, R.id.tv_hotPlace, hotPlace);
        lv.setTextFilterEnabled(true);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                          final Intent intent = new Intent(MainActivity.this, Selected_Place.class);
                                          //position이 선택된 item의 순서
                                          StringRequest request = new StringRequest(Request.Method.POST, url,
                                                  new Response.Listener<String>() {
                                                      public void onResponse(String response) {
                                                          try {
                                                              //결과 값 출력
                                                              JSONArray jarr = new JSONArray(response);   // JSONArray 생성

                                                              for(int i=0; i < jarr.length(); i++){
                                                                  JSONObject jObject = jarr.getJSONObject(i);  // JSONObject 추출
                                                                  if(jObject.getInt("id") == position +1) {
                                                                      seletedLat = jObject.getDouble("lat");
                                                                      seletedLng = jObject.getDouble("lng");
                                                                      seletedName = jObject.getString("name");
                                                                      break;
                                                                  }
                                                              }
                                                              intent.putExtra("lat", seletedLat);
                                                              intent.putExtra("lng", seletedLng);
                                                              intent.putExtra("name", seletedName);

                                                              //Toast.makeText(MainActivity.this,lat + " , " + lng,Toast.LENGTH_LONG).show();

                                                              startActivity(intent);
                                                          } catch (Exception e) {
                                                              e.printStackTrace();
                                                          }
                                                      }
                                                  },
                                                  new Response.ErrorListener() {
                                                      public void onErrorResponse(VolleyError error) {
                                                          error.printStackTrace();
                                                      }
                                                  }
                                          ) {
                                              protected Map<String, String> getParams() {
                                                  Map<String, String> params = new HashMap<>();

                                                  return params;
                                              }
                                          };

                                          Volley.newRequestQueue(getApplicationContext()).add(request);
                                          //intent.putExtra("SelectedPlace", "");    여기다가 선택된 아이템의 db ID 텍스트로 뒤에 넣어주면 됌
//                                          try{
//                                              manager.removeUpdates(gpsListener);
//                                          } catch(SecurityException ex) {
//                                              ex.printStackTrace();}
                                          // Toast.makeText(MainActivity.this,"리스트클릭",Toast.LENGTH_LONG).show();
                                      }
                                  }
        );
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

        ContentResolver res = getContentResolver();

        boolean gpsEnabled = isLocationProviderEnabled(res, LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
           alertDialogBuilder
                    .setTitle("GPS 사용유무셋팅")
                    .setMessage("GPS 셋팅이 되지 않았습니다. \n 설정창으로 가시겠습니까?")
                    .setPositiveButton("Setting",
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
    }

    //gps 정보요청
    private void startLocationService() {

        //GPSListener gpsListener = new GPSListener();
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

        //위치 정보가 확인될 때 자동 호출되는 메소드
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude  = location.getLongitude();
            myLat = Double.toString(latitude);
            myLng = Double.toString(longitude);
            String msg = "Latitude : "+ myLat+ "\nLongitude:"+ myLng;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            try {
                manager.removeUpdates(gpsListener);
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

    //액션바 메뉴 띄워주기
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
            case R.id.near:
               // intent = new Intent(MainActivity.this, Selected_Place.class);
               // startActivity(intent);
                break;
            case R.id.home:
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}


