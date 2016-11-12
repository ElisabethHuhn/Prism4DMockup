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
    private int globalDataID;
    private int nextProjectID;

    /*****************************************************/
    /********    Attributes stored in the DB     *********/
    /*****************************************************/

    /*************************************/
    /*         Static Methods            */
    /*************************************/


    /*************************************/
    /*         CONSTRUCTORS              */

    /*************************************/


    /*****************************************************/
    /********    Constructors                    *********/
    /*****************************************************/


    /*****************************************************/
    /********    Setters and Getters             *********/
    /*****************************************************/

    public int  getGlobalDataID()                  {return globalDataID; }
    public void setGlobalDataID(int globalDataID) {this.globalDataID = globalDataID;}


    public int  getNextProjectID() {
        int returnCode = nextProjectID;
        nextProjectID++; //increment for next use
        //write it out to the DB
        Prism4DGlobalDataManager globalDataManager = Prism4DGlobalDataManager.getInstance();
        globalDataManager.updateGlobalData(this);

        return returnCode;
    }

    public int getPotentialNextID(){
        return nextProjectID;
    }

    //This routine is only used by the DB Manager, so we don't get into an infinite loop
    public int  getForDBNextProjectID ()            {return nextProjectID;}
    public void setNextProjectID(int nextProjectID) { this.nextProjectID = nextProjectID; }




    /*****************************************************/
    /********    Private Member Methods          *********/
    /*****************************************************/


}
