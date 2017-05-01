package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 6/17/2016.
 *
 * This class is NOT part of the Coordinate's hierarchy
 * But it is only meant as a temporary
 * holder of statistical data such as standard deviation
 *
 * NOTE all fields are double, even Degrees and Minutes
 *
 */


public class Prism4DCoordinateMean {
    //

    /*****************************************************/
    /********    Attributes                      *********/
    /*****************************************************/

    //ID is necessary for the DB
    private int mCoordinateID;

    //Project of the meaning process
    private int mProjectID;
    private int mPointID;

    //total number of readings since meaning started
    private int mRawReadings;

    //number of readings that have been used in the mean
    private int mMeanedReadings;

    //number of the readings included in the mean that are of quality fixed
    // (per Project Settings Quality definition)
    private int mFixedReadings;


    //Latitude in DD and DMS formats
    private double mLatitude;
    private double mLatitudeDegree;
    private double mLatitudeMinute;
    private double mLatitudeSecond;
    private double mLatitudeStdDev;


    //Longitude in DD and DMS formats
    private double mLongitude;
    private double mLongitudeDegree;
    private double mLongitudeMinute;
    private double mLongitudeSecond;
    private double mLongitudeStdDev;

    private double mElevation; //Orthometric Elevation in Meters
    private double mElevationStdDev;

    private double mGeoid;     //Mean Sea Level in Meters

    private int    mSatellites; //Number of satellites in the fix

    private boolean mValidCoordinate = true;
    private boolean mIsFixed = true;
    private boolean mType = true; //true = Easting/Northing, false = Latitude/Longitude


    /*****************************************************/
    /********    Static Conversion Utilities     *********/
    /*****************************************************/


     /********
     *
     * Setters and Getters
     *
     **********/

    public int getCoordinateID() {  return mCoordinateID; }
    public void setCoordinateID(int coordinateID) {  mCoordinateID = coordinateID; }


    public int getProjectID()                              { return mProjectID;  }
    public void setProjectID(int projectID)                { mProjectID = projectID; }

    public int  getPointID()                               { return mPointID; }
    public void setPointID(int pointID)                    {  mPointID = pointID; }


    public int getRawReadings()                           {return mRawReadings;}
    public void   setRawReadings(int size)                 {mRawReadings = size;}

    public int getMeanedReadings()                         {return mMeanedReadings;}
    public void   setMeanedReadings(int size)              {mMeanedReadings = size;}

    public int getFixedReadings()                          { return mFixedReadings; }
    public void   setFixedReadings(int fixedReadings)      {  mFixedReadings = fixedReadings; }

    public double getLatitude()                            { return mLatitude;        }
    public void   setLatitude(double latitude)             { mLatitude = latitude; }

    public double getLatitudeDegree()                      { return mLatitudeDegree;  }
    public void   setLatitudeDegree(double latitudeDegree) { mLatitudeDegree = latitudeDegree; }

    public double getLatitudeMinute()                      { return mLatitudeMinute;  }
    public void   setLatitudeMinute(double latitudeMinute) { mLatitudeMinute = latitudeMinute; }

    public double getLatitudeSecond()                      { return mLatitudeSecond;  }
    public void   setLatitudeSecond(double latitudeSecond) { mLatitudeSecond = latitudeSecond; }

    public double getLatitudeStdDev()                      { return mLatitudeStdDev;        }
    public void   setLatitudeStdDev(double latitudeStdDev) { mLatitudeStdDev = latitudeStdDev; }

    public double getLongitude()                           { return mLongitude;       }
    public void   setLongitude(double longitude)           { mLongitude = longitude; }

    public double getLongitudeDegree()                     { return mLongitudeDegree; }
    public void   setLongitudeDegree(double longitudeDegree) { mLongitudeDegree = longitudeDegree; }

    public double getLongitudeMinute()                     { return mLongitudeMinute; }
    public void   setLongitudeMinute(double longitudeMinute) { mLongitudeMinute = longitudeMinute; }

    public double getLongitudeSecond()                     { return mLongitudeSecond; }
    public void   setLongitudeSecond(double longitudeSecond) {mLongitudeSecond = longitudeSecond; }

    public double getLongitudeStdDev()                      { return mLongitudeStdDev;        }
    public void   setLongitudeStdDev(double longitudeStdDev) { mLongitudeStdDev = longitudeStdDev; }

    public double getElevation()                           {  return mElevation;   }
    public void   setElevation(double elevation)           { mElevation = elevation;   }
    public double getElevationFeet() {return Prism4DConstantsAndUtilities.convertMetersToFeet(mElevation); }

    public double getElevationStdDev()                      {return mElevationStdDev;}
    public void   setElevationStdDev(double elevationStdDev){mElevationStdDev = elevationStdDev;}

    public double getGeoid()                               {  return mGeoid; }
    public double getGeoidFeet() { return Prism4DConstantsAndUtilities.convertMetersToFeet(mGeoid);}
    public void   setGeoid(double geoid)                   { mGeoid = geoid;  }

    //RMS is just another term for Std Deviation
    public double getHrms()                                {return mLongitudeStdDev; }
    public   void setHrms(double hrms)                     { mLongitudeStdDev = hrms; }

    public double getVrms()                                { return mLatitudeStdDev; }
    public void   setVrms(double vrms)                     { mLatitudeStdDev = vrms; }

    public int getSatellites()                             {  return mSatellites; }
    public void setSatellites(int satellites)              { mSatellites = satellites; }

    public double getEasting()                            { return mLongitude;        }
    public void   setEasting(double easting)              { mLongitude = easting; }

    public double getEastingStdDev()                      { return mLongitudeStdDev;        }
    public void   setEastingStdDev(double eastingStdDev)  { mLongitudeStdDev = eastingStdDev; }

    public double getNorthing()                           { return mLatitude;       }
    public void   setNorthing(double northing)            { mLatitude = northing; }

    public double getNorthingStdDev()                     { return mLatitudeStdDev;       }
    public void   setNorthingStdDev(double northingStdDev){ mLatitudeStdDev = northingStdDev; }

    public void setValidCoordinate(boolean validCoordinate){this.mValidCoordinate = validCoordinate;}
    public boolean isValidCoordinate() {
        return mValidCoordinate;
    }

    public void    setIsFixed (boolean isFixed){this.mIsFixed = isFixed;}
    public boolean isFixed()                   {return mIsFixed;}

    //type = true if Easting/Northing and = false if Latitude / Longitude
    public static final boolean EASTING_NORTHING   = true;
    public static final boolean LATITUDE_LONGITUDE = false;
    public void setType(boolean type)                       {this.mType = type;}
    public boolean isEastingNorthing()                      { return mType; }
    public boolean isLatitudeLongitude()                    {
        if (mType) return false;
        return true;
    }

    /********
     *
     * Static functions
     *
     **********/



    /********
     *
     * Constructors
     *
     **********/
    /**************************************************************/
    /**************************************************************/
    /*       Vanilla constructor, default values only             */
    /*                                                            */
    /**************************************************************/
    /**************************************************************/
    public Prism4DCoordinateMean() {
        //set all variables to their defaults
        initializeDefaultVariables();
    }

    private void initializeDefaultVariables() {
        //I know that one does not have to initialize int's etc, but
        //to be explicit about the initialization, do it anyway
        mPointID        = 0;

        mRawReadings    = 0;
        mMeanedReadings = 0;
        mFixedReadings  = 0;

        //Latitude in DD and DMS formats
        mLatitude       = 0d;

        mLatitudeDegree = 0;
        mLatitudeMinute = 0;
        mLatitudeSecond = 0d;
        mLatitudeStdDev = 0.d;


        //Longitude in DD and DMS formats
        mLongitude      = 0d;

        mLongitudeDegree = 0;
        mLongitudeMinute = 0;
        mLongitudeSecond = 0d;
        mLongitudeStdDev = 0d;

        mElevation       = 0d; //Orthometric Elevation in Meters
        mElevationStdDev = 0d;
        mGeoid           = 0d;     //Mean Sea Level in Meters

        mValidCoordinate = false;
    }


    /***************
     * constructor utilities
     *
     */


}
