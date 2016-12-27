package com.asc.msigeosystems.prism4d;

import android.content.ContentValues;
import android.database.Cursor;

import com.asc.msigeosystems.prism4d.database.Prism4DDatabaseManager;
import com.asc.msigeosystems.prism4d.database.Prism4DSqliteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
    //If the project is NOT already in memory list, it is added, else it is updated
    //It is added/updated in the DB regardless
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
            returnCode = update (project, newPoint);
        }

        return returnCode;

    }//end public add()



    //A point that is from the DB needs to be incorporated into memory version
    public boolean addFromDB(Prism4DPoint point){
        if (point == null) return false;

        int projectID = point.getForProjectID();
        if (projectID == 0) return false; //there is no project

        //We have a point from the DB, now add it to it's project
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);

        //check if the point already exists on the project
        //    private int findPointPosition(ArrayList<Prism4DPoint> pointList, int pointID)
        ArrayList<Prism4DPoint> points = project.getPoints();
        if (points == null) {
            //The list should exist, but we can recover if for some reason it doesn't
            points = new ArrayList<>();
            project.setPoints(points);
        }

        int position = findPointPosition(points, point.getPointID());
        if (position == POINT_NOT_FOUND) {
            //So the point was in the DB, but not in memory
            //just add it to the project array
            points.add(point);

            //Now need to check if any coordinates exist for this point in the DB
            int coordinateID = point.getHasACoordinateID();

            //if so, read it in and record the coordinate on the point
            if (coordinateID != 0) {
                Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();
                Prism4DCoordinate coordinate =
                        coordinateManager.getCoordinateFromDB(coordinateID, projectID);
                point.setCoordinate(coordinate);
            }

        } else {
            //It existed in memory AND in the db
/*
            // TODO: 11/3/2016 Check that the assumption that the db version is more up to date is valid
            Prism4DPoint toPoint = points.get(position);
            if (toPoint == null) return false; //This shouldn't happen, we just checked it
            copyPointAttributes(point, toPoint, true );//copy the ID as well
*/
            // TODO: 11/4/2016 What we really need to do is throw an exception!!!
            String message = "Database is corrupt! In memory different from DB. Person = "+
                    project.getProjectName().toString() +
                    " for medication "+(String.valueOf(point.getPointID()));
            throw new RuntimeException(message);
            // TODO: 11/4/2016 But should the exception be a fatal one????
        }
        return true;

    }

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

        //Assert that the project has a points list
        if (pointList == null){
            pointList = new ArrayList<>();
            project.setPoints(pointList);
        }

        //Get the DB Manager to help with DB
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();

        //determine whether the point already exists in the list
        int atPosition = findPointPosition(pointList, newPoint.getPointID());
        if (atPosition == POINT_NOT_FOUND){
            //The point does not already exist. Add it
            pointList.add(newPoint);
            if (addToDB) {
                //  Add the point to the DB
                databaseManager.addPoint(newPoint);

            }
        } else {
            //The point does exist, Update it
            Prism4DPoint listPoint = pointList.get(atPosition);

            //update the list instance with the attributes from the new point being added
            //copy the point attributes, but not the ID
            // TODO: 11/25/2016 maybe need to do a deep copy here
            //The list point came from the DB, and we need to correct it there, so a
            //shallow copy is sufficient
            copyPointAttributes(newPoint, listPoint, false);

            if (addToDB) {
                //Update the point in the DB
                databaseManager.updatePoint(newPoint);
            }
        }

        //if the point has a coordinate, deal with it too
        Prism4DCoordinate coordinate = newPoint.getCoordinate();
        if (coordinate != null){
            Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();
            if (coordinateManager.addToPoint(newPoint, coordinate, addToDB)){
                //What do we do when something is wrong with the coordinate
                // TODO: 11/25/2016 What do we do when something is wrong with the coordinate on a point???
                return false;
            }
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


    public int getPointsFromDB(Prism4DProject project){
        int projectID = project.getProjectID();

        //get all medications in the DB that are linked to this Project
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.getAllPointsFromDB(project);
    }



    //***********************  UPDATE **************************************

    //This routine not only updates the point on the project, but also in the DB
    public boolean update(Prism4DProject project, Prism4DPoint point){
        //The update functionality already exists in add
        //    as a Point can only appear once
        //The third parameter indicates whether to affect DB
        boolean addToDBToo = true;
        return addToProject(project, point, addToDBToo);
    }//end public update()



    //This routine not only replaces in the in memory list, but also in the DB
    public boolean update(int projectID, Prism4DPoint point){
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);

        //The update functionality already exists in add
        //    as a Point can only appear once
        //The third parameter indicates whether to affect DB
        boolean addToDBToo = true;
        return addToProject(project, point, addToDBToo);
    }//end public update()

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



    public void removeProjectPointsFromDB(int projectID) {
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        databaseManager.removeProjectCoordinates(projectID);
        databaseManager.removeProjectPoints(projectID);
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
    // TODO: 12/13/2016 add deepCopy of point if it becomes needed

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
        toPoint.setPointFeatureCode(fromPoint.getPointFeatureCode());
        toPoint.setPointNotes      (fromPoint.getPointNotes());
        toPoint.setCoordinate      (fromPoint.getCoordinate());
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



    public ContentValues getPointCV(Prism4DPoint point){
        ContentValues values = new ContentValues();
        values.put(Prism4DSqliteOpenHelper.POINT_ID,               point.getPointID());
        values.put(Prism4DSqliteOpenHelper.POINT_FOR_PROJECT_ID,   point.getForProjectID());
        values.put(Prism4DSqliteOpenHelper.POINT_ISA_COORDINATE_ID,point.getHasACoordinateID());
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
