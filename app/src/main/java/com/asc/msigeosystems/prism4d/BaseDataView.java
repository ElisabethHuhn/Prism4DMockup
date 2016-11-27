package com.asc.msigeosystems.prism4d;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Elisabeth Huhn on 6/2/2016.
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
        mDescription.setGravity(Gravity.START);
        mDescription.setTextColor(YGPSConstants.dataviewTextColor);
        mUnits = new TextView(getContext());
        mUnits.setBackgroundColor(YGPSConstants.dataviewLegendFieldBgColor);
        mUnits.setGravity(Gravity.END);
        mUnits.setTextColor(YGPSConstants.dataviewTextColor);
        mLegend = new LinearLayout(getContext());
        mLegend.setOrientation(LinearLayout.HORIZONTAL);
        mLegend.setBackgroundColor(YGPSConstants.dataviewLegendFieldBgColor);
    }

    protected void addLegend(){
        mLegend.addView(mDescription, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT,1));
        mLegend.addView(mUnits, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT,1));
        addView(mLegend, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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

