package com.example.seyedamirhoseinhoseini.openstreetmap;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class CustomizedCluster extends RadiusMarkerClusterer {

   public CustomizedCluster(Context ctx) {
      super(ctx);
   }

   @Override
   public boolean onTouchEvent(MotionEvent event, MapView mapView) {

      Log.d("Click", "Ok");
      return true;
   }
}
