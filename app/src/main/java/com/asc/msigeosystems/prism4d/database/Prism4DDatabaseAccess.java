package com.asc.msigeosystems.prism4d.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

/**
 * Created by elisabethhuhn on 7/13/2016.
 */
public class Prism4DDatabaseAccess {
    /*****
     * OpenDB
     * opens the Prism4D Database
     * This involves creating the DBManager singleton and the DBHelper
     * Then, if a file exists, it is opened as the DB,
     *       if the file doesn't exsist, it is first created, then opened.
     */
    public static class OpenPrism4DDB extends AsyncTask<Context, Void, Void> {


        // String... params means an array will be passed with a
        //                        variable number of strings in the array
        @Override
        public Void doInBackground(Context... params) {
            //runs on own background thread, i.e. not UI thread
            //input parameter is the application Context

            //This creates the DBManager singleton the DBHelper, and
            //     creates/opens the DB from and preexisting DB file
            Prism4DDatabaseManager.initializeInstance(params[1]);

            //There is no return as there is nothing to do in onPostExecute
            return null;
        }


    }



    /*****
     * AddProject
     * Creates a single database record matching the passed project instance
     * If the project already exists: WHAT??
     */
    /*
    private class AddProject extends AsyncTask<ContentValues, Void, Void> {


        // String... params means an array will be passed
        // with a variable number of strings in the array
        @Override
        public Void doInBackground(ContentValues... params) {
            //runs on own background thread, i.e. not UI thread
            //the input argument is the project to be added

            // TODO: 9/5/2016 add the code to add the project from the ContentValues
            Prism4DDatabaseManager.getDatabase().insert(Prism4DSqliteOpenHelper.TABLE_PROJECT,
                    null, params[1]);

            return Void;

        }

        @Override
        public void onPreExecute(){
            //runs on UI thread before doInBackground
        }


        //now turn the contents of the Cursor into a list of Prism4DProjects
        @Override
        public void onProgressUpdate(Void... parm) {
            //runs on UI thread whenever publishProgress(progreessArgType)
            // is called from doInBackground()

        }

        //convert rows in Cursor into Prism4DProject objects,
        //then store such objects in the List<Prism4DProject>
        public void onPostExecute(Cursor cursor){
            //runs on UI thread after doInBackground() finishes


            //

        }
    }

*/


    /*****
     * GetProjects (note plural)
     * creates a list of Prism4DProject's
     * from the data stored in the database
     */
    private class GetProjects extends AsyncTask<Void, Void, Cursor> {


        // String... params means an array will be passed
        // with a variable number of strings in the array
        @Override
        public Cursor doInBackground(Void... params) {
            //runs on own background thread, i.e. not UI thread
            //there is no input argument as this returns all projects in the database

            //returned cursor contains all projects in the database
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            Cursor cursor = databaseManager.getDatabase().query(
                    Prism4DSqliteOpenHelper.TABLE_PROJECT,
                    new String[]{Prism4DSqliteOpenHelper.PROJECT_ID,
                            Prism4DSqliteOpenHelper.PROJECT_NAME,
                            Prism4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED,
                            Prism4DSqliteOpenHelper.PROJECT_DESCRIPTION
                    },
                    null, null, null, null, null, null);

            return cursor;
        }

        @Override
        public void onPreExecute(){
            //runs on UI thread before doInBackground
        }


        //now turn the contents of the Cursor into a list of Prism4DProjects
        @Override
        public void onProgressUpdate(Void... parm) {
            //runs on UI thread whenever publishProgress(progreessArgType)
            // is called from doInBackground()

        }

        //convert rows in Cursor into Prism4DProject objects,
        //then store such objects in the List<Prism4DProject>
        public void onPostExecute(Cursor cursor){
            //runs on UI thread after doInBackground() finishes


            //

        }
    }




    /*****
     * GetProject
     * Fetches the database record matching the passed project ID
     */
    private class GetProject extends AsyncTask<ContentValues, Void, Cursor> {


        // String... params means an array will be passed
        // with a variable number of strings in the array
        @Override
        public Cursor doInBackground(ContentValues... params) {
            //runs on own background thread, i.e. not UI thread
            //there is no input argument as this returns all projects in the database


            //returned cursor contains only the matching project
            Prism4DDatabaseManager databaseManager = Prism4DDatabaseManager.getInstance();
            Cursor cursor = databaseManager.getDatabase().query(
                    Prism4DSqliteOpenHelper.TABLE_PROJECT,
                    new String[]{Prism4DSqliteOpenHelper.PROJECT_ID,
                            Prism4DSqliteOpenHelper.PROJECT_NAME,
                            Prism4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED,
                            Prism4DSqliteOpenHelper.PROJECT_DESCRIPTION
                    },
                    null, null, null, null, null, null);

            return cursor;
        }

        @Override
        public void onPreExecute(){
            //runs on UI thread before doInBackground
        }


        //now turn the contents of the Cursor into a single Prism4DProject
        @Override
        public void onProgressUpdate(Void... parm) {
            //runs on UI thread whenever publishProgress(progreessArgType)
            // is called from doInBackground()

        }

        //convert rows in Cursor into Prism4DProject objects,
        //then store such objects in the List<Prism4DProject>
        public void onPostExecute(Cursor cursor){
            //runs on UI thread after doInBackground() finishes


            //

        }
    }




}
