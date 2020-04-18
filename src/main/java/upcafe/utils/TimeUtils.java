package upcafe.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class TimeUtils {
	
	private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("H:mm");

	public static LocalTime getTime(String time) {
		
		
		return LocalTime.parse(time, FORMATTER);
	}
	
	public static LocalTime getTimeNow() {
//		LocalDateTime fullNow = LocalDateTime.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//		String now = formatter.format(fullNow);
//		
//		return LocalTime.parse(now, formatter);
		
		LocalDateTime fullNow = LocalDateTime.now().minus(12, ChronoUnit.HOURS);
		String now = FORMATTER.format(fullNow);
		
		return LocalTime.parse(now, FORMATTER);
	}
	
	public static LocalTime getTimeNowWithIntervalPadding(int interval) {
		LocalTime now = getTimeNow();
		LocalTime nowWithPadding = now.plusMinutes(interval);

		return nowWithPadding;
	}
	
	public static LocalTime getTimeWithNegativePadding(int interval, LocalTime time) {
		LocalTime timeWithPadding = time.minusMinutes(interval);

		return timeWithPadding;
	}
	
	public static String getTimeString(LocalTime time) {
		return FORMATTER.format(time);
	}
}
