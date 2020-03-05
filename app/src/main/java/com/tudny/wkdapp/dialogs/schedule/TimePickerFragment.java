package com.tudny.wkdapp.dialogs.schedule;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tudny.wkdapp.R;
import com.tudny.wkdapp.dialogs.schedule.listeners.TimeListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private LocalTime time;

	private TimeListener timeListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){

		final Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		if(time != null){
			hour = time.getHour();
			minute = time.getMinute();
		}

		return new TimePickerDialog(getActivity(), R.style.TimePickerTheme, this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		time = LocalTime.of(hourOfDay, minute);
		Log.d("TimePickerFragment.onTimeSet", "Chosen time: " + time.format(DateTimeFormatter.ofPattern("hh:mm a")));
		timeListener.update(time);
	}

	public LocalTime getTime(){
		return time;
	}

	public void setTime(LocalTime time){
		this.time = time;
	}

	public void setTimeListener(TimeListener timeListener) {
		this.timeListener = timeListener;
	}
}
