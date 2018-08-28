package com.example.seyedamirhoseinhoseini.openstreetmap;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MapView extends AppCompatActivity implements Marker.OnMarkerClickListener {
org.osmdroid.views.MapView map;
IMapController mapController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),getApplicationContext().getSharedPreferences("map",MODE_PRIVATE));
        setContentView(R.layout.activity_map_view);
        map=findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController=map.getController();
        mapController.setZoom(9);
        mapController.setCenter(new GeoPoint(35.7,51.4));
        addMarker(35.7,51.4);
        initCurrentLocation();
    }
    public void addMarker(double alt,double lng)
    {
        map.setFocusableInTouchMode(true);
        Marker marker=new Marker(map);
        marker.setOnMarkerClickListener(this);
        marker.setPosition(new GeoPoint(alt,lng));
        marker.setTitle("title");
        marker.setSnippet("description");
        map.getOverlays().add(marker);
        map.invalidate();
    }
    public void initCurrentLocation()
    {
        GpsMyLocationProvider provider=new GpsMyLocationProvider(this);
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        MyLocationNewOverlay locationMarker=new MyLocationNewOverlay(provider,map);
        locationMarker.enableMyLocation();
        mapController.setZoom(13);
        mapController.setCenter(locationMarker.getMyLocation());
        map.getOverlays().add(locationMarker);

    }

    @Override
    public boolean onMarkerClick(Marker marker, org.osmdroid.views.MapView mapView) {
        if(!marker.isInfoWindowShown())
        marker.showInfoWindow();
        else {
            marker.getInfoWindow().close();
        }
        return false;
    }
}