package com.asc.msigeosystems.prism4d;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asc.msigeosystems.prism4dmockup.R;

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



    //these same values are set in setDefaults()
    private CharSequence mDistanceUnits     = "US Survey Feet";
    private CharSequence mDecimalDisplay    = "123,456,789.00";
    private CharSequence mAngleUnits        = "Degrees";
    private CharSequence mAngleDisplay      = "123 45' 11";
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_project_settings_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later
        wireWidgets(v);

        //Set the titlebar subtitle
        ((MainPrism4DActivity) getActivity()).switchSubtitle(getString(R.string.subtitle_project_settings));

        //Initialize the fields with the defaults
        //set and display default values
        setDefaults();

        return v;

    }//end CreateView

    private void wireWidgets(View v){

        //Project ID
        mProjectIDOutput = (TextView) v.findViewById(R.id.projectIDOutput);

        //Project Name
        mProjectNameOutput = (TextView) v.findViewById(R.id.projectNameOutput);

        //Distance Units
        mDistanceUnitsInput = (EditText) v.findViewById(R.id.distanceUnitsInput);

        //DecimalDisplay
        mDecimalDisplayInput = (EditText) v.findViewById(R.id.decimalDisplayInput);

        //AngleUnits
        mAngleUnitsInput = (EditText) v.findViewById(R.id.angleUnitsInput);

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

    private void setDefaults() {

        //Restore default values
        mDistanceUnits     = "US Survey Feet";
        mDecimalDisplay    = "123,456,789.00";
        mAngleUnits        = "Degrees";
        mAngleDisplay      = "123 45' 11";
        mGridDirection     = "North Azimuth";
        mScaleFactor       = .99982410;
        mSeaLevel          = "Off";
        mRefraction        = "Off";
        mDatum             = "NAD 1983 (2011)";
        mProjection        = "US State Plane Coordinates";
        mZone              = "Georgia West (1002)";
        mCoordinateDisplay = "North, East";
        mGeoidModel        = "GEOID99";
        mStartingPointID   = "1001";
        mAlphanumeric      = "Off";
        mFeatureCodes      = "RHM2";
        mFCControlFile     = "CP2";
        mFCTimeStamp       = "NO";

        //display the new defaults
        mProjectIDOutput.     setText(String.valueOf(mProject.getProjectID()));
        mProjectNameOutput.   setText(String.valueOf(mProject.getProjectName()));
        mDistanceUnitsInput.  setText(mDistanceUnits);
        mDecimalDisplayInput. setText(mDecimalDisplay);
        mAngleUnitsInput.     setText(mAngleUnits);
        mAngleDisplayInput.   setText(mAngleDisplay);
        mGridDirectionInput.  setText(mGridDirection);
        mScaleFactorInput.    setText(String.valueOf(mScaleFactor));
        mSeaLevelInput.       setText(mSeaLevel);
        mRefractionInput.     setText(mRefraction);
        mDatumInput.          setText(mDatum);
        mProjectionInput.     setText(mProjection);
        mZoneInput.           setText(mZone);
        mCoordinateDisplayInput.setText(mCoordinateDisplay);
        mGeoidModelInput.     setText(mGeoidModel);
        mStartingPointIDInput.setText(mStartingPointID);
        mAlphanumericInput.   setText(mAlphanumeric);
        mFeatureCodesInput.   setText(mFeatureCodes);
        mFCControlFileInput.  setText(mFCControlFile);
        mFCTimeStampInput.    setText(mFCTimeStamp);


    }
}


