package com.asc.msigeosystems.prism4d;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by elisabethhuhn on 6/2/2016.
 */
public class BaseDataView extends LinearLayout {
    protected Context mContext;
    protected LinearLayout mLegend;
    protected TextView mDescription;
    protected TextView mUnits;
    protected DecimalFormat mFormat;


    public BaseDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFormat = new DecimalFormat("#.######");
        setBackgroundColor(YGPSConstants.dataviewBgColor);

        setPadding(5,5,5,5);
        setOrientation(LinearLayout.VERTICAL);
        mDescription = new TextView(getContext());
        mDescription.setBackgroundColor(YGPSConstants.dataviewLegendFieldBgColor);
        mDescription.setGravity(Gravity.LEFT);
        mDescription.setTextColor(YGPSConstants.dataviewTextColor);
        mUnits = new TextView(getContext());
        mUnits.setBackgroundColor(YGPSConstants.dataviewLegendFieldBgColor);
        mUnits.setGravity(Gravity.RIGHT);
        mUnits.setTextColor(YGPSConstants.dataviewTextColor);
        mLegend = new LinearLayout(getContext());
        mLegend.setOrientation(LinearLayout.HORIZONTAL);
        mLegend.setBackgroundColor(YGPSConstants.dataviewLegendFieldBgColor);
    }

    protected void addLegend(){
        mLegend.addView(mDescription, new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.FILL_PARENT,1));
        mLegend.addView(mUnits, new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.FILL_PARENT,1));
        addView(mLegend, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void setUnits(CharSequence text){
        mUnits.setText(text);
        mUnits.postInvalidate();
    }

    public void setDescription(CharSequence text){
        mDescription.setText(text);
        mDescription.postInvalidate();
    }

    public void setFormatting(String d){
        mFormat = new DecimalFormat(d);
    }

}

