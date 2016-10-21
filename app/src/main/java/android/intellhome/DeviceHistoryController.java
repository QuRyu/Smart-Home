package android.intellhome;

import android.intellhome.entity.DeviceHistoryData;
import android.intellhome.utils.DateUtil;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Quentin on 20/10/2016.
 */
public class DeviceHistoryController {

    public DataWithnDays requestData(String startDate, String endDate) throws IllegalArgumentException {
        // get difference of days
        // check if the date is valid (endDate must be equal to or less than current date
        // if it's smaller than a month, draw one-month diagram
        // else if it's no longer than a year, draw twelve months
        // if it has been several years, draw yearly diagram
        if (!checkDate(startDate, endDate))
            throw new IllegalArgumentException("end date is larger than current time");

        int numOfDays = DateUtil.calculateDateDiff(startDate, endDate);

        return null;
    }

    // TODO: 21/10/2016 add criteria for endDate
    private boolean checkDate(String startDate, String endDate) {
        long endD = DateUtil.stringdate2Unixtime(endDate);
        return !(endD > System.currentTimeMillis());
    }

    public static final class DataWithnDays {

        private int nOfDays;
        private List<DeviceHistoryData> historyData;

        DataWithnDays(int nOfDays, List<DeviceHistoryData> historyData) {
            this.nOfDays = nOfDays;
            this.historyData = historyData;
        }

        public int getnOfDays() {
            return nOfDays;
        }

        public List<DeviceHistoryData> getHistoryData() {
            return historyData;
        }
    }
}
