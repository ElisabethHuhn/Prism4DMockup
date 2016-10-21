package com.asc.msigeosystems.prism4d;

/**
 * Created by elisabethhuhn on 5/7/2016.
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
    public int getmProjectID() {
        return mProjectID;
    }

    public void setmProjectID(int mProjectID) {
        this.mProjectID = mProjectID;
    }

    public CharSequence getmDistanceUnits() {
        return mDistanceUnits;
    }

    public void setmDistanceUnits(CharSequence mDistanceUnits) {
        this.mDistanceUnits = mDistanceUnits;
    }

    public CharSequence getmDecimalDisplay() {
        return mDecimalDisplay;
    }

    public void setmDecimalDisplay(CharSequence mDecimalDisplay) {
        this.mDecimalDisplay = mDecimalDisplay;
    }

    public CharSequence getmAngleUnits() {
        return mAngleUnits;
    }

    public void setmAngleUnits(CharSequence mAngleUnits) {
        this.mAngleUnits = mAngleUnits;
    }

    public CharSequence getmAngleDisplay() {
        return mAngleDisplay;
    }

    public void setmAngleDisplay(CharSequence mAngleDisplay) {
        this.mAngleDisplay = mAngleDisplay;
    }

    public CharSequence getmGridDirection() {
        return mGridDirection;
    }

    public void setmGridDirection(CharSequence mGridDirection) {
        this.mGridDirection = mGridDirection;
    }

    public double getmScaleFactor() {
        return mScaleFactor;
    }

    public void setmScaleFactor(double mScaleFactor) {
        this.mScaleFactor = mScaleFactor;
    }

    public boolean ismSeaLevel() {
        return mSeaLevel;
    }

    public void setmSeaLevel(boolean mSeaLevel) {
        this.mSeaLevel = mSeaLevel;
    }

    public boolean ismRefraction() {
        return mRefraction;
    }

    public void setmRefraction(boolean mRefraction) {
        this.mRefraction = mRefraction;
    }

    public CharSequence getmDatum() {
        return mDatum;
    }

    public void setmDatum(CharSequence mDatum) {
        this.mDatum = mDatum;
    }

    public CharSequence getmProjection() {
        return mProjection;
    }

    public void setmProjection(CharSequence mProjection) {
        this.mProjection = mProjection;
    }

    public CharSequence getmZone() {
        return mZone;
    }

    public void setmZone(CharSequence mZone) {
        this.mZone = mZone;
    }

    public CharSequence getmCoordinateDisplay() {
        return mCoordinateDisplay;
    }

    public void setmCoordinateDisplay(CharSequence mCoordinateDisplay) {
        this.mCoordinateDisplay = mCoordinateDisplay;
    }

    public CharSequence getmGeoidModel() {
        return mGeoidModel;
    }

    public void setmGeoidModel(CharSequence mGeoidModel) {
        this.mGeoidModel = mGeoidModel;
    }

    public CharSequence getmStartingPointID() {
        return mStartingPointID;
    }

    public void setmStartingPointID(CharSequence mStartingPointID) {
        this.mStartingPointID = mStartingPointID;
    }

    public boolean ismAlphanumeric() {
        return mAlphanumeric;
    }

    public void setmAlphanumeric(boolean mAlphanumeric) {
        this.mAlphanumeric = mAlphanumeric;
    }

    public CharSequence getmFeatureCodes() {
        return mFeatureCodes;
    }

    public void setmFeatureCodes(CharSequence mFeatureCodes) {
        this.mFeatureCodes = mFeatureCodes;
    }

    public CharSequence getmFCControlFile() {
        return mFCControlFile;
    }

    public void setmFCControlFile(CharSequence mFCControlFile) {
        this.mFCControlFile = mFCControlFile;
    }

    public boolean ismFCTimeStamp() {
        return mFCTimeStamp;
    }

    public void setmFCTimeStamp(boolean mFCTimeStamp) {
        this.mFCTimeStamp = mFCTimeStamp;
    }

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
