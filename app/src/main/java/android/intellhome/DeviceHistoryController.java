package android.intellhome;

import android.intellhome.entity.DeviceHistoryData;
import android.intellhome.utils.DateUtil;

import java.util.List;

/**
 * Created by Quentin on 20/10/2016.
 */
public class DeviceHistoryController {

    public List<DeviceHistoryData> requestData(String startDate, String endDate) {
        // get difference of days
        // check if the date is valid (endDate must be equal to or less than current date
        // if it's smaller than a month, draw one-month diagram
        // else if it's no longer than a year, draw twelve months
        // if it has been several years, draw yearly diagram
        
        int numOfDays = DateUtil.calculateDateDiff(startDate, endDate);
        return null;
    }


}
