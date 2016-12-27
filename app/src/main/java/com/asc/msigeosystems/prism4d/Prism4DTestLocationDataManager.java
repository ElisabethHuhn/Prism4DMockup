package com.asc.msigeosystems.prism4d;

import java.util.ArrayList;

/**
 * Created by Elisabeth Huhn on 12/7/2016.
 * Holds test locations for Collect Points
 */

public class Prism4DTestLocationDataManager {


    /************************************/
    /********* Static Variables *********/
    /************************************/
    private static Prism4DTestLocationDataManager ourInstance ;

    /************************************/
    /********* Member Variables *********/
    /************************************/
    private ArrayList<Prism4DTestLocationData> mTestDataList;



    /************************************/
    /********* Static Methods   *********/
    /************************************/
    public static Prism4DTestLocationDataManager getInstance() {
        if (ourInstance == null){
            ourInstance = new Prism4DTestLocationDataManager();

        }
        return ourInstance;
    }




    /************************************/
    /********* Constructors     *********/
    /************************************/
    private Prism4DTestLocationDataManager() {

        mTestDataList = new ArrayList<>();

        //Create the test points
        prepareTestDataset();
    }


    /************************************/
    /********* Setters/Getters  *********/
    /************************************/

    //public ArrayList<Prism4DTestLocationData> getTestDataList() { return mTestDataList; }

    public Prism4DTestLocationData get(int position){ return mTestDataList.get(position); }



    /************************************/
    /********* Instance Methods  ********/
    /************************************/
    private void prepareTestDataset(){

        Prism4DTestLocationData testData;

        //Point 0
        testData = new Prism4DTestLocationData();
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (31.85041);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-45.78961);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (422303.2210);
        testData.setEasting         (701908.4840);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);

        testData = new Prism4DTestLocationData();

        //Point 1
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (30.52389);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-39.86394);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (422262.3854);
        testData.setEasting         (702060.8845);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);


        testData = new Prism4DTestLocationData();

        //Point 3
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (26.90175);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-35.52681);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (422150.8205);
        testData.setEasting         (702172.4494);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);




        testData = new Prism4DTestLocationData();

        //Point 4
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (21.95462);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-33.94034);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (421998.4200);
        testData.setEasting         (702213.2850);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);

        testData = new Prism4DTestLocationData();

        //Point 5
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (17.00810);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-35.52951);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (421846.0195);
        testData.setEasting         (702172.4494);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);


        testData = new Prism4DTestLocationData();

        //Point 6
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (13.38757);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-39.86838);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (421734.4546);
        testData.setEasting         (702060.8845);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);



        testData = new Prism4DTestLocationData();

        //Point 7
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (12.06310);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-45.79436);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (421693.6190);
        testData.setEasting         (701908.4840);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);


        testData = new Prism4DTestLocationData();

        //Point 8
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (13.38955);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-51.71970);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (421734.4546);
        testData.setEasting         (701756.0835);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);


        testData = new Prism4DTestLocationData();

        //Point 9
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (17.01153);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-56.05683);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (421846.0195);
        testData.setEasting         (701644.5186);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);



        testData = new Prism4DTestLocationData();

        //Point 10
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (21.95858);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-57.64363);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (421998.4200);
        testData.setEasting         (701603.6830);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);



        testData = new Prism4DTestLocationData();

        //Point 11
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (26.90519);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-56.05479);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (422150.8205);
        testData.setEasting         (701644.5186);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);



        testData = new Prism4DTestLocationData();

        //Point 12
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (30.52587);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-51.71592);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (422262.3854);
        testData.setEasting         (701756.0835);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);


        testData = new Prism4DTestLocationData();

        //Point 13
        testData.setLatitudeDegrees (33);
        testData.setLatitudeMinutes (48);
        testData.setLatitudeSeconds (21.95676);
        testData.setLongitudeDegrees(-84);
        testData.setLongitudeMinutes(-8);
        testData.setLongitudeSeconds(-45.79198);
        testData.setGeoid           (-26.0);
        testData.setNorthing        (421998.4200);
        testData.setEasting         (701908.4840);
        testData.setElevation       (513.100);

        mTestDataList.add(testData);


    }


}
