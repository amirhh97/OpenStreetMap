package com.example.seyedamirhoseinhoseini.openstreetmap;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.views.MapView;

public class CustomizedCluster extends RadiusMarkerClusterer {
   public CustomizedCluster(Context ctx) {
      super(ctx);
   }
   int i;
   ClickListener mClickListener;


   @Override
   public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView) {
      boolean touched = hitTest(event, mapView, this);
      if (touched && mapView.getZoomLevel() < 17) {
         if (event.getAction() == MotionEvent.ACTION_DOWN)
            return mClickListener.OnCLusterCLickListener(this.clusterer(mapView).get(i).getPosition());
      }
      return super.onSingleTapConfirmed(event, mapView);
   }


   private boolean hitTest(final MotionEvent event, final org.osmdroid.views.MapView mapView, RadiusMarkerClusterer cluster) {
      Point mPositionPixels = new Point();
      for (i=0; i < cluster.clusterer(mapView).size(); i++) {
         StaticCluster c = cluster.clusterer(mapView).get(i);
         mapView.getProjection().toPixels(c.getPosition(), mPositionPixels);
         final Rect screenRect = mapView.getIntrinsicScreenRect(null);
         int x = -mPositionPixels.x + screenRect.left + (int) event.getX();
         int y = -mPositionPixels.y + screenRect.top + (int) event.getY();
         if(cluster.getBounds().contains(x,y))
         return true;
      }
      return false;
   }

   public void setOnClickListener(ClickListener listener) {
      this.mClickListener = listener;
   }

   interface ClickListener {
      boolean OnCLusterCLickListener(IGeoPoint point);

   }


}
