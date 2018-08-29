package com.example.seyedamirhoseinhoseini.openstreetmap;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

/**
 * Created by SeyedAmirhoseinHoseini on 8/28/18.
 */

public class MyInfoWindow extends MarkerInfoWindow {

    /**
     * @param layoutResId layout that must contain these ids: bubble_title,bubble_description,
     * bubble_subdescription, bubble_image
     * @param mapView
     */
    MapView mapView;
    TextView title;
    TextView description;
    ImageView img;
    Marker marker;

    public MyInfoWindow(int layoutResId, MapView mapView, final Marker marker) {
        super(layoutResId, mapView);
        this.mapView = mapView;
        this.marker = marker;
        title = mView.findViewById(R.id.bubble_title);
        description = mView.findViewById(R.id.bubble_description);
        img = mView.findViewById(R.id.bubble_image);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    Log.i("msg", "Click");
                marker.getInfoWindow().close();
                return true;
            }
        });


    }

    @Override
    public Marker getMarkerReference() {
        return super.getMarkerReference();
    }

    @Override
    public void onOpen(Object item) {
        title.setText(marker.getTitle());
        description.setText(marker.getSnippet());
        // img.setImageDrawable(marker.getImage());
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
