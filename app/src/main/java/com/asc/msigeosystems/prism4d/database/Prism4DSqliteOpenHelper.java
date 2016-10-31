package com.asc.msigeosystems.prism4d.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asc.msigeosystems.prism4d.Prism4DProject;

import java.util.ArrayList;

/**
 * Created by elisabethhuhn on 7/9/2016.
 */
public class Prism4DSqliteOpenHelper extends SQLiteOpenHelper {
    //logcat Tag
    private static final String TAG = "Prism4DSqliteOpenHelper";

    /*****************************************************/
    /*****************************************************/
    /*****************************************************/

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "Prism4D";

    /*****************************************************/
    /**********  Tables          *************************/
    /*****************************************************/

    //Table Names
    public static final String TABLE_PROJECT = "Project";
    public static final String TABLE_PROJECT_SETTINGS = "ProjectSettings";
    public static final String TABLE_POINT = "Point";
    public static final String TABLE_COORDINATE_EN = "CoordinateEN";
    public static final String TABLE_COORDINATE_LL = "CoordinateLL";

    //Global Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";


    /*****************************************************/
    /************ Project       **************************/
    /*****************************************************/

    //Project Column Names
    public static final String PROJECT_ID =              "project_id";
    public static final String PROJECT_NAME =            "project_name";
    public static final String PROJECT_CREATED =         "project_created";
    public static final String PROJECT_LAST_MAINTAINED = "project_last_maintained";
    public static final String PROJECT_DESCRIPTION =     "project_description";

    //create project table
    private static final String CREATE_TABLE_PROJECT = "CREATE TABLE " + TABLE_PROJECT +"(" +
            KEY_ID                  + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROJECT_ID              + "INTEGER, " +
            PROJECT_NAME            + "TEXT"      +
            PROJECT_CREATED         + "INTEGER"   +
            PROJECT_LAST_MAINTAINED + "INTEGER"   +
            PROJECT_DESCRIPTION     + "TEXT"      +
            KEY_CREATED_AT          + "DATETIME"  + ")";

    /*****************************************************/
    /********  Project Settings   ************************/
    /*****************************************************/

    //Project Settings Column Names
    public static final String PROJECT_SETTINGS_ID                = "project_settings_id";
    public static final String PROJECT_SETTINGS_DISTANCE_UNITS    = "project_settings_distance_units";
    public static final String PROJECT_SETTINGS_DECIMAL_DISPLAY   = "project_settings_decimal_display";
    public static final String PROJECT_SETTINGS_ANGLE_UNITS       = "project_settings_angle_units";
    public static final String PROJECT_SETTINGS_GRID_DIRECTION    = "project_settings_grid_direction";
    public static final String PROJECT_SETTINGS_SCALE_FACTOR      = "project_settings_scale_factor";
    public static final String PROJECT_SETTINGS_SEA_LEVEL         = "project_settings_sea_level";
    public static final String PROJECT_SETTINGS_REFRACTION        = "project_settings_refraction";
    public static final String PROJECT_SETTINGS_DATUM             = "project_settings_datum";
    public static final String PROJECT_SETTINGS_PROJECTION        = "project_settings_projection";
    public static final String PROJECT_SETTINGS_ZONE              = "project_settings_zone";
    public static final String PROJECT_SETTINGS_SPC_SCALE_FACTOR  = "project_settings_spc_scale_factor";
    public static final String PROJECT_SETTINGS_GEOID_MODEL       = "project_settings_geoid_model";
    public static final String PROJECT_SETTINGS_STARTING_POINT_ID = "project_settings_starting_point_id";
    public static final String PROJECT_SETTINGS_ALPHANUMERIC      = "project_settings_alphanumeric";
    public static final String PROJECT_SETTINGS_FEATURE_CODES     = "project_settings_feature_codes";
    public static final String PROJECT_SETTINGS_FC_CONTROL_FILE   = "project_settings_fc_control_file";
    public static final String PROJECT_SETTINGS_FC_TIMESTAMP      = "project_settings_fc_timestamp";


    //create project settings table
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
            KEY_CREATED_AT                   + "INTEGER, "  + ")";


    /*****************************************************/
    /***********    Point           **********************/
    /*****************************************************/

    //Point Column Names
    public static final String POINT_ID =          "point_id";
    public static final String POINT_PROJECT_ID =  "point_project_id";
    public static final String POINT_COORDINATE =  "point_coordinate";
    public static final String POINT_DESCRIPTION = "point_description";
    public static final String POINT_NOTES =       "point_notes";


    //create point table
    private static final String CREATE_TABLE_POINT = "CREATE TABLE " + TABLE_POINT +"(" +
            KEY_ID             + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            POINT_ID           + "INTEGER, " +
            POINT_PROJECT_ID   + "INTEGER, " +
            POINT_COORDINATE   + "INTEGER, " +
            POINT_DESCRIPTION  + "TEXT, "    +
            POINT_NOTES        + "TEXT, "    +
            KEY_CREATED_AT     + "INTEGER"   + ")";

    /*****************************************************/
    /********   EN Coordinates        ********************/
    /*****************************************************/

    //Coordinate EN column names
    public static final String COORDINATE_EN_PROJECT_ID = "coordinate_en_project_id";
    public static final String COORDINATE_EN_POINT_ID   = "coordinate_en_point_id";
    public static final String COORDINATE_EN_ZONE       = "coordinate_en_zone";        //1-60
    public static final String COORDINATE_EN_HEMISPHERE = "coordinate_en_hemisphere";  //N or S
    public static final String COORDINATE_EN_EASTING    = "coordinate_en_easting";
    public static final String COORDINATE_EN_NORTHING   = "coordinate_en_northing";
    public static final String COORDINATE_EN_LATBAND    = "coordinate_en_latband";
    public static final String COORDINATE_EN_DATUM      = "coordinate_en_datum";       //eg WGS84
    public static final String COORDINATE_EN_CONVERGENCE = "coordinate_en_convergence" ; //
    public static final String COORDINATE_EN_SCALE      = "coordinate_en_scale";
    public static final String COORDINATE_EN_VALID_COORDINATE = "coordinate_en_valid_coordinate";

    //create project table
    private static final String CREATE_TABLE_COORDINATE_EN = "CREATE TABLE " + TABLE_COORDINATE_EN +"(" +
            KEY_ID                  + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COORDINATE_EN_PROJECT_ID        + "INTEGER, " +
            COORDINATE_EN_POINT_ID          + "INTEGER, " +
            COORDINATE_EN_ZONE              + "INTEGER"   +
            COORDINATE_EN_HEMISPHERE        + "TEXT"      +
            COORDINATE_EN_EASTING           + "REAL"      +
            COORDINATE_EN_NORTHING          + "REAL"      +
            COORDINATE_EN_LATBAND           + "TEXT"      +
            COORDINATE_EN_DATUM             + "TEXT"      +
            COORDINATE_EN_CONVERGENCE       + "REAL"      +
            COORDINATE_EN_SCALE             + "REAL"      +
            COORDINATE_EN_VALID_COORDINATE  + "INTEGER"   +
            KEY_CREATED_AT                  + "DATETIME"  + ")";



    /*****************************************************/
    /********    LL Coordinates        *******************/
    /*****************************************************/


    protected double mTime; //time coordinate taken

    //Latitude in DD and DMS formats
    protected double mLatitude;

    protected int    mLatitudeDegree;
    protected int    mLatitudeMinute;
    protected double mLatitudeSecond;


    //Longitude in DD and DMS formats
    protected double mLongitude;

    protected int    mLongitudeDegree;
    protected int    mLongitudeMinute;
    protected double mLongitudeSecond;

    protected double mElevation; //Orthometric Elevation in Meters
    protected double mGeoid;     //Mean Sea Level in Meters

    protected boolean mValidCoordinate = true;


    //Coordinate LL column names
    public static final String COORDINATE_LL_PROJECT_ID        = "coordinate_ll_project_id";
    public static final String COORDINATE_LL_POINT_ID          = "coordinate_ll_point_id";
    public static final String COORDINATE_LL_TIME              = "coordinate_ll_time";
    public static final String COORDINATE_LL_LATITUDE         = "coordinate_ll_latitude";
    public static final String COORDINATE_LL_LATITUDE_DEGREE  = "coordinate_ll_latitude_degree";
    public static final String COORDINATE_LL_LATITUDE_MINUTE  = "coordinate_ll_latitude_minute";
    public static final String COORDINATE_LL_LATITUDE_SECOND  = "coordinate_ll_latitude_second";
    public static final String COORDINATE_LL_LONGITUDE         = "coordinate_ll_longitude";
    public static final String COORDINATE_LL_LONGITUDE_DEGREE  = "coordinate_ll_longitude_degree";
    public static final String COORDINATE_LL_LONGITUDE_MINUTE  = "coordinate_ll_longitude_minute";
    public static final String COORDINATE_LL_LONGITUDE_SECOND  = "coordinate_ll_longitude_second";
    public static final String COORDINATE_LL_ELEVATION         = "coordinate_ll_elevation" ;
    public static final String COORDINATE_LL_GEOID             = "coordinate_ll_geoid";
    public static final String COORDINATE_LL_VALID_COORDINATE  = "coordinate_ll_valid_coordinate";

    //create project table
    private static final String CREATE_TABLE_COORDINATE_LL = "CREATE TABLE " + TABLE_COORDINATE_LL +"(" +
            KEY_ID                  + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COORDINATE_LL_PROJECT_ID        + "INTEGER, " +
            COORDINATE_LL_POINT_ID          + "INTEGER, " +
            COORDINATE_LL_TIME              + "REAL"      +
            COORDINATE_LL_LATITUDE          + "REAL"      +
            COORDINATE_LL_LATITUDE_DEGREE   + "INTEGER"   +
            COORDINATE_LL_LATITUDE_MINUTE   + "INTEGER"   +
            COORDINATE_LL_LATITUDE_SECOND   + "REAL"      +
            COORDINATE_LL_LONGITUDE         + "REAL"      +
            COORDINATE_LL_LONGITUDE_DEGREE  + "INTEGER"   +
            COORDINATE_LL_LONGITUDE_MINUTE  + "INTEGER"   +
            COORDINATE_LL_LONGITUDE_SECOND  + "REAL"      +
            COORDINATE_LL_ELEVATION         + "REAL"      +
            COORDINATE_LL_GEOID             + "REAL"      +
            COORDINATE_LL_VALID_COORDINATE  + "INTEGER"   +
            KEY_CREATED_AT                  + "DATETIME"  + ")";



    /*****************************************************/
    /*****************************************************/
    /*****************************************************/







    /*****************************************************/
    /*******  Member Variables          ******************/
    /*****************************************************/



    private Context mContext;
    private SQLiteDatabase mDatabase;


    /*****************************************************/
    /*******  Constructor               ******************/
    /*****************************************************/

    //This should be called with the APPLICATION context
    public Prism4DSqliteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.mContext = context;
    }


    /*****************************************************/
    /*******  Lifecycle Methods         ******************/
    /*****************************************************/

    /******************
     * onCreate()
     * when the helper constructor is executed with a name (2nd param),
     * the platform checks if the database (second parameter) exists or not and
     * if the database exists, it gets the version information from the database file header and
     * triggers the right call back (e.g. onUpdate())
     * if the database with the name doesn't exist, the platform triggers onCreate().
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        //create the tables using the pre-defined SQL
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_PROJECT_SETTINGS);
        db.execSQL(CREATE_TABLE_POINT);
        db.execSQL(CREATE_TABLE_COORDINATE_EN);
        db.execSQL(CREATE_TABLE_COORDINATE_LL);
    }

    /******************
     * This default version of the onUpgrade() method just
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




    //The CRUD routines:
    //The same routine will do for all data types
    public void add(SQLiteDatabase db,
                    String         table,
                    String         nullColumnHack,
                    ContentValues  values){
        // Inserting Row
        db.insert(table, null, values);
        //db.close(); //never close the db instance. Just leave the connection open
    }


    //CRUD routines for a Project
    //STUBS for now

    public ArrayList<Prism4DProject> getAllProjects(){ return new ArrayList<Prism4DProject>(); }

    public Prism4DProject getProject(int projectID){
        return new Prism4DProject("StubProject", 10001);//dummy parameters for now
    }


    public  void updateProject(Prism4DProject project){}

    public void removeProject(int projectID){}



}
