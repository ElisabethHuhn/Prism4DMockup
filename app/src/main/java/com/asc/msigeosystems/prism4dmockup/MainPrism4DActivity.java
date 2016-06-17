package com.asc.msigeosystems.prism4dmockup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

//used to extend
public class MainPrism4DActivity extends AppCompatActivity {

    //DEFINE constants / literals
    public static final int MY_PERMISSIONS_REQUEST_COURSE_LOCATIONS = 1;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATIONS = 2;


    private static final String sHomeTag               = "HOME";//HOME screen fragment

    private static final String sProjectTopTag         = "PROJECT_TOP";
    private static final String sProjectCreateTag      = "PROJECT_CREATE_TOP";
    private static final String sProjectOpenTag        = "PROJECT_OPEN";
    private static final String sProjectCopyTag        = "PROJECT_COPY";
    private static final String sProjectEditTag        = "PROJECT_EDIT";
    private static final String sProjectUpdateTag      = "PROJECT_UPDATE";
    private static final String sProjectDeleteTag      = "PROJECT_DELETE";
    private static final String sProjectSettingsTag    = "PROJECT_SETTINGS";

    private static final String sPointTopTag         = "POINT_TOP";
    private static final String sPointCreateTag      = "POINT_CREATE_TOP";
    private static final String sPointOpenTag        = "POINT_OPEN";
    private static final String sPointCopyTag        = "POINT_COPY";
    private static final String sPointEditTag        = "POINT_EDIT";
    private static final String sPointUpdateTag      = "POINT_UPDATE";
    private static final String sPointDeleteTag      = "POINT_DELETE";
    private static final String sPointSettingsTag    = "POINT_SETTINGS";

    private static final String sCollectTopTag         = "COLLECT_TOP";
    private static final String sCollectPointsTag      = "COLLECT_POINTS";
    private static final String sCollectPointsMapTag   = "COLLECT_POINTS_MAP";

    private static final String sStakeoutTopTag        = "STAKEOUT_TOP";

    private static final String sCogoTopTag            = "COGO_TOP";
    private static final String sCogoCnversionTag      = "COGO_CONVERSION";
    private static final String sCogoWorkflowTag       = "COGO_WORKFLOW";

    private static final String sSkyplotTopTag         = "SKYPLOT_TOP";
    private static final String sSkyplotListNmeaTag    = "SKYPLOT_LIST_NMEA";
    private static final String sSkyplotListSatelliteTag = "SKYPLOT_LIST_SATELLITE";
    private static final String sSkyplotGpsNmeaTag     = "SKYPLOT_GPS_NMEA";

    private static final String sConfigTopTag          = "CONFIG_TOP";

    private static final String sSettingsTopTag        = "SETTINGS_TOP";
    private static final String sSettingsGlobalTag     = "SETTINGS_GLOBAL";
    private static final String sSettingsProjectDefaultTag = "SETTINGS_PROJECT_DEFAULT";

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
            //fragment = new MainPrism4DCollect11PointsFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


        //Put Home on the title bar
        getSupportActionBar().setSubtitle(R.string.action_home);

        GpsStuff();

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
            if (
                    //shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                 false){
                //tell the user why GPS is required
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MainPrism4DActivity.MY_PERMISSIONS_REQUEST_COURSE_LOCATIONS);

                // MY_PERMISSIONS_REQUEST_COURSE_LOCATIONS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        //If we don't currently have permission, we have to ask for it
        if (permissionCheckFine != PackageManager.PERMISSION_GRANTED) {
            //find out if we need to explain to the user why we need GPS
            if (false) {
                //shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){

                //tell the user why GPS is required
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MainPrism4DActivity.MY_PERMISSIONS_REQUEST_FINE_LOCATIONS);

                // MY_PERMISSIONS_REQUEST_FINE_LOCATIONS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
            //So now signup for the GpsStatus.NmeaListener

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_COURSE_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_FINE_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_prism4_dmockup, menu);
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



    /***
     * Switch to the fragment that is on the top of the backstack
     * EMH 4/27/2016
     */
    public void switchToPopBackstack(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //settings is at the top of the back stack, so pop it off
        fm.popBackStack();

/****  We may need to do something about the case of invoking settings from the menu
         //get the index of the last entry of the back stack
         // this is the screen we want to return to
         //
         int currentIndex   = fm.getBackStackEntryCount();
         int lastEntryIndex = currentIndex - 1; //programmers start counting at zero

         if (currentIndex > 0){

             //get the last entry of the back stack
             android.support.v4.app.FragmentManager.BackStackEntry lastBackStackEntry =
             getSupportFragmentManager().getBackStackEntryAt(lastEntryIndex);

             //get the name (we assigned) of fragment this last entry
             // expecting Home or ProjectTop
             String lastFragTagName    = lastBackStackEntry.getName();

             //get the actual fragment off the top of the stack
             Fragment lastFragment     = getSupportFragmentManager().findFragmentByTag(lastFragTagName);

             //Now that we have the fragment, display it
             fm.beginTransaction()
             //replace whatever is being displayed with the Home fragment
             .replace(R.id.fragment_container, lastFragment, lastFragTagName)
             //and add the transaction to the back stack
             .addToBackStack(lastFragTagName)
             .commit();

         }
***/
    }




    /****
     * Method to switch fragment to home screen
     * EMH 4/20/16
     */
    public void switchToHomeScreen(){
        //replace the fragment with the Home UI

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //clear the back stack
        while (fm.getBackStackEntryCount() > 0){
            fm.popBackStackImmediate();
        }

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DHomeFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sHomeTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DHomeFragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the Home fragment
                    .replace(R.id.fragment_container, fragment, sHomeTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sHomeTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar

        getSupportActionBar().setSubtitle(R.string.action_home);

    }



    /*******************************************
     * PROJECTS
     *******************************************/




    /****
     * Method to switch fragment to top level Project screen
     * EMH 4/23/16
     */
    public void switchToProject1Screen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DProject1Fragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectTopTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DProject1Fragment();

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sProjectTopTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sProjectTopTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_project);

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
        getSupportActionBar().setSubtitle(R.string.action_project);

    }




    /****
     * Method to switch fragment to screen to create new project
     * EMH 5/11/16
     */
    public void switchToProject11CreateScreen(){


        //Gets the project which contains the defaults for all other projects
        Prism4DProject project = getProjectForCreate();

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        Prism4DPath projectPath = new Prism4DPath(Prism4DPath.sCreateTag);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with
            // no fragments on the screen,
            // but code defensively
            fragment = MainPrism4DProject14UpdateFragment.newInstance(project, projectPath);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectCreateTag)
                    .commit();
        } else {

            fragment = MainPrism4DProject14UpdateFragment.newInstance(project, projectPath);

            fm.beginTransaction()
                    //replace whatever is being displayed
                    .replace(R.id.fragment_container, fragment, sProjectCreateTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sProjectCreateTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_create_project);

    }


    /****
     * Method to switch fragment to show a list of projects to open
     * EMH 5/11/16
     */
    public void switchToProject12OpenScreen(){


        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //create the path for open
        Prism4DPath projectPath = new Prism4DPath(Prism4DPath.sOpenTag);

        if (fragment == null){
            //pass the update project fragment the open path
            fragment = new MainPrism4DListProjectsFragment().newInstance(projectPath);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectOpenTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DListProjectsFragment().newInstance(projectPath);

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sProjectOpenTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sProjectOpenTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_open);

    }


    /****
     * Method to switch fragment to show a list of projects to open
     * EMH 5/11/16
     */
    public void switchToProjectListProjectsScreen(Prism4DPath projectPath){


        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //pass the update project fragment the open path
            fragment = new MainPrism4DListProjectsFragment().newInstance(projectPath);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectOpenTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DListProjectsFragment().newInstance(projectPath);

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sProjectOpenTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sProjectOpenTag)
                    .commit();
        }

        //Put the path being followed on the action bar
        getSupportActionBar().setSubtitle(projectPath.getPath());

    }



    /****
     * Method to switch fragment to show a list of projects to
     * pick the one to copy
     * EMH 5/11/16
     */
    public void switchToProject13CopyScreen(){


        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //create the path for copy
        Prism4DPath path = new Prism4DPath(Prism4DPath.sCopyTag);


        if (fragment == null){

            fragment = new MainPrism4DListProjectsFragment().newInstance(path);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectOpenTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new fragment
            fragment = new MainPrism4DListProjectsFragment().newInstance(path);

            fm.beginTransaction()
                    //replace whatever is being displayed
                    .replace(R.id.fragment_container, fragment, sProjectOpenTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sProjectOpenTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_open);

    }


    /****
     * Method to switch fragment to Edit Project screen
     * EMH 4/26/16
     */
    public void switchToProject14EditScreen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DProject14EditFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectEditTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DProject14EditFragment();

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sProjectEditTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sProjectEditTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_maintain_project);

    }



    /****
     * Method to
     * clear the stack of the top fragment, then switch to
     * switch fragment to display an existing project
     * EMH 5/10/16
     */
    public void switchBackToProject14UpdateScreen(
            Prism4DProject project,
            Prism4DPath path){

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        fm.popBackStack(); //get rid of last update screen


        if (fragment == null){

            fragment = MainPrism4DProject14UpdateFragment.newInstance(project, path);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSettingsProjectDefaultTag)
                    .commit();
        } else {

            fragment = MainPrism4DProject14UpdateFragment.newInstance(project, path);

            fm.beginTransaction()
                    //replace whatever is being displayed
                    .replace(R.id.fragment_container, fragment, sSettingsProjectDefaultTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sSettingsProjectDefaultTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_project_default_settings);

    }


    /****
     * Method to switch fragment to display and existing Project
     * EMH 5/10/16
     */
    public void switchToProject14UpdateScreen(
            Prism4DProject project,
            Prism4DPath path){

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);


        if (fragment == null){
            //It shouldn't ever be the case that we got this far with
            // no fragments on the screen,
            // but code defensively
            fragment = MainPrism4DProject14UpdateFragment.newInstance(project, path);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectUpdateTag)
                    .commit();
        } else {

            fragment = MainPrism4DProject14UpdateFragment.newInstance(project, path);

            fm.beginTransaction()
                    //replace whatever is being displayed
                    .replace(R.id.fragment_container, fragment, sProjectUpdateTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sProjectUpdateTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_maintain_project);

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


        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_maintain_project);

    }




    /****
     * Method to switch fragment to show a list of projects to
     * pick the one to delete
     * EMH 5/13/16
     */
    public void switchToProject15DeleteScreen(){


        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //create the path for open
        Prism4DPath path = new Prism4DPath(Prism4DPath.sDeleteTag);


        if (fragment == null){

            fragment = new MainPrism4DListProjectsFragment().newInstance(path);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectDeleteTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new fragment
            fragment = new MainPrism4DListProjectsFragment().newInstance(path);

            fm.beginTransaction()
                    //replace whatever is being displayed
                    .replace(R.id.fragment_container, fragment, sProjectDeleteTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sProjectDeleteTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_delete);

    }



    /****
     * Method to switch fragment to Project Settings screen
     * EMH 4/26/16
     */
    public void switchToProjectSettingsScreen(
            Prism4DProject project,
            Prism4DPath projectPath){

        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DSettingsFragment().newInstance(project, projectPath);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sProjectSettingsTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DSettingsFragment().newInstance(project, projectPath);

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sProjectSettingsTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sProjectSettingsTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_project_settings);

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

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with
            // no fragments on the screen,
            fragment = new MainPrism4DPoint1Fragment().newInstance (project, projectPath);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sPointTopTag)
                    .commit();
        } else {

            fragment = new MainPrism4DPoint1Fragment().newInstance(project, projectPath);

            fm.beginTransaction()
                    //replace whatever is being displayed
                    .replace(R.id.fragment_container, fragment, sPointTopTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sPointTopTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_maintain_point);

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

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with
            // no fragments on the screen,
            // but code defensively
            fragment = MainPrism4DProjectUpdatePointFragment.newInstance(
                    projectPath, newPoint, pointPath);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sPointCreateTag)
                    .commit();
        } else {

            fragment = MainPrism4DProjectUpdatePointFragment.newInstance(
                    projectPath, newPoint, pointPath);

            fm.beginTransaction()
                    //replace whatever is being displayed
                    .replace(R.id.fragment_container, fragment, sPointCreateTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sPointCreateTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_create_point);

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
        Prism4DProjectsContainer projectContainer = Prism4DProjectsContainer.getInstance();
        //      then go get our project
        Prism4DProject newProject = projectContainer.getProject(projectID);

        if (newProject == null){
            //Need a project, not just the ID
            // going forward do this right,
            // but for now, punt
            newProject = new Prism4DProject(
                    getResources().getString(R.string.dummy_project_name), projectID);
            newProject.setProjectDescription(
                    getResources().getString(R.string.dummy_project_description));


            projectContainer.add(newProject);

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
        } else if (path.equals(Prism4DPath.sDeleteTag)){
            subtitle = R.string.subtitle_delete_point;
            tag = sProjectDeleteTag;
        } else {
            //todo probably need to throw an exception
            subtitle = R.string.subtitle_unknown_error;
            tag = getResources().getString(R.string.unknown_process);
        }


        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        Fragment fragment;
        //Are any fragments already being displayed?
        //fragment = fm.findFragmentById(R.id.fragment_container);
        //we usually check for null, but its not worth the effort

         //Create a new fragment
         fragment = new MainPrism4DListPointsFragment().newInstance(
                 newProject,
                 projectPath,
                 pointPath);

         fm.beginTransaction()
         //replace whatever is being displayed with the new fragment
         .replace(R.id.fragment_container, fragment, tag)
         //and add the transaction to the back stack
         .addToBackStack(tag)
         .commit();


        //Put the path being followed on the action bar
        getSupportActionBar().setSubtitle(subtitle);

    }






    /****
     * Method to switch fragment to show a list of projects to open
     * EMH 5/16/16
     */
    public void switchToMaintainPointScreen(
            Prism4DPath projectPath,
            Prism4DPoint point,
            Prism4DPath pointPath){


        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //create the path for open
        Prism4DPath path = new Prism4DPath(Prism4DPath.sOpenTag);

        if (fragment == null){
            //pass the update project fragment the open path
            fragment = new MainPrism4DProjectUpdatePointFragment().newInstance(
                                projectPath,
                                point,
                                pointPath);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sPointUpdateTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DProjectUpdatePointFragment().newInstance(
                    projectPath,
                    point,
                    pointPath);

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sPointUpdateTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sProjectOpenTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_maintain_point);

    }





    /*******************************************
     * COLLECT
     *******************************************/





    /****
     * Method to switch fragment to top level collect screen
     * EMH 5/1/16
     */
    public void switchToCollect1Screen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DCollect1Fragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sCollectTopTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DCollect1Fragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sCollectTopTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sCollectTopTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_collect);

    }

    /****
     * Method to switch fragment to top level collect screen
     * EMH 4/13/16
     */
    public void switchToCollect11PointsScreen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DCollect11PointsFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sCollectPointsTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DCollect11PointsFragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sCollectPointsTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sCollectPointsTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_measure_points);

    }


    /****
     * Method to switch fragment to top level collect screen
     *                      with map behind the drawing area
     * EMH 4/13/16
     */
    public void switchToCollect11PointsWithMapScreen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DCollect11PointsWithMapFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sCollectPointsMapTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DCollect11PointsWithMapFragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sCollectPointsMapTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sCollectPointsMapTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_measure_points);

    }



    /*******************************************
     * STACKOUT
     *******************************************/






    /****
     * Method to switch fragment to top level Stakeout screen
     * EMH 5/1/16
     */
    public void switchToStakeout1Screen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DStakeout1Fragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sStakeoutTopTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DStakeout1Fragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sCollectTopTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sStakeoutTopTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_stakeout);

    }



    /*******************************************
     * COGO
     *******************************************/





    /****
     * Method to switch fragment to top level Cogo screen
     * EMH 5/1/16
     */
    public void switchToCogo1Screen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DCogo1Fragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sCogoTopTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DCogo1Fragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sCogoTopTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sCogoTopTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_cogo);

    }


    /****
     * Method to switch fragment to coordinate conversion screen
     * EMH 6/15/16
     */
    public void switchToCoordWorkflow(){
        //Show Nmea Sentences

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DCoordWorkflowFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sCogoWorkflowTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DCoordWorkflowFragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sCogoWorkflowTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sCogoWorkflowTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_workflow);

    }



    /****
     * Method to switch fragment to coordinate conversion screen
     * EMH 4/13/16
     */
    public void switchToConvertScreen(){
        //replace the fragment with the coordinate conversion UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DCoordConversionFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sConversionTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DCoordConversionFragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sConversionTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sConversionTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_convert_to_utm);

    }



    /*******************************************
     * SKYPLOT
     *******************************************/


    /****
     * Method to switch fragment to top level Skyplot screen
     * EMH 5/1/16
     */
    public void switchToSkyplot1Screen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DSkyplots1Fragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSkyplotTopTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DSkyplots1Fragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sSkyplotTopTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sSkyplotTopTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_skyplots);

    }


    /****
     * Method to switch fragment to coordinate conversion screen
     * EMH 6/7/16
     */
    public void switchToListNmeaScreen(){
        //Show Nmea Sentences

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DListNmeaFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSkyplotListNmeaTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DListNmeaFragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sSkyplotListNmeaTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sSkyplotListNmeaTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_list_nmea);

    }


    /****
     * Method to switch fragment to coordinate conversion screen
     * EMH 6/13/16
     */
    public void switchToListSatellitesScreen(){
        //Show Visible Satellites

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DListSatellitesFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSkyplotListSatelliteTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DListSatellitesFragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sSkyplotListSatelliteTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sSkyplotListSatelliteTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_list_satellites);

    }




    /****
     * Method to switch fragment to display GPS data from NMEA Sentences
     * EMH 6/7/16
     */
    public void switchToGpsNmeaScreen(){
        //show GPS data collected from NMEA Sentences

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DGpsFromNmeaFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSkyplotGpsNmeaTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DGpsFromNmeaFragment();
            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sSkyplotGpsNmeaTag)
                    //and add the transaction to the back stack
                    .addToBackStack(sSkyplotGpsNmeaTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.subtitle_gps_nmea);

    }



    /*******************************************
     * CONFIG
     *******************************************/






    /****
     * Method to switch fragment to Configurations top matrix screen
     * EMH 4/26/16
     */
    public void switchToConfig1Screen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DConfigurations1Fragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sConfigTopTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DConfigurations1Fragment();

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sConfigTopTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sConfigTopTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_config);

    }



    /*******************************************
     * SETTINGS
     *******************************************/






    /****
     * Method to switch fragment to top level Settings screen
     * EMH 5/1/16
     */
    public void switchToSettings1Screen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DSettings1Fragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSettingsTopTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DSettings1Fragment();

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sSettingsTopTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sSettingsTopTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_settings);

    }


    /****
     * Method to switch fragment to Global Settings screen
     * EMH 5/11/16
     */
    public void switchToSettings11GlobalScreen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DSettings11GlobalFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSettingsGlobalTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DSettings11GlobalFragment();

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sSettingsGlobalTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sSettingsGlobalTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_global_settings);

    }

    /****
     * Method to switch fragment to display Settings - Project Defaults
     * EMH 5/11/16
     */
    public void switchToSettings12ProjectDefaultsScreen(){


        //Gets the project which contains the defaults for all other projects
        Prism4DProject project = getDefaultProject();

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //This project will always exist, we will only ever want to update it
        Prism4DPath openPath = new Prism4DPath(Prism4DPath.sOpenTag);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with
            // no fragments on the screen,
            // but code defensively
            fragment = MainPrism4DProject14UpdateFragment.newInstance(project, openPath);

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSettingsProjectDefaultTag)
                    .commit();
        } else {

            fragment = MainPrism4DProject14UpdateFragment.newInstance(project, openPath);

            fm.beginTransaction()
                    //replace whatever is being displayed
                    .replace(R.id.fragment_container, fragment, sSettingsProjectDefaultTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sSettingsProjectDefaultTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_project_default_settings);

    }




    /****
     * Method to switch fragment to Default Project Settings screen
     * EMH 5/11/16
     */
    public void switchToOLDSettings12ProjectDefaultsScreen(){
        //replace the fragment with the collections UI
        //Do fragments in real time, not xml

        //Need the Fragment Manager to do the swap for us
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        //Are any fragments already being displayed?
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            //It shouldn't ever be the case that we got this far with no fragments on the screen,
            // but code defensively
            fragment = new MainPrism4DSettingsFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, sSettingsProjectDefaultTag)
                    .commit();
        } else {
            //There is already a fragment being displayed

            //Create a new Collect fragment
            fragment = new MainPrism4DSettingsFragment();

            fm.beginTransaction()
                    //replace whatever is being displayed with the collect fragment
                    .replace(R.id.fragment_container, fragment, sSettingsProjectDefaultTag)
                            //and add the transaction to the back stack
                    .addToBackStack(sSettingsProjectDefaultTag)
                    .commit();
        }

        //Put the name of the fragment on the title bar
        getSupportActionBar().setSubtitle(R.string.action_project_default_settings);

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


    private Prism4DPoint getDefaultPoint(){
        Prism4DPoint point = new Prism4DPoint(
                              Prism4DPoint.sPointDefaultsID);

        point.setPointDescription(Prism4DPoint.sPointDefaultsDesc);
        return point;
    }

    private Prism4DPoint getPointForCreate(){
        Prism4DPoint point = new Prism4DPoint(
                                Prism4DPoint.sPointNewID);

        point.setPointDescription(Prism4DPoint.sPointNewDesc);
        return point;
    }

}
