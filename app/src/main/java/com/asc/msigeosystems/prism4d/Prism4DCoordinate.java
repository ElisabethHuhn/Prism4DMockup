package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 11/20/2016.
 *
 * This is the superclass of all coordinate types
 * It doesn't do much, as it will be subtypes that are actually instantiated.
 * This level just provides the capability for the subtypes to
 * identify the type of the instance that is actually instantiated
 */

public abstract class Prism4DCoordinate {

    public static final String sCoordinateTypeWGS84 = "WGS84 G1762";
    public static final String sCoordinateTypeNAD83 = "NAD83 2011";
    public static final String sCoordinateTypeUTM   = "UTM";
    public static final String sCoordinateTypeSPCS  = "SPCS";

    public static final String sCoordinateTypeClassWGS84 = "Prism4DCoordinateWGS84";
    public static final String sCoordinateTypeClassNAD83 = "Prism4DCoordinateNAD83";
    public static final String sCoordinateTypeClassUTM   = "Prism4DCoordinateUTM";
    public static final String sCoordinateTypeClassSPCS  = "Prism4DCoordinateSPCS";

    public static final int sUNKWidgets = 0;
    public static final int sLLWidgets = 1;
    public static final int sENWidgets = 2;

    protected CharSequence mThisCoordinateType = "Undefined";
    protected CharSequence mThisCoordinateClass = "Prism4DCoordinate";


    /***********************************************************/
    /*****    Variables common to ALL coordinates        *******/
    /***********************************************************/
    protected int mCoordinateID; //All coordinates have a DB ID
    protected int mProjectID; //May or may not describe a point
    protected int mPointID;   //These will be null if not describing a point


    /***********************************************************/
    /*****    Static methods                             *******/
    /***********************************************************/
    public static int getNextCoordinateID(){
        return (int) (System.currentTimeMillis() & 0xfffffff);
    }

    /***********************************************************/
    /*****    Setters and Getters for common variables   *******/
    /***********************************************************/


    //This method returns the type of the instance actually instantiated
    public CharSequence getCoordinateType() { return mThisCoordinateType; }

    //This method returns the type of the instance as a string for UI display
    public CharSequence getCoordinateClass(){ return mThisCoordinateClass; }


    public  int getCoordinateID(){return mCoordinateID;}
    public  void setCoordinateID(int coordinateID){this.mCoordinateID = coordinateID;}


    public int  getProjectID()              { return mProjectID; }
    public void setProjectID(int projectID) { mProjectID = projectID; }

    public int  getPointID()            { return mPointID; }
    public void setPointID(int pointID) { mPointID = pointID; }




    /***********************************************************/
    /*****     Methods common to ALL coordinates         *******/
    /***********************************************************/

    protected void initializeDefaultVariables() {
        //set all variables with defaults, so that none are null
        //I know that one does not have to initialize int's etc, but
        //to be explicit about the initialization, do it anyway

        mCoordinateID = Prism4DCoordinate.getNextCoordinateID();

        mProjectID = 0; //assume does not describe a point
        mPointID = 0;
    }



}
