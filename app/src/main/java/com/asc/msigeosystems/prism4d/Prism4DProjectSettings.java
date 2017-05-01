package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 5/7/2016.
 * Settings that govern how the App behaves
 */
public class Prism4DProjectSettings {

    public static final int sMeanByTime = 0;
    public static final int sMeanByNumber = 1;


    //Constants for enumerated types
    public static final CharSequence sUIDistanceUSFeet  = "US Survey Feet";
    public static final CharSequence sUIDistanceIntFeet = "International Feet";
    public static final CharSequence sUIDistanceMeters  = "Meters";
    public static final int sDBDistanceMeters  = 0;
    public static final int sDBDistanceUSFeet  = 1;
    public static final int sDBDistanceIntFeet = 2;

    public static final CharSequence sUIDecimalDisplayCommas   = "123,456,789.00";
    public static final CharSequence sUIDecimalDisplayNoCommas = "123456789.00";
    public static final int sDBDecimalDisplayCommas = 0;
    public static final int sDBDecimalDisplayNoCommas = 1;

    public static final CharSequence sUIAngleUnitsDeg = "Degrees";
    public static final CharSequence sUIAngleUnitsRad = "Radians";
    public static final int sDBAngleUnitsDeg = 0;
    public static final int sDBAngleUnitsRad = 1;

    public static final CharSequence sUIAngleDisplayDD   = "123.456789";//Decimal Degrees
    public static final CharSequence sUIAngleDisplayDM   = "123 45.11'";//Degrees Minutes
    public static final CharSequence sUIAngleDisplayDMS  = "123 45' 11\"";//Degrees Minutes Seconds
    public static final CharSequence sUIAngleDisplayGONS = "Gons????";
    public static final CharSequence sUIAngleDisplayMils = "MILs?????";
    public static final int sDBAngleDisplayDD   = 0;
    public static final int sDBAngleDisplayDM   = 1;//Degrees Minutes
    public static final int sDBAngleDisplayDMS  = 2;
    public static final int sDBAngleDisplayGONS = 3;
    public static final int sDBAngleDisplayMils = 4;


    public static final CharSequence sUIGridDirectionN = "North Azimuth";
    public static final CharSequence sUIGridDirectionS = "South Azimuth";
    public static final CharSequence sUIGridDirectionE = "East Azimuth";
    public static final CharSequence sUIGridDirectionW = "West Azimuth";
    public static final int sDBGridDirectionN = 0;
    public static final int sDBGridDirectionS = 1;
    public static final int sDBGridDirectionE = 2;
    public static final int sDBGridDirectionW = 3;

    //scale factor is double

    public static final CharSequence sUISeaLevelTrue  = "True";
    public static final CharSequence sUISeaLevelFalse = "False";
    public static final int sDBSeaLevelTrue  = 0;
    public static final int sDBSeaLevelFalse = 1;

    public static final CharSequence sUIRefractionTrue  = "True";
    public static final CharSequence sUIRefractionFalse = "False";
    public static final int sDBRefractionTrue  = 0;
    public static final int sDBRefractionFalse = 1;


    public static final CharSequence sUIDatumWGS = Prism4DCoordinate.sCoordinateTypeWGS84;
    public static final CharSequence sUIDatumNAD = Prism4DCoordinate.sCoordinateTypeNAD83;
    public static final int sDBDatumWGS  = 0;
    public static final int sDBDatumNAD = 1;


    public static final CharSequence sUIProjectionNone = "No Projection";
    public static final CharSequence sUIProjectionUTM = Prism4DCoordinate.sCoordinateTypeUTM;
    public static final CharSequence sUIProjectionSPC = Prism4DCoordinate.sCoordinateTypeClassSPCS;
    public static final int sDBProjectionNone = 0;
    public static final int sDBProjectionUTM  = 1;
    public static final int sDBProjectionSPC  = 2;


    public static final CharSequence sUIZoneNone = "Not SPCS";
    public static final CharSequence sUIZoneAl  = "Alabama";
    public static final CharSequence sUIZoneGaW = "Georgia West (1002)";
    public static final int sDBZoneNone = 0;
    public static final int sDBZoneAl   = 1;
    public static final int sDBZoneGaW   = 2;

    //spc scale factor is double value

    public static final CharSequence sUICoordinateDisplayNE = "Northing/Easting";//Northing first
    public static final CharSequence sUICoordinateDisplayEN = "Easting/Northing";//Easting first
    public static final int sDBCoordinateDisplayNE = 0;
    public static final int sDBCoordinateDisplayEN = 1;

    public static final CharSequence sUIGeoidModelNone = "None";
    public static final CharSequence sUIGeoidModel99  = "GEOID99";
    public static final int sDBGeoidModelNone = 0;
    public static final int sDBGeoidModel99   = 1;


    public static final CharSequence sUIAlphanumericIDTrue  = "True";
    public static final CharSequence sUIAlphanumericIDFalse = "False";
    public static final int sDBAlphanumericIDTrue  = 0;
    public static final int sDBAlphanumericIDFalse = 1;


    public static final CharSequence sUITimeStampTrue  = "True";
    public static final CharSequence sUITimeStampFalse = "False";
    public static final int sDBTimeStampTrue  = 0;
    public static final int sDBTimeStampFalse = 1;


    public static final CharSequence sUIFeatureCodeTrue  = "True";
    public static final CharSequence sUIFeatureCodeFalse = "False";
    public static final int sDBFeatureCodeTrue  = 0;
    public static final int sDBFeatureCodeFalse = 1;



    //starting point ID is integer


    //If Project ID = -1, these are the global defaults for Project Settings
    private int          mProjectID; //The project these settings belong to
    private int          mDistanceUnits ;
    private int          mDecimalDisplay ;
    private int          mAngleUnits ;
    private int          mAngleDisplay ;
    private int          mGridDirection  ;
    private double       mScaleFactor  ;
    private boolean      mSeaLevel  ;
    private boolean      mRefraction ;
    private int          mDatum ;
    private int          mProjection ;
    private int          mZone  ;
    private double       mSpcScaleFactor  ;
    private int          mCoordinateDisplay;
    private int          mGeoidModel ;
    private int          mStartingPointID ;
    private boolean      mAlphanumericID;
    private boolean      mFeatureCodes;
    //private CharSequence mFCControlFile;
    private boolean      mFCTimeStamp;

    private int          mMeaningMethod = sMeanByNumber;
    private int          mMeaningNumber; //either the number of readings or the number of seconds
    //Digits of precision are for the UI only. Underlying numbers are not truncated
    // TODO: 12/13/2016 Should underlying values be truncated to precision digits? 
    private int          mLocationPrecision; //# digits of precision for locations eg Lat/Long or N/E
    private int          mElevationPrecision;
    private int          mStdDevPrecision;//precision of standard deviations


    /********
     *
     * Setters and Getters
     *
     ********/
    public int getProjectID() {
        return mProjectID;
    }
    public void setProjectID(int projectID) {
        this.mProjectID = projectID;
    }

    public int getDistanceUnits() {
        return mDistanceUnits;
    }
    public void setDistanceUnits(int distanceUnits) {this.mDistanceUnits = distanceUnits;}

    public int getDecimalDisplay() {
        return mDecimalDisplay;
    }
    public void setDecimalDisplay(int decimalDisplay) {mDecimalDisplay = decimalDisplay; }

    public int getAngleUnits() {
        return mAngleUnits;
    }
    public void setAngleUnits(int angleUnits) { mAngleUnits = angleUnits; }

    public int getAngleDisplay() {
        return mAngleDisplay;
    }
    public void setAngleDisplay(int angleDisplay) { mAngleDisplay = angleDisplay; }

    public int getGridDirection() {
        return mGridDirection;
    }
    public void setGridDirection(int gridDirection) { mGridDirection = gridDirection; }

    public double getScaleFactor() {
        return mScaleFactor;
    }
    public void setScaleFactor(double scaleFactor) {mScaleFactor = scaleFactor;   }

    public boolean isSeaLevel() {
        return mSeaLevel;
    }
    public void setSeaLevel(boolean seaLevel) { mSeaLevel = seaLevel;  }

    public boolean isRefraction() {
        return mRefraction;
    }
    public void setRefraction(boolean refraction) { mRefraction = refraction;}

    public int getDatum() {
        return mDatum;
    }
    public void setDatum(int datum) { mDatum = datum; }

    public int getProjection() {
        return mProjection;
    }
    public void setProjection(int projection) { mProjection = projection; }

    public int getZone() {
        return mZone;
    }
    public void setZone(int zone) {  mZone = zone; }

    public double getSpcScaleFactor() {return mSpcScaleFactor;}
    public void   setSpcScaleFactor(double scaleFactor) {mSpcScaleFactor = scaleFactor;}

    public int getCoordinateDisplay() {
        return mCoordinateDisplay;
    }
    public void setCoordinateDisplay(int coordinateDisplay) {
        mCoordinateDisplay = coordinateDisplay;
    }

    public int getGeoidModel() {
        return mGeoidModel;
    }
    public void setGeoidModel(int geoidModel) { mGeoidModel = geoidModel; }

    public int getStartingPointID() {
        return mStartingPointID;
    }
    public void setStartingPointID(int startingPointID) {
        mStartingPointID = startingPointID;
    }

    public boolean isAlphanumericID() {
        return mAlphanumericID;
    }
    public void setAlphanumericID(boolean alphanumericID) { mAlphanumericID = alphanumericID; }

    public boolean isFeatureCodes() {
        return mFeatureCodes;
    }
    public void setFeatureCodes(boolean featureCodes) { mFeatureCodes = featureCodes; }

/*
    public CharSequence getFCControlFile() { return mFCControlFile; }
    public void setFCControlFile(CharSequence fcControlFile){mFCControlFile = fcControlFile;}
 */

    public boolean isFCTimeStamp() {
        return mFCTimeStamp;
    }
    public void setFCTimeStamp(boolean fcTimeStamp) {mFCTimeStamp = fcTimeStamp;}

    public int getMeaningMethod() {  return mMeaningMethod; }
    public void setMeaningMethod(int meaningMethod) { mMeaningMethod = meaningMethod; }

    public int getMeaningNumber() { return mMeaningNumber;}
    public void setMeaningNumber(int meaningNumber) {  mMeaningNumber = meaningNumber; }

    public int getLocationPrecision() {       return mLocationPrecision; }
    public void setLocationPrecision(int locationPrecision) {mLocationPrecision = locationPrecision; }

    public int getElevationPrecision() { return mElevationPrecision;}
    public void setElevationPrecision(int elevationPrecision) {mElevationPrecision = elevationPrecision;}

    public int getStdDevPrecision() {       return mStdDevPrecision; }
    public void setStdDevPrecision(int stdDevPrecision) {mStdDevPrecision = stdDevPrecision; }

    /*
     *
     * Constructor
     *
     */

    //this creates a new instance with values defined by global defaults
    public Prism4DProjectSettings() {
        setDefaults();
    }

    public Prism4DProjectSettings(int projectID){
        setDefaults();
        mProjectID = projectID;
    }

    /*
     *
     * Other Methods of the class
     *
     */
    public void setDefaults() {


        //Restore default values
        mProjectID      = -1; //by default, the default settings
        mDistanceUnits  = sDBDistanceMeters;
        mDecimalDisplay = sDBDecimalDisplayCommas;
        mAngleUnits     = sDBAngleUnitsDeg;
        mAngleDisplay   = sDBAngleDisplayDD;
        mGridDirection  = sDBGridDirectionN;
        mScaleFactor    = .99982410;
        mSeaLevel       = false;
        mRefraction     = false;
        mDatum          = sDBDatumWGS;
        mProjection     = sDBProjectionSPC;
        mZone           = sDBZoneGaW;
        mCoordinateDisplay = sDBCoordinateDisplayEN;
        mGeoidModel      = sDBGeoidModel99;
        mStartingPointID = 1001;
        mAlphanumericID  = false;
        mFeatureCodes    = true;
        //mFCControlFile   = "CP2";
        mFCTimeStamp     = false;

        mMeaningMethod   = sMeanByNumber;
        mMeaningNumber   = 180;
        mLocationPrecision = Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision;
        mElevationPrecision = Prism4DConstantsAndUtilities.sHundredthsDigitsOfPrecision;
        mStdDevPrecision   = Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision;
    }




    public String convertToCDF(){

        String msg =
                String.valueOf(this.getProjectID()) + ", " +
                this.getDistanceUnits()             + ", " +
                this.getDecimalDisplay()            + ", " +
                this.getAngleUnits()                + ", " +
                this.getAngleDisplay()              + ", " +
                this.getGridDirection()             + ", " +
                String.valueOf(this.getScaleFactor())  + ", ";

                if (isSeaLevel()){        msg = msg + "true, "; }
                else {                    msg = msg + "false, ";}
                if (isRefraction()){      msg = msg + "true, "; }
                else {                    msg = msg + "false, ";}

        msg = msg +
                this.getDatum()                     + ", " +
                this.getProjection()                + ", " +
                this.getZone()                      + ", " +
                this.getCoordinateDisplay()         + ", " +
                this.getGeoidModel()                + ", " +
                this.getStartingPointID()           + ", " ;

                if (isAlphanumericID()){    msg = msg + "true, "; }
                else {                    msg = msg + "false, ";}

                if (isFeatureCodes()){    msg = msg + "true, "; }
                else {                    msg = msg + "false, ";}

                //this.getFCControlFile()             + ", " ;

                if (isFCTimeStamp()){     msg = msg + "true, "; }
                else {                    msg = msg + "false, ";}
        msg = msg +
                String.valueOf(this.getMeaningMethod() )      + ", " +
                String.valueOf(this.getMeaningNumber() )      + ", " +
                String.valueOf(this.getLocationPrecision() )  + ", " +
                String.valueOf(this.getElevationPrecision())  + ", " +
                String.valueOf(this.getStdDevPrecision() )    + ", " ;

                System.getProperty("line.separator");
        return msg;
    }

}
