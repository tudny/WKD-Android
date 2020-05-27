package com.tudny.wkdapp;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tudny.wkdapp.core.data.Route;
import com.tudny.wkdapp.core.data.StationOnRoute;
import com.tudny.wkdapp.recycler.routeRecycler.RouteRecyclerAdapter;
import com.tudny.wkdapp.recycler.routeRecycler.RowModel;
import com.tudny.wkdapp.ui.schedule.ScheduleFragment;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity implements RouteRecyclerAdapter.ItemClickListener{

	private static final String DEBUG_TAG = RouteActivity.class.getSimpleName();

	private static final DateTimeFormatter time12Formatter = DateTimeFormatter.ofPattern("hh:mm a");
	private static final DateTimeFormatter time24Formatter = DateTimeFormatter.ofPattern("HH:mm");
	private static final DateTimeFormatter time12FormatterNewLine = DateTimeFormatter.ofPattern("hh:mm\na");

	private RouteRecyclerAdapter adapter;

	@Override
	public void onItemClick(View view, int position) {
		Log.d(DEBUG_TAG, "Clicked on: " + adapter.getItem(position) + " at position: " + position + ".");
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);

		Log.d(DEBUG_TAG, "onCreate");

		try {
			if(getSupportActionBar() == null) throw new NullPointerException();
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (Exception e){
			Log.e(DEBUG_TAG, e.getMessage(), e);
		}

		String json = getIntent().getStringExtra(ScheduleFragment.JSON_ROUTE_STRING_KEY);
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
				.create();
		Route route = gson.fromJson(json, Route.class);

		Log.d(DEBUG_TAG, "Loaded route: " + route.toString().substring(0, 100) + "...");

		setupRecycler(route);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			finish();
		} else if(item.getItemId() == R.id.follow_option) {
			// TODO Following
		}

		return super.onOptionsItemSelected(item);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.route_menu,menu);
		return super.onCreateOptionsMenu(menu);
	}



	private void setupRecycler(Route route) {

		Log.d(DEBUG_TAG, "Setting up route recycler view: " + route.toString().substring(0, 100) + "...");

		RecyclerView recyclerView = findViewById(R.id.route_recycler_view);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setLayoutManager(llm);

		/* DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);*/

		int mostImportant = -1;
		for(StationOnRoute station : route.getIntermediateStations()){
			if(station.getActive() == Boolean.TRUE){
				mostImportant = route.getIntermediateStations().indexOf(station);
				break;
			}
		}

		adapter = new RouteRecyclerAdapter(this, new ArrayList<RowModel>(){
			{
				int type = 0;

				for(StationOnRoute station : route.getIntermediateStations()){
					if(station.getActive()) type++;
					add(new RowModel(station.getName(), getString(R.string.arrives) + " • " + station.getArrival().format(getTimeFormatter()), station.getDeparture().format(getTimeFormatterNewLine()), type));
					if(station.getActive()) type++;
				}
			}
		});

		adapter.setClickListener(this);
		recyclerView.setAdapter(adapter);
		llm.scrollToPositionWithOffset(mostImportant, 20);
	}

	private DateTimeFormatter getTimeFormatter() {
		if (DateFormat.is24HourFormat(this)) {
			return time24Formatter;
		} else {
			return time12Formatter;
		}
	}

	private DateTimeFormatter getTimeFormatterNewLine() {
		DateTimeFormatter dateTimeFormatter = getTimeFormatter();
		if(dateTimeFormatter.equals(time12Formatter)) return time12FormatterNewLine;
		else return time24Formatter;
	}
}
