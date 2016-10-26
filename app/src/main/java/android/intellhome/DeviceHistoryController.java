package android.intellhome;

import android.intellhome.entity.DeviceHistoryData;
import android.intellhome.entity.Result;
import android.intellhome.services.RequestService;
import android.intellhome.utils.DateUtil;
import android.intellhome.utils.RegExp;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Quentin on 20/10/2016.
 */
public class DeviceHistoryController implements Callback<Result> {

    static final String TAG = "DeviceHistoryController";

    public static final int METRIC_DAY = 1;
    public static final int METRIC_MONTH = 2;
    public static final int METRIC_YEAR = 3;

    public static final int REQUEST_SUCCESS = 1;
    public static final int REQUEST_FAILURE = 2;

    static String serverSN = "0000000000000107";

    private Handler handler;

    static final String MOCK_START_DATE = "20161007104353";
    static final String MOCK_END_DATE= "20161008004753";

    public DeviceHistoryController(Handler handler) {
        this.handler = handler;
    }

    public void requestData(String startDate, String endDate) throws IOException {
//        if (checkDate(startDate, endDate))
//              ask user to change date

        RequestService.getHisDataList(serverSN, MOCK_START_DATE, MOCK_END_DATE, this);
    }

    // TODO: 21/10/2016 add criteria for endDate
    private boolean checkDate(String startDate, String endDate) {
        long endD = DateUtil.stringdate2Unixtime(endDate);
        return !(endD > System.currentTimeMillis());
    }

    private static int computeMonth(int nOfDays) {
        int n = nOfDays / 30;
        if (nOfDays % 30 > 0)
            ++n;
        return n;
    }

    private static int computeYear(int nOfDays) {
        int n = nOfDays / 365;
        if (nOfDays % 365 > 0)
            ++n;
        return n;
    }

    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {

        Message message = handler.obtainMessage();
        List<DeviceHistoryData> data = response.body().result;

//                int numOfDays = DateUtil.calculateDateDiff("2016 10 07", "2016 10 08");
        int numOfDays = 1;
        Bundle bundle = new Bundle();

        if (numOfDays <= 30) {
            bundle.putInt(DeviceActivity.METRIC, METRIC_DAY);
            bundle.putInt(DeviceActivity.DAYS_OF_DIFFERENCE, numOfDays);
//            bundle.putInt(DeviceActivity.START, RegExp.getDay(MOCK_START_DATE));
            bundle.putInt(DeviceActivity.START, 7);

        }
        else if (numOfDays <= 365) {
            bundle.putInt(DeviceActivity.METRIC, METRIC_MONTH);
            bundle.putInt(DeviceActivity.DAYS_OF_DIFFERENCE, computeMonth(numOfDays));
            bundle.putInt(DeviceActivity.START, RegExp.getMonth(MOCK_START_DATE));
        }

        else{
            bundle.putInt(DeviceActivity.METRIC, METRIC_YEAR);
            bundle.putInt(DeviceActivity.DAYS_OF_DIFFERENCE, computeYear(numOfDays));
            bundle.putInt(DeviceActivity.START, RegExp.getYear(MOCK_START_DATE));
        }

        message.setData(bundle);
        message.obj = data;
        message.what = REQUEST_SUCCESS;
        handler.sendMessage(message);

        Log.i(TAG, "data has been sent to handler");
    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
        Log.d(TAG, "failed to retrieve data", t);
        Message message = handler.obtainMessage();
        message.what = REQUEST_FAILURE;
        handler.sendMessage(message);
    }
}
