package com.asc.msigeosystems.prism4dmockup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Collect Fragment is the UI
 * when the user is making point measurements in the field
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DMockupCoordConversionFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Input / Output Fields on screen
    private TextView mlatitudeLabel;
    private EditText mlatitudeInput;
    private TextView mlongitudeLabel;
    private EditText mlongitudeInput;
    private TextView mutmLabel;
    private TextView mutmIntegerOutput;
    private TextView mutmNmOutput;
    private TextView mutmSOOutput;

    private TextView mUtmNmZoneOutput;
    private TextView mUtmNmLatBandOutput;
    private TextView mUtmNmEastingOutput;
    private TextView mUtmNmNorthingOutput;
    private TextView mUtmNmHemisphereOutput;
   // private TextView mUtmNmConvergenceOutput;
   // private TextView mUtmNmScaleOutput;


    private Button mConvertButton;
    private Button mClearButton;

    private double mLatitude;
    private double mLongitude;

    private String mLatitudeString;
    private String mLongitudeString;

    private int    mZone;
    private char   mLatBand;
    private double mEasting;
    private double mNorthing;
    private char   mHemisphere;
    private double mConvergence;
    private double mScale;



    //footer
    //footer left button
    private Button mEscButton;
    //footer row 1
    private TextView mCurrentFilenameField;
    //footer row 2
    private TextView mModelField;
    private TextView mSnField;
    //footer row 3
    private TextView mTrackingField;
    private TextView mModeField;
    //footer row 4
    private TextView mHorizField;
    private TextView mVertField;
    //footer row 5
    private TextView mRmsField;
    private TextView mPdopField;
    //footer right button
    private Button mEnterButton;


    public MainPrism4DMockupCoordConversionFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_coord_conversion_prism4_dmockup, container, false);


        //Wire up the UI widgets so they can handle events later
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields

        //Latitude
        mlatitudeLabel = (TextView) v.findViewById(R.id.latitudeLabel);
        mlatitudeInput = (EditText) v.findViewById(R.id.latitudeInput);

        //Longitude
        mlongitudeLabel = (TextView)v.findViewById(R.id.longitudeLabel);
        mlongitudeInput = (EditText) v.findViewById(R.id.longitudeInput);

        //UTM
        mutmIntegerOutput = (TextView) v.findViewById(R.id.utmIntgerOutput);
        mutmNmOutput =      (TextView) v.findViewById(R.id.utmNmOutput);
        mutmSOOutput =      (TextView) v.findViewById(R.id.utmSOOutput);

        mUtmNmZoneOutput       =  (TextView) v.findViewById(R.id.utm_zone);
        mUtmNmLatBandOutput    =  (TextView) v.findViewById(R.id.utm_latband);
        mUtmNmHemisphereOutput =  (TextView) v.findViewById(R.id.utm_hemisphere);
        mUtmNmEastingOutput    =  (TextView) v.findViewById(R.id.utm_easting);
        mUtmNmNorthingOutput   =  (TextView) v.findViewById(R.id.utm_northing);






        //Conversion Button
        mConvertButton = (Button) v.findViewById(R.id.convertButton);
        mConvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                boolean inputsValid = true;

                /***
                 * if (latitude  <  -90.0  || latitude  > 90.0      ||
                 longitude < -180.0  || longitude >= 180.0)
                 {
                 throw new IllegalArgumentException(
                 "Legal ranges: latitude [-90,90], longitude [-180,180).");
                 }
                 */
                try {
                    //Latitude must be >= -90.0 and <= 90.0
                    mLatitudeString = mlatitudeInput.getText().toString();
                    mLatitude = Double.parseDouble(mLatitudeString);

                    //Don't trust the conversion routines to do the checking
                    // or to throw IllegalArgumentException  if not in proper format
                    if (mLatitude < -90.0 || mLatitude > 90.0){
                        mlatitudeInput.setText(R.string.input_wrong_range_error);
                        inputsValid = false;
                    }



                } catch (NumberFormatException e) {
                    // lat did not contain a valid double
                    mlatitudeInput.setText(R.string.input_double_format_error_string);
                    inputsValid = false;

                }
                try {
                    //longitude must be >= -180.0 and <180.0

                    mLongitudeString = mlongitudeInput.getText().toString();
                    mLongitude = Double.parseDouble(mLongitudeString);

                    //Don't trust the conversion routines to do the checking
                    // or to throw IllegalArgumentException  if not in proper format
                    if (mLongitude < -180.0  || mLongitude >= 180.0){
                        mlongitudeInput.setText(R.string.input_wrong_range_error);
                        inputsValid = false;
                    }

                } catch (NumberFormatException e) {
                    // long did not contain a valid double
                    mlongitudeInput.setText(R.string.input_double_format_error_string);
                    inputsValid = false;
                }


                //only attempt the conversions if the inputs are valid
                if (inputsValid) {

                    //The IBM code:
                    try {
                        //an actual coversion
                        CoordinateConversion coordinateConversion = new CoordinateConversion();

                        //integer precision (meter)
                        String utmStringCoordinates = coordinateConversion.latLon2UTM(mLatitude, mLongitude);
                        mutmIntegerOutput.setText(utmStringCoordinates);

                        //double precision (sub-meter?,
                        // these are wrong but included for comparison)
                        //mutmDoubleOutput.setText(coordinateConversion.latLon2PreciseUTM(mLatitude, mLongitude));


                    } catch (IllegalArgumentException exc) {
                        //input parameters were not within range
                        mlatitudeInput.setText(R.string.input_wrong_range_error);
                        mlongitudeInput.setText(R.string.input_wrong_range_error);
                        inputsValid = false;
                    }
                }

                if (inputsValid){
                    //The stack overflow conversion code:

                    //create a new WGS84 object from the Lat / Long coordinates
                    WGS84 latlong = new WGS84(mLatitude, mLongitude);

                    //Now convert the coordinates into UTM
                    //There is not very robust exception handling in this class
                    UTM utmCoordinates = new UTM(latlong);

                    mutmSOOutput.setText(utmCoordinates.toString());

                    //And do it again for the nanometer accuracy conversion based on Kearny (2010)
                    Prism4DUTM prism4DUTMCoordinates = new Prism4DUTM(latlong);
                    mutmNmOutput.setText(prism4DUTMCoordinates.toString());

                    //Also output the result in separate fields
                    mUtmNmZoneOutput      .setText(prism4DUTMCoordinates.getZone());
                    mUtmNmHemisphereOutput.setText(prism4DUTMCoordinates.getHemisphere());
                    mUtmNmLatBandOutput   .setText(prism4DUTMCoordinates.getLatBand());
                    mUtmNmEastingOutput   .setText(prism4DUTMCoordinates.getEasting());
                    mUtmNmNorthingOutput  .setText(prism4DUTMCoordinates.getNorthing());

                }


                Toast.makeText(getActivity(),
                        R.string.conversion_stub,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Conversion Button
        mClearButton = (Button) v.findViewById(R.id.clearFormButton);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mlongitudeInput.setText("");
                mlatitudeInput.setText("");
                mutmIntegerOutput.setText("");
                mutmNmOutput.setText("");
                mutmSOOutput.setText("");

                mUtmNmZoneOutput.setText(R.string.utm_zone_label);
                mUtmNmLatBandOutput.setText(R.string.utm_latband_label);
                mUtmNmHemisphereOutput.setText(R.string.utm_hemisphere_label);
                mUtmNmEastingOutput.setText(R.string.utm_easting_label);
                mUtmNmNorthingOutput.setText(R.string.utm_northing_label);
            }
        });

        //FOOTER WIDGETS


        //Esc Button
        mEscButton = (Button) v.findViewById(R.id.escButton);
        mEscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.esc_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Enter Button
        mEnterButton = (Button) v.findViewById(R.id.enterButton);
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.enter_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}


