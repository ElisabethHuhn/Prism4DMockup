package com.asc.msigeosystems.prism4d;

import android.content.ContentValues;

import com.asc.msigeosystems.prism4d.database.Prism4DSqliteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisabethhuhn on 5/18/2016.
 */
public class Prism4DProjectManager {

    private static List<Prism4DProject> sProjectList;

    private static Prism4DProjectManager ourInstance ;

    public static Prism4DProjectManager getInstance() {
        if (ourInstance == null){
            ourInstance = new Prism4DProjectManager();

        }
        return ourInstance;
    }



    private Prism4DProjectManager() {

        sProjectList = new ArrayList<>();

        //This is where we would read the list from the database
        //// TODO: 8/28/2016   get the list of projects from the DB

        // TODO: 8/28/2016 create project data if none exists
        //but for now, just make up some data
        prepareProjectDataset();
    }


    public List<Prism4DProject> getProjects() {return sProjectList;}


    //Get the project matching the passed project ID
    //from the projects stored in the database
    // TODO: 8/28/2016 write database access for a given single project 
    public Prism4DProject getProject(int projectID) {
        for (Prism4DProject project : sProjectList){
            if (project.getProjectID() == projectID){
                return project;
            }
        }
        return null;
    }

    public Prism4DProject getFirstProject() {

        return sProjectList.get(0);

        /*****
         *  //todo improve this method
        for (Prism4DProject project : sProjectList){
            //Return the first project in the list
            return project;
        }
        return null;
         ************/
    }

    public void add (Prism4DProject project){
        sProjectList.add(project);
    }

    public void addProjectCV (Prism4DProject project){
        //convert the Prism4DProject object into a ContentValues object containing a project
        getProjectCV(project);
        //then store the ContentValues


    }

    public ContentValues getProjectCV (Prism4DProject project){
        //convert the Prism4DProject object into a ContentValues object containing a project
        ContentValues cvProject = new ContentValues();
        //put(columnName, value);
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_ID,              project.getProjectID());
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_NAME,            project.getProjectName().toString());
        cvProject.put(Prism4DSqliteOpenHelper.KEY_CREATED_AT,          project.getProjectDateCreated().toString());
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED, project.getProjectLastModified().toString());
        cvProject.put(Prism4DSqliteOpenHelper.PROJECT_DESCRIPTION,     project.getProjectDescription().toString());

        return cvProject;
    }



        //Mock up some projects for now
    private void prepareProjectDataset(){
        //no use doing anything if the Adapter is not created yet


        Prism4DProject project = new Prism4DProject("Cambridge Subdivision", 1001);
        sProjectList.add(project);

        project = new Prism4DProject("Jones Creek",   1002);
        sProjectList.add(project);

        project = new Prism4DProject("Hampton South", 1003);
        sProjectList.add(project);

        project = new Prism4DProject("Johns Creek",   1004);
        sProjectList.add(project);

        project = new Prism4DProject("Macon Airport", 1005);
        sProjectList.add(project);

        project = new Prism4DProject("MSI Demo",      1006);
        sProjectList.add(project);

        project = new Prism4DProject("Roswell",       1007);
        sProjectList.add(project);

        project = new Prism4DProject("Cambridge Subdivision 2", 1008);
        sProjectList.add(project);

        project = new Prism4DProject("Jones Creek 2",   1009);
        sProjectList.add(project);

        project = new Prism4DProject("Hampton South 2", 1010);
        sProjectList.add(project);

        project = new Prism4DProject("Johns Creek 2",   1011);
        sProjectList.add(project);

        project = new Prism4DProject("Macon Airport 2", 1012);
        sProjectList.add(project);

        project = new Prism4DProject("MSI Demo 2",      1013);
        sProjectList.add(project);

        project = new Prism4DProject("Roswell 2",       1014);
        sProjectList.add(project);

        project = new Prism4DProject("Cambridge Subdivision 3", 1015);
        sProjectList.add(project);

        project = new Prism4DProject("Jones Creek 3",   1016);
        sProjectList.add(project);

        project = new Prism4DProject("Hampton South 3", 1017);
        sProjectList.add(project);

        project = new Prism4DProject("Johns Creek 3",   1018);
        sProjectList.add(project);

        project = new Prism4DProject("Macon Airport 3", 1019);
        sProjectList.add(project);

        project = new Prism4DProject("MSI Demo 3",      1020);
        sProjectList.add(project);

        project = new Prism4DProject("Roswell 3",       1021);
        sProjectList.add(project);



    }

}
