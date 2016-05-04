package com.asc.msigeosystems.prism4dmockup;

/**
 * Created by elisabethhuhn on 4/23/2016.
 * This class is a proof of concept for
 * converting WGS84 coordinates into UTM coordinates.
 * The algorithm implemented comes from Karney 2010
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Class representing UTM-coordinates. Based on code from stack overflow.
 * @see <a href="https://stackoverflow.com/questions/176137/java-convert-lat-lon-to-utm">
 *         Stack Overflow</a>
 * @see <a href="https://en.wikipedia.org/wiki/Universal_Transverse_Mercator_coordinate_system">
 *         Wikipedia-entry on UTM</a>
 * @author Rolf Rander Næss
 *
 * Example Usage:
 *   //Create WGS84 coordinate object using lat/long
 *    WGS84 wgs_a = new WGS84(56,-5);
 *
 *  //Create a UTM coordinate object using UTM coordinates
 *    UTM utm_a   = new UTM(31, 'V', 375273.85, 6207884.59);
 *
 *    //Create a WGS84 coordinate object using a UTM coordinate instance (created above)
 *    WGS84 wgs_b = new WGS84(utm_a);
 *
 *    //Create a UTM coordinate object using a WGS84 coordinate instance (created above)
 *    UTM utm_b   = new UTM(wgs_a);

 */
public class Prism4DUTM
{
    private int    mZone;        //1-60
    private char   mHemisphere;  //N or S
    private double mEasting;
    private double mNorthing;
    private char   mLatBand;

    private CharSequence mDatum = "WGS84"; //eg WGS84
    private double mConvergence; //
    private double mScale;

    /***
     * Setters and getters
     * I know this isn't standard, but it works for me today 5/1/2016
     * @return
     */
    public String getEasting()     { return String.valueOf(mEasting);  }
    public String getNorthing()    { return String.valueOf(mNorthing); }
    public String getZone()        { return String.valueOf(mZone);     }
    public String getLatBand()     { return String.valueOf(mLatBand);  }
    public String getHemisphere()  { return String.valueOf(mHemisphere); }
    public String getDatum()       { return String.valueOf(mDatum);    }
    public String getConvergence() { return String.valueOf(mConvergence); }
    public String getScale()       { return String.valueOf(mScale);    }

    public String toString() {
        return String.format("%s %c %c %s %s", mZone, mHemisphere, mLatBand, mEasting, mNorthing);
    }

    /**
     * Tests the exact representation. There might be more representations for
     * the same geographical point with different Hemisphere letters or zones, but that is
     * not taken into account.
     */
    public boolean equals(Object o) {
        if(o instanceof Prism4DUTM) {
            Prism4DUTM other = (Prism4DUTM)o;
            return (mZone == other.mZone) &&
                    (mLatBand == other.mLatBand) &&
                    (mEasting == other.mEasting) &&
                    (mNorthing == other.mNorthing);
        }
        return false;
    }

    @Override
    public int hashCode() {
        long least = Double.doubleToRawLongBits(mEasting);
        long lnort = Double.doubleToRawLongBits(mNorthing);
        long x = least ^ lnort;
        return (int)(x ^ (x >>> 32));
    }



    /***
     * Constructor for the UTM Coordinate
     * Parameters are the coordinate fields as defined by UTM
     *
     *
     */
    public Prism4DUTM(int zone, char letter, char hemi, double easting, double northing) {
        this.mZone = zone;
        this.mLatBand = Character.toUpperCase(mLatBand);
        this.mHemisphere = Character.toUpperCase(mHemisphere);
        this.mEasting = easting;
        this.mNorthing = northing;
    }

    /***
     * Can also construct a UTM coordinate with a single string
     * with the format:
     *  UTMZone + " " + hemisphere + " " + easting + " " + northing
     *
     * @param utm
     */
    public Prism4DUTM(String utm) {
        String[] parts=utm.split(" ");
        mZone=Integer.parseInt(parts[0]);
        mLatBand=parts[1].toUpperCase(Locale.ENGLISH).charAt(0);
        mEasting=Double.parseDouble(parts[2]);
        mNorthing=Double.parseDouble(parts[3]);
    }

    /***
     * Constructor of UTM from a WGS84 Lat/Long coordinate object
     *
     * @param wgs
     */
    public Prism4DUTM(WGS84 wgs) {
        //fromWGS84(wgs.getLatitude(), wgs.getLongitude());
        convertWGStoUTM(wgs.getLatitude(), wgs.getLongitude());
    }



    /***
     * This method actually performas the conversion from
     * WGS84 Lat/Long format to UTM format
     *
     * @param latitude WGS84 latitude in decimal format
     * @param longitude WGS84 longitude in decimal format
     */
    private void fromWGS84(double latitude, double longitude) {

        mZone = (int) Math.floor(longitude/6+31);

        if (latitude<-72)
            mLatBand='C';
        else if (latitude<-64)
            mLatBand='D';
        else if (latitude<-56)
            mLatBand='E';
        else if (latitude<-48)
            mLatBand='F';
        else if (latitude<-40)
            mLatBand='G';
        else if (latitude<-32)
            mLatBand='H';
        else if (latitude<-24)
            mLatBand='J';
        else if (latitude<-16)
            mLatBand='K';
        else if (latitude<-8)
            mLatBand='L';
        else if (latitude<0)
            mLatBand='M';
        else if (latitude<8)
            mLatBand='N';
        else if (latitude<16)
            mLatBand='P';
        else if (latitude<24)
            mLatBand='Q';
        else if (latitude<32)
            mLatBand='R';
        else if (latitude<40)
            mLatBand='S';
        else if (latitude<48)
            mLatBand='T';
        else if (latitude<56)
            mLatBand='U';
        else if (latitude<64)
            mLatBand='V';
        else if (latitude<72)
            mLatBand='W';
        else
            mLatBand='X';

        //mEasting  = getEasting(latitude, longitude, mZone);
        //mNorthing = getNorthing(latitude, longitude, mZone);
        mEasting  = calcEasting(latitude, longitude);
        mNorthing = calcNorthing(latitude, longitude);
        /***
         *
         easting=0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180))/(1-Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180)))*0.9996*6399593.62/Math.pow((1+Math.pow(0.0820944379, 2)*Math.pow(Math.cos(latitude*Math.PI/180), 2)), 0.5)*(1+ Math.pow(0.0820944379,2)/2*Math.pow((0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180))/(1-Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180)))),2)*Math.pow(Math.cos(latitude*Math.PI/180),2)/3)+500000;
         easting=Math.round(easting*100)*0.01;
         northing = (Math.atan(Math.tan(latitude*Math.PI/180)/Math.cos((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))-latitude*Math.PI/180)*0.9996*6399593.625/Math.sqrt(1+0.006739496742*Math.pow(Math.cos(latitude*Math.PI/180),2))*(1+0.006739496742/2*Math.pow(0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))/(1-Math.cos(latitude*Math.PI/180)*Math.sin((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))),2)*Math.pow(Math.cos(latitude*Math.PI/180),2))+0.9996*6399593.625*(latitude*Math.PI/180-0.005054622556*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+4.258201531e-05*(3*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2))/4-1.674057895e-07*(5*(3*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2))/4+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2)*Math.pow(Math.cos(latitude*Math.PI/180),2))/3);
         if (letter<'M')
         northing = northing + 10000000;
         northing=Math.round(northing*100)*0.01;

         */
    }

    private double calcEasting(double lat, double longi){
        double newEasting;
        // To avoid negative numbers, ‘false eastings’ and ‘false northings’ are used:

        //Eastings are referenced in meters from the central meridian of each zone,
        //Eastings are measured from 500,000 metres west of the central meridian
        //Eastings (at the equator) range from 166,021m to 833,978m
        // (the range decreases moving away from the equator);
        // a point on the the central meridian has the value 500,000m.
        double falseEasting = 500e3;

        //In the northern hemisphere, northings are measured in meters from the equator –
        // ranging from 0 at the equator to 9,329,005m at 84°N).
        //In the southern hemisphere they are measured from 10,000,000 metres
        // south of the equator (close to the pole) –
        // ranging from 1,116,915m at 80°S to 10,000,000m at the equator.
        double falseNorthing = 10000e3;


        //Assert that the input parameters are valid (ie are actually numbers)
        if (Double.isNaN(lat) || Double.isNaN(longi)){
            throw new IllegalArgumentException();
        }
        // and within range
        //lat must be larger than -80 and smaller than 84 as UTM does not span the entire globe
        if ((lat < -80.0)|| (lat > 84) ){

            throw new IllegalArgumentException();
        }


        //Each Zone is each 6° of longitude in width
        //So there are 60 zones, numbered 1-60
        //Calculate the UTM Longitude Zone member for this UTM object
        // and set this objects private variable

        //floor gives the largest integer that is less than or equal to the argument
        //Each Zone is each 6° of longitude in width
        mZone = (int) Math.floor(longi/6+31);

        //phi φ is Latitude in radians ( +/- from equator)
        // degrees = radians * (360 / 2 pi)
        // radians = degrees * (2 pi / 360)
        double latRad = lat * Math.PI/180;

        //Calculate the longitude of central meridian
        //Each zone is 6 degrees wide, so the central meridian is 3 degrees inside
        //longitude runs from -180 to +180
        //Lamda zero
        double zoneCentralMeridanRad = (6*mZone - 180 - 3)*Math.PI/180;

        //
        //TODO Need to handle the zone exceptions for Norway and Svalbard


        //lamda  λ is longitude in radians ( +/- from central meridian)
        double longRad = longi * Math.PI/180 - zoneCentralMeridanRad;


        //WGS84 Datum constants
        double ellipsoidA = 6378137;
        double ellipsoidB = 6356752.314245;
        double ellipsoidF = 1/298.257223563;

        // UTM scale on the central meridian
        double K0 = 0.9996;

        //Some interum functions not in the vanilla Math package
        //arctanh(z)
        // atanh function definition is: tanh−1(z) = ½ log((1+z) /(1-z))
        //       or equivalently:        arctanh z = (log (1+z) - log (1-z))/2.
        //so in our case: z = Math.cos(latRad)*Math.sin(longRad)
        double argumentZ = Math.cos(latRad)*Math.sin(longRad);
        double logArgZ = (1+argumentZ) / (1-argumentZ);
        //logArgZ must be between -1 and 1

        double arctanhZ = 0.5 * Math.log(logArgZ);

        //Some arbitrary constants that I don't understand yet
        double K1 = 0.0820944379;
        double K1sqr = Math.pow(K1, 2);

        double K2 = 6399593.62;

        double cosLatSqrd = Math.pow( Math.cos(latRad), 2);

        double term1 = Math.pow( (1 + K1sqr * cosLatSqrd ), 0.5);
        double term2 = 2 * Math.pow(arctanhZ,2) * cosLatSqrd ;
        double term3 = term1 * (1 + K1sqr / term2 / 3);

        newEasting= arctanhZ * K0 * K2 / term3 + falseEasting;

        //an inaccurate way of enforcing precision
        newEasting=Math.round(newEasting*100)*0.01;


        return newEasting;
    }



    private double calcNorthing(double latitude, double longitude){

        double retNorthing;

        double latRad                 = latitude*Math.PI/180;
        double cosLatRad              = Math.cos(latRad);
        double cosLatRadSqrd          = Math.pow(cosLatRad, 2);
        double sinLatRad              = Math.sin(2 * latRad);
        double longCentMeridian       = longitude*Math.PI/180-(6*mZone -183)*Math.PI/180;

        double sinLongRad             = Math.sin(longCentMeridian);

        double cosLatRad_X_sinLongRad = cosLatRad * sinLongRad ;

        double onePlusOverOneMinus    = (1 + cosLatRad_X_sinLongRad ) / (1 - cosLatRad_X_sinLongRad );


        double K2 = 6399593.625;
        double K3 = 0.006739496742;

        double K5 = 4.258201531e-05;
        double K6 = 1.674057895e-07;
        double K7 = 0.005054622556;
        double K8 = 0.9996;


        double term1 = (Math.atan(Math.tan( latRad ) / Math.cos( longCentMeridian )) - latRad );
        double term2 = (sinLatRad * cosLatRadSqrd * cosLatRadSqrd);
        double term4 = ( latRad + sinLatRad / 2);
        double term3 = (term4 + sinLatRad * cosLatRadSqrd );

        double term5 = (latRad - K7 * term4 + K5 * 3 * term3 / 4 - K6 * (5 * 3 * term3 / 4 + term2 ) / 3);
        double term6 = (1 + K3 / 2 * Math.pow(0.5 * Math.log(onePlusOverOneMinus),2) * cosLatRadSqrd);
        double term7 = Math.sqrt(1 + K3 * cosLatRadSqrd);


        retNorthing = term1 * K8 * K2 / term7 * term6 + K8 * K2 * term5;

        if (mLatBand<'M')
            retNorthing = retNorthing + 10000000;

        retNorthing = Math.round(retNorthing *100) * 0.01;


        return retNorthing;
    }
/*******
    private void calcEastingNorthing(double lat, double longi){
        double newEasting;
        double newNorthing;

        // To avoid negative numbers, ‘false eastings’ and ‘false northings’ are used:

        //Eastings are referenced in meters from the central meridian of each zone,
        //Eastings are measured from 500,000 metres west of the central meridian
        //Eastings (at the equator) range from 166,021m to 833,978m
        // (the range decreases moving away from the equator);
        // a point on the the central meridian has the value 500,000m.
        double falseEasting = 500e3;

        //In the northern hemisphere, northings are measured in meters from the equator –
        // ranging from 0 at the equator to 9,329,005m at 84°N).
        //In the southern hemisphere they are measured from 10,000,000 metres
        // south of the equator (close to the pole) –
        // ranging from 1,116,915m at 80°S to 10,000,000m at the equator.
        double falseNorthing = 10000e3;


        //Assert that the input parameters are valid (ie are actually numbers)
        if (Double.isNaN(lat) || Double.isNaN(longi)){
            throw new IllegalArgumentException();
        }
        // and within range
        //lat must be larger than -80 and smaller than 84 as UTM does not span the entire globe
        if ((lat < -80.0)|| (lat > 84) ){

            throw new IllegalArgumentException();
        }


        //Each Zone is each 6° of longitude in width
        //So there are 60 zones, numbered 1-60
        //Calculate the UTM Longitude Zone member for this UTM object
        // and set this objects private variable

        //floor gives the largest integer that is less than or equal to the argument
        //Each Zone is each 6° of longitude in width
        mZone = (int) Math.floor(longi/6+31);

        //phi φ is Latitude in radians ( +/- from equator)
        // degrees = radians * (360 / 2 pi)
        // radians = degrees * (2 pi / 360)
        double latRad = lat * Math.PI/180;

        //Calculate the longitude of central meridian
        //Each zone is 6 degrees wide, so the central meridian is 3 degrees inside
        //longitude runs from -180 to +180
        //Lamda zero
        double zoneCentralMeridanRad = (6*mZone - 180 - 3)*Math.PI/180;

        //
        //TODO Need to handle the zone exceptions for Norway and Svalbard


        //lamda  λ is longitude in radians ( +/- from central meridian)
        double longRad = longi * Math.PI/180 - zoneCentralMeridanRad;


        //WGS84 Datum constants
        double ellipsoidA = 6378137;
        double ellipsoidB = 6356752.314245;
        double ellipsoidF = 1/298.257223563;

        // UTM scale on the central meridian
        double K0 = 0.9996;

        //********************************
        //Some functions not in the vanilla Math package
        //arctanh(z)
        // atanh function definition is: tanh−1(z) = ½ log((1+z) /(1-z))
        //       or equivalently:        arctanh z = (log (1+z) - log (1-z))/2.
        //arctanh argument z
        double z = Math.cos(latRad)*Math.sin(longRad) ;
        double atanhZ = 0.5*Math.log((1+z)/(1-z)) ;

        double magicNumber = Math.pow(0.0820944379,2);
        double magicNumber2 = 6399593.625;
        double cosLatSqrd  = Math.pow(Math.cos(latRad),2);

        newEasting=atanhZ *K0*magicNumber2 / Math.pow((1+magicNumber*cosLatSqrd), 0.5) * (1+ magicNumber/2*Math.pow((atanhZ),2)*cosLatSqrd/3) + falseEasting;


        //suspect rounding method
        newEasting=Math.round(newEasting*100)*0.01;

        newNorthing = (Math.atan(Math.tan(longi*Math.PI/180)/Math.cos( longRad ))-latRad)*K0*magicNumber2/Math.sqrt(1+0.006739496742*Math.pow(Math.cos(latRad),2))*(1+0.006739496742/2*Math.pow(0.5*Math.log((1+Math.cos(latRad)*Math.sin((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))/(1-Math.cos(latRad)*Math.sin((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))),2)*Math.pow(Math.cos(latRad),2))+K0*magicNumber25*(latRad-0.005054622556*(latRad+Math.sin(2*latRad)/2)+4.258201531e-05*(3*(latRad+Math.sin(2*latRad)/2)+Math.sin(2*latRad)*Math.pow(Math.cos(latRad),2))/4-1.674057895e-07*(5*(3*(latRad+Math.sin(2*latRad)/2)+Math.sin(2*latRad)*Math.pow(Math.cos(latRad),2))/4+Math.sin(2*latRad)*Math.pow(Math.cos(latRad),2)*Math.pow(Math.cos(latRad),2))/3);

        if (letter<'M')
            newNorthing = newNorthing + 10000000;
        newNorthing=Math.round(newNorthing*100)*0.01;

        mEasting = newEasting;
        mNorthing = newNorthing;



    }
******/

    /**
 * Converts latitude/longitude to UTM coordinate.
 *
 * Implements Karney’s method, using Krüger series to order n^6,
 * giving results accurate to 5nm for
 * distances up to 3900km from the central meridian.
 *
 *  * @constructor
 * @param  {int}    zone - UTM 6° longitudinal zone (1..60 covering 180°W..180°E).
 * @param  {string} hemisphere - N for northern hemisphere, S for southern hemisphere.
 * @param  {number} easting - Easting in metres from false easting (-500km from central meridian).
 * @param  {number} northing - Northing in metres from equator (N) or
 *                              from false northing -10,000km (S).
 *  Datum UTM coordinate is based on WGS84.
 * @param  {number} [convergence] - Meridian convergence
 *                                (bearing of grid north clockwise from true north), in degrees
 * @param  {number} [scale] - Grid scale factor
 *
 * @throws {}  Invalid Argument Exception when Lat/Long not numbers or not within range

 ***/
    private void convertWGStoUTM (double lat, double longi) throws IllegalArgumentException{


        //Assert that the input parameters are valid (ie are actually numbers)
        if (Double.isNaN(lat) || Double.isNaN(longi)){
            throw new IllegalArgumentException();
        }
        // and within range
        //lat must be larger than -80 and smaller than 84 as UTM does not span the entire globe
        if ((lat < -80.0)|| (lat > 84) ){

            throw new IllegalArgumentException();

        }

        //Hemisphere

        //lat ranges from -80 Southern Hemisphere to equator to 84 Northern Hemisphere
        if (lat >= 0) {
            mHemisphere = 'N';
        } else {
            mHemisphere = 'S';
        }

        //Legal range of the longitude is -180 to +180
        //longitude which is between -180 and -0
        // <0 (or negative) is (in Asian hemisphere)
        // between international date line and Greenwich
        // zones 31 to 60
        //longitudes between +0 and +180
        // > 0 (or positive) are in American hemisphere
        // between Greenwich and International Date Line
        // zones 1 to 30

        //Calculate the longitude zone
        //  then the longitude of the central maridian of that zone
        //

        //Each Zone is each 6° of longitude in width
        //So there are 60 zones, numbered 1-60
        //
        //Calculate the UTM Longitude Zone member for this UTM object
        // and set this objects private variable

        //floor gives the largest integer that is less than or equal to the argument
        //Each Zone is each 6° of longitude in width
        //zone ranges from 1 to 60
        //but remember, longitude ranges from -180 to 0 to +180
        // Date Line -180  through the Americas to 0 Greenwich through Europe to 180 date line
        mZone = (int) Math.floor((longi+180)/6) + 1;

        //phi φ is Latitude in radians ( +/- from equator)
        // degrees = radians * (360 / 2 pi)
        // radians = degrees * (2 pi / 360)
        double latRad = lat * Math.PI/180;

        //Calculate the longitude of central meridian
        //Each zone is 6 degrees wide, so the central meridian is 3 degrees inside
        //longitude runs from -180 to +180
        //Lamda zero
        double zoneCentralMeridanRad = (6*mZone - 180 - 3);
         zoneCentralMeridanRad = zoneCentralMeridanRad*Math.PI/180;


        // ---- handle Norway/Svalbard exceptions
        // MGRS grid zones are 8° tall; 0°N is offset 10 into latitude bands array

        CharSequence mgrsLatBands = "CDEFGHJKLMNPQRSTUVWXX"; // X is repeated for 80-84°N
        int i = (int) Math.floor(lat/8)+10;
        char latBand = mgrsLatBands.charAt(i);

        mLatBand = latBand; //Set the object variable on this

        // degrees = radians * (360 / 2 pi)
        // radians = degrees * (2 PI / 360)
        double sixRadians = 6 * (Math.PI / 180);
        // adjust zone & central meridian for Norway
        if (mZone==31 && latBand=='V' && longi>= 3) { mZone++; zoneCentralMeridanRad += sixRadians; }
        // adjust zone & central meridian for Svalbard
        if (mZone==32 && latBand=='X' && longi<  9) { mZone--; zoneCentralMeridanRad -= sixRadians; }
        if (mZone==32 && latBand=='X' && longi>= 9) { mZone++; zoneCentralMeridanRad += sixRadians; }
        if (mZone==34 && latBand=='X' && longi< 21) { mZone--; zoneCentralMeridanRad -= sixRadians; }
        if (mZone==34 && latBand=='X' && longi>=21) { mZone++; zoneCentralMeridanRad += sixRadians; }
        if (mZone==36 && latBand=='X' && longi< 33) { mZone--; zoneCentralMeridanRad -= sixRadians; }
        if (mZone==36 && latBand=='X' && longi>=33) { mZone++; zoneCentralMeridanRad += sixRadians; }


        //lamda  λ is longitude in radians ( +/- from central meridian)
        double longRad = longi * Math.PI/180 - zoneCentralMeridanRad;

        //Karney:
        //Consider an ellipsoid of revolution with:
        // a = equatorial radius
        // b = polar semi axis, i.e. polar radius
        // f = flattening = (a - b) / a
        // e = eccentricity = sqrt{(f * (2-f)) }
        // n = third flattening = (a-b)/(a+b) = f / (2-f)

        // phi   = latitude
        // lamda = longitude
        //


        //WGS84 Datum constants
        //Karney 2010 page
        //From the Ellipsoid
        double equatorialRadiusA = 6378137; //equitorial radius
        double polarRadiusB = 6356752.314245; //polar semi axis
        //flattening = (equatorialRadius-polarRadius)/equatorialRadius;
        //double inverseFlatteningF = 1/298.257223563; //0.0033528107
        double inverseFlatteningF =  (equatorialRadiusA-polarRadiusB)/equatorialRadiusA ;

        //mean radius = sqroot (equatorialRadius * polarRadius)

        // ---- easting, northing: Karney 2011 Eq 7-14, 29, 35:

        // eccentricity  (.0066943801)1/2 = 0.0818191915
        double e = Math.sqrt(inverseFlatteningF*(2-inverseFlatteningF));
        double ee = Math.sqrt(1 - Math.pow((polarRadiusB/equatorialRadiusA),2));

        //lamda  λ is longitude in radians
        double cosLongRad = Math.cos(longRad);
        double sinLongRad = Math.sin(longRad);
        double tanlongRad = Math.tan(longRad);


        //tau is the tangent of the latitude in radians
        // τ ≡ tanφ, τʹ ≡ tanφʹ; prime (ʹ) indicates angles on the conformal sphere
        //var τ = Math.tan(φ);
        double tau = Math.tan(latRad); //Karney (8)

        // τ = tanLatRad; //Karney (8)

        //Karney (9)
        //var σ = Math.sinh(e*Math.atanh(e*τ/Math.sqrt(1+τ*τ)));

          //********************************
        //Some functions not in the vanilla Math package
        //arctanh(z)
        // atanh function definition is: tanh−1(z) = ½ log((1+z) /(1-z))
        //       or equivalently:        arctanh z = (log (1+z) - log (1-z))/2.


        //*******************************************************

        double atanhArg = e * tau / Math.sqrt(1 + tau * tau);

        //atanhArg must be within -1 to 1
        if ((atanhArg < -1 ) || (atanhArg > 1)){
            //throw an exception for argument out of bounds
            throw new IllegalArgumentException();
        }

        double atanh = 0.5 * Math.log((1+atanhArg)/(1-atanhArg));

        //assure that we are calculating arctanh correctly
        double atanhprime = .5 * (Math.log(1+atanhArg) - Math.log(1-atanhArg));
/***  It is close enough to the tenth decimal place
        if (atanh != atanhprime) {
            //this isn't really an illegal argument, but.....
            throw new IllegalArgumentException();
        }
***/
        double sigma = Math.sinh( e * atanh ) ; //Karney (9)


        //tau prime τʹ  Karney (7)
        //var τʹ = τ*Math.sqrt(1+σ*σ) - σ*Math.sqrt(1+τ*τ);
        //Karney (7)
        double tauPrime = tau * Math.sqrt(1+sigma*sigma) - sigma*Math.sqrt(1+tau*tau);

        //xi prime ξʹ
        //var ξʹ = Math.atan2(τʹ, cosλ);
        //Karney (10)
        //tan-1(tauPrime/cosLamda)    lamda is longitude
        double xiPrime = Math.atan2(tauPrime, cosLongRad);

        //eta prime  ηʹ
        //var ηʹ = Math.asinh(sinλ / Math.sqrt(τʹ*τʹ + cosλ*cosλ));
        //Karney (10)
        //************************************
        //asinh function is also missing from vanilla Math package
        //sinh−1(z) = log[z+(z2 + 1)½ ].

        //so assign termz = (z2 + 1)½
        //asinh = log ( z + termz)


        //var ηʹ = Math.asinh(sinλ / Math.sqrt(τʹ*τʹ + cosλ*cosλ));
        double etaDenominator = Math.sqrt((tauPrime * tauPrime) + (cosLongRad * cosLongRad));
        double z = sinLongRad / etaDenominator;

        double termz = Math.sqrt((z * z) + 1);
        double etaPrime = Math.log(z + termz);

        //prepare the array for Karney (12) and (35)
        // WGS84 n = .0033528107/1.9966471893 = .0016792204
       // double n = inverseFlatteningF / (2 - inverseFlatteningF); // 3rd flattening
        double n = (equatorialRadiusA - polarRadiusB)/ (equatorialRadiusA+polarRadiusB);
        double n2 = n*n; //.0000028197
        double n3 = n*n2;//.0000000047
        double n4 = n*n3;
        double n5 = n*n4;
        double n6 = n*n5; // TODO: compare Horner-form accuracy?
        double n7 = n*n6;
        double n8 = n*n7;

        double a1dot1 = (1/2)*n;
        double a1dot2 = (2/3)*n2;
        double a1dot3 = (5/16)*n3;
        double a1dot4 = (41/180)*n4;
        double a1dot5 = (127/288)*n5;
        double a1dot6 = (7891/37800)*n6;
        double a1dot7 = (72161/387072)*n7;
        double a1dot8 = (18975107/50803200)*n8;


        double a2dot2 = (13/48)*n2;
        double a2dot3 = (3/5)    *n3;
        double a2dot4 = (557/1440) *n4;
        double a2dot5 = (281/630)     *n5;
        double a2dot6 = (1983433/1935360)*n6;
        double a2dot7 = (13769/28800)      *n7;
        double a2dot8 = (148003883/174182400)*n8;


        double a3dot3 = (61/240)*n3;
        double a3dot4 = (103/140)*n4;
        double a3dot5 = (15061/26880)*n5;
        double a3dot6 = (167603/181440)*n6;
        double a3dot7 = (67102379/29030400)*n7;
        double a3dot8 = (79682431/79833600) *n8;


        double a4dot4 = (49561/161280)*n4;
        double a4dot5 = (179/168)*n5;
        double a4dot6 = (6601661/7257600)*n6;
        double a4dot7 = (97445/49896)*n7;
        double a4dot8 = (40176129013.0/7664025600.0)*n8; //does this need to be truncated?


        double a5dot5 = (34729/80640)*n5;
        double a5dot6 = (3418889/1995840)*n6;
        double a5dot7 = (14644087/9123840)*n7;
        double a5dot8 = (2605413599.0/622702080.0)*n8;  //does this need to be truncated?


        double a6dot6 = (212378941/319334400)*n6;
        double a6dot7 = (30705481/10378368)*n7;
        double a6dot8 = (175214326799.0/58118860800.0)*n8;//does this need to be truncated?


        double a7dot7 = (1522256789/1383782400)*n7;
        double a7dot8 = (16759934899.0/3113510400.0)*n8;//does this need to be truncated?


        double a8dot8 = (1424729850961.0/743921418240.0)*n8;//does this need to be truncated?

        // 2πA is the circumference of a meridian
        //Karney (14) and (29)
        double A = (equatorialRadiusA/(1+n)) * (1 + (1/4)*n2 + (1/64)*n4 + (1/256)*n6);// + (25/16384)*n8);

        //alpha α is one-based array (6th order Krüger expressions)
        //The number of terms used in the series calculations below
        int seriesTerms = 5;//remember we count from 0 to seriesTerms,

        //Karney (12) takes it to n4, (35) to n8
        double alpha[] = {
                a1dot1 - a1dot2 + a1dot3 + a1dot4 - a1dot5 + a1dot6,
                         a2dot2 - a2dot3 + a2dot4 + a2dot5 - a2dot6,
                                  a3dot3 - a3dot4 + a3dot5 + a3dot6,
                                           a4dot4 - a4dot5 + a4dot6,
                                                    a5dot5 - a5dot6,
                                                             a6dot6};
        /***
         * No need to perform these calculations unless they are used

        double alpha8[] = {
                a1dot1 - a1dot2 + a1dot3 + a1dot4 - a1dot5 + a1dot6 + a1dot7 - a1dot8,
                         a2dot2 - a2dot3 + a2dot4 + a2dot5 - a2dot6 + a2dot7 + a2dot8,
                                  a3dot3 - a3dot4 + a3dot5 + a3dot6 - a3dot7 + a3dot8,
                                           a4dot4 - a4dot5 + a4dot6 + a4dot7 - a4dot8,
                                                    a5dot5 - a5dot6 + a5dot7 + a5dot8,
                                                             a6dot6 - a6dot7 + a6dot8,
                                                                      a7dot7 - a7dot8,
                                                                               a8dot8};
        double alpha1[] = {
                1/2*n - 2/3*n2 + 5/16*n3 +   41/180*n4 -     127/288*n5 +      7891/37800*n6 ,
                      13/48*n2 -  3/5*n3 + 557/1440*n4 +     281/630*n5 - 1983433/1935360*n6,
                               61/240*n3 -  103/140*n4 + 15061/26880*n5 +   167603/181440*n6,
                                       49561/161280*n4 -     179/168*n5 + 6601661/7257600*n6,
                                                         34729/80640*n5 - 3418889/1995840*n6,
                                                                      212378941/319334400*n6 };
        ******/

        //xi ξ = ξʹ
        //build xi with a series of 6 members
        //Karney (11)
        double xi = xiPrime;
        for (int j=0; j<=seriesTerms; j++){
            xi += alpha[j] * Math.sin(2*(j+1)*xiPrime) * Math.cosh(2*(j+1)*etaPrime);
        }


        //eta η = ηʹ
        //build eta with a series of 6 members
        //Karney (11)
        double eta = etaPrime;
        for (int j=0; j<=seriesTerms; j++){
            eta += alpha[j] * Math.cos(2*(j+1)*xiPrime) * Math.sinh(2*(j+1)*etaPrime);
        }


        //scale the result to give the transverse Mercator easting and northing

        // UTM scale on the central meridian
        double K0 = 0.9996;
        //Karney (13)
        mEasting  = K0 * A * eta;
        mNorthing = K0 * A * xi;


        // ---- convergence: Karney 2011 Eq 23, 24

        //Karney (23)
        //rho pʹ prime
        double rhoPrime = 1;
        for (int j=0; j<=seriesTerms; j++) {
            rhoPrime += (2*(j+1)*alpha[j]) * Math.cos(2*(j+1)*xiPrime) * Math.cosh(2*(j+1)*etaPrime);
        }

        //Karney (23)
        //q prime q'
        double qʹ = 0;
        for (int j=0; j<=seriesTerms; j++){
            qʹ += (2*(j+1)*alpha[j]) * Math.sin(2*(j+1)*xiPrime) * Math.sinh(2*(j+1)*etaPrime);
        }

        //Meridian convergence
        //  bearing of grid north, the y axis, measured clockwise from true north
        // nu = nuPrime + nuDoublePrime   //KARNEY paragraph between (23) and (24)

        //nu prime γʹ
        double nuPrime = Math.atan((tauPrime/Math.sqrt(1+tauPrime*tauPrime)) * tanlongRad);
        //nu double prime γʺ
        double nuDoublePrime = Math.atan2(qʹ, rhoPrime);
        //nu γ
        double nu = nuPrime + nuDoublePrime;



        // ---- scale: Karney 2011 Eq (25)

        //φ is latitude,  sinφ is sin of latitude (in radians)
        double sinLat = Math.sin(latRad);

        double latDenominator = Math.sqrt(tauPrime*tauPrime + cosLongRad*cosLongRad);
        //τ is tanLatRad

        //kʹ kappaPrime
        double kappaPrime = (Math.sqrt(1 - e*e*sinLat*sinLat) * Math.sqrt(1 + tau*tau)) / latDenominator;
        double kappaDoublePrime = (A / equatorialRadiusA) * Math.sqrt(rhoPrime*rhoPrime + qʹ*qʹ);

        //kappa k
        //Scale [karney paragraph between (24) and (25)
        double kappa = K0 * kappaPrime * kappaDoublePrime;

        // ------------

        // To avoid negative numbers, ‘false eastings’ and ‘false northings’ are used:

        //Eastings are referenced in meters from the central meridian of each zone,
        //Eastings are measured from 500,000 metres west of the central meridian
        //Eastings (at the equator) range from 166,021m to 833,978m
        // (the range decreases moving away from the equator);
        // a point on the the central meridian has the value 500,000m.
        double falseEasting = 500e3;


        // shift easting/northing to false origins
        mEasting = mEasting + falseEasting;    // make easting relative to false easting



        //In the northern hemisphere, northings are measured in meters from the equator –
        // ranging from 0 at the equator to 9,329,005m at 84°N).
        //In the southern hemisphere they are measured from 10,000,000 metres
        // south of the equator (close to the pole) –
        // ranging from 1,116,915m at 80°S to 10,000,000m at the equator.
        double falseNorthing = 10000e3;

        // make northing in southern hemisphere relative to false northing
        if (mNorthing < 0){
            mNorthing = mNorthing + falseNorthing;
        }

        // ------------

        // round to reasonable precision of 6 decimal places - nanometer precision
        BigDecimal bdx = new BigDecimal(mEasting).setScale(6, RoundingMode.HALF_UP);
        mEasting = bdx.doubleValue();

        BigDecimal bdy = new BigDecimal(mNorthing).setScale(6, RoundingMode.HALF_UP);
        mNorthing = bdy.doubleValue(); // nm precision



        //report convergence to 9 decimal places
        // toDegrees() converts a radians number to degrees
        // degrees = radians * (360 / 2 pi)
        double nuDegrees = nu * (360 / (2 * Math.PI));
        BigDecimal bdConvergence = new BigDecimal(nuDegrees).setScale(9, RoundingMode.HALF_UP);
        mConvergence = bdConvergence.doubleValue();


        //report scale to 12 decimal places
        BigDecimal bdScale = new BigDecimal(kappa).setScale(12, RoundingMode.HALF_UP);
        mScale = bdScale.doubleValue();


        return ;
    }



    }


