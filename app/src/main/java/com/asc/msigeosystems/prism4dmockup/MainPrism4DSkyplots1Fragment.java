package com.asc.msigeosystems.prism4dmockup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
    private Button MGpsPositionButton;
    private Button mQcPositionButton;
    private Button mSkyplotsButton;

    private Button mSatInfoButton;
    private Button mReferenceButton;
    private Button mCompassButton;

    private Button mWaypointsButton;
    private Button mNavigateButton;
    private Button mSaveButton;





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

        //GPS Position Button
        MGpsPositionButton = (Button) v.findViewById(R.id.row1Button1);
        MGpsPositionButton.setText(R.string.skyplot_gps_position_button_label);
        //the order of images here is left, top, right, bottom
        MGpsPositionButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
        MGpsPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_gps_position_button_label,
                        Toast.LENGTH_SHORT).show();
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToGpsNmeaScreen();
                }

            }
        });




        //QC Position Button
        mQcPositionButton = (Button) v.findViewById(R.id.row1Button2);
        mQcPositionButton.setText(R.string.skyplot_qc_position_button_label);
        //the order of images here is left, top, right, bottom
        mQcPositionButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
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


        //Skyplots Button
        mSkyplotsButton = (Button) v.findViewById(R.id.row3Button3);
        mSkyplotsButton.setText(R.string.skyplot_plots_button_label);
        mSkyplotsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
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
        mSatInfoButton = (Button) v.findViewById(R.id.row1Button3);
        mSatInfoButton.setText(R.string.skyplot_satinfo_button_label);
        mSatInfoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
        mSatInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_satinfo_button_label,
                        Toast.LENGTH_SHORT).show();
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    //myActivity.switchToListNmeaScreen();
                }


            }
        });

        //Reference Button
        mReferenceButton = (Button) v.findViewById(R.id.row2Button1);
        mReferenceButton.setText(R.string.skyplot_reference_button_label);
        mReferenceButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
        mReferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_reference_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Compass Button
        mCompassButton = (Button) v.findViewById(R.id.row2Button2);
        mCompassButton.setText(R.string.skyplot_compas_button_label);
        mCompassButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
        mCompassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_compas_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Waypoints Button
        mWaypointsButton = (Button) v.findViewById(R.id.row2Button3);
        mWaypointsButton.setText(R.string.skyplot_waypoints_button_label);
        mWaypointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
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
        mNavigateButton = (Button) v.findViewById(R.id.row3Button1);
        mNavigateButton.setText(R.string.skyplot_navigate_button_label);
        mNavigateButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
        mNavigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_navigate_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Save Button
        mSaveButton = (Button) v.findViewById(R.id.row3Button2);
        mSaveButton.setText(R.string.skyplot_save_button_label);
        mSaveButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_skyplot, 0, 0);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_save_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        return v;
    }
}


