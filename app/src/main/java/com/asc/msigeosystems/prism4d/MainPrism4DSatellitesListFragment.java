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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

import java.util.Date;
import java.util.List;

/**
 * The List Satellite Fragment is the UI
 * when the user can see the satellites visible to GPS
 * Created by Elisabeth Huhn on 5/15/2016.
 */
public class MainPrism4DSatellitesListFragment extends Fragment implements  GpsStatus.Listener,
                                                                            LocationListener,
                                                                            GpsStatus.NmeaListener{


    private static final String TAG = "LIST_PROJECTS_FRAGMENT";
    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private List<Prism4DSatellite>  mSatelliteList ;
    private RecyclerView            mRecyclerView;
    private Prism4DSatelliteAdapter mAdapter;

    private String                  mSatelliteID;
    private String                  mSatelliteElevation;
    private Date                    mSatelliteAzimuth;
    private String                  mSatelliteSignalToNoise;

    private Prism4DSatellite        mSelectedSatellite;
    private Prism4DSatellite        mLastSelectedSatellite;
    private int                     mSelectedPosition;

    private CharSequence            mSatellitePath;
    private LocationManager         mLocationManager;
    private Prism4DNmea             mNmeaData;
    private Prism4DNmeaParser       mNmeaParser = new Prism4DNmeaParser();


    /**********************************************************/
    //          Fragment Lifecycle Functions                  //
    /**********************************************************/


    //Constructor
    public MainPrism4DSatellitesListFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }


    //set up the recycler view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //1) Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_satellite_list_prism4d, container, false);
        v.setTag(TAG);

        initializeRecyclerView(v);

        ((MainPrism4DActivity) getActivity()).switchSubtitle(getString(R.string.subtitle_list_satellites));

        //turn on GPS and start listening for satellites
        //Make sure we have the proper GPS permissions before starting
        //If we don't currently have permission, bail
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){return v;}

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);


        return v;
    }

    private void initializeRecyclerView(View v){

        /*
         * The steps for doing recycler view in onCreateView() of a fragment are:
         * 1) inflate the .xml
         *
         * the special recycler view stuff is:
         * 2) get and store a reference to the recycler view widget that you created in xml
         * 3) create and assign a layout manager to the recycler view
         * 4) assure that there is data for the recycler view to show.
         * 5) use the data to create and set an adapter in the recycler view
         * 6) create and set an item animator (if desired)
         * 7) create and set a line item decorator
         * 8) add event listeners to the recycler view
         *
         * 9) return the view
         */

        //2) find and remember the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.satellitesList);

        //3) create and assign a layout manager to the recycler view
        RecyclerView.LayoutManager mLayoutManager  = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //4) read data in from the database and tell the adapter about it
        //   this is now done in the satellites container singleton

        //      get the singleton manager
        Prism4DSatelliteManager satelliteManager = Prism4DSatelliteManager.getInstance();
        //      then go get our list of satellites
        mSatelliteList = satelliteManager.getSatellites();

        //5) Use the data to Create and set out satellite Adapter
        mAdapter = new Prism4DSatelliteAdapter(mSatelliteList);
        mRecyclerView.setAdapter(mAdapter);

        //6) create and set the itemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //7) create and add the item decorator
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), LinearLayoutManager.VERTICAL));


        //8) add event listeners to the recycler view
        mRecyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {

                    @Override
                    public void onClick(View view, int position) {
                       onSelect(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        //No FOOTER on this screen

        //9) return the view

    }


    /************************************************************************/
    //                Fragment Lifecycle Functions                          //
    /***********************************************************************/

    //Ask for location events to start
    @Override
    public void onResume() {
        super.onResume();
        setSubtitle();

        //If we don't currently have permission, bail
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

    @Override
    public void onStop(){
        super.onStop();
    }

    //Ask for location events to stop
    @Override
    public void onPause() {
        super.onPause();
        //If we don't currently have permission, bail
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){return;}

        mLocationManager.removeGpsStatusListener(this);
        //mLocationManager.removeUpdates(this);
        mLocationManager.removeNmeaListener(this);
    }


    //*************** Listener Callback Routines *****************//
    //        Called by OS when a Listener condition is met       //
    //************************************************************//

    /**********************************************************************/
    /*           GPS NEMA Callback                                        */
    /**********************************************************************/
    @Override
    public void onNmeaReceived (long timestamp, String nmea) {

        //create an object with all the fields from the string
        //The parser updataes the satellite list
        mNmeaData = mNmeaParser.parse(nmea);
        if (mNmeaData != null) {

            int temp = 1; //so the debugger can stop somewhere
            temp++;
            //Actually need to check that this is a satellite sentence
            temp++;
            /*
            mNmeaList.add(mNmeaData);
                        */
            mRecyclerView.getAdapter().notifyDataSetChanged();
            //notify item inserted rather than data set changed
            //why this makes a difference, I don't know. But the other doesn't scroll
            mRecyclerView.getAdapter().notifyItemInserted(mRecyclerView.getAdapter().getItemCount());
            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());


        }
    }

    /**********************************************************************/
    /*           GPS Location Callbacks                                     */
    /**********************************************************************/

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

    /**********************************************************************/
    /*           GPS Status Callback                                      */
    /**********************************************************************/
    // The state is one of:
    //      GPS_EVENT_STARTED,
    //      GPS_EVENT_STOPPED,
    //      GPS_EVENT_FIRST_FIX ,
    //      GPS_EVENT_SATELLITE_STATUS
    @Override
    public void onGpsStatusChanged(int state) {
        setGpsStatus();
    }

    //OS calls this callback when the location has changed
    /**********************************************************************/
    /*           GPS Location Callback                                    */
    /**********************************************************************/
    @Override
    public void onLocationChanged(Location loc) {

    }



    //*********************** End of Callbacks *******************//
    //*********************** GPS Utilities *************************//


    //Update the UI with satellite status info from GPS
    protected void setGpsStatus(){
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //for now, do nothing
            //but leave the if stmt so we can set breakpoint
        }
    }


    /**********************************************************/
    //      Utility Functions used in handling events         //
    /**********************************************************/


    private void setSubtitle(){
        ((MainPrism4DActivity)getActivity())
                .switchSubtitle(R.string.subtitle_list_satellites);
    }


    //called from onClick(), executed when a satellite is selected
    private void onSelect(int position){
        //todo need to update selection visually
        mSelectedPosition = position;
        mSelectedSatellite = mSatelliteList.get(position);

        Toast.makeText(getActivity().getApplicationContext(),
                mSelectedSatellite.getSatelliteID() + " is selected!",
                Toast.LENGTH_SHORT).show();

    }



    //Add some code to improve the recycler view
    //Here is the interface for event handlers for Click and LongClick
    public interface ClickListener {
        void onClick    (View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainPrism4DSatellitesListFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context,
                                     final RecyclerView recyclerView,
                                     final MainPrism4DSatellitesListFragment.ClickListener
                                             clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}


