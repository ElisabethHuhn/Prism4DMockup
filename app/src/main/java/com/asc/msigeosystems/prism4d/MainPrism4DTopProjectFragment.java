package com.asc.msigeosystems.prism4d;

import android.content.Intent;
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

import java.util.ArrayList;

import static android.R.id.message;

/**
 * The Project Fragment is the UI
 * when the user is creating / making changes to the project definition
 *
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DTopProjectFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *
     */

    //Screen Label, used on this screen to display the open project
    private TextView mScreenLabel;

    //Matrix Buttons
    private Button mCreateButton;
    private Button mOpenButton;
    private Button mCopyButton;

    private Button mEditButton;
    private Button mDeleteButton;
    private Button mControlButton;

    private Button mListPointsButton;
    private Button mFeatureCodesButton;
    private Button mExchangeButton;



    /***********************************************************************/
    /**********   Member Variables  ****************************************/
    /***********************************************************************/

    private int mOpenProject;


    public MainPrism4DTopProjectFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
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
        ((MainPrism4DActivity) getActivity()).switchSubtitle(R.string.subtitle_projects);
    }



    private void wireWidgets(View v){
        //Tell the user which project is open
        mScreenLabel = (TextView) v.findViewById(R.id.matrix_screen_label);
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                     Prism4DConstantsAndUtilities.getInstance();
        mScreenLabel.setText(constantsAndUtilities.getOpenProjectIDMessage(getActivity()));
        int color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        mScreenLabel.setBackgroundColor(color);



        //Create Button
        mCreateButton = (Button) v.findViewById(R.id.row1Button1);
        mCreateButton.setText(R.string.create_button_label);
        //the order of images here is left, top, right, bottom
        mCreateButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_111_createfolder, 0, 0);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch the fragment to the collect with maps fragment.
                // But the switching happens on the container Activity
                ((MainPrism4DActivity)getActivity()).switchToProjectCreateScreen();


            }
        });

        //open Button
        mOpenButton = (Button) v.findViewById(R.id.row1Button2);
        mOpenButton.setText(R.string.open_button_label);
        //the order of images here is left, top, right, bottom
        mOpenButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_112_openfolder, 0, 0);
        mOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPrism4DActivity)getActivity()).switchToProjectOpenScreen();


            }
        });


        //copy Button
        mCopyButton = (Button) v.findViewById(R.id.row1Button3);
        mCopyButton.setText(R.string.copy_button_label);
        mCopyButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_113_copyfolder, 0, 0);
        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((MainPrism4DActivity)getActivity()).switchToProjectCopyScreen();

            }
        });

        //Edit Button
        mEditButton = (Button) v.findViewById(R.id.row2Button1);
        mEditButton.setEnabled(true);
        mEditButton.setText(R.string.edit_button_label);
        mEditButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_114_editfolder, 0, 0);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(getActivity(),
                        R.string.edit_button_label,
                        Toast.LENGTH_SHORT).show();

                ((MainPrism4DActivity)getActivity()).switchToProjectEditScreen();


            }
        });

        //Delete Button
        mDeleteButton = (Button) v.findViewById(R.id.row2Button2);
        mDeleteButton.setText(R.string.delete_button_label);
        mDeleteButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_115_deletefolder, 0, 0);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ((MainPrism4DActivity)getActivity()).switchToProjectDeleteScreen();
            }
        });


        //Control Button
        mControlButton = (Button) v.findViewById(R.id.row2Button3);
        mControlButton.setText(R.string.control_button_label);
        mControlButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_116_controlfile, 0, 0);
        mControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.control_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //List Points Button
        mListPointsButton = (Button) v.findViewById(R.id.row3Button1);
        mListPointsButton.setText(R.string.list_points_button_label);
        mListPointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_117_pointsfile, 0, 0);
        mListPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();

                Prism4DProject openProject = constantsAndUtilities.getOpenProject();
                if (openProject != null){
                    myActivity.switchToListPointsScreen(openProject,
                            new Prism4DPath(Prism4DPath.sShowTag));
                }

                Toast.makeText(getActivity(),
                        R.string.project_not_open_to_list_points,
                        Toast.LENGTH_LONG).show();
            }
        });

        //Feature Codes Button
        mFeatureCodesButton = (Button) v.findViewById(R.id.row3Button2);
        mFeatureCodesButton.setText(R.string.feature_codes_button_label);
        mFeatureCodesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_118_fcfile, 0, 0);
        mFeatureCodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.feature_codes_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Exchange Button
        mExchangeButton = (Button) v.findViewById(R.id.row3Button3);
        mExchangeButton.setText(R.string.exchange_button_label);
        mExchangeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_119_exchangefolder, 0, 0);
        mExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                /*
                Toast.makeText(getActivity(),
                        R.string.exchange_button_label,
                        Toast.LENGTH_SHORT).show();
                */
                onExchange();

            }
        });



        //FOOTER WIDGETS

    }

    private void onExchange(){
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();
        Prism4DProject openProject = constantsAndUtilities.getOpenProject();
        if (openProject == null){
            Toast.makeText(getActivity(),R.string.no_project_open,Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList pointsList = openProject.getPoints();
        if (pointsList == null){
            Toast.makeText(getActivity(),R.string.no_points_in_project,Toast.LENGTH_SHORT).show();
            return;
        }

        int last = pointsList.size();
        if (last == 0){
            Toast.makeText(getActivity(),R.string.no_points_in_project,Toast.LENGTH_SHORT).show();
            return;
        }

        String subject = getString(R.string.exchange_subject) + " " + openProject.getProjectName();
        String message = getString(R.string.email_message_prologue_1) +
                         " "+ openProject.getProjectName() + " "+
                         getString(R.string.email_message_prologue_2)+
                         System.getProperty("line.separator")+
                         System.getProperty("line.separator")+
                         System.getProperty("line.separator");

        message = message + openProject.convertToCDF() ;
        message = message +
                    System.getProperty("line.separator")+
                    System.getProperty("line.separator")+
                    System.getProperty("line.separator");

        Prism4DPoint point;


        for (int i = 0; i < last; i++) {
            point = (Prism4DPoint) pointsList.get(i);
            message = message + point.convertToCDF();
        }

        Intent emailApp = new Intent(Intent.ACTION_SEND);
        emailApp.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailApp.putExtra(Intent.EXTRA_TEXT, message);
        emailApp.setType("message/rfc822");
        startActivity(Intent.createChooser(emailApp, "Send Email Via"));
    }
}


