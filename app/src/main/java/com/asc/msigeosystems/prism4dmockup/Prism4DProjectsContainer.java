package com.asc.msigeosystems.prism4dmockup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisabethhuhn on 5/18/2016.
 */
public class Prism4DProjectsContainer {

    private static List<Prism4DProject> sProjectList;

    private static Prism4DProjectsContainer ourInstance ;

    public static Prism4DProjectsContainer getInstance() {
        if (ourInstance == null){
            ourInstance = new Prism4DProjectsContainer();
        }
        return ourInstance;
    }



    private Prism4DProjectsContainer() {

        sProjectList = new ArrayList<>();

        //This is where we would read the list from the database
        //but for now, just make up some data
        prepareProjectDataset();
    }


    public List<Prism4DProject> getProjects() {
        return sProjectList;
    }


    public Prism4DProject getProject(int projectID) {
        for (Prism4DProject project : sProjectList){
            if (project.getProjectID() == projectID){
                return project;
            }
        }
        return null;
    }

    public Prism4DProject getFirstProject() {
        //todo improve this method
        for (Prism4DProject project : sProjectList){
            //Return the first project in the list
            return project;
        }
        return null;
    }

    public void add (Prism4DProject project){
        sProjectList.add(project);
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



    }

}
