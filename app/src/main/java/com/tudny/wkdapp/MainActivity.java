package com.tudny.wkdapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tudny.wkdapp.navigation.KeepStateNavigator;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

	private final static String DEBUG_TAG = MainActivity.class.getSimpleName();

	private static final String URL_WKD = "http://www.wkd.com.pl/";

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
	private static final DateTimeFormatter time12Formatter = DateTimeFormatter.ofPattern("hh:mm a");
	private static final DateTimeFormatter time24Formatter = DateTimeFormatter.ofPattern("HH:mm");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BottomNavigationView navView = findViewById(R.id.nav_view);

		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

		Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
		assert navHostFragment != null;
		navController.getNavigatorProvider().addNavigator(new KeepStateNavigator(this, navHostFragment.getChildFragmentManager(), R.id.nav_host_fragment));

		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(navView, navController);

		Log.d(DEBUG_TAG, "onCreate");
	}

	public void openNavigation(double latitude, double longitude){
		openNavigation(this, latitude, longitude);
	}

	public static void openNavigation(Activity activity, double latitude, double longitude){
		Log.d(DEBUG_TAG, "Opening Google Maps navigation on pos(" + latitude + ", " + longitude + ")");
		Uri googleMapsIntentUri = Uri.parse("google.navigation:q=" + latitude + ", " + longitude);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMapsIntentUri);
		mapIntent.setPackage("com.google.android.apps.maps");
		activity.startActivity(mapIntent);
	}

	public DateTimeFormatter getDateFormatter() {
		return dateFormatter;
	}

	public DateTimeFormatter getTimeFormatter() {
		if (DateFormat.is24HourFormat(this)) {
			return time24Formatter;
		} else {
			return time12Formatter;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.top_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.nav_host_fragment));
	}


	public static void setGoodTitle(Fragment fragment, Integer stringId) {
		try {
			Objects.requireNonNull(((MainActivity) fragment.requireActivity()).getSupportActionBar()).setTitle(fragment.getString(stringId));
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}
}
