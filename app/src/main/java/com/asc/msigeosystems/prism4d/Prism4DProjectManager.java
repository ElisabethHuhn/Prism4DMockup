package com.asc.msigeosystems.prism4d;

import android.content.ContentValues;
import android.database.Cursor;

import com.asc.msigeosystems.prism4d.database.Prism4DDatabaseManager;
import com.asc.msigeosystems.prism4d.database.Prism4DSqliteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Elisabeth Huhn on 5/18/2016.
 *
 * This manager hides the fact that the Project objects are mirrored in a DB
 * If a Project is not found in memory, the DB is queried for it.
 * If a Project is added to memory,     it will also be added to the DB
 * If a Project is updated in memory,   it is also updated in teh DB
 * If a Project is deleted from memory, it is also deleted from the DB
 */
public class Prism4DProjectManager {

    /************************************/
    /********* Static Constants *********/
    /************************************/
    public static final int PROJECT_NOT_FOUND = -1;


    /************************************/
    /********* Static Variables *********/
    /************************************/
    private static Prism4DProjectManager ourInstance ;

    /************************************/
    /********* Member Variables *********/
    /************************************/
    private ArrayList<Prism4DProject> mProjectList;


    /************************************/
    /********* Static Methods   *********/
    /************************************/
    public static Prism4DProjectManager getInstance() {
        if (ourInstance == null){
            ourInstance = new Prism4DProjectManager();

        }
        return ourInstance;
    }




    /************************************/
    /********* Constructors     *********/
    /************************************/
    private Prism4DProjectManager() {

        mProjectList = new ArrayList<>();

        //The DB isn't read in until the first time a project is accessed

    }


    /************************************/
    /********* Setters/Getters  *********/
    /************************************/

    /*******************************************/
    /*********     CRUD Methods        *********/
    /*******************************************/


    //******************  CREATE *******************************************


    //This routine not only adds to the in memory list, but also to the DB
    //If the project is NOT already in memory list, it is added, else it is updated
    //It is added/updated in the DB regardless
    public void add(Prism4DProject newProject){

        if (mProjectList == null){
            mProjectList = new ArrayList<>();
        }

        //determine whether the project already exists in the list
        int position = findProjectPosition(newProject.getProjectID());
        if (position == PROJECT_NOT_FOUND) {
            addProject (newProject, true);
        } else {
            updateProject (newProject, position, true);
        }

    }//end public add()

    //This routine ONLY adds to in memory list. It's coming from the DB
    public void addFromDB(Prism4DProject newProject){
        //determine if list exists yet
        if (mProjectList == null){
            mProjectList = new ArrayList<>();
        }

        //determine whether the project already exists
        int position = findProjectPosition(newProject.getProjectID());
        if (position == PROJECT_NOT_FOUND) {
            addProject (newProject, false);
            //Need to check if any points exist in the DB belonging to this project
            Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
            pointManager.getPointsFromDB(newProject);
        } else {
            updateProject (newProject, position, false);
        }
    }//end public addFromDB()


    //The routine that actually adds the instance to in memory list and
    // potentially (third boolean parameter) to the DB
    private boolean addProject(Prism4DProject newProject, boolean addToDBToo){
        //Add project to memory list
        if (!mProjectList.add(newProject)){
            return false;
        }

        if (addToDBToo){

            //add project to DB
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            if (!databaseManager.addProject(newProject)) return false;
        }

        //Now deal with any points on the project
        ArrayList<Prism4DPoint> points = newProject.getPoints();
        if (points != null){
            Prism4DPointManager pointManager = Prism4DPointManager.getInstance();

            for (int position = 0; position < points.size(); position++) {
                if (!pointManager.addToProject(newProject, points.get(position), addToDBToo)){
                    return false;
                }
            }

        }

        return true;
    }


    //******************  READ *******************************************

    public ArrayList<Prism4DProject> getProjectList() {
        if (mProjectList == null){
            mProjectList = new ArrayList<>();
        }
        if (mProjectList.size() == 0){
            //get the Projects from the DB
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            databaseManager.getAllProjects();
        }
        return mProjectList;
    }


    //Get the project matching the passed project ID
    //from the projects stored in the database
    public Prism4DProject getProject(int projectID) {
        int atPosition = findProjectPosition(projectID);

        if (atPosition == PROJECT_NOT_FOUND) {

            //attempt to read the DB before giving up
            Prism4DProject project = getProjectFromDB(projectID);

            if (project != null) {
                //if a matching project was in the DB, add it to RAM
                mProjectList.add(project);

                //and go get the points for this person
                Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
                pointManager.getPointsFromDB(project);
            }
            return project;
        }
        return (mProjectList.get(atPosition));
    }

    //Returns null if it's not in the DB
    public Prism4DProject getProjectFromDB (int projectID){
        //Ignore the in memory list, just go straight to the DB
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        return databaseManager.getProject(projectID);
    }


    //returns the position of the project instance that matches the argument project
    //returns constant = PROJECT_NOT_FOUND if the project is not in the list
    //NOTE it is a RunTimeException to call this routine if the list is null or empty
    private int findProjectPosition(int projectID){
        Prism4DProject project;
        int position        = 0;
        int last            = mProjectList.size();

        //Determine whether an instance of the project is already in the list
        //NOTE that if list is empty, while doesn't loop even once
        while (position < last){
            project = mProjectList.get(position);

            if (project.getProjectID() == projectID){
                //Found the project in the list at this position
                return position;
            }
            position++;
        }
        return PROJECT_NOT_FOUND;
    }


    //******************  UPDATE *******************************************

    //This routine not only replaces in the in memory list, but also in the DB
    public void update(Prism4DProject project){
        //The update functionality already exists in add
        //    as a Project can only appear once
        add(project);
    }//end public add()


    //This routine  only replaces in the in memory list
    public void updateFromDB(Prism4DProject project){
        //The update functionality already exists in add
        //    as a Project can only appear once
        addFromDB(project);
    }//end public add()



    private void updateProject(Prism4DProject fromProject, int atPosition, boolean addToDBToo){
        boolean assignNextID = false;
        //deep copies the project settings as well as the project
        Prism4DProject toProject = deepCopyProject(fromProject, assignNextID);

        // TODO: 12/25/2016 have to remove the points and coordinates as well
        removeProject(atPosition);
       // mProjectList.remove(atPosition);
        mProjectList.add(atPosition, toProject);

        if (addToDBToo) {
            // update the project adn project Settinsg already in the DB
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            databaseManager.updateProject(fromProject);

            //also have to deal with any Points on the project
            //add any points to the DB
            //The ASSUMPTION is that if it didn't exist in memory, it doesn't exist in the DB
            //This is perhaps a risky assumption.......
            // TODO: 11/2/2016 Determine if add project assumption is too risky

            ArrayList<Prism4DPoint> points = fromProject.getPoints();
            if (points != null){
                for (int position = 0; position < points.size(); position++) {
                    databaseManager.updatePoint(points.get(position));
                }

            }

        }
    }



    //******************  DELETE *******************************************


    //This routine not only removes from the in-memory list, but also from the DB
    //This routine removes all traces of a project: from menory and the DB
    //It takes care of the project, its points, and the points coordinates
    //return code only indicates whether the project existed in the first place, not
    //if it was removed successfully. That is rather rashly assumed
    public boolean removeProject(int position) {
        if (position > mProjectList.size()) {
            //Can't remove a position that the list isn't long enough for
            return false;
        }
        Prism4DProject project = mProjectList.get(position);
        //get rid of all project objects in the DB
        removeProjectFromDB(project.getProjectID());

        //Clear all points from memory
        ArrayList<Prism4DPoint> points = project.getPoints();
        points.clear();

        //remove the project itself from management
        mProjectList.remove(position);
        return true;
    }//end public remove position


    //Returns null if it's not in the DB
    public void removeProjectFromDB (int projectID){

        //first remove the coordinates and the points
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        pointManager.removeProjectPointsFromDB(projectID);

        //then remove project settings
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        databaseManager.removeProjectSettings(projectID);

        //finally, remove the project itself
        databaseManager.removeProject(projectID);
    }


    /********************************************/
    /********* Translation Utility Methods  *****/
    /********************************************/

    public Prism4DProject deepCopyProject(Prism4DProject fromProject, boolean assignNextID){

        //This method of deep copy only works if the most current copy of the
        //project exists in the DB.
        //This assumption is often valid, as in ListProjects. The only way a project
        //  can appear in the UI List is if is the most current copy of the object

        //do a deep copy of the project by reading in the
        //DB version of the project
        //This also brings in the ProjectSettings associated with the project
        Prism4DProject toProject = getProjectFromDB(fromProject.getProjectID());

        int toProjectID;
        if (assignNextID) {
            //set the new project ID on the toProject and on its Settings
            toProjectID = Prism4DProject.getNextProjectID();
            toProject.setProjectID(toProjectID);
            toProject.getSettings().setProjectID(toProjectID);
        } else {
            toProjectID = fromProject.getProjectID();
        }

           //update the date created and date last maintained
        toProject.setProjectDateCreated(new Date().getTime());
        toProject.setProjectLastModified(new Date().getTime());

        //the coordinate type is OK as read in from the DB, it does not need to be touched

        //do a deep copy of the points on this project is to read them in from the DB
        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
        //This not only reads in the points from the DB, it adds those points to
        //    the pointsList on the project
        //The return value is not what is important here, it's the side effect of reading
        //    all the points in from the DB and adding them to memory
        int numbPoints = pointManager.getPointsFromDB(toProject);

        //change the pointIDs and the projectID's on the points of the project
        Prism4DPoint toPoint;
        ArrayList<Prism4DPoint> toPointList   = toProject.getPoints();

        //initialize the first point ID
        toProject.initializePointID();

        //iterate through the point list, changing the IDs
        int last = toPointList.size();
        for (int position = 0; position < last; position++) {

            //set the forProjectID on the point to the new projectID
            toPoint = toPointList.get(position);
            toPoint.setForProjectID(toProjectID);

            //and assign a new point id to this point
            toPoint.setPointID(toProject.getNextPointID());

            //add the new point to the list on the project AND to the db
            //pointManager.addToProject(toProject, toPoint, true);

            //  give the coordinate on the point a new ID
            int toCoordinateID = Prism4DCoordinate.getNextCoordinateID();
            toPoint.getCoordinate().setCoordinateID(toCoordinateID);
            toPoint.setHasACoordinateID(toCoordinateID);

        }
        return toProject;
    }


    //returns the ContentValues object needed to add/update the PROJECT to/in the DB
    public ContentValues getCVFromProject(Prism4DProject project){
        //convert the Prism4DProject object into a ContentValues object containing a project
        ContentValues cvProject = new ContentValues();
        //put(columnName, value);
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_ID,project.getProjectID());
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_NAME,project.getProjectName().toString());
        //CONVERT the dates to strings in the DB.
        // They will be converted back to milliseconds in memory
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_CREATED,
                                                String.valueOf(project.getProjectDateCreated()));
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED,
                                                String.valueOf(project.getProjectLastModified()));
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_DESCRIPTION,
                                                project.getProjectDescription().toString());
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_COORDINATE_TYPE,
                                                project.getProjectCoordinateType().toString());

        return cvProject;
    }


    //returns the ContentValues object needed to add/update the PROJECT SETTINGS to/in the DB
    public ContentValues getCVFromProjectSettings(Prism4DProject project){
        Prism4DProjectSettings projectSettings = project.getSettings();
        if (projectSettings == null){
            //use default settings if there are none
            projectSettings = new Prism4DProjectSettings();
            //the two objects must point at each other
            projectSettings.setProjectID(project.getProjectID());
            project.setSettings(projectSettings);
        }
        //convert the Prism4DProject object into a ContentValues object containing a project
        ContentValues cvProjectSettings = new ContentValues();
        //put(columnName, value);
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ID,
                                                    projectSettings.getProjectID());
        /*
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_DISTANCE_UNITS,
                                                    projectSettings.getDistanceUnits().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_DECIMAL_DISPLAY,
                                                    projectSettings.getDecimalDisplay().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ANGLE_UNITS,
                                                    projectSettings.getAngleUnits().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ANGLE_DISPLAY,
                                                    projectSettings.getAngleDisplay().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_GRID_DIRECTION,
                                                    projectSettings.getGridDirection().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_SCALE_FACTOR,
                                                    projectSettings.getScaleFactor());
        int temp = 0;//set default to no
        if (projectSettings.isSeaLevel())temp = 1;
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_SEA_LEVEL, temp);

        temp = 0;//set default to no
        if (projectSettings.isRefraction())temp = 1;
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_REFRACTION,temp);

        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_DATUM,
                                                    projectSettings.getDatum().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_PROJECTION,
                                                    projectSettings.getProjection().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ZONE,
                                                    projectSettings.getZone().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_COORDINATE_DISPLAY,
                                                    projectSettings.getCoordinateDisplay().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_GEOID_MODEL,
                                                    projectSettings.getGeoidModel().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_STARTING_POINT_ID,
                                                    projectSettings.getStartingPointID().toString());
        temp = 0;//set default to no
        if (projectSettings.isAlphanumeric())temp = 1;
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ALPHANUMERIC, temp);

        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_FEATURE_CODES,
                                                    projectSettings.getFeatureCodes().toString());
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_FC_CONTROL_FILE,
                                                    projectSettings.getFCControlFile().toString());
        temp = 0;//set default to no
        if (projectSettings.isFCTimeStamp())temp = 1;
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_FC_TIMESTAMP,temp);
*/
        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_MEANING_METHOD,
                projectSettings.getProjectID());

        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_MEANING_NUMBER,
                projectSettings.getProjectID());

        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_MEANING_METHOD,
                projectSettings.getProjectID());

        cvProjectSettings.put(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_MEANING_METHOD,
                projectSettings.getProjectID());


        return cvProjectSettings;
    }


    //returns the Prism4DProject characterized by the position within the Cursor
    //returns null if the position is larger than the size of the Cursor
    //NOTE    this routine does NOT add the project to the list maintained by this ProjectManager
    //        The caller of this routine is responsible for that.
    //        This is only a translation utility
    //WARNING As the app is not multi-threaded, this routine is not synchronized.
    //        If the app becomes multi-threaded, this routine must be made thread safe
    //WARNING The cursor is NOT closed by this routine. It assumes the caller will close the
    //         cursor when it is done with it
    public Prism4DProject getProjectFromCursor(Cursor cursor, int position){

        int last = cursor.getCount();
        if (position >= last) return null;

        Prism4DProject project = new Prism4DProject(); //filled with defaults, no ID is assigned

        cursor.moveToPosition(position);
        project.setProjectID(
                cursor.getInt  (cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_ID)));
        project.setProjectName(
                cursor.getString(cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_NAME)));
        project.setProjectDateCreated(Long.parseLong(cursor.getString(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_CREATED))));
        project.setProjectLastModified(Long.parseLong(cursor.getString(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED))));
        project.setProjectDescription(cursor.getString(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_DESCRIPTION)));
        project.setProjectCoordinateType(cursor.getString(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_COORDINATE_TYPE)));

        return project;
    }

    //totally analogous to same function for project.
    // Whenever project is fetched frmo DB, so is Project Settings
    public Prism4DProjectSettings getProjectSettingsFromCursor(Cursor cursor, int position){

        int last = cursor.getCount();
        if (position >= last) return null;

        //filled with defaults, no ID is assigned
        Prism4DProjectSettings projectSettings = new Prism4DProjectSettings();

        cursor.moveToPosition(position);
        projectSettings.setProjectID(cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ID)));
/*
        projectSettings.setDistanceUnits (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_DISTANCE_UNITS)));
        projectSettings.setDecimalDisplay (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_DECIMAL_DISPLAY)));
        projectSettings.setAngleUnits (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ANGLE_UNITS)));
        projectSettings.setAngleDisplay (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ANGLE_DISPLAY)));
        projectSettings.setGridDirection (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_GRID_DIRECTION)));

        projectSettings.setScaleFactor (cursor.getDouble(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_SCALE_FACTOR)));


        int temp = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_SEA_LEVEL));
        boolean tempBoolean = false;
        if (temp == 1)tempBoolean = true;
        projectSettings.setSeaLevel(tempBoolean);

        temp = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_REFRACTION));
        tempBoolean = false;
        if (temp == 1)tempBoolean = true;
        projectSettings.setRefraction(tempBoolean);

        projectSettings.setDatum (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_DATUM)));
        projectSettings.setProjection (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_PROJECTION)));
        projectSettings.setZone (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ZONE)));
        projectSettings.setCoordinateDisplay (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_COORDINATE_DISPLAY)));
        projectSettings.setGeoidModel (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_GEOID_MODEL)));
        projectSettings.setStartingPointID (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_STARTING_POINT_ID)));


        temp = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_ALPHANUMERIC));
        tempBoolean = false;
        if (temp == 1)tempBoolean = true;
        projectSettings.setAlphanumeric(tempBoolean);

        projectSettings.setFeatureCodes (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_FEATURE_CODES)));
        projectSettings.setFCControlFile (cursor.getString (
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_FC_CONTROL_FILE)));


        temp = cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_FC_TIMESTAMP));
        tempBoolean = false;
        if (temp == 1)tempBoolean = true;
        projectSettings.setFCTimeStamp(tempBoolean);
*/
        projectSettings.setProjectID(cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_MEANING_METHOD)));


        projectSettings.setProjectID(cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_MEANING_NUMBER)));


        projectSettings.setProjectID(cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_LOCATION_PRECISION)));


        projectSettings.setProjectID(cursor.getInt(
                cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_SETTINGS_STDDEV_PRECISION)));



        return projectSettings;
    }



    /********************************************/
    /********* General Utility Methods      *****/
    /********************************************/


}
