package com.jmcaldera.counters.data;

import com.jmcaldera.counters.data.model.Counter;

import java.util.List;

/**
 * Created by jmcaldera on 04/12/2017.
 */

public interface DataSource {

    interface LoadCountersCallback {
        void onSuccess(List<Counter> counters);
        void onError(Throwable error);
    }

    void getCounters(LoadCountersCallback callback);

    void addCounter(String title, LoadCountersCallback callback);

    void incrementCounter(String id, LoadCountersCallback callback);

    void decrementCounter(String id, LoadCountersCallback callback);

    void deleteCounter(String id, LoadCountersCallback callback);

    void refreshCounters();

    int getCounterSum();
}
