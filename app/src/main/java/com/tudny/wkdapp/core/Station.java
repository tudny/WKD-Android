package com.tudny.wkdapp.core;


import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;

// ENUM storing data of Stations
@SuppressWarnings("unused")
public enum Station {
	WWA_SRODMIESCIE_WKD("W-wa Śródmieście WKD", 26, Fork.N, 52.227788, 21.001061, 0, "http://www.wkd.com.pl/images/przystanki/001_Warszawa_Srodmiescie_WKD_2.JPG", "http://www.wkd.com.pl/warszawa-srodmiescie-wkd.html"),
	WWA_OCHOTA_WKD("W-wa Ochota WKD", 25, Fork.N, 52.225597, 20.990477, 1, "http://www.wkd.com.pl/images/przystanki/002_Warszawa_Ochota_WKD.JPG", "http://www.wkd.com.pl/warszawa-ochota-wkd.html"),
	WWA_ZACHODNIA_WKD("W-wa Zachodnia WKD", 24, Fork.N, 52.219493, 20.965755, 3, "http://www.wkd.com.pl/images/przystanki/003_Warszawa_Zachodnia_WKD_2.JPG", "http://www.wkd.com.pl/warszawa-zachodnia-wkd.html"),
	WWA_REDUTA_ORDONA("W-wa Reduta Ordona", 23, Fork.N, 52.214348, 20.947894, 4, "http://www.wkd.com.pl/images/przystanki/004_Warszawa_Reduta_Ordona.JPG", "http://www.wkd.com.pl/warszawa-reduta-ordona.html"),
	WWA_ALEJE_JEROZOLIMSKIE("W-wa Aleje Jerozolimskie", 22, Fork.N, 52.205432, 20.942154, 6, "http://www.wkd.com.pl/images/przystanki/005_Warszawa_Aleje_Jerozolimskie.JPG", "http://www.wkd.com.pl/warszawa-aleje-jerozolimskie.html"),
	WWA_RAKOW("W-wa Raków", 21, Fork.N, 52.194463, 20.935792, 7, "http://www.wkd.com.pl/images/przystanki/006_Warszawa_Rak%C3%B3w.JPG", "http://www.wkd.com.pl/warszawa-rakow.html"),
	WWA_SALOMEA("W-wa Salomea", 20, Fork.N, 52.186598, 20.924865, 8, "http://www.wkd.com.pl/images/przystanki/007_Warszawa_Salomea.JPG", "http://www.wkd.com.pl/warszawa-salomea.html"),
	OPACZ("Opacz", 19, Fork.N, 52.181395, 20.904681, 10, "http://www.wkd.com.pl/images/przystanki/008_Opacz.JPG", "http://www.wkd.com.pl/opacz.html"),
	MICHALOWICE("Michałowice", 18, Fork.N, 52.175357, 20.881329, 11, "http://www.wkd.com.pl/images/przystanki/009_Michalowice.JPG", "http://www.wkd.com.pl/michalowice.html"),
	REGULY("Reguły", 17, Fork.N, 52.170484, 20.859615, 13, "http://www.wkd.com.pl/images/przystanki/010_Reguly.JPG", "http://www.wkd.com.pl/reguly.html"),
	MALICHY("Malichy", 16, Fork.N, 52.169364, 20.841145, 14, "http://www.wkd.com.pl/images/przystanki/011_Malichy.JPG", "http://www.wkd.com.pl/malichy.html"),
	TWORKI("Tworki", 15, Fork.N, 52.169346, 20.823499, 16, "http://www.wkd.com.pl/images/przystanki/012_Tworki.JPG", "http://www.wkd.com.pl/tworki.html"),
	PRUSZKOW_WKD("Pruszków WKD", 14, Fork.N, 52.161592, 20.816559, 17, "http://www.wkd.com.pl/images/przystanki/013_Pruszkow_WKD.JPG", "http://www.wkd.com.pl/pruszkow-wkd.html"),
	KOMOROW("Komorów", 13, Fork.N, 52.148153, 20.811406, 18, "http://www.wkd.com.pl/images/przystanki/014_Komorow.JPG", "http://www.wkd.com.pl/komorow.html"),
	NOWA_WIES_WARSZAWSKA("Nowa Wieś Warszawska", 12, Fork.N, 52.140372, 20.795140, 20, "http://www.wkd.com.pl/images/przystanki/015_Nowa_Wies_Warszawska.JPG", "http://www.wkd.com.pl/nowa-wies-warszawska.html"),
	KANIE_HELENOWSKIE("Kanie Helenowskie", 11, Fork.N, 52.131637, 20.774329, 21, "http://www.wkd.com.pl/images/przystanki/016_Kanie_Helenowskie.JPG", "http://www.wkd.com.pl/kanie-helenowskie.html"),
	OTEBUSY("Otrębusy", 10, Fork.N, 52.126268, 20.761091, 22, "http://www.wkd.com.pl/images/przystanki/017_Otrebusy.JPG", "http://www.wkd.com.pl/otrebusy.html"),
	PODKOWA_LESNA_WSCHODNIA("Podkowa Leśna Wschodnia", 9, Fork.N, 52.123692, 20.737815, 24, "http://www.wkd.com.pl/images/przystanki/018_Podkowa_Lesna_Wschodnia.JPG", "http://www.wkd.com.pl/podkowa-lesna-wschodnia.html"),
	PODKOWA_LESNA_GLOWNA("Podkowa Leśna Główna", 8, Fork.N, 52.122399, 20.725375, 25, "http://www.wkd.com.pl/images/przystanki/019_Podkowa_Lesna_Glowna.JPG", "http://www.wkd.com.pl/podkowa-lesna-glowna.html"),
	PODKOWA_LESNA_ZACHODNIA("Podkowa Leśna Zachodnia", 7, Fork.N, 52.120779, 20.711606, 26, "http://www.wkd.com.pl/images/przystanki/020_Podkowa_Lesna_Zachodnia.JPG", "http://www.wkd.com.pl/podkowa-lesna-zachodnia.html"),

	// Milanówek fork
	POLESIE("Polesie", 27, Fork.M, 52.121742, 20.697664, 27, "http://www.wkd.com.pl/images/przystanki/027_Polesie.JPG", "http://www.wkd.com.pl/polesie.html"),
	MILANOWEK_GRUDOW("Milanówek Grudów", 28, Fork.M, 52.122249, 20.682245, 28, "http://www.wkd.com.pl/images/przystanki/028_Milanowek_Grudow.JPG", "http://www.wkd.com.pl/milanowek-grudow.html"),

	// Grodzisk fork
	KAZIMIEROWKA("Kazimierówka", 6, Fork.G, 52.110917, 20.698145, 27, "http://www.wkd.com.pl/images/przystanki/021_Kazimierowka.JPG", "http://www.wkd.com.pl/kazimierowka.html"),
	BRZOZKI("Brzózki", 5, Fork.G, 52.104964, 20.678092, 29, "http://www.wkd.com.pl/images/przystanki/Brzozki.jpg", "http://www.wkd.com.pl/brzozki.html"),
	GRODZISK_MAZ_OKREZNA("Grodzisk Maz. Okrężna", 4, Fork.G, 52.100717, 20.660121, 30, "http://www.wkd.com.pl/images/przystanki/023_Grodzisk_Maz_Okrezna_2.JPG", "http://www.wkd.com.pl/grodzisk-maz-okrezna.html"),
	GRODZISK_MAZ_PIASKOWA("Grodzisk Maz. Piaskowa", 3, Fork.G, 52.102661, 20.651302, 31, "http://www.wkd.com.pl/images/przystanki/Grodzisk-Maz.-Piaskowa.jpg", "http://www.wkd.com.pl/grodzisk-maz-piaskowa.html"),
	GRODZISK_MAZ_JORDANOWICE("Grodzisk Maz. Jordanowice", 2, Fork.G, 52.103277, 20.636724, 32, "http://www.wkd.com.pl/images/przystanki/025_Grodzisk_Maz_Jordanowice.JPG", "http://www.wkd.com.pl/grodzisk-maz-jordanowice.html"),
	GRODZISK_MAZ_RADONSKA("Grodzisk Maz. Radońska", 1, Fork.G, 52.100549, 20.628119, 33, "http://www.wkd.com.pl/images/przystanki/026_Grodzisk_Maz_Radonska.JPG", "http://www.wkd.com.pl/grodzisk-maz-radonska.html");


	private final String stationName;
	private final Integer stationNumber;
	private final Fork fork;
	private final Location location;
	private final Integer fromWWA;
	private final String imageURL;
	private final String pageURL;

	public String getStationName() {
		return stationName;
	}

	public Integer getStationNumber() {
		return stationNumber;
	}

	public Fork getFork() {
		return fork;
	}

	public Location getLocation() {
		return location;
	}

	public Double getLatitude(){
		return getLocation().getLatitude();
	}

	public Double getLongitude(){
		return getLocation().getLongitude();
	}

	public String getImageURL() {
		return imageURL;
	}

	public String getPageURL() {
		return pageURL;
	}


	// Constructor
	Station(String stationName, Integer stationNumber, Fork forkType, Integer fromWWA, String imageURL, String pageURL) {
		this.stationName = stationName;
		this.stationNumber = stationNumber;
		this.fork = forkType;
		this.fromWWA = fromWWA;
		this.imageURL = imageURL;
		this.pageURL = pageURL;

		location = new Location("");
	}

	Station(String stationName, Integer stationNumber, Fork forkType, Double latitude, Double longitude, Integer fromWWA, String imageURL, String pageURL){
		this(stationName, stationNumber, forkType, fromWWA, imageURL, pageURL);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
	}

	// ArrayList with station names
	public static ArrayList<String> getListOfStationsArrayList() {
		ArrayList<String> listOfStations = new ArrayList<>();

		for(Station station : Station.values()){
			listOfStations.add(station.stationName);
		}

		return listOfStations;
	}

	// String array with station names
	public static String[] getListOfStationStringArray() {
		String[] listOfStations = new String[Station.values().length];

		for(int i = 0; i < Station.values().length; i++){
			Station station = Station.values()[i];
			listOfStations[i] = station.getStationName();
		}

		return listOfStations;
	}

	// Station array with stations
	public static Station[] getListOfStations(){
		return Station.values();
	}

	public static ArrayList<Station> getArrayListOfStations(){
		return new ArrayList<>(Arrays.asList(Station.values()));
	}

	// Compares two Stations
	public boolean equals(Station toCompare){
		return (this.getStationNumber().equals(toCompare.getStationNumber()));
	}

	// Checks if Stations can connect
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean canConnect(Station from, Station to){
		if(from.fork == Fork.G && to.fork == Fork.M) return false;
		return from.fork != Fork.M || to.fork != Fork.G;
	}

	// Fork types (Grodzisk fork, Milanówek fork, Normal fork)
	public enum Fork{
		N(1), // Normal
		G(2), // Grodzisk
		M(3); // Milanówek

		// Fork type
		private final Integer forkType;

		public Integer getForkType() {
			return forkType;
		}

		// Constructor
		Fork(Integer forkType){
			this.forkType = forkType;
		}
	}

	public static Station choseByName(String name){
		Station station = null;
		for(Station stat : Station.getListOfStations()){
			if(stat.getStationName().equals(name)){
				station = stat;
			}
		}
		return station;
	}

	public static Station choseByNumber(Integer id){
		Station station = null;
		for(Station stat : Station.getListOfStations()){
			if(stat.getStationNumber().equals(id)){
				station = stat;
			}
		}
		return station;
	}

	public static Integer distance(Station begin, Station end){
		if(begin.equals(end)) return 0;
		if(!canConnect(begin, end)) return -1;

		return Math.abs(end.fromWWA - begin.fromWWA);
	}

	public Station nextStation() throws Exception {
		int numberOfStations = getArrayListOfStations().size();
		int id = getArrayListOfStations().indexOf(this);
		if(id + 1 < numberOfStations) id++;
		else throw new Exception("No next station.");
		return getArrayListOfStations().get(id);
	}

	public Station previousStation() throws Exception {
		int numberOfStations = getArrayListOfStations().size();
		int id = getArrayListOfStations().indexOf(this);
		if(id - 1 >= 0) id--;
		else throw new Exception("No previous station.");
		return getArrayListOfStations().get(id);
	}
}

