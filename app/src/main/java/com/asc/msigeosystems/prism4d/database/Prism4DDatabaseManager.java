package com.asc.msigeosystems.prism4d.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by elisabethhuhn on 5/18/2016.
 */
public class Prism4DDatabaseManager {

    private static Prism4DDatabaseManager   sManagerInstance ;
    private static Prisim4DSqliteOpenHelper sDatabaseHelper;
    private static SQLiteDatabase sDatabase;

    private static String sNoContextException = "Can not create database without a context";
    private static String sNotInitializedException =
            "Attempt to access the database before it has been initialized";
    private static String sNotOpenedException =
            "Attempt to access the database before it has been opened";



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
            sDatabaseHelper = new Prisim4DSqliteOpenHelper(context);

            //Now that we have the connection, create the database as well
            sDatabase = sDatabaseHelper.getWritableDatabase();

        } else if (sDatabaseHelper == null){
            //if here, we had a Database Manager, but no connection to a database
            //Something is definately fishy, but maybe we can recover
            if (context == null) throw new RuntimeException(sNoContextException);

            //all the constructor does is save the context
            sDatabaseHelper = new Prisim4DSqliteOpenHelper(context);
            //opening the database will create the tables if they do not
            //  already exist. It will also force an upgrade if the system
            //  detects a new version of the DB since the last time it was opened
            sDatabase = sDatabaseHelper.getWritableDatabase();

        } else if (sDatabase == null){
            //again, if we had a database manager, and a database helper/connection
            // we certainly should have had an instance of the database
            //something is fishy, but attempt recovery
            sDatabase = sDatabaseHelper.getWritableDatabase();
        }

    }

    //null constructor. It should never be called.
    //    initializeInstance() is the proper protocol
    private Prism4DDatabaseManager() {}


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


    public static synchronized Prisim4DSqliteOpenHelper getDatabaseHelper() throws RuntimeException {
        //The reason we can't just initialize it now is because we need a context to initialize
        if ((sManagerInstance == null) || (sDatabaseHelper == null)){
            throw new RuntimeException(sNotInitializedException);
        }
        return sDatabaseHelper;
    }


    public static synchronized SQLiteDatabase getDatabase() throws RuntimeException {
        //we need a context to initialize the helper,
        if ((sManagerInstance == null) || (sDatabaseHelper == null) || (sDatabase == null)){
            throw new RuntimeException(sNotOpenedException);
        }
        return sDatabase;
    }



}
