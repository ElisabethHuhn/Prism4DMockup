package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 5/25/2016.
 * This coordinate extends the basic capabilities of Prism4DCoordinateEN
 * and adds:
 *
 * A) returns its type from getCoordinateType as "Pris4DCoordinateUTM"
 * B) remembers its coordinate system specific constants, e.g. Dataum
 *
 */

public class Prism4DCoordinateUTM extends Prism4DCoordinateEN {

    //variables and setters/getters at super level
    private CharSequence mThisCoordinateType  = Prism4DCoordinate.sCoordinateTypeUTM;
    private CharSequence mThisCoordinateClass = Prism4DCoordinate.sCoordinateTypeClassUTM;


    /********
     *
     * Constructors
     *
     **********/


    public Prism4DCoordinateUTM(){super.initializeDefaultVariables();}

    public Prism4DCoordinateUTM(Prism4DCoordinateWGS84 coordinate) {
        //initialize all variables to their defaults
        super.initializeDefaultVariables();
        convertWGStoUTM(coordinate.getLatitude(), coordinate.getLongitude());
        mDatum = "WGS84"; //eg WGS84
    }

    public Prism4DCoordinateUTM(Prism4DCoordinateNAD83 coordinate) {
        //initialize all variables to their defaults
        super.initializeDefaultVariables();
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


    /********
     *
     * Setters and Getters
     *
     **********/

    //This method returns the type of the instance actually instantiated
    @Override
    public CharSequence getCoordinateType() { return mThisCoordinateType; }
    public void         setCoordinateType(){
        this.mThisCoordinateType = Prism4DCoordinate.sCoordinateTypeUTM;
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


    private void setWgsConstants() {
        mEquatorialRadiusA = Prism4DCoordinateWGS84.sEquatorialRadiusA;
        mPolarRadiusB      = Prism4DCoordinateWGS84.sPolarRadiusB;
    }

    private void setNadConstants() {
        mEquatorialRadiusA = Prism4DCoordinateNAD83.sEquatorialRadiusA;
        mPolarRadiusB      = Prism4DCoordinateNAD83.sPolarRadiusB;
    }


}
