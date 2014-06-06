package com.ndsec.wifisec;

import android.content.Context;    
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity; 
import android.widget.ImageView; 
import android.widget.LinearLayout;

class TabView extends LinearLayout {  

    ImageView imageView ;  
public TabView(Context c, int drawable, int drawableselec) {  
    super(c);  
    imageView = new ImageView(c);  
    StateListDrawable listDrawable = new StateListDrawable();  
    listDrawable.addState(SELECTED_STATE_SET, this.getResources()  
            .getDrawable(drawableselec));  
    listDrawable.addState(ENABLED_STATE_SET, this.getResources()  
            .getDrawable(drawable));  
    imageView.setImageDrawable(listDrawable);  
    imageView.setBackgroundColor(Color.TRANSPARENT);  
    setGravity(Gravity.CENTER);  
    addView(imageView);  
}  
}