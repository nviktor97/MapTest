package com.example.maptest;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maptest.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Button btnBack;

    float lgt = 0;
    float lat = 0;

    String location = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //A koordináták lekérdezése az intentből
        Bundle extras = getIntent().getExtras();
        lgt = extras.getFloat("longitude");
        lat = extras.getFloat("latitude");
        location = extras.getString("location");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnBack = (Button) findViewById(R.id.button2);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                startActivity(intent);
                finish();

            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //Jelző lerakása a térkép megjelenítésekor és kezdőkoordináták megadása a kiválasztott város alapján
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng destination = new LatLng(lat, lgt);
        mMap.addMarker(new MarkerOptions().position(destination).title("Marker in " + location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 10));
    }
}