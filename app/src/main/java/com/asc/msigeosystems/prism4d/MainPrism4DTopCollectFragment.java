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
 * The Project Fragment is the UI
 * when the user is creating / making changes to the project definition
 * Created by elisabethhuhn on 4/13/2016.
 */
public class MainPrism4DTopCollectFragment extends Fragment {

    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private TextView mScreenLabel;

    //Matrix Buttons
    private Button mPointsButton;
    private Button mLinesButton;
    private Button mAreasButton;

    private Button mAutoStoreButton;
    private Button mTraverseButton;
    private Button mResectionButton;

    private Button mMonitorButton;
    private Button mScanButton;
    private Button mLevelButton;


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


    public MainPrism4DTopCollectFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_matrix_prism4d, container, false);


        //Wire up the UI widgets so they can handle events later
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
        ((MainPrism4DActivity) getActivity()).switchSubtitle(R.string.subtitle_collect);
    }

    private void wireWidgets(View v){
        //Tell the user which project is open
        mScreenLabel = (TextView) v.findViewById(R.id.matrix_screen_label);
        Prism4DConstantsAndUtilities constantsAndUtilities =
                                                        Prism4DConstantsAndUtilities.getInstance();
        mScreenLabel.setText(constantsAndUtilities.getOpenProjectIDMessage(getActivity()));
        int color = ContextCompat.getColor(getActivity(), R.color.colorWhite);
        mScreenLabel.setBackgroundColor(color);


        //Measure Points Button
        mPointsButton = (Button) v.findViewById(R.id.row1Button1);
        mPointsButton.setText(R.string.collect_points_button_label);
        //the order of images here is left, top, right, bottom
        mPointsButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_211_dcpoints, 0, 0);
        mPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Location source must also be defined
                // TODO: 12/2/2016 define how location source will be identified in configurations
                //Can only collect points if a project is open
                MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();

                Prism4DConstantsAndUtilities constantsAndUtilities =
                                                         Prism4DConstantsAndUtilities.getInstance();

                Prism4DProject openProject = constantsAndUtilities.getOpenProject();
                if (openProject != null) {
                    //Switch the fragment to the collect with maps fragment.
                    // But the switching happens on the container Activity
                    myActivity.switchToCollectPointsScreen();
                } else {
                    //Tell the user a project must be open
                    Toast.makeText(getActivity(),
                                   R.string.collect_project_must_be_open,
                                   Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        //Measure Lines Button
        mLinesButton = (Button) v.findViewById(R.id.row1Button2);
        mLinesButton.setText(R.string.collect_lines_button_label);
        mLinesButton.setBackgroundResource(R.color.colorGray);
        //the order of images here is left, top, right, bottom
        mLinesButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_212_dclines, 0, 0);
        mLinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.collect_lines_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Measure Areas Button
        mAreasButton = (Button) v.findViewById(R.id.row1Button3);
        mAreasButton.setText(R.string.collect_areas_button_label);
        mAreasButton.setBackgroundResource(R.color.colorGray);
        mAreasButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_213_dcareas, 0, 0);
        mAreasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.collect_areas_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Autostore Button
        mAutoStoreButton = (Button) v.findViewById(R.id.row2Button1);
        mAutoStoreButton.setText(R.string.autostore_button_label);
        mAutoStoreButton.setBackgroundResource(R.color.colorGray);
        mAutoStoreButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_214_dcauto, 0, 0);
        mAutoStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.autostore_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });


        //Traverse Button
        mTraverseButton = (Button) v.findViewById(R.id.row2Button2);
        mTraverseButton.setText(R.string.traverse_button_label);
        mTraverseButton.setBackgroundResource(R.color.colorGray);
        mTraverseButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_215_dctraverse, 0, 0);
        mTraverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.traverse_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Resection Button
        mResectionButton = (Button) v.findViewById(R.id.row2Button3);
        mResectionButton.setText(R.string.resection_button_label);
        mResectionButton.setBackgroundResource(R.color.colorGray);
        mResectionButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_216_dcresection, 0, 0);
        mResectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.resection_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Monitor Button
        mMonitorButton = (Button) v.findViewById(R.id.row3Button1);
        mMonitorButton.setText(R.string.monitor_button_label);
        mMonitorButton.setBackgroundResource(R.color.colorGray);
        mMonitorButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_217_dcmonitor, 0, 0);
        mMonitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.monitor_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Scan Button
        mScanButton = (Button) v.findViewById(R.id.row3Button2);
        mScanButton.setText(R.string.scan_button_label);
        mScanButton.setBackgroundResource(R.color.colorGray);
        mScanButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_218_dcscan, 0, 0);
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.scan_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

        //Level Button
        mLevelButton = (Button) v.findViewById(R.id.row3Button3);
        mLevelButton.setText(R.string.level_button_label);
        mLevelButton.setBackgroundResource(R.color.colorGray);
        mLevelButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_219_dclevel, 0, 0);
        mLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ///for now, just put up a toast that the button was pressed
                Toast.makeText(getActivity(),
                        R.string.level_button_label,
                        Toast.LENGTH_SHORT).show();

            }
        });

    }
}


