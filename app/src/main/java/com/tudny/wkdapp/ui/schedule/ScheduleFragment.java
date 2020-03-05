package com.tudny.wkdapp.ui.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tudny.wkdapp.MainActivity;
import com.tudny.wkdapp.R;
import com.tudny.wkdapp.RouteActivity;
import com.tudny.wkdapp.core.Station;
import com.tudny.wkdapp.core.WKDApi;
import com.tudny.wkdapp.core.data.Route;
import com.tudny.wkdapp.core.data.Schedule;
import com.tudny.wkdapp.dialogs.schedule.DatePickerFragment;
import com.tudny.wkdapp.dialogs.schedule.StationPickerFragment;
import com.tudny.wkdapp.dialogs.schedule.TimePickerFragment;
import com.tudny.wkdapp.gestures.WKDGestureListener;
import com.tudny.wkdapp.recycler.scheduleRecycler.RowModel;
import com.tudny.wkdapp.recycler.scheduleRecycler.ScheduleRecyclerAdapter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class ScheduleFragment extends Fragment implements ScheduleRecyclerAdapter.ItemClickListener{

	public static final String JSON_ROUTE_STRING_KEY = "json_route_string";
	private static final String DEBUG_TAG = ScheduleFragment.class.getSimpleName();

	private DialogFragment datePickerFragment;
	private DialogFragment timePickerFragment;
	private static final String datePickerTag = "datePicker";
	private static final String timePickerTag = "timePicker";

	private DialogFragment fromStationFragment;
	private DialogFragment toStationFragment;
	private static final String fromStationTag = "fromPicker";
	private static final String toStationTag = "toPicker";

	private LocalDate localDate;
	private LocalTime localTime;

	private Station defaultFromStation = Station.GRODZISK_MAZ_RADONSKA;
	private Station defaultToStation = Station.WWA_SRODMIESCIE_WKD;

	private Station fromStation;
	private Station toStation;

	private ScheduleRecyclerAdapter adapter;
	private ReloadThread reloader;

	private RequestQueue requestQueue;
	private WKDApi wkdApi;
	private Schedule savedSchedule;

	private View view;
	private Boolean initialCreation = Boolean.FALSE;

	private final static Integer minutes_interval = 20;
	private final static Integer days_interval = 1;

	private GestureDetectorCompat dateGestureDetector;
	private GestureDetectorCompat timeGestureDetector;
	private GestureDetectorCompat fromGestureDetector;
	private GestureDetectorCompat toGestureDetector;

	private Vibrator singleClickVibration;
	private static final int vibrationDurationForClick = 25;
	private static final int vibrationDurationForSearch = 50;
	private static final int vibrationDurationForError = 500;

	@Override
	public void onStart() {
		super.onStart();
		updateFromStation();
		updateToStation();
	}

	@Override
	public void onResume() {
		super.onResume();
		reloader.setRunning(Boolean.TRUE);
	}

	@Override
	public void onPause() {
		super.onPause();
		reloader.setRunning(Boolean.FALSE);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(view == null) {
			view = inflater.inflate(R.layout.fragment_schedule, container, false);
			initialCreation = Boolean.TRUE;
		} else {
			initialCreation = Boolean.FALSE;
		}

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
		if(initialCreation) {
			setupFAB(view);
			setupAll();
		}
	}

	private void setupAll() {
		loadDefaultValues();

		setupFrom();
		setupTo();
		setupDate();
		setupTime();

		setupVibration();
		setupRecyclerView();
		setupQueueAndAPI();
		setupReloader();
	}

	private void loadDefaultValues() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		defaultFromStation = Station.choseByNumber(Integer.parseInt(sharedPreferences.getString(getString(R.string.default_base_key), "")));
		defaultToStation = Station.choseByNumber(Integer.parseInt(sharedPreferences.getString(getString(R.string.default_target_key), "")));
	}

	@SuppressLint("ClickableViewAccessibility")
	private void setupTime() {
		setLocalTime(LocalTime.now());
		timePickerFragment = new TimePickerFragment();
		((TimePickerFragment) timePickerFragment).setTime(localTime);
		((TimePickerFragment) timePickerFragment).setTimeListener(this::setLocalTime);

		TextInputEditText inputTime = Objects.requireNonNull(getView()).findViewById(R.id.inputTime);
		inputTime.setShowSoftInputOnFocus(false);

		inputTime.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(timePickerTag) == null) {
				((TimePickerFragment) timePickerFragment).setTime(localTime);
				timePickerFragment.show(getActivity().getSupportFragmentManager(), timePickerTag);
			}
		});

		timeGestureDetector = new GestureDetectorCompat(getContext(), new WKDGestureListener(null,
				null,
				() -> {
					setLocalTime(localTime.plusMinutes(minutes_interval));
					clearFocus();
					makeVibration(vibrationDurationForClick);
				},
				() -> {
					setLocalTime(localTime.minusMinutes(minutes_interval));
					clearFocus();
					makeVibration(vibrationDurationForClick);
				})
		);

		TextInputEditText timeTextView = getView().findViewById(R.id.inputTime);
		timeTextView.setOnTouchListener((view, motionEvent) -> timeGestureDetector.onTouchEvent(motionEvent));

		updateTime();
	}

	private void setLocalTime(LocalTime localTime) {
		this.localTime = localTime;
		updateTime();
	}

	private void updateTime() {
		TextInputEditText inputTime = Objects.requireNonNull(getView()).findViewById(R.id.inputTime);
		LocalTime time = localTime;
		String timeAsString = time.format(((MainActivity) Objects.requireNonNull(getActivity())).getTimeFormatter());
		if(time.equals(LocalTime.now())) {
			timeAsString = getString(R.string.now);
		}
		inputTime.setText(timeAsString);
		inputTime.setKeyListener(null);
		clearFocus();
	}


	@SuppressLint("ClickableViewAccessibility")
	private void setupDate() {
		setLocalDate(LocalDate.now());
		datePickerFragment = new DatePickerFragment();
		((DatePickerFragment) datePickerFragment).setDate(localDate);
		((DatePickerFragment) datePickerFragment).setDateListener(this::setLocalDate);

		TextInputEditText inputDate = Objects.requireNonNull(getView()).findViewById(R.id.inputDate);
		inputDate.setShowSoftInputOnFocus(false);

		inputDate.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(datePickerTag) == null) {
				((DatePickerFragment) datePickerFragment).setDate(localDate);
				datePickerFragment.show(getActivity().getSupportFragmentManager(), datePickerTag);
			}
		});

		dateGestureDetector = new GestureDetectorCompat(getContext(), new WKDGestureListener(null,
				null,
				() -> {
					setLocalDate(localDate.plusDays(days_interval));
					clearFocus();
					makeVibration(vibrationDurationForClick);
				},
				() -> {
					setLocalDate(localDate.minusDays(days_interval));
					clearFocus();
					makeVibration(vibrationDurationForClick);
				})
		);

		TextInputEditText dateTextView = getView().findViewById(R.id.inputDate);
		dateTextView.setOnTouchListener((view, motionEvent) -> dateGestureDetector.onTouchEvent(motionEvent));

		updateDate();
	}

	private void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
		updateDate();
	}

	private void updateDate() {
		TextInputEditText inputDate = Objects.requireNonNull(getView()).findViewById(R.id.inputDate);
		LocalDate date = localDate;
		String dateAsString = date.format(((MainActivity) Objects.requireNonNull(getActivity())).getDateFormatter());
		if(date.equals(LocalDate.now())){
			dateAsString = getString(R.string.today);
		} else if(date.equals(LocalDate.now().plusDays(1))){
			dateAsString = getString(R.string.tomorrow);
		} else if(date.equals(LocalDate.now().minusDays(1))){
			dateAsString = getString(R.string.yesterday);
		}
		inputDate.setText(dateAsString);
		inputDate.setKeyListener(null);
		clearFocus();
	}

	@SuppressLint("ClickableViewAccessibility")
	private void setupTo() {
		setToStation(defaultToStation);
		toStationFragment = new StationPickerFragment();
		((StationPickerFragment) toStationFragment).setStation(toStation);
		((StationPickerFragment) toStationFragment).setStationListener(this::setToStation);
		((StationPickerFragment) toStationFragment).setTitle(getString(R.string.to));

		TextInputEditText inputToStation = Objects.requireNonNull(getView()).findViewById(R.id.inputTo);
		inputToStation.setShowSoftInputOnFocus(false);

		inputToStation.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(toStationTag) == null){
				((StationPickerFragment) toStationFragment).setStation(toStation);
				toStationFragment.show(getActivity().getSupportFragmentManager(), toStationTag);
			}
		});
		toGestureDetector = new GestureDetectorCompat(getContext(), new WKDGestureListener(this::swapFromAndTo,
				null,
				() -> {
					try {
						setToStation(toStation.nextStation());
						makeVibration(vibrationDurationForClick);
					} catch (Exception e){
						makeVibration(vibrationDurationForError);
					} finally {
						clearFocus();
					}
				},
				() -> {
					try {
						setToStation(toStation.previousStation());
						makeVibration(vibrationDurationForClick);
					} catch (Exception e){
						makeVibration(vibrationDurationForError);
					} finally {
						clearFocus();
					}
				})
		);

		TextInputEditText toTextView = getView().findViewById(R.id.inputTo);
		toTextView.setOnTouchListener((view, motionEvent) -> toGestureDetector.onTouchEvent(motionEvent));

		updateToStation();
	}

	private void setToStation(Station toStation) {
		this.toStation = toStation;
		updateToStation();
	}

	private void updateToStation() {
		TextInputEditText inputTo = Objects.requireNonNull(getView()).findViewById(R.id.inputTo);
		Station to = toStation;
		String stationAsString = to.getStationName();
		inputTo.setText(stationAsString);
		inputTo.setKeyListener(null);
		clearFocus();
	}

	@SuppressLint("ClickableViewAccessibility")
	private void setupFrom() {
		setFromStation(defaultFromStation);
		fromStationFragment = new StationPickerFragment();
		((StationPickerFragment) fromStationFragment).setStation(fromStation);
		((StationPickerFragment) fromStationFragment).setStationListener(this::setFromStation);
		((StationPickerFragment) fromStationFragment).setTitle(getString(R.string.from));

		TextInputEditText inputFromStation = Objects.requireNonNull(getView()).findViewById(R.id.inputFrom);
		inputFromStation.setShowSoftInputOnFocus(false);

		inputFromStation.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(fromStationTag) == null){
				((StationPickerFragment) fromStationFragment).setStation(fromStation);
				fromStationFragment.show(getActivity().getSupportFragmentManager(), fromStationTag);
			}
		});

		fromGestureDetector = new GestureDetectorCompat(getContext(), new WKDGestureListener(null,
				this::swapFromAndTo,
				() -> {
					try {
						setFromStation(fromStation.nextStation());
						makeVibration(vibrationDurationForClick);
					} catch (Exception e){
						makeVibration(vibrationDurationForError);
					} finally {
						clearFocus();
					}
				},
				() -> {
					try {
						setFromStation(fromStation.previousStation());
						makeVibration(vibrationDurationForClick);
					} catch (Exception e){
						makeVibration(vibrationDurationForError);
					} finally {
						clearFocus();
					}
				})
		);

		TextInputEditText fromTextView = getView().findViewById(R.id.inputFrom);
		fromTextView.setOnTouchListener((view, motionEvent) -> fromGestureDetector.onTouchEvent(motionEvent));

		updateFromStation();
	}

	private void setFromStation(Station fromStation) {
		this.fromStation = fromStation;
		updateFromStation();
	}

	private void updateFromStation() {
		TextInputEditText inputFrom = Objects.requireNonNull(getView()).findViewById(R.id.inputFrom);
		Station from = fromStation;
		String stationAsString = from.getStationName();
		inputFrom.setText(stationAsString);
		inputFrom.setKeyListener(null);
		clearFocus();
	}


	private void setupVibration(){
		singleClickVibration = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
	}

	private void makeVibration(int milliseconds){
		singleClickVibration.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
	}

	private void clearFocus(){
		View current = Objects.requireNonNull(getActivity()).getCurrentFocus();
		if (current != null) current.clearFocus();
	}

	private void swapFromAndTo(){
		int fromID = fromStation.getStationNumber();
		int toID = toStation.getStationNumber();

		setFromStation(Station.choseByNumber(toID));
		setToStation(Station.choseByNumber(fromID));

		TextInputEditText fromEditText = Objects.requireNonNull(getView()).findViewById(R.id.inputFrom);
		TextInputEditText toEditText = Objects.requireNonNull(getView()).findViewById(R.id.inputTo);

		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_from_bottom);
		animation.reset();
		fromEditText.clearAnimation();
		fromEditText.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_from_top);
		animation.reset();
		toEditText.clearAnimation();
		toEditText.startAnimation(animation);

		makeVibration(vibrationDurationForClick);
	}

	private void setupRecyclerView(){
		RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recycleList);
		LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		llm.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setLayoutManager(llm);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		adapter = new ScheduleRecyclerAdapter(getActivity(), new ArrayList<>());

		adapter.setClickListener(this);
		recyclerView.setAdapter(adapter);
	}

	private void setupQueueAndAPI(){
		requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
		wkdApi = new WKDApi(schedule -> {
			updateRecyclerView(schedule, Boolean.TRUE);
			savedSchedule = schedule;
			Toast.makeText(getContext(), schedule.getInfo().toPrintString(), Toast.LENGTH_SHORT).show();
		}, error -> {
			if(error instanceof TimeoutError)
				Toast.makeText(getActivity().getApplicationContext(), "Timeout occurred!", Toast.LENGTH_LONG).show();
			else if(error instanceof NoConnectionError)
				Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
		});
	}

	private void setupFAB(View view){
		FloatingActionButton fab = view.findViewById(R.id.searchButton);
		fab.setOnClickListener(viewTemp -> {

			String message;
			if(makeRequest()){
				message = getString(R.string.searching);
			} else {
				message = getString(R.string.bad_input);
			}

			Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
		});
	}

	private boolean makeRequest() {
		if(fromStation.equals(toStation) || !Station.canConnect(fromStation, toStation)){
			return false;
		}

		makeVibration(vibrationDurationForSearch);

		wkdApi.wkdRequest(requestQueue, fromStation, toStation, localDate, localTime);
		return true;
	}

	private void updateRecyclerView(Schedule schedule, Boolean playAnimation){
		List<RowModel> listValues = new ArrayList<>();

		/*
		 * Checking if there is any route available
		 * */

		if(schedule.getRoute().size() == 0) {
			RowModel rowModel = new RowModel(getString(R.string.no_train_left), "");
			listValues.add(rowModel);
		} else {
			RowModel rowModel = new RowModel(getString(R.string.dep_at), getString(R.string.dep_in));
			listValues.add(rowModel);
		}

		for(Route route : schedule.getRoute()){
			LocalTime arr = route.getArrival();

			LocalDateTime arrFull = LocalDateTime.of(localDate, arr);
			LocalDateTime now = LocalDateTime.now();

			int minutes = Math.toIntExact(now.until(arrFull, ChronoUnit.MINUTES));

			int h = minutes / 60;
			int m = minutes;

			String display = "";
			if(m < 0){
				display = getString(R.string.to_late);
			} else if(m == 0){
				display = getString(R.string.departures);
			} else {
				m -= h * 60;

				if(h > 0){
					display = display + " " + h + getString(R.string.h);
				}

				if(m > 0){
					display = display + " " + m + getString(R.string.m);
				}
			}

			RowModel rowModel = new RowModel(arr.format(((MainActivity) Objects.requireNonNull(getActivity())).getTimeFormatter()), display);
			listValues.add(rowModel);
		}

		RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recycleList);
		try{
			((ScheduleRecyclerAdapter) Objects.requireNonNull(recyclerView.getAdapter())).updateItems(listValues, schedule, playAnimation);
		} catch (NullPointerException e){
			Log.e(DEBUG_TAG, "Sth went wrong! " + e.getMessage() + " at " + Arrays.toString(e.getStackTrace()));
		}
	}

	private static final class LocalTimeAdapter extends TypeAdapter<LocalTime> {
		@Override
		public void write(final JsonWriter jsonWriter, final LocalTime localTime ) throws IOException {
			jsonWriter.value(localTime.toString());
		}

		@Override
		public LocalTime read( final JsonReader jsonReader ) throws IOException {
			return LocalTime.parse(jsonReader.nextString());
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		// header :(
		position--;

		try {
			Log.d(DEBUG_TAG, "Clicked: " + adapter.getItem(position) + " on " + position);

			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
					.create();

			String json = gson.toJson(adapter.getRoute(position));

			Intent intent = new Intent(getContext(), RouteActivity.class);
			intent.putExtra(JSON_ROUTE_STRING_KEY, json);
			startActivity(intent);
		} catch (Exception e){
			if(e instanceof IndexOutOfBoundsException){
				Log.d(DEBUG_TAG, "No trains left and trying to open whole route.");
			} else {
				Log.e(DEBUG_TAG, "Error occurred with opening route");
			}
			e.printStackTrace();
		}
	}

	private void setupReloader() {
		int delay = 1000;
		final Handler handler = new Handler();
		reloader = new ReloadThread(handler, delay);
		reloader.start();
	}

	private class ReloadThread extends Thread {

		private Handler handler;
		private Integer delay;

		ReloadThread(Handler handler, Integer delay){
			this.handler = handler;
			this.delay = delay;
		}

		@Getter
		@Setter
		private Boolean running = false;

		@Override
		public void start(){
			setRunning(Boolean.TRUE);
			super.start();
		}

		public void run(){
			if(!running) return;
			if(savedSchedule != null)
				updateRecyclerView(savedSchedule, Boolean.FALSE);
			handler.postDelayed(this, delay);
		}
	}
}
