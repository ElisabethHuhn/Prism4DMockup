package com.asc.msigeosystems.prism4dmockup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Project Fragment is the UI
 * when the user is creating / making changes to the project definition
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DCollect1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mPointsButton;
    private Button mLinesButton;
    private Button mAreasButton;

    private Button mAutoStoreButton;
    private Button mGPSBaseStnButton;
    private Button mTotalStnButton;

    private Button mTraverseButton;
    private Button mGalleryButton;
    private Button mNotesButton;


    //footer
    //footer left button
    private Button mEscButton;
    //footer row 1
    private TextView mCurrentFilenameField;
    //footer row 2
    private TextView mModelField;
    private TextView mSnField;
    //footer row 3
    private TextView mTrackingField;
    private TextView mModeField;
    //footer row 4
    private TextView mHorizField;
    private TextView mVertField;
    //footer row 5
    private TextView mRmsField;
    private TextView mPdopField;
    //footer right button
    private Button mEnterButton;


    public MainPrism4DCollect1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        //Measure Points Button
        mPointsButton = (Button) v.findViewById(R.id.row1Button1);
        mPointsButton.setText(R.string.collect_points_button_label);
        //the order of images here is left, top, right, bottom
        mPointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
        mPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the collect with maps fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToCollect11PointsScreen();
                }
            }
        });

        //Measure Lines Button
        mLinesButton = (Button) v.findViewById(R.id.row1Button2);
        mLinesButton.setText(R.string.collect_lines_button_label);
        //the order of images here is left, top, right, bottom
        mLinesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
        mLinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.collect_lines_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Measure Areas Button
        mAreasButton = (Button) v.findViewById(R.id.row3Button3);
        mAreasButton.setText(R.string.collect_areas_button_label);
        mAreasButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
        mAreasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.collect_areas_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Autostore Button
        mAutoStoreButton = (Button) v.findViewById(R.id.row1Button3);
        mAutoStoreButton.setText(R.string.autostore_button_label);
        mAutoStoreButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
        mAutoStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.autostore_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //GPS Base Station Button
        mGPSBaseStnButton = (Button) v.findViewById(R.id.row2Button1);
        mGPSBaseStnButton.setText(R.string.gps_base_station_button_label);
        mGPSBaseStnButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
        mGPSBaseStnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.gps_base_station_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Total Station Button
        mTotalStnButton = (Button) v.findViewById(R.id.row2Button2);
        mTotalStnButton.setText(R.string.total_station_button_label);
        mTotalStnButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
        mTotalStnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.total_station_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Traverse Button
        mTraverseButton = (Button) v.findViewById(R.id.row2Button3);
        mTraverseButton.setText(R.string.traverse_button_label);
        mTraverseButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
        mTraverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.traverse_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Gallery Button
        mGalleryButton = (Button) v.findViewById(R.id.row3Button1);
        mGalleryButton.setText(R.string.gallery_button_label);
        mGalleryButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
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
        mNotesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_collect, 0, 0);
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


