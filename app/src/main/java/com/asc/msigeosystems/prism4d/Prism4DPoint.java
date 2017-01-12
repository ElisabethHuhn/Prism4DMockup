package com.asc.msigeosystems.prism4d;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Elisabeth Huhn on 5/15/2016.
 *
 * One of the principal Data Classes of the model
 * A Point is contained within one and only one project
 */
public class Prism4DPoint {

    /*****************************************************/
    /********    Static constants                *********/
    /*****************************************************/

    //Tags for fragment arguments
    public static final String sPointProjectIDTag  = "PROJECT_ID";
    public static final String sPointTag           = "POINT_OBJECT";
    public static final String sPointNameTag       = "POINT_NAME";
    public static final String sPointIDTag         = "POINT_ID";
    public static final String sPointEastingTag    = "POINT_EASTING";
    public static final String sPointNorthingTag   = "POINT_NORTHING";
    public static final String sPointElevationTag  = "POINT_ELEVATION";
    public static final String sPointFCTag         = "POINT_FEATURE_CODE";
    public static final String sPointNotesTag      = "POINT_NOTES";

    public static final int    sPointDefaultsID   = -1;
    public static final String sPointDefaultsDesc =
            "This point represents the defaults that all other projects start with";

    public static final int    sPointNewID   = -2;
    public static final String sPointNewDesc = "";



    /*************************************/
    /*    Static (class) Variables       */
    /*************************************/


    /*************************************/
    /*    Member (instance) Variables    */
    /*************************************/

    /*****************************************************/
    /********    Attributes stored in the DB     *********/
    /*****************************************************/

    private int          mPointID;
    private int          mForProjectID;

    //Actual location of point is given by Coordinate
    private int          mHasACoordinateID;
    private Prism4DCoordinate mCoordinate;

    //The original offset. The Coordinate has this offset calculated into it
    private double       mOffsetDistance;
    private double       mOffsetHeading;
    private double       mOffsetElevation;

    //Original height due to artificial means, e.g. tripod
    private double       mHeight;

    private CharSequence mPointFeatureCode;
    private CharSequence mPointNotes;


    private ArrayList<Prism4DPicture> mPictures;

    //Quality fields
    private double      mHdop;
    private double      mVdop;
    private double      mTdop;
    private double      mPdop;
    private double      mGdop;
    private double      mHrms;
    private double      mVrms;





    /****************************************************************/
    /*               Static Methods                                 */
    /****************************************************************/

    public static Bundle putPointInArguments(Bundle args, int projectID, Prism4DPoint point) {

        args.putInt         (Prism4DPoint.sPointProjectIDTag, projectID);
        if (point == null){
            //This  happens when the point is being created by this fragment on save
            args.putInt(Prism4DPoint.sPointIDTag,0);
        } else {
            args.putInt(Prism4DPoint.sPointIDTag, point.getPointID());
        }
        //assume all other attributes exist on the point being managed by the PointManager
        return args;
    }


    public static Prism4DPoint getPointFromArguments(Bundle args) {
        Prism4DPoint point;
        int projectID = args.getInt (Prism4DPoint.sPointProjectIDTag);
        int pointID   = args.getInt (Prism4DPoint.sPointIDTag);

        //If we are on the Create Path, we won't have a point here
        if (pointID != 0) {
            Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
                                point        = pointManager.getPoint(projectID, pointID);

            if (point == null) {
                throw new RuntimeException("PointID " + String.valueOf(point) +
                        " in ProjectID " + String.valueOf(projectID) +
                        " does NOT exist. Can not edit");
            }
        } else {
            //there is no existing point, it is being created

            //Start with an empty point with default values
            point = new Prism4DPoint();
            //set the bogus point ID
            point.setPointID(0);
            //save the project ID
            point.setForProjectID(projectID);
        }

        return point;
    }


    /*************************************/
    /*         CONSTRUCTORS              */

    /*************************************/

    /*****************************************************/
    /********    Constructors                    *********/
    /*****************************************************/
    //Need to know what project the new point will be in
    public Prism4DPoint(Prism4DProject project) {

        initializeDefaultVariables();
        //initialize all variables so we are assured that none are null
        //that way we never have to check for null later
        this.mForProjectID = project.getProjectID();
        this.mPointID      = project.getNextPointID();
    }


    //a coule of special case points flag "create new point" and "open a point" path
    public Prism4DPoint(int specialPointID) {
        initializeDefaultVariables();

        this.mForProjectID     = specialPointID;
        this.mPointID          = specialPointID ;
        this.mPointFeatureCode = "A special point";
    }

    public Prism4DPoint() {
        initializeDefaultVariables();
    }



    /*****************************************************/
    /********    Setters and Getters             *********/
    /*****************************************************/



    public int getForProjectID()                  {  return mForProjectID;    }
    public void setForProjectID(int forProjectID) {  this.mForProjectID = forProjectID;  }

    public int  getPointID()             {  return mPointID;    }
    public void setPointID(int pointID) {  this.mPointID = pointID;  }

    public int getHasACoordinateID()                    {return mHasACoordinateID; }
    public void setHasACoordinateID(int isACoordinateID) { mHasACoordinateID = isACoordinateID; }

    public Prism4DCoordinate getCoordinate()                { return mCoordinate;  }
    public void setCoordinate(Prism4DCoordinate coordinate) { mCoordinate = coordinate; }


    public double getOffsetDistance() {  return mOffsetDistance;   }
    public void setOffsetDistance(double offsetDistance) {  mOffsetDistance = offsetDistance; }

    public double getOffsetHeading() {   return mOffsetHeading;}
    public void setOffsetHeading(double offsetHeading) { mOffsetHeading = offsetHeading;   }

    public double getOffsetElevation() {  return mOffsetElevation;  }
    public void setOffsetElevation(double offsetElevation) { mOffsetElevation = offsetElevation; }


    public double getHeight()              {  return mHeight; }
    public void   setHeight(double height) { mHeight = height; }

    public CharSequence getPointFeatureCode() { return mPointFeatureCode;  }
    public void setPointFeatureCode(CharSequence description) { mPointFeatureCode = description;  }

    public CharSequence getPointNotes() {  return mPointNotes;   }
    public void setPointNotes(CharSequence notes) { mPointNotes = notes;   }

    public double getHdop() {
        return mHdop;
    }
    public void   setHdop(double hdop) {
        mHdop = hdop;
    }

    public double getVdop() {
        return mVdop;
    }
    public void   setVdop(double vdop) {
        mVdop = vdop;
    }

    public double getTdop() {
        return mTdop;
    }
    public void   setTdop(double tdop) {
        mTdop = tdop;
    }

    public double getPdop() {
        return mPdop;
    }
    public void   setPdop(double pdop) {
        mPdop = pdop;
    }

    public double getGdop() {
        return mGdop;
    }
    public void   setGdop(double gdop) {
        mGdop = gdop;
    }

    public double getHrms() {
        return mHrms;
    }
    public void   setHrms(double hrms) {
        mHrms = hrms;
    }

    public double getVrms() {
        return mVrms;
    }
    public void   setVrms(double vrms) {
        mVrms = vrms;
    }

    public ArrayList<Prism4DPicture> getPictures()                   {   return mPictures; }
    public void   setPictures(ArrayList<Prism4DPicture> pictures) {  mPictures = pictures; }

    public boolean addPicture(Prism4DPicture picture){
        return getPictures().add(picture);
    }

    /*****************************************************/
    /********    Private Member Methods          *********/
    /*****************************************************/

    private void initializeDefaultVariables(){
        this.mForProjectID     = 0;
        this.mPointID          = 0;
        this.mHasACoordinateID = 0;
        this.mCoordinate       = null;

        this.mOffsetDistance   = 0d;
        this.mOffsetHeading    = 0d;
        this.mOffsetElevation  = 0d;

        this.mHeight           = 0d;

        this.mPointFeatureCode = "";
        this.mPointNotes       = "";

          this.mPictures         = new ArrayList<>();
        this.mHdop = 0d;
        this.mVdop = 0d;
        this.mTdop = 0d;
        this.mPdop = 0d;
        this.mGdop = 0d;
        this.mHrms = 0d;
        this.mVrms = 0d;

    }


    //Convert point to comma delimited file for exchange
    public String convertToCDF() {
        return String.valueOf(this.getPointID()) + ", " +
                this.getPointFeatureCode()       + ", " +
                this.getPointNotes()             + ", " +
                this.getHdop()                   + ", " +
                this.getVdop()                   + ", " +
                this.getTdop()                   + ", " +
                this.getPdop()                   + ", " +
                this.getHrms()                   + ", " +
                this.getVrms()                   + ", " +
                this.getOffsetDistance()         + ", " +
                this.getOffsetHeading()          + ", " +
                this.getOffsetElevation()        + ", " +
                this.getHeight()                 + ", " +
                //todo 12/13/2016 have to add in coordinates here
                "plus coordinate positions "      +
                System.getProperty("line.separator");
    }


}
