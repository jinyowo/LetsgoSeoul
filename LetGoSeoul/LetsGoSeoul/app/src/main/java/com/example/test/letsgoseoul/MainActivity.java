package com.example.test.letsgoseoul;

import android.*;
import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.widget.SearchView;
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

import java.util.HashMap;
import java.util.Map;

import static android.provider.Settings.Secure.isLocationProviderEnabled;
import static com.example.test.letsgoseoul.R.id.listView;

public class MainActivity extends AppCompatActivity {
    private ListView  mListView;
    private Button btnSeoul;
    private Button btnNear;
    GPSListener gpsListener = new GPSListener();
    final String[] hotPlace = {};
    String url = "http://nodetest.iptime.org:3000/facebook/listlocation";
   // private double lat;
   // private double lon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(listView);
        btnSeoul = (Button) findViewById(R.id.btn_seoul);
        btnNear = (Button) findViewById(R.id.btn_near);

        initList();
        //startSort(mListView,hotPlace);

        checkDangerousPermissions();
    }
    public void initList() {
        //통신
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_main);
        final StringBuffer sb = new StringBuffer();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();

                            //결과 값 출력
                            JSONArray jarr = new JSONArray(response);   // JSONArray 생성

                            for(int i=0; i < jarr.length(); i++){
                                JSONObject jObject = jarr.getJSONObject(i);  // JSONObject 추출
                                String name = jObject.getString("name");
                                int checkins = jObject.getInt("checkins");
                                int id = jObject.getInt("id");

                                Log.v("Location", id + " , " +  name);
                                hotPlace[id-1] = id + " : " + name;

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

    public void onSeoulButtonClicked(View v) {    //서울중심
        //hotPlace[0] = "test";

        initList();

    }

    public void onNearButtonClicked(View v) {    //사용자위치 중심
        hotPlace[0] = "near";
        showSettingsAlert();
        startLocationService();
        //Double lat = gpsListener.getLat();
       // Double lon = gpsListener.getLon();
        //String msg = "Latitude : "+ lat + "\nLongitude:"+ lon;
       //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        //String[] hotPlace = {"1. 현선   (Myeongdong)"};
        startSort(mListView,hotPlace);
    }

   public void startSort(ListView lv,String[] hotPlace) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_item, R.id.tv_hotPlace, hotPlace);
        lv.setTextFilterEnabled(true);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                           Object vo = parent.getAdapter().getItem(position);
                                          Intent intent = new Intent(MainActivity.this, Selected_Place.class);
                                          intent.putExtra("hotPlace", parent.getItemIdAtPosition(position)); //선택된 것의 id 추가
                                          startActivity(intent);
                                          // Toast.makeText(MainActivity.this,"리스트클릭",Toast.LENGTH_LONG).show();
                                      }
                                  }
        );
    }

    //GPS Permission
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
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 위치 정보 확인을 위해 정의한 메소드
     */

    public void showSettingsAlert() {

        ContentResolver res = getContentResolver();

        boolean gpsEnabled = isLocationProviderEnabled(res, LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle("GPS 사용유무셋팅")
                    .setMessage("GPS 셋팅이 되지 않았습니다. \n 설정창으로 가시겠습니까?")
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("Cancle",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
        }
    }
    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 리스너 생성
        //GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
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

                Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

     //   Toast.makeText(getApplicationContext(), "위치 확인이 시작되었습니다. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
    }


    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        double latitude;
        double longitude;
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.i("GPSListener", msg);
           Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        public double getLat()
        {return latitude;}
        public double getLon()
        {return longitude;}

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}


    /*  사용자가 찾을 장소 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Hot Place");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색버튼 클릭시
                Intent intent = new Intent(getApplicationContext(),Selected_Place.class);
                intent.putExtra("hotPlace",query);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "[검색버튼클릭] 검색어 = "+query, Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //검색어 입력중
                //Toast.makeText(MainActivity.this, "입력하고있는 단어 = "+newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_search:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/


