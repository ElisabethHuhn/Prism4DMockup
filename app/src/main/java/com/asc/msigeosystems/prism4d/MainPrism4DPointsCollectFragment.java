package com.asc.msigeosystems.prism4d;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.CalendarContract.CalendarCache.URI;


/**
 * The Collect Fragment is the UI
 * when the user is making point measurements in the field
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DPointsCollectFragment extends Fragment implements //OnMapReadyCallback,
                                                                          GpsStatus.Listener,
                                                                          LocationListener,
                                                                          GpsStatus.NmeaListener {

    //DEFINE constants / literals
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int MY_PERMISSIONS_REQUEST_SAVE_PICTURES = 2;



    static final boolean  DEBUG = false;

    private static String TAG = "CollectPointsFragment";
    private static float  markerColorProvisional = BitmapDescriptorFactory.HUE_BLUE;
    private static float  markerColorFinal      = BitmapDescriptorFactory.HUE_RED;

    //return codes from intents
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;

    /*******************************************/
    /****     variables for UI widgets      ****/
    /*******************************************/
    //Buttons to activate UI stuff
    private Button mPictureButton;
    private Button mNotesButton;

    //Widgets dealing with the map
    private Button      mMapsButton;
    private ImageButton mZoomInButton;
    private ImageButton mZoomOutButton;
    private ImageButton mZoomExtButton;
    private TextView    mScaleFactorField;

    //Widgets dealing with location and creation of points
    private Button   mCurrentPositionButton;
    private TextView mPointIDField;
    private TextView mCurrentNorthingPositionField;
    private TextView mCurrentEastingPositionField;
    private TextView mCurrentElevationField;
    private EditText mCurrentFunctionCodeField;
    private EditText mCurrentHeightField;
    private Button   mStorePositionButton;
    private Button   mOffsetPositionButton;
    private Button   mAveragePositionButton;
    private Button   mShowAllPointsButton;


    //footer
    //footer left button
    private Button   mEscButton;
    //footer row 1
    private TextView mCurrentProjectField;
    //footer row 2
    private TextView mModelField;
    private TextView mSnField;
    //footer row 3
    private TextView mHdopField;
    private TextView mHrmsField;
    //footer row 4
    private TextView mVdopField;
    private TextView mVrmsField;
    //footer row 5
    private TextView mPdopField;
    private TextView mTdopField;
    //footer right button
    private Button   mEnterButton;


    /*******************************************/
    /****     variables for processing      ****/
    /*******************************************/

    //Screen Focus
    private double mLatitudeScreenFocus;
    private double mLongitudeScreenFocus;


    //Offset Positions
    // if the isOffsetResetAfterOnePoint flag is true, apply it once, then reset offsets to zero
    // if false, just keep applying the offset
    private boolean isOffsetResetAfterOnePoint = false;
    //if false, the first dialog was cancled
    private boolean isOffsetCancle = true;
    private double mOffsetLatitude   = 0d;
    private double mOffsetLongitude  = 0d;
    private double mOffsetElevation  = 0d;


    //Camera
     String mCurrentPhotoPath = "";

    //Notes
    private CharSequence mPointNotes = "";


    //variables needed to keep track of location
    private LocationManager     mLocationManager;
    private Location            mCurLocation;
    private Prism4DNmea         mNmeaData;     //latest nmea sentence received
    private Prism4DNmeaParser   mNmeaParser = new Prism4DNmeaParser();
    private GpsStatus           mGpsStatus = null;



    //variables for the meaning process
    private boolean isMeanInProgress = false;
    private boolean isFirstPointInMean = false;
    private boolean isLastPointInMean = false;
    private double  mMeanTime;
    private int     mFixedReadings;
    private int     mRawReadings;

    private ArrayList<Prism4DCoordinateWGS84> mMeanCoordinateList;

    //Quality fields to be added to the new point
    private double       mHdop = 0d;
    private double       mVdop = 0d;
    private double       mTdop = 0d;
    private double       mPdop = 0d;
    private double       mHrms = 0d;
    private double       mVrms = 0d;




    //Test Data
    private int                     mTestDataCounter = 0;
    private Prism4DTestLocationData mTestData;
    private int                     mTestDataMax = 13;


    //variables for the map

    private MapView      mMapView;
    private GoogleMap    mMap;
    private float        mMapZoom;
    private LatLng       mLastPoint;
    private int          mPointsPlotted;
    private Marker       mLastMarkerAdded;
    //only used at very beginning of app to mark end of initialization
    private boolean      isMapCentered = false;
    //indicates whether map should be automatically resized when a point is added
    private boolean      isAutoResizeOn = true;

    private PolylineOptions mLineContainer;
    private Polyline     mPointsLine;

    private LatLngBounds.Builder mZoomBuilder;
    private LatLngBounds         mZoomBounds;





    /**********************************************************************/
    /*                Constructor                                         */
    /**********************************************************************/
    public MainPrism4DPointsCollectFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }


    /**********************************************************************/
    /*          Lifecycle Methods                                         */
    /**********************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_points_collect_prism4d, container, false);

        //update the view with the mapsView
       // addMapsView(v);
        //and then initialize it
        initializeMaps(savedInstanceState, v);

        //Wire up the other UI widgets so they can handle events later
        wireWidgets(v);

        Prism4DConstantsAndUtilities constantsAndUtilities = Prism4DConstantsAndUtilities.getInstance();
        CharSequence projectName = constantsAndUtilities.getOpenProject().getProjectName();

        //Inform the user of the name of the open project
        mCurrentProjectField.setText("Current File: "+projectName);

        initializeGPS();

        //Set the titlebar subtitle
        setSubtitle();

        initializeTestData();
        return v;
    }

    //Ask for location events to start
    @Override
    public void onStart(){
        if (mMapView != null){
            mMapView.onStart();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        if (mMapView != null){
            mMapView.onResume();
        }

        super.onResume();

        setSubtitle();

        startGps();
     }


    //Ask for location events to stop
    @Override
    public void onPause() {

        if (mMapView != null){
            mMapView.onPause();
        }
        super.onPause();

        stopGps();
    }

    @Override
    public void onStop(){
        if (mMapView != null){
            mMapView.onStop();
        }
        super.onStop();
    }


    @Override
    public void onDestroy(){
        try {
            if (mMapView != null){
                mMapView.onDestroy();
            }
        } catch (Exception e) {
            Log.e(TAG, getString(R.string.collect_points_destroy_error), e);
        }
        super.onDestroy();

    }

    @Override
    public void onLowMemory(){

        if (mMapView != null){
            mMapView.onLowMemory();
        }
        super.onLowMemory();

    }

/*
    @Override
    public void onSaveInstanceState(Bundle outState){
        if (mMapView != null){
            mMapView.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }
*/
    //********************************************************//
    //***************  GPS Routines    ***********************//
    //********************************************************//

    private void initializeGPS(){

        //Make sure we have the proper GPS permissions before starting
        //If we don't currently have permission, bail
        if ((ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){return;}

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        //but don't turn them on until onResume()
        //mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        //mLocationManager.addGpsStatusListener(this);
        //mLocationManager.addNmeaListener(this);

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

    /*******************************************************
     *
     *
     *OS calls this callback when
     * a change has been detected in GPS satellite status
     *Called to report changes in the GPS status.
     *
     * The parameter event type is one of:
     * o GPS_EVENT_STARTED
     * o GPS_EVENT_STOPPED
     * o GPS_EVENT_FIRST_FIX
     * o GPS_EVENT_SATELLITE_STATUS
     *
     *When this method is called,
     * the client should call getGpsStatus(GpsStatus)
     * to get additional status information.
     */
    @Override
    public void onGpsStatusChanged(int eventType) {
        setGpsStatus();
    }



    //******************************************************************//
    //             Location Listener Callbacks                          //
    //            Called by the OS to handle GPS events                 //
    //******************************************************************//

    /* called when the GPS provider is turned off
    *  (i.e. user turning off the GPS on the phone)
    *  If requestLocationUpdates is called on an already disabled provider,
    *  this method is called immediately.
    */
    @Override
    public void onProviderDisabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }
    }

    /* called when the GPS provider is turned on
    *  (i.e. user turning on the GPS on the phone)
    */
    @Override
    public void onProviderEnabled(String provider) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            setGpsStatus();
        }
    }

    /*Called when the provider status changes.
    *  This method is called when
    *  o a provider is unable to fetch a location or
    *  o if the provider has recently become available
    *     after a period of unavailability.
    */
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
        Prism4DNmea nmeaData;
        //todo maybe need to do something with the timestamp

         try {
            //create an object with all the fields from the string
             nmeaData = mNmeaParser.parse(nmea);
             if (nmeaData == null)return;

             // TODO: 12/20/2016 remove kludge for test data
            if (DEBUG && isMeanInProgress){
                if (nmeaData.getTime() <= 1)return;
                if (mMeanTime <= 1){
                    mMeanTime = nmeaData.getTime();
                } else {
                    //wait at least a second between data points
                    if ((nmeaData.getTime() - mMeanTime) < 1) return;
                    mMeanTime = nmeaData.getTime();
                }
                nmeaData = getNmeaFromTestData();
            }

            //Only returns sentences we might possibly want
            if (nmeaData != null){
                //update the UI
                //strips out all sentences that are NOT locations
                nmeaData = updateNmeaUI(nmeaData);

                //so while the parser recognized it, we still might not be interested
                if (nmeaData != null){
                    //increment the number of raw readings in the mean
                    mRawReadings++;
                    //The reading must be fixed to be used in the meaning
                    if (!nmeaData.isFixed()){
                        mFixedReadings++;
                        return;
                    }
                    if (!isMapCentered){
                        centerMap(nmeaData);
                        isMapCentered = true;
                    }
                    //So now, store the sentence
                    mNmeaData = nmeaData;

                    if (isMeanInProgress){
                        updateMeanWithNmea(mNmeaData);
                    }
                }
            }


        } catch (RuntimeException e){
            //there was an exception processing the NMEA Sentence
            Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
            //throw new RuntimeException(e);
        }
    }


    //******************** Callback Utilities ********************//
    //        Called by OS when a Listener condition is met       //
    //************************************************************//


    //only returns non-null value if it's a sentence we are interested in
    // TODO: 12/24/2016 Need to add in other types of coordinates besides WGS and UTM
    private Prism4DNmea updateNmeaUI(Prism4DNmea nmeaData){

        if (nmeaData == null){
            //there was an exception processing the NMEA Sentence
            Toast toast = Toast.makeText(getActivity(), "Null type found", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }

        //Which fields have meaning depend upon the type of the sentence
        String type = nmeaData.getNmeaType().toString();
        if (type == null)return null;

        //project has to be open
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                Prism4DConstantsAndUtilities.getInstance();
        int openProjectID = constantsAndUtilities.getOpenProjectID();
        if (openProjectID == 0) return null;

        int coordinateType = Prism4DCoordinate.getCoordinateTypeFromProjectID(openProjectID);
        String nLable = "N: ";
        String eLable = "E: ";

        if (coordinateType == Prism4DCoordinate.sLLWidgets) {
            nLable = "Lat: ";
            eLable = "Long: ";
        }

        double nLat, eLong, ele;

        //the primary location sentences
        if ((type.contains("GGA")) || (type.contains("GNS")) ){

            if (coordinateType == Prism4DCoordinate.sLLWidgets){
                nLat =   nmeaData.getLatitude();
                eLong =  nmeaData.getLongitude();
            } else {
                // TODO: 12/7/2016 this conversion assumes wgs and utm. Bad assumption, fix
                Prism4DCoordinateWGS84 wgsCoordinate =
                                    new Prism4DCoordinateWGS84(mNmeaData.getLatitude(),
                                                               mNmeaData.getLongitude());

                //The UTM constructor performs the conversion from WGS84
                Prism4DCoordinateUTM utmCoordinate = new Prism4DCoordinateUTM(wgsCoordinate);
                if (!wgsCoordinate.isValidCoordinate()){
                    Toast.makeText(getActivity(),
                                    R.string.coordinate_not_valid,
                                    Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(getString(R.string.coordinate_not_valid));
                }

                nLat =  utmCoordinate.getNorthing();
                eLong = utmCoordinate.getEasting();
            }


            int precisionDigits = constantsAndUtilities
                                            .getOpenProject().getSettings().getLocationPrecision();

            BigDecimal bdTemp = new BigDecimal(nLat).setScale(precisionDigits,RoundingMode.HALF_UP);
            nLat = bdTemp.doubleValue();

            bdTemp = new BigDecimal(eLong).setScale(precisionDigits,RoundingMode.HALF_UP);
            eLong = bdTemp.doubleValue();

            ele = nmeaData.getOrthometricElevation();
            bdTemp = new BigDecimal(ele).setScale(precisionDigits,RoundingMode.HALF_UP);
            ele = bdTemp.doubleValue();

            mCurrentNorthingPositionField.setText(nLable + Double.toString(nLat));
            mCurrentEastingPositionField .setText(eLable + Double.toString(eLong));

            //mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
            mHdopField                   .setText("HDop: "+Double.toString(nmeaData.getHdop()));
            mHdop = nmeaData.getHdop();
            mCurrentElevationField       .setText("Ele: "+Double.toString(ele));

            return nmeaData;

            //mGeoidOutput.setText(Double.toString(nmeaData.getGeoid()));
            //fixed or quality
        }

        /* else if (type.contains("GNS")) {
           // mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
            //mTimeOutput.setText(Double.toString(nmeaData.getTime()));
            mCurrentNorthingPositionField.setText(Double.toString(nmeaData.getLatitude()));
            mCurrentEastingPositionField.setText(Double.toString(nmeaData.getLongitude()));
            //mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
            mHdopField.setText(Double.toString(nmeaData.getHdop()));
            mHdop = nmeaData.getHdop();
            mCurrentElevationField.
                    setText(Double.toString(nmeaData.getOrthometricElevation()));
            //mGeoidOutput.setText(Double.toString(nmeaData.getGeoid()));
            //fixed or quality
        } else if (type.contains("GGL")) {
            //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
            //mTimeOutput.setText(Double.toString(nmeaData.getTime()));
            mCurrentNorthingPositionField.setText(Double.toString(nmeaData.getLatitude()));
            mCurrentEastingPositionField.setText(Double.toString(nmeaData.getLongitude()));
        } else if (type.contains("RMA")) {
            //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
            mCurrentNorthingPositionField.setText(Double.toString(nmeaData.getLatitude()));
            mCurrentEastingPositionField.setText(Double.toString(nmeaData.getLongitude()));
        } else if (type.contains("RMC")) {
            //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
            //mTimeOutput.setText(Double.toString(nmeaData.getTime()));
            mCurrentNorthingPositionField.setText(Double.toString(nmeaData.getLatitude()));
            mCurrentEastingPositionField.setText(Double.toString(nmeaData.getLongitude()));
        }else if (type.contains("GSV")) {
            //this is better shown graphically
           // mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
           /// mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
            //TODO rest of satellite data
        } */
        else if (type.contains("GSA")) {
            //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
            //mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
            mPdopField.setText("PDop: "+Double.toString(nmeaData.getPdop()));
            mHdopField.setText("HDop: "+Double.toString(nmeaData.getHdop()));
            mVdopField.setText("VDop: "+Double.toString(nmeaData.getVdop()));
            mPdop = nmeaData.getPdop();
            mHdop = nmeaData.getHdop();
            mVdop = nmeaData.getVdop();
            return null;
        }

        return null;

    }

    //adds the current nmea to the meaned collection,
    //adds marker if the first reading in mean
    //creates point if the last reading in mean
    private void updateMeanWithNmea(Prism4DNmea nmeaData){
        //Add to the list of points being meaned.
        Prism4DCoordinateWGS84 wgsCoordinate = getWgsFromNmea(nmeaData);
        if (wgsCoordinate == null) return;

        mMeanCoordinateList.add(wgsCoordinate);

        //calculate the mean using the coordinates in the list
        Prism4DCoordinateTag meanCoordinate = calculateMeanWGS(mMeanCoordinateList);
        if (meanCoordinate != null) {
            meanCoordinate.setSatellites(nmeaData.getSatellites());
            meanCoordinate.setRawReadings  (mRawReadings);
            meanCoordinate.setFixedReadings(mFixedReadings);
        }

        //Assume the marker is the most recent
        // TODO: 12/12/2016 is marker assumption valid
        if ((mLastMarkerAdded == null) || (isFirstPointInMean)){
            isFirstPointInMean = false;

            //No marker, so we have to create one
            //returns LatLng of current Position, but sets mLastMarkerAdded as a side effect
            makeNewMarkerFromNmea(nmeaData,
                                  getString(R.string.collect_points_provisional_marker),
                                  markerColorProvisional);
        }

        //Check if this is the last point to be meaned
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();
        Prism4DProject project = constantsAndUtilities.getOpenProject();
        int lastPoint = project.getSettings().getMeaningNumber();
        if (lastPoint <= mMeanCoordinateList.size())isLastPointInMean = true;

        //isLastPointInMean also set true by user with StorePoint
        if (isLastPointInMean){
            //the handler is smart enough to know whether to get location from nmea or mean
            handleStorePosition(nmeaData, meanCoordinate);
        } else {
            LatLng newPosition = new LatLng(meanCoordinate.getLatitude(),
                                            meanCoordinate.getLongitude());


            //change the screen focus and the marker to the most current mean
            setFocus(meanCoordinate.getLatitude(), meanCoordinate.getLongitude());


            mLastMarkerAdded.setPosition(newPosition);
            //pass the info for the window to the marker
            mLastMarkerAdded.setTag(meanCoordinate);
            mLastMarkerAdded.showInfoWindow();
            zoomToFit();
        }
    }


    //Update the UI with satellite status info from GPS
    protected void setGpsStatus(){

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            try {
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
                //mSatInSky.setData(inSky);
                //indicate whether the position is fixed (locked) or not
                //mSatInFix.setData(inFix);
                if (inFix > 0){
                    //mGpsState.stateLock();
                } else {
                    //mGpsState.stateOn();
                }
                //mTtff.setData(mGpsStatus.getTimeToFirstFix());
            } catch (SecurityException e) {
                Toast.makeText(getActivity(), "GPS must be enabled", Toast.LENGTH_SHORT).show();

            }



        } else {
            //location manager is not enabled, u
            // pdate the UI with that
            /*
            mGpsState.stateOff();
            mSatInFix.setData("no fix");
            mSatInSky.setData("gps off");
            */
        }
/*
        mPositionView.postInvalidate();
        mSignalView.postInvalidate();
        */
    }


    //***********************************************************//
    //***********  Maps Callback and other Maps routines   ******//
    //***********************************************************//

    private void initializeMaps(Bundle savedInstanceState, View v){
        //Full fragment initialization was this:

        // Gets the MapView from the XML layout and creates it
        mMapView = (MapView) v.findViewById(R.id.map_view);
        //we need to be the one stepping the map through it's lifecycle, not the system
        //  so pass the creation event on to the map
        mMapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff

        //onMapReadyCallback is triggered when the map is ready to be used
        //                   is called when the map is ready for initialization
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                initializeMapsReady();
            }
        });
    }

    private void initializeMapsReady(){
        //current location control depends upon permissions
        boolean locEnabled = false;
        //This check doesn't do anything other than allow us to disable locationEnabled
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)   == PackageManager.PERMISSION_GRANTED) {

            //locEnabled = true;

        }

        mMap.getUiSettings().setMyLocationButtonEnabled(locEnabled);
        mMap.setMyLocationEnabled(locEnabled);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // TODO: 12/10/2016 make the minZoomLevel a constant somewhere
        int minZoomLevel = 8;
        //Make sure we don't zoom in too close
        mMap.setMinZoomPreference(minZoomLevel);

        mMap.setInfoWindowAdapter(new Prism4DInfoWindowAdapter(getActivity()));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //Handle the map touch event
                handleMapTouch(latLng);
            }
        });

        //use the drag events for remove and / or edit the points
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker marker){
            Toast.makeText(getActivity(), "Marker Touched",Toast.LENGTH_SHORT).show();

            Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();
            Prism4DProject openProject = constantsAndUtilities.getOpenProject();

            Prism4DCoordinateTag pointMarker = (Prism4DCoordinateTag)marker.getTag();
            int pointID = pointMarker.getPointID();
            Prism4DPoint point = openProject.getPoint(pointID);
            if(point == null){
                Toast.makeText(getActivity(),R.string.no_point_at_marker,Toast.LENGTH_SHORT).show();
            } else {
                askRemoveEditPoint(openProject, point, marker);
            }
            return false;//true suppresses default marker behavior, false tells system to respond to event also
        }

    });
}



    //***************************************************************//
    //*********   Other Initialization Methods   ********************//
    //***************************************************************//
    private void setSubtitle() {
        ((MainPrism4DActivity)getActivity()).switchSubtitle(R.string.subtitle_collect_points);
    }

    private void initializeTestData(){
        mTestDataCounter = 0;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                mPictureButton.setEnabled(true);
                Toast.makeText(getActivity(),R.string.try_camera_again,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void wireWidgets(View v){
        //Current Postion Block of fields and buttons
        mCurrentPositionButton = (Button) v.findViewById(R.id.currentPositionButton);
        mCurrentPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Toast.makeText(getActivity(),R.string.current_position_label,Toast.LENGTH_SHORT).show();
                handleCurrentPosition();

            }
        });


        mPointIDField                 = (TextView)v.findViewById(R.id.pointIDField);
        mCurrentNorthingPositionField = (TextView)v.findViewById(R.id.currentNorthingPositionField);
        mCurrentEastingPositionField  = (TextView)v.findViewById(R.id.currentEastingPositionField);
        mCurrentElevationField        = (TextView)v.findViewById(R.id.currentElevationField);
        mCurrentFunctionCodeField     = (EditText)v.findViewById(R.id.currentFeatureCodeField);
        mCurrentHeightField           = (EditText)v.findViewById(R.id.currentHeightField);



        //Store position button
        mStorePositionButton = (Button) v.findViewById(R.id.storePositionButton);
        mStorePositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.store_position_label,
                        Toast.LENGTH_SHORT).show();

                //This knows whether we are in the middle of meaning or just use current position
                Prism4DCoordinateTag meanCoordinate =  null;
                if (isMeanInProgress){
                    //calculate the mean using the coordinates in the list
                    meanCoordinate = calculateMeanWGS(mMeanCoordinateList);
                }
                handleStorePosition(mNmeaData, meanCoordinate);


            }
        });

        //Offset position Button
        mOffsetPositionButton = (Button) v.findViewById(R.id.offsetPositionButton);
        mOffsetPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.offset_position_label,
                        Toast.LENGTH_SHORT).show();

                askApplyOffset();

                

            }
        });

        //Average Button
        mAveragePositionButton = (Button) v.findViewById(R.id.averagePositionButton);
        mAveragePositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.ave_position_label,
                        Toast.LENGTH_SHORT).show();
                if (!isMeanInProgress){
                    //start the meaning process by calling the event handler
                    handleAveragePosition();
                } else {
                    //cancle any process in progress
                    handleCancelMeaning();
                }


            }
        });


        //Average Button
        mShowAllPointsButton = (Button) v.findViewById(R.id.showAllPointsButton);
        mShowAllPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.show_all_points_label,
                        Toast.LENGTH_SHORT).show();

                handleShowAllPoints();

            }
        });

        //maps Button
        mMapsButton = (Button) v.findViewById(R.id.mapsButtonCollect);
        mMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mapType = mMap.getMapType();
                if (mapType == GoogleMap.MAP_TYPE_TERRAIN){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }


            }
        });

        //picture (as in camera or video) Button
        mPictureButton = (Button) v.findViewById(R.id.pictureButton);
        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                PackageManager packageManager = getActivity().getPackageManager();
                boolean hasCamera =
                        packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

                //In order to take pictures, we must both have a camera and 
                // Permission to take pictures
                if (!hasCamera) {
                    Toast.makeText(getActivity(),
                            R.string.no_camera_on_this_device,
                            Toast.LENGTH_SHORT).show();
                } else if (
                        ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                            R.string.need_permission_to_take_pictures,
                            Toast.LENGTH_SHORT).show();

                } else {
                    /*

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(packageManager) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
                    }
                    */
                    //Send off an intent to the camera
                    dispatchTakePictureIntent();
                }
            }
        });
        if (ContextCompat.checkSelfPermission
                (getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //mPictureButton.setEnabled(false);
            //used to identify the request in onRequestPermissionsResult()

            int requestCode = MY_PERMISSIONS_REQUEST_CAMERA;
            ActivityCompat.requestPermissions
                    (getActivity(),
                     new String[] {Manifest.permission.CAMERA,
                                   Manifest.permission.WRITE_EXTERNAL_STORAGE },
                     requestCode);
        }


        //notes Button
        mNotesButton = (Button) v.findViewById(R.id.notesButton);
        mNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.notes_button_label,
                        Toast.LENGTH_SHORT).show();

                if (!isMeanInProgress) {
                    getNotesText();
                }

            }
        });


        //ZOOMIN Button
        mZoomInButton = (ImageButton) v.findViewById(R.id.zoomInButton);
        mZoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.zoom_in_button_label,
                        Toast.LENGTH_SHORT).show();

                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                isAutoResizeOn = false;

            }
        });

        //ZOOMOUT Button
        mZoomOutButton = (ImageButton) v.findViewById(R.id.zoomOutButton);
        mZoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.zoom_out_button_label,
                        Toast.LENGTH_SHORT).show();


                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                isAutoResizeOn = false;
            }
        });

        //ZOOM ext Button
        mZoomExtButton = (ImageButton) v.findViewById(R.id.zoomExtButton);
        mZoomExtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.zoom_ext_button_label,
                        Toast.LENGTH_SHORT).show();
                zoomToFit();
                isAutoResizeOn = true;

            }
        });


        //FOOTER WIDGETS


        //Footer fields with status and quality info about GPS source
        mCurrentProjectField  = (TextView)v.findViewById(R.id.currentProjectField);

        mModelField           = (TextView)v.findViewById(R.id.modelField);
        mSnField              = (TextView)v.findViewById(R.id.snField);
        mHdopField            = (TextView)v.findViewById(R.id.hdop_field);
        mVdopField            = (TextView)v.findViewById(R.id.vdop_field);
        mPdopField            = (TextView)v.findViewById(R.id.pdop_field);
        mTdopField            = (TextView)v.findViewById(R.id.tdop_field);
        mHrmsField            = (TextView)v.findViewById(R.id.hrms_field);
        mVrmsField            = (TextView)v.findViewById(R.id.vrms_field);


        //  Esc and Enter buttons are enabled on the collect screen

        //Esc Button
        mEscButton = (Button) v.findViewById(R.id.escButton);
        //have to set the color and enable the button as the default is NOT enabled/grayed out
        mEscButton.setEnabled(true);
        mEscButton.setTextColor(Color.BLACK);
        mEscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMeanInProgress){
                    isMeanInProgress = false;
                    handleCancelMeaning();
                } else {
                    //for now, just put up a toast that the button was pressed
                    Toast.makeText(getActivity(),
                            R.string.no_save,
                            Toast.LENGTH_SHORT).show();
                    //Switch the fragment to the previous fragment.
                    // But the switching happens on the container Activity
                    MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                    if (myActivity != null) {
                        myActivity.switchToPopBackstack();
                    }
                }

            }
        });

        //Enter Button
        mEnterButton = (Button) v.findViewById(R.id.enterButton);
        //have to set the color and enable the button as the default is NOT enabled/grayed out
        mEnterButton.setEnabled(true);
        mEnterButton.setTextColor(Color.BLACK);
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            //Let user know data was saved
            Toast.makeText(getActivity(),
                    R.string.save_contents,
                    Toast.LENGTH_SHORT).show();


                //This knows whether we are in the middle of meaning or just use current position
                Prism4DCoordinateTag meanCoordinate =  null;
                if (isMeanInProgress){
                    //calculate the mean using the coordinates in the list
                    meanCoordinate = calculateMeanWGS(mMeanCoordinateList);
                }
                setFocus(mNmeaData);

                handleStorePosition(mNmeaData, meanCoordinate);

                //Switch the fragment to the previous fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToPopBackstack();
                }

            }
        });


    }

    /************************************************************/
    /****** Call backs for Activities started with intents ******/
    /************************************************************/
    //The place where intents return to this fragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                //Ths picture has been successfully taken
                //record the picture in on the point or the project
                String temp = mCurrentPhotoPath;
                galleryAddPic();
                startGps();


                //thumbnail comes back in the data intent, but need to deal with the whole picture
                /* code for displaying a thumbprint of the picture
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                int offset = 0;
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.length);

                imageView.setImageBitmap(bitmap);
                */
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText( getActivity(),
                                R.string.unable_to_create_picture_file,
                                Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {/*
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                                          "com.asc.msigeosystems.prism4d.fileprovider",
                                                          photoFile);
*/
                Uri photoURI = URI.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
            } else {
                // Error occurred while creating the File
                Toast.makeText( getActivity(),
                                R.string.unable_to_create_picture_file,
                                Toast.LENGTH_SHORT).show();

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();
        Prism4DProject project = constantsAndUtilities.getOpenProject();
        String imageFileName = project.getProjectName() + timeStamp + "_";
        /*
        //getExternalStoragePublicDirectory() //accessible to all apps and the user
        //with the DIRECTORY_PICTURES argument
        //requires manifest permission
        //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        //
        // if use: getExternalFilesDir()
        // files are deleted when app uninstalled
        //requires manifest permission:
        //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        //                               android:maxSdkVersion="18" />
        */

        //Assure the path exists
        File projectDirectory = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                //getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                project.getProjectName().toString());

        //check if the project subdirectory already exists
        if (!projectDirectory.exists()){
            //if not, create it
            if (!projectDirectory.mkdirs()){
                //Unable to create the file
                return null;
            }
        }

        File imageFile = File.createTempFile(imageFileName,  /* prefix */
                                         ".jpg",             /* suffix */
                                         projectDirectory    /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }


    //Make the photo available through Gallery
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }


    /************************************************************/
    /****** Handlers for Buttons on Collect Points screen  ******/
    /************************************************************/
    private void handleCurrentPosition(){
        isAutoResizeOn = true;
        //ignore the event if meaning is in process
        if (isMeanInProgress) return;

        //set screen focus to this location
        setFocus(mNmeaData);

        //update screen with the nmea data
        updateNmeaUI(mNmeaData);
    }

    private void handleAveragePosition(){
        if (mNmeaData == null) {
            Toast.makeText(getActivity(), R.string.gps_not_running,Toast.LENGTH_SHORT).show();
            return;
        }
         //set screen focus to this location
         setFocus(mNmeaData);

        //start the meaning process by setting the proper flags
        isMeanInProgress   = true;
        isFirstPointInMean = true;
        isLastPointInMean  = false;
        mRawReadings       = 0;
        mFixedReadings     = 0;

        //set the time
        mMeanTime = 0;


        //dump any old contents from past meaning processes
        if ((mMeanCoordinateList == null) || (mMeanCoordinateList.size() > 0)){
            mMeanCoordinateList = new ArrayList<>();
        }
        //Actual meaning and creation of marker will happen on the next NMEA interrupt handler
        //when it notices the mean flag is set true
    }

    private void handleCancelMeaning(){
        isMeanInProgress   = false;
        isFirstPointInMean = false;
        isLastPointInMean  = false;
        mRawReadings       = 0;
        mFixedReadings     = 0;

        mMeanTime          = 0;

        mMeanCoordinateList = null;
        mLastMarkerAdded.remove();
    }


    //handler is smart enough to know whether mean or button press
    private void handleStorePosition(Prism4DNmea nmeaData,
                                     Prism4DCoordinateTag locationCoordinate){

        if (nmeaData == null) return;
        setFocus(nmeaData);
        if ((mLatitudeScreenFocus == 0d) || (mLongitudeScreenFocus == 0d)) return;


        //knows whether we have to stop the mean, or use current position

        //step 1 create new point
        //step 2 create the WSG coordinate
        //step 3 determine which kind of coordinate, create it and add to the point:
        //Step 4 add the point to the project list
        //step 5 change the final location and color of the marker
        //step 6 draw lines and do zooming or panning necessary
        //step 7 change the info window
        //step 8 change screen focus to this location
        //step 9 change the flag saying the meaning is over
        //step 10 reset variables in preparation for next meaning process

        //step 1 create point from the open project
        //get the open project
        Prism4DConstantsAndUtilities constantsAndUtilities = Prism4DConstantsAndUtilities.getInstance();
        Prism4DProject project = constantsAndUtilities.getOpenProject();
        Prism4DPoint point = createPoint(project);
        

        //Update the UI with the point id
        mPointIDField.setText(String.valueOf(point.getPointID()));
        //update the meaning coordinate with the point id
        //if locationCoordinate is null, we'll make one for the marker info window soon
        if (locationCoordinate != null)locationCoordinate.setPointID(point.getPointID());



        //step 2 create the WSG coordinate
        Prism4DCoordinateWGS84 wgs84Coordinate;
        //starts with the WSG84 coordinate, regardless of the final type
        //valid coordinate is set automatically based on latitude/longitude
        if ((!isMeanInProgress) || (locationCoordinate == null)) {
            //have to create the locationCoordinate from current position
            //locationCoordinate =  getPCSFromNmea(nmeaData);
            wgs84Coordinate = getWgsFromNmea(nmeaData);
        } else {
            //creates coordinate in open project
            wgs84Coordinate = getWgsCoordinateFromMean(locationCoordinate);
        }


        //step 3 determine which kind of coordinate, create it, add to point:
        createCoordinateFromWSG(project, point, wgs84Coordinate);


        //Step 4 add the point to the project
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        boolean addToDBToo = true;
        pointManager.addToProject(project, point, addToDBToo);


        //step 5 change the final location and color of the marker
        LatLng currentPosition = new LatLng(wgs84Coordinate.getLatitude(),
                                            wgs84Coordinate.getLongitude());

        if (mLastMarkerAdded == null){
             //If not meaning, create a marker and zoom properly
            String markerName = "Point ID = "+String.valueOf(point.getPointID());
            //mLastMarkerAdded is updated as a side effect
            //it actually returns a LatLng
            makeNewMarkerFromNmea(nmeaData, markerName, markerColorFinal);
        } else {
            //change the position of the marker to the designated position
            mLastMarkerAdded.setPosition(currentPosition);
            mLastMarkerAdded.setIcon(BitmapDescriptorFactory.defaultMarker(markerColorFinal));
        }


        //step 6 draw any lines and do any zooming or panning necessary
        addPointToLine(currentPosition);


        //step 7 change the info window
        //we need the locationCoordinate even if we aren't meaning as it's information
        //for the marker info window
        //pass the info for the window to the marker
        if (locationCoordinate == null){
            //we weren't meaning, so we need to create this information
            locationCoordinate = new Prism4DCoordinateTag();
            locationCoordinate.setPointID(point.getPointID());
            locationCoordinate.setMeanedReadings(1);//we are only dealing with a single point
            int fixed = 0;
            if (nmeaData.isFixed())fixed++;
            locationCoordinate.setFixedReadings(fixed);
            locationCoordinate.setLatitude(nmeaData.getLatitude());
            locationCoordinate.setLongitude(nmeaData.getLongitude());
            locationCoordinate.setElevation(nmeaData.getOrthometricElevation());
            locationCoordinate.setLatitudeStdDev(0);
            locationCoordinate.setLongitudeStdDev(0);
            locationCoordinate.setElevationStdDev(0);
            locationCoordinate.setHrms(1);
            locationCoordinate.setVrms(1);
        }
        mLastMarkerAdded.setTag(locationCoordinate);
        mLastMarkerAdded.showInfoWindow();


        //step 8 Change screen focus to this location
        setFocus(locationCoordinate.getLatitude(), locationCoordinate.getLongitude());



        //step 9 change the flags to indicating the meaning is over
        isMeanInProgress   = false;
        isLastPointInMean  = false;
        isFirstPointInMean = false;
        mRawReadings       = 0;
        mFixedReadings     = 0;
        mMeanTime          = 0;


        //step 10 reset variables in preparation for next meaning process
        mLastMarkerAdded    = null;
        mMeanCoordinateList = null;
    }

    private void handleMapTouch(LatLng latLng){
        if (isMeanInProgress) return;
        setFocus(latLng);

        String msg = getString(R.string.map_touched)+ latLng.toString();
        Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();

        isAutoResizeOn = false;
    }

    private void handleOffsetPosition() {
        //ignore the event if meaning is in process
        if (isMeanInProgress) return;
    }


    //handler is smart enough to know whether mean or button press
    private void handleShowAllPoints(){

        //Step 0 clear any old markers and reset variables
        //step 1 Get the open project
        //step 2 For each point on the project
        //step 3 Create a Marker
        //Step 4 Create a tag
        //step 5 Add to poly line, draw lines and do zooming or panning necessary

        //step 7 change screen focus to last point's location


        mLineContainer = null; //clear any old lines
        mZoomBounds    = null;
        mZoomBuilder   = null;
        mPointsPlotted = 0;
        isAutoResizeOn = true;
        //clear the map of old markers
        mMap.clear();

        //step 1 Get the open project
        //get the open project
        Prism4DConstantsAndUtilities constantsAndUtilities = Prism4DConstantsAndUtilities.getInstance();
        Prism4DProject project = constantsAndUtilities.getOpenProject();

        ArrayList<Prism4DPoint> points = project.getPoints();
        int last = points.size();

        //step 2 For each point on the project
        int          position;
        Prism4DPoint point;
        LatLng       markerLocation = null;
        Prism4DCoordinateWGS84 coordinateWGS84;
        Prism4DCoordinateTag   coordinateTag;
        for (position = 0; position <= last; position++){
            point = points.get(position);
            //step 3 Create a Marker
            coordinateWGS84 = makeNewMarkerFromPoint(project, point);

            //Step 4 Create a tag
            coordinateTag = createTag(point, coordinateWGS84);
            mLastMarkerAdded.setTag(coordinateTag);

            //step 5 Add to poly line, draw lines and do zooming or panning necessary
            markerLocation = new LatLng(coordinateWGS84.getLatitude(),
                                        coordinateWGS84.getLongitude());
            addPointToLine(markerLocation);

        }
        //step 8 change screen focus to last point's location
        if (markerLocation != null) {
            setFocus(markerLocation);
        }

    }



    /************************************************************/
    /******     AlertDialog Utilities                      ******/
    /************************************************************/

    private void getNotesText(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.enter_notes));
        builder.setIcon(R.drawable.ground_station_icon);

        // Set up the input
        final EditText input = new EditText(getActivity());
        input.setText(String.valueOf(mPointNotes));
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPointNotes = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void askApplyOffset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.apply_offset));
        builder.setIcon(R.drawable.ground_station_icon);

        // Set up the buttons
        builder.setPositiveButton("Next Point", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //offsets will be set to zero after applied to a single point
                isOffsetResetAfterOnePoint = true;
                isOffsetCancle = false;
                getOffsetPosition();
            }
        });

        builder.setNegativeButton("All Future Points", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //offsets are not reset to zero, they are just applied
                isOffsetResetAfterOnePoint = false;
                isOffsetCancle = false;
                getOffsetPosition();
            }
        });

        builder.setNeutralButton("Cancle", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int whichButton){
                isOffsetCancle = true;
                isOffsetResetAfterOnePoint = false;
                mOffsetLatitude = 0d;
                mOffsetLongitude = 0d;
                mOffsetElevation = 0d;
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void getOffsetPosition(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.enter_offsets));
        builder.setIcon(R.drawable.ground_station_icon);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up the input
        //Latitude
        final EditText latitude = new EditText(getActivity());
        latitude.setInputType(InputType.TYPE_CLASS_NUMBER |
                              InputType.TYPE_NUMBER_FLAG_DECIMAL |
                              InputType.TYPE_NUMBER_FLAG_SIGNED);
        latitude.setHint(R.string.latitude_input_label);
        layout.addView(latitude);

        //longitude
        final EditText longitude = new EditText(getActivity());
        longitude.setInputType(InputType.TYPE_CLASS_NUMBER |
                               InputType.TYPE_NUMBER_FLAG_DECIMAL |
                               InputType.TYPE_NUMBER_FLAG_SIGNED);
        longitude.setHint(R.string.longitude_input_label);
        layout.addView(longitude);

        //elevation
        final EditText elevation = new EditText(getActivity());
        elevation.setInputType(InputType.TYPE_CLASS_NUMBER |
                               InputType.TYPE_NUMBER_FLAG_DECIMAL |
                               InputType.TYPE_NUMBER_FLAG_SIGNED);
        elevation.setHint(R.string.elevation_input_label);
        layout.addView(elevation);

        builder.setView(layout);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                mOffsetLatitude  = Double.valueOf(latitude.getText().toString());
                mOffsetLongitude = Double.valueOf(longitude.getText().toString());
                mOffsetElevation = Double.valueOf(elevation.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                mOffsetLatitude  = 0d;
                mOffsetLongitude = 0d;
                mOffsetElevation = 0d;
            }
        });

        builder.show();
    }



    private void askRemoveEditPoint(Prism4DProject project, Prism4DPoint point, final Marker marker){
        // TODO: 12/25/2016 There must be a better way to pass arguments to the handler routines
        final Prism4DProject openProject       = project;
        final Prism4DPoint   openPoint         = point;
        final Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        final Marker         mapMarker         = marker;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.point_actions));
        builder.setIcon(R.drawable.ground_station_icon);

        // Set up the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //move the marker back to its location.
                Prism4DCoordinateTag tagCoordinate = (Prism4DCoordinateTag)marker.getTag();
                LatLng latLng = new LatLng(tagCoordinate.getLatitude(), tagCoordinate.getLongitude());
                marker.setPosition(latLng);

                //Switch to the Edit Point Fragment
                Prism4DPath pointPath  = new Prism4DPath(Prism4DPath.sEditFromMaps);
                ((MainPrism4DActivity)getActivity())
                        .switchToEditPointScreen(openProject.getProjectID(), pointPath, openPoint);
            }
        });

        builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //Delete the point from the project and from the DB
                pointManager.removePoint(openProject.getProjectID(), openPoint);

                //remove the marker
                mapMarker.remove();

            }
        });

        builder.setNeutralButton("Exclude (From Zoom)", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int whichButton){
                //exclude point from zoom
            }
        });

        builder.show();
    }





    /************************************************************/
    /****** Utilities used by Button Handlers              ******/
    /************************************************************/


    private Prism4DPoint createPoint(Prism4DProject project){
        //create the point
        Prism4DPoint point = new Prism4DPoint();
        point.setForProjectID(project.getProjectID());
        point.setPointID(project.getNextPointID());
        point.setPointNotes(mPointNotes);
        point.setPointFeatureCode(mCurrentFunctionCodeField.getText());

        //update the quality fields
        point.setHdop(mHdop);
        point.setVdop(mVdop);
        point.setTdop(mTdop);
        point.setPdop(mPdop);
        point.setHrms(mHrms);
        point.setVrms(mVrms);

        return point;

    }

    //create coordinate and add to the point
    private void createCoordinateFromWSG(Prism4DProject project,
                                         Prism4DPoint   point,
                                         Prism4DCoordinateWGS84 wsg84Coordinate){

        CharSequence coordinateType = project.getProjectCoordinateType();

        if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeWGS84)){

            //put the new coordinate on the point
            point.setHasACoordinateID(wsg84Coordinate.getCoordinateID());
            point.setCoordinate(wsg84Coordinate);
        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83)){
            Prism4DCoordinateNAD83 newNad83Coordinate = new Prism4DCoordinateNAD83(wsg84Coordinate);
            point.setCoordinate(newNad83Coordinate);
            point.setHasACoordinateID(newNad83Coordinate.getCoordinateID());
        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeUTM)){
            Prism4DCoordinateUTM newUtmCoordinate = new Prism4DCoordinateUTM(wsg84Coordinate);
            point.setCoordinate(newUtmCoordinate);
            point.setHasACoordinateID(newUtmCoordinate.getCoordinateID());
        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeSPCS)){
            Prism4DCoordinateSPCS newSpcsCoordinate = new Prism4DCoordinateSPCS(wsg84Coordinate);
            point.setCoordinate(newSpcsCoordinate);
            point.setHasACoordinateID(newSpcsCoordinate.getCoordinateID());
        } else {
            throw new RuntimeException("Something wrong with Coordinate type in Collect Points");
        }
    }

    //called from the nmea event handler when mean is in progress
    private Prism4DCoordinateTag calculateMeanWGS(
                                            ArrayList<Prism4DCoordinateWGS84> coordinateList){

        Prism4DCoordinateTag meanCoordinate = new Prism4DCoordinateTag();
        Prism4DCoordinateTag residuals      = new Prism4DCoordinateTag();
        int size= coordinateList.size();

        double tempMeanD;

        meanCoordinate.setMeanedReadings(size);

        //assure that we have enough readings to calculate mean
        if (size > 1){
            //calculate the mean
            //Step one - Sum of each member in the list
            for (int i = 0; i<size; i++){
                tempMeanD = meanCoordinate.getLatitude()  + coordinateList.get(i).getLatitude();
                meanCoordinate.setLatitude(tempMeanD);

                tempMeanD = meanCoordinate.getLongitude() + coordinateList.get(i).getLongitude();
                meanCoordinate.setLongitude(tempMeanD);

                tempMeanD = meanCoordinate.getElevation() + coordinateList.get(i).getElevation();
                meanCoordinate.setElevation(tempMeanD);
                tempMeanD = meanCoordinate.getGeoid()     + coordinateList.get(i).getGeoid();
                meanCoordinate.setGeoid(tempMeanD);

            }
            //Step two: divide by the number in the list
            double sizeD = (double)size;

            meanCoordinate.setLatitude (meanCoordinate.getLatitude()  / sizeD);

            meanCoordinate.setLongitude(meanCoordinate.getLongitude() / sizeD);

            meanCoordinate.setElevation(meanCoordinate.getElevation() / sizeD);

            meanCoordinate.setGeoid    (meanCoordinate.getGeoid()     / sizeD);

            //calculate the variance of the squared residuals
            //Step three: residual = sum( (mean - reading) squared  )
            for (int i = 0; i<size; i++){

                tempMeanD = meanCoordinate.getLatitude()  - coordinateList.get(i).getLatitude();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLatitude (residuals.getLatitude()  + tempMeanD);

                tempMeanD = meanCoordinate.getLongitude() - coordinateList.get(i).getLongitude();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setLongitude(residuals.getLongitude() + tempMeanD);

                tempMeanD = meanCoordinate.getElevation() - coordinateList.get(i).getElevation();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setElevation(residuals.getElevation() + tempMeanD);

                tempMeanD = meanCoordinate.getGeoid()     - coordinateList.get(i).getGeoid();
                tempMeanD = tempMeanD * tempMeanD;
                residuals.setGeoid    (residuals.getGeoid()    + tempMeanD);
            }


            //step 3.5 mean of residuals = sum / # of readings
            //Step 4: sqrt of (mean of the residuals)

            //Treat readings as sample of a larger population
            //so take sample mean (i.e. divide by size - 1)
            sizeD = sizeD - 1.0;
            meanCoordinate.setLatitudeStdDev  (Math.sqrt(residuals.getLatitude()  / sizeD));
            meanCoordinate.setLongitudeStdDev (Math.sqrt(residuals.getLongitude() / sizeD));
            meanCoordinate.setElevationStdDev (Math.sqrt(residuals.getElevation() / sizeD));

            return meanCoordinate;

        }
        return null; //
    }


    /*******************************************************/
    /***********      Map Utilities          ***************/
    /*******************************************************/

    private void zoomToFit(){
        // TODO: 12/10/2016 make markerPadding and forceZoomAfter constants somewhere
        int markerPadding = 50;
        int forceZoomAfter = 2;

        //only zoom after the first couple of points have been plotted
        if (mPointsPlotted > forceZoomAfter) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(mZoomBounds, markerPadding);
            mMap.animateCamera(cu);
        }

    }

    private double getScaleFactorMetersPerPixel(){

        //float latitude = mMap.getCenter.lat();
        LatLng center = mMap.getCameraPosition().target;
        double latitude = center.latitude;
        double latRadians = latitude * (Math.PI / 180);
        float zoom = mMap.getCameraPosition().zoom;
        double metersPerPixel = 156543.03392 * Math.cos(latRadians) / Math.pow(2, zoom);
        return metersPerPixel;
    }

    private Prism4DCoordinateWGS84 getWgsFromNmea(Prism4DNmea nmeaData){
        Prism4DCoordinateWGS84 wgsCoordinate = new Prism4DCoordinateWGS84(nmeaData.getLatitude(),
                                                                          nmeaData.getLongitude());

        wgsCoordinate.setElevation(nmeaData.getOrthometricElevation());
        wgsCoordinate.setGeoid(nmeaData.getGeoid());

        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();

        wgsCoordinate.setProjectID(constantsAndUtilities.getOpenProjectID());
        wgsCoordinate.setTime(System.currentTimeMillis());
        return wgsCoordinate;
    }


    private Prism4DCoordinateWGS84 getWgsCoordinateFromMean(
                                                  Prism4DCoordinateTag meanCoordinate){

        Prism4DCoordinateWGS84 wgs84Coordinate = new Prism4DCoordinateWGS84(
                                                                    meanCoordinate.getLatitude(),
                                                                    meanCoordinate.getLongitude());

        wgs84Coordinate.setElevation(meanCoordinate.getElevation());
        wgs84Coordinate.setGeoid    (meanCoordinate.getGeoid());

        Prism4DConstantsAndUtilities constantsAndUtilities = Prism4DConstantsAndUtilities.getInstance();
        wgs84Coordinate.setProjectID(constantsAndUtilities.getOpenProjectID());
        wgs84Coordinate.setTime(System.currentTimeMillis());
        return wgs84Coordinate;
    }


    private Prism4DNmea getNmeaFromTestData(){
        // TODO: 12/12/2016 This routine will have to be replaced with real data

        //Step 1 - Get Test data object
        Prism4DTestLocationDataManager testLocationDataManager =
                                                    Prism4DTestLocationDataManager.getInstance();
        mTestData = testLocationDataManager.get(mTestDataCounter);
        mTestDataCounter++;
        if (mTestDataCounter >= mTestDataMax)mTestDataCounter = 0;

        //Convert the Test Data Point to GPS digital degrees
        if (mTestData == null) {
            startGps();
            Toast.makeText(getActivity(), R.string.test_data_invalid, Toast.LENGTH_SHORT).show();
            throw new RuntimeException(getString(R.string.test_data_invalid));

        }

        //Step 2 - Convert DMS to DD
        //create a coordinate as the easiest way to convert DMS to DD
        Prism4DCoordinateWGS84 wgsCoordinate = new Prism4DCoordinateWGS84 (
                mTestData.getLatitudeDegrees(),  mTestData.getLatitudeMinutes(),  mTestData.getLatitudeSeconds(),
                mTestData.getLongitudeDegrees(), mTestData.getLongitudeMinutes(), mTestData.getLongitudeSeconds());

        wgsCoordinate.setElevation(mTestData.getElevation());
        wgsCoordinate.setGeoid    (mTestData.getGeoid());
        if (!wgsCoordinate.isValidCoordinate()) {
            startGps();
            Toast.makeText(getActivity(), R.string.test_data_invalid, Toast.LENGTH_SHORT).show();
            throw new RuntimeException(getString(R.string.test_data_invalid));
        }

        //Step 2 - Create NMEA data object
        //update the screen with the location
        //build a nmeaData with the TestData location
        Prism4DNmea nmeaData = new Prism4DNmea();
        //nmeaData.setNmeaType("GGA");//set to GPGGA when initialized
        nmeaData.setLatitude (wgsCoordinate.getLatitude());
        nmeaData.setLongitude(wgsCoordinate.getLongitude());
        nmeaData.setOrthometricElevation(wgsCoordinate.getElevation());
        nmeaData.setGeoid    (wgsCoordinate.getGeoid());
        nmeaData.setSatellites(7);

        // TODO: 12/10/2016 get rid of these debug statements
        //double check conversion to northing/easting
        Prism4DCoordinateUTM utmCoordinate = new Prism4DCoordinateUTM(wgsCoordinate);
        double tempTestNorthing = mTestData.getNorthing();
        double tempUTMNorthing  = utmCoordinate.getNorthing();
        double tempTestEasting  = mTestData.getEasting();
        double tempUTMEasting   = utmCoordinate.getEasting();

        //check the different values of elevation
        double tempElevation    = mTestData.getElevation();
        tempElevation           = wgsCoordinate.getElevation();
        tempElevation           = utmCoordinate.getElevation();

        return nmeaData;
    }


    private void centerMap(Prism4DNmea nmeaData){
        //update the maps
        LatLng newPoint = new LatLng(nmeaData.getLatitude(), nmeaData.getLongitude());

        CameraUpdate myZoom = CameraUpdateFactory.newLatLngZoom(newPoint, 15);
        mMap.animateCamera(myZoom);

    }

    private void setFocus(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        setFocus(latLng);
    }

    private void setFocus(Prism4DNmea nmeaData) {
        LatLng latLng = new LatLng(nmeaData.getLatitude(), nmeaData.getLongitude());
        setFocus(latLng);
    }

    private void setFocus(LatLng latLng) {
        mLatitudeScreenFocus  = latLng.latitude;
        mLongitudeScreenFocus = latLng.longitude;

        //only actualy change the focus if the auto resize is off
        if (isAutoResizeOn) {
            //update the maps zoom focus
            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
            mMap.animateCamera(center);
        }
    }

    private LatLng makeNewMarkerFromNmea(Prism4DNmea nmeaData, String markerName, float markerColor){

        //update the maps
        LatLng newPoint = new LatLng(nmeaData.getLatitude(), nmeaData.getLongitude());

        MarkerOptions newPointMarkerOptions = new MarkerOptions().position(newPoint)
                                                                 .title(markerName)
                                                                 .draggable(false)
                                                                 .icon(
                                                BitmapDescriptorFactory.defaultMarker(markerColor));
        mLastMarkerAdded = mMap.addMarker(newPointMarkerOptions);

        //CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        //get zoom level from the mpa
        mMapZoom = mMap.getCameraPosition().zoom;

        CameraUpdate myZoom = CameraUpdateFactory.newLatLngZoom(newPoint, mMapZoom);
        mMap.animateCamera(myZoom);

        return newPoint;
    }

    private Prism4DCoordinateWGS84 makeNewMarkerFromPoint(Prism4DProject project, Prism4DPoint point){

        Prism4DCoordinateWGS84 coordinateWGS84;
        CharSequence coordinateType = project.getProjectCoordinateType();

        if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeWGS84)){
            coordinateWGS84 = (Prism4DCoordinateWGS84) point.getCoordinate();

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83)){
            Prism4DCoordinateNAD83 coordinateNAD83 = (Prism4DCoordinateNAD83) point.getCoordinate();
            coordinateWGS84 = new Prism4DCoordinateWGS84(coordinateNAD83);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeUTM)){
            Prism4DCoordinateUTM coordinateUTM = (Prism4DCoordinateUTM) point.getCoordinate();
            coordinateWGS84 = new Prism4DCoordinateWGS84(coordinateUTM);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeSPCS)){
            Prism4DCoordinateSPCS coordinateSPCS = (Prism4DCoordinateSPCS) point.getCoordinate();
            coordinateWGS84 = new Prism4DCoordinateWGS84(coordinateSPCS);
        } else {
            throw new RuntimeException("Something wrong with Coordinate type in Collect Points");
        }

        //update the maps
        LatLng newPoint = new LatLng(coordinateWGS84.getLatitude(), coordinateWGS84.getLongitude());

        String markerName = String.valueOf(point.getPointID());
        MarkerOptions newPointMarkerOptions = new MarkerOptions().position(newPoint)
                                                                 .title(markerName)
                                                                 .draggable(false)
                                                                 .icon(
                                           BitmapDescriptorFactory.defaultMarker(markerColorFinal));
        mLastMarkerAdded = mMap.addMarker(newPointMarkerOptions);

        return coordinateWGS84;
    }

    private Prism4DCoordinateTag createTag(Prism4DPoint point,
                                           Prism4DCoordinateWGS84 coordinateWGS84){
        Prism4DCoordinateTag coordinateTag;
        //we weren't meaning, so we need to create this information
        coordinateTag = new Prism4DCoordinateTag();
        coordinateTag.setPointID(point.getPointID());
        coordinateTag.setMeanedReadings(1);//we are only dealing with a single point
        coordinateTag.setFixedReadings(1);
        coordinateTag.setLatitude(coordinateWGS84.getLatitude());
        coordinateTag.setLongitude(coordinateWGS84.getLongitude());
        coordinateTag.setElevation(coordinateWGS84.getElevation());
        coordinateTag.setLatitudeStdDev(0);
        coordinateTag.setLongitudeStdDev(0);
        coordinateTag.setElevationStdDev(0);
        coordinateTag.setHrms(1);
        coordinateTag.setVrms(1);

        return coordinateTag;
    }

    private void addPointToLine(LatLng newPoint){
        //add the new point to the line
        if (mLineContainer == null){
            mLineContainer = new PolylineOptions();
        }
        mLineContainer.add(newPoint).add(newPoint).width(25).color(Color.BLUE).geodesic(true);
        mPointsLine = mMap.addPolyline(mLineContainer);
        //The way to find out how many points we've processed so far
        //mPointsLine.getPoints().size();

        //now update the zoom boundary around the set of points
        if (mZoomBuilder == null){
            mZoomBuilder = new LatLngBounds.Builder();
            mPointsPlotted = 0;
        }
        mZoomBuilder = mZoomBuilder.include(newPoint);
        //update the zoom to fit bounds
        mZoomBounds = mZoomBuilder.build();
        mPointsPlotted++;

        if (isAutoResizeOn) {
            zoomToFit();
        }
    }
}


