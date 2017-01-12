package com.asc.msigeosystems.prism4d;

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

import com.asc.msigeosystems.prism4dmockup.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * The Collect Fragment is the UI
 * when the workflow from WGS84 GPS to NAD83 to UTM/State Plane Coordinates
 * Created by Elisabeth Huhn on 6/15/2016.
 */
public class MainPrism4DCoordWorkflowFragment extends Fragment implements GpsStatus.Listener, LocationListener, GpsStatus.NmeaListener {

    private static Prism4DNmeaParser mNmeaParser = new Prism4DNmeaParser();
    private LocationManager          mLocationManager;
    private Prism4DNmea              mNmeaData; //latest nmea sentence received
    private GpsStatus                mGpsStatus = null;
    private Location                 mCurLocation;

    //Data that must survive a reconfigure
            int     counter = 0;

    private boolean isMeanInProgress   = false;
    private boolean isFirstPointInMean = false;
    private boolean isLastPointInMean  = false;
    private boolean isGpsOn            = true;

    private Prism4DCoordinateWGS84 mMeanCoordinateWGS84;

    private List<Prism4DCoordinateWGS84> mMeanWgs84List;



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

    private TextView mGpsWgs84ElevationMetersInput;
    private TextView mGpsWgs84GeoidHeightMetersInput;
    private TextView mGpsWgs84ElevationFeetInput;
    private TextView mGpsWgs84GeoidHeightFeetInput;


    private TextView mMeanWgs84StartTimeOutput;
    private TextView mMeanWgs84EndTimeOutput;
    private TextView mMeanWgs84PointsInMeanOutput;

    private TextView mMeanWgs84LatitudeInput;
    private TextView mMeanWgs84LatDegreesInput;
    private TextView mMeanWgs84LatMinutesInput;
    private TextView mMeanWgs84LatSecondsInput;

    private TextView mMeanWgs84LatitudeInput2;
    private TextView mMeanWgs84LatDegreesInput2;
    private TextView mMeanWgs84LatMinutesInput2;
    private TextView mMeanWgs84LatSecondsInput2;

    private TextView mMeanWgs84LongitudeInput;
    private TextView mMeanWgs84LongDegreesInput;
    private TextView mMeanWgs84LongMinutesInput;
    private TextView mMeanWgs84LongSecondsInput;

    private TextView mMeanWgs84ElevationMetersInput;
    private TextView mMeanWgs84ElevationFeetInput;
    private TextView mMeanWgs84GeoidHeightMetersInput;
    private TextView mMeanWgs84GeoidHeightFeetInput;

    private TextView mMeanWgs84LatSigmaOutput;
    private TextView mMeanWgs84LongSigmaOutput;
    private TextView mMeanWgs84ElevSigmaOutput;

    private TextView mMeanWgs84LatSigmaOutput2;
    private TextView mMeanWgs84LongSigmaOutput2;
    private TextView mMeanWgs84ElevSigmaOutput2;



    private TextView mNad83LatitudeInput;
    private TextView mNad83LatDegreesInput;
    private TextView mNad83LatMinutesInput;
    private TextView mNad83LatSecondsInput;

    private TextView mNad83LongitudeInput;
    private TextView mNad83LongDegreesInput;
    private TextView mNad83LongMinutesInput;
    private TextView mNad83LongSecondsInput;

    private TextView mNad83ElevationMetersInput;
    private TextView mNad83ElevationFeetInput;
    private TextView mNad83GeoidHeightMetersInput;
    private TextView mNad83GeoidHeightFeetInput;



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




    private Prism4DCoordinateWGS84 mWSG84Coordinate;

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

        startGps();
        setSubtitle();
    }

    private void setSubtitle() {
        ((MainPrism4DActivity) getActivity()).switchSubtitle(R.string.subtitle_workflow);
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
       stopGps();
    }

    private void startGps(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                         != PackageManager.PERMISSION_GRANTED){return;}

        //ask the Location Manager to start sending us updates
        mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        //mLocationManager.addGpsStatusListener(this);
        mLocationManager.addNmeaListener(this);

        setGpsStatus();
    }

    private void stopGps() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
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
                Prism4DNmeaManager nmeaManager = Prism4DNmeaManager.getInstance();
                nmeaManager.add(mNmeaData);
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

            mNad83LatitudeInput.setText(
                    "Set GPS is called for the "+(Integer.toString(counter))+"th time.");
        }

    }


    private void updateNmeaUI(Prism4DNmea nmeaData){

        if (nmeaData != null){
            //Which fields have meaning depend upon the type of the sentence
            String type = nmeaData.getNmeaType().toString();
            if (!(type.isEmpty())){
                Prism4DCoordinateWGS84 coordinateWGS84;

                if ((type.contains("GGA")) || (type.contains("GNS"))) {
                    coordinateWGS84 = new Prism4DCoordinateWGS84(nmeaData.getLatitude(),
                                                                 nmeaData.getLongitude());
                    if (coordinateWGS84.isValidCoordinate()){
                        //create the WGS coordinate with NMEA data
                        // TODO: 11/26/2016  time attribute is broken
                        //NMEA time is HHMMSS
                        //Coordinate time is milliseconds since 1970
                        //coordinateWGS84.setTime(nmeaData.getTime());
                        coordinateWGS84.setElevation(nmeaData.getOrthometricElevation());
                        coordinateWGS84.setGeoid(nmeaData.getGeoid());
                        if (isMeanInProgress){
                            mMeanWgs84List.add(coordinateWGS84);
                            //calculate mean, and update screen
                            updateMeanWGS();
                            updateMeanedWGS();
                            if (isFirstPointInMean){
                                clearMeanUI();
                                mMeanWgs84StartTimeOutput.setText(
                                        //String.valueOf(coordinateWGS84.getTime()));
                                        String.valueOf(nmeaData.getTime()));
                                isFirstPointInMean = false;
                            }
                            if (isLastPointInMean){
                                mMeanWgs84EndTimeOutput.setText(
                                        //todo fix time
                                        //String.valueOf(coordinateWGS84.getTime()));
                                        String.valueOf(nmeaData.getTime()));
                                isMeanInProgress = false;



                                //with an abundance of caution, set these too
                                isLastPointInMean = false;
                                isFirstPointInMean = false;

                                //then store the readings to permanent storage
                                storeRawReadings();
                            }
                        }
                        //update the screen with the latest NMEA update
                        updateUIWithNmea(coordinateWGS84, nmeaData);
                    }
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
                    mPDopOutput.setText(doubleToUI(nmeaData.getPdop()));
                    mHDopOutput.setText(doubleToUI(nmeaData.getHdop()));
                    mVDopOutput.setText(doubleToUI(nmeaData.getVdop()));
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


    private void updateUIWithNmea(Prism4DCoordinateWGS84 coordinateWGS84, Prism4DNmea nmeaData){
        mGpsWgs84TimeOutput.
                //todo fix time between nmea and coordinate WGS84
                //setText(Double.toString(coordinateWGS84.getTime()));
                        setText(Double.toString(nmeaData.getTime()));

        mGpsWgs84LatitudeInput.
                setText(doubleToUI(coordinateWGS84.getLatitude()));
        mGpsWgs84LatDegreesInput.
                setText(doubleToUI(coordinateWGS84.getLatitudeDegree()));
        mGpsWgs84LatMinutesInput.
                setText(doubleToUI(coordinateWGS84.getLatitudeMinute()));
        mGpsWgs84LatSecondsInput.
                setText(doubleToUI(coordinateWGS84.getLatitudeSecond()));

        mGpsWgs84LongitudeInput.
                setText(doubleToUI(coordinateWGS84.getLongitude()));
        mGpsWgs84LongDegreesInput.
                setText(doubleToUI(coordinateWGS84.getLongitudeDegree()));
        mGpsWgs84LongMinutesInput.
                setText(doubleToUI(coordinateWGS84.getLongitudeMinute()));
        mGpsWgs84LongSecondsInput.
                setText(doubleToUI(coordinateWGS84.getLongitudeSecond()));

        mGpsWgs84ElevationMetersInput.
                setText(doubleToUI(coordinateWGS84.getElevation()));
        mGpsWgs84GeoidHeightMetersInput.
                setText(doubleToUI(coordinateWGS84.getGeoid()));
        mGpsWgs84ElevationFeetInput.
                setText(doubleToUI(coordinateWGS84.getElevationFeet()));
        mGpsWgs84GeoidHeightFeetInput.
                setText(doubleToUI(coordinateWGS84.getGeoidFeet()));
        //fixed or quality

    }

    private void updateMeanWGS(){
        int size= mMeanWgs84List.size();


        double meanLat = 0.;
        double meanLatDeg = 0.;
        double meanLatMin = 0.;
        double meanLatSec = 0.;

        double meanLong = 0.;
        double meanLongDeg = 0.;
        double meanLongMin = 0.;
        double meanLongSec = 0.;

        double meanEle = 0.;
        double meanGeoid = 0.;

        double r = 0;

        double rLat = 0.;
        double rLatDeg = 0.;
        double rLatMin = 0.;
        double rLatSec = 0.;

        double rLng = 0.;
        double rLngDeg = 0.;
        double rLngMin = 0.;
        double rLngSec = 0.;

        double rEle = 0.;
        double rGeoid = 0.;

        double sigmaLat = 0.;
        /******
        double sigmaLatDeg = 0.;
        double sigmaLatMin = 0.;
        double sigmaLatSec = 0.;
         ***/

        double sigmaLong = 0.;
        /******
        double sigmaLongDeg = 0.;
        double sigmaLongMin = 0.;
        double sigmaLongSec = 0.;
         ****/

        double sigmaEle = 0.;
        double sigmaGeoid = 0.;

        //assure that we have enough readings to calculate mean
        if (size > 1){
            //calculate the mean
            for (int i = 0; i<size; i++){
                meanLat    = meanLat    + mMeanWgs84List.get(i).getLatitude();
                meanLatDeg = meanLatDeg + mMeanWgs84List.get(i).getLatitudeDegree();
                meanLatMin = meanLatMin + mMeanWgs84List.get(i).getLatitudeMinute();
                meanLatSec = meanLatSec + mMeanWgs84List.get(i).getLatitudeSecond();

                meanLong    = meanLong    + mMeanWgs84List.get(i).getLongitude();
                meanLongDeg = meanLongDeg + mMeanWgs84List.get(i).getLongitudeDegree();
                meanLongMin = meanLongMin + mMeanWgs84List.get(i).getLongitudeMinute();
                meanLongSec = meanLongSec + mMeanWgs84List.get(i).getLongitudeSecond();

                meanEle   = meanEle   + mMeanWgs84List.get(i).getElevation();
                meanGeoid = meanGeoid + mMeanWgs84List.get(i).getGeoid();
            }
            double sizeD = (double)size;

            meanLat    = meanLat / sizeD;
            meanLatDeg = meanLatDeg / sizeD;
            meanLatMin = meanLatMin / sizeD;
            meanLatSec = meanLatSec / sizeD;

            meanLong    = meanLong / sizeD;
            meanLongDeg = meanLongDeg / sizeD;
            meanLongMin = meanLongMin / sizeD;
            meanLongSec = meanLongSec / sizeD;

            meanEle   = meanEle / sizeD;
            meanGeoid = meanGeoid / sizeD;

            //calculate the variance of the squared residuals
            for (int i = 0; i<size; i++){

                rLat    = rLat    +
                        ((meanLat - mMeanWgs84List.get(i).getLatitude())*
                         (meanLat - mMeanWgs84List.get(i).getLatitude()));
                rLatDeg = rLatDeg +
                        ((meanLatDeg - mMeanWgs84List.get(i).getLatitudeDegree()) *
                         (meanLatDeg - mMeanWgs84List.get(i).getLatitudeDegree()));
                rLatMin = rLatMin +
                        ((meanLatMin - mMeanWgs84List.get(i).getLatitudeMinute()) *
                         (meanLatMin - mMeanWgs84List.get(i).getLatitudeMinute()));
                rLatSec = rLatSec +
                        ((meanLatSec - mMeanWgs84List.get(i).getLatitudeSecond()) *
                         (meanLatSec - mMeanWgs84List.get(i).getLatitudeSecond()));

                rLng    = rLng    + (
                        (meanLong    - mMeanWgs84List.get(i).getLongitude())*
                        (meanLong    - mMeanWgs84List.get(i).getLongitude()));
                rLngDeg = rLngDeg +
                        ((meanLongDeg - mMeanWgs84List.get(i).getLongitudeDegree())*
                         (meanLongDeg - mMeanWgs84List.get(i).getLongitudeDegree()));
                rLngMin = rLngMin +
                        ((meanLongMin - mMeanWgs84List.get(i).getLongitudeMinute())*
                         (meanLongMin - mMeanWgs84List.get(i).getLongitudeMinute()));
                rLngSec = rLngSec +
                        ((meanLongSec - mMeanWgs84List.get(i).getLongitudeSecond())*
                         (meanLongSec - mMeanWgs84List.get(i).getLongitudeSecond()));

                rEle = rEle +
                        ((meanEle - mMeanWgs84List.get(i).getElevation())*
                         (meanEle - mMeanWgs84List.get(i).getElevation()));
                rGeoid = rGeoid +
                        ((meanGeoid - mMeanWgs84List.get(i).getGeoid())*
                         (meanGeoid - mMeanWgs84List.get(i).getGeoid()));
            }

            //Treat readings as sample of a larger population
            //so take sample mean (i.e. divide by size - 1
            sizeD = sizeD - 1.0;
            sigmaLat    = Math.sqrt(rLat     / sizeD);
            /***
            sigmaLatDeg = Math.sqrt(rLatDeg  / sizeD);
            sigmaLatMin = Math.sqrt(rLatMin  / sizeD);
            sigmaLatSec = Math.sqrt(rLatSec  / sizeD);
             ***/

            sigmaLong    = Math.sqrt(rLng    / sizeD);
            /***
            sigmaLongDeg = Math.sqrt(rLngDeg / sizeD);
            sigmaLongMin = Math.sqrt(rLngMin / sizeD);
            sigmaLongSec = Math.sqrt(rLngSec / sizeD);
             ***/

            sigmaEle     = Math.sqrt(rEle     / sizeD);
            sigmaGeoid   = Math.sqrt(rGeoid   / sizeD);



            //show the mean and standard deviation on the screen
            mMeanWgs84LatitudeInput  .setText(doubleToUI(meanLat));
            mMeanWgs84LatDegreesInput.setText(doubleToUI(meanLatDeg));
            mMeanWgs84LatMinutesInput.setText(doubleToUI(meanLatMin));
            mMeanWgs84LatSecondsInput.setText(doubleToUI(meanLatSec));

            mMeanWgs84LongitudeInput  .setText(doubleToUI(meanLong));
            mMeanWgs84LongDegreesInput.setText(doubleToUI(meanLongDeg));
            mMeanWgs84LongMinutesInput.setText(doubleToUI(meanLongMin));
            mMeanWgs84LongSecondsInput.setText(doubleToUI(meanLongSec));

            mMeanWgs84ElevationMetersInput.setText(doubleToUI(meanEle));
            double feetReading = Prism4DConstantsAndUtilities.convertMetersToFeet(truncatePrecision(meanEle));
            mMeanWgs84ElevationFeetInput.setText(doubleToUI(feetReading));
            mMeanWgs84GeoidHeightMetersInput.setText(doubleToUI(meanGeoid));
            feetReading = Prism4DConstantsAndUtilities.convertMetersToFeet(truncatePrecision(meanGeoid));
            mMeanWgs84GeoidHeightFeetInput.setText(doubleToUI(feetReading));

            mMeanWgs84PointsInMeanOutput.setText(String.valueOf(size));

            mMeanWgs84LatSigmaOutput.setText(doubleToUI(sigmaLat));
            mMeanWgs84LongSigmaOutput.setText(doubleToUI(sigmaLong));
            mMeanWgs84ElevSigmaOutput.setText(doubleToUI(sigmaEle));

            if (isLastPointInMean){
                //create the coordinate object with the mean
                mMeanCoordinateWGS84 = new Prism4DCoordinateWGS84(meanLat, meanLong);
                mMeanCoordinateWGS84.setElevation(meanEle);
                mMeanCoordinateWGS84.setGeoid(meanGeoid);
            }

        }
    }

    private void updateMeanedWGS(){
        int size= mMeanWgs84List.size();
        Prism4DCoordinateMean meanCoordinate     = new Prism4DCoordinateMean();
        Prism4DCoordinateMean residuals          = new Prism4DCoordinateMean();
        Prism4DCoordinateMean sigma              = new Prism4DCoordinateMean();
        double tempMeanD;
        int    meanTempI;

        //assure that we have enough readings to calculate mean
        if (size > 1){
            //calculate the mean
            //Step one - Sum of each member in the list
            for (int i = 0; i<size; i++){
                tempMeanD = meanCoordinate.getLatitude() + mMeanWgs84List.get(i).getLatitude();
                meanCoordinate.setLatitude(tempMeanD);
                /*
                meanTempI = meanCoordinate.getLatitudeDegree() + mMeanWgs84List.get(i).getLatitudeDegree();
                meanCoordinate.setLatitudeDegree(meanTempI);
                meanTempI = meanCoordinate.getLatitudeMinute() + mMeanWgs84List.get(i).getLatitudeMinute();
                meanCoordinate.setLatitudeMinute(meanTempI);
                tempMeanD = meanCoordinate.getLatitudeSecond() + mMeanWgs84List.get(i).getLatitudeSecond();
                meanCoordinate.setLatitudeSecond(tempMeanD);
*/
                tempMeanD = meanCoordinate.getLongitude() + mMeanWgs84List.get(i).getLongitude();
                meanCoordinate.setLongitude(tempMeanD);
    /*
                meanTempI = meanCoordinate.getLongitudeDegree() + mMeanWgs84List.get(i).getLongitudeDegree();
                meanCoordinate.setLongitudeDegree(meanTempI);
                meanTempI = meanCoordinate.getLongitudeMinute() + mMeanWgs84List.get(i).getLongitudeMinute();
                meanCoordinate.setLongitudeMinute(meanTempI);
                tempMeanD = meanCoordinate.getLongitudeSecond() + mMeanWgs84List.get(i).getLongitudeSecond();
                meanCoordinate.setLongitudeSecond(tempMeanD);
*/
                tempMeanD = meanCoordinate.getElevation() + mMeanWgs84List.get(i).getElevation();
                meanCoordinate.setElevation(tempMeanD);
                tempMeanD = meanCoordinate.getGeoid() + mMeanWgs84List.get(i).getGeoid();
                meanCoordinate.setGeoid(tempMeanD);

            }
            //Step two: devide by the number in the list
            double sizeD = (double)size;

            tempMeanD = meanCoordinate.getLatitude() / sizeD;
            meanCoordinate.setLatitude(tempMeanD);
/*
            meanTempI = meanCoordinate.getLatitudeDegree() / size;
            meanCoordinate.setLatitudeDegree(meanTempI);
            meanTempI = meanCoordinate.getLatitudeMinute() / size;
            meanCoordinate.setLatitudeMinute(meanTempI);
            tempMeanD = meanCoordinate.getLatitudeSecond() / sizeD;
            meanCoordinate.setLatitudeSecond(tempMeanD);
*/

            meanCoordinate.setLongitude((meanCoordinate.getLongitude() / sizeD));
/*
            meanTempI = meanCoordinate.getLongitudeDegree() / size;

            meanCoordinate.setLongitudeDegree(meanTempI);
            meanTempI = meanCoordinate.getLongitudeMinute() / size;
            meanCoordinate.setLongitudeMinute(meanTempI);
            tempMeanD = meanCoordinate.getLongitudeSecond() / sizeD;
            meanCoordinate.setLongitudeSecond(tempMeanD);
*/
            tempMeanD = meanCoordinate.getElevation() / sizeD;
            meanCoordinate.setElevation(tempMeanD);
            tempMeanD = meanCoordinate.getGeoid() / sizeD;
            meanCoordinate.setGeoid(tempMeanD);

            //calculate the variance of the squared residuals
            //Step three: sum( (mean - reading) squared  )
            for (int i = 0; i<size; i++){

                tempMeanD = meanCoordinate.getLatitude()      - mMeanWgs84List.get(i).getLatitude();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLatitude      (residuals.getLatitude()+ tempMeanD);
/*
                tempMeanD = meanCoordinate.getLatitudeDegree() - mMeanWgs84List.get(i).getLatitudeDegree();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLatitudeDegree(residuals.getLatitudeDegree() + tempMeanD);


                tempMeanD = meanCoordinate.getLatitudeMinute() - mMeanWgs84List.get(i).getLatitudeMinute();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLatitudeMinute(residuals.getLatitudeMinute()+ tempMeanD);


                tempMeanD = meanCoordinate.getLatitudeSecond() - mMeanWgs84List.get(i).getLatitudeSecond();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLatitudeSecond(residuals.getLatitudeSecond()+ tempMeanD);
*/


                tempMeanD = meanCoordinate.getLongitude() - mMeanWgs84List.get(i).getLongitude();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLongitude(residuals.getLongitude() + tempMeanD);
/*
                tempMeanD = meanCoordinate.getLongitudeDegree() - mMeanWgs84List.get(i).getLongitudeDegree();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLongitudeDegree(residuals.getLongitudeDegree()+ tempMeanD);

                tempMeanD = meanCoordinate.getLongitudeMinute() - mMeanWgs84List.get(i).getLongitudeMinute();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLongitudeMinute(residuals.getLongitudeMinute()+ tempMeanD);

                tempMeanD = meanCoordinate.getLongitudeSecond() - mMeanWgs84List.get(i).getLongitudeSecond();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLongitudeSecond(residuals.getLongitudeSecond()+ tempMeanD);
*/
                tempMeanD = meanCoordinate.getElevation() - mMeanWgs84List.get(i).getElevation();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setElevation(residuals.getElevation() + tempMeanD);


                tempMeanD = meanCoordinate.getGeoid() - mMeanWgs84List.get(i).getGeoid();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setGeoid(residuals.getGeoid()+ tempMeanD);
            }

            //Step 3: residual = sum of [(mean - reading) squared]
            //step 3.5 mean of residuals = sum / # of readings
            //Step 4: sqrt of (mean of the residuals)

            //Treat readings as sample of a larger population
            //so take sample mean (i.e. divide by size - 1
            sizeD = sizeD - 1.0;
            meanCoordinate.setLatitudeStdDev(Math.sqrt(residuals.getLatitude() / sizeD));

            meanCoordinate.setLongitudeStdDev(Math.sqrt(residuals.getLongitude()/ sizeD));

            meanCoordinate.setElevationStdDev(Math.sqrt(residuals.getElevation()/ sizeD));
            //sigma.setGeoid          (Math.sqrt(residuals.getGeoid()           / sizeD));

            updateUIWithMean(meanCoordinate, size);

            if (isLastPointInMean){
                //create the coordinate object with the mean
                mMeanCoordinateWGS84 = new Prism4DCoordinateWGS84(meanCoordinate.getLatitude(),
                                                                  meanCoordinate.getLongitude());
                mMeanCoordinateWGS84.setElevation(meanCoordinate.getElevation());
                mMeanCoordinateWGS84.setGeoid(meanCoordinate.getGeoid());
            }

        }
    }


    private void updateUIWithMean(Prism4DCoordinateMean meanCoordinate,
                                  int                         size){

        //show the mean and standard deviation on the screen
        mMeanWgs84LatitudeInput2  .setText(doubleToUI(meanCoordinate.getLatitude()));
        mMeanWgs84LatDegreesInput2.setText(doubleToUI(meanCoordinate.getLatitudeDegree()));
        mMeanWgs84LatMinutesInput2.setText(doubleToUI(meanCoordinate.getLatitudeMinute()));
        mMeanWgs84LatSecondsInput2.setText(doubleToUI(meanCoordinate.getLatitudeSecond()));
/***
        mMeanWgs84LongitudeInput  .setText(doubleToUI(meanCoordinate.getLongitude()));
        mMeanWgs84LongDegreesInput.setText(doubleToUI(meanCoordinate.getLongitudeDegree()));
        mMeanWgs84LongMinutesInput.setText(doubleToUI(meanCoordinate.getLongitudeMinute()));
        mMeanWgs84LongSecondsInput.setText(doubleToUI(meanCoordinate.getLongitudeSecond()));

        mMeanWgs84ElevationMetersInput.setText(doubleToUI(meanCoordinate.getElevation()));
        double feetReading = Prism4DConstantsAndUtilities.convertMetersToFeet
                                                (truncatePrecision(meanCoordinate.getElevation()));
        mMeanWgs84ElevationFeetInput.setText(doubleToUI(feetReading));
        mMeanWgs84GeoidHeightMetersInput.setText(doubleToUI(meanCoordinate.getGeoid()));
        feetReading = Prism4DConstantsAndUtilities.convertMetersToFeet
                                                    (truncatePrecision(meanCoordinate.getGeoid()));
        mMeanWgs84GeoidHeightFeetInput.setText(doubleToUI(feetReading));

        mMeanWgs84PointsInMeanOutput.setText(String.valueOf(size));
***/
        mMeanWgs84LatSigmaOutput2.setText(doubleToUI(meanCoordinate.getLatitudeStdDev()));
        mMeanWgs84LongSigmaOutput2.setText(doubleToUI(meanCoordinate.getLongitudeStdDev()));
        mMeanWgs84ElevSigmaOutput2.setText(doubleToUI(meanCoordinate.getElevationStdDev()));
    }



    private boolean convertKarney(){
        try{
            //The UTM constructor performs the conversion from WGS84
            Prism4DCoordinateUTM utmCoordinate = new Prism4DCoordinateUTM(mMeanCoordinateWGS84);

            //Also output the result in separate fields
            mUtmZoneOutput        .setText(String.valueOf(utmCoordinate.getZone()));
            mUtmHemisphereOutput  .setText(String.valueOf(utmCoordinate.getHemisphere()));
            mUtmLatbandOutput     .setText(String.valueOf(utmCoordinate.getLatBand()));
            mUtmEastingMetersOutput.setText(String.valueOf(utmCoordinate.getEasting()));
            mUtmNorthingMetersOutput.setText(String.valueOf(utmCoordinate.getNorthing()));
            mUtmEastingFeetOutput .setText(String.valueOf(utmCoordinate.getEastingFeet()));
            mUtmNorthingFeetOutput.setText(String.valueOf(utmCoordinate.getNorthingFeet()));
            mUtmConvergenceOutput .setText(String.valueOf(utmCoordinate.getConvergence()));
            mUtmScaleFactorOutput .setText(String.valueOf(utmCoordinate.getScale()));
            return true;

        } catch (IllegalArgumentException exc) {
            //input parameters were not within range
            mUtmEastingMetersOutput.setText(R.string.input_wrong_range_error);
            mUtmNorthingMetersOutput.setText(R.string.input_wrong_range_error);
            return false;
        }
    }


    private String doubleToUI(double reading){
        return String.valueOf(truncatePrecision(reading));
    }

    //truncate digits of precision
    private double truncatePrecision(double reading) {

        BigDecimal bd = new BigDecimal(reading).
                setScale(Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
        mGpsWgs84ElevationMetersInput   = (TextView) v.findViewById(R.id.gpsWgs84ElevationMetersInput);
        mGpsWgs84GeoidHeightMetersInput = (TextView) v.findViewById(R.id.gpsWgs84GeoidHeightMetersInput);
        mGpsWgs84ElevationFeetInput     = (TextView) v.findViewById(R.id.gpsWgs84ElevationFeetInput);
        mGpsWgs84GeoidHeightFeetInput   = (TextView) v.findViewById(R.id.gpsWgs84GeoidHeightFeetInput);

        //Mean Latitude
        mMeanWgs84LatitudeInput   = (TextView) v.findViewById(R.id.meanWgs84LatitudeInput);
        mMeanWgs84LatDegreesInput = (TextView) v.findViewById(R.id.meanWgs84LatDegreesInput);
        mMeanWgs84LatMinutesInput = (TextView) v.findViewById(R.id.meanWgs84LatMinutesInput);
        mMeanWgs84LatSecondsInput = (TextView) v.findViewById(R.id.meanWgs84LatSecondsInput);
        //Mean Latitude
        mMeanWgs84LatitudeInput2   = (TextView) v.findViewById(R.id.meanWgs84LatitudeInput2);
        mMeanWgs84LatDegreesInput2 = (TextView) v.findViewById(R.id.meanWgs84LatDegreesInput2);
        mMeanWgs84LatMinutesInput2 = (TextView) v.findViewById(R.id.meanWgs84LatMinutesInput2);
        mMeanWgs84LatSecondsInput2 = (TextView) v.findViewById(R.id.meanWgs84LatSecondsInput2);

        //Mean Longitude
        mMeanWgs84LongitudeInput   = (TextView) v.findViewById(R.id.meanWgs84LongitudeInput);
        mMeanWgs84LongDegreesInput = (TextView) v.findViewById(R.id.meanWgs84LongDegreesInput);
        mMeanWgs84LongMinutesInput = (TextView) v.findViewById(R.id.meanWgs84LongMinutesInput);
        mMeanWgs84LongSecondsInput = (TextView) v.findViewById(R.id.meanWgs84LongSecondsInput);

        //Elevation
        mMeanWgs84ElevationMetersInput   = (TextView) v.findViewById(
                                                        R.id.meanWgs84ElevationMetersInput);
        mMeanWgs84ElevationFeetInput     = (TextView) v.findViewById(
                                                        R.id.meanWgs84ElevationFeetInput);
        mMeanWgs84GeoidHeightMetersInput = (TextView) v.findViewById(
                                                        R.id.meanWgs84GeoidHeightMetersInput);
        mMeanWgs84GeoidHeightFeetInput   = (TextView) v.findViewById(
                                                        R.id.meanWgs84GeoidHeightFeetInput);

        //Mean Parameters
        mMeanWgs84StartTimeOutput    = (TextView)v.findViewById(R.id.meanWgs84StartTimeOutput);
        mMeanWgs84EndTimeOutput      = (TextView)v.findViewById(R.id.meanWgs84EndTimeOutput);
        mMeanWgs84PointsInMeanOutput = (TextView)v.findViewById(R.id.meanWgs84PointsInMeanOutput);


        //Mean Standard Deviations
        mMeanWgs84LatSigmaOutput = (TextView)v.findViewById(R.id.meanWgs84LatSigmaOutput);
        mMeanWgs84LongSigmaOutput= (TextView)v.findViewById(R.id.meanWgs84LongSigmaOutput);
        mMeanWgs84ElevSigmaOutput= (TextView)v.findViewById(R.id.meanWgs84ElevSigmaOutput);

        //Mean Standard Deviations
        mMeanWgs84LatSigmaOutput2 = (TextView)v.findViewById(R.id.meanWgs84LatSigmaOutput2);
        mMeanWgs84LongSigmaOutput2= (TextView)v.findViewById(R.id.meanWgs84LongSigmaOutput2);
        mMeanWgs84ElevSigmaOutput2= (TextView)v.findViewById(R.id.meanWgs84ElevSigmaOutput2);


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
        mNad83ElevationMetersInput = (TextView) v.findViewById(R.id.nad83ElevationMetersInput);
        mNad83ElevationFeetInput = (TextView) v.findViewById(R.id.nad83ElevationFeetInput);
        mNad83GeoidHeightMetersInput = (TextView) v.findViewById(R.id.nad83GeoidHeightMetersInput);
        mNad83GeoidHeightFeetInput = (TextView) v.findViewById(R.id.nad83GeoidHeightFeetInput);

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

                startGps();
                isGpsOn = true;

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

                stopGps();
                isGpsOn = false;

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

                //set flags to start taking mean
                mMeanWgs84List = new ArrayList<>();
                isFirstPointInMean = true;
                isMeanInProgress = true;



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
                //set flags that mean is done
                isLastPointInMean = true;

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

                convertKarney();

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

    private void storeRawReadings() {
        //Store off the list of raw gps readings to permanent storage
        //mMeanWgs84List contains the raw gps readings in the form of coordinateWgs84 objects
    }

    private boolean convertInputs() {
        mWSG84Coordinate = new Prism4DCoordinateWGS84(mGpsWgs84LatitudeInput.getText(),
                                                      mGpsWgs84LongitudeInput.getText());
        if (!mWSG84Coordinate.isValidCoordinate()) {
            mWSG84Coordinate = new Prism4DCoordinateWGS84(
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
        mGpsWgs84LatitudeInput.setText(doubleToUI(mWSG84Coordinate.getLatitude()));
        mGpsWgs84LatDegreesInput   .setText(doubleToUI(mWSG84Coordinate.getLatitudeDegree()));
        mGpsWgs84LatMinutesInput   .setText(doubleToUI(mWSG84Coordinate.getLatitudeMinute()));
        mGpsWgs84LatSecondsInput   .setText(doubleToUI(mWSG84Coordinate.getLatitudeSecond()));

        mGpsWgs84LongitudeInput.setText(doubleToUI(mWSG84Coordinate.getLongitude()));
        mGpsWgs84LongDegreesInput   .setText(doubleToUI(mWSG84Coordinate.getLongitudeDegree()));
        mGpsWgs84LongMinutesInput   .setText(doubleToUI(mWSG84Coordinate.getLongitudeMinute()));
        mGpsWgs84LongSecondsInput   .setText(doubleToUI(mWSG84Coordinate.getLongitudeSecond()));

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
                Prism4DCoordinateUTM utmCoordinate = new Prism4DCoordinateUTM(mWSG84Coordinate);

                //Also output the result in separate fields
                mUtmZoneOutput          .setText(doubleToUI(utmCoordinate.getZone()));
                mUtmHemisphereOutput    .setText(doubleToUI(utmCoordinate.getHemisphere()));
                mUtmLatbandOutput       .setText(doubleToUI(utmCoordinate.getLatBand()));
                mUtmEastingMetersOutput .setText(doubleToUI(utmCoordinate.getEasting()));
                mUtmNorthingMetersOutput.setText(doubleToUI(utmCoordinate.getNorthing()));

                mUtmEastingFeetOutput   .setText(doubleToUI(utmCoordinate.getEastingFeet()));
                mUtmNorthingFeetOutput  .setText(doubleToUI(utmCoordinate.getNorthingFeet()));

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

        clearMeanUI();

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

    private void clearMeanUI(){
        mMeanWgs84StartTimeOutput   .setText("");
        mMeanWgs84EndTimeOutput     .setText("");
        mMeanWgs84PointsInMeanOutput.setText("");

        mMeanWgs84LatitudeInput     .setText("");
        mMeanWgs84LatDegreesInput   .setText("");
        mMeanWgs84LatMinutesInput   .setText("");
        mMeanWgs84LatSecondsInput   .setText("");

        mMeanWgs84LongitudeInput     .setText("");
        mMeanWgs84LongDegreesInput   .setText("");
        mMeanWgs84LongMinutesInput   .setText("");
        mMeanWgs84LongSecondsInput   .setText("");

        mMeanWgs84ElevationMetersInput.setText("");
        mMeanWgs84ElevationFeetInput .setText("");
        mMeanWgs84GeoidHeightMetersInput   .setText("");
        mMeanWgs84GeoidHeightFeetInput.setText("");
        mMeanWgs84LatSigmaOutput     .setText("");
        mMeanWgs84LongSigmaOutput    .setText("");

        mMeanWgs84ElevSigmaOutput    .setText("");

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


