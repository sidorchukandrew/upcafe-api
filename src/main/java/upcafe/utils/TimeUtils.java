package upcafe.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

public class TimeUtils {
	
	private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
	private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");

	public static LocalTime getTime(String time) {		
		return LocalTime.parse(time, TIME_FORMATTER);
	}
	
	public static LocalTime getTimeNow() {
		LocalDateTime fullNow = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String now = formatter.format(fullNow);
		
		return LocalTime.parse(now, formatter);
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
		return TIME_FORMATTER.format(time);
	}

	public static LocalDate getMondayOfWeek(LocalDate dateRequest) {

		
		LocalDate now = LocalDate.now();
		if(now.get(ChronoField.DAY_OF_WEEK) == 1) {
			return now;
		}
	
		LocalDate previousMonday = dateRequest.with( TemporalAdjusters.previous( DayOfWeek.MONDAY ) );
		return previousMonday;
	}

	public static LocalDate toLocalDate(String date) {
		return LocalDate.parse(date, DATE_FORMATTER);
	}
}
