package com.tudny.wkdapp.core.data;

import com.tudny.wkdapp.core.RespondJson;

import java.time.LocalTime;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Data, but not raw
@ToString
@Getter
@Setter
public class AdditionalInfo {

	private Integer distance;
	private LocalTime time;

	private AdditionalInfo(String distanceT, String timeT){
		try {
			distance = Integer.parseInt(distanceT);
			time = LocalTime.parse(timeT);
		} catch (Exception e){
			distance = -1;
			time = LocalTime.of(0, 0);
		}
	}

	AdditionalInfo(RespondJson.JsonScheduleAdditionalInfo additionalInfo){
		this(additionalInfo.getDistance(), additionalInfo.getTime());
	}

	public String toPrintString(){
		return String.format(Locale.ENGLISH,"Distance: %d | Time: %02d:%02d", distance, time.getHour(), time.getMinute());
	}
}
