package android.intellhome.utils;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by Quentin on 19/10/2016.
 */
public class RegExpTest {

    String date1 = "2015 04 21";
    String date2 = "2016 02 9";
    String date3 = "2015 2 12";
    String date4 = "201 2 31";

    GregorianCalendar calendar1 = new GregorianCalendar(2016, 4, 21);
    GregorianCalendar calendar2 = new GregorianCalendar(2016, 2, 9);
    GregorianCalendar calendar3 = new GregorianCalendar(2015, 2, 12);

    @Test
    public void testIsExpFormatCorrect() throws Exception {
        Assert.assertTrue(RegExp.isExpFormatCorrect(date1));
        Assert.assertTrue(RegExp.isExpFormatCorrect(date2));
        Assert.assertTrue(RegExp.isExpFormatCorrect(date3));
        Assert.assertTrue(!RegExp.isExpFormatCorrect(date4));
    }

    @Test
    public void testText2Date() throws Exception {
        GregorianCalendar calendar = RegExp.text2Date(date1);
        System.out.println(calendar.getTime());
        System.out.println(RegExp.text2Date(date2).getTime());
    }
}