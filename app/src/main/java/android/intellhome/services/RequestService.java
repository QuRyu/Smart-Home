package android.intellhome.services;

import android.intellhome.entity.DeviceHistoryData;
import android.intellhome.entity.Result;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Quentin on 19/10/2016.
 */
public class RequestService {


    public static final String TAG = "RequestService";

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URLs.BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    static RequestInterface requestService = retrofit.create(RequestInterface.class);

    public static DeviceHistoryData getHisData(String serverSN, String startDate, String endDate) throws IOException {
//        Log.i(TAG, "start history data request");

        Call<Result> call = requestService.getHisData(serverSN, startDate, endDate);

        Response<Result> response = call.execute();
        List<DeviceHistoryData> data = response.body().result;
        return data.size() == 0 ? null : data.get(0);
    }

    public static String getData(String serverSN, String startDate, String endDate) {
        Call<Result> call = requestService.getHisData(serverSN, startDate, endDate);
        return call.request().url().toString();
    }
}
