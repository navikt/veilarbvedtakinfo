package no.nav.fo.veilarbvedtakinfo.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date addMinutesToDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
