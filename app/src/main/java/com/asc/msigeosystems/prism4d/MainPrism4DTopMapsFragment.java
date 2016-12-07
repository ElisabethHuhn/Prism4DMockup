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
 * The Maps Fragment is the top level selection UI for map features
 *
 * when the user is 
 * Created by Elisabeth Huhn on 5/132016.
 */
public class MainPrism4DTopMapsFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private TextView mScreenLabel;

    //Matrix Buttons
    private Button mGoogleMapsButton;
    private Button mEarthButton;
    private Button mCustomMapsButton;

    private Button mLayersButton;
    private Button mMarkersButton;
    private Button mPolylinesButton;

    private Button mTracksButton;
    private Button mBackgroundButton;
    private Button mSaveProfileButton;




    public MainPrism4DTopMapsFragment() {
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
        ((MainPrism4DActivity) getActivity()).switchSubtitle(R.string.subtitle_maps);
    }

    private void wireWidgets(View v){
        //Tell the user which project is open
        mScreenLabel = (TextView) v.findViewById(R.id.matrix_screen_label);
        mScreenLabel.setText(((MainPrism4DActivity) getActivity()).getOpenProjectIDMessage());
        int color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        mScreenLabel.setBackgroundColor(color);


        //Google Maps Button
        mGoogleMapsButton = (Button) v.findViewById(R.id.row1Button1);
        mGoogleMapsButton.setText(R.string.google_maps_button_label);
        //the order of images here is left, top, right, bottom
        mGoogleMapsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_511_googlemaps, 0, 0);
        mGoogleMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.google_maps_button_label,
                        Toast.LENGTH_SHORT).show();
            }
        });


        //Google Earth Button
        mEarthButton = (Button) v.findViewById(R.id.row1Button2);
        mEarthButton.setText(R.string.earth_button_label);
        mEarthButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_512_googleearth, 0, 0);
        mEarthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.earth_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Custom Maps Button
        mCustomMapsButton = (Button) v.findViewById(R.id.row1Button3);
        mCustomMapsButton.setText(R.string.custom_maps_button_label);
        //the order of images here is left, top, right, bottom
        mCustomMapsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_513_custommaps, 0, 0);
        mCustomMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.custom_maps_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Layers Button
        mLayersButton = (Button) v.findViewById(R.id.row2Button1);
        mLayersButton.setText(R.string.layers_button_label);
        mLayersButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_514_maplayers, 0, 0);
        mLayersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        //R.string.cogo_map_check_button_label,
                        R.string.layers_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Markers Button
        mMarkersButton = (Button) v.findViewById(R.id.row2Button2);
        mMarkersButton.setText(R.string.markers_button_label);
        mMarkersButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_515_mapmarkers, 0, 0);
        mMarkersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.markers_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });
        //Polylines Button
        mPolylinesButton = (Button) v.findViewById(R.id.row2Button3);
        mPolylinesButton.setText(R.string.polylines_button_label);
        mPolylinesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_516_polylines, 0, 0);
        mPolylinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.polylines_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //tracks Button
        mTracksButton = (Button) v.findViewById(R.id.row3Button1);
        mTracksButton.setText(R.string.track_button_label);
        mTracksButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_517_maptracks, 0, 0);
        mTracksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.track_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Background Button
        mBackgroundButton = (Button) v.findViewById(R.id.row3Button2);
        mBackgroundButton.setText(R.string.background_button_label);
        mBackgroundButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_518_backgroundmaps, 0, 0);
        mBackgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.background_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Save Button
        mSaveProfileButton = (Button) v.findViewById(R.id.row3Button3);
        mSaveProfileButton.setText(R.string.save_profile_button_label);
        mSaveProfileButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_519_saveviews, 0, 0);
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

        //  Esc and Enter buttons are desabled on this screen
        //      so do nothing in the footer


    }
}


