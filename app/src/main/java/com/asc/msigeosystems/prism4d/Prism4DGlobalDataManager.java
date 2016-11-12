package com.asc.msigeosystems.prism4d;

import android.content.ContentValues;
import android.database.Cursor;

import com.asc.msigeosystems.prism4d.database.Prism4DDatabaseManager;
import com.asc.msigeosystems.prism4d.database.Prism4DSqliteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elisabeth Huhn on 11/10/2016.
 *
 *
 * The class in charge of maintaining the set of instances of Point
 *  both in-memory and in the DB
 *
 */
public class Prism4DGlobalDataManager {
    /************************************/
    /********* Static Constants  ********/
    /************************************/
    private static final int GLOBAL_DATA_NOT_FOUND = -1;


    /************************************/
    /********* Static Variables  ********/
    /************************************/
   private static Prism4DGlobalDataManager ourInstance ;


    /**************************************/
    /********* Member Variables   *********/
    /**************************************/
    private ArrayList<Prism4DGlobalData> mGlobalDataList;



    /************************************/
    /********* Static Methods   *********/
    /************************************/
    public static Prism4DGlobalDataManager getInstance() {
        if (ourInstance == null){
            //Get the data from the DB

            //If it doesn't exist in the DB, then generate a new instance from scratch
            ourInstance = new Prism4DGlobalDataManager();
        }
        return ourInstance;
    }


    /************************************/
    /********* Constructors     *********/
    /************************************/
    private Prism4DGlobalDataManager() {
        mGlobalDataList = new ArrayList<>();
        //Read in the Global Data from the DB
        //Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        //databaseManager.getGlobalData();
    }



    /*****************************************************/
    /********    Setters and Getters             *********/
    /*****************************************************/


    /*******************************************/
    /********* Public Member Methods   *********/
    /*******************************************/



    /*******************************************/
    /*********     CRUD Methods        *********/
    /*******************************************/

    //***********************  SIZE **************************************



    //***********************  CREATE **************************************


    //***********************  READ **************************************

    public Prism4DGlobalData getGlobalData(){
        Prism4DGlobalData globalData = null;
        //First try memory
        if (mGlobalDataList == null){
            mGlobalDataList = new ArrayList<>();
        }

        if (mGlobalDataList.size() > 0) {
            globalData = mGlobalDataList.get(0);
        }

        if (globalData == null){
            //then try DB
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            globalData = databaseManager.getGlobalData();

            if (globalData != null) {
                //we found it in the DB
                mGlobalDataList.add(globalData);
            } else {
                //It wasn't in the DB, so start from scratch
                globalData = new Prism4DGlobalData();
                globalData.setNextProjectID(Prism4DGlobalData.DefaultFirstProjectID);
                //and add it back to memory
                mGlobalDataList.add(globalData);

                //and put it in the DB
                databaseManager.addGlobalData(globalData);
            }
        }
        return globalData;
    }


    //***********************  UPDATE **************************************
    //This is called every time a piece of global data changes
    public void updateGlobalData(Prism4DGlobalData globalData){
        Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
        databaseManager.updateGlobalData(globalData);

    }

    //***********************  DELETE **************************************




    /********************************************/
    /*********  Member Methods   *********/
    /********************************************/


    //***********************  COPY **************************************


    /********************************************/
    /****  Translation utility Methods   ********/
    /********************************************/



    public ContentValues getGlobalDataCV(Prism4DGlobalData globalData){
        ContentValues values = new ContentValues();
        values.put(Prism4DSqliteOpenHelper.GLOBAL_DATA_ID,               globalData.getGlobalDataID());
        values.put(Prism4DSqliteOpenHelper.GLOBAL_DATA_NEXT_PROJECT_ID,   globalData.getForDBNextProjectID());

        return values;
    }

    //returns the Prism4DGlobalData characterized by the position within the Cursor
    //returns null if the position is larger than the size of the Cursor
    //NOTE    this routine does NOT add the globalData to the list maintained by this GlobalDataManager
    //        The caller of this routine is responsible for that.
    //        This is only a translation utility
    //WARNING As the app is not multi-threaded, this routine is not synchronized.
    //        If the app becomes multi-threaded, this routine must be made thread safe
    //WARNING The cursor is NOT closed by this routine. It assumes the caller will close the
    //         cursor when it is done with it
    public Prism4DGlobalData getGlobalDataFromCursor(Cursor cursor, int position){

        int last = cursor.getCount();
        if (position >= last) return null;

        Prism4DGlobalData globalData = new Prism4DGlobalData(); //filled with defaults

        cursor.moveToPosition(position);
        globalData.setGlobalDataID  (cursor.getInt   (cursor.getColumnIndex(Prism4DSqliteOpenHelper.GLOBAL_DATA_ID)));
        globalData.setNextProjectID (cursor.getInt   (cursor.getColumnIndex(Prism4DSqliteOpenHelper.GLOBAL_DATA_NEXT_PROJECT_ID)));

        return globalData;
    }



}
