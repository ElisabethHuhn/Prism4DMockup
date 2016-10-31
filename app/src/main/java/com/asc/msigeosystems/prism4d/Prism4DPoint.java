package com.asc.msigeosystems.prism4d;

/**
 * Created by elisabethhuhn on 5/15/2016.
 * One of the principal Data Classes of the model
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


    /*****************************************************/
    /********    Attributes stored in the DB     *********/
    /*****************************************************/

    private int          mProjectID;
    private int          mPointID;

    private double       mPointEasting;
    private double       mPointNorthing;
    private double       mPointElevation;
    private CharSequence mPointDescription;
    private CharSequence mPointNotes;


    // TODO: 10/31/2016 Consider adding a List of the Coordinates that describe this point


    /*****************************************************/
    /********    Setters and Getters             *********/
    /*****************************************************/



    public int  getProjectID()             {  return mProjectID;    }
    public void setProjectID(int projectID) {  this.mProjectID = projectID;  }

    public int  getPointID()             {  return mPointID;    }
    public void setPointID(int pointID) {  this.mPointID = pointID;  }


    public double getPointEasting() { return mPointEasting;  }
    public void   setPointEasting(double easting) { this.mPointEasting = easting;   }

    public double getPointNorthing() { return mPointNorthing; }
    public void   setPointNorthing(double northing) { this.mPointNorthing = northing; }

    public double getPointElevation() { return mPointElevation;  }
    public void   setPointElevation(double elevation) { this.mPointElevation = elevation;  }

    public CharSequence getPointDescription() { return mPointDescription;  }
    public void setPointDescription(CharSequence description) { mPointDescription = description;  }

    public CharSequence getPointNotes() {  return mPointNotes;   }
    public void setPointNotes(CharSequence notes) { mPointNotes = notes;   }


    /*****************************************************/
    /********    Constructors                    *********/
    /*****************************************************/
    //Need to know what project the new point will be in
    public Prism4DPoint(Prism4DProject project) {

        initializeDefaultVariables();
        //initialize all variables so we are assured that none are null
        //that way we never have to check for null later
        this.mProjectID = project.getProjectID();
        this.mPointID = project.getNextPointID();
    }


     //Need to know what project the new point will be in
    public Prism4DPoint(Prism4DProject project, double easting, double northing, double elevation) {
        initializeDefaultVariables();

        this.mProjectID = project.getProjectID();
        this.mPointID = project.getNextPointID() ;
    }

    //a coule of special case points flag "create new point" and "open a point" path
    public Prism4DPoint(int specialPointID) {
        initializeDefaultVariables();

        this.mProjectID        = specialPointID;
        this.mPointID          = specialPointID ;

        this.mPointDescription = "A special point";
    }



    /*****************************************************/
    /********    Private Member Methods          *********/
    /*****************************************************/

    private void initializeDefaultVariables(){
        this.mProjectID        = 0;
        this.mPointID          = 0;
        this.mPointEasting     = 0.0;
        this.mPointNorthing    = 0.0;
        this.mPointElevation   = 5.0;
        this.mPointDescription = "";
        this.mPointNotes       = "";

    }

}
