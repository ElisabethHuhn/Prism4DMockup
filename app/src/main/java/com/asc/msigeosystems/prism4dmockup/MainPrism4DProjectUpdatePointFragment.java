package com.asc.msigeosystems.prism4dmockup;

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

/**
 * The Maintain Point Fragment
 * is passed a point on startup. The point attribute fields are
 * pre-populated prior to updating the point
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DProjectUpdatePointFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Input / Output Fields on screen
    private TextView mPointProjectIDLabel;
    private EditText mPointProjectIDInput;

    private TextView mPointIDLabel;
    private EditText mPointIDInput;

    private TextView mPointEastingLabel;
    private EditText mPointEastingInput;

    private TextView mPointNorthingLabel;
    private EditText mPointNorthingInput;

    private TextView mPointElevationLabel;
    private EditText mPointElevationInput;

    private TextView mPointDescLabel;
    private EditText mPointDescInput;

    private TextView mPointNotesLabel;
    private EditText mPointNotesInput;


    private Button mPointViewExistingButton;
    private Button mPointSaveChangesButton;

    private int          mPointProjectID;

    private int          mPointID;
    private double       mPointEasting;
    private double       mPointNorthing;
    private double       mPointElevation;
    private CharSequence mPointDescription;
    private CharSequence mPointNotes;

    private boolean      mPointChanged = false;
    private CharSequence mProjectPath;
    private CharSequence mPointPath;





    public static MainPrism4DProjectUpdatePointFragment newInstance(
            Prism4DPath projectPath,
            Prism4DPoint point,
            Prism4DPath pointPath) {

        Bundle args = new Bundle();

        //It will be some work to make all of the data model serializable
        //so for now, just pass the point values
        args.putInt         (Prism4DPoint.sPointProjectIDTag,point.getProjectID());
        args.putInt         (Prism4DPoint.sPointIDTag,       point.getPointID());
        args.putDouble      (Prism4DPoint.sPointEastingTag,  point.getPointEasting());
        args.putDouble      (Prism4DPoint.sPointNorthingTag, point.getPointNorthing());
        args.putDouble      (Prism4DPoint.sPointElevationTag,point.getPointElevation());
        args.putCharSequence(Prism4DPoint.sPointDescTag,     point.getPointDescription());
        args.putCharSequence(Prism4DPoint.sPointNotesTag,    point.getPointNotes());
        args.putCharSequence(Prism4DPath.sProjectPathTag,   projectPath.getPath());
        args.putCharSequence(Prism4DPath.sPointPathTag,     pointPath  .getPath());


        MainPrism4DProjectUpdatePointFragment fragment =
                new MainPrism4DProjectUpdatePointFragment();

        fragment.setArguments(args);
        return fragment;
    }

    public MainPrism4DProjectUpdatePointFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        mPointProjectID  = getArguments().getInt   (Prism4DPoint.sPointProjectIDTag);
        mPointID         = getArguments().getInt   (Prism4DPoint.sPointIDTag);
        mPointEasting    = getArguments().getDouble(Prism4DPoint.sPointEastingTag);
        mPointNorthing   = getArguments().getDouble(Prism4DPoint.sPointNorthingTag);
        mPointElevation  = getArguments().getDouble(Prism4DPoint.sPointNameTag);
        mPointDescription = getArguments().getCharSequence(Prism4DPoint.sPointDescTag);
        mPointNotes      = getArguments().getCharSequence(Prism4DPoint.sPointNotesTag);
        mProjectPath     = getArguments().getCharSequence(Prism4DPath.sProjectPathTag);
        mPointPath       = getArguments().getCharSequence(Prism4DPath.sPointPathTag);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(
                R.layout.fragment_project_1_6_maintain_point_prism4d,
                container,
                false);


        //Wire up the UI widgets so they can handle events later
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields


        //Project ID
        mPointProjectIDLabel = (TextView)v.findViewById(R.id.pointProjectIDLabel);
        mPointProjectIDInput = (EditText) v.findViewById(R.id.pointProjectIDInput);
        //Can't change the project of a point
        mPointProjectIDInput.setFocusable(false);
        mPointProjectIDInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChanged();
                return false;
            }

        });


        //Point ID
        mPointIDLabel = (TextView)v.findViewById(R.id.pointIDLabel);
        mPointIDInput = (EditText) v.findViewById(R.id.pointIDInput);
        mPointIDInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChanged();
                return false;
            }

        });

        //Point Easting Date
        mPointEastingLabel = (TextView) v.findViewById(R.id.pointEastingLabel);
        mPointEastingInput = (EditText) v.findViewById(R.id.pointEastingInput);
        mPointEastingInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

               setPointChanged();
                return false;
            }
        });

        //Point Northing Date
        mPointNorthingLabel = (TextView) v.findViewById(R.id.pointNorthingLabel);
        mPointNorthingInput = (EditText) v.findViewById(R.id.pointNorthingInput);
        mPointNorthingInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChanged();
                return false;
            }
        });

        //Point Elevation Date
        mPointElevationLabel = (TextView) v.findViewById(R.id.pointElevationLabel);
        mPointElevationInput = (EditText) v.findViewById(R.id.pointElevationInput);
        mPointElevationInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChanged();
                return false;
            }
        });

        //Point Description
        mPointDescLabel = (TextView)v.findViewById(R.id.pointDescLabel);
        mPointDescInput = (EditText) v.findViewById(R.id.pointDescInput);
        mPointDescInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChanged();
                return false;
            }
        });


        //Point Notes
        mPointNotesLabel = (TextView)v.findViewById(R.id.pointNotesLabel);
        mPointNotesInput = (EditText) v.findViewById(R.id.pointNotesInput);
        mPointNotesInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setPointChanged();
                return false;
            }
        });


        if (mPointPath.equals(Prism4DPath.sCreateTag)){
            setCreateNewPoint();
        } else if (mPointPath.equals(Prism4DPath.sCopyTag)){
            setCopyPoint();
        }


        //set the screen fields from the arguments bundle
        showInputPoint();




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

                //Create a new point with these attributes
                Prism4DPoint newPoint = createNewPoint(mPointProjectID);

                //what this button does depends upon the path  {create, open, copy}
                if (mPointPath.equals(Prism4DPath.sCreateTag)){
                    Toast.makeText(getActivity(),
                            R.string.cant_view_points,
                            Toast.LENGTH_SHORT).show();

                    MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                    if (myActivity != null){
                        myActivity.switchToListPointsScreen(
                                mPointProjectID,
                                new Prism4DPath(mProjectPath),
                                new Prism4DPath(mPointPath));
                    }

                } else if ((mPointPath.equals(Prism4DPath.sOpenTag)) ||
                           (mPointPath.equals(Prism4DPath.sCopyTag))) {
                    if (mPointChanged){
                        //ask the user if should continue
                        areYouSureViewPoints();

                    } else {
                        Toast.makeText(getActivity(),
                                R.string.point_unchanged,
                                Toast.LENGTH_SHORT).show();

                        MainPrism4DActivity myActivity =
                                (MainPrism4DActivity) getActivity();
                        if (myActivity != null){
                            myActivity.switchToListPointsScreen(
                                    mPointProjectID,
                                    new Prism4DPath(mProjectPath),
                                    new Prism4DPath(mPointPath));
                        }
                    }
                } else {
                    Toast.makeText(getActivity(),
                            R.string.unrecognized_path_encountered,
                            Toast.LENGTH_SHORT).show();
                    //todo need to throw an exception here
                    //Notice that we don't leave the screen on this condition
                }
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

                //The point must have been changed for this button to work
                if (mPointChanged) {
                    saveChanges();
                    setPointSaved();

                    MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                    if (myActivity != null) {
                        myActivity.popToProjectUpdateScreen();
                    }
                }
            }
        });



        //FOOTER WIDGETS






        return v;
    }

    //Either returns the new/updated point, or null if errors encountered
    private Prism4DPoint saveChanges() {
        //get any existing point from the singleton
        Prism4DPointsContainer pointsContainer = Prism4DPointsContainer.getInstance();

        Prism4DPoint newPoint;

        if (mPointPath.equals(Prism4DPath.sCreateTag)){
            newPoint = storeNewPoint(pointsContainer);

            Toast.makeText(getActivity(),
                    R.string.save_new_point,
                    Toast.LENGTH_SHORT).show();

        } else if (mPointPath.equals(Prism4DPath.sOpenTag)){
            newPoint = updateExistingPoint(pointsContainer);

            Toast.makeText(getActivity(),
                    R.string.save_existing_point,
                    Toast.LENGTH_SHORT).show();

        } else if (mPointPath.equals(Prism4DPath.sCopyTag)){
            newPoint = storeNewPoint(pointsContainer);

            Toast.makeText(getActivity(),
                    R.string.save_copied_point,
                    Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(),
                    R.string.unrecognized_path_encountered,
                    Toast.LENGTH_SHORT).show();
            //todo need to throw an exception here
            //Note that here we really do leave the screen
            //so the user can get out of the error condition
            //but no point is created or stored or updated

            return null;
        }

        if (newPoint == null){
            //we must be creating it for the first time

            //first we need the project
            Prism4DProjectsContainer projectsContainer = Prism4DProjectsContainer.getInstance();
            Prism4DProject project = projectsContainer.getProject(mPointProjectID);

            //so now make the new point
            newPoint = new Prism4DPoint(project);
            //and store it off in the singleton repository
            pointsContainer.add(newPoint);
        }

        //update the point with values from this screen
        newPoint.setPointEasting(Double.valueOf(mPointEastingInput.getText().toString()));
        newPoint.setPointNorthing(Double.valueOf(mPointNorthingInput.getText().toString()));
        newPoint.setPointElevation(Double.valueOf(mPointElevationInput.getText().toString()));
        newPoint.setPointDescription(mPointDescInput.getText().toString());
        newPoint.setPointNotes(mPointNotesInput.getText().toString());

        return newPoint;

    }


    private Prism4DPoint storeNewPoint (Prism4DPointsContainer pointsContainer){
        Prism4DPoint newPoint;
        newPoint = createNewPoint(mPointProjectID);
        pointsContainer.getPoints().add(newPoint);
        return updateThisPoint(newPoint);
    }

    private Prism4DPoint updateExistingPoint (Prism4DPointsContainer pointsContainer){
        Prism4DPoint newPoint = pointsContainer.getPoint(mPointProjectID, mPointID);

        if (newPoint == null){
            //todo throw an exception here
            //but for the mockup, keep going
            newPoint = createNewPoint(mPointProjectID);
            pointsContainer.getPoints().add(newPoint);
        }
        return updateThisPoint(newPoint);
    }

    private Prism4DPoint updateThisPoint (Prism4DPoint thisPoint){
        thisPoint.setPointEasting(mPointEasting);
        thisPoint.setPointNorthing(mPointNorthing);
        thisPoint.setPointElevation(mPointElevation);
        thisPoint.setPointDescription(mPointDescription);
        thisPoint.setPointNotes(mPointNotes);

        return thisPoint;
    }

    //Build and display the alert dialog
    private void areYouSureEsc(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.abandon_title)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.continue_abandon_changes,
                                   new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Leave even though project has chaged
                        Toast.makeText(getActivity(),
                                R.string.continue_abandon_changes,
                                Toast.LENGTH_SHORT).show();

                        MainPrism4DActivity myActivity =
                                                  (MainPrism4DActivity) getActivity();
                        myActivity.popToProjectUpdateScreen();
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

                                MainPrism4DActivity myActivity =
                                        (MainPrism4DActivity) getActivity();

                                myActivity.switchToListPointsScreen(
                                        mPointProjectID,
                                        new Prism4DPath(mProjectPath),
                                        new Prism4DPath(mPointPath));
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




    private void setCreateNewPoint() {

        //mPointProject ID was set with the passed arguments
        //The problem here is that we only have the project ID,
        //   not the real project
        //And we don't have a database yet, so we can't look it up
        Prism4DProject project =
                new Prism4DProject("Dummy Project Name", mPointProjectID);
        mPointID = project.getNextPointID();
        mPointEasting = 0.0;
        mPointNorthing = 0.0;
        mPointElevation = 0.0;
        mPointDescription = "";
        mPointNotes = "";


    }


    private Prism4DProject getOurProject (int projectID){
        //get the project list
        Prism4DProjectsContainer projectList = Prism4DProjectsContainer.getInstance();
        //      then go get our project out of the list
        Prism4DProject ourProject = projectList.getProject(projectID);
        if (ourProject == null){
            //if it doesn't already exist, we have to create one
            Prism4DProject project =
                    new Prism4DProject("Dummy Project Name", projectID);
            //todo really need to throw an exception here

        }
        return ourProject;
    }


    private Prism4DPoint createNewPoint(int projectID){

        //mPointProject ID was set with the passed arguments
        //Look up the project

        // get the point if it already exists
        //      get our project list
        Prism4DPointsContainer pointList = Prism4DPointsContainer.getInstance();
        //      then go get our point
        Prism4DPoint ourPoint = pointList.getPoint(projectID, mPointID);

        if (ourPoint == null){
            //it isn't alread in the list, so add it
            ourPoint = new Prism4DPoint(getOurProject(projectID));
            pointList.add(ourPoint);
            //todo probably need to assure that this is create or copy path
        }

        ourPoint.setPointID         (mPointID);
        ourPoint.setPointEasting    (mPointEasting);
        ourPoint.setPointNorthing   (mPointNorthing);
        ourPoint.setPointElevation  (mPointElevation);
        ourPoint.setPointDescription(mPointDescription);
        ourPoint.setPointNotes      (mPointNotes);

        return ourPoint;
    }


    private void setCopyPoint() {

        Prism4DProject project = getOurProject(mPointProjectID);

        if (project == null){
            project = new Prism4DProject("Dummy Project Name",mPointProjectID);
            //todo throw an exception here
        }

        mPointID = project.getNextPointID();
        mPointIDInput.setText(Double.toString(mPointID));

    }


    private void showInputPoint() {
        //show the data that came out of the input arguments bundle
        mPointProjectIDInput.setText(String.valueOf(mPointProjectID));
        mPointIDInput.setText       (String.valueOf (mPointID));
        mPointEastingInput.setText  (Double.toString(mPointEasting));
        mPointNorthingInput.setText (Double.toString(mPointNorthing));
        mPointElevationInput.setText(Double.toString(mPointElevation));
        mPointDescInput.setText     (mPointDescription.toString());
        mPointNotesInput.setText    (mPointNotes.toString());
    }


    private void setPointChanged(){
        mPointChanged = true;
        //enable the enter button as the default is NOT enabled/grayed out


        //enable the save changes button too
        mPointSaveChangesButton.setEnabled(true);
        mPointSaveChangesButton.setTextColor(Color.BLACK);
    }

    private void setPointSaved(){
        mPointChanged = false;

        //enable the save changes button too
        mPointSaveChangesButton.setEnabled(false);
        mPointSaveChangesButton.setTextColor(Color.GRAY);
    }


}


