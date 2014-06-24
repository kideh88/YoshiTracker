package com.kideh.yoshitracker.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.telephony.SmsManager;
import android.util.Log;

public class MoveSensorListener implements SensorEventListener {

    private Float mAccel;        // acceleration apart from gravity
    private Float mAccelCurrent; // current acceleration including gravity
    private Float mAccelLast;    // last acceleration including gravity
    private int intHitsOnBox = 0;

    private static int YOSHI_HIT_LIMIT = 20;

    public MoveSensorListener(Float accel, Float accelCurrent, Float accelLast) {
        mAccel = accel;
        mAccelCurrent = accelCurrent;
        mAccelLast = accelLast;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        if(mAccel > 0.60f || mAccel < -0.60f) {
            addTouchAlerts();
        }
        Log.d("mAccel: ", mAccel.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void addTouchAlerts() {
        if(intHitsOnBox > YOSHI_HIT_LIMIT) {
            alertBySMS();
            intHitsOnBox = 0;
        }
        else {
            intHitsOnBox += 1;
        }
    }


    private void alertBySMS() {
        String phoneNo = "23370288";
        String message = "YoshiBox Hit Alert!!";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}
