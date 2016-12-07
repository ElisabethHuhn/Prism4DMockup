package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 5/7/2016.
 * Settings that govern how the App behaves
 */
public class Prism4DProjectSettings {


    //If Project ID = -1, these are the global defaults for Project Settings
    private int          mProjectID; //The project these settings belong to
    private CharSequence mDistanceUnits ;
    private CharSequence mDecimalDisplay ;
    private CharSequence mAngleUnits ;
    private CharSequence mAngleDisplay ;
    private CharSequence mGridDirection  ;
    private double       mScaleFactor  ;
    private boolean      mSeaLevel  ;
    private boolean      mRefraction ;
    private CharSequence mDatum ;
    private CharSequence mProjection ;
    private CharSequence mZone  ;
    private double       mSpcScaleFactor  ;
    private CharSequence mCoordinateDisplay;
    private CharSequence mGeoidModel ;
    private CharSequence mStartingPointID ;
    private boolean      mAlphanumeric ;
    private CharSequence mFeatureCodes;
    private CharSequence mFCControlFile;
    private boolean      mFCTimeStamp;


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

    public CharSequence getDistanceUnits() {
        return mDistanceUnits;
    }
    public void setDistanceUnits(CharSequence distanceUnits) {this.mDistanceUnits = distanceUnits;}

    public CharSequence getDecimalDisplay() {
        return mDecimalDisplay;
    }
    public void setDecimalDisplay(CharSequence decimalDisplay) {mDecimalDisplay = decimalDisplay; }

    public CharSequence getAngleUnits() {
        return mAngleUnits;
    }
    public void setAngleUnits(CharSequence angleUnits) { mAngleUnits = angleUnits; }

    public CharSequence getAngleDisplay() {
        return mAngleDisplay;
    }
    public void setAngleDisplay(CharSequence angleDisplay) { mAngleDisplay = angleDisplay; }

    public CharSequence getGridDirection() {
        return mGridDirection;
    }
    public void setGridDirection(CharSequence gridDirection) { mGridDirection = gridDirection; }

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

    public CharSequence getDatum() {
        return mDatum;
    }
    public void setDatum(CharSequence datum) { mDatum = datum; }

    public CharSequence getProjection() {
        return mProjection;
    }
    public void setProjection(CharSequence projection) { mProjection = projection; }

    public CharSequence getZone() {
        return mZone;
    }
    public void setZone(CharSequence zone) {  mZone = zone; }

    public CharSequence getCoordinateDisplay() {
        return mCoordinateDisplay;
    }
    public void setCoordinateDisplay(CharSequence coordinateDisplay) {
        mCoordinateDisplay = coordinateDisplay;
    }

    public CharSequence getGeoidModel() {
        return mGeoidModel;
    }
    public void setGeoidModel(CharSequence geoidModel) { mGeoidModel = geoidModel; }

    public CharSequence getStartingPointID() {
        return mStartingPointID;
    }
    public void setStartingPointID(CharSequence startingPointID) {
        mStartingPointID = startingPointID;
    }

    public boolean isAlphanumeric() {
        return mAlphanumeric;
    }
    public void setAlphanumeric(boolean alphanumeric) { mAlphanumeric = alphanumeric; }

    public CharSequence getFeatureCodes() {
        return mFeatureCodes;
    }
    public void setFeatureCodes(CharSequence featureCodes) { mFeatureCodes = featureCodes; }

    public CharSequence getFCControlFile() {
        return mFCControlFile;
    }
    public void setFCControlFile(CharSequence fcControlFile){mFCControlFile = fcControlFile;}

    public boolean isFCTimeStamp() {
        return mFCTimeStamp;
    }
    public void setFCTimeStamp(boolean fcTimeStamp) {mFCTimeStamp = fcTimeStamp;}

    /*
     *
     * Constructor
     *
     */

    //this creates a new instance with values defined by global defaults
    public Prism4DProjectSettings() {
        setDefaults();
    }

    /*
     *
     * Other Methods of the class
     *
     */
    public void setDefaults() {

        //Restore default values
        mProjectID      = -1; //by default, the default settings
        mDistanceUnits  = "US Survey Feet";
        mDecimalDisplay = "123,456,789.00";
        mAngleUnits     = "Degrees";
        mAngleDisplay   = "123 45' 11";
        mGridDirection  = "North Azimuth";
        mScaleFactor    = .99982410;
        mSeaLevel       = false;
        mRefraction     = false;
        mDatum          = "NAD 1983 (2011)";
        mProjection     = "US State Plane Coordinates";
        mZone           = "Georgia West (1002)";
        mCoordinateDisplay = "North, East";
        mGeoidModel      = "GEOID99";
        mStartingPointID = "1001";
        mAlphanumeric    = false;
        mFeatureCodes    = "RHM2";
        mFCControlFile   = "CP2";
        mFCTimeStamp     = false;
    }
}
