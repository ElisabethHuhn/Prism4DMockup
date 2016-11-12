package com.asc.msigeosystems.prism4d;

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

    //constructor
    public Prism4DPath(CharSequence path) {
        this.mPath = path;
    }

    public CharSequence getPath()                  { return mPath; }
    public void         setPath(CharSequence path) {  mPath = path; }
}
