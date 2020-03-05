package com.tudny.wkdapp.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class GPSLocationListener implements LocationListener {

	private static final String DEBUG_TAG = GPSLocationListener.class.getSimpleName();

	private Location lastLocation;

	public Location getLastLocation() {
		return lastLocation;
	}

	public GPSLocationListener(){
	}

	@Override
	public void onLocationChanged(Location location) {
		lastLocation = location;
		// It was pissing me off
		// Log.d(DEBUG_TAG, "Location changed: " + location.getLatitude() + " " + location.getLongitude());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(DEBUG_TAG,"Status CHANGED");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(DEBUG_TAG,"Status ENABLED");
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(DEBUG_TAG,"Status DISABLED");
	}
}
