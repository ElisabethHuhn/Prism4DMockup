package com.asc.msigeosystems.prism4d;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;

import com.asc.msigeosystems.prism4dmockup.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Elisabeth Huhn on 6/13/2016.
 *
 * This is a holder for constants and utilities that are not data type specific
 * enough to live in another class
 */
public class Prism4DConstantsAndUtilities {

    /********
     *
     * Static constants
     *
     **********/
    public static final int sMicrometerDigitsOfPrecision = 6;
    public static final int sNanometerDigitsOfPrecision = 9;
    public static final int sPicometerDigitsOfPrecision = 12;

    public static final double sGravitationalConstant = 3.986004418e14; // cubic meters/square seconds
    public static final double sMeanAngularVelocity   = 7.292115e-5;    // rads / s


    //used in calculating Moments of Inertia, based on EGM2008
    public static final double sDynSecDegZonal = -4.84165143790815e-4; //
    public static final double sSectorialHarmonics = 2.43938357328313e-6; //
    

    //Constants for transforming WGS84 to NAD83
    public static final double sTx19972011 = 0.99343; // meters
    public static final double sTx1997PS11 = 0.9080;  
    public static final double sTx1997MA11 = 0.9080;
    
    public static final double sTy19972011 = -1.90331; //meters
    public static final double sTy1997PA11 = -2.0161;
    public static final double sTy1997MA11 = -2.0161;

    public static final double sTz19972011 = -0.52655; //meters
    public static final double sTz1997PA11 = -0.5653;
    public static final double sTz1997MA11 = -0.5653;

    public static final double sWx19972011 = 125.63787; //nanoradians
    public static final double sWx1997PA11 = 134.49216;
    public static final double sWx1997MA11 = 140.45537;

    public static final double sWy19972011 = 45.70072; //nanoradians
    public static final double sWy1997PA11 = 65.29956;
    public static final double sWy1997MA11 = 50.51759;

    public static final double sWz19972011 = 56.23524; //nanoradians
    public static final double sWz1997PA11 = 13.14815;
    public static final double sWz1997MA11 = 43.28416;
    
    public static final double sS19972011  = 1.71504; //parts per billion
    public static final double sS1997PA11  = 1.10;
    public static final double sS1997MA11  = 1.10;

    public static final double sdTx19972011 = 0.00079; // meters/yr
    public static final double sdTx1997PS11 = 0.0001;
    public static final double sdTx1997MA11 = 0.0001;

    public static final double sdTy19972011 = -0.00060; //meters/yr
    public static final double sdTy1997PA11 = 0.0001;
    public static final double sdTy1997MA11 = 0.0001;

    public static final double sdTz19972011 = -0.00134; //meters/yr
    public static final double sdTz1997PA11 = -0.0018;
    public static final double sdTz1997MA11 = -0.0018;

    public static final double sdWx19972011 = 0.32322; //nanoradians/yr
    public static final double sdWx1997PA11 = -1.86168;
    public static final double sdWx1997MA11 = -0.09696;

    public static final double sdWy19972011 = -3.67217; //nanoradians/yr
    public static final double sdWy1997PA11 = 4.88207;
    public static final double sdWy1997MA11 = 0.50905;

    public static final double sdWz19972011 = -0.24886; //nanoradians/yr
    public static final double sdWz1997PA11 = -10.59803;
    public static final double sdWz1997MA11 = -1.68230;

    public static final double sdS19972011  = -0.10201; //parts per billion/yr
    public static final double sdS1997PA11  = 0.08;
    public static final double sdS1997MA11  = 0.08;


    //Constants used in conversion from NAD83 to State Plane Coordinates
    public static final double E0 = 2000000.0000; //meters Easting of projection and grid origion
    public static final double E0feet = 6561666.667; //E0 in feet
    public static final double Nb = 500000.0000;  //meters northing of the grid base
    public static final double Nbfeet = 1640416.667; //feet

    public static final double feetPerMeter = 3.280833333;
    public static final double cmPerInch =    100./feetPerMeter; //2.54
    public static final double inchesPerMeter = feetPerMeter/12.;   //39.37

    /********
     *
     * Static functions
     *
     **********/

    public static double convertMetersToFeet(double meters) {
        //function converts Feet to Meters.
        //truncate to a reasonable number of decimal digits
        double feet = meters * Prism4DConstantsAndUtilities.feetPerMeter;
        BigDecimal bd = new BigDecimal(feet).setScale(sMicrometerDigitsOfPrecision, RoundingMode.HALF_UP);
        feet = bd.doubleValue();

        return (feet);  // official conversion rate of Meters to Feet

    }

    public static double convertFeetToMeters(double feet){
        double meters = feet / Prism4DConstantsAndUtilities.feetPerMeter;
        //truncate to a reasonable number of decimal digits
        BigDecimal bd = new BigDecimal(meters).setScale(sMicrometerDigitsOfPrecision, RoundingMode.HALF_UP);
        meters = bd.doubleValue();

        return (meters);
    }


    public static boolean convertMetersToFeet(Context context, EditText metersWidget, EditText feetWidget){
        double meters, feet;
        String meterString, feetString;

        try {
            //The inputs are limited to digital numbers by the xml, so don't need to check
            meterString = metersWidget.getText().toString();
            meters = Double.parseDouble(meterString);
        } catch  (NumberFormatException e) {
            return false;
        }

        feet = meters * Prism4DConstantsAndUtilities.feetPerMeter;
        BigDecimal bd = new BigDecimal(feet).setScale(sMicrometerDigitsOfPrecision, RoundingMode.HALF_UP);
        feet = bd.doubleValue();

        feetWidget.setText(String.valueOf(feet));

        if (meters < 0){
            metersWidget.setTextColor(ContextCompat.getColor(context, R.color.colorNegNumber));
            feetWidget  .setTextColor(ContextCompat.getColor(context, R.color.colorNegNumber));
        } else {
            metersWidget.setTextColor(ContextCompat.getColor(context, R.color.colorPosNumber));
            feetWidget  .setTextColor(ContextCompat.getColor(context, R.color.colorPosNumber));
        }

        return true;

    }

    public static boolean convertFeetToMeters(Context context, EditText metersWidget, EditText feetWidget){
        double meters, feet;
        String meterString, feetString;

        try {
            //The inputs are limited to digital numbers by the xml, so don't need to check
            feetString = feetWidget.getText().toString();
            feet = Double.parseDouble(feetString);

        } catch  (NumberFormatException e) {
            return false;
        }

        meters = feet  / Prism4DConstantsAndUtilities.feetPerMeter;
        //truncate to a reasonable number of decimal digits
        BigDecimal bd = new BigDecimal(meters).setScale(sMicrometerDigitsOfPrecision, RoundingMode.HALF_UP);
        meters = bd.doubleValue();

        metersWidget.setText(String.valueOf(meters));


        if (feet < 0){
            metersWidget.setTextColor(ContextCompat.getColor(context, R.color.colorNegNumber));
            feetWidget  .setTextColor(ContextCompat.getColor(context, R.color.colorNegNumber));
        } else {
            metersWidget.setTextColor(ContextCompat.getColor(context, R.color.colorPosNumber));
            feetWidget  .setTextColor(ContextCompat.getColor(context, R.color.colorPosNumber));
        }

        return true;
    }



}
