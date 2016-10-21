package com.asc.msigeosystems.prism4d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.GpsSatellite;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.view.View;

import com.asc.msigeosystems.prism4dmockup.R;

/**
 * Created by elisabethhuhn on 6/2/2016.
 */

/*
Copyright (C) 2009-2010  Ludwig M Brinckmann <ludwigbrinckmann@gmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

public class SatellitePositionView extends View {

    private Paint  mGridPaint;
    private Paint  mTextPaint;
    private Paint  mBackground;
    private Bitmap mSatelliteBitmapUsed;
    private Bitmap mSatelliteBitmapUnused;
    private Bitmap mSatelliteBitmapNoFix;

    private float mBitmapAdjustment;

    android.location.GpsStatus gpsStatus = null;
    android.location.LocationManager lp = null;

    public SatellitePositionView(Context context) {
        this(context, null);
    }

    public SatellitePositionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatellitePositionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        lp = (android.location.LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        mGridPaint = new Paint();
        mGridPaint.setColor(0xFFDDDDDD);
        mGridPaint.setAntiAlias(true);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setStrokeWidth(1.0f);

        mBackground = new Paint();
        mBackground.setColor(0xFF4444DD);

        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFFFFFFF);
        mTextPaint.setTextSize(15.0f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mSatelliteBitmapUsed = ((BitmapDrawable)getResources().getDrawable(R.drawable.satgreen)).getBitmap();

        //Drawable temp = (Drawable)ContextCompat.getDrawable(context, R.drawable.satgreen);
        //mSatelliteBitmapUsed = (BitmapDrawable) temp.getBitmap();

        mSatelliteBitmapUnused = ((BitmapDrawable)getResources().getDrawable(R.drawable.satyellow)).getBitmap();
        mSatelliteBitmapNoFix = ((BitmapDrawable)getResources().getDrawable(R.drawable.satred)).getBitmap();
        mBitmapAdjustment = mSatelliteBitmapUsed.getHeight() / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //find the center of the grid, and it's radius
        float centerY = getHeight() / 2;
        float centerX = getWidth() / 2;
        int minRadius = Math.min(getHeight(), getWidth());
        int radius = (int)(minRadius / 2) - 8;

        //set the paints for the grid and text overlay
        final Paint gridPaint = mGridPaint;
        final Paint textPaint = mTextPaint;
        canvas.drawPaint(mBackground);

        //draw the grid
        canvas.drawCircle(centerX, centerY, radius, gridPaint);
        canvas.drawCircle(centerX, centerY, radius * 3 / 4, gridPaint);
        canvas.drawCircle(centerX, centerY, radius >> 1, gridPaint);
        canvas.drawCircle(centerX, centerY, radius >> 2, gridPaint);

        canvas.drawLine(centerX, centerY - (radius >> 2), centerX, centerY - radius, gridPaint);
        canvas.drawLine(centerX, centerY + (radius >> 2) , centerX, centerY + radius, gridPaint);
        canvas.drawLine(centerX - (radius >> 2), centerY, centerX - radius, centerY, gridPaint);
        canvas.drawLine(centerX + (radius >> 2), centerY, centerX + radius, centerY, gridPaint);

        //put the satellites on the grid
        float scale = radius / 90.0f;
        if (lp.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            gpsStatus = this.lp.getGpsStatus(gpsStatus);

            float mX;
            float mY;

            for (GpsSatellite s: gpsStatus.getSatellites()){
                //get degrees
                float theta = - (s.getAzimuth() + 90);
                //convert to radians
                float rad = (float) (theta * Math.PI/180.0f);

                //find the (x,y) grid coordinates
                mX = (float)Math.cos(rad);
                mY = -(float)Math.sin(rad);

                //determine whether the satellite is above the horizon
                float elevation = s.getElevation() - 90.0f;
                if (elevation > 90 || s.getAzimuth() < 0 || s.getPrn() < 0){
                    //ignore this satellite, jump to next
                    continue;
                }
                float a = elevation * scale;

                int x = (int)Math.round(centerX + (mX * a) - mBitmapAdjustment);
                int y = (int)Math.round(centerY + (mY * a) - mBitmapAdjustment);
                if (s.usedInFix()){
                    canvas.drawBitmap(mSatelliteBitmapUsed, x, y, gridPaint);
                } else {
                    if (gpsStatus.getTimeToFirstFix() > 0){
                        canvas.drawBitmap(mSatelliteBitmapUnused, x, y, gridPaint);
                    } else {
                        canvas.drawBitmap(mSatelliteBitmapNoFix, x, y, gridPaint);
                    }
                }
                canvas.drawText(new Integer(s.getPrn()).toString(),x, y, textPaint);
            }
        }
    }
}

