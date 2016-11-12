package com.asc.msigeosystems.prism4d;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Elisabeth Huhn on 5/7/2016.
 *
 * This is the Principal Data Model Object.
 * A Project contains points
 *
 */
public class Prism4DProject {
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
    public static final String sProjectSettingsTag = "PROJECT_SETTINGS";
    public static final String sProjectPointsTag = "PROJECT_POINTS";

    public static final int    sProjectDefaultsID   = -1;
    public static final String sProjectDefaultName  = "Project Defaults";
    public static final String sProjectDefaultsDesc =
            "This project represents the defaults that all other projects start with";

    public static final int    sProjectNewID   = -2;
    public static final String sProjectNewName = "Create Project";
    public static final String sProjectNewDesc = "";

    /*************************************/
    /*    Static (class) Variables       */
    /*************************************/


    /*************************************/
    /*    Member (instance) Variables    */
    /*************************************/

    // Project Data
    private int          mProjectID;
    private CharSequence mName;
    private Date         mDateCreated;
    private Date         mLastModified;
    private CharSequence mDescription;
    //Settings are linked to the project with an attribute on the project settings
    //which is the project ID the settings belong to
    private Prism4DProjectSettings mSettings;
    private ArrayList<Prism4DPoint> mPoints;



    private int mNextPointID = 10001;



    /*************************************/
    /*         Static Methods            */
    /*************************************/

    /*************************************************************
     *             Constructor
     *************************************************************/
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

    //Every field is initialized. No Nulls Allowed
    public Prism4DProject() {
        initializeNoID();
    }


    private void initializeDefaultVariables(){
        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
        Prism4DGlobalData globalData = globalDataManager.getGlobalData();

        this.mProjectID    = globalData.getNextProjectID();
        initializeNoID();
    }

    private void initializeNoID(){
        //defaults for the project
        this.mName         = sProjectDefaultName;
        this.mDescription  = "";
        this.mDateCreated  = new Date();
        this.mLastModified = new Date();
        this.mPoints       = new ArrayList<>();

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

    public Date getProjectDateCreated()                  {return mDateCreated; }
    public void setProjectDateCreated(Date dateCreated) { this.mDateCreated = dateCreated;  }

    public Date getProjectLastModified() { return mLastModified;    }
    public void setProjectLastModified(Date lastModified) { this.mLastModified = lastModified; }

    public CharSequence getProjectDescription() {
        return mDescription;
    }
    public void setProjectDescription(CharSequence description) {
        this.mDescription = description;
    }

    public ArrayList<Prism4DPoint> getPoints()                               { return mPoints; }
    public void                    setPoints(ArrayList<Prism4DPoint> points) {mPoints = points;  }

    public Prism4DProjectSettings getSettings()                                {return mSettings; }
    public void                 setSettings(Prism4DProjectSettings settings) {mSettings = settings;}


    /*************************************/
    /*          Member Methods           */
    /*************************************/


}
