package com.asc.msigeosystems.prism4d.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Elisabeth Huhn on 7/9/2016.
 * This class makes all the actual calls to the DB
 * Thus, if there is a need to put such calls on a background thread, that
 * can be managed by the DB Manager.
 * But if it touches the DB directly, this class does it
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
    /*****************************************************/
    /*****************************************************/

    //Global Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";


    /*****************************************************/
    /**********  Tables          *************************/
    /*****************************************************/

    //Table Names
    public static final String TABLE_PROJECT =          "Project";
    public static final String TABLE_PROJECT_SETTINGS = "ProjectSettings";
    public static final String TABLE_POINT =            "Point";
    public static final String TABLE_COORDINATE_EN =    "CoordinateEN";
    public static final String TABLE_COORDINATE_LL =    "CoordinateLL";
    public static final String TABLE_GLOBAL_DATA   =    "GlobalData";


    /*****************************************************/
    /************ Project       **************************/
    /*****************************************************/

    //Project Column Names
    public static final String PROJECT_ID =              "proj_id";
    public static final String PROJECT_NAME =            "proj_name";
    public static final String PROJECT_CREATED =         "proj_created";
    public static final String PROJECT_LAST_MAINTAINED = "proj_last_maintained";
    public static final String PROJECT_DESCRIPTION =     "proj_description";
    public static final String PROJECT_COORDINATE_TYPE = "proj_coordinate_type";


    //create project table
    //NOTE: Dates are stored as long, NOT as a Date.
    //      The conversion is done when CV is created and when Cursor is translated
    private static final String CREATE_TABLE_PROJECT = "CREATE TABLE " +
            TABLE_PROJECT           + "("            +
            KEY_ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROJECT_ID              + " INTEGER, "   +
            PROJECT_NAME            + " TEXT, "      +
            PROJECT_CREATED         + " TEXT, "      +
            PROJECT_LAST_MAINTAINED + " TEXT, "      +
            PROJECT_DESCRIPTION     + " TEXT, "      +
            PROJECT_COORDINATE_TYPE + " TEXT, "      +
            KEY_CREATED_AT          + " DATETIME"    + ")";

    /*****************************************************/
    /********  Project Settings   ************************/
    /*****************************************************/

    //Project Settings Column Names
    public static final String PROJECT_SETTINGS_ID                = "projset_id";
    /*
    public static final String PROJECT_SETTINGS_DISTANCE_UNITS    = "projset_distance_units";
    public static final String PROJECT_SETTINGS_DECIMAL_DISPLAY   = "projset_decimal_display";
    public static final String PROJECT_SETTINGS_ANGLE_UNITS       = "projset_angle_units";
    public static final String PROJECT_SETTINGS_ANGLE_DISPLAY     = "projset_angle_display";
    public static final String PROJECT_SETTINGS_GRID_DIRECTION    = "projset_grid_direction";
    public static final String PROJECT_SETTINGS_SCALE_FACTOR      = "projset_scale";
    public static final String PROJECT_SETTINGS_SEA_LEVEL         = "projset_sealevel";
    public static final String PROJECT_SETTINGS_REFRACTION        = "projset_refraction";
    public static final String PROJECT_SETTINGS_DATUM             = "projset_datum";
    public static final String PROJECT_SETTINGS_PROJECTION        = "projset_projection";
    public static final String PROJECT_SETTINGS_ZONE              = "projset_zone";
    public static final String PROJECT_SETTINGS_COORDINATE_DISPLAY= "projset_coord_display";
    public static final String PROJECT_SETTINGS_SPC_SCALE_FACTOR  = "projset_spc_scale";
    public static final String PROJECT_SETTINGS_GEOID_MODEL       = "projset_geoid_model";
    public static final String PROJECT_SETTINGS_STARTING_POINT_ID = "projset_starting_point_id";
    public static final String PROJECT_SETTINGS_ALPHANUMERIC      = "projset_alphanumeric";
    public static final String PROJECT_SETTINGS_FEATURE_CODES     = "projset_feature_codes";
    public static final String PROJECT_SETTINGS_FC_CONTROL_FILE   = "projset_fc_control_file";
    public static final String PROJECT_SETTINGS_FC_TIMESTAMP      = "projset_fc_timestamp";
    */
    public static final String PROJECT_SETTINGS_MEANING_METHOD    = "projset_meaning_method";
    public static final String PROJECT_SETTINGS_MEANING_NUMBER    = "projset_meaning_number";
    public static final String PROJECT_SETTINGS_LOCATION_PRECISION = "projset_location_precision";
    public static final String PROJECT_SETTINGS_STDDEV_PRECISION  = "projset_stddev_precision";



    //create project settings table
    private static final String CREATE_TABLE_PROJECT_SETTINGS = "CREATE TABLE " +
            TABLE_PROJECT_SETTINGS           + "("           +
            KEY_ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROJECT_SETTINGS_ID              + " INTEGER, "  +
            /*
            PROJECT_SETTINGS_DISTANCE_UNITS  + " TEXT, "     +
            PROJECT_SETTINGS_DECIMAL_DISPLAY + " TEXT, "     +
            PROJECT_SETTINGS_ANGLE_UNITS     + " TEXT, "     +
            PROJECT_SETTINGS_GRID_DIRECTION  + " TEXT, "     +
            PROJECT_SETTINGS_SCALE_FACTOR    + " REAL, "     +
            PROJECT_SETTINGS_SEA_LEVEL       + " INTEGER, "  + //BOOLEAN  no-0/1-yes
            PROJECT_SETTINGS_REFRACTION      + " INTEGER, "  + //BOOLEAN  0/1
            PROJECT_SETTINGS_DATUM           + " TEXT, "     +
            PROJECT_SETTINGS_PROJECTION      + " TEXT, "     +
            PROJECT_SETTINGS_ZONE            + " TEXT, "     +
            PROJECT_SETTINGS_COORDINATE_DISPLAY + " TEXT, "  +
            PROJECT_SETTINGS_SPC_SCALE_FACTOR + " REAL, "    +
            PROJECT_SETTINGS_GEOID_MODEL     + " TEXT, "     +
            PROJECT_SETTINGS_STARTING_POINT_ID + " INTEGER, "+
            PROJECT_SETTINGS_ALPHANUMERIC    + " INTEGER, "  + //BOOLEAN  no-0/1-yes
            PROJECT_SETTINGS_FEATURE_CODES   + " TEXT, "     +
            PROJECT_SETTINGS_FC_CONTROL_FILE + " TEXT, "     +
            PROJECT_SETTINGS_FC_TIMESTAMP    + " INTEGER, "  + //BOOLEAN  no-0/1-yes
            */
            PROJECT_SETTINGS_MEANING_METHOD      + " INTEGER, "  +
            PROJECT_SETTINGS_MEANING_NUMBER      + " INTEGER, "  +
            PROJECT_SETTINGS_LOCATION_PRECISION  + " INTEGER, "  +
            PROJECT_SETTINGS_STDDEV_PRECISION    + " INTEGER, "  +

            KEY_CREATED_AT                   + " INTEGER "  + ")";


    /*****************************************************/
    /***********    Point           **********************/
    /*****************************************************/

    //Point Column Names
    public static final String POINT_ID             = "point_id";
    public static final String POINT_FOR_PROJECT_ID = "point_project_id";
    public static final String POINT_ISA_COORDINATE_ID =  "point_coordinate_ID";
    public static final String POINT_FEATURE_CODE   = "point_feature_code";
    public static final String POINT_NOTES          = "point_notes";
    public static final String POINT_HDOP           = "point_hdop";
    public static final String POINT_VDOP           = "point_vdop";
    public static final String POINT_TDOP           = "point_tdop";
    public static final String POINT_PDOP           = "point_pdop";
    public static final String POINT_HRMS           = "point_hrms";
    public static final String POINT_VRMS           = "point_vrms";



    //create point table
    private static final String CREATE_TABLE_POINT = "CREATE TABLE " +
            TABLE_POINT             + "("          +
            KEY_ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            POINT_ID                + " INTEGER, " +
            POINT_FOR_PROJECT_ID    + " INTEGER, " +
            POINT_ISA_COORDINATE_ID + " INTEGER, " +
            POINT_FEATURE_CODE      + " TEXT, "    +
            POINT_NOTES             + " TEXT, "    +
            POINT_HDOP              + " REAL, "    +
            POINT_VDOP              + " REAL, "    +
            POINT_TDOP              + " REAL, "    +
            POINT_PDOP              + " REAL, "    +
            POINT_HRMS              + " REAL, "    +
            POINT_VRMS              + " REAL, "    +
            KEY_CREATED_AT          + " INTEGER"   + ")";

    /*****************************************************/
    /********   EN Coordinates        ********************/
    /*****************************************************/

    //Coordinate EN column names
    public static final String COORDINATE_ID            = "coord_id";
    public static final String COORDINATE_PROJECT_ID    = "coord_project_id";
    public static final String COORDINATE_POINT_ID      = "coord_point_id";
    public static final String COORDINATE_TYPE          = "coord_type";
    public static final String COORDINATE_EN_EASTING    = "coord_en_easting";
    public static final String COORDINATE_EN_NORTHING   = "coord_en_northing";
    public static final String COORDINATE_EN_ELEVATION  = "coord_en_elevation";
    public static final String COORDINATE_EN_ZONE       = "coord_en_zone";        //1-60
    public static final String COORDINATE_EN_HEMISPHERE = "coord_en_hemisphere";  //N or S
    public static final String COORDINATE_EN_LATBAND    = "coord_en_latband";
    public static final String COORDINATE_EN_DATUM      = "coord_en_datum";       //eg WGS84
    public static final String COORDINATE_EN_CONVERGENCE = "coord_en_convergence" ; //
    public static final String COORDINATE_EN_SCALE      = "coord_en_scale";
    public static final String COORDINATE_EN_VALID_COORDINATE = "coord_en_valid_coordinate";


    //create project table
    private static final String CREATE_TABLE_COORDINATE_EN = "CREATE TABLE " +
            TABLE_COORDINATE_EN             + "("            +
            KEY_ID                          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COORDINATE_ID                   + " INTEGER, "   +
            COORDINATE_PROJECT_ID           + " INTEGER, "   +
            COORDINATE_POINT_ID             + " INTEGER, "   +
            COORDINATE_TYPE + " TEXT, "      +
            COORDINATE_EN_EASTING           + " REAL, "      +
            COORDINATE_EN_NORTHING          + " REAL, "      +
            COORDINATE_EN_ELEVATION         + " REAL, "      +
            COORDINATE_EN_ZONE              + " INTEGER, "   +
            COORDINATE_EN_HEMISPHERE        + " TEXT, "      +
            COORDINATE_EN_LATBAND           + " TEXT, "      +
            COORDINATE_EN_DATUM             + " TEXT, "      +
            COORDINATE_EN_CONVERGENCE       + " REAL, "      +
            COORDINATE_EN_SCALE             + " REAL, "      +
            COORDINATE_EN_VALID_COORDINATE  + " INTEGER, "   +
            KEY_CREATED_AT                  + " DATETIME"  + ")";



    /*****************************************************/
    /********    LL Coordinates        *******************/
    /*****************************************************/

    //Coordinate LL column names

    //public static final String COORDINATE_ID                 = "coord_id";
    //public static final String COORDINATE_PROJECT_ID         = "coord_project_id";
    //public static final String COORDINATE_POINT_ID           = "coord_point_id";
    //public static final String COORDINATE_TYPE               = "coord_type";
    public static final String COORDINATE_LL_TIME              = "coord_ll_time";
    public static final String COORDINATE_LL_LATITUDE          = "coord_ll_latitude";
    public static final String COORDINATE_LL_LATITUDE_DEGREE   = "coord_ll_latitude_degree";
    public static final String COORDINATE_LL_LATITUDE_MINUTE   = "coord_ll_latitude_minute";
    public static final String COORDINATE_LL_LATITUDE_SECOND   = "coord_ll_latitude_second";
    public static final String COORDINATE_LL_LONGITUDE         = "coord_ll_longitude";
    public static final String COORDINATE_LL_LONGITUDE_DEGREE  = "coord_ll_longitude_degree";
    public static final String COORDINATE_LL_LONGITUDE_MINUTE  = "coord_ll_longitude_minute";
    public static final String COORDINATE_LL_LONGITUDE_SECOND  = "coord_ll_longitude_second";
    public static final String COORDINATE_LL_ELEVATION         = "coord_ll_elevation" ;
    public static final String COORDINATE_LL_GEOID             = "coord_ll_geoid";
    public static final String COORDINATE_LL_VALID_COORDINATE  = "coord_ll_valid_coordinate";


    //create project table
    private static final String CREATE_TABLE_COORDINATE_LL = "CREATE TABLE " +
            TABLE_COORDINATE_LL             + "("          +
            KEY_ID                          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COORDINATE_ID                   + " INTEGER, " +
            COORDINATE_PROJECT_ID           + " INTEGER, " +
            COORDINATE_POINT_ID             + " INTEGER, " +
            COORDINATE_TYPE                 + " TEXT, "    +
            COORDINATE_LL_TIME              + " REAL"      +
            COORDINATE_LL_LATITUDE          + " REAL"      +
            COORDINATE_LL_LATITUDE_DEGREE   + " INTEGER"   +
            COORDINATE_LL_LATITUDE_MINUTE   + " INTEGER"   +
            COORDINATE_LL_LATITUDE_SECOND   + " REAL"      +
            COORDINATE_LL_LONGITUDE         + " REAL"      +
            COORDINATE_LL_LONGITUDE_DEGREE  + " INTEGER"   +
            COORDINATE_LL_LONGITUDE_MINUTE  + " INTEGER"   +
            COORDINATE_LL_LONGITUDE_SECOND  + " REAL"      +
            COORDINATE_LL_ELEVATION         + " REAL"      +
            COORDINATE_LL_GEOID             + " REAL"      +
            COORDINATE_LL_VALID_COORDINATE  + " INTEGER"   +
            KEY_CREATED_AT                  + " DATETIME"  + ")";



    /*****************************************************/
    /*****************************************************/
    /*****************************************************/




    /*****************************************************/
    /********   Global Data            *******************/
    /*****************************************************/

    //Column names
    public static final String GLOBAL_DATA_ID              = "global_data_id";
    public static final String GLOBAL_DATA_NEXT_PROJECT_ID = "global_data_next_project_id";

    //create project table
    private static final String CREATE_TABLE_GLOBAL_DATA = "CREATE TABLE " +
            TABLE_GLOBAL_DATA               + "("          +
            KEY_ID                          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            GLOBAL_DATA_ID                  + " INTEGER, " +
            GLOBAL_DATA_NEXT_PROJECT_ID     + " INTEGER, " +
            KEY_CREATED_AT                  + " DATETIME"  + ")";



    /*****************************************************/
    /*****************************************************/
    /*****************************************************/





    /*****************************************************/
    /*******  Member Variables          ******************/
    /*****************************************************/

    private Context mContext;


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
     * @param db The Database instance being created
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        //create the tables using the pre-defined SQL
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_PROJECT_SETTINGS);
        db.execSQL(CREATE_TABLE_POINT);
        db.execSQL(CREATE_TABLE_COORDINATE_EN);
        db.execSQL(CREATE_TABLE_COORDINATE_LL);
        db.execSQL(CREATE_TABLE_GLOBAL_DATA);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATE_EN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATE_LL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GLOBAL_DATA);

        //Create new tables
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }



    /************************************************/
    /*         Generic CRUD routines                */
    /* The same routine will do for all data types  */
    /************************************************/

    //*************************** CREATE *************************************
    public void add(SQLiteDatabase db,
                    String   table,
                    String   nullColumnHack,
                    ContentValues  values){
        // Inserting Rows
        db.insert(table, null, values);
        //db.close(); //never close the db instance. Just leave the connection open
    }

    //**************************** READ *******************************
    public Cursor getObject(SQLiteDatabase db,
                            String   table,
                            String[] columns,
                            String   where_clause,
                            String[] selectionArgs,
                            String   groupBy,
                            String   having,
                            String   orderBy){
        /********************************
         Cursor query (String table, //Table Name
                         String[] columns,   //Columns to return, null for all columns
                         String where_clause,
                         String[] selectionArgs, //replaces ? in the where_clause with these arguments
                         String groupBy, //null meanas no grouping
                         String having,   //row grouping
                         String orderBy)  //null means the default sort order
         *********************************/
        return (db.query(table, columns, where_clause, selectionArgs, groupBy, having, orderBy));
    }

    //******************************************UPDATE***************************
    //returns the number of rows affected
    public int update (SQLiteDatabase  db,
                       String         table,
                       ContentValues  cv,          //Column names and new values
                       String         where_clause,//null updates all rows
                       String[]       where_args ){ //values that replace ? in where clause
        //update  (String        table,
                // ContentValues values,
                // String        whereClause,
                // String[]      whereArgs) //Any ? in the where_clause are replaced with arguments

        return (db.update(table, cv, where_clause, where_args));
    }

    //*****************************************DELETE*********************************
    //returns the number of rows affected
    public int remove (SQLiteDatabase  db,
                       String         table,
                       String         where_clause,//null updates all rows
                       String[]       where_args ){ //values that replace ? in where clause

        //Actually, this is just a pass through to the delete method
        return (delete(db, table, where_clause, where_args));
    }


    //returns the number of rows affected
    public int delete (SQLiteDatabase  db,
                       String         table,
                       String         where_clause,//null updates all rows
                       String[]       where_args ){ //values that replace ? in where clause

        return (db.delete(table, where_clause, where_args));
    }


    /************************************************/
    /*      Object Specific CRUD routines           */
    /*     Each Class has it's own routine          */
    /*     in it's manager                          */
    /************************************************/

}
