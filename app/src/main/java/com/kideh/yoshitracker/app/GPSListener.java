package com.kideh.yoshitracker.app;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

class GPSListener implements LocationListener {
    public double dblLastLongitude = 0.000;
    public double dblLastLatitude = 0.000;

    public GPSListener() {}

    @Override
    public void onLocationChanged(Location location) {
        Double dblLongitude = location.getLongitude();
        Double dblLatitude = location.getLatitude();
        Log.d("GPSListener Longitude: ", dblLongitude.toString());
        Log.d("GPSListener Latitude: ", dblLatitude.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("onStatusChanged: ", s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("onProviderEnabled: ", s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("onProviderDisabled: ", s);
    }

    public Double getLatitude() {
        return this.dblLastLatitude;
    }

    public Double getLongitude() {
        return this.dblLastLongitude;
    }
}