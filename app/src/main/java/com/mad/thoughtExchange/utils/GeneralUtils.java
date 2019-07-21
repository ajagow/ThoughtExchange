package com.mad.thoughtExchange.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GeneralUtils {

    String getCountdown(Date createDate, int countdownHourTime) {
        Calendar inputCal = Calendar.getInstance();
        inputCal.setTime(createDate);
        Calendar currentCal = Calendar.getInstance();

        long countdownHourInMillis = TimeUnit.HOURS.toMillis(countdownHourTime);
        long inputCalMillisValue = inputCal.getTimeInMillis() + countdownHourInMillis;
        long currentCalMillisValue = currentCal.getTimeInMillis();

        long difference = inputCalMillisValue - currentCalMillisValue;

        long hours = TimeUnit.MILLISECONDS.toHours(difference);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(difference) - TimeUnit.HOURS.toMinutes(hours);

        return String.format("%d hrs %d min", hours, minutes);
    }
}
