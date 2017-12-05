package com.jmcaldera.counters.data;

import android.support.annotation.NonNull;

import com.jmcaldera.counters.data.local.LocalDataSource;
import com.jmcaldera.counters.data.model.Counter;
import com.jmcaldera.counters.data.remote.RemoteDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by jmcaldera on 04/12/2017.
 */

public class CountersRepository implements DataSource {


    private static CountersRepository INSTANCE = null;

    private final DataSource mRemoteDataSource;

//    private final DataSource mLocalDataSource;

    Map<String, Counter> mCachedCounters;

    boolean mCacheIsDirty = false;

//    public CountersRepository(@NonNull DataSource remoteDataSource, @NonNull DataSource localDataSource) {
    public CountersRepository(@NonNull DataSource remoteDataSource) {
        this.mRemoteDataSource = checkNotNull(remoteDataSource);
//        this.mLocalDataSource = checkNotNull(localDataSource);
    }

//    public static CountersRepository getInstance(DataSource remoteDataSource, DataSource localDataSource) {
    public static CountersRepository getInstance(DataSource remoteDataSource) {
        if (INSTANCE == null){
//            INSTANCE = new CountersRepository(remoteDataSource, localDataSource);
            INSTANCE = new CountersRepository(remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getCounters(@NonNull LoadCountersCallback callback) {
        checkNotNull(callback);

        if (mCachedCounters != null) {
            callback.onSuccess(new ArrayList<Counter>(mCachedCounters.values()));
            return;
        }

        getCountersFromRemoteDataSource(callback);
    }

    private void getCountersFromRemoteDataSource(@NonNull final LoadCountersCallback callback) {
        checkNotNull(callback);
        mRemoteDataSource.getCounters(new LoadCountersCallback() {
            @Override
            public void onSuccess(List<Counter> counters) {
                refreshCache(counters);
                callback.onSuccess(new ArrayList<Counter>(mCachedCounters.values()));
            }

            @Override
            public void onError(Throwable error) {
                callback.onError(error);
            }
        });
    }

    private void refreshCache(List<Counter> counters) {
        if (mCachedCounters == null) {
            mCachedCounters = new LinkedHashMap<>();
        }
        mCachedCounters.clear();
        for (Counter counter : counters) {
            mCachedCounters.put(counter.getId(), counter);
        }
//        mCacheIsDirty = false;
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
