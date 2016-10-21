package com.asc.msigeosystems.prism4d;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by elisabethhuhn on 6/17/2016.
 */


public class Prism4DCoordinateLL {
    //This class holds raw coordinates and is responsible for swapping DD to DMS format

    protected double mTime; //time coordinate taken

    //Latitude in DD and DMS formats
    protected double mLatitude;

    protected int    mLatitudeDegree;
    protected int    mLatitudeMinute;
    protected double mLatitudeSecond;


    //Longitude in DD and DMS formats
    protected double mLongitude;

    protected int    mLongitudeDegree;
    protected int    mLongitudeMinute;
    protected double mLongitudeSecond;

    protected double mElevation; //Orthometric Elevation in Meters
    protected double mGeoid;     //Mean Sea Level in Meters

    protected boolean mValidCoordinate = true;

    /********
     *
     * Setters and Getters
     *
     **********/

    public double getLatitude()        { return mLatitude;        }
    public void setLatitude(double latitude) { mLatitude = latitude; }

    public int getLatitudeDegree()     { return mLatitudeDegree;  }
    public void setLatitudeDegree(int latitudeDegree) { mLatitudeDegree = latitudeDegree; }

    public int getLatitudeMinute()     { return mLatitudeMinute;  }
    public void setLatitudeMinute(int latitudeMinute) { mLatitudeMinute = latitudeMinute; }

    public double getLatitudeSecond()  { return mLatitudeSecond;  }
    public void setLatitudeSecond(double latitudeSecond) { mLatitudeSecond = latitudeSecond; }

    public double getLongitude()       { return mLongitude;       }
    public void setLongitude(double longitude) { mLongitude = longitude; }

    public int getLongitudeDegree()    { return mLongitudeDegree; }
    public void setLongitudeDegree(int longitudeDegree) { mLongitudeDegree = longitudeDegree; }

    public int getLongitudeMinute()    { return mLongitudeMinute; }
    public void setLongitudeMinute(int longitudeMinute) { mLongitudeMinute = longitudeMinute; }

    public double getLongitudeSecond() {
        return mLongitudeSecond;
    }
    public void setLongitudeSecond(double longitudeSecond) {mLongitudeSecond = longitudeSecond; }

    public double getElevation()       {  return mElevation;   }
    public void setElevation(double elevation) { mElevation = elevation;   }
    public double getElevationFeet() {return Prism4DConstants.convertMetersToFeet(mElevation); }

    public double getGeoid()           {  return mGeoid; }
    public double getGeoidFeet() { return Prism4DConstants.convertMetersToFeet(mGeoid);}
    public void setGeoid(double geoid) { mGeoid = geoid;  }

    public boolean isValidCoordinate() {
        return mValidCoordinate;
    }

    public double getTime()            {  return mTime;    }
    public void setTime(double time)          {  mTime = time;  }

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
    public Prism4DCoordinateLL() {
        //not real useful
        mValidCoordinate = false;
    }

    public Prism4DCoordinateLL(double latitude, double longitude) {
        latLongDD(latitude, longitude);
    }


    public Prism4DCoordinateLL(int latitudeDegree,
                               int latitudeMinute,
                               double latitudeSecond,
                               int longitudeDegree,
                               int longitudeMinute,
                               double longitudeSecond) {

        latLongDMS(latitudeDegree,
                latitudeMinute,
                latitudeSecond,
                longitudeDegree,
                longitudeMinute,
                longitudeSecond);
    }

    public Prism4DCoordinateLL(CharSequence latitudeString, CharSequence longitudeString) {
        latLongDDStrings(latitudeString, longitudeString);
    }


    public Prism4DCoordinateLL(CharSequence latitudeDegreeString,
                               CharSequence latitudeMinuteString,
                               CharSequence latitudeSecondString,
                               CharSequence longitudeDegreeString,
                               CharSequence longitudeMinuteString,
                               CharSequence longitudeSecondString) {
        latLongDMSStrings(latitudeDegreeString,
                 latitudeMinuteString,
                 latitudeSecondString,
                 longitudeDegreeString,
                 longitudeMinuteString,
                 longitudeSecondString);
    }

    /***************
     * constructor utilities
     *
     */

    protected void latLongDD(double latitude, double longitude) {
        this.mLatitude  = latitude;
        this.mLongitude = longitude;

        mValidCoordinate = convertDDToDMS ();
    }

    protected void latLongDMS(int latitudeDegree,
                              int latitudeMinute,
                              double latitudeSecond,
                              int longitudeDegree,
                              int longitudeMinute,
                              double longitudeSecond) {
        this.mLatitudeDegree = latitudeDegree;
        this.mLatitudeMinute = latitudeMinute;
        this.mLatitudeSecond = latitudeSecond;

        this.mLongitudeDegree = longitudeDegree;
        this.mLongitudeMinute = longitudeMinute;
        this.mLongitudeSecond = longitudeSecond;

        mValidCoordinate = convertDMSToDD ();

    }

    protected void latLongDDStrings(CharSequence latitudeString, CharSequence longitudeString) {
        if (latitudeString.toString().isEmpty()  ) {
            latitudeString = "0.0";
        }
        if ( longitudeString.toString().isEmpty() ){
            longitudeString = "0.0";
        }
        this.mLatitude = Double.parseDouble(latitudeString.toString());
        this.mLongitude = Double.parseDouble(longitudeString.toString());
        mValidCoordinate = convertDDToDMS ();
    }

    protected void latLongDMSStrings(CharSequence latitudeDegreeString,
                                     CharSequence latitudeMinuteString,
                                     CharSequence latitudeSecondString,
                                     CharSequence longitudeDegreeString,
                                     CharSequence longitudeMinuteString,
                                     CharSequence longitudeSecondString) {

        if (latitudeDegreeString.toString().isEmpty()) {
            latitudeDegreeString = "0";
        }
        if (latitudeMinuteString.toString().isEmpty()){
            latitudeMinuteString = "0";
        }
        if (latitudeSecondString.toString().isEmpty() ){
            latitudeSecondString = "0.0";
        }
        if (longitudeDegreeString.toString().isEmpty()){
            longitudeDegreeString = "0";
        }
        if (longitudeMinuteString.toString().isEmpty() ){
            longitudeMinuteString = "0";
        }
        if (longitudeSecondString.toString().isEmpty()) {
            longitudeSecondString = "0.0";
        }
        this.mLatitudeDegree = Integer.valueOf(latitudeDegreeString.toString());
        this.mLatitudeMinute = Integer.valueOf(latitudeMinuteString.toString());
        this.mLatitudeSecond = Double.parseDouble(latitudeSecondString.toString());

        this.mLongitudeDegree = Integer.valueOf(longitudeDegreeString.toString());
        this.mLongitudeMinute = Integer.valueOf(longitudeMinuteString.toString());
        this.mLongitudeSecond = Double.parseDouble(longitudeSecondString.toString());

        mValidCoordinate = convertDMSToDD ();
    }



    protected boolean convertDDToDMS(){
        //The inputs have to be valid
        if ((mLatitude  < -90.0 || mLatitude  >= 90.0) &&
            (mLongitude < -180. || mLongitude >= 180.)){
            return false;
        }
        //
        //Latitude
        //
        boolean isLatPos = true;
        boolean isLongPos = true;

        if (mLatitude < 0){
            mLatitude = Math.abs(mLatitude);
            isLatPos = false;
        }

        //strip out the decimal parts of mLatitude
        mLatitudeDegree = (int) mLatitude;
        int temp = mLatitudeDegree;
        double degree = (double) mLatitudeDegree;
        degree = temp;

        //digital degrees minus degrees will be decimal minutes plus seconds
        //converting to int strips out the seconds
        double minuteSec = mLatitude - degree;
        double minutes = minuteSec * 60.;
        mLatitudeMinute = (int) minutes;
        double minuteOnly = (double)mLatitudeMinute;

        //start with the DD, subtract out Degrees, subtract out Minutes
        //convert the remainder into whole seconds
        mLatitudeSecond = (mLatitude - degree - (minuteOnly/60.)) * (60. *60.);
        //mLatitudeSecond = (mLatitude - minutes) * (60. *60.);

        if (!isLatPos){
            mLatitude       = 0. - mLatitude;
            mLatitudeDegree = 0  - mLatitudeDegree;
            mLatitudeMinute = 0  - mLatitudeMinute;
            mLatitudeSecond = 0. - mLatitudeSecond;
        }
        //truncate to a reasonable number of decimal digits
        BigDecimal bd = new BigDecimal(mLatitudeSecond).setScale(5, RoundingMode.HALF_UP);
        mLatitudeSecond = bd.doubleValue();

        //
        //Longitude
        //
        if (mLongitude < 0){
            mLongitude = Math.abs(mLongitude);
            isLongPos = false;
        }

        //strip out the decimal parts of decimal degrees, leaving whole degrees
        mLongitudeDegree = (int) mLongitude;
        degree =        (double) mLongitudeDegree;

        //digital degrees minus degrees will be decimal minutes plus seconds
        //converting to int strips out the seconds
        minuteSec = mLongitude - degree;
        minutes   = minuteSec * 60.;
        mLongitudeMinute = (int)minutes;
        minuteOnly = (double)mLongitudeMinute;

        //start with the DD, subtract out Degrees, subtract out Minutes
        //convert the remainder into whole seconds
        mLongitudeSecond = (mLongitude - degree - (minuteOnly/60.)) * (60. *60.);

        if (!isLongPos){
            mLongitude       = 0. - mLongitude;
            mLongitudeDegree = 0  - mLongitudeDegree;
            mLongitudeMinute = 0  - mLongitudeMinute;
            mLongitudeSecond = 0. - mLongitudeSecond;
        }
        //truncate to a reasonable number of decimal digits
        bd = new BigDecimal(mLongitudeSecond).setScale(5, RoundingMode.HALF_UP);
        mLongitudeSecond = bd.doubleValue();
        return true;
    }

    protected boolean convertDMSToDD(){

        if ((mLatitudeDegree <   -90 || mLatitudeDegree >=   90) ||
            (mLatitudeMinute <   -60 || mLatitudeMinute >    60) ||
            (mLatitudeSecond <   -60.|| mLatitudeSecond >    60.)||
            (mLongitudeDegree < -180 || mLongitudeDegree >= 180) ||
            (mLongitudeMinute <  -60 || mLongitudeMinute >   60) ||
            (mLongitudeSecond <  -60.|| mLongitudeSecond >   60.)) {
            return false;
        }

        boolean isLatPos = true;
        boolean isLongPos = true;

        if (mLatitudeDegree < 0){
            isLatPos = false;
            mLatitudeDegree = Math.abs(mLatitudeDegree);
        }
        if (mLatitudeMinute < 0){
            isLatPos = false;
            mLatitudeMinute = Math.abs(mLatitudeMinute);
        }
        if (mLatitudeSecond < 0){
            isLatPos = false;
            mLatitudeSecond = Math.abs(mLatitudeSecond);
        }
        if (mLongitudeDegree < 0){
            isLatPos = false;
            mLongitudeDegree = Math.abs(mLongitudeDegree);
        }
        if (mLongitudeMinute < 0){
            isLatPos = false;
            mLongitudeMinute = Math.abs(mLongitudeMinute);
        }
        if (mLongitudeSecond < 0){
            isLatPos = false;
            mLongitudeSecond = Math.abs(mLongitudeSecond);
        }

        double degrees = (double)mLatitudeDegree;
        double minutes = (double)mLatitudeMinute ;
        double seconds =         mLatitudeSecond ;
        mLatitude =  degrees + (minutes/ 60.) + (seconds/ (60.*60.));

        degrees = (double)mLongitudeDegree;
        minutes = (double)mLongitudeMinute ;
        seconds =         mLongitudeSecond ;
        mLongitude = degrees + (minutes/ 60.) + (seconds/ (60.*60.));

        if (!isLatPos) {
            mLatitude       = 0. - mLatitude;
            mLatitudeDegree = 0  - mLatitudeDegree;
            mLatitudeMinute = 0  - mLatitudeMinute;
            mLatitudeSecond = 0. - mLatitudeSecond;
        }
        if (!isLatPos) {
            mLongitude       = 0. - mLongitude;
            mLongitudeDegree = 0  - mLongitudeDegree;
            mLongitudeMinute = 0  - mLongitudeMinute;
            mLongitudeSecond = 0. - mLongitudeSecond;
        }
        return true;
    }
}
