package com.asc.msigeosystems.prism4d;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;



/**
 * The Collect Fragment is the UI
 * when the user is making point measurements in the field
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DCollectPointsFragment extends Fragment implements //OnMapReadyCallback,
                                                                          GpsStatus.Listener,
                                                                          LocationListener,
                                                                          GpsStatus.NmeaListener {

    private static String TAG = "CollectPointsFragment";

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
    private TextView mGdopField;
    //footer right button
    private Button   mEnterButton;


    /*******************************************/
    /****     variables for processing      ****/
    /*******************************************/

    //variables needed to keep track of location
    private LocationManager     mLocationManager;
    private Location            mCurLocation;
    private Prism4DNmea         mNmeaData;     //latest nmea sentence received
    private Prism4DNmeaParser   mNmeaParser = new Prism4DNmeaParser();
    private GpsStatus           mGpsStatus = null;
    private boolean             isGpsOn = true;


    //variables for the meaning process
    private boolean isMeanInProgress = false;
    private boolean isFirstPointInMean = false;
    private boolean isLastPointInMean = false;


    //Coordinates and Points variablese
    private Prism4DCoordinateWGS84 mMeanCoordinateWGS84;
    private List<Prism4DCoordinateWGS84> mMeanWgs84List;

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

    private PolylineOptions mLineContainer;
    private Polyline        myLine;

    private LatLngBounds.Builder mZoomBuilder;
    private LatLngBounds         mZoomBounds;



    /**********************************************************************/
    /*                Constructor                                         */
    /**********************************************************************/
    public MainPrism4DCollectPointsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_collect_points_prism4d, container, false);

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

            if (mNmeaData != null){
                //update the UI
                updateNmeaUI(mNmeaData);
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


    private void updateNmeaUI(Prism4DNmea nmeaData){

        if (nmeaData != null){
            //Which fields have meaning depend upon the type of the sentence
            String type = nmeaData.getNmeaType().toString();
            if (type != null){

                Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();
                int openProjectID = constantsAndUtilities.getOpenProjectID();
                //project has to be open
                if (openProjectID == 0) return;
                int coordinateType = Prism4DCoordinate.getCoordinateTypeFromProjectID(openProjectID);
                String nLable = "N: ";
                String eLable = "E: ";

                if (coordinateType == Prism4DCoordinate.sLLWidgets) {

                    nLable = "Lat: ";
                    eLable = "Long: ";
                }

                double nLat, eLong, ele;


                if ((type.contains("GGA")) || (type.contains("GNS")) ){
                    //mNmeaSentenceOutput.setText(nmeaData.getNmeaSentence());
                    //mTimeOutput.setText(Double.toString(nmeaData.getTime()));
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

                    BigDecimal bdTemp = new BigDecimal(nLat).
                            setScale(Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision,
                                    RoundingMode.HALF_UP);
                    nLat = bdTemp.doubleValue();

                    bdTemp = new BigDecimal(eLong).
                            setScale(Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision,
                                    RoundingMode.HALF_UP);
                    eLong = bdTemp.doubleValue();

                    ele = nmeaData.getOrthometricElevation();
                    bdTemp = new BigDecimal(ele).
                            setScale(Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision,
                                    RoundingMode.HALF_UP);
                    ele = bdTemp.doubleValue();

                    mCurrentNorthingPositionField.setText(nLable + Double.toString(nLat));
                    mCurrentEastingPositionField .setText(eLable + Double.toString(eLong));

                    //mSatellitesOutput.setText(Integer.toString(nmeaData.getSatellites()));
                    mHdopField            .setText("HDop: "+Double.toString(nmeaData.getHdop()));
                    mCurrentElevationField.setText("Ele "+Double.toString(ele));

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
                }


            } else {
                //there was an exception processing the NMEA Sentence
                Toast toast = Toast.makeText(getActivity(), "Null type found", Toast.LENGTH_SHORT);
                toast.show();
            }


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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
*/
/*
    private void addMapsView(View v){

        //Determine the width and height needed
        LinearLayout currentPositionContainer =
                                    (LinearLayout)v.findViewById(R.id.current_position_container);
        int cpWidth  = currentPositionContainer.getWidth();
        int cpHeight = currentPositionContainer.getHeight();

        LinearLayout drawingButtonsContainer =
                                    (LinearLayout) v.findViewById(R.id.drawing_buttons_container);
        int dbWidth  = drawingButtonsContainer.getWidth();
        int dbHeight = drawingButtonsContainer.getHeight();

        int mapsWidth  = dbWidth;
        int mapsHeight = cpHeight - dbHeight;

        //we need the inflater and the LinearLayout container for the maps
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(getActivity());
        //Get the LinearLayout which will contain the coordinates
        LinearLayout mapsContainer = (LinearLayout) v.findViewById(R.id.maps_container);

        //inflate the map and attach to the maps container
        View mapsLayou = layoutInflater.inflate(R.layout.element_maps_view,
                                                mapsContainer,
                                                true);


    }
*/
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

                //current location control depends upon permissions
                boolean locEnabled = false;
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                                                            == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                                                            == PackageManager.PERMISSION_GRANTED) {

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


                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                // TODO: 12/10/2016 make the minZoomLevel a constant somewhere
                int minZoomLevel = 8;
                //Make sure we don't zoom in too close
                mMap.setMinZoomPreference(minZoomLevel);
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
        mCurrentFunctionCodeField     = (EditText)v.findViewById(R.id.currentFunctionCodeField);
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
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.picture_button_label,
                        Toast.LENGTH_SHORT).show();

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
        mGdopField            = (TextView)v.findViewById(R.id.gdop_field);
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
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.no_save,
                        Toast.LENGTH_SHORT).show();
                //Switch the fragment to the previous fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToPopBackstack();
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
    /******** Does everything necessary to handle       *********/
    /********  Pressing the Current Position Button     *********/
    /************************************************************/
    private void handleCurrentPosition(){
        //set screen focus to the current position
        //Current position fields are updated as a result of the GPS event


        //For testing purposes:
        //   o Turn off GPS
        stopGps();

        //   o Get the next Test Data Point
        Prism4DTestLocationDataManager testLocationDataManager = Prism4DTestLocationDataManager.getInstance();
        mTestData = testLocationDataManager.get(mTestDataCounter);
        mTestDataCounter++;
        if (mTestDataCounter >= mTestDataMax)mTestDataCounter = 0;

        //Convert the Test Data Point to GPS digital degrees
        if (mTestData == null) {
            startGps();
            Toast.makeText(getActivity(), R.string.test_data_invalid, Toast.LENGTH_SHORT).show();
            throw new RuntimeException(getString(R.string.test_data_invalid));

        } else {
            Prism4DCoordinateWGS84 wgsCoordinate =
                    new Prism4DCoordinateWGS84 (
                            mTestData.getLatitudeDegrees(),  mTestData.getLatitudeMinutes(),  mTestData.getLatitudeSeconds(),
                            mTestData.getLongitudeDegrees(), mTestData.getLongitudeMinutes(), mTestData.getLongitudeSeconds());

            wgsCoordinate.setElevation(mTestData.getElevation());
            if (!wgsCoordinate.isValidCoordinate()) {
                startGps();
                Toast.makeText(getActivity(), R.string.test_data_invalid, Toast.LENGTH_SHORT).show();
                throw new RuntimeException(getString(R.string.test_data_invalid));
            }

            //update the screen with the location
            //build a nmeaData with the TestData location
            mNmeaData = new Prism4DNmea();
            //mNmeaData.setNmeaType("GGA");//set to GPGGA when initialized
            mNmeaData.setLatitude (wgsCoordinate.getLatitude());
            mNmeaData.setLongitude(wgsCoordinate.getLongitude());
            mNmeaData.setOrthometricElevation(wgsCoordinate.getElevation());

            //double check conversion to northing/easting
            Prism4DCoordinateUTM utmCoordinate = new Prism4DCoordinateUTM(wgsCoordinate);
            double tempTestNorthing = mTestData.getNorthing();
            double tempUTMNorthing  = utmCoordinate.getNorthing();
            double tempTestEasting  = mTestData.getEasting();
            double tempUTMEasting   = utmCoordinate.getEasting();

            // TODO: 12/10/2016 get rid of these debug statements 
            //check the different values of elevation
            double tempElevation    = mTestData.getElevation();
            tempElevation           = wgsCoordinate.getElevation();
            tempElevation           = utmCoordinate.getElevation();

            //update screen with the faked nmea data
            updateNmeaUI(mNmeaData);

            String markerName = getString(R.string.collect_points_test_marker) +
                                          " " + String.valueOf(mTestDataCounter);

            double latitude = wgsCoordinate.getLatitude();
            double longitude = wgsCoordinate.getLongitude();
            //update the maps
            LatLng myPoint = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(myPoint)
                                              .title(markerName)
                                              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            //CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
            //get zoom level from the mpa
            mMapZoom = mMap.getCameraPosition().zoom;

            CameraUpdate myZoom = CameraUpdateFactory.newLatLngZoom(myPoint, 15);
            mMap.animateCamera(myZoom);

            //add the new point to the line
            if (mLineContainer == null){
                mLineContainer = new PolylineOptions();
            }
            mLineContainer.add(myPoint).add(myPoint).width(25).color(Color.BLUE).geodesic(true);
            myLine = mMap.addPolyline(mLineContainer);
            //The way to find out how many points we've processed so far
            //myLine.getPoints().size();

            //now update the zoom boundary around the set of points
            if (mZoomBuilder == null){
                mZoomBuilder = new LatLngBounds.Builder();
                mPointsPlotted = 0;
            }
            mZoomBuilder = mZoomBuilder.include(myPoint);
            //update the zoom to fit bounds
            mZoomBounds = mZoomBuilder.build();
            mPointsPlotted++;

            zoomToFit();
        }
    }

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


}


