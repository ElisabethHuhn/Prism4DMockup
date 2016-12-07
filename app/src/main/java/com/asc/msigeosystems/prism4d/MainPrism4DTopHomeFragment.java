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
 * A placeholder fragment containing a simple view.
 */
public class MainPrism4DTopHomeFragment extends Fragment {


    //Screen Label, used on this screen to display the open project
    private TextView mScreenLabel;

    //main area of screen fragment
    private Button mProjectButton;
    private Button mCollectButton;
    private Button mStakeoutButton;

    private Button mCogoButton;
    private Button mMapsButton;
    private Button mSkyplotButton;

    private Button mConfigButton;
    private Button mSettingsButton;
    private Button mHelpButton;




    public MainPrism4DTopHomeFragment() {
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
    public void onResume() {
        super.onResume();
        setSubtitle();
    }

    private void setSubtitle(){
        ((MainPrism4DActivity)getActivity()).switchSubtitle(R.string.subtitle_home);
    }


    private void wireWidgets(View v){
        //Tell the user which project is open
        mScreenLabel = (TextView) v.findViewById(R.id.matrix_screen_label);
        mScreenLabel.setText(((MainPrism4DActivity) getActivity()).getOpenProjectIDMessage());
        int color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        mScreenLabel.setBackgroundColor(color);



        //projects Button
        mProjectButton = (Button) v.findViewById(R.id.row1Button1);
        mProjectButton.setText(R.string.projects_button_label);
        //the order of images here is left, top, right, bottom
        mProjectButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
        mProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the top level project fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToTopProjectScreen();
                }

            }
        });

        //collect Button
        mCollectButton = (Button) v.findViewById(R.id.row1Button2);
        mCollectButton.setText(R.string.collect_button_label);
        //the order of images here is left, top, right, bottom
        mCollectButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_002_collect, 0, 0);
        mCollectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Switch the fragment to the top level collect fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToTopCollectScreen();
                }
            }
        });

        //stakeout Button
        mStakeoutButton = (Button) v.findViewById(R.id.row1Button3);
        mStakeoutButton.setText(R.string.stakeout_button_label);
        //the order of images here is left, top, right, bottom
        mStakeoutButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_003_stakeout, 0, 0);
        mStakeoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the top level stakeout fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToTopStakeoutScreen();
                }

            }
        });


        //cogo Button
        mCogoButton = (Button) v.findViewById(R.id.row2Button1);
        mCogoButton.setText(R.string.cogo_button_label);
        //the order of images here is left, top, right, bottom
        mCogoButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_004_cogo, 0, 0);
        mCogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the top level collect fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToTopCogoScreen();
                }

            }
        });

        //maps Button
        mMapsButton = (Button) v.findViewById(R.id.row2Button2);
        mMapsButton.setText(R.string.maps_button_label);
        //the order of images here is left, top, right, bottom
        mMapsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_005_maps, 0, 0);
        mMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.maps_button_label,
                        Toast.LENGTH_SHORT).show();
                //Switch the fragment to the top level collect fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToTopMapsScreen();
                }

            }
        });


        //skyplot Button
        mSkyplotButton = (Button) v.findViewById(R.id.row2Button3);
        mSkyplotButton.setText(R.string.skyplot_button_label);
        mSkyplotButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_006_skyplots, 0, 0);
        mSkyplotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the top level collect fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToTopSkyplotScreen();
                }

            }
        });

        //config Button
        mConfigButton = (Button) v.findViewById(R.id.row3Button1);
        mConfigButton.setText(R.string.config_button_label);
        mConfigButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_007_config, 0, 0);
        mConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the top level collect fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToTopConfigScreen();
                }

            }
        });

        //settings Button
        mSettingsButton = (Button) v.findViewById(R.id.row3Button2);
        mSettingsButton.setText(R.string.settings_button_label);
        mSettingsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_008_settings, 0, 0);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToTopSettingsScreen();
                }

            }
        });

        //help Button
        mHelpButton = (Button) v.findViewById(R.id.row3Button3);
        mHelpButton.setText(R.string.help_button_label);
        mHelpButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_009_support, 0, 0);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.help_button_label,
                        Toast.LENGTH_SHORT).show();
                //Switch the fragment to the top level collect fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToTopSupportScreen();
                }

            }
        });

        //ESC and Enter are disabled on the Help Screen


    }
}
