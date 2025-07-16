package util;

import java.time.format.DateTimeFormatter;

public class FormatterUtil {
    public static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss dd MMMM yyyy");
}
