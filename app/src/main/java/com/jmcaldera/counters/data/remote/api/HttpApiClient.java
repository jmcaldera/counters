package com.jmcaldera.counters.data.remote.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jmcaldera on 04/12/2017.
 */

public class HttpApiClient {

    private String API_BASE_URL = "http://192.168.0.6:3000/";

    public CounterApiClient getApiClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(CounterApiClient.class);
    }

}
