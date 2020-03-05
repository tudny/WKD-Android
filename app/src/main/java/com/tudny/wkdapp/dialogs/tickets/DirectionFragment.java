package com.tudny.wkdapp.dialogs.tickets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tudny.wkdapp.dialogs.tickets.listeners.DirectionListener;
import com.tudny.wkdapp.tickets.WKDTickets;

public class DirectionFragment extends DialogFragment {

	private static final String DEBUG_TAG = DirectionFragment.class.getSimpleName();

	private static final boolean IsCANCELABLE = true;

	private WKDTickets.Direction direction;
	private String title;

	private DirectionListener directionListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);

		Log.d(DEBUG_TAG, "Creating dialog.");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(WKDTickets.Direction.getArrayOfDirectionNames(), (dialog, which) -> {
			direction = WKDTickets.Direction.getArrayOfDirections()[which];
			directionListener.update(direction);
			dialog.cancel();
		})
		.setTitle(title)
		.setCancelable(IsCANCELABLE);

		return builder.create();
	}

	public void setTitle(String title){
		this.title = title;
	}

	public WKDTickets.Direction getDirection() {
		return direction;
	}

	public void setDirection(WKDTickets.Direction direction) {
		this.direction = direction;
	}

	public void setDirectionListener(DirectionListener directionListener) {
		this.directionListener = directionListener;
	}
}
