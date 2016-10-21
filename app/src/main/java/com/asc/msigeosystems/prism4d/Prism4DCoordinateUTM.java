package com.asc.msigeosystems.prism4d;

/**
 * Created by elisabethhuhn on 5/25/2016.
 */
public class Prism4DCoordinateUTM extends Prism4DCoordinateEN {

    //variables and setters/getters at super level

    public Prism4DCoordinateUTM(Prism4DCoordinateWGS84 coordinate) {
        convertWGStoUTM(coordinate.getLatitude(), coordinate.getLongitude());
        mDatum = "WGS84"; //eg WGS84
    }

    public Prism4DCoordinateUTM(Prism4DCoordinateNAD83 coordinate) {
        convertNADtoUTM(coordinate.getLatitude(), coordinate.getLongitude());
        mDatum = "NAD83"; //eg NAD83
    }

    private void convertWGStoUTM (double lat, double longi) throws IllegalArgumentException {
        setWgsConstants(); //use the WGS constants for the conversion
        convertLLtoUTM(lat, longi);
    }

    private void convertNADtoUTM (double lat, double longi) throws IllegalArgumentException {
        setNadConstants(); //use the NAD constants for the conversion
        convertLLtoUTM(lat, longi); //on superclass
    }

    private void setWgsConstants() {
        mEquatorialRadiusA = Prism4DCoordinateWGS84.sEquatorialRadiusA;
        mPolarRadiusB      = Prism4DCoordinateWGS84.sPolarRadiusB;
    }

    private void setNadConstants() {
        mEquatorialRadiusA = Prism4DCoordinateNAD83.sEquatorialRadiusA;
        mPolarRadiusB      = Prism4DCoordinateNAD83.sPolarRadiusB;
    }


}
