package com.asc.msigeosystems.prism4dmockup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.GpsSatellite;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by elisabethhuhn on 6/2/2016.
 *
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



public class SatelliteSignalView extends View {

        private Paint mLinePaint;
        private Paint mThinLinePaint;
        private Paint mBarPaintUsed;
        private Paint mBarPaintUnused;
        private Paint mBarPaintNoFix;
        private Paint mBarOutlinePaint;
        private Paint mTextPaint;
        private Paint mBackground;

        android.location.GpsStatus gpsStatus = null;
        android.location.LocationManager lp = null;

        public SatelliteSignalView(Context context) {
            this(context, null);
        }
        public SatelliteSignalView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }
        public SatelliteSignalView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            lp = (android.location.LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

            mLinePaint = new Paint();
            mLinePaint.setColor(0xFFDDDDDD);
            mLinePaint.setAntiAlias(true);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setStrokeWidth(1.0f);

            mThinLinePaint = new Paint(mLinePaint);
            mThinLinePaint.setStrokeWidth(0.5f);

            mBarPaintUsed = new Paint();
            mBarPaintUsed.setColor(0xFF00BB00);
            mBarPaintUsed.setAntiAlias(true);
            mBarPaintUsed.setStyle(Paint.Style.FILL);
            mBarPaintUsed.setStrokeWidth(1.0f);

            mBarPaintUnused = new Paint(mBarPaintUsed);
            mBarPaintUnused.setColor(0xFFFFCC33);

            mBarPaintNoFix = new Paint(mBarPaintUsed);
            mBarPaintNoFix.setStyle(Paint.Style.STROKE);

            mBarOutlinePaint = new Paint();
            mBarOutlinePaint.setColor(0xFFFFFFFF);
            mBarOutlinePaint.setAntiAlias(true);
            mBarOutlinePaint.setStyle(Paint.Style.STROKE);
            mBarOutlinePaint.setStrokeWidth(1.0f);

            mTextPaint = new Paint();
            mTextPaint.setColor(0xFFFFFFFF);
            mTextPaint.setTextSize(15.0f);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mBackground = new Paint();
            mBackground.setColor(0xFF222222);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            final int fill = 6;
            final int baseline = getHeight() - 30;
            final int maxHeight = getHeight() - 40;
            final float scale = maxHeight / 100.0F;

            canvas.drawPaint(mBackground);

            canvas.drawLine(0, baseline, getWidth(), baseline, mLinePaint);
            // paint lines to indicate signal strength
            float y = baseline - (100 * scale);
            canvas.drawLine(0, y, getWidth(), y, mThinLinePaint);
            y = baseline - (50 * scale);
            canvas.drawLine(0, y, getWidth(), y, mThinLinePaint);
            y = baseline - (25 * scale);
            canvas.drawLine(0, y, getWidth(), y, mThinLinePaint);
            y = baseline - (75 * scale);
            canvas.drawLine(0, y, getWidth(), y, mThinLinePaint);


            if (lp.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                int drawn = 0;

                gpsStatus = this.lp.getGpsStatus(gpsStatus);
                int maxSatellites = 12;

                float slotWidth = (float)Math.floor(getWidth() / maxSatellites);
                float barWidth = slotWidth - fill;
                float margin = (getWidth() - (slotWidth * maxSatellites)) / 2 ;

                for (GpsSatellite s: gpsStatus.getSatellites()){
                    float left = margin + (drawn * slotWidth) + fill/2;
                    if (s.usedInFix()){
                        canvas.drawRect(
                                left,
                                baseline - (s.getSnr() * scale),
                                left + barWidth,
                                baseline,
                                mBarPaintUsed);
                    } else {
                        canvas.drawRect(
                                left,
                                baseline - (s.getSnr() * scale),
                                left + barWidth,
                                baseline,
                                mBarPaintUnused);
                    }
                    canvas.drawRect(
                            left,
                            baseline - (s.getSnr() * scale),
                            left + barWidth,
                            baseline,
                            mBarOutlinePaint);
                    canvas.drawText(
                            new Integer(s.getPrn()).toString(),
                            left + barWidth / 2 ,
                            baseline + 15,
                            mTextPaint);
                    drawn += 1;
                }
            }
        }
    }
