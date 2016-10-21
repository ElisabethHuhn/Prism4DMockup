package com.asc.msigeosystems.prism4d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisabethhuhn on 5/18/2016.
 */
public class Prism4DPointsManager {

    private static List<Prism4DPoint> sPointList;

    private static Prism4DPointsManager ourInstance ;

    public static Prism4DPointsManager getInstance() {
        if (ourInstance == null){
            ourInstance = new Prism4DPointsManager();
        }
        return ourInstance;
    }



    private Prism4DPointsManager() {

        sPointList = new ArrayList<>();

        //This is where we would read the list from the database
        //but for now, just make up some data
        preparePointDataset();
    }


    public List<Prism4DPoint> getPoints() {
        return sPointList;
    }


    //returns a list of points for just this project
    public List<Prism4DPoint> getProjectPointsList(int projectID){

        List<Prism4DPoint> projectPointsList = new ArrayList<>();

        //iterate through the point list, counting the points with the project ID
        Prism4DPoint point;
        int size = 0;
        for (int i = 0; i < sPointList.size(); i++) {

            point = sPointList.get(i);
            if ((point != null) &&
                    ((point.getProjectID()) == projectID)) {
                projectPointsList.add(point);

            }
        }
        return projectPointsList;
    }



    public Prism4DPoint getPoint(int projectID, int pointID) {
        for (Prism4DPoint point : sPointList){
            if (point.getProjectID() == projectID){
                if (point.getPointID() == pointID) {
                    return point;
                }
            }
        }
        return null;
    }



    public boolean add(Prism4DPoint newPoint){
        return sPointList.add(newPoint);
    }



    //returns the number of points of the indicated project
    public int getSize(int projectID){
        //iterate through the point list, counting the points with the project ID
        Prism4DPoint point;
        int size = 0;
        int pointProjectID;
        for (int i = 0; i < sPointList.size(); i++) {

            point = sPointList.get(i);
            pointProjectID = point.getProjectID();
            if ((point != null) &&
               ((point.getProjectID()) == projectID)) {
                size++;

            }
        }
        return size;
    }

    //removes the indicated point
    public boolean remove(Prism4DPoint point){
        return sPointList.remove(point);
    }

    //copies all the points from one project to another proejct
    public void copyProjectPoints(int fromProjectID, int toProjectID){
        //iterate through the point list, counting the points with the project ID
        Prism4DPoint fromPoint;
        Prism4DPoint toPoint;
        int pointProjectID;
        Prism4DProjectManager projectsManager = Prism4DProjectManager.getInstance();

        List<Prism4DProject> projects = projectsManager.getProjects();
        Prism4DProject fromProject = projectsManager.getProject(fromProjectID);
        Prism4DProject toProject   = projectsManager.getProject(toProjectID);

        for (int i = 0; i < sPointList.size(); i++) {

            fromPoint = sPointList.get(i);

            pointProjectID = fromPoint.getProjectID();
            if ((pointProjectID == fromProjectID)) {
                //create a new point and copy over all the fields
                toPoint = new Prism4DPoint(toProject);
                toPoint.setPointID(fromPoint.getPointID());
                toPoint.setPointEasting(fromPoint.getPointEasting());
                toPoint.setPointNorthing(fromPoint.getPointNorthing());
                toPoint.setPointElevation(fromPoint.getPointElevation());
                toPoint.setPointDescription(fromPoint.getPointDescription());
                toPoint.setPointNotes(fromPoint.getPointNotes());
                sPointList.add(toPoint);

            }
        }
    }


    //Mock up some points for now
    private void preparePointDataset() {
        //Points get created for projects
        //get the singleton container
        Prism4DProjectManager projectManager = Prism4DProjectManager.getInstance();
        List<Prism4DProject> projectList = projectManager.getProjects();
        Prism4DProject project;

        //for each project, create some dummy points
        for (int i = 0; i < projectList.size(); i++) {
            project = projectList.get(i);
            makeSomePoints(project);
        }
        if (projectList.size() == 0) {
            //create at least one dummy project
            int projectID = 2001;

            project = new Prism4DProject("Boston Subdivision", projectID);
            projectList.add(project);
            makeSomePoints(project);
            //todo need to throw an exception here
        }

    }



    private void makeSomePoints(Prism4DProject project){

        // add some points for the indicated project
        //Stone Mountain
        Prism4DPoint point = new Prism4DPoint(project);
        point.setPointEasting(764195.051);
        point.setPointNorthing(3744319.729);
        point.setPointElevation(5.3);
        point.setPointDescription("Stone Mountain");
        sPointList.add(point);

        //Beuford
        point = new Prism4DPoint(project);
        point.setPointEasting(775612.280);
        point.setPointNorthing(3776838.145);
        point.setPointElevation(5.3);
        point.setPointDescription("Beauford");
        sPointList.add(point);

        //Canton
        point = new Prism4DPoint(project);
        point.setPointEasting(736973.849);
        point.setPointNorthing(3799564.248);
        point.setPointElevation(5.3);
        point.setPointDescription("A Canton");
        sPointList.add(point);


        //Canton
        point = new Prism4DPoint(project);
        point.setPointEasting(737391.296);
        point.setPointNorthing(3800010.792);
        point.setPointElevation(5.3);
        point.setPointDescription("B Canton");
        sPointList.add(point);



    }

}
