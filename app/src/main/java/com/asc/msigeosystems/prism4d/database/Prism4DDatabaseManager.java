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

    public static synchronized void initializeInstance(Context context) throws RuntimeException {
        //todo need to investigate what exceptions can be thrown by these
        if (sManagerInstance == null){
            if (context == null) throw new RuntimeException("Can't create database without a context");
            sManagerInstance = new Prism4DDatabaseManager();
            sDatabaseHelper = new Prisim4DSqliteOpenHelper(context);
            sDatabase = sDatabaseHelper.getWritableDatabase();
        } else if (sDatabaseHelper == null){
            if (context == null) throw new RuntimeException("Can't create database without a context");
            sDatabaseHelper = new Prisim4DSqliteOpenHelper(context);
            sDatabase = sDatabaseHelper.getWritableDatabase();
        } else if (sDatabase == null){
            sDatabase = sDatabaseHelper.getWritableDatabase();
        }

    }

    private Prism4DDatabaseManager() {}


    public static synchronized Prism4DDatabaseManager getInstance() {
        if (sManagerInstance == null){return null;}
        return sManagerInstance;
    }


    public Prisim4DSqliteOpenHelper getDatabaseHelper() {
        if (sManagerInstance == null){return null;}
        if (sDatabaseHelper == null){return null;}
        return sDatabaseHelper;
    }


    public SQLiteDatabase getDatabase() {
        if (sManagerInstance == null){return null;}
        if (sDatabaseHelper == null){return null;}
        if (sDatabase == null){return null;}
        return sDatabase;
    }



}
