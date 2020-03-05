package com.tudny.wkdapp.core;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


// Class which holds data about transit rawly from json string
@Getter
@Setter
@ToString
@SuppressWarnings("unused")
public class RespondJson {

	static JsonSchedule convert(String jsonString){
		Gson gson = new Gson();

		return gson.fromJson(jsonString, JsonSchedule.class);
	}

	@Getter
	@Setter
	@ToString
	public static class JsonSchedule {
		private List<JsonRoute> route = new ArrayList<>(0);
		private JsonScheduleAdditionalInfo info = new JsonScheduleAdditionalInfo();

		boolean isEmpty() {
			return route.isEmpty();
		}
	}

	@Getter
	@Setter
	@ToString
	public static class JsonScheduleAdditionalInfo {

		private String distance = "";
		private String time = "";

	}

	@Getter
	@Setter
	@ToString
	public static class JsonRoute {
		private String number;
		private String arr;
		private String dep;
		private Integer low_floor;
		private String dayOperationCode;
		private String symbol;
		private String time;
		private String serviceType;
		private List<JsonStation> intermediateStations;
	}

	@Getter
	@Setter
	@ToString
	public static class JsonStation {
		private String name;
		private String arr;
		private String dep;
		private String serviceType;
		private String active;
	}
}
