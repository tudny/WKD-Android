package com.tudny.wkdapp.dialogs.tickets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tudny.wkdapp.dialogs.tickets.listeners.TicketTypeListener;
import com.tudny.wkdapp.tickets.WKDTickets;

public class TicketTypeFragment extends DialogFragment {

	private static final boolean IsCANCELABLE = true;

	private WKDTickets.TicketType ticketType;
	private String title;

	private TicketTypeListener ticketTypeListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(WKDTickets.TicketType.getArrayOfTicketTypeNames(), (dialog, which) -> {
			ticketType = WKDTickets.TicketType.getArrayOfTicketTypes()[which];
			ticketTypeListener.update(ticketType);
			dialog.cancel();
		})
		.setTitle(title)
		.setCancelable(IsCANCELABLE);

		return builder.create();
	}

	public void setTitle(String title){
		this.title = title;
	}

	public WKDTickets.TicketType getTicketType(){
		return ticketType;
	}

	public void setTicketType(WKDTickets.TicketType ticketType){
		this.ticketType = ticketType;
	}

	public void setTicketTypeListener(TicketTypeListener ticketTypeListener){
		this.ticketTypeListener = ticketTypeListener;
	}

}
