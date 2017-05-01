package com.asc.msigeosystems.prism4d;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.asc.msigeosystems.prism4dmockup.R;

import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIAngleDisplayDD;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIAngleDisplayDM;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIAngleDisplayDMS;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIAngleDisplayGONS;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIAngleDisplayMils;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIDecimalDisplayCommas;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIDecimalDisplayNoCommas;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIDistanceIntFeet;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIDistanceMeters;
import static com.asc.msigeosystems.prism4d.Prism4DProjectSettings.sUIDistanceUSFeet;

/**
 * The Project Settings Fragment is the UI
 * when the user customizes settings for this project
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DProjectSettingsFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Input / Output Fields on screen
    private TextView mProjectIDOutput;
    private TextView mProjectNameOutput;

    private EditText mDistanceUnitsInput;
    private EditText mDecimalDisplayInput;
    private EditText mAngleUnitsInput;
    private EditText mAngleDisplayInput;
    private EditText mGridDirectionInput;
    private EditText mScaleFactorInput;
    private EditText mSeaLevelInput;
    private EditText mRefractionInput;
    private EditText mDatumInput;
    private EditText mProjectionInput;
    private EditText mZoneInput;
    private EditText mCoordinateDisplayInput;
    private EditText mGeoidModelInput;
    private EditText mStartingPointIDInput;
    private EditText mAlphanumericInput;
    private EditText mFeatureCodesInput;
    private EditText mFCControlFileInput;
    private EditText mFCTimeStampInput;

    private Button mResetButton;
    private Button mExitButton;

    //Arrays for enumerated types
    private CharSequence[] mDistanceTypes =
                            new CharSequence[]{sUIDistanceMeters, sUIDistanceUSFeet, sUIDistanceIntFeet};
    private CharSequence[] mDecimalDisplayTypes =
                            new CharSequence[] {sUIDecimalDisplayCommas, sUIDecimalDisplayNoCommas};
    /*
    private CharSequence[] mAngleUnitTypes =
                            new CharSequence[] {sUIAngleUnitsDeg, sUIAngleUnitsRad};
                            */
    private CharSequence[] mAngleDisplayTypes =
                            new CharSequence[]{sUIAngleDisplayDD, sUIAngleDisplayDM,
                                               sUIAngleDisplayDMS, sUIAngleDisplayGONS,
                                               sUIAngleDisplayMils};

    ;
/*
    //these same values are set in setDefaults()
    private int mDistanceUnits     = sDBDistanceMeters;
    private int mDecimalDisplay    = sDBDecimalDisplayCommas;
    private int mAngleUnits        = sDBAngleUnitsDeg;
    private int mAngleDisplay      = sDBAngleDisplayDD;
    private CharSequence mGridDirection     = "North Azimuth";
    private double       mScaleFactor       = .99982410;
    private CharSequence mSeaLevel          = "Off";
    private CharSequence mRefraction        = "Off";
    private CharSequence mDatum             = "NAD 1983 (2011)";
    private CharSequence mProjection        = "US State Plane Coordinates";
    private CharSequence mZone              = "Georgia West (1002)";
    private CharSequence mCoordinateDisplay = "North, East";
    private CharSequence mGeoidModel        = "GEOID99";
    private CharSequence mStartingPointID   = "1001";
    private CharSequence mAlphanumeric      = "Off";
    private CharSequence mFeatureCodes      = "RHM2";
    private CharSequence mFCControlFile     = "CP2";
    private CharSequence mFCTimeStamp       = "NO";
*/
    private Prism4DProjectSettings mPSBeingMaintained;

    private Spinner      mSpinnerDistUnits;
    private Spinner      mSpinnerDecDisplay;
    //private Spinner      mSpinnerAngleUnits;
    private Spinner      mSpinnerAngleDisplay;


    private Prism4DProject mProject;
    private CharSequence   mProjectPath;





    public static MainPrism4DProjectSettingsFragment newInstance(
            Prism4DProject project,
            Prism4DPath projectPath) {

        Bundle args = Prism4DProject.putProjectInArguments(new Bundle(), project);
        args = Prism4DPath.putPathInArguments(args, projectPath);


        MainPrism4DProjectSettingsFragment fragment = new MainPrism4DProjectSettingsFragment();

        fragment.setArguments(args);
        return fragment;
    }

    public MainPrism4DProjectSettingsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        mProject = Prism4DProject.getProjectFromArguments(getArguments());
        Prism4DPath path = Prism4DPath.getPathFromArguments(getArguments());
        mProjectPath = path.getPath();

        if (mPSBeingMaintained == null){
            mPSBeingMaintained = new Prism4DProjectSettings(); //filled with default values
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_project_settings_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later
        wireWidgets(v);

        wireSpinners(v);


        //Initialize the fields with the defaults
        //set and display default values
        setDefaults();

        return v;

    }//end CreateView

    @Override
    public void onResume(){
        super.onResume();
        setSubtitle();
    }


    private void setSubtitle(){
        ((MainPrism4DActivity)getActivity())
                .switchSubtitle(R.string.subtitle_project_settings);
    }


    private void wireWidgets(View v){

        //Project ID
        mProjectIDOutput = (TextView) v.findViewById(R.id.projectIDOutput);

        //Project Name
        mProjectNameOutput = (TextView) v.findViewById(R.id.projectNameOutput);


        //AngleUnits
        //mAngleUnitsInput = (EditText) v.findViewById(R.id.angleUnitsInput);

        //AngleDisplay
        mAngleDisplayInput = (EditText) v.findViewById(R.id.angleDisplayInput);

        //GridDirection
        mGridDirectionInput = (EditText) v.findViewById(R.id.gridDirectionInput);

        //ScaleFactor
        mScaleFactorInput = (EditText) v.findViewById(R.id.scaleFactorInput);

        //SeaLevel
        mSeaLevelInput = (EditText) v.findViewById(R.id.seaLevelInput);

        //Refraction
        mRefractionInput = (EditText) v.findViewById(R.id.refractionInput);

        //Datum
        mDatumInput = (EditText) v.findViewById(R.id.datumInput);

        //Projection
        mProjectionInput = (EditText) v.findViewById(R.id.projectionInput);

        //Zone
        mZoneInput = (EditText) v.findViewById(R.id.zoneInput);

        //CoordinateDisplay
        mCoordinateDisplayInput = (EditText) v.findViewById(R.id.coordinateDisplayInput);

        //GeoidModel
        mGeoidModelInput = (EditText) v.findViewById(R.id.geoidModelInput);

        //StartingPointID
        mStartingPointIDInput = (EditText) v.findViewById(R.id.startingPointIDInput);

        //Alphanumeric
        mAlphanumericInput = (EditText) v.findViewById(R.id.alphanumericInput);

        //FeatureCodes
        mFeatureCodesInput = (EditText) v.findViewById(R.id.featureCodesInput);

        //FCControlFile
        mFCControlFileInput = (EditText) v.findViewById(R.id.fcControlFileInput);

        //FCTimeStamp
        mFCTimeStampInput = (EditText) v.findViewById(R.id.fcTimeStampInput);


        //Exit Button
        mExitButton = (Button) v.findViewById(R.id.exitButton);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ((MainPrism4DActivity)getActivity()).switchToPopBackstack();
            }
        });


        //Reset Defaults Button
        mResetButton = (Button) v.findViewById(R.id.resetDefaultsButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //set the variables to their default values and display them
                setDefaults();
            }
        });


        //FOOTER WIDGETS


    }



    private void wireSpinners(View v){

        //defaults are already set in mPSBeingMaintained;

        //initialize the spinner
        mSpinnerDistUnits = (Spinner) v.findViewById(R.id.distance_units_spinner);

        // Create an ArrayAdapter using the Activities context AND
        // the string array and a default spinner layout
        ArrayAdapter<CharSequence> distUnitsAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                                                            android.R.layout.simple_spinner_item,
                                                            mDistanceTypes);

        // Specify the layout to use when the list of choices appears
        distUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinnerDistUnits.setAdapter(distUnitsAdapter);

        //Set the value of the spinner to the value in the object being maintained
        mSpinnerDistUnits.setSelection(mPSBeingMaintained.getDistanceUnits());

        //attach the listener to the spinner
        mSpinnerDistUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mPSBeingMaintained.setDistanceUnits(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        mSpinnerDecDisplay = (Spinner) v.findViewById(R.id.decimal_display_spinner);
        ArrayAdapter<CharSequence> decDispAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                                                            android.R.layout.simple_spinner_item,
                                                            mDecimalDisplayTypes);
        decDispAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDecDisplay.setAdapter(decDispAdapter);
        mSpinnerDecDisplay.setSelection(mPSBeingMaintained.getDecimalDisplay());
        mSpinnerDecDisplay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mPSBeingMaintained.setDecimalDisplay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        mSpinnerAngleDisplay = (Spinner) v.findViewById(R.id.angle_units_spinner);
        ArrayAdapter<CharSequence> angDispAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                                                            android.R.layout.simple_spinner_item,
                                                            mAngleDisplayTypes);
        angDispAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAngleDisplay.setAdapter(angDispAdapter);
        mSpinnerAngleDisplay.setSelection(mPSBeingMaintained.getAngleDisplay());
        mSpinnerAngleDisplay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mPSBeingMaintained.setAngleDisplay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }




    private void setDefaults() {

        mPSBeingMaintained.setDefaults();
        //display the new defaults
        initializeUI();
    }

    private void initializeUI() {

        mProjectIDOutput.     setText(String.valueOf(mProject.getProjectID()));
        mProjectNameOutput.   setText(String.valueOf(mProject.getProjectName()));

        mSpinnerDistUnits.setSelection(mPSBeingMaintained.getDistanceUnits());
        mSpinnerDecDisplay.setSelection(mPSBeingMaintained.getDecimalDisplay());
        mSpinnerAngleDisplay.setSelection(mPSBeingMaintained.getAngleDisplay());


  /*
        mAngleDisplayInput.   setText(mPSBeingMaintained.getAngleDisplay());
        mGridDirectionInput.  setText(mPSBeingMaintained.getGridDirection());
        mScaleFactorInput.    setText(String.valueOf(mPSBeingMaintained.getScaleFactor()));

        mSeaLevelInput.       setText(mPSBeingMaintained.getSeaLevel());
        mRefractionInput.     setText(mPSBeingMaintained.getRefraction());
        mDatumInput.          setText(mPSBeingMaintained.getDatum());
        mProjectionInput.     setText(mPSBeingMaintained.getProjection());
        mZoneInput.           setText(mPSBeingMaintained.getZone());
        mCoordinateDisplayInput.setText(mPSBeingMaintained.getCoordinateDisplay());
        mGeoidModelInput.     setText(mPSBeingMaintained.getGeoidModel());
        mStartingPointIDInput.setText(mPSBeingMaintained.getStartingPointID());
        mAlphanumericInput.   setText(mPSBeingMaintained.isAlphanumericID());
        mFeatureCodesInput.   setText(mPSBeingMaintained.getFeatureCodes());
        mFCControlFileInput.  setText(mPSBeingMaintained.getFCControlFile());
        mFCTimeStampInput.    setText(mPSBeingMaintained.getFCTimeStamp());
*/

    }
}


