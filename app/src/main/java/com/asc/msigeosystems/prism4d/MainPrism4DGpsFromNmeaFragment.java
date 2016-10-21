package com.asc.msigeosystems.prism4d;


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
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

/**
 * The Maintain Point Fragment
 * is passed a point on startup. The point attribute fields are
 * pre-populated prior to updating the point
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DGpsFromNmeaFragment extends Fragment implements GpsStatus.Listener, LocationListener, GpsStatus.NmeaListener {

    private GpsStatus mGpsStatus = null;
    private static Prism4DNmeaParser mNmeaParser = new Prism4DNmeaParser();
    int counter = 0;
    private Prism4DNmea mNmeaData; //latest nmea sentence received

    long     lastNmeaTime;
    String   lastNmeaMessage;
    Criteria criteria;

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


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




    public MainPrism4DGpsFromNmeaFragment() {
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
/*
        boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled){
            //Leave even though project has chaged
            Toast.makeText(getActivity(),
                    R.string.skyplot_gps_not_enabled,
                    Toast.LENGTH_SHORT).show();
        }
*/
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){return v;}

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);






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
        //mLocationManager.addGpsStatusListener(this);
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

        mLocationManager.removeUpdates(this);
        //mLocationManager.removeGpsStatusListener(this);
        mLocationManager.removeNmeaListener(this);
    }




    //******************************************************************//
    //             GPS Listener Callbacks                               //
    //            Called by the OS to handle GPS events                 //
    //******************************************************************//

    //GpsStatus.Listener Callback


    //OS calls this callback when
    // a change has been detected in GPS satellite status
    //Called to report changes in the GPS status.

    // The parameter event type is one of:

    // o GPS_EVENT_STARTED
    // o GPS_EVENT_STOPPED
    // o GPS_EVENT_FIRST_FIX
    // o GPS_EVENT_SATELLITE_STATUS

    //When this method is called,
    // the client should call getGpsStatus(GpsStatus)
    // to get additional status information.
    @Override
    public void onGpsStatusChanged(int eventType) {
        setGpsStatus();
    }



//******************************************************************//
//             Location Listener Callbacks                          //
//            Called by the OS to handle GPS events                 //
//******************************************************************//

    // called when the GPS provider is turned off
    // (i.e. user turning off the GPS on the phone)
    // If requestLocationUpdates is called on an already disabled provider,
    // this method is called immediately.
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

    //Called when the provider status changes.
    // This method is called when
    // o a provider is unable to fetch a location or
    // o if the provider has recently become available
    //    after a period of unavailability.
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
    //todo maybe need to do something with the timestamp
    try {
        //create an object with all the fields from the string
        mNmeaData = mNmeaParser.parse(nmea);

        if (!(mNmeaData == null)){
            //update the UI
            updateNmeaUI(mNmeaData);

            //save the raw data
            //get the nmea container
            Prism4DNmeaContainer nmeaContainer = Prism4DNmeaContainer.getInstance();
            nmeaContainer.add(mNmeaData);
        }


    } catch (RuntimeException e){
        //there was an exception processing the NMEA Sentence
        Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
        //throw new RuntimeException(e);
    }
}


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

    private void wireWidgets(View v) {
        //NMEA Sentence
        mNmeaSentenceLabel = (TextView)v.findViewById(R.id.skyplotNmeaSentenceLabel);
        mNmeaSentenceOutput = (TextView) v.findViewById(R.id.skyplotNmeaSentenceOutput);

        //Time
        mTimeLabel = (TextView)v.findViewById(R.id.skyplotTimeLabel);
        mTimeOutput = (TextView) v.findViewById(R.id.skyplotTimeOutput);

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

    private void updateNmeaUI(Prism4DNmea nmeaData){

        if (nmeaData != null){
            //Which fields have meaning depend upon the type of the sentence
            String type = nmeaData.getNmeaType().toString();
            if (type != null){

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
                Toast toast = Toast.makeText(getActivity(), "Null type found", Toast.LENGTH_SHORT);
                toast.show();
            }


        }

    }



    /*********************************************************/

}


