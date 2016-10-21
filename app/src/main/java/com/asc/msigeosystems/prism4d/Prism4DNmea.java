package com.asc.msigeosystems.prism4d;

import java.util.ArrayList;
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

    public void setNmeaType(CharSequence type) {
        mNmeaType = type;
    }

    public double getTime() {
        return mTime;
    }

    public void setTime(double time) {
        mTime = time;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getEllipsoidalElevation() {
        return mEllipsoidalElevation;
    }

    public void setEllipsoidalElevation(double ellipsoidalElevation) {
        mEllipsoidalElevation = ellipsoidalElevation;
    }

    public double getGeoid() {
        return mGeoid;
    }

    public void setGeoid(double geoid) {
        mGeoid = geoid;
    }

    public double getOrthometricElevation() {
        return mOrthometricElevation;
    }

    public void setOrthometricElevation(double orthometricElevation) {
        mOrthometricElevation = orthometricElevation;
    }

    public double getLocalization() {
        return mLocalization;
    }

    public void setLocalization(double localization) {
        mLocalization = localization;
    }

    public double getLocalNorthing() {
        return mLocalNorthing;
    }

    public void setLocalNorthing(double localNorthing) {
        mLocalNorthing = localNorthing;
    }

    public double getLocalEasting() {
        return mLocalEasting;
    }

    public void setLocalEasting(double localEasting) {
        mLocalEasting = localEasting;
    }

    public double getLocalElevation() {
        return mLocalElevation;
    }

    public void setLocalElevation(double localElevation) {
        mLocalElevation = localElevation;
    }

    public int getSatelliteStatus() {
        return mSatelliteStatus;
    }

    public void setSatelliteStatus(int satelliteStatus) {
        mSatelliteStatus = satelliteStatus;
    }

    public int getSatellites() {
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

    public void setHdop(double hdop) {
        mHdop = hdop;
    }

    public double getVdop() {
        return mVdop;
    }

    public void setVdop(double vdop) {
        mVdop = vdop;
    }

    public double getTdop() {
        return mTdop;
    }

    public void setTdop(double tdop) {
        mTdop = tdop;
    }

    public double getPdop() {
        return mPdop;
    }

    public void setPdop(double pdop) {
        mPdop = pdop;
    }

    public double getGdop() {
        return mGdop;
    }

    public void setGdop(double gdop) {
        mGdop = gdop;
    }

    public double getHrms() {
        return mHrms;
    }

    public void setHrms(double hrms) {
        mHrms = hrms;
    }

    public double getVrms() {
        return mVrms;
    }

    public void setVrms(double vrms) {
        mVrms = vrms;
    }

    public int getQuality() {
        return mQuality;
    }

    public void setQuality(int quality) {
        mQuality = quality;
    }

    public boolean isFixed() {
        return mFixed;
    }

    public void setFixed(boolean fixed) {
        mFixed = fixed;
    }

    /********************** constructors ********************************/

    public Prism4DNmea(CharSequence nmeaSentence) {
        //save the raw sentence
        mNmeaSentence = nmeaSentence;

    }
}
