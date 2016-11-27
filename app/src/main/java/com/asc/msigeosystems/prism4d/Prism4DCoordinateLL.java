package com.asc.msigeosystems.prism4d;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;

import com.asc.msigeosystems.prism4dmockup.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Elisabeth Huhn on 6/17/2016.
 *
 * This class is part of the Coordinate's hierarchy
 * This is the super class of any Coordinate expressed with
 * Latitude / Longitude spherical surface coordinate systems
 *
 *
 * The class knows how to:
 *   A) store all attributes required of Latitude / Longitude coordinate systems.
 *   B) convert from any other coordinate system to this one
 *   C) convert swapping between DD and DMS formats of Latitude / Longitude
 *
 */


public abstract class Prism4DCoordinateLL extends Prism4DCoordinate {
    //

    /*****************************************************/
    /********    Attributes stored in the DB     *********/
    /*****************************************************/

    protected long   mTime; //time coordinate taken in milliseconds

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


    /*****************************************************/
    /********    Static Conversion Utilities     *********/
    /*****************************************************/

    //last parameter indicates whether latitude (true) or longitude (false)
    public static boolean convertDDtoDMS(Context  context,
                                         EditText tudeDDInput,
                                         EditText tudeDInput,
                                         EditText tudeMInput,
                                         EditText tudeSInput,
                                         boolean  isLatitude) {

        String tudeString = tudeDDInput.getText().toString().trim();
        if (tudeString.isEmpty()) {
            tudeString = context.getString(R.string.zero_decimal_string);
            tudeDDInput.setText(tudeString);
        }

        double tude = Double.parseDouble(tudeString);

        //The user inputs have to be within range to be
        if (   (isLatitude   && ((tude < -90.0) || (tude >= 90.0)))  || //Latitude
             ((!isLatitude)  && ((tude < -180.) || (tude >= 180.)))) {  //Longitude

            tudeDInput.setText(R.string.zero_decimal_string);
            tudeMInput.setText(R.string.zero_decimal_string);
            tudeSInput.setText(R.string.zero_decimal_string);
            return false;
        }

        //check sign of tude
        boolean isTudePos = true;
        int tudeColor= R.color.colorPosNumber;
        if (tude < 0) {
            //tude is negative, remember this and work with the absolute value
            tude = Math.abs(tude);
            isTudePos = false;
            tudeColor = R.color.colorNegNumber;
        }

        //strip out the decimal parts of tude
        int tudeDegree = (int) tude;

        double degree = tudeDegree;

        //digital degrees minus degrees will be decimal minutes plus seconds
        //converting to int strips out the seconds
        double minuteSec = tude - degree;
        double minutes = minuteSec * 60.;
        int tudeMinute = (int) minutes;
        double minuteOnly = (double) tudeMinute;

        //start with the DD, subtract out Degrees, subtract out Minutes
        //convert the remainder into whole seconds
        double tudeSecond = (tude - degree - (minuteOnly / 60.)) * (60. * 60.);
        //tudeSecond = (tude - minutes) * (60. *60.);

        //If tude was negative before, restore it to negative
        if (!isTudePos) {
            tude       = 0. - tude;
            tudeDegree = 0 - tudeDegree;
            tudeMinute = 0 - tudeMinute;
            tudeSecond = 0. - tudeSecond;
        }

        //truncate to a reasonable number of decimal digits
        BigDecimal bd = new BigDecimal(tudeSecond).
                setScale(Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision,
                         RoundingMode.HALF_UP);
        tudeSecond = bd.doubleValue();

        //show the user the result
        tudeDInput.setText(String.valueOf(tudeDegree));
        tudeMInput.setText(String.valueOf(tudeMinute));
        tudeSInput.setText(String.valueOf(tudeSecond));

        tudeDDInput.setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeDInput .setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeMInput .setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeSInput .setTextColor(ContextCompat.getColor(context, tudeColor));

        return true;
    }



    //last parameter indicates whether latitude (true) or longitude (false)
    public static boolean convertDMStoDD(Context  context,
                                         EditText tudeDDInput,
                                         EditText tudeDInput,
                                         EditText tudeMInput,
                                         EditText tudeSInput,
                                         boolean  isLatitude){

        //String tudeString = tudeDDInput.getText().toString().trim();
        String tudeDString = tudeDInput.getText().toString().trim();
        String tudeMString = tudeMInput.getText().toString().trim();
        String tudeSString = tudeSInput.getText().toString().trim();

        if ((tudeDString == null)||(tudeDString == "")){
            tudeDString = context.getString(R.string.zero_decimal_string);
        }

        //double tude = Double.parseDouble(tudeString);
        int    tudeD = Integer.parseInt(tudeDString);
        int    tudeM = Integer.parseInt(tudeMString);
        double tudeS = Double.parseDouble(tudeSString);

        if (((isLatitude) && ((tudeD <   -90) || (tudeD >=   90))) ||
            ((isLatitude) && ((tudeM <   -60) || (tudeM >    60))) ||
            ((isLatitude) && ((tudeS <   -60.)|| (tudeS >    60.)))||
           ((!isLatitude) && ((tudeD <  -180) || (tudeD >=  180))) ||
           ((!isLatitude) && ((tudeM <   -60) || (tudeM >    60))) ||
           ((!isLatitude) && ((tudeS <   -60.)|| (tudeS >    60.)))) {

            tudeDDInput.setText(R.string.zero_decimal_string);
            return false;
        }

        boolean isTudePos = true;
        int tudeColor = R.color.colorPosNumber;
        if ((tudeD < 0) || (tudeM < 0) || (tudeS < 0)){
            isTudePos = false;
            tudeD = Math.abs(tudeD);
            tudeM = Math.abs(tudeM);
            tudeS = Math.abs(tudeS);
            tudeColor = R.color.colorNegNumber;
        }

        double degrees = (double)tudeD;
        double minutes = (double)tudeM ;
        double seconds =         tudeS ;
        double tude =  degrees + (minutes/ 60.) + (seconds/ (60.*60.));


        if (!isTudePos) {
            tude  = 0. - tude;
        }

        tudeDDInput.setText(String.valueOf(tude));

        tudeDDInput.setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeDInput .setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeMInput .setTextColor(ContextCompat.getColor(context, tudeColor));
        tudeSInput .setTextColor(ContextCompat.getColor(context, tudeColor));

        return true;
    }




    /********
     *
     * Setters and Getters
     *
     **********/

    public long getTime()              {  return mTime;    }
    public void setTime(long time)     {  mTime = time;  }

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
    public double getElevationFeet() {return Prism4DConstantsAndUtilities.convertMetersToFeet(mElevation); }

    public double getGeoid()           {  return mGeoid; }
    public double getGeoidFeet() { return Prism4DConstantsAndUtilities.convertMetersToFeet(mGeoid);}
    public void setGeoid(double geoid) { mGeoid = geoid;  }

    public void    setValidCoordinate(boolean validCoordinate){ this.mValidCoordinate = validCoordinate;}
    public boolean isValidCoordinate() {
        return mValidCoordinate;
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
    public Prism4DCoordinateLL() {
        //set all variables to their defaults
        initializeDefaultVariables();
    }

    protected void initializeDefaultVariables() {
        //I know that one does not have to initialize int's etc, but
        //to be explicit about the initialization, do it anyway

        //initialize all variables common to all coordinates
        super.initializeDefaultVariables();

        //initialize all variables common to EN coordinates
        mTime           = 0; //time coordinate taken

        //Latitude in DD and DMS formats
        mLatitude       = 0d;

        mLatitudeDegree = 0;
        mLatitudeMinute = 0;
        mLatitudeSecond = 0d;


        //Longitude in DD and DMS formats
        mLongitude      = 0d;

        mLongitudeDegree = 0;
        mLongitudeMinute = 0;
        mLongitudeSecond = 0d;

        mElevation       = 0; //Orthometric Elevation in Meters
        mGeoid           = 0;     //Mean Sea Level in Meters

        mValidCoordinate = false;
    }

    /**************************************************************/
    /**************************************************************/
    /*                                                            */
    /*       Create the LL Coordinate with Lat / Long             */
    /*     Automatically convert DD to DMS values as well         */
    /*                                                            */
    /**************************************************************/
    /**************************************************************/
    public Prism4DCoordinateLL(double latitude, double longitude) {
        initializeDefaultVariables();
        latLongDD(latitude, longitude);
    }


    /**************************************************************/
    /**************************************************************/
    /*                                                            */
    /*      Create the LL Coordinate with Lat DMS / Long DMS      */
    /*      Automatically convert DMS to DD values as well        */
    /*                                                            */
    /**************************************************************/
    /**************************************************************/
    public Prism4DCoordinateLL(int latitudeDegree,
                               int latitudeMinute,
                               double latitudeSecond,
                               int longitudeDegree,
                               int longitudeMinute,
                               double longitudeSecond) {
        initializeDefaultVariables();

        latLongDMS(latitudeDegree,
                latitudeMinute,
                latitudeSecond,
                longitudeDegree,
                longitudeMinute,
                longitudeSecond);
    }


    /**************************************************************/
    /**************************************************************/
    /*                                                            */
    /*       Create the LL Coordinate with Lat / Long Strings     */
    /*       Automatically convert to numbers AND                 */
    /*        convert DD to DMS values as well                    */
    /*                                                            */
    /**************************************************************/
    /**************************************************************/
    public Prism4DCoordinateLL(CharSequence latitudeString, CharSequence longitudeString) {
        initializeDefaultVariables();
        latLongDDStrings(latitudeString, longitudeString);
    }



    /**************************************************************/
    /**************************************************************/
    /*                                                            */
    /*      Create the LL Coordinate with                         */
    /*         Lat DMS / Long DMS String values                   */
    /*      Automatically convert to number AND                   */
    /*         convert DMS to DD values as well                   */
    /*                                                            */
    /**************************************************************/
    /**************************************************************/
    public Prism4DCoordinateLL(CharSequence latitudeDegreeString,
                               CharSequence latitudeMinuteString,
                               CharSequence latitudeSecondString,
                               CharSequence longitudeDegreeString,
                               CharSequence longitudeMinuteString,
                               CharSequence longitudeSecondString) {
        initializeDefaultVariables();
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
        double degree = temp;

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
        BigDecimal bd = new BigDecimal(mLatitudeSecond).
                                setScale(Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision,
                                         RoundingMode.HALF_UP);
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
        bd = new BigDecimal(mLongitudeSecond).
                                setScale(Prism4DConstantsAndUtilities.sMicrometerDigitsOfPrecision,
                                         RoundingMode.HALF_UP);
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
            isLongPos = false;
            mLongitudeDegree = Math.abs(mLongitudeDegree);
        }
        if (mLongitudeMinute < 0){
            isLongPos = false;
            mLongitudeMinute = Math.abs(mLongitudeMinute);
        }
        if (mLongitudeSecond < 0){
            isLongPos = false;
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
        if (!isLongPos) {
            mLongitude       = 0. - mLongitude;
            mLongitudeDegree = 0  - mLongitudeDegree;
            mLongitudeMinute = 0  - mLongitudeMinute;
            mLongitudeSecond = 0. - mLongitudeSecond;
        }
        return true;
    }
}
