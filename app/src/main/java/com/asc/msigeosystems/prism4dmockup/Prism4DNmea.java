package com.asc.msigeosystems.prism4dmockup;

import java.util.Date;
import java.util.List;

/**
 * Created by elisabethhuhn on 5/15/2016.
 */
public class Prism4DNmea {

    //Tags for fragment arguments
    public static final String sNmeaTag  = "NMEA";

    private CharSequence  mNmeaSentence;

    //Fields of interest to skyplot Prism4D that can be pulled out of the NMEA Sentence

    //Info about the NMEA Sentence
    private CharSequence mType; //e.g. GLL, GGA, etc.
    private Date   mTime;//UTC time

    //Position
    private double mLatitude;
    private double mLongitude;
    private double mEllipsoidalElevation;
    private double mGeoid;
    private double mOrthometricElevation;
    private double mLocalization;
    private double mLocalNorthing;
    private double mLocalEasting;
    private double mLocalElevation;

    //Satellites in view information
    private int    mSatelliteStatus;
    private int    mSatellites;
    private List<Prism4DSatellite> mSatelliteList;

    //Quality of fix Information
    private double mHdop;
    private double mVdop;
    private double mTdop;
    private double mPdop;
    private double mGdop;
    private double mHrms;
    private double mVrms;




    public CharSequence getNmeaSentence() {
        return mNmeaSentence;
    }

    public void setNmeaSentence(CharSequence nmeaSentence) {
        this.mNmeaSentence = nmeaSentence;
    }



}
