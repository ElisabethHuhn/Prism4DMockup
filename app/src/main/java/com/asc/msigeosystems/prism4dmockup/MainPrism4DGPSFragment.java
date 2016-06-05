package com.asc.msigeosystems.prism4dmockup;


import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * The Maintain Point Fragment
 * is passed a point on startup. The point attribute fields are
 * pre-populated prior to updating the point
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DGPSFragment extends Fragment
        implements GpsStatus.Listener, LocationListener, GpsStatus.NmeaListener {

    private GpsStatus mGpsStatus = null;

    long     lastNmeaTime;
    String   lastNmeaMessage;
    Criteria criteria;

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    // Output Fields on screen

    //Location
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



    private double   mLocalEasting;
    private double   mLocalNorthing;
    private double   mLocalElevation;

    private NotificationManager mNM;
    private LocationManager     mLocationManager;
    private Location            mCurLocation;


    private String              mCurRMCString;
    private String              mCurGGAString;




    public MainPrism4DGPSFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    //******************************************************************//
    //        Fragment Lifecycle routines                               //
    //    Called by the OS to step the fragment through its lifecycle   //
    //******************************************************************//

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Get Managers
        //mNM = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        //the location manager is started in onResume() rather than here

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle         savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(
                            R.layout.fragment_skyplot_status_prism4d,
                            container,
                            false);


        //Wire up the UI widgets so they can handle events later
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields
        wireWidgets(v);

        return v;
    }

    //Ask for location events to start
    @Override
    public void onResume() {
        super.onResume();
        //If we don't currently have permission, bail
        if (
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){return;}

        //ask the Location Manager to start sending us updates
        mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        mLocationManager.addGpsStatusListener(this);
        mLocationManager.addNmeaListener(this);

        setGpsStatus();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    //Ask for location events to stop
    @Override
    public void onPause() {
        super.onPause();
        //If we don't currently have permission, bail
        if (
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED){return;}
        mLocationManager.removeGpsStatusListener(this);
        mLocationManager.removeUpdates(this);
        mLocationManager.removeNmeaListener(this);
    }

/****************************** Utilities *********************************************/

    private void wireWidgets(View v) {
        //Latitude
        mLatitudeLabel = (TextView)v.findViewById(R.id.skyplotLatitudeLabel);
        mLatitudeOutput = (TextView) v.findViewById(R.id.skyplotLatitudeOutput);

        //Longitude
        mLongitudeLabel = (TextView)v.findViewById(R.id.skyplotLongitudeLabel);
        mLongitudeOutput = (TextView) v.findViewById(R.id.skyplotLongitudeOutput);

        //Ellipsoid Elevation
        mEllipsoidElevationLabel = (TextView) v.findViewById(R.id.skyplotEllipsoidElevationLabel);
        mEllipsoidElevationOutput = (TextView) v.findViewById(R.id.skyplotEllipsoidElevationOutput);

        //Ellipsoid Elevation
        mGeoidLabel = (TextView) v.findViewById(R.id.skyplotGeoidLabel);
        mGeoidOutput = (TextView) v.findViewById(R.id.skyplotGeoidOutput);

        //Orthometric Elevation
        mOrthometricElevationLabel = (TextView) v.findViewById(R.id.skyplotOrthometricElevationLabel);
        mOrthometricElevationOutput = (TextView) v.findViewById(R.id.skyplotOrthometricElevationOutput);


        //Localization
        mLocalizationLabel = (TextView) v.findViewById(R.id.skyplotLocalizationLabel);
        mLocalizationOutput = (TextView) v.findViewById(R.id.skyplotLocalizationOutput);

        //Local Northing
        mLocalNorthingLabel = (TextView) v.findViewById(R.id.skyplotLocalNorthingLabel);
        mLocalNorthingOutput = (TextView) v.findViewById(R.id.skyplotLocalNorthingOutput);

        //Local Easting
        mLocalEastingLabel = (TextView) v.findViewById(R.id.skyplotLocalEastingLabel);
        mLocalEastingOutput = (TextView) v.findViewById(R.id.skyplotLocalEastingOutput);


        //Local Elevation
        mLocalElevationLabel = (TextView) v.findViewById(R.id.skyplotLocalElevationLabel);
        mLocalElevationOutput = (TextView) v.findViewById(R.id.skyplotLocalElevationOutput);

        //Status
        mStatusLabel = (TextView) v.findViewById(R.id.skyplotStatusLabel);
        mStatusOutput = (TextView) v.findViewById(R.id.skyplotStatusOutput);

        //Satellites
        mSatellitesLabel = (TextView) v.findViewById(R.id.skyplotSatellitesLabel);
        mSatellitesOutput = (TextView) v.findViewById(R.id.skyplotSatellitesOutput);

        //HDOP
        mHDopLabel = (TextView) v.findViewById(R.id.skyplotHdopLabel);
        mHDopOutput = (TextView) v.findViewById(R.id.skyplotHdopOutput);

        //VDOP
        mVDopLabel = (TextView) v.findViewById(R.id.skyplotVdopLabel);
        mVDopOutput = (TextView) v.findViewById(R.id.skyplotVdopOutput);

        //TDOP
        mTDopLabel = (TextView) v.findViewById(R.id.skyplotTdopLabel);
        mTDopOutput = (TextView) v.findViewById(R.id.skyplotTdopOutput);

        //PDOP
        mPDopLabel = (TextView) v.findViewById(R.id.skyplotPdopLabel);
        mPDopOutput = (TextView) v.findViewById(R.id.skyplotPdopOutput);

        //GDOP
        mGDopLabel = (TextView) v.findViewById(R.id.skyplotGdopLabel);
        mGDopOutput = (TextView) v.findViewById(R.id.skyplotGdopOutput);

        //HRMS
        mHRmsLabel = (TextView) v.findViewById(R.id.skyplotHrmsLabel);
        mHRmsOutput = (TextView) v.findViewById(R.id.skyplotHrmsOutput);

        //VRMS
        mVRmsLabel = (TextView) v.findViewById(R.id.skyplotVrmsLabel);
        mVRmsOutput = (TextView) v.findViewById(R.id.skyplotVrmsOutput);
    }

/*********************************************************/

//******************************************************************//
//             GPS Listener Callbacks                               //
//            Called by the OS to handle GPS events                 //
//******************************************************************//

    @Override
    public void onGpsStatusChanged(int state) {
        setGpsStatus();
    }

    //utility that is called by most of the GPS callbacks to update the UI
    protected void setGpsStatus() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mGpsStatus = mLocationManager.getGpsStatus(mGpsStatus);
        }
        //plluss a whole bunch of other stuff
    }


//******************************************************************//
//             Location Listener Callbacks                          //
//            Called by the OS to handle GPS events                 //
//******************************************************************//

    // called when the GPS provider is turned off
    // (i.e. user turning off the GPS on the phone)
    @Override
    public void onProviderDisabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }
    }

    // called when the GPS provider is turned on
    // (i.e. user turning on the GPS on the phone)
    @Override
    public void onProviderEnabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }
    }

    // called when the status of the GPS provider changes
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }

    }


    // called when the listener is notified with a location update from the GPS
    @Override
    public void onLocationChanged(Location loc) {
        mCurLocation = new Location(loc); // copy location
    }


//******************************************************************//
//             NMEA Listener Callbacks                              //
//            Called by the OS to handle GPS events                 //
//******************************************************************//

    @Override
    public void onNmeaReceived(long timestamp, String nmea) {
        String lowerNmea = nmea.toLowerCase(Locale.ENGLISH);
        if (nmea.startsWith("$GPRMC")) {
            if(lowerNmea.indexOf(",v,") == -1) {
//						Log.v(TAG, "NMEAListener: " + nmea.trim());
                mCurRMCString = nmea;
                return;
            } else {
                mCurRMCString = null;
            }
        }

        if (nmea.startsWith("$GPGGA")) {
            if( lowerNmea.indexOf(",e,") > 0 ||
                    lowerNmea.indexOf(",w,") > 0 ) {
//						Log.v(TAG, "NMEAListener: " + nmea.trim());
                mCurGGAString = nmea;
                return;
            } else {
                mCurGGAString = null;
            }
        }
    }



    /********************** Another GPS example ************************************/

    //supress compiler warnings about type checking
    //@SuppressWarnings("unchecked")
    public void GeoNmea(Context context) {


        //super(context);
        //setSucker(this);

        //check for permission to continue
        if (
           (ContextCompat.checkSelfPermission(getActivity(),
                                              Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) ||
           (ContextCompat.checkSelfPermission(getActivity(),
                                              Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
            return;
        }

        LocationManager lm = (LocationManager)
                context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        } else {
            //Log.d(LOG, "NETWORK PROVIDER is unavailable");
        }

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            //Log.d(LOG, "GPS PROVIDER is unavailable");
        }

        lm.addNmeaListener(new GpsStatus.NmeaListener() {

            /**
             * Used for receiving NMEA sentences from the GPS.
             * NMEA 0183 is a standard for communicating with
             * marine electronic devices and is a common method
             * for receiving data from a GPS,
             * typically over a serial port.
             * See NMEA 0183 for more details.
             * You can implement this interface and call
             * addNmeaListener(GpsStatus.NmeaListener)
             * to receive NMEA data from the GPS engine.
             */
            @Override
            public void onNmeaReceived(long timestamp, String nmea) {

                lastNmeaTime = timestamp;
                lastNmeaMessage = nmea;
            }

        });

        criteria = new Criteria();

        try
        {
            criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        }
        catch (IllegalArgumentException iae){}

        try
        {
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
        }
        catch (IllegalArgumentException iae){}



/************
        setTask(new TimerTask() {

            @Override
            public void run() throws NullPointerException {
                if(getIsRunning()) {
                    try {
                        double[] loc = updateLocation();
                        if (loc != null)
                            sendToBuffer(
                                    new ILogPack(Geo.Keys.GPS_COORDS,
                                            "[" + loc[0] + "," + loc[1] + "]"));
                    } catch(NullPointerException e) {
                        Log.e(LOG, "location NPE", e);
                    }
                }
            }
        });


        getTimer().schedule(getTask(), 0, Geo.LOG_RATE);
********************/

    }


}


