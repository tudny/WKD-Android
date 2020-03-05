package com.tudny.wkdapp.dialogs.tickets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tudny.wkdapp.dialogs.tickets.listeners.PeriodListener;
import com.tudny.wkdapp.tickets.WKDTickets;

public class PeriodUnnamedFragment extends DialogFragment {

	private static final boolean IsCANCELABLE = true;

	private WKDTickets.Period period;
	private String title;

	private PeriodListener periodListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(WKDTickets.Period.getArrayOfPeriodsUnNamedNames(), (dialog, which) -> {
			period = WKDTickets.Period.getArrayOfPeriodsUnNamed()[which];
			periodListener.update(period);
			dialog.cancel();
		})
				.setTitle(title)
				.setCancelable(IsCANCELABLE);

		return builder.create();
	}

	public void setTitle(String title){
		this.title = title;
	}

	public WKDTickets.Period getPeriod() {
		return period;
	}

	public void setPeriod(WKDTickets.Period period) {
		this.period = period;
	}

	public void setPeriodListener(PeriodListener periodListener) {
		this.periodListener = periodListener;
	}
}
