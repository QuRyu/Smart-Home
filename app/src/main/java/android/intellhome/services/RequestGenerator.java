package android.intellhome.services;

import android.intellhome.entity.Result;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Quentin on 20/10/2016.
 */
public class RequestGenerator {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URLs.BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    public static <T> T generate(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
