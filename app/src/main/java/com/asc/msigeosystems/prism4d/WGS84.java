package com.asc.msigeosystems.prism4d;

/**
 * Created by elisabethhuhn on 4/23/2016.
 */
/**
 * Class representing WGS84-coordinates. Based on code from stack overflow.
 * @see <a href="https://stackoverflow.com/questions/176137/java-convert-lat-lon-to-utm">
 *         Stack Overflow</a>
 * @see <a href="https://en.wikipedia.org/wiki/World_Geodetic_System">
 *         Wikipedia-entry on WGS-84</a>
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
 *
 */
public class WGS84 {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public WGS84(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public WGS84(UTM utm) {
        fromUTM(utm.getZone(), utm.getLetter(), utm.getEasting(), utm.getNorthing());
    }

    public String toString() {
        char ns = (latitude < 0) ? 'S' : 'N';
        char ew = (longitude< 0) ? 'W' : 'E';
        return String.format("%s%c %s%c", Math.abs(latitude), ns, Math.abs(longitude), ew);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof WGS84) {
            WGS84 other = (WGS84)o;
            return (latitude == other.latitude) &&
                    (longitude == other.longitude);
        }
        return false;
    }

    @Override
    public int hashCode() {
        long llat = Double.doubleToRawLongBits(latitude);
        long llon = Double.doubleToRawLongBits(longitude);
        long x = llat ^ llon;
        return (int)(x ^ (x >>> 32));
    }

    private void fromUTM(int zone, char letter, double easting, double northing)
    {
        double north;
        if (letter>'M') {
            north = northing;
        } else {
            north = northing - 10000000;
        }

        latitude = (north/6366197.724/0.9996+(1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)-0.006739496742*Math.sin(north/6366197.724/0.9996)*Math.cos(north/6366197.724/0.9996)*(Math.atan(Math.cos(Math.atan(( Math.exp((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*( 1 -  0.006739496742*Math.pow((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996 )/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996 - 0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996 )*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996)*3/2)*(Math.atan(Math.cos(Math.atan((Math.exp((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996))*180/Math.PI;
        latitude=Math.round(latitude*10000000);
        latitude=latitude/10000000;

        longitude =Math.atan((Math.exp((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*( north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2* north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3)) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))*180/Math.PI+zone*6-183;
        longitude=Math.round(longitude*10000000);
        longitude=longitude/10000000;
    }

    private void fromUTMOld(int zone, char letter, double easting, double northing)
    {
        double K1 = 6366197.724;
        double K2 = 6399593.625;
        double K3 = 0.006739496742;

        double K8 = 0.9996;
        double K3K8 = 0.9996*6399593.625;


        double north;
        if (letter>'M') {
            north = northing;
        } else {
            north = northing - 10000000.;
        }




        latitude = (north/6366197.724/0.9996+(1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)-0.006739496742*Math.sin(north/6366197.724/0.9996)*Math.cos(north/6366197.724/0.9996)*(Math.atan(Math.cos(Math.atan(( Math.exp((easting - 500000.) / (0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1.-0.006739496742*Math.pow((easting - 500000.) / (0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*( 1 -  0.006739496742*Math.pow((easting - 500000.) / (0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2.)/3.)))/2./Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3./4.*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2.)+Math.pow(0.006739496742*3./4.,2.)*5./3.*(3.*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996 )/2.)+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.-Math.pow(0.006739496742*3./4.,3.)*35./27.*(5.*(3.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/3.))/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1.-0.006739496742*Math.pow((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2.))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996 - 0.006739496742*3./4.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.pow(0.006739496742*3./4.,2.)*5./3.*(3.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.sin(2.*north/6366197.724/0.9996 )*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.-Math.pow(0.006739496742*3./4.,3.)*35./27.*(5.*(3.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1.-0.006739496742*Math.pow((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2.))+north/6366197.724/0.9996))-north/6366197.724/0.9996)*3./2.)*(Math.atan(Math.cos(Math.atan((Math.exp((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1.-0.006739496742*Math.pow((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2.)/3.))-Math.exp(-(easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1.-0.006739496742*Math.pow((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2.)/3.)))/2./Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3./4.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.pow(0.006739496742*3./4.,2.)*5./3.*(3.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.-Math.pow(0.006739496742*3./4.,3.)*35./27.*(5.*(3.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/3.))/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1.-0.006739496742*Math.pow((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3./4.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.pow(0.006739496742*3./4.,2.)*5./3.*(3.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.-Math.pow(0.006739496742*3./4.,3.)*35./27.*(5.*(3.*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2.)+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/3.))/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1-0.006739496742*Math.pow((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2.))+north/6366197.724/0.9996))-north/6366197.724/0.9996))*180./Math.PI;
        latitude=Math.round(latitude*10000000.);
        latitude=latitude/10000000.;

        longitude =Math.atan((Math.exp((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2.)/3.))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2.)/3.)))/2./Math.cos((north-0.9996*6399593.625*( north/6366197.724/0.9996-0.006739496742*3./4.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.pow(0.006739496742*3./4.,2.)*5./3.*(3.*(north/6366197.724/0.9996+Math.sin(2.*north/6366197.724/0.9996)/2.)+Math.sin(2.* north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.-Math.pow(0.006739496742*3./4.,3.)*35./27.*(5.*(3.*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2.)+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/4.+Math.sin(2.*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2.)*Math.pow(Math.cos(north/6366197.724/0.9996),2.))/3.)) / (0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.))))*(1.-0.006739496742*Math.pow((easting-500000.)/(0.9996*6399593.625/Math.sqrt((1.+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2.)))),2.)/2.*Math.pow(Math.cos(north/6366197.724/0.9996),2.))+north/6366197.724/0.9996))*180./Math.PI+zone*6.-183.;
        longitude=Math.round(longitude*10000000.);
        longitude=longitude/10000000.;

/***
        double n18 = north/K1/K8;
        double falseEasting = easting-500000;
        //double cosSqN18 = 1+K3*Math.pow(Math.cos(n18),2);
        double cosSqN18 = Math.pow(Math.cos(n18),2);
        double sinCosN18 = Math.sin(n18)*Math.cos(n18);
        double onePlusCos2N18 = 1+K3*cosSqN18;
        double falseEasting2 = falseEasting/(K8*K2/Math.sqrt(onePlusCos2N18));

        double latitude1 =
                (n18+ (onePlusCos2N18-
                                K3*sinCosN18 *
                                        (Math.atan(Math.cos(Math.atan((Math.exp(falseEasting2*(1-K3*Math.pow(falseEasting2,2)/2*cosSqN18/3))-Math.exp(-falseEasting2*( 1 -K3*Math.pow(falseEasting2,2)/2*cosSqN18/3)))/2/Math.cos((north-K8*K2*(n18-K3*3/4*(n18+Math.sin(2*n18)/2)+Math.pow(K3*3/4,2)*5/3*(3*(n18+Math.sin(2*n18 )/2)+Math.sin(2*n18)*cosSqN18)/4-Math.pow(K3*3/4,3)*35/27*(5*(3*(n18+Math.sin(2*n18)/2)+Math.sin(2*n18)*cosSqN18)/4+Math.sin(2*n18)*cosSqN18*cosSqN18)/3))/(K8*K2/Math.sqrt(onePlusCos2N18))*(1-K3*Math.pow(falseEasting2,2)/2*cosSqN18)+n18)))*Math.tan((north-K8*K2*(n18 - K3*3/4*(n18+Math.sin(2*n18)/2)+Math.pow(K3*3/4,2)*5/3*(3*(n18+Math.sin(2*n18)/2)+Math.sin(2*n18 )*cosSqN18)/4-Math.pow(K3*3/4,3)*35/27*(5*(3*(n18+Math.sin(2*n18)/2)+Math.sin(2*n18)*cosSqN18)/4+Math.sin(2*n18)*cosSqN18*cosSqN18)/3))/(K8*K2/Math.sqrt(onePlusCos2N18))*(1-K3*Math.pow(falseEasting2,2)/2*cosSqN18)+n18))-n18)*3/2)*(Math.atan(Math.cos(Math.atan((Math.exp(falseEasting2*(1-K3*Math.pow(falseEasting2,2)/2*cosSqN18/3))-Math.exp(-falseEasting2*(1-K3*Math.pow(falseEasting2,2)/2*cosSqN18/3)))/2/Math.cos((north-K8*K2*(n18-K3*3/4*(n18+Math.sin(2*n18)/2)+Math.pow(K3*3/4,2)*5/3*(3*(n18+Math.sin(2*n18)/2)+Math.sin(2*n18)*cosSqN18)/4-Math.pow(K3*3/4,3)*35/27*(5*(3*(n18+Math.sin(2*n18)/2)+Math.sin(2*n18)*cosSqN18)/4+Math.sin(2*n18)*cosSqN18*cosSqN18)/3))/(K8*K2/Math.sqrt(onePlusCos2N18))*(1-K3*Math.pow(falseEasting2,2)/2*cosSqN18)+n18)))*Math.tan((north-K8*K2*(n18-K3*3/4*(n18+Math.sin(2*n18)/2)+Math.pow(K3*3/4,2)*5/3*(3*(n18+Math.sin(2*n18)/2)+Math.sin(2*n18)*cosSqN18)/4-Math.pow(K3*3/4,3)*35/27*(5*(3*(n18+Math.sin(2*n18)/2)+Math.sin(2*n18)*cosSqN18)/4+Math.sin(2*n18)*cosSqN18*cosSqN18)/3))/(K8*K2/Math.sqrt(onePlusCos2N18))*(1-K3*Math.pow(falseEasting2,2)/2*cosSqN18)+n18))-n18))*180/Math.PI;

        double longitude1 = Math.atan((Math.exp((falseEasting)/(K3K8/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((falseEasting)/(K3K8/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(falseEasting)/(K3K8/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((falseEasting)/(K3K8/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-K3K8*( north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2* north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3)) / (K3K8/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((falseEasting)/(K3K8/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))*180/Math.PI+zone*6-183;
***/
        /***


               latitude = (north/6366197.724/0.9996+(1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)-0.006739496742*Math.sin(north/6366197.724/0.9996)*Math.cos(north/6366197.724/0.9996)*(Math.atan(Math.cos(Math.atan(( Math.exp((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*( 1 -  0.006739496742*Math.pow((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996 )/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996 - 0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996 )*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996)*3/2)*(Math.atan(Math.cos(Math.atan((Math.exp((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996))*180/Math.PI;
               latitude=Math.round(latitude*10000000);
               latitude=latitude/10000000;

               longitude =Math.atan((Math.exp((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*( north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2* north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3)) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))*180/Math.PI+zone*6-183;
               longitude=Math.round(longitude*10000000);
               longitude=longitude/10000000;
         ***/
    }
}

