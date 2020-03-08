package com.tudny.wkdapp;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.tudny.wkdapp.core.Station;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

	public static final String DEBUG_TAG = SettingsActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.settings, new SettingsFragment())
				.commit();
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

		SharedPreferences sharedPreferences;

		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			setPreferencesFromResource(R.xml.root_preferences, rootKey);

			fillDynamicStationData(R.string.default_base_key);
			fillDynamicStationData(R.string.default_target_key);

			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

			onSharedPreferenceChanged(sharedPreferences, getString(R.string.default_base_key));
			onSharedPreferenceChanged(sharedPreferences, getString(R.string.default_target_key));
		}

		private void fillDynamicStationData(int id) {
			ListPreference listPreference = findPreference(getString(id));
			if(listPreference != null){
				ArrayList<Station> stations = Station.getArrayListOfStations();
				CharSequence[] entries = new String[stations.size()];
				CharSequence[] entryValues = new String[stations.size()];
				stations.forEach(station -> {
					entries[stations.indexOf(station)] = station.getStationName();
					entryValues[stations.indexOf(station)] = station.getStationNumber().toString();
				});
				listPreference.setEntries(entries);
				listPreference.setEntryValues(entryValues);
			}
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			Preference preference = findPreference(key);

			if(preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
				if(prefIndex >= 0) {
					preference.setSummary(listPreference.getEntries()[prefIndex]);
				}
			}
		}

		@Override
		public void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		}
	}
}