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
 * The Project Fragment is the UI
 * when the user is creating / making changes to the project definition
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DProject1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mCreateButton;
    private Button mOpenButton;
    private Button mCopyButton;

    private Button mEditButton;
    private Button mDeleteButton;
    private Button mControlButton;

    private Button mListPointsButton;
    private Button mFeatureCodesButton;
    private Button mExchangeButton;





    public MainPrism4DProject1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        //Create Button
        mCreateButton = (Button) v.findViewById(R.id.row1Button1);
        mCreateButton.setText(R.string.create_button_label);
        //the order of images here is left, top, right, bottom
        mCreateButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_111_createfolder, 0, 0);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the collect with maps fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToProject11CreateScreen();
                }
            }
        });

        //open Button
        mOpenButton = (Button) v.findViewById(R.id.row1Button2);
        mOpenButton.setText(R.string.open_button_label);
        //the order of images here is left, top, right, bottom
        mOpenButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_112_openfolder, 0, 0);
        mOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToProject12OpenScreen();
                }

            }
        });


        //copy Button
        mCopyButton = (Button) v.findViewById(R.id.row1Button3);
        mCopyButton.setText(R.string.copy_button_label);
        mCopyButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_113_copyfolder, 0, 0);
        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToProject13CopyScreen();
                }

            }
        });

        //Edit Button
        mEditButton = (Button) v.findViewById(R.id.row2Button1);
        mEditButton.setEnabled(false);
        mEditButton.setText(R.string.edit_button_label);
        mEditButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_114_editfolder, 0, 0);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(getActivity(),
                        R.string.edit_button_label,
                        Toast.LENGTH_SHORT).show();
/***
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToProject14EditScreen();
                }
***/
            }
        });

        //Delete Button
        mDeleteButton = (Button) v.findViewById(R.id.row2Button2);
        mDeleteButton.setText(R.string.delete_button_label);
        mDeleteButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_115_deletefolder, 0, 0);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToProject15DeleteScreen();
                }

            }
        });


        //Control Button
        mControlButton = (Button) v.findViewById(R.id.row2Button3);
        mControlButton.setText(R.string.control_button_label);
        mControlButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_116_controlfile, 0, 0);
        mControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.control_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Maintain Points Button
        mListPointsButton = (Button) v.findViewById(R.id.row3Button1);
        mListPointsButton.setText(R.string.maintain_points_button_label);
        mListPointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_117_pointsfile, 0, 0);
        mListPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
/***
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToMaintainPoints1Screen();
                }
***/
                ///for now, tell the user to go to maintain project screen
                Toast.makeText(getActivity(),
                        "Maintain points from project screen. Go open the project of interest",
                        Toast.LENGTH_LONG).show();
            }
        });

        //Feature Codes Button
        mFeatureCodesButton = (Button) v.findViewById(R.id.row3Button2);
        mFeatureCodesButton.setText(R.string.feature_codes_button_label);
        mFeatureCodesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_118_fcfile, 0, 0);
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
        mExchangeButton = (Button) v.findViewById(R.id.row3Button3);
        mExchangeButton.setText(R.string.exchange_button_label);
        mExchangeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_119_exchangefolder, 0, 0);
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


        return v;
    }
}


