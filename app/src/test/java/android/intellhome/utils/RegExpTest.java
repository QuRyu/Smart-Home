package android.intellhome.utils;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Quentin on 19/10/2016.
 */
public class RegExpTest {

    String date1 = "2015 04 21";
    String date2 = "2016 02 9";
    String date3 = "2015 2 12";
    String date4 = "201 2 31";

    @Test
    public void testIsExpFormatCorrect() throws Exception {
        isTrue(date1);
        isTrue(date2);
        isTrue(date3);
        Assert.assertTrue(!RegExp.isExpFormatCorrect(date4));
    }

    private void isTrue(String date) {
        Assert.assertTrue(RegExp.isExpFormatCorrect(date));
    }
}