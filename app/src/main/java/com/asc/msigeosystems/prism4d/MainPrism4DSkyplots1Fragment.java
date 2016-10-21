package com.asc.msigeosystems.prism4d;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

/**
 * The Stakeout Fragment is the top level selection UI
 * for stakeout functions
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DSkyplots1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons

    private Button mSkyplotsButton;
    private Button mSatInfoButton;
    private Button mQcPositionButton;

    private Button mAccelerometerButton;
    private Button mCompassButton;
    private Button mGeocashingButton;


    private Button mWaypointsButton;
    private Button mNavigateButton;
    private Button mNoaaButton;





    public MainPrism4DSkyplots1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later
        //Wire up the UI widgets so they can handle events later
        wireWidgets(v);

        return v;
    }

    private void wireWidgets(View v){


        //Skyplots Button
        mSkyplotsButton = (Button) v.findViewById(R.id.row1Button1);
        mSkyplotsButton.setText(R.string.skyplot_plots_button_label);
        mSkyplotsButton.setText(R.string.skyplot_ygps);
        mSkyplotsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_611_skyplots, 0, 0);
        mSkyplotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_plots_button_label,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), YGPS.class);
                startActivity(intent);

            }
        });


        //Sat Info Button
        mSatInfoButton = (Button) v.findViewById(R.id.row1Button2);
        mSatInfoButton.setText(R.string.skyplot_satinfo_button_label);
        mSatInfoButton.setText(R.string.skyplot_list_satellites);
        mSatInfoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_612_qcposition, 0, 0);
        mSatInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_satinfo_button_label,
                        Toast.LENGTH_SHORT).show();
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToListSatellitesScreen();
                }


            }
        });


        //QC Position Button
        mQcPositionButton = (Button) v.findViewById(R.id.row1Button3);
        mQcPositionButton.setText(R.string.skyplot_qc_position_button_label);
        mQcPositionButton.setText(R.string.skyplot_nmea_activity);
        //the order of images here is left, top, right, bottom
        mQcPositionButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_613_qcpositions, 0, 0);
        mQcPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_qc_position_button_label,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Prism4DGPSActivity.class);
                startActivity(intent);


            }
        });




        //Accelerometer Button
        mAccelerometerButton = (Button) v.findViewById(R.id.row2Button1);
        mAccelerometerButton.setText(R.string.skyplot_accelerometer_button_label);
        //mAccelerometerButton.setText(R.string.skyplot_nmea_fragment);
        //the order of images here is left, top, right, bottom
        mAccelerometerButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_614_accelerometer, 0, 0);
        mAccelerometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_accelerometer_button_label,
                        Toast.LENGTH_SHORT).show();
                /*
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToGpsNmeaScreen();
                }
                */

            }
        });



        //Compass Button
        mCompassButton = (Button) v.findViewById(R.id.row2Button2);
        mCompassButton.setText(R.string.skyplot_compas_button_label);
        mCompassButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_615_compass, 0, 0);
        mCompassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_compas_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Geocashing Button
        mGeocashingButton = (Button) v.findViewById(R.id.row2Button3);
        mGeocashingButton.setText(R.string.skyplot_geocasching_button_label);
        //mGeocashingButton.setText(R.string.skyplot_list_nmea);
        mGeocashingButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_616_geocaching, 0, 0);
        mGeocashingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_reference_button_label,
                        Toast.LENGTH_SHORT).show();
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToListNmeaScreen();
                }


            }
        });


        //Waypoints Button
        mWaypointsButton = (Button) v.findViewById(R.id.row3Button1);
        mWaypointsButton.setText(R.string.skyplot_waypoints_button_label);
        mWaypointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_617_waypoints, 0, 0);
        mWaypointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_waypoints_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Navigate Button
        mNavigateButton = (Button) v.findViewById(R.id.row3Button2);
        mNavigateButton.setText(R.string.skyplot_navigate_button_label);
        mNavigateButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_618_navigate, 0, 0);
        mNavigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_navigate_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //NOAA Button
        mNoaaButton = (Button) v.findViewById(R.id.row3Button2);
        mNoaaButton.setText(R.string.skyplot_noaa_button_label);
        mNoaaButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_619_swpc, 0, 0);
        mNoaaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_noaa_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


    }
}


