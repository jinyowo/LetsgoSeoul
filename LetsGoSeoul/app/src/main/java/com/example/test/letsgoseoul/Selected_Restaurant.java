package com.example.test.letsgoseoul;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//선택된 RESTARURANT OR SIGHT의 정보를 보여주는 클래스
public class Selected_Restaurant extends MenuBar implements OnMapReadyCallback{
    private GoogleMap mMap;
    private static Bitmap bm;   //이미지

    private TextView placeNameForm;
    private TextView addressForm;
    private ImageView placeImg;

    private TextView detailForm;

    private double lat;   //위도
    private double lng;   //경도
    private String placename;

    private TextView phoneNumberForm;
    private TextView homepageForm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_restaurant);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lets Go Seoul");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(this.getIntent());

        int getPlaceId = intent.getExtras().getInt("Selected");   //넘어온 선택된 아이템 ID
        String getPlaceUrl = intent.getExtras().getString("SelectedUrl");   //넘어온 선택된 아이템 ID
        lat =intent.getExtras().getDouble("Lat");
        lng =intent.getExtras().getDouble("Lng");
        placename = intent.getExtras().getString("name");

        //Toast.makeText(getBaseContext(),"tq lat "+lat+" lng"+lng,Toast.LENGTH_SHORT).show();
        placeNameForm = (TextView)findViewById(R.id.Name);
        addressForm = (TextView)findViewById(R.id.Address);

        detailForm = (TextView) findViewById(R.id.Detail);

        phoneNumberForm = (TextView)findViewById(R.id.PhoneNumber);
        homepageForm = (TextView)findViewById(R.id.Homepage);

        final String url = "http://nodetest.iptime.org:3000/tourapi/detail?contentid="+getPlaceId;

        Thread mTread = new Thread() {
            @Override
            public void run() {
                try {
                    StringRequest request = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                public void onResponse(String response) {
                                    try {
                                        //결과 값 출력
                                        JSONObject jObject = new JSONObject(response);
                                        String address = jObject.getString("address");
                                        String detail = jObject.getString("overview");

                                        String tel = jObject.getString("tel");
                                        String homepage = jObject.getString("homepage");

                                        placeNameForm.setText(placename);
                                        addressForm.setText(address);

                                        detailForm.setText(Html.fromHtml(detail));

                                        phoneNumberForm.setText(tel);

                                        // 홈페이지 링크를 클릭하면 해당 홈페이지로 이동하도록 html <a> 태그 적용
                                        homepageForm.setText(Html.fromHtml(homepage));

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
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(request);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mTread.start();
        try{
            mTread.join();
            getBitmap(getPlaceUrl);
        } catch (InterruptedException e) {

        } finally
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ///받아온 getPlaceId이용해 좌표 넣어줄 위치
        LatLng placePoint= new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(placePoint).title(placename));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(placePoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }


    public void getBitmap(String imgUrl) {
        final String urlImg =imgUrl;
        placeImg = (ImageView)findViewById(R.id.imageView);
        Thread mTread = new Thread() {
            @Override
            public void run() {
                try {
                    URL imgUrlTest = new URL(urlImg);
                    HttpURLConnection connection = (HttpURLConnection) imgUrlTest.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    bm = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mTread.start();
        try {
            mTread.join();
            placeImg.setImageBitmap(bm);
        } catch (InterruptedException e) {
        }
    }

    // 전화번호를 누르면 자동으로 전화가 걸어짐
    public void onCallNumberClicked(View view) {
        Log.v("phone number", phoneNumberForm.getText().toString());
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumberForm.getText().toString()));
        startActivity(intent);
    }
}



