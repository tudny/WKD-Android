package com.tudny.wkdapp.recycler.stationsRecycler;

import com.tudny.wkdapp.core.Station;

import java.util.ArrayList;

public class RowModel {
	final String stationName;

	public RowModel(String stationName){
		this.stationName = stationName;
	}

	public static ArrayList<RowModel> convertArrayListOfStationsToRowModelArrayList(ArrayList<Station> stationsArrayList){
		ArrayList<RowModel> rowModelArrayList = new ArrayList<>();
		for(Station station : stationsArrayList) {
			rowModelArrayList.add(new RowModel(station.getStationName()));
		}
		return rowModelArrayList;
	}
}
