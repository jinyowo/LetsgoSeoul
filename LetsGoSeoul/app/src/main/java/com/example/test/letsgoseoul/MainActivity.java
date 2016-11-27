package com.example.test.letsgoseoul;


import android.content.Intent;
import android.os.Bundle;

import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.test.letsgoseoul.R.id.listView;
public class MainActivity extends MenuBar {

    private ListView  mListView;

    //화면에 띄워줄 리스트뷰
    private static ArrayList<String> hotPlace;

    //장소 Top 10 리스트 받아옴
    private String url = "http://nodetest.iptime.org:3000/facebook/listlocation";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  Lets Go Seoul");
        actionBar.setDisplayHomeAsUpEnabled(false);
        mListView = (ListView) findViewById(listView);
        hotPlace = new ArrayList<String>();
        //서울중심
        setSeoul();
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
                                int id = i;

                                Log.v("Location", id + " , " +  name);
                                temp[id] = id + " :  " + name;
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
;
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

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
                                                              JSONObject jObject = jarr.getJSONObject(position);
                                                              seletedLat = jObject.getDouble("lat");
                                                              seletedLng = jObject.getDouble("lng");
                                                              seletedName = jObject.getString("name");

//                                                              for(int i=0; i < jarr.length(); i++){
//                                                                  JSONObject jObject = jarr.getJSONObject(i);  // JSONObject 추출
//                                                                  if(jObject.getInt("id") == position +1) {
//                                                                      seletedLat = jObject.getDouble("lat");
//                                                                      seletedLng = jObject.getDouble("lng");
//                                                                      seletedName = jObject.getString("name");
//                                                                      break;
//                                                                  }
//                                                              }
                                                              intent.putExtra("lat", seletedLat);
                                                              intent.putExtra("lng", seletedLng);
                                                              intent.putExtra("name", seletedName);
                                                              intent.putExtra("near", "no" );

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
                                      }
                                  }
        );
    }
}


