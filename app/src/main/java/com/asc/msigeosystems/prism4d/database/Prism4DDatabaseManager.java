package com.asc.msigeosystems.prism4d.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.asc.msigeosystems.prism4d.Prism4DProject;
import com.asc.msigeosystems.prism4d.Prism4DProjectManager;

import java.util.ArrayList;

/**
 * Created by elisabethhuhn on 5/18/2016.
 */
public class Prism4DDatabaseManager {

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
    private  SQLiteDatabase           mDatabase;



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
     * SQLiteDatabase myDB = Prism4DDatabaseManager.getInstance().getDatabase();
     */
    public static synchronized void initializeInstance(Context context) throws RuntimeException {
        if (sManagerInstance == null){
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

        } else if (sManagerInstance.getDatabaseHelper() == null){
            //if here, we had a Database Manager, but no connection to a database
            //Something is definately fishy, but maybe we can recover
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



    /************************************************/
    /*         Instance methods                     */
    /************************************************/
    //The CRUD routines:


    //CRUD routines for a Project
    //STUBS for now

    public ArrayList<Prism4DProject> getAllProjects(){
        return new ArrayList<>();
    }

    public Prism4DProject getProject(int projectID){
        return new Prism4DProject("StubProject", 10001);//dummy parameters for now
    }

    public void addProject(Prism4DProject project){
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        mDatabaseHelper.add(mDatabase,                               //database to add to
                            Prism4DSqliteOpenHelper.TABLE_PROJECT,   //table to add to
                            null,                                    //nullColumnHack
                            projectManager.getProjectCV(project));   //Content Values of the object to add
    }

    public  void updateProject(Prism4DProject project){}

    public void removeProject(int projectID){}





}
