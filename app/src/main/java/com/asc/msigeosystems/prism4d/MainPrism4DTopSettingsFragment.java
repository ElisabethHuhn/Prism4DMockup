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
 * The Settings Fragment is the top level selection UI
 * for Global Settings for the app
 * Note that each of the other screens has the potential for
 * local settings which are not covered here
 * Created by elisabethhuhn on 5/1/2016.
 */
public class MainPrism4DTopSettingsFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mUnitsButton;
    private Button mFormatsButton;
    private Button mTolerancesButton;

    private Button mDatumsButton;
    private Button mProjectionsButton;
    private Button mGeoidButton;

    private Button mGeneralButton;
    private Button mLocalizationsButton;
    private Button mSurveyStylesButton;




    public MainPrism4DTopSettingsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        //Units Button for global settings
        mUnitsButton = (Button) v.findViewById(R.id.row1Button1);
        mUnitsButton.setText(R.string.setting_units_button_label);
        //the order of images here is left, top, right, bottom
        mUnitsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_811_units, 0, 0);
        mUnitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_units_button_label,
                        Toast.LENGTH_SHORT).show();
                /*
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToSettingsGlobalScreen();
                }
                */

            }
        });




        //Formats Button
        mFormatsButton = (Button) v.findViewById(R.id.row1Button2);
        mFormatsButton.setText(R.string.setting_formats_button_label);
        //the order of images here is left, top, right, bottom
        mFormatsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_812_formats, 0, 0);
        mFormatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_formats_button_label,
                        Toast.LENGTH_SHORT).show();
                /*
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToSettingsProjectDefaultsScreen();
                }
                */

            }
        });


        //Tolerances
        mTolerancesButton = (Button) v.findViewById(R.id.row1Button3);
        mTolerancesButton.setText(R.string.setting_tolerances_button_label);
        mTolerancesButton.setEnabled(true);
        mTolerancesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_813_tolerances, 0, 0);
        mTolerancesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_tolerances_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Datums
        mDatumsButton = (Button) v.findViewById(R.id.row2Button1);
        mDatumsButton.setText(R.string.setting_datums_button_label);
        mDatumsButton.setEnabled(true);
        mDatumsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_814_datums, 0, 0);
        mDatumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_datums_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Projections
        mProjectionsButton = (Button) v.findViewById(R.id.row2Button2);
        mProjectionsButton.setText(R.string.setting_projections_button_label);

        mProjectionsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_815_projections, 0, 0);
        mProjectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_projections_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Geod
        mGeoidButton = (Button) v.findViewById(R.id.row2Button3);
        mGeoidButton.setText(R.string.setting_geoid_models_button_label);

        mGeoidButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_816_geoidmodels, 0, 0);
        mGeoidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_geoid_models_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //General
        mGeneralButton = (Button) v.findViewById(R.id.row3Button1);
        mGeneralButton.setText(R.string.setting_general_button_label);

        mGeneralButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mGeneralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_general_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Localizations
        mLocalizationsButton = (Button) v.findViewById(R.id.row3Button2);
        mLocalizationsButton.setText(R.string.setting_localizations_button_label);

        mLocalizationsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_818_localizations, 0, 0);
        mLocalizationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_localizations_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Survey Styles
        mSurveyStylesButton = (Button) v.findViewById(R.id.row3Button3);
        mSurveyStylesButton.setText(R.string.setting_survey_styles_button_label);

        mSurveyStylesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_819_surveystyles, 0, 0);
        mSurveyStylesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_survey_styles_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });





        //FOOTER WIDGETS

        //  Esc and Enter buttons are NOT enabled on the collect screen
        //so we can ignore the footer for now


        return v;
    }
}


