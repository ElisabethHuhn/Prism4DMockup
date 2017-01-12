package com.asc.msigeosystems.prism4d.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.asc.msigeosystems.prism4d.Prism4DCoordinate;
import com.asc.msigeosystems.prism4d.Prism4DCoordinateManager;
import com.asc.msigeosystems.prism4d.Prism4DCoordinateMean;
import com.asc.msigeosystems.prism4d.Prism4DGlobalData;
import com.asc.msigeosystems.prism4d.Prism4DGlobalDataManager;
import com.asc.msigeosystems.prism4d.Prism4DPicture;
import com.asc.msigeosystems.prism4d.Prism4DPoint;
import com.asc.msigeosystems.prism4d.Prism4DPointManager;
import com.asc.msigeosystems.prism4d.Prism4DProject;
import com.asc.msigeosystems.prism4d.Prism4DProjectManager;
import com.asc.msigeosystems.prism4d.Prism4DProjectSettings;

import java.util.ArrayList;

/**
 * Created by Elisabeth Huhn on 5/18/2016.
 * This manager hides the CRUD routines of the DB.
 * Originally this is a pass through layer, but if background threads are required to get
 * IO off the UI thread, this manager will maintain them.
 * This manager is a singleton that holds the one connection to the DB for the app.
 * This connection is opened when the app is first initialized, and never closed
 */
public class Prism4DDatabaseManager {
    private static final String TAG = "Prism4DDatabaseManager";
    private static final long   sERROR_CODE = -1;


    /************************************************/
    /*         static variables                     */
    /************************************************/

    private static Prism4DDatabaseManager   sManagerInstance ;

    private static String sNoContextException = "Can not create database without a context";
    private static String sNotInitializedException =
            "Attempt to access the database before it has been initialized";
    private static String sNotOpenedException =
            "Attempt to access the database before it has been opened";



    /************************************************/
    /*         Instance variables                   */
    /************************************************/

    private Prism4DSqliteOpenHelper mDatabaseHelper;
    private  SQLiteDatabase         mDatabase;



    /************************************************/
    /*         setters & getters                    */
    /************************************************/

    //mDatabaseHelper
    public void setDatabaseHelper(Prism4DSqliteOpenHelper mDatabaseHelper) {
        this.mDatabaseHelper = mDatabaseHelper;
    }
    //Return null if the field has not yet been initialized
    public synchronized Prism4DSqliteOpenHelper getDatabaseHelper()  {
        return mDatabaseHelper;
    }


    //mDatabase
    public    void   setDatabase(SQLiteDatabase mDatabase) {this.mDatabase = mDatabase; }
    //return null if the field has not yet been initialized
    public synchronized SQLiteDatabase getDatabase()       { return mDatabase; }


    /************************************************/
    /*         constructor                          */
    /************************************************/
    //null constructor. It should never be called.
    //    initializeInstance() is the proper protocol
    private Prism4DDatabaseManager() {}



    /************************************************/
    /*         static methods                       */
    /************************************************/


    /**********************
     * This method initializes the singleton Database Manager
     *
     * The database Manager holds onto a single instance of the helper connection
     *    to the database.
     *
     * The purpose of a singleton connection is to keep the app threadsafe
     *    in the case of attempted concurrent access to the database.
     *
     * There can be no concurrent access to the database from multiple threads,
     *    as there is only one connection, it can be accessed serially,
     *    from only one thread at a time.
     *
     * Thie lifetime of this singleton is the execution lifetime of the App,
     *    thus the application context is passed, not the activity context
     *
     * synchronized method to ensure only 1 instance exists
     *
     * @param context               The application context
     * @throws RuntimeException     Thrown if there is no context passed
     *
     * USAGE
     * Prism4DDatabaseManager.initializeInstance(getApplicationContext());
     */
    public static synchronized Prism4DDatabaseManager initializeInstance(Context context)
                                                            throws RuntimeException {
        if (sManagerInstance == null){
                try {
                //Note the hard coded strings here.
                // todo: figure out how to access the string resources from the DatabaseManager
                if (context == null) throw new RuntimeException(sNoContextException);
                //create the singleton Database Manager
                sManagerInstance = new Prism4DDatabaseManager();

                //create and store it's singleton connection to the database
                //The helper is the database connection
                //It's a singleton to keep the app thread safe
                sManagerInstance.setDatabaseHelper(new Prism4DSqliteOpenHelper(context));

                //Now that we have the connection, create the database as well
                sManagerInstance.setDatabase (sManagerInstance.getDatabaseHelper().getWritableDatabase());
            }catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

        } else if (sManagerInstance.getDatabaseHelper() == null){
            //if here, we had a Database Manager, but no connection to a database
            //Something is definitely fishy, but maybe we can recover
            if (context == null) throw new RuntimeException(sNoContextException);

            //all the constructor does is save the context
            sManagerInstance.setDatabaseHelper( new Prism4DSqliteOpenHelper(context));
            //opening the database will create the tables if they do not
            //  already exist. It will also force an upgrade if the system
            //  detects a new version of the DB since the last time it was opened
            sManagerInstance.setDatabase(sManagerInstance.getDatabaseHelper().getWritableDatabase());

        } else if (sManagerInstance.getDatabase() == null){
            //again, if we had a database manager, and a database helper/connection
            // we certainly should have had an instance of the database
            //something is fishy, but attempt recovery
            sManagerInstance.setDatabase(sManagerInstance.getDatabaseHelper().getWritableDatabase());
        }

        return sManagerInstance;
    }


    //returns null if the Database Manager has not yet bee initialized
    //in that case, initializeInstance() must be called first
    // We can't just fix the problem, as we need the application context
    //because this is an error condition. Treat it as an error
    public static synchronized Prism4DDatabaseManager getInstance() throws RuntimeException {
        //The reason we can't just initialize it now is because we need a context to initialize
        if (sManagerInstance == null)  {
            throw new RuntimeException(sNotInitializedException);
        }
        return sManagerInstance;
    }

    //If we do happen to have a context, we can initialize
    public static synchronized Prism4DDatabaseManager getInstance(Context context)
                                                            throws RuntimeException {
        //The reason we can't just initialize it now is because we need a context to initialize
        if (sManagerInstance == null)  {
            if (context == null)throw new RuntimeException(sNotInitializedException);
            Prism4DDatabaseManager.initializeInstance(context);
        }
        return sManagerInstance;
    }




    /************************************************/
    /*         Instance methods                     */
    /************************************************/
    //The CRUD routines:


    /************************************************/
    /*         Project CRUD methods                 */
    /************************************************/

    //******************************    COUNT    ***********************
    //Get count of projects
    //// TODO: 11/1/2016 write this routine if it is needed
    //public int getProjectCount() {}

    //*********************************** CREATE ********************************
    public boolean addProject(Prism4DProject project){
        boolean returnCode = true;
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        long addCode = mDatabaseHelper.add(mDatabase,                //database to add to
                            Prism4DSqliteOpenHelper.TABLE_PROJECT,   //table to add to
                            null,                                    //nullColumnHack
                            projectManager.getCVFromProject(project));  //Content Values of the object to add
        if (addCode == sERROR_CODE)returnCode = false;

        returnCode = returnCode & addProjectSettingsToDB(project);

        returnCode = returnCode & addProjectPicturesToDB(project);

        return returnCode;
    }

    public boolean addProjectSettingsToDB(Prism4DProject project){

        Prism4DProjectSettings projectSettings = project.getSettings();
        if (projectSettings == null){
            //Settings had not yet been created. Do so now, with default values
            projectSettings = new Prism4DProjectSettings();//defaults
            projectSettings.setProjectID(project.getProjectID());
            project.setSettings(projectSettings);
        }
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        //store the settings in the DB with the project
        long addCode = mDatabaseHelper.add(mDatabase,
                                            Prism4DSqliteOpenHelper.TABLE_PROJECT_SETTINGS,
                                            null,
                                            projectManager.getCVFromProjectSettings(project));
        if (addCode == sERROR_CODE)return false;
        return true;
    }

    //*********************************** READ ********************************
    /********************************
     Cursor query (  String table,           //Table Name
                     String[] columns,       //Columns to return, null for all columns
                     String where_clause,
                     String[] selectionArgs, //replaces ? in the where_clause with these arguments
                     String groupBy,         //null meanas no grouping
                     String having,          //row grouping
                     String orderBy)         //null means the default sort order
     *********************************/

    //returns the number of Projects read in from the DB
    //side effect is to load all projects into memory
    //  along with all the associated project settings
    public int getAllProjects(){
        Prism4DProjectSettings projectSettings;

        Cursor cursor = mDatabaseHelper.getObject(  mDatabase,
                                                    Prism4DSqliteOpenHelper.TABLE_PROJECT,
                                                    null,    //get the whole project
                                                    null,    //get all projects.
                                                    null, null, null, null);

        //create a project object from the Cursor object
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

        int position = 0;
        int last = cursor.getCount();
        Prism4DProject project;
        while (position < last) {
            project = projectManager.getProjectFromCursor(cursor, position);
            if (project != null) {
                // TODO: 12/21/2016 Need to let someone know that something is wrong here if null
                //Add the DB project to memory
                projectManager.addFromDB(project);

                //add DB settings object to the new (in memory) project object
                project.setSettings(getProjectSettings(project.getProjectID()));

                //get pictures for this project and add to the project
                project.setPictures(getProjectPicturesFromDB(project));
            }
       
            position++;
        }
        cursor.close();
        return last;

    }

    //NOTE this routine does NOT add the project to the RAM list maintained by ProjectManager
    public Prism4DProject getProject(int projectID){
        //get the project row from the DB
        Cursor cursor = mDatabaseHelper.getObject(mDatabase,      //the db to access
                                                  Prism4DSqliteOpenHelper.TABLE_PROJECT,//table name
                                                  null,           //get the whole project
                                                  getProjectWhereClause(projectID),   //where clause
                                                  null, null, null, null); //args,group,row grouping,order

        //create a project object from the Cursor object
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        //get the first row in the cursor
        int position = 0;//so we don't have a magic number in the last parameter
        Prism4DProject project = projectManager.getProjectFromCursor(cursor, position);
        cursor.close();

        //read settings object from the DB and put in the new project object
        project.setSettings(getProjectSettings(projectID));

        //get pictures for this project and add to the project
        project.setPictures(getProjectPicturesFromDB(project));

        return project;
    }


    //get project settings from the DB, analogous to getProject(projectID)
    public Prism4DProjectSettings getProjectSettings(int projectID){

        Cursor cursor = mDatabaseHelper.getObject(mDatabase,      //the db to access
                                                  Prism4DSqliteOpenHelper.TABLE_PROJECT_SETTINGS,//table
                                                  null,           //get the whole project
                                                  getProjectSettingsWhereClause(projectID), //where clause
                                                  null, null, null, null); //args,group,row grouping,order

        //create a project object from the Cursor object
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        int position = 0;

        Prism4DProjectSettings projectSettings =
                                    projectManager.getProjectSettingsFromCursor(cursor, position);
        cursor.close();
        return projectSettings;

    }

    //*********************************** UPDATE ********************************

    public  int updateProjectOnly(Prism4DProject project) {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        int                   projectID      = project.getProjectID();

        int returnCodeP = mDatabaseHelper.update(mDatabase,
                                                 Prism4DSqliteOpenHelper.TABLE_PROJECT,
                                                 projectManager.getCVFromProject(project),
                                                 getProjectWhereClause(projectID),
                                                 null);  //values that replace ? in where clause
        //No cascading updates as in updateProject()

        return (returnCodeP);
    }

    public  int updateProject(int  projectID) {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject        project        = projectManager.getProject(projectID);

        return updateProject(project);

    }

    public  int updateProject(Prism4DProject project) {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        int                   projectID      = project.getProjectID();

        int returnCodeP = mDatabaseHelper.update(mDatabase,
                                                Prism4DSqliteOpenHelper.TABLE_PROJECT,
                                                projectManager.getCVFromProject(project),
                                                getProjectWhereClause(projectID),
                                                null);  //values that replace ? in where clause
        int returnCodePS = updateProjectSettings(project);

        int returnCodePic = updateProjectPictures(project);

        //updates the points and the pictures on the points
        int returnCodePts = updateProjectPoints(project);

        return (returnCodeP + returnCodePS + returnCodePic + returnCodePts);
    }


    public  int updateProjectSettings(Prism4DProject project) {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        int                   projectID      = project.getProjectID();

        return mDatabaseHelper.update(  mDatabase,
                                        Prism4DSqliteOpenHelper.TABLE_PROJECT_SETTINGS,
                                        projectManager.getCVFromProjectSettings(project),
                                        getProjectSettingsWhereClause(projectID),
                                        null);  //values that replace ? in where clause

    }

    public  int updateProjectPictures(Prism4DProject project) {
        ArrayList<Prism4DPicture> pictures = project.getPictures();
        int returnCode = updatePictures(pictures);

        return returnCode;
    }


    public  int updateProjectPoints(Prism4DProject project) {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        int                   projectID      = project.getProjectID();

        Prism4DPoint point;
        ArrayList<Prism4DPoint> points = project.getPoints();
        int last = points.size();
        int position = 0;
        int returnCode = 0;

        while (position < last){
            point = points.get(position);

            returnCode = returnCode + updatePoint(point);
        }
        return returnCode;
    }



    //*********************************** DELETE ********************************

    //The return code indicates how many rows affected
    public int removeProject(int projectID){

        removeProjectSettings(projectID);

        return mDatabaseHelper.remove(  mDatabase,
                                        Prism4DSqliteOpenHelper.TABLE_PROJECT,
                                        getProjectWhereClause(projectID),
                                        null);  //values that replace ? in where clause
    }

    //The return code indicates how many rows affected
    public void removeProjectSettings(int projectID){

        mDatabaseHelper.remove( mDatabase,
                                Prism4DSqliteOpenHelper.TABLE_PROJECT_SETTINGS,
                                getProjectSettingsWhereClause(projectID),
                                null);  //values that replace ? in where clause
    }


    /************************************************/
    /*        Project specific CRUD  utility         */
    /************************************************/
    private String getProjectWhereClause(int projectID){
        return Prism4DSqliteOpenHelper.PROJECT_ID + " = " + String.valueOf(projectID);
    }


    private String getProjectSettingsWhereClause(int projectID){
        return Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ID + " = " + String.valueOf(projectID);
    }


    /************************************************/
    /*        Point CRUD methods               */
    /************************************************/


    //Get count of points
    //// TODO: 11/1/2016 write this routine if it is needed
    //public int getPointCount() {}

    //******************************    Create    ***********************
    public boolean addPoint(Prism4DPoint point){
        boolean returnCode = true;
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        long addCode = mDatabaseHelper.add(mDatabase,
                                                Prism4DSqliteOpenHelper.TABLE_POINT,
                                                null,
                                                pointManager.getCVFromPoint(point));

        if (addCode == sERROR_CODE)returnCode = false;

        Prism4DCoordinate coordinate = (Prism4DCoordinate)point.getCoordinate();
        if (!addCoordinate(coordinate))returnCode = false;


        if (!addPointPicturesToDB(point)) returnCode = false;
        return returnCode;
    }


    //***********************  Read **********************************
    //Reads the Points in from the DB,
    //     and adds them to the project instance passed as an argument

    //Returns the number of points read in
    public int getPointsForProjectFromDB(Prism4DProject project) {
        //Do not go to the ProjectManager for the project, as we may be doing a deep copy
        int projectID = project.getProjectID();
        if (projectID == 0) return 0;

        //This list will be added to the project after the points are read in
        ArrayList<Prism4DPoint> pointsList = new ArrayList<>();

        //read in all the points that belong to the project from the DB
        Cursor cursor = mDatabaseHelper.getObject(  mDatabase,
                                                    Prism4DSqliteOpenHelper.TABLE_POINT,
                                                    null,    //get the whole point
                                                    //Only get the points for this project
                                                    getPointWhereClause(projectID),
                                                    null, null, null, null);

        //need the pointManager to convert a point in the Cursor to a point object
        Prism4DPointManager      pointManager      = Prism4DPointManager.getInstance();

        //iterate over all the rows in the cursor
        Prism4DPoint point;
        int last = cursor.getCount();
        int position = 0;
        while (position < last){
            point = pointManager.getPointFromCursor(cursor, position);
            //add the point to the project
            pointsList.add(point);

            //get the coordinate
            int coordinateID = point.getHasACoordinateID();
            if (coordinateID > 0) {
                //and set it on the point
                point.setCoordinate(getCoordinateFromDB(coordinateID, projectID));
            }

            //Get the pictures for this point
            point.setPictures(getPointPicturesFromDB(point));

            position++;
        }
        //put these point instances on the project
        project.setPoints(pointsList);
        cursor.close();

        return last;
    }




    //********************************    Update   *************************

    public  int updatePoint(Prism4DPoint point) {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject        project        = projectManager.getProject(point.getForProjectID());
        int returnCode = 0;

        //cascade the update to pictures on teh point
        if (updatePointPictures(point) < 1) return returnCode;

        //cascade the update to coordinates on the point
        Prism4DCoordinate coordinate = point.getCoordinate();
        if (coordinate != null){
            if (updateCoordinate(coordinate) < 1)return returnCode;
        }

        //then update the point itself
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();

        returnCode = mDatabaseHelper.update(
                                    mDatabase,
                                    Prism4DSqliteOpenHelper.TABLE_POINT,
                                    pointManager.getCVFromPoint(point),
                                    getPointWhereClause(point.getPointID(), point.getForProjectID()),
                                    null);  //values that replace ? in where clause

        return returnCode;
    }


    //Just updates the point, no subordinate objects
    public  int updatePointOnly(Prism4DPoint point) {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();

        return mDatabaseHelper.update(
                                mDatabase,
                                Prism4DSqliteOpenHelper.TABLE_POINT,
                                pointManager.getCVFromPoint(point),
                                getPointWhereClause(point.getPointID(), point.getForProjectID()),
                                null);  //values that replace ? in where clause

    }





    public  int updatePointPictures(Prism4DPoint point) {

        ArrayList<Prism4DPicture> pictures = point.getPictures();
        int returnCode = updatePictures(pictures);

         return returnCode;
    }

    private int updatePictures(ArrayList<Prism4DPicture> pictures){

        Prism4DPicture picture;

        int last       = pictures.size();
        int position   = 0;
        int returnCode = 0;

        while (position < last){
            picture = pictures.get(position);
            returnCode = returnCode + updatePicture(picture);
        }
        return returnCode;

    }




    //*********************************     Delete    ***************************
    //The return code indicates how many rows affected
    public int removePoint(int pointID, int projectID){

        return mDatabaseHelper.remove(
                mDatabase,
                Prism4DSqliteOpenHelper.TABLE_POINT,
                getPointWhereClause(pointID, projectID),
                null);  //values that replace ? in where clause
    }

    //The return code indicates how many rows affected
    public int removeProjectPoints(int projectID){

        return mDatabaseHelper.remove(
                mDatabase,
                Prism4DSqliteOpenHelper.TABLE_POINT,
                getPointWhereClause(projectID),
                null);  //values that replace ? in where clause
    }


    /************************************************/
    /*        Point specific CRUD  utility         */
    /************************************************/
    //This only gets the one point related to this project
    private String getPointWhereClause(int pointID, int projectID){
        return Prism4DSqliteOpenHelper.POINT_ID + " = '"   +
                String.valueOf(pointID)         + "' AND " +
                Prism4DSqliteOpenHelper.POINT_FOR_PROJECT_ID + " = '" +
                String.valueOf(projectID)       + "'";

    }

    //This gets all the points related to this project
    private String getPointWhereClause(int projectID) {
        return Prism4DSqliteOpenHelper.POINT_FOR_PROJECT_ID +
                                                         " = '" + String.valueOf(projectID) + "'";
    }






    /************************************************/
    /*         Coordinate CRUD methods              */
    /************************************************/



    //Get count of coordinates
    //public int getCoordinateCount() {}

    //******************************    Create    ***********************
    public boolean addCoordinate(Prism4DCoordinate coordinate){
        if (coordinate == null)return false;

        int projectID = coordinate.getProjectID();
        if (projectID == 0)return false;

        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);
        if (project == null)return false;

        String table = getCoordinateTypeTable(project);

        Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();
        ContentValues cv = coordinateManager.getCVFromCoordinate(coordinate);

        long returncode = mDatabaseHelper.add(mDatabase,
                                              table,
                                              null,
                                              cv);

        if (returncode == sERROR_CODE)return false;
        return true;
    }


    //***********************  Read **********************************

    //NOTE this routine does NOT add the coordinate to the Project where
    public Prism4DCoordinate getCoordinateFromDB(int coordinateID, int projectID){
        if (coordinateID == 0) return null;
        if (projectID == 0) return null;

        String table = getCoordinateTypeTable(projectID);

        //get the coordinate row from the DB
        Cursor cursor = mDatabaseHelper.getObject(
                                mDatabase,     //the db to access
                                table,  //table name
                                null,          //get the whole coordinate
                                getCoordinateWhereClause(coordinateID, projectID), //where clause
                                null, null, null, null);//args, group, row grouping, order

        //create a coordinate object from the Cursor object
        Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();

        //get the first row in the cursor
        Prism4DCoordinate coordinate =  coordinateManager.getCoordinateFromCursor(cursor, 0);
        cursor.close();
        return coordinate;
    }



    //********************************    Update   *************************

    public  int updateCoordinate(Prism4DCoordinate coordinate) {

        Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();

        int projectID = coordinate.getProjectID();
        if (projectID == 0) return 0;

        String table = getCoordinateTypeTable(projectID);

        return mDatabaseHelper.update(  mDatabase,
                                        table,
                                        coordinateManager.getCVFromCoordinate(coordinate),
                                        getCoordinateWhereClause(coordinate.getCoordinateID(), projectID),
                                        null);  //values that replace ? in where clause

    }



    //*********************************     Delete    ***************************
    //The return code indicates how many rows affected
    public int removeCoordinate(int coordinateID, int projectID){
        if (coordinateID == 0) return 0;
        if (projectID == 0) return 0;


        String table = getCoordinateTypeTable(projectID);

        return mDatabaseHelper.remove(  mDatabase,
                                        table,
                                        getCoordinateWhereClause(coordinateID, projectID),
                                        null);  //values that replace ? in where clause
    }

    //The return code indicates how many rows affected
    public int removeProjectCoordinates(int projectID){
        if (projectID == 0) return 0;

        String table = getCoordinateTypeTable(projectID);

        return mDatabaseHelper.remove(  mDatabase,
                                        table,
                                        getCoordinateWhereClause(projectID),
                                        null);  //values that replace ? in where clause
    }


    /************************************************/
    /*        Coordinate specific CRUD  utility         */
    /************************************************/
    //This only gets the one coordinate related to this project
    private String getCoordinateWhereClause(int coordinateID, int projectID){
        return Prism4DSqliteOpenHelper.COORDINATE_ID +
                                     " = '" +  String.valueOf(coordinateID)   + "' AND " +
                Prism4DSqliteOpenHelper.COORDINATE_PROJECT_ID +
                                     " = '" + String.valueOf(projectID)       + "'";

    }

    //This gets all the coordinates related to this project
    private String getCoordinateWhereClause(int projectID) {
        return Prism4DSqliteOpenHelper.COORDINATE_PROJECT_ID +
                                                         " = '" + String.valueOf(projectID) + "'";
    }


    private String getCoordinateTypeTable(Prism4DProject project){
        //Get the table to read from from the Project
        CharSequence coordinateType = project.getProjectCoordinateType();
        String table = Prism4DSqliteOpenHelper.TABLE_COORDINATE_LL; //assume a default
        if ((coordinateType == Prism4DCoordinate.sCoordinateTypeUTM) ||
                (coordinateType == Prism4DCoordinate.sCoordinateTypeSPCS)){
            table = Prism4DSqliteOpenHelper.TABLE_COORDINATE_EN;
        }
        return table;
    }

    private String getCoordinateTypeTable(int projectID){
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);
        return getCoordinateTypeTable(project);
    }






    /************************************************/
    /*         CoordinateMean CRUD methods              */
    /************************************************/



    //Get count of coordinates

    //public int getCoordinateMeanCount() {}

    //******************************    Create    ***********************
    public boolean addCoordinateMean(Prism4DCoordinateMean coordinate){
        if (coordinate == null)return false;

        int projectID = coordinate.getProjectID();
        if (projectID == 0)return false;

        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject project = projectManager.getProject(projectID);
        if (project == null)return false;

        Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();
        ContentValues cv = coordinateManager.getCVFromCoordinateMean(coordinate);

        long returncode = mDatabaseHelper.add(mDatabase,
                Prism4DSqliteOpenHelper.TABLE_COORDINATE_MEAN,
                null,
                cv);

        if (returncode == sERROR_CODE)return false;
        return true;
    }


    //***********************  Read **********************************

    //just returns the single coordinateMean object that corresponds to the coordinateID
    public Prism4DCoordinateMean getCoordinateMeanFromDB(int coordinateID, int projectID){
        if (coordinateID == 0) return null;
        if (projectID == 0) return null;

        //get the coordinate row from the DB
        Cursor cursor = mDatabaseHelper.getObject(
                mDatabase,     //the db to access
                Prism4DSqliteOpenHelper.TABLE_COORDINATE_MEAN,  //table name
                null,          //get the whole coordinate
                getCoordinateWhereClause(coordinateID, projectID), //where clause
                null, null, null, null);//args, group, row grouping, order

        //create a coordinate object from the Cursor object
        Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();

        //get the first row in the cursor
        Prism4DCoordinateMean coordinate =  coordinateManager.getCoordinateMeanFromCursor(cursor, 0);
        cursor.close();
        return coordinate;
    }


    //Get all the CoordinateMean objects for this project that are in the DB
    public ArrayList<Prism4DCoordinateMean> getAllCoordinateMeanFromDB(int projectID){

        if (projectID == 0) return null;

        //get the coordinate row from the DB
        Cursor cursor = mDatabaseHelper.getObject(
                mDatabase,     //the db to access
                Prism4DSqliteOpenHelper.TABLE_COORDINATE_MEAN,  //table name
                null,          //get the whole coordinate
                getCoordinateWhereClause(projectID), //where clause
                null, null, null, null);//args, group, row grouping, order

        //create a coordinate object from the Cursor object
        Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();

        Prism4DCoordinateMean coordinateMean;
        ArrayList<Prism4DCoordinateMean> coordinateMeans = new ArrayList<>();

        int position = 0;
        int last = cursor.getCount();
        Prism4DProject project;
        while (position < last) {
            coordinateMean = coordinateManager.getCoordinateMeanFromCursor(cursor, position);
            if (coordinateMean != null) {
                // TODO: 12/21/2016 Need to let someone know that something is wrong here if null
                //Add the coordinateMean object to the list
                coordinateMeans.add(coordinateMean);
            }

            position++;
        }
        cursor.close();
        return coordinateMeans;

    }



    //********************************    Update   *************************

    public  int updateCoordinateMean(Prism4DCoordinateMean coordinate) {

        Prism4DCoordinateManager coordinateManager = Prism4DCoordinateManager.getInstance();

        int projectID = coordinate.getProjectID();
        if (projectID == 0) return 0;


        return mDatabaseHelper.update(  mDatabase,
                Prism4DSqliteOpenHelper.TABLE_COORDINATE_MEAN,
                coordinateManager.getCVFromCoordinateMean(coordinate),
                getCoordinateMeanWhereClause(coordinate.getCoordinateID(), projectID),
                null);  //values that replace ? in where clause

    }



    //*********************************     Delete    ***************************
    //The return code indicates how many rows affected
    public int removeCoordinateMean(int coordinateID, int projectID){
        if (coordinateID == 0) return 0;
        if (projectID == 0) return 0;

        return mDatabaseHelper.remove(  mDatabase,
                                        Prism4DSqliteOpenHelper.TABLE_COORDINATE_MEAN,
                                        getCoordinateMeanWhereClause(coordinateID, projectID),
                                        null);  //values that replace ? in where clause
    }

    //The return code indicates how many rows affected
    public int removeProjectCoordinatesMean(int projectID){
        if (projectID == 0) return 0;

        return mDatabaseHelper.remove(  mDatabase,
                                        Prism4DSqliteOpenHelper.TABLE_COORDINATE_MEAN,
                                        getCoordinateMeanWhereClause(projectID),
                                        null);  //values that replace ? in where clause
    }


    /************************************************/
    /*        Coordinate specific CRUD  utility         */
    /************************************************/
    //This only gets the one coordinate related to this project
    private String getCoordinateMeanWhereClause(int coordinateID, int projectID){
        return Prism4DSqliteOpenHelper.COORDINATE_MEAN_ID + " = '" +
                                                    String.valueOf(coordinateID)   + "' AND " +
               Prism4DSqliteOpenHelper.COORDINATE_MEAN_PROJECT_ID + " = '" +
                                                    String.valueOf(projectID)       + "'";

    }

    //This gets all the coordinates related to this project
    private String getCoordinateMeanWhereClause(int projectID) {
        return Prism4DSqliteOpenHelper.COORDINATE_MEAN_PROJECT_ID + " = '" +
                                                    String.valueOf(projectID) + "'";
    }






    /************************************************/
    /*         Picture CRUD methods             */
    /************************************************/

    //*********************    Create    ***********************


    public boolean addProjectPicturesToDB(Prism4DProject project){
        ArrayList<Prism4DPicture> pictures = project.getPictures();
        if (pictures == null){
            pictures = new ArrayList<Prism4DPicture>();
            project.setPictures(pictures);
        }

        return addPicturesToDB(pictures);
    }


    public boolean addPointPicturesToDB(Prism4DPoint point){
        ArrayList<Prism4DPicture> pictures = point.getPictures();
        if (pictures == null){
            pictures = new ArrayList<Prism4DPicture>();
            point.setPictures(pictures);
        }

        return addPicturesToDB(pictures);
    }


    public boolean addPicturesToDB(ArrayList<Prism4DPicture> pictures){
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

        int position = 0;
        int last = pictures.size();
        Prism4DPicture picture;
        boolean returnCode = true;
        while (position < last) {
            picture = pictures.get(position);
            returnCode = returnCode & addPicture(picture);
            position++;
        }
        return returnCode;
    }

    public boolean addPicture(Prism4DPicture picture){
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        long ret =  mDatabaseHelper.add(mDatabase,
                                        Prism4DSqliteOpenHelper.TABLE_PICTURE,
                                        null,
                                        projectManager.getCVFromPicture(picture));
        if (ret == sERROR_CODE)return false;

        return true;
    }



    //***********************  Read **********************************


    //return the pictures for this project (that are currently in the DB)
    public ArrayList<Prism4DPicture> getProjectPicturesFromDB(Prism4DProject project){
        if (project == null)return null;
        String whereClause = getPictureWhereClause(project.getProjectID());
        return getPicturesFromDB(whereClause);

    }


    //return the pictures for this point (that are currently in the DB)
    public ArrayList<Prism4DPicture> getPointPicturesFromDB (Prism4DPoint point){
        if (point == null)return null;
        String whereClause = getPictureWhereClause(point.getForProjectID(), point.getPointID());
        return getPicturesFromDB(whereClause);

    }

    //actually get the pictures from the DB, regardless of where they are to be stored
    public ArrayList<Prism4DPicture> getPicturesFromDB (String whereClause){

        //get the pictures for this project from the DB
        Cursor cursor = mDatabaseHelper.getObject(
                                        mDatabase,     //the db to access
                                        Prism4DSqliteOpenHelper.TABLE_PICTURE,  //table name
                                        null,          //get the whole picture
                                        whereClause, //where clause
                                        null, null, null, null);//args, group, row grouping, order

        //create the picture objects from the Cursor object
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

        //and store the pictures in a list
        ArrayList<Prism4DPicture> pictures = new ArrayList<>();

        int position = 0;
        int last = cursor.getCount();
        Prism4DPicture picture;
        while (position < last) {
            picture = projectManager.getPictureFromCursor(cursor, position);
            pictures.add(picture);
            position++;
        }
        cursor.close();

        return pictures;

    }

    //***********************  Update   *************************


    public  int updatePicture(Prism4DPicture picture) {

        int returnCode = 0;
        //pictures are managed by the project manager
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

        returnCode = mDatabaseHelper.update(mDatabase,
                Prism4DSqliteOpenHelper.TABLE_PICTURE,
                projectManager.getCVFromPicture(picture),
                getPictureWhereClause(picture.getPictureID().toString(), picture.getProjectID()),
                null);  //values that replace ? in where clause

        return returnCode;
    }




    //********************     Delete    ***************************

    //The return code indicates how many rows affected
    //Removes all pictures, both at the project level and the point level
    public int removeProjectPictures(int projectID){

        return mDatabaseHelper.remove(  mDatabase,
                Prism4DSqliteOpenHelper.TABLE_PICTURE,
                getPictureWhereClause(projectID),
                null);  //values that replace ? in where clause
    }


    //The return code indicates how many rows affected
    public int removePicturesFromPoint(Prism4DPoint point){
        String whereClause = getPictureWhereClause(point.getForProjectID(), point.getPointID());

        return mDatabaseHelper.remove(  mDatabase,
                Prism4DSqliteOpenHelper.TABLE_PICTURE,
                whereClause,
                null);  //values that replace ? in where clause
    }


    //The return code indicates how many rows affected
    //Removes only one picture from the project level
    public int removeProjectPicture(String pictureID, int projectID){

        return mDatabaseHelper.remove(  mDatabase,
                                        Prism4DSqliteOpenHelper.TABLE_PICTURE,
                                        getPictureWhereClause(pictureID, projectID),
                                        null);  //values that replace ? in where clause
    }


    //The return code indicates how many rows affected
    //Removes only one picture from the project level
    public int removePointPicture(String pictureID, int projectID, int pointID){
        String whereClause = getPictureWhereClause(pictureID, projectID, pointID);

        return mDatabaseHelper.remove(  mDatabase,
                                        Prism4DSqliteOpenHelper.TABLE_PICTURE,
                                        whereClause,
                                        null);  //values that replace ? in where clause
    }



    /************************************************/
    /*        Picture specific CRUD  utility         */
    /************************************************/

    //This only gets the one picture related to this project
    private String getPictureWhereClause(String pictureID, int projectID){
        return
         Prism4DSqliteOpenHelper.PICTURE_ID         + " = '" + pictureID               + "' AND " +
         Prism4DSqliteOpenHelper.PICTURE_PROJECT_ID + " = '" + String.valueOf(projectID) + "'";

    }

    //This only gets the one picture from the point
    private String getPictureWhereClause(String pictureID, int projectID, int pointID){
        return
          Prism4DSqliteOpenHelper.PICTURE_ID         + " = '" + pictureID                 + "' AND " +
          Prism4DSqliteOpenHelper.PICTURE_PROJECT_ID + " = '" + String.valueOf(projectID) + "' AND " +
          Prism4DSqliteOpenHelper.PICTURE_POINT_ID   + " = '" + String.valueOf(pointID) + "' " ;

    }


    //This gets all the pictures related to this project
    private String getPictureWhereClause(int projectID) {
        return
            Prism4DSqliteOpenHelper.PICTURE_PROJECT_ID + " = '" + String.valueOf(projectID) + "'";
    }


    //This gets all pictures on this point of this project
    private String getPictureWhereClause(int projectID, int pointID){
        return
         Prism4DSqliteOpenHelper.PICTURE_PROJECT_ID + " = '" + String.valueOf(projectID) + "' AND "+
         Prism4DSqliteOpenHelper.PICTURE_POINT_ID   + " = '" + String.valueOf(pointID)   + "' ";

    }



    /************************************************/
    /*         Global Data CRUD methods             */
    /************************************************/

    //******************************    Create    ***********************
    public boolean addGlobalData(Prism4DGlobalData globalData){
        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
        long returnCode = mDatabaseHelper.add(mDatabase,        //database to add to
                Prism4DSqliteOpenHelper.TABLE_GLOBAL_DATA,     //table to add to
                null,                                          //nullColumnHack
                globalDataManager.getGlobalDataCV(globalData));//Content Values of the object to add
        if (returnCode == sERROR_CODE)return false;
        return true;
    }



    //***********************  Read **********************************
    //Reads the Points into memory
    //Returns true if read was successful
    public Prism4DGlobalData getGlobalData(){

        Cursor cursor = mDatabaseHelper.getObject(  mDatabase,
                                                    Prism4DSqliteOpenHelper.TABLE_GLOBAL_DATA,
                                                    null,    //get the whole object
                                                    null,   //get all the global data rows
                                                    null, null, null, null);


        //create a globalData object from the Cursor object
        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
        Prism4DGlobalData globalData = globalDataManager.getGlobalDataFromCursor(cursor, 0);

        cursor.close();
        return globalData;

    }

    //********************************    Update   *************************

    public  int updateGlobalData(Prism4DGlobalData globalData) {

        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();

        return mDatabaseHelper.update(
                                        mDatabase,
                                        Prism4DSqliteOpenHelper.TABLE_GLOBAL_DATA,
                                        globalDataManager.getGlobalDataCV(globalData),
                                        null,
                                        null);  //values that replace ? in where clause

    }



    //*********************************     Delete    ***************************





    /************************************************/
    /*         Static inner classes                 */
    /************************************************/

    /************************************************/
    /*         inner classes                        */
    /************************************************/

}
