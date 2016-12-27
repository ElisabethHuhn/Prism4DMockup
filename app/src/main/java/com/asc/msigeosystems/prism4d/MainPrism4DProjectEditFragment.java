package com.asc.msigeosystems.prism4d;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

import java.util.Date;

/**
 * The Update Project Fragment
 * is passed a project on startup. The project attribute fields are
 * pre-populated prior to updating the project
 *
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DProjectEditFragment extends    Fragment
                                            implements AdapterView.OnItemSelectedListener{

    /**
     * Create variables for all the widgets
     *
     */


    /***********************************************************************/
    /**********   UI Widget Variables  *************************************/
    /***********************************************************************/

    //Input / Output Fields on screen
    private EditText mProjectNameInput;
    private TextView mProjectIDLabel;
    private EditText mProjectIDInput;
    private EditText mProjectDateInput;
    private EditText mProjectMaintInput;
    private EditText mProjectDescInput;


    //private TextView mPointCoordinateTypePrompt;
    private EditText mProjectCoordTypeOutput;



    private Button mProjectViewSettingsButton;
    private Button mProjectViewExistingButton;
    private Button mProjectListPointsButton;
    private Button mProjectSaveChangesButton;
    private Button mProjectExitButton;


    /***********************************************************/
    /******  Coordinate types for Spinner Widgets     **********/
    /***********************************************************/
    private String[] mCoordinateTypes;
    private Spinner  mSpinner;

    private String   mSelectedCoordinateType;
    private int      mSelectedCoordinateTypePosition;

    /***********************************************************************/
    /**********   Member Variables  ****************************************/
    /***********************************************************************/
    private Prism4DProject mProjectBeingMaintained;

    private boolean      mProjectChanged = false;
    private CharSequence mProjectPath;




    //Constructor
    public MainPrism4DProjectEditFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created with this constructor
    }



    //newInstance() stores the passed parameters in the fragments argument bundle
    public static MainPrism4DProjectEditFragment newInstance(Prism4DProject project,
                                                             Prism4DPath projectPath) {

        Bundle temp = new Bundle();
        //Put the project into an arguments bundle
        Bundle args = Prism4DProject.putProjectInArguments(temp, project);

        //put the path into the same bundle
        args = Prism4DPath.putPathInArguments(args, projectPath);

        MainPrism4DProjectEditFragment fragment = new MainPrism4DProjectEditFragment();

        fragment.setArguments(args);
        return fragment;
    }


    /***********************************************************/
    /*****     Lifecycle Methods                         *******/
    /***********************************************************/

    //pull the arguments out of the fragment bundle and store in the member variables
    //In this case, prepopulate the path (create/update/etc) and project
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        mProjectBeingMaintained = Prism4DProject.getProjectFromArguments(getArguments());

        Prism4DPath path        = Prism4DPath.getPathFromArguments(getArguments());
        mProjectPath            = path.getPath();

        //put this info on the screen in initializeUI() after the view has been created

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(
                R.layout.fragment_project_edit_prism4d,
                container,
                false);


        //Wire up the UI widgets so they can handle events later
        wireWidgets(v);

        wireCoordinateSpinner(v);


        //If we had any arguments passed, update the screen with them
        initializeUI();

        //set the title bar subtitle
        setSubtitle();

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        setSubtitle();
    }


    /***********************************************************/
    /*****     Initialize                                *******/
    /***********************************************************/
    private void wireWidgets(View v){
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields

        //Project Name
        mProjectNameInput = (EditText) v.findViewById(R.id.projectNameInput);
        mProjectNameInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChangedFlags();
                return false;
            }
        });

        //Project ID
        mProjectIDLabel = (TextView) v.findViewById(R.id.projectIDLabel);
        if (mProjectPath == Prism4DPath.sCreateTag){
            mProjectIDLabel.setText(getString(R.string.project_id_will_be_label));
        }
        mProjectIDInput = (EditText) v.findViewById(R.id.projectIDInput);
        //Shouldn't be able to change ID
        mProjectIDInput.setFocusable(false);
        mProjectIDInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChangedFlags();
                return false;
            }

        });

        //Project Creation Date
        mProjectDateInput = (EditText) v.findViewById(R.id.projectCreationDateInput);
        mProjectDateInput.setFocusable(false);
        /*
        mProjectDateInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChangedFlags();
                return false;
            }
        });
        */

        //Project Last Modified Date
        mProjectMaintInput = (EditText) v.findViewById(R.id.projectModifiedDateInput);
        mProjectMaintInput.setFocusable(false);

        /*
       mProjectMaintInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChangedFlags();
                return false;
            }
        });
        */

        //Project Description
        mProjectDescInput = (EditText) v.findViewById(R.id.projectDescInput);
        mProjectDescInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChangedFlags();
                return false;
            }
        });

        //Coordinate Type
       // mProjectCoordTypeOutput = (EditText)v.findViewById(R.id.projectCoordTypeInput);
        //mProjectCoordTypeOutput.setFocusable(false);




        //View Settings Button
        mProjectViewSettingsButton = (Button) v.findViewById(R.id.projectViewSettingsButton);
        mProjectViewSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (mProjectChanged){
                    areYouSureViewSettings();
                } else {
                    switchToProjectSettings();
                }

            }
        });

        //View Existing Projects Button
        mProjectViewExistingButton = (Button) v.findViewById(R.id.projectViewExistingButton);
        if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            //disable the button on the create path
            mProjectViewExistingButton.setEnabled(false);
            mProjectViewExistingButton.setTextColor(Color.GRAY);
        }
        mProjectViewExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                viewProjects();

            }
        });


        //List Points Button
        mProjectListPointsButton = (Button) v.findViewById(R.id.projectListPointsButton);
        if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            mProjectListPointsButton.setEnabled(false);
            mProjectListPointsButton.setTextColor(Color.GRAY);
        } else {
            mProjectListPointsButton.setEnabled(true);
            mProjectListPointsButton.setTextColor(Color.BLACK);
        }

        mProjectListPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mProjectChanged){
                    //need to ask first about abandoning changes
                    areYouSureListPoints();
                } else {
                    switchToListPoints();

                }

            }
        });


        //Save Changes Button
        mProjectSaveChangesButton = (Button) v.findViewById(R.id.projectSaveChangesButton);
        //button is enabled once something changes
        mProjectSaveChangesButton.setEnabled(false);
        mProjectSaveChangesButton.setTextColor(Color.GRAY);
        mProjectSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //The project must have been changed for this button to work
                if (mProjectChanged) {
                    onSave();

                    setProjectSavedFlags();
                    //For now, stay around after save
                    //((MainPrism4DActivity) getActivity()).popToTopProjectScreen();

                }
            }
        });

        //Exit Button
        mProjectExitButton = (Button) v.findViewById(R.id.projectExitButton);
        //button is always enabled
        mProjectExitButton.setEnabled(true);
        mProjectExitButton.setTextColor(Color.BLACK);
        mProjectExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //If the project changed, ask before exiting
                if (mProjectChanged) {
                    areYouSureExit();
                } else {
                    switchToExit();
                }
            }
        });
    }


    private void wireCoordinateSpinner(View v){
        //set the default
        mSelectedCoordinateType = Prism4DCoordinate.sCoordinateTypeClassUTM;

        //Create the array of spinner choices from the Types of Coordinates defined
        mCoordinateTypes = new String[]{
                getString(R.string.enter_coordinate_type),
                Prism4DCoordinate.sCoordinateTypeWGS84,
                Prism4DCoordinate.sCoordinateTypeNAD83,
                Prism4DCoordinate.sCoordinateTypeUTM,
                Prism4DCoordinate.sCoordinateTypeSPCS };

        //Then initialize the spinner itself
        mSpinner = (Spinner) v.findViewById(R.id.coordinate_type_spinner);

        // Create an ArrayAdapter using the Activities context AND
        // the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                                          android.R.layout.simple_spinner_item,
                                                          mCoordinateTypes);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        //attach the listener to the spinner
        mSpinner.setOnItemSelectedListener(this);

        int temp = mProjectBeingMaintained.getPoints().size();
        if (temp > 0){
            //can't change the coordinate type if any points are on the project
            mSpinner.setEnabled(false);
            mSpinner.setClickable(false);
            mSpinner.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGray));
        }


        //mPointCoordinateTypePrompt = (TextView) v.findViewById(R.id.coordinate_prompt);

    }


    private void initializeUI() {
        //show the data that came out of the input arguments bundle

        //a new id is not assigned until the first save
        if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            mProjectIDInput.setText(String.valueOf(Prism4DProject.getPotentialNextID()));
        } else {
            mProjectIDInput.setText(String.valueOf(mProjectBeingMaintained.getProjectID()));
        }
        mProjectNameInput .setText(mProjectBeingMaintained.getProjectName());
        mProjectDateInput .setText(mProjectBeingMaintained.getDateString(
                                                mProjectBeingMaintained.getProjectDateCreated()));
        mProjectMaintInput.setText(mProjectBeingMaintained.getDateString(
                                                mProjectBeingMaintained.getProjectLastModified()));
        mProjectDescInput .setText(mProjectBeingMaintained.getProjectDescription());
        //mProjectCoordTypeOutput.setText(mProjectBeingMaintained.getProjectCoordinateType());
        CharSequence spinnerSelection = mProjectBeingMaintained.getProjectCoordinateType();
        //NOTE: THIS NEXT SECTION MUST BE IN THE SAME ORDER AS THAT WHEN THE SPINNER
        //IS CREATED. THE ORDERING IS ENFORCED BY THE PROGRAMMER AT CODING TIME
        //THERE IS NOTHING AUTOMATIC ABOUT THIS HARD CODING
        //I'm not proud of it, but it works, so.......... be it
        if (spinnerSelection.equals(Prism4DCoordinate.sCoordinateTypeNAD83)) {
            mSelectedCoordinateType = Prism4DCoordinate.sCoordinateTypeClassNAD83;
            mSelectedCoordinateTypePosition = 2;
            mSpinner.setSelection(2);
        } else if (spinnerSelection == Prism4DCoordinate.sCoordinateTypeUTM) {
            mSelectedCoordinateType = Prism4DCoordinate.sCoordinateTypeUTM;
            mSelectedCoordinateTypePosition = 3;
            mSpinner.setSelection(3);
        } else if (spinnerSelection == Prism4DCoordinate.sCoordinateTypeSPCS){
            mSelectedCoordinateType = Prism4DCoordinate.sCoordinateTypeSPCS;
            mSelectedCoordinateTypePosition = 4;
            mSpinner.setSelection(4);
        } else  { //Use WGS84 as the default
            mSelectedCoordinateType = Prism4DCoordinate.sCoordinateTypeWGS84;
            mSelectedCoordinateTypePosition = 1;
            mSpinner.setSelection(1);
            mProjectBeingMaintained.setProjectCoordinateType(mSelectedCoordinateType);
            setProjectChangedFlags();
        }

        //mPointCoordinateTypePrompt.setText();
    }

    private void setSubtitle(){
        String msg;

        if (mProjectPath.equals(Prism4DPath.sOpenTag)) {
            msg = getString(R.string.subtitle_open_project);
        } else if (mProjectPath.equals(Prism4DPath.sCopyTag)) {
            msg = getString(R.string.subtitle_copy_project);
        } else if (mProjectPath.equals(Prism4DPath.sCreateTag)) {
            msg = getString(R.string.subtitle_create_project);
        } else if (mProjectPath.equals(Prism4DPath.sDeleteTag)) {
            msg = getString(R.string.subtitle_delete_project);
        } else if (mProjectPath.equals(Prism4DPath.sEditTag)) {
            msg = getString(R.string.subtitle_edit_project);
        } else {
            msg = getString(R.string.subtitle_error_in_path);
        }

        ((MainPrism4DActivity) getActivity()).switchSubtitle(msg);

    }



    /***********************************************************/
    /*****     Respond to UI events                      *******/
    /***********************************************************/


    /*********************************************/
    /**********     Spinner Callbacks   **********/
    /***********************************************************/

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        mSelectedCoordinateTypePosition = position;
        mSelectedCoordinateType = (String) parent.getItemAtPosition(position);

        setProjectChangedFlags();

        int msg = 0;
        switch(position){
            case 1:
                msg = R.string.wgs84_prompt;

                break;
            case 2:
                msg = R.string.nad83_prompt;

                break;
            case 3:
                msg = R.string.utm_prompt;

                break;
            case 4:
                msg = R.string.spc_prompt;

                break;
            default:
                msg = R.string.enter_coordinate_type;
         }


    }

    public void onNothingSelected(AdapterView<?> parent) {
        //for now, do nothing
    }



    /************************************/
    /*****     Save Button        *******/
    /************************************/
    private Prism4DProject onSave() {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

        /******************* CREATE **************************************/
        if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            Toast.makeText(getActivity(),
                    R.string.save_new_project,
                    Toast.LENGTH_SHORT).show();


            //returns false if there is something wrong with information on the screen and
            //the project can not be updated
            if (updateProjectFromUIFields(mProjectBeingMaintained)) {
                //need to assign a project ID
                int projectID = Prism4DProject.getNextProjectID();
                mProjectBeingMaintained.setProjectID(projectID);
                //Make the Project Settings ID match the project ID
                Prism4DProjectSettings projectSettings = mProjectBeingMaintained.getSettings();
                if (projectSettings == null){
                    projectSettings = new Prism4DProjectSettings(projectID);

                } else {
                    projectSettings.setProjectID(projectID);
                }
                //change the ID field label
                mProjectIDLabel.setText(R.string.project_id_label);

                //put it into the project list
                projectManager.add(mProjectBeingMaintained);
                //change the path to edit
                mProjectPath = Prism4DPath.sEditTag;
                //enable the list points button
                mProjectListPointsButton.setEnabled(true);
                mProjectListPointsButton.setTextColor(Color.BLACK);

            }

        /************************************** Edit *****************************/
        } else if (mProjectPath.equals(Prism4DPath.sEditTag)){
            if (mProjectBeingMaintained == null){
               Toast.makeText(getActivity(),
                              R.string.project_missing_exception,
                              Toast.LENGTH_SHORT).show();
                throw new RuntimeException(getString(R.string.project_missing_exception));
            }
            Toast.makeText(getActivity(),
                    R.string.save_existing_project,
                    Toast.LENGTH_SHORT).show();


            if (updateProjectFromUIFields(mProjectBeingMaintained)){
                projectManager.update(mProjectBeingMaintained);
            }

            /***************************   COPY **************************************/
        } else if (mProjectPath.equals(Prism4DPath.sCopyTag)){
            //mProjectBeingMaintained will be null before it is saved for the first time
            //might have been created with the save changes button
            if (mProjectBeingMaintained == null){

                mProjectBeingMaintained = new Prism4DProject(mProjectNameInput.getText(),
                                             Integer.valueOf(mProjectIDInput.getText().toString()));
            }
            Toast.makeText(getActivity(),
                    R.string.save_copied_project,
                    Toast.LENGTH_SHORT).show();

            if (updateProjectFromUIFields(mProjectBeingMaintained)){
                projectManager.update(mProjectBeingMaintained);
            }

            /************************* UNKNOWN *******************************************/
        } else {
            Toast.makeText(getActivity(),
                    R.string.unrecognized_path_encountered,
                    Toast.LENGTH_SHORT).show();
            throw new RuntimeException(getString(R.string.unrecognized_path_encountered));

        }

        //update the Last Maintained field
        mProjectBeingMaintained.setProjectLastModified(new Date().getTime());
        //and update it on the screen as well
        initializeUI();

        return mProjectBeingMaintained;
    }

    //returns false if there is something wrong with information on the screen
    private boolean updateProjectFromUIFields(Prism4DProject project){
        boolean returnCode = true;
        //show the data that came out of the input arguments bundle
        project.setProjectID(Integer.valueOf(mProjectIDInput  .getText().toString().trim()));
        project.setProjectName              (mProjectNameInput.getText().toString().trim());
        project.setProjectDescription       (mProjectDescInput .getText().toString().trim());
        //The date fields are not modifiable from this screen
        //Set the coordinate type
        if (mSelectedCoordinateType == null){
            project.setProjectCoordinateType("");
        }else{
            project.setProjectCoordinateType(mSelectedCoordinateType);
        }
        return returnCode;

    }


    /************************************/
    /*****   List Projects Button  ******/
    /************************************/
    private void viewProjects(){
        //what this button does depends upon the path  {create, open, copy}
        //*********************** CREATE **************************************/
        if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            Toast.makeText(getActivity(),
                    R.string.cant_view_projects,
                    Toast.LENGTH_SHORT).show();

            //switchToProjectList();

            //*********************** OPEN or COPY or EDIT **************************************/
        } else if ((mProjectPath.equals(Prism4DPath.sOpenTag)) ||
                (mProjectPath.equals(Prism4DPath.sCopyTag)) ||
                (mProjectPath.equals(Prism4DPath.sEditTag)) ) {
            if (mProjectChanged){
                //ask the user if should continue
                areYouSureViewProjects();

            } else {
                Toast.makeText(getActivity(),
                        R.string.project_unchanged,
                        Toast.LENGTH_SHORT).show();

                switchToProjectList();

            }

            //*********************** UNKNOWN **************************************/
        } else {
            Toast.makeText(getActivity(),
                    R.string.unrecognized_path_encountered,
                    Toast.LENGTH_SHORT).show();

            //Notice that we don't leave the screen on this condition
        }

    }

    //Build and display the alert dialog
    private void areYouSureViewProjects(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.abandon_title)
                .setIcon(R.drawable.ground_station_icon)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.continue_abandon_changes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Leave even though project has chaged
                                Toast.makeText(getActivity(),
                                        R.string.continue_abandon_changes,
                                        Toast.LENGTH_SHORT).show();

                               switchToProjectList();
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

    private void switchToProjectList(){
        ((MainPrism4DActivity) getActivity()).
                switchToProjectListProjectsScreen(new Prism4DPath(mProjectPath));
    }



    /************************************/
    /*****  Project Settings Button   ***/
    /************************************/
    //Build and display the alert dialog
    private void areYouSureViewSettings(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.abandon_title)
                .setIcon(R.drawable.ground_station_icon)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.continue_abandon_changes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Leave even though project has chaged
                                Toast.makeText(getActivity(),
                                        R.string.continue_abandon_changes,
                                        Toast.LENGTH_SHORT).show();


                                switchToProjectSettings();

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

    private void switchToProjectSettings(){

        ((MainPrism4DActivity) getActivity()).switchToProjectSettingsScreen(mProjectBeingMaintained,
                                                                new Prism4DPath(mProjectPath));
    }



    /************************************/
    /***** List POints Button     *******/
    /************************************/
    //Build and display the alert dialog
    private void areYouSureListPoints(){

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.abandon_title)
                .setIcon(R.drawable.ground_station_icon)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.continue_abandon_changes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Leave even though project has chaged
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
        //cant't do this if we are creating the project
        //The project must be saved and the path changed to EDIT
        if (!(mProjectPath.equals(Prism4DPath.sCreateTag))) {
            MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
            myActivity.switchToListPointsScreen(mProjectBeingMaintained,
                                                new Prism4DPath(mProjectPath));
        }

    }


    /************************************/
    /*****     Exit Button        *******/
    /************************************/
    //Build and display the alert dialog
    private void areYouSureExit(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.abandon_title)
                .setIcon(R.drawable.ground_station_icon)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.continue_abandon_changes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Leave even though project has chaged
                                Toast.makeText(getActivity(),
                                        R.string.continue_abandon_changes,
                                        Toast.LENGTH_SHORT).show();
                                switchToExit();

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

    private void switchToExit(){
        ((MainPrism4DActivity) getActivity()).popToTopProjectScreen();

    }


    /***********************************************************/
    /*****     Maintain State Flags in this Fragment     *******/
    /***********************************************************/
    private void setProjectChangedFlags(){
        mProjectChanged = true;

        //enable the save changes button
        mProjectSaveChangesButton.setEnabled(true);
        mProjectSaveChangesButton.setTextColor(Color.BLACK);
    }

    private void setProjectSavedFlags(){
        mProjectChanged = false;
        //enable the enter button as the default is NOT enabled/grayed out
        //mEnterButton.setText(R.string.enter_to_save_button_label);
        //mEnterButton.setEnabled(false);
        //mEnterButton.setTextColor(Color.GRAY);

        mProjectSaveChangesButton.setEnabled(false);
        mProjectSaveChangesButton.setTextColor(Color.GRAY);
    }


}


