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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.maps.android.SphericalUtil;

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
public class MainPrism4DPointCollectFragment extends Fragment implements //OnMapReadyCallback,
                                                                          GpsStatus.Listener,
                                                                          LocationListener,
                                                                          GpsStatus.NmeaListener {

    //DEFINE constants / literals
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int MY_PERMISSIONS_REQUEST_SAVE_PICTURES = 2;

    private static final int OFFSET_NEXT_ONLY  = 0;
    private static final int OFFSET_ALL_FUTURE = 1;
    private static final int OFFSET_ID_CONSTANT = 100;





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

    /****************************************************************/
    /****     variables to be saved on configuration change      ****/
    /****************************************************************/

    //Screen Focus
    private double mLatitudeScreenFocus;
    private double mLongitudeScreenFocus;

    //Reconfiguration in the middle of camera or notes is problematic


    //Can not take pictures or record notes while the meaning is in progress HOWEVER
    //The following variables are used in the communication
    // of one part of the picture/notes process to the next
    //Camera
    private String mCurrentPhotoPath = "";
    private String mCurrentPhotoFileName = "";
    private String mCurrentPhotoTimestamp = "";

    //Notes
    private CharSequence mNotes = "";




    //Offset Positions
    //Offsets are defined by the user, then used on subsequent point creation events. Meaned or not
    private double mOffsetDistance   = 0d;
    private double mOffsetHeading    = 0d;
    private double mOffsetElevation  = 0d;
    //The id of the radio button serves as the
    // flag for how many points the offset is to be applied to
    private int    mOffsetCheckedID  = OFFSET_NEXT_ONLY + OFFSET_ID_CONSTANT;


    //variables needed to keep track of location
    private LocationManager     mLocationManager;
    //private Location            mCurLocation; //unused because we get data from nmea
    private Prism4DNmea         mNmeaData;     //latest nmea sentence received

    //needs to be removed,
    // but have to figure out how to get this info from NMEA rather than the LocationManager
    private GpsStatus           mGpsStatus = null;



       //variables for the meaning process
    //Meaning MUST continue through reconfiguration, but intermediate points will be lost
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




    //variables for the map


    // TODO: 1/8/2017 Think about maintaining a list of points, rather than a list of markers
    //then use the "add all points" routine to recreate the map


    //Markers that are actually on the map
    private ArrayList<Marker> mMarkers     = new ArrayList<>();
    //Markers that are within the zoom boundaries
    private ArrayList<Marker> mZoomMarkers = new ArrayList<>();

    //only used at very beginning of app to mark end of initialization
    private boolean      isMapInitialized = false;
    //indicates whether map should be automatically resized when a point is added
    private boolean      isAutoResizeOn = true;




    /******************************************************************/
    /****     variables to be ignored on configuration change      ****/
    /******************************************************************/
    //Test Data
    private int                     mTestDataCounter = 0;
    private Prism4DTestLocationData mTestData;
    private int                     mTestDataMax = 13;


    //set by determineFocus()
    //the first step of picture or notes
    private boolean isFocusOnPoint = false; //if false, focus on the project
    private Marker mFocusMarker;




    //Map data that doesn't need to survive a configuration change

    //The map will need to be reinitialized after each reconfiguration change
    private MapView      mMapView;
    private GoogleMap    mMap;

    private Marker       mLastMarkerAdded;

    //buildNewZoom(markerList, doZoom) will rebuild these from mZoomMarkers
    //boolean doZoom controls whether the map is actually updated
    private LatLngBounds.Builder mZoomBuilder;
    private LatLngBounds         mZoomBounds;


    //redrawLineBetweenMarkers() recreates these variables
    // and draws the marker line from points in mMarkers
    private Polyline     mPointsLine;
    private PolylineOptions mLineOptions;



    //this recreates itself if it becomes null on configuration change
    private Prism4DNmeaParser   mNmeaParser = new Prism4DNmeaParser();



    /**********************************************************************/
    /*                Constructor                                         */
    /**********************************************************************/
    public MainPrism4DPointCollectFragment() {
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

        CharSequence projectName = getOpenProject().getProjectName();

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
        //mCurLocation = new Location(loc); // copy location
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
             if (mNmeaParser == null){
                 Prism4DNmeaParser nmeaParser = Prism4DNmeaParser.getInstance();
             }

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
                    if (!isMapInitialized){
                        centerMap(nmeaData);
                        isMapInitialized = true;
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
        int openProjectID = getOpenProjectID();
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


            int precisionDigits = getOpenProject().getSettings().getLocationPrecision();

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
        //Add to the list of locations being meaned.
        Prism4DCoordinateWGS84 wgsCoordinate = getWgsFromNmea(nmeaData);
        if (wgsCoordinate == null) return;

        mMeanCoordinateList.add(wgsCoordinate);

        //calculate the mean using the coordinates in the list
        Prism4DCoordinateMean meanCoordinate = calculateMeanWGS(mMeanCoordinateList);
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
        Prism4DProject project = getOpenProject();
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
        // TODO: 12/27/2016 Figure out how to dynamically resize the map view 

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


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(), "Marker Touched", Toast.LENGTH_SHORT).show();

                Prism4DCoordinateMean pointMarker = (Prism4DCoordinateMean) marker.getTag();
                int pointID = pointMarker.getPointID();
                Prism4DPoint point = getOpenProject().getPoint(pointID);
                if (point == null) {
                    Toast.makeText(getActivity(),
                                   R.string.no_point_at_marker,
                                   Toast.LENGTH_SHORT).show();
                } else {
                    if (!isMeanInProgress) {
                        askMarker(point, marker);
                    }
                }
                return false;//true suppresses default marker behavior, false tells system to respond to event also
            }
        });

        if (mMarkers == null){
            mMarkers = new ArrayList<>();
        }
        if (mZoomMarkers == null){
            mZoomMarkers = new ArrayList<>();
        }
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
        //We need permissions to take pictures with the camera then store the images
        if (ContextCompat.checkSelfPermission
                (getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            //used to identify the request in onRequestPermissionsResult()
            int requestCode = MY_PERMISSIONS_REQUEST_CAMERA;
            ActivityCompat.requestPermissions(getActivity(),
                                              new String[] {Manifest.permission.CAMERA,
                                                       Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                              requestCode);
        }


        //Current Position Block of fields and buttons
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
                Prism4DCoordinateMean meanCoordinate =  null;
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

                if (!isMeanInProgress) {
                    if ((mOffsetDistance != 0d) ||
                        (mOffsetHeading != 0d)  ||
                        (mOffsetElevation != 0d)){
                        Toast.makeText(getActivity(),
                                       R.string.offsets_reset,
                                       Toast.LENGTH_SHORT).show();
                        mOffsetDistance  = 0d;
                        mOffsetHeading   = 0d;
                        mOffsetElevation = 0d;
                    } else {
                        askOffsetPosition();
                    }
                }
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

                if (!isMeanInProgress) {
                    int mapType = mMap.getMapType();
                    if (mapType == GoogleMap.MAP_TYPE_TERRAIN) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    } else {
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    }
                }
            }
        });

        //picture (as in camera or video) Button
        mPictureButton = (Button) v.findViewById(R.id.pictureButton);
        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (!isMeanInProgress) {
                    handleTakePicture();
                }

            }
        });


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
                    handleNotes();
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

        //ZOOM OUT Button
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

                boolean doZoom = true;
                buildNewZoom(mZoomMarkers, doZoom);
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
                    //for now, just put up a toast that the button was pressed
                    Toast.makeText(getActivity(),
                            R.string.meaning_terminated,
                            Toast.LENGTH_SHORT).show();
                } else {
                    //for now, just put up a toast that the button was pressed
                    Toast.makeText(getActivity(),
                            R.string.exit_without_save,
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
                Prism4DCoordinateMean meanCoordinate =  null;
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


                //String temp = mCurrentPhotoPath;
                //Add the picture to the gallery so the user can see it there
                galleryAddPic();

                Prism4DProject openProject = getOpenProject();

                //create the picture object, The null is for a point ID
                Prism4DPicture picture = new Prism4DPicture(mCurrentPhotoTimestamp,
                        openProject,
                        null);

                picture.setPathName(mCurrentPhotoPath);
                picture.setFileName(mCurrentPhotoFileName);

                Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
                //The focus flags tell us where to put the picture
                if (isFocusOnPoint) {

                    Prism4DPoint point = getPointFromFocus(openProject);
                    if (point == null){
                        tellUserNoPoint();
                        startGps();
                        return ;
                    }

                    //add the point id to the picture
                    picture.setPointID(point.getPointID());

                    //update the point in the DB

                    //No need for a cascading update, only update the picture and the point
                    if (!projectManager.addPictureToDB(picture)){
                        Toast.makeText(getActivity(),
                            R.string.error_adding_picture_to_db,Toast.LENGTH_SHORT).show();
                    } else {
                        //add the picture to the point
                        point.addPicture(picture);
                    }

                } else {

                    //No need for a cascading update of the project
                    // and ALLLLL of its subordinate objects.
                    // Just update the project and the picture in the DB
                    if (!projectManager.addPictureToDB(picture)){
                        Toast.makeText(getActivity(),
                                R.string.error_adding_picture_to_db,Toast.LENGTH_SHORT).show();
                    } else {
                        //put the picture on the open project
                        openProject.addPicture(picture);
                    }
                }
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

    private Prism4DPoint getPointFromFocus(Prism4DProject openProject){
        //put the picture on the point of the marker with current focus
        Prism4DCoordinateMean coordinateTag = (Prism4DCoordinateMean) mFocusMarker.getTag();
        //The following check was done when the picture was started
        //but it never hurts to check again
        if ((coordinateTag == null) || (coordinateTag.getPointID() < 1)) {

            return null;
        }
        //find the point object the marker stands for
        Prism4DPoint point = openProject.getPoint(coordinateTag.getPointID());


        return point;

    }

    private void tellUserNoPoint(){
        Toast.makeText(getActivity(),
                R.string.point_not_created_at_marker_yet,
                Toast.LENGTH_SHORT).show();

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

        Prism4DProject project = getOpenProject();
        String imageFileName = project.getProjectName() + "_" + timeStamp + "_";
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
        mCurrentPhotoFileName = imageFileName + " ";//won't store in DB without a space at the end
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        mCurrentPhotoTimestamp = timeStamp;
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
        mMarkers.remove(mLastMarkerAdded);
        mLastMarkerAdded.remove();
    }


    //handler is smart enough to know whether mean or button press
    //This is the only place where a point and it's coordinate is actually created
    private void handleStorePosition(Prism4DNmea nmeaData,
                                     Prism4DCoordinateMean locationCoordinate){

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
        Prism4DProject project = getOpenProject();
        Prism4DPoint point     = createPoint(project);

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

        //step 2.1 Add the offset position to the location

        if ((mOffsetDistance  != 0) ||
            (mOffsetHeading   != 0) ||
            (mOffsetElevation != 0)) {

            //calculate the location using the offset
            LatLng fromLocation = new LatLng(wgs84Coordinate.getLatitude(), wgs84Coordinate.getLongitude());
            LatLng toLocation = SphericalUtil.computeOffset(fromLocation, mOffsetDistance, mOffsetHeading);

            //update the coordinaate
            wgs84Coordinate.setLatitude(toLocation.latitude);
            wgs84Coordinate.setLongitude(toLocation.longitude);
            double newElevation = wgs84Coordinate.getElevation() + mOffsetElevation;
            wgs84Coordinate.setElevation(newElevation);

            //record the offset itself
            point.setOffsetDistance(mOffsetDistance);
            point.setOffsetHeading(mOffsetHeading);
            point.setOffsetElevation(mOffsetElevation);

            //then reset the offsets if it was for a single point
            if (mOffsetCheckedID == (OFFSET_NEXT_ONLY + OFFSET_ID_CONSTANT)) {
                //reset the offset constants as they have now been applied to a point
                mOffsetDistance  = 0;
                mOffsetHeading   = 0;
                mOffsetElevation = 0;
            }
        }

        //Step 2.2 Alter Elevation if necessary with Height
        double height = Double.valueOf(mCurrentHeightField.getText().toString());
        if (height != 0d){
            double elevation = wgs84Coordinate.getElevation();
            wgs84Coordinate.setElevation(elevation - height);
        }

        //step 3 determine which kind of coordinate, create it, add to point:
        createCoordinateFromWSG(project, point, wgs84Coordinate);


        //Step 4 add the point to the project
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        boolean addToDBToo = true;
        if (!pointManager.addToProject(project, point, addToDBToo)){
            Toast.makeText(getActivity(),
                            getString(R.string.error_adding_point),
                            Toast.LENGTH_SHORT).show();

            //remove the point from the project
            if (!project.removePoint(point)){
                Toast.makeText(getActivity(),
                        getString(R.string.error_removing_point),
                        Toast.LENGTH_SHORT).show();
            }

            //get rid of marker
            if (mLastMarkerAdded != null){
                mLastMarkerAdded.remove();
            }
        }


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
            locationCoordinate = new Prism4DCoordinateMean();
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



    private void handleNotes(){
        if (!determineFocus())return;

        if (isFocusOnPoint){
            Prism4DPoint point = getPointFromFocus(getOpenProject());
            mNotes = point.getPointNotes();
        } else {
            mNotes = getOpenProject().getProjectDescription();
        }

        getNotesText();
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


        mLineOptions = null; //clear any old lines
        mZoomBounds    = null;
        mZoomBuilder   = null;

        isAutoResizeOn = true;
        //clear the map of old markers
        mMap.clear();
        mMarkers.clear();
        mZoomMarkers.clear();

        //step 1 Get the open project
        //get the open project
        Prism4DProject project = getOpenProject();

        ArrayList<Prism4DPoint> points = project.getPoints();
        int last = points.size();

        //step 2 For each point on the project
        int          position;
        Prism4DPoint point;
        LatLng       markerLocation = null;
        Prism4DCoordinateWGS84 coordinateWGS84;
        Prism4DCoordinateMean coordinateTag;
        for (position = 0; position < last; position++){
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


    private void handleTakePicture(){

        //focus must be valid to take a picture
        if (!determineFocus()) return;


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

            //Send off an intent to the camera
            dispatchTakePictureIntent();
        }
    }

    private boolean determineFocus(){

        //What object the picture is to be associated with depends upon the screen focus
        if ((mLatitudeScreenFocus == 0) || (mLongitudeScreenFocus == 0)){
            Toast.makeText(getActivity(),
                    R.string.invalid_screen_focus_for_picture,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //see if screen focus is near a marker
        isFocusOnPoint = true;
        mFocusMarker = getMarkerByFocus();

        if (mFocusMarker == null){
            //we aren't near a point marker, assign the picture to the open project
            isFocusOnPoint = false;
        } else {
            //Focus is on a marker,
            // but the point must be associated with the marker before we can take a picture
            Prism4DCoordinateMean coordinateTag = (Prism4DCoordinateMean)mFocusMarker.getTag();
            //if point id > 1, the point has not yet been created
            if ((coordinateTag == null) || (coordinateTag.getPointID() < 1)){
                Toast.makeText(getActivity(),
                        R.string.point_not_created_at_marker_yet,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            isFocusOnPoint = true;
        }

        return true;

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
        input.setText(String.valueOf(mNotes));
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNotes = input.getText().toString();
                storeNotesOnPointOrProject(mNotes);
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


    private void askOffsetPosition(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.enter_offsets));
        builder.setIcon(R.drawable.ground_station_icon);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up the input

        //Radio button for which points to apply to: next/all future buttons
        final RadioButton[] radioButtons = new RadioButton[2];
        RadioGroup radioGroup = new RadioGroup(getActivity()); //create the RadioGroup
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);//or RadioGroup.VERTICAL

        radioButtons[0] = new RadioButton(getActivity());
        radioButtons[0].setId(OFFSET_NEXT_ONLY + OFFSET_ID_CONSTANT);
        radioGroup.addView(radioButtons[0]);
        radioButtons[0].setText(getString(R.string.next_point));
        radioButtons[0].setSelected(true);
        mOffsetCheckedID = OFFSET_NEXT_ONLY + OFFSET_ID_CONSTANT;
        radioButtons[1] = new RadioButton(getActivity());
        radioButtons[1].setId(OFFSET_ALL_FUTURE + OFFSET_ID_CONSTANT);
        radioGroup.addView(radioButtons[1]);
        radioButtons[1].setText(getString(R.string.all_future_points));

        layout.addView(radioGroup);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mOffsetCheckedID = radioGroup.getCheckedRadioButtonId();
            }
        });

        //Distance
        final EditText distance = new EditText(getActivity());
        distance.setInputType(InputType.TYPE_CLASS_NUMBER |
                              InputType.TYPE_NUMBER_FLAG_DECIMAL |
                              InputType.TYPE_NUMBER_FLAG_SIGNED);
        distance.setHint(R.string.offset_distance_meters);
        layout.addView(distance);

        //Heading
        final EditText heading = new EditText(getActivity());
        heading.setInputType(InputType.TYPE_CLASS_NUMBER |
                               InputType.TYPE_NUMBER_FLAG_DECIMAL |
                               InputType.TYPE_NUMBER_FLAG_SIGNED);
        heading.setHint(R.string.offset_heading);
        layout.addView(heading);

        //elevation
        final EditText elevation = new EditText(getActivity());
        elevation.setInputType(InputType.TYPE_CLASS_NUMBER |
                               InputType.TYPE_NUMBER_FLAG_DECIMAL |
                               InputType.TYPE_NUMBER_FLAG_SIGNED);
        elevation.setHint(R.string.offset_elevation);
        layout.addView(elevation);

        builder.setView(layout);

        distance.setText(String.valueOf(mOffsetDistance));
        heading.setText(String.valueOf(mOffsetHeading));
        elevation.setText(String.valueOf(mOffsetElevation));


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String distanceValue  = distance.getText().toString();
                String headingValue   = heading.getText().toString();
                String elevationValue = elevation.getText().toString();
                if (distanceValue.isEmpty()){
                    mOffsetDistance = 0;
                } else {
                    mOffsetDistance = Double.valueOf(distanceValue);
                }
                if (headingValue.isEmpty()){
                    mOffsetHeading = 0;
                } else {
                    mOffsetHeading = Double.valueOf(headingValue);
                }
                if (elevationValue.isEmpty()){
                    mOffsetElevation = 0;
                }else {
                    mOffsetElevation = Double.valueOf(elevationValue);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void askMarker(Prism4DPoint point, final Marker marker){
        // TODO: 12/25/2016 There must be a better way to pass arguments to the handler routines
        final Prism4DPoint   openPoint         = point;
        final Marker         mapMarker         = marker;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Prism4D Collect Points Marker Options");
        builder.setIcon(R.drawable.ground_station_icon);
        builder.setItems(new CharSequence[] {
                        getString(R.string.marker_edit_point),
                        getString(R.string.marker_remove_point),
                        getString(R.string.marker_remove_marker),
                        getString(R.string.marker_exclude_point),
                        getString(R.string.marker_set_focus),
                        getString(R.string.cancel)                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Toast.makeText(getActivity(),
                                                getString(R.string.marker_edit_point),
                                                Toast.LENGTH_SHORT).show();
                                //Switch to the Edit Point Fragment
                                Prism4DPath pointPath  = new Prism4DPath(Prism4DPath.sEditFromMaps);
                                ((MainPrism4DActivity)getActivity())
                                        .switchToEditPointScreen(getOpenProjectID(),
                                                                 pointPath,
                                                                 openPoint);
                                break;
                            case 1:
                                Toast.makeText(getActivity(),
                                                getString(R.string.marker_remove_point),
                                                Toast.LENGTH_SHORT).show();
                                //Delete the point from the project and from the DB
                                Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
                                pointManager.removePoint(getOpenProjectID(), openPoint);

                                //remove the marker
                                mapMarker.remove();
                                // TODO: 1/8/2017 redraw the map after point/marker is removed?
                                //redraw the map?
                                break;
                            case 2:
                                Toast.makeText(getActivity(),
                                        getString(R.string.marker_remove_marker),
                                        Toast.LENGTH_SHORT).show();
                                mapMarker.remove();
                                break;
                            case 3:
                                Toast.makeText(getActivity(),
                                        getString(R.string.marker_exclude_point),
                                        Toast.LENGTH_SHORT).show();
                                mZoomMarkers.remove(mapMarker);
                                break;
                            case 4:
                                Toast.makeText(getActivity(),
                                                getString(R.string.marker_set_focus),
                                                Toast.LENGTH_SHORT).show();
                                LatLng mapPosition = mapMarker.getPosition();
                                setFocus(mapPosition);
                                break;
                            case 5:
                                Toast.makeText(getActivity(),
                                                getString(R.string.cancel),
                                                Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                                break;
                        }
                    }
                });
        builder.create().show();
    }




    private void storeNotesOnPointOrProject(CharSequence notes){

        Prism4DProject openProject = getOpenProject();

        //The focus flags tell us where to put the picture
        if (isFocusOnPoint) {
            //put the picture on the point of the marker with current focus
            Prism4DCoordinateMean coordinateTag = (Prism4DCoordinateMean) mFocusMarker.getTag();
            //The following check was done when the picture was started
            //but it never hurts to check again
            if (coordinateTag == null){
                tellUserNoPoint();
                startGps();
                return;
            }
            int pointID = coordinateTag.getPointID();
            if (pointID < 1) {
                tellUserNoPoint();
                startGps();
                return;
            }
            //find the point object the marker stands for
            Prism4DPoint point = openProject.getPoint(coordinateTag.getPointID());

            if (point == null){
                tellUserNoPoint();
                startGps();
                return;
            }

            //add the notes to the point

            point.setPointNotes(notes);

            //update the point in the DB
            Prism4DPointManager pointManager = Prism4DPointManager.getInstance();

            //No need for a cascading update, only update the picture and the point
            pointManager.updateSinglePointInDB(point);

        } else {
            //put the notes on the open project
            openProject.setProjectDescription(mNotes);
            Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

            //No need for a cascading update of the project
            // and ALLLLL of its subordinate objects.
            // Just update the project and the picture in the DB
            projectManager.updateSingleProjectInDB(openProject);
        }
        startGps();


    }

    /************************************************************/
    /****** Utilities used by Button Handlers              ******/
    /************************************************************/


    //This routine creates the point object, but not it's coordinate
    private Prism4DPoint createPoint(Prism4DProject project){
        //create the point
        Prism4DPoint point = new Prism4DPoint();
        point.setForProjectID(project.getProjectID());
        point.setPointID(project.getNextPointID());
        point.setPointNotes(mNotes);
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
            //put the point ID on the coordinate
            wsg84Coordinate.setPointID(point.getPointID());

            //put the new coordinate on the point
            point.setHasACoordinateID(wsg84Coordinate.getCoordinateID());
            point.setCoordinate(wsg84Coordinate);
        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83)){
            Prism4DCoordinateNAD83 newNad83Coordinate = new Prism4DCoordinateNAD83(wsg84Coordinate);
            newNad83Coordinate.setPointID(point.getPointID());
            point.setCoordinate(newNad83Coordinate);
            point.setHasACoordinateID(newNad83Coordinate.getCoordinateID());
        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeUTM)){
            Prism4DCoordinateUTM newUtmCoordinate = new Prism4DCoordinateUTM(wsg84Coordinate);
            newUtmCoordinate.setPointID(point.getPointID());
            point.setCoordinate(newUtmCoordinate);
            point.setHasACoordinateID(newUtmCoordinate.getCoordinateID());
        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeSPCS)){
            Prism4DCoordinateSPCS newSpcsCoordinate = new Prism4DCoordinateSPCS(wsg84Coordinate);
            newSpcsCoordinate.setPointID(point.getPointID());
            point.setCoordinate(newSpcsCoordinate);
            point.setHasACoordinateID(newSpcsCoordinate.getCoordinateID());
        } else {
            throw new RuntimeException("Something wrong with Coordinate type in Collect Points");
        }
    }

    //called from the nmea event handler when mean is in progress
    private Prism4DCoordinateMean calculateMeanWGS(
                                            ArrayList<Prism4DCoordinateWGS84> coordinateList){

        Prism4DCoordinateMean meanCoordinate = new Prism4DCoordinateMean();
        Prism4DCoordinateMean residuals      = new Prism4DCoordinateMean();
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
        if (mMarkers.size() > forceZoomAfter) {
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

        wgsCoordinate.setProjectID(getOpenProjectID());
        wgsCoordinate.setTime(System.currentTimeMillis());
        return wgsCoordinate;
    }


    private Prism4DCoordinateWGS84 getWgsCoordinateFromMean(
                                                  Prism4DCoordinateMean meanCoordinate){

        Prism4DCoordinateWGS84 wgs84Coordinate = new Prism4DCoordinateWGS84(
                                                                    meanCoordinate.getLatitude(),
                                                                    meanCoordinate.getLongitude());

        wgs84Coordinate.setElevation(meanCoordinate.getElevation());
        wgs84Coordinate.setGeoid    (meanCoordinate.getGeoid());

        wgs84Coordinate.setProjectID(getOpenProjectID());
        wgs84Coordinate.setTime(System.currentTimeMillis());
        return wgs84Coordinate;
    }


    private Prism4DNmea getNmeaFromTestData(){

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
        mMarkers.add(mLastMarkerAdded);
        mZoomMarkers.add(mLastMarkerAdded);

        //CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        //get zoom level from the mpa
        float mapZoom = mMap.getCameraPosition().zoom;

        CameraUpdate myZoom = CameraUpdateFactory.newLatLngZoom(newPoint, mapZoom);
        mMap.animateCamera(myZoom);

        return newPoint;
    }

    private Marker getMarkerByFocus(){
        int last = mMarkers.size();
        LatLng focusLocation = getFocusLatLng();
        double distanceToMarker;
        Marker marker;
        for (int position = 0; position < last; position++){
            marker = mMarkers.get(position);
            distanceToMarker = SphericalUtil.computeDistanceBetween(marker.getPosition(), focusLocation);

            if( distanceToMarker < getProximity() ) { // distance depends upon zoom level
                return marker;
            }
        }
        return null;
    }


    private LatLng getFocusLatLng(){
        return new LatLng(mLatitudeScreenFocus, mLongitudeScreenFocus);
    }

    private double getProximity(){
        // TODO: 12/28/2016 calculate proximity based on zoom level
        // TODO: 1/8/2017 what do we do if there is more than one marker within the proximity???
        return 50d;
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
        mMarkers.add(mLastMarkerAdded);
        mZoomMarkers.add(mLastMarkerAdded);

        return coordinateWGS84;
    }

    private Prism4DCoordinateMean createTag(Prism4DPoint point,
                                            Prism4DCoordinateWGS84 coordinateWGS84){
        Prism4DCoordinateMean coordinateTag;
        //we weren't meaning, so we need to create this information
        coordinateTag = new Prism4DCoordinateMean();
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
        if (mLineOptions == null){
            mLineOptions = new PolylineOptions();
        }
        mLineOptions.add(newPoint).width(25).color(Color.BLUE).geodesic(true);
        mPointsLine = mMap.addPolyline(mLineOptions);
        //The way to find out how many points we've processed so far
        //mPointsLine.getPoints().size();

        //now update the zoom boundary around the set of points
        if (mZoomBuilder == null){
            mZoomBuilder = new LatLngBounds.Builder();
        }

        mZoomBuilder = mZoomBuilder.include(newPoint);
        //update the zoom to fit bounds
        mZoomBounds = mZoomBuilder.build();


        if (isAutoResizeOn) {
            zoomToFit();
        }
    }

    private void redrawLineBetweenMarkers(ArrayList<Marker> lineMarkers, boolean doMapDraw){
        //build the mLineOptions from scratch
        mLineOptions = new PolylineOptions();

        int last = lineMarkers.size();
        int position = 0;
        Marker lineMarker;
        LatLng location;
        while (position < last){
            lineMarker = lineMarkers.get(position);
            location = lineMarker.getPosition();
            mLineOptions.add(location).width(25).color(Color.BLUE).geodesic(true);
        }
        if (doMapDraw) mPointsLine = mMap.addPolyline(mLineOptions);
    }

    private void buildNewZoom(ArrayList<Marker> zoomMarkers, boolean doZoom){
        //now update the zoom boundary around the set of points
        if (mZoomBuilder == null){
            mZoomBuilder = new LatLngBounds.Builder();
        }


        int last = zoomMarkers.size();
        int position = 0;
        Marker zoomMarker;
        while (position < last){
            zoomMarker = zoomMarkers.get(position);
            mZoomBuilder = mZoomBuilder.include(zoomMarker.getPosition());
        }

        //update the zoom to fit bounds
        mZoomBounds = mZoomBuilder.build();

        if (doZoom) zoomToFit();
    }


    /*******************************************************/
    /***********          Utilities          ***************/
    /*******************************************************/

    private Prism4DProject getOpenProject(){
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();
        return constantsAndUtilities.getOpenProject();

    }

    private int getOpenProjectID(){
        return getOpenProject().getProjectID();
    }
}


