package com.asc.msigeosystems.prism4dmockup;

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
 * The Collect Fragment is the UI
 * when the user is making point measurements in the field
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DProject14EditFragment extends Fragment {

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

    private TextView mProjectDescLabel;
    private EditText mProjectDescInput;

    private Button mProjectViewSettingsButton;
    private Button mProjectViewExistingButton;

    private String mProjectName;
    private String mProjectID;
    private String mProjectDate;
    private String mProjectDescription;






    public MainPrism4DProject14EditFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_project_1_n_maintain_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later
        //For now ignore the text view widgets, as this is just a mockup
        //      for the real screen we'll have to actually fill the fields

        //Project Name
        mProjectNameLabel = (TextView) v.findViewById(R.id.projectNameLabel);
        mProjectNameInput = (EditText) v.findViewById(R.id.projectNameInput);

        //Project ID
        mProjectIDLabel = (TextView)v.findViewById(R.id.projectIDLabel);
        mProjectIDInput = (EditText) v.findViewById(R.id.projectIDInput);

        //Project Date
        mProjectDateLabel = (TextView) v.findViewById(R.id.projectCreationDateLabel);
        mProjectDateInput = (EditText) v.findViewById(R.id.projectCreationDateInput);

        //Project Description
        mProjectDescLabel = (TextView)v.findViewById(R.id.projectDescLabel);
        mProjectDescInput = (EditText) v.findViewById(R.id.projectDescInput);


        setDefaults();


        //View Settings Button
        mProjectViewSettingsButton = (Button) v.findViewById(R.id.projectViewSettingsButton);
        mProjectViewSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
                if (myActivity != null){
                    //myActivity.switchToProjectSettingsScreen();
                }
            }
        });

        //View Existing Projects Button
        mProjectViewExistingButton = (Button) v.findViewById(R.id.projectViewExistingButton);
        mProjectViewExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.project_view_existing_label,
                        Toast.LENGTH_SHORT).show();
           }
        });

        //FOOTER WIDGETS



        return v;
    }

    private void setDefaults(){
        mProjectName = "Cambridge Bridge Survey & Layout";
        mProjectID   = "MSI_26021008";
        mProjectDate = "04/16/2016";
        mProjectDescription = "Sargent Road Reconnaissance Survey. "+
                "Party Chief: Rusty Martin, GPS Rodman: Chip Schuller ";

        mProjectNameInput.setText(mProjectName);
        mProjectIDInput.setText(mProjectID);
        mProjectDateInput.setText(mProjectDate);
        mProjectDescInput.setText(mProjectDescription);

    }
}


