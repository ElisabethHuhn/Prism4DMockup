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

/**
 * The Collect Fragment is the UI
 * when the user is making point measurements in the field
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DCoordConversionFragment extends Fragment {

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


    private Prism4DWSG84Coordinate mWSG84Coordinate;
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


    public MainPrism4DCoordConversionFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_coord_conversion_prism4d, container, false);


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

    private boolean convertInputs() {
        mWSG84Coordinate = new Prism4DWSG84Coordinate(mLatDigDegInput.getText(),
                                                      mLongDigDegInput.getText());
        if (!mWSG84Coordinate.isValidCoordinate()) {
            mWSG84Coordinate = new Prism4DWSG84Coordinate(
                    mLatDegInput.getText(),
                    mLatMinInput.getText(),
                    mLatSecInput.getText(),
                    mLongDegInput.getText(),
                    mLongMinInput.getText(),
                    mLongSecInput.getText());
            if (!mWSG84Coordinate.isValidCoordinate()){
                Toast.makeText(getActivity(),
                        R.string.coordinate_try_again,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //display the coordinate values in the UI
        mLatDigDegInput.setText(String.valueOf(mWSG84Coordinate.getLatitude()));
        mLatDegInput   .setText(String.valueOf(mWSG84Coordinate.getLatitudeDegree()));
        mLatMinInput   .setText(String.valueOf(mWSG84Coordinate.getLatitudeMinute()));
        mLatSecInput   .setText(String.valueOf(mWSG84Coordinate.getLatitudeSecond()));

        mLongDigDegInput.setText(String.valueOf(mWSG84Coordinate.getLongitude()));
        mLongDegInput   .setText(String.valueOf(mWSG84Coordinate.getLongitudeDegree()));
        mLongMinInput   .setText(String.valueOf(mWSG84Coordinate.getLongitudeMinute()));
        mLongSecInput   .setText(String.valueOf(mWSG84Coordinate.getLongitudeSecond()));

        setLatColor();
        setLongColor();

        return true;

    }

    private void performConversion() {


        /*
         * Compare three conversion routines:
         * 1) IBM
         * 2) Stack Overflow
         * 3) Prism4D developed from scratch, based on Karney (2010)
         *
         *
         *
         */

        //"Legal ranges: latitude [-90,90], longitude [-180,180).");
        boolean inputsValid = convertInputs() ;

        //only attempt the conversions if the inputs are valid
         if (inputsValid) {

            //The IBM code:
            try {
                //an actual conversion
                IBMCoordinateConversion coordinateConversion =
                                            new IBMCoordinateConversion();

                //integer precision (meter)
                String utmStringCoordinates =
                        coordinateConversion.latLon2UTM(mWSG84Coordinate.getLatitude(),
                                                        mWSG84Coordinate.getLongitude());
                mUtmIntegerOutput.setText(utmStringCoordinates);

            } catch (IllegalArgumentException exc) {
                //input parameters were not within range
                mLatDigDegInput.setText(R.string.input_wrong_range_error);
                mLongDigDegInput.setText(R.string.input_wrong_range_error);
                inputsValid = false;
            }
        }

        if (inputsValid) {
            //The stack overflow conversion code:

            try {
                //create a new WGS84 object from the Lat / Long coordinates
                WGS84 latlong = new WGS84(mWSG84Coordinate.getLatitude(),
                                          mWSG84Coordinate.getLongitude());

                //Now convert the coordinates into UTM
                //There is not very robust exception handling in this class
                UTM utmCoordinates = new UTM(latlong);

                mUtmSOOutput.setText(utmCoordinates.toString());
            } catch (IllegalArgumentException exc) {
                //input parameters were not within range
                mLatDigDegInput.setText(R.string.input_wrong_range_error);
                mLongDigDegInput.setText(R.string.input_wrong_range_error);
                inputsValid = false;
            }
        }
        if (inputsValid){
            //Create the UTM coordinate based on the WSG coordinate from the user
            // The Prism4D conversion
            // algorithm based on Kearny (2010)
            // supposed nanometer accuracy

            try{
                Prism4DUTM utmCoordinate = new Prism4DUTM(mWSG84Coordinate);

                //Also output the result in separate fields
                mUtmNmZoneOutput.setText(utmCoordinate.getZone());
                mUtmNmHemisphereOutput.setText(utmCoordinate.getHemisphere());
                mUtmNmLatBandOutput.setText(utmCoordinate.getLatBand());
                mUtmNmEastingOutput.setText(utmCoordinate.getEasting());
                mUtmNmNorthingOutput.setText(utmCoordinate.getNorthing());
            } catch (IllegalArgumentException exc) {
                //input parameters were not within range
                mLatDigDegInput.setText(R.string.input_wrong_range_error);
                mLongDigDegInput.setText(R.string.input_wrong_range_error);
                inputsValid = false;
            }
        }

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
        if (mWSG84Coordinate.getLatitude() >= 0.0) {
            setLatColorPos();

        } else {
            setLatColorNeg();
        }
    }

    private void setLongColor(){
        if (mWSG84Coordinate.getLongitude() >= 0.0) {
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


