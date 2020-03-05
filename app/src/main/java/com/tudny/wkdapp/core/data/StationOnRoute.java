package com.tudny.wkdapp.core.data;

import com.tudny.wkdapp.core.RespondJson;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Data, but not raw
@Getter
@Setter
@ToString
public class StationOnRoute {
	LocalTime arrival;
	LocalTime departure;
	String name;
	Boolean active;

	private StationOnRoute(String name, String arr, String dep, String act){
		this.name = name;
		if(arr.equals("")) arr = dep;
		if(dep.equals("")) dep = arr;
		arrival = LocalTime.parse(arr);
		departure = LocalTime.parse(dep);
		active = (Integer.parseInt(act) == 1) ? Boolean.TRUE : Boolean.FALSE;
	}

	StationOnRoute(RespondJson.JsonStation jsonStation){
		this(jsonStation.getName(), jsonStation.getArr(), jsonStation.getDep(), jsonStation.getActive());
	}
}
