package android.intellhome.utils;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Quentin on 19/10/2016.
 */
public class DateUtilTest {

    String startDate1 = "2015 4 1";
    String endDate1 = "2015 4 3";

    String startDate2 = "2016 4 30";
    String endDate2 = "2016 5 1";

    @Test
    public void testCalculateDateDiff() throws Exception {
        Assert.assertEquals(
                2, DateUtil.calculateDateDiff(startDate1, endDate1));
        Assert.assertEquals(
                1, DateUtil.calculateDateDiff(startDate2, endDate2));
    }
}