package com.asc.msigeosystems.prism4d;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * The Maintain Point Fragment
 * is passed a point on startup. The point attribute fields are
 * pre-populated prior to updating the point
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DPointEditFragment extends Fragment  {


    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */



    /***********************************************************/
    /**********  Point Attribute UI Widgets     ****************/
    /***********************************************************/
    private EditText mPointProjectIDInput;
    private EditText mPointProjectNameInput;
    private EditText mPointIDInput;
    private EditText mPointFeatureCodeInput;
    private EditText mPointNotesInput;


    private TextView mPointCoordinateTypeLabel;

    /***********************************************************/
    /**********  Lat/Long Coordinate UI Widgets ****************/
    /***********************************************************/
    private EditText mPointLatitudeDDInput;
    private EditText mPointLatitudeDInput;
    private EditText mPointLatitudeMInput;
    private EditText mPointLatitudeSInput;

    private EditText mPointLongitudeDDInput;
    private EditText mPointLongitudeDInput;
    private EditText mPointLongitudeMInput;
    private EditText mPointLongitudeSInput;

    private EditText mPointElevationMetersInput;
    private EditText mPointElevationFeetInput;
    private EditText mPointGeoidMetersInput;
    private EditText mPointGeoidFeetInput;


    /***********************************************************/
    /*  Lat/Long Old values to determine if they have changed **/
    /***********************************************************/
    private String mPointLatitudeDDOld;
    private String mPointLatitudeDOld;
    private String mPointLatitudeMOld;
    private String mPointLatitudeSOld;

    private String mPointLongitudeDDOld;
    private String mPointLongitudeDOld;
    private String mPointLongitudeMOld;
    private String mPointLongitudeSOld;

    private String mPointElevationMetersOld;
    private String mPointElevationFeetOld;
    private String mPointGeoidMetersOld;
    private String mPointGeoidFeetOld;

    /***********************************************************/
    /**********  E/N Coordinate UI Widgets      ****************/
    /***********************************************************/
    private EditText       mPointEastingMetersInput;
    private EditText       mPointNorthingMetersInput;
    private EditText       mPointEastingFeetInput;
    private EditText       mPointNorthingFeetInput;
    private EditText       mPointENElevationMetersInput;
    private EditText       mPointENElevationFeetInput;

    private EditText       mPointZoneInput;
    private EditText       mPointHemisphereInput;
    private EditText       mPointLatbandInput;
    private EditText       mPointDatumInput;
    private EditText       mPointConvergenceInput;
    private EditText       mPointScaleFactorInput;
    private EditText       mPointElevationInput;

    /***********************************************************/
    /*    E/N Old values to determine if they have changed    **/
    /***********************************************************/
    private String       mPointEastingMetersOld;
    private String       mPointNorthingMetersOld;
    private String       mPointEastingFeetOld;
    private String       mPointNorthingFeetOld;
    private String       mPointENElevationMetersOld;
    private String       mPointENElevationFeetOld;

    private String       mPointZoneOld;
    private String       mPointHemisphereOld;
    private String       mPointLatbandOld;
    private String       mPointConvergenceOld;
    private String       mPointScaleFactorOld;
    private String       mPointElevationOld;
    /***********************************************************/
    /**********  Other UI Widgets     **************************/
    /***********************************************************/

    private Button mPointExitButton;
    private Button mPointViewExistingButton;
    private Button mPointSaveChangesButton;



    /***********************************************************/
    /**********  Point Attribute Variables      ****************/
    /***********************************************************/

    private Prism4DPoint mPointBeingMaintained;
    private int          mCoordinateWidgetType;



    /***********************************************************/
    /**********  Lat/Long Coordinate Variables  ****************/
    /***********************************************************/

    private Prism4DCoordinateWGS84 mCoordinateWGS84;


    /***********************************************************/
    /**********  E/N Coordinate Variables       ****************/
    /***********************************************************/


    /***********************************************************/
    /**********  Other Variables                ****************/
    /***********************************************************/


    private boolean      mPointChanged = false;
    private CharSequence mPointPath;




    /***********************************************************/
    /**********  Constructor                    ****************/
    /***********************************************************/

    public MainPrism4DPointEditFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }


    /***********************************************************/
    /**********  Lifecycle Methods                   ***********/
    /***********************************************************/

    public static MainPrism4DPointEditFragment newInstance(int            projectID,
                                                           Prism4DPath    pointPath,
                                                           Prism4DPoint   point) {

        Bundle args = Prism4DPoint.putPointInArguments(new Bundle(), projectID, point);
        args = Prism4DPath.putPathInArguments(args, pointPath);

        MainPrism4DPointEditFragment fragment = new MainPrism4DPointEditFragment();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        Prism4DPath path = Prism4DPath.getPathFromArguments(getArguments());
        mPointPath       = path.getPath();

        mPointBeingMaintained = Prism4DPoint.getPointFromArguments(getArguments());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(
                R.layout.fragment_point_edit_prism4d,
                container,
                false);


        //Wire up the UI widgets so they can handle events later
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields
        wireWidgets(v);


        //Need to add widgets for the coordinates, but there are differnt widgets
        //depending upon the type of coordinate: Latitude/Longitude or Easting/Northing

        //Regardless, we need the inflater and the LinearLayout container for the coordinates
        //Get an inflater
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(getActivity());
        //Get the LinearLayout which will contain the coordinates
        LinearLayout   coordinatesContainer    = (LinearLayout) v.findViewById(R.id.point_coordinate_container);

        //get the coordinate type out of the project
        mCoordinateWidgetType = getCoordinateTypeFromProject();

        if (mCoordinateWidgetType == Prism4DCoordinate.sLLWidgets){
            //inflate the coordinates and
            // attach the coordinates to the LinearLayout which exists to contain the coordinates
            View coordinatesLayout = layoutInflater.inflate(R.layout.element_ll_coordinate,
                                                            coordinatesContainer,
                                                            true);
            //then wire up the appropriate widgets for this type of coordinate
            wireLLCoordinateWidgets(v);
        } else if (mCoordinateWidgetType == Prism4DCoordinate.sENWidgets) {
            //inflate the coordinates, and
            // attach the coordinates to the LinearLayout which exists to contain the coordinates
            View coordinatesLayout = layoutInflater.inflate(R.layout.element_en_coordinate,
                                                            coordinatesContainer,
                                                            true);

            //then wire up the appropriate widgets for this type of coordinat
            wireENCoordinateWidgets(v);
        }

        initializeUI();

        if (mCoordinateWidgetType == Prism4DCoordinate.sLLWidgets){
            saveLLFieldsAsOldValues();
        }else if (mCoordinateWidgetType == Prism4DCoordinate.sENWidgets) {
            saveENFieldsAsOldValues();
        }

        //Show the subtitle for this fragment
        ((MainPrism4DActivity) getActivity()).switchSubtitle(getString(R.string.subtitle_edit_point));

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        setSubtitle();
    }



    /***********************************************************/
    /**********     Initialization                   ***********/
    /***********************************************************/

    private void setSubtitle(){
        ((MainPrism4DActivity)getActivity())
                .switchSubtitle(R.string.subtitle_edit_point);
    }

    private void wireWidgets(View v){

        //Project ID
        mPointProjectIDInput = (EditText) v.findViewById(R.id.pointProjectIDInput);
        //Can't change the project of a point
        mPointProjectIDInput.setFocusable(false);

        //Project name
        mPointProjectNameInput = (EditText) v.findViewById(R.id.pointProjectNameInput);
        //Can't change the project of a point
        mPointProjectNameInput.setFocusable(false);



        //Point ID
        mPointIDInput = (EditText) v.findViewById(R.id.pointIDInput);
        mPointIDInput.setFocusable(false);


        //Point Description
        mPointFeatureCodeInput = (EditText) v.findViewById(R.id.pointFeatureCodeInput);
        mPointFeatureCodeInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Notes
        mPointNotesInput = (EditText) v.findViewById(R.id.pointNotesInput);
        mPointNotesInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Coordinate type
        mPointCoordinateTypeLabel = (TextView)v.findViewById(R.id.coordinate_label);


        //Exit Button
        mPointExitButton = (Button) v.findViewById(R.id.pointExitButton);
        mPointExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainPrism4DActivity)getActivity()).popToTopCogoScreen();
            }
        });



        //View Existing Points Button
        mPointViewExistingButton = (Button) v.findViewById(R.id.pointViewExistingButton);
        if (mPointPath.equals(Prism4DPath.sCreateTag)){
            //disable the button on the create path
            mPointViewExistingButton.setEnabled(false);
            mPointViewExistingButton.setTextColor(Color.GRAY);
        }
        mPointViewExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                onListPoints();


           }
        });


        //Save Changes Button
        mPointSaveChangesButton = (Button) v.findViewById(R.id.pointSaveChangesButton);
        //button is enabled once something changes
        mPointSaveChangesButton.setEnabled(false);
        mPointSaveChangesButton.setTextColor(Color.GRAY);
        mPointSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onSave();
            }
        });


    }

    private void wireLLCoordinateWidgets(View v){
        /****************************************************************************/
        /********             Latitude                                       ********/
        /****************************************************************************/
        View field_container;
        TextView label;

        //set up the UI widgets for the latitude/longetude coordinates
        field_container = v.findViewById(R.id.latitudeContainer);
        label = (TextView)field_container.findViewById(R.id.ll_label);
        label.setText(getString(R.string.latitude_label));
        mPointLatitudeDDInput = (EditText) field_container.findViewById(R.id.ll_dd_input);
        mPointLatitudeDDInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                                           InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                           InputType.TYPE_NUMBER_FLAG_SIGNED);

        mPointLatitudeDInput  = (EditText) field_container.findViewById(R.id.ll_d_Input) ;
        mPointLatitudeMInput  = (EditText) field_container.findViewById(R.id.ll_m_Input);
        mPointLatitudeSInput  = (EditText) field_container.findViewById(R.id.ll_s_Input) ;
        mPointLatitudeSInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                                          InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                          InputType.TYPE_NUMBER_FLAG_SIGNED);




        mPointLatitudeDDInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Digital Degree just lost focus,
                    //has the value changed?
                    String temp = mPointLatitudeDDInput.getText().toString();
                    if (!temp.equals(mPointLatitudeDDOld)){
                        setPointChangedFlags();
                        // so convert Latitude DD to DMS
                        boolean isLatitude = true;
                        //Prism4DCoordinateLL.
                        convertDDtoDMS( getActivity(),
                                        mPointLatitudeDDInput,
                                        mPointLatitudeDInput,
                                        mPointLatitudeMInput,
                                        mPointLatitudeSInput,
                                        isLatitude);
                        mPointLatitudeDOld = mPointLatitudeDInput.getText().toString();
                        mPointLatitudeMOld = mPointLatitudeMInput.getText().toString();
                        mPointLatitudeSOld = mPointLatitudeSInput.getText().toString();
                        mPointLatitudeDDOld = temp;
                    }

                }
            }
        });
        mPointLatitudeDInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Degree just lost focus,
                    //has the value changed?
                    String temp = mPointLatitudeDInput.getText().toString();
                    if (!temp.equals(mPointLatitudeDOld)){
                        setPointChangedFlags();
                        // so convert Latitude DMS to DD
                        boolean isLatitude = true;
                        //Prism4DCoordinateLL.
                        convertDMStoDD( getActivity(),
                                        mPointLatitudeDDInput,
                                        mPointLatitudeDInput,
                                        mPointLatitudeMInput,
                                        mPointLatitudeSInput,
                                        isLatitude);
                        mPointLatitudeDDOld = mPointLatitudeDDInput.getText().toString();
                        mPointLatitudeDOld = temp;
                    }

                }
            }
        });
        mPointLatitudeMInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Minute just lost focus,
                    //has the value changed?
                    String temp = mPointLatitudeMInput.getText().toString();
                    if (!temp.equals(mPointLatitudeMOld)){
                        setPointChangedFlags();
                        // so convert Latitude DMS to DD
                        boolean isLatitude = true;
                        //Prism4DCoordinateLL.
                                            convertDMStoDD( getActivity(),
                                                            mPointLatitudeDDInput,
                                                            mPointLatitudeDInput,
                                                            mPointLatitudeMInput,
                                                            mPointLatitudeSInput,
                                                            isLatitude);
                        mPointLatitudeDDOld = mPointLatitudeDDInput.getText().toString();
                        mPointLatitudeMOld = temp;
                    }

                }
            }
        });
         mPointLatitudeSInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Seconds just lost focus,
                    //has the value changed?
                    String temp = mPointLatitudeSInput.getText().toString();
                    if (!temp.equals(mPointLatitudeSOld)){
                        setPointChangedFlags();
                        // so convert Latitude DMS to DD
                        boolean isLatitude = true;
                        //Prism4DCoordinateLL.
                                            convertDMStoDD( getActivity(),
                                                            mPointLatitudeDDInput,
                                                            mPointLatitudeDInput,
                                                            mPointLatitudeMInput,
                                                            mPointLatitudeSInput,
                                                            isLatitude);
                        mPointLatitudeDDOld = mPointLatitudeDDInput.getText().toString();
                        mPointLatitudeSOld = temp;
                    }

                }
            }
        });



        /****************************************************************************/
        /********             Longitude                                      ********/
        /****************************************************************************/


        field_container = v.findViewById(R.id.longitudeContainer);
        label = (TextView)field_container.findViewById(R.id.ll_label);
        label.setText(getString(R.string.longitude_label));
        mPointLongitudeDDInput = (EditText) field_container.findViewById(R.id.ll_dd_input);
        mPointLongitudeDDInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                                            InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                            InputType.TYPE_NUMBER_FLAG_SIGNED);

        mPointLongitudeDInput  = (EditText) field_container.findViewById(R.id.ll_d_Input) ;
        mPointLongitudeMInput  = (EditText) field_container.findViewById(R.id.ll_m_Input);
        mPointLongitudeSInput  = (EditText) field_container.findViewById(R.id.ll_s_Input) ;
        mPointLongitudeSInput.setInputType( InputType.TYPE_CLASS_NUMBER |
                                            InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                            InputType.TYPE_NUMBER_FLAG_SIGNED);




        mPointLongitudeDDInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Digital Degree just lost focus,
                    //has the value changed?
                    String temp = mPointLongitudeDDInput.getText().toString();
                    if (!temp.equals(mPointLongitudeDDOld)){
                        setPointChangedFlags();
                        // so convert Longitude DD to DMS
                        boolean isLatitude = false;
                        //Prism4DCoordinateLL.
                                convertDDtoDMS( getActivity(),
                                                            mPointLongitudeDDInput,
                                                            mPointLongitudeDInput,
                                                            mPointLongitudeMInput,
                                                            mPointLongitudeSInput,
                                                            isLatitude);

                        mPointLongitudeDDOld = temp;
                        mPointLongitudeDOld  = mPointLongitudeDInput.getText().toString();
                        mPointLongitudeMOld  = mPointLongitudeMInput.getText().toString();
                        mPointLongitudeSOld  = mPointLongitudeSInput.getText().toString();
                    }

                }
            }
        });

        mPointLongitudeDInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Degree just lost focus,
                    //has the value changed?
                    String temp = mPointLongitudeDInput.getText().toString();
                    if (!temp.equals(mPointLongitudeDOld)){
                        setPointChangedFlags();
                        // so convert Longitude DMS to DD
                        boolean isLatitude = false;
                        //Prism4DCoordinateLL.
                                convertDMStoDD( getActivity(),
                                                            mPointLongitudeDDInput,
                                                            mPointLongitudeDInput,
                                                            mPointLongitudeMInput,
                                                            mPointLongitudeSInput,
                                                            isLatitude);
                        mPointLongitudeDOld = temp;
                        mPointLongitudeDDOld = mPointLongitudeDDInput.getText().toString();
                    }

                }
            }
        });


         mPointLongitudeMInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Minute just lost focus,
                    //has the value changed?
                    String temp = mPointLongitudeMInput.getText().toString();
                    if (!temp.equals(mPointLongitudeMOld)){
                        setPointChangedFlags();
                        // so convert Longitude DMS to DD
                        boolean isLatitude = false;
                        //Prism4DCoordinateLL.
                                convertDMStoDD( getActivity(),
                                                            mPointLongitudeDDInput,
                                                            mPointLongitudeDInput,
                                                            mPointLongitudeMInput,
                                                            mPointLongitudeSInput,
                                                            isLatitude);
                        mPointLongitudeMOld = temp;
                        mPointLongitudeDDOld = mPointLongitudeDDInput.getText().toString();
                    }

                }
            }
        });


        mPointLongitudeSInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Second just lost focus,
                    //has the value changed?
                    String temp = mPointLongitudeSInput.getText().toString();
                    if (!temp.equals(mPointLongitudeSOld)){
                        setPointChangedFlags();
                        // so convert Longitude DMS to DD
                        boolean isLatitude = false;
                        //Prism4DCoordinateLL.
                                convertDMStoDD( getActivity(),
                                                            mPointLongitudeDDInput,
                                                            mPointLongitudeDInput,
                                                            mPointLongitudeMInput,
                                                            mPointLongitudeSInput,
                                                            isLatitude);
                        mPointLongitudeSOld = temp;
                        mPointLongitudeDDOld = mPointLongitudeDDInput.getText().toString();
                    }

                }
            }
        });

        /****************************************************************************/
        /********             Elevation                                      ********/
        /****************************************************************************/

        field_container = v.findViewById(R.id.elevationGeoidContainer);
        mPointElevationMetersInput = (EditText) field_container.findViewById(R.id.elevationMetersInput);
        mPointElevationMetersInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mPointElevationFeetInput   = (EditText) field_container.findViewById(R.id.elevationFeetInput) ;
        mPointElevationFeetInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);



        mPointElevationMetersInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Meters just lost focus,
                    // has the value changed
                    String temp = mPointElevationMetersInput.getText().toString();
                    if (!temp.equals(mPointElevationMetersOld)){
                        setPointChangedFlags();
                        // so convert Meters to Feet
                        Prism4DConstantsAndUtilities.convertMetersToFeet(getActivity(),
                                                                         mPointElevationMetersInput,
                                                                         mPointElevationFeetInput);
                        mPointElevationMetersOld = temp;
                        mPointElevationFeetOld = mPointElevationFeetInput.getText().toString();
                    }

                }
            }
        });

        mPointElevationFeetInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Feet just lost focus,
                    // has the value changed
                    String temp = mPointElevationFeetInput.getText().toString();
                    if (!temp.equals(mPointElevationFeetOld)){
                        setPointChangedFlags();
                        // so convert Feet to Meters
                        Prism4DConstantsAndUtilities.convertFeetToMeters(getActivity(),
                                                                         mPointElevationMetersInput,
                                                                         mPointElevationFeetInput);
                        mPointElevationFeetOld = temp;
                        mPointElevationMetersOld = mPointElevationMetersInput.getText().toString();
                    }

                }
            }
        });


        /****************************************************************************/
        /********             Geoid                                          ********/
        /****************************************************************************/
        mPointGeoidMetersInput = (EditText) field_container.findViewById(R.id.GeoidHeightMetersInput);
        mPointGeoidMetersInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mPointGeoidFeetInput   = (EditText) field_container.findViewById(R.id.GeoidHeightFeetInput);
        mPointGeoidFeetInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);



        mPointGeoidMetersInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Meters just lost focus,
                    // has the value changed
                    String temp = mPointGeoidMetersInput.getText().toString();
                    if (!temp.equals(mPointGeoidMetersOld)){
                        setPointChangedFlags();
                        // so convert Meters to Feet
                        Prism4DConstantsAndUtilities.convertMetersToFeet(getActivity(),
                                                                         mPointGeoidMetersInput,
                                                                         mPointGeoidFeetInput);
                        mPointGeoidMetersOld = temp;
                        mPointGeoidFeetOld = mPointGeoidFeetInput.getText().toString();
                    }
                }
            }
        });

        mPointGeoidFeetInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Feet just lost focus,
                    // has the value changed
                    String temp = mPointGeoidFeetInput.getText().toString();
                    if (!temp.equals(mPointGeoidFeetOld)){
                        setPointChangedFlags();
                        // so convert Feet to Meters
                        Prism4DConstantsAndUtilities.convertFeetToMeters(getActivity(),
                                                                         mPointGeoidMetersInput,
                                                                         mPointGeoidFeetInput);
                        mPointGeoidFeetOld = temp;
                        mPointGeoidMetersOld = mPointGeoidMetersInput.getText().toString();
                    }
                }
            }
        });

    }


    private void wireENCoordinateWidgets(View v){

        View field_container;
        TextView label;

        MainPrism4DActivity myActivity = (MainPrism4DActivity)getActivity();


        //set up the widgets for the easting/northing oordinate


        field_container = v.findViewById(R.id.eastingContainer);
        label = (TextView) field_container.findViewById(R.id.en_label) ;
        label.setText(getString(R.string.easting_label));
        mPointEastingMetersInput = (EditText)field_container.findViewById(R.id.metersOutput);
        mPointEastingMetersInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                                                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mPointEastingMetersInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Meters just lost focus,
                    // has the value changed
                    String temp = mPointEastingMetersInput.getText().toString();
                    if (!temp.equals(mPointEastingMetersOld)){
                        setPointChangedFlags();
                        // so convert Meters to Feet
                        Prism4DConstantsAndUtilities.convertMetersToFeet(getActivity(),
                                                                         mPointEastingMetersInput,
                                                                         mPointEastingFeetInput);
                        mPointEastingMetersOld = temp;
                        mPointEastingFeetOld = mPointEastingFeetInput.getText().toString();
                    }

                }
            }
        });


        mPointEastingFeetInput   = (EditText)field_container.findViewById(R.id.feetOutput);
        mPointEastingFeetInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mPointEastingFeetInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Feet just lost focus,
                    // has the value changed
                    String temp = mPointEastingFeetInput.getText().toString();
                    if (!temp.equals(mPointEastingFeetOld)){
                        setPointChangedFlags();
                        // so convert Feet to Meters
                        Prism4DConstantsAndUtilities.convertFeetToMeters(getActivity(),
                                                                         mPointEastingMetersInput,
                                                                         mPointEastingFeetInput);
                        mPointEastingFeetOld = temp;
                        mPointEastingMetersOld = mPointEastingMetersInput.getText().toString();
                    }

                }
            }
        });


        field_container = v.findViewById(R.id.northingContainer);
        label = (TextView) field_container.findViewById(R.id.en_label) ;
        label.setText(getString(R.string.northing_label));
        mPointNorthingMetersInput = (EditText)field_container.findViewById(R.id.metersOutput);
        mPointNorthingMetersInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mPointNorthingMetersInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Meters just lost focus,
                    // has the value changed
                    String temp = mPointNorthingMetersInput.getText().toString();
                    if (!temp.equals(mPointNorthingMetersOld)){
                        setPointChangedFlags();
                        // so convert Meters to Feet
                        Prism4DConstantsAndUtilities.convertMetersToFeet(getActivity(),
                                                                         mPointNorthingMetersInput,
                                                                         mPointNorthingFeetInput);
                        mPointNorthingMetersOld = temp;
                        mPointNorthingFeetOld = mPointNorthingFeetInput.getText().toString();
                    }

                }
            }
        });
        mPointNorthingFeetInput   = (EditText)field_container.findViewById(R.id.feetOutput);
        mPointNorthingFeetInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mPointNorthingFeetInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Feet just lost focus,
                    // has the value changed
                    String temp = mPointNorthingFeetInput.getText().toString();
                    if (!temp.equals(mPointNorthingFeetOld)){
                        setPointChangedFlags();
                        // so convert Feet to Meters
                        Prism4DConstantsAndUtilities.convertFeetToMeters(getActivity(),
                                                                         mPointNorthingMetersInput,
                                                                         mPointNorthingFeetInput);
                        mPointNorthingFeetOld = temp;
                        mPointNorthingMetersOld = mPointNorthingMetersInput.getText().toString();
                    }

                }
            }
        });


        field_container = v.findViewById(R.id.elevationContainer);
        mPointENElevationMetersInput = (EditText)field_container.findViewById(R.id.elevationMetersInput);
        mPointENElevationMetersInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mPointENElevationMetersInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Meters just lost focus,
                    // has the value changed
                    String temp = mPointENElevationMetersInput.getText().toString();
                    if (!temp.equals(mPointENElevationMetersOld)){
                        setPointChangedFlags();
                        // so convert Meters to Feet
                        Prism4DConstantsAndUtilities.convertMetersToFeet(getActivity(),
                                                                         mPointENElevationMetersInput,
                                                                         mPointENElevationFeetInput);
                        mPointENElevationMetersOld = temp;
                        mPointENElevationFeetOld = mPointENElevationFeetInput.getText().toString();
                    }

                }
            }
        });

        mPointENElevationFeetInput   = (EditText)field_container.findViewById(R.id.elevationFeetInput);
        mPointENElevationFeetInput.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        mPointENElevationFeetInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Feet just lost focus,
                    // has the value changed
                    String temp = mPointENElevationFeetInput.getText().toString();
                    if (!temp.equals(mPointENElevationFeetOld)){
                        setPointChangedFlags();
                        // so convert Feet to Meters
                        Prism4DConstantsAndUtilities.convertFeetToMeters(getActivity(),
                                                                        mPointENElevationMetersInput,
                                                                         mPointENElevationFeetInput);



                        mPointENElevationFeetOld = temp;
                        mPointENElevationMetersOld = mPointElevationMetersInput.getText().toString();
                    }

                }
            }
        });




        field_container = v.findViewById(R.id.zhlContainer);
        label = (TextView) field_container.findViewById(R.id.coord_label) ;
        //label.setText();
        mPointZoneInput       = (EditText) field_container.findViewById(R.id.zoneOutput);
        mPointHemisphereInput = (EditText) field_container.findViewById(R.id.hemisphereOutput);
        mPointLatbandInput    = (EditText) field_container.findViewById(R.id.latbandOutput);


        field_container = v.findViewById(R.id.convergenceContainer);
        mPointDatumInput       = (EditText) field_container.findViewById(R.id.datumOutput);
        mPointConvergenceInput = (EditText) field_container.findViewById(R.id.convergenceOutput);
        mPointScaleFactorInput = (EditText) field_container.findViewById(R.id.scaleFactorOutput);

    }


    //last parameter indicates whether latitude (true) or longitude (false)
    public static boolean convertDDtoDMS(Context context,
                                         EditText tudeDDInput,
                                         EditText tudeDInput,
                                         EditText tudeMInput,
                                         EditText tudeSInput,
                                         boolean  isLatitude) {

        String tudeString = tudeDDInput.getText().toString().trim();
        if (tudeString.isEmpty()) {
            tudeString = context.getString(R.string.zero_decimal_string);
            tudeDDInput.setText(tudeString);
        }

        double tude = Double.parseDouble(tudeString);

        //The user inputs have to be within range to be
        if (   (isLatitude   && ((tude < -90.0) || (tude >= 90.0)))  || //Latitude
                ((!isLatitude)  && ((tude < -180.) || (tude >= 180.)))) {  //Longitude

            tudeDInput.setText(R.string.zero_decimal_string);
            tudeMInput.setText(R.string.zero_decimal_string);
            tudeSInput.setText(R.string.zero_decimal_string);
            return false;
        }

        //check sign of tude
        boolean isTudePos = true;
        int tudeColor= R.color.colorPosNumber;
        if (tude < 0) {
            //tude is negative, remember this and work with the absolute value
            tude = Math.abs(tude);
            isTudePos = false;
            tudeColor = R.color.colorNegNumber;
        }

        //strip out the decimal parts of tude
        int tudeDegree = (int) tude;

        double degree = tudeDegree;

        //digital degrees minus degrees will be decimal minutes plus seconds
        //converting to int strips out the seconds
        double minuteSec = tude - degree;
        double minutes = minuteSec * 60.;
        int tudeMinute = (int) minutes;
        double minuteOnly = (double) tudeMinute;

        //start with the DD, subtract out Degrees, subtract out Minutes
        //convert the remainder into whole seconds
        double tudeSecond = (tude - degree - (minuteOnly / 60.)) * (60. * 60.);
        //tudeSecond = (tude - minutes) * (60. *60.);

        //If tude was negative before, restore it to negative
        if (!isTudePos) {
            //tude       = 0. - tude;
            tudeDegree = 0 - tudeDegree;
            tudeMinute = 0 - tudeMinute;
            tudeSecond = 0. - tudeSecond;
        }

        //truncate to a reasonable number of decimal digits
        BigDecimal bd = new BigDecimal(tudeSecond).
                setScale(Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision, RoundingMode.HALF_UP);
        tudeSecond = bd.doubleValue();

        //show the user the result
        tudeDInput.setText(String.valueOf(tudeDegree));
        tudeMInput.setText(String.valueOf(tudeMinute));
        tudeSInput.setText(String.valueOf(tudeSecond));

        tudeDDInput.setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeDInput .setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeMInput .setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeSInput .setTextColor(ContextCompat.getColor(context, tudeColor));

        return true;
    }



    //last parameter indicates whether latitude (true) or longitude (false)
    public static boolean convertDMStoDD(Context  context,
                                         EditText tudeDDInput,
                                         EditText tudeDInput,
                                         EditText tudeMInput,
                                         EditText tudeSInput,
                                         boolean  isLatitude){

        //String tudeString = tudeDDInput.getText().toString().trim();
        String tudeDString = tudeDInput.getText().toString().trim();
        String tudeMString = tudeMInput.getText().toString().trim();
        String tudeSString = tudeSInput.getText().toString().trim();

        if (tudeDString == null){
            tudeDString = context.getString(R.string.zero_decimal_string);
        }

        //double tude = Double.parseDouble(tudeString);
        int    tudeD = Integer.parseInt(tudeDString);
        int    tudeM = Integer.parseInt(tudeMString);
        double tudeS = Double.parseDouble(tudeSString);

        if (    ((isLatitude) && ((tudeD <   -90) || (tudeD >=   90))) ||
                ((isLatitude) && ((tudeM <   -60) || (tudeM >    60))) ||
                ((isLatitude) && ((tudeS <   -60.)|| (tudeS >    60.)))||
                ((!isLatitude) && ((tudeD <  -180) || (tudeD >=  180))) ||
                ((!isLatitude) && ((tudeM <   -60) || (tudeM >    60))) ||
                ((!isLatitude) && ((tudeS <   -60.)|| (tudeS >    60.)))) {

            tudeDDInput.setText(R.string.zero_decimal_string);
            return false;
        }

        boolean isTudePos = true;
        int tudeColor = R.color.colorPosNumber;
        if ((tudeD < 0) || (tudeM < 0) || (tudeS < 0)){
            isTudePos = false;
            tudeD = Math.abs(tudeD);
            tudeM = Math.abs(tudeM);
            tudeS = Math.abs(tudeS);
            tudeColor = R.color.colorNegNumber;
        }

        double degrees = (double)tudeD;
        double minutes = (double)tudeM ;
        double seconds =         tudeS ;
        double tude =  degrees + (minutes/ 60.) + (seconds/ (60.*60.));


        if (!isTudePos) {
            tude  = 0. - tude;
        }

        tudeDDInput.setText(String.valueOf(tude));

        tudeDDInput.setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeDInput .setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeMInput .setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeSInput .setTextColor(ContextCompat.getColor(context, tudeColor));

        return true;
    }



    private int getCoordinateTypeFromProject() {
        //show the data that came out of the input arguments bundle
        int projectID = mPointBeingMaintained.getForProjectID();
        return Prism4DCoordinate.getCoordinateTypeFromProjectID(projectID);
    }



    private CharSequence getFullCoordTypeFromProject(){
        //show the data that came out of the input arguments bundle
        int projectID = mPointBeingMaintained.getForProjectID();

        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);

        return project.getProjectCoordinateType();

    }


    private void initializeUI() {
        //show the data that came out of the input arguments bundle
        int projectID = mPointBeingMaintained.getForProjectID();
        mPointProjectIDInput.setText(String.valueOf(projectID));
        mPointIDInput.setText       (String.valueOf (mPointBeingMaintained.getPointID()));

        //Project name
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);
        mPointProjectNameInput.setText(project.getProjectName());

        if (mPointBeingMaintained.getPointID() != 0){
            mPointFeatureCodeInput.setText(mPointBeingMaintained.getPointFeatureCode());
            mPointNotesInput.setText(mPointBeingMaintained.getPointNotes());
        }


        CharSequence coordinateType = project.getProjectCoordinateType();
        String msg = getString(R.string.coordinate_type) + " " + coordinateType ;

        if (coordinateType.equals(null)){
            msg = getString(R.string.point_no_coordinate_type);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeWGS84)) {
            initializeUIFromLLCoordinate(mPointBeingMaintained);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83) ) {
            initializeUIFromLLCoordinate(mPointBeingMaintained);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeUTM)) {
            initializeUIFromENCoordinate(mPointBeingMaintained);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeSPCS)) {
            initializeUIFromENCoordinate(mPointBeingMaintained);

        } else {
            msg = getString(R.string.unk_coord_type);
        }
        mPointCoordinateTypeLabel.setText(msg);
    }

    private void saveLLFieldsAsOldValues(){
        mPointLatitudeDDOld = mPointLatitudeDDInput.getText().toString();
        mPointLatitudeDOld  = mPointLatitudeDInput.getText().toString();
        mPointLatitudeMOld  = mPointLatitudeMInput.getText().toString();
        mPointLatitudeSOld  = mPointLatitudeSInput.getText().toString();

        mPointLongitudeDDOld = mPointLongitudeDDInput.getText().toString();
        mPointLongitudeDOld  = mPointLongitudeDInput.getText().toString();
        mPointLongitudeMOld  = mPointLongitudeMInput.getText().toString();
        mPointLongitudeSOld  = mPointLongitudeSInput.getText().toString();

        mPointElevationMetersOld = mPointElevationMetersInput.getText().toString();
        mPointElevationFeetOld   = mPointElevationFeetInput.getText().toString();
        mPointGeoidMetersOld     = mPointGeoidMetersInput.getText().toString();
        mPointGeoidFeetOld       = mPointGeoidFeetInput.getText().toString();

    }

    private void saveENFieldsAsOldValues(){
        mPointEastingMetersOld = mPointEastingMetersInput.getText().toString();
        mPointEastingFeetOld   = mPointEastingFeetInput.getText().toString();

        mPointNorthingMetersOld = mPointNorthingMetersInput.getText().toString();
        mPointNorthingFeetOld   = mPointNorthingFeetInput.getText().toString();

        mPointENElevationMetersOld = mPointENElevationMetersInput.getText().toString();
        mPointENElevationFeetOld   = mPointENElevationFeetInput.getText().toString();

        mPointZoneOld       = mPointZoneInput.getText().toString();
        mPointHemisphereOld = mPointHemisphereInput.getText().toString();
        mPointLatbandOld    = mPointLatbandInput.getText().toString();


        mPointConvergenceOld = mPointConvergenceInput.getText().toString();
        mPointScaleFactorOld = mPointScaleFactorInput.getText().toString();
    }



    private void initializeUIFromLLCoordinate(Prism4DPoint point ){

        Prism4DCoordinateLL coordinate = (Prism4DCoordinateLL) point.getCoordinate();
        if (coordinate == null) return;

        double latitude = coordinate.getLatitude();
        double longitude = coordinate.getLongitude();
        double elevation = coordinate.getElevation();
        double geoid     = coordinate.getGeoid();

        mPointLatitudeDDInput.setText(String.valueOf(latitude));
        mPointLatitudeDInput.setText(String.valueOf(coordinate.getLatitudeDegree()));
        mPointLatitudeMInput.setText(String.valueOf(coordinate.getLatitudeMinute()));
        mPointLatitudeSInput.setText(String.valueOf(coordinate.getLatitudeSecond()));

        mPointLongitudeDDInput.setText(String.valueOf(longitude));
        mPointLongitudeDInput.setText(String.valueOf(coordinate.getLongitudeDegree()));
        mPointLongitudeMInput.setText(String.valueOf(coordinate.getLongitudeMinute()));
        mPointLongitudeSInput.setText(String.valueOf(coordinate.getLongitudeSecond()));

        mPointElevationMetersInput.setText(String.valueOf(elevation));
        mPointElevationFeetInput.setText(String.valueOf(coordinate.getElevationFeet()));

        mPointGeoidMetersInput.setText(String.valueOf(geoid));
        mPointGeoidFeetInput.setText(String.valueOf(coordinate.getGeoidFeet()));

        if (latitude < 0.0){
            mPointLatitudeDDInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointLatitudeDInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
            mPointLatitudeMInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointLatitudeSInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        }
        if (longitude < 0.0){
            mPointLongitudeDDInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointLongitudeDInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
            mPointLongitudeMInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointLongitudeSInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        }
        if (elevation < 0.0){
            mPointElevationMetersInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointElevationFeetInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        }
        if (geoid < 0.0){
            mPointGeoidMetersInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointGeoidFeetInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        }

    }

    private void initializeUIFromENCoordinate(Prism4DPoint point ){
        Prism4DCoordinateEN coordinate = (Prism4DCoordinateEN)point.getCoordinate();
        if (coordinate == null)return;

        double easting = coordinate.getEasting();
        double northing = coordinate.getNorthing();
        double elevation = coordinate.getElevation();


        mPointEastingMetersInput.setText(String.valueOf(easting));
        mPointEastingFeetInput.setText(String.valueOf(coordinate.getEastingFeet()));

        mPointNorthingMetersInput.setText(String.valueOf(northing));
        mPointNorthingFeetInput.setText(String.valueOf(coordinate.getNorthingFeet()));

        mPointENElevationMetersInput.setText(String.valueOf(elevation));
        mPointENElevationFeetInput.setText(String.valueOf(coordinate.getElevationFeet()));

        mPointZoneInput.setText(String.valueOf(coordinate.getZone()));
        mPointLatbandInput.setText(String.valueOf(coordinate.getLatBand()));
        mPointHemisphereInput.setText(String.valueOf(coordinate.getHemisphere()));

        mPointDatumInput.setText(String.valueOf(coordinate.getDatum()));
        mPointConvergenceInput.setText(String.valueOf(coordinate.getConvergence()));
        mPointScaleFactorInput.setText(String.valueOf(coordinate.getScale()));

        if (elevation < 0.0){
            mPointElevationMetersInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointElevationFeetInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        }
        if (easting < 0.0){
            mPointEastingMetersInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointEastingFeetInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        }
        if (northing < 0.0){
            mPointNorthingMetersInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNegNumber));
            mPointNorthingFeetInput.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorNegNumber));
        }

    }




    /***********************************************************/
    /********     Called from EditText Listeners     ***********/
    /***********************************************************/



    /***********************************************************/
    /**********     Called from Button Listeners     ***********/
    /***********************************************************/

    private void onSave(){

        //The point must have been changed for this button to work
        if (mPointChanged) {
            //saveChanges();

            //What happens here depends upon the path
            if (mPointPath.equals(Prism4DPath.sCreateTag)){
                addNewPoint();

            } else if (mPointPath.equals(Prism4DPath.sEditTag)){

                updatePointFromUI(mPointBeingMaintained);

                //update the stored point
                Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
                pointManager.update(mPointBeingMaintained.getForProjectID(), mPointBeingMaintained);
            }
            //all other paths do nothing

            setPointSavedFlags();
        }

    }

    private boolean addNewPoint(){
        boolean returnCode = true;

        int pointID, projectID;
        //assign a new point ID
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        projectID = mPointBeingMaintained.getForProjectID();
        Prism4DProject project = projectManager.getProject(projectID);
        pointID = project.getNextPointID();
        mPointBeingMaintained.setPointID(pointID);


        updatePointFromUI(mPointBeingMaintained);
        //and update the screen with the new ID
        initializeUI();

        //now add the point to memory and db
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        pointManager.addToProject(project, mPointBeingMaintained, true);

        //from now on we are editing the point, not creating it
        mPointPath = Prism4DPath.sEditTag;

        //So we can go look at other points
        mPointViewExistingButton.setEnabled(true);
        mPointViewExistingButton.setTextColor(Color.BLACK);

        return returnCode;
    }


    private void updatePointFromUI(Prism4DPoint point){
        int pointID = point.getPointID();
        int projectID = point.getForProjectID();

        //Create a coordinate of the proper type with the attributes from the screen
        CharSequence coordType = getFullCoordTypeFromProject();
        if (coordType.equals(Prism4DCoordinate.sCoordinateTypeWGS84)){

            Prism4DCoordinateWGS84 newCoordinate = new Prism4DCoordinateWGS84();
            updateLLCoordinateFromUI(point, newCoordinate);
        } else if (coordType.equals(Prism4DCoordinate.sCoordinateTypeNAD83)){
            Prism4DCoordinateNAD83 newCoordinate = new Prism4DCoordinateNAD83();
            updateLLCoordinateFromUI(point, newCoordinate);

        } else if (coordType.equals(Prism4DCoordinate.sCoordinateTypeUTM)) {
            Prism4DCoordinateUTM newCoordinate = new Prism4DCoordinateUTM();
            updateENCoordinateFromUI(point, newCoordinate);

        } else { //Prism4DCoordinate.sCoordinateTypeClassSPCS
            Prism4DCoordinateSPCS newCoordinate = new Prism4DCoordinateSPCS();
            updateENCoordinateFromUI(point, newCoordinate);
        }

        point.setPointFeatureCode(mPointFeatureCodeInput.getText().toString().trim());
        point.setPointNotes(mPointNotesInput.getText().toString().trim());

    }


    private void updateLLCoordinateFromUI(Prism4DPoint point, Prism4DCoordinateLL coordinate){
        int pointID = point.getPointID();
        int projectID = point.getForProjectID();

        String latString, longString, eleString, geoidString;
        latString   = mPointLatitudeDDInput.getText().toString().trim();
        longString  = mPointLongitudeDDInput.getText().toString().trim();
        eleString   = mPointElevationMetersInput.getText().toString().trim();
        geoidString = mPointGeoidMetersInput.getText().toString().trim();
        //The values on the screen must be valid to make a coordinate
        if ((projectID !=0)     && (pointID != 0)       &&
            (latString != null) && (longString != null) &&
            (eleString != null) && (geoidString != null)) {

            coordinate.setProjectID(projectID);
            coordinate.setPointID(pointID);
            coordinate.setTime(new Date().getTime());

            coordinate.setLatitude(Double.parseDouble(latString));
            coordinate.setLatitudeDegree(Integer.parseInt(mPointLatitudeDInput.getText().toString().trim()));
            coordinate.setLatitudeMinute(Integer.parseInt(mPointLatitudeMInput.getText().toString().trim()));
            coordinate.setLatitudeSecond(Double.parseDouble(mPointLatitudeSInput.getText().toString().trim()));

            coordinate.setLongitude(Double.parseDouble(longString));
            coordinate.setLongitudeDegree(Integer.parseInt(mPointLongitudeDInput.getText().toString().trim()));
            coordinate.setLongitudeMinute(Integer.parseInt(mPointLongitudeMInput.getText().toString().trim()));
            coordinate.setLongitudeSecond(Double.parseDouble(mPointLongitudeSInput.getText().toString().trim()));

            coordinate.setElevation(Double.parseDouble(eleString));
            coordinate.setGeoid(Double.parseDouble(geoidString));

            mPointBeingMaintained.setHasACoordinateID(coordinate.getCoordinateID());
            mPointBeingMaintained.setCoordinate(coordinate);
        }else {
            //Coordinate not valid
            mPointBeingMaintained.setHasACoordinateID(0);
            mPointBeingMaintained.setCoordinate(null);
        }
    }

    private void updateENCoordinateFromUI(Prism4DPoint point, Prism4DCoordinateEN coordinate){
        int pointID = point.getPointID();
        int projectID = point.getForProjectID();

        String eastString, northString, eleString;

        eastString  = mPointEastingMetersInput.getText().toString().trim();
        northString = mPointNorthingMetersInput.getText().toString().trim();
        eleString = mPointENElevationMetersInput.getText().toString().trim();

        coordinate.setProjectID(projectID);
        coordinate.setPointID(pointID);

        if ((projectID !=0)      && (pointID != 0)        &&
            (eastString != null) && (northString != null) &&
            (eleString != null) ) {

            coordinate.setEasting(Double.parseDouble(eastString));
            coordinate.setNorthing(Double.parseDouble(northString));
            coordinate.setElevation(Double.parseDouble(eleString));

            String zoneString = mPointZoneInput.getText().toString().trim();
            if (zoneString.equals("")) zoneString = "0";
            coordinate.setZone(Integer.parseInt(zoneString));

            String latBandString = mPointLatbandInput.getText().toString().trim();
            if (latBandString.equals(""))latBandString = "A";
            coordinate.setLatBand((latBandString.charAt(0)));

            String hemisphere = mPointHemisphereInput.getText().toString().trim();
            if (hemisphere.equals(""))hemisphere = "N";
            coordinate.setHemisphere((hemisphere).charAt(0));

            coordinate.setDatum(mPointDatumInput.getText().toString().trim());

            String convergence = mPointConvergenceInput.getText().toString().trim();
            if (convergence.equals(""))convergence="0.0";
            coordinate.setConvergence(Double.parseDouble(convergence));

            String scale = mPointScaleFactorInput.getText().toString().trim();
            if (scale.equals(""))scale = "0.0";
            coordinate.setScale(Double.parseDouble(scale));

            coordinate.setValidCoordinate(true);

            mPointBeingMaintained.setHasACoordinateID(coordinate.getCoordinateID());
            mPointBeingMaintained.setCoordinate(coordinate);
        }else {
            //Coordinate not valid
            mPointBeingMaintained.setHasACoordinateID(0);
            mPointBeingMaintained.setCoordinate(null);
        }
    }


    private void onListPoints(){
        //what this button does depends upon the path  {create, open, copy, edit, show}
        /***************** CREATE ************************************************/
        if (mPointPath.equals(Prism4DPath.sCreateTag)){
            Toast.makeText(getActivity(),
                    R.string.cant_view_points,
                    Toast.LENGTH_SHORT).show();
            maybeAskFirstListPoints();

        /*************************** OPEN / COPY / EDIT / SHOW **************************************/
        } else if ((mPointPath.equals(Prism4DPath.sOpenTag)) ||
                   (mPointPath.equals(Prism4DPath.sCopyTag)) ||
                   (mPointPath.equals(Prism4DPath.sEditTag)) ||
                   (mPointPath.equals(Prism4DPath.sShowTag))) {

            maybeAskFirstListPoints();


        /************************************** UNKNOWN *************************/
        } else {
            Toast.makeText(getActivity(),
                    R.string.unrecognized_path_encountered,
                    Toast.LENGTH_SHORT).show();

            //Don't really know what to do here, but switch path to show and continue
            mPointPath = Prism4DPath.sShowTag;
            maybeAskFirstListPoints();

        }
    }

    private void maybeAskFirstListPoints(){
        if (mPointChanged){
            //ask the user if should continue
            areYouSureListPoints();

        } else {
            Toast.makeText(getActivity(),
                    R.string.point_unchanged,
                    Toast.LENGTH_SHORT).show();


                switchToListPoints();
        }
    }

    //Build and display the alert dialog
    private void areYouSureListPoints(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.abandon_title)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.continue_abandon_changes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Leave even though point has chaged
                                Toast.makeText(getActivity(),
                                        R.string.continue_abandon_changes,
                                        Toast.LENGTH_SHORT).show();

                                switchToListPoints();
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        Toast.makeText(getActivity(),
                                "Pressed Cancel",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(R.drawable.ground_station_icon)
                .show();
    }

    private void switchToListPoints(){
        MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
        myActivity.switchToListPointsScreen(mPointBeingMaintained.getForProjectID(),
                new Prism4DPath(mPointPath));
    }


    /***********************************************************/
    /*****     Maintain State Flags in this Fragment     *******/
    /***********************************************************/

    private void setPointChangedFlags(){
        mPointChanged = true;
        //enable the enter button as the default is NOT enabled/grayed out

        //enable the save changes button too
        //But only if on the Create or Edit path
        if ((mPointPath.equals(Prism4DPath.sCreateTag))||
            (mPointPath.equals(Prism4DPath.sEditTag))) {
            mPointSaveChangesButton.setEnabled(true);
            mPointSaveChangesButton.setTextColor(Color.BLACK);
        }
    }

    private void setPointSavedFlags(){
        mPointChanged = false;

        //enable the save changes button too
        mPointSaveChangesButton.setEnabled(false);
        mPointSaveChangesButton.setTextColor(Color.GRAY);
    }


}


