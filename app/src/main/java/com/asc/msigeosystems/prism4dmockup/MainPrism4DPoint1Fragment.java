package com.asc.msigeosystems.prism4dmockup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Points Fragment is the UI
 * when the user is creating / making changes to project points
 * Created by elisabethhuhn on 5/15/2016.
 */
public class MainPrism4DPoint1Fragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    //Matrix Buttons
    private Button mCreateButton;
    private Button mListPoints;
    private Button mCopyButton;
    private Button mEditButton;
    private Button mDeleteButton;
    private Button m6Button;
    private Button m7Button;
    private Button m8Button;
    private Button m9Button;

    //input arguments
    private Prism4DPath mProjectPath;
    private Prism4DProject mProject;

    private CharSequence mProjectDateCreated;
    private CharSequence mProjectLastMaintained;


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

    public MainPrism4DPoint1Fragment newInstance(
            Prism4DProject project,
            Prism4DPath projectPath){

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

        MainPrism4DPoint1Fragment fragment = new MainPrism4DPoint1Fragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        int projectID       = getArguments().getInt(Prism4DProject.sProjectIDTag);

        //      make sure our singleton list holder exists first
        Prism4DProjectsContainer projectList = Prism4DProjectsContainer.getInstance();
        //      then go get our project
        mProject = projectList.getProject(projectID);

        if (mProject == null){
            mProject = new Prism4DProject(
                    getArguments().getCharSequence(Prism4DProject.sProjectNameTag),
                    getArguments().getInt(Prism4DProject.sProjectIDTag)
            );
            mProject.setProjectDescription(
                    getArguments().getCharSequence(Prism4DProject.sProjectDescTag)
            );
            mProjectDateCreated    =
                    getArguments().getCharSequence(Prism4DProject.sProjectCreateTag);
            mProjectLastMaintained =
                    getArguments().getCharSequence(Prism4DProject.sProjectMaintTag);



            //But tell the user
            Toast.makeText(getActivity(),
                    "Error, project " + mProject.getProjectName() + " not found",
                    Toast.LENGTH_LONG).show();
        }

        mProjectPath = new Prism4DPath(
                getArguments().getCharSequence(Prism4DPath.sProjectPathTag));



    }

    public MainPrism4DPoint1Fragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later

              //List Point Button
        mListPoints = (Button) v.findViewById(R.id.row1Button1);
        mListPoints.setText(R.string.list_points_button_label);
        //the order of images here is left, top, right, bottom
        mListPoints.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_color_folders, 0, 0);
        mListPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToListPointsScreen(
                            mProject.getProjectID(),
                            mProjectPath,
                            new Prism4DPath(Prism4DPath.sOpenTag));
                }

            }
        });

        //Create Button
        mCreateButton = (Button) v.findViewById(R.id.row1Button2);
        mCreateButton.setText(R.string.create_button_label);
        //the order of images here is left, top, right, bottom
        mCreateButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_color_folders, 0, 0);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the collect with maps fragment.
                // But the switching happens on the container Activity
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null) {
                    myActivity.switchToPoint11CreateScreen(mProject,mProjectPath);

                }
            }
        });


        //copy Button
        mCopyButton = (Button) v.findViewById(R.id.row1Button3);
        mCopyButton.setText(R.string.copy_button_label);
        mCopyButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_color_folders, 0, 0);
        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToListPointsScreen(
                            mProject.getProjectID(),
                            mProjectPath,
                            new Prism4DPath(Prism4DPath.sCopyTag));
                }

            }
        });

        //Edit Button
        mEditButton = (Button) v.findViewById(R.id.row2Button1);
        //edit is disabled, so disable the button.
        mEditButton.setEnabled(false);
        mEditButton.setText(R.string.unused_button_label);
        //mEditButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_color_folders, 0, 0);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ///Use open, not Edit
                Toast.makeText(getActivity(),
                        R.string.project_no_edit,
                        Toast.LENGTH_SHORT).show();
/***
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToProject14EditScreen(mProject,mProjectPath);
                }
***/
            }
        });

        //Delete Button
        mDeleteButton = (Button) v.findViewById(R.id.row2Button2);
        mDeleteButton.setText(R.string.delete_button_label);
        mDeleteButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_color_folders, 0, 0);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.switchToListPointsScreen(
                            mProject.getProjectID(),
                            mProjectPath,
                            new Prism4DPath(Prism4DPath.sDeleteTag));
                }

            }
        });

        //6 Button
        m6Button = (Button) v.findViewById(R.id.row2Button3);
        m6Button.setText(R.string.unused_button_label);
        //m6Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_color_folders, 0, 0);
        m6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //7 Button
        m7Button = (Button) v.findViewById(R.id.row3Button1);
        m7Button.setText(R.string.unused_button_label);
        //m7Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.color_folders, 0, 0);
        m7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //8 Button
        m8Button = (Button) v.findViewById(R.id.row3Button2);
        m8Button.setText(R.string.unused_button_label);
        //m8Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.color_folders, 0, 0);
        m8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //9
        m9Button = (Button) v.findViewById(R.id.row3Button3);
        m9Button.setText(R.string.unused_button_label);
        //m9Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_color_folders, 0, 0);
        m9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });



        //FOOTER WIDGETS

        //  Esc and Enter buttons are enabled on the project screen

        //Esc Button
        mEscButton = (Button) v.findViewById(R.id.escButton);
        mEscButton.setEnabled(true);
        mEscButton.setTextColor(Color.BLACK);
        mEscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    myActivity.popToProject1Screen();
                }

            }
        });

        //Enter Button
        mEnterButton = (Button) v.findViewById(R.id.enterButton);
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            //for now, just put up a toast that the button was pressed
            Toast.makeText(getActivity(),
                    R.string.enter_button_label,
                    Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}


