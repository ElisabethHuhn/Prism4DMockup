package com.asc.msigeosystems.prism4dmockup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The Collect Fragment is the UI
 * when the workflow from WGS84 GPS to NAD83 to UTM/State Plane Coordinates
 * Created by elisabethhuhn on 6/15/2016.
 */
public class MainPrism4DCoordWorkflowFragment extends Fragment implements GpsStatus.Listener, LocationListener, GpsStatus.NmeaListener {

    private GpsStatus mGpsStatus = null;
    private static Prism4DNmeaParser mNmeaParser = new Prism4DNmeaParser();
    int counter = 0;
    private Prism4DNmea mNmeaData; //latest nmea sentence received
    private LocationManager     mLocationManager;
    private Location            mCurLocation;



    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private Button mStartGpsButton;
    private Button mStopGpsButton;
    private Button mStartMeanButton;
    private Button mStopMeanButton;

    private Button mConvertButton;
    private Button mClearButton;

    //Input / Output Fields on screen
    private TextView mGpsWgs84TimeOutput;
    private TextView mGpsWgs84LatitudeInput;
    private TextView mGpsWgs84LatDegreesInput;
    private TextView mGpsWgs84LatMinutesInput;
    private TextView mGpsWgs84LatSecondsInput;

    private TextView mGpsWgs84LongitudeInput;
    private TextView mGpsWgs84LongDegreesInput;
    private TextView mGpsWgs84LongMinutesInput;
    private TextView mGpsWgs84LongSecondsInput;

    private TextView mGpsWgs84ElevationInput;
    private TextView mGpsWgs84GeoidHeightInput;


    private TextView mMeanWgs84StartTimeOutput;
    private TextView mMeanWgs84EndTimeOutput;
    private TextView mMeanWgs84PointsInMeanOutput;

    private TextView mMeanWgs84LatitudeInput;
    private TextView mMeanWgs84LatDegreesInput;
    private TextView mMeanWgs84LatMinutesInput;
    private TextView mMeanWgs84LatSecondsInput;

    private TextView mMeanWgs84LongitudeInput;
    private TextView mMeanWgs84LongDegreesInput;
    private TextView mMeanWgs84LongMinutesInput;
    private TextView mMeanWgs84LongSecondsInput;

    private TextView mMeanWgs84ElevationInput;
    private TextView mMeanWgs84GeoidHeightInput;



    private TextView mNad83LatitudeInput;
    private TextView mNad83LatDegreesInput;
    private TextView mNad83LatMinutesInput;
    private TextView mNad83LatSecondsInput;

    private TextView mNad83LongitudeInput;
    private TextView mNad83LongDegreesInput;
    private TextView mNad83LongMinutesInput;
    private TextView mNad83LongSecondsInput;

    private TextView mNad83ElevationInput;
    private TextView mNad83GeoidHeightInput;


    private TextView mSpcZoneOutput;
    private TextView mSpcHemisphereOutput;
    private TextView mSpcLatbandOutput;
    private TextView mSpcEastingMetersOutput;
    private TextView mSpcEastingFeetOutput;
    private TextView mSpcNorthingMetersOutput;
    private TextView mSpcNorthingFeetOutput;
    private TextView mSpcConvergenceOutput;
    private TextView mSpcScaleFactorOutput;

    private TextView mUtmZoneOutput;
    private TextView mUtmHemisphereOutput;
    private TextView mUtmLatbandOutput;
    private TextView mUtmEastingMetersOutput;
    private TextView mUtmEastingFeetOutput;
    private TextView mUtmNorthingMetersOutput;
    private TextView mUtmNorthingFeetOutput;
    private TextView mUtmConvergenceOutput;
    private TextView mUtmScaleFactorOutput;




    private Prism4DWSG84Coordinate mWSG84Coordinate;

    private double mConvergence;
    private double mScale;






    public MainPrism4DCoordWorkflowFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_coord_workflow_prism4d, container, false);

        wireWidgets(v);

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
            //mLocalizationOutput.setText(

            mMeanWgs84LatitudeInput.setText(
                    "Set GPS is called for the "+(Integer.toString(counter))+"th time.");
        }

    }


    private void updateNmeaUI(Prism4DNmea nmeaData){

        if (nmeaData != null){
            //Which fields have meaning depend upon the type of the sentence
            String type = nmeaData.getNmeaType().toString();
            if (type != null){

                if (type.contains("GGA")) {
                    //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mGpsWgs84TimeOutput.setText(Double.toString(nmeaData.getTime()));

                    mGpsWgs84LatitudeInput.setText(Double.toString(nmeaData.getLatitude()));
                    mGpsWgs84LongitudeInput.setText(Double.toString(nmeaData.getLongitude()));

                    //mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
                    //mHDopOutput.setText(Double.toString(nmeaData.getHdop()));
                    mGpsWgs84ElevationInput.
                            setText(Double.toString(nmeaData.getOrthometricElevation()));
                    mGpsWgs84GeoidHeightInput.setText(Double.toString(nmeaData.getGeoid()));
                    //fixed or quality
                } else if (type.contains("GNS")) {
                    //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mGpsWgs84TimeOutput.setText(Double.toString(nmeaData.getTime()));

                    mGpsWgs84LatitudeInput.setText(Double.toString(nmeaData.getLatitude()));
                    mGpsWgs84LongitudeInput.setText(Double.toString(nmeaData.getLongitude()));
                    //mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
                    //mHDopOutput.setText(Double.toString(nmeaData.getHdop()));
                    mGpsWgs84ElevationInput.
                            setText(Double.toString(nmeaData.getOrthometricElevation()));
                    mGpsWgs84GeoidHeightInput.setText(Double.toString(nmeaData.getGeoid()));
                   //fixed or quality
                } else if (type.contains("GGL")) {
                    //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mGpsWgs84TimeOutput.setText(Double.toString(nmeaData.getTime()));

                    mGpsWgs84LatitudeInput.setText(Double.toString(nmeaData.getLatitude()));
                    mGpsWgs84LongitudeInput.setText(Double.toString(nmeaData.getLongitude()));
                } else if (type.contains("RMA")) {
                    //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());

                    mGpsWgs84LatitudeInput.setText(Double.toString(nmeaData.getLatitude()));
                    mGpsWgs84LongitudeInput.setText(Double.toString(nmeaData.getLongitude()));
                } else if (type.contains("RMC")) {
                    // mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    mGpsWgs84TimeOutput.setText(Double.toString(nmeaData.getTime()));

                    mGpsWgs84LatitudeInput.setText(Double.toString(nmeaData.getLatitude()));
                    mGpsWgs84LongitudeInput.setText(Double.toString(nmeaData.getLongitude()));
                }
           /****
                else if (type.contains("GSV")) {
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

  *********************/
            } else {
                //there was an exception processing the NMEA Sentence
                Toast toast = Toast.makeText(getActivity(), "Null type found", Toast.LENGTH_SHORT);
                toast.show();
            }

        } else {
            //there was an exception processing the NMEA Sentence
            Toast toast = Toast.makeText(getActivity(), "Null NMEA found", Toast.LENGTH_SHORT);
            toast.show();
        }

    }




    private void wireWidgets(View v) {
        //Wire up the UI widgets so they can handle events later
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields

        //Time
        mGpsWgs84TimeOutput   = (TextView) v.findViewById(R.id.gpsWgs84TimeOutput);
        //GPS Latitude
        mGpsWgs84LatitudeInput   = (TextView) v.findViewById(R.id.gpsWgs84LatitudeInput);
        mGpsWgs84LatDegreesInput = (TextView) v.findViewById(R.id.gpsWgs84LatDegreesInput);
        mGpsWgs84LatMinutesInput = (TextView) v.findViewById(R.id.gpsWgs84LatMinutesInput);
        mGpsWgs84LatSecondsInput = (TextView) v.findViewById(R.id.gpsWgs84LatSecondsInput);

        //GPS Longitude
        mGpsWgs84LongitudeInput   = (TextView) v.findViewById(R.id.gpsWgs84LongitudeInput);
        mGpsWgs84LongDegreesInput = (TextView) v.findViewById(R.id.gpsWgs84LongDegreesInput);
        mGpsWgs84LongMinutesInput = (TextView) v.findViewById(R.id.gpsWgs84LongMinutesInput);
        mGpsWgs84LongSecondsInput = (TextView) v.findViewById(R.id.gpsWgs84LongSecondsInput);

        //Elevation
        mGpsWgs84ElevationInput = (TextView) v.findViewById(R.id.gpsWgs84ElevationInput);
        mGpsWgs84GeoidHeightInput = (TextView) v.findViewById(R.id.gpsWgs84GeoidHeightInput);

        //Mean Latitude
        mMeanWgs84LatitudeInput   = (TextView) v.findViewById(R.id.meanWgs84LatitudeInput);
        mMeanWgs84LatDegreesInput = (TextView) v.findViewById(R.id.meanWgs84LatDegreesInput);
        mMeanWgs84LatMinutesInput = (TextView) v.findViewById(R.id.meanWgs84LatMinutesInput);
        mMeanWgs84LatSecondsInput = (TextView) v.findViewById(R.id.meanWgs84LatSecondsInput);

        //Mean Longitude
        mMeanWgs84LongitudeInput   = (TextView) v.findViewById(R.id.meanWgs84LongitudeInput);
        mMeanWgs84LongDegreesInput = (TextView) v.findViewById(R.id.meanWgs84LongDegreesInput);
        mMeanWgs84LongMinutesInput = (TextView) v.findViewById(R.id.meanWgs84LongMinutesInput);
        mMeanWgs84LongSecondsInput = (TextView) v.findViewById(R.id.meanWgs84LongSecondsInput);

        //Elevation
        mMeanWgs84ElevationInput = (TextView) v.findViewById(R.id.meanWgs84ElevationInput);
        mMeanWgs84GeoidHeightInput = (TextView) v.findViewById(R.id.meanWgs84GeoidHeightInput);


        //NAD83 Latitude
        mNad83LatitudeInput   = (TextView) v.findViewById(R.id.nad83LatitudeInput);
        mNad83LatDegreesInput = (TextView) v.findViewById(R.id.nad83LatDegreesInput);
        mNad83LatMinutesInput = (TextView) v.findViewById(R.id.nad83LatMinutesInput);
        mNad83LatSecondsInput = (TextView) v.findViewById(R.id.nad83LatSecondsInput);

        //NAD83 Longitude
        mNad83LongitudeInput   = (TextView) v.findViewById(R.id.nad83LongitudeInput);
        mNad83LongDegreesInput = (TextView) v.findViewById(R.id.nad83LongDegreesInput);
        mNad83LongMinutesInput = (TextView) v.findViewById(R.id.nad83LongMinutesInput);
        mNad83LongSecondsInput = (TextView) v.findViewById(R.id.nad83LongSecondsInput);

        //Elevation
        mNad83ElevationInput   = (TextView) v.findViewById(R.id.nad83ElevationInput);
        mNad83GeoidHeightInput = (TextView) v.findViewById(R.id.nad83GeoidHeightInput);

        //SPC
        mSpcZoneOutput           =  (TextView) v.findViewById(R.id.spcZoneOutput);
        mSpcLatbandOutput        =  (TextView) v.findViewById(R.id.spcLatbandOutput);
        mSpcHemisphereOutput     =  (TextView) v.findViewById(R.id.spcHemisphereOutput);
        mSpcEastingMetersOutput  =  (TextView) v.findViewById(R.id.spcEastingMetersOutput);
        mSpcNorthingMetersOutput =  (TextView) v.findViewById(R.id.spcNorthingMetersOutput);
        mSpcEastingFeetOutput    =  (TextView) v.findViewById(R.id.spcEastingFeetOutput);
        mSpcNorthingFeetOutput   =  (TextView) v.findViewById(R.id.spcNorthingFeetOutput);

        mSpcConvergenceOutput    =  (TextView) v.findViewById(R.id.spcConvergenceOutput);
        mSpcScaleFactorOutput    =  (TextView) v.findViewById(R.id.spcScaleFactorOutput);

        //UTM
        mUtmZoneOutput           =  (TextView) v.findViewById(R.id.utmZoneOutput);
        mUtmLatbandOutput        =  (TextView) v.findViewById(R.id.utmLatbandOutput);
        mUtmHemisphereOutput     =  (TextView) v.findViewById(R.id.utmHemisphereOutput);
        mUtmEastingMetersOutput  =  (TextView) v.findViewById(R.id.utmEastingMetersOutput);
        mUtmNorthingMetersOutput =  (TextView) v.findViewById(R.id.utmNorthingMetersOutput);
        mUtmEastingFeetOutput    =  (TextView) v.findViewById(R.id.utmEastingFeetOutput);
        mUtmNorthingFeetOutput   =  (TextView) v.findViewById(R.id.utmNorthingFeetOutput);

        mUtmConvergenceOutput    =  (TextView) v.findViewById(R.id.utmConvergence);
        mUtmScaleFactorOutput    =  (TextView) v.findViewById(R.id.utmScaleFactor);


        //Start GPS Button
        mStartGpsButton = (Button) v.findViewById(R.id.startGpsButton);
        mStartGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Toast.makeText(getActivity(),
                        R.string.start_gps_button_label,
                        Toast.LENGTH_SHORT).show();

            }//End on Click
        });

        //Stop GPS Button
        mStopGpsButton = (Button) v.findViewById(R.id.stopGPSButton);
        mStopGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Toast.makeText(getActivity(),
                        R.string.stop_gps_button_label,
                        Toast.LENGTH_SHORT).show();

            }//End on Click
        });

        //Start Mean Button
        mStartMeanButton = (Button) v.findViewById(R.id.startMeanButton);
        mStartMeanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Toast.makeText(getActivity(),
                        R.string.start_mean_button_label,
                        Toast.LENGTH_SHORT).show();

            }//End on Click
        });

        //Stop Mean Button
        mStopMeanButton = (Button) v.findViewById(R.id.stopMeanButton);
        mStopMeanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Toast.makeText(getActivity(),
                        R.string.stop_mean_button_label,
                        Toast.LENGTH_SHORT).show();

            }//End on Click
        });





        //Conversion Button
        mConvertButton = (Button) v.findViewById(R.id.convertButton);
        mConvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //performConversion();

                Toast.makeText(getActivity(),
                        R.string.conversion_stub,
                        Toast.LENGTH_SHORT).show();

            }//End on Click
        });

        //Clear Form Button
        mClearButton = (Button) v.findViewById(R.id.clearFormButton);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                clearForm();
            }
        });



    }

    private boolean convertInputs() {
        mWSG84Coordinate = new Prism4DWSG84Coordinate(mGpsWgs84LatitudeInput.getText(),
                                                      mGpsWgs84LongitudeInput.getText());
        if (!mWSG84Coordinate.isValidCoordinate()) {
            mWSG84Coordinate = new Prism4DWSG84Coordinate(
                    mGpsWgs84LatDegreesInput.getText(),
                    mGpsWgs84LatMinutesInput.getText(),
                    mGpsWgs84LatSecondsInput.getText(),
                    mGpsWgs84LongDegreesInput.getText(),
                    mGpsWgs84LongMinutesInput.getText(),
                    mGpsWgs84LongSecondsInput.getText());
            if (!mWSG84Coordinate.isValidCoordinate()){
                Toast.makeText(getActivity(),
                        R.string.coordinate_try_again,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //display the coordinate values in the UI
        mGpsWgs84LatitudeInput.setText(String.valueOf(mWSG84Coordinate.getLatitude()));
        mGpsWgs84LatDegreesInput   .setText(String.valueOf(mWSG84Coordinate.getLatitudeDegree()));
        mGpsWgs84LatMinutesInput   .setText(String.valueOf(mWSG84Coordinate.getLatitudeMinute()));
        mGpsWgs84LatSecondsInput   .setText(String.valueOf(mWSG84Coordinate.getLatitudeSecond()));

        mGpsWgs84LongitudeInput.setText(String.valueOf(mWSG84Coordinate.getLongitude()));
        mGpsWgs84LongDegreesInput   .setText(String.valueOf(mWSG84Coordinate.getLongitudeDegree()));
        mGpsWgs84LongMinutesInput   .setText(String.valueOf(mWSG84Coordinate.getLongitudeMinute()));
        mGpsWgs84LongSecondsInput   .setText(String.valueOf(mWSG84Coordinate.getLongitudeSecond()));

        setLatColor();
        setLongColor();

        return true;

    }

    private void performConversion() {


        /*
         * Compare three conversion routines:
         * 1) WGS84 to NAD83
         * 2) NAD86 to State Plane Coordinates
         * 3) Prism4D developed from scratch, based on Karney (2010)
         *
         *
         *
         */

        //"Legal ranges: latitude [-90,90], longitude [-180,180).");
        boolean inputsValid = convertInputs() ;

        //only attempt the conversions if the inputs are valid

        if (inputsValid){
            //Create the UTM coordinate based on the WSG coordinate from the user
            // The Prism4D conversion
            // algorithm based on Kearny (2010)
            // supposed nanometer accuracy

            try{
                Prism4DUTM utmCoordinate = new Prism4DUTM(mWSG84Coordinate);

                //Also output the result in separate fields
                mUtmZoneOutput          .setText(String.valueOf(utmCoordinate.getZone()));
                mUtmHemisphereOutput    .setText(String.valueOf(utmCoordinate.getHemisphere()));
                mUtmLatbandOutput       .setText(String.valueOf(utmCoordinate.getLatBand()));
                mUtmEastingMetersOutput .setText(String.valueOf(utmCoordinate.getEasting()));
                mUtmNorthingMetersOutput.setText(String.valueOf(utmCoordinate.getNorthing()));

                //convert meters to feet
                double temp =   Prism4DWSG84Coordinate.convertMetersToFeet(utmCoordinate.getEasting());
                //and round to a reasonable precision
                BigDecimal bdTemp = new BigDecimal(temp).setScale(6, RoundingMode.HALF_UP);
                temp = bdTemp.doubleValue();
                mUtmEastingFeetOutput.setText(String.valueOf(temp));

                temp = Prism4DWSG84Coordinate.convertMetersToFeet(utmCoordinate.getNorthing());
                bdTemp = new BigDecimal(temp).setScale(6, RoundingMode.HALF_UP);
                temp = bdTemp.doubleValue();

                mUtmNorthingFeetOutput.setText(String.valueOf(temp ));

            } catch (IllegalArgumentException exc) {
                //input parameters were not within range
                mGpsWgs84LatitudeInput.setText(R.string.input_wrong_range_error);
                mGpsWgs84LongitudeInput.setText(R.string.input_wrong_range_error);
                inputsValid = false;
            }
        }

    }




    private void clearForm() {

        mGpsWgs84LatitudeInput.setText("");
        mGpsWgs84LatDegreesInput   .setText("");
        mGpsWgs84LatMinutesInput   .setText("");
        mGpsWgs84LatSecondsInput   .setText("");

        mGpsWgs84LongitudeInput.setText("");
        mGpsWgs84LongDegreesInput   .setText("");
        mGpsWgs84LongMinutesInput   .setText("");
        mGpsWgs84LongSecondsInput   .setText("");

        mUtmZoneOutput.          setText(R.string.utm_zone_label);
        mUtmLatbandOutput.       setText(R.string.utm_latband_label);
        mUtmHemisphereOutput.    setText(R.string.utm_hemisphere_label);
        mUtmEastingMetersOutput. setText(R.string.utm_easting_label);
        mUtmNorthingMetersOutput.setText(R.string.utm_northing_label);
        mUtmEastingFeetOutput.   setText(R.string.utm_easting_label);
        mUtmNorthingFeetOutput.  setText(R.string.utm_northing_label);
        setLatColorPos();
        setLongColorPos();
    }

    private void setLatColor(){
        if (mWSG84Coordinate.getLatitude() >= 0.0) {
            setLatColorPos();

        } else {
            setLatColorNeg();
        }
    }

    private void setLongColor(){
        if (mWSG84Coordinate.getLongitude() >= 0.0) {
            setLongColorPos();

        } else {
            setLongColorNeg();

        }
    }

    private void setLatColorNeg(){

        mGpsWgs84LatitudeInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mGpsWgs84LatDegreesInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mGpsWgs84LatMinutesInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mGpsWgs84LatSecondsInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));

    }

    private void setLatColorPos(){

        mGpsWgs84LatitudeInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mGpsWgs84LatDegreesInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mGpsWgs84LatMinutesInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mGpsWgs84LatSecondsInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));

    }

    private void setLongColorNeg(){

        mGpsWgs84LongitudeInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mGpsWgs84LongDegreesInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mGpsWgs84LongMinutesInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mGpsWgs84LongSecondsInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));

    }

    private void setLongColorPos(){

        mGpsWgs84LongitudeInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mGpsWgs84LongDegreesInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mGpsWgs84LongMinutesInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mGpsWgs84LongSecondsInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));

    }

}


