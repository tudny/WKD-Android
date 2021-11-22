package com.tudny.wkdapp.core;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.tudny.wkdapp.core.apiListeners.OnErrorOccurredListener;
import com.tudny.wkdapp.core.apiListeners.OnScheduleFetchedListener;
import com.tudny.wkdapp.core.data.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

public class WKDApi {

	private final static String DEBUG_TAG = WKDApi.class.getSimpleName();

	// Default values of patterns od date and time
	private static final DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("HH:mm");

	// WKD server URL
	private static final String url = "http://archiwum.wkd.com.pl/rozklad/rozklad.php";
	// Default timeout
	private static final int SOCKET_TIMEOUT_MS = 2000;

	private final OnScheduleFetchedListener onScheduleFetchedListener;
	private final OnErrorOccurredListener onErrorOccurredListener;

	public WKDApi(OnScheduleFetchedListener onScheduleFetchedListener, OnErrorOccurredListener onErrorOccurredListener){
		this.onScheduleFetchedListener = onScheduleFetchedListener;
		this.onErrorOccurredListener = onErrorOccurredListener;
	}

	public void wkdRequest(RequestQueue queue, Station base, Station target, LocalDate date, LocalTime time) {

		Log.d(DEBUG_TAG, "Making WKD request with POST request");

		final String baseName = base.getStationName();
		final String targetName = target.getStationName();
		final String dateString = date.format(datePattern);
		final String timeFromString = time.format(timePattern);

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				response -> {
					Log.d(DEBUG_TAG, "Request POST -> successful");

					// Log.e(DEBUG_TAG, response);

					/*
					 * As mentioned a few lines lower WKD API is broken
					 * */
					response = removeHTMLAndLeaveJSON(response);
					/*
					 * END
					 * */

					RespondJson.JsonSchedule jsonSchedule = RespondJson.convert(response);
					Schedule schedule = new Schedule(jsonSchedule);

					/*
					 * Great safety check!!!
					 * WKD API is broken at this point so that we have to initiate schedule list on our own if there are no trains left for this date
					 * */
					if(schedule.getRoute() == null) schedule.setRoute(new ArrayList<>());
					/*
					 * END of this creepy thing
					 * */

					onScheduleFetchedListener.onScheduleFetched(schedule);
				},

				error -> {
					Log.d(DEBUG_TAG, "Request POST -> ERROR");
					Log.e("Error.Response", error.toString());
					onErrorOccurredListener.onErrorOccurred(error);
				}
		) {
			@Override
			protected Map<String, String> getParams(){
				Map<String, String> params = new HashMap<>();
				params.put("base", baseName);
				params.put("target", targetName);
				params.put("date", dateString);
				params.put("from", timeFromString);
				params.put("to", "23:59");

				return params;
			}

			@Override
			public Map<String, String> getHeaders(){
				return singletonMap("Content-Type", "application/x-www-form-urlencoded");
			}
		};

		postRequest.setRetryPolicy(new DefaultRetryPolicy(
				SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
		));

		queue.add(postRequest);

		Log.d(DEBUG_TAG, "Adding request to queue DONE.");
	}

	private String removeHTMLAndLeaveJSON(String response) {

		int begin = response.indexOf("{");
		int end = response.lastIndexOf("}");

		return response.substring(begin, end + 1);
	}
}
