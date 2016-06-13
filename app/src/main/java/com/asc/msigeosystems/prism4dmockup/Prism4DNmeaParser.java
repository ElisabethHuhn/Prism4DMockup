package com.asc.msigeosystems.prism4dmockup;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by elisabethhuhn on 6/2/2016.
 *
 * copied from https://gist.github.com/javisantana/1326141
 * and modified:
 *  double rather than float
 *  precision greater than two places
 *  also need more sentences, but will modify as needed
 */
public class Prism4DNmeaParser {

    //specify the interface to keep Java happy
    interface SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nemaInfo);
    }

    // utils

    //Latitude arrives with format DDMM.MMM... with variable number of decimals
    //returns decimal degrees DD format
    static double Latitude2Decimal(String lat, String NS) {
        //first get minutes, ignore first two digits which are degrees
        //and padded with leading zeros if necessary
        //convert minutes to degrees by dividing by 60.
        double degrees = Double.parseDouble(lat.substring(2))/60.0d;
        //top two digits are degrees
        degrees +=  Double.parseDouble(lat.substring(0, 2));
        if(NS.startsWith("S")) {
            degrees = -degrees;
        }
        return degrees;
    }


    //Longitude arrives with format DDDMM.MMM... with variable number of decimals
    //returns decimal degrees DD format
    static double Longitude2Decimal(String lon, String WE) {
        //first get minutes, ignore first three digits which are degrees
        //and padded with leading zeros if necessary
        //convert minutes to degrees by dividing by 60.
        double degrees = Double.parseDouble(lon.substring(3))/60.0d;
        //top three digits are degrees
        degrees +=  Double.parseDouble(lon.substring(0, 3));
        if(WE.startsWith("W")) {
            degrees = -degrees;
        }
        return degrees;
    }

    // parsers
    class GPGGA implements SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo) {
            nmeaInfo.mTime                 = Double.parseDouble(tokens[1]);
            nmeaInfo.mLatitude             = Latitude2Decimal(tokens[2], tokens[3]);
            nmeaInfo.mLongitude            = Longitude2Decimal(tokens[4], tokens[5]);
            nmeaInfo.mQuality              = Integer.parseInt(tokens[6]);
            nmeaInfo.mSatellites           = Integer.parseInt(tokens[7]);
            nmeaInfo.mHdop                 = Double.parseDouble(tokens[8]);
            nmeaInfo.mEllipsoidalElevation = Double.parseDouble(tokens[9]);
            nmeaInfo.mGeoid                = Double.parseDouble(tokens[11]);

            if (nmeaInfo.mQuality > 0) {nmeaInfo.mFixed = true;}
            return true;
        }
    }

    class GNGNS implements SentenceParser {
        char quality;
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo) {
            nmeaInfo.mTime                 = Double.parseDouble(tokens[1]);
            nmeaInfo.mLatitude             = Latitude2Decimal(tokens[2], tokens[3]);
            nmeaInfo.mLongitude            = Longitude2Decimal(tokens[4], tokens[5]);
            //see quality below for tokens[6]
            nmeaInfo.mSatellites           = Integer.parseInt(tokens[7]);
            nmeaInfo.mHdop                 = Double.parseDouble(tokens[8]);
            nmeaInfo.mEllipsoidalElevation = Double.parseDouble(tokens[9]);
            nmeaInfo.mGeoid                = Double.parseDouble(tokens[11]);

            int quality = 0;
            //convert the quality indicator to the other format from GGA
            switch (tokens[6]){
                case "N":
                    quality = 0;
                    break;
                case "A":
                    quality = 1;
                    break;
                case "D":
                    quality = 2;
                    break;
                case "P":
                    quality = 3;
                    break;
                case "R":
                    quality = 4;
                    break;
                case "F":
                    quality = 5;
                    break;
                case "E":
                    quality = 6;
                    break;
                case "M":
                    quality = 7;
                    break;
                case "S":
                    quality = 8;
                    break;
            }
            nmeaInfo.mQuality = quality;
            if (nmeaInfo.mQuality > 0) {nmeaInfo.mFixed = true;}
            return true;
        }
    }

    class GPGGL implements SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo) {
            nmeaInfo.mLatitude  = Latitude2Decimal(tokens[1], tokens[2]);
            nmeaInfo.mLongitude = Longitude2Decimal(tokens[3], tokens[4]);
            //if tokens[5] starts with * it is a checksum, else it is time
            if(tokens[5].startsWith("*")) {
                //field is checksum
            } else {
                //time
                nmeaInfo.mTime      = Double.parseDouble(tokens[5]);
            }

            return true;
        }
    }

    class GPRMA implements SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo) {
            nmeaInfo.mLatitude  = Latitude2Decimal(tokens[2], tokens[3]);
            nmeaInfo.mLongitude = Longitude2Decimal(tokens[4], tokens[5]);
            return true;
        }
    }

    class GPRMC implements SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo) {
            nmeaInfo.mTime      = Double.parseDouble(tokens[1]);
            nmeaInfo.mLatitude  = Latitude2Decimal(tokens[3], tokens[4]);
            nmeaInfo.mLongitude = Longitude2Decimal(tokens[5], tokens[6]);
            return true;
        }
    }

    /****************************************************/
    //              Satellite Status classes            //
    /****************************************************/
    class GPGSV implements SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo){
            int i = Integer.parseInt(tokens[3]);
            nmeaInfo.mSatellites  = i;
            Prism4DSatellite satellite;

            for(int l=0; l<=i; l++){
                satellite = new Prism4DSatellite(
                        Integer.parseInt(tokens[(l*4)+4]), //Satellite ID
                        Integer.parseInt(tokens[(l*4)+5]), //elevation
                        Integer.parseInt(tokens[(l*4)+6]), //azimuth
                        Integer.parseInt(tokens[(l*4)+7])); //SNR
                nmeaInfo.setSatellite(satellite);

            }

            return true;
        }
    }

    class GLGSV implements SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo){
            int i = Integer.parseInt(tokens[3]);
            nmeaInfo.mSatellites  = i;
            Prism4DSatellite satellite;

            for(int l=0; l<=i; l++){
                satellite = new Prism4DSatellite(
                        Integer.parseInt(tokens[(l*4)+4]), //Satellite ID
                        Integer.parseInt(tokens[(l*4)+5]), //elevation
                        Integer.parseInt(tokens[(l*4)+6]), //azimuth
                        Integer.parseInt(tokens[(l*4)+7])); //SNR
                nmeaInfo.setSatellite(satellite);
            }

            return true;
        }
    }

    class GPGSA implements SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo){

            Prism4DSatellite satellite;
            int counter = 0;
            int satelliteID;
            for(int l=3; l<=14; l++){
                satelliteID = Integer.parseInt(tokens[l]);

                if (satelliteID > 0){
                    satellite = new Prism4DSatellite(satelliteID);
                    nmeaInfo.setSatellite(satellite);
                    counter++;
                }

            }
            nmeaInfo.setSatellites(counter);
            nmeaInfo.mPdop = Double.parseDouble(tokens[15]);
            nmeaInfo.mHdop = Double.parseDouble(tokens[16]);
            nmeaInfo.mVdop = Double.parseDouble(tokens[17]);

            return true;
        }
    }

    class GLGSA implements SentenceParser {
        public boolean parse(String [] tokens, Prism4DNmea nmeaInfo){
            Prism4DSatellite satellite;
            int counter = 0;
            int satelliteID;
            for(int l=3; l<=14; l++){
                satelliteID = Integer.parseInt(tokens[l]);

                if (satelliteID > 0){
                    satellite = new Prism4DSatellite(satelliteID);
                    nmeaInfo.setSatellite(satellite);
                    counter++;
                }

            }
            nmeaInfo.setSatellites(counter);
            nmeaInfo.mPdop = Double.parseDouble(tokens[15]);
            nmeaInfo.mHdop = Double.parseDouble(tokens[16]);
            nmeaInfo.mVdop = Double.parseDouble(tokens[17]);

            return true;
        }
    }

    /****************************************************/
    //              Position Data class                 //
    /****************************************************/

    public class GPSPosition {
        public double time = 0.0d;
        public double lat = 0.0d;
        public double lon = 0.0d;
        public boolean fixed = false;
        public int quality = 0;
        public double dir = 0.0d;
        public double altitude = 0.0d;
        public double velocity = 0.0d;

        public void updatefix() {
            fixed = quality > 0;
        }

        //note that the formater uses float for the decimals
        public String toString() {
            return String.format("POSITION: lat: %f, lon: %f, time: %f, Q: %d, dir: %f, alt: %f, vel: %f", lat, lon, time, quality, dir, altitude, velocity);
        }
    }


    Prism4DNmea mPrism4DNmea;

    private static final Map<String, SentenceParser> sentenceParsers = new HashMap<String, SentenceParser>();


    /**********************************************/
    /*        Constructor                         */
    /**********************************************/

    public Prism4DNmeaParser() {
        //build the Map of NMEA string types we care about
  /******
        // first - position only NMEA sentences
        sentenceParsers.put("GPGGA", new GPGGA());
        sentenceParsers.put("GPGGL", new GPGGL());
        sentenceParsers.put("GPRMC", new GPRMC());
        sentenceParsers.put("GPRMZ", new GPRMZ());
        //only really good GPS devices have this sentence but ...
        sentenceParsers.put("GPVTG", new GPVTG());
******/

        //then a map of any strings that Prism is interested in

        //IN sample data
        //position
        sentenceParsers.put("GPGGA", new GPGGA());//Global Positioning System Fix Data
        //sentenceParsers.put("GPGNS", new GPGNS());//fix data
        sentenceParsers.put("GNGNS", new GNGNS());
        //sentenceParsers.put("GNGNS", new GLGNS());
        sentenceParsers.put("GPRMC", new GPRMC());

        //satellite information
        sentenceParsers.put("GPGSA", new GPGSA()); //PDOP, HDOP, VHOP
        //sentenceParsers.put("GNGSA", new GNGSA()); //GPS DOP and active satellites
        sentenceParsers.put("GPGSV", new GPGSV());//Satellites in view
        sentenceParsers.put("GLGSV", new GLGSV());



        //not yet seen in sample data
        //position
        //sentenceParsers.put("GPNEP", new GPNEP());
        sentenceParsers.put("GPGGL", new GPGGL());//Geographic position, latitude / longitude
        //sentenceParsers.put("GPRMZ", new GPRMZ());

    }

    //this routine parses the input string for position information
    //returns null if the NMEA sentence isn't one we are looking for
    //Note: if the string is parsed,
    // it is returned in the Prism4DNmea returned by the function
    public Prism4DNmea parse(String line) {

        //A NMEA sentence must start with $
        if(line.startsWith("$")) {
            //get the rest of the sentence after the $
            String nmea = line.substring(1);

            //split the sentence into tokens
            String[] tokens = nmea.split(",");

            //the type of the sentence is the first token
            String type = tokens[0];

            //if this parser class has a parser for this sentence,
            // its type will be represented in the sentenceParsers HashMap
            if(sentenceParsers.containsKey(type)) {
                //if here, we are interested in this string
                //so create an object to return and
                // store the original NMEA sentence in it
                mPrism4DNmea = new Prism4DNmea(line);

                sentenceParsers.get(type).parse(tokens, mPrism4DNmea);
                //finally, set the fixed boolean based on the quality token
                //TODO mPrism4DNmea.updatefix();
                return mPrism4DNmea;
            }

        }
        return null;
    }


    /******************************************************************/

    private void test(){

        final String sentence = "$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A";
        //                          0      1   2     3    4      5    6    7     8     9    10  11 cs

        if (sentence.startsWith("$GPRMC")) {
            String[] strValues = sentence.split(",");
            double latitude = Double.parseDouble(strValues[3])*.01;
            if (strValues[4].charAt(0) == 'S') {
                latitude = -latitude;
            }
            double longitude = Double.parseDouble(strValues[5])*.01;
            if (strValues[6].charAt(0) == 'W') {
                longitude = -longitude;
            }
            double course = Double.parseDouble(strValues[8]);
            System.out.println("latitude="+latitude+" ; longitude="+longitude+" ; course = "+course);
        }
    }
}
