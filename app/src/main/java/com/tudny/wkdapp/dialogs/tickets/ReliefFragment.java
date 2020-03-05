package com.tudny.wkdapp.dialogs.tickets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tudny.wkdapp.dialogs.tickets.listeners.ReliefListener;
import com.tudny.wkdapp.tickets.WKDTickets;

public class ReliefFragment extends DialogFragment {

	private static final boolean IsCANCELABLE = true;

	private WKDTickets.Relief relief;
	private String title;

	private ReliefListener reliefListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(WKDTickets.Relief.getArrayOfFullNames(), (dialog, which) -> {
			relief = WKDTickets.Relief.getArrayOfReliefs()[which];
			reliefListener.update(relief);
			dialog.cancel();
		})
		.setTitle(title)
		.setCancelable(IsCANCELABLE);

		return builder.create();
	}

	public void setTitle(String title){
		this.title = title;
	}

	public WKDTickets.Relief getRelief() {
		return relief;
	}

	public void setRelief(WKDTickets.Relief relief) {
		this.relief = relief;
	}

	public void setReliefListener(ReliefListener reliefListener) {
		this.reliefListener = reliefListener;
	}
}
