package com.asc.msigeosystems.prism4d;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

/**
 * The Cogo Fragment is the UI
 * when the user is 
 * Created by elisabethhuhn on 5/132016.
 */
public class MainPrism4DCogo1Fragment extends Fragment {

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




    public MainPrism4DCogo1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        //Key in points Button
        mPointsButton = (Button) v.findViewById(R.id.row1Button1);
        mPointsButton.setText(R.string.cogo_points_button_label);
        //the order of images here is left, top, right, bottom
        mPointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_411_keyinput, 0, 0);
        mPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_points_button_label,
                        Toast.LENGTH_SHORT).show();
            }
        });


        //Inverse Button
        mInverseButton = (Button) v.findViewById(R.id.row1Button2);
        mInverseButton.setText(R.string.cogo_inverse_button_label);
        mInverseButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_412_inverse, 0, 0);
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
        mIntersectionsButton = (Button) v.findViewById(R.id.row1Button3);
        mIntersectionsButton.setText(R.string.cogo_intersections_button_label);
        mIntersectionsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_413_intersections, 0, 0);
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
        mTrianglesButton = (Button) v.findViewById(R.id.row2Button1);
        mTrianglesButton.setText(R.string.cogo_triangles_button_label);
        mTrianglesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_414_triangles, 0, 0);
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
        mCurvesButton = (Button) v.findViewById(R.id.row2Button2);
        mCurvesButton.setText(R.string.cogo_curves_button_label);
        mCurvesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_415_curves, 0, 0);
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
        mAreasButton = (Button) v.findViewById(R.id.row2Button3);
        mAreasButton.setText(R.string.cogo_areas_button_label);
        mAreasButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_416_areas, 0, 0);
        mAreasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_areas_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //coordinates Button
        mCoordinatesButton = (Button) v.findViewById(R.id.row3Button1);
        mCoordinatesButton.setText(R.string.cogo_coordinates_button_label);
        //the order of images here is left, top, right, bottom
        mCoordinatesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_417_coordinates, 0, 0);
        mCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.cogo_coordinates_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Map Check Button
        mMapCheckButton = (Button) v.findViewById(R.id.row3Button2);
        mMapCheckButton.setText(R.string.cogo_map_check_button_label);
        //mMapCheckButton.setText(R.string.cogo_workflow_button_label);
        mMapCheckButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_418_mapcheck, 0, 0);
        mMapCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        //R.string.cogo_map_check_button_label,
                        R.string.cogo_workflow_button_label,
                        Toast.LENGTH_SHORT).show();

                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToCoordWorkflow();
                }

            }
        });

        //T Button
        mTButton = (Button) v.findViewById(R.id.row3Button3);
        mTButton.setText(R.string.cogo_convert_button_label);
        mTButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_419_translate, 0, 0);
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


