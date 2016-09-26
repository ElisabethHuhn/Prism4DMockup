package com.asc.msigeosystems.prism4dmockup;

import java.util.Date;

/**
 * Created by elisabethhuhn on 5/7/2016.
 */
public class Prism4DProject {

    private static int sNextProjectID = 1008;

    // Project Data
    private int          mID;
    private CharSequence mName;
    private Date         mDateCreated;
    private Date         mLastModified;
    private CharSequence mDescription;
    //Settings are linked to the project with an attribute on the project settings
    //which is the project ID the settings belong to
    //private Prism4DProjectSettings mSettings;



    private int mNextPointID = 10001;


    //Tags for fragment arguments
    public static final String sProjectTag       = "PROJECT_OBJECT";
    public static final String sProjectNameTag   = "PROJECT_NAME";
    public static final String sProjectIDTag     = "PROJECT_ID";
    public static final String sProjectCreateTag = "PROJECT_CREATION";
    public static final String sProjectMaintTag  = "PROJECT_MAINTAINED";
    public static final String sProjectDescTag   = "PROJECT_DESCRIPTION";

    public static final int    sProjectDefaultsID   = -1;
    public static final String sProjectDefaultName  = "Project Defaults";
    public static final String sProjectDefaultsDesc =
            "This project represents the defaults that all other projects start with";

    public static final int    sProjectNewID   = -2;
    public static final String sProjectNewName = "Create Project";
    public static final String sProjectNewDesc = "";


    //
    //Setters and Getters
    //

    //this should be good enough for the Mockup,
    //but we must do MUCH better for the real thing
    public static int getNextProjectID(){
        sNextProjectID = sNextProjectID + 1;
        return sNextProjectID;
    }
    //same comment for the next point ID
    //Good enough for the mockup, but must do better for the real thing
    public int getNextPointID() {
        mNextPointID = mNextPointID + 1;
        return mNextPointID;
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
    public Prism4DProject(CharSequence projectName, int projectID) {
        this.mName = projectName;
        this.mID = projectID;

        //defaults for the project
        this.mDescription = "";
        this.mDateCreated = new Date();
        this.mLastModified = new Date();

    }
}
