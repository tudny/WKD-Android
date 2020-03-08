package com.tudny.wkdapp.ui.stations;

import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tudny.wkdapp.MainActivity;
import com.tudny.wkdapp.R;
import com.tudny.wkdapp.StationActivity;
import com.tudny.wkdapp.core.Station;
import com.tudny.wkdapp.location.GPSLocationListener;
import com.tudny.wkdapp.location.MobileLocationListener;
import com.tudny.wkdapp.location.NavigationManager;
import com.tudny.wkdapp.math.WKDMath;
import com.tudny.wkdapp.recycler.stationsRecycler.RowModel;
import com.tudny.wkdapp.recycler.stationsRecycler.StationRecyclerAdapter;

import java.util.ArrayList;
import java.util.Objects;

import lombok.Setter;

public class StationsFragment extends Fragment implements StationRecyclerAdapter.ItemClickListener{

	private static final String DEBUG_TAG = StationsFragment.class.getSimpleName();

	private LocationListener gpsLocationListener;
	private LocationListener mobileLocationListener;

	private Station closestStation;

	private UpdateStation updateStation;

	private StationRecyclerAdapter adapter;

	private Boolean wasFindEverClicked = Boolean.FALSE;

	@Override
	public void onResume(){
		setupUpdateStation();
		if(wasFindEverClicked) {
			findStation();
		}
		updateStation.setRunning(Boolean.TRUE);
		super.onResume();
	}

	@Override
	public void onPause(){
		updateStation.setRunning(Boolean.FALSE);
		updateStation.cancel(true);
		super.onPause();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stations, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

		setupRecyclerView();

		if(allPermissionsOk()){
			onPermissionGranted();
		} else {
			requestPermissions();
		}

		setupUpdateStation();

		final Button findStationButton = Objects.requireNonNull(getView()).findViewById(R.id.find_station_button);
		findStationButton.setOnClickListener(v -> {
			findStation();
			wasFindEverClicked = true;
		});
		final Button navigationButton = getView().findViewById(R.id.navigate_button);
		navigationButton.setOnClickListener(v -> NavigationManager.openNavigation(closestStation.getLatitude(), closestStation.getLongitude(), Objects.requireNonNull(getActivity())));
		try {
			final Button detailsButton = getView().findViewById(R.id.details_button);
			detailsButton.setOnClickListener(v -> {
				runNewStationActivity(closestStation);
			});
		} catch (Exception ignore){}
	}

	private void setupUpdateStation() {
		updateStation = new UpdateStation();
	}

	private void setupRecyclerView() {
		RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewList);
		LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		llm.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setLayoutManager(llm);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		adapter = new StationRecyclerAdapter(getActivity(), new ArrayList<RowModel>(){{
			addAll(RowModel.convertArrayListOfStationsToRowModelArrayList(Station.getArrayListOfStations()));
		}});

		adapter.setItemClickListener(this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(View view, int position) {
		Log.d(DEBUG_TAG, "Clicked item: " + position);
		runNewStationActivity(Station.getArrayListOfStations().get(position));
	}

	public static final String STATION_ID_TO_PASS = "station_id_to_pass";
	private void runNewStationActivity(Station station) {
		try {
			Intent intent = new Intent(getActivity(), StationActivity.class);
			intent.putExtra(STATION_ID_TO_PASS, station.getStationNumber());
			startActivity(intent);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private Location getLocationAny(){

		Location GPSLoc = ((GPSLocationListener) gpsLocationListener).getLastLocation();
		Location MobileLoc = ((MobileLocationListener) mobileLocationListener).getLastLocation();

		if(GPSLoc != null && MobileLoc != null) {
			Location loc = new Location("");
			loc.setLatitude((GPSLoc.getLatitude() + MobileLoc.getLatitude()) / 2);
			loc.setLongitude((GPSLoc.getLongitude() + GPSLoc.getLongitude()) / 2);
			return loc;
		} else if(GPSLoc != null){
			return GPSLoc;
		} else if(MobileLoc != null){
			return MobileLoc;
		}

		return null;
	}

	private void findStation() {

		if(!allPermissionsOk()) requestPermissions();

		if(updateStation.getStatus() == AsyncTask.Status.PENDING)
			updateStation.execute(this);
	}

	private boolean allPermissionsOk() {
		return ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}

	private void requestPermissions(){
		requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
	}

	/*@SuppressWarnings("unused")
	private void checkLocation() {
		if(!allPermissionsOk()) requestPermissions();

		Location loc = null;
		Location locM = null;

		if(gpsLocationListener != null) loc = ((GPSLocationListener) gpsLocationListener).getLastLocation();
		if(mobileLocationListener != null) locM = ((MobileLocationListener) mobileLocationListener).getLastLocation();

		System.out.println("-----------------------------------------------------");
		System.out.println("Location check: ");

		if(loc != null) System.out.println("GPS: " + loc.getLatitude() + " " + loc.getLongitude());
		if(locM != null) System.out.println("Mobile: " + locM.getLatitude() + " " + locM.getLongitude());

		if(loc != null && locM != null) System.out.println("Difference: " + WKDMath.distanceOnEarth(loc.getLatitude(), loc.getLongitude(), locM.getLatitude(), locM.getLongitude()));

		System.out.println("-----------------------------------------------------");
	}*/

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

		int sum = 0;
		for(int res : grantResults){
			if(res == PackageManager.PERMISSION_GRANTED){
				++sum;
			}
		}

		if(sum == grantResults.length){
			onPermissionGranted();
		} else {
			onPermissionNotGranted();
		}
	}

	private void onPermissionNotGranted() {
		TextView stationTextView = Objects.requireNonNull(getView()).findViewById(R.id.station_name_text_view);
		stationTextView.setText(getString(R.string.no_permission_or_signal));
	}

	private void onPermissionGranted() throws SecurityException{

		if(getActivity() == null) Log.e(DEBUG_TAG, "Fatal error with Activity!");

		LocationManager locationManagerMobile = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		LocationManager locationManagerGPS = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		gpsLocationListener = new GPSLocationListener();
		mobileLocationListener = new MobileLocationListener();

		Objects.requireNonNull(locationManagerMobile).requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mobileLocationListener);
		Objects.requireNonNull(locationManagerGPS).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
	}

	private void setStationName(Station station){
		setNavigationButtonEnable(Boolean.TRUE);
		try {
			setDetailsButtonEnabled(Boolean.TRUE);
		} catch (Exception ignored){}
		TextView stationView = Objects.requireNonNull(getView()).findViewById(R.id.station_name_text_view);
		stationView.setText(station.getStationName());
	}

	private void setNavigationButtonEnable(Boolean enabled) {
		final Button navigationButton = Objects.requireNonNull(getView()).findViewById(R.id.navigate_button);
		navigationButton.setEnabled(enabled);
		navigationButton.setText(R.string.navigate);
		navigationButton.invalidate();
	}

	private void setDetailsButtonEnabled(Boolean enabled){
		final Button detailsButton = Objects.requireNonNull(getView()).findViewById(R.id.details_button);
		detailsButton.setEnabled(enabled);
		detailsButton.setText(R.string.show_details);
		detailsButton.invalidate();
	}

	private void setNoPermissionText() {
		setNavigationButtonEnable(Boolean.FALSE);
		try {
			setDetailsButtonEnabled(Boolean.FALSE);
		} catch (Exception ignored){}
		((TextView) Objects.requireNonNull(getView()).findViewById(R.id.station_name_text_view)).setText(getString(R.string.no_permission_or_signal));
	}

	private static class UpdateStation extends AsyncTask<StationsFragment, Station, Void> {

		private final Integer DELAY = 2000;
		@Setter private Boolean running = false;
		private StationsFragment stationsFragment;

		@Override
		protected Void doInBackground(StationsFragment... stationsFragments) {

			stationsFragment = stationsFragments[0];

			while(!isCancelled()){

				if(!running) continue;

				try {
					Location loc = stationsFragment.getLocationAny();

					if (loc == null) throw new Exception("There is no location available");

					Double latitude = loc.getLatitude();
					Double longitude = loc.getLongitude();

					Station closest = null;
					Double distance = WKDMath.R * Math.PI;
					for (Station station : Station.getListOfStations()) {
						Location stationLocation = station.getLocation();
						Double dis = WKDMath.distanceOnEarth(
								latitude,
								longitude,
								stationLocation.getLatitude(),
								stationLocation.getLongitude()
						);

						if (dis < distance) {
							distance = dis;
							closest = station;
						}
					}

					stationsFragment.closestStation = closest;

					if (closest == null)
						throw new Exception("No station found. Fatal error with math or something.");

					publishProgress(stationsFragment.closestStation);

				} catch (Exception e) {
					publishProgress((Station) null);
					Log.e(DEBUG_TAG, Objects.requireNonNull(e.getMessage()));
				}


				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.e(DEBUG_TAG, "Error with ASyncTask delay | Don't bother");
				}
			}

			return null;
		}

		protected void onProgressUpdate(Station... stations){

			Log.d(DEBUG_TAG, "Updated station: " + stations[0]);

			try {
				if (stations[0] == null) {
					stationsFragment.setNoPermissionText();
				} else {
					stationsFragment.setStationName(stations[0]);
				}

			} catch (Exception e) {
				Log.e(DEBUG_TAG, e.getMessage());
			}
		}
	}
}

