package com.asc.msigeosystems.prism4dmockup;

/**
 * Created by elisabethhuhn on 5/23/2016.
 */


public class Prism4DCoordinateNAD83 extends Prism4DCoordinateLL {


    //WGS84 Constants
    //public static final double sSemiMajorAxis      = 6378137.0;    // a meters
    //public static final double sFlattening         = 298.257223563; // 1/f unitless

    //NAD83 Constants
    public static final double sSemiMajorAxis = 6378137.;      // a meters
    public static final double sFlattening    = 298.257222101; // 1/f unitless

    //Consider an ellipsoid of revolution with:
    // a = equatorial radius, aka semi major axis
    // b = polar radius, aka polar semi axis
    // f = flattening = (a - b) / a
    // e = eccentricity =  sqrt[f * (2.-f)]
    // n = third flattening = (a-b)/(a+b) = f / (2.-f)


    //From the Ellipsoid
    public static final double sEquatorialRadiusA = 6378137.0;      //equatorial radius in meters
    public static final double sPolarRadiusB = -sEquatorialRadiusA * (sFlattening-1.0);


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









}
