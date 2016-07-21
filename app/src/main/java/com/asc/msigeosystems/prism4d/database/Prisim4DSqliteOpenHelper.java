package com.asc.msigeosystems.prism4d.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by elisabethhuhn on 7/9/2016.
 */
public class Prisim4DSqliteOpenHelper extends SQLiteOpenHelper {
    //logcat Tag
    private static final String TAG = "Prisim4DSqliteOpenHelper";

       //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "Prism4D";

    //Table Names
    public static final String TABLE_PROJECT = "Project";
    public static final String TABLE_PROJECT_SETTINGS = "ProjectSettings";
    public static final String TABLE_POINT = "Point";
    public static final String TABLE_COORDINATE = "Coordinate";

    //Common Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";

    //Project Column Names
    public static final String PROJECT_ID = "project_id";
    public static final String PROJECT_NAME = "project_name";
    public static final String PROJECT_LAST_MAINTAINED = "project_last_maintained";
    public static final String PROJECT_DESCRIPTION = "project_description";

    //Project Settings Column Names
    public static final String PROJECT_SETTINGS_ID = "project_settings_id";
    public static final String PROJECT_SETTINGS_DISTANCE_UNITS = "project_settings_distance_units";
    public static final String PROJECT_SETTINGS_DECIMAL_DISPLAY = "project_settings_decimal_display";
    public static final String PROJECT_SETTINGS_ANGLE_UNITS = "project_settings_angle_units";
    public static final String PROJECT_SETTINGS_GRID_DIRECTION = "project_settings_grid_direction";
    public static final String PROJECT_SETTINGS_SCALE_FACTOR = "project_settings_scale_factor";
    public static final String PROJECT_SETTINGS_SEA_LEVEL = "project_settings_sea_level";
    public static final String PROJECT_SETTINGS_REFRACTION = "project_settings_refraction";
    public static final String PROJECT_SETTINGS_DATUM = "project_settings_datum";
    public static final String PROJECT_SETTINGS_PROJECTION = "project_settings_projection";
    public static final String PROJECT_SETTINGS_ZONE = "project_settings_zone";
    public static final String PROJECT_SETTINGS_SPC_SCALE_FACTOR = "project_settings_spc_scale_factor";
    public static final String PROJECT_SETTINGS_GEOID_MODEL = "project_settings_geoid_model";
    public static final String PROJECT_SETTINGS_STARTING_POINT_ID = "project_settings_starting_point_id";
    public static final String PROJECT_SETTINGS_ALPHANUMERIC = "project_settings_alphanumeric";
    public static final String PROJECT_SETTINGS_FEATURE_CODES = "project_settings_feature_codes";
    public static final String PROJECT_SETTINGS_FC_CONTROL_FILE = "project_settings_fc_control_file";
    public static final String PROJECT_SETTINGS_FC_TIMESTAMP = "project_settings_fc_timestamp";


    //Point Column Names
    public static final String POINT_ID = "point_id";
    public static final String POINT_PROJECT_ID = "point_project_id";
    public static final String POINT_COORDINATE = "point_coordinate";
    public static final String POINT_DESCRIPTION = "point_description";
    public static final String POINT_NOTES = "point_notes";

    //create table statements

    //create project table
    private static final String CREATE_TABLE_PROJECT = "CREATE TABLE " + TABLE_PROJECT +"(" +
            KEY_ID                  + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROJECT_ID              + "INTEGER, " +
            PROJECT_NAME            + "TEXT"    +
            PROJECT_LAST_MAINTAINED + "DATETIME" +
            PROJECT_DESCRIPTION     + "TEXT"    +
            KEY_CREATED_AT          + "DATETIME" + ")";

    //table create statements
    private static final String CREATE_TABLE_PROJECT_SETTINGS = "CREATE TABLE " + TABLE_PROJECT_SETTINGS +"(" +
            KEY_ID                           + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROJECT_SETTINGS_ID              + "INTEGER, "  +
            PROJECT_SETTINGS_DISTANCE_UNITS  + "TEXT, "     +
            PROJECT_SETTINGS_DECIMAL_DISPLAY + "TEXT, "     +
            PROJECT_SETTINGS_ANGLE_UNITS     + "TEXT, "     +
            PROJECT_SETTINGS_GRID_DIRECTION  + "TEXT, "     +
            PROJECT_SETTINGS_SCALE_FACTOR    + "REAL, "     +
            PROJECT_SETTINGS_SEA_LEVEL       + "INTEGER, "  + //BOOLEAN  no-0/1-yes
            PROJECT_SETTINGS_REFRACTION      + "INTEGER, "  + //BOOLEAN  0/1
            PROJECT_SETTINGS_DATUM           + "TEXT, "     +
            PROJECT_SETTINGS_PROJECTION      + "TEXT, "     +
            PROJECT_SETTINGS_ZONE            + "TEXT, "     +
            PROJECT_SETTINGS_SPC_SCALE_FACTOR + "REAL, "    +
            PROJECT_SETTINGS_GEOID_MODEL     + "TEXT, "     +
            PROJECT_SETTINGS_STARTING_POINT_ID + "INTEGER, " +
            PROJECT_SETTINGS_ALPHANUMERIC    + "INTEGER, "  + //BOOLEAN  no-0/1-yes
            PROJECT_SETTINGS_FEATURE_CODES   + "TEXT, "     +
            PROJECT_SETTINGS_FC_CONTROL_FILE + "TEXT, "     +
            PROJECT_SETTINGS_FC_TIMESTAMP    + "INTEGER, "  + //BOOLEAN  no-0/1-yes
            KEY_CREATED_AT                   + "DATETIME, " + ")";

    //create project table
    private static final String CREATE_TABLE_POINT = "CREATE TABLE " + TABLE_POINT +"(" +
            KEY_ID             + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            POINT_ID           + "INTEGER, " +
            POINT_PROJECT_ID   + "INTEGER, " +
            POINT_COORDINATE   + "INTEGER, " +
            POINT_DESCRIPTION  + "TEXT, "    +
            POINT_NOTES        + "TEXT, "    +
            KEY_CREATED_AT     + "DATETIME"  + ")";


    private Context mContext;
    private SQLiteDatabase mDatabase;


    //This should be called with the APPLICATION context
    public Prisim4DSqliteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //create the tables
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_PROJECT_SETTINGS);
        db.execSQL(CREATE_TABLE_POINT);
    }

    /******************
     * This default verson of the onUpgrade() method just
     * deletes any data in the database file, and recreates the
     * database from scratch.
     *
     * Obviously, in the production version, this method will have
     * to migrate data in the old version table layout
     * to the new version table layout.
     * Renaming tables,
     * creating new tables,
     * writing data from renamed table to the new table,
     * then dropping the renamed table.
     * And doing this in a cascading fashion so the tables can
     * be brought up to date over several versions.
     * @param db         The instance of the db to be upgraded
     * @param OldVersion The old version number
     * @param newVersion The new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINT);

        //Create new tables
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    //Open the database, and return our instance of the database


    //The CRUD routines:


}
