package com.asc.msigeosystems.prism4d;

import android.os.Bundle;

/**
 * Created by Elisabeth Huhn on 5/13/2016.
 *
 * This object just keeps track of what path through
 * mainenance we are on:
 * Create, Open. Copy or Delete
 */
public class Prism4DPath {

    //Tags for fragment arguments
    public static final String sProjectPathTag = "PROJECT_PATH";
    public static final String sPointPathTag   = "POINT_PATH";

    //Literals defining the possible paths to be taken
    public static final String sOpenTag   = "OPEN";
    public static final String sCreateTag = "CREATE";
    public static final String sCopyTag   = "COPY";
    public static final String sDeleteTag = "DELETE";
    public static final String sEditTag   = "EDIT";
    public static final String sShowTag   = "SHOW";

    //stores the path of this instance
    private CharSequence mPath;

    //
    /****************************************************************/
    /*               Constructor                                 */
    /****************************************************************/
    public Prism4DPath(CharSequence path) {
        this.mPath = path;
    }

    /****************************************************************/
    /*               Static Methods                                 */
    /****************************************************************/

    public static Bundle putPathInArguments(Bundle args, Prism4DPath projectPath) {

        args.putCharSequence(Prism4DPath.sProjectPathTag, projectPath.getPath());
        return args;
    }


    public static Prism4DPath getPathFromArguments(Bundle args) {
        return new Prism4DPath(args.getCharSequence(Prism4DPath.sProjectPathTag));
    }


    /****************************************************************/
    /*              Setters and Getters                             */
    /****************************************************************/

        public CharSequence getPath()                  { return mPath; }
        public void         setPath(CharSequence path) {  mPath = path; }


    /****************************************************************/
    /*               Member Methods                                 */
    /****************************************************************/

}
