package com.asc.msigeosystems.prism4d;

/**
 * Created by elisabethhuhn on 5/23/2016.
 */


public class Prism4DCoordinateWGS84 extends Prism4DCoordinateLL {

    //WGS84 Datum constants
    //Karney 2010 page 5
    //From the Ellipsoid
    public static final double sEquatorialRadiusA = 6378137.0;      //equatorial radius in meters
    public static final double sPolarRadiusB      = 6356752.314245; //polar semi axis

    //WGS84 Constants
    public static final double sSemiMajorAxis      = 6378137.0;    // a meters
    public static final double sFlattening         = 298.257223563; // 1/f unitless


    /********
     *
     * Setters and Getters
     *
     **********/


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

    public Prism4DCoordinateWGS84(){

    }

    public Prism4DCoordinateWGS84(double latitude, double longitude) {
        latLongDD(latitude, longitude);
    }


    public Prism4DCoordinateWGS84(int latitudeDegree,
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

    public Prism4DCoordinateWGS84(CharSequence latitudeString, CharSequence longitudeString) {
        latLongDDStrings(latitudeString, longitudeString);
    }


    public Prism4DCoordinateWGS84(CharSequence latitudeDegreeString,
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









}
