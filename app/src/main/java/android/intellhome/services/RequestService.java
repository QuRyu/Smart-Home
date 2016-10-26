package android.intellhome.services;

import android.intellhome.entity.DeviceHistoryData;
import android.intellhome.entity.Result;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Quentin on 19/10/2016.
 */
public class RequestService {

    public static final String TAG = "RequestService";

    static IRequestInterface requestService = RequestGenerator.generate(IRequestInterface.class);

    public static void getHisDataList(String serverSN, String startDate, String endDate, Callback<Result> callback) throws IOException {
        Log.i(TAG, "start to request a list of history data");
        Call<Result> call = requestService.getHisData(serverSN, startDate, endDate);
        call.enqueue(callback);
    }

}
