package com.asc.msigeosystems.prism4d.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.asc.msigeosystems.prism4d.Prism4DGlobalData;
import com.asc.msigeosystems.prism4d.Prism4DGlobalDataManager;
import com.asc.msigeosystems.prism4d.Prism4DPoint;
import com.asc.msigeosystems.prism4d.Prism4DPointManager;
import com.asc.msigeosystems.prism4d.Prism4DProject;
import com.asc.msigeosystems.prism4d.Prism4DProjectManager;

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
    public static synchronized Prism4DDatabaseManager initializeInstance(Context context) throws RuntimeException {
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
    public static synchronized Prism4DDatabaseManager getInstance(Context context) throws RuntimeException {
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
    public void addProject(Prism4DProject project){
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        mDatabaseHelper.add(mDatabase,                               //database to add to
                            Prism4DSqliteOpenHelper.TABLE_PROJECT,   //table to add to
                            null,                                    //nullColumnHack
                            projectManager.getCVFromProject(project));   //Content Values of the object to add
    }


    //*********************************** READ ********************************
    //returns the number of Projects read in from the DB
    public int getAllProjects(){

        Cursor cursor = mDatabaseHelper.getObject(  mDatabase,
                                                    Prism4DSqliteOpenHelper.TABLE_PROJECT,
                                                    null,    //get the whole project
                                                    null,    //get all projects.
                                                    null, null, null, null);

        //get the project row from the DB
        /********************************
         Cursor query (String table, //Table Name
         String[] columns,   //Columns to return, null for all columns
         String where_clause,
         String[] selectionArgs, //replaces ? in the where_clause with these arguments
         String groupBy, //null meanas no grouping
         String having,   //row grouping
         String orderBy)  //null means the default sort order
         *********************************/

        //create a project object from the Cursor object
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();

        int position = 0;
        int last = cursor.getCount();
        Prism4DProject project;
        while (position < last) {
            project = projectManager.getProjectFromCursor(cursor, position);
            if (project != null) {
                projectManager.addFromDB(project);
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
                                            Prism4DSqliteOpenHelper.TABLE_PROJECT,  //table name
                                            null,                 //get the whole project
                                            getProjectWhereClause(projectID),      //where clause
                                            null, null, null, null); //args,group,row grouping,order


/********************************
 Cursor query (String table, //Table Name
 String[] columns,   //Columns to return, null for all columns
 String where_clause,
 String[] selectionArgs, //replaces ? in the where_clause with these arguments
 String groupBy, //null meanas no grouping
 String having,   //row grouping
 String orderBy)  //null means the default sort order
 *********************************/

        //create a project object from the Cursor object
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        return projectManager.getProjectFromCursor(cursor, 0);//get the first row in the cursor
    }



    //*********************************** UPDATE ********************************

    public  int updateProject(int  projectID) {

        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        Prism4DProject        project        = projectManager.getProject(projectID);

        return mDatabaseHelper.update(
                mDatabase,
                Prism4DSqliteOpenHelper.TABLE_PROJECT,
                projectManager.getCVFromProject(project),
                getProjectWhereClause(projectID),
                null);  //values that replace ? in where clause

    }

    public  int updateProject(Prism4DProject project) {
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        int             projectID      = project.getProjectID();

        return mDatabaseHelper.update(
                mDatabase,
                Prism4DSqliteOpenHelper.TABLE_PROJECT,
                projectManager.getCVFromProject(project),
                getProjectWhereClause(projectID),
                null);  //values that replace ? in where clause

    }



    //*********************************** DELETE ********************************

    //The return code inidcates how many rows affected
    public int removeProject(int projectID){

        return mDatabaseHelper.remove(
                mDatabase,
                Prism4DSqliteOpenHelper.TABLE_PROJECT,
                getProjectWhereClause(projectID),
                null);  //values that replace ? in where clause
    }


    /************************************************/
    /*        Project specific CRUD  utility         */
    /************************************************/
    private String getProjectWhereClause(int projectID){
        return Prism4DSqliteOpenHelper.PROJECT_ID + " = " + String.valueOf(projectID);
    }



    /************************************************/
    /*        Point CRUD methods               */
    /************************************************/


    //Get count of points
    //// TODO: 11/1/2016 write this routine if it is needed
    //public int getPointCount() {}

    //******************************    Create    ***********************
    public void addPoint(Prism4DPoint point){
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        mDatabaseHelper.add(mDatabase,
                Prism4DSqliteOpenHelper.TABLE_POINT,
                null,
                pointManager.getPointCV(point));
    }


    //***********************  Read **********************************
    //Reads the Points into memory
    //Returns the number of points read in
    public int getAllPoints(int projectID){
        if (projectID == 0) return 0;

        Cursor cursor = mDatabaseHelper.getObject(  mDatabase,
                Prism4DSqliteOpenHelper.TABLE_POINT,
                null,    //get the whole point
                getPointWhereClause(projectID),    //Only get the points for this project
                null, null, null, null);

        //get the point row from the DB
        /********************************
         Cursor query (String table, //Table Name
         String[] columns,   //Columns to return, null for all columns
         String where_clause,
         String[] selectionArgs, //replaces ? in the where_clause with these arguments
         String groupBy, //null meanas no grouping
         String having,   //row grouping
         String orderBy)  //null means the default sort order
         *********************************/

        //create a point object from the Cursor object
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();

        int position = 0;
        int last = cursor.getCount();
        Prism4DPoint point;
        while (position < last) {
            //translate the cursor into a point object
            //but only if the point belongs to the right project
            point = pointManager.getPointFromCursor(cursor, position);
            //add the point object to the point manager's list (which is on the project)
            if (point != null) {
                if (!pointManager.addFromDB(point)) {
                    throw new RuntimeException("Can't add point from DB");
                }
            }
            position++;
        }
        cursor.close();
        return last;

    }

    //NOTE this routine does NOT add the point to the Project where
    public Prism4DPoint getPoint(int pointID, int projectID){

        //get the point row from the DB
        Cursor cursor = mDatabaseHelper.getObject(
                mDatabase,     //the db to access
                Prism4DSqliteOpenHelper.TABLE_POINT,  //table name
                null,          //get the whole point
                getPointWhereClause(pointID, projectID), //where clause
                null, null, null, null);//args, group, row grouping, order


/********************************
 Cursor query (String table, //Table Name
 String[] columns,   //Columns to return, null for all columns
 String where_clause,
 String[] selectionArgs, //replaces ? in the where_clause with these arguments
 String groupBy, //null meanas no grouping
 String having,   //row grouping
 String orderBy)  //null means the default sort order
 *********************************/

        //create a point object from the Cursor object
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        return pointManager.getPointFromCursor(cursor, 0);//get the first row in the cursor
    }



    //********************************    Update   *************************

    public  int updatePoint(Prism4DPoint point) {

        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();

        return mDatabaseHelper.update(
                mDatabase,
                Prism4DSqliteOpenHelper.TABLE_POINT,
                pointManager.getPointCV(point),
                getPointWhereClause(point.getPointID(), point.getForProjectID()),
                null);  //values that replace ? in where clause

    }



    //*********************************     Delete    ***************************
    //The return code inidcates how many rows affected
    public int removePoint(int pointID, int projectID){

        return mDatabaseHelper.remove(
                mDatabase,
                Prism4DSqliteOpenHelper.TABLE_POINT,
                getPointWhereClause(pointID, projectID),
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
        return Prism4DSqliteOpenHelper.POINT_FOR_PROJECT_ID + " = '" +
                                                            String.valueOf(projectID) + "'";
    }



    /************************************************/
    /*         Global Data CRUD methods             */
    /************************************************/

    //******************************    Create    ***********************
    public void addGlobalData(Prism4DGlobalData globalData){
        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
        mDatabaseHelper.add(mDatabase,                               //database to add to
                Prism4DSqliteOpenHelper.TABLE_PROJECT,   //table to add to
                null,                                    //nullColumnHack
                globalDataManager.getGlobalDataCV(globalData));   //Content Values of the object to add
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
