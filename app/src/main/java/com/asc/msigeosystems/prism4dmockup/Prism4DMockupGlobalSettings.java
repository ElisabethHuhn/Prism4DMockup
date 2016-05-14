package com.asc.msigeosystems.prism4dmockup;

/**
 * Created by elisabethhuhn on 5/7/2016.
 */
public class Prism4DMockupGlobalSettings {

    private Prism4DMockupProjectSettings globalProjectSettings;




    private static Prism4DMockupGlobalSettings ourInstance = new Prism4DMockupGlobalSettings();


    public static Prism4DMockupGlobalSettings getInstance() {
        return ourInstance;
    }




    /****
     *
     * Constructor
     *
     */
    private Prism4DMockupGlobalSettings() {
        //Set the defaults for project settings to those supplied by the company
        globalProjectSettings.setDefaults();
    }
}
