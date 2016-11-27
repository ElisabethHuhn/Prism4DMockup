package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 6/1/2016.
 *
 * GPS example
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;


public class Prism4DGPSActivity  extends Activity implements GpsStatus.Listener, LocationListener, GpsStatus.NmeaListener {


    public final static String TAG = "PRISM4D_GPS";

    private LocationManager mLocationManager = null;

    //Parser figures out what is in the NMEA Sentences
    private static Prism4DNmeaParser mNmeaParser = new Prism4DNmeaParser();

    private Prism4DNmea mNmeaData; //latest nmea sentence received

    Handler mHandler;
    long mClockSkew;
    LastFixUpdater mLastFixUpdater;
    long mLastUpdateTime = -1;

    private GpsStatus mGpsStatus = null;

    int counter = 0;

    // Output Fields on screen

    //Location
    private TextView mTimeLabel;
    private TextView mTimeOutput;

    private TextView mNmeaSentenceLabel;
    private TextView mNmeaSentenceOutput;

    private TextView mLatitudeLabel;
    private TextView mLatitudeOutput;

    private TextView mLongitudeLabel;
    private TextView mLongitudeOutput;

    private TextView mEllipsoidElevationLabel;
    private TextView mEllipsoidElevationOutput;

    private TextView mGeoidLabel;
    private TextView mGeoidOutput;

    private TextView mOrthometricElevationLabel;
    private TextView mOrthometricElevationOutput;

    private TextView mLocalizationLabel;
    private TextView mLocalizationOutput;

    private TextView mLocalNorthingLabel;
    private TextView mLocalNorthingOutput;

    private TextView mLocalEastingLabel;
    private TextView mLocalEastingOutput;

    private TextView mLocalElevationLabel;
    private TextView mLocalElevationOutput;

    //Status
    private TextView mStatusLabel;
    private TextView mStatusOutput;

    private TextView mSatellitesLabel;
    private TextView mSatellitesOutput;

    private TextView mHDopLabel;
    private TextView mHDopOutput;

    private TextView mVDopLabel;
    private TextView mVDopOutput;

    private TextView mTDopLabel;
    private TextView mTDopOutput;

    private TextView mPDopLabel;
    private TextView mPDopOutput;

    private TextView mGDopLabel;
    private TextView mGDopOutput;

    private TextView mHRmsLabel;
    private TextView mHRmsOutput;

    private TextView mVRmsLabel;
    private TextView mVRmsOutput;




    //*************** Activity Lifecycle Routines ****************//
    //   Called by OS to step the Activity through its lifecycle  //
    //************************************************************//

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skyplot_status_prism4d);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        wireWidgets();
/*
        boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled){
            //Leave even though project has chaged
            Toast.makeText(this,
                    R.string.skyplot_gps_not_enabled,
                    Toast.LENGTH_SHORT).show();

        }
 */
        //GPS Stuff
        //Make sure we have the proper GPS permissions before starting
        //If we don't currently have permission, bail
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){return;}

        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //ask for updates in onResume()
        //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, this);
        //mLocationManager.addGpsStatusListener(this);
        //mLocationManager.addNmeaListener(this);



        mHandler = new Handler();
        mLastFixUpdater = new LastFixUpdater();
        mHandler.post(mLastFixUpdater);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //If we don't currently have permission, bail
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if (
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED){return;}

            //start up the listeners
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            //mLocationManager.addGpsStatusListener(this);
            mLocationManager.addNmeaListener(this);
            setGpsStatus();
        }

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
        //mLocationManager.removeUpdates(this);
        mLocationManager.removeNmeaListener(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
    }



    //*************** Listener Callback Routines *****************//
    //        Called by OS when a Listener condition is met       //
    //************************************************************//

    //LocationListener Callbacks

    //Called when the provider is disabled by the user.
    // If requestLocationUpdates is called on an already disabled provider,
    // this method is called immediately.
    @Override
    public void onProviderDisabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }
    }

    //Called when the provider is enabled by the user.
    @Override
    public void onProviderEnabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }
    }

    //Called when the provider status changes.
    // This method is called when
    // o a provider is unable to fetch a location or
    // o if the provider has recently become available
    //    after a period of unavailability.
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (!LocationManager.GPS_PROVIDER.equals(provider)){
            return;
        }
        setGpsStatus();
    }


        //OS calls this callback when the location has changed
    //Called when the location has changed.
    @Override
    public void onLocationChanged(Location loc) {
        /***************
        mGpsState.stateLock();
        updateLocation(loc);
         *********/
        mLastUpdateTime = loc.getTime();
        mClockSkew = System.currentTimeMillis() - mLastUpdateTime;
        updateLastUpdateTime();

    }


    //GpsStatus.Listener Callback


    //OS calls this callback when
    // a change has been detected in GPS satellite status
    //Called to report changes in the GPS status.

    // The parameter event ID is one of:

    // o GPS_EVENT_STARTED
    // o GPS_EVENT_STOPPED
    // o GPS_EVENT_FIRST_FIX
    // o GPS_EVENT_SATELLITE_STATUS

    //When this method is called,
    // the client should call getGpsStatus(GpsStatus)
    // to get additional status information.
    @Override
    public void onGpsStatusChanged(int eventID) {
        setGpsStatus();
    }


    //GpsStatus.NmeaListener callback

    @Override
    public void onNmeaReceived(long timestamp, String nmea) {
        //todo maybe need to do something with the timestamp
        //String tempString = nmea; //debug. If you see this, remove it
        try {
            //create an object with all the fields from the string
            mNmeaData = mNmeaParser.parse(nmea);

            if (!(mNmeaData == null)){
                //update the UI
                updateNmeaUI(mNmeaData);

                //save the raw data
                //get the nmea container
                Prism4DNmeaManager nmeaManager = Prism4DNmeaManager.getInstance();
                nmeaManager.add(mNmeaData);
            }


        } catch (RuntimeException e){
            //there was an exception processing the NMEA Sentence
            Toast toast = Toast.makeText(this, "Runtime Exception: " + e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
            //throw new RuntimeException(e);
        }
    }


    //*********************** End of Callbacks *******************//


    //******************** Callback Utilities *****************//
    //        Called by OS when a Listener condition is met       //
    //************************************************************//


    //Update the UI with satellite status info from GPS
    protected void setGpsStatus(){
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //location manager is enabled,
            //update the UI with info from GPS
            counter++;
            mLocalizationOutput.setText("Set GPS is called for the "+(Integer.toString(counter))+"th time.");        }
    }

    /****************************** Utilities *********************************************/

    private void wireWidgets() {
        //NMEA Sentence
        mNmeaSentenceLabel = (TextView)findViewById(R.id.skyplotNmeaSentenceLabel);
        mNmeaSentenceOutput = (TextView) findViewById(R.id.skyplotNmeaSentenceOutput);

        //Time
        mTimeLabel = (TextView)findViewById(R.id.skyplotTimeLabel);
        mTimeOutput = (TextView) findViewById(R.id.skyplotTimeOutput);

        //Latitude
        mLatitudeLabel = (TextView)findViewById(R.id.skyplotLatitudeLabel);
        mLatitudeOutput = (TextView) findViewById(R.id.skyplotLatitudeOutput);

        //Longitude
        mLongitudeLabel = (TextView)findViewById(R.id.skyplotLongitudeLabel);
        mLongitudeOutput = (TextView) findViewById(R.id.skyplotLongitudeOutput);

        //Ellipsoid Elevation
        mEllipsoidElevationLabel = (TextView) findViewById(R.id.skyplotEllipsoidElevationLabel);
        mEllipsoidElevationOutput = (TextView) findViewById(R.id.skyplotEllipsoidElevationOutput);

        //Ellipsoid Elevation
        mGeoidLabel = (TextView) findViewById(R.id.skyplotGeoidLabel);
        mGeoidOutput = (TextView) findViewById(R.id.skyplotGeoidOutput);

        //Orthometric Elevation
        mOrthometricElevationLabel = (TextView) findViewById(R.id.skyplotOrthometricElevationLabel);
        mOrthometricElevationOutput = (TextView) findViewById(R.id.skyplotOrthometricElevationOutput);


        //Localization
        mLocalizationLabel = (TextView) findViewById(R.id.skyplotLocalizationLabel);
        mLocalizationOutput = (TextView) findViewById(R.id.skyplotLocalizationOutput);

        //Local Northing
        mLocalNorthingLabel = (TextView) findViewById(R.id.skyplotLocalNorthingLabel);
        mLocalNorthingOutput = (TextView) findViewById(R.id.skyplotLocalNorthingOutput);

        //Local Easting
        mLocalEastingLabel = (TextView) findViewById(R.id.skyplotLocalEastingLabel);
        mLocalEastingOutput = (TextView) findViewById(R.id.skyplotLocalEastingOutput);


        //Local Elevation
        mLocalElevationLabel = (TextView) findViewById(R.id.skyplotLocalElevationLabel);
        mLocalElevationOutput = (TextView) findViewById(R.id.skyplotLocalElevationOutput);

        //Status
        mStatusLabel = (TextView) findViewById(R.id.skyplotStatusLabel);
        mStatusOutput = (TextView) findViewById(R.id.skyplotStatusOutput);

        //Satellites
        mSatellitesLabel = (TextView) findViewById(R.id.skyplotSatellitesLabel);
        mSatellitesOutput = (TextView) findViewById(R.id.skyplotSatellitesOutput);

        //HDOP
        mHDopLabel = (TextView) findViewById(R.id.skyplotHdopLabel);
        mHDopOutput = (TextView) findViewById(R.id.skyplotHdopOutput);

        //VDOP
        mVDopLabel = (TextView) findViewById(R.id.skyplotVdopLabel);
        mVDopOutput = (TextView) findViewById(R.id.skyplotVdopOutput);

        //TDOP
        mTDopLabel = (TextView) findViewById(R.id.skyplotTdopLabel);
        mTDopOutput = (TextView) findViewById(R.id.skyplotTdopOutput);

        //PDOP
        mPDopLabel = (TextView) findViewById(R.id.skyplotPdopLabel);
        mPDopOutput = (TextView) findViewById(R.id.skyplotPdopOutput);

        //GDOP
        mGDopLabel = (TextView) findViewById(R.id.skyplotGdopLabel);
        mGDopOutput = (TextView) findViewById(R.id.skyplotGdopOutput);

        //HRMS
        mHRmsLabel = (TextView) findViewById(R.id.skyplotHrmsLabel);
        mHRmsOutput = (TextView) findViewById(R.id.skyplotHrmsOutput);

        //VRMS
        mVRmsLabel = (TextView) findViewById(R.id.skyplotVrmsLabel);
        mVRmsOutput = (TextView) findViewById(R.id.skyplotVrmsOutput);
    }

    private void updateNmeaUI(Prism4DNmea nmeaData){

        if (!(nmeaData == null)){
            //Which fields have meaning depend upon the type of the sentence
            String type = nmeaData.getNmeaType().toString();
            if (!(type == null)){

                if (type.contains("GGA")) {
                    mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mTimeOutput.setText(Double.toString(nmeaData.getTime()));
                    mLatitudeOutput.setText(Double.toString(nmeaData.getLatitude()));
                    mLongitudeOutput.setText(Double.toString(nmeaData.getLongitude()));
                    mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
                    mHDopOutput.setText(Double.toString(nmeaData.getHdop()));
                    mOrthometricElevationOutput.
                            setText(Double.toString(nmeaData.getOrthometricElevation()));
                    mGeoidOutput.setText(Double.toString(nmeaData.getGeoid()));
                    //fixed or quality
                } else if (type.contains("GNS")) {
                    mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mTimeOutput.setText(Double.toString(nmeaData.getTime()));
                    mLatitudeOutput.setText(Double.toString(nmeaData.getLatitude()));
                    mLongitudeOutput.setText(Double.toString(nmeaData.getLongitude()));
                    mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
                    mHDopOutput.setText(Double.toString(nmeaData.getHdop()));
                    mOrthometricElevationOutput.
                            setText(Double.toString(nmeaData.getOrthometricElevation()));
                    mGeoidOutput.setText(Double.toString(nmeaData.getGeoid()));
                    //fixed or quality
                } else if (type.contains("GGL")) {
                    mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mTimeOutput.setText(Double.toString(nmeaData.getTime()));
                    mLatitudeOutput.setText(Double.toString(nmeaData.getLatitude()));
                    mLongitudeOutput.setText(Double.toString(nmeaData.getLongitude()));
                } else if (type.contains("RMA")) {
                    mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mLatitudeOutput.setText(Double.toString(nmeaData.getLatitude()));
                    mLongitudeOutput.setText(Double.toString(nmeaData.getLongitude()));
                } else if (type.contains("RMC")) {
                    mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mTimeOutput.setText(Double.toString(nmeaData.getTime()));
                    mLatitudeOutput.setText(Double.toString(nmeaData.getLatitude()));
                    mLongitudeOutput.setText(Double.toString(nmeaData.getLongitude()));
                }else if (type.contains("GSV")) {
                    //this is better shown graphically
                    mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
                    //TODO rest of satellite data
                } else if (type.contains("GSA")) {
                    mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
                    mPDopOutput.setText(Double.toString(nmeaData.getPdop()));
                    mHDopOutput.setText(Double.toString(nmeaData.getHdop()));
                    mVDopOutput.setText(Double.toString(nmeaData.getVdop()));
                }

            } else {
                //there was an exception processing the NMEA Sentence
                Toast toast = Toast.makeText(this, "Null type found", Toast.LENGTH_SHORT);
                toast.show();
            }


        }

    }


    /*********************************************************/


    private void updateLastUpdateTime(){
        if (mLastUpdateTime >= 0){
            long t = Math.round((System.currentTimeMillis() - mLastUpdateTime - mClockSkew) / 1000);
            long sec = t % 60;
            long min = (t / 60);
            //mTslf.setData(String.format("%d:%02d", min, sec));
        }
        //mDeviceTime.setData(System.currentTimeMillis());
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

