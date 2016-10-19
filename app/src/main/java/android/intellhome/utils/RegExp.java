package android.intellhome.utils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Quentin on 19/10/2016.
 */
public class RegExp {

    static String regex = "^(201\\d) (\\d\\d?) (\\d\\d?\\s?)$";

    /**
     * Test against user date input to see whether it follows correct format.
     * @param exp date sttring
     * @return return true if the input follows format
     */
    public static boolean isExpFormatCorrect(String exp) {
        return Pattern.compile(regex).matcher(exp).find();
    }

    /**
     * Convert date in the string form "yyyy mm dd" into GregorianCalendar.
     * Since this method is invoked only after {@code isExpFormatCorrect} returns
     * true, it is unnecessary to check against null.
     *
     * @param date date acquired from EditText
     * @return GregorianCalendar object that denotes time
     */
    public static GregorianCalendar text2Date(String date) {
        Matcher m = Pattern.compile(regex).matcher(date);
        if (m.find()) {
            int year = string2Int(m.group(1));
            int month = string2Int(m.group(2)) - 1;
            int day = string2Int(m.group(3));
            return new GregorianCalendar(year, month, day);
        } else
            return null;
    }

    private static int string2Int(String text) {
        return Integer.valueOf(text);
    }
}
