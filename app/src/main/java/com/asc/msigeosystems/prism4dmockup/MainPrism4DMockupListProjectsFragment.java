package com.asc.msigeosystems.prism4dmockup;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Project Open Fragment is the UI
 * when the user is making point measurements in the field
 * Created by elisabethhuhn on 5/8/2016.
 */
public class MainPrism4DMockupListProjectsFragment extends Fragment {

    private static final String TAG = "Project12Open_RecyclerViewFragment";
    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private List<Prism4DMockupProject> projectList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProjectAdapter mAdapter;




    private String mProjectName;
    private String mProjectID;
    private Date   mProjectDate;
    private String mProjectDescription;

    private Prism4DMockupProject mSelectedProject;
    private CharSequence         mPath;



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

    public MainPrism4DMockupListProjectsFragment newInstance(Prism4DMockupPath path){

        Bundle args = new Bundle();

        //It will be some work to make all of the data model serializable
        //so for now, just pass the project values
        //For now, the only thing to pass is the path type itself
        args.putCharSequence(Prism4DMockupPath.sPathTag,   path.getPath());

        MainPrism4DMockupListProjectsFragment fragment =
                new MainPrism4DMockupListProjectsFragment();

        fragment.setArguments(args);
        return fragment;
    }

    //Constructor
    public MainPrism4DMockupListProjectsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        CharSequence path = getArguments().getCharSequence(Prism4DMockupPath.sPathTag);
        mPath = path;

        //This would be the place to do anything unique to a given path

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
         * The steps for doing recycler view in onCreateView() of a fragment are:
         * 1) inflate the .xml
         *
         * the special recycler view stuff is:
         * 2) get and store a reference to the recycler view widget that you created in xml
         * 3) create and assign a layout manager to the recycler view
         * 4) assure that there is data for the recycler view to show.
         * 5) use the data to create and set an adapter in the recycler view
         * 6) create and set an item animator (if desired)
         * 7) create and set a line item decorator
         * 8) add event listeners to the recycler view
         *
         * 8) return the view
         */
        //1) Inflate the layout for this fragment
        View v = inflater.inflate
                (R.layout.fragment_project_1_2_open_prism4_dmockup, container, false);
        v.setTag(TAG);

        //Even though this is a mockup, use the real recycler view to assure
        //that I understand it for the real project

        //2) find and remember the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.projectsList);


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        //3) create and assign a layout manager to the recycler view
        RecyclerView.LayoutManager mLayoutManager  = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //4) create some dummy data and tell the adapter about it
        prepareProjectDataset();

        //5) Use the data to Create and set the Adapter
        mAdapter = new ProjectAdapter(projectList);
        mRecyclerView.setAdapter(mAdapter);



        //6) create and set the itemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //7) create and add the item decorator
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), LinearLayoutManager.VERTICAL));


        //8) add event listeners to the recycler view
        mRecyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {

                    @Override
                    public void onClick(View view, int position) {
                        mSelectedProject = projectList.get(position);
                        Toast.makeText(getActivity().getApplicationContext(),
                                mSelectedProject.getProjectName() + " is selected!",
                                Toast.LENGTH_SHORT).show();

                        //also enable the Enter button
                        mEnterButton.setEnabled(true);
                        mEnterButton.setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));



        //FOOTER WIDGETS

        //The footer buttons are not enabled initially, as the main event on this page
        //is selecting a project

        //Esc Button
        mEscButton = (Button) v.findViewById(R.id.escButton);
        //Enter button is disabled until a project has been selected
        //have to set the color and enable the button as the default is NOT enabled/grayed out
        //mEscButton.setEnabled(true);
        //mEscButton.setTextColor(Color.BLACK);
        mEscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                MainPrism4DMockupActivity myActivity = (MainPrism4DMockupActivity) getActivity();
                if (myActivity != null){
                    myActivity.popToProject1Screen();
                }

            }
        });


        //Enter Button
        mEnterButton = (Button) v.findViewById(R.id.enterButton);
        //The Enter button is enabled with the first project selection
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //Project has to have been selected to do anything here
                MainPrism4DMockupActivity myActivity = (MainPrism4DMockupActivity) getActivity();
                if (myActivity != null){
                    if (mSelectedProject != null) {

                        //We'll need to pass the path forward
                        Prism4DMockupPath newPath = new Prism4DMockupPath(mPath);

                        if (mPath.equals(Prism4DMockupPath.sOpenTag)){

                            //if the path is open, open the selected project
                            myActivity.switchToProject14UpdateScreen(mSelectedProject, newPath);

                        }else if (mPath.equals(Prism4DMockupPath.sCopyTag)){

                            //if the path is copy, create a new project with the selected projects details
                            Prism4DMockupProject newProject = new Prism4DMockupProject(
                                    mSelectedProject.getProjectName(),
                                    Prism4DMockupProject.sProjectNewID  );
                            newProject.setProjectDescription(mSelectedProject.getProjectDescription());

                            //then switch to project update with that new project
                            myActivity.switchToProject14UpdateScreen(newProject, newPath);

                        }else if (mPath.equals(Prism4DMockupPath.sDeleteTag)){
                            CharSequence message =
                                    "Project " + mSelectedProject.getProjectName() + " is deleted";
                            Toast.makeText(getActivity(),
                                    message,
                                    Toast.LENGTH_SHORT).show();

                            myActivity.popToProject1Screen();
                        }else {

                            //todo need to throw an unrecognized path exception
                            Toast.makeText(getActivity(),
                                    R.string.unrecognized_path_encountered,
                                    Toast.LENGTH_SHORT).show();

                            //for now, go home
                            myActivity.switchToHomeScreen();

                        }


                    } else {
                        //for now, just put up a toast that nothing has been pressed yet
                        Toast.makeText(getActivity(),
                                R.string.project_no_selection,
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        return v;
    }

    //Mock up some projects for now
    private void prepareProjectDataset(){
        //no use doing anything if the Adapter is not created yet


        Prism4DMockupProject project = new Prism4DMockupProject("Cambridge Subdivision", 1001);
        projectList.add(project);

        project = new Prism4DMockupProject("Jones Creek",   1002);
        projectList.add(project);

        project = new Prism4DMockupProject("Hampton South", 1003);
        projectList.add(project);

        project = new Prism4DMockupProject("Johns Creek",   1004);
        projectList.add(project);

        project = new Prism4DMockupProject("Macon Airport", 1005);
        projectList.add(project);

        project = new Prism4DMockupProject("MSI Demo",      1006);
        projectList.add(project);

        project = new Prism4DMockupProject("Roswell",       1007);
        projectList.add(project);



    }

    //Add some code to improve the recycler view
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainPrism4DMockupListProjectsFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context,
                                     final RecyclerView recyclerView,
                                     final MainPrism4DMockupListProjectsFragment.ClickListener
                                             clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}


