package com.asc.msigeosystems.prism4d;

import android.content.Intent;
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
 * Currently, this has all the utilities code for development, but not the final product
 *
 * Created by Elisabeth Huhn on 11/19/2016.
 */
public class MainPrism4DTopSettingsGeneralFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *
     */

    //Screen Label, used on this screen to display the open project
    private TextView mScreenLabel;

    //Matrix Buttons
    private Button mCoordinateWorkflowButton;
    private Button mConvertCoordinatesButton;
    private Button mListNmeaSentencesButton;

    private Button mSkyplotButton;
    private Button mListSattelitesButton;
    private Button mGpsFromNmeaButton;

    private Button mCompassActivityButton;
    private Button mCompassFragmentButton;
    private Button m9Button;



    /***********************************************************************/
    /**********   Member Variables  ****************************************/
    /***********************************************************************/

    private int mOpenProject;


    public MainPrism4DTopSettingsGeneralFragment() {
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

    private void wireWidgets(View v){
        //Tell the user which project is open
        mScreenLabel = (TextView) v.findViewById(R.id.matrix_screen_label);
        mScreenLabel.setText(((MainPrism4DActivity) getActivity()).getOpenProjectIDMessage());
        int color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        mScreenLabel.setBackgroundColor(color);




        mCoordinateWorkflowButton = (Button) v.findViewById(R.id.row1Button1);
        mCoordinateWorkflowButton.setText(R.string.cogo_workflow_button_label);
        //the order of images here is left, top, right, bottom
        mCoordinateWorkflowButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mCoordinateWorkflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToCoordWorkflow();
                }


            }
        });


        mConvertCoordinatesButton = (Button) v.findViewById(R.id.row1Button2);
        mConvertCoordinatesButton.setText(R.string.convert_coordinates);
        //the order of images here is left, top, right, bottom
        mConvertCoordinatesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mConvertCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToConvertScreen();
                }


            }
        });



        mListNmeaSentencesButton = (Button) v.findViewById(R.id.row1Button3);
        mListNmeaSentencesButton.setText(R.string.skyplot_nmea_sentence_label);
        mListNmeaSentencesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mListNmeaSentencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainPrism4DActivity)getActivity()).switchToListNmeaScreen();


            }
        });


        mSkyplotButton = (Button) v.findViewById(R.id.row2Button1);
        mSkyplotButton.setEnabled(true);
        mSkyplotButton.setText(R.string.skyplot_nmea_activity);

        mSkyplotButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mSkyplotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(getActivity(),
                        R.string.skyplot_nmea_activity,
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), Prism4DGPSActivity.class);
                startActivity(intent);

            }
        });


        mGpsFromNmeaButton = (Button) v.findViewById(R.id.row2Button2);
        mGpsFromNmeaButton.setText(R.string.skyplot_gps_from_nmea);
        mGpsFromNmeaButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mGpsFromNmeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                ((MainPrism4DActivity)getActivity()).switchToGpsNmeaScreen();

            }
        });


        mListSattelitesButton = (Button) v.findViewById(R.id.row2Button3);
        mListSattelitesButton.setText(R.string.skyplot_list_satellites);
        mListSattelitesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mListSattelitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ((MainPrism4DActivity) getActivity()).switchToListSatellitesScreen();

            }
        });




        mCompassActivityButton = (Button) v.findViewById(R.id.row3Button1);
        mCompassActivityButton.setText("");
        mCompassActivityButton.setFocusable(false);
        mCompassActivityButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mCompassActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //Intent intent = new Intent(getActivity(), com.asc.msigeosystems.prism4d.MainPrism4DCompassActivity.class);
                //startActivity(intent);
            }
        });


        mCompassFragmentButton = (Button) v.findViewById(R.id.row3Button2);
        mCompassFragmentButton.setText(R.string.skyplot_compass_fragment);
        mCompassFragmentButton.setFocusable(true);
        mCompassFragmentButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        mCompassFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.skyplot_compass_fragment,
                        Toast.LENGTH_SHORT).show();

                ((MainPrism4DActivity)getActivity()).switchToCompassScreen();

            }
        });


        m9Button = (Button) v.findViewById(R.id.row3Button3);
        m9Button.setText("");
        m9Button.setFocusable(false);
        m9Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_817_generalsettings, 0, 0);
        m9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        "",
                        Toast.LENGTH_SHORT).show();

            }
        });



        //FOOTER WIDGETS

    }
}


