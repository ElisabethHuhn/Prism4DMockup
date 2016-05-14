package com.asc.msigeosystems.prism4dmockup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private TextView mLatLabel;
    private EditText mLatDigDegInput;
    private EditText mLatDegInput;
    private EditText mLatMinInput;
    private EditText mLatSecInput;

    private TextView mLongLabel;
    private EditText mLongDigDegInput;
    private EditText mLongDegInput;
    private EditText mLongMinInput;
    private EditText mLongSecInput;

    private TextView mUtmLabel;
    private TextView mUtmIntegerOutput;
    private TextView mUtmNmOutput;
    private TextView mUtmSOOutput;

    private TextView mUtmNmZoneOutput;
    private TextView mUtmNmLatBandOutput;
    private TextView mUtmNmEastingOutput;
    private TextView mUtmNmNorthingOutput;
    private TextView mUtmNmHemisphereOutput;
   // private TextView mUtmNmConvergenceOutput;
   // private TextView mUtmNmScaleOutput;


    private Button mConvertButton;
    private Button mClearButton;


    private double mLatitude; //This is always in digital degrees regardless of input type
    private double mLongitude;
    private double mDegree;
    private double mMinute;
    private double mSecond;

    private String mLatString;
    private String mLongString;
    private String mDegreeString;
    private String mMinuteString;
    private String mSecondString;


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
        mLatLabel       = (TextView) v.findViewById(R.id.latitudeLabel);
        mLatDigDegInput = (EditText) v.findViewById(R.id.latitudeInput);
        mLatDegInput    = (EditText) v.findViewById(R.id.latDegreesInput);
        mLatMinInput    = (EditText) v.findViewById(R.id.latMinutesInput);
        mLatSecInput    = (EditText) v.findViewById(R.id.latSecondsInput);

        //Longitude
        mLongLabel       = (TextView)v.findViewById(R.id.longitudeLabel);
        mLongDigDegInput = (EditText) v.findViewById(R.id.longitudeInput);
        mLongDegInput    = (EditText) v.findViewById(R.id.longDegreesInput);
        mLongMinInput    = (EditText) v.findViewById(R.id.longMinutesInput);
        mLongSecInput    = (EditText) v.findViewById(R.id.longSecondsInput);

        //UTM
        mUtmIntegerOutput = (TextView) v.findViewById(R.id.utmIntgerOutput);
        mUtmNmOutput =      (TextView) v.findViewById(R.id.utmNmOutput);
        mUtmSOOutput =      (TextView) v.findViewById(R.id.utmSOOutput);

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

                performConversion();

                Toast.makeText(getActivity(),
                        R.string.conversion_stub,
                        Toast.LENGTH_SHORT).show();

            }//End on Click
        });

        //Clear Form Button
        mClearButton = (Button) v.findViewById(R.id.clearFormButton);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
               clearForm();
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
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.enter_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }

    private void performConversion() {


        /*
         * Need to determine which input format was used
         * and whether the value input is valid
         *
         * Use the prompt string to determine whether a value
         * has been input into any given field
         *
         * "Legal ranges: latitude [-90,90], longitude [-180,180).");
         *
         */

        boolean inputsValid = convertLatitude() ;
        if (inputsValid){
            inputsValid = convertLongitude();
        }else{
            //if latitude was out of range, can't sully the flag
            //if longitude is OK
            convertLongitude();
        }

        //only attempt the conversions if the inputs are valid
        if (inputsValid) {

            //The IBM code:
            try {
                //an actual conversion
                CoordinateConversion coordinateConversion =
                                            new CoordinateConversion();

                //integer precision (meter)
                String utmStringCoordinates =
                        coordinateConversion.latLon2UTM(mLatitude, mLongitude);
                mUtmIntegerOutput.setText(utmStringCoordinates);

                //double precision (sub-meter?,
                // these are wrong but included for comparison)
                //mUtmDoubleOutput.setHint(coordinateConversion.latLon2PreciseUTM(mLatitude, mLongitude));


            } catch (IllegalArgumentException exc) {
                //input parameters were not within range
                mLatDigDegInput.setText(R.string.input_wrong_range_error);
                mLongDigDegInput.setText(R.string.input_wrong_range_error);
                inputsValid = false;
            }
        }

        if (inputsValid) {
            //The stack overflow conversion code:

            //create a new WGS84 object from the Lat / Long coordinates
            WGS84 latlong = new WGS84(mLatitude, mLongitude);

            //Now convert the coordinates into UTM
            //There is not very robust exception handling in this class
            UTM utmCoordinates = new UTM(latlong);

            mUtmSOOutput.setText(utmCoordinates.toString());

            //And do it again for the nanometer accuracy conversion based on Kearny (2010)
            Prism4DUTM prism4DUTMCoordinates = new Prism4DUTM(latlong);
            mUtmNmOutput.setText(prism4DUTMCoordinates.toString());

            //Also output the result in separate fields
            mUtmNmZoneOutput.setText(prism4DUTMCoordinates.getZone());
            mUtmNmHemisphereOutput.setText(prism4DUTMCoordinates.getHemisphere());
            mUtmNmLatBandOutput.setText(prism4DUTMCoordinates.getLatBand());
            mUtmNmEastingOutput.setText(prism4DUTMCoordinates.getEasting());
            mUtmNmNorthingOutput.setText(prism4DUTMCoordinates.getNorthing());

        }

    }

    private boolean convertLatitude() {

        boolean inputsValid = true;

        try {
            //
            //lat must be larger than -80 and smaller than 84 as
            // UTM does not span the entire globe
            mLatString = mLatDigDegInput.getText().toString();

            //determine which input field was used: DD or DMS
            if (mLatString.isEmpty()  ){
                //value isn't in the decimal degrees field
                //check in the separate fields
                mDegreeString = mLatDegInput.getText().toString();
                mMinuteString = mLatMinInput.getText().toString();
                mSecondString = mLatSecInput.getText().toString();
                if (mDegreeString.isEmpty() ){
                    mDegreeString = "0";
                }
                if (mMinuteString.isEmpty() ){
                    mMinuteString = "0";
                }
                if (mSecondString.isEmpty() ){
                    mSecondString = "0.0";
                }

                mDegree = Double.parseDouble(mDegreeString);
                mMinute = Double.parseDouble(mMinuteString);
                mSecond = Double.parseDouble(mSecondString);
                mLatitude = mDegree + (mMinute/60) + (mSecond/(60*60));
                //show the user the value we are actually converting
                mLatString = "0.0";
                mLatDigDegInput.setText(String.valueOf(mLatitude));
                mLatDegInput   .setText(String.valueOf((int) mDegree));
                mLatMinInput   .setText(String.valueOf((int) mMinute));
                mLatSecInput   .setText(String.valueOf(mSecond));
                setLatColor();

            } else {
                mLatitude = Double.parseDouble(mLatString);

                if (mLatitude >= -90.0 || mLatitude < 90.0) {

                    //While here, show the DMS of the DD
                    //but only if the latitude is a valid one

                    //strip out the decimal parts of mLatitude
                    int intDegree = (int) mLatitude;
                    mDegree = (double) intDegree;

                    //remainder will have decimal minutes and seconds
                    double remainder = mLatitude - mDegree;

                    //integer part is minutes, decimal part is seconds
                    mMinute = remainder * 60;
                    int intMinute = (int) mMinute; //pure minutes
                    //save the minutes in double in case we need it later
                    mMinute = intMinute;

                    //decimal part is seconds
                    mSecond = remainder - (mMinute / 60);
                    mSecond = mSecond * 60 * 60;

                    //truncate to a reasonable number of decimal digits
                    BigDecimal bd = new BigDecimal(mSecond).setScale(5, RoundingMode.HALF_UP);
                    mSecond = bd.doubleValue();

                    //Show the user the DMS value
                    mLatDegInput.setText(String.valueOf(intDegree));
                    mLatMinInput.setText(String.valueOf(intMinute));
                    mLatSecInput.setText(Double.toString(mSecond));
                    setLatColor();
                }
            }


            //Don't trust the conversion routines to do the checking
            // or to throw IllegalArgumentException  if not in proper format
            //lat must be larger than -80 and smaller than 84 as
            // UTM does not span the entire globe
            //
            if (mLatitude <= -80.0 || mLatitude >= 84.0){
                mLatDigDegInput.setText(R.string.input_wrong_range_error);
                inputsValid = false;
            }

        } catch (NumberFormatException e) {
            // lat did not contain a valid double
            mLatDigDegInput.setText(R.string.input_double_format_error_string);
            inputsValid = false;

        }

        return inputsValid;

    }

    private boolean convertLongitude() {

        boolean inputsValid = true;

        try {
            //longitude must be >= -180.0 and <180.0
            mLongString = mLongDigDegInput.getText().toString();

            /***************************/
            //User may input either DD or DMS
            //convert between the two
            //determine which input field was used: DD or DMS
            if (mLongString.isEmpty()) {
                //value isn't in the decimal degrees field
                //check in the separate fields
                mDegreeString = mLongDegInput.getText().toString();
                mMinuteString = mLongMinInput.getText().toString();
                mSecondString = mLongSecInput.getText().toString();
                if (mDegreeString.isEmpty()) {
                    mDegreeString = "0.0";
                }
                if (mMinuteString.isEmpty()) {
                    mMinuteString = "0.0";
                }
                if (mSecondString.isEmpty()) {
                    mSecondString = "0.0";
                }

                mDegree = Double.parseDouble(mDegreeString);
                mMinute = Double.parseDouble(mMinuteString);
                mSecond = Double.parseDouble(mSecondString);
                mLongitude = mDegree + (mMinute / 60) + (mSecond / (60 * 60));
                //show the user the value we are actually converting
                mLongString = "0.0";
                mLongDigDegInput.setText(String.valueOf(mLongitude));
                mLongDegInput.setText(String.valueOf((int) mDegree));
                mLongMinInput.setText(String.valueOf((int) mMinute));
                mLongSecInput.setText(String.valueOf(mSecond));
                setLongColor();

            } else {
                mLongitude = Double.parseDouble(mLongString);

                if (mLongitude >= -180.0 || mLongitude > 180.0) {

                    //While here, show the DMS of the DD
                    int intDegree = (int) mLongitude; //strip out the decimal parts of mLongitude
                    mDegree = (double) intDegree;

                    //remainder will have decimal minutes and seconds
                    double remainder = mLongitude - mDegree;

                    //integer part is minutes, decimal part is seconds
                    mMinute = remainder * 60;
                    int intMinute = (int) mMinute; //pure minutes
                    //save the minutes in double in case we need it later
                    mMinute = intMinute;

                    //decimal part is seconds
                    mSecond = remainder - (mMinute / 60);
                    mSecond = mSecond * 60 * 60;

                    //truncate to a reasonable number of decimal digits
                    BigDecimal bd = new BigDecimal(mSecond).setScale(5, RoundingMode.HALF_UP);
                    mSecond = bd.doubleValue();

                    //Show the user the DMS value
                    mLongDegInput.setText(String.valueOf(intDegree));
                    mLongMinInput.setText(String.valueOf(intMinute));
                    mLongSecInput.setText(Double.toString(mSecond));
                    setLongColor();
                }
            }
/******************************/


            mLongitude = Double.parseDouble(mLongString);

            //Don't trust the conversion routines to do the checking
            // or to throw IllegalArgumentException  if not in proper format
            if (mLongitude < -180.0 || mLongitude >= 180.0) {
                mLongDigDegInput.setText(R.string.input_wrong_range_error);
                inputsValid = false;
            }

        } catch (NumberFormatException e) {
            // long did not contain a valid double
            mLongDigDegInput.setText(R.string.input_double_format_error_string);
            inputsValid = false;
        }

        return inputsValid;
    }

    private void clearForm() {

        mLatDigDegInput.  setText("");
        mLatDegInput.setText("");
        mLatMinInput.setText("");
        mLatSecInput.     setText("");

        mLongDigDegInput. setText("");
        mLongDegInput.setText("");
        mLongMinInput.setText("");
        mLongSecInput.setText("");

        mUtmIntegerOutput.setText("");
        mUtmNmOutput.setText("");
        mUtmSOOutput.     setText("");

        mUtmNmZoneOutput.setText(R.string.utm_zone_label);
        mUtmNmLatBandOutput.setText(R.string.utm_latband_label);
        mUtmNmHemisphereOutput.setText(R.string.utm_hemisphere_label);
        mUtmNmEastingOutput.setText(R.string.utm_easting_label);
        mUtmNmNorthingOutput.setText(R.string.utm_northing_label);

        setLatColorPos();
        setLongColorPos();
    }

    private void setLatColor(){
        if (mLatitude >= 0.0) {
            setLatColorPos();

        } else {
            setLatColorNeg();
        }
    }

    private void setLongColor(){
        if (mLongitude >= 0.0) {
            setLongColorPos();

        } else {
            setLongColorNeg();

        }
    }

    private void setLatColorNeg(){

        mLatDigDegInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mLatDegInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mLatMinInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mLatSecInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));

    }

    private void setLatColorPos(){

        mLatDigDegInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mLatDegInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mLatMinInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mLatSecInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));

    }

    private void setLongColorNeg(){

        mLongDigDegInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mLongDegInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mLongMinInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        mLongSecInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorNegNumber));

    }

    private void setLongColorPos(){

        mLongDigDegInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mLongDegInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mLongMinInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));
        mLongSecInput.setTextColor(
                ContextCompat.getColor(getActivity(),R.color.colorPosNumber));

    }

}


