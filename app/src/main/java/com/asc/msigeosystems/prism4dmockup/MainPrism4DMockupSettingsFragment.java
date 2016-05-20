package com.asc.msigeosystems.prism4dmockup;

import android.graphics.Color;
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
 * The Project Settings Fragment is the UI
 * when the user customizes settings for this project
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DMockupSettingsFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Input / Output Fields on screen
    private TextView mProjectIDLabel;
    private TextView mProjectIDOutput;

    private TextView mProjectNameLabel;
    private TextView mProjectNameOutput;

    private TextView mDistanceUnitsLabel;
    private EditText mDistanceUnitsInput;

    private TextView mDecimalDisplayLabel;
    private EditText mDecimalDisplayInput;

    private TextView mAngleUnitsLabel;
    private EditText mAngleUnitsInput;

    private TextView mAngleDisplayLabel;
    private EditText mAngleDisplayInput;

    private TextView mGridDirectionLabel;
    private EditText mGridDirectionInput;

    private TextView mScaleFactorLabel;
    private EditText mScaleFactorInput;

    private TextView mSeaLevelLabel;
    private EditText mSeaLevelInput;

    private TextView mRefractionLabel;
    private EditText mRefractionInput;

    private TextView mDatumLabel;
    private EditText mDatumInput;

    private TextView mProjectionLabel;
    private EditText mProjectionInput;

    private TextView mZoneLabel;
    private EditText mZoneInput;

    private TextView mCoordinateDisplayLabel;
    private EditText mCoordinateDisplayInput;

    private TextView mGeoidModelLabel;
    private EditText mGeoidModelInput;

    private TextView mStartingPointIDLabel;
    private EditText mStartingPointIDInput;

    private TextView mAlphanumericLabel;
    private EditText mAlphanumericInput;

    private TextView mFeatureCodesLabel;
    private EditText mFeatureCodesInput;

    private TextView mFCControlFileLabel;
    private EditText mFCControlFileInput;

    private TextView mFCTimeStampLabel;
    private EditText mFCTimeStampInput;

    private Button mResetButton;

    private boolean mAnyChanges = false;


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



    private int          mProjectID ;
    private CharSequence mProjectName;
    private CharSequence mProjectCreateDate;
    private CharSequence mProjectMaintDate;
    private CharSequence mProjectDescription;
    private CharSequence mProjectPath;

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


    public static MainPrism4DMockupSettingsFragment newInstance(
            Prism4DProject project,
            Prism4DPath projectPath) {

        Bundle args = new Bundle();

        //It will be some work to make all of the data model serializable
        //so for now, just pass the project values
        args.putInt         (Prism4DProject.sProjectIDTag,
                project.getProjectID());
        args.putCharSequence(Prism4DProject.sProjectNameTag,
                project.getProjectName());
        args.putCharSequence(Prism4DProject.sProjectCreateTag,
                project.getProjectDateCreated().toString());
        args.putCharSequence(Prism4DProject.sProjectMaintTag,
                project.getProjectLastModified().toString());
        args.putCharSequence(Prism4DProject.sProjectDescTag,
                project.getProjectDescription());
        args.putCharSequence(Prism4DPath.sProjectPathTag,
                projectPath.getPath());


        MainPrism4DMockupSettingsFragment fragment =
                new MainPrism4DMockupSettingsFragment();

        fragment.setArguments(args);
        return fragment;
    }

    public MainPrism4DMockupSettingsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        mProjectID          = getArguments().getInt(Prism4DProject.sProjectIDTag);
        mProjectName        = getArguments().getCharSequence(Prism4DProject.sProjectNameTag);
        mProjectCreateDate  = getArguments().getCharSequence(Prism4DProject.sProjectCreateTag);
        mProjectMaintDate   = getArguments().getCharSequence(Prism4DProject.sProjectMaintTag);
        mProjectDescription = getArguments().getCharSequence(Prism4DProject.sProjectDescTag);
        mProjectPath        = getArguments().getCharSequence(Prism4DPath.sProjectPathTag);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_project_settings_prism4_dmockup, container, false);


        //Wire up the UI widgets so they can handle events later
        //Initialize the fields with the defaults

        //Project ID
        mProjectIDLabel  = (TextView) v.findViewById(R.id.projectIDLabel);
        mProjectIDOutput = (TextView) v.findViewById(R.id.projectIDOutput);

        //Project Name
        mProjectNameLabel  = (TextView) v.findViewById(R.id.projectNameLabel);
        mProjectNameOutput = (TextView) v.findViewById(R.id.projectNameOutput);

        //Distance Units
        mDistanceUnitsLabel = (TextView) v.findViewById(R.id.distanceUnitsLabel);
        mDistanceUnitsInput = (EditText) v.findViewById(R.id.distanceUnitsInput);

        //DecimalDisplay
        mDecimalDisplayLabel = (TextView)v.findViewById(R.id.decimalDisplayLabel);
        mDecimalDisplayInput = (EditText) v.findViewById(R.id.decimalDisplayInput);

        //AngleUnits
        mAngleUnitsLabel = (TextView) v.findViewById(R.id.angleUnitsLabel);
        mAngleUnitsInput = (EditText) v.findViewById(R.id.angleUnitsInput);

        //AngleDisplay
        mAngleDisplayLabel = (TextView)v.findViewById(R.id.angleDisplayLabel);
        mAngleDisplayInput = (EditText) v.findViewById(R.id.angleDisplayInput);


        //GridDirection
        mGridDirectionLabel = (TextView) v.findViewById(R.id.gridDirectionLabel);
        mGridDirectionInput = (EditText) v.findViewById(R.id.gridDirectionInput);

        //ScaleFactor
        mScaleFactorLabel = (TextView)v.findViewById(R.id.scaleFactorLabel);
        mScaleFactorInput = (EditText) v.findViewById(R.id.scaleFactorInput);

        //SeaLevel
        mSeaLevelLabel = (TextView) v.findViewById(R.id.seaLevelLabel);
        mSeaLevelInput = (EditText) v.findViewById(R.id.seaLevelInput);

        //Refraction
        mRefractionLabel = (TextView)v.findViewById(R.id.refractionLabel);
        mRefractionInput = (EditText) v.findViewById(R.id.refractionInput);

        //Datum
        mDatumLabel = (TextView) v.findViewById(R.id.datumLabel);
        mDatumInput = (EditText) v.findViewById(R.id.datumInput);

        //Projection
        mProjectionLabel = (TextView)v.findViewById(R.id.projectionLabel);
        mProjectionInput = (EditText) v.findViewById(R.id.projectionInput);

        //Zone
        mZoneLabel = (TextView) v.findViewById(R.id.zoneLabel);
        mZoneInput = (EditText) v.findViewById(R.id.zoneInput);

        //CoordinateDisplay
        mCoordinateDisplayLabel = (TextView)v.findViewById(R.id.coordinateDisplayLabel);
        mCoordinateDisplayInput = (EditText) v.findViewById(R.id.coordinateDisplayInput);


        //GeoidModel
        mGeoidModelLabel = (TextView) v.findViewById(R.id.geoidModelLabel);
        mGeoidModelInput = (EditText) v.findViewById(R.id.geoidModelInput);

        //StartingPointID
        mStartingPointIDLabel = (TextView)v.findViewById(R.id.startingPointIDLabel);
        mStartingPointIDInput = (EditText) v.findViewById(R.id.startingPointIDInput);

        //Alphanumeric
        mAlphanumericLabel = (TextView) v.findViewById(R.id.alphanumericLabel);
        mAlphanumericInput = (EditText) v.findViewById(R.id.alphanumericInput);

        //FeatureCodes
        mFeatureCodesLabel = (TextView)v.findViewById(R.id.featureCodesLabel);
        mFeatureCodesInput = (EditText) v.findViewById(R.id.featureCodesInput);

        //FCControlFile
        mFCControlFileLabel = (TextView) v.findViewById(R.id.fcControlFileLabel);
        mFCControlFileInput = (EditText) v.findViewById(R.id.fcControlFileInput);

        //FCTimeStamp
        mFCTimeStampLabel = (TextView)v.findViewById(R.id.fcTimeStampLabel);
        mFCTimeStampInput = (EditText) v.findViewById(R.id.fcTimeStampInput);


        //set and display default values
        setDefaults();

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


        //Esc Button
        mEscButton = (Button) v.findViewById(R.id.escButton);
        //have to set the color and enable the button as the default is NOT enabled/grayed out
        mEscButton.setEnabled(true);
        mEscButton.setTextColor(Color.BLACK);
        mEscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.no_save,
                        Toast.LENGTH_SHORT).show();

                //Switch to fragment on the top of the back stack
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToPopBackstack();
                }
            }
        });

        //Enter Button
        mEnterButton = (Button) v.findViewById(R.id.enterButton);
        //have to set the color and enable the button as the default is NOT enabled/grayed out
        mEnterButton.setEnabled(true);
        mEnterButton.setTextColor(Color.BLACK);
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.settings_stored,
                        Toast.LENGTH_SHORT).show();

                //Switch to fragment on the top of the back stack
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToPopBackstack();
                }
            }
        });

        return v;
    }//end CreateView

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
        mProjectIDLabel.      setText(R.string.setting_project_id);
        mProjectIDOutput.     setText(String.valueOf(mProjectID));
        mProjectNameLabel.    setText(R.string.settings_project_name);
        mProjectNameOutput.   setText(String.valueOf(mProjectName));
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


