package com.asc.msigeosystems.prism4dmockup;

/**
 * Created by elisabethhuhn on 6/1/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Menu;

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

public class YGPS extends Activity implements Listener, LocationListener {

    public final static String TAG = "YGPS";

    private LocationManager mLocationManager = null;
    SatellitePositionView mPositionView;
    SatelliteSignalView mSignalView;
    private GPSStateView mGpsState;
    private DataView mLatitude;
    private DataView mLongitude;
    private DataView mAccuracy;
    private DataView mAltitude;
    private DataView mBearing;
    private DataView mSpeed;
    private DateView mTime;
    private DateView mDeviceTime;
    private DataView mTtff;
    private DataView mTslf;
    private DataView mSatInSky;
    private DataView mSatInFix;

    //Parser figures out what is in the NMEA Sentences
    private static Prism4DNmeaParser mNmeaParser = new Prism4DNmeaParser();

    private Prism4DNmea mNmeaData; //latest nmea sentence received


    Handler mHandler;
    long mClockSkew;
    LastFixUpdater mLastFixUpdater;
    long mLastUpdateTime = -1;

    private GpsStatus mGpsStatus = null;

    protected int getLayout(){
        return R.layout.ygps;
    }


    //*************** Activity Lifecycle Routines ****************//
    //   Called by OS to step the Activity through its lifecycle  //
    //************************************************************//

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        //GPS Stuff
        //Make sure we have the proper GPS permissions before starting
        //If we don't currently have permission, bail
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){return;}

        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        mLocationManager.addGpsStatusListener(this);
        //mLocationManager.addNmeaListener(this);

        mPositionView = (SatellitePositionView) findViewById(R.id.positionview);
        mSignalView   = (SatelliteSignalView)   findViewById(R.id.signalview);

        mGpsState = (GPSStateView) findViewById(R.id.gpsstate);
        mLatitude =   getField(R.id.latitude,  "Latitude", "degrees");
        mLongitude =  getField(R.id.longitude, "Longitude","degrees");

        mAccuracy =   getField(R.id.accuracy,  "Accuracy", "m");
        mAltitude =   getField(R.id.altitude,  "Altitude", "m", "#.#");
        mSpeed =      getField(R.id.speed,     "Speed",    "kmh", "#.###");
        mBearing =    getField(R.id.bearing,   "Bearing",  "degrees");

        mTime =       getDateField(R.id.time,       "GPS Time",    "");
        mDeviceTime = getDateField(R.id.devicetime, "Device Time", "");

        mSatInSky =   getField(R.id.satinsky,  "Sat in Sky", "");
        mSatInFix =   getField(R.id.satinfix,  "Sat in Fix", "");

        mTtff =       getField(R.id.ttff,      "Time to first fix",   "ms");
        mTslf =       getField(R.id.tslf,      "Time since last fix", "mmm:ss");


        mHandler = new Handler();
        mLastFixUpdater = new LastFixUpdater();
        mHandler.post(mLastFixUpdater);

    }

    //*************** routines called by onCreate() *****************//
    private DataView getField(int id, String description, String units, String format){
        DataView result = getField(id, description, units);
        result.setFormatting(format);
        return result;
    }

    private DataView getField(int id, String description, String units){
        DataView result = (DataView)findViewById(id);
        result.setDescription(description);
        result.setUnits(units);
        return result;
    }

    private DateView getDateField(int id, String description, String units){
        DateView result = (DateView)findViewById(id);
        result.setDescription(description);
        result.setUnits(units);
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean supRetVal = super.onCreateOptionsMenu(menu);
        return supRetVal;
    }

    @Override
    public void onPause() {
        super.onPause();
        //If we don't currently have permission, bail
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED){return;}

        //shut down the listeners
        mLocationManager.removeGpsStatusListener(this);
        mLocationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //If we don't currently have permission, bail
        if (
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){return;}

        //start up the listeners
        mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        mLocationManager.addGpsStatusListener(this);
        setGpsStatus();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }



    //*************** Listener Callback Routines *****************//
    //        Called by OS when a Listener condition is met       //
    //************************************************************//



    @Override
    public void onProviderDisabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (!LocationManager.GPS_PROVIDER.equals(provider)){
            return;
        }
        setGpsStatus();
    }


       //OS calls this callback when
    // a change has been detected in GPS satellite status
    @Override
    public void onGpsStatusChanged(int state) {
        setGpsStatus();
    }

        //OS calls this callback when the location has changed
    @Override
    public void onLocationChanged(Location loc) {
        mGpsState.stateLock();
        updateLocation(loc);
        mLastUpdateTime = loc.getTime();
        mClockSkew = System.currentTimeMillis() - mLastUpdateTime;
        updateLastUpdateTime();
    }

    /****************
    @Override
    public void onNmeaReceived(long timestamp, String nmea) {
        //create an object with all the fields from the string
        mNmeaData = mNmeaParser.parse(nmea);

        //update the UI
        //updateNmeaUI(mNmeaData);

        //save the raw data
        //get the nmea container
        Prism4DNmeaContainer nmeaContainer = Prism4DNmeaContainer.getInstance();
        nmeaContainer.add(mNmeaData);
    }

    *******************************/



    //*********************** End of Callbacks *******************//


    //******************** Callback Utilities *****************//
    //        Called by OS when a Listener condition is met       //
    //************************************************************//


    //Update the UI with satellite status info from GPS
    protected void setGpsStatus(){
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //location manager is enabled,
            //update the UI with info from GPS
            mGpsStatus = mLocationManager.getGpsStatus(mGpsStatus);

            //get satellite data from GPS
            Iterable<GpsSatellite> sats = mGpsStatus.getSatellites();
            int inSky = 0;
            int inFix = 0;
            for (GpsSatellite s : sats){
                inSky += 1;
                if (s.usedInFix()){
                    inFix += 1;
                }
            }

            //update the UI with the GPS info
            mSatInSky.setData(inSky);
            //indicate whether the position is fixed (locked) or not
            mSatInFix.setData(inFix);
            if (inFix > 0){
                mGpsState.stateLock();
            } else {
                mGpsState.stateOn();
            }
            mTtff.setData(mGpsStatus.getTimeToFirstFix());

        } else {
            //location manager is not enabled, u
            // pdate the UI with that
            mGpsState.stateOff();
            mSatInFix.setData("no fix");
            mSatInSky.setData("gps off");
        }

        mPositionView.postInvalidate();
        mSignalView.postInvalidate();
    }


    //update the UI with location data
    private void updateLocation(Location loc){
        mLatitude. setData(loc.getLatitude());
        mLongitude.setData(loc.getLongitude());
        mAccuracy. setData(loc.getAccuracy());
        mAltitude. setData(loc.getAltitude());
        mBearing.  setData(loc.getBearing());
        mSpeed.    setData(loc.getSpeed());
        mTime.     setData(loc.getTime());
    }


    private void updateLastUpdateTime(){
        if (mLastUpdateTime >= 0){
            long t = Math.round((System.currentTimeMillis() - mLastUpdateTime - mClockSkew) / 1000);
            long sec = t % 60;
            long min = (t / 60);
            mTslf.setData(String.format("%d:%02d", min, sec));
        }
        mDeviceTime.setData(System.currentTimeMillis());
    }

    //inner  class that runs on another thread
    //every time it runs,
    // it posts another message for itself to run again in a second
    private class LastFixUpdater implements Runnable {
        @Override
        public void run() {
            updateLastUpdateTime();
            mHandler.postDelayed(this, 1000);
        }
    }
}

