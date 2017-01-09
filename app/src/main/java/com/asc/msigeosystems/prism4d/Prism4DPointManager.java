package com.asc.msigeosystems.prism4d;

import android.content.ContentValues;
import android.database.Cursor;

import com.asc.msigeosystems.prism4d.database.Prism4DDatabaseManager;
import com.asc.msigeosystems.prism4d.database.Prism4DSqliteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Elisabeth Huhn on 5/18/2016.
 *
 *
 * The class in charge of maintaining the set of instances of Point
 *  both in-memory and in the DB
 *
 */
public class Prism4DPointManager {
    /************************************/
    /********* Static Constants  ********/
    /************************************/

    public static final int POINT_NOT_FOUND = -1;


    /************************************/
    /********* Static Variables  ********/
    /************************************/
   private static Prism4DPointManager ourInstance ;


    /**************************************/
    /********* Member Variables   *********/
    /**************************************/
    //The point lists exist on the Projects, rather than on a list here
    //private ArrayList<Prism4DPoint> mPointList;


    /************************************/
    /********* Static Methods   *********/
    /************************************/
    public static Prism4DPointManager getInstance() {
        if (ourInstance == null){
            ourInstance = new Prism4DPointManager();
        }
        return ourInstance;
    }





    /************************************/
    /********* Constructors     *********/
    /************************************/
    private Prism4DPointManager() {

        //The point list already exists on the Project Instance
        //mPointList = new ArrayList<>();

        //Points are contained in lists on the Project instances
        //ie Points are project specific.
        // So this is not the place to gin up dummy data
    }


    /*******************************************/
    /********* Public Member Methods   *********/
    /*******************************************/



    /*******************************************/
    /*********     CRUD Methods        *********/
    /*******************************************/

    //***********************  SIZE **************************************

    //returns the number of points of the indicated project
    public int getSize(int projectID){

        ArrayList<Prism4DPoint> pointList = getProjectPointsList(projectID);

        return pointList.size();
    }

    //***********************  CREATE **************************************

    //This routine not only adds to the in memory list, but also to the DB
    //If the project is NOT already in memory list, it is added, and updated in the DB
    //else false is returned
    public boolean add(Prism4DProject project, Prism4DPoint newPoint){
        boolean returnCode = false;

        if ((project == null) || (newPoint == null)) return returnCode;

        ArrayList<Prism4DPoint> pointsList = project.getPoints();

        if (pointsList == null){
            pointsList = new ArrayList<>();
            project.setPoints(pointsList);
        }

        //determine whether the project already exists in the list
        int position = findPointPosition(pointsList, newPoint.getPointID());
        if (position == POINT_NOT_FOUND) {
            boolean addToDBToo = true;
            returnCode = addToProject (project, newPoint, addToDBToo);
        } else {
            returnCode = false;
        }

        return returnCode;

    }//end public add()



    //This routine not only adds to the in memory list,
    // but has an argument, that if true,  also adds to the DB
    //returns FALSE if for any reason the point can not be added
    //ALSO deals with the coordinate on the point
    //Use this if you already have the Project object in hand
    public boolean addToProject(Prism4DProject project, Prism4DPoint newPoint, boolean addToDB){
        //  Can not add a point to a project that does not exist
        if ((project == null) || (newPoint == null)) return false;

        //Find the project the point is for
        int medProjectID = newPoint.getForProjectID();
        int projectID    = project.getProjectID();
        //The point and the project must point at each other
        if (medProjectID != projectID) return false;

        //determine if the point already is associated with this project
        ArrayList<Prism4DPoint> pointList = project.getPoints();

        //Assert that the project has a points list, which is done when project is created
        if (pointList == null){
            pointList = new ArrayList<>();
            project.setPoints(pointList);
        }

        //Get the DB Manager to help with DB operations
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();

        //determine whether the point already exists in the list
        int atPosition = findPointPosition(pointList, newPoint.getPointID());
        if (atPosition == POINT_NOT_FOUND){
            //The point does not already exist. Add it
            pointList.add(newPoint);
            if (addToDB) {
                //  Add the point and it's coordinate to the DB
                return databaseManager.addPoint(newPoint);
            }
        } else {
            return false;
        }

        return true;
    }



    //***********************  READ **************************************

    //returns a list of points for just this project
    public ArrayList<Prism4DPoint> getProjectPointsList(int projectID){
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject        project        = projectManager.getProject(projectID);

        ArrayList<Prism4DPoint> projectPointsList;
        if (project != null){
            projectPointsList = project.getPoints();
        } else {
            throw new RuntimeException("Project Should Exist by now");

        }
        return projectPointsList;
    }


    public Prism4DPoint getPoint(int projectID, int pointID) {
        ArrayList<Prism4DPoint> pointsList = getProjectPointsList(projectID);

        for (Prism4DPoint point : pointsList){
            if (point.getForProjectID() == projectID){
                if (point.getPointID() == pointID) {
                    return point;
                }
            }
        }
        return null;
    }


    public int getPointsForProjectFromDB(Prism4DProject project){
        int projectID = project.getProjectID();

        //get all medications in the DB that are linked to this Project
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.getPointsForProjectFromDB(project);
    }



    //***********************  UPDATE **************************************


    //This routine only replaces in the the DB version of the point
    //cascading update Point->coordinate, Point->Pictures
    public int updatePoint(Prism4DPoint point){

        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.updatePoint(point);

    }//end public update()


    //Updates the Project Object in the DB and the Picture Object in the DB
    //Assumes all subordinate objects are correct, and don't need updating
    public boolean updateSinglePictureInDB(Prism4DPicture picture){
        int pointID = picture.getPointID();
        if (pointID < 1){
            return false;
        }
        int projectID = picture.getProjectID();
        if (projectID < 1){
            return false;
        }

        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);
        if (project == null)return false;

        Prism4DPoint   point   = project.getPoint(pointID);
        if (point == null)return false;
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();


        //not necessary to update the point,
        // as the picture relationship isn't stored on the project in the DB
        //if (databaseManager.updatePointOnly(point) != 1)return false;

        //the relationship is only stored on the picture side in the DB
        if (databaseManager.updatePicture(picture) != 1) return false;
        return true;
    }


    //Updates the Project Object in the DB and the Picture Object in the DB
    //Assumes all subordinate objects are correct, and don't need updating
    public boolean updateSinglePointInDB(Prism4DPoint point){
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        int pointID = point.getPointID();
        if (pointID < 1){
            return false;
        }
        int projectID = point.getForProjectID();
        if (projectID < 1){
            return false;
        }

        Prism4DProject project = projectManager.getProject(projectID);
        if (project == null)return false;

        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();

        if (databaseManager.updatePointOnly(point) != 1)return false;

        return true;
    }


    //***********************  DELETE **************************************

    //Because the list is on one project instance, we must also have the project ID
    //of the instance being manipulated
    //This routine not only removes from the in-memory list, but also from the DB
    public boolean removePoint(int projectID, Prism4DPoint point) {
        //Now find that project using the ProjectManager
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject        project        = projectManager.getProject(projectID);

        //If project not found, return false.
        //  Can not add a point to a project who does not exist
        if (project == null) return false;

        //determine if the point already is associated with this project
        //get the list of points contained in this project
        ArrayList<Prism4DPoint> pointList = project.getPoints();

        //if not, create one
        if (pointList == null){
            pointList = new ArrayList<>();
            project.setPoints(pointList);
        }

        //Make sure it's in the memory list before removing it from the DB
        boolean returnCode = pointList.remove(point);

        if (returnCode){
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            //first get rid of any coordinate on the point
            int coordinateID = point.getHasACoordinateID();
            databaseManager.removeCoordinate(coordinateID, projectID);
            // then get rid of the point itself
            databaseManager.removePoint(point.getPointID(), projectID);
        }

        //have to remove the point from the DB as well as the list
        return returnCode;
    }//end public remove position



    public void removeProjectPointsFromDB(Prism4DProject project) {
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        databaseManager.removeProjectCoordinates(project.getProjectID());
        databaseManager.removeProjectPoints(project.getProjectID());
    }//end public remove position





    /********************************************/
    /*********  Member Methods   *********/
    /********************************************/

    //Find the position of the point instance
    //     that matches the argument pointID
    //     within the argument list pointList
    //returns constant = POINT_NOT_FOUND if the point is not in the list
    //NOTE it is a RunTimeException to call this routine if the list is null or empty
    private int findPointPosition(ArrayList<Prism4DPoint> pointList, int pointID){
        Prism4DPoint point;
        int          position = 0;
        int          last     = pointList.size();

        //Determine whether an instance of the point is already in the list
        //NOTE that if list is empty, while doesn't loop even once
        while (position < last){
            point = pointList.get(position);

            if (point.getPointID() == pointID){
                //Found the point in the list at this position
                return position;
            }
            position++;
        }
        return POINT_NOT_FOUND;
    }



    //***********************  COPY **************************************

    //This routine does a deep copy by reading in the point from the DB
    //so it only works if the point already exists in the DB
    // TODO: 12/13/2016 replace with deepCopy, shallow copy just isn't safe enough

    //This routine does a shallow copy of attributes,
    //    i.e. if an attribute changes later, it will change on both instances
    //Does not affect Project ID field, but will copy PointID if last parameter is true
    //A better choice would be the deep copy method
    public void copyPointAttributes(Prism4DPoint fromPoint, Prism4DPoint toPoint, boolean copyID){

        if (copyID){
            toPoint.setPointID(fromPoint.getPointID());
        }

        toPoint.setHasACoordinateID(fromPoint.getHasACoordinateID());
        toPoint.setCoordinate      (fromPoint.getCoordinate());
        toPoint.setOffsetDistance  (fromPoint.getOffsetDistance());
        toPoint.setOffsetHeading   (fromPoint.getOffsetDistance());
        toPoint.setOffsetElevation (fromPoint.getOffsetDistance());
        toPoint.setPointFeatureCode(fromPoint.getPointFeatureCode());
        toPoint.setPointNotes      (fromPoint.getPointNotes());
        toPoint.setPictures        (fromPoint.getPictures());
        toPoint.setHdop            (fromPoint.getHdop());
        toPoint.setVdop            (fromPoint.getVdop());
        toPoint.setTdop            (fromPoint.getTdop());
        toPoint.setPdop            (fromPoint.getPdop());
        toPoint.setHrms            (fromPoint.getHrms());
        toPoint.setVrms            (fromPoint.getVrms());

    }


    /********************************************/
    /****  Translation utility Methods   ********/
    /********************************************/

    public ContentValues getCVFromPoint(Prism4DPoint point){
        ContentValues values = new ContentValues();
        values.put(Prism4DSqliteOpenHelper.POINT_ID,               point.getPointID());
        values.put(Prism4DSqliteOpenHelper.POINT_FOR_PROJECT_ID,   point.getForProjectID());
        values.put(Prism4DSqliteOpenHelper.POINT_ISA_COORDINATE_ID,point.getHasACoordinateID());
        values.put(Prism4DSqliteOpenHelper.POINT_OFFSET_DISTANCE,  point.getOffsetDistance());
        values.put(Prism4DSqliteOpenHelper.POINT_OFFSET_HEADING,   point.getOffsetHeading());
        values.put(Prism4DSqliteOpenHelper.POINT_OFFSET_ELEVATION, point.getOffsetElevation());
        values.put(Prism4DSqliteOpenHelper.POINT_FEATURE_CODE,     point.getPointFeatureCode().toString());
        values.put(Prism4DSqliteOpenHelper.POINT_NOTES,            point.getPointNotes().toString());
        values.put(Prism4DSqliteOpenHelper.POINT_HDOP ,            point.getHdop());
        values.put(Prism4DSqliteOpenHelper.POINT_VDOP ,            point.getVdop());
        values.put(Prism4DSqliteOpenHelper.POINT_TDOP ,            point.getTdop());
        values.put(Prism4DSqliteOpenHelper.POINT_PDOP ,            point.getPdop());
        values.put(Prism4DSqliteOpenHelper.POINT_HRMS ,            point.getHrms());
        values.put(Prism4DSqliteOpenHelper.POINT_VRMS ,            point.getVrms());
        //Doesn't insert coordinate object here, just the ID above

        return values;
    }

    //returns the Prism4DPoint characterized by the position within the Cursor
    //returns null if the position is larger than the size of the Cursor
    //NOTE    this routine does NOT add the point to the list maintained by this PointManager
    //        The caller of this routine is responsible for that.
    //        This is only a translation utility
    //WARNING As the app is not multi-threaded, this routine is not synchronized.
    //        If the app becomes multi-threaded, this routine must be made thread safe
    //WARNING The cursor is NOT closed by this routine. It assumes the caller will close the
    //         cursor when it is done with it
    public Prism4DPoint getPointFromCursor(Cursor cursor, int position){

        int last = cursor.getCount();
        if (position >= last) return null;

        Prism4DPoint point = new Prism4DPoint(); //filled with defaults

        cursor.moveToPosition(position);
        point.setPointID         (cursor.getInt   (
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_ID)));
        point.setForProjectID    (cursor.getInt   (
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_FOR_PROJECT_ID)));
        point.setHasACoordinateID(cursor.getInt   (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_ISA_COORDINATE_ID)));

        point.setOffsetDistance  (cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_OFFSET_DISTANCE)));
        point.setOffsetHeading   (cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_OFFSET_HEADING)));
        point.setOffsetElevation (cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_OFFSET_ELEVATION)));

        point.setPointFeatureCode(cursor.getString(
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_FEATURE_CODE)));
        point.setPointNotes      (cursor.getString(
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_NOTES)));

        point.setHdop            (cursor.getDouble(
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_HDOP)));
        point.setVdop            (cursor.getDouble(
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_VDOP)));
        point.setTdop            (cursor.getDouble(
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_TDOP)));
        point.setPdop            (cursor.getDouble(
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_PDOP)));
        point.setHrms            (cursor.getDouble(
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_HRMS)));
        point.setVrms            (cursor.getDouble(
                                  cursor.getColumnIndex(Prism4DSqliteOpenHelper.POINT_VRMS)));


        return point;
    }

}
