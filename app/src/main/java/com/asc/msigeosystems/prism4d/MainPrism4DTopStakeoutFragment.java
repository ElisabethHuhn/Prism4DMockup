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
 * The Stakeout Fragment is the top level selection UI
 * for stakeout functions
 * Created by Elisabeth Huhn on 4/13/2016.
 */
public class MainPrism4DTopStakeoutFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */


    private TextView mScreenLabel;

    //Matrix Buttons
    private Button mPointsButton;
    private Button mLinesButton;
    private Button mArcButton;

    private Button mOffsetsButton;
    private Button mGridsButton;
    private Button mDTMsButton;

    private Button mAllignmentsButton;
    private Button mSectionsButton;
    private Button mReportButton;




    public MainPrism4DTopStakeoutFragment() {
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
        ((MainPrism4DActivity) getActivity()).switchSubtitle(R.string.subtitle_stakeout);
    }



    private void wireWidgets(View v){
        //Tell the user which project is open
        mScreenLabel = (TextView) v.findViewById(R.id.matrix_screen_label);
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                     Prism4DConstantsAndUtilities.getInstance();
        mScreenLabel.setText(constantsAndUtilities.getOpenProjectIDMessage(getActivity()));
        int color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        mScreenLabel.setBackgroundColor(color);



        //Stake Points Button
        mPointsButton = (Button) v.findViewById(R.id.row1Button1);
        mPointsButton.setText(R.string.stakeout_points_button_label);
        //the order of images here is left, top, right, bottom
        mPointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_311_sopoints, 0, 0);
        mPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_points_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });




        //Measure Lines Button
        mLinesButton = (Button) v.findViewById(R.id.row1Button2);
        mLinesButton.setText(R.string.stakeout_lines_button_label);
        //the order of images here is left, top, right, bottom
        mLinesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_312_solines, 0, 0);
        mLinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_lines_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Arcs Button
        mArcButton = (Button) v.findViewById(R.id.row1Button3);
        mArcButton.setText(R.string.stakeout_arcs_button_label);
        mArcButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_313_socurves, 0, 0);
        mArcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_arcs_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Offset Button
        mOffsetsButton = (Button) v.findViewById(R.id.row2Button1);
        mOffsetsButton.setText(R.string.stakeout_offset_button_label);
        mOffsetsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_314_sooffsets, 0, 0);
        mOffsetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_offset_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Grids
        mGridsButton = (Button) v.findViewById(R.id.row2Button2);
        mGridsButton.setText(R.string.stakeout_grids_button_label);
        mGridsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_315_sogrids, 0, 0);
        mGridsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_grids_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //DTMs Button
        mDTMsButton = (Button) v.findViewById(R.id.row2Button3);
        mDTMsButton.setText(R.string.stakeout_dtms_button_label);
        mDTMsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_316_sodtms, 0, 0);
        mDTMsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_dtms_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Allignment Button
        mAllignmentsButton = (Button) v.findViewById(R.id.row3Button1);
        mAllignmentsButton.setText(R.string.stakeout_alignments_button_label);
        mAllignmentsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_317_soalignments, 0, 0);
        mAllignmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_alignments_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Sections Button
        mSectionsButton = (Button) v.findViewById(R.id.row3Button2);
        mSectionsButton.setText(R.string.stakeout_sections_button_label);
        mSectionsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_318_sosections, 0, 0);
        mSectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_sections_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });



        //Stakeout Report Button
        mReportButton = (Button) v.findViewById(R.id.row3Button3);
        mReportButton.setText(R.string.stakeout_report_button_label);
        mReportButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_319_soreports, 0, 0);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.stakeout_report_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });



        //FOOTER WIDGETS

        //  Esc and Enter buttons are NOT enabled on the collect screen
        //so we can ignore the footer for now

    }
}


