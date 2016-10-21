package com.asc.msigeosystems.prism4d;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

/**
 * The Collect Fragment is the UI
 * when the user is making point measurements in the field
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DCollect11PointsFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Buttons on left margin of screen
    private Button mMapsButton;
    private Button mPictureButton;
    private Button mNotesButton;

    //Widgets below drawing area
    private ImageButton mZoomInButton;
    private ImageButton mZoomOutButton;
    private ImageButton mZoomExtButton;
    private TextView mScaleFactorField;

    //Widgets to right of drawing area
    private TextView mCurrentPositionHeader;
    private TextView mPointIDField;
    private TextView mCurrentNorthingPositionField;
    private TextView mCurrentEastingPositionField;
    private TextView mCurrentElevationField;
    private TextView mCurrentFunctionCodeField;
    private TextView mCurrentHeightField;
    private Button mStorePositionButton;
    private Button mOffsetPositionButton;
    private Button mAveragePositionButton;


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


    public MainPrism4DCollect11PointsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_collect_11_points_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields
        //Wire up the UI widgets so they can handle events later
        wireWidgets(v);

        return v;
    }

    private void wireWidgets(View v){

        //ON THE LEFT PORTION OF THE MAIN DRAWING AREA

        //maps Button
        mMapsButton = (Button) v.findViewById(R.id.mapsButtonCollect);
        mMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //Switch the fragment to the collect with maps fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToCollect11PointsWithMapScreen();
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

        //UNDER THE DRAWING AREA
        //ZOOMIN Button
        mZoomInButton = (ImageButton) v.findViewById(R.id.zoomInButton);
        mZoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.zoom_in_button_label,
                        Toast.LENGTH_SHORT).show();

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

            }
        });


        //WIDGETS TO THE RIGHT OF THE MAIN DRAWING AREA
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


        //FOOTER WIDGETS

        //  unlike the home screen
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
}


