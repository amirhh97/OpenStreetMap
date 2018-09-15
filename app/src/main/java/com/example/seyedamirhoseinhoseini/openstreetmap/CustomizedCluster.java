package com.example.seyedamirhoseinhoseini.openstreetmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.PointL;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class CustomizedCluster extends RadiusMarkerClusterer {

   public CustomizedCluster(Context ctx) {
      super(ctx);
   }

   ClickListener mClickListener;

   @Override
   public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView) {
      boolean touched = hitTest(event, mapView, this);
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
         if (touched) {
            return mClickListener.OnCLusterCLickListener(mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY()));
         }
         return super.onSingleTapConfirmed(event, mapView);
      }
      return super.onSingleTapConfirmed(event, mapView);
   }

   private boolean hitTest(final MotionEvent event, final org.osmdroid.views.MapView mapView, RadiusMarkerClusterer cluster) {
      Point mPositionPixels = new Point();
      StaticCluster c = cluster.clusterer(mapView).get(0);
      mapView.getProjection().toPixels(c.getPosition(), mPositionPixels);
      final Rect screenRect = mapView.getIntrinsicScreenRect(null);
      int x = -mPositionPixels.x + screenRect.left + (int) event.getX();
      int y = -mPositionPixels.y + screenRect.top + (int) event.getY();
      return cluster != null && cluster.getBounds().contains(x, y);
   }

   public void setOnClickListener(ClickListener listner) {
      this.mClickListener = listner;
   }

   interface ClickListener {
      boolean OnCLusterCLickListener(IGeoPoint point);
   }
}
