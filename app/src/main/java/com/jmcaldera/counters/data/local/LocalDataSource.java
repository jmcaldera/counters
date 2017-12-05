package com.jmcaldera.counters.data.local;

import com.jmcaldera.counters.data.DataSource;

/**
 * Created by jmcaldera on 04/12/2017.
 */

public class LocalDataSource implements DataSource {


    @Override
    public void getCounters(LoadCountersCallback callback) {


    }

    @Override
    public void addCounter(String title, LoadCountersCallback callback) {

    }

    @Override
    public void incrementCounter(String id, LoadCountersCallback callback) {

    }

    @Override
    public void decrementCounter(String id, LoadCountersCallback callback) {

    }

    @Override
    public void deleteCounter(String id, LoadCountersCallback callback) {

    }
}
