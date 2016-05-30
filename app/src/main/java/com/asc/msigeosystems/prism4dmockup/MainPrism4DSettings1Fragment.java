package com.asc.msigeosystems.prism4dmockup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * The Settings Fragment is the top level selection UI
 * for Global Settings for the app
 * Note that each of the other screens has the potential for
 * local settings which are not covered here
 * Created by elisabethhuhn on 5/1/2016.
 */
public class MainPrism4DSettings1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mGlobalButton;
    private Button mProjectButton;
    private Button m3Button;

    private Button m4Button;
    private Button m5Button;
    private Button m6Button;

    private Button m7Button;
    private Button m8Button;
    private Button m9Button;




    public MainPrism4DSettings1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

        //Global Button for global settings
        mGlobalButton = (Button) v.findViewById(R.id.row1Button1);
        mGlobalButton.setText(R.string.setting_global_button_label);
        //the order of images here is left, top, right, bottom
        mGlobalButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        mGlobalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToSettings11GlobalScreen();
                }

            }
        });




        //Project Button
        mProjectButton = (Button) v.findViewById(R.id.row1Button2);
        mProjectButton.setText(R.string.setting_project_defaults_button_label);
        //the order of images here is left, top, right, bottom
        mProjectButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        mProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToSettings12ProjectDefaultsScreen();
                }

            }
        });


        //3 Button
        m3Button = (Button) v.findViewById(R.id.row3Button3);
        m3Button.setText(R.string.unused_button_label);
        m3Button.setEnabled(false);
        m3Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        m3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //4 Button
        m4Button = (Button) v.findViewById(R.id.row1Button3);
        m4Button.setText(R.string.unused_button_label);
        m4Button.setEnabled(false);
        m4Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        m4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //5 Button
        m5Button = (Button) v.findViewById(R.id.row2Button1);
        m5Button.setText(R.string.unused_button_label);
        m5Button.setEnabled(false);
        m5Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        m5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //6 Button
        m6Button = (Button) v.findViewById(R.id.row2Button2);
        m6Button.setText(R.string.unused_button_label);
        m6Button.setEnabled(false);
        m6Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        m6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //7 Button
        m7Button = (Button) v.findViewById(R.id.row2Button3);
        m7Button.setText(R.string.unused_button_label);
        m7Button.setEnabled(false);
        m7Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        m7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //8 Button
        m8Button = (Button) v.findViewById(R.id.row3Button1);
        m8Button.setText(R.string.unused_button_label);
        m8Button.setEnabled(false);
        m8Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        m8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //9 Button
        m9Button = (Button) v.findViewById(R.id.row3Button2);
        m9Button.setText(R.string.unused_button_label);
        m9Button.setEnabled(false);
        m9Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings, 0, 0);
        m9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });





        //FOOTER WIDGETS

        //  Esc and Enter buttons are NOT enabled on the collect screen
        //so we can ignore the footer for now


        return v;
    }
}


