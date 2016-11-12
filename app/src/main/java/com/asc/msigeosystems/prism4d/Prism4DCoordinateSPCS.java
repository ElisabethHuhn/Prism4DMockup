package com.asc.msigeosystems.prism4d;

/**
 * This is coordinate in the State Plane Coordinate System
 * Created by Elisabeth Huhn on 11/11/2016.
 */
public class Prism4DCoordinateSPCS extends Prism4DCoordinateEN {

    //variables and setters/getters at super level

    //For now, just a dummy constructor:

    public Prism4DCoordinateSPCS() {
    }

/****************************************************************

 //Conversion Routines are called by the constructors
    public Prism4DCoordinateSPCS(Prism4DCoordinateWGS84 coordinate) {
        //initialize all variables to their defaults
        super.initializeDefaultVariables();
        convertWGSToSPCS(coordinate.getLatitude(), coordinate.getLongitude());
        mDatum = "WGS84"; //eg WGS84
    }

    public Prism4DCoordinateSPCS(Prism4DCoordinateNAD83 coordinate) {
        //initialize all variables to their defaults
        super.initializeDefaultVariables();
        convertNADtoSPCS(coordinate.getLatitude(), coordinate.getLongitude());
        mDatum = "NAD83"; //eg NAD83
    }

    private void convertWGStoUTM (double lat, double longi) throws IllegalArgumentException {
        setWgsConstants(); //use the WGS constants for the conversion
        convertLLtoSPCS(lat, longi);
    }

    private void convertNADtoSPCS (double lat, double longi) throws IllegalArgumentException {
        setNadConstants(); //use the NAD constants for the conversion
        convertLLtoSPCS(lat, longi); //on superclass
    }

    private void setWgsConstants() {
        mEquatorialRadiusA = Prism4DCoordinateWGS84.sEquatorialRadiusA;
        mPolarRadiusB      = Prism4DCoordinateWGS84.sPolarRadiusB;
    }

    private void setNadConstants() {
        mEquatorialRadiusA = Prism4DCoordinateNAD83.sEquatorialRadiusA;
        mPolarRadiusB      = Prism4DCoordinateNAD83.sPolarRadiusB;
    }

 ****************************************************************/

}
