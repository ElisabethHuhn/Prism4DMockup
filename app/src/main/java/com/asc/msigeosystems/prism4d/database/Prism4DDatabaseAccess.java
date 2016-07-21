package com.asc.msigeosystems.prism4d.database;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;

import com.asc.msigeosystems.prism4dmockup.Prism4DProject;

import java.util.List;

/**
 * Created by elisabethhuhn on 7/13/2016.
 */
public class Prism4DDatabaseAccess extends Activity {
    /*****
     * LoadProjects (note plural)
     * creates a list of Prism4DProject's
     * from the data stored in the database
     */
    private class LoadProjects extends AsyncTask<Void, String, Cursor> {


        // String... params means an array will be passed
        // with a variable number of strings in the array
        @Override
        public Cursor doInBackground(String... params) {
            //runs on own background thread, i.e. not UI thread
            //there is no input argument as this returns all projects in the database

            //The return is a cursor that will be converted to Prism4D objects in onPostExecute
            Prism4DDatabaseManager dbManager = Prism4DDatabaseManager.getInstance();

            //returned cursor contains all projects in the database
            Cursor cursor = dbManager.getDatabase().query(
                    Prisim4DSqliteOpenHelper.TABLE_PROJECT,
                    new String[]{Prisim4DSqliteOpenHelper.PROJECT_ID,
                            Prisim4DSqliteOpenHelper.PROJECT_NAME,
                            Prisim4DSqliteOpenHelper.PROJECT_LAST_MAINTAINED,
                            Prisim4DSqliteOpenHelper.PROJECT_DESCRIPTION
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
        public void onProgressUpdate() {
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
     * LoadProject (note singular)
     * creates a list of Prism4DProject's
     * from the data stored in the database
     */
    private class LoadProject extends AsyncTask <executeArg, progressArg, resultArg> {
        protected void onPreExecute(){
            //runs on UI thread before doInBackground
        }

        protected Cursor doInBackground(int projectID) {
            //runs on own background thread, i.e. not UI thread
            Prism4DDatabaseManager dbManager = Prism4DDatabaseManager.getInstance();
            Cursor cursor = dbManager.getDatabase().execSQL(dbManager.SOME_LITERAL_WHICH_IS_SQL_STMT);

            //now turn the contents of the Cursor into a list of Prism4DProjects

            return cursor;
        }

        protected void onProgressUpdate() {
            //runs on UI thread whenever publishProgress is called from doInBackground()

        }

        protected void onPostExecute(Cursor cursor){
            //runs on UI thread after doInBackground() finishes

        }
    }
}
