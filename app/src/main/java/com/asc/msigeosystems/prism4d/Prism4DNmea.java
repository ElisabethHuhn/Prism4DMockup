package com.asc.msigeosystems.prism4d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elisabeth Huhn on 5/15/2016.
 * Represents a NMEA sentence
 */
public class Prism4DNmea {

    //Tags for fragment arguments
    public static final String sNmeaTag  = "NMEA";

    /*
    public static final String sNemaType           = "NMEA_TYPE";
    public static final String sNmeaLatitude       = "NMEA_LATITUDE";
    public static final String sNmeaLongitude      = "NMEA_LONGITUDE";
    public static final String sNmeaElipElevation  = "NMEA_E_ELEVATION";
    public static final String sNmeaGeoid          = "NMEA_GEOID";
    public static final String sNmeaOrthoElevation = "NMEA_O_ELEVATION";
    public static final String sNmeaLocalization   = "NMEA_LOCALIZATION";
    public static final String sNmeaLocalNorthing       = "NMEA_LOC_NORTHING";
    public static final String sNmeaLocalEasting        = "NMEA_LOC_EASTING";
    public static final String sNmeaLocalElevation      = "NMEA_LOC_ELEVATION";
    public static final String sNmeaSatelliteStatus     = "NMEA_SATELLITE_STATUS";
    public static final String sNmeaSatellites      = "NMEA_SATELLITES";
    public static final String sNmeaHdop            = "NMEA_HDOP";
    public static final String sNmeaVdop            = "NMEA_VDOP";
    public static final String sNmeaTdop            = "NMEA_TDOP";
    public static final String sNmeaPdop            = "NMEA_PDOP";
    public static final String sNmeaGdop            = "NMEA_GDOP";
    public static final String sNmeaHrms            = "NMEA_HRMS";
    public static final String sNmeaVrms            = "NMEA_VRMS";
    public static final String sNmeaQuality         = "NMEA_QUALITY";
    public static final String sNmeaFixed           = "NMEA_FIXED";
*/


    private CharSequence  mNmeaSentence;


    //Fields of interest to skyplot Prism4D that can be pulled out of the NMEA Sentence

    //Info about the NMEA Sentence
    public CharSequence mNmeaType; //e.g. GLL, GGA, etc.
    public double mTime;//UTC time: milliseconds since 1/1/1970

    //Position
    public double mLatitude;
    public double mLongitude;
    public double mEllipsoidalElevation;
    public double mGeoid;
    public double mOrthometricElevation;
    public double mLocalization;
    public double mLocalNorthing;
    public double mLocalEasting;
    public double mLocalElevation;

    //Satellites in view information
    public int    mSatelliteStatus;
    public int    mSatellites;
    public List<Prism4DSatellite> mSatelliteList = new ArrayList<>();

    //Quality of fix Information
    public double mHdop;
    public double mVdop;
    public double mTdop;
    public double mPdop;
    public double mGdop;
    public double mHrms;
    public double mVrms;


    public int    mQuality;
    public boolean mFixed;


    /******************************* Static Methods *********************/

    /* FOR NOW, JUST DUMP THE LATEST NMEA DATA BUNDLE
    public static Bundle putNmeaInBundle(Bundle state, Prism4DNmea nmeaData) {


        state.putCharSequence(sNemaType, nmeaData.getNmeaType());

        return state;
    }
    */

    /*************************** Setters and Getters ********************/

    public CharSequence getNmeaSentence() {
        return mNmeaSentence;
    }
    public void setNmeaSentence(CharSequence nmeaSentence) {
        this.mNmeaSentence = nmeaSentence;
    }

    public CharSequence getNmeaType() {
        return mNmeaType;
    }
    public void         setNmeaType(CharSequence type) {
        mNmeaType = type;
    }

    public double getTime() {
        return mTime;
    }
    public void   setTime(double time) {
        mTime = time;
    }

    public double getLatitude() {
        return mLatitude;
    }
    public void   setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
    public void   setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getEllipsoidalElevation() {
        return mEllipsoidalElevation;
    }
    public void   setEllipsoidalElevation(double ellipsoidalElevation) {
        mEllipsoidalElevation = ellipsoidalElevation;
    }

    public double getGeoid() {
        return mGeoid;
    }
    public void   setGeoid(double geoid) {
        mGeoid = geoid;
    }

    public double getOrthometricElevation() {
        return mOrthometricElevation;
    }
    public void   setOrthometricElevation(double orthometricElevation) {
        mOrthometricElevation = orthometricElevation;
    }

    public double getLocalization() {
        return mLocalization;
    }
    public void   setLocalization(double localization) {
        mLocalization = localization;
    }

    public double getLocalNorthing() {
        return mLocalNorthing;
    }
    public void   setLocalNorthing(double localNorthing) {
        mLocalNorthing = localNorthing;
    }

    public double getLocalEasting() {
        return mLocalEasting;
    }
    public void   setLocalEasting(double localEasting) {
        mLocalEasting = localEasting;
    }

    public double getLocalElevation() {
        return mLocalElevation;
    }
    public void   setLocalElevation(double localElevation) {
        mLocalElevation = localElevation;
    }

    public int  getSatelliteStatus() {
        return mSatelliteStatus;
    }
    public void setSatelliteStatus(int satelliteStatus) {
        mSatelliteStatus = satelliteStatus;
    }

    public int  getSatellites() {
        return mSatellites;
    }
    public void setSatellites(int satellites) {
        mSatellites = satellites;
    }

    public List<Prism4DSatellite> getSatelliteList() {
        return mSatelliteList;
    }
    public void setSatelliteList(List<Prism4DSatellite> satelliteList) {
        mSatelliteList = satelliteList;
    }

    public Prism4DSatellite getSatellite(int position) {
        return mSatelliteList.get(position);
    }
    public void setSatellite(Prism4DSatellite satellite) {
        mSatelliteList.add(satellite);
    }



    public double getHdop() {
        return mHdop;
    }
    public void   setHdop(double hdop) {
        mHdop = hdop;
    }

    public double getVdop() {
        return mVdop;
    }
    public void   setVdop(double vdop) {
        mVdop = vdop;
    }

    public double getTdop() {
        return mTdop;
    }
    public void   setTdop(double tdop) {
        mTdop = tdop;
    }

    public double getPdop() {
        return mPdop;
    }
    public void   setPdop(double pdop) {
        mPdop = pdop;
    }

    public double getGdop() {
        return mGdop;
    }
    public void   setGdop(double gdop) {
        mGdop = gdop;
    }

    public double getHrms() {
        return mHrms;
    }
    public void   setHrms(double hrms) {
        mHrms = hrms;
    }

    public double getVrms() {
        return mVrms;
    }
    public void   setVrms(double vrms) {
        mVrms = vrms;
    }

    public int  getQuality() {
        return mQuality;
    }
    public void setQuality(int quality) {
        mQuality = quality;
    }

    public boolean isFixed() {
        return mFixed;
    }
    public void    setFixed(boolean fixed) {
        mFixed = fixed;
    }

    /********************** constructors ********************************/

    public Prism4DNmea(CharSequence nmeaSentence) {
        //save the raw sentence
        mNmeaSentence = nmeaSentence;

    }

    public Prism4DNmea(){
        initializeDefaultData();
    }
    private void initializeDefaultData(){
        mNmeaType = "GPGGA"; //e.g. GLL, GGA, etc.
        mTime = 0d ;//UTC time: milliseconds since 1/1/1970

        //Position
        mLatitude = 0d ;
        mLongitude = 0d ;
        mEllipsoidalElevation = 0d ;
        mGeoid = 0d ;
        mOrthometricElevation = 0d ;
        mLocalization = 0d ;
        mLocalNorthing = 0d ;
        mLocalEasting = 0d ;
        mLocalElevation = 0d ;

        //Satellites in view information
        mSatelliteStatus = 0;
        mSatellites = 0;
        //mSatelliteList = new ArrayList<>(); //already initialized above

        //Quality of fix Information
        mHdop = 0d ;
        mVdop = 0d ;
        mTdop = 0d ;
        mPdop = 0d ;
        mGdop = 0d ;
        mHrms = 0d ;
        mVrms = 0d ;


        mQuality =0;
        mFixed = false;

    }
}
