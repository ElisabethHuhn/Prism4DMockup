package com.asc.msigeosystems.prism4d;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

/**
 * The Points Fragment is the UI
 * when the user is creating / making changes to project points
 * Created by Elisabeth Huhn on 5/15/2016.
 */
public class MainPrism4DTopPointFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    private TextView mScreenLabel;

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

    private long          mProjectDateCreated;
    private long          mProjectLastMaintained;




    public MainPrism4DTopPointFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    public static MainPrism4DTopPointFragment newInstance(
            Prism4DProject project,
            Prism4DPath projectPath){

        Bundle args = Prism4DProject.putProjectInArguments(new Bundle(), project);

        args = Prism4DPath.putPathInArguments(args, projectPath);

        MainPrism4DTopPointFragment fragment = new MainPrism4DTopPointFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        Prism4DProject project = Prism4DProject.getProjectFromArguments(getArguments());

        mProjectPath = Prism4DPath.getPathFromArguments(getArguments());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later
        wireWidgets(v);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        setSubtitle();
    }

    private void setSubtitle() {
        ((MainPrism4DActivity) getActivity()).switchSubtitle(R.string.subtitle_points);
    }


    private void wireWidgets(View v){


        //Tell the user which project is open
        mScreenLabel = (TextView) v.findViewById(R.id.matrix_screen_label);
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                      Prism4DConstantsAndUtilities.getInstance();
        mScreenLabel.setText(constantsAndUtilities.getOpenProjectIDMessage(getActivity()));
        int color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        mScreenLabel.setBackgroundColor(color);


        //List Point Button
        mListPoints = (Button) v.findViewById(R.id.row1Button1);
        mListPoints.setText(R.string.list_points_button_label);
        //the order of images here is left, top, right, bottom
        mListPoints.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
        mListPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPrism4DActivity)getActivity()).switchToListPointsScreen(
                            mProject.getProjectID(),
                            new Prism4DPath(Prism4DPath.sShowTag));

            }
        });

        //Create Button
        mCreateButton = (Button) v.findViewById(R.id.row1Button2);
        mCreateButton.setText(R.string.create_button_label);
        //the order of images here is left, top, right, bottom
        mCreateButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the collect with maps fragment.
                // But the switching happens on the container Activity
                ((MainPrism4DActivity)getActivity()).switchToPointCreateScreen(mProject);
            }
        });


        //copy Button
        mCopyButton = (Button) v.findViewById(R.id.row1Button3);
        mCopyButton.setText(R.string.copy_button_label);
        mCopyButton.setBackgroundResource(R.color.colorGray);
        mCopyButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(getActivity(), "Can Not Copy Points at this time", Toast.LENGTH_SHORT).show();
                /*
                ((MainPrism4DActivity)getActivity()).switchToListPointsScreen(
                            mProject.getProjectID(),
                            new Prism4DPath(Prism4DPath.sCopyTag));
                 */
            }
        });

        //Edit Button
        mEditButton = (Button) v.findViewById(R.id.row2Button1);
        mEditButton.setText(R.string.edit_button_label);
        mEditButton.setBackgroundResource(R.color.colorGray);
        mEditButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ///Need to know which point to EDIT
                Toast.makeText(getActivity(),
                        R.string.edit_button_label,
                        Toast.LENGTH_SHORT).show();

                Prism4DPath pointPath = new Prism4DPath(Prism4DPath.sEditTag) ;
                ((MainPrism4DActivity)getActivity()).switchToListPointsScreen(
                                                            mProject.getProjectID(),pointPath);

            }
        });

        //Delete Button
        mDeleteButton = (Button) v.findViewById(R.id.row2Button2);
        mDeleteButton.setText(R.string.delete_button_label);
        mDeleteButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ((MainPrism4DActivity)getActivity()).switchToListPointsScreen(
                            mProject.getProjectID(),
                            new Prism4DPath(Prism4DPath.sDeleteTag));
            }
        });

        //6 Button
        m6Button = (Button) v.findViewById(R.id.row2Button3);
        m6Button.setText(R.string.unused_button_label);
        //m6Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
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
        //m7Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
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
        //m8Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
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
        //m9Button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_001_folders, 0, 0);
        m9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.unused_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

    }
}


