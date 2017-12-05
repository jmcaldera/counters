package com.jmcaldera.counters.counters;

import com.jmcaldera.counters.BasePresenter;
import com.jmcaldera.counters.BaseView;
import com.jmcaldera.counters.data.model.Counter;

import java.util.List;

/**
 * Created by jmcaldera on 05/12/2017.
 */

public interface CountersContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showCounters(List<Counter> counters);

        void showNoCounters();

        void showSuccessMessage();

        void showErrorMessage(String message);

        void showAddNewCounter();

        void showCountersSum(int sum);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadCounters(boolean force);

        void addNewCounter();

        void incrementCounter(String id);

        void decrementCounter(String id);

        void deleteCounter(String id);

        void getCountersSum();
    }
}
