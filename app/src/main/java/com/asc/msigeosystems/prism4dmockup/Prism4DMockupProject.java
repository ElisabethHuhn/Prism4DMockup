package com.asc.msigeosystems.prism4dmockup;

import java.util.Date;

/**
 * Created by elisabethhuhn on 5/7/2016.
 */
public class Prism4DMockupProject {

    private static int sNextID = 1008;

    // Project Data
    private CharSequence mName;
    private int          mID;
    private Date         mDateCreated;
    private Date         mLastModified;
    private CharSequence mDescription;
    //Settings are linked to the project with an attribute on the settings
    //which is the project ID the settings belong to
    //private Prism4DMockupProjectSettings mSettings;


    //Tags for fragment arguments
    public static final String sProjectTag       = "PROJECT_OBJECT";
    public static final String sProjectNameTag   = "PROJECT_NAME";
    public static final String sProjectIDTag     = "PROJECT_ID";
    public static final String sProjectCreateTag = "PROJECT_CREATION";
    public static final String sProjectMaintTag  = "PROJECT_MAINTAINED";
    public static final String sProjectDescTag   = "PROJECT_DESCRIPTION";

    public static int    sProjectDefaultsID   = -1;
    public static String sProjectDefaultName  = "Project Defaults";
    public static String sProjectDefaultsDesc =
            "This project represents the defaults that all other projects start with";

    public static int    sProjectNewID   = -2;
    public static String sProjectNewName = "Create Project";
    public static String sProjectNewDesc = "";


    //Setters and Getters

    //this should be good enough for the Mockup,
    //but we must do MUCH better for the real thing
    public static int getsNextID(){
        sNextID = sNextID + 1;
        return sNextID;
    }

    public CharSequence getProjectName() {  return mName;    }
    public void setProjectName(CharSequence mName) {
        this.mName = mName;
    }

    public int getProjectID() {
        return mID;
    }
    public void setProjectID(int mID) {
        this.mID = mID;
    }

    public Date getProjectDateCreated() {
        return mDateCreated;
    }
    public void setProjectDateCreated(Date mDateCreated) { this.mDateCreated = mDateCreated;  }

    public Date getProjectLastModified() { return mLastModified;    }
    public void setProjectLastModified(Date mLastModified) { this.mLastModified = mLastModified; }

    public CharSequence getProjectDescription() {
        return mDescription;
    }
    public void setProjectDescription(CharSequence description) {
        this.mDescription = description;
    }




    /*
     *  Note this constructor does NOT have description
     */
    public Prism4DMockupProject(CharSequence projectName, int projectID) {
        this.mName = projectName;
        this.mID = projectID;

        //defaults for the project
        this.mDescription = "";
        this.mDateCreated = new Date();
        this.mLastModified = new Date();

    }
}
