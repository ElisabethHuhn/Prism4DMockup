package com.asc.msigeosystems.prism4d;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Elisabeth Huhn on 6/2/2016.
 */
public class IconView extends BaseDataView {

    ImageView mData;
    /* dummy is used to force height of field to height of text fields */
    TextView mDummy;
    LinearLayout mDataWrapper;

    public IconView(Context context){
        this(context, null);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mData = new ImageView(getContext());
        mData.setBackgroundColor(YGPSConstants.dataviewTextFieldBgColor);
        mDummy = new TextView(getContext());
        mDummy.setBackgroundColor(YGPSConstants.dataviewTextFieldBgColor);
        mDummy.setTextSize(YGPSConstants.dataviewTextSize);
        mDataWrapper = new LinearLayout(getContext());
        mDataWrapper.setOrientation(HORIZONTAL);
        mDataWrapper.setBackgroundColor(YGPSConstants.dataviewTextFieldBgColor);

        mDataWrapper.addView(mData, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT,10000));
        mDataWrapper.addView(mDummy, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT,1));
        addView(mDataWrapper, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addLegend();
    }

    public void setImageResource(int drawable){
        mData.setImageResource(drawable);
    }
}
