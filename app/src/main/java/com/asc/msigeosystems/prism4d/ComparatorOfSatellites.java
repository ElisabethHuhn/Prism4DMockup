package com.asc.msigeosystems.prism4d;

import android.annotation.SuppressLint;

import java.util.Comparator;

/**
 * Created by Elisabeth Huhn on 11/28/2016.
 */

public class ComparatorOfSatellites implements Comparator<Prism4DSatellite> {
    //Constructor
    public ComparatorOfSatellites(){}

    //the one required method
    public int compare(Prism4DSatellite satellite1, Prism4DSatellite satellite2) {
        //returns negative integer if satellite1 < satellite2
        //                    zero if satellite1 = satellite2
        //        positive integer if satellite1 > satellite2

        int i1 = satellite1.getSatelliteID();
        int i2 = satellite2.getSatelliteID();
        if (i1 < i2) return -1;
        if (i1 == i2)return 0;
        if (i1 > i2) return 1;

        return 1;
    }
}
