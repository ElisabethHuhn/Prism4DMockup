package com.asc.msigeosystems.prism4d;

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
    public static final String sPointDescTag       = "POINT_DESCRIPTION";
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
    private int          mHasACoordinateID;

    private CharSequence mPointDescription;
    private CharSequence mPointNotes;

    //Actual location of point is given by Coordinates
    private Prism4DCoordinateNAD83 mNad83Coordinate;
    private Prism4DCoordinateWGS84 mWgs84Coordinate;
    private Prism4DCoordinateUTM   mUtmCoordinate;
    private Prism4DCoordinateSPCS  mSpcsCoordinate;



    /*************************************/
    /*         Static Methods            */
    /*************************************/


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
        this.mPointDescription = "A special point";
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

    public CharSequence getPointDescription() { return mPointDescription;  }
    public void setPointDescription(CharSequence description) { mPointDescription = description;  }

    public CharSequence getPointNotes() {  return mPointNotes;   }
    public void setPointNotes(CharSequence notes) { mPointNotes = notes;   }



    /*****************************************************/
    /********    Private Member Methods          *********/
    /*****************************************************/

    private void initializeDefaultVariables(){
        this.mForProjectID     = 0;
        this.mPointID          = 0;
        this.mHasACoordinateID = 0;
        this.mPointDescription = "";
        this.mPointNotes       = "";

        this.mNad83Coordinate  = null;
        this.mWgs84Coordinate  = null;
        this.mUtmCoordinate    = null;
        this.mSpcsCoordinate   = null;

    }

}
