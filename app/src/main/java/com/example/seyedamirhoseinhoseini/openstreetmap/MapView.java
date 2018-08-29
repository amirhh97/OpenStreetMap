package com.example.seyedamirhoseinhoseini.openstreetmap;

import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.api.IMapController;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MapView extends AppCompatActivity implements Marker.OnMarkerClickListener, MapListener {
    org.osmdroid.views.MapView map;
    IMapController mapController;
    ArrayList<Marker> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), getApplicationContext().getSharedPreferences("map", MODE_PRIVATE));
        setContentView(R.layout.activity_map_view);
        map = findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(9);
        mapController.setCenter(new GeoPoint(35.7, 51.4));
        items=new ArrayList<>();
        for (double i = 0; i < 0.001; i += 0.0001)
            addMarker(35.7, 51.4 + i);
        initRotationGesture();
        initCurrentLocation();
        map.setMapListener(this);
        addCluster(items);
       // addFolder(items);

    }

    public void addMarker(double alt, double lng) {
        map.setFocusableInTouchMode(true);
        Marker marker = new Marker(map);
        marker.setOnMarkerClickListener(this);
        marker.setPosition(new GeoPoint(alt, lng));
        marker.setTitle("title");
        marker.setSnippet("description");
        //marker.setIcon(ContextCompat.getDrawable(this,R.mipmap.ic_arrow_foreground));
        marker.setInfoWindow(new MyInfoWindow(R.layout.info_window, map, marker));
        items.add(marker);

    }

    public void initCurrentLocation() {
        GpsMyLocationProvider provider = new GpsMyLocationProvider(this);
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        MyLocationNewOverlay locationMarker = new MyLocationNewOverlay(provider, map);
        locationMarker.enableMyLocation();
        mapController.setZoom(13);
        mapController.setCenter(locationMarker.getMyLocation());
        map.getOverlays().add(locationMarker);
    }

    public void initRotationGesture() {
        RotationGestureOverlay rotaiotn = new RotationGestureOverlay(map);
        rotaiotn.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(rotaiotn);
    }

    @Override
    public boolean onMarkerClick(Marker marker, org.osmdroid.views.MapView mapView) {
        if (!marker.isInfoWindowShown()) {
            //mapController.animateTo(marker.getPosition());
            InfoWindow.closeAllInfoWindowsOn(mapView);
            marker.showInfoWindow();

        }
        return true;
    }

    public void addCluster(ArrayList<Marker> markers) {
        RadiusMarkerClusterer cluster = new RadiusMarkerClusterer(this);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_circle);
        cluster.setIcon(bitmapDrawable.getBitmap());
        map.getOverlays().add(cluster);
        for (Marker marker : markers) {
            cluster.add(marker);
        }
    }
    public void addFolder(ArrayList<Marker> markers)
    {
        FolderOverlay folder=new FolderOverlay(this);
        for (Marker marker : markers) {
            folder.add(marker);
        }
        map.getOverlays().add(folder);

    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        InfoWindow.closeAllInfoWindowsOn(map);
        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }

}
