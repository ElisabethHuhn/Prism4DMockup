package com.asc.msigeosystems.prism4d;

/**
 * Created by elisabethhuhn on 4/23/2016.
 */

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
public class UTM
{
    private double easting;
    private double northing;
    private int zone;
    private char letter;

    public double getEasting() {
        return easting;
    }

    public double getNorthing() {
        return northing;
    }

    public int getZone() {
        return zone;
    }

    public char getLetter() {
        return letter;
    }

    public String toString() {
        return String.format("%s %c %s %s", zone, letter, easting, northing);
    }

    /**
     * Tests the exact representation. There might be more representations for
     * the same geographical point with different letters or zones, but that is
     * not taken into account.
     */
    public boolean equals(Object o) {
        if(o instanceof UTM) {
            UTM other = (UTM)o;
            return (zone == other.zone) &&
                    (letter == other.letter) &&
                    (easting == other.easting) &&
                    (northing == other.northing);
        }
        return false;
    }

    @Override
    public int hashCode() {
        long least = Double.doubleToRawLongBits(easting);
        long lnort = Double.doubleToRawLongBits(northing);
        long x = least ^ lnort;
        return (int)(x ^ (x >>> 32));
    }

    public UTM(int zone, char letter, double easting, double northing) {
        this.zone = zone;
        this.letter = Character.toUpperCase(letter);
        this.easting = easting;
        this.northing = northing;
    }

    public UTM(String utm) {
        String[] parts=utm.split(" ");
        zone=Integer.parseInt(parts[0]);
        letter=parts[1].toUpperCase(Locale.ENGLISH).charAt(0);
        easting=Double.parseDouble(parts[2]);
        northing=Double.parseDouble(parts[3]);
    }

    public UTM(WGS84 wgs) {
        fromWGS84(wgs.getLatitude(), wgs.getLongitude());
    }

    private void fromWGS84(double latitude, double longitude) {
        zone= (int) Math.floor(longitude/6+31);
        if (latitude<-72)
            letter='C';
        else if (latitude<-64)
            letter='D';
        else if (latitude<-56)
            letter='E';
        else if (latitude<-48)
            letter='F';
        else if (latitude<-40)
            letter='G';
        else if (latitude<-32)
            letter='H';
        else if (latitude<-24)
            letter='J';
        else if (latitude<-16)
            letter='K';
        else if (latitude<-8)
            letter='L';
        else if (latitude<0)
            letter='M';
        else if (latitude<8)
            letter='N';
        else if (latitude<16)
            letter='P';
        else if (latitude<24)
            letter='Q';
        else if (latitude<32)
            letter='R';
        else if (latitude<40)
            letter='S';
        else if (latitude<48)
            letter='T';
        else if (latitude<56)
            letter='U';
        else if (latitude<64)
            letter='V';
        else if (latitude<72)
            letter='W';
        else
            letter='X';
        easting=0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180))/(1-Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180)))*0.9996*6399593.62/Math.pow((1+Math.pow(0.0820944379, 2)*Math.pow(Math.cos(latitude*Math.PI/180), 2)), 0.5)*(1+ Math.pow(0.0820944379,2)/2*Math.pow((0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180))/(1-Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180)))),2)*Math.pow(Math.cos(latitude*Math.PI/180),2)/3)+500000;
        easting=Math.round(easting*100)*0.01;
        northing = (Math.atan(Math.tan(latitude*Math.PI/180)/Math.cos((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))-latitude*Math.PI/180)*0.9996*6399593.625/Math.sqrt(1+0.006739496742*Math.pow(Math.cos(latitude*Math.PI/180),2))*(1+0.006739496742/2*Math.pow(0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))/(1-Math.cos(latitude*Math.PI/180)*Math.sin((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))),2)*Math.pow(Math.cos(latitude*Math.PI/180),2))+0.9996*6399593.625*(latitude*Math.PI/180-0.005054622556*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+4.258201531e-05*(3*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2))/4-1.674057895e-07*(5*(3*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2))/4+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2)*Math.pow(Math.cos(latitude*Math.PI/180),2))/3);
        if (letter<'M')
            northing = northing + 10000000;
        northing=Math.round(northing*100)*0.01;
    }


    private void fromWGS84Old(double latitude, double longitude) {

        zone = (int) Math.floor(longitude/6+31);

        if (latitude<-72)
            letter='C';
        else if (latitude<-64)
            letter='D';
        else if (latitude<-56)
            letter='E';
        else if (latitude<-48)
            letter='F';
        else if (latitude<-40)
            letter='G';
        else if (latitude<-32)
            letter='H';
        else if (latitude<-24)
            letter='J';
        else if (latitude<-16)
            letter='K';
        else if (latitude<-8)
            letter='L';
        else if (latitude<0)
            letter='M';
        else if (latitude<8)
            letter='N';
        else if (latitude<16)
            letter='P';
        else if (latitude<24)
            letter='Q';
        else if (latitude<32)
            letter='R';
        else if (latitude<40)
            letter='S';
        else if (latitude<48)
            letter='T';
        else if (latitude<56)
            letter='U';
        else if (latitude<64)
            letter='V';
        else if (latitude<72)
            letter='W';
        else
            letter='X';

        //easting  = getEasting (latitude, longitude, zone);
       // northing = getNorthing(latitude, longitude, zone );
        easting=0.5*Math.log((1.+Math.cos(latitude*Math.PI/180.)*Math.sin(longitude*Math.PI/180.-(6.*zone-183.)*Math.PI/180.))/(1.-Math.cos(latitude*Math.PI/180.)*Math.sin(longitude*Math.PI/180.-(6.*zone-183.)*Math.PI/180.)))*0.9996*6399593.62/Math.pow((1.+Math.pow(0.0820944379, 2.)*Math.pow(Math.cos(latitude*Math.PI/180.), 2.)), 0.5)*(1+ Math.pow(0.0820944379,2.)/2.*Math.pow((0.5*Math.log((1.+Math.cos(latitude*Math.PI/180.)*Math.sin(longitude*Math.PI/180.-(6.*zone-183.)*Math.PI/180.))/(1.-Math.cos(latitude*Math.PI/180.)*Math.sin(longitude*Math.PI/180.-(6.*zone-183.)*Math.PI/180.)))),2.)*Math.pow(Math.cos(latitude*Math.PI/180.),2.)/3.)+500000.;
        easting=Math.round(easting*100.)*0.01;
        northing = (Math.atan(Math.tan(latitude*Math.PI/180.)/Math.cos((longitude*Math.PI/180.-(6.*zone -183.)*Math.PI/180.)))-latitude*Math.PI/180.)*0.9996*6399593.625/Math.sqrt(1.+0.006739496742*Math.pow(Math.cos(latitude*Math.PI/180.),2.))*(1.+0.006739496742/2.*Math.pow(0.5*Math.log((1.+Math.cos(latitude*Math.PI/180.)*Math.sin((longitude*Math.PI/180.-(6.*zone -183.)*Math.PI/180.)))/(1.-Math.cos(latitude*Math.PI/180.)*Math.sin((longitude*Math.PI/180.-(6.*zone -183.)*Math.PI/180.)))),2.)*Math.pow(Math.cos(latitude*Math.PI/180.),2.))+0.9996*6399593.625*(latitude*Math.PI/180.-0.005054622556*(latitude*Math.PI/180.+Math.sin(2.*latitude*Math.PI/180.)/2.)+4.258201531e-05*(3.*(latitude*Math.PI/180.+Math.sin(2.*latitude*Math.PI/180.)/2.)+Math.sin(2.*latitude*Math.PI/180.)*Math.pow(Math.cos(latitude*Math.PI/180.),2.))/4.-1.674057895e-07*(5.*(3.*(latitude*Math.PI/180.+Math.sin(2.*latitude*Math.PI/180.)/2.)+Math.sin(2.*latitude*Math.PI/180.)*Math.pow(Math.cos(latitude*Math.PI/180.),2.))/4.+Math.sin(2.*latitude*Math.PI/180.)*Math.pow(Math.cos(latitude*Math.PI/180.),2.)*Math.pow(Math.cos(latitude*Math.PI/180.),2.))/3.);
        if (letter<'M')
            northing = northing + 10000000.;
        northing=Math.round(northing*100.)*0.01;

        double temp = getNewEasting(latitude, longitude);

    }

    private double getEasting(double latitude, double longitude, int eastZone){

        double retEasting;

        double latRad                 = latitude*Math.PI/180.;
        double cosLatRad              = Math.cos(latRad);
        double pow2cosLatRad          = Math.pow(cosLatRad, 2.);
        double longMinusZoneRad       = longitude*Math.PI/180.-(6.*eastZone-183.)*Math.PI/180.;

        double sinLongRad             = Math.sin(longMinusZoneRad);

        double cosLatRad_X_sinLongRad = cosLatRad * sinLongRad ;

        double onePlusOverOneMinus  = (1. + cosLatRad_X_sinLongRad ) / (1. - cosLatRad_X_sinLongRad);


        double K1 = Math.pow(0.0820944379, 2.);

        //northing constant has decimal .625
        double K2 = 6399593.62;//NOTE this is different from the northing constant

        double K8 = 0.9996;

        double term1 = 0.5 * Math.log( onePlusOverOneMinus );
        double term2 = Math.pow( (1. + K1 * pow2cosLatRad ), 0.5);




        retEasting = term1 * K8 * (K2 / term2) *
                (1. + K1 / 2. * Math.pow(term1,2.) * pow2cosLatRad / 3.) +
                500000.;

        retEasting= Math.round(retEasting*100.)*0.01;

        return retEasting;
    }

    private double getNorthing(double latitude, double longitude, int nZone){

        double retNorthing;

        double latRad                 = latitude*Math.PI/180.;
        double cosLatRad              = Math.cos(latRad);
        double pow2cosLatRad          = Math.pow(cosLatRad, 2.);
        double sinLatRad              = Math.sin(2. * latRad);
        double longMinusZoneRad       = longitude*Math.PI/180.-(6.*nZone -183.)*Math.PI/180.;

        double sinLongRad             = Math.sin(longMinusZoneRad);

        double cosLatRad_X_sinLongRad = cosLatRad * sinLongRad ;

        double onePlusOverOneMinus    = (1. + cosLatRad_X_sinLongRad ) / (1. - cosLatRad_X_sinLongRad );


        double K2 = 6399593.625;
        double K3 = 0.006739496742;

        double K5 = 4.258201531e-05;
        double K6 = 1.674057895e-07;
        double K7 = 0.005054622556;
        double K8 = 0.9996;


        double term1 = (Math.atan(Math.tan( latRad ) / Math.cos( longMinusZoneRad )) - latRad );
        double term2 = (sinLatRad * pow2cosLatRad * pow2cosLatRad);
        double term4 = ( latRad + sinLatRad / 2.);
        double term3 = (term4 + sinLatRad * pow2cosLatRad );

        double term5 = (latRad - K7 * term4 + K5 * 3. * term3 / 4. - K6 * (5. * 3. * term3 / 4. + term2 ) / 3.);
        double term6 = (1. + K3 / 2. * Math.pow(0.5 * Math.log(onePlusOverOneMinus),2.) * pow2cosLatRad);
        double term7 = Math.sqrt( 1. + K3 * pow2cosLatRad );


        retNorthing = term1 * K8 * K2 / term7 * term6 + K8 * K2 * term5;

        if (letter<'M')
            retNorthing = retNorthing + 10000000.;

        retNorthing = Math.round(retNorthing *100.) * 0.01;


        return retNorthing;
    }
    private double getNewEasting(double latitude, double longitude) {

        double falseEasting = 500000.;
        double K2 = 6399593.62;

        double literal1 = 0.0820944379;
        double lit1SqrPlus1 = 1+Math.pow(literal1,2);
        double K8 = 0.9996;

        double literal23 = K8*K2;
        double literal4 = 0.006739496742;
        double literal5 = 0.005054622556;
        double literal6 = 4.258201531e-05;
        double literal7 = 1.674057895e-07;


        double latRad = latitude*Math.PI/180.;
        double longRad = longitude*Math.PI/180.-(6.*zone-183.)*Math.PI/180.;

        double cosLat = Math.cos(latRad);
        double sin2Lat = Math.sin(2.*latRad);
        double tanLat = Math.tan(latRad);
        double cosLatSqr = Math.pow(cosLat, 2.);
        double latPlusSinLat = (latRad+sin2Lat/2.);


        double sinLong = Math.sin(longRad);
        double cosLong = Math.cos(longRad);

        double arcSomething = 0.5*Math.log((1+cosLat*sinLong)/(1.-cosLat*sinLong));
        double arcSomethingSqr = Math.pow(arcSomething,2.);

        double arcSomethingElse = Math.pow(0.5*Math.log((1.+cosLat*sinLong)/(1.-cosLat*sinLong)),2.);


        double sqrtCosLat = Math.pow((lit1SqrPlus1*cosLatSqr), 0.5);

        //easting=arcSomething * literal23/sqrtCosLat * lit1SqrPlus1/2 * arcSomethingSqr * cosLatSqr/3;

        easting=arcSomething *
                K8 *
                K2/Math.pow((1+Math.pow(0.0820944379,2)*Math.pow(cosLat,2.)), 0.5) *
                (1. +
                        Math.pow(0.0820944379,2.)/2. *
                        Math.pow((arcSomething),2.) *
                        Math.pow(cosLat,2.)/3.)         +
                500000.;

        easting = easting + falseEasting;


        easting=Math.round(easting*100.)*0.01;

        northing = (Math.atan(tanLat/cosLong)-latRad)*literal23/Math.sqrt(1.+literal4*cosLatSqr)*(1.+literal4/2.*arcSomethingSqr*cosLatSqr)+literal23*(latRad-literal5*(latPlusSinLat)+literal6*(3.*(latPlusSinLat)+sin2Lat*cosLatSqr)/4.-literal7*(5.*(3.*(latPlusSinLat)+sin2Lat*cosLatSqr)/4.+sin2Lat*cosLatSqr*cosLatSqr)/3.);
        if (letter<'M')
            northing = northing + 10000000.;
        northing=Math.round(northing*100.)*0.01;

        return easting;

    }
    }


