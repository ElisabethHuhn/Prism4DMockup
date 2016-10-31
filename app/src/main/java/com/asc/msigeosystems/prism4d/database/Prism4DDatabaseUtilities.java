package com.asc.msigeosystems.prism4d.database;

import android.content.ContentValues;

import com.asc.msigeosystems.prism4d.Prism4DProject;

/**
 * Created by elisabethhuhn on 9/5/2016.
 *  The following are utilities for
 o converting Prism4D Objects into ContentValue's
 o converting Cursor Rows into Prism4D Objects
 */
public class Prism4DDatabaseUtilities {

    /*****************
     * This routine converts a Prism4dProject object
     * that is used by the App, into
     * a ContentValues containing that same project
     * so that it can be added to the database
     *
     * @return ContentValues containing the given project
     */
    public static ContentValues dbConvert(Prism4DProject project) {
        ContentValues cv = new ContentValues();
        cv.put(Prism4DSqliteOpenHelper.PROJECT_ID, project.getProjectID());
        cv.put(Prism4DSqliteOpenHelper.PROJECT_NAME, project.getProjectName().toString());
        //dates in Prism4D are Date, and in the DB are Integers representing the
        //  number of milliseconds since Jan 1, 1970 00:00:00 gmt
        cv.put(Prism4DSqliteOpenHelper.PROJECT_CREATED, project.getProjectDateCreated().getTime());
        cv.put(Prism4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED, project.getProjectDateCreated().getTime());
        cv.put(Prism4DSqliteOpenHelper.PROJECT_DESCRIPTION, project.getProjectDescription().toString());

        return cv;
    }



}
