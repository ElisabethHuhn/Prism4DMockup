package com.asc.msigeosystems.prism4dmockup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Update Project Fragment
 * is passed a project on startup. The project attribute fields are
 * pre-populated prior to updating the project
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DMockupProject14UpdateFragment extends Fragment {

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

    private CharSequence mProjectName;
    private CharSequence mProjectID;
    private int          mProjectIntID;
    private CharSequence mProjectCreateDate;
    private CharSequence mProjectMaintDate;
    private CharSequence mProjectDescription;

    private boolean      mProjectChanged = false;
    private CharSequence mPath;



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

    public static MainPrism4DMockupProject14UpdateFragment newInstance(
            Prism4DMockupProject project, Prism4DMockupPath path) {

        Bundle args = new Bundle();

        //It will be some work to make all of the data model serializable
        //so for now, just pass the project values
        args.putInt         (Prism4DMockupProject.sProjectIDTag,     project.getProjectID());
        args.putCharSequence(Prism4DMockupProject.sProjectNameTag,   project.getProjectName());
        args.putCharSequence(Prism4DMockupProject.sProjectCreateTag, project.getProjectDateCreated().toString());
        args.putCharSequence(Prism4DMockupProject.sProjectMaintTag,  project.getProjectLastModified().toString());
        args.putCharSequence(Prism4DMockupProject.sProjectDescTag,   project.getProjectDescription());
        args.putCharSequence(Prism4DMockupPath.sPathTag,             path.getPath());


        MainPrism4DMockupProject14UpdateFragment fragment =
                new MainPrism4DMockupProject14UpdateFragment();

        fragment.setArguments(args);
        return fragment;
    }

    public MainPrism4DMockupProject14UpdateFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        int projectID       = getArguments().getInt(Prism4DMockupProject.sProjectIDTag);
        mProjectID = String.valueOf(projectID);
        mProjectName        = getArguments().getCharSequence(Prism4DMockupProject.sProjectNameTag);
        mProjectCreateDate  = getArguments().getCharSequence(Prism4DMockupProject.sProjectCreateTag);
        mProjectMaintDate   = getArguments().getCharSequence(Prism4DMockupProject.sProjectMaintTag);
        mProjectDescription = getArguments().getCharSequence(Prism4DMockupProject.sProjectDescTag);
        mPath               = getArguments().getCharSequence(Prism4DMockupPath   .sPathTag);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_project_1_1_create_prism4_dmockup, container, false);


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


        if (mPath.equals(Prism4DMockupPath.sCreateTag)){
            setCreateNewProject();
        }


        //set the screen fields from the arguments bundle
        showInputProject();


        //View Settings Button
        mProjectViewSettingsButton = (Button) v.findViewById(R.id.projectViewSettingsButton);
        mProjectViewSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                MainPrism4DMockupActivity myActivity = (MainPrism4DMockupActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToProjectSettingsScreen();
                }
            }
        });

        //View Existing Projects Button
        mProjectViewExistingButton = (Button) v.findViewById(R.id.projectViewExistingButton);
        if (mPath.equals(Prism4DMockupPath.sCreateTag)){
            //disable the button on the create path
            mProjectViewExistingButton.setEnabled(false);
            mProjectViewExistingButton.setTextColor(Color.GRAY);
        }
        mProjectViewExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //what this button does depends upon the path  {create, open, copy}
                if (mPath.equals(Prism4DMockupPath.sCreateTag)){
                    Toast.makeText(getActivity(),
                            R.string.cant_view_projects,
                            Toast.LENGTH_SHORT).show();
                } else if ((mPath.equals(Prism4DMockupPath.sOpenTag)) ||
                           (mPath.equals(Prism4DMockupPath.sCopyTag))) {
                    if (mProjectChanged){
                        //project has changed, ask if want to abandon changes
                        Toast.makeText(getActivity(),
                                R.string.project_changed_sure_question,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(),
                                R.string.project_unchanged,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            R.string.unrecognized_path_encountered,
                            Toast.LENGTH_SHORT).show();
                    //todo need to throw an exception here
                }

                Prism4DMockupPath newPath = new Prism4DMockupPath(mPath);


                MainPrism4DMockupActivity myActivity = (MainPrism4DMockupActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToProjectListProjectsScreen(newPath);
                }
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

                if (mProjectChanged){
                    Toast.makeText(getActivity(),
                            R.string.project_changed_sure_question,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),
                            R.string.no_save,
                            Toast.LENGTH_SHORT).show();
                }

                MainPrism4DMockupActivity myActivity = (MainPrism4DMockupActivity) getActivity();
                if (myActivity != null){
                    myActivity.popToProject1Screen();
                }

            }
        });

        //Enter Button
        mEnterButton = (Button) v.findViewById(R.id.enterButton);
        //button is enabled once something changes
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //The project must have been changed for this button to work
                if (mProjectChanged) {
                    if (mPath.equals(Prism4DMockupPath.sCreateTag)){
                        Toast.makeText(getActivity(),
                                R.string.save_new_project,
                                Toast.LENGTH_SHORT).show();
                    } else if (mPath.equals(Prism4DMockupPath.sOpenTag)){
                        Toast.makeText(getActivity(),
                                R.string.save_existing_project,
                                Toast.LENGTH_SHORT).show();
                    } else if (mPath.equals(Prism4DMockupPath.sCopyTag)){
                        Toast.makeText(getActivity(),
                                R.string.save_copied_project,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(),
                                R.string.unrecognized_path_encountered,
                                Toast.LENGTH_SHORT).show();
                        //todo need to throw an exception here
                    }


                    MainPrism4DMockupActivity myActivity = (MainPrism4DMockupActivity) getActivity();
                    if (myActivity != null) {
                        myActivity.popToProject1Screen();
                    }
                }
            }
        });

        return v;
    }

    private void setDefaults(){
        mProjectName = "Cambridge Bridge Survey & Layout";
        mProjectID   = "MSI_26021008";
        mProjectCreateDate = "04/16/2016";
        mProjectMaintDate  = "05/10/2016";
        mProjectDescription = "Sargent Road Reconnaissance Survey. "+
                "Party Chief: Rusty Martin, GPS Rodman: Chip Schuller ";


        mProjectIDInput.setText(mProjectID);
        mProjectNameInput.setText(mProjectName);
        mProjectDateInput.setText(mProjectCreateDate);
        mProjectMaintInput.setText(mProjectMaintDate);
        mProjectDescInput.setText(mProjectDescription);

    }

    private void setCreateNewProject() {
        mProjectName = "";
        mProjectIntID = Prism4DMockupProject.getsNextID();
        mProjectID   = String.valueOf(mProjectIntID);
        mProjectCreateDate = "";
        mProjectMaintDate  = "";
        mProjectDescription = "";

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
        mEnterButton.setEnabled(true);
        mEnterButton.setTextColor(Color.BLACK);
    }
}


