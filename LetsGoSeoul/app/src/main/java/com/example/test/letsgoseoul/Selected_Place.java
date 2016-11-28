package com.example.test.letsgoseoul;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Selected_Place extends MenuBar implements OnMapReadyCallback{
    private int getPlaceId; //test
   // private String getPlaceId; //id
    private GoogleMap mMap;
    private TextView placeName;
    private TextView textview;

    private double lat;
    private double lng;
    private String name;
    private String near;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected__place);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lets Go Seoul");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = new Intent(this.getIntent());
        //String getPlaceId = intent.getExtras().getString("SelectedPlace");   //넘어온 선택된 아이템 ID
        // getPlaceId = intent.getExtras().getInt("SelectedPlace");   //넘어온 test용 ID

        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");
        name = intent.getStringExtra("name");
        near = intent.getStringExtra("near");

        //getPlaceId가 잘 넘어왔나 확인
        //Toast.makeText(Selected_Place.this,"My location"+lat+" "+ lng, Toast.LENGTH_LONG).show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        placeName = (TextView)findViewById(R.id.placeName);     //선택된 장소의 이름 입력하는 곳
        placeName.setText(name);
        textview = (TextView)findViewById(R.id.placeText);     //상세정보 넣는 곳
        String text;


        if(near.equals("yes"))
        {
            text = getAddress(lat,lng);
        }
        else {
            text = getAddress(lat,lng);;
        }
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
        mMap.addMarker(new MarkerOptions().position(placePoint).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(placePoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

public void onRestaurantButtonClicked(View v){
    Intent intent = new Intent(getApplicationContext(),RestaurantList.class);
    intent.putExtra("lat", lat);
    intent.putExtra("lng", lng);
    intent.putExtra("buttonOption", "restaurant");
    startActivity(intent);
}
    public void onSightsButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(),RestaurantList.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("buttonOption", "sights");
        startActivity(intent);
    }

    private String getAddress(double lat, double lng) {
        String currentLocationAddress=null;
        Geocoder geocoder = new Geocoder(this, Locale.KOREAN);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소  ,  대한민국은 뗌
                    currentLocationAddress = address.get(0).getAddressLine(0).toString().substring(5);
                }
            }

        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "주소취득 실패", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return currentLocationAddress;
    }

}
