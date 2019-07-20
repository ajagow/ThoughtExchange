package com.mad.thoughtExchange.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GeneralUtils {

    String getCountdown(Date createDate) {
        Calendar inputCal = Calendar.getInstance();
        inputCal.setTime(createDate);
        Calendar currentCal = Calendar.getInstance();

        long inputCalMillisValue = inputCal.getTimeInMillis();
        long currentCalMillisValue = currentCal.getTimeInMillis();

        long difference = currentCalMillisValue - inputCalMillisValue;

        long hours = TimeUnit.MILLISECONDS.toHours(difference);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(difference) - TimeUnit.HOURS.toMinutes(hours);

        return String.format("%d hrs %d min", hours, minutes);
    }
}
