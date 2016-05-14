package com.asc.msigeosystems.prism4dmockup;

/**
 * Created by elisabethhuhn on 5/13/2016.
 * This object just keeps track of what path through
 * mainenance we are on:
 * Create, Open. Copy or Delete
 */
public class Prism4DMockupPath {

    //Tags for fragment arguments
    public static String sPathTag     = "PATH";

    //Literals defining the possible paths to be taken
    public static final String sOpenTag   = "OPEN";
    public static final String sCreateTag = "CREATE";
    public static final String sCopyTag   = "COPY";
    public static final String sDeleteTag = "DELETE";

    //stores the path of this instance
    private CharSequence mPath;

    //constructor
    public Prism4DMockupPath(CharSequence path) {
        this.mPath = path;
    }

    public CharSequence getPath() {
        return mPath;
    }
}
