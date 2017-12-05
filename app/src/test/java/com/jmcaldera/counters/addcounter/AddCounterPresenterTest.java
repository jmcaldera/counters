package com.jmcaldera.counters.addcounter;

import com.google.common.collect.Lists;
import com.jmcaldera.counters.data.CountersRepository;
import com.jmcaldera.counters.data.DataSource;
import com.jmcaldera.counters.data.model.Counter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jmcaldera on 05/12/2017.
 */

public class AddCounterPresenterTest {

    private List<Counter> COUNTERS;

    private static final String COUNTER_TITLE1 = "bob";
    private static final String COUNTER_TITLE2 = "steve";

    @Mock
    private AddCounterContract.View mView;

    @Mock
    private CountersRepository mRepository;

    @Captor
    private ArgumentCaptor<DataSource.LoadCountersCallback> mCallbackCaptor;

    private AddCounterPresenter mPresenter;

    @Before
    public void setupAddCounterPresenter() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new AddCounterPresenter(mView, mRepository);

        // espera que la vista este activa
        when(mView.isActive()).thenReturn(true);

        COUNTERS = Lists.newArrayList(new Counter(0, "asdf", COUNTER_TITLE1),
                new Counter(0, "qwer", COUNTER_TITLE2));
    }

    @Test
    public void createPresenter_setsPresenterToView() {
        mPresenter = new AddCounterPresenter(mView, mRepository);

        verify(mView).setPresenter(mPresenter);
    }

    @Test
    public void addCounterToRepository_ShowSuccessMessage() {
        String title = "max";
        mPresenter.addCounter(title);
        verify(mRepository).addCounter(eq(title), mCallbackCaptor.capture());
        mCallbackCaptor.getValue().onSuccess(COUNTERS);
        verify(mView).showAddCounterSuccess();
    }
}
