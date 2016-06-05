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
public class NmeaParser {

    //specify the interface to keep Java happy
    interface SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position);
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
        public boolean parse(String [] tokens, GPSPosition position) {
            position.time = Double.parseDouble(tokens[1]);
            position.lat = Latitude2Decimal(tokens[2], tokens[3]);
            position.lon = Longitude2Decimal(tokens[4], tokens[5]);
            position.quality = Integer.parseInt(tokens[6]);
            position.altitude = Double.parseDouble(tokens[9]);
            return true;
        }
    }

    class GPGGL implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            position.lat = Latitude2Decimal(tokens[1], tokens[2]);
            position.lon = Longitude2Decimal(tokens[3], tokens[4]);
            position.time = Double.parseDouble(tokens[5]);
            return true;
        }
    }

    class GPRMC implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            position.time = Double.parseDouble(tokens[1]);
            position.lat = Latitude2Decimal(tokens[3], tokens[4]);
            position.lon = Longitude2Decimal(tokens[5], tokens[6]);
            position.velocity = Double.parseDouble(tokens[7]);
            position.dir = Double.parseDouble(tokens[8]);
            return true;
        }
    }

    class GPVTG implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            position.dir = Double.parseDouble(tokens[3]);
            return true;
        }
    }

    class GPRMZ implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            position.altitude = Double.parseDouble(tokens[1]);
            return true;
        }
    }

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

    GPSPosition position = new GPSPosition();

    private static final Map<String, SentenceParser> sentenceParsers = new HashMap<String, SentenceParser>();

    public NmeaParser() {
        sentenceParsers.put("GPGGA", new GPGGA());
        sentenceParsers.put("GPGGL", new GPGGL());
        sentenceParsers.put("GPRMC", new GPRMC());
        sentenceParsers.put("GPRMZ", new GPRMZ());
        //only really good GPS devices have this sentence but ...
        sentenceParsers.put("GPVTG", new GPVTG());
    }

    public GPSPosition parse(String line) {

        //A NMEA sentence must start with $
        if(line.startsWith("$")) {
            //get the rest of the sentence after the $
            String nmea = line.substring(1);
            //split the sentence into tokens
            String[] tokens = nmea.split(",");
            //the type of the sentence is the first token
            String type = tokens[0];
            //TODO check crc
            //if this parser class has a parser for this sentence,
            // its type will be represented in the sentenceParsers HashMap
            if(sentenceParsers.containsKey(type)) {
                sentenceParsers.get(type).parse(tokens, position);
            }
            //finally, set the fixed boolean based on the quality token
            position.updatefix();
        }

        return position;
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
