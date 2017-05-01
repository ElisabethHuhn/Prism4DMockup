package com.asc.msigeosystems.prism4d;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

/**
 * The Configurations Fragment is the top level selection UI
 * for stakeout functions
 * Created by Elisabeth Huhn on 5/1/2016.
 */
public class MainPrism4DTopConfigurationsFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private TextView mScreenLabel;

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




    public MainPrism4DTopConfigurationsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later
        wireWidgets(v);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        setSubtitle();
    }

    private void setSubtitle() {
        ((MainPrism4DActivity) getActivity()).switchSubtitle(R.string.subtitle_configurations);
    }

    private void wireWidgets(View v){
        //Tell the user which project is open
        mScreenLabel = (TextView) v.findViewById(R.id.matrix_screen_label);
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                    Prism4DConstantsAndUtilities.getInstance();
        mScreenLabel.setText(constantsAndUtilities.getOpenProjectIDMessage(getActivity()));
        int color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        mScreenLabel.setBackgroundColor(color);


        //Equipment Button
        mEquipmentButton = (Button) v.findViewById(R.id.row1Button1);
        mEquipmentButton.setText(R.string.config_equipment_button_label);
        mEquipmentButton.setBackgroundResource(R.color.colorGray);
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
        mCommunicationsButton.setText(R.string.config_communications_button_label);
        //the order of images here is left, top, right, bottom
        mCommunicationsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_712_communications, 0, 0);
        mCommunicationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.config_communications_button_label,
                        Toast.LENGTH_SHORT).show();
                ((MainPrism4DActivity)getActivity()).switchToListNmeaScreen();

            }
        });


        //Corrections Button
        mCorrectionsButton = (Button) v.findViewById(R.id.row1Button3);
        mCorrectionsButton.setText(R.string.config_corrections_label);
        mCorrectionsButton.setBackgroundResource(R.color.colorGray);
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
        mPeripheralsButton.setBackgroundResource(R.color.colorGray);
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
        mCalibrationsButton.setBackgroundResource(R.color.colorGray);
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
        mLogRawDataButton.setBackgroundResource(R.color.colorGray);
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
        mGeneralButton.setBackgroundResource(R.color.colorGray);
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
        mUtilitiesButton.setBackgroundResource(R.color.colorGray);
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
        mSaveProfileButton.setBackgroundResource(R.color.colorGray);
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

    }
}


