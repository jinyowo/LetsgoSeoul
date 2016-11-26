package com.example.test.letsgoseoul;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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


public class Selected_Restaurant extends MenuBar implements OnMapReadyCallback{
    private GoogleMap mMap;
    private static Bitmap bm;   //이미지

    private TextView placeNameForm;
    private TextView addressForm;
    private ImageView placeImg;

    private TextView detailForm;

    private double lat;   //위도
    private double lng;   //경도

    private TextView phoneNumberForm;
    private TextView homepageForm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_restaurant);
        Intent intent = new Intent(this.getIntent());
        int getPlaceId = intent.getExtras().getInt("Selected");   //넘어온 선택된 아이템 ID
        String getPlaceUrl = intent.getExtras().getString("SelectedUrl");   //넘어온 선택된 아이템 ID
        lat =intent.getExtras().getDouble("Lat");
        lng =intent.getExtras().getDouble("Lng");

        Toast.makeText(getBaseContext(),"tq lat "+lat+" lng"+lng,Toast.LENGTH_SHORT).show();
        placeNameForm = (TextView)findViewById(R.id.Name);
        addressForm = (TextView)findViewById(R.id.Address);

        detailForm = (TextView) findViewById(R.id.Detail);

        phoneNumberForm = (TextView)findViewById(R.id.PhoneNumber);
        homepageForm = (TextView)findViewById(R.id.Homepage);
        //homepageForm.setMovementMethod(LinkMovementMethod.getInstance());


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
                                        String name = jObject.getString("name");
                                        String address = jObject.getString("address");
                                        String detail = jObject.getString("overview");

                                        String tel = jObject.getString("tel");
                                        String homepage = jObject.getString("homepage");

                                        placeNameForm.setText(name);
                                        addressForm.setText(address);

                                        detailForm.setText(Html.fromHtml(detail));

                                        phoneNumberForm.setText(tel);
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
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            return params;
                        }
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
        //getPlaceId가 잘 넘어왔나 확인
        Toast.makeText(Selected_Restaurant.this,"restaurant "+getPlaceId, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ///받아온 getPlaceId이용해 좌표 넣어줄 위치

        LatLng placePoint= new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(placePoint).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(placePoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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
}



