package android.intellhome.services;

import android.intellhome.entity.DeviceHistoryData;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunListener;

import static org.junit.Assert.*;

/**
 * Created by Quentin on 20/10/2016.
 */
public class RequestServiceTest {

    DeviceHistoryData data = new DeviceHistoryData();

    String serverSN = "0000000000000107";
    String startDate = "20161007104353";
    String endDate = "20161008004753";

    String expected = "http://192.168.1.135:3000/tools/gethisData/getHis?_hserverSN=0000000000000107&begtime=20161007104353&endtime=20161008004753";

    //{"id":29472,"device_sn":"0001","updatetime":"2016-10-07T07:30:55.000Z","device_state":1,"device_U":220,"device_I":1,"divice_P":0,"device_electricity":8.9,"remark":null}
    @Before
    public void setup() {
        data.device_electricity = 8.9;
        data.device_U = 220;
        data.device_I = 1;
        data.device_P = 0;
        data.id = 29472L;
        data.device_sn = "0001";
        data.device_state = 1;
    }

    @Test
    public void testGetHisData() throws Exception {
        Assert.assertTrue(RequestService.getHisData(serverSN, startDate, endDate) == null);
    }

    @Test
    public void testMakeHisUrl() {
        Assert.assertEquals(expected,
                RequestService.makeHisURL(serverSN, startDate, endDate));
    }
}