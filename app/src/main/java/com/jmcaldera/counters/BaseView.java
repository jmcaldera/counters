package com.jmcaldera.counters;

/**
 * Created by jmcaldera on 04/12/2017.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
