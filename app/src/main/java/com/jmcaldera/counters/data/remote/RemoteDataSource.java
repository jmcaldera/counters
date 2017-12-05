package com.jmcaldera.counters.data.remote;

import android.support.annotation.NonNull;

import com.jmcaldera.counters.data.DataSource;
import com.jmcaldera.counters.data.model.Counter;
import com.jmcaldera.counters.data.remote.api.CounterApiClient;
import com.jmcaldera.counters.data.remote.api.HttpApiClient;
import com.jmcaldera.counters.data.remote.api.form.AddCounterForm;
import com.jmcaldera.counters.data.remote.api.form.OperateCounterForm;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jmcaldera on 04/12/2017.
 */

public class RemoteDataSource implements DataSource {

    private static RemoteDataSource INSTANCE;

    private RemoteDataSource () {}

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getCounters(final @NonNull LoadCountersCallback callback) {
        HttpApiClient httpClient = new HttpApiClient();
        CounterApiClient apiClient = httpClient.getApiClient();

        Call<List<Counter>> call = apiClient.getCounters();

        call.enqueue(new Callback<List<Counter>>() {
            @Override
            public void onResponse(Call<List<Counter>> call, Response<List<Counter>> response) {
                if (!response.isSuccessful()) {
                    // Process error
                    try {
                        callback.onError(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
//                        e.printStackTrace();
                        callback.onError(e);
                    }
                    return;
                }
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Counter>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void addCounter(@NonNull String title, final @NonNull LoadCountersCallback callback) {

        HttpApiClient httpClient = new HttpApiClient();
        CounterApiClient apiClient = httpClient.getApiClient();

        Call<List<Counter>> call = apiClient.addCounter(AddCounterForm.create(title));

        call.enqueue(new Callback<List<Counter>>() {
            @Override
            public void onResponse(Call<List<Counter>> call, Response<List<Counter>> response) {
                if (!response.isSuccessful()) {
                    try {
                        callback.onError(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
//                        e.printStackTrace();
                        callback.onError(e);
                    }
                    return;
                }
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Counter>> call, Throwable t) {
                callback.onError(t);
            }
        });

    }

    @Override
    public void incrementCounter(@NonNull String id, final @NonNull LoadCountersCallback callback) {
        HttpApiClient httpClient = new HttpApiClient();
        CounterApiClient apiClient = httpClient.getApiClient();

        Call<List<Counter>> call = apiClient.incrementCounter(OperateCounterForm.create(id));
        call.enqueue(new Callback<List<Counter>>() {
            @Override
            public void onResponse(Call<List<Counter>> call, Response<List<Counter>> response) {
                if (!response.isSuccessful()) {
                    try {
                        callback.onError(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
//                        e.printStackTrace();
                        callback.onError(e);
                    }
                    return;
                }
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Counter>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void decrementCounter(@NonNull String id, final @NonNull LoadCountersCallback callback) {
        HttpApiClient httpClient = new HttpApiClient();
        CounterApiClient apiClient = httpClient.getApiClient();

        Call<List<Counter>> call = apiClient.decrementCounter(OperateCounterForm.create(id));
        call.enqueue(new Callback<List<Counter>>() {
            @Override
            public void onResponse(Call<List<Counter>> call, Response<List<Counter>> response) {
                if (!response.isSuccessful()) {
                    try {
                        callback.onError(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
//                        e.printStackTrace();
                        callback.onError(e);
                    }
                    return;
                }
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Counter>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void deleteCounter(@NonNull String id, @NonNull final LoadCountersCallback callback) {
        HttpApiClient httpClient = new HttpApiClient();
        CounterApiClient apiClient = httpClient.getApiClient();

        Call<List<Counter>> call = apiClient.deleteCounter(OperateCounterForm.create(id));
        call.enqueue(new Callback<List<Counter>>() {
            @Override
            public void onResponse(Call<List<Counter>> call, Response<List<Counter>> response) {
                if (!response.isSuccessful()) {
                    try {
                        callback.onError(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
//                        e.printStackTrace();
                        callback.onError(e);
                    }
                    return;
                }
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Counter>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void refreshCounters() {
        /**
         * Not used. {@link com.jmcaldera.counters.data.CountersRepository} handles this logic
         */
    }

    /**
     * Should not be called. Instead, use the cached counters.
     * @return 0
     */
    @Override
    public int getCounterSum() {
        return 0;
    }
}
