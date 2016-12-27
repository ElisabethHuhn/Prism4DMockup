package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 5/23/2016.
 *
 * This coordinate extends the basic capabilities of Prism4DCoordinateLL
 * and adds:
 *
 * A) returns its type from getCoordinateType as "Pris4DCoordinateNAD83"
 * B) remembers its coordinate system specific constants, e.g. Dataum
 *
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


    private CharSequence mThisCoordinateType  = Prism4DCoordinate.sCoordinateTypeNAD83;
    private CharSequence mThisCoordinateClass = Prism4DCoordinate.sCoordinateTypeClassNAD83;

    /********
     *
     * Constructors
     *
     **********/

    public  Prism4DCoordinateNAD83(){
        super.initializeDefaultVariables();
    }

    public Prism4DCoordinateNAD83(Prism4DCoordinateWGS84 wsg84Coordinate) {

        // TODO: 12/14/2016 complete conversion from WSG84 to NAD83
    }






    /********
     *
     * Setters and Getters
     *
     **********/

    //This method returns the type of the instance actually instantiated
    @Override
    public CharSequence getCoordinateType() { return mThisCoordinateType; }
    public void         setCoordinateType(){
        this.mThisCoordinateType = Prism4DCoordinate.sCoordinateTypeNAD83;
    }


    //This method returns the type of the instance as a string for UI display
    @Override
    public CharSequence getCoordinateClass(){ return mThisCoordinateClass; }



    /********
     *
     * Static methods
     *
     **********/


    /********
     *
     * Member methods
     *
     **********/

    protected void initializeDefaultVariables(){
        //set all variables with defaults, so that none are null
        //I know that one does not have to initialize int's etc, but
        //to be explicit about the initialization, do it anyway

        //initialize all variables common to EN coordinates
        super.initializeDefaultVariables();

        //initialize all variables from this level

    }


}
