package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 12/3/2016.
 *
 * this class holds information about the hardware environment the App must function within
 */
public class Prism4DConfigurations {


    //
    private CharSequence mLocationMode; //This indicates whether the app data is from
                                        // a Total Station or GPS source
    private boolean      mIsPhone; //indicates GPS source: true = phone, false =a peripherial
    private CharSequence mPeripheralModel ;
    private CharSequence mPeripheralSN;
    private CharSequence mPeripheralTracking;
    private CharSequence mPeripheralMode;





    /********
     *
     * Setters and Getters
     *
     ********/


    public CharSequence getLocationMode() {
        return mLocationMode;
    }
    public void setLocationMode(CharSequence locationMode) {this.mLocationMode = locationMode;}

    public CharSequence getmDecimalDisplay() {
        return mPeripheralModel;
    }

    public void setmDecimalDisplay(CharSequence mDecimalDisplay) {
        this.mPeripheralModel = mDecimalDisplay;
    }

    public CharSequence getmAngleUnits() {
        return mPeripheralSN;
    }

    public void setmAngleUnits(CharSequence mAngleUnits) {
        this.mPeripheralSN = mAngleUnits;
    }

    public CharSequence getmAngleDisplay() {
        return mPeripheralTracking;
    }

    public void setmAngleDisplay(CharSequence mAngleDisplay) {
        this.mPeripheralTracking = mAngleDisplay;
    }

    public CharSequence getmGridDirection() {
        return mPeripheralMode;
    }

    public void setmGridDirection(CharSequence mGridDirection) {
        this.mPeripheralMode = mGridDirection;
    }


    /*
     *
     * Constructor
     *
     */

    //this creates a new instance with values defined by global defaults
    public Prism4DConfigurations() {
        setDefaults();
    }

    /*
     *
     * Other Methods of the class
     *
     */
    public void setDefaults() {
/*
        //Restore default values
        mLocationMode = "US Survey Feet";
        mPeripheralModel = "123,456,789.00";
        mPeripheralSN = "Degrees";
        mPeripheralTracking = "123 45' 11";
        mPeripheralMode = "North Azimuth";
        mScaleFactor    = .99982410;
        mIsPhone = false;
*/
    }
}
