package com.asc.msigeosystems.prism4d;

import java.util.ArrayList;

/**
 * Created by Elisabeth Huhn on 12/7/2016.
 * Holds test locations for Collect Points
 */

public class Prism4DTestLocationData {
    private int    mLatitudeDegrees;
    private int    mLatitudeMinutes;
    private double mLatitudeSeconds;
    private int    mLongitudeDegrees;
    private int    mLongitudeMinutes;
    private double mLongitudeSeconds;
    private double mGeoid;
    private double mNorthing;
    private double mEasting;
    private double mElevation;



    /************************************/
    /********* Static Methods   *********/
    /************************************/



    /************************************/
    /********* Constructors     *********/
    /************************************/
    public Prism4DTestLocationData() {
        initializeDefaultData();

    }


    /************************************/
    /********* Setters/Getters  *********/
    /************************************/

    public int    getLatitudeDegrees()                  { return mLatitudeDegrees; }
    public void   setLatitudeDegrees(int    latitude)   { mLatitudeDegrees = latitude; }

    public int    getLatitudeMinutes()                  { return mLatitudeMinutes; }
    public void   setLatitudeMinutes(int    latitude)   { mLatitudeMinutes = latitude; }

    public double getLatitudeSeconds()                  { return mLatitudeSeconds; }
    public void   setLatitudeSeconds(double latitude)   { mLatitudeSeconds = latitude; }

    public int    getLongitudeDegrees()                 { return mLongitudeDegrees; }
    public void   setLongitudeDegrees(int    longitude) { mLongitudeDegrees = longitude; }

    public int    getLongitudeMinutes()                 { return mLongitudeMinutes; }
    public void   setLongitudeMinutes(int    longitude) { mLongitudeMinutes = longitude; }

    public double getLongitudeSeconds()                 { return mLongitudeSeconds; }
    public void   setLongitudeSeconds(double longitude) { mLongitudeSeconds = longitude; }

    public double getGeoid()                     {   return mGeoid;    }
    public void   setGeoid(double geoid)         {     mGeoid = geoid;    }

    public double getNorthing()                  { return mNorthing; }
    public void   setNorthing(double northing)   { mNorthing = northing; }

    public double getEasting()                   { return mEasting; }
    public void   setEasting(double easting)     { mEasting = easting; }

    public double getElevation()                 { return mElevation; }
    public void setElevation(double elevation)   { mElevation = elevation; }


    //I know this isn't necessary, but it never hurts to make things explicit
    private void initializeDefaultData(){
        mLatitudeDegrees  = 0;
        mLatitudeMinutes  = 0;
        mLatitudeSeconds  = 0d;
        mLongitudeDegrees = 0;
        mLongitudeMinutes = 0;
        mLongitudeSeconds = 0d;
        mNorthing         = 0d;
        mEasting          = 0d;
        mElevation        = 0d;
    }

}
