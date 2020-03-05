package com.tudny.wkdapp.core.data;

import com.tudny.wkdapp.core.RespondJson;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Data, but not raw
@Getter
@Setter
@ToString
public class Route {
	private LocalTime arrival;
	private String symbol;
	private Integer low_floor;
	private String serviceType;
	private Duration time;
	private String dayOperationCode;
	private LocalTime departure;
	private Route(String arrival, String symbol, Integer low_floor, String serviceType, String time, String dayOperationCode, String departure, List<RespondJson.JsonStation> intermediateStations) {
		this.arrival = LocalTime.parse(arrival);
		this.symbol = symbol;
		this.low_floor = low_floor;
		this.serviceType = serviceType;

		String[] timeS = time.split(":");
		int h = Integer.parseInt(timeS[0]);
		int m = Integer.parseInt(timeS[1]) + h * 60;
		this.time = Duration.of(m, ChronoUnit.MINUTES);

		this.dayOperationCode = dayOperationCode;
		this.departure = LocalTime.parse(departure);

		this.intermediateStations = new ArrayList<>();
		for(RespondJson.JsonStation jStation : intermediateStations){
			this.intermediateStations.add(new StationOnRoute(jStation));
		}
	}

	private List<StationOnRoute> intermediateStations;

	Route(RespondJson.JsonRoute jRoute){
		this(jRoute.getArr(), jRoute.getSymbol(), jRoute.getLow_floor(), jRoute.getServiceType(), jRoute.getTime(), jRoute.getDayOperationCode(), jRoute.getDep(), jRoute.getIntermediateStations());
	}
}
