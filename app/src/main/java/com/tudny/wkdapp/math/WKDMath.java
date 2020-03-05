package com.tudny.wkdapp.math;

@SuppressWarnings("unused")
public class WKDMath {

	/**
	 * Radius of earth in kilometers
	 * */
	public static final Double R = 6371.0;

	/**
	 * Function returns distance between two points on sphere from its coordinates
	 *
	 * @param lat1D latitude of first point
	 * @param lat2D latitude of second point
	 * @param long1D longitude of first point
	 * @param long2D longitude of second point
	 *
	 * @return Distance between two points as Double value
	 * */
	public static Double distanceOnEarth(Double lat1D, Double long1D, Double lat2D, Double long2D){

		Double lat1 = Math.toRadians(lat1D);
		Double long1 = Math.toRadians(long1D);
		Double lat2 = Math.toRadians(lat2D);
		Double long2 = Math.toRadians(long2D);

		double sinSqrtLat = Math.pow(Math.sin((lat2 - lat1) / 2), 2);
		double sinSqrtLong = Math.pow(Math.sin((long2 - long1) / 2), 2);
		double product = sinSqrtLat + Math.cos(lat1) * Math.cos(lat2) * sinSqrtLong;
		double arcsinSqrt = Math.asin(Math.sqrt(product));

		double score = 2d * R * arcsinSqrt;

		//Log.d("DistanceOnEarth", "Calculated distance is " + score + " km.");

		return score * 1d;
	}
}
