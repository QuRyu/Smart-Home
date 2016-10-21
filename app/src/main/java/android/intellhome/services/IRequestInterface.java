package android.intellhome.services;

import android.intellhome.entity.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Quentin on 20/10/2016.
 */
public interface IRequestInterface {

    @GET("tools/gethisData/getHis")
    public Call<Result> getHisData(@Query("_hserverSN") String serverSN,
                                   @Query("begtime") String startDate, @Query("endtime") String endDate);

}
