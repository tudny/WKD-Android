package com.tudny.wkdapp.tickets;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.Getter;

@SuppressWarnings("unused")
public class WKDTickets {

	static class TicketsCustomReliefs {
		public static final Integer LUGGAGE = "luggage".hashCode();
		static final Integer RAILWAYMAN = 999; //"railwayman".hashCode();
		public static final Integer DRIVER = "driver".hashCode();
		public static final Integer URBAN2 = "urban2".hashCode();
		public static final Integer URBAN3 = "urban3".hashCode();
	}

	public enum Relief{

		NORMAL(0, "Normalny", "taryfa normalna"),
		RELIEF33(33, "Ulgowy 33%", "33% - nauczyciele"),
		RELIEF37(37, "Ulgowy 37%", "37% - niewidomi, cywilne niewidome ofiary działań wojennych całkowicie niezdolne do pracy"),
		RELIEF49(49, "Ulgowy 49%", "49% - dzieci i uczniowie, rodzice oraz małżonkowie rodziców z rodzin wielodzietnych legitymujący się Kartą Dużej Rodziny"),
		RELIEF50(50, "Ulgowy 50%", "50% - seniorzy powyżej 60 roku życia, pracownicy sfery budżetowej"),
		RELIEF51(51, "Ulgowy 51%", "51% - studenci, doktoranci"),
		//RELIEF70(70, "ulgowy 70%"),
		RELIEF78(78, "Ulgowy 78%", "78% - dzieci i młodzież niepełnosprawna"),
		RELIEF80(80, "Ulgowy 80%", "80% - dzieci i współmałżonkowie pracowników kolejowych"),
		//RELIEF85(85, "ulgowy 85%"),
		//RELIEF95(95, "ulgowy 95%"),
		RELIEF93(93, "Ulgowy 93%", "93% - osoby niewidome uznane za niezdolne do samodzielnej egzystencji"),
		//RELIEF100(100, "ulgowy 100%"),
		//LUGGAGE(TicketsCustomReliefs.LUGGAGE, "bagaż"),
		RAILWAYMAN(TicketsCustomReliefs.RAILWAYMAN, "Kolejarz", "Kolejarz – pracownicy, emeryci, renciści kolejowi");
		//DRIVER(TicketsCustomReliefs.DRIVER, "maszynista"),
		//URBAN2(TicketsCustomReliefs.URBAN2, "miejski 2"),
		//URBAN3(TicketsCustomReliefs.URBAN3, "miejski 3")

		@Getter
		private final Integer type;
		@Getter
		private final String name;
		@Getter
		private final String fullName;

		Relief(Integer type, String name, String fullName){
			this.type = type;
			this.name = name;
			this.fullName = fullName;
		}

		public static ArrayList<Relief> getArrayListOfRelies(){
			return new ArrayList<>(Arrays.asList(Relief.values()));
		}

		public static ArrayList<String> getArrayListOfNames(){
			ArrayList<String> list = new ArrayList<>();
			for(Relief relief : Relief.values()){
				list.add(relief.getName());
			}
			return list;
		}

		public static ArrayList<String> getArrayListOfFullNames(){
			ArrayList<String> list = new ArrayList<>();
			for(Relief relief : Relief.values()){
				list.add(relief.getFullName());
			}
			return list;
		}

		public static Relief[] getArrayOfReliefs(){
			return Relief.values();
		}

		public static String[] getArrayOfNames(){
			String[] list = new String[Relief.values().length];
			for(int i = 0; i < Relief.values().length; i++){
				list[i] = Relief.values()[i].getName();
			}
			return list;
		}

		public static String[] getArrayOfFullNames(){
			String[] list = new String[Relief.values().length];
			for(int i = 0; i < Relief.values().length; i++){
				list[i] = Relief.values()[i].getFullName();
			}
			return list;
		}

		public static Relief[] getArrayOfReliefsMiddle(){
			return new Relief[]{NORMAL, RELIEF50};
		}

		public static String[] getArrayOfNamesMiddle(){
			String[] tab = new String[getArrayOfReliefsMiddle().length];
			for(int i = 0; i < getArrayOfReliefsMiddle().length; i++){
				tab[i] = getArrayOfReliefsMiddle()[i].getFullName();
			}
			return tab;
		}

		public static Relief[] getArrayOfReliefsShort(){
			return new Relief[]{NORMAL};
		}

		public static String[] getArrayOfNamesShort(){
			String[] tab = new String[getArrayOfReliefsShort().length];
			for(int i = 0; i < getArrayOfReliefsShort().length; i++){
				tab[i] = getArrayOfReliefsShort()[i].getFullName();
			}
			return tab;
		}
	}

	public enum Zone{
		ZONE1(1, "I strefa"),
		ZONE2(2, "II strefa"),
		ZONE3(3, "III strefa");

		@Getter
		private final Integer zone_number;

		@Getter
		private final String name;

		Zone(Integer zone_no, String name){
			zone_number = zone_no;
			this.name = name;
		}
	}

	public enum DistanceZone{
		ZONE0(0, -9999, 0),
		ZONE1(1, 1, 5),
		ZONE2(2, 6, 10),
		ZONE3(3, 11, 15),
		ZONE4(4, 16, 20),
		ZONE5(5, 21, 9999);

		@Getter
		private final Integer number;
		private final Integer down;
		private final Integer up;

		DistanceZone(Integer number, Integer down, Integer up) {
			this.number = number;
			this.down = down;
			this.up = up;
		}

		public static DistanceZone findDistanceZoneByDistance(Integer distance){
			DistanceZone distanceZone = null;
			for(DistanceZone zone : values()){
				if(zone.down <= distance && distance <= zone.up){
					distanceZone = zone;
				}
			}
			return distanceZone;
		}
	}

	public enum TicketType{
		NAMED(1, "Imienny"),
		UNNAMED(2, "Bezimienny");

		@Getter
		private final Integer index;
		@Getter
		private final String name;

		TicketType(Integer index, String name){
			this.index = index;
			this.name = name;
		}

		public static TicketType[] getArrayOfTicketTypes(){
			return TicketType.values();
		}

		public static String[] getArrayOfTicketTypeNames(){
			String[] names = new String[TicketType.values().length];
			for(int i = 0; i < TicketType.values().length; i++){
				names[i] = TicketType.values()[i].getName();
			}
			return names;
		}
	}

	public enum Period{

		//NAMED
		WEEKLY(1, "Tygodniowy"),
		FORTNIGHTLY(2, "Dwutygodniowy"),
		MONTHLY(3, "Miesięczny"),
		QUARTERLY(4, "Kwartalny"),

		//NO NAMED
		MONTHLY_UNNAMED(5, "Miesięczny"),
		MONTHLY_WEEKEND_UNNAMED(6, "Miesięczny weekendowy"),
		MONTHLY_BIKE_UNNAMED(7, "Miesięczny ROWER");

		@Getter
		private final Integer index;
		@Getter
		private final String name;

		Period(Integer index, String name) {
			this.index = index;
			this.name = name;
		}

		public static Period[] getArrayOfPeriodsNamed(){
			return new Period[]{WEEKLY, FORTNIGHTLY, MONTHLY, QUARTERLY};
		}

		public static String[] getArrayOfPeriodNamedNames(){
			String[] names = new String[getArrayOfPeriodsNamed().length];
			for(int i = 0; i < getArrayOfPeriodsNamed().length; i++){
				names[i] = getArrayOfPeriodsNamed()[i].getName();
			}
			return names;
		}

		public static Period[] getArrayOfPeriodsUnNamed(){
			return new Period[]{MONTHLY_UNNAMED, MONTHLY_WEEKEND_UNNAMED, MONTHLY_BIKE_UNNAMED, QUARTERLY};
		}

		public static String[] getArrayOfPeriodsUnNamedNames(){
			String[] names = new String[getArrayOfPeriodsUnNamed().length];
			for(int i = 0; i < getArrayOfPeriodsUnNamed().length; i++){
				names[i] = getArrayOfPeriodsUnNamed()[i].getName();
			}
			return names;
		}
	}

	public enum Direction{
		ONEWAY(1, "W jedną stronę"),
		RETURNWAY(0, "Tam i z powrotem");

		@Getter
		private final Integer index;
		@Getter
		private final String name;

		Direction(Integer index, String name) {
			this.index = index;
			this.name = name;
		}

		public static Direction[] getArrayOfDirections(){
			return Direction.values();
		}

		public static String[] getArrayOfDirectionNames(){
			String[] names = new String[Direction.values().length];
			for(int i = 0; i < Direction.values().length; i++){
				names[i] = Direction.values()[i].getName();
			}
			return names;
		}
	}

	// ZONE - 1, 2, 3
	// RELIEF - 0, 33, 37, 49, 50, 51, 70, 78, 80, 85, 93, 100, LUGGAGE, RAILWAYMAN, DRIVER, URBAN2, URBAN3
	public static Double singleTicket(Zone zone, Relief relief){

		double price = 1000000000.0;

		if(zone == Zone.ZONE1){

			/*if(relief == Relief.NORMAL*//* || relief == Relief.LUGGAGE*//*)
				price = 3.60;
			else if(relief == Relief.RELIEF33)
				price = 2.40;
			else if(relief == Relief.RELIEF37)
				price = 2.30;
			else if(relief == Relief.RELIEF49)
				price = 1.85;
			else if(relief == Relief.RELIEF50)
				price = 1.80;
			else if(relief == Relief.RELIEF51)
				price = 1.76;
//			else if(relief == Relief.RELIEF70)
//				price = 1.80;
			else if(relief == Relief.RELIEF78)
				price = 0.80;
			else if(relief == Relief.RELIEF80)
				price = 1.80;
//			else if(relief == Relief.RELIEF85)
//				price = 0.50;
			else if(relief == Relief.RELIEF93)
				price = 0.25;
//			else if(relief == Relief.RELIEF95)
//				price = 0.20;
//			else if(relief == Relief.RELIEF100)
//				price = 0.00;
			else if(relief == Relief.RAILWAYMAN)
				price = 1.80;
//			else if(relief == Relief.DRIVER)
//				price = 5.0;
//			else if(relief == Relief.URBAN2)
//				price = 2.00;
//			else if(relief == Relief.URBAN3)
//				price = 0.00;
			else
				price = 18.00;
*/
			if(relief == Relief.NORMAL /*|| relief == 'luggage'*/)
				price = 4.10;
			else if(relief == Relief.RELIEF33)
				price = 2.75;
			else if(relief == Relief.RELIEF37)
				price = 2.58;
			else if(relief == Relief.RELIEF49)
				price = 2.09;
			else if(relief == Relief.RELIEF50)
				price = 2.05;
			else if(relief == Relief.RELIEF51)
				price = 2.01;
			else if(relief == Relief.RELIEF78)
				price = 0.90;
			else if(relief == Relief.RELIEF80)
				price = 2.05;
//			else if(relief == Relief.RELIEF85)
//				price = 0.62;
			else if(relief == Relief.RELIEF93)
				price = 0.29;
//			else if(relief == Relief.RELIEF95)
//				price = 0.21;
//			else if(relief == Relief.RELIEF100)
//				price = 0.00;
			else if(relief == Relief.RAILWAYMAN)
				price = 2.05;
//			else if(relief == 'driver')
//				price = 5.50;
//			else if(relief == 'urban2')
//				price = 2.26;
//			else if(relief == 'urban3')
//				price = 0.00;
			else
				price = 20.50;

		} else if(zone == Zone.ZONE2){
			/*if(relief == Relief.NORMAL*//* || relief == Relief.LUGGAGE*//*)
				price = 4.80;
			else if(relief == Relief.RELIEF33)
				price = 3.20;
			else if(relief == Relief.RELIEF37)
				price = 3.00;
			else if(relief == Relief.RELIEF49)
				price = 2.45;
			else if(relief == Relief.RELIEF50)
				price = 2.40;
			else if(relief == Relief.RELIEF51)
				price = 2.35;
//			else if(relief == Relief.RELIEF70)
//				price = 1.80;
			else if(relief == Relief.RELIEF78)
				price = 1.00;
			else if(relief == Relief.RELIEF80)
				price = 1.80;
//			else if(relief == Relief.RELIEF85)
//				price = 0.70;
			else if(relief == Relief.RELIEF93)
				price = 0.34;
//			else if(relief == Relief.RELIEF95)
//				price = 0.25;
//			else if(relief == Relief.RELIEF100)
//				price = 0.00;
			else if(relief == Relief.RAILWAYMAN)
				price = 1.80;
//			else if(relief == Relief.DRIVER)
//				price = 6.0;
			else
				price = 24.00;*/

			if(relief == Relief.NORMAL/* || relief == 'luggage'*/)
				price = 5.50;
			else if(relief == Relief.RELIEF33)
				price = 3.69;
			else if(relief == Relief.RELIEF37)
				price = 3.47;
			else if(relief == Relief.RELIEF49)
				price = 2.81;
			else if(relief == Relief.RELIEF50)
				price = 2.75;
			else if(relief == Relief.RELIEF51)
				price = 2.70;
			else if(relief == Relief.RELIEF78)
				price = 1.21;
			else if(relief == Relief.RELIEF80)
				price = 2.05;
//			else if(relief == Relief.RELIEF85)
//				price = 0.83;
			else if(relief == Relief.RELIEF93)
				price = 0.39;
//			else if(relief == Relief.RELIEF95)
//				price = 0.28;
//			else if(relief == Relief.RELIEF100)
//				price = 0.00;
			else if(relief == Relief.RAILWAYMAN)
				price = 2.05;
//			else if(relief == 'driver')
//				price = 6.70;
			else
				price = 27.50;

		} else if(zone == Zone.ZONE3){
			/*if(relief == Relief.NORMAL*//* || relief == Relief.LUGGAGE*//*)
				price = 7.00;
			else if(relief == Relief.RELIEF33)
				price = 4.70;
			else if(relief == Relief.RELIEF37)
				price = 4.40;
			else if(relief == Relief.RELIEF49)
				price = 3.60;
			else if(relief == Relief.RELIEF50)
				price = 3.50;
			else if(relief == Relief.RELIEF51)
				price = 3.43;
//			else if(relief == Relief.RELIEF70)
//				price = 2.10;
			else if(relief == Relief.RELIEF78)
				price = 1.50;
			else if(relief == Relief.RELIEF80)
				price = 1.80;
//			else if(relief == Relief.RELIEF85)
//				price = 1.10;
			else if(relief == Relief.RELIEF93)
				price = 0.49;
//			else if(relief == Relief.RELIEF95)
//				price = 0.35;
//			else if(relief == Relief.RELIEF100)
//				price = 0.00;
			else if(relief == Relief.RAILWAYMAN)
				price = 1.80;
//			else if(relief == Relief.DRIVER)
//				price = 8.0;
			else
				price = 35.00;*/

			if(relief == Relief.NORMAL /* || relief == 'luggage'*/)
				price = 8.00;
			else if(relief == Relief.RELIEF33)
				price = 5.36;
			else if(relief == Relief.RELIEF37)
				price = 5.04;
			else if(relief == Relief.RELIEF49)
				price = 4.08;
			else if(relief == Relief.RELIEF50)
				price = 4.00;
			else if(relief == Relief.RELIEF51)
				price = 3.92;
			else if(relief == Relief.RELIEF78)
				price = 1.76;
			else if(relief == Relief.RELIEF80)
				price = 2.05;
//			else if(relief == Relief.RELIEF85)
//				price = 1.20;
			else if(relief == Relief.RELIEF93)
				price = 0.56;
//			else if(relief == Relief.RELIEF95)
//				price = 0.40;
//			else if(relief == Relief.RELIEF100)
//				price = 0.00;
			else if(relief == Relief.RAILWAYMAN)
				price = 2.05;
//			else if(relief == 'driver')
//				price = 9;
			else
				price = 40.00;
		}

		return price;
	}

	public static Double seasonTicket(DistanceZone distanceZone, Period period, TicketType ticketType, Direction direction, Relief reliefClass) throws Exception{
		Integer distance = distanceZone.getNumber();
		Integer season = period.getIndex();
		Integer name = ticketType.getIndex();
		boolean oneWay = (direction.getIndex() == 1);
		Integer relief = reliefClass.getType();

		if(distanceZone == DistanceZone.ZONE0){
			throw new Exception("Go on foot!");
		}

		double price;

		if(season == 1){
			if(name == 1){
				if(relief == 0 || relief == 50){
					if(relief == 0){
						if(distance == 1)
							price = (oneWay) ? 18.00 : 36.00;
						else if(distance == 2)
							price = (oneWay) ? 24.50 : 49.00;
						else if(distance == 3)
							price = (oneWay) ? 27.50 : 55.00;
						else if(distance == 4)
							price = (oneWay) ? 34.50 : 69.00;
						else
							price = (oneWay) ? 44.50 : 89.00;
					} else {
						if(distance == 1)
							price = (oneWay) ? 9.00 : 18.00;
						else if(distance == 2)
							price = (oneWay) ? 12.25 : 24.50;
						else if(distance == 3)
							price = (oneWay) ? 13.75 : 27.50;
						else if(distance == 4)
							price = (oneWay) ? 17.25 : 34.50;
						else
							price = (oneWay) ? 22.25 : 44.50;
					}
					return price;
				} else {
					throw new Exception("Bilet tygodniowy można kupić tylko wg. taryfy normalnej lub z ulgą handlową 50%");
				}
			} else {
				throw new Exception("Bilet tygodniowy musi być imienny");
			}
		}
  /*
  Okres: dwutygodniowe -> season = 2
  Imienne: tylko imienne -> name = 1
  W jedną stronę: w obie -> onWay = 0 or onWay = 1
  Ulga: 0 lub 50 -> relief = 0 or relief = 50
  */
		if(season == 2){
			if(name == 1){
				if(relief == 0 || relief == 50){
					if(relief == 0){
						if(distance == 1)
							price = (oneWay) ? 33.50 : 67.00;
						else if(distance == 2)
							price = (oneWay) ? 45.00 : 90.00;
						else if(distance == 3)
							price = (oneWay) ? 48.50 : 97.00;
						else if(distance == 4)
							price = (oneWay) ? 64.00 : 128.00;
						else
							price = (oneWay) ? 82.50 : 165.00;
					} else {
						if(distance == 1)
							price = (oneWay) ? 16.75 : 33.50;
						else if(distance == 2)
							price = (oneWay) ? 22.50: 45.00;
						else if(distance == 3)
							price = (oneWay) ? 24.25 : 48.50;
						else if(distance == 4)
							price = (oneWay) ? 32.00 : 64.00;
						else
							price = (oneWay) ? 41.25 : 82.50;
					}
					return price;
				} else {
					throw new Exception("Bilet dwutygodniowy można kupić tylko wg. taryfy normalnej lub z ulgą handlową 50%");
				}
			} else {
				throw new Exception("Bilet dwutygodniowy musi być imienny");
			}
		}
  /*
  Okres: miesięczny -> season = 3
  Imienne: name = 1 or name = 0
  W jedną stronę: w obie -> onWay = 0 or onWay = 1
  Ulga: 0, 33, 37, 49, 50, 51, 78, 80, 93
  */
		if(season == 3){
			if(name == 1){
				if(relief == 0 || relief == 33 || relief == 37 || relief == 49 || relief == 50 || relief == 51 || relief == 78 || relief == 80 || relief == 93 || relief == 999){
					if(relief == 0){
						if(distance == 1)
							price = (oneWay) ? 54.50 : 109.00;
						else if(distance == 2)
							price = (oneWay) ? 73.00 : 146.00;
						else if(distance == 3)
							price = (oneWay) ? 79.00 : 158.00;
						else if(distance == 4)
							price = (oneWay) ? 97.50 : 195.00;
						else
							price = (oneWay) ? 124.00 : 248.00;
					} else if(relief == 33){
						if(distance == 1)
							price = (oneWay) ? 36.52 : 73.03;
						else if(distance == 2)
							price = (oneWay) ? 48.91 : 97.82;
						else if(distance == 3)
							price = (oneWay) ? 52.93 : 105.86;
						else if(distance == 4)
							price = (oneWay) ? 65.33 : 130.65;
						else
							price = (oneWay) ? 83.08 : 166.16;
					} else if(relief == 37){
						if(distance == 1)
							price = (oneWay) ? 34.34 : 68.67;
						else if(distance == 2)
							price = (oneWay) ? 45.99 : 91.98;
						else if(distance == 3)
							price = (oneWay) ? 49.77 : 99.54;
						else if(distance == 4)
							price = (oneWay) ? 61.43 : 122.85;
						else
							price = (oneWay) ? 78.12 : 156.24;
					} else if(relief == 49){
						if(distance == 1)
							price = (oneWay) ? 27.80 : 55.59;
						else if(distance == 2)
							price = (oneWay) ? 37.23 : 74.46;
						else if(distance == 3)
							price = (oneWay) ? 40.29 : 80.58;
						else if(distance == 4)
							price = (oneWay) ? 49.73 : 99.45;
						else
							price = (oneWay) ? 63.24 : 126.48;
					} else if(relief == 50){
						if(distance == 1)
							price = (oneWay) ? 27.25 : 54.50;
						else if(distance == 2)
							price = (oneWay) ? 36.50 : 73.00;
						else if(distance == 3)
							price = (oneWay) ? 39.50 : 79.00;
						else if(distance == 4)
							price = (oneWay) ? 48.75 : 97.50;
						else
							price = (oneWay) ? 62.00 : 124.00;
					} else if(relief == 51){
						if(distance == 1)
							price = (oneWay) ? 26.71 : 53.41;
						else if(distance == 2)
							price = (oneWay) ? 35.77 : 71.54;
						else if(distance == 3)
							price = (oneWay) ? 38.71 : 77.42;
						else if(distance == 4)
							price = (oneWay) ? 47.78 : 95.55;
						else
							price = (oneWay) ? 60.76 : 121.52;
					} else if(relief == 78){
						if(distance == 1)
							price = (oneWay) ? 11.99 : 23.98;
						else if(distance == 2)
							price = (oneWay) ? 16.06 : 32.12;
						else if(distance == 3)
							price = (oneWay) ? 17.38 : 34.76;
						else if(distance == 4)
							price = (oneWay) ? 21.45 : 42.90;
						else
							price = (oneWay) ? 27.28 : 54.56;
					} else if(relief == 80){
						if(distance == 1)
							price = (oneWay) ? 10.90 : 21.80;
						else if(distance == 2)
							price = (oneWay) ? 14.60 : 29.20;
						else if(distance == 3)
							price = (oneWay) ? 15.80 : 31.60;
						else if(distance == 4)
							price = (oneWay) ? 19.50 : 39.00;
						else
							price = (oneWay) ? 24.80 : 49.60;
					} else if(relief == 93){
						if(distance == 1)
							price = (oneWay) ? 3.82 : 7.63;
						else if(distance == 2)
							price = (oneWay) ? 5.11 : 10.22;
						else if(distance == 3)
							price = (oneWay) ? 5.53 : 11.06;
						else if(distance == 4)
							price = (oneWay) ? 6.83 : 13.65;
						else
							price = (oneWay) ? 8.68 : 17.36;
					} else {
						price = 14.81;
					}
					return price;
				} else {
					throw new Exception("Bilet miesięczny imienny można kupić tylko wg. taryfy normalnej lub z ulgą 33%, 37%, 49%, 50%, 51%, 78%, 80%, 93%");
				}
			}
		}
  /*
  Okres: kwartalny -> season = 4
  Imienne: name = 1 or name = 0
  W jedną stronę: w obie -> onWay = 0 or onWay = 1
  Ulga: 0, 33, 37, 49, 50, 51, 78, 80, 93
  */
		if(season == 4){
			if(name == 1){
				if(relief == 0 || relief == 50){
					if(relief == 0){
						if(distance == 1)
							price = (oneWay) ? 136.00 : 272.00;
						else if(distance == 2)
							price = (oneWay) ? 182.00 : 364.00;
						else if(distance == 3)
							price = (oneWay) ? 199.50 : 399.00;
						else if(distance == 4)
							price = (oneWay) ? 244.00 : 488.00;
						else
							price = (oneWay) ? 310.50 : 621.00;
					} else {
						if(distance == 1)
							price = (oneWay) ? 68.00 : 136.00;
						else if(distance == 2)
							price = (oneWay) ? 91.00 : 182.00;
						else if(distance == 3)
							price = (oneWay) ? 99.75 : 199.50;
						else if(distance == 4)
							price = (oneWay) ? 122.00 : 244.00;
						else
							price = (oneWay) ? 155.25 : 310.50;
					}
					return price;
				} else {
					throw new Exception("Bilet kwartalny imienny można kupić tylko wg. taryfy normalnej lub z ulgą 50%");
				}
			}
		}
		// miesieczny bezimienny
		if(season == 5){
			if(distance == 1)
				price = 163.00;
			else if(distance == 2)
				price = 217.00;
			else if(distance == 3)
				price = 245.00;
			else if(distance == 4)
				price = 293.00;
			else
				price = 374.00;
			return price;
		}
		// kwartalny bezimienny
		if(season == 6){
			if(distance == 1)
				price = 407.00;
			else if(distance == 2)
				price = 544.00;
			else if(distance == 3)
				price = 614.00;
			else if(distance == 4)
				price = 732.00;
			else
				price = 931.00;
			return price;
		}
		// miesieczny-weekendowy bezimienny
		if(season == 7){
			if(distance == 1)
				price = 37.00;
			else if(distance == 2)
				price = 48.00;
			else if(distance == 3)
				price = 56.00;
			else if(distance == 4)
				price = 66.00;
			else
				price = 86.00;
			return price;
		}
		// miesieczny ROWER - bezimienny
		if(season == 8){
			if(distance == 1)
				price = 51.50;
			else if(distance == 2)
				price = 51.50;
			else if(distance == 3)
				price = 51.50;
			else if(distance == 4)
				price = 51.50;
			else
				price = 51.50;
			return price;
		}

		throw new Exception("Unknown error");
	}
}
