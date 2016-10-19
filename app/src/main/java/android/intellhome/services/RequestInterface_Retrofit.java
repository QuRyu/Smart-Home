package android.intellhome.services;

import android.intellhome.entity.DeviceHistoryData;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Quentin on 19/10/2016.
 */
public interface RequestInterface_Retrofit {
    /*
    {
        "id": 1,
        "device_sn": "0001",
        "device_state": 1,
        "device_U": 220,//电压
        "device_I": 1,//电流
        "device_P": 0,//电功率
        "device_electricity": 0.5,//耗电量
        "updatetime": "2016-09-06 18:52:06"//更新时间
    }
     */

    String BASE_URL = "192.168.1.135:3000/";

    @GET("tools/gethisData/getHis?_hserverSN=0000000000000107&begtime=20161007104353&endtime=20161008004753")
    Call<DeviceHistoryData > getDeviceHistoryData(String startDate, String endDate);
}
