package com.jmcaldera.counters.data.remote.api;

import com.jmcaldera.counters.data.model.Counter;
import com.jmcaldera.counters.data.remote.api.form.AddCounterForm;
import com.jmcaldera.counters.data.remote.api.form.OperateCounterForm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jmcaldera on 04/12/2017.
 */

public interface CounterApiClient {

    @GET("/api/v1/counters")
    Call<List<Counter>> getCounters();

    @POST("/api/v1/counter")
    Call<List<Counter>> addCounter(@Body AddCounterForm form);

    @POST("/api/v1/counter/inc")
    Call<List<Counter>> incrementCounter(@Body OperateCounterForm form);

    @POST("/api/v1/counter/dec")
    Call<List<Counter>> decrementCounter(@Body OperateCounterForm form);

    @DELETE("/api/v1/counter")
    Call<List<Counter>> deleteCounter(@Body OperateCounterForm form);

}
