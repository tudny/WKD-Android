package com.tudny.wkdapp.recycler.scheduleRecycler;

public class RowModel {
	final String departureTime;
	final String timeToDeparture;
	final String destinationStation;
	final String trainNumber;

//	public RowModel(String departureTime, String timeToDeparture) {
//		this.departureTime = departureTime;
//		this.timeToDeparture = timeToDeparture;
//	}

	public RowModel(String departureTime, String timeToDeparture, String destinationStation, String trainNumber) {
		this.departureTime = departureTime;
		this.timeToDeparture = timeToDeparture;
		this.destinationStation = destinationStation;
		this.trainNumber = trainNumber;
	}
}
