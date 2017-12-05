package com.jmcaldera.counters.addcounter;

import com.jmcaldera.counters.BasePresenter;
import com.jmcaldera.counters.BaseView;

/**
 * Created by jmcaldera on 05/12/2017.
 */

public interface AddCounterContract {

    interface View extends BaseView<Presenter> {

        void showAddCounterSuccess();

        void showAddCounterError(String message);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void addCounter(String title);
    }
}
