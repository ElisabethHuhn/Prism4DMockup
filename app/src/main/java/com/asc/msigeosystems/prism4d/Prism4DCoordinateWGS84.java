package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 5/23/2016.
 *
 * * This coordinate extends the basic capabilities of Prism4DCoordinateLL
 * and adds:
 *
 * A) returns its type from getCoordinateType as "Pris4DCoordinateWGS84"
 * B) remembers its coordinate system specific constants, e.g. Dataum
 *

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

    private CharSequence mThisCoordinateType  = Prism4DCoordinate.sCoordinateTypeWGS84;
    private CharSequence mThisCoordinateClass = Prism4DCoordinate.sCoordinateTypeClassWGS84;



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
        super.initializeDefaultVariables();
    }

    public Prism4DCoordinateWGS84(double latitude, double longitude) {

        //initialize all variables to their defaults
        initializeDefaultVariables();

        latLongDD(latitude, longitude);
    }


    public Prism4DCoordinateWGS84(int latitudeDegree,
                                  int latitudeMinute,
                                  double latitudeSecond,
                                  int longitudeDegree,
                                  int longitudeMinute,
                                  double longitudeSecond) {

        //initialize all variables to their defaults
        super.initializeDefaultVariables();

        latLongDMS( latitudeDegree,
                    latitudeMinute,
                    latitudeSecond,
                    longitudeDegree,
                    longitudeMinute,
                    longitudeSecond);
    }

    public Prism4DCoordinateWGS84(CharSequence latitudeString, CharSequence longitudeString) {

        //initialize all variables to their defaults
        super.initializeDefaultVariables();

        super.latLongDDStrings(latitudeString, longitudeString);
    }





    public Prism4DCoordinateWGS84(CharSequence latitudeDegreeString,
                                  CharSequence latitudeMinuteString,
                                  CharSequence latitudeSecondString,
                                  CharSequence longitudeDegreeString,
                                  CharSequence longitudeMinuteString,
                                  CharSequence longitudeSecondString) {

        //initialize all variables to their defaults
        initializeDefaultVariables();

        latLongDMSStrings(latitudeDegreeString,
                          latitudeMinuteString,
                          latitudeSecondString,
                          longitudeDegreeString,
                          longitudeMinuteString,
                          longitudeSecondString);
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
        this.mThisCoordinateType = Prism4DCoordinate.sCoordinateTypeWGS84;
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
