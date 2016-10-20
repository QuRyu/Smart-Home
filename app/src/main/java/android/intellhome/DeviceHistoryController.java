package android.intellhome;

import android.intellhome.entity.DeviceHistoryData;

import java.util.List;

/**
 * Created by Quentin on 20/10/2016.
 */
public class DeviceHistoryController {

    private static List<DeviceHistoryData> dataList;

    private int numOfData;

    public List<DeviceHistoryData> requestData(String startDate, String endDate) {
        return null;
    }

    public static void dataReceiver(DeviceHistoryData data) {
        dataList.add(data);
    }
}
