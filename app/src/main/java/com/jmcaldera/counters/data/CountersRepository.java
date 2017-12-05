package com.jmcaldera.counters.data;

import android.support.annotation.NonNull;

import com.jmcaldera.counters.data.model.Counter;

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

    Map<String, Counter> mCachedCounters;

    boolean mCacheIsDirty = false;

    public CountersRepository(@NonNull DataSource remoteDataSource) {
        this.mRemoteDataSource = checkNotNull(remoteDataSource);
    }

    public static CountersRepository getInstance(DataSource remoteDataSource) {
        if (INSTANCE == null){
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
    public void addCounter(@NonNull String title, @NonNull final LoadCountersCallback callback) {
        checkNotNull(title, "Title cannot be null");
        checkNotNull(callback, "Callback cannot be null");
        mRemoteDataSource.addCounter(title, new LoadCountersCallback() {
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

    // TODO: chequear si es mejor pasar objeto Counter a pasar id, para refrescar la cache
    @Override
    public void incrementCounter(@NonNull String id, @NonNull final LoadCountersCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);
        mRemoteDataSource.incrementCounter(id, new LoadCountersCallback() {
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

    @Override
    public void decrementCounter(@NonNull String id, @NonNull final LoadCountersCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);
        mRemoteDataSource.decrementCounter(id, new LoadCountersCallback() {
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

    @Override
    public void deleteCounter(@NonNull String id, @NonNull final LoadCountersCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);
        mRemoteDataSource.deleteCounter(id, new LoadCountersCallback() {
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
}
