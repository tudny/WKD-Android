package com.tudny.wkdapp.core.data;

import com.tudny.wkdapp.core.RespondJson;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Data, but not raw
@Getter
@Setter
@ToString
public class Schedule {
	private List<Route> route;
	private AdditionalInfo info;

	@SuppressWarnings("WeakerAccess")
	public Schedule(List<RespondJson.JsonRoute> jsonRoutes, RespondJson.JsonScheduleAdditionalInfo additionalInfo){
		route = new ArrayList<>();
		for(RespondJson.JsonRoute jsonRoute : jsonRoutes){
			route.add(new Route(jsonRoute));
		}
		info = new AdditionalInfo(additionalInfo);
	}

	public Schedule(RespondJson.JsonSchedule jsonSchedule){
		this(jsonSchedule.getRoute(), jsonSchedule.getInfo());
	}
}
