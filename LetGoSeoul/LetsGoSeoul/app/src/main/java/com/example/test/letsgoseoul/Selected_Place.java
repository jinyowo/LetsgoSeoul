package com.example.test.letsgoseoul;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Selected_Place extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int position;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected__place);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        lat = intent.getDoubleExtra("lat", -1);
        lng = intent.getDoubleExtra("lng", -1);

        Toast.makeText(getApplicationContext(), position + " = (" + lat + " , " + lng + ")", Toast.LENGTH_LONG).show();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        ///좌표 넣어줄 위치
        //LatLng placePoint= new LatLng(37.560891, 126.985246);
        LatLng placePoint = new LatLng(lat, lng);
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
