package android.intellhome.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Quentin on 19/10/2016.
 */
public class RegExp {

    static String regex = "^201\\d\\s\\d+\\s\\d+ ";

    public static boolean isExpFormatCorrect(String exp) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(exp);
        return m.find();
    }
}
