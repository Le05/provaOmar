package com.example.provaomar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle cityrespost = getIntent().getExtras();

        String lat="";
        String lng="";

        if(cityrespost != null){
            lat = (String) cityrespost.get("lat");
            lng = (String) cityrespost.get("lng");
        }

        double lat1 =Double.parseDouble(lat);
        double lng1 =Double.parseDouble(lng);
        // Add a marker in Sydney and move the camera
        LatLng cidade = new LatLng(lat1, lng1);
        mMap.addMarker(new MarkerOptions().position(cidade).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cidade));
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }
    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];
        double velocidade = Math.sqrt(x*x+y*y+z*z);
        if(velocidade >= 30){
            System.out.println(velocidade);
            startActivity(new Intent(MapsActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}