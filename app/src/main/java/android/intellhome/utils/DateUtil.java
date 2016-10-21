package android.intellhome.utils;

import java.util.GregorianCalendar;

/**
 * Created by Quentin on 19/10/2016.
 */
public class DateUtil {

    public static long stringdate2Unixtime(String date) {
        return RegExp.text2Date(date).getTimeInMillis();
    }

    /**
     * Calculate the days of difference between two dates.
     * @param startDate start date for query
     * @param endDate end dat efor query
     * @return days of difference
     */
    public static int calculateDateDiff(String startDate, String endDate) {
        GregorianCalendar startCalendar = RegExp.text2Date(startDate);
        GregorianCalendar endCalendar = RegExp.text2Date(endDate);

        long unixDiff = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        return unixDiff2Days(unixDiff);
    }

    private static int unixDiff2Days(long unixTime) {
        return (int) (unixTime / 86400000);
    }
}
