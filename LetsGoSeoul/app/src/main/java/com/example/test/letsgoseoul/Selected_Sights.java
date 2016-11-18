package com.example.test.letsgoseoul;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import java.util.List;


public class Selected_Sights extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private double lat;   //위도
    private double lng;   //경도
    private TextView placeName;
    private TextView infomation;
    private TextView address;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_restaurant);
        Intent intent = new Intent(this.getIntent());
        String getPlaceId = intent.getExtras().getString("SelectedPlace");   //넘어온 선택된 아이템 ID

        //getPlaceId가 잘 넘어왔나 확인
        Toast.makeText(Selected_Sights.this,"placId "+getPlaceId, Toast.LENGTH_LONG).show();

        placeName = (TextView)findViewById(R.id.Name);
        placeName.setText("NAME : "+ "전주비빔밥");
        infomation= (TextView)findViewById(R.id.PhoneNumber);
        infomation.setText("INFOMATION : "+"02-231-2654");
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
}


