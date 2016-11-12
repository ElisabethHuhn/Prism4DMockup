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

import java.util.Date;

/**
 * The Update Project Fragment
 * is passed a project on startup. The project attribute fields are
 * pre-populated prior to updating the project
 *
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DProjectEditFragment extends Fragment {

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


    private Button mProjectViewSettingsButton;
    private Button mProjectViewExistingButton;
    private Button mProjectListPointsButton;
    private Button mProjectSaveChangesButton;
    private Button mProjectExitButton;


    /***********************************************************************/
    /**********   Member Variables  ****************************************/
    /***********************************************************************/

    private CharSequence mProjectName;
    private CharSequence mProjectID;
    private CharSequence mProjectCreateDate;
    private CharSequence mProjectMaintDate;
    private CharSequence mProjectDescription;

    private Prism4DProject mProjectBeingMaintained;

    private boolean      mProjectChanged = false;
    private CharSequence mProjectPath;




    //newInstance() stores the passed parameters in the fragments argument bundle
    public static MainPrism4DProjectEditFragment newInstance(Prism4DProject project,
                                                             Prism4DPath projectPath) {

        Bundle args = new Bundle();

        // TODO: 11/10/2016 Rather than treat dates as char sequences, convert to long milliseconds
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


        MainPrism4DProjectEditFragment fragment = new MainPrism4DProjectEditFragment();

        fragment.setArguments(args);
        return fragment;
    }

    //Constructor
    public MainPrism4DProjectEditFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created with this constructor
    }

    //pull the arguments out of the fragment bundle and store in the member variables
    //In this case, prepopulate the path (create/update/etc) and project
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        int projectID       = getArguments().getInt(Prism4DProject.sProjectIDTag);

        mProjectID          = String.valueOf(projectID);
        mProjectName        = getArguments().getCharSequence(Prism4DProject.sProjectNameTag);
        mProjectCreateDate  = getArguments().getCharSequence(Prism4DProject.sProjectCreateTag);
        mProjectMaintDate   = getArguments().getCharSequence(Prism4DProject.sProjectMaintTag);
        mProjectDescription = getArguments().getCharSequence(Prism4DProject.sProjectDescTag);
        mProjectPath        = getArguments().getCharSequence(Prism4DPath.   sProjectPathTag);

        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(Integer.parseInt(mProjectID.toString().trim()));
        if (project == null){
            //don't assign a new Project ID to this instance, or put it in the ProjectManager List
            project             = new Prism4DProject(mProjectName.toString(), Prism4DProject.sProjectNewID);
            mProjectID          = String.valueOf(project.getProjectID());
            mProjectCreateDate  = project.getProjectDateCreated().toString();
            mProjectMaintDate   = project.getProjectLastModified().toString();
            mProjectDescription = project.getProjectDescription();

        }
        mProjectBeingMaintained = project;


        //put this info on the screen in initializeUI()

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


        //If we had any arguments passed, update the screen with them
        initializeUI();

        //set the title bar subtitle
        setSubtitle();



        return v;
    }

    private void wireWidgets(View v){
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields

        //Project Name
        mProjectNameInput = (EditText) v.findViewById(R.id.projectNameInput);
        mProjectNameInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChanged();
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
        mProjectIDInput.setEnabled(false);
        mProjectIDInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChanged();
                return false;
            }

        });

        //Project Creation Date
        mProjectDateInput = (EditText) v.findViewById(R.id.projectCreationDateInput);
        mProjectDateInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChanged();
                return false;
            }
        });

        //Project Last Modified Date
        mProjectMaintInput = (EditText) v.findViewById(R.id.projectModifiedDateInput);
        mProjectMaintInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChanged();
                return false;
            }
        });

        //Project Description
        mProjectDescInput = (EditText) v.findViewById(R.id.projectDescInput);
        mProjectDescInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChanged();
                return false;
            }
        });




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
        mProjectListPointsButton.setEnabled(true);
        mProjectListPointsButton.setTextColor(Color.BLACK);
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
                    saveChanges();

                    setProjectSaved();
                    ((MainPrism4DActivity) getActivity()).popToTopProjectScreen();

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

    private void initializeUI() {
        //show the data that came out of the input arguments bundle
        mProjectIDInput   .setText(mProjectID);
        mProjectNameInput .setText(mProjectName);
        mProjectDateInput .setText(mProjectCreateDate);
        mProjectMaintInput.setText(mProjectMaintDate);
        mProjectDescInput .setText(mProjectDescription);
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
            //todo need to throw an exception here
            //Notice that we don't leave the screen on this condition
        }

    }

    private void setProjectChanged(){
        mProjectChanged = true;

        //enable the save changes button
        mProjectSaveChangesButton.setEnabled(true);
        mProjectSaveChangesButton.setTextColor(Color.BLACK);
    }

    private void setProjectSaved(){
        mProjectChanged = false;
        //enable the enter button as the default is NOT enabled/grayed out
        //mEnterButton.setText(R.string.enter_to_save_button_label);
        //mEnterButton.setEnabled(false);
        //mEnterButton.setTextColor(Color.GRAY);

        mProjectSaveChangesButton.setEnabled(false);
        mProjectSaveChangesButton.setTextColor(Color.GRAY);
    }

    //returns null if not found
    private Prism4DProject findThisProject(){

        //it should be the one we saved coming into the fragment
        if (mProjectBeingMaintained != null)return mProjectBeingMaintained;

        //      Get the project manager instance
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        //      then use the project manager to get our project
        Prism4DProject newProject = projectManager.getProject(Integer.valueOf(mProjectID.toString()));

        if (newProject == null){
            // TODO: 11/7/2016 is throwing an exception the right thing to do here?
            throw new RuntimeException("Could not find project which must exist");

        }
        return newProject;
    }


    private Prism4DProject saveChanges() {
        if (mProjectBeingMaintained == null){
            mProjectBeingMaintained = findThisProject();
        }

        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

        /******************* CREATE **************************************/
        if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            Toast.makeText(getActivity(),
                    R.string.save_new_project,
                    Toast.LENGTH_SHORT).show();

            if (mProjectBeingMaintained != null){
                //It should have been created earlier with the save changes button

                mProjectBeingMaintained = new Prism4DProject(mProjectNameInput.getText());
                //put it into the project list
                projectManager.add(mProjectBeingMaintained);
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
            projectManager.update(mProjectBeingMaintained);

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

            projectManager.add(mProjectBeingMaintained);

        /************************* UNKNOWN *******************************************/
        } else {
            Toast.makeText(getActivity(),
                    R.string.unrecognized_path_encountered,
                    Toast.LENGTH_SHORT).show();
            throw new RuntimeException(getString(R.string.unrecognized_path_encountered));

        }
        //but regardless of the path,
        // update the project with the screen attributes
        mProjectBeingMaintained.setProjectName(mProjectNameInput.getText());
        // TODO: 11/7/2016 have to do better with date here!!!
        mProjectBeingMaintained.setProjectDateCreated(new Date());
        mProjectBeingMaintained.setProjectLastModified(new Date());
        mProjectBeingMaintained.setProjectDescription(mProjectDescInput.getText());

        //disable the Enter and the Save Changes button


        return mProjectBeingMaintained;
    }



    //Build and display the alert dialog
    private void areYouSureViewProjects(){
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

    //Build and display the alert dialog
    private void areYouSureViewSettings(){
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

        ((MainPrism4DActivity) getActivity()).switchToProjectSettingsScreen(findThisProject(),
                                                                new Prism4DPath(mProjectPath));
    }


    //Build and display the alert dialog
    private void areYouSureListPoints(){

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
        //doesn't matter what path we are on, switch to maintain points
        MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
        myActivity.switchToListPointsScreen(findThisProject(),
                                            new Prism4DPath(Prism4DPath.sShowTag));

    }



    //Build and display the alert dialog
    private void areYouSureExit(){
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

}


