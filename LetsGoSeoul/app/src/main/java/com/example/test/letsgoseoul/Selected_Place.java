package com.example.test.letsgoseoul;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Selected_Place extends FragmentActivity implements OnMapReadyCallback {
    private int getPlaceId; //test
   // private String getPlaceId; //id
    private GoogleMap mMap;
    private TextView placeName;
    private TextView textview;

    private double lat;
    private double lng;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected__place);

        Intent intent = new Intent(this.getIntent());
        //String getPlaceId = intent.getExtras().getString("SelectedPlace");   //넘어온 선택된 아이템 ID
        getPlaceId = intent.getExtras().getInt("SelectedPlace");   //넘어온 test용 ID

        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");
        //lng = intent.getDoubleExtra("lng", -1);
        name = intent.getStringExtra("name");


        //getPlaceId가 잘 넘어왔나 확인
        Toast.makeText(Selected_Place.this,"placeId "+getPlaceId, Toast.LENGTH_LONG).show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        placeName = (TextView)findViewById(R.id.placeName);     //선택된 장소의 이름 입력하는 곳
        placeName.setText(name);
      textview = (TextView)findViewById(R.id.placeText);     //상세정보 넣는 곳
       String text = "명동은 대한민국 서울특별시 중구에 있는 번화가이자, 지역 이름이다. 명동1가와 명동2가를 합친 면적은 0.91 ㎢이다. 명동1·2가, 충무로1·2가, 을지로1·2가 등을 포함하는 지역이다.";
        textview.setText(text);


    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ///받아온 getPlaceId이용해 좌표 넣어줄 위치
        //LatLng placePoint= new LatLng(37.560891, 126.985246);
        LatLng placePoint= new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(placePoint).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(placePoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

public void onRestaurantButtonClicked(View v){
    Intent intent = new Intent(getApplicationContext(),RestaurantList.class);
    startActivity(intent);
}
    public void onSightsButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(),RestaurantList.class);
        startActivity(intent);
    }
}
