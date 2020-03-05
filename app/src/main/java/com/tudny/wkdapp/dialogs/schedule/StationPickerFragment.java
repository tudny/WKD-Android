package com.tudny.wkdapp.dialogs.schedule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tudny.wkdapp.core.Station;
import com.tudny.wkdapp.dialogs.schedule.listeners.StationListener;

public class StationPickerFragment extends DialogFragment {

	private static final boolean IsCANCELABLE = true;

	private Station station;
	private String title;

	private StationListener stationListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(Station.getListOfStationStringArray(), (dialog, which) -> {
			station = Station.getListOfStations()[which];
			stationListener.update(station);
			//dialog.dismiss();
			dialog.cancel();
		})
		.setTitle(title)
		.setCancelable(IsCANCELABLE);

		return builder.create();
	}

	public void setTitle(String title){
		this.title = title;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public void setStationListener(StationListener stationListener){
		this.stationListener = stationListener;
	}
}
