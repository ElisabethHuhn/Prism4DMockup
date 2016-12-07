package com.asc.msigeosystems.prism4d;

/**
 * This is coordinate in the State Plane Coordinate System
 * Created by Elisabeth Huhn on 11/11/2016.
 *
 * * This coordinate extends the basic capabilities of Prism4DCoordinateEN
 * and adds:
 *
 * A) returns its type from getCoordinateType as "Pris4DCoordinateSPCS"
 * B) remembers its coordinate system specific constants, e.g. Dataum
 *

 */
public class Prism4DCoordinateSPCS extends Prism4DCoordinateEN {

    //variables and setters/getters at super level

    private CharSequence mThisCoordinateType  = Prism4DCoordinate.sCoordinateTypeSPCS;
    private CharSequence mThisCoordinateClass = Prism4DCoordinate.sCoordinateTypeClassSPCS;

    //For now, just a dummy constructor:

    public Prism4DCoordinateSPCS() {super.initializeDefaultVariables(); }

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

 ****************************************************************/

    /********
     *
     * Setters and Getters
     *
     **********/

    //This method returns the type of the instance actually instantiated
    @Override
    public CharSequence getCoordinateType() { return mThisCoordinateType; }
    public void         setCoordinateType(){
        this.mThisCoordinateType = Prism4DCoordinate.sCoordinateTypeSPCS;
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


/****************************************************************
 private void setWgsConstants() {
 mEquatorialRadiusA = Prism4DCoordinateWGS84.sEquatorialRadiusA;
 mPolarRadiusB      = Prism4DCoordinateWGS84.sPolarRadiusB;
 }

 private void setNadConstants() {
 mEquatorialRadiusA = Prism4DCoordinateNAD83.sEquatorialRadiusA;
 mPolarRadiusB      = Prism4DCoordinateNAD83.sPolarRadiusB;
 }

 ****************************************************************/

    public static final double E0 = 2000000.0000; //meters Easting of projection and grid origion
    public static final double E0feet = 6561666.667; //E0 in feet
    public static final double Nb = 500000.0000;  //meters northing of the grid base
    public static final double Nbfeet = 1640416.667; //feet

    private void someFunctions(double B, double L){ //B = Latitude, L = Longitude


        double a = 6378137.;       //Semimajor axis of geodetic ellipsoid - equitorial radius
        double b = 6356752.314245; //Semiminor axis of the geodetic ellipsoid - polar radius
        double f = (a - b) / a;    // f = flattening
        double e = Math.sqrt((2*f) - (f*f)); //First eccentricity of geodetic ellipsoid sqrt (2f-f squared)
        double e_sq      = e*e;

        double K0 = 0.9996;       // scale on the central meridian

        //B = Parallel of geodetic latitude, North is positive
         double Bs; //Southern standard parallel
        /****** remove this hack **************/
        /****** its to get rid of errors in the interrum *******/
        Bs = 1.;

        double sin_Bs    = Math.sin(Bs);
        double sin_Bs_sq = sin_Bs * sin_Bs;
        double cos_Bs    = Math.cos(Bs);

        double Bn; //Northern standarad parallel
        /****** remove this hack **************/
        /****** its to get rid of errors in the interrum *******/
        Bn = 1.;

        double sin_Bn    = Math.sin(Bn);
        double sin_Bn_sq = sin_Bn * sin_Bn;
        double cos_Bn    = Math.cos(Bn);

        double Bb; //Latitude of the grid origin
        /****** remove this hack **************/
        /****** its to get rid of errors in the interrum *******/
        Bb = 1.;
        double sin_Bb    = Math.sin(Bb);

        double Bo; //Central parallel, the latitude of the true projection origin
        //double sin_Bo    = Math.sin(Bo);
        /****** remove this hack **************/
        /****** its to get rid of errors in the interrum *******/
        Bo = 1.;

        //L = Meridian of geodetic longitude, West is positive
        double Lo; //Central meridian. Longitude of the true and grid origin
        /****** remove this hack **************/
        /****** its to get rid of errors in the interrum *******/
        Lo = 1.;


        /******************************************************************/
        /*                Zone Constants                                  */
        /******************************************************************/

        double Qs_Term1 = Math.log(     (1.+ sin_Bs)   /   (1.-sin_Bs)    );
        double Qs_Term2 = Math.log(   (1.+ (e*sin_Bs)) /(1.- (e*sin_Bs))  );
        double Qs = (Qs_Term1 - (e*Qs_Term2))/2.;

        double Qn_Term1 = Math.log(     (1.+ sin_Bn)   /   (1.-sin_Bn)    );
        double Qn_Term2 = Math.log(   (1.+ (e*sin_Bn)) /(1.- (e*sin_Bn))  );
        double Qn       = (Qn_Term1 - (e*Qn_Term2))/2.;

        double Qb_Term1 = Math.log(     (1.+ sin_Bb)   /   (1.-sin_Bb)    );
        double Qb_Term2 = Math.log(   (1.+ (e*sin_Bb)) /(1.- (e*sin_Bb))  );
        double Qb       = (Qb_Term1 - (e*Qb_Term2))/2.;


        double Ws       = Math.sqrt(1. - (e_sq * sin_Bs_sq));
        double Wn       = Math.sqrt(1. - (e_sq * sin_Bn_sq));


        double sin_Bo  = Math.log( (Wn * cos_Bs) / (Ws * cos_Bn) ) / (Qn - Qs);
        double sin_Bo_sq = sin_Bo * sin_Bo;

        double Qo_Term1 = Math.log(     (1.+ sin_Bo)   /   (1.-sin_Bo)    );
        double Qo_Term2 = Math.log(   (1.+ (e*sin_Bo)) /(1.- (e*sin_Bo))  );
        double Qo       = (Qo_Term1 - (e*Qo_Term2))/2.;

        double Wo       = Math.sqrt(1. - (e_sq * sin_Bo_sq));

        //K = Mapping radius at the equator
        double K_term1 = (a * cos_Bs * Math.exp(Qs * sin_Bo)) / (Ws * sin_Bo);
        double K_term2 = (a * cos_Bn * Math.exp(Qn * sin_Bo)) / (Wn * sin_Bo);
        double K       = K_term1 - K_term2;

        // R = Mapping radius at indicated latitude B
        double Rb = K / (Math.exp(Qb * sin_Bo));
        double Ro = K / (Math.exp(Qo * sin_Bo));

        //k = Grid scale factor at general point
        double ko = (Wo * Math.tan(Bo * Ro)) / a;

        double No = Rb + Nb - Ro;
        double Eo;
        /****** remove this hack **************/
        /****** its to get rid of errors in the interrum *******/
        Eo = 1.;



        /******************************************************************/
        /*               Direct Conversion Computation                    */
        /******************************************************************/

        /******************************************************************
         *   Conversion:
         *   Inputs:  (B, L) - geodetic coordinates(latitude, longitude) to
         *   Outputs: (N, E) - Lambert Grid Coordinates
         *   with convergence angle C
         *   and  grid scale factor k
         ******************************************************************/

        double sin_B = Math.sin(B);
        double sin_B_sq = sin_B * sin_B;
        double cos_B = Math.cos(B);

        double Q_Term1 = Math.log(     (1.+ sin_B)   /   (1.-sin_B)    );
        double Q_Term2 = Math.log(   (1.+ (e*sin_B)) /(1.- (e*sin_B))  );
        double Q = (Q_Term1 - (e*Q_Term2))/2.;

        // R = Mapping radius at indicated latitude B
        double R = K / (Math.exp(Q * sin_B));

        //C = Convergence angle
        double C = (Lo - L) * sin_Bo;
        double cos_C = Math.cos(C);
        double sin_C = Math.sin(C);

        //Northing Coordinate
        double N = Rb + Nb - (R * cos_C);

        //Easting Coordinate
        double E = Eo + (R * sin_C);


        //k = Grid scale factor
        double k_term1 = Math.sqrt(1. - (e_sq * sin_B_sq));
        double k_term2 = (R * sin_Bo) / (a * cos_B);
        double k = k_term1 * k_term2;



        /******************************************************************
         *   Inverse Conversion
         *   Inputs:  (N, E) - Lambert Grid Coordinates   to
         *   Outputs: (B, L) - geodetic coordinates(latitude, longitude)
         *   with convergence angle C
         *   and  grid scale factor k
         ******************************************************************/



    }

}
