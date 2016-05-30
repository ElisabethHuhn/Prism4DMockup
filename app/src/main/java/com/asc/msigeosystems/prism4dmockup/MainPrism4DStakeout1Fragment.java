package com.asc.msigeosystems.prism4dmockup;

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
public class MainPrism4DStakeout1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mPointsButton;
    private Button mLinesButton;
    private Button mArcButton;

    private Button mOffsetsButton;
    private Button mGPSBaseStnButton;
    private Button mTotalStnButton;

    private Button mReportButton;
    private Button mGalleryButton;
    private Button mNotesButton;




    public MainPrism4DStakeout1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        //Stake Points Button
        mPointsButton = (Button) v.findViewById(R.id.row1Button1);
        mPointsButton.setText(R.string.stakeout_points_button_label);
        //the order of images here is left, top, right, bottom
        mPointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_points_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });




        //Measure Lines Button
        mLinesButton = (Button) v.findViewById(R.id.row1Button2);
        mLinesButton.setText(R.string.stakeout_lines_button_label);
        //the order of images here is left, top, right, bottom
        mLinesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mLinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_lines_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Arcs Button
        mArcButton = (Button) v.findViewById(R.id.row3Button3);
        mArcButton.setText(R.string.stakeout_arcs_button_label);
        mArcButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mArcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_arcs_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Offset Button
        mOffsetsButton = (Button) v.findViewById(R.id.row1Button3);
        mOffsetsButton.setText(R.string.stakeout_offset_button_label);
        mOffsetsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mOffsetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_offset_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //GPS Base Setup Button
        mGPSBaseStnButton = (Button) v.findViewById(R.id.row2Button1);
        mGPSBaseStnButton.setText(R.string.gps_base_station_button_label);
        mGPSBaseStnButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mGPSBaseStnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.gps_base_station_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Total Station Setup Button
        mTotalStnButton = (Button) v.findViewById(R.id.row2Button2);
        mTotalStnButton.setText(R.string.total_station_button_label);
        mTotalStnButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mTotalStnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.total_station_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Stakeout Report Button
        mReportButton = (Button) v.findViewById(R.id.row2Button3);
        mReportButton.setText(R.string.stakeout_report_button_label);
        mReportButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_report_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Gallery Button
        mGalleryButton = (Button) v.findViewById(R.id.row3Button1);
        mGalleryButton.setText(R.string.gallery_button_label);
        mGalleryButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.gallery_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Notes Button
        mNotesButton = (Button) v.findViewById(R.id.row3Button2);
        mNotesButton.setText(R.string.notes_button_label);
        mNotesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stakeout, 0, 0);
        mNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.notes_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });





        //FOOTER WIDGETS

        //  Esc and Enter buttons are NOT enabled on the collect screen
        //so we can ignore the footer for now


        return v;
    }
}


