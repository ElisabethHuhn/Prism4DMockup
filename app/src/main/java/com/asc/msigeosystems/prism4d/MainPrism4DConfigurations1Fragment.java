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
 * The Configurations Fragment is the top level selection UI
 * for stakeout functions
 * Created by elisabethhuhn on 5/1/2016.
 */
public class MainPrism4DConfigurations1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mEquipmentButton;
    private Button mCommunicationsButton;
    private Button mCorrectionsButton;

    private Button mPeripheralsButton;
    private Button mCalibrationsButton;
    private Button mLogRawDataButton;

    private Button mUtilitiesButton;
    private Button mGeneralButton;
    private Button mSaveProfileButton;




    public MainPrism4DConfigurations1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        //Equipment Button
        mEquipmentButton = (Button) v.findViewById(R.id.row1Button1);
        mEquipmentButton.setText(R.string.config_equipment_button_label);
        //the order of images here is left, top, right, bottom
        mEquipmentButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_711_equipment, 0, 0);
        mEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.config_equipment_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });




        //Communications Button
        mCommunicationsButton = (Button) v.findViewById(R.id.row1Button2);
        mCommunicationsButton.setText(R.string.stakeout_lines_button_label);
        //the order of images here is left, top, right, bottom
        mCommunicationsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_712_communications, 0, 0);
        mCommunicationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_lines_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Corrections Button
        mCorrectionsButton = (Button) v.findViewById(R.id.row1Button3);
        mCorrectionsButton.setText(R.string.config_corrections_label);
        mCorrectionsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_713_corrections, 0, 0);
        mCorrectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.config_corrections_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Peripherals Button
        mPeripheralsButton = (Button) v.findViewById(R.id.row2Button1);
        mPeripheralsButton.setText(R.string.config_peripherals_button_label);
        mPeripheralsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_714_peripherals, 0, 0);
        mPeripheralsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.config_peripherals_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Calibrations Button
        mCalibrationsButton = (Button) v.findViewById(R.id.row2Button2);
        mCalibrationsButton.setText(R.string.config_calibrations_button_label);
        mCalibrationsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_715_calibrations, 0, 0);
        mCalibrationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.config_calibrations_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Log Raw Data Button
        mLogRawDataButton = (Button) v.findViewById(R.id.row2Button3);
        mLogRawDataButton.setText(R.string.config_log_raw_button_label);
        mLogRawDataButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_716_rawdata, 0, 0);
        mLogRawDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.config_log_raw_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

            //General Button
        mGeneralButton = (Button) v.findViewById(R.id.row3Button1);
        mGeneralButton.setText(R.string.config_general_button_label);
        mGeneralButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_717_generalconfig, 0, 0);
        mGeneralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.config_general_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Utilities Button
        mUtilitiesButton = (Button) v.findViewById(R.id.row3Button2);
        mUtilitiesButton.setText(R.string.config_utilities_button_label);
        mUtilitiesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_718_utilities, 0, 0);
        mUtilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.config_utilities_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Save Button
        mSaveProfileButton = (Button) v.findViewById(R.id.row3Button3);
        mSaveProfileButton.setText(R.string.save_profile_button_label);
        mSaveProfileButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_719_saveprofile, 0, 0);
        mSaveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.save_profile_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });





        //FOOTER WIDGETS

        //  Esc and Enter buttons are NOT enabled on the collect screen
        //so we can ignore the footer for now


        return v;
    }
}


