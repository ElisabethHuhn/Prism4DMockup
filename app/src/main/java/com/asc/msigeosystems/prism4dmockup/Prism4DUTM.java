package com.asc.msigeosystems.prism4dmockup;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by elisabethhuhn on 5/25/2016.
 */
public class Prism4DUTM {
    private int    mZone;        //1-60
    private char   mHemisphere;  //N or S
    private double mEasting;
    private double mNorthing;
    private char   mLatBand;

    //for now, the only conversion supported is from WGS84 to UTM
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

    public Prism4DUTM(Prism4DWSG84Coordinate coordinate) {
        convertWGStoUTM(coordinate.getLatitude(), coordinate.getLongitude());
    }

    /**
     * Converts latitude/longitude to UTM coordinate.
     *
     * Implements Karney’s method, using Krüger series to order n^6,
     * giving results accurate to 5nm for
     * distances up to 3900km from the central meridian.
     *
     *  * @constructor
     * @param  {int}    zone       - UTM 6° longitudinal zone (1..60 covering 180°W..180°E).
     * @param  {string} hemisphere - N for northern hemisphere, S for southern hemisphere.
     * @param  {number} easting    - Easting in metres from false easting (-500km from central meridian).
     * @param  {number} northing   - Northing in metres from equator (N) or
     *                               from false northing -10,000km (S).
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
        if ((lat < -80.0)|| (lat > 84.) ){

            throw new IllegalArgumentException();

        }


        //phi φ is Latitude in radians ( +/- from equator)
        // degrees = radians * (360. / 2. pi)
        // radians = degrees * (2. pi / 360.)
        double latRad = lat * Math.PI/180.;

        //Hemisphere depends upon latitude
        //lat ranges from -80 Southern Hemisphere to equator
        // and from the equator to 84 Northern Hemisphere
        if (lat >= 0.) {
            mHemisphere = 'N';
        } else {
            mHemisphere = 'S';
        }

        //Legal range of the longitude is -180 to +180
        if ((longi <-80.) || (longi > 180.)){
            throw new IllegalArgumentException();
        }

        //Longitude -180/+180 is the International Date Line
        // Date Line -180  through the Americas to 0 Greenwich
        // through Europe to 180 date line

        //longitude which is between -180 and -0
        // <0 (or negative) is (in Asian hemisphere)
        // between international date line and Greenwich
        // zones 31 to 60

        //longitudes between +0 and +180
        // > 0 (or positive) are in American hemisphere
        // between Greenwich and International Date Line
        // zones 1 to 30

        //Calculate the longitude zone
        //  then the longitude of the central meridian of that zone
        //

        //Each Zone is each 6° of longitude in width
        //So there are 60 zones, numbered 1-60
        // Zone 1 is longitude 0-5 degrees (and all of the minutes and seconds in 5 degrees),
        // Zone 2 is longitude 6-11 degrees,
        // etc
        //

        //floor gives the largest integer that is less than or equal to the argument
        // remember, input longitude ranges from -180 to 0 to +180, so
        // start by expressing longitude from 0 to 360
        //Each Zone is each 6° of longitude in width
        //zone ranges from 1 to 60 (not 0 to 59, hence the +1)
        mZone = (int) Math.floor((longi+180.)/6.) + 1;

        //Calculate the longitude of central meridian
        //Each zone is 6 degrees wide, so the central meridian is 3 degrees inside
        //longitude runs from -180 to +180, so we need to convert to 0 to 359
        //Lamda zero is what Karney calls this number
        double zoneCentralMeridanRad = ((6.*mZone) - 180. - 3.);
        zoneCentralMeridanRad = zoneCentralMeridanRad*Math.PI/180.;


        // ---- handle Norway/Svalbard exceptions
        // MGRS grid zones are 8° tall; 0°N is offset 10 into latitude bands array

        CharSequence mgrsLatBands = "CDEFGHJKLMNPQRSTUVWXX"; // X is repeated for 80.-84.°N
        int i = (int) Math.floor(lat/8.)+10;
        char latBand = mgrsLatBands.charAt(i);

        mLatBand = latBand; //Set the object variable on this

        // degrees = radians * (360. / 2. pi)
        // radians = degrees * (2. PI / 360.)
        double sixRadians = 6. * (Math.PI / 180.);
        // adjust zone & central meridian for Norway
        if (mZone==31 && latBand=='V' && longi>= 3.) { mZone++; zoneCentralMeridanRad += sixRadians; }
        // adjust zone & central meridian for Svalbard
        if (mZone==32 && latBand=='X' && longi<  9.) { mZone--; zoneCentralMeridanRad -= sixRadians; }
        if (mZone==32 && latBand=='X' && longi>= 9.) { mZone++; zoneCentralMeridanRad += sixRadians; }
        if (mZone==34 && latBand=='X' && longi< 21.) { mZone--; zoneCentralMeridanRad -= sixRadians; }
        if (mZone==34 && latBand=='X' && longi>=21.) { mZone++; zoneCentralMeridanRad += sixRadians; }
        if (mZone==36 && latBand=='X' && longi< 33.) { mZone--; zoneCentralMeridanRad -= sixRadians; }
        if (mZone==36 && latBand=='X' && longi>=33.) { mZone++; zoneCentralMeridanRad += sixRadians; }


        //lamda  λ is longitude in radians ( +/- from central meridian)
        double longRad = (longi * Math.PI/180.) - zoneCentralMeridanRad;

        //Karney:
        //Consider an ellipsoid of revolution with:
        // a = equatorial radius
        // b = polar semi axis, i.e. polar radius
        // f = flattening = (a - b) / a
        // e = eccentricity =  sqrt[f * (2.-f)]
        // n = third flattening = (a-b)/(a+b) = f / (2.-f)

        // phi   = latitude
        // lamda = longitude
        //


        //WGS84 Datum constants
        //Karney 2010 page 5
        //From the Ellipsoid
        double equatorialRadiusA = 6378137.0; //equatorial radius in meters
        double polarRadiusB = 6356752.314245; //polar semi axis
        //flattening = (equatorialRadius-polarRadius)/equatorialRadius;
        double a = equatorialRadiusA;
        double b = polarRadiusB;
        double numerator = a-b;
        double flatten = numerator/a;

        double flatteningF = (equatorialRadiusA-polarRadiusB)/equatorialRadiusA;
        //double flatteningF = 1/298.257223563; //0.0033528107

        //this calculation for inverseFlatteningF is just wrong, it is flattening
        //double inverseFlatteningF =  (equatorialRadiusA-polarRadiusB)/equatorialRadiusA ;

        //mean radius = sqrt (equatorialRadius * polarRadius)

        // ---- easting, northing: Karney 2011 Eq 7-14, 29, 35:

        // eccentricity  (.0066943801)1/2 = 0.0818191915
        double e = Math.sqrt(flatteningF*(2.-flatteningF));
        double ee = Math.sqrt(1. - Math.pow((polarRadiusB/equatorialRadiusA),2.));

        //lamda  λ is longitude in radians
        double cosLongRad = Math.cos(longRad);
        double sinLongRad = Math.sin(longRad);
        double tanlongRad = Math.tan(longRad);


        //tau is the tangent of the latitude in radians
        // τ ≡ tanφ, τʹ ≡ tanφʹ; prime (ʹ) indicates angles on the conformal sphere
        //var τ = Math.tan(φ);
        double tau = Math.tan(latRad); //Karney (8) page 2

        // τ = tanLatRad; //Karney (8)

        //Karney (9)
        //var σ = Math.sinh(e*Math.atanh(e*τ/Math.sqrt(1+τ*τ)));

        //********************************
        //Some functions not in the vanilla Math package
        //arctanh(z)
        // atanh function definition is: tanh−1(z) = ½ log((1+z) /(1-z))
        //       or equivalently:        arctanh z = (log (1+z) - log (1-z))/2.


        //*******************************************************

        double atanhArg = (e * tau) / Math.sqrt(1. + (tau * tau));

        //atanhArg must be within -1 to 1
        if ((atanhArg < -1. ) || (atanhArg > 1.)){
            //throw an exception for argument out of bounds
            throw new IllegalArgumentException();
        }

        double atanh = 0.5 * Math.log((1.+atanhArg)/(1.-atanhArg));

        //assure that we are calculating arctanh correctly
        double atanhprime = .5 * (Math.log(1.+atanhArg) - Math.log(1.-atanhArg));

        /***  It is close enough to the tenth decimal place
        if ((atanh - atanhprime) > .0000000001) {
            //this isn't really an illegal argument, but.....
             throw new IllegalArgumentException();
         }
         ***/
        double sigma = Math.sinh( e * atanh ) ; //Karney (9)


        //tau prime τʹ  Karney (7)
        //var τʹ = τ*Math.sqrt(1+σ*σ) - σ*Math.sqrt(1+τ*τ);
        //Karney (7)
        double tauPrime = (tau * Math.sqrt(1.+(sigma*sigma))) - (sigma*Math.sqrt(1.+tau*tau));

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
        //asinh (z) = log ( z + termz)


        //var ηʹ = Math.asinh(sinλ / Math.sqrt(τʹ*τʹ + cosλ*cosλ));
        double etaDenominator = Math.sqrt((tauPrime * tauPrime) + (cosLongRad * cosLongRad));
        double z = sinLongRad / etaDenominator;

        double termz = Math.sqrt((z * z) + 1.);
        double etaPrime = Math.log(z + termz);

        //prepare the array for Karney (12) and (35)
        // WGS84 n = .0033528107/1.9966471893 = .0016792204
        double n = flatteningF / (2. - flatteningF); // 3rd flattening
        //double n = (equatorialRadiusA - polarRadiusB)/ (equatorialRadiusA+polarRadiusB);
        double n2 = n*n; //.0000028197
        double n3 = n*n2;//.0000000047
        double n4 = n*n3;
        double n5 = n*n4;
        double n6 = n*n5; // TODO: compare Horner-form accuracy?
        //double n7 = n*n6;
        //double n8 = n*n7;



        double a1dot1 = (1.0/2.0)*n;
        double a1dot2 = (2.0/3.0)*n2;
        double a1dot3 = (5.0/16.0)*n3;
        double a1dot4 = (41.0/180.0)*n4;
        double a1dot5 = (127.0/288.0)*n5;
        double a1dot6 = (7891.0/37800.0)*n6;
        //double a1dot7 = (72161/387072)*n7;
        //double a1dot8 = (18975107/50803200)*n8;


        double a2dot2 = (13.0/48.0)*n2;
        double a2dot3 = (3/5.0)    *n3;
        double a2dot4 = (557.0/1440.0) *n4;
        double a2dot5 = (281.0/630.0)     *n5;
        double a2dot6 = (1983433.0/1935360.0)*n6;
        //double a2dot7 = (13769.0/28800.0)      *n7;
        //double a2dot8 = (148003883.0/174182400.0)*n8;


        double a3dot3 = (61.0/240.0)*n3;
        double a3dot4 = (103.0/140.0)*n4;
        double a3dot5 = (15061.0/26880.0)*n5;
        double a3dot6 = (167603.0/181440.0)*n6;
        //double a3dot7 = (67102379.0/29030400.0)*n7;
        //double a3dot8 = (79682431.0/79833600.0) *n8;


        double a4dot4 = (49561.0/161280.0)*n4;
        double a4dot5 = (179.0/168.0)*n5;
        double a4dot6 = (6601661.0/7257600.0)*n6;
        //double a4dot7 = (97445.0/49896.00)*n7;
        //double a4dot8 = (40176129013.0/7664025600.0)*n8; //does this need to be truncated?


        double a5dot5 = (34729.0/80640.0)*n5;
        double a5dot6 = (3418889.0/1995840.0)*n6;
        //double a5dot7 = (14644087.0/9123840.0)*n7;
        //double a5dot8 = (2605413599.0/622702080.0)*n8;  //does this need to be truncated?


        double a6dot6 = (212378941.0/319334400.0)*n6;
        //double a6dot7 = (30705481.0/10378368.0)*n7;
        //double a6dot8 = (175214326799.0/58118860800.0)*n8;//does this need to be truncated?


        //double a7dot7 = (1522256789.0/1383782400.0)*n7;
        //double a7dot8 = (16759934899.0/3113510400.0)*n8;//does this need to be truncated?


        //double a8dot8 = (1424729850961.0/743921418240.0)*n8;//does this need to be truncated? //Horner form


        /****************************************************
         * The following were used to track errors
         * If you see this block, delete it, it is not needed anymore

        double alpha1 = a1dot1 - a1dot2 + a1dot3 + a1dot4 -a1dot5 +a1dot6;
        double alpha2 = a2dot2 - a2dot3 + a2dot4 + a2dot5 - a2dot6;
        double alpha3 = a3dot3 - a3dot4 + a3dot5 + a3dot6;
        double alpha4 = a4dot4 - a4dot5 + a4dot6;
        double alpha5 = a5dot5 - a5dot6;
        double alpha6 = a6dot6;

        //                 n term     nsq       n cubed      n to 4th
        double alpha41 = ( (1.0/2.0)  + ((-2.0/3.)  + ( (5./16.)  + (   (41./180.)   *n)*n)*n)*n);
        double alpha42 = (  (0)   + ((13./48.) + ((-3./5.)   + (  (557./1440.)  *n)*n)*n)*n);
        double alpha43 = (  (0)   + (  (0)   + ((61./240.) + ( (-103./140.)   *n)*n)*n)*n);
        double alpha44 = (  (0)   + (  (0)   + ((  0   ) + ((49561./161280.)*n)*n)*n)*n);


        //                 n term     nsq       n cubed      n to 4th         n to 5th          n to 6th
        double alpha61 = ( (1./2.)+ ((-2./3.) + ( (5./16.)  + (   (41./180.)   + ( (-127./288.)  + (   (7891./37800.)    *n)*n)*n)*n)*n)*n);
        double alpha62 = (  (0)   + ((13./48.)+ ((-3./5.)   + (  (557./1440.)  + (  (281./630.)  + ((1983433./1935360.)  *n)*n)*n)*n)*n)*n);
        double alpha63 = (  (0)   + (  (0)    + ((61./240.) + ( (-103./140.)   + ((15061./26880.)+( (167603./181440.)    *n)*n)*n)*n)*n)*n);
        double alpha64 = (  (0)   + (  (0)    + ((  0   )   + ((49561./161280.)+ ( (-179./168.)  +((6601661./7257600.)   *n)*n)*n)*n)*n)*n);
        double alpha65 = (  (0)   + (  (0)    + ((  0   )   + (     (0)        + ((34729./80640.)+((3418889./1995840.)   *n)*n)*n)*n)*n)*n);
        double alpha66 = (  (0)   + (  (0)    + ((  0   )   + (     (0)        + (     (0)       +((212378941./319334400.)*n)*n)*n)*n)*n)*n);


         **********************************************************/


        //alpha α is one-based array (6th order Krüger expressions)
        //The number of terms used in the series calculations below

        //Karney (12) takes it to n4, (35) to n8
        double alpha[] = {
                a1dot1 - a1dot2 + a1dot3 + a1dot4 - a1dot5 + a1dot6,//alpha[0]
                a2dot2 - a2dot3 + a2dot4 + a2dot5 - a2dot6,//alpha[1]
                a3dot3 - a3dot4 + a3dot5 + a3dot6,//alpha[2]
                a4dot4 - a4dot5 + a4dot6,//alpha[3]
                a5dot5 - a5dot6,//alpha[4]
                a6dot6};//alpha5]

        /***
         * Again, the following was bug chasing, if you see this, delete it
         *
         * No need to perform these calculations unless they are used

         double kalpha4[] = {alpha41, alpha42, alpha43, alpha44};
         double kalpha6[] = {alpha61, alpha62, alpha63, alpha64, alpha65, alpha66};


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
        //the multiplying by (j+1) instead of (j) as in the paper
        // is because we are counting 0 to 5, not 1 - 6
        //Karney (11)
        int seriesTerms = 6;//remember we count from 0 to seriesTerms,
        double xi =  xiPrime;
        double termTwo;
        double jj;
        for (int j=1; j<=seriesTerms; j++){
            jj=(double)j;
            xi = xi + (alpha[j-1] * Math.sin(2.*jj*xiPrime) * Math.cosh(2.*jj*etaPrime));
        }

        /****************
         * if you see this, you know the drill

        double xi4 = xiPrime;
        for (int j=1; j<=seriesTerms-2.; j++){
            jj=(double)j;
            xi4 += kalpha4[j-1] * Math.sin(2.*jj*xiPrime) * Math.cosh(2.*jj*etaPrime);
        }

        double xi6 = xiPrime;
        for (int j=1; j<=seriesTerms; j++){
            jj=(double)j;
            xi6 += kalpha6[j-1] * Math.sin(2*j*xiPrime) * Math.cosh(2*j*etaPrime);
        }

        double xiNoLoop = xiPrime +
                alpha[0] * (Math.sin(2.*xiPrime)  * Math.cosh(2.*etaPrime)) +
                alpha[1] * (Math.sin(4.*xiPrime)  * Math.cosh(4.*etaPrime)) +
                alpha[2] * (Math.sin(6.*xiPrime)  * Math.cosh(6.*etaPrime)) +
                alpha[3] * (Math.sin(8.*xiPrime)  * Math.cosh(8.*etaPrime)) +
                alpha[4] * (Math.sin(10.*xiPrime) * Math.cosh(10.*etaPrime)) +
                alpha[5] * (Math.sin(12.*xiPrime) * Math.cosh(12.*etaPrime)) ;
        double xiNoLoopNoArray = xiPrime +
                alpha1 * (Math.sin(2.*xiPrime)  * Math.cosh(2.*etaPrime)) +
                alpha2 * (Math.sin(4.*xiPrime)  * Math.cosh(4.*etaPrime)) +
                alpha3 * (Math.sin(6.*xiPrime)  * Math.cosh(6.*etaPrime)) +
                alpha4 * (Math.sin(8.*xiPrime)  * Math.cosh(8.*etaPrime)) +
                alpha5 * (Math.sin(10.*xiPrime) * Math.cosh(10.*etaPrime)) +
                alpha6 * (Math.sin(12.*xiPrime) * Math.cosh(12.*etaPrime)) ;

        ***********************************************/

        //eta η = ηʹ
        //build eta with a series of 6 members
        //Karney (11)
        double eta = etaPrime;
        for (int j=1; j<=seriesTerms; j++){
            jj = (double)j;
            eta += alpha[j-1] * Math.cos(2.*jj*xiPrime) * Math.sinh(2.*jj*etaPrime);
        }

        /***********************
        double eta4 = etaPrime;
        for (int j=1; j<=seriesTerms-2; j++){
            jj = (double)j;
            eta4 += kalpha4[j-1] * Math.cos(2*(j+1)*xiPrime) * Math.sinh(2*(j+1)*etaPrime);
        }

        double eta6 = etaPrime;

        for (int j=1; j<=seriesTerms; j++){
            jj = (double)j;
            eta6 += kalpha6[j-1] * Math.cos(2.*jj*xiPrime) * Math.sinh(2.*jj*etaPrime);
        }

        double etaNoLoop =  etaPrime +
                alpha[0] * (Math.cos(2.*xiPrime)  * Math.sinh(2.*etaPrime)) +
                alpha[1] * (Math.cos(4.*xiPrime)  * Math.sinh(4.*etaPrime)) +
                alpha[2] * (Math.cos(6.*xiPrime)  * Math.sinh(6.*etaPrime)) +
                alpha[3] * (Math.cos(8.*xiPrime)  * Math.sinh(8.*etaPrime)) +
                alpha[4] * (Math.cos(10.*xiPrime) * Math.sinh(10.*etaPrime))+
                alpha[5] * (Math.cos(12.*xiPrime) * Math.sinh(12.*etaPrime));

        double etaNoLoopNoArray = etaPrime +
                alpha1 * (Math.cos(2.*xiPrime)  * Math.sinh(2.*etaPrime)) +
                alpha2 * (Math.cos(4.*xiPrime)  * Math.sinh(4.*etaPrime)) +
                alpha3 * (Math.cos(6.*xiPrime)  * Math.sinh(6.*etaPrime)) +
                alpha4 * (Math.cos(8.*xiPrime)  * Math.sinh(8.*etaPrime)) +
                alpha5 * (Math.cos(10.*xiPrime) * Math.sinh(10.*etaPrime))+
                alpha6 * (Math.cos(12.*xiPrime) * Math.sinh(12.*etaPrime));



        //double falseEasting = 500000;
        double literal1 = 0.0820944379;
        double lit1SqrPlus1 = 1.+Math.pow(literal1,2.);
        double literal2 = 0.9996;
        double literal3 = 6399593.62;
        double literal23 = literal2*literal3;
        double literal4 = 0.006739496742;
        double literal5 = 0.005054622556;
        double literal6 = 4.258201531e-05;
        double literal7 = 1.674057895e-07;
         **********************************/

        // 2πA is the circumference of a meridian
        //Karney (14) and (29)
        double A = (equatorialRadiusA/(1.+n)) * (1. + (1./4.)*n2 + (1./64.)*n4 + (1./256.)*n6);// + (25/16384)*n8);

        //scale the result to give the transverse Mercator easting and northing
        // UTM scale on the central meridian
        double K0 = 0.9996;
        //Karney (13)
        //Karney's x is mEasting and y is mNorthing
        mEasting  = K0 * A * eta;
        mNorthing = K0 * A * xi;

        /********************************

        double mEasting4 = K0 * A * eta4;
        double mEasting6 = K0 * A * eta6;
        double mNorthing4 = K0 * A * xi4;
        double mNorthing6 = K0 * A * xi6;

         **********************/

        // ---- convergence: Karney 2011 Eq 23, 24

        //Karney (23)
        //rho pʹ prime
        double rhoPrime = 1.;
        for (int j=1; j<=seriesTerms; j++) {
            jj = (double)j;
            rhoPrime += (2.*jj*alpha[j-1]) * Math.cos(2.*jj*xiPrime) * Math.cosh(2.*jj*etaPrime);
        }

        //Karney (23)
        //q prime q'
        double qʹ = 0.;
        for (int j=1; j<=seriesTerms; j++){
            jj = (double)j;
            qʹ += (2.*jj*alpha[j-1]) * Math.sin(2.*jj*xiPrime) * Math.sinh(2.*jj*etaPrime);
        }

        //Meridian convergence
        //  bearing of grid north, the y axis, measured clockwise from true north
        // nu = nuPrime + nuDoublePrime   //KARNEY paragraph between (23) and (24)

        //nu prime γʹ
        double nuPrime = Math.atan((tauPrime/Math.sqrt(1.+tauPrime*tauPrime)) * tanlongRad);
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
        double kappaPrime = (Math.sqrt(1. - e*e*sinLat*sinLat) * Math.sqrt(1. + tau*tau)) / latDenominator;
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

        /***********************************
        mEasting4 = mEasting4 + falseEasting;
        mEasting6 = mEasting6 + falseEasting;
         *********************************/

        //In the northern hemisphere, northings are measured in meters from the equator –
        // ranging from 0 at the equator to 9,329,005m at 84°N).
        //In the southern hemisphere they are measured from 10,000,000 metres
        // south of the equator (close to the pole) –
        // ranging from 1,116,915m at 80°S to 10,000,000m at the equator.
        double falseNorthing = 10000e3;

        // make northing in southern hemisphere relative to false northing
        if (mNorthing < 0.){
            mNorthing = mNorthing + falseNorthing;
            /***************************************
            mNorthing4 = mNorthing4 + falseNorthing;
            mNorthing6 = mNorthing6 + falseNorthing;
             *************************************/
        }

        double x = mEasting;
        double y = mNorthing;

        /***************************************

        double x4 = mEasting4;
        double x6 = mEasting6;


        double y4 = mNorthing4;
        double y6 = mNorthing6;
         ***************************************/
        // ------------

        // round to reasonable precision of 6 decimal places - nanometer precision
        BigDecimal bdx = new BigDecimal(mEasting).setScale(6, RoundingMode.HALF_UP);
        mEasting = bdx.doubleValue();

        BigDecimal bdy = new BigDecimal(mNorthing).setScale(6, RoundingMode.HALF_UP);
        mNorthing = bdy.doubleValue(); // nm precision



        //report convergence to 9 decimal places
        // toDegrees() converts a radians number to degrees
        // degrees = radians * (360 / 2 pi)
        double nuDegrees = nu * (360. / (2. * Math.PI));
        BigDecimal bdConvergence = new BigDecimal(nuDegrees).setScale(9, RoundingMode.HALF_UP);
        mConvergence = bdConvergence.doubleValue();


        //report scale to 12 decimal places
        BigDecimal bdScale = new BigDecimal(kappa).setScale(12, RoundingMode.HALF_UP);
        mScale = bdScale.doubleValue();


        return ;
    }
}
