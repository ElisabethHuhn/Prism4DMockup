package com.asc.msigeosystems.prism4dmockup;

/**
 * Created by elisabethhuhn on 6/13/2016.
 */
public class Prism4DConstants {
    //WGS84 Constants
    public static final double sWGSSemiMajorAxis      = 6378137.0;    // a meters
    public static final double sWGSFlattening         = 298.257223563; // 1/f unitless
    public static final double sGravitationalConstant = 3.986004418e14; // cubic meters/square seconds
    public static final double sMeanAngularVelocity   = 7.292115e-5;    // rads / s

    //WGS84 Datum constants
    //Karney 2010 page 5
    //From the Ellipsoid
    public static final double sWGSEquatorialRadiusA = 6378137.0;      //equatorial radius in meters
    public static final double sWGSPolarRadiusB      = 6356752.314245; //polar semi axis

    //used in calculating Moments of Inertia, based on EGM2008
    public static final double sDynSecDegZonal = -4.84165143790815e-4; //
    public static final double sSectorialHarmonics = 2.43938357328313e-6; //
    
    //NAD83 Constants
    public static final double sNADSemiMajorAxis = 6378137.;      // a meters
    public static final double sNADFlattening    = 298.257222101; // 1/f unitless
    
    //Constants for transforming WGS84 to NAD83
    public static final double sTx19972011 = 0.99343; // meters
    public static final double sTx1997PS11 = 0.9080;  
    public static final double sTx1997MA11 = 0.9080;
    
    public static final double sTy19972011 = -1.90331; //meters
    public static final double sTy1997PA11 = -2.0161;
    public static final double sTy1997MA11 = -2.0161;

    public static final double sTz19972011 = -0.52655; //meters
    public static final double sTz1997PA11 = -0.5653;
    public static final double sTz1997MA11 = -0.5653;

    public static final double sWx19972011 = 125.63787; //nanoradians
    public static final double sWx1997PA11 = 134.49216;
    public static final double sWx1997MA11 = 140.45537;

    public static final double sWy19972011 = 45.70072; //nanoradians
    public static final double sWy1997PA11 = 65.29956;
    public static final double sWy1997MA11 = 50.51759;

    public static final double sWz19972011 = 56.23524; //nanoradians
    public static final double sWz1997PA11 = 13.14815;
    public static final double sWz1997MA11 = 43.28416;
    
    public static final double sS19972011  = 1.71504; //parts per billion
    public static final double sS1997PA11  = 1.10;
    public static final double sS1997MA11  = 1.10;

    public static final double sdTx19972011 = 0.00079; // meters/yr
    public static final double sdTx1997PS11 = 0.0001;
    public static final double sdTx1997MA11 = 0.0001;

    public static final double sdTy19972011 = -0.00060; //meters/yr
    public static final double sdTy1997PA11 = 0.0001;
    public static final double sdTy1997MA11 = 0.0001;

    public static final double sdTz19972011 = -0.00134; //meters/yr
    public static final double sdTz1997PA11 = -0.0018;
    public static final double sdTz1997MA11 = -0.0018;

    public static final double sdWx19972011 = 0.32322; //nanoradians/yr
    public static final double sdWx1997PA11 = -1.86168;
    public static final double sdWx1997MA11 = -0.09696;

    public static final double sdWy19972011 = -3.67217; //nanoradians/yr
    public static final double sdWy1997PA11 = 4.88207;
    public static final double sdWy1997MA11 = 0.50905;

    public static final double sdWz19972011 = -0.24886; //nanoradians/yr
    public static final double sdWz1997PA11 = -10.59803;
    public static final double sdWz1997MA11 = -1.68230;

    public static final double sdS19972011  = -0.10201; //parts per billion/yr
    public static final double sdS1997PA11  = 0.08;
    public static final double sdS1997MA11  = 0.08;


    public static final double feetInAMeter = 3.280833333;

}
