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

import java.util.Date;

/**
 * The Update Project Fragment
 * is passed a project on startup. The project attribute fields are
 * pre-populated prior to updating the project
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DProject14UpdateFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Input / Output Fields on screen
    private TextView mProjectNameLabel;
    private EditText mProjectNameInput;

    private TextView mProjectIDLabel;
    private EditText mProjectIDInput;

    private TextView mProjectDateLabel;
    private EditText mProjectDateInput;

    private TextView mProjectMaintLabel;
    private EditText mProjectMaintInput;

    private TextView mProjectDescLabel;
    private EditText mProjectDescInput;

    private Button mProjectViewSettingsButton;
    private Button mProjectViewExistingButton;
    private Button mProjectMaintainPointsButton;
    private Button mProjectSaveChangesButton;

    private CharSequence mProjectName;
    private CharSequence mProjectID;
    private int          mProjectIntID;
    private CharSequence mProjectCreateDate;
    private CharSequence mProjectMaintDate;
    private CharSequence mProjectDescription;

    private boolean      mProjectChanged = false;
    private CharSequence mProjectPath;





    public static MainPrism4DProject14UpdateFragment newInstance(
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


        MainPrism4DProject14UpdateFragment fragment =
                new MainPrism4DProject14UpdateFragment();

        fragment.setArguments(args);
        return fragment;
    }

    public MainPrism4DProject14UpdateFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        int projectID       = getArguments().getInt(Prism4DProject.sProjectIDTag);
        mProjectID = String.valueOf(projectID);
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
        View v = inflater.inflate(
                R.layout.fragment_project_1_n_maintain_prism4d,
                container,
                false);


        //Wire up the UI widgets so they can handle events later
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields

        //Project Name
        mProjectNameLabel = (TextView) v.findViewById(R.id.projectNameLabel);
        mProjectNameInput = (EditText) v.findViewById(R.id.projectNameInput);
        mProjectNameInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChanged();
                return false;
            }
        });

        //Project ID
        mProjectIDLabel = (TextView)v.findViewById(R.id.projectIDLabel);
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
        mProjectDateLabel = (TextView) v.findViewById(R.id.projectCreationDateLabel);
        mProjectDateInput = (EditText) v.findViewById(R.id.projectCreationDateInput);
        mProjectDateInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               setProjectChanged();
                return false;
            }
        });

        //Project Last Modified Date
        mProjectMaintLabel = (TextView) v.findViewById(R.id.projectModifiedDateLabel);
        mProjectMaintInput = (EditText) v.findViewById(R.id.projectModifiedDateInput);
        mProjectMaintInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChanged();
                return false;
            }
        });

        //Project Description
        mProjectDescLabel = (TextView)v.findViewById(R.id.projectDescLabel);
        mProjectDescInput = (EditText) v.findViewById(R.id.projectDescInput);
        mProjectDescInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setProjectChanged();
                return false;
            }
        });

/***  For now, assume we did the right thing whith the project we passed in
        if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            setCreateNewProject();
        } else if (mProjectPath.equals(Prism4DPath.sCopyTag)){
            setCopyProject();
        }
***/

        //set the screen fields from the arguments bundle
        showInputProject();


        //View Settings Button
        mProjectViewSettingsButton = (Button) v.findViewById(R.id.projectViewSettingsButton);
        mProjectViewSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (mProjectChanged){
                    areYouSureViewSettings();
                } else {
                    MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                    if (myActivity != null){
                        myActivity.switchToProjectSettingsScreen(
                                findThisProject(),
                                new Prism4DPath(mProjectPath));
                    }
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
                //what this button does depends upon the path  {create, open, copy}
                if (mProjectPath.equals(Prism4DPath.sCreateTag)){
                    Toast.makeText(getActivity(),
                            R.string.cant_view_projects,
                            Toast.LENGTH_SHORT).show();

                    MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                    if (myActivity != null){
                        myActivity.switchToProjectListProjectsScreen(new Prism4DPath(mProjectPath));
                    }

                } else if ((mProjectPath.equals(Prism4DPath.sOpenTag)) ||
                           (mProjectPath.equals(Prism4DPath.sCopyTag))) {
                    if (mProjectChanged){
                        //ask the user if should continue
                        areYouSureViewProjects();

                    } else {
                        Toast.makeText(getActivity(),
                                R.string.project_unchanged,
                                Toast.LENGTH_SHORT).show();

                        MainPrism4DActivity myActivity =
                                (MainPrism4DActivity) getActivity();
                        if (myActivity != null){
                            myActivity.switchToProjectListProjectsScreen(new Prism4DPath(mProjectPath));
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


        //Maintain Points Button
        mProjectMaintainPointsButton = (Button) v.findViewById(R.id.projectMaintainPointsButton);
        mProjectMaintainPointsButton.setEnabled(true);
        mProjectMaintainPointsButton.setTextColor(Color.BLACK);
        mProjectMaintainPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProjectChanged){
                    //need to ask first about abandoning changes
                    areYouSureMaintPoints();
                } else {
                    //doesn't matter what path we are on, switch to maintain points
                    MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                    if (myActivity != null){
                        myActivity.switchToMaintainPoints1Screen(
                                findThisProject(),
                                new Prism4DPath(mProjectPath));
                    }
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
                    MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                    if (myActivity != null) {
                        myActivity.popToProject1Screen();
                    }
                }
            }
        });






        return v;
    }

    //Build and display the alert dialog
    private void areYouSureMaintPoints(){
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
                                myActivity.switchToMaintainPoints1Screen(
                                        findThisProject(),
                                        new Prism4DPath(mProjectPath));
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
                                myActivity.popToProject1Screen();
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

                                MainPrism4DActivity myActivity =
                                        (MainPrism4DActivity) getActivity();

                                myActivity.switchToProjectListProjectsScreen(
                                        new Prism4DPath(mProjectPath));
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

                                MainPrism4DActivity myActivity =
                                        (MainPrism4DActivity) getActivity();
                                if (myActivity != null){
                                    myActivity.switchToProjectSettingsScreen(
                                            findThisProject(),
                                            new Prism4DPath(mProjectPath));
                                }
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



    private void showInputProject() {
        //show the data that came out of the input arguments bundle
        mProjectIDInput.setText(mProjectID);
        mProjectNameInput.setText(mProjectName);
        mProjectDateInput.setText(mProjectCreateDate);
        mProjectMaintInput.setText(mProjectMaintDate);
        mProjectDescInput.setText(mProjectDescription);
    }

    private void setProjectChanged(){
        mProjectChanged = true;
        //enable the enter button as the default is NOT enabled/grayed out
        /*****
        if (mProjectPath.equals(Prism4DPath.sCopyTag)){
            mEnterButton.setText(R.string.enter_to_copy_button_label);

        } else if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            mEnterButton.setText(R.string.enter_to_create_button_label);
        } else {
            mEnterButton.setText(R.string.enter_to_save_button_label);
        }
        mEnterButton.setText(R.string.enter_to_save_button_label);
        mEnterButton.setEnabled(true);
        mEnterButton.setTextColor(Color.BLACK);
*****/
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
        //      make sure our singleton list holder exists first
        Prism4DProjectsContainer projectList = Prism4DProjectsContainer.getInstance();
        //      then go get our project
        Prism4DProject newProject = projectList.getProject(Integer.valueOf(mProjectID.toString()));

        if (newProject == null){
            newProject = new Prism4DProject(
                    mProjectName,
                    Integer.valueOf(mProjectID.toString())) ;
            newProject.setProjectDescription(mProjectDescription);
            //for now punt on the dates

            //But tell the user
            Toast.makeText(getActivity(),
                    "Error, project " + mProjectName + " not found",
                    Toast.LENGTH_LONG).show();

            //todo eventually throw an exception
            //and save it in the project list
            projectList.add(newProject);
        }
        return newProject;
    }


    private Prism4DProject saveChanges() {

        //get the project list
        Prism4DProjectsContainer projectList = Prism4DProjectsContainer.getInstance();
        //   get our project
        Prism4DProject newProject = projectList.getProject(
                Integer.valueOf(mProjectID.toString()));

        if (mProjectPath.equals(Prism4DPath.sCreateTag)){
            Toast.makeText(getActivity(),
                    R.string.save_new_project,
                    Toast.LENGTH_SHORT).show();
            //newProject should be null here
            if (newProject != null){
                //It was created earlier with the save changes button
                //but we don't have to create it again
            } else {
                newProject = new Prism4DProject(
                        mProjectNameInput.getText(),
                        Integer.valueOf(mProjectIDInput.getText().toString()));
                //put it into the project list
                projectList.add(newProject);
            }

        } else if (mProjectPath.equals(Prism4DPath.sOpenTag)){
            if (newProject == null){
                //todo decide what to do here
                //there really should have been one already created
                newProject = new Prism4DProject(
                        mProjectNameInput.getText(),
                        Integer.valueOf(mProjectIDInput.getText().toString()));
                projectList.add(newProject);
            }
            Toast.makeText(getActivity(),
                    R.string.save_existing_project,
                    Toast.LENGTH_SHORT).show();

        } else if (mProjectPath.equals(Prism4DPath.sCopyTag)){
            //newProject will be null before it is saved for the first time
            //might have been created with the save changes button
            if (newProject == null){

                newProject = new Prism4DProject(
                        mProjectNameInput.getText(),
                        Integer.valueOf(mProjectIDInput.getText().toString()));
                projectList.add(newProject);
            }
            Toast.makeText(getActivity(),
                    R.string.save_copied_project,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(),
                    R.string.unrecognized_path_encountered,
                    Toast.LENGTH_SHORT).show();
            //todo need to throw an exception here
            //but create a project to be able to continue
            if (newProject == null){

                newProject = new Prism4DProject(
                        mProjectNameInput.getText(),
                        Integer.valueOf(mProjectIDInput.getText().toString()));
                projectList.add(newProject);
            }
        }
        //but regardless of the path,
        // update the project with the screen attributes
        newProject.setProjectName(mProjectNameInput.getText());
        newProject.setProjectDateCreated(new Date());
        newProject.setProjectLastModified(new Date());
        newProject.setProjectDescription(mProjectDescInput.getText());

        //disable the Enter and the Save Changes button


        return newProject;
    }

}


