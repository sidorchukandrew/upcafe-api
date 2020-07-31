package upcafe.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SquareUtils {

    public static String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    public static String getSquareLocationId() {

//        return System.getenv("SQUARE_LOCATION");
        return "1RF0H24Q11X26";
    }

    public static String toUtcTimestamp(LocalDateTime time) {
        ZonedDateTime zonedTimeStamp = time.atZone(ZoneId.of("UTC"));
        return zonedTimeStamp.format(DateTimeFormatter.ISO_INSTANT);
    }
}
