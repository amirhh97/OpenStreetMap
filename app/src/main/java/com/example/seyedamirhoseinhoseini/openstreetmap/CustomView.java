package com.example.seyedamirhoseinhoseini.openstreetmap;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by SeyedAmirhoseinHoseini on 8/29/18.
 */

public class CustomView extends LinearLayout {


   public CustomView(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   @Override
   public boolean onInterceptTouchEvent(MotionEvent ev) {
      onTouchEvent(ev);
      Log.d("touch", "PARENT.onInterceptTouchEvent");
      return true;
   }
}
