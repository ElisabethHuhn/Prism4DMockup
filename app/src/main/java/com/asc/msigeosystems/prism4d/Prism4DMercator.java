package com.asc.msigeosystems.prism4d;

/**
 * Created by Elisabeth Huhn on 12/6/2016.
 * From wikipedia
 */

public class Prism4DMercator {

    //The following two constants are set in Prism4DCoordinateWSG84 and Prism4DCoordinateNAD83
    final private static double R_MAJOR = 6378137.0; //equitorial radius a
    final private static double R_MINOR = 6356752.3142; //polar radius b

    public double[] merc(double x, double y) {
        return new double[] {mercX(x), mercY(y)};
    }

    private double  mercX(double lon) {
        return R_MAJOR * Math.toRadians(lon);
    }

    private double mercY(double lat) {
        if (lat > 89.5) {
            lat = 89.5;
        }
        if (lat < -89.5) {
            lat = -89.5;
        }
        double temp = R_MINOR / R_MAJOR;
        double es = 1.0 - (temp * temp);
        double eccent = Math.sqrt(es);

        double phi = Math.toRadians(lat);
        double sinphi = Math.sin(phi);

        double con = eccent * sinphi;
        double com = 0.5 * eccent;
        con = Math.pow(((1.0-con)/(1.0+con)), com);

        double ts = Math.tan(0.5 * ((Math.PI*0.5) - phi))/con;

        double y = 0 - R_MAJOR * Math.log(ts);
        return y;
    }

}
