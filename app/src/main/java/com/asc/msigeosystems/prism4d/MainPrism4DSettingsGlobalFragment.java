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
public class MainPrism4DSettingsGlobalFragment extends Fragment {

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
    private Button mGeoidModelButton;

    private Button mGeneralButton;
    private Button mLocalizationsButton;
    private Button mSurveyStylesButton;





    public MainPrism4DSettingsGlobalFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        //Units Button
        mUnitsButton = (Button) v.findViewById(R.id.row1Button1);
        mUnitsButton.setText(R.string.setting_units_button_label);
        //the order of images here is left, top, right, bottom
        mUnitsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mUnitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_units_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });




        //Formats Button
        mFormatsButton = (Button) v.findViewById(R.id.row1Button2);
        mFormatsButton.setText(R.string.setting_formats_button_label);
        //the order of images here is left, top, right, bottom
        mFormatsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mFormatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_formats_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Tolerances Button
        mTolerancesButton = (Button) v.findViewById(R.id.row3Button3);
        mTolerancesButton.setText(R.string.setting_tolerances_button_label);
        mTolerancesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mTolerancesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_tolerances_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Datums Button
        mDatumsButton = (Button) v.findViewById(R.id.row1Button3);
        mDatumsButton.setText(R.string.setting_datums_button_label);
        mDatumsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mDatumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_datums_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Projections Button
        mProjectionsButton = (Button) v.findViewById(R.id.row2Button1);
        mProjectionsButton.setText(R.string.setting_projections_button_label);
        mProjectionsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mProjectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_projections_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Geoid Models Button
        mGeoidModelButton = (Button) v.findViewById(R.id.row2Button2);
        mGeoidModelButton.setText(R.string.setting_geoid_models_button_label);
        mGeoidModelButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mGeoidModelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_geoid_models_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //General Button
        mGeneralButton = (Button) v.findViewById(R.id.row2Button3);
        mGeneralButton.setText(R.string.setting_general_button_label);
        mGeneralButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mGeneralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_general_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Localizations Button
        mLocalizationsButton = (Button) v.findViewById(R.id.row3Button1);
        mLocalizationsButton.setText(R.string.setting_localizations_button_label);
        mLocalizationsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mLocalizationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.setting_localizations_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Survey Styles Button
        mSurveyStylesButton = (Button) v.findViewById(R.id.row3Button2);
        mSurveyStylesButton.setText(R.string.setting_survey_styles_button_label);
        mSurveyStylesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
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


