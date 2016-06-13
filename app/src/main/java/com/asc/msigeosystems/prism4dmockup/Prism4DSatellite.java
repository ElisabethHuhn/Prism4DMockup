package com.asc.msigeosystems.prism4dmockup;

/**
 * Created by elisabethhuhn on 5/7/2016.
 */
public class Prism4DSatellite {

    // Satellite Data
    private int mSatelliteID;
    private int mElevation;//degrees
    private int mAzimuth;  //degrees to true
    private int mSnr;      //dB
    //Satellite data as reported in GSV NMEA sentence

    //
    //Setters and Getters
    //

    public int getSatelliteID() {
        return mSatelliteID;
    }

    public void setSatelliteID(int satelliteID) {
        mSatelliteID = satelliteID;
    }

    public int getElevation() {
        return mElevation;
    }

    public void setElevation(int elevation) {
        mElevation = elevation;
    }

    public int getAzimuth() {
        return mAzimuth;
    }

    public void setAzimuth(int azimuth) {
        mAzimuth = azimuth;
    }

    public int getSnr() {
        return mSnr;
    }

    public void setSnr(int snr) {
        mSnr = snr;
    }




    /*
     *   constructorS
     */

    public Prism4DSatellite(int satelliteID, int elevation, int azimuth, int snr) {
        mSatelliteID = satelliteID;
        mElevation = elevation;
        mAzimuth = azimuth;
        mSnr = snr;
    }
    public Prism4DSatellite(int satelliteID) {
        mSatelliteID = satelliteID;
    }
}

