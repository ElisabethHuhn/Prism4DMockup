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

import java.util.ArrayList;
import java.util.List;

/**
 * The List Points Fragment is the UI
 * for the user to see points of a given project
 * Created by elisabethhuhn on 5/8/2016.
 */
public class MainPrism4DListPointsFragment extends Fragment {

    private static final String TAG = "LIST_POINTS_FRAGMENT";
    /**
     * Create variables for all the widgets
     *  although in the mockup, most will be statically defined in the xml
     */

    private List<Prism4DPoint> mPointList = new ArrayList<>();
    private List<Prism4DPoint> mProjectPointList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Prism4DPointAdapter mAdapter;



    private int          mProjectID;
    private CharSequence mProjectName;

    private int          mPointID;
    private double       mPointEasting;
    private double       mPointNorthing;
    private double       mPointElevation;
    private CharSequence mPointDescription;
    private CharSequence mNotes;

    private Prism4DPoint mSelectedPoint;
    private int          mSelectedPosition;

    private CharSequence mProjectPath;
    private CharSequence mPointPath;


    /**********************************************************/
    //          Fragment Lifecycle Functions                  //
    /**********************************************************/


    public static MainPrism4DListPointsFragment newInstance(
            Prism4DProject project,
            Prism4DPath projectPath,
            Prism4DPath pointPath){

        Bundle args = new Bundle();

        //It will be some work to make all of the data model serializable
        //so for now, just pass the point values
        //For now, the only thing to pass is the path type itself
        args.putInt         (Prism4DProject.sProjectIDTag,   project.getProjectID());
        args.putCharSequence(Prism4DProject.sProjectNameTag, project.getProjectName());
        args.putCharSequence(Prism4DPath.sProjectPathTag,    projectPath.getPath());
        args.putCharSequence(Prism4DPath.sPointPathTag,      pointPath.getPath());

        MainPrism4DListPointsFragment fragment = new MainPrism4DListPointsFragment();

        fragment.setArguments(args);
        return fragment;
    }

    //Constructor
    public MainPrism4DListPointsFragment() {
        //for now, we don't need to initialize anything when the fragment
        //  is first created
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        mProjectID   = getArguments().getInt         (Prism4DProject.sProjectIDTag);
        mProjectName = getArguments().getCharSequence(Prism4DProject.sProjectNameTag);
        mProjectPath = getArguments().getCharSequence(Prism4DPath.sProjectPathTag);
        mPointPath   = getArguments().getCharSequence(Prism4DPath.sPointPathTag);

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
         * 9) return the view
         */
        //1) Inflate the layout for this fragment
        View v = inflater.inflate
                (R.layout.fragment_point_list_prism4d, container, false);
        v.setTag(TAG);

        //2) find and remember the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.pointsList);


        // The RecyclerView.LayoutManager defines how elements are laid out.
        //3) create and assign a layout manager to the recycler view
        RecyclerView.LayoutManager mLayoutManager  = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //4) create some dummy data and tell the adapter about it
        //  this is done in the singleton container

        //      get our singleton list container
        Prism4DPointsManager pointsManager = Prism4DPointsManager.getInstance();
        //filter out points not in this project
        mPointList = pointsManager.getProjectPointsList(mProjectID);


        //5) Use the data to Create and set out points Adapter
        mAdapter = new Prism4DPointAdapter(mPointList);
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
                        //for now, ignore the long click
                    }
                }));

        //No FOOTER on this screen

        //9) return the view
        return v;
    }


    /**********************************************************/
    //      Utility Functions used in handling events         //
    /**********************************************************/

    //executed when an item in the list is selected
    private void onSelect(int position){
        mSelectedPosition = position;
        mSelectedPoint = mPointList.get(position);
        Toast.makeText(getActivity().getApplicationContext(),
                String.valueOf(mSelectedPoint.getPointID()) + " is selected!",
                Toast.LENGTH_SHORT).show();


        onEnter();

    }

    //executed when enter is selected
    private void onEnter(){
        //Point has to have been selected to do anything here
        MainPrism4DActivity myActivity = (MainPrism4DActivity) getActivity();
        if (myActivity != null){
            if (mSelectedPoint != null) {

                //We'll need to pass the path forward

                if (mPointPath.equals(Prism4DPath.sOpenTag)){

                    //if the path is open, open the selected point
                    myActivity.switchToMaintainPointScreen(
                            new Prism4DPath(mProjectPath),
                            mSelectedPoint,
                            new Prism4DPath(mPointPath));

                }else if (mPointPath.equals(Prism4DPath.sCopyTag)){

                    //if the path is copy,
                    // create a new point with the selected points details
                    // but it has to be in the same project as this one
                    Prism4DProject ourProject =
                            getOurProject(mSelectedPoint.getProjectID());

                    Prism4DPoint newPoint = new Prism4DPoint(ourProject);
                    newPoint.setPointEasting    (mSelectedPoint.getPointEasting());
                    newPoint.setPointNorthing   (mSelectedPoint.getPointNorthing());
                    newPoint.setPointElevation  (mSelectedPoint.getPointElevation());
                    newPoint.setPointDescription(mSelectedPoint.getPointDescription());
                    newPoint.setPointNotes      (mSelectedPoint.getPointNotes());

                    //then switch to point update with that new point
                    myActivity.switchToMaintainPointScreen(
                            new Prism4DPath(mProjectPath),
                            mSelectedPoint,
                            new Prism4DPath(mPointPath));

                }else if (mPointPath.equals(Prism4DPath.sDeleteTag)){

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
                //user hasn't selected anything yet
                //for now, just put up a toast that nothing has been pressed yet
                Toast.makeText(getActivity(),
                        R.string.point_no_selection,
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

                        CharSequence message =
                                        "Point " +
                                        String.valueOf(mSelectedPoint.getPointID()) +
                                        " is deleted";

                        Prism4DPointAdapter myAdapter =
                                (Prism4DPointAdapter) mRecyclerView.getAdapter();

                        myAdapter.removeItem(mSelectedPosition);

                        Toast.makeText(getActivity(),
                                message,
                                Toast.LENGTH_SHORT).show();

                        //delete the point and return to list points
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

    private Prism4DProject getOurProject(int projectID){
        //ultimately, this needs to look up in the DB
        return new Prism4DProject(
          getResources().getString(R.string.dummy_project_name),
          mProjectID );
    }

     //Add some code to improve the recycler view
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainPrism4DListPointsFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context,
                                     final RecyclerView recyclerView,
                                     final MainPrism4DListPointsFragment.ClickListener
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


