package com.asc.msigeosystems.prism4dmockup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by elisabethhuhn on 6/2/2016.
 */
public class DataView extends BaseDataView {

    TextView mData;

    public DataView(Context context){
        this(context, null);
    }

    public DataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mData = new TextView(getContext());
        mData.setTextColor(YGPSConstants.dataviewTextColor);
        mData.setGravity(Gravity.FILL_HORIZONTAL);
        mData.setBackgroundColor(YGPSConstants.dataviewTextFieldBgColor);
        mData.setTextSize(YGPSConstants.dataviewTextSize);
        mData.setGravity(Gravity.RIGHT);
        addView(mData, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addLegend();
    }

    public void setData(CharSequence text){
        mData.setText(text);
        mData.postInvalidate();
    }

    public void setData(Double d){
        mData.setText(mFormat.format(d));
        mData.postInvalidate();
    }
    public void setData(Float d){
        mData.setText(Float.toString(d));
        mData.postInvalidate();
    }

    public void setData(int d) {
        mData.setText(Integer.toString(d));
        mData.postInvalidate();
    }

    public void setData(long d) {
        mData.setText(Long.toString(d));
        mData.postInvalidate();
    }

}
