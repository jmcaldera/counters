package com.jmcaldera.counters.counters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.jmcaldera.counters.data.CountersRepository;
import com.jmcaldera.counters.data.DataSource;
import com.jmcaldera.counters.data.model.Counter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jmcaldera on 05/12/2017.
 */

public class CountersPresenter implements CountersContract.Presenter {

    private static final String TAG = CountersPresenter.class.getSimpleName();

    private CountersContract.View mView;

    private CountersRepository mRepository;

    public CountersPresenter(@NonNull CountersContract.View mView, @NonNull CountersRepository mRepository) {
        this.mView = checkNotNull(mView, "View cannot be null");
        this.mRepository = checkNotNull(mRepository, "Repository cannot be null");
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCounters(true);
    }

    @Override
    public void loadCounters(boolean force) {
        if (force) {
            mRepository.refreshCounters();
        }

        mRepository.getCounters(new DataSource.LoadCountersCallback() {
            @Override
            public void onSuccess(List<Counter> counters) {
                Log.d(TAG, "onSuccess, counters: " + counters.toString());
                if (!mView.isActive()) {
                    return;
                }
                processCounters(counters);
            }

            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "onError, error: " + error.getMessage());
                if (!mView.isActive()) {
                    return;
                }
                mView.showErrorMessage(error.getMessage());
            }
        });
    }

    private void processCounters(List<Counter> counters) {
        if (counters.isEmpty()) {
            // Process empty counters
            mView.showNoCounters();
        } else {
            mView.showCounters(counters);
        }
    }

    @Override
    public void addNewCounter() {
        mView.showAddNewCounter();
    }

    @Override
    public void incrementCounter(@NonNull String id) {
        checkNotNull(id);
        mRepository.incrementCounter(id, new DataSource.LoadCountersCallback() {
            @Override
            public void onSuccess(List<Counter> counters) {
                if (!mView.isActive()) {
                    return;
                }
                processCounters(counters);
            }

            @Override
            public void onError(Throwable error) {
                if (!mView.isActive()) {
                    return;
                }
                mView.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void decrementCounter(@NonNull String id) {
        checkNotNull(id);
        mRepository.decrementCounter(id, new DataSource.LoadCountersCallback() {
            @Override
            public void onSuccess(List<Counter> counters) {
                if (!mView.isActive()) {
                    return;
                }
                processCounters(counters);
            }

            @Override
            public void onError(Throwable error) {
                if (!mView.isActive()) {
                    return;
                }
                mView.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void deleteCounter(@NonNull String id) {
        checkNotNull(id);
        mRepository.deleteCounter(id, new DataSource.LoadCountersCallback() {
            @Override
            public void onSuccess(List<Counter> counters) {
                if (!mView.isActive()) {
                    return;
                }
//                processCounters(counters);
            }

            @Override
            public void onError(Throwable error) {
                if (!mView.isActive()) {
                    return;
                }
                mView.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void getCountersSum() {
        int sum = mRepository.getCounterSum();
        mView.showCountersSum(sum);
    }
}
