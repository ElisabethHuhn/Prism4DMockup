package com.asc.msigeosystems.prism4dmockup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.List;

/**
 * The List Nmea Fragment is the UI
 * for the user to see the NMEA Sentences received from GPS
 * Created by elisabethhuhn on 5/8/2016.
 */
public class MainPrism4DListNmeaFragment extends Fragment implements GpsStatus.Listener, LocationListener, GpsStatus.NmeaListener{

    private static final String TAG = "LIST_NMEA_FRAGMENT";
    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private List<Prism4DNmea>   mNmeaList = new ArrayList<>();
    private RecyclerView        mRecyclerView;
    private Prism4DNmeaAdapter  mAdapter;



    private LocationManager     mLocationManager;
    private CharSequence        mNmeaSentence;
    private Prism4DNmea         mNmeaData;
    private Prism4DNmeaParser   mNmeaParser = new Prism4DNmeaParser();

    private Prism4DNmea         mSelectedNmea;
    private int                 mSelectedPosition;

    Handler                     mHandler;
    long                        mClockSkew;
    LastFixUpdater              mLastFixUpdater;
    long                        mLastUpdateTime = -1;

    private GpsStatus mGpsStatus = null;



    /**********************************************************/
    //          Fragment Lifecycle Functions                  //
    /**********************************************************/


    //Constructor
    public MainPrism4DListNmeaFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        //1) Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nmea_list_prism4d, container, false);
        v.setTag(TAG);

        //2) find and remember the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.nmeaList);


        // The RecyclerView.LayoutManager defines how elements are laid out.
        //3) create and assign a layout manager to the recycler view
        RecyclerView.LayoutManager mLayoutManager  = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //4) create some dummy data and tell the adapter about it
        // we actually aren't going to do that, it'll be enough
        // when NMEA Sentences come from GPA
        Prism4DNmeaContainer nmeaContainer = Prism4DNmeaContainer.getInstance();

        //      then go get our list of nmea
        mNmeaList = nmeaContainer.getNmeaList();

        //5) Use the data to Create and set the Adapter
        mAdapter = new Prism4DNmeaAdapter(mNmeaList);
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
                        //for now, ignore the long click
                    }
                }));

        //Now take care of the GPS Stuff
        //GPS Stuff
        //Make sure we have the proper GPS permissions before starting
        //If we don't currently have permission, bail
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){return v;}

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        //but don't turn them on until onResume()
        //mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        //mLocationManager.addGpsStatusListener(this);
        //mLocationManager.addNmeaListener(this);

        //9) return the view
        return v;
    }

    /************************************************************************/
    //                Fragment Lifecycle Functions                          //
    /***********************************************************************/

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
        //mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
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
        //mLocationManager.removeGpsStatusListener(this);
        //mLocationManager.removeUpdates(this);
        mLocationManager.removeNmeaListener(this);
    }


    //*************** Listener Callback Routines *****************//
    //        Called by OS when a Listener condition is met       //
    //************************************************************//

    @Override
    public void onNmeaReceived (long timestamp, String nmea){
        //create an object with all the fields from the string
        mNmeaData = mNmeaParser.parse(nmea);
        mNmeaList.add(mNmeaData);

    }


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

    private void updateLastUpdateTime(){
        if (mLastUpdateTime >= 0){
            long t = Math.round((System.currentTimeMillis() - mLastUpdateTime - mClockSkew) / 1000);
            long sec = t % 60;
            long min = (t / 60);
            //mTslf.setData(String.format("%d:%02d", min, sec));
        }
        //mDeviceTime.setData(System.currentTimeMillis());
    }







    //************************ Utilities *************************//

    //executed when an item in the list is selected
    private void onSelect(int position){
        mSelectedPosition = position;
        mSelectedNmea = mNmeaList.get(position);
        Toast.makeText(getActivity().getApplicationContext(),
                String.valueOf(mSelectedNmea.getNmeaType()) + " is selected!",
                Toast.LENGTH_SHORT).show();

    }

    //Add some code to improve the recycler view
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainPrism4DListNmeaFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context,
                                     final RecyclerView recyclerView,
                                     final MainPrism4DListNmeaFragment.ClickListener
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


