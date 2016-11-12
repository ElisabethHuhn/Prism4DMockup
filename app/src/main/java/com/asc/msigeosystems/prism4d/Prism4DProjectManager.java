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

        // TODO: 8/28/2016 Remember to delete the canned data before release
        //but for now, just make up some data
        //prepareProjectDataset();
    }


    /************************************/
    /********* Setters/Getters  *********/
    /************************************/

    /*******************************************/
    /*********     CRUD Methods        *********/
    /*******************************************/


    //******************  CREATE *******************************************


    //This routine not only adds to the in memory list, but also to the DB
    public void add(Prism4DProject newProject){

        if (mProjectList == null){
            mProjectList = new ArrayList<>();
        }

        //determine whether the project already exists in the list
        int position = getProjectPosition(newProject.getProjectID());
        if (position == PROJECT_NOT_FOUND) {
            addProject (newProject, true);
        } else {
            updateProject (newProject, position, true);
        }

    }//end public add()

    //This routine ONLY adds to in memory list. It's coming from the DB
    public void addFromDB(Prism4DProject newProject){
        //determine if already in list
        if (mProjectList == null){
            mProjectList = new ArrayList<>();
        }

        //determine whether the project already exists
        int position = getProjectPosition(newProject.getProjectID());
        if (position == PROJECT_NOT_FOUND) {
            addProject (newProject, false);
            //Need to check if any points exist in the DB belonging to this project
            Prism4DPointManager pointManager = Prism4DPointManager.getInstance();
            pointManager.getPointsFromDB(newProject);
        } else {
            updateProject (newProject, position, false);
        }
    }//end public add()


    //The routine that actually adds the instance to in memory list and
    // potentially (third boolean parameter) to the DB
    private void addProject(Prism4DProject newProject, boolean addToDBToo){
        mProjectList.add(newProject);

        if (addToDBToo){

            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            databaseManager.addProject(newProject);

            //also have to deal with any Points on the project
            //add any points to the DB
            //The ASSUMPTION is that if it didn't exist in memory, it doesn't exist in the DB
            //This is perhaps a risky assumption.......
            // TODO: 11/2/2016 Determine if add project assumption is too risky

            ArrayList<Prism4DPoint> points = newProject.getPoints();
            if (points != null){
                for (int position = 0; position < points.size(); position++) {
                    databaseManager.addPoint(points.get(position));
                }

            }
        }

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
        int atPosition = getProjectPosition(projectID);

        if (atPosition == PROJECT_NOT_FOUND) {

            //attempt to read the DB before giving up
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            Prism4DProject project = databaseManager.getProject(projectID);

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


    //returns the position of the project instance that matches the argument projectEmailAddr
    //returns constant = PROJECT_NOT_FOUND if the project is not in the list
    //NOTE it is a RunTimeException to call this routine if the list is null or empty
    private int getProjectPosition(int projectID){
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



    private void updateProject(Prism4DProject newProject, int atPosition, boolean addToDBToo){
        Prism4DProject listProject = mProjectList.get(atPosition);

        //update the list instance with the attributes from the new project being added
        //        don't copy the ID
        copyProjectAttributes (newProject, listProject, false);

        if (addToDBToo) {
            // update the project already in the DB
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            databaseManager.updateProject(newProject);

            //also have to deal with any Points on the project
            //add any points to the DB
            //The ASSUMPTION is that if it didn't exist in memory, it doesn't exist in the DB
            //This is perhaps a risky assumption.......
            // TODO: 11/2/2016 Determine if add project assumption is too risky

            ArrayList<Prism4DPoint> points = newProject.getPoints();
            if (points != null){
                for (int position = 0; position < points.size(); position++) {
                    databaseManager.updatePoint(points.get(position));
                }

            }

        }
    }



    //******************  DELETE *******************************************


    //This routine not only removes from the in-memory list, but also from the DB
    public boolean removeProject(int position) {
        if (position > mProjectList.size()) {
            //Can't remove a position that the list isn't long enough for
            return false;
        }

        mProjectList.remove(position);
        return true;
    }//end public remove position



    /********************************************/
    /********* Translation Utility Methods  *****/
    /********************************************/

    public void copyProjectAttributes(Prism4DProject fromProject, Prism4DProject toProject, boolean copyID){
        if (copyID){
            toProject.setProjectID(fromProject.getProjectID());
        }
        toProject.setProjectName        (fromProject.getProjectName());
        toProject.setProjectDateCreated (fromProject.getProjectDateCreated());
        toProject.setProjectLastModified(fromProject.getProjectLastModified());
        toProject.setProjectDescription (fromProject.getProjectDescription());
    }


    //returns the ContentValues object needed to add/update the PROJECT to/in the DB
    public ContentValues getCVFromProject(Prism4DProject project){
        //convert the Prism4DProject object into a ContentValues object containing a project
        ContentValues cvProject = new ContentValues();
        //put(columnName, value);
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_ID,              project.getProjectID());
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_NAME,            project.getProjectName().toString());

        // TODO: 11/3/2016 Check the date conversions are correct
        //long millisecondDate = project.getProjectDateCreated().getTime();
        //The two lines that are Date objects in memory, become milliseconds in the DB
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_CREATED,         project.getProjectDateCreated().getTime());
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED, project.getProjectLastModified().getTime());

        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_DESCRIPTION,     project.getProjectDescription().toString());

        return cvProject;
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
        project.setProjectID           (cursor.getInt  (cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_ID)));
        project.setProjectName         (cursor.getString(cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_NAME)));
        
        //Need to convert int into Date format
        //DB stores date in milliseconds. Must convert this format to Date
        // TODO: 11/3/2016 convert milliseconds from the cursor into Date in the project
        long millisecondsDate = cursor.getInt(cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_CREATED));
        Date theDate = new Date(millisecondsDate);
        project.setProjectDateCreated  (theDate);
        
        millisecondsDate = cursor.getInt(cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED));
        theDate = new Date(millisecondsDate);
        project.setProjectLastModified (theDate);
        
        project.setProjectDescription  (cursor.getString(cursor.getColumnIndex(Prism4DSqliteOpenHelper.PROJECT_DESCRIPTION)));


        return project;
    }



    /********************************************/
    /********* General Utility Methods      *****/
    /********************************************/

    //Mock up some projects for now
    private void prepareProjectDataset(){
        //no use doing anything if the Adapter is not created yet

        if (mProjectList.size() > 0){
            return;
        }

        Prism4DPointManager pointManager = Prism4DPointManager.getInstance();

        Prism4DProject project = new Prism4DProject("Cambridge Subdivision", 1001);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Jones Creek",   1002);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Hampton South", 1003);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Johns Creek",   1004);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Macon Airport", 1005);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("MSI Demo",      1006);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Roswell",       1007);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Cambridge Subdivision 2", 1008);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Jones Creek 2",   1009);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Hampton South 2", 1010);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Johns Creek 2",   1011);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Macon Airport 2", 1012);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("MSI Demo 2",      1013);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Roswell 2",       1014);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Cambridge Subdivision 3", 1015);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Jones Creek 3",   1016);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Hampton South 3", 1017);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Johns Creek 3",   1018);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Macon Airport 3", 1019);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("MSI Demo 3",      1020);
        add(project);
        pointManager.addSomePoints(project);

        project = new Prism4DProject("Roswell 3",       1021);
        add(project);
        pointManager.addSomePoints(project);



    }

}
