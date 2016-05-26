package com.asc.msigeosystems.prism4dmockup;

/**
 * Created by elisabethhuhn on 5/7/2016.
 */
public class Prism4DGlobalSettings {

    private Prism4DProjectSettings globalProjectSettings;




    private static Prism4DGlobalSettings ourInstance = new Prism4DGlobalSettings();


    public static Prism4DGlobalSettings getInstance() {
        return ourInstance;
    }




    /****
     *
     * Constructor
     *
     */
    private Prism4DGlobalSettings() {
        //Set the defaults for project settings to those supplied by the company
        globalProjectSettings.setDefaults();
    }
}
