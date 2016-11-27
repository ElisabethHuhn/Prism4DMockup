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


    //This routine not only adds to the in memory list, but also to the DB
    //If the coordinate is NOT already in memory on the point, it is added, else it is updated
    //It is added/updated in the DB regardless
    public boolean add(Prism4DProject project, Prism4DPoint point, Prism4DCoordinate coordinate){

        boolean returnCode = false;
        if ((project == null) || (point == null) || (coordinate == null)) return returnCode;

        Prism4DCoordinate pointCoordinate = point.getCoordinate();
        //bail if the coordinate is not fully created
        if (pointCoordinate.getCoordinateID() == 0) return returnCode;
        
        if (( pointCoordinate.getCoordinateID() == point.getHasACoordinateID())){
                //The coordinate already exists, update it rather than add it
            // TODO: 11/26/2016 Need to write update routine
            returnCode = addToPoint (point, coordinate, true);
        } else {
            returnCode = addToPoint(point, coordinate, true);

        }
        return returnCode;

    }//end public add()


    //The routine that actually adds the instance to in memory list and
    // potentially (third boolean parameter) to the DB
    public boolean addToPoint(Prism4DPoint point, Prism4DCoordinate coordinate, boolean addToDBToo){
        //pointID on point must match the point on the coordinate
        if (point.getPointID() != coordinate.getPointID()) return false;
        point.setCoordinate(coordinate);

        if (addToDBToo){
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            if (!databaseManager.addCoordinate(coordinate)){
                return false;
            }
        }
        return true;

    }//end of addToPoint



    //******************  READ *******************************************



    //Returns null if it's not in the DB
    public Prism4DCoordinate getCoordinateFromDB (int coordinateID, int projectID){
        if (coordinateID == 0) return null;
        //Ignore the in memory list, just go straight to the DB
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.getCoordinateFromDB(coordinateID, projectID);
    }


    //******************  UPDATE *******************************************







    //******************  DELETE *******************************************




    /********************************************/
    /********* Translation Utility Methods  *****/
    /********************************************/

    //returns the ContentValues object needed to add/update the COORDINATE to/in the DB
    public ContentValues getCoordinateCV(Prism4DCoordinate coordinate){
        //convert the Prism4DCoordinate object into a ContentValues object containing a coordinate
        ContentValues cvCoordinate = new ContentValues();
        //put(columnName, value);
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_ID,       coordinate.getCoordinateID());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_PROJECT_ID,  coordinate.getProjectID());
        cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_POINT_ID,     coordinate.getPointID());
        CharSequence coordinateType = coordinate.getCoordinateType();
        cvCoordinate.put(Prism4DSqliteOpenHelper.PROJECT_COORDINATE_TYPE, coordinateType.toString());

        //The rest of the attributes depend upon the specific subtype

        if ((coordinateType.equals(Prism4DCoordinate.sCoordinateTypeWGS84))||
            (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83))    ){
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_TIME,
                                            ((Prism4DCoordinateLL)coordinate).getTime());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitude());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_DEGREE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeDegree());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_MINUTE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeMinute());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_SECOND,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeSecond());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitude());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_DEGREE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeDegree());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_MINUTE,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeMinute());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_SECOND,
                                            ((Prism4DCoordinateLL)coordinate).getLatitudeSecond());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_ELEVATION,
                                            ((Prism4DCoordinateLL)coordinate).getElevation());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_GEOID,
                                            ((Prism4DCoordinateLL)coordinate).getGeoid());
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_LL_VALID_COORDINATE,
                                            ((Prism4DCoordinateLL)coordinate).isValidCoordinate());

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
            cvCoordinate.put(Prism4DSqliteOpenHelper.COORDINATE_EN_VALID_COORDINATE,
                                            ((Prism4DCoordinateEN)coordinate).isValidCoordinate());
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

        CharSequence coordinateType = cursor.getString(
                                    cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_TYPE));
        if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeWGS84)) {
            Prism4DCoordinateWGS84 coordinate = new Prism4DCoordinateWGS84();
            coordinate = (Prism4DCoordinateWGS84) getLLCoordinateFromCursor(coordinate, cursor);
            coordinate.setCoordinateType();
            return coordinate;

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeNAD83)) {
                Prism4DCoordinateNAD83 coordinate = new Prism4DCoordinateNAD83();
                getLLCoordinateFromCursor( coordinate, cursor);
                coordinate = (Prism4DCoordinateNAD83) getLLCoordinateFromCursor(coordinate, cursor);
            coordinate.setCoordinateType();
            return coordinate;


        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeUTM)) {
            Prism4DCoordinateUTM coordinate = new Prism4DCoordinateUTM();
            getENCoordinateFromCursor( coordinate, cursor);
            coordinate = (Prism4DCoordinateUTM)getENCoordinateFromCursor(coordinate, cursor);
            coordinate.setCoordinateType();
            return coordinate;

        } else if (coordinateType.equals(Prism4DCoordinate.sCoordinateTypeSPCS)) {
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
        coordinate.setCoordinateID(
                cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_ID)));
        coordinate.setProjectID(
                cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_PROJECT_ID)));
        coordinate.setPointID(
                cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_POINT_ID)));

        coordinate.setLatitude(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE)));
        coordinate.setLatitudeDegree(
                cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_DEGREE)));
        coordinate.setLatitudeMinute(
                cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_MINUTE)));
        coordinate.setLatitudeSecond(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LATITUDE_SECOND)));


        coordinate.setLongitude(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE)));
        coordinate.setLongitudeDegree(
                cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_DEGREE)));
        coordinate.setLongitudeMinute(
                cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_MINUTE)));
        coordinate.setLongitudeSecond(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_LONGITUDE_SECOND)));


        coordinate.setElevation(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_ELEVATION)));
        coordinate.setGeoid(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_GEOID)));

        int valid = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_VALID_COORDINATE));
        if (valid == 0) {
            coordinate.setValidCoordinate(false);
        }else{
            coordinate.setValidCoordinate(true);
        }

        return coordinate;
    }



    private Prism4DCoordinateEN getENCoordinateFromCursor(Prism4DCoordinateEN coordinate,
                                                          Cursor cursor){
        coordinate.setCoordinateID(
                cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_ID)));
        coordinate.setProjectID(
                cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_PROJECT_ID)));
        coordinate.setPointID(
                cursor.getInt  (
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_POINT_ID)));

        coordinate.setEasting(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_EASTING)));
        coordinate.setNorthing(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_NORTHING)));
        coordinate.setElevation(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_ELEVATION)));

        coordinate.setZone(
                cursor.getInt(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_ZONE)));


        String latBand = cursor.getString(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_LATBAND));
        coordinate.setLatBand(latBand.charAt(0));

        String hemisphere = cursor.getString(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_HEMISPHERE));
        coordinate.setHemisphere(hemisphere.charAt(0)  ) ;


        coordinate.setDatum(
                cursor.getString(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_DATUM)));
        coordinate.setConvergence(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_CONVERGENCE)));
        coordinate.setScale(
                cursor.getDouble(
                        cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_LL_ELEVATION)));


        int valid = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.COORDINATE_EN_VALID_COORDINATE));
        if (valid == 0) {
            coordinate.setValidCoordinate(false);
        }else{
            coordinate.setValidCoordinate(true);
        }

        return coordinate;
    }


    /********************************************/
    /********* General Utility Methods      *****/
    /********************************************/


}
