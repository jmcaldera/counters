package com.jmcaldera.counters.addcounter;

import android.support.annotation.NonNull;

import com.jmcaldera.counters.data.CountersRepository;
import com.jmcaldera.counters.data.DataSource;
import com.jmcaldera.counters.data.model.Counter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jmcaldera on 05/12/2017.
 */

public class AddCounterPresenter implements AddCounterContract.Presenter {

    private AddCounterContract.View mView;

    private CountersRepository mRepository;

    public AddCounterPresenter(@NonNull AddCounterContract.View mView, @NonNull CountersRepository mRepository) {
        this.mView = checkNotNull(mView);
        this.mRepository = checkNotNull(mRepository);
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void addCounter(@NonNull String title) {
        checkNotNull(title, "Title cannot be null");
        mRepository.addCounter(title, new DataSource.LoadCountersCallback() {
            @Override
            public void onSuccess(List<Counter> counters) {
                if (!mView.isActive()) {
                    return;
                }
                mView.showAddCounterSuccess();
            }

            @Override
            public void onError(Throwable error) {
                if (!mView.isActive()) {
                    mView.showAddCounterError(error.getMessage());
                }
            }
        });
    }
}
