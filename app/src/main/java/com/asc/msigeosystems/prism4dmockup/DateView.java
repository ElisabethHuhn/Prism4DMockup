package com.asc.msigeosystems.prism4dmockup;

import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;

/**
 * Created by elisabethhuhn on 6/2/2016.
 */
public class DateView extends DataView {

    Time mTime;

    public DateView(Context context){
        this(context, null);
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTime = new Time();
    }

    public void setData(long d) {
        mTime.set(d);
        mData.setText(mTime.format3339(false));
        mData.postInvalidate();
    }

}
