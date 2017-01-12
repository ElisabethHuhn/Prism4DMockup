package com.asc.msigeosystems.prism4d;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Maintain Point Fragment
 * is passed a point on startup. The point attribute fields are
 * pre-populated prior to updating the point
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DPointEditFragment extends Fragment  {

    private static final String TAG = "EDIT_POINT_FRAGMENT";

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
    private EditText mPointHdopInput;
    private EditText mPointVdopInput;
    private EditText mPointTdopInput;
    private EditText mPointPdopInput;
    private EditText mPointHrmsInput;
    private EditText mPointVrmsInput;
    private EditText mPointOffsetDistInput;
    private EditText mPointOffsetHeadInput;
    private EditText mPointOffsetEleInput;
    private EditText mPointHeightInput;


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
    /******         Recycler View Widgets             **********/
    /***********************************************************/
    private List<Prism4DPicture> mPictureList = new ArrayList<>();
    private RecyclerView               mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Prism4DPictureAdapter      mAdapter;

    private ImageView mPictureImage;



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


        //Need to add widgets for the coordinates, but there are different widgets
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

        initializeRecyclerView(v);

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


        //Point Quality HDOP
        mPointHdopInput = (EditText) v.findViewById(R.id.pointHdopInput);
        mPointHdopInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Quality VDOP
        mPointVdopInput = (EditText) v.findViewById(R.id.pointVdopInput);
        mPointVdopInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Quality TDOP
        mPointTdopInput = (EditText) v.findViewById(R.id.pointTdopInput);
        mPointTdopInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Quality PDOP
        mPointPdopInput = (EditText) v.findViewById(R.id.pointPdopInput);
        mPointPdopInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Quality HRMS
        mPointHrmsInput = (EditText) v.findViewById(R.id.pointHrmsInput);
        mPointHrmsInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Quality VRMS
        mPointVrmsInput = (EditText) v.findViewById(R.id.pointVrmsInput);
        mPointVrmsInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });



        //Point Offset Distance
        mPointOffsetDistInput = (EditText) v.findViewById(R.id.pointOffDistInput);
        mPointOffsetDistInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Offset Heading
        mPointOffsetHeadInput = (EditText) v.findViewById(R.id.pointOffHeadInput);
        mPointOffsetHeadInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Offset Elevation
        mPointOffsetEleInput = (EditText) v.findViewById(R.id.pointOffEleInput);
        mPointOffsetEleInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }
        });


        //Point Height
        mPointHeightInput = (EditText) v.findViewById(R.id.pointHeightInput);
        mPointHeightInput.setOnTouchListener(new View.OnTouchListener() {
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
                //hide the keyboard if it is visable
                Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();
                constantsAndUtilities.hideKeyboard(getActivity());

                if (mPointPath.equals(Prism4DPath.sEditFromMaps)){
                    //pop back to the Collect Points Screen
                    ((MainPrism4DActivity)getActivity()).
                                                popToScreen(MainPrism4DActivity.sCollectPointsTag);
                } else {
                    ((MainPrism4DActivity) getActivity()).popToTopCogoScreen();
                }
            }
        });



        //View Existing Points Button
        mPointViewExistingButton = (Button) v.findViewById(R.id.pointViewExistingButton);
        if ((mPointPath.equals(Prism4DPath.sCreateTag)) ||
            (mPointPath.equals(Prism4DPath.sEditFromMaps)))    {
            //disable the button on the create path OR the edit from maps path
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
                //ignore the return code. If successful, the save path is consumed, otherwise not
                onSave();
            }
        });
        mPointSaveChangesButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    int temp = 0;//something for the debugger to land on
                }
            }
        });

        mPictureImage = (ImageView) v.findViewById(R.id.pictureImage);


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


        //Can't change the coordinate if coming from maps
        if (mPointPath.equals(Prism4DPath.sEditFromMaps)){
            mPointLatitudeDDInput.setFocusable(false);
            mPointLatitudeDDInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointLatitudeDInput.setFocusable(false);
            mPointLatitudeDInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointLatitudeMInput.setFocusable(false);
            mPointLatitudeMInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointLatitudeSInput.setFocusable(false);
            mPointLatitudeSInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
        }


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
                        Prism4DCoordinateLL.convertDDtoDMS( getActivity(),
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
                    String tempD = mPointLatitudeDInput.getText().toString();
                    String tempM = mPointLatitudeMInput.getText().toString();
                    String tempS = mPointLatitudeSInput.getText().toString();
                    if (!tempD.equals(mPointLatitudeDOld)){
                        setPointChangedFlags();
                        //only convert if all three fields have values
                        if ((!tempD.isEmpty()) && (!tempM.isEmpty()) && (!tempS.isEmpty())){
                            // so convert Latitude DMS to DD
                            boolean isLatitude = true;
                            Prism4DCoordinateLL.convertDMStoDD( getActivity(),
                                    mPointLatitudeDDInput,
                                    mPointLatitudeDInput,
                                    mPointLatitudeMInput,
                                    mPointLatitudeSInput,
                                    isLatitude);
                            mPointLatitudeDDOld = mPointLatitudeDDInput.getText().toString();
                            mPointLatitudeDOld = tempD;
                        }

                    }

                }
            }
        });
        mPointLatitudeMInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Minute just lost focus,
                    //has the value changed?
                    String tempD = mPointLatitudeDInput.getText().toString();
                    String tempM = mPointLatitudeMInput.getText().toString();
                    String tempS = mPointLatitudeSInput.getText().toString();
                    if (!tempM.equals(mPointLatitudeMOld)){
                        setPointChangedFlags();
                        //only convert if all three fields have values
                        if ((!tempD.isEmpty()) && (!tempM.isEmpty()) && (!tempS.isEmpty())) {
                            // so convert Latitude DMS to DD
                            boolean isLatitude = true;
                            Prism4DCoordinateLL.convertDMStoDD(getActivity(),
                                    mPointLatitudeDDInput,
                                    mPointLatitudeDInput,
                                    mPointLatitudeMInput,
                                    mPointLatitudeSInput,
                                    isLatitude);
                            mPointLatitudeDDOld = mPointLatitudeDDInput.getText().toString();
                            mPointLatitudeMOld = tempM;
                        }
                    }

                }
            }
        });
         mPointLatitudeSInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Seconds just lost focus,
                    //has the value changed?
                    String tempD = mPointLatitudeDInput.getText().toString();
                    String tempM = mPointLatitudeMInput.getText().toString();
                    String tempS = mPointLatitudeSInput.getText().toString();
                    if (!tempS.equals(mPointLatitudeSOld)){
                        setPointChangedFlags();
                        //only convert if all three fields have values
                        if ((!tempD.isEmpty()) && (!tempM.isEmpty()) && (!tempS.isEmpty())) {
                            // so convert Latitude DMS to DD
                            boolean isLatitude = true;
                            Prism4DCoordinateLL.convertDMStoDD(getActivity(),
                                    mPointLatitudeDDInput,
                                    mPointLatitudeDInput,
                                    mPointLatitudeMInput,
                                    mPointLatitudeSInput,
                                    isLatitude);
                            mPointLatitudeDDOld = mPointLatitudeDDInput.getText().toString();
                            mPointLatitudeSOld = tempS;
                        }
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


        if (mPointPath.equals(Prism4DPath.sEditFromMaps)){
            mPointLongitudeDDInput.setFocusable(false);
            mPointLongitudeDDInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointLongitudeDInput.setFocusable(false);
            mPointLongitudeDInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointLongitudeMInput.setFocusable(false);
            mPointLongitudeMInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointLongitudeSInput.setFocusable(false);
            mPointLongitudeSInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
        }


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
                        Prism4DCoordinateLL.convertDDtoDMS( getActivity(),
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
                    String tempD = mPointLongitudeDInput.getText().toString();
                    String tempM = mPointLongitudeMInput.getText().toString();
                    String tempS = mPointLongitudeSInput.getText().toString();
                    if (!tempD.equals(mPointLongitudeDOld)){
                        setPointChangedFlags();
                        //only convert if all three fields have values
                        if ((!tempD.isEmpty()) && (!tempM.isEmpty()) && (!tempS.isEmpty())) {
                            // so convert Longitude DMS to DD
                            boolean isLatitude = false;
                            Prism4DCoordinateLL.convertDMStoDD(getActivity(),
                                    mPointLongitudeDDInput,
                                    mPointLongitudeDInput,
                                    mPointLongitudeMInput,
                                    mPointLongitudeSInput,
                                    isLatitude);
                            mPointLongitudeDOld = tempD;
                            mPointLongitudeDDOld = mPointLongitudeDDInput.getText().toString();
                        }
                    }

                }
            }
        });


         mPointLongitudeMInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Minute just lost focus,
                    //has the value changed?
                    String tempD = mPointLongitudeDInput.getText().toString();
                    String tempM = mPointLongitudeMInput.getText().toString();
                    String tempS = mPointLongitudeSInput.getText().toString();
                    if (!tempM.equals(mPointLongitudeMOld)){
                        setPointChangedFlags();
                        //only convert if all three fields have values
                        if ((!tempD.isEmpty()) && (!tempM.isEmpty()) && (!tempS.isEmpty())) {
                            // so convert Longitude DMS to DD
                            boolean isLatitude = false;
                            Prism4DCoordinateLL.convertDMStoDD(getActivity(),
                                    mPointLongitudeDDInput,
                                    mPointLongitudeDInput,
                                    mPointLongitudeMInput,
                                    mPointLongitudeSInput,
                                    isLatitude);
                            mPointLongitudeMOld = tempM;
                            mPointLongitudeDDOld = mPointLongitudeDDInput.getText().toString();
                        }
                    }

                }
            }
        });


        mPointLongitudeSInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    //Second just lost focus,
                    //has the value changed?
                    String tempD = mPointLongitudeDInput.getText().toString();
                    String tempM = mPointLongitudeMInput.getText().toString();
                    String tempS = mPointLongitudeSInput.getText().toString();
                    if (!tempS.equals(mPointLongitudeSOld)){
                        setPointChangedFlags();
                        //only convert if all three fields have values
                        if ((!tempD.isEmpty()) && (!tempM.isEmpty()) && (!tempS.isEmpty())) {
                            // so convert Longitude DMS to DD
                            boolean isLatitude = false;
                            Prism4DCoordinateLL.convertDMStoDD(getActivity(),
                                    mPointLongitudeDDInput,
                                    mPointLongitudeDInput,
                                    mPointLongitudeMInput,
                                    mPointLongitudeSInput,
                                    isLatitude);
                            mPointLongitudeSOld = tempS;
                            mPointLongitudeDDOld = mPointLongitudeDDInput.getText().toString();
                        }
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


        if (mPointPath.equals(Prism4DPath.sEditFromMaps)) {

            mPointElevationMetersInput.setFocusable(false);
            mPointElevationMetersInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointElevationFeetInput.setFocusable(false);
            mPointElevationFeetInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));

        }

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

        if (mPointPath.equals(Prism4DPath.sEditFromMaps)) {

            mPointGeoidMetersInput.setFocusable(false);
            mPointGeoidMetersInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointGeoidFeetInput.setFocusable(false);
            mPointGeoidFeetInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));

        }

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

        if (mPointPath.equals(Prism4DPath.sEditFromMaps)) {

            mPointEastingMetersInput.setFocusable(false);
            mPointEastingMetersInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointEastingFeetInput.setFocusable(false);
            mPointEastingFeetInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));

        }



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

        if (mPointPath.equals(Prism4DPath.sEditFromMaps)) {

            mPointNorthingMetersInput.setFocusable(false);
            mPointNorthingMetersInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointNorthingFeetInput.setFocusable(false);
            mPointNorthingFeetInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));

        }



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

        if (mPointPath.equals(Prism4DPath.sEditFromMaps)) {

            mPointENElevationMetersInput.setFocusable(false);
            mPointENElevationMetersInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointENElevationFeetInput.setFocusable(false);
            mPointENElevationFeetInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));

        }



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

        if (mPointPath.equals(Prism4DPath.sEditFromMaps)) {

            mPointZoneInput.setFocusable(false);
            mPointZoneInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointHemisphereInput.setFocusable(false);
            mPointHemisphereInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointLatbandInput.setFocusable(false);
            mPointLatbandInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointDatumInput.setFocusable(false);
            mPointDatumInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointConvergenceInput.setFocusable(false);
            mPointConvergenceInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));
            mPointScaleFactorInput.setFocusable(false);
            mPointScaleFactorInput.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.colorGray));

        }

    }


    private void initializeRecyclerView(View v){

       /*
         * The steps for doing recycler view in onCreateView() of a fragment are:
         * 1) inflate the .xml
         *
         * the special recycler view stuff is:
         * 2) get and store a reference to the recycler view widget that you created in xml
         * 3) create and assign a layout manager to the recycler view
         * 4) assure that there is data for the recycler view to show.
         * 5) use the data to create and set an adapter in the recycler view
         * 6) create and set an item animator (if desired)
         * 7) create and set a line item decorator
         * 8) add event listeners to the recycler view
         *
         * 9) return the view
         */
        v.setTag(TAG);

        //2) find and remember the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.pictureList);


        // The RecyclerView.LayoutManager defines how elements are laid out.
        //3) create and assign a layout manager to the recycler view
        mLayoutManager  = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //4) create some dummy data and tell the adapter about it
        //  this is done in the singleton container

        //      get our singleton list container
        Prism4DPointManager pointsManager = Prism4DPointManager.getInstance();
        //Get this projects points
        mPictureList = mPointBeingMaintained.getPictures();


        //5) Use the data to Create and set out points Adapter
        mAdapter = new Prism4DPictureAdapter(mPictureList);
        mRecyclerView.setAdapter(mAdapter);

        //6) create and set the itemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //7) create and add the item decorator
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), LinearLayoutManager.VERTICAL));


        //8) add event listeners to the recycler view
        mRecyclerView.addOnItemTouchListener(
                new MainPrism4DPointListFragment.RecyclerTouchListener(getActivity(),
                        mRecyclerView,
                        new MainPrism4DPointListFragment.ClickListener() {

                            @Override
                            public void onClick(View view, int position) {
                                onSelectPicture(view, position);
                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                //for now, ignore the long click
                            }
                        }));
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

        //Project ID
        int projectID = mPointBeingMaintained.getForProjectID();
        mPointProjectIDInput.setText(String.valueOf(projectID));
        mPointIDInput.setText       (String.valueOf (mPointBeingMaintained.getPointID()));

        //Project name
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);
        mPointProjectNameInput.setText(project.getProjectName());

        //point ID
        if (mPointBeingMaintained.getPointID() != 0){
            mPointFeatureCodeInput.setText(mPointBeingMaintained.getPointFeatureCode());
            mPointNotesInput.setText(mPointBeingMaintained.getPointNotes());
        }


        //Coordinate Type
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

        //Offsets
        mPointOffsetDistInput.setText(String.valueOf(mPointBeingMaintained.getOffsetDistance()));
        mPointOffsetHeadInput.setText(String.valueOf(mPointBeingMaintained.getOffsetHeading()));
        mPointOffsetEleInput .setText(String.valueOf(mPointBeingMaintained.getOffsetElevation()));

        //Height
        mPointHeightInput.setText(String.valueOf(mPointBeingMaintained.getHeight()));
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
    /********  Called from RecyclerView Listeners    ***********/
    /***********************************************************/


    private void onSelectPicture(View view, int position){
        //Toast.makeText(getActivity(), "Picture Selected", Toast.LENGTH_SHORT).show();
        Prism4DPicture picture = mAdapter.getPicture(position);
        String pathToPicture = picture.getPathName();
        Bitmap bitmap = BitmapFactory.decodeFile(pathToPicture);

        if (bitmap != null) {
            mPictureImage.setImageBitmap(bitmap);

            //for some reason, showing the image blanks out the recycler view, so force a redraw
            mRecyclerView.getRecycledViewPool().clear();
            //mAdapter.notifyDataSetChanged();
            mRecyclerView.invalidate();
        } else {
            String msg = getString(R.string.missing_picture_file)+ " " + pathToPicture;
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }

    }


    /***********************************************************/
    /**********     Called from Button Listeners     ***********/
    /***********************************************************/

    private boolean onSave(){
        boolean returnCode = true;

        //The point must have been changed for this button to work
        if (mPointChanged) {
            //saveChanges();

            //What happens here depends upon the path
            if (mPointPath.equals(Prism4DPath.sCreateTag)){
                returnCode = addNewPoint();

            } else if (mPointPath.equals(Prism4DPath.sEditTag)){

                returnCode = updatePointFromUI(mPointBeingMaintained);

                if (returnCode) {
                    //update the stored point
                    Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
                    pointManager.updatePoint(mPointBeingMaintained);
                }
            }
            //all other paths do nothing

            if (returnCode) setPointSavedFlags();
        }
        return returnCode;

    }

    private boolean addNewPoint(){
        boolean returnCode = true;

        int pointID, projectID;
        //assign a new point ID
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        projectID = mPointBeingMaintained.getForProjectID();
        Prism4DProject project = projectManager.getProject(projectID);

        //Notice that this is the potential next id. The ID is not incremented yet
        pointID = project.getPotentialNextPointID();
        mPointBeingMaintained.setPointID(pointID);

        returnCode = updatePointFromUI(mPointBeingMaintained);

        if (!returnCode){
            //The coordinate is invalid, or something else is wrong
            //user is informed in updatePointFromUI
              mPointBeingMaintained.setPointID(0);//reset the point ID to not yet created
            //NOTE that we are still on the create path
            return false;
        }

        //and update the screen with the new ID
        initializeUI();

        //now add the point to memory and db
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        boolean addToDBToo = true;
        if (!pointManager.addToProject(project, mPointBeingMaintained, addToDBToo)){
            Toast.makeText(getActivity(),
                            getString(R.string.error_adding_point),
                            Toast.LENGTH_SHORT).show();
        }

        //from now on we are editing the point, not creating it
        mPointPath = Prism4DPath.sEditTag;

        //So we can go look at other points
        mPointViewExistingButton.setEnabled(true);
        mPointViewExistingButton.setTextColor(Color.BLACK);

        return returnCode;
    }


    //returns false if there are any errors in the point
    private boolean updatePointFromUI(Prism4DPoint point){

        boolean returnCode = true;

        //Create a coordinate of the proper type with the attributes from the screen
        CharSequence coordType = getFullCoordTypeFromProject();
        if (coordType.equals(Prism4DCoordinate.sCoordinateTypeWGS84)){

            Prism4DCoordinateWGS84 newCoordinate = new Prism4DCoordinateWGS84();
            returnCode = updateLLCoordinateFromUI(point, newCoordinate);
        } else if (coordType.equals(Prism4DCoordinate.sCoordinateTypeNAD83)){
            Prism4DCoordinateNAD83 newCoordinate = new Prism4DCoordinateNAD83();
           returnCode = updateLLCoordinateFromUI(point, newCoordinate);

        } else if (coordType.equals(Prism4DCoordinate.sCoordinateTypeUTM)) {
            Prism4DCoordinateUTM newCoordinate = new Prism4DCoordinateUTM();
            returnCode = updateENCoordinateFromUI(point, newCoordinate);

        } else { //Prism4DCoordinate.sCoordinateTypeClassSPCS
            Prism4DCoordinateSPCS newCoordinate = new Prism4DCoordinateSPCS();
            returnCode = updateENCoordinateFromUI(point, newCoordinate);
        }

        if (returnCode) {
            point.setPointFeatureCode(mPointFeatureCodeInput.getText().toString().trim());
            point.setPointNotes(mPointNotesInput.getText().toString().trim());

            //There are several fields that can not be updated from this screen
            // TODO: 1/12/2017 Height, quality, offset fields should perhaps be written to the point object
            return true;
        } else {
            //Coordinate not valid
            Toast.makeText(getActivity(),
                    getString(R.string.coordinate_not_valid),
                    Toast.LENGTH_SHORT).show();

            return returnCode;
        }

    }


    //returns false if the coordinate is not valid
    private boolean updateLLCoordinateFromUI(Prism4DPoint point, Prism4DCoordinateLL coordinate){
        int pointID = point.getPointID();
        int projectID = point.getForProjectID();


        String latString   = mPointLatitudeDDInput.getText().toString().trim();
        String longString  = mPointLongitudeDDInput.getText().toString().trim();
        String eleString   = mPointElevationMetersInput.getText().toString().trim();
        String geoidString = mPointGeoidMetersInput.getText().toString().trim();

        String dLatString  = mPointLatitudeDInput.getText().toString().trim();
        String mLatString  = mPointLatitudeMInput.getText().toString().trim();
        String sLatString  = mPointLatitudeSInput.getText().toString().trim();

        String dLngString  = mPointLongitudeDInput.getText().toString().trim();
        String mLngString  = mPointLongitudeMInput.getText().toString().trim();
        String sLngString  = mPointLongitudeSInput.getText().toString().trim();
 /*
        if (dString.isEmpty())dString = getString(R.string.zero_decimal_string);
        if (mString.isEmpty())mString = getString(R.string.zero_decimal_string);
        if (sString.isEmpty())sString = getString(R.string.zero_decimal_string);
        if (dString.isEmpty())dString = getString(R.string.zero_decimal_string);
        if (mString.isEmpty())mString = getString(R.string.zero_decimal_string);
        if (sString.isEmpty())sString = getString(R.string.zero_decimal_string);
*/

        //if (eleString.isEmpty())eleString = getString(R.string.zero_decimal_string);
        //if (geoidString.isEmpty())geoidString = getString(R.string.zero_decimal_string);

        //The values on the screen must be valid to make a coordinate
         if ((projectID !=0)         && (pointID != 0)           &&
            (!latString.isEmpty())  && (!longString.isEmpty())  &&
            (!eleString.isEmpty())  && (!geoidString.isEmpty()) &&
            (!dLatString.isEmpty()) && (!mLatString.isEmpty())  &&
            (!sLatString.isEmpty()) && (!dLngString.isEmpty())  &&
            (!mLngString.isEmpty()) && (!mLngString.isEmpty())  ) {

             coordinate.setProjectID(projectID);
             coordinate.setPointID(pointID);
             coordinate.setTime(new Date().getTime());


             //Latitude
            coordinate.setLatitude      (Double.parseDouble(latString));
            coordinate.setLatitudeDegree(Integer.parseInt(dLatString));
            coordinate.setLatitudeMinute(Integer.parseInt(mLatString));
            coordinate.setLatitudeSecond(Double.parseDouble(sLatString));

            //Longitude
            coordinate.setLongitude      (Double.parseDouble(longString));
            coordinate.setLongitudeDegree(Integer.parseInt(dLngString));
            coordinate.setLongitudeMinute(Integer.parseInt(mLngString));
            coordinate.setLongitudeSecond(Double.parseDouble(sLngString));

            coordinate.setElevation(Double.parseDouble(eleString));
            coordinate.setGeoid(Double.parseDouble(geoidString));

            mPointBeingMaintained.setHasACoordinateID(coordinate.getCoordinateID());
            mPointBeingMaintained.setCoordinate(coordinate);
            return true;
        }else {

            return false;
        }
    }

    private boolean updateENCoordinateFromUI(Prism4DPoint point, Prism4DCoordinateEN coordinate){
        int pointID = point.getPointID();
        int projectID = point.getForProjectID();

        String eastString        = mPointEastingMetersInput.getText().toString().trim();
        String northString       = mPointNorthingMetersInput.getText().toString().trim();
        String eleString         = mPointENElevationMetersInput.getText().toString().trim();
        String zoneString        = mPointZoneInput.getText().toString().trim();
        String latBandString     = mPointLatbandInput.getText().toString().trim();
        String hemisphereString  = mPointHemisphereInput.getText().toString().trim();
        String datumString       = mPointDatumInput.getText().toString().trim();
        String convergenceString = mPointConvergenceInput.getText().toString().trim();
        String scaleString       = mPointScaleFactorInput.getText().toString().trim();

 /*
            if (zoneString.equals("")) zoneString = getString(R.string.default_zone);
            if (latBandString.equals(""))latBandString = getString(R.string.default_latband);
            if (hemisphere.equals(""))hemisphere = getString(R.string.default_hemisphere);
            if (convergence.equals(""))convergence=getString(R.string.zero_decimal_string);
            if (scale.equals(""))scale = getString(R.string.zero_decimal_string);

  */

        if ((projectID !=0)                && (pointID != 0)                &&
            (!eastString.isEmpty())        && (!northString.isEmpty())      &&
            (!eleString.isEmpty())         && (!zoneString.isEmpty()        &&
            (!latBandString.isEmpty())     && (!hemisphereString.isEmpty()) &&
            (!convergenceString.isEmpty()) && (!scaleString.isEmpty())      )) {

            coordinate.setProjectID(projectID);
            coordinate.setPointID(pointID);

            coordinate.setEasting  (Double.parseDouble(eastString));
            coordinate.setNorthing (Double.parseDouble(northString));
            coordinate.setElevation(Double.parseDouble(eleString));

            coordinate.setZone(Integer.parseInt(zoneString));
            coordinate.setLatBand((latBandString.charAt(0)));
            coordinate.setHemisphere((hemisphereString).charAt(0));
            coordinate.setDatum(datumString);
            coordinate.setConvergence(Double.parseDouble(convergenceString));
            coordinate.setScale(Double.parseDouble(scaleString));
            coordinate.setValidCoordinate(true);

            mPointBeingMaintained.setHasACoordinateID(coordinate.getCoordinateID());
            mPointBeingMaintained.setCoordinate(coordinate);
            return true;
        }else {
            //Coordinate not valid
            return false;
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
                .setIcon(R.drawable.ground_station_icon)
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


