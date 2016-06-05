package com.asc.msigeosystems.prism4dmockup;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by elisabethhuhn on 6/2/2016.
 */
public class GPSStateView extends IconView {

    public GPSStateView(Context context){
        this(context, null);
    }

    public GPSStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDescription("GPS State");
    }

    void stateOff(){
        setImageResource(R.drawable.satred);
        setUnits("off");
    }

    void stateOn(){
        setImageResource(R.drawable.satyellow);
        setUnits("no lock");
    }

    void stateLock(){
        setImageResource(R.drawable.satgreen);
        setUnits("lock");
    }
}


