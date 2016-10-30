package com.asc.msigeosystems.prism4d;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

import static com.asc.msigeosystems.prism4d.Prism4DPath.sDeleteTag;
import static com.asc.msigeosystems.prism4dmockup.R.string.action_cogo;
import static com.asc.msigeosystems.prism4dmockup.R.string.action_config;
import static com.asc.msigeosystems.prism4dmockup.R.string.action_global_settings;
import static com.asc.msigeosystems.prism4dmockup.R.string.action_settings;
import static com.asc.msigeosystems.prism4dmockup.R.string.action_skyplots;
import static com.asc.msigeosystems.prism4dmockup.R.string.subtitle_convert_to_utm;
import static com.asc.msigeosystems.prism4dmockup.R.string.subtitle_gps_nmea;
import static com.asc.msigeosystems.prism4dmockup.R.string.subtitle_list_nmea;
import static com.asc.msigeosystems.prism4dmockup.R.string.subtitle_list_satellites;
import static com.asc.msigeosystems.prism4dmockup.R.string.subtitle_maps;
import static com.asc.msigeosystems.prism4dmockup.R.string.subtitle_support;
import static com.asc.msigeosystems.prism4dmockup.R.string.subtitle_workflow;

//used to extend
public class MainPrism4DActivity extends AppCompatActivity {

    //DEFINE constants / literals
    public static final int MY_PERMISSIONS_REQUEST_COURSE_LOCATIONS = 1;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATIONS = 2;


    private static final String sHomeTag               = "HOME";//HOME screen fragment

    private static final String sProjectTopTag         = "PROJECT_TOP";
    private static final String sProjectCreateTag      = "PROJECT_CREATE_TOP";
    private static final String sProjectOpenTag        = "PROJECT_OPEN";
    //private static final String sProjectCopyTag        = "PROJECT_COPY";
    //private static final String sProjectEditTag        = "PROJECT_EDIT";
    private static final String sProjectUpdateTag      = "PROJECT_UPDATE";
    private static final String sProjectDeleteTag      = "PROJECT_DELETE";
    private static final String sProjectSettingsTag    = "PROJECT_SETTINGS";

    private static final String sPointTopTag         = "POINT_TOP";
    private static final String sPointCreateTag      = "POINT_CREATE_TOP";
    private static final String sPointOpenTag        = "POINT_OPEN";
    private static final String sPointCopyTag        = "POINT_COPY";
    //private static final String sPointEditTag        = "POINT_EDIT";
    private static final String sPointUpdateTag      = "POINT_UPDATE";
    //private static final String sPointDeleteTag      = "POINT_DELETE";
    //private static final String sPointSettingsTag    = "POINT_SETTINGS";

    private static final String sCollectTopTag         = "COLLECT_TOP";
    private static final String sCollectPointsTag      = "COLLECT_POINTS";
    private static final String sCollectPointsMapTag   = "COLLECT_POINTS_MAP";

    private static final String sStakeoutTopTag        = "STAKEOUT_TOP";

    private static final String sCogoTopTag            = "COGO_TOP";
    //private static final String sCogoCnversionTag      = "COGO_CONVERSION";
    private static final String sCogoWorkflowTag       = "COGO_WORKFLOW";

    private static final String sMapsTopTag            = "MAPS_TOP";

    private static final String sSkyplotTopTag         = "SKYPLOT_TOP";
    private static final String sSkyplotListNmeaTag    = "SKYPLOT_LIST_NMEA";
    private static final String sSkyplotListSatelliteTag = "SKYPLOT_LIST_SATELLITE";
    private static final String sSkyplotGpsNmeaTag     = "SKYPLOT_GPS_NMEA";

    private static final String sConfigTopTag          = "CONFIG_TOP";

    private static final String sSettingsTopTag        = "SETTINGS_TOP";
    private static final String sSettingsGlobalTag     = "SETTINGS_GLOBAL";
    private static final String sSettingsProjectDefaultTag = "SETTINGS_PROJECT_DEFAULT";

    private static final String sSupportTopTag        = "SUPPORT_TOP";

    private static final String sConversionTag         = "CONVERSION";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prism4_dmockup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //set up fragments
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            //when we first create the activity,
            // the fragment needs to be the home screen
            fragment = new MainPrism4DHomeFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


        //Put Home on the title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(R.string.action_home);
        }
        GpsStuff();

        //open the database here for the whole application
        //new Prism4DDatabaseAccess.OpenPrism4DDB().execute(getApplicationContext());

/****************** For now, comment out the floating action button
 FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
 fab.setOnClickListener(new View.OnClickListener() {
@Override public void onClick(View view) {
Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
.setAction("Action", null).show();
}
});
 ****************/
    }

    private void GpsStuff() {

        //make sure we have GPS permissions
        //check for permission to continue
        int permissionCheckCourse = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheckFine = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        //If we don't currently have permission, we have to ask for it
        if (permissionCheckCourse != PackageManager.PERMISSION_GRANTED){
            //find out if we need to explain to the user why we need GPS
/*******
            if (
                    //shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                 false){
                //// TODO: 9/5/2016 need to add code if GPS is off
                //tell the user why GPS is required
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
 *******/
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MainPrism4DActivity.MY_PERMISSIONS_REQUEST_COURSE_LOCATIONS);

                // MY_PERMISSIONS_REQUEST_COURSE_LOCATIONS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            //}
        }

        //If we don't currently have permission, we have to ask for it
        if (permissionCheckFine != PackageManager.PERMISSION_GRANTED) {
            //find out if we need to explain to the user why we need GPS
/*********
            if (false) {
                //shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){

                // TODO: 9/5/2016 so write the code to do this
                //tell the user why GPS is required
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
*******/
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MainPrism4DActivity.MY_PERMISSIONS_REQUEST_FINE_LOCATIONS);

                // MY_PERMISSIONS_REQUEST_FINE_LOCATIONS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            //}
            //So now signup for the GpsStatus.NmeaListener

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_COURSE_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                // TODO: 9/5/2016 Build in this functionality
/*******
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
 *****/
            }
            case MY_PERMISSIONS_REQUEST_FINE_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                // TODO: 9/5/2016  fill this in
/********
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
********/
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_prism4d, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //Skipping out on lower level processes is probably not a good idea
        //for now, disallow any navigating on menu except home
        switch (item.getItemId()){
            case R.id.action_home :
                switchToHomeScreen();
                return true;
/***
            case  R.id.action_project:
                switchToProject1Screen();
                return true;

            case R.id.action_collect:
                switchToCollect1Screen();
                return true;

            case  R.id.action_stakeout:
                switchToStakeout1Screen();
                return true;

            case R.id.action_cogo:
                switchToCogo1Screen();
                return true;

            case R.id.action_maps:
                Toast.makeText(MainPrism4DActivity.this,
                        R.string.action_maps,
                        Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_skyplots:
                switchToSkyplot1Screen();
                return true;

            case R.id.action_config:
                switchToConfig1Screen();
                return true;

            case R.id.action_settings:
                switchToSettings1Screen();
                return true;

            case R.id.action_help:
                Toast.makeText(MainPrism4DActivity.this,
                        R.string.action_help,
                        Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_convert:
                switchToConvertScreen();
                return true;
****/
            } //end switch



        return super.onOptionsItemSelected(item);
    }




    /**************************************************************************
     * The following methods switch the fragment being displayed by this activity
     * In the future, need to refactor. Seen BNR page 171 for suggestions
     *
     * Fragments are added with a TAG so that the stack can be popped later
     * EMH 4/24/2016
     */


    /******
     * Order of the screens is:
     * Home
     * Project
     * Collect
     * Stakeout
     * Cogo
     * Maps
     * Skyplot
     * Configuration
     * Settings
     ********/


    /*******************************************
     * Screen switching utilities
     *******************************************/




    /***
     * Switch to the fragment that is on the top of the backstack
     * EMH 4/27/2016
     */
    public void switchToPopBackstack(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //settings is at the top of the back stack, so pop it off
        fm.popBackStack();

    }




    /****
     * Method to switch fragment to home screen
     * EMH 4/20/16
     */
    private void switchScreen(Fragment toFragment, String tag) {
        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fromFragment = fm.findFragmentById(R.id.fragment_container);

        if (fromFragment == null) {
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fm.beginTransaction()
                    .add(R.id.fragment_container, toFragment, tag)
                    .commit();
        } else {
            //There is already a fragment being displayed
            fm.beginTransaction()
                    //replace whatever is being displayed with the Home fragment
                    .replace(R.id.fragment_container, toFragment, tag)
                    //and add the transaction to the back stack
                    .addToBackStack(tag)
                    .commit();
        }

    }




    //change the title bar subtitle
    public void switchSubtitle(int subTitle){

        //Put the name of the fragment on the title bar

        if (getSupportActionBar() != null){
            getSupportActionBar().setSubtitle(subTitle);
        }


    }

    //Overload the method with a String argument, rather than a Resource ID
    public void switchSubtitle(String subTitle){

        //Put the name of the fragment on the title bar

        if (getSupportActionBar() != null){
            getSupportActionBar().setSubtitle(subTitle);
        }


    }

    //clear the backstack
    public void clearBackstack() {
        //replace the fragment with the Home UI

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //clear the back stack
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }


        /*******************************************
         * Home
         *******************************************/



    /****
     * Method to switch fragment to home screen
     * EMH 4/20/16
     */
    public void switchToHomeScreen() {
        //replace the fragment with the Home UI
        clearBackstack();
        Fragment fragment = new MainPrism4DHomeFragment();
        String tag        = sHomeTag;
        int subTitle      = R.string.action_home;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);
    }




    /*******************************************
     * PROJECTS
     *******************************************/




    /****
     * Method to switch fragment to top level Project screen
     * EMH 4/23/16
     */
    public void switchToProject1Screen() {

        Fragment fragment = new MainPrism4DProject1Fragment();
        String tag        = sProjectTopTag;
        int subTitle      = R.string.action_project;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }






    /****
     * Method to switch fragment to top level Project screen
     * But clear any intermediate screens from the stack
     * EMH 5/13/16
     */
    public void popToProject1Screen(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(sProjectTopTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        //Put the name of the fragment on the title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(R.string.action_project);
        }

    }




    /****
     * Method to switch fragment to screen to create new project
     * EMH 5/11/16
     */
    public void switchToProject11CreateScreen(){

        //We are going to need the database to create a project,
        //and we can only open the database from an Activity,
        //so open the database now
        //Use the application context, not the activity context
        //The lifetime of this open is while the application is active
        //From now on, can call the static getInstance() and it will return
        //    the open database.
        //Prism4DDatabaseManager.initializeInstance(getApplicationContext());


        //Gets the project which contains the defaults for all other projects
        Prism4DProject project = getProjectForCreate();
        //set the action for the project to create
        Prism4DPath projectPath = new Prism4DPath(Prism4DPath.sCreateTag);

        Fragment fragment = MainPrism4DProject14UpdateFragment.newInstance(project, projectPath);
        String tag        = sProjectCreateTag;
        int subTitle      = R.string.subtitle_create_project;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /****
     * Method to switch fragment to show a list of projects to open
     * EMH 5/11/16
     */
    public void switchToProject12OpenScreen(){

        //set the action for the project to create
        Prism4DPath projectPath = new Prism4DPath(Prism4DPath.sOpenTag);

        Fragment fragment = MainPrism4DListProjectsFragment.newInstance(projectPath);
        String tag        = sProjectOpenTag;
        int subTitle      = R.string.action_open;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }



    /****
     * Method to switch fragment to show a list of projects to open
     * EMH 5/11/16
     */
    public void switchToProjectListProjectsScreen(Prism4DPath projectPath){

        Fragment fragment = MainPrism4DListProjectsFragment.newInstance(projectPath);
        String tag        = sProjectOpenTag;
        String subTitle   = projectPath.getPath().toString();

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /****
     * Method to switch fragment to show a list of projects to
     * pick the one to copy
     * EMH 5/11/16
     */
    public void switchToProject13CopyScreen(){

        //create the path for copy
        Prism4DPath path = new Prism4DPath(Prism4DPath.sCopyTag);

        Fragment fragment = MainPrism4DListProjectsFragment.newInstance(path);
        String tag        = sProjectOpenTag;
        int subTitle      = R.string.action_open;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }





    /****
     * Method to switch fragment to display and existing Project
     * EMH 5/10/16
     */
    public void switchToProject14UpdateScreen(
            Prism4DProject project,
            Prism4DPath path){

        Fragment fragment = MainPrism4DProject14UpdateFragment.newInstance(project, path);
        String tag        = sProjectUpdateTag;
        int subTitle      = R.string.subtitle_maintain_project;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /****
     * Method to switch fragment to top level Project screen
     * But clear any intermediate screens from the stack
     * EMH 5/13/16
     */
    public void popToProjectUpdateScreen(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        //fm.popBackStack(sProjectUpdateTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        boolean stillLooking = true;
        if (fm.getBackStackEntryCount() == 0) stillLooking = false;

        int i;
        CharSequence fragName;
        while (stillLooking){
            i = fm.getBackStackEntryCount()-1;
            fragName = fm.getBackStackEntryAt(i).getName();
            if (fragName.equals(sProjectUpdateTag)){
                stillLooking = false;
            } else {
                fm.popBackStackImmediate();
                if (fm.getBackStackEntryCount() == 0) stillLooking = false;
            }
        }


        int subTitle = R.string.subtitle_maintain_project;
        switchSubtitle(subTitle);

    }




    /****
     * Method to switch fragment to show a list of projects to
     * pick the one to delete
     * EMH 5/13/16
     */
    public void switchToProject15DeleteScreen(){

        //create the path for open
        Prism4DPath path = new Prism4DPath(sDeleteTag);

        Fragment fragment = MainPrism4DListProjectsFragment.newInstance(path);
        String tag        = sDeleteTag;
        int subTitle      = R.string.action_delete;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /****
     * Method to switch fragment to Project Settings screen
     * EMH 4/26/16
     */
    public void switchToProjectSettingsScreen(
            Prism4DProject project,
            Prism4DPath projectPath){

        Fragment fragment =  MainPrism4DSettingsFragment.newInstance(project, projectPath);
        String tag        = sProjectSettingsTag;
        int subTitle      = R.string.subtitle_project_settings;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /*******************************************
     * POINTS
     *******************************************/


    /****
     * Method to switch fragment to screen to create new point
     * EMH 5/11/16
     */
    public void switchToMaintainPoints1Screen(
            Prism4DProject project,
            Prism4DPath projectPath){


        Fragment fragment =  MainPrism4DPoint1Fragment.newInstance(project, projectPath);
        String tag        = sPointTopTag;
        int subTitle      = R.string.subtitle_maintain_point;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /****
     * Method to switch fragment to screen to create new project
     * EMH 5/11/16
     */
    public void switchToPoint11CreateScreen(Prism4DProject project,
                                            Prism4DPath projectPath){


        //Gets the point which contains the defaults for all other projects
        Prism4DPoint newPoint = getPointForCreate();
        newPoint.setProjectID(project.getProjectID());
        //overwrite the dummy ID with the next ID in the proejct
        newPoint.setPointID(project.getNextPointID());

        Prism4DPath pointPath = new Prism4DPath(Prism4DPath.sCreateTag);

        Fragment fragment =  MainPrism4DProjectUpdatePointFragment.newInstance(
                                                                    projectPath,
                                                                    newPoint,
                                                                    pointPath);
        String tag        = sPointCreateTag;
        int subTitle      = R.string.subtitle_create_point;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /****
     * Method to switch fragment to show a list of projects to open
     * EMH 5/16/16
     */
    public void switchToListPointsScreen(
            int         projectID,
            Prism4DPath projectPath,
            Prism4DPath pointPath){


        //      Get the singleton container of projects
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        //      then go get our project
        Prism4DProject newProject = projectManager.getProject(projectID);

        if (newProject == null){
            //Need a project, not just the ID
            // going forward do this right,
            // but for now, punt
            newProject = new Prism4DProject(
                    getResources().getString(R.string.dummy_project_name), projectID);
            newProject.setProjectDescription(
                    getResources().getString(R.string.dummy_project_description));


            projectManager.add(newProject);

            //todo need to throw an exception here
            //But tell the user
            Toast.makeText(this,
                    "Error, project " + String.valueOf(projectID) + " not found",
                    Toast.LENGTH_LONG).show();
        }


        //figure out the Tag and the Subtitle from the path we are on
        CharSequence path = pointPath.getPath();
        int subtitle;
        String tag;
        if (path.equals(Prism4DPath.sCopyTag)){
            subtitle = R.string.subtitle_copy_point;
            tag = sPointCopyTag;
        } else if (path.equals(Prism4DPath.sOpenTag)) {
            subtitle = R.string.subtitle_open_point;
            tag = sPointOpenTag;
        } else if (path.equals(sDeleteTag)){
            subtitle = R.string.subtitle_delete_point;
            tag = sProjectDeleteTag;
        } else {
            //todo probably need to throw an exception
            subtitle = R.string.subtitle_unknown_error;
            tag = getResources().getString(R.string.unknown_process);
        }

        Fragment fragment =  MainPrism4DListPointsFragment.newInstance(
                newProject,
                projectPath,
                pointPath);

        switchScreen(fragment, tag);
        switchSubtitle(subtitle);

    }


    /****
     * Method to switch fragment to show a list of projects to open
     * EMH 5/16/16
     */
    public void switchToMaintainPointScreen(
            Prism4DPath projectPath,
            Prism4DPoint point,
            Prism4DPath pointPath){


        Fragment fragment =  MainPrism4DProjectUpdatePointFragment.newInstance(
                projectPath,
                point,
                pointPath);
        String tag        = sPointUpdateTag;
        int subTitle      = R.string.subtitle_maintain_point;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /*******************************************
     * COLLECT
     *******************************************/


    /****
     * Method to switch fragment to top level collect screen
     * EMH 5/1/16
     */
    public void switchToCollect1Screen(){

        Fragment fragment = new MainPrism4DCollect1Fragment();
        String tag        = sCollectTopTag;
        int subTitle      = R.string.action_collect;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }

    /****
     * Method to switch fragment to top level collect screen
     * EMH 4/13/16
     */
    public void switchToCollect11PointsScreen(){

        Fragment fragment = new MainPrism4DCollect11PointsFragment();
        String tag        = sCollectPointsTag;
        int subTitle      = R.string.action_measure_points;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /****
     * Method to switch fragment to top level collect screen
     *                      with map behind the drawing area
     * EMH 4/13/16
     */
    public void switchToCollect11PointsWithMapScreen(){

        Fragment fragment = new MainPrism4DCollect11PointsWithMapFragment();
        String tag        = sCollectPointsMapTag;
        int subTitle      = R.string.action_measure_points;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }



    /*******************************************
     * STACKOUT
     *******************************************/


    /****
     * Method to switch fragment to top level Stakeout screen
     * EMH 5/1/16
     */
    public void switchToStakeout1Screen(){

        Fragment fragment = new MainPrism4DStakeout1Fragment();
        String tag        = sStakeoutTopTag;
        int subTitle      = R.string.action_stakeout;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /*******************************************
     * COGO
     *******************************************/

    /****
     * Method to switch fragment to top level Cogo screen
     * EMH 5/1/16
     */
    public void switchToCogo1Screen(){

        Fragment fragment = new MainPrism4DCogo1Fragment();
        String tag        = sCogoTopTag;
        int subTitle      = action_cogo;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /****
     * Method to switch fragment to coordinate conversion screen
     * EMH 6/15/16
     */
    public void switchToCoordWorkflow(){

        Fragment fragment = new MainPrism4DCoordWorkflowFragment();
        String tag        = sCogoWorkflowTag;
        int subTitle      = subtitle_workflow;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }



    /****
     * Method to switch fragment to coordinate conversion screen
     * EMH 4/13/16
     */
    public void switchToConvertScreen(){

        Fragment fragment = new MainPrism4DCoordConversionFragment();
        String tag        = sConversionTag;
        int subTitle      = subtitle_convert_to_utm;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /*******************************************
     * Maps
     *******************************************/

    /****
     * Method to switch fragment to top level Cogo screen
     * EMH 10/21/2016
     */
    public void switchToMaps1Screen(){

        Fragment fragment = new MainPrism4DMaps1Fragment();
        String tag        = sMapsTopTag;
        int subTitle      = subtitle_maps;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /*******************************************
     * SKYPLOT
     *******************************************/


    /****
     * Method to switch fragment to top level Skyplot screen
     * EMH 5/1/16
     */
    public void switchToSkyplot1Screen(){

        Fragment fragment = new MainPrism4DSkyplots1Fragment();
        String tag        = sSkyplotTopTag;
        int subTitle      = action_skyplots;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /****
     * Method to switch fragment to coordinate conversion screen
     * EMH 6/7/16
     */
    public void switchToListNmeaScreen(){

        Fragment fragment = new MainPrism4DListNmeaFragment();
        String tag        = sSkyplotListNmeaTag;
        int subTitle      = subtitle_list_nmea;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /****
     * Method to switch fragment to coordinate conversion screen
     * EMH 6/13/16
     */
    public void switchToListSatellitesScreen(){

        Fragment fragment = new MainPrism4DListSatellitesFragment();
        String tag        = sSkyplotListSatelliteTag;
        int subTitle      = subtitle_list_satellites;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /****
     * Method to switch fragment to display GPS data from NMEA Sentences
     * EMH 6/7/16
     */
    public void switchToGpsNmeaScreen(){

        Fragment fragment = new MainPrism4DGpsFromNmeaFragment();
        String tag        = sSkyplotGpsNmeaTag;
        int subTitle      = subtitle_gps_nmea;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }



    /*******************************************
     * CONFIG
     *******************************************/






    /****
     * Method to switch fragment to Configurations top matrix screen
     * EMH 4/26/16
     */
    public void switchToConfig1Screen(){

        Fragment fragment = new MainPrism4DConfigurations1Fragment();
        String tag        = sConfigTopTag;
        int subTitle      = action_config;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /*******************************************
     * SETTINGS
     *******************************************/


    /****
     * Method to switch fragment to top level Settings screen
     * EMH 5/1/16
     */
    public void switchToSettings1Screen(){

        Fragment fragment = new MainPrism4DSettings1Fragment();
        String tag        = sSettingsTopTag;
        int subTitle      = action_settings;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /****
     * Method to switch fragment to Global Settings screen
     * EMH 5/11/16
     */
    public void switchToSettings11GlobalScreen(){

        Fragment fragment = new MainPrism4DSettings11GlobalFragment();
        String tag        = sSettingsGlobalTag;
        int subTitle      = action_global_settings;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }

    /****
     * Method to switch fragment to display Settings - Project Defaults
     * EMH 5/11/16
     */
    public void switchToSettings12ProjectDefaultsScreen(){


        //Gets the project which contains the defaults for all other projects
        Prism4DProject project = getDefaultProject();

        //This project will always exist, we will only ever want to update it
        Prism4DPath openPath = new Prism4DPath(Prism4DPath.sOpenTag);


        Fragment fragment = MainPrism4DProject14UpdateFragment.newInstance(project, openPath);
        String tag        = sSettingsProjectDefaultTag;
        int subTitle      = R.string.action_project_default_settings;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }




    /*******************************************
     * Support
     *******************************************/

    /****
     * Method to switch fragment to top level Support screen
     * EMH 10/21/2016
     */
    public void switchToSupport1Screen(){

        Fragment fragment = new MainPrism4DSupport1Fragment();
        String tag        = sSupportTopTag;
        int subTitle      = subtitle_support;

        switchScreen(fragment, tag);
        switchSubtitle(subTitle);

    }


    /*******************************************
     * CONVERT
     *******************************************/






    /*******************************************
     * MISC
     *******************************************/





    private Prism4DProject getDefaultProject(){
        Prism4DProject project = new Prism4DProject(
                Prism4DProject.sProjectDefaultName,
                Prism4DProject.sProjectDefaultsID);

        project.setProjectDescription(Prism4DProject.sProjectDefaultsDesc);
        return project;
    }

    private Prism4DProject getProjectForCreate(){
        Prism4DProject project = new Prism4DProject(
                Prism4DProject.sProjectNewName,
                Prism4DProject.sProjectNewID);

        project.setProjectDescription(Prism4DProject.sProjectNewDesc);
        return project;
    }

    private Prism4DPoint getPointForCreate(){
        Prism4DPoint point = new Prism4DPoint(
                                Prism4DPoint.sPointNewID);

        point.setPointDescription(Prism4DPoint.sPointNewDesc);
        return point;
    }

}
