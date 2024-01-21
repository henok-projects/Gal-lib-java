package com.galsie.lib.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static long secondsBetween(LocalDateTime from, LocalDateTime to){
        return to.toEpochSecond(ZoneOffset.MIN) - to.toEpochSecond(ZoneOffset.MIN);
    }

    public static Date add(Date date, int field, int amount){
       var calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.add(field, amount);
       return calendar.getTime();
    }

    public static Date timezoneFormatted(String date, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, formatter);
        return Date.from(zonedDateTime.toInstant());
    }

}
