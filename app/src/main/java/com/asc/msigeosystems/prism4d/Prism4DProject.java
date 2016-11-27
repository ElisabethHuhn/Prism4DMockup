package com.asc.msigeosystems.prism4d;

import android.os.Bundle;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Elisabeth Huhn on 5/7/2016.
 *
 * This is the Principal Data Model Object.
 * A Project contains points
 *
 */
public class Prism4DProject  {
    /*************************************/
    /*    Static (class) Constants       */
    /*************************************/

    private static int sNextProjectID = 1008;

    //Tags for fragment arguments
    public static final String sProjectTag       = "PROJECT_OBJECT";
    public static final String sProjectNameTag   = "PROJECT_NAME";
    public static final String sProjectIDTag     = "PROJECT_ID";
    public static final String sProjectCreateTag = "PROJECT_CREATION";
    public static final String sProjectMaintTag  = "PROJECT_MAINTAINED";
    public static final String sProjectDescTag   = "PROJECT_DESCRIPTION";
    public static final String sProjectCoordType = "PROJECT_COORDINATE_TYPE";
    //public static final String sProjectSettingsTag = "PROJECT_SETTINGS";
    //public static final String sProjectPointsTag = "PROJECT_POINTS";

   // public static final int    sProjectDefaultsID   = -1;
    public static final String sProjectDefaultName  = "Project ";
    public static final String sProjectDefaultsDesc =
            "This project represents the defaults that all other projects start with";

    public static final int    sProjectNewID   = -2;
    public static final String sProjectNewName = "Create Project";
    public static final String sProjectNewDesc = "";

    public static final int    sFirstPointID   = 10001;

    /*************************************/
    /*    Static (class) Variables       */
    /*************************************/


    /*************************************/
    /*    Member (instance) Variables    */
    /*************************************/

    // Project Data
    private int          mProjectID;
    private CharSequence mName;
    private long         mDateCreated;//milliseconds since 1/1/70
    private long         mLastModified;
    private CharSequence mDescription;
    //Settings are linked to the project with an attribute on the project settings
    //which is the project ID the settings belong to
    private Prism4DProjectSettings mSettings;

    //The coordinate type governs the type for all the points in the project
    //Once the first point is saved, this can no longer be saved.
    private CharSequence mCoordinateType;
    private ArrayList<Prism4DPoint> mPoints;



    private int mNextPointID = 10001;



    /*************************************************************
     *             Constructor
     *************************************************************/

    //Default constructor, A dummy project that is not in memory list or in the DB
    //Every field is initialized. No Nulls Allowed
    public Prism4DProject() {
        initializeNoID();
    }
    
    
    public Prism4DProject(CharSequence projectName, int projectID) {
        initializeNoID();
        this.mName         = projectName;
        if (projectID == sProjectNewID){
            Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
            Prism4DGlobalData globalData = globalDataManager.getGlobalData();
            this.mProjectID = globalData.getNextProjectID();
        } else {
            this.mProjectID = projectID;
        }
    }

    public Prism4DProject(CharSequence projectName){
        initializeDefaultVariables();
        this.mName = projectName;
    }


    private void initializeDefaultVariables(){
        this.mProjectID    = Prism4DProject.getNextProjectID();
        initializeNoID();
    }

    private void initializeNoID(){
        //defaults for the project
        this.mName         = sProjectDefaultName;
        this.mDescription  = "";
        this.mDateCreated  = new Date().getTime();
        this.mLastModified = new Date().getTime();
        this.mCoordinateType = Prism4DCoordinate.sCoordinateTypeClassUTM;
        this.mPoints       = new ArrayList<>();
        //initialize the default starting ID for points
        initializePointID();
    }

    public void initializePointID(){
        mNextPointID = Prism4DProject.sFirstPointID;
    }
    /**********************************************/
    //Setters and Getters
    /**********************************************/


    //same comment for the next point ID
    //Good enough for the mockup, but must do better for the real thing
    public int getNextPointID() {
        // TODO: 11/6/2016 need to figure out what to do about point ids
        return (int) (System.currentTimeMillis() & 0xfffffff);

/*


        mNextPointID = mNextPointID + 1;
        return mNextPointID;
*/
    }


    public CharSequence getProjectName() {  return mName;    }
    public void setProjectName(CharSequence name) {
        this.mName = name;
    }

    public int  getProjectID()        { return mProjectID; }
    public void setProjectID(int id) {
        this.mProjectID = id;
    }


    public long getProjectDateCreated()                  {return mDateCreated; }
    public void setProjectDateCreated(long dateCreated) { this.mDateCreated = dateCreated;  }

    public long getProjectLastModified() { return mLastModified;    }
    public void setProjectLastModified(long lastModified) { this.mLastModified = lastModified; }

    public CharSequence getProjectDescription() {
        return mDescription;
    }
    public void setProjectDescription(CharSequence description) {
        this.mDescription = description;
    }

    public CharSequence getProjectCoordinateType() { return mCoordinateType;   }
    public void setProjectCoordinateType(CharSequence coordinateType){mCoordinateType = coordinateType;}

    public ArrayList<Prism4DPoint> getPoints()                               { return mPoints; }
    public void                    setPoints(ArrayList<Prism4DPoint> points) {mPoints = points;  }

    public Prism4DProjectSettings getSettings()                                {return mSettings; }
    public void                 setSettings(Prism4DProjectSettings settings) {mSettings = settings;}




    /*************************************/
    /*          Static Methods           */
    /*************************************/


    /***********************************************************************/
    /*       methods dealing with determining the next project ID         */
    /***********************************************************************/
    public static int getNextProjectID(){
        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
        Prism4DGlobalData globalData = globalDataManager.getGlobalData();

        return globalData.getNextProjectID();
    }

    public static int getPotentialNextID(){
        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
        Prism4DGlobalData globalData = globalDataManager.getGlobalData();

        return globalData.getPotentialNextID();

    }

    /***********************************************************************/
    /*  This methods dealing with an argument bundle adn a project         */
    /***********************************************************************/

    public static Bundle putProjectInArguments(Bundle args, Prism4DProject project) {


        //It will be some work to make all of the data model serializable
        //so for now, just pass the project values
        if (project == null){
            //we are creating the project for the first time, so just store that flag
            args.putInt(Prism4DProject.sProjectIDTag,    0);
        } else {
            args.putInt         (Prism4DProject.sProjectIDTag,    project.getProjectID());
            args.putCharSequence(Prism4DProject.sProjectNameTag,  project.getProjectName());
            args.putLong        (Prism4DProject.sProjectCreateTag,project.getProjectDateCreated());
            args.putLong        (Prism4DProject.sProjectMaintTag, project.getProjectLastModified());
            args.putCharSequence(Prism4DProject.sProjectDescTag,  project.getProjectDescription());
            args.putCharSequence(Prism4DProject.sProjectCoordType,project.getProjectCoordinateType());
        }
        return args;
    }

    public static Prism4DProject getProjectFromArguments(Bundle args){

        Prism4DProject project;
        
        int projectID       = args.getInt(Prism4DProject.sProjectIDTag);
        
        if (projectID == 0){
            //the project is being created for the first time, 
            // just pass a default project that isn't in the DB
            project = new Prism4DProject();
        } else {
            //go find the project in either memory or the DB
            Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

            project = projectManager.getProject(projectID);
            
            if (project == null) {
                // TODO: 11/13/2016 What is the proper thing to do here?
                /*
                //Create a project that doesn't affect the memory list or the DB
                project = new Prism4DProject();
                //Set the attributes of the project from the arguments
                project.setProjectID(projectID);
                project.setProjectName(args.getCharSequence(Prism4DProject.sProjectNameTag));
                project.setProjectDateCreated(args.getInt(Prism4DProject.sProjectCreateTag));
                project.setProjectLastModified(args.getInt(Prism4DProject.sProjectMaintTag));
                project.setProjectDescription(args.getCharSequence(Prism4DProject.sProjectDescTag));
                */
                throw new RuntimeException("Bad project input");
            }
        }

         return project;
    }

    /***********************************************************************/
    /*         convert a project to exchange format                        */
    /***********************************************************************/

    public String convertToCDFMillisecond(){

        return  String.valueOf(this.getProjectID()    + ", " +
                this.getProjectName()                 + ", " +
                this.getProjectDateCreated()          + ", " +
                this.getProjectLastModified()         + ", " +
                this.getProjectDescription())         + ", " +
                this.getProjectCoordinateType()       +
                System.getProperty("line.separator");
    }
    public String convertToCDF(){

        return  String.valueOf(this.getProjectID()                + ", " +
                this.getProjectName()                             + ", " +
                this.getDateString(this.getProjectDateCreated())  + ", " +
                this.getDateString(this.getProjectLastModified()) + ", " +
                this.getProjectDescription())                     + ", " +
                this.getProjectCoordinateType()                   +
                System.getProperty("line.separator");
    }

    /*************************************/
    /*          Member Methods           */
    /*************************************/
    public String getProjectDateCreatedString() {
        return getDateString(mDateCreated);
    }
    public boolean setProjectDateCreatedString(String dateString){
        long milliseconds = setDateString(dateString);
        if (milliseconds == 0){
            return false;
        } else {
            mDateCreated = milliseconds;
            return true;
        }
    }

    public String getProjectLastModifiedString() {
        return getDateString(mLastModified);
    }
    public boolean setProjectLastModifiedString(String dateString){
        long milliseconds = setDateString(dateString);
        if (milliseconds == 0){
            return false;
        } else {
            mLastModified = milliseconds;
            return true;
        }
    }

    public String getDateString(long milliseconds) {
        return DateFormat.getDateInstance().format((new Date(milliseconds)));
    }

    public long setDateString(String dateString){
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
            Date myDate = df.parse(dateString);
            return myDate.getTime();
        } catch (java.text.ParseException e){
            return 0;
        }
    }


}
