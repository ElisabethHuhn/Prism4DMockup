package com.asc.msigeosystems.prism4d;

/**
 * This class will hold Data that must persist but has no
 * clear class to live on. For example: information on next ID
 *
 * Created by Elisabeth Huhn on 11/7/2016.
 */

public class Prism4DGlobalData {

    /*****************************************************/
    /********    Static constants                *********/
    /*****************************************************/
    public static final int DefaultFirstProjectID = 1000;


    /*************************************/
    /*    Static (class) Variables       */
    /*************************************/


    /*************************************/
    /*    Member (instance) Variables    */
    /*************************************/
    private int mGlobalDataID;
    private int mNextProjectID;



    /*****************************************************/
    /********    Attributes stored in the DB     *********/
    /*****************************************************/

    /*************************************/
    /*         Static Methods            */
    /*************************************/
    //generate a guaranteed unique ID
    public static int getUniqueID(){
        return (int) (System.currentTimeMillis() & 0xfffffff);
    }



    /*************************************/
    /*         CONSTRUCTORS              */
    /*************************************/
    public Prism4DGlobalData() {
        this.mGlobalDataID = getUniqueID();
    }
/*****************************************************/
    /********    Constructors                    *********/
    /*****************************************************/


    /*****************************************************/
    /********    Setters and Getters             *********/
    /*****************************************************/

    public int  getGlobalDataID()                  {return mGlobalDataID; }
    public void setGlobalDataID(int globalDataID) {this.mGlobalDataID = globalDataID;}


    public int  getNextProjectID() {
        int returnCode = mNextProjectID;
        mNextProjectID++; //increment for next use
        //write it out to the DB
        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
        globalDataManager.updateGlobalData(this);

        return returnCode;
    }

    public int getPotentialNextID(){
        return mNextProjectID;
    }

    //This routine is only used by the DB Manager, so we don't get into an infinite loop
    public int  getForDBNextProjectID ()            {return mNextProjectID;}
    public void setNextProjectID(int nextProjectID) { this.mNextProjectID = nextProjectID; }




    /*****************************************************/
    /********    Private Member Methods          *********/
    /*****************************************************/


}
