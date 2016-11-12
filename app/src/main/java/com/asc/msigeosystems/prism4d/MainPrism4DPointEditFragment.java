package com.asc.msigeosystems.prism4d;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

/**
 * The Maintain Point Fragment
 * is passed a point on startup. The point attribute fields are
 * pre-populated prior to updating the point
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DPointEditFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */



    /***********************************************************/
    /**********  Point Attribute UI Widgets     ****************/
    /***********************************************************/
    private EditText mPointProjectIDInput;
    private EditText mPointIDInput;
    private EditText mPointDescInput;
    private EditText mPointNotesInput;

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
    /**********  E/N Coordinate UI Widgets      ****************/
    /***********************************************************/
    private EditText       mPointEastingMetersInput;
    private EditText       mPointNorthingMetersInput;
    private EditText       mPointEastingFeetInput;
    private EditText       mPointNorthingFeetInput;
    private EditText       mPointZoneInput;
    private EditText       mPointHemisphereInput;
    private EditText       mPointLatbandInput;
    private EditText       mPointConvergenceInput;
    private EditText       mPointScaleFactorInput;
    private EditText       mPointElevationInput;

    /***********************************************************/
    /**********  Other UI Widgets     **************************/
    /***********************************************************/

    private Button mPointViewExistingButton;
    private Button mPointSaveChangesButton;





    /***********************************************************/
    /**********  Point Attribute Variables      ****************/
    /***********************************************************/


    private int          mForProjectID;
    private int          mPointID;
    private CharSequence mPointDescription;
    private CharSequence mPointNotes;



    /***********************************************************/
    /**********  Lat/Long Coordinate Variables  ****************/
    /***********************************************************/
    private Prism4DCoordinateWGS84 llCoordinate;



    /***********************************************************/
    /**********  E/N Coordinate Variables       ****************/
    /***********************************************************/
    private Prism4DCoordinateUTM enCoordinate;

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

        Bundle args = new Bundle();

        //It will be some work to make all of the data model serializable
        //so for now, just pass the point values
        args.putCharSequence(Prism4DPath.sPointPathTag,       pointPath.getPath());
        args.putInt         (Prism4DPoint.sPointProjectIDTag, projectID);

        if (point == null){
            //This  happens when the point is being created by this fragment on save
            args.putInt(Prism4DPoint.sPointIDTag,0);
        } else {
            args.putInt(Prism4DPoint.sPointIDTag, point.getPointID());
        }
        //assume all other attributes exist on the point being managed by the PointManager



        MainPrism4DPointEditFragment fragment = new MainPrism4DPointEditFragment();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        mPointPath      = getArguments().getCharSequence(Prism4DPath.sPointPathTag);
        mForProjectID = getArguments().getInt         (Prism4DPoint.sPointProjectIDTag);
        mPointID        = getArguments().getInt         (Prism4DPoint.sPointIDTag);

        //If we are on the Create Path, we won't have a point here
        if (mPointID != 0) {
            Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
            Prism4DPoint point = pointManager.getPoint(mForProjectID, mPointID);

            if (point == null) {
                throw new RuntimeException("PointID " + String.valueOf(mPointID) +
                        " in ProjectID " + String.valueOf(mForProjectID) +
                        " does NOT exist. Can not edit");
            }
        }


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
        wireCoordinateWidgets(v);

        //set the screen fields from the arguments bundle
        initializeUI();

        //Show the subtitle for this fragment
        ((MainPrism4DActivity) getActivity()).switchSubtitle(getString(R.string.subtitle_edit_point));

        return v;
    }


    private void wireWidgets(View v){

        //Project ID
        mPointProjectIDInput = (EditText) v.findViewById(R.id.pointProjectIDInput);
        //Can't change the project of a point
        mPointProjectIDInput.setFocusable(false);
        mPointProjectIDInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }

        });


        //Point ID
        mPointIDInput = (EditText) v.findViewById(R.id.pointIDInput);
        mPointIDInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChangedFlags();
                return false;
            }

        });


        //Point Description
        mPointDescInput = (EditText) v.findViewById(R.id.pointDescInput);
        mPointDescInput.setOnTouchListener(new View.OnTouchListener() {
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

                listPoints();


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

    private void wireCoordinateWidgets(View v){
        /****************************************************************************/
        /********  For now, just one LL Coordinate and one EN Coordinate     ********/
        /****************************************************************************/
        View field_container;
        TextView label;

        MainPrism4DActivity myActivity = (MainPrism4DActivity)getActivity();

        //set up the UI widgets for the latitude/longetude coordinates
        field_container = v.findViewById(R.id.latitudeContainer);
        label = (TextView)field_container.findViewById(R.id.ll_label);
        label.setText(getString(R.string.latitude_label));
        mPointLatitudeDDInput = (EditText) field_container.findViewById(R.id.ll_dd_input);
        mPointLatitudeDInput  = (EditText) field_container.findViewById(R.id.ll_d_Input) ;
        mPointLatitudeMInput  = (EditText) field_container.findViewById(R.id.ll_m_Input);
        mPointLatitudeSInput  = (EditText) field_container.findViewById(R.id.ll_s_Input) ;

        field_container = v.findViewById(R.id.longitudeContainer);
        mPointLongitudeDDInput = (EditText) field_container.findViewById(R.id.ll_dd_input);
        mPointLongitudeDInput  = (EditText) field_container.findViewById(R.id.ll_d_Input) ;
        mPointLongitudeMInput  = (EditText) field_container.findViewById(R.id.ll_m_Input);
        mPointLongitudeSInput  = (EditText) field_container.findViewById(R.id.ll_s_Input) ;

        field_container = v.findViewById(R.id.elevationGeoidContainer);
        mPointElevationMetersInput = (EditText) field_container.findViewById(R.id.elevationMetersInput);
        mPointElevationFeetInput   = (EditText) field_container.findViewById(R.id.elevationFeetInput) ;
        mPointGeoidMetersInput     = (EditText) field_container.findViewById(R.id.GeoidHeightMetersInput);
        mPointGeoidFeetInput       = (EditText) field_container.findViewById(R.id.GeoidHeightFeetInput) ;

        //set up the widgets for the easting/northing oordinate

        label = (TextView) v.findViewById(R.id.en_prompt);
        //label.setText()
        // TODO: 11/11/2016 Right now the prompt and the label below defaulted to UTM, may need to change it

        field_container = v.findViewById(R.id.zhlContainer);
        label = (TextView) field_container.findViewById(R.id.coord_label) ;
        //label.setText();

        mPointZoneInput       = (EditText) field_container.findViewById(R.id.zoneOutput);
        mPointHemisphereInput = (EditText) field_container.findViewById(R.id.hemisphereOutput);
        mPointLatbandInput    = (EditText) field_container.findViewById(R.id.latbandOutput);


        field_container = v.findViewById(R.id.eastingContainer);
        label = (TextView) field_container.findViewById(R.id.en_label) ;
        label.setText(getString(R.string.easting_label));
        mPointEastingMetersInput = (EditText)field_container.findViewById(R.id.metersOutput);
        mPointEastingFeetInput   = (EditText)field_container.findViewById(R.id.feetOutput);

        field_container = v.findViewById(R.id.northingContainer);
        label = (TextView) field_container.findViewById(R.id.en_label) ;
        label.setText(getString(R.string.northing_label));
        mPointNorthingMetersInput = (EditText)field_container.findViewById(R.id.metersOutput);
        mPointNorthingFeetInput   = (EditText)field_container.findViewById(R.id.feetOutput);


        field_container = v.findViewById(R.id.convergenceContainer);
        mPointConvergenceInput = (EditText) field_container.findViewById(R.id.convergenceOutput);
        mPointScaleFactorInput = (EditText) field_container.findViewById(R.id.scaleFactorOutput);

    }

    private void initializeUI() {
        //show the data that came out of the input arguments bundle
        mPointProjectIDInput.setText(String.valueOf(mForProjectID));
        mPointIDInput.setText       (String.valueOf (mPointID));

        if (mPointID != 0){
            mPointDescInput.setText(mPointDescription.toString());
            mPointNotesInput.setText(mPointNotes.toString());
        }
    }



    private void onSave(){

        //The point must have been changed for this button to work
        if (mPointChanged) {
            //saveChanges();

            //What happens here depends upon the path
            if (mPointPath.equals(Prism4DPath.sCreateTag)){
                createNewPoint();
                //from now on we are editing the point
                mPointPath = Prism4DPath.sEditTag;
            } else if (mPointPath.equals(Prism4DPath.sEditTag)){
                //update the existing point with info on the screen
                Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
                Prism4DPoint point = pointManager.getPoint(mForProjectID, mPointID);

                updatePointAttributesFromUI(point);

                //update the stored point
                pointManager.update(mForProjectID, point);
            }
            //all other paths do nothing

            setPointSavedFlags();
        }

    }

    private void createNewPoint(){
        //Start with an empty point with default values
        Prism4DPoint newPoint = new Prism4DPoint();

        //save the project ID
        newPoint.setForProjectID(mForProjectID);
        //have to ask the project for a point ID
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject        project        = projectManager.getProject(mForProjectID);

        //assign a new point ID
        newPoint.setPointID(project.getNextPointID());

        updatePointAttributesFromUI(newPoint);

        //now add the point to memory and db
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        pointManager.addToProject(project, newPoint, true);
    }

    private void updatePointAttributesFromUI(Prism4DPoint point){
        point.setHasACoordinateID(0);
        point.setPointDescription(mPointDescInput.getText().toString().trim());
        point.setPointNotes(mPointNotesInput.getText().toString().trim());

    }


    private void listPoints(){
        //what this button does depends upon the path  {create, open, copy, edit, show}
        /***************** CREATE ************************************************/
        if (mPointPath.equals(Prism4DPath.sCreateTag)){
            Toast.makeText(getActivity(),
                    R.string.cant_view_points,
                    Toast.LENGTH_SHORT).show();
            goToListPoints();

        /*************************** OPEN / COPY / EDIT / SHOW **************************************/
        } else if ((mPointPath.equals(Prism4DPath.sOpenTag)) ||
                   (mPointPath.equals(Prism4DPath.sCopyTag)) ||
                   (mPointPath.equals(Prism4DPath.sEditTag)) ||
                   (mPointPath.equals(Prism4DPath.sShowTag))) {

            goToListPoints();


        /************************************** UNKNOWN *************************/
        } else {
            Toast.makeText(getActivity(),
                    R.string.unrecognized_path_encountered,
                    Toast.LENGTH_SHORT).show();

            //Don't really know what to do here, but switch path to show and continue
            mPointPath = Prism4DPath.sShowTag;
            goToListPoints();

        }
    }

    private void goToListPoints(){
        if (mPointChanged){
            //ask the user if should continue
            areYouSureViewPoints();

        } else {
            Toast.makeText(getActivity(),
                    R.string.point_unchanged,
                    Toast.LENGTH_SHORT).show();


                switchToListPoints();
        }
    }

    private void switchToListPoints(){
        MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
        myActivity.switchToListPointsScreen(mForProjectID, new Prism4DPath(mPointPath));
    }





    //Build and display the alert dialog
    private void areYouSureViewPoints(){
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


    private void setPointChangedFlags(){
        mPointChanged = true;
        //enable the enter button as the default is NOT enabled/grayed out


        //enable the save changes button too
        mPointSaveChangesButton.setEnabled(true);
        mPointSaveChangesButton.setTextColor(Color.BLACK);
    }

    private void setPointSavedFlags(){
        mPointChanged = false;

        //enable the save changes button too
        mPointSaveChangesButton.setEnabled(false);
        mPointSaveChangesButton.setTextColor(Color.GRAY);
    }


}


