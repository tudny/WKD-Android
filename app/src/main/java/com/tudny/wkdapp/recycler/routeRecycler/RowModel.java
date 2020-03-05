package com.tudny.wkdapp.recycler.routeRecycler;

import lombok.ToString;

@ToString
public class RowModel {
	final String stationName;
	final String arrivalTime;
	final String departureTime;
	final Integer boldType;

	public RowModel(String stationName, String arrivalTime, String departureTime, Integer boldType) {
		this.stationName = stationName;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.boldType = boldType;
	}
}
