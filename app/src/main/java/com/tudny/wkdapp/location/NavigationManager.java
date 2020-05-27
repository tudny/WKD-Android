package com.tudny.wkdapp.location;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class NavigationManager {

	public static final String DEBUG_TAG = NavigationManager.class.getSimpleName();

	public static void openNavigation(double latitude, double longitude, Activity activity){
		Log.d(DEBUG_TAG, "Opening Google Maps navigation on pos(" + latitude + ", " + longitude + ")");
		Uri googleMapsIntentUri = Uri.parse("google.navigation:q=" + latitude + ", " + longitude);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMapsIntentUri);
		mapIntent.setPackage("com.google.android.apps.maps");
		activity.startActivity(mapIntent);
	}
}
