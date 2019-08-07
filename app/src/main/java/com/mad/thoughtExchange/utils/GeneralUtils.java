package com.mad.thoughtExchange.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * General Utils class for dealing with converting JSON times to Java readable times.
 */

public class GeneralUtils {

    // get how much time is left
    static String getCountdown(Date createDate, int countdownHourTime) {
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

    // convert date to string readable format
    static String getPostedDate(Date created) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM-dd-yyyy");
        String stringDate = sdf.format(created);
        return stringDate;
    }

    // determine if given date is over 48 hours ago
    static boolean isFinishedOnMarket(Date createDate) {
        Date current = new Date();
        int differenceFloor = hourDifference(current, createDate);

        return differenceFloor > 48;
    }

    // get number of hours between date1 and date2
    private static int hourDifference(Date date1, Date date2) {
        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
    }

}
