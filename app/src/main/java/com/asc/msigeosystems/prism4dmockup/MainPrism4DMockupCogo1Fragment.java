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
 * The Cogo Fragment is the UI
 * when the user is 
 * Created by elisabethhuhn on 5/132016.
 */
public class MainPrism4DMockupCogo1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mPointsButton;
    private Button mCoordinatesButton;
    private Button mInverseButton;

    private Button mIntersectionsButton;
    private Button mTrianglesButton;
    private Button mCurvesButton;

    private Button mAreasButton;
    private Button mMapCheckButton;
    private Button mTButton;


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


    public MainPrism4DMockupCogo1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4_dmockup, container, false);


        //Wire up the UI widgets so they can handle events later

        //Key in points Button
        mPointsButton = (Button) v.findViewById(R.id.row1Button1);
        mPointsButton.setText(R.string.cogo_points_button_label);
        //the order of images here is left, top, right, bottom
        mPointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_points_button_label,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //coordinates Button
        mCoordinatesButton = (Button) v.findViewById(R.id.row1Button2);
        mCoordinatesButton.setText(R.string.cogo_coordinates_button_label);
        //the order of images here is left, top, right, bottom
        mCoordinatesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_coordinates_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Inverse Button
        mInverseButton = (Button) v.findViewById(R.id.row1Button3);
        mInverseButton.setText(R.string.cogo_inverse_button_label);
        mInverseButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mInverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_inverse_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Intersections Button
        mIntersectionsButton = (Button) v.findViewById(R.id.row2Button1);
        mIntersectionsButton.setText(R.string.cogo_intersections_button_label);
        mIntersectionsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mIntersectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_intersections_button_label,

                        Toast.LENGTH_SHORT).show();

            }
        });

        //Triangles Button
        mTrianglesButton = (Button) v.findViewById(R.id.row2Button2);
        mTrianglesButton.setText(R.string.cogo_triangles_button_label);
        mTrianglesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mTrianglesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_triangles_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Curves Button
        mCurvesButton = (Button) v.findViewById(R.id.row2Button3);
        mCurvesButton.setText(R.string.cogo_curves_button_label);
        mCurvesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mCurvesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_curves_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Areas Button
        mAreasButton = (Button) v.findViewById(R.id.row3Button1);
        mAreasButton.setText(R.string.cogo_areas_button_label);
        mAreasButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mAreasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_areas_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Map Check Button
        mMapCheckButton = (Button) v.findViewById(R.id.row3Button2);
        mMapCheckButton.setText(R.string.cogo_map_check_button_label);
        mMapCheckButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mMapCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_map_check_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //T Button
        mTButton = (Button) v.findViewById(R.id.row3Button3);
        mTButton.setText(R.string.cogo_convert_button_label);
        mTButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cogo, 0, 0);
        mTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToConvertScreen();
                }

            }
        });



        //FOOTER WIDGETS

        //  Esc and Enter buttons are desabled on this screen
        //      so do nothing in the footer




        return v;
    }
}


