package com.tudny.wkdapp.ui.tickets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tudny.wkdapp.MainActivity;
import com.tudny.wkdapp.R;
import com.tudny.wkdapp.core.Station;
import com.tudny.wkdapp.dialogs.schedule.StationPickerFragment;
import com.tudny.wkdapp.dialogs.tickets.DirectionFragment;
import com.tudny.wkdapp.dialogs.tickets.PeriodNamedFragment;
import com.tudny.wkdapp.dialogs.tickets.PeriodUnnamedFragment;
import com.tudny.wkdapp.dialogs.tickets.ReliefFragment;
import com.tudny.wkdapp.dialogs.tickets.ReliefMiddleFragment;
import com.tudny.wkdapp.dialogs.tickets.ReliefShortFragment;
import com.tudny.wkdapp.dialogs.tickets.TicketTypeFragment;
import com.tudny.wkdapp.recycler.ticketRecycle.RowModel;
import com.tudny.wkdapp.recycler.ticketRecycle.TicketsRecycleAdapter;
import com.tudny.wkdapp.tickets.WKDTickets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TicketsFragment extends Fragment implements TicketsRecycleAdapter.ItemClickListener{

	private static final String URL = "http://www.wkd.com.pl/bilety/ceny-biletow.html";

	private TicketsRecycleAdapter adapter;

	private static final String DEBUG_TAG = TicketsFragment.class.getSimpleName();

	private static final String fromStationTagTickets = "fromPickerTickets";
	private static final String toStationTagTickets = "toPickerTickets";
	private static final String reliefPickerTagTickets = "reliefPPickerTickets";
	private static final String ticketTypePickerTagTickets = "ticketTypePicketTickets";
	private static final String periodPickerTagTickets = "periodPicketTickets";
	private static final String directionPickerTagTickets = "directionPickerTickets";

	private DialogFragment fromStationFragment;
	private DialogFragment toStationFragment;
	private DialogFragment reliefFragment;
	private DialogFragment ticketTypeFragment;
	private DialogFragment periodNamedFragment;
	private DialogFragment directionFragment;
	private DialogFragment periodUnnamedFragment;
	private DialogFragment reliefMiddleFragment;
	private DialogFragment reliefShortFragment;

	private Station fromStation;
	private Station toStation;
	private WKDTickets.Relief relief;
	private WKDTickets.TicketType ticketType;
	private WKDTickets.Period period;
	private WKDTickets.Direction direction;

	private final Station defaultFromStation = Station.WWA_SRODMIESCIE_WKD;
	private final Station defaultToStation = Station.GRODZISK_MAZ_RADONSKA;
	private final WKDTickets.Relief defaultRelief = WKDTickets.Relief.NORMAL;
	private final WKDTickets.TicketType defaultTicketType = WKDTickets.TicketType.NAMED;
	private final WKDTickets.Period defaultPeriod = WKDTickets.Period.MONTHLY;
	private final WKDTickets.Direction defaultDirection = WKDTickets.Direction.RETURNWAY;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tickets, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

		MainActivity.setGoodTitle(this, R.string.tickets_label);

		setupRecycler();
		setupFromStationFragment();
		setupToStationFragment();
		setupTicketTypeFragment();

		setupFAB();
		setupLayout(R.id.season_ticket_layout, R.id.season_ticket_view);
		setupLayout(R.id.single_ticket_layout, R.id.single_ticket_view);

		new FetchHTMLTable().execute(URL);
	}

	private void setupLayout(Integer layoutId, Integer clickableId) {
		LinearLayout topLayout = getView().findViewById(layoutId);
		TextView clickable = getView().findViewById(clickableId);

		clickable.setOnClickListener(view -> {
			int vis;
			switch(topLayout.getVisibility()){
				case View.VISIBLE:
					vis = View.GONE;
					break;
				case View.GONE:
					vis = View.VISIBLE;
					break;
				default:
					vis = View.VISIBLE;
			}
			topLayout.setVisibility(vis);
		});
	}

	private void setupFAB(){
		Button button = Objects.requireNonNull(getView()).findViewById(R.id.search_button);
		button.setOnClickListener(v -> {

			WKDTickets.DistanceZone distanceZone = WKDTickets.DistanceZone.findDistanceZoneByDistance(Station.distance(fromStation, toStation));

			Log.d("TicketFragment.fab.onClickListener", String.format(
					Locale.ENGLISH, "Calculating ticket: %s | %s | %s | %s | %s | %s | Distance: %d | DistanceZone: %d",
					fromStation.getStationName(),
					toStation.getStationName(),
					ticketType.getName(),
					period.getName(),
					relief.getFullName(),
					direction.getName(),
					Station.distance(fromStation, toStation),
					distanceZone.getNumber()
					)
			);

			TextView priceTextView = getView().findViewById(R.id.price_textView);

			try {
				Double price = WKDTickets.seasonTicket(distanceZone, period, ticketType, direction, relief);
				Log.d("TicketFragment.fab.onClickListener", "Price of the ticket is: " + price);
				priceTextView.setText(getString(R.string.price_sentence, price));

				Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_fall_down);
				animation.reset();
				priceTextView.clearAnimation();
				priceTextView.startAnimation(animation);
			} catch (Exception e){
				Log.e("TicketFragment.fab.onClickListener", "Error with ticket: " + e.getMessage());
				priceTextView.setText(e.getMessage());
			}
		});
	}

	private void setupFromStationFragment(){
		fromStation = defaultFromStation;
		fromStationFragment = new StationPickerFragment();
		((StationPickerFragment) fromStationFragment).setStation(fromStation);
		((StationPickerFragment) fromStationFragment).setStationListener(station -> {
			fromStation = station;
			updateFromStation();
		});
		((StationPickerFragment) fromStationFragment).setTitle(getString(R.string.from));

		TextInputEditText inputFromStation = Objects.requireNonNull(getView()).findViewById(R.id.inputFrom);
		inputFromStation.setShowSoftInputOnFocus(false);
		inputFromStation.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(fromStationTagTickets) == null){
				fromStationFragment.show(getActivity().getSupportFragmentManager(), fromStationTagTickets);
			}
		});
		updateFromStation();
	}

	private void updateFromStation(){
		TextInputEditText inputFrom = Objects.requireNonNull(getView()).findViewById(R.id.inputFrom);
		Station from = ((StationPickerFragment) fromStationFragment).getStation();
		String stationAsString = from.getStationName();
		inputFrom.setText(stationAsString);
		inputFrom.setKeyListener(null);
		clearFocus();
	}

	private void setupToStationFragment(){
		toStation = defaultToStation;
		toStationFragment = new StationPickerFragment();
		((StationPickerFragment) toStationFragment).setStation(toStation);
		((StationPickerFragment) toStationFragment).setStationListener(station -> {
			toStation = station;
			updateToStation();
		});
		((StationPickerFragment) toStationFragment).setTitle(getString(R.string.to));

		TextInputEditText inputToStation = Objects.requireNonNull(getView()).findViewById(R.id.inputTo);
		inputToStation.setShowSoftInputOnFocus(false);
		inputToStation.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(toStationTagTickets) == null){
				toStationFragment.show(getActivity().getSupportFragmentManager(), toStationTagTickets);
			}
		});
		updateToStation();
	}

	private void updateToStation(){
		TextInputEditText inputTo = Objects.requireNonNull(getView()).findViewById(R.id.inputTo);
		Station to = ((StationPickerFragment) toStationFragment).getStation();
		String stationAsString = to.getStationName();
		inputTo.setText(stationAsString);
		inputTo.setKeyListener(null);
		clearFocus();
	}

	private void setupReliefFragment(){
		relief = defaultRelief;
		reliefFragment = new ReliefFragment();
		((ReliefFragment) reliefFragment).setRelief(relief);
		((ReliefFragment) reliefFragment).setReliefListener(relief -> {
			this.relief = relief;
			updateRelief();
			setupDirectionFragment();
		});
		((ReliefFragment) reliefFragment).setTitle(getString(R.string.relief));

		TextInputEditText inputRelief = Objects.requireNonNull(getView()).findViewById(R.id.inputRelief);
		inputRelief.setShowSoftInputOnFocus(false);
		inputRelief.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(reliefPickerTagTickets) == null){
				reliefFragment.show(getActivity().getSupportFragmentManager(), reliefPickerTagTickets);
			}
		});
		updateRelief();
		setupDirectionFragment();
	}

	private void updateRelief(){
		TextInputEditText inputRelief = Objects.requireNonNull(getView()).findViewById(R.id.inputRelief);
		WKDTickets.Relief relief = ((ReliefFragment) reliefFragment).getRelief();
		String reliefAsString = relief.getName();
		inputRelief.setText(reliefAsString);
		inputRelief.setKeyListener(null);
		clearFocus();
	}

	private void setupReliefMiddleFragment(){
		relief = defaultRelief;
		reliefMiddleFragment = new ReliefMiddleFragment();
		((ReliefMiddleFragment) reliefMiddleFragment).setRelief(relief);
		((ReliefMiddleFragment) reliefMiddleFragment).setReliefListener(relief -> {
			this.relief = relief;
			updateReliefMiddle();
			setupDirectionFragment();
		});
		((ReliefMiddleFragment) reliefMiddleFragment).setTitle(getString(R.string.relief));

		TextInputEditText inputRelief = Objects.requireNonNull(getView()).findViewById(R.id.inputRelief);
		inputRelief.setShowSoftInputOnFocus(false);
		inputRelief.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(reliefPickerTagTickets) == null){
				reliefMiddleFragment.show(getActivity().getSupportFragmentManager(), reliefPickerTagTickets);
			}
		});
		updateReliefMiddle();
		setupDirectionFragment();
	}

	private void updateReliefMiddle(){
		TextInputEditText inputRelief = Objects.requireNonNull(getView()).findViewById(R.id.inputRelief);
		WKDTickets.Relief relief = ((ReliefMiddleFragment) reliefMiddleFragment).getRelief();
		String reliefAsString = relief.getName();
		inputRelief.setText(reliefAsString);
		inputRelief.setKeyListener(null);
		clearFocus();
	}

	private void setupReliefShortFragment(){
		relief = defaultRelief;
		reliefShortFragment = new ReliefShortFragment();
		((ReliefShortFragment) reliefShortFragment).setRelief(relief);
		((ReliefShortFragment) reliefShortFragment).setReliefListener(relief -> {
			this.relief = relief;
			updateReliefShort();
			setupDirectionFragment();
		});
		((ReliefShortFragment) reliefShortFragment).setTitle(getString(R.string.relief));

		TextInputEditText inputRelief = Objects.requireNonNull(getView()).findViewById(R.id.inputRelief);
		inputRelief.setShowSoftInputOnFocus(false);
		inputRelief.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(reliefPickerTagTickets) == null){
				reliefShortFragment.show(getActivity().getSupportFragmentManager(), reliefPickerTagTickets);
			}
		});
		updateReliefShort();
		setupDirectionFragment();
	}

	private void updateReliefShort(){
		TextInputEditText inputRelief = Objects.requireNonNull(getView()).findViewById(R.id.inputRelief);
		WKDTickets.Relief relief = ((ReliefShortFragment) reliefShortFragment).getRelief();
		String reliefAsString = relief.getName();
		inputRelief.setText(reliefAsString);
		inputRelief.setKeyListener(null);
		clearFocus();
	}

	private void setupTicketTypeFragment(){
		ticketType = defaultTicketType;
		ticketTypeFragment = new TicketTypeFragment();
		((TicketTypeFragment) ticketTypeFragment).setTicketType(ticketType);
		((TicketTypeFragment) ticketTypeFragment).setTicketTypeListener(ticketType -> {
			this.ticketType = ticketType;
			updateTicketType();

			if(ticketType == WKDTickets.TicketType.NAMED){
				setupPeriodNamedFragment();
			} else if (ticketType == WKDTickets.TicketType.UNNAMED){
				setupPeriodUnnamedFragment();
			}
		});
		((TicketTypeFragment) ticketTypeFragment).setTitle(getString(R.string.ticket_type));

		TextInputEditText inputTicketType = Objects.requireNonNull(getView()).findViewById(R.id.inputTicketType);
		inputTicketType.setShowSoftInputOnFocus(false);
		inputTicketType.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(ticketTypePickerTagTickets) == null){
				ticketTypeFragment.show(getActivity().getSupportFragmentManager(), ticketTypePickerTagTickets);
			}
		});
		updateTicketType();
		if(ticketType == WKDTickets.TicketType.NAMED){
			setupPeriodNamedFragment();
		} else if (ticketType == WKDTickets.TicketType.UNNAMED){
			setupPeriodUnnamedFragment();
		}
	}

	private void updateTicketType(){
		TextInputEditText inputTicketType = Objects.requireNonNull(getView()).findViewById(R.id.inputTicketType);
		WKDTickets.TicketType ticketType = ((TicketTypeFragment) ticketTypeFragment).getTicketType();
		String ticketTypeAsString = ticketType.getName();
		inputTicketType.setText(ticketTypeAsString);
		inputTicketType.setKeyListener(null);
		clearFocus();
	}

	private void setupPeriodNamedFragment(){
		period = defaultPeriod;
		periodNamedFragment = new PeriodNamedFragment();
		((PeriodNamedFragment) periodNamedFragment).setPeriod(period);
		((PeriodNamedFragment) periodNamedFragment).setPeriodListener(period -> {
			this.period = period;
			updatePeriodNamed();

			if(period == WKDTickets.Period.MONTHLY){
				setupReliefFragment();
			} else {
				setupReliefMiddleFragment();
			}
		});
		((PeriodNamedFragment) periodNamedFragment).setTitle(getString(R.string.period));

		TextInputEditText inputPeriod = Objects.requireNonNull(getView()).findViewById(R.id.inputPeriod);
		inputPeriod.setShowSoftInputOnFocus(false);
		inputPeriod.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(periodPickerTagTickets) == null){
				periodNamedFragment.show(getActivity().getSupportFragmentManager(), periodPickerTagTickets);
			}
		});
		updatePeriodNamed();
		if(period == WKDTickets.Period.MONTHLY){
			setupReliefFragment();
		} else {
			setupReliefMiddleFragment();
		}
	}

	private void updatePeriodNamed(){
		TextInputEditText inputPeriod = Objects.requireNonNull(getView()).findViewById(R.id.inputPeriod);
		WKDTickets.Period period = ((PeriodNamedFragment) periodNamedFragment).getPeriod();
		String periodAsString = period.getName();
		inputPeriod.setText(periodAsString);
		inputPeriod.setKeyListener(null);
		clearFocus();
	}

	private void setupPeriodUnnamedFragment(){
		period = defaultPeriod;
		periodUnnamedFragment = new PeriodUnnamedFragment();
		((PeriodUnnamedFragment) periodUnnamedFragment).setPeriod(period);
		((PeriodUnnamedFragment) periodUnnamedFragment).setPeriodListener(period -> {
			this.period = period;
			updatePeriodUnnamed();

			setupReliefShortFragment();
		});
		((PeriodUnnamedFragment) periodUnnamedFragment).setTitle(getString(R.string.period));

		TextInputEditText inputPeriod = Objects.requireNonNull(getView()).findViewById(R.id.inputPeriod);
		inputPeriod.setShowSoftInputOnFocus(false);
		inputPeriod.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(periodPickerTagTickets) == null){
				periodUnnamedFragment.show(getActivity().getSupportFragmentManager(), periodPickerTagTickets);
			}
		});
		updatePeriodUnnamed();
		setupReliefShortFragment();
	}

	private void updatePeriodUnnamed(){
		TextInputEditText inputPeriod = Objects.requireNonNull(getView()).findViewById(R.id.inputPeriod);
		WKDTickets.Period period = ((PeriodUnnamedFragment) periodUnnamedFragment).getPeriod();
		String periodAsString = period.getName();
		inputPeriod.setText(periodAsString);
		inputPeriod.setKeyListener(null);
		clearFocus();
	}


	private void setupDirectionFragment(){
		direction = defaultDirection;
		directionFragment = new DirectionFragment();
		((DirectionFragment) directionFragment).setDirection(direction);
		((DirectionFragment) directionFragment).setDirectionListener(direction -> {
			this.direction = direction;
			updateDirection();
		});
		((DirectionFragment) directionFragment).setTitle(getString(R.string.direction));

		TextInputEditText inputDirection = Objects.requireNonNull(getView()).findViewById(R.id.inputDirection);
		inputDirection.setShowSoftInputOnFocus(false);
		inputDirection.setOnClickListener(v -> {
			if(Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(directionPickerTagTickets) == null){
				directionFragment.show(getActivity().getSupportFragmentManager(), directionPickerTagTickets);
			}
		});
		updateDirection();
	}

	private void updateDirection(){
		TextInputEditText inputDirection = Objects.requireNonNull(getView()).findViewById(R.id.inputDirection);
		WKDTickets.Direction direction = ((DirectionFragment) directionFragment).getDirection();
		String directionAsString = direction.getName();
		inputDirection.setText(directionAsString);
		inputDirection.setKeyListener(null);
		clearFocus();
	}

	@SuppressWarnings("unused")
	private void clearTicketType(){
		TextInputEditText inputTicketType = Objects.requireNonNull(getView()).findViewById(R.id.inputTicketType);
		inputTicketType.setOnClickListener(null);
		inputTicketType.setText(null);
	}

	@SuppressWarnings("unused")
	private void clearPeriod(){
		TextInputEditText inputPeriod = Objects.requireNonNull(getView()).findViewById(R.id.inputPeriod);
		inputPeriod.setOnClickListener(null);
		inputPeriod.setText(null);
	}

	@SuppressWarnings("unused")
	private void clearRelief(){
		TextInputEditText inputRelief = Objects.requireNonNull(getView()).findViewById(R.id.inputRelief);
		inputRelief.setOnClickListener(null);
		inputRelief.setText(null);
	}

	@SuppressWarnings("unused")
	private void clearDirection(){
		TextInputEditText inputDirection = Objects.requireNonNull(getView()).findViewById(R.id.inputDirection);
		inputDirection.setOnClickListener(null);
		inputDirection.setText(null);
	}

	private void clearFocus(){
		View current = Objects.requireNonNull(getActivity()).getCurrentFocus();
		if (current != null) current.clearFocus();
	}

	private void setupRecycler() {
		RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.ticketsRecyclerView);
		LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		llm.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setLayoutManager(llm);

		adapter = new TicketsRecycleAdapter(getActivity(), new ArrayList<RowModel>(){
			{
				for(int i = 0; i < 1; i++){
					add(new RowModel("Loading", "Loading", "Loading", "Loading"));
				}
			}
		});

		adapter.setItemClickListener(this);
		recyclerView.setAdapter(adapter);
	}

	private void updateRecyclerView(String[][] strings){
		List<RowModel> data = new ArrayList<>();

		for(int i = 0; i < strings.length; i++){

			if(strings[i].length < 4)
				continue;

			if(!strings[i][0].contains("Taryfa") && i > 0)
				break;

			String relief = strings[i][0],
					zone1 = strings[i][1],
					zone2 = strings[i][2],
					zone3 = strings[i][3];

			if(relief.contains("(ulga ustawowa)"))
				relief = relief.replace(
						"(ulga ustawowa)",
						"\n(ulga ustawowa)"
				);

			if(i == 0){
				relief = "<b>" + relief + "</b>";
				zone1 = "<b>" + zone1 + "</b>";
				zone2 = "<b>" + zone2 + "</b>";
				zone3 = "<b>" + zone3 + "</b>";
			} else {
				relief = "<i>" + relief + "</i>";
				zone1 = "<i>" + zone1 + "</i>";
				zone2 = "<i>" + zone2 + "</i>";
				zone3 = "<i>" + zone3 + "</i>";
			}

			RowModel rowModel = new RowModel(
					relief,
					zone1,
					zone2,
					zone3
			);

			data.add(rowModel);
		}

		adapter.updateItems(data);
	}

	private void HTMLTableFetched(String[][] table){
		if(table != null) {
			updateRecyclerView(table);
		} else {
			adapter.updateItems(new ArrayList<RowModel>() {{
				add(new RowModel("No", "Internet", "Error", ""));
			}});
		}
	}

	@SuppressWarnings("StaticFieldLeak")
	class FetchHTMLTable extends AsyncTask<String, Void, String[][]> {
		@Override
		protected String[][] doInBackground(String... params) {
			String url = params[0];

			String[][] trtd;

			try {
				Document doc = Jsoup.connect(url).get();
				Elements tables = doc.select("tbody");
				Element table = tables.get(0);

				Elements trs = table.select("tr");
				trtd = new String[trs.size()][];
				for(int i = 0; i < trs.size(); i++){
					Elements tds = trs.get(i).select("td");
					trtd[i] = new String[tds.size()];
					for(int j = 0; j < tds.size(); j++){
						trtd[i][j] = tds.get(j).text();
					}
				}

			} catch (Exception e) {
				Log.e(DEBUG_TAG, Objects.requireNonNull(e.getMessage()));
				return null;
			}

			String[][] thead = null;

			try{
				Document doc = Jsoup.connect(url).get();
				Elements threads = doc.select("thead");
				for(Element thea : threads) {
					Elements trs = thea.select("tr");
					thead = new String[trs.size()][];
					for (int i = 0; i < trs.size(); i++) {
						Elements ths = trs.get(i).select("th");
						thead[i] = new String[ths.size()];
						for (int j = 0; j < ths.size(); j++) {
							thead[i][j] = ths.get(j).text();
						}
					}
				}
			} catch (Exception e){
				e.printStackTrace();
				return null;
			}

			return combineTables(Objects.requireNonNull(thead), Objects.requireNonNull(trtd));
		}

		@Override
		protected void onPostExecute(String[][] result){
			HTMLTableFetched(result);
		}
	}

	private static String[][] combineTables(String[][] header, String[][] body){
		String[][] table = new String[header.length + body.length][];

		int ws = 0;
		for (String[] strings : header) {
			table[ws] = strings;
			ws++;
		}

		for (String[] strings : body) {
			table[ws] = strings;
			ws++;
		}

		return table;
	}

	@Override
	public void onItemClick(View view, int position) {
		System.out.println("Clicked: " + adapter.getItem(position) + " on " + position);
		if(position == 0) return;
		Toast.makeText(
				getActivity(),
				adapter.getItem(position).relief
						.replace("<i>", "")
						.replace("</i>", ""),
				Toast.LENGTH_SHORT
		).show();
	}
}
