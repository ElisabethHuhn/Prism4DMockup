package com.asc.msigeosystems.prism4d;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

import java.util.List;

/**
 * The List Project Fragment is the UI
 * when the user can see the projects residing on this handset
 * Created by elisabethhuhn on 5/15/2016.
 */
public class MainPrism4DListProjectsFragment extends Fragment {

    private static final String TAG = "LIST_PROJECTS_FRAGMENT";
    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private List<Prism4DProject> mProjectList ;
    private RecyclerView mRecyclerView;
    private Prism4DProjectAdapter mAdapter;

    private Prism4DProject mSelectedProject;
    private int            mSelectedPosition;

    private CharSequence   mProjectPath;


    /**********************************************************/
    //          Fragment Lifecycle Functions                  //
    /**********************************************************/

    //this is called by Activity to store parameters before fragment is instantiated
    public static MainPrism4DListProjectsFragment newInstance(Prism4DPath projectPath){

        Bundle args = new Bundle();

        //It will be some work to make all of the data model serializable
        //so for now, just pass the project values
        //For now, the only thing to pass is the path type itself
        args.putCharSequence(Prism4DPath.sProjectPathTag,   projectPath.getPath());

        MainPrism4DListProjectsFragment fragment = new MainPrism4DListProjectsFragment();

        fragment.setArguments(args);
        return fragment;
    }

    //Constructor
    public MainPrism4DListProjectsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    //This is where parameters are unbundled
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        mProjectPath = getArguments().getCharSequence(Prism4DPath.sProjectPathTag);

        //This would be the place to do anything unique to a given path

    }


    //set up the recycler view
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
         * 9) return the view
         */
        //1) Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_project_list_prism4d, container, false);
        v.setTag(TAG);

        //2) find and remember the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.projectsList);

        //3) create and assign a layout manager to the recycler view
        RecyclerView.LayoutManager mLayoutManager  = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //4) read data in from the database and tell the adapter about it
        //   this is now done in the projects container singleton

        //      get the singleton list container
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        //      then go get our list of projects
        mProjectList = projectManager.getProjects();

        //5) Use the data to Create and set out project Adapter
        mAdapter = new Prism4DProjectAdapter(mProjectList);
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
                       onSelect(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        //No FOOTER on this screen

        //9) return the view
        return v;
    }

    /**********************************************************/
    //      Utility Functions used in handling events         //
    /**********************************************************/

    //called from onClick(), executed when a project is selected
    private void onSelect(int position){
        //todo need to update selection visually
        mSelectedPosition = position;
        // TODO: 10/3/2016 Need to query list in Project Manager or Adapter, not locally 
        mSelectedProject = mProjectList.get(position);

        Toast.makeText(getActivity().getApplicationContext(),
                mSelectedProject.getProjectName() + " is selected!",
                Toast.LENGTH_SHORT).show();

        MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
        if (myActivity != null){
            if (mSelectedProject != null) {

                //We'll need to pass the path forward
                Prism4DPath copiedProjectPath = new Prism4DPath(mProjectPath);

                if (mProjectPath.equals(Prism4DPath.sOpenTag)){

                    //if the path is open, open the selected project
                    myActivity.switchToProject14UpdateScreen(
                            mSelectedProject,
                            copiedProjectPath);

                }else if (mProjectPath.equals(Prism4DPath.sCopyTag)){

                    //if the path is copy, create a new project
                    // with the selected projects details
                    Prism4DProject copiedProject = new Prism4DProject(
                            mSelectedProject.getProjectName(),
                            Prism4DProject.getNextProjectID()  );
                    copiedProject.setProjectDescription(
                            mSelectedProject.getProjectDescription());

                    //copy the points from the selected project
                    // to the newly created project
                    Prism4DPointsManager pointsManager =
                            Prism4DPointsManager.getInstance();
                    pointsManager.copyProjectPoints(
                            mSelectedProject.getProjectID(),
                            copiedProject.getProjectID());

                    //then switch to project update with that new project
                    myActivity.switchToProject14UpdateScreen(
                            copiedProject,
                            copiedProjectPath);

                }else if (mProjectPath.equals(Prism4DPath.sDeleteTag)){

                    //ask the user if they are sure they want to proceed.
                    areYouSureDelete();
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

    //Build and display the alert dialog
    private void areYouSureDelete(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_title)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.continue_delete_dont_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        Prism4DProjectAdapter myAdapter = (Prism4DProjectAdapter) mRecyclerView.getAdapter();
                        myAdapter.removeItem(mSelectedPosition);

                        CharSequence message =
                                "Project " + mSelectedProject.getProjectName() + " is deleted";
                        Toast.makeText(getActivity(),
                                message,
                                Toast.LENGTH_SHORT).show();

                        MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
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


    //Add some code to improve the recycler view
    //Here is the interface for event handlers for Click and LongClick
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainPrism4DListProjectsFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context,
                                     final RecyclerView recyclerView,
                                     final MainPrism4DListProjectsFragment.ClickListener
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


