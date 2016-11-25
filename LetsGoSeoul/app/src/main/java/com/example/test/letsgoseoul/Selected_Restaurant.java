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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class Selected_Restaurant extends MenuBar implements OnMapReadyCallback{
    private GoogleMap mMap;
    private TextView placeName;
    private TextView phoneNumber;
    private TextView address;
    private ImageView placeImg;
    private static Bitmap bm;   //이미지
    private double lat;   //위도
    private double lng;   //경도

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_restaurant);
        Intent intent = new Intent(this.getIntent());
        int getPlaceId = intent.getExtras().getInt("Selected");   //넘어온 선택된 아이템 ID
        String getPlaceUrl = intent.getExtras().getString("SelectedUrl");   //넘어온 선택된 아이템 ID


        //getPlaceId가 잘 넘어왔나 확인
        Toast.makeText(Selected_Restaurant.this,"restaurant "+getPlaceId, Toast.LENGTH_LONG).show();
        getBitmap(getPlaceUrl);
        placeName = (TextView)findViewById(R.id.Name);
        placeName.setText("NAME : "+ "전주비빔밥");
        phoneNumber = (TextView)findViewById(R.id.PhoneNumber);
        phoneNumber.setText("PHONE NUMBER : "+"02-231-2654");
        address = (TextView)findViewById(R.id.Address);
        address.setText("ADDRESS : "+"서울 서울시 중구 명동 1254-2번지");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ///받아온 getPlaceId이용해 좌표 넣어줄 위치
        LatLng placePoint= new LatLng(37.560891, 126.985246);
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



