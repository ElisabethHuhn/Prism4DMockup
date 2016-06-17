package com.asc.msigeosystems.prism4dmockup;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by elisabethhuhn on 5/23/2016.
 */


public class Prism4DWSG84Coordinate {
    //This class holds raw coordinates and is responsible for swapping DD to DMS format

    //Latitude in DD and DMS formats
    private double mLatitude;

    private int    mLatitudeDegree;
    private int    mLatitudeMinute;
    private double mLatitudeSecond;


    //Longitude in DD and DMS formats
    private double mLongitude;

    private int    mLongitudeDegree;
    private int    mLongitudeMinute;
    private double mLongitudeSecond;

    private double mElevation; //Orthometric Elevation in Meters
    private double mGeoid;     //Mean Sea Level in Meters

    private boolean mValidCoordinate = true;

    /********
     *
     * Setters and Getters
     *
     **********/

    public double getLatitude() {
        return mLatitude;
    }

    public int getLatitudeDegree() {
        return mLatitudeDegree;
    }

    public int getLatitudeMinute() {
        return mLatitudeMinute;
    }

    public double getLatitudeSecond() {
        return mLatitudeSecond;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public int getLongitudeDegree() {
        return mLongitudeDegree;
    }

    public int getLongitudeMinute() {
        return mLongitudeMinute;
    }

    public double getLongitudeSecond() {
        return mLongitudeSecond;
    }

    public double getElevation() {  return mElevation;   }

    public double getElevationFeet() {return convertMetersToFeet(mElevation); }

    public void setElevation(double elevation) { mElevation = elevation;   }

    public double getGeoid() {   return mGeoid; }

    public double getGeoidFeet() { return convertMetersToFeet(mGeoid);}

    public void setGeoid(double geoid) { mGeoid = geoid;  }

    public boolean isValidCoordinate() {
        return mValidCoordinate;
    }

    /********
     *
     * Static functions
     *
     **********/

    public static double convertMetersToFeet(double meters) {
        //function converts Feet to Meters.

        return (meters * Prism4DConstants.feetInAMeter);  // official conversion rate of Meters to Feet

    }

    /********
     *
     * Constructors
     *
     **********/


    public Prism4DWSG84Coordinate(double latitude, double longitude) {
        this.mLatitude  = latitude;
        this.mLongitude = longitude;

        mValidCoordinate = convertDDToDMS ();
    }

    public Prism4DWSG84Coordinate(CharSequence latitudeString, CharSequence longitudeString) {

        if (latitudeString.toString().isEmpty() || longitudeString.toString().isEmpty() ){
            mValidCoordinate = false;
        } else {

            this.mLatitude = Double.parseDouble(latitudeString.toString());
            this.mLongitude = Double.parseDouble(longitudeString.toString());
            mValidCoordinate = convertDDToDMS ();
        }

    }

    public Prism4DWSG84Coordinate(int latitudeDegree,
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

    public Prism4DWSG84Coordinate(CharSequence latitudeDegreeString,
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




    private boolean convertDDToDMS(){
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

    private boolean convertDMSToDD(){

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
