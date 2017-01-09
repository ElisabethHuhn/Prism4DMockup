package com.asc.msigeosystems.prism4d;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Elisabeth Huhn on 5/15/2016.
 *
 * One of the principal Data Classes of the model
 * A Point is contained within one and only one project
 */
public class Prism4DPicture {

    /*****************************************************/
    /********    Static constants                *********/
    /*****************************************************/




    /*************************************/
    /*    Static (class) Variables       */
    /*************************************/


    /*************************************/
    /*    Member (instance) Variables    */
    /*************************************/

    /*****************************************************/
    /********    Attributes stored in the DB     *********/
    /*****************************************************/

    private String       mPictureID;
    private int          mProjectID;
    private int          mPointID;
    //Pathname is the directory plus file. This is all you need to determine the file
    private String       mPathName;
    //The file name is within the directory that is named after the project
    private String       mFileName;


    /****************************************************************/
    /*               Static Methods                                 */
    /****************************************************************/



    //Convert point to comma delimited file for exchange
    public String convertToCDF() {
        return String.valueOf(this.getProjectID()) + ", " +
               String.valueOf(this.getPointID() )  + ", " +
                this.getPathName()                 + ", " +
                this.getFileName()                 + ", " +

                System.getProperty("line.separator");
    }

    /*************************************/
    /*         CONSTRUCTORS              */

    /*************************************/

    /*****************************************************/
    /********    Constructors                    *********/
    /*****************************************************/
    public Prism4DPicture(){
        initializeDefaultVariables();
    }


    public Prism4DPicture(String timestamp, Prism4DProject project, Prism4DPoint point) {

        initializeDefaultVariables();
        //initialize all variables so we are assured that none are null
        //that way we never have to check for null later
        this.mPictureID = timestamp;
        this.mProjectID = project.getProjectID();
        if (point == null){
            this.mPointID = 0;
        } else {
            this.mPointID = point.getPointID();
        }
    }


    /*****************************************************/
    /********    Setters and Getters             *********/
    public String getPictureID() {
        return mPictureID;
    }

    public void setPictureID(String pictureID) {
        mPictureID = pictureID;
    }

    /*****************************************************/


    public int getProjectID() {
        return mProjectID;
    }
    public void setProjectID(int projectID) {
        mProjectID = projectID;
    }

    public int getPointID() {
        return mPointID;
    }
    public void setPointID(int pointID) {
        mPointID = pointID;
    }

    public String getPathName() {
        return mPathName;
    }
    public void setPathName(String pathName) {
        mPathName = pathName;
    }

    public String getFileName() {
        return mFileName;
    }
    public void setFileName(String fileName) {
        mFileName = fileName;
    }

/*****************************************************/
    /********    Private Member Methods          *********/
    /*****************************************************/

    private void initializeDefaultVariables(){
        this.mPictureID  = ";";
        this.mProjectID  = 0;
        this.mPointID    = 0;
        this.mPathName   = "";
        this.mFileName   = "";
    }



}
