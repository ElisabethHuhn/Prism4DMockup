package com.asc.msigeosystems.prism4dmockup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisabethhuhn on 5/18/2016.
 */
public class Prism4DSatelliteContainer {

    private static List<Prism4DSatellite> sSatelliteList;

    private static Prism4DSatelliteContainer ourInstance ;

    public static Prism4DSatelliteContainer getInstance() {
        if (ourInstance == null){
            ourInstance = new Prism4DSatelliteContainer();
        }
        return ourInstance;
    }



    private Prism4DSatelliteContainer() {

        sSatelliteList = new ArrayList<>();

        //This is where we would read the list from the database
        //but for now, just make up some data
        prepareSatelliteDataset();
    }


    public List<Prism4DSatellite> getSatellites() {
        return sSatelliteList;
    }


    public Prism4DSatellite getSatellite(int satelliteID) {
        for (Prism4DSatellite satellite : sSatelliteList){
            if (satellite.getSatelliteID() == satelliteID){
                return satellite;
            }
        }
        return null;
    }


    //add a satellite to the list,
    // if it isn't already there, add it
    // if it was previously in the list,
    //       remove the old version before adding the new one
    public void add (Prism4DSatellite satellite) {
        //if the given satellite is already in the list, remove the old one
        boolean foundIt = true;
        //remove any old satellites with this id from the list
        while (foundIt = removeSat(satellite)){};

        sSatelliteList.add(satellite);

    }

    //returns true if match found and removed from the list
    public boolean removeSat (Prism4DSatellite satellite) {
        Prism4DSatellite satellite1;
        int size = 0;

        for (int i = 0; i < sSatelliteList.size(); i++) {

            satellite1 = sSatelliteList.get(i);

            if ((satellite1 != null) &&
                (satellite1.getSatelliteID() == satellite.getSatelliteID())) {
                sSatelliteList.remove(i);
                return true;

            }
        }
        return false;
    }


    //Mock up some satellites for now
    private void prepareSatelliteDataset(){
        //format of the constructor
        //Prism4DSatellite(int satelliteID, int elevation, int azimuth, int snr)

        Prism4DSatellite satellite = new Prism4DSatellite(01,47,261,20);
        sSatelliteList.add(satellite);

        satellite = new Prism4DSatellite(03,07,036,14);
        sSatelliteList.add(satellite);

        satellite = new Prism4DSatellite(06,69,000,23);
        sSatelliteList.add(satellite);

        satellite = new Prism4DSatellite(12,29,316,18);
        sSatelliteList.add(satellite);

        satellite = new Prism4DSatellite(17,43,075,23);
        sSatelliteList.add(satellite);

        satellite = new Prism4DSatellite(19,59,040,27);
        sSatelliteList.add(satellite);







    }

}
