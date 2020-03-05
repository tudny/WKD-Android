package com.tudny.wkdapp.dialogs.schedule;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tudny.wkdapp.R;
import com.tudny.wkdapp.dialogs.schedule.listeners.DateListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private static final String DEBUG_TAG = DatePickerFragment.class.getSimpleName();

	private LocalDate date;

	private DateListener dateListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){

		Log.d(DEBUG_TAG, "Creating dialog.");

		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		if(date != null){
			Log.d(DEBUG_TAG, "Loading date.");
			year = date.getYear();
			month = date.getMonthValue() - 1;
			day = date.getDayOfMonth();
		} else {
			Log.d(DEBUG_TAG, "Date not saved. Making default.");
		}

		return new DatePickerDialog(Objects.requireNonNull(getActivity()), R.style.DatePickerTheme, this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		date = LocalDate.of(year, month + 1, dayOfMonth);
		Log.d(DEBUG_TAG, "Chosen date: " + date.format(DateTimeFormatter.ISO_DATE));
		dateListener.update(date);
	}

	@SuppressWarnings("unused")
	public LocalDate getDate(){
		return date;
	}

	public void setDate(LocalDate date){
		this.date = date;
	}

	public void setDateListener(DateListener dateListener){
		this.dateListener = dateListener;
	}

}
