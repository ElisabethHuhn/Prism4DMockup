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
 * The Support Fragment is the top level selection UI
 * for support features
 *
 * Created by elisabethhuhn on 5/1/2016.
 */
public class MainPrism4DSupport1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mManualsButton;
    private Button mFaqButton;
    private Button mVideosButton;

    private Button mModulesButton;
    private Button mUpgradesButton;
    private Button mRegistrationButton;

    private Button mSupportButton;
    private Button mFeedbackButton;
    private Button mAboutUsButton;




    public MainPrism4DSupport1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        mManualsButton = (Button) v.findViewById(R.id.row1Button1);
        mManualsButton.setText(R.string.support_manuals_button_label);
        //the order of images here is left, top, right, bottom
        mManualsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_911_manuals, 0, 0);
        mManualsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_manuals_button_label,
                        Toast.LENGTH_SHORT).show();
                /*
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToSettings11GlobalScreen();
                }
                */

            }
        });





        mFaqButton = (Button) v.findViewById(R.id.row1Button2);
        mFaqButton.setText(R.string.support_faq_button_label);
        //the order of images here is left, top, right, bottom
        mFaqButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_912_faqs, 0, 0);
        mFaqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_faq_button_label,
                        Toast.LENGTH_SHORT).show();
                /*
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToSettings12ProjectDefaultsScreen();
                }
                */

            }
        });


        //
        mVideosButton = (Button) v.findViewById(R.id.row1Button3);
        mVideosButton.setText(R.string.support_videos_button_label);
        mVideosButton.setEnabled(true);
        mVideosButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_913_videos, 0, 0);
        mVideosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_videos_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        mModulesButton = (Button) v.findViewById(R.id.row2Button1);
        mModulesButton.setText(R.string.support_modules_button_label);

        mModulesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_914_modules, 0, 0);
        mModulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_modules_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        mUpgradesButton = (Button) v.findViewById(R.id.row2Button2);
        mUpgradesButton.setText(R.string.support_upgrades_button_label);

        mUpgradesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_915_upgrades, 0, 0);
        mUpgradesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_upgrades_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //
        mRegistrationButton = (Button) v.findViewById(R.id.row2Button3);
        mRegistrationButton.setText(R.string.support_registration_button_label);

        mRegistrationButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_916_registration, 0, 0);
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_registration_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //
        mSupportButton = (Button) v.findViewById(R.id.row3Button1);
        mSupportButton.setText(R.string.support_support_button_label);

        mSupportButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_917_support, 0, 0);
        mSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_support_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //
        mFeedbackButton = (Button) v.findViewById(R.id.row3Button2);
        mFeedbackButton.setText(R.string.support_feedback_button_label);

        mFeedbackButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_818_localizations, 0, 0);
        mFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_feedback_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //
        mAboutUsButton = (Button) v.findViewById(R.id.row3Button3);
        mAboutUsButton.setText(R.string.support_aboutus_button_label);

        mAboutUsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_919_aboutus, 0, 0);
        mAboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.support_aboutus_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });





        //FOOTER WIDGETS

        //  Esc and Enter buttons are NOT enabled on the collect screen
        //so we can ignore the footer for now


        return v;
    }
}


