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
public class MainPrism4DMockupProject1NoIconsFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Buttons on left margin of screen
    private Button mCreateButton;
    private Button mOpenButton;
    private Button mCopyButton;
    private Button mEditButton;
    private Button mDeleteButton;
    private Button mControlButton;
    private Button mListPointsButton;
    private Button mFeatureCodesButton;
    private Button mExchangeButton;


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


    public MainPrism4DMockupProject1NoIconsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_project_1_noicons_prism4_dmockup, container, false);


        //Wire up the UI widgets so they can handle events later

        //Create Button
        mCreateButton = (Button) v.findViewById(R.id.createButton);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            ///for now, just put up a toast that the button was pressed
            Toast.makeText(getActivity(),
                    R.string.create_button_label,
                    Toast.LENGTH_SHORT).show();

            }
        });

        //open Button
        mOpenButton = (Button) v.findViewById(R.id.openButton);
        mOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.open_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //copy Button
        mCopyButton = (Button) v.findViewById(R.id.copyButton);
        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.copy_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Edit Button
        mEditButton = (Button) v.findViewById(R.id.editButton);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.edit_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Delete Button
        mDeleteButton = (Button) v.findViewById(R.id.deleteButton);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.delete_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Control Button
        mControlButton = (Button) v.findViewById(R.id.controlButton);
        mControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.control_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //List Points Button
        mCreateButton = (Button) v.findViewById(R.id.listPointsButton);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.list_points_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Feature Codes Button
        mFeatureCodesButton = (Button) v.findViewById(R.id.featureCodesButton);
        mFeatureCodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.feature_codes_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Exchange Button
        mExchangeButton = (Button) v.findViewById(R.id.exchangeButton);
        mExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.exchange_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });



        //FOOTER WIDGETS

        //  unlike the home screen
        //  Esc and Enter buttons are enabled on the collect screen

        //Esc Button
        mEscButton = (Button) v.findViewById(R.id.escButton);
        mEscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.esc_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Enter Button
        mEnterButton = (Button) v.findViewById(R.id.enterButton);
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            //for now, just put up a toast that the button was pressed
            Toast.makeText(getActivity(),
                    R.string.enter_button_label,
                    Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}


