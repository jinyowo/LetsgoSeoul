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


import java.util.ArrayList;

import static android.provider.Settings.Secure.isLocationProviderEnabled;
import static com.example.test.letsgoseoul.R.id.listView;
public class MainActivity extends AppCompatActivity {
    private ListView  mListView;
    private static ArrayList<String> hotPlace;
    private GPSListener gpsListener = new GPSListener();
    private LocationManager manager;
    private static String lat;
    private static String lon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(listView);
        hotPlace = new ArrayList<String>();
        //서울중심
        setSeoul();
        checkDangerousPermissions();
    }

public void onSeoulButtonClicked(View v) {    //서울중심
    setSeoul();
};

    public void onNearButtonClicked(View v) {    //사용자위치 중심
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        showSettingsAlert();     //설정창
        startLocationService();  //gps

        }


    public void setSeoul() {    //Seoul이 중심인 경우
        hotPlace.clear();
        hotPlace.add("명동");
        hotPlace.add("명동");
        hotPlace.add("명동");
        hotPlace.add("명동");
        startSort(mListView,hotPlace);
    }

    public void setNear() {     //NEAR가 중심인 경우

        hotPlace.clear();
        hotPlace.add("니어");
        hotPlace.add("명동 ");
        hotPlace.add("명동");
        hotPlace.add("명동");
        startSort(mListView,hotPlace);

    }
   public void startSort(ListView lv,ArrayList hotPlace) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_item, R.id.tv_hotPlace, hotPlace);
        lv.setTextFilterEnabled(true);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                           Object vo = parent.getAdapter().getItem(position);
                                          Intent intent = new Intent(MainActivity.this, Selected_Place.class);
                                          //position이 선택된 item의 순서
                                          intent.putExtra("SelectedPlace", position);   //test용
                                          //intent.putExtra("SelectedPlace", "");    여기다가 선택된 아이템의 db ID 텍스트로 뒤에 넣어주면 됌
//                                          try{
//                                              manager.removeUpdates(gpsListener);
//                                          } catch(SecurityException ex) {
//                                              ex.printStackTrace();}
                                          startActivity(intent);
                                          // Toast.makeText(MainActivity.this,"리스트클릭",Toast.LENGTH_LONG).show();
                                      }
                                  }
        );
    }

    private void checkDangerousPermissions() {     //gps 허가
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

    /**
     * 위치 정보 확인을 위해 정의한 메소드
     */

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
        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude  = location.getLongitude();
            lat = Double.toString(latitude);
            lon = Double.toString(longitude);
            String msg = "Latitude : "+ lat+ "\nLongitude:"+ lon;
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


