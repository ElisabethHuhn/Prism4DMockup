package com.asc.msigeosystems.prism4d;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.asc.msigeosystems.prism4dmockup.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Created by Elisabeth Huhn on 11/19/2016.
 *
 * Code from internet that's been modified for Prism4D
 */

public class MainPrism4DCompassActivity extends Activity implements SensorEventListener, LocationListener {

    /*************************************************************************/
    /*                Static Variables                                       */
    /*************************************************************************/
    public static final String NA = "N/A";
    public static final String FIXED = "FIXED";
    // location min time
    private static final int LOCATION_MIN_TIME = 30 * 1000;
    // location min distance
    private static final int LOCATION_MIN_DISTANCE = 10;


    /*************************************************************************/
    /*                Member Variables                                       */
    /*************************************************************************/

    // Gravity for accelerometer data
    private float[] gravity = new float[3];
    // magnetic data
    private float[] geomagnetic = new float[3];
    // Rotation data
    private float[] rotation = new float[9];
    // orientation (azimuth, pitch, roll)
    private float[] orientation = new float[3];
    // smoothed values
    private float[] smoothed = new float[3];

    // sensor manager
    private SensorManager sensorManager;
    // sensor gravity
    private Sensor sensorGravity;
    private Sensor sensorMagnetic;

    private LocationManager locationManager;
    private Location currentLocation;

    private GeomagneticField geomagneticField;
    private double bearing = 0;

    private TextView textDirection, textLat, textLong;

    //The Custom View for the compass itself
    private Prism4DCompassRoseView compassView;


    /*************************************************************************/
    /*                Lifecycle Methods                                      */
    /*************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //wire up the widgets
        setContentView(R.layout.fragment_compass_rose);
        textLat = (TextView) findViewById(R.id.compass_latitude);
        textLong = (TextView) findViewById(R.id.compass_longitude);
        textDirection = (TextView) findViewById(R.id.compass_label);
        compassView = (Prism4DCompassRoseView) findViewById(R.id.compass_rose);

        // keep screen light on (wake lock light)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //set up the sensors
        sensorManager  = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorGravity  = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // listen to these sensors
        sensorManager.registerListener(this, sensorGravity,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagnetic,SensorManager.SENSOR_DELAY_NORMAL);

        //Make sure we have the proper GPS permissions before starting
        //If we don't currently have permission, bail
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            getFalseLocation();
            return;
        }

        //get the location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //request the location data
         locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                                                LOCATION_MIN_TIME,
                                                                LOCATION_MIN_DISTANCE,
                                                                this);

        // get last known position from gps
        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //if we can't get the location from gps, least try the network position
        // if we can't even get a network location, put in an obviously false position
        if (gpsLocation != null) {
            //the gps location is good
            currentLocation = gpsLocation;
        } else {
            // try with network provider
            Location networkLocation =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (networkLocation != null) {
                currentLocation = networkLocation;
            } else {
                // Fix a position
                getFalseLocation();
            }

            // set current location
            onLocationChanged(currentLocation);
        }
    }

    private Location getFalseLocation() {
        Location location = new Location(FIXED);
        location.setAltitude(1);
        location.setLatitude(0);
        location.setLongitude(0);
        return location;
    }


    @Override
    protected void onStop() {
        super.onStop();
        // remove listeners
        sensorManager.unregisterListener(this, sensorGravity);
        sensorManager.unregisterListener(this, sensorMagnetic);

        //We can't even stop the updates if we don't have the proper permissions

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){return;}
        locationManager.removeUpdates(this);
    }


    /*************************************************************************/
    /*               Sensor Callbacks                                        */
    /*************************************************************************/
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        // used to update location info on screen
        updateLocation(location);
        geomagneticField = new GeomagneticField((float) currentLocation.getLatitude(),
                                                (float) currentLocation.getLongitude(),
                                                (float) currentLocation.getAltitude(),
                                                System.currentTimeMillis());
    }

    private void updateLocation(Location location) {
        if (FIXED.equals(location.getProvider())) {
            textLat.setText(NA);
            textLong.setText(NA);
        } else {

            // better => make this creation outside method
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            NumberFormat formatter = new DecimalFormat("#0.00", dfs);
            CharSequence temp = "Lat : " + formatter.format(location.getLatitude());
            textLat.setText(temp);
            temp = "Long : " + formatter.format(location.getLongitude());
            textLong.setText(temp);
        }
    }

    /*************************************************************************/
    /*          GPS Status Callbacks stubs only                              */
    /*************************************************************************/
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    /*************************************************************************/
    /*          Magnetic or Accelerometer Sensor Callback                    */
    /*************************************************************************/
    /*         When the magnetic sensor changes, rotate the rose             */
    /*************************************************************************/
    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean accelOrMagnetic = false;

        // get accelerometer data
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // we need to use a low pass lowPassFilter to make data smoothed
            smoothed = lowPassFilter(event.values, gravity);
            gravity[0] = smoothed[0];
            gravity[1] = smoothed[1];
            gravity[2] = smoothed[2];
            accelOrMagnetic = true;

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            smoothed = lowPassFilter(event.values, geomagnetic);
            geomagnetic[0] = smoothed[0];
            geomagnetic[1] = smoothed[1];
            geomagnetic[2] = smoothed[2];
            accelOrMagnetic = true;

        }

        // get rotation matrix to get gravity and magnetic data
        SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic);
        // get bearing to target
        SensorManager.getOrientation(rotation, orientation);
        // east degrees of true North
        bearing = orientation[0];
        // convert from radians to degrees
        bearing = Math.toDegrees(bearing);

        // fix difference between true North and magnetic North
        if (geomagneticField != null) {
            bearing += geomagneticField.getDeclination();
        }

        // bearing must be in 0-360
        if (bearing < 0) {
            bearing += 360;
        }

        // update compass view
        compassView.setBearing((float) bearing);

        if (accelOrMagnetic) {
            compassView.postInvalidate();
        }

        updateTextDirection(bearing); // display text direction on screen
    }

    /*************************************************************************/
    /*          Utilities                                                    */
    /*************************************************************************/

    private float[] lowPassFilter(float[] eventValues, float[] pastValues ) {
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = 0.8f;
        float [] linear_acceleration = new float[3];

        pastValues[0] = alpha * pastValues[0] + (1 - alpha) * eventValues[0];
        pastValues[1] = alpha * pastValues[1] + (1 - alpha) * eventValues[1];
        pastValues[2] = alpha * pastValues[2] + (1 - alpha) * eventValues[2];

        linear_acceleration[0] = eventValues[0] - pastValues[0];
        linear_acceleration[1] = eventValues[1] - pastValues[1];
        linear_acceleration[2] = eventValues[2] - pastValues[2];

        return linear_acceleration;
    }

    private void updateTextDirection(double bearing) {
        int range = (int) (bearing / (360f / 16f));
        String dirTxt = "";

        if (range == 15 || range == 0)
            dirTxt = "N";
        if (range == 1 || range == 2)
            dirTxt = "NE";
        if (range == 3 || range == 4)
            dirTxt = "E";
        if (range == 5 || range == 6)
            dirTxt = "SE";
        if (range == 7 || range == 8)
            dirTxt = "S";
        if (range == 9 || range == 10)
            dirTxt = "SW";
        if (range == 11 || range == 12)
            dirTxt = "W";
        if (range == 13 || range == 14)
            dirTxt = "NW";

        textDirection.setText("" + ((int) bearing) + ((char) 176) + " "
                + dirTxt); // char 176 ) = degrees ...
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
                && accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            // manage fact that compass data are unreliable ...
            // toast ? display on screen ?
            Toast.makeText(this, "Compass data is unreliable", Toast.LENGTH_SHORT).show();
        }
    }
}

