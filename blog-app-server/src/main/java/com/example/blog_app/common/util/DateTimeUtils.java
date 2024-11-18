package com.example.blog_app.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting date and time objects.
 * Provides methods to format {@link LocalDateTime} into a specified string pattern,
 * including handling time zones.
 */
public class DateTimeUtils {

    /**
     * Default date-time pattern: "E, MMM dd yyyy HH:mm:ss z".
     * Example: "Sun, Nov 17 2024 14:23:45 ICT".
     */
    private static final String DEFAULT_PATTERN = "E, MMM dd yyyy HH:mm:ss z";

    /**
     * Default system time zone.
     */
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    /**
     * Formats a {@link LocalDateTime} using the default pattern and system time zone.
     *
     * @param dateTime the {@link LocalDateTime} to format
     * @return the formatted date-time string, or {@code null} if {@code dateTime} is null
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_PATTERN);
    }

    /**
     * Formats a {@link LocalDateTime} using a specified pattern and system time zone.
     *
     * @param dateTime the {@link LocalDateTime} to format
     * @param pattern  the pattern to use for formatting
     * @return the formatted date-time string, or {@code null} if {@code dateTime} is null
     * @throws IllegalArgumentException if the pattern is invalid
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        ZonedDateTime zonedDateTime = dateTime.atZone(DEFAULT_ZONE_ID);
        return zonedDateTime.format(formatter);
    }
}
