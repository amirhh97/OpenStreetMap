package com.example.seyedamirhoseinhoseini.openstreetmap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.api.IMapController;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MapView extends AppCompatActivity implements Marker.OnMarkerClickListener, MapListener, MapEventsReceiver {
   org.osmdroid.views.MapView map;
   IMapController mapController;
   ArrayList<Marker> markers;
   boolean permissions = false;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      SharedPreferences pref=getApplicationContext().getSharedPreferences("map", MODE_PRIVATE);
      Configuration.getInstance().load(getApplicationContext(),pref);
      setContentView(R.layout.activity_map_view);
      map = findViewById(R.id.map);
      map.getController().setZoom(15);
      map.getController().animateTo(new GeoPoint(pref.getFloat("long",51),pref.getFloat("alt",37)));
      checkPermission();
      if (!checkGpsState())
         showGpsAlertDialog();
      initCurrentLocation();
      map.setBuiltInZoomControls(true);
      map.setMultiTouchControls(true);
      mapController = map.getController();
      map.addMapListener(this);
      initRotationGesture();
      markers = new ArrayList<>();
      for (double i = 0; i < 0.001; i += 0.0001) {
         markers.add(addMarkerForCluster(35.72, 51.41 + i));
      }
      addCluster(markers);
      MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
      map.getOverlays().add(mapEventsOverlay);

   }

   public boolean checkGpsState() {
      LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
      return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
   }

   public void showGpsAlertDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage("برنامه نیاز به Location شما دارد آیا مایل به فعال کردن آن هستید؟")
              .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                 }
              }).setPositiveButton("بله", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
         }
      }).create().show();

   }

   public Marker addMarkerForCluster(double alt, double lng) {
      map.setFocusableInTouchMode(true);
      Marker marker = new Marker(map);
      marker.setOnMarkerClickListener(this);
      marker.setPosition(new GeoPoint(alt, lng));
      marker.setTitle("title");
      marker.setSnippet("description");
      //marker.setIcon(ContextCompat.getDrawable(this,R.mipmap.ic_arrow_foreground));
      marker.setInfoWindow(new MyInfoWindow(R.layout.info_window, map, marker));
      return marker;

   }

   public void addMarkerToOverlay(Marker marker) {
      map.getOverlays().add(marker);
      map.invalidate();
   }
   public void initCurrentLocation() {
      if (permissions) {
         GpsMyLocationProvider provider = new GpsMyLocationProvider(this);
         provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
         provider.addLocationSource(LocationManager.GPS_PROVIDER);
         provider.getLastKnownLocation();
         final MyLocationNewOverlay locationMarker = new MyLocationNewOverlay(provider, map);
         locationMarker.enableMyLocation();
         locationMarker.enableFollowLocation();
         locationMarker.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
               mapController.setZoom(15);
               mapController.setCenter(locationMarker.getMyLocation());
               SharedPreferences pref= getApplicationContext().getSharedPreferences("map",MODE_PRIVATE);
               pref.edit().putFloat("long", (float) locationMarker.getMyLocation().getLongitude()).apply();
               pref.edit().putFloat("alt", (float) locationMarker.getMyLocation().getAltitude()).apply();
            }
         });
         map.getOverlays().add(locationMarker);
      }
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
      cluster.setMaxClusteringZoomLevel(16);
      BitmapDrawable bitmapDrawable = ((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.marker_cluster));
      cluster.setIcon(bitmapDrawable.getBitmap());
      map.getOverlays().add(cluster);
      for (Marker marker : markers) {
         cluster.add(marker);
      }
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


   public void checkPermission() {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
              &&
              ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
         if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
      } else {
         permissions = true;
      }
   }

   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (permissions.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
         this.permissions = true;
         initCurrentLocation();
      }
   }

   @Override
   public boolean singleTapConfirmedHelper(GeoPoint p) {
      return false;
   }

   @Override
   public boolean longPressHelper(GeoPoint p) {
      return false;
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Log.d("resultCode", resultCode + "");
      initCurrentLocation();
   }
}
