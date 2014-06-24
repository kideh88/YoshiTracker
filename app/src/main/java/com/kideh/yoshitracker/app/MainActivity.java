package com.kideh.yoshitracker.app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity {

    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = (float)0.2;

    GPSListener customListener;
    SensorEventListener customSensorListener;
    Context mContext;
    Sensor mAccelerometer;
    SensorManager mSensorManager;


    private boolean blnSensorIsOn = false;
    private long intMinUpdateTime = 20000;
    private Double dblLastKnowLat = 0.000;
    private Double dblLastKnowLong = 0.000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        customListener = new GPSListener();

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isGPSEnabled) {
            Toast.makeText(this, "GPS is active!", Toast.LENGTH_LONG).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    intMinUpdateTime,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, customListener);
            Location lastLocation = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(null != lastLocation) {
                dblLastKnowLat = lastLocation.getLatitude();
                dblLastKnowLong = lastLocation.getLongitude();
            }
        }
        else {
            Toast.makeText(this, "GPS is NOT active!", Toast.LENGTH_LONG).show();
        }

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Float mAccel = 0.00f;
        Float mAccelCurrent = SensorManager.GRAVITY_EARTH;
        Float mAccelLast = SensorManager.GRAVITY_EARTH;
        customSensorListener = new MoveSensorListener(mAccel, mAccelCurrent, mAccelLast);

    }




    public void clickLocation(View view) {
        try{
            dblLastKnowLat = customListener.getLatitude();
            dblLastKnowLong = customListener.getLongitude();

            TextView txtLatitude = (TextView) findViewById(R.id.textLocationLatitude);
            txtLatitude.setText(dblLastKnowLat.toString());

            TextView txtLongitude = (TextView) findViewById(R.id.textLocationLongitude);
            txtLongitude.setText(dblLastKnowLong.toString());
        }
        catch (Exception e) {
            Log.d("clickLocation: ", e.getMessage().toString());
        }
    }

    public void clickToggleSensor(View view) {
        Button btnToggleSensor = (Button) findViewById(R.id.btnToggleSensor);
        if(blnSensorIsOn) {
            mSensorManager.unregisterListener(customSensorListener, mAccelerometer);
            blnSensorIsOn = false;
            btnToggleSensor.setText("Turn Sensor ON");
        }
        else {
            mSensorManager.registerListener(customSensorListener, mAccelerometer, 100000000);
            blnSensorIsOn = true;
            btnToggleSensor.setText("Turn Sensor OFF");
        }
    }


    public void clickSendSms(View view) {
        String phoneNo = "23370288";
        String message = "Test Message";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
