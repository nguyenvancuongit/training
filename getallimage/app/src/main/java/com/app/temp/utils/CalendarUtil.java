package com.app.temp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Windows 7 on 1/4/2016.
 */
public class CalendarUtil {
    /**
     * compare date time with current date time
     *
     * @param format yyyy-MM-dd HH:mm:ss
     * @param date   2016-01-15 23:00:00
     * @return 1 is bigger, -1 is smaller
     */
    public static int compareDateTimeWithCurrentDateTime(String format, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        try {
            return sdf.parse(date).compareTo(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * get all date between two date
     *
     * @param format      ex yyyy-MM-dd
     * @param dateString1 ex 2016-01-15
     * @param dateString2 ex 2016-02-15
     * @return list date
     */
    public static List<Date> getDates(String format, String dateString1, String dateString2) {
        ArrayList<Date> dates = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat(format, Locale.US);

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    /**
     * convert local time on device to GMT 00 timezone
     *
     * @param format ex "yyyy-MM-dd hh:mm:ss"
     * @param date   ex yyyy-MM-dd
     * @return ex hh:mm:ss
     */
    public static String convertTimeTo00Timezone(String format, String date, String time) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar cal = Calendar.getInstance();
        String[] tempDate = date.split("-");
        cal.set(Calendar.YEAR, Integer.valueOf(tempDate[0]));
        cal.set(Calendar.MONTH, Integer.valueOf(tempDate[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tempDate[2]) - 1); // before 1 day
        String[] tempTime = time.split(":");
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(tempTime[0]));
        cal.set(Calendar.MINUTE, Integer.valueOf(tempTime[1]));
        cal.set(Calendar.SECOND, Integer.valueOf(tempTime[2]));
        return formatter.format(cal.getTime());
    }
}
