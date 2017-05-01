package com.asc.msigeosystems.prism4d;

import android.content.ContentValues;
import android.database.Cursor;

import com.asc.msigeosystems.prism4d.database.Prism4DDatabaseManager;
import com.asc.msigeosystems.prism4d.database.Prism4DSqliteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Elisabeth Huhn on 11/25/2016.
 *
 * This manager hides the fact that the Coordinate objects are mirrored in a DB
 * If a Coordinate is not found in memory, the DB is queried for it.
 * If a Coordinate is added to memory,     it will also be added to the DB
 * If a Coordinate is updated in memory,   it is also updated in teh DB
 * If a Coordinate is deleted from memory, it is also deleted from the DB
 *
 * This manager is a bit different from the other object managers of Prism4D in that
 * it manages more than one type of coordinate. It manages all the subclasses of
 * Prism4DCoordinate. The subclasses are in different tables, depending on whether
 * they are principally Lat/Long coordinate systems or Easting/Northing coordinate systems.
 */
public class Prism4DCoordinateManager {

    /************************************/
    /********* Static Constants *********/
    /************************************/
    //public static final int COORDINATE_NOT_FOUND = -1;


    /************************************/
    /********* Static Variables *********/
    /************************************/
    private static Prism4DCoordinateManager ourInstance ;

    /************************************/
    /********* Member Variables *********/
    /************************************/


    /************************************/
    /********* Static Methods   *********/
    /************************************/
    public static Prism4DCoordinateManager getInstance() {
        if (ourInstance == null){
            ourInstance = new Prism4DCoordinateManager();

        }
        return ourInstance;
    }

    /************************************/
    /********* Constructors     *********/
    /************************************/
    private Prism4DCoordinateManager() {

        //mCoordinateList = new ArrayList<>();

        //The DB isn't read in until the first time a coordinate is accessed
    }


    /************************************/
    /********* Setters/Getters  *********/
    /************************************/

    /*******************************************/
    /*********     CRUD Methods        *********/
    /*******************************************/


    //******************  CREATE *******************************************

   public boolean addCoordinateMean(Prism4DCoordinateMean coordinateMean){
       Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
       return databaseManager.addCoordinateMean(coordinateMean);
   }

    //******************  READ *******************************************

    public ArrayList<Prism4DCoordinateMean> getAllCoordinateMeanFromDB (int projectID){
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.getAllCoordinateMeanFromDB(projectID);
    }

    public Prism4DCoordinateMean getCoordinateMeanFromDB(int coordinateMeanID, int projectID){
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.getCoordinateMeanFromDB(coordinateMeanID, projectID);
    }

    //******************  UPDATE *******************************************



    public  int updateCoordinateMean(Prism4DCoordinateMean coordinate) {
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.updateCoordinateMean(coordinate);
    }





    //******************  DELETE *******************************************


    //The return code indicates how many rows affected
    public int removeCoordinateMean(int coordinateMeanID, int projectID){
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.removeCoordinateMean(coordinateMeanID, projectID);
    }

    //The return code indicates how many rows affected
    public int removeProjectCoordinatesMean(int projectID){
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.removeProjectCoordinatesMean(projectID);
    }



    /********************************************/
    /********* Translation Utility Methods  *****/
    /********************************************/

    //returns the ContentValues object needed to add/update the COORDINATE to/in the DB
    public ContentValues getCVFromCoordinate(Prism4DCoordinate coordinate){
        //convert the Prism4DCoordinate object into a ContentValues object containing a coordinate
        ContentValues cvCoordinate = new ContentValues();
        //put(columnName, value);
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_ID,       coordinate.getCoordinateID());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_PROJECT_ID,  coordinate.getProjectID());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_POINT_ID,     coordinate.getPointID());

        CharSequence coordinateType = coordinate.getCoordinateType();
        if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeWGS84)){
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_TYPE,
                             Prism4DCoordinate.sCoordinateDBTypeWGS84);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83)){
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_TYPE,
                             Prism4DCoordinate.sCoordinateDBTypeNAD83);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeUTM)){
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_TYPE,
                             Prism4DCoordinate.sCoordinateDBTypeUTM);

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeSPCS)){
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_TYPE,
                             Prism4DCoordinate.sCoordinateDBTypeSPCS);
        } else {
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_TYPE,
                             Prism4DCoordinate.sCoordinateDBTypeUnknown);
        }

        int valCoord = 0; //false
        boolean validCoordinate = coordinate.isValidCoordinate();
        if (validCoordinate)valCoord = 1;//true;

        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_VALID_COORD, valCoord);


        //The rest of the attributes depend upon the specific subtype

        if ((coordinateType.equals(Prism4DCoordinate.sCoordinateTypeWGS84))||
            (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83))    ){

            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_TIME,
                                            ((Prism4DCoordinateLL)coordinate).getTime());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitude());
 /*
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_DEGREE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeDegree());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_MINUTE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeMinute());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_SECOND,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeSecond());
   */
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE,
                                            ((Prism4DCoordinateLL)coordinate).getLongitude());
/*
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_DEGREE,
                                            ((Prism4DCoordinateLL)coordinate).getLongitudeDegree());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_MINUTE,
                                            ((Prism4DCoordinateLL)coordinate).getLongitudeMinute());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_SECOND,
                                            ((Prism4DCoordinateLL)coordinate).getLongitudeSecond());
 */
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_ELEVATION,
                                            ((Prism4DCoordinateLL)coordinate).getElevation());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_GEOID,
                                            ((Prism4DCoordinateLL)coordinate).getGeoid());


        } else {
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_EASTING,
                                            ((Prism4DCoordinateEN)coordinate).getEasting());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_NORTHING,
                                            ((Prism4DCoordinateEN)coordinate).getNorthing());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_ELEVATION,
                                            ((Prism4DCoordinateEN)coordinate).getElevation());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_ZONE,
                                            ((Prism4DCoordinateEN)coordinate).getZone());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_HEMISPHERE,
                                String.valueOf(((Prism4DCoordinateEN)coordinate).getHemisphere()));
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_LATBAND,
                                String.valueOf(((Prism4DCoordinateEN)coordinate).getLatBand()));
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_DATUM,
                                            (String) ((Prism4DCoordinateEN)coordinate).getDatum());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_CONVERGENCE,
                                            ((Prism4DCoordinateEN)coordinate).getConvergence());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_SCALE,
                                            ((Prism4DCoordinateEN)coordinate).getScale());

        }
        return cvCoordinate;
    }


    //returns the Prism4DCoordinate characterized by the position within the Cursor
    //returns null if the position is larger than the size of the Cursor
    //NOTE    this routine does NOT add the coordinate to the list maintained by this CoordinateManager
    //        The caller of this routine is responsible for that.
    //        This is only a translation utility
    //WARNING As the app is not multi-threaded, this routine is not synchronized.
    //        If the app becomes multi-threaded, this routine must be made thread safe
    //WARNING The cursor is NOT closed by this routine. It assumes the caller will close the
    //         cursor when it is done with it
    public Prism4DCoordinate getCoordinateFromCursor(Cursor cursor, int position){

        int last = cursor.getCount();
        if (position >= last) return null;

        cursor.moveToPosition(position);

        int coordinateType = cursor.getInt(
                                    cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_TYPE));

        if (coordinateType == Prism4DCoordinate.sCoordinateDBTypeWGS84) {
            Prism4DCoordinateWGS84 coordinate = new Prism4DCoordinateWGS84();
            coordinate = (Prism4DCoordinateWGS84) getLLCoordinateFromCursor(coordinate, cursor);

            coordinate.setCoordinateType();
            return coordinate;

        } else if (coordinateType == Prism4DCoordinate.sCoordinateDBTypeNAD83) {
                Prism4DCoordinateNAD83 coordinate = new Prism4DCoordinateNAD83();
                getLLCoordinateFromCursor( coordinate, cursor);
                coordinate = (Prism4DCoordinateNAD83) getLLCoordinateFromCursor(coordinate, cursor);

            coordinate.setCoordinateType();
            return coordinate;


        } else if (coordinateType == Prism4DCoordinate.sCoordinateDBTypeUTM) {
            Prism4DCoordinateUTM coordinate = new Prism4DCoordinateUTM();
            getENCoordinateFromCursor( coordinate, cursor);
            coordinate = (Prism4DCoordinateUTM)getENCoordinateFromCursor(coordinate, cursor);

            coordinate.setCoordinateType();
            return coordinate;

        } else if (coordinateType == Prism4DCoordinate.sCoordinateDBTypeSPCS) {
            Prism4DCoordinateSPCS coordinate = new Prism4DCoordinateSPCS();
            getENCoordinateFromCursor( coordinate, cursor);
            coordinate = (Prism4DCoordinateSPCS) getENCoordinateFromCursor(coordinate, cursor);

            coordinate.setCoordinateType();
            return coordinate;

        }
        return null;
    }

    private Prism4DCoordinateLL getLLCoordinateFromCursor(Prism4DCoordinateLL coordinate,
                                                           Cursor cursor){
        coordinate.setCoordinateID(cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_ID)));
        coordinate.setProjectID(cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_PROJECT_ID)));
        coordinate.setPointID(cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_POINT_ID)));

        //type is set in calling routine


        int valid = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_VALID_COORD));
        if (valid == 0) {
            coordinate.setValidCoordinate(false);
        }else{
            coordinate.setValidCoordinate(true);
        }


        int isFixed = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_IS_FIXED));
        if (isFixed == 0) {
            coordinate.setIsFixed(false);
        }else{
            coordinate.setIsFixed(true);
        }


        coordinate.setTime(cursor.getLong (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_TIME)));

        coordinate.setLatitude(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE)));
  /*
        coordinate.setLatitudeDegree(cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_DEGREE)));
        coordinate.setLatitudeMinute(cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_MINUTE)));
        coordinate.setLatitudeSecond(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_SECOND)));
*/

        coordinate.setLongitude(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE)));
 /*
        coordinate.setLongitudeDegree(cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_DEGREE)));
        coordinate.setLongitudeMinute(cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_MINUTE)));
        coordinate.setLongitudeSecond(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_SECOND)));
*/

        coordinate.setElevation(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_ELEVATION)));
        coordinate.setGeoid(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_GEOID)));

        //Degrees, minutes, seconds are not stored in the DB
        coordinate.convertDDToDMS();

        return coordinate;
    }



    private Prism4DCoordinateEN getENCoordinateFromCursor(Prism4DCoordinateEN coordinate,
                                                          Cursor cursor){
        coordinate.setCoordinateID(cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_ID)));
        coordinate.setProjectID(cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_PROJECT_ID)));
        coordinate.setPointID(cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_POINT_ID)));

        //type set in caller of this routine


        int valid = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_VALID_COORD));
        if (valid == 0) {
            coordinate.setValidCoordinate(false);
        }else{
            coordinate.setValidCoordinate(true);
        }

        int isFixed = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_IS_FIXED));
        if (isFixed == 0) {
            coordinate.setIsFixed(false);
        }else{
            coordinate.setIsFixed(true);
        }


        coordinate.setEasting(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_EASTING)));
        coordinate.setNorthing(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_NORTHING)));
        coordinate.setElevation(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_ELEVATION)));

        coordinate.setZone(cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_ZONE)));


        String latBand = cursor.getString(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_LATBAND));
        coordinate.setLatBand(latBand.charAt(0));

        String hemisphere = cursor.getString(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_HEMISPHERE));
        coordinate.setHemisphere(hemisphere.charAt(0)  ) ;


        coordinate.setDatum(cursor.getString(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_DATUM)));
        coordinate.setConvergence(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_CONVERGENCE)));
        coordinate.setScale(cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_ELEVATION)));


        return coordinate;
    }




    //returns the ContentValues object needed to add/update the COORDINATE to/in the DB
    public ContentValues getCVFromCoordinateMean(Prism4DCoordinateMean coordinate){

        ContentValues cvCoordinate = new ContentValues();
        //put(columnName, value);
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_ID, coordinate.getCoordinateID());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_PROJECT_ID,coordinate.getProjectID());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_POINT_ID,coordinate.getPointID());

        //Easting northing vs Latitude longitude
        int coorTyp = 0; //false or Latitude/Longitude
        boolean eastingNorthing = coordinate.isEastingNorthing();
        if (eastingNorthing)coorTyp = 1;//true; Easting/Northing
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_TYPE, coorTyp);

        //are the latitude longitude values valid on Earth?
        int valCoord = 0; //false
        boolean validCoordinate = coordinate.isValidCoordinate();
        if (validCoordinate)valCoord = 1;//true;
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_VALID_COORD, valCoord);

        int isFixedInt = 0; //false
        boolean isFixed = coordinate.isFixed();
        if (isFixed)isFixedInt = 1;//true;
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_IS_FIXED, isFixedInt);

        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_RAW,coordinate.getRawReadings());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_MEANED,coordinate.getMeanedReadings());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_FIXED,coordinate.getFixedReadings());

        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_LATITUDE,coordinate.getLatitude());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_LATITUDE_STD,coordinate.getLatitudeStdDev());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_LONGITUDE,coordinate.getLongitude());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_LONGITUDE_STD,coordinate.getLongitudeStdDev());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_ELEVATION,coordinate.getElevation());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_ELEVATION_STD,coordinate.getElevationStdDev());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_GEOID,coordinate.getGeoid());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_MEAN_SATELLITES,coordinate.getSatellites());


        return cvCoordinate;
    }


    public Prism4DCoordinateMean getCoordinateMeanFromCursor(Cursor cursor, int position){

        int last = cursor.getCount();
        if (position >= last) return null;

        cursor.moveToPosition(position);

        Prism4DCoordinateMean coordinate = new Prism4DCoordinateMean();

        coordinate.setCoordinateID(cursor.getInt  (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_ID)));
        coordinate.setProjectID(cursor.getInt  (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_PROJECT_ID)));
        coordinate.setPointID(cursor.getInt  (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_POINT_ID)));

        //type is set in calling routine


        int type = cursor.getInt(cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_TYPE));
        if (type == 0) { //0 = Latitude/Longitude, 1 = Easting/Northing
            coordinate.setType(Prism4DCoordinateMean.LATITUDE_LONGITUDE);
        }else{
            coordinate.setType(Prism4DCoordinateMean.EASTING_NORTHING);
        }


        int valid = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_VALID_COORD));
        if (valid == 0) {
            coordinate.setValidCoordinate(false);
        }else{
            coordinate.setValidCoordinate(true);
        }

        int isFixed = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_IS_FIXED));
        if (isFixed == 0) {
            coordinate.setIsFixed(false);
        }else{
            coordinate.setIsFixed(true);
        }

        coordinate.setRawReadings(cursor.getInt (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_RAW)));

        coordinate.setMeanedReadings(cursor.getInt (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_MEANED)));

        coordinate.setFixedReadings(cursor.getInt (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_FIXED)));

        coordinate.setLatitude(cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_LATITUDE)));

        coordinate.setLongitude(cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_LONGITUDE)));


        //convert DD to DMS by creating a new coordinateWGS
        Prism4DCoordinateWGS84 coordinateWGS84 =
                new Prism4DCoordinateWGS84(coordinate.getLatitude(), coordinate.getLongitude());
        coordinate.setLatitudeDegree(coordinateWGS84.getLatitudeDegree());
        coordinate.setLatitudeMinute(coordinateWGS84.getLatitudeMinute());
        coordinate.setLatitudeSecond(coordinateWGS84.getLatitudeSecond());

        coordinate.setLongitudeDegree(coordinateWGS84.getLongitudeDegree());
        coordinate.setLongitudeMinute(coordinateWGS84.getLongitudeMinute());
        coordinate.setLongitudeSecond(coordinateWGS84.getLongitudeSecond());

        coordinate.setLatitudeStdDev(cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_LATITUDE_STD)));

        coordinate.setLongitudeStdDev(cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_LONGITUDE_STD)));



        coordinate.setElevation(cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_ELEVATION)));
        coordinate.setElevationStdDev(cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_ELEVATION_STD)));
        coordinate.setGeoid(cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_GEOID)));


        coordinate.setSatellites(cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_MEAN_SATELLITES)));



        return coordinate;

    }


    /********************************************/
    /********* General Utility Methods      *****/
    /********************************************/


}
